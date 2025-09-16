package ch.schuum.backend.controller;

import ch.schuum.backend.dto.BrewableDto;
import ch.schuum.backend.dto.IngredientAmountDto;
import ch.schuum.backend.dto.IngredientDto;
import ch.schuum.backend.service.CanBrewService;
import ch.schuum.backend.service.TankService;
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

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CanBrewController.class)
@AutoConfigureMockMvc(addFilters = false)
class CanBrewControllerTest {

    @MockBean
    CanBrewService canBrewService;

    @Autowired
    MockMvc mockMvc;

    List<BrewableDto> brewableList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        IngredientDto ingredientDto = new IngredientDto(1, "Hopfen", 356, 50, false);
        IngredientAmountDto ingredientAmountDto = new IngredientAmountDto(ingredientDto, 32);

        BrewableDto brewableDto = new BrewableDto("Lager", true, Collections.emptyList());
        BrewableDto brewableDto2 = new BrewableDto("Weizen", false, Collections.singletonList(ingredientAmountDto));
        Collections.addAll(brewableList, brewableDto, brewableDto2);
    }

    @Test
    @WithMockUser
    void test_getBrewableBeerTypes() throws Exception {
        // Arrange
        when(canBrewService.getBrewableBeerTypes()).thenReturn(brewableList);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/canbrew")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brewable")));
    }
}