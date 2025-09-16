package ch.schuum.backend.controller;

import ch.schuum.backend.dto.TankDto;
import ch.schuum.backend.service.TankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TankController.class)
class AuthTest {
    @MockBean
    TankService tankService;

    @Autowired
    MockMvc mockMvc;

    TankDto tankDto1;

    @BeforeEach
    void setUp() {
        tankDto1 = new TankDto(1, 50, null);
    }

    @Test
    @WithMockUser
    void test_whenAccessEndpointWithAuthentication_returnOk() throws Exception {
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
    void test_whenAccessEndpointWithoutAuthentication_returnNotAllowed() throws Exception {
        // Arrange
        when(tankService.getTankById(1)).thenReturn(Optional.ofNullable(tankDto1));

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/tanks/1")
                        .accept(MediaType.ALL))
                // Assert
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(containsString("Unauthorized")));
    }
}
