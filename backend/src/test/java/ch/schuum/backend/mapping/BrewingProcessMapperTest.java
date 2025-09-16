package ch.schuum.backend.mapping;

import ch.schuum.backend.dto.*;
import ch.schuum.backend.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BrewingProcessMapperTest {

    Tank tank;

    TankDto tankDto;

    Ingredient ingredient;

    IngredientDto ingredientDto;

    IngredientAmount ingredientAmount;

    IngredientAmountDto ingredientAmountDto;
    BeerType beerType;
    BeerTypeDto beerTypeDto;
    BrewingProcess brewingProcess;
    BrewingProcess brewingProcess2;
    BrewingProcessDto brewingProcessDto;
    BrewingProcessDto brewingProcessDto2;

    @BeforeEach
    void setUp() {
        tank = new Tank(3, 40);

        tankDto = new TankDto(3, 40, null);

        ingredient = new Ingredient(2, "Hopfen", 356.0F, 50.0F, false);
        ingredientDto = new IngredientDto(2, "Hopfen", 356.0F, 50.0F, false);

        ingredientAmount = new IngredientAmount(ingredient, 32F);
        ingredientAmountDto = new IngredientAmountDto(ingredientDto, 32F);

        beerType = new BeerType(3, "Red Ale", 30, Collections.singletonList(ingredientAmount), false);
        beerTypeDto = new BeerTypeDto(3, "Red Ale", 30, Collections.singletonList(ingredientAmountDto), false);

        brewingProcess = new BrewingProcess(1, beerType, tank, LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), "4326457", "no comment 3", "green", false);
        brewingProcess2 = new BrewingProcess(2, beerType, tank, LocalDate.now().minusDays(50), LocalDate.now().minusDays(20), "2354", "-", "pink", false);

        brewingProcessDto = new BrewingProcessDto(3, beerTypeDto, tankDto, LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), "4326457", "no comment 3", "green", false);
        brewingProcessDto2 = new BrewingProcessDto(2, beerTypeDto, tankDto, LocalDate.now().minusDays(50), LocalDate.now().minusDays(20), "2354", "-", "pink", false);
    }

    @Test
    void test_dtoToModel() {
        // Arrange

        // Act
        BrewingProcess model = BrewingProcessMapper.INSTANCE.dtoToModel(brewingProcessDto);

        // Assert
        assertEquals(brewingProcessDto.getBrewingId(), model.getBrewingId());
        assertEquals(brewingProcessDto.getTank().getTankId(), model.getTank().getTankId());
        assertEquals(brewingProcessDto.getTank().getVolume(), model.getTank().getVolume());
        assertEquals(brewingProcessDto.getBeerType().getBeerTypeId(), model.getBeerType().getBeerTypeId());
        assertEquals(brewingProcessDto.getBeerType().getName(), model.getBeerType().getName());
        assertEquals(brewingProcessDto.getBeerType().getDuration(), model.getBeerType().getDuration());
        assertEquals(brewingProcessDto.getBeerType().getIngredients().size(), model.getBeerType().getIngredients().size());
        assertEquals(brewingProcessDto.getStartDate(), model.getStartDate());
        assertEquals(brewingProcessDto.getEndDate(), model.getEndDate());
        assertEquals(brewingProcessDto.getSudNumber(), model.getSudNumber());
        assertEquals(brewingProcessDto.getComment(), model.getComment());
        assertEquals(brewingProcessDto.getColor(), model.getColor());
        assertEquals(brewingProcessDto.isDeleted(), model.isDeleted());
    }

    @Test
    void test_modelToDto() {
        // Arrange

        // Act
        BrewingProcessDto dto = BrewingProcessMapper.INSTANCE.modelToDto(brewingProcess);

        // Assert
        assertEquals(brewingProcess.getBrewingId(), dto.getBrewingId());
        assertEquals(brewingProcess.getTank().getTankId(), dto.getTank().getTankId());
        assertEquals(brewingProcess.getTank().getVolume(), dto.getTank().getVolume());
        assertEquals(brewingProcess.getBeerType().getBeerTypeId(), dto.getBeerType().getBeerTypeId());
        assertEquals(brewingProcess.getBeerType().getName(), dto.getBeerType().getName());
        assertEquals(brewingProcess.getBeerType().getDuration(), dto.getBeerType().getDuration());
        assertEquals(brewingProcess.getBeerType().getIngredients().size(), dto.getBeerType().getIngredients().size());
        assertEquals(brewingProcess.getStartDate(), dto.getStartDate());
        assertEquals(brewingProcess.getEndDate(), dto.getEndDate());
        assertEquals(brewingProcess.getSudNumber(), dto.getSudNumber());
        assertEquals(brewingProcess.getComment(), dto.getComment());
        assertEquals(brewingProcess.getColor(), dto.getColor());
        assertEquals(brewingProcess.isDeleted(), dto.isDeleted());
    }

    @Test
    void test_dtoListToModelList() {
        // Arrange

        // Act
        List<BrewingProcess> models = BrewingProcessMapper.INSTANCE.dtoListToModelList(Arrays.asList(brewingProcessDto, brewingProcessDto2));

        // Assert
        assertEquals(2, models.size());
        assertNotNull(models.get(0).getTank());
        assertNotNull(models.get(0).getBeerType());
        assertNotNull(models.get(1).getTank());
        assertNotNull(models.get(1).getBeerType());
    }

    @Test
    void test_modelListToDtoList() {
        // Arrange

        // Act
        List<BrewingProcessDto> dtos = BrewingProcessMapper.INSTANCE.modelListToDtoList(Arrays.asList(brewingProcess, brewingProcess2));

        // Assert
        assertEquals(2, dtos.size());
        assertNotNull(dtos.get(0).getTank());
        assertNotNull(dtos.get(0).getBeerType());
        assertNotNull(dtos.get(1).getTank());
        assertNotNull(dtos.get(1).getBeerType());
    }
}