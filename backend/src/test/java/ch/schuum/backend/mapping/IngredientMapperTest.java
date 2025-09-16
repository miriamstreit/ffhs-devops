package ch.schuum.backend.mapping;

import ch.schuum.backend.dto.IngredientDto;
import ch.schuum.backend.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IngredientMapperTest {

    Ingredient ingredient1;
    Ingredient ingredient2;

    IngredientDto ingredientDto1;
    IngredientDto ingredientDto2;

    List<Ingredient> ingredients = new ArrayList<>();
    List<IngredientDto> ingredientDtos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        ingredient1 = new Ingredient(1, "Weizen", 34.0F, 50.0F, false);
        ingredient2 = new Ingredient(2, "Hopfen", 356.0F, 50.0F, false);
        ingredientDto1 = new IngredientDto(1, "Weizen", 34.0F, 50.0F, false);
        ingredientDto2 = new IngredientDto(2, "Hopfen", 356.0F, 50.0F, false);
        Collections.addAll(ingredients, ingredient1, ingredient2);
        Collections.addAll(ingredientDtos, ingredientDto1, ingredientDto2);
    }

    @Test
    void test_dtoToModel() {
        // Arrange

        // Act
        Ingredient model = IngredientMapper.INSTANCE.dtoToModel(ingredientDto1);

        // Assert
        assertEquals(ingredientDto1.getIngredientId(), model.getIngredientId());
        assertEquals(ingredientDto1.getName(), model.getName());
        assertEquals(ingredientDto1.getStock(), model.getStock());
        assertEquals(ingredientDto1.getThreshold(), model.getThreshold());
    }

    @Test
    void test_modelToDto() {
        // Arrange

        // Act
        IngredientDto dto = IngredientMapper.INSTANCE.modelToDto(ingredient1);

        // Assert
        assertEquals(ingredientDto1.getIngredientId(), dto.getIngredientId());
        assertEquals(ingredientDto1.getName(), dto.getName());
        assertEquals(ingredientDto1.getStock(), dto.getStock());
        assertEquals(ingredientDto1.getThreshold(), dto.getThreshold());
    }

    @Test
    void test_dtoListToModelList() {
        // Arrange

        // Act
        List<Ingredient> models = IngredientMapper.INSTANCE.dtoListToModelList(ingredientDtos);

        // Assert
        assertEquals(ingredientDtos.size(), models.size());
    }

    @Test
    void test_modelListToDtoList() {
        // Arrange

        // Act
        List<IngredientDto> dtos = IngredientMapper.INSTANCE.modelListToDtoList(ingredients);

        // Assert
        assertEquals(ingredients.size(), dtos.size());
    }
}