package ch.schuum.backend.controller;

import ch.schuum.backend.dto.TankDto;
import ch.schuum.backend.service.TankService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TankController.class)
@AutoConfigureMockMvc(addFilters = false)
class TankControllerTest {

    @MockBean
    TankService tankService;

    @Autowired
    MockMvc mockMvc;

    TankDto tankDto1;
    TankDto tankDto2;
    List<TankDto> tankDtoList = new ArrayList<>();

    ObjectMapper mapper;
    ObjectWriter objectWriter;

    @BeforeEach
    void setUp() {
        tankDto1 = new TankDto(1, 50, null);
        tankDto2 = new TankDto(2, 30, null);
        Collections.addAll(tankDtoList, tankDto1, tankDto2);

        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    @WithMockUser
    void test_getAllTanks() throws Exception {
        // Arrange
        when(tankService.getAllTanks()).thenReturn(tankDtoList);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/tanks")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("tankId")));
    }

    @Test
    @WithMockUser
    void test_getTankById_whenTankExists_returnTank() throws Exception {
        // Arrange
        when(tankService.getTankById(1)).thenReturn(Optional.ofNullable(tankDto1));

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/tanks/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("tankId")));
    }

    @Test
    @WithMockUser
    void test_getTankById_whenTankDoesntExist_returnException() throws Exception {
        // Arrange
        when(tankService.getTankById(1)).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/tanks/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isNotFound())
                .andExpect(status().reason(containsString("konnte nicht gefunden werden")));
    }

    @Test
    @WithMockUser
    void test_createTank() throws Exception {
        // Arrange
        when(tankService.createTank(any())).thenReturn(tankDto1);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/tanks/")
                        .content(objectWriter.writeValueAsString(tankDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("tankId")));
    }

    @Test
    @WithMockUser
    void test_updateTank() throws Exception {
        // Arrange
        when(tankService.updateTank(anyInt(), any())).thenReturn(tankDto1);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.put("/tanks/1")
                        .content(objectWriter.writeValueAsString(tankDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("tankId")));
    }
}