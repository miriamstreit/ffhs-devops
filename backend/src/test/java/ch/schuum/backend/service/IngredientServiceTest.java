package ch.schuum.backend.service;

import ch.schuum.backend.dto.IngredientDto;
import ch.schuum.backend.model.Ingredient;
import ch.schuum.backend.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.springframework.context.annotation.Import;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import org.springframework.test.context.junit4.SpringRunner;

import org.junit.runner.RunWith;

@TestConfiguration
class MeterConfig {
  @Bean
  public MeterRegistry registry() {
    return new SimpleMeterRegistry();
  }
}

@RunWith(SpringRunner.class)
@Import(MeterConfig.class)
@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    IngredientRepository ingredientRepository;

    @Mock
    MeterRegistry meterRegistry;

    @InjectMocks
    IngredientService ingredientService;
    Ingredient ingredient1;
    Ingredient ingredient2;

    IngredientDto ingredientDto1;
    IngredientDto ingredientDto2;

    List<Ingredient> ingredients = new ArrayList<>();
    List<IngredientDto> ingredientDtos = new ArrayList<>();

    AtomicInteger ingredientsBelowThresholdGauge;

    @BeforeEach
    void setUp() {
        ingredient1 = new Ingredient(1, "Weizen", 34.0F, 50.0F, false);
        ingredient2 = new Ingredient(2, "Hopfen", 356.0F, 50.0F, false);
        ingredientDto1 = new IngredientDto(1, "Weizen", 34.0F, 50.0F, false);
        ingredientDto2 = new IngredientDto(2, "Hopfen", 356.0F, 50.0F, false);
        Collections.addAll(ingredients, ingredient1, ingredient2);
        Collections.addAll(ingredientDtos, ingredientDto1, ingredientDto2);

        meterRegistry = new SimpleMeterRegistry();
        Metrics.addRegistry(meterRegistry);

        this.ingredientsBelowThresholdGauge = meterRegistry.gauge("ingredients_below_threshold_per_minute", new AtomicInteger(0));
    }

    @Test
    void test_getAllIngredients_whenIngredientsExist_returnIngredientsList() {
        // Arrange
        when(ingredientRepository.findAllByDeleted(false)).thenReturn(ingredients);

        // Act
        List<IngredientDto> ingredientsFromDb = ingredientService.getAllIngredients();

        // Assert
        assertEquals(ingredients.size(), ingredientsFromDb.size());
    }

    @Test
    void test_getAllIngredients_whenNoIngredientsExist_returnEmptyList() {
        // Arrange
        when(ingredientRepository.findAllByDeleted(false)).thenReturn(Collections.emptyList());

        // Act
        List<IngredientDto> ingredientsFromDb = ingredientService.getAllIngredients();

        // Assert
        assertEquals(0, ingredientsFromDb.size());
    }

    @Test
    void test_getAllIngredientsBelowThreshold() {
        // Arrange
        when(ingredientRepository.findIngredientsBelowThreshold()).thenReturn(Collections.singletonList(ingredient2));

        // Act
        List<IngredientDto> ingredientsFromDb = ingredientService.getAllIngredientsBelowThreshold();

        // Assert
        assertEquals(1, ingredientsFromDb.size());
        assertEquals(ingredient2.getIngredientId(), ingredientsFromDb.get(0).getIngredientId());
        assertEquals(ingredient2.getStock(), ingredientsFromDb.get(0).getStock());
        assertEquals(ingredient2.getThreshold(), ingredientsFromDb.get(0).getThreshold());
    }

    @Test
    void test_getIngredientById_whenIngredientExists_returnIngredient() {
        // Arrange
        when(ingredientRepository.findById(ingredient1.getIngredientId())).thenReturn(Optional.ofNullable(ingredient1));

        // Act
        Optional<IngredientDto> ingredientFromDb = ingredientService.getIngredientById(ingredient1.getIngredientId());

        // Assert
        assertTrue(ingredientFromDb.isPresent());
        assertEquals(ingredient1.getIngredientId(), ingredientFromDb.get().getIngredientId());
    }

    @Test
    void test_getIngredientById_whenIngredientDoesNotExist_returnNull() {
        // Arrange
        when(ingredientRepository.findById(ingredient1.getIngredientId())).thenReturn(Optional.empty());

        // Act
        Optional<IngredientDto> ingredientFromDb = ingredientService.getIngredientById(ingredient1.getIngredientId());

        // Assert
        assertFalse(ingredientFromDb.isPresent());
    }

    @Test
    void test_createIngredient_createNewBeerTypeAndReturn() {
        // Arrange
        when(ingredientRepository.save(any())).thenReturn(ingredient1);

        // Act
        IngredientDto newIngredient = ingredientService.createIngredient(ingredientDto1);

        // Assert
        assertEquals(ingredientDto1.getIngredientId(), newIngredient.getIngredientId());
        assertEquals(ingredientDto1.getName(), newIngredient.getName());
        assertEquals(ingredientDto1.getThreshold(), newIngredient.getThreshold());
        assertEquals(ingredientDto1.getStock(), newIngredient.getStock());
        verify(ingredientRepository, times(1)).save(any());
    }

    @Test
    void test_updateIngredient_whenIngredientExists_updateAndReturn() {
        // Arrange
        when(ingredientRepository.findById(anyInt())).thenReturn(Optional.ofNullable(ingredient2));
        ingredient2.setThreshold(20);
        when(ingredientRepository.save(any())).thenReturn(ingredient2);
        ingredientDto2.setThreshold(20);

        // Act
        IngredientDto beerTypeDto = ingredientService.updateIngredient(ingredient2.getIngredientId(), ingredientDto2);

        // Assert
        assertEquals(20, beerTypeDto.getThreshold());
        verify(ingredientRepository, times(1)).save(any());
    }

    @Test
    void test_deleteIngredient_ifExists_deleteIngredient() {
        // Arrange
        when(ingredientRepository.findById(anyInt())).thenReturn(Optional.of(ingredient1));

        // Act
        int deletedId = ingredientService.deleteIngredient(ingredient1.getIngredientId());

        // Assert
        assertEquals(ingredient1.getIngredientId(), deletedId);
        verify(ingredientRepository, times(1)).save(any());
        verify(ingredientRepository, times(0)).delete(any());
    }

    @Test
    void test_deleteIngredient_ifNotExists_return() {
        // Arrange
        when(ingredientRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        int deletedId = ingredientService.deleteIngredient(ingredient1.getIngredientId());

        // Assert
        assertEquals(0, deletedId);
        verify(ingredientRepository, times(0)).save(any());
        verify(ingredientRepository, times(0)).delete(any());
    }
}