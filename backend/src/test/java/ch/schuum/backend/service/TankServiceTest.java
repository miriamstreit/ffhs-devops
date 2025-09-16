package ch.schuum.backend.service;

import ch.schuum.backend.dto.TankDto;
import ch.schuum.backend.model.BrewingProcess;
import ch.schuum.backend.model.Tank;
import ch.schuum.backend.repository.TankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TankServiceTest {

    @Mock
    TankRepository tankRepository;

    @Mock
    BrewingProcessService brewingProcessService;

    @InjectMocks
    TankService tankService;

    private Tank tank1;
    private TankDto tankDto1;
    private Tank tank2;
    private Tank tank3;
    private List<Tank> tanks;

    BrewingProcess brewingProcess1;
    BrewingProcess brewingProcess2;

    @BeforeEach
    void setUp() {
        tank1 = new Tank(1, 50);
        tankDto1 = new TankDto(1, 50, null);
        tank2 = new Tank(2, 30);
        tank3 = new Tank(3, 40);
        tanks = new ArrayList<>();
        tanks.add(tank1);
        tanks.add(tank2);
        tanks.add(tank3);
        brewingProcess1 = new BrewingProcess(1, null, tank2, LocalDate.of(2022, 10, 3), LocalDate.of(2022, 11, 2), "12434", "no comment", "blue", false);
        brewingProcess2 = new BrewingProcess(2, null, tank3, LocalDate.now(), LocalDate.now().plusDays(30), "46457", "no comment 2", "red", false);
    }

    @Test
    void test_getAllTanks_whenTanksExist_returnTanksList() {
        // Arrange
        when(tankRepository.findAll()).thenReturn(tanks);

        // Act
        List<TankDto> tanksFromDb = tankService.getAllTanks();

        // Assert
        assertEquals(tanks.size(), tanksFromDb.size());
    }

    @Test
    void test_getAllTanks_whenNoTanksExist_returnEmptyList() {
        // Arrange
        when(tankRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<TankDto> tanksFromDb = tankService.getAllTanks();

        // Assert
        assertEquals(0, tanksFromDb.size());
    }

    @Test
    void test_getAllTanks_whenBrewingProcessExists_returnTanksWithCurrentBrewingProcess() {
        // Arrange
        when(tankRepository.findAll()).thenReturn(tanks);
        when(brewingProcessService.getCurrentBrewingProcessByTankId(anyInt())).thenReturn(brewingProcess2);

        // Act
        List<TankDto> tanksFromDb = tankService.getAllTanks();

        // Assert
        Optional<TankDto> tankDto3 = tanksFromDb.stream().filter(tank -> tank.getTankId() == tank3.getTankId()).findFirst();
        assertTrue(tankDto3.isPresent());
        assertNotNull(tankDto3.get().getBrewingProcess());
    }

    @Test
    void test_getAllTanks_whenNoBrewingProcessExists_returnTanksWithoutBrewingProcess() {
        // Arrange
        when(tankRepository.findAll()).thenReturn(tanks);
        when(brewingProcessService.getCurrentBrewingProcessByTankId(anyInt())).thenReturn(null);

        // Act
        List<TankDto> tanksFromDb = tankService.getAllTanks();

        // Assert
        Optional<TankDto> tankDto2 = tanksFromDb.stream().filter(tank -> tank.getTankId() == tank2.getTankId()).findFirst();
        assertTrue(tankDto2.isPresent());
        assertNull(tankDto2.get().getBrewingProcess());
    }

    @Test
    void test_getTankById_whenTankExists_returnTank() {
        // Arrange
        when(tankRepository.findById(tank1.getTankId())).thenReturn(Optional.ofNullable(tank1));

        // Act
        Optional<TankDto> tankFromDb = tankService.getTankById(tank1.getTankId());

        // Assert
        assertTrue(tankFromDb.isPresent());
        assertEquals(tank1.getTankId(), tankFromDb.get().getTankId());
    }

    @Test
    void test_getTankById_whenTankDoesNotExist_returnNull() {
        // Arrange
        when(tankRepository.findById(tank1.getTankId())).thenReturn(Optional.empty());

        // Act
        Optional<TankDto> tankFromDb = tankService.getTankById(tank1.getTankId());

        // Assert
        assertFalse(tankFromDb.isPresent());
    }

    @Test
    void test_getTankById_whenBrewingProcessExists_returnTankWithCurrentBrewingProcess() {
        // Arrange
        when(tankRepository.findById(anyInt())).thenReturn(Optional.ofNullable(tank3));
        when(brewingProcessService.getCurrentBrewingProcessByTankId(anyInt())).thenReturn(brewingProcess2);

        // Act
        Optional<TankDto> tankFromDb = tankService.getTankById(tank1.getTankId());

        // Assert
        assertTrue(tankFromDb.isPresent());
        assertNotNull(tankFromDb.get().getBrewingProcess());
    }

    @Test
    void test_getTankById_whenNoBrewingProcessExists_returnTankWithoutBrewingProcess() {
        // Arrange
        when(tankRepository.findById(anyInt())).thenReturn(Optional.ofNullable(tank3));
        when(brewingProcessService.getCurrentBrewingProcessByTankId(anyInt())).thenReturn(null);

        // Act
        Optional<TankDto> tankFromDb = tankService.getTankById(tank1.getTankId());

        // Assert
        assertTrue(tankFromDb.isPresent());
        assertNull(tankFromDb.get().getBrewingProcess());
    }

    @Test
    void test_createTank_createNewTankAndReturn() {
        // Arrange
        when(tankRepository.save(any())).thenReturn(tank1);

        // Act
        TankDto newTank = tankService.createTank(tankDto1);

        // Assert
        assertEquals(tankDto1.getTankId(), newTank.getTankId());
        assertEquals(tankDto1.getVolume(), newTank.getVolume());
        verify(tankRepository, times(1)).save(any());
    }

    @Test
    void test_updateTank_whenTankExists_updateAndReturn() {
        // Arrange
        when(tankRepository.findById(anyInt())).thenReturn(Optional.ofNullable(tank2));
        tank2.setVolume(50);
        when(tankRepository.save(any())).thenReturn(tank2);

        // Act
        TankDto updatedTank = tankService.updateTank(tank2.getTankId(), new TankDto(0, 50, null));

        // Assert
        assertEquals(50, updatedTank.getVolume());
        verify(tankRepository, times(1)).save(any());
    }

    @Test
    void test_updateTank_whenTankDoesNotExist_createAndReturn() {
        // Arrange
        when(tankRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        tankService.updateTank(tank2.getTankId(), new TankDto(0, 50, null));

        // Assert
        verify(tankRepository, times(1)).save(any());
    }
}