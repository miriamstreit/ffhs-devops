package ch.schuum.backend.service;

import ch.schuum.backend.dto.BeerTypeDto;
import ch.schuum.backend.dto.IngredientAmountDto;
import ch.schuum.backend.dto.IngredientDto;
import ch.schuum.backend.dto.TankDto;
import ch.schuum.backend.model.BeerType;
import ch.schuum.backend.model.BrewingProcess;
import ch.schuum.backend.model.Ingredient;
import ch.schuum.backend.model.IngredientAmount;
import ch.schuum.backend.repository.BeerTypeRepository;
import ch.schuum.backend.repository.BrewingProcessRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeerTypeServiceTest {

    @Mock
    BeerTypeRepository beerTypeRepository;

    @InjectMocks
    BeerTypeService beerTypeService;

    Ingredient ingredient1;
    Ingredient ingredient2;

    IngredientDto ingredientDto1;
    IngredientDto ingredientDto2;

    IngredientAmount ingredientAmount1;
    IngredientAmount ingredientAmount2;

    IngredientAmountDto ingredientAmountDto1;
    IngredientAmountDto ingredientAmountDto2;
    List<IngredientAmount> ingredientAmountsType2;

    List<IngredientAmount> ingredientAmountsType3;

    List<IngredientAmountDto> ingredientAmountsTypeDto2;

    List<IngredientAmountDto> ingredientAmountsTypeDto3;

    BeerType beerType1;
    BeerType beerType2;
    BeerType beerType3;

    BeerTypeDto beerTypeDto1;
    BeerTypeDto beerTypeDto2;
    BeerTypeDto beerTypeDto3;

    List<BeerType> beerTypes;
    List<BeerTypeDto> beerTypeDtos;

    @BeforeEach
    void setUp() {
        ingredient1 = new Ingredient(1, "Weizen", 34.0F, 50.0F, false);
        ingredient2 = new Ingredient(2, "Hopfen", 356.0F, 50.0F, false);
        ingredientDto1 = new IngredientDto(1, "Weizen", 34.0F, 50.0F, false);
        ingredientDto2 = new IngredientDto(2, "Hopfen", 356.0F, 50.0F, false);

        ingredientAmount1 = new IngredientAmount(ingredient1, 45F);
        ingredientAmount2 = new IngredientAmount(ingredient2, 32F);
        ingredientAmountDto1 = new IngredientAmountDto(ingredientDto1, 45F);
        ingredientAmountDto2 = new IngredientAmountDto(ingredientDto2, 32F);
        ingredientAmountsType2 = new ArrayList<>();
        Collections.addAll(ingredientAmountsType2, ingredientAmount1);
        ingredientAmountsTypeDto2 = new ArrayList<>();
        Collections.addAll(ingredientAmountsTypeDto2, ingredientAmountDto1);
        ingredientAmountsType3 = new ArrayList<>();
        Collections.addAll(ingredientAmountsType3, ingredientAmount2);
        ingredientAmountsTypeDto3 = new ArrayList<>();
        Collections.addAll(ingredientAmountsTypeDto3, ingredientAmountDto2);

        beerType1 = new BeerType(1, "Lager", 30, Collections.emptyList(), false);
        beerType2 = new BeerType(2, "IPA", 30, ingredientAmountsType2, false);
        beerType3 = new BeerType(3, "Red Ale", 30, ingredientAmountsType3, false);

        beerTypeDto1 = new BeerTypeDto(1, "Lager", 30, Collections.emptyList(), false);
        beerTypeDto2 = new BeerTypeDto(2, "IPA", 30, ingredientAmountsTypeDto2, false);
        beerTypeDto3 = new BeerTypeDto(3, "Red Ale", 30, ingredientAmountsTypeDto3, false);

        beerTypes = new ArrayList<>();
        Collections.addAll(beerTypes, beerType1, beerType2, beerType3);

        beerTypeDtos = new ArrayList<>();
        Collections.addAll(beerTypeDtos, beerTypeDto1, beerTypeDto2, beerTypeDto3);
    }

    @Test
    void test_getAllBeerTypes_whenBeerTypesExist_returnBeerTypesList() {
        // Arrange
        when(beerTypeRepository.findAllByDeleted(false)).thenReturn(beerTypes);

        // Act
        List<BeerTypeDto> beerTypesFromDb = beerTypeService.getAllBeerTypes();

        // Assert
        assertEquals(beerTypes.size(), beerTypesFromDb.size());
    }

    @Test
    void test_getAllBeerTypes_whenNoBeerTypesExist_returnEmptyList() {
        // Arrange
        when(beerTypeRepository.findAllByDeleted(false)).thenReturn(Collections.emptyList());

        // Act
        List<BeerTypeDto> beerTypesFromDb = beerTypeService.getAllBeerTypes();

        // Assert
        assertEquals(0, beerTypesFromDb.size());
    }

    @Test
    void test_getBeerTypeById_whenBeerTypeExists_returnBeerType() {
        // Arrange
        when(beerTypeRepository.findById(beerType1.getBeerTypeId())).thenReturn(Optional.ofNullable(beerType1));

        // Act
        Optional<BeerTypeDto> beerTypeFromDb = beerTypeService.getBeerTypeById(beerType1.getBeerTypeId());

        // Assert
        assertTrue(beerTypeFromDb.isPresent());
        assertEquals(beerType1.getBeerTypeId(), beerTypeFromDb.get().getBeerTypeId());
    }

    @Test
    void test_getBeerTypeById_whenBeerTypeDoesNotExist_returnNull() {
        // Arrange
        when(beerTypeRepository.findById(beerType1.getBeerTypeId())).thenReturn(Optional.empty());

        // Act
        Optional<BeerTypeDto> beerTypeFromDb = beerTypeService.getBeerTypeById(beerType1.getBeerTypeId());

        // Assert
        assertFalse(beerTypeFromDb.isPresent());
    }

    @Test
    void test_createBeerType_createNewBeerTyüeAndReturn() {
        // Arrange
        when(beerTypeRepository.save(any())).thenReturn(beerType1);

        // Act
        BeerTypeDto newBeerType = beerTypeService.createBeerType(beerTypeDto1);

        // Assert
        assertEquals(beerTypeDto1.getBeerTypeId(), newBeerType.getBeerTypeId());
        assertEquals(beerTypeDto1.getName(), newBeerType.getName());
        assertEquals(beerTypeDto1.getDuration(), newBeerType.getDuration());
        assertEquals(beerTypeDto1.getIngredients().size(), newBeerType.getIngredients().size());
        verify(beerTypeRepository, times(1)).save(any());
    }

    @Test
    void test_updateBeerType_whenBeerTypeExists_updateAndReturn() {
        // Arrange
        when(beerTypeRepository.findById(anyInt())).thenReturn(Optional.ofNullable(beerType2));
        beerType2.setDuration(15);
        when(beerTypeRepository.save(any())).thenReturn(beerType2);
        beerTypeDto2.setDuration(15);

        // Act
        BeerTypeDto beerTypeDto = beerTypeService.updateBeerType(beerType2.getBeerTypeId(), beerTypeDto2);

        // Assert
        assertEquals(15, beerTypeDto.getDuration());
        verify(beerTypeRepository, times(1)).save(any());
    }

    @Test
    void test_deleteBeerType_ifExists_deleteBeerType() {
        // Arrange
        when(beerTypeRepository.findById(anyInt())).thenReturn(Optional.of(beerType1));

        // Act
        int deletedId = beerTypeService.deleteBeerType(beerType1.getBeerTypeId());

        // Assert
        assertEquals(beerType1.getBeerTypeId(), deletedId);
        verify(beerTypeRepository, times(1)).save(any());
        verify(beerTypeRepository, times(0)).delete(any());
    }

    @Test
    void test_deleteBeerType_ifNotExists_return() {
        // Arrange
        when(beerTypeRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        int deletedId = beerTypeService.deleteBeerType(beerType1.getBeerTypeId());

        // Assert
        assertEquals(0, deletedId);
        verify(beerTypeRepository, times(0)).save(any());
        verify(beerTypeRepository, times(0)).delete(any());
    }
}