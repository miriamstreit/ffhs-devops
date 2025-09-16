package ch.schuum.backend.mapping;

import ch.schuum.backend.dto.BeerTypeDto;
import ch.schuum.backend.dto.IngredientAmountDto;
import ch.schuum.backend.dto.IngredientDto;
import ch.schuum.backend.dto.TankDto;
import ch.schuum.backend.model.BeerType;
import ch.schuum.backend.model.Ingredient;
import ch.schuum.backend.model.IngredientAmount;
import ch.schuum.backend.model.Tank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BeerTypeMapperTest {

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
    void test_dtoToModel() {
        // Arrange

        // Act
        BeerType model = BeerTypeMapper.INSTANCE.dtoToModel(beerTypeDto1);

        // Assert
        assertEquals(beerTypeDto1.getBeerTypeId(), model.getBeerTypeId());
        assertEquals(beerTypeDto1.getDuration(), model.getDuration());
        assertEquals(beerTypeDto1.getIngredients().size(), model.getIngredients().size());
    }

    @Test
    void test_modelToDto() {
        // Arrange

        // Act
        BeerTypeDto dto = BeerTypeMapper.INSTANCE.modelToDto(beerType1);

        // Assert
        assertEquals(beerTypeDto1.getBeerTypeId(), dto.getBeerTypeId());
        assertEquals(beerTypeDto1.getDuration(), dto.getDuration());
        assertEquals(beerTypeDto1.getIngredients().size(), dto.getIngredients().size());
    }

    @Test
    void test_dtoListToModelList() {
        // Arrange

        // Act
        List<BeerType> models = BeerTypeMapper.INSTANCE.dtoListToModelList(beerTypeDtos);

        // Assert
        assertEquals(beerTypeDtos.size(), models.size());
    }

    @Test
    void test_modelListToDtoList() {
        // Arrange

        // Act
        List<BeerTypeDto> dtos = BeerTypeMapper.INSTANCE.modelListToDtoList(beerTypes);

        // Assert
        assertEquals(beerTypes.size(), dtos.size());
    }
}