package ch.schuum.backend.service;

import ch.schuum.backend.dto.BeerTypeDto;
import ch.schuum.backend.dto.BrewableDto;
import ch.schuum.backend.dto.IngredientAmountDto;
import ch.schuum.backend.dto.IngredientDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CanBrewServiceTest {

    @Mock
    BeerTypeService beerTypeService;

    @InjectMocks
    CanBrewService canBrewService;

    IngredientDto ingredient1;
    IngredientDto ingredient2;
    IngredientDto ingredient3;

    IngredientAmountDto ingredientAmount1;
    IngredientAmountDto ingredientAmount2;
    IngredientAmountDto ingredientAmount3;

    List<IngredientAmountDto> type1Ingredients;
    List<IngredientAmountDto> type2Ingredients;

    BeerTypeDto beerType1;
    BeerTypeDto beerType2;
    BeerTypeDto beerType3;
    List<BeerTypeDto> beerTypes;

    @BeforeEach
    void setUp() {
        ingredient1 = new IngredientDto(1, "Weizen", 10F, 50.0F, false);
        ingredient2 = new IngredientDto(2, "Hopfen", 4F, 50.0F, false);
        ingredient3 = new IngredientDto(3, "Malz", 7F, 50.0F, false);

        ingredientAmount1 = new IngredientAmountDto(ingredient1, 5F);
        ingredientAmount2 = new IngredientAmountDto(ingredient2, 5F);
        ingredientAmount3 = new IngredientAmountDto(ingredient3, 3F);

        type1Ingredients = new ArrayList<>();
        Collections.addAll(type1Ingredients, ingredientAmount1, ingredientAmount2, ingredientAmount3);
        type2Ingredients = new ArrayList<>();
        Collections.addAll(type1Ingredients, ingredientAmount1, ingredientAmount3);

        beerType1 = new BeerTypeDto(1, "Lager", 30, type1Ingredients, false);
        beerType2 = new BeerTypeDto(2, "IPA", 30, type2Ingredients, false);
        beerType3 = new BeerTypeDto(3, "Red Ale", 30, Collections.emptyList(), false);
        beerTypes = new ArrayList<>();
    }

    @Test
    void test_getBrewableBeerTypes_whenOneIngredientMissing_returnFalseAndMissingIngredient() {
        // Arrange
        Collections.addAll(beerTypes, beerType1);
        when(beerTypeService.getAllBeerTypes()).thenReturn(beerTypes);

        // Act
        List<BrewableDto> brewables = canBrewService.getBrewableBeerTypes();
        BrewableDto brewable = brewables.get(0);

        // Assert
        assertFalse(brewable.isBrewable());
        assertEquals(beerType1.getName(), brewable.getName());
        assertEquals(1, brewable.getMissingIngredients().size());
        assertEquals(1F, brewable.getMissingIngredients().get(0).getAmount());
        assertEquals(ingredient2.getName(), brewable.getMissingIngredients().get(0).getIngredient().getName());
    }

    @Test
    void test_getBrewableBeerTypes_whenNoIngredientsMissing_returnTrueAndEmptyIngredientsList() {
        // Arrange
        Collections.addAll(beerTypes, beerType2);
        when(beerTypeService.getAllBeerTypes()).thenReturn(beerTypes);

        // Act
        List<BrewableDto> brewables = canBrewService.getBrewableBeerTypes();
        BrewableDto brewable = brewables.get(0);

        // Assert
        assertTrue(brewable.isBrewable());
        assertEquals(beerType2.getName(), brewable.getName());
        assertEquals(0, brewable.getMissingIngredients().size());
    }

    @Test
    void test_getBrewableBeerTypes_whenNoIngredients_returnTrueAndEmptyIngredientsList() {
        // Arrange
        Collections.addAll(beerTypes, beerType3);
        when(beerTypeService.getAllBeerTypes()).thenReturn(beerTypes);

        // Act
        List<BrewableDto> brewables = canBrewService.getBrewableBeerTypes();
        BrewableDto brewable = brewables.get(0);

        // Assert
        assertTrue(brewable.isBrewable());
        assertEquals(beerType3.getName(), brewable.getName());
        assertEquals(0, brewable.getMissingIngredients().size());
    }
}