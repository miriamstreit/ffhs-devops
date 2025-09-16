package ch.schuum.backend.controller;

import ch.schuum.backend.dto.BeerTypeDto;
import ch.schuum.backend.dto.IngredientAmountDto;
import ch.schuum.backend.dto.IngredientDto;
import ch.schuum.backend.service.BeerTypeService;
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

import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BeerTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
class BeerTypeControllerTest {

    @MockBean
    BeerTypeService beerTypeService;

    @Autowired
    MockMvc mockMvc;

    IngredientDto ingredientDto1;
    IngredientDto ingredientDto2;

    IngredientAmountDto ingredientAmountDto1;
    IngredientAmountDto ingredientAmountDto2;

    List<IngredientAmountDto> ingredientAmountsDto;

    BeerTypeDto beerTypeDto1;
    BeerTypeDto beerTypeDto2;

    ObjectMapper mapper;
    ObjectWriter objectWriter;

    @BeforeEach
    void setUp() {
        ingredientDto1 = new IngredientDto(1, "Weizen", 34.0F, 50.0F, false);
        ingredientDto2 = new IngredientDto(2, "Hopfen", 356.0F, 50.0F, false);

        ingredientAmountDto1 = new IngredientAmountDto(ingredientDto1, 45F);
        ingredientAmountDto2 = new IngredientAmountDto(ingredientDto2, 32F);
        ingredientAmountsDto = new ArrayList<>();
        Collections.addAll(ingredientAmountsDto, ingredientAmountDto1, ingredientAmountDto2);

        beerTypeDto1 = new BeerTypeDto(1, "Lager", 30, Collections.emptyList(), false);
        beerTypeDto2 = new BeerTypeDto(2, "IPA", 30, ingredientAmountsDto, false);

        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    @WithMockUser
    void test_getAllBeerTypes() throws Exception {
        // Arrange
        when(beerTypeService.getAllBeerTypes()).thenReturn(Arrays.asList(beerTypeDto1, beerTypeDto2));

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/beertypes")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("beerTypeId")));
    }

    @Test
    @WithMockUser
    void test_getBeerTypeById_whenBeerTypeExists_returnBeerType() throws Exception {
        // Arrange
        when(beerTypeService.getBeerTypeById(1)).thenReturn(Optional.ofNullable(beerTypeDto1));

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/beertypes/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("beerTypeId")));
    }

    @Test
    @WithMockUser
    void test_getBeerTypeById_whenBeerTypeDoesntExist_returnException() throws Exception {
        // Arrange
        when(beerTypeService.getBeerTypeById(1)).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/beertypes/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isNotFound())
                .andExpect(status().reason(containsString("konnte nicht gefunden werden")));
    }


    @Test
    @WithMockUser
    void test_createBeerType() throws Exception {
        // Arrange
        when(beerTypeService.createBeerType(any())).thenReturn(beerTypeDto1);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/beertypes/")
                        .content(objectWriter.writeValueAsString(beerTypeDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("beerTypeId")));
    }

    @Test
    @WithMockUser
    void test_updateBeerType() throws Exception {
        // Arrange
        when(beerTypeService.updateBeerType(anyInt(), any())).thenReturn(beerTypeDto1);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.put("/beertypes/1")
                        .content(objectWriter.writeValueAsString(beerTypeDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("beerTypeId")));
    }

    @Test
    @WithMockUser
    void test_deleteBeerType_whenBeerTypeExists_delete() throws Exception {
        // Arrange
        when(beerTypeService.deleteBeerType(anyInt())).thenReturn(1);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/beertypes/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));
    }

    @Test
    @WithMockUser
    void test_deleteBeerType_whenBeerTypeNotExists_return() throws Exception {
        // Arrange
        when(beerTypeService.deleteBeerType(anyInt())).thenReturn(0);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/beertypes/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("0")));
    }
}