package ch.schuum.backend.mapping;

import ch.schuum.backend.dto.TankDto;
import ch.schuum.backend.model.Tank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TankMapperTest {

    Tank tank1;
    Tank tank2;
    TankDto tankDto1;
    TankDto tankDto2;
    List<Tank> tankList = new ArrayList<>();
    List<TankDto> tankDtoList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        tank1 = new Tank(1, 50);
        tank2 = new Tank(2, 30);
        tankDto1 = new TankDto(1, 50, null);
        tankDto2 = new TankDto(2, 30, null);
        Collections.addAll(tankList, tank1, tank2);
        Collections.addAll(tankDtoList, tankDto1, tankDto2);
    }

    @Test
    void test_dtoToModel() {
        // Arrange

        // Act
        Tank model = TankMapper.INSTANCE.dtoToModel(tankDto1);

        // Assert
        assertEquals(tankDto1.getTankId(), model.getTankId());
        assertEquals(tankDto1.getVolume(), model.getVolume());
    }

    @Test
    void test_modelToDto() {
        // Arrange

        // Act
        TankDto dto = TankMapper.INSTANCE.modelToDto(tank1);

        // Assert
        assertEquals(tank1.getTankId(), dto.getTankId());
        assertEquals(tank1.getVolume(), dto.getVolume());
        assertNull(dto.getBrewingProcess());
    }

    @Test
    void test_dtoListToModelList() {
        // Arrange

        // Act
        List<Tank> models = TankMapper.INSTANCE.dtoListToModelList(tankDtoList);

        // Assert
        assertEquals(tankDtoList.size(), models.size());
    }

    @Test
    void test_modelListToDtoList() {
        // Arrange

        // Act
        List<TankDto> dtos = TankMapper.INSTANCE.modelListToDtoList(tankList);

        // Assert
        assertEquals(tankList.size(), dtos.size());
        assertNull(dtos.get(0).getBrewingProcess());
        assertNull(dtos.get(1).getBrewingProcess());
    }
}