package ch.schuum.backend.service;

import ch.schuum.backend.dto.*;
import ch.schuum.backend.model.*;
import ch.schuum.backend.repository.BrewingProcessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrewingProcessServiceTest {

    @Mock
    BrewingProcessRepository brewingProcessRepository;

    @Mock
    IngredientService ingredientService;

    @Mock
    BeerTypeService beerTypeService;

    @InjectMocks
    BrewingProcessService brewingProcessService;

    Tank tank2;
    Tank tank3;

    TankDto tankDto2;
    TankDto tankDto3;

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
    BrewingProcess brewingProcess1;
    BrewingProcess brewingProcess2;
    BrewingProcess brewingProcess3;
    List<BrewingProcess> brewingProcesses;
    BrewingProcessDto brewingProcessDto2;
    BrewingProcessDto brewingProcessDto3;


    @BeforeEach
    void setUp() {
        tank2 = new Tank(2, 30);
        tank3 = new Tank(3, 40);

        tankDto2 = new TankDto(2, 30, null);
        tankDto3 = new TankDto(3, 40, null);

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

        brewingProcess1 = new BrewingProcess(1, beerType1, tank2, LocalDate.now().minusDays(60), LocalDate.now().minusDays(30), "12434", "no comment", "blue", false);
        brewingProcess2 = new BrewingProcess(2, beerType2, tank3, LocalDate.now(), LocalDate.now().plusDays(30), "46457", "no comment 2", "red", false);
        brewingProcess3 = new BrewingProcess(3, beerType3, tank3, LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), "4326457", "no comment 3", "green", true);
        brewingProcesses = new ArrayList<>();
        Collections.addAll(brewingProcesses, brewingProcess1, brewingProcess2);

        brewingProcessDto2 = new BrewingProcessDto(2, beerTypeDto2, tankDto3, LocalDate.now(), LocalDate.now().plusDays(30), "46457", "no comment 2", "red", false);
        brewingProcessDto3 = new BrewingProcessDto(3, beerTypeDto3, tankDto3, LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), "4326457", "no comment 3", "green", true);
    }

    @Test
    void test_getAllBrewingProcesses_whenBrewingProcessesExist_returnList() {
        // Arrange
        when(brewingProcessRepository.findAllByDeleted(false)).thenReturn(brewingProcesses);

        // Act
        List<BrewingProcessDto> brewingProcessDtos = brewingProcessService.getAllBrewingProcesses();

        // Assert
        assertEquals(2, brewingProcessDtos.size());
    }

    @Test
    void test_getAllBrewingProcesses_whenNoBrewingProcessesExist_returnEmptyList() {
        // Arrange
        when(brewingProcessRepository.findAllByDeleted(false)).thenReturn(Collections.emptyList());

        // Act
        List<BrewingProcessDto> brewingProcessDtos = brewingProcessService.getAllBrewingProcesses();

        // Assert
        assertEquals(0, brewingProcessDtos.size());
    }

    @Test
    void test_getBrewingProcessById_whenExists_returnBrewingProcess() {
        // Arrange
        when(brewingProcessRepository.findById(anyInt())).thenReturn(Optional.ofNullable(brewingProcess1));

        // Act
        Optional<BrewingProcessDto> brewingProcessDto = brewingProcessService.getBrewingProcessById(brewingProcess1.getBrewingId());

        // Assert
        assertTrue(brewingProcessDto.isPresent());
        assertEquals(brewingProcess1.getBrewingId(), brewingProcessDto.get().getBrewingId());
    }

    @Test
    void test_getBrewingProcessById_whenNotExists_returnEmptyOptional() {
        // Arrange
        when(brewingProcessRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        Optional<BrewingProcessDto> brewingProcessDto = brewingProcessService.getBrewingProcessById(brewingProcess1.getBrewingId());

        // Assert
        assertFalse(brewingProcessDto.isPresent());
    }

    @Test
    void test_createBrewingProcess_whenEnoughIngredientsAndFreeTank_createAndReturn() {
        // Arrange
        when(beerTypeService.getBeerTypeById(anyInt())).thenReturn(Optional.ofNullable(beerTypeDto3));
        when(ingredientService.getIngredientById(anyInt())).thenReturn(Optional.ofNullable(ingredientDto2));
        when(ingredientService.updateIngredient(anyInt(), any())).thenReturn(ingredientDto2);
        when(brewingProcessRepository.findByTank_TankIdAndDeleted(tank3.getTankId(), false)).thenReturn(Collections.emptyList());
        IngredientDto subtractedIngredients = ingredientDto2;
        subtractedIngredients.setStock(324F);

        // Act
        brewingProcessService.createBrewingProcess(brewingProcessDto3);

        // Assert
        verify(ingredientService, times(1)).updateIngredient(ingredientDto2.getIngredientId(), subtractedIngredients);
        verify(brewingProcessRepository, times(1)).save(any());
    }

    @Test
    void test_createBrewingProcess_whenNotEnoughIngredients_throwException() {
        // Arrange
        when(beerTypeService.getBeerTypeById(anyInt())).thenReturn(Optional.ofNullable(beerTypeDto2));
        when(ingredientService.getIngredientById(anyInt())).thenReturn(Optional.ofNullable(ingredientDto1));

        // Act
        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class,
                () -> brewingProcessService.createBrewingProcess(brewingProcessDto2),
                "Expected createBrewingProcess() to throw, but it didn't"
        );

        // Assert
        verify(brewingProcessRepository, times(0)).save(any());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
        assertEquals("Die benötigten Zutaten reichen nicht aus für diese Aktion.", thrown.getReason());
    }

    @Test
    void test_createBrewingProcess_whenTankAlreadyInUse_throwException() {
        // Arrange
        when(beerTypeService.getBeerTypeById(anyInt())).thenReturn(Optional.ofNullable(beerTypeDto2));
        when(brewingProcessRepository.findByTank_TankIdAndDeleted(tank3.getTankId(), false)).thenReturn(Collections.singletonList(brewingProcess2));
        // Act
        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class,
                () -> brewingProcessService.createBrewingProcess(brewingProcessDto3),
                "Expected createBrewingProcess() to throw, but it didn't"
        );

        // Assert
        verify(brewingProcessRepository, times(0)).save(any());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
        assertEquals("Dieser Tank wird zu diesem Zeitpunkt bereits verwendet.", thrown.getReason());
    }

    @Test
    void test_updateBrewingProcess_whenExists_updateAndReturn() {
        // Arrange
        when(brewingProcessRepository.findById(brewingProcess3.getBrewingId())).thenReturn(Optional.ofNullable(brewingProcess3));
        brewingProcess3 = new BrewingProcess(3, beerType3, tank3, LocalDate.now().minusDays(5), LocalDate.now().plusDays(25), "1111", "no comment 5", "pink", false);
        when(brewingProcessRepository.save(any())).thenReturn(brewingProcess3);

        // Act
        BrewingProcessDto brewingProcessDto = brewingProcessService.updateBrewingProcess(brewingProcessDto3.getBrewingId(), brewingProcessDto3);

        // Assert
        verify(brewingProcessRepository, times(1)).save(any());
        assertEquals(brewingProcess3.getTank().getTankId(), brewingProcessDto.getTank().getTankId());
        assertEquals(brewingProcess3.getBeerType().getBeerTypeId(), brewingProcessDto.getBeerType().getBeerTypeId());
        assertEquals(brewingProcess3.getStartDate(), brewingProcessDto.getStartDate());
        assertEquals(brewingProcess3.getEndDate(), brewingProcessDto.getEndDate());
        assertEquals(brewingProcess3.getSudNumber(), brewingProcessDto.getSudNumber());
        assertEquals(brewingProcess3.getComment(), brewingProcessDto.getComment());
        assertEquals(brewingProcess3.getColor(), brewingProcessDto.getColor());
        assertEquals(brewingProcess3.isDeleted(), brewingProcessDto.isDeleted());
    }

    @Test
    void test_updateBrewingProcess_whenNotExists_createAndReturn() {
        // Arrange
        when(brewingProcessRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(brewingProcessRepository.save(any())).thenReturn(brewingProcess3);
        when(beerTypeService.getBeerTypeById(anyInt())).thenReturn(Optional.of(beerTypeDto3));

        // Act
        brewingProcessService.updateBrewingProcess(brewingProcessDto3.getBrewingId(), brewingProcessDto3);

        // Assert
        verify(brewingProcessRepository, times(1)).save(any());
    }

    @Test
    void test_deleteBrewingProcess_ifExists_addIngredientsAndDelete() {
        // Arrange
        when(brewingProcessRepository.findById(anyInt())).thenReturn(Optional.of(brewingProcess3));
        when(ingredientService.getIngredientById(anyInt())).thenReturn(Optional.ofNullable(ingredientDto2));
        when(ingredientService.updateIngredient(anyInt(), any())).thenReturn(ingredientDto2);
        IngredientDto addedIngredients = ingredientDto2;
        addedIngredients.setStock(324F);

        // Act
        int deletedId = brewingProcessService.deleteBrewingProcess(brewingProcess3.getBrewingId());

        // Assert
        assertEquals(brewingProcess3.getBrewingId(), deletedId);
        verify(brewingProcessRepository, times(1)).save(any());
        verify(ingredientService, times(1)).updateIngredient(ingredientDto2.getIngredientId(), addedIngredients);
    }

    @Test
    void test_deleteBrewingProcess_ifNotExists_return() {
        // Arrange
        when(brewingProcessRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        int deletedId = brewingProcessService.deleteBrewingProcess(brewingProcess3.getBrewingId());

        // Assert
        assertEquals(0, deletedId);
        verify(brewingProcessRepository, times(0)).delete(any());
    }

    @Test
    void test_getCurrentBrewingProcessByTankId_whenExistsAndCurrentAndNotDeleted_returnBrewingProcess() {
        // Arrange
        when(brewingProcessRepository.findFirstByTank_TankIdAndEndDateGreaterThanEqualAndDeleted(anyInt(), any(), anyBoolean())).thenReturn(Optional.ofNullable(brewingProcess2));

        // Act
        BrewingProcess currentBrewingProcess = brewingProcessService.getCurrentBrewingProcessByTankId(tank3.getTankId());

        // Assert
        assertNotNull(currentBrewingProcess);
        assertEquals(tank3.getTankId(), currentBrewingProcess.getTank().getTankId());
    }

    @Test
    void test_getCurrentBrewingProcessByTankId_whenNotCurrentOrDeleted_returnNull() {
        // Arrange
        when(brewingProcessRepository.findFirstByTank_TankIdAndEndDateGreaterThanEqualAndDeleted(anyInt(), any(), anyBoolean())).thenReturn(Optional.empty());

        // Act
        BrewingProcess currentBrewingProcess = brewingProcessService.getCurrentBrewingProcessByTankId(tank2.getTankId());

        // Assert
        assertNull(currentBrewingProcess);
    }
}