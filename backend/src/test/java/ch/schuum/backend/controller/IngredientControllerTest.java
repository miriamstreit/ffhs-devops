package ch.schuum.backend.controller;

import ch.schuum.backend.dto.IngredientDto;
import ch.schuum.backend.service.IngredientService;
import ch.schuum.backend.service.TankService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(IngredientController.class)
@AutoConfigureMockMvc(addFilters = false)
class IngredientControllerTest {

    @MockBean
    IngredientService ingredientService;

    @Autowired
    MockMvc mockMvc;

    IngredientDto ingredientDto1;
    IngredientDto ingredientDto2;

    List<IngredientDto> ingredients = new ArrayList<>();

    ObjectMapper mapper;
    ObjectWriter objectWriter;

    @BeforeEach
    void setUp() {
        ingredientDto1 = new IngredientDto(1, "Weizen", 34.0F, 50.0F, false);
        ingredientDto2 = new IngredientDto(2, "Hopfen", 356.0F, 50.0F, false);
        Collections.addAll(ingredients, ingredientDto1, ingredientDto2);

        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    @WithMockUser
    void test_getAllIngredients() throws Exception {
        // Arrange
        when(ingredientService.getAllIngredients()).thenReturn(ingredients);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/ingredients")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ingredientId")));
    }

    @Test
    @WithMockUser
    void test_getAllIngredientsBelowThreshold() throws Exception {
        // Arrange
        when(ingredientService.getAllIngredientsBelowThreshold()).thenReturn(Collections.singletonList(ingredientDto1));

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/ingredients/belowthreshold")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ingredientId")));
    }

    @Test
    @WithMockUser
    void test_getIngredientById_whenIngredientExists_returnIngredient() throws Exception {
        // Arrange
        when(ingredientService.getIngredientById(1)).thenReturn(Optional.ofNullable(ingredientDto1));

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/ingredients/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ingredientId")));
    }

    @Test
    @WithMockUser
    void test_getIngredientById_whenIngredientDoesntExist_returnException() throws Exception {
        // Arrange
        when(ingredientService.getIngredientById(1)).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/ingredients/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isNotFound())
                .andExpect(status().reason(containsString("konnte nicht gefunden werden")));
    }


    @Test
    @WithMockUser
    void test_createIngredient() throws Exception {
        // Arrange
        when(ingredientService.createIngredient(any())).thenReturn(ingredientDto1);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/ingredients/")
                        .content(objectWriter.writeValueAsString(ingredientDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ingredientId")));
    }

    @Test
    @WithMockUser
    void test_updateIngredient() throws Exception {
        // Arrange
        when(ingredientService.updateIngredient(anyInt(), any())).thenReturn(ingredientDto1);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.put("/ingredients/1")
                        .content(objectWriter.writeValueAsString(ingredientDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ingredientId")));
    }

    @Test
    @WithMockUser
    void test_deleteIngredient_whenIngredientExists_delete() throws Exception {
        // Arrange
        when(ingredientService.deleteIngredient(anyInt())).thenReturn(1);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/ingredients/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));
    }

    @Test
    @WithMockUser
    void test_deleteIngredient_whenIngredientNotExists_return() throws Exception {
        // Arrange
        when(ingredientService.deleteIngredient(anyInt())).thenReturn(0);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/ingredients/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("0")));
    }
}