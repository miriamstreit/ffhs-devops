package ch.schuum.backend.mapping;

import ch.schuum.backend.dto.IngredientAmountDto;
import ch.schuum.backend.dto.IngredientDto;
import ch.schuum.backend.model.Ingredient;
import ch.schuum.backend.model.IngredientAmount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IngredientAmountMapperTest {

    Ingredient ingredient;
    Ingredient ingredient2;
    IngredientDto ingredientDto;
    IngredientDto ingredientDto2;

    IngredientAmount ingredientAmount;
    IngredientAmount ingredientAmount2;
    IngredientAmountDto ingredientAmountDto;
    IngredientAmountDto ingredientAmountDto2;

    @BeforeEach
    void setUp() {
        ingredient = new Ingredient(1, "Hopfen", 356, 50, false);
        ingredientDto = new IngredientDto(1, "Hopfen", 356, 50, false);

        ingredient2 = new Ingredient(2, "Malz", 32, 50, false);
        ingredientDto2 = new IngredientDto(2, "Malz", 32, 50, false);

        ingredientAmount = new IngredientAmount(ingredient, 32);
        ingredientAmountDto = new IngredientAmountDto(ingredientDto, 32);

        ingredientAmount2 = new IngredientAmount(ingredient2, 12);
        ingredientAmountDto2 = new IngredientAmountDto(ingredientDto2, 12);
    }

    @Test
    void test_dtoToModel() {
        // Arrange

        // Act
        IngredientAmount model = IngredientAmountMapper.INSTANCE.dtoToModel(ingredientAmountDto);

        // Assert
        assertEquals(ingredientAmountDto.getAmount(), model.getAmount());
        assertNotNull(ingredientAmountDto.getIngredient());
        assertEquals(ingredientAmountDto.getIngredient().getIngredientId(), model.getIngredient().getIngredientId());
        assertEquals(ingredientAmountDto.getIngredient().getName(), model.getIngredient().getName());
        assertEquals(ingredientAmountDto.getIngredient().getStock(), model.getIngredient().getStock());
        assertEquals(ingredientAmountDto.getIngredient().getThreshold(), model.getIngredient().getThreshold());
    }

    @Test
    void test_modelToDto() {
        // Arrange

        // Act
        IngredientAmountDto dto = IngredientAmountMapper.INSTANCE.modelToDto(ingredientAmount);

        // Assert
        assertEquals(ingredientAmount.getAmount(), dto.getAmount());
        assertNotNull(ingredientAmount.getIngredient());
        assertEquals(ingredientAmount.getIngredient().getIngredientId(), dto.getIngredient().getIngredientId());
        assertEquals(ingredientAmount.getIngredient().getName(), dto.getIngredient().getName());
        assertEquals(ingredientAmount.getIngredient().getStock(), dto.getIngredient().getStock());
        assertEquals(ingredientAmount.getIngredient().getThreshold(), dto.getIngredient().getThreshold());
    }

    @Test
    void test_dtoListToModelList() {
        // Arrange

        // Act
        List<IngredientAmount> models = IngredientAmountMapper.INSTANCE.dtoListToModelList(Arrays.asList(ingredientAmountDto, ingredientAmountDto2));

        // Assert
        assertEquals(2, models.size());
        assertNotNull(models.get(0).getIngredient());
        assertNotNull(models.get(1).getIngredient());
    }

    @Test
    void modelListToDtoList() {
        // Arrange

        // Act
        List<IngredientAmountDto> dto = IngredientAmountMapper.INSTANCE.modelListToDtoList(Arrays.asList(ingredientAmount, ingredientAmount2));

        // Assert
        assertEquals(2, dto.size());
        assertNotNull(dto.get(0).getIngredient());
        assertNotNull(dto.get(1).getIngredient());
    }
}