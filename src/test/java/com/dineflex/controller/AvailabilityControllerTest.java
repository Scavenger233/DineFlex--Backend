package com.dineflex.controller;

import com.dineflex.dto.response.AvailabilitySlotDTO;
import com.dineflex.security.JwtAuthenticationFilter;
import com.dineflex.service.AvailabilityService;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AvailabilityController.class,
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
class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AvailabilityService availabilityService;

    @Test
    void getAvailability_shouldReturn200_withSlots() throws Exception {
        AvailabilitySlotDTO slot = AvailabilitySlotDTO.builder()
                .time("18:00")
                .type("REGULAR")
                .build();

        when(availabilityService.getAvailability(1L, LocalDate.of(2026, 6, 1)))
                .thenReturn(List.of(slot));

        mockMvc.perform(get("/api/restaurants/1/availability")
                        .param("date", "2026-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurantId").value("1"))
                .andExpect(jsonPath("$.date").value("2026-06-01"))
                .andExpect(jsonPath("$.availableSlots[0].time").value("18:00"))
                .andExpect(jsonPath("$.availableSlots[0].type").value("REGULAR"));
    }

    @Test
    void getAvailability_shouldReturnEmptySlots_whenNoneAvailable() throws Exception {
        when(availabilityService.getAvailability(1L, LocalDate.of(2026, 6, 1)))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/restaurants/1/availability")
                        .param("date", "2026-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableSlots").isEmpty());
    }

    @Test
    void getAvailability_shouldReturn400_whenDateParamMissing() throws Exception {
        mockMvc.perform(get("/api/restaurants/1/availability"))
                .andExpect(status().isInternalServerError());
    }
}