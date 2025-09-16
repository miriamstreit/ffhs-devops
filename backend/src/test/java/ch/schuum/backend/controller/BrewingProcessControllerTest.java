package ch.schuum.backend.controller;

import ch.schuum.backend.dto.*;
import ch.schuum.backend.service.BrewingProcessService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
class MeterConfig {
  @Bean
  public MeterRegistry registry() {
    return new SimpleMeterRegistry();
  }
}

@RunWith(SpringRunner.class)
@Import(MeterConfig.class)
@WebMvcTest(BrewingProcessController.class)
@AutoConfigureMockMvc(addFilters = false)
class BrewingProcessControllerTest {

    @MockBean
    BrewingProcessService brewingProcessService;

    @Autowired
    MeterRegistry meterRegistry;

    @Autowired
    MockMvc mockMvc;

    TankDto tankDto;

    IngredientDto ingredientDto;

    IngredientAmountDto ingredientAmountDto;
    BeerTypeDto beerTypeDto;
    BrewingProcessDto brewingProcessDto;
    BrewingProcessDto brewingProcessDto2;

    ObjectMapper mapper;
    ObjectWriter objectWriter;

    Counter brewingProcessCounter;

    @BeforeEach
    void setUp() {
        tankDto = new TankDto(3, 40, null);

        ingredientDto = new IngredientDto(2, "Hopfen", 356.0F, 50.0F, false);

        ingredientAmountDto = new IngredientAmountDto(ingredientDto, 32F);

        beerTypeDto = new BeerTypeDto(3, "Red Ale", 30, Collections.singletonList(ingredientAmountDto), false);

        brewingProcessDto = new BrewingProcessDto(3, beerTypeDto, tankDto, LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), "4326457", "no comment 3", "green", false);
        brewingProcessDto2 = new BrewingProcessDto(2, beerTypeDto, tankDto, LocalDate.now().minusDays(50), LocalDate.now().minusDays(20), "2354", "-", "pink", false);


        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.registerModule(new JavaTimeModule());
        objectWriter = mapper.writer().withDefaultPrettyPrinter();;

        brewingProcessCounter = Counter.builder("brewing_process_creation_request_total").
                tag("version", "v1").
                description("Brewing Process Creation Count").
                register(meterRegistry);
    }

    @Test
    @WithMockUser
    void test_getAllBrewingProcesses() throws Exception {
        // Arrange
        when(brewingProcessService.getAllBrewingProcesses()).thenReturn(Arrays.asList(brewingProcessDto, brewingProcessDto2));

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/brewingprocess")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brewingId")));
    }

    @Test
    @WithMockUser
    void test_getBrewingProcessById_whenBrewingProcessExists_returnBrewingProcess() throws Exception {
        // Arrange
        when(brewingProcessService.getBrewingProcessById(1)).thenReturn(Optional.ofNullable(brewingProcessDto));

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/brewingprocess/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brewingId")));
    }

    @Test
    @WithMockUser
    void test_getBrewingProcessById_whenBrewingProcessDoesntExist_returnException() throws Exception {
        // Arrange
        when(brewingProcessService.getBrewingProcessById(1)).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/brewingprocess/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isNotFound())
                .andExpect(status().reason(containsString("konnte nicht gefunden werden")));
    }


    @Test
    @WithMockUser
    void test_createBrewingProcess() throws Exception {
        // Arrange
        when(brewingProcessService.createBrewingProcess(any())).thenReturn(brewingProcessDto);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/brewingprocess/")
                        .content(objectWriter.writeValueAsString(brewingProcessDto))
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brewingId")));
    }

    @Test
    @WithMockUser
    void test_updateBrewingProcess() throws Exception {
        // Arrange
        when(brewingProcessService.updateBrewingProcess(anyInt(), any())).thenReturn(brewingProcessDto);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.put("/brewingprocess/1")
                        .content(objectWriter.writeValueAsString(brewingProcessDto))
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brewingId")));
    }

    @Test
    @WithMockUser
    void test_deleteBrewingProcess_whenBrewingProcessExists_delete() throws Exception {
        // Arrange
        when(brewingProcessService.deleteBrewingProcess(anyInt())).thenReturn(1);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/brewingprocess/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));
    }

    @Test
    @WithMockUser
    void test_deleteBrewingProcess_whenBrewingProcessNotExists_return() throws Exception {
        // Arrange
        when(brewingProcessService.deleteBrewingProcess(anyInt())).thenReturn(0);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/brewingprocess/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("0")));
    }
}