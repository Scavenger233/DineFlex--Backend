package com.dineflex.controller;

import com.dineflex.dto.response.RestaurantDetailResponse;
import com.dineflex.dto.response.RestaurantListResponse;
import com.dineflex.security.JwtAuthenticationFilter;
import com.dineflex.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = RestaurantController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ClientAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@WithMockUser
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @Test
    void getAllRestaurants_shouldReturn200_withList() throws Exception {
        RestaurantListResponse response = RestaurantListResponse.builder()
                .id(1L)
                .name("Test Restaurant")
                .location("Dublin")
                .cuisine("Irish")
                .hasEarlyBird(true)
                .hasLastMinute(false)
                .build();

        when(restaurantService.getAllRestaurants()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Restaurant"))
                .andExpect(jsonPath("$[0].location").value("Dublin"))
                .andExpect(jsonPath("$[0].cuisine").value("Irish"));
    }

    @Test
    void getAllRestaurants_shouldReturnEmptyList_whenNoRestaurants() throws Exception {
        when(restaurantService.getAllRestaurants()).thenReturn(List.of());

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getRestaurantById_shouldReturn200_whenFound() throws Exception {
        RestaurantDetailResponse response = RestaurantDetailResponse.builder()
                .id(1L)
                .name("Test Restaurant")
                .cuisine("Irish")
                .address("1 Main St")
                .phone("+353 1 234 5678")
                .hasEarlyBird(true)
                .hasLastMinute(false)
                .build();

        when(restaurantService.getRestaurantById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.cuisine").value("Irish"))
                .andExpect(jsonPath("$.address").value("1 Main St"));
    }

    @Test
    void getRestaurantById_shouldReturn500_whenNotFound() throws Exception {
        when(restaurantService.getRestaurantById(99L))
                .thenThrow(new RuntimeException("Restaurant not found"));

        mockMvc.perform(get("/api/restaurants/99"))
                .andExpect(status().isInternalServerError());
    }
}