package com.dineflex.controller;

import com.dineflex.dto.request.BookingRequest;
import com.dineflex.dto.response.BookingResponse;
import com.dineflex.entity.*;
import com.dineflex.entity.enums.BookingStatus;
import com.dineflex.security.JwtAuthenticationFilter;
import com.dineflex.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = BookingController.class,
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
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookingService bookingService;

    private Booking booking;
    private BookingResponse bookingResponse;

    @BeforeEach
    void setUp() {
        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .hasEarlyBird(false)
                .hasLastMinute(false)
                .build();

        Customer customer = Customer.builder()
                .id(1L)
                .customerName("Test User")
                .customerEmail("test@example.com")
                .phone("+353 87 123 4567")
                .passwordHash("hashed")
                .build();

        booking = Booking.builder()
                .id(1L)
                .restaurant(restaurant)
                .customer(customer)
                .date(LocalDate.of(2026, 6, 1))
                .time(LocalTime.of(18, 0))
                .partySize(2)
                .status(BookingStatus.CONFIRMED)
                .confirmationCode("DINE123456")
                .build();

        bookingResponse = BookingResponse.builder()
                .id(1L)
                .status(BookingStatus.CONFIRMED)
                .restaurantId(1L)
                .restaurantName("Test Restaurant")
                .date("2026-06-01")
                .time("18:00")
                .partySize(2)
                .customerName("Test User")
                .confirmationCode("DINE123456")
                .build();
    }

    @Test
    void createBooking_shouldReturn200_whenValidRequest() throws Exception {
        BookingRequest request = BookingRequest.builder()
                .restaurantId(1L)
                .customerEmail("test@example.com")
                .date("2026-06-01")
                .time("18:00")
                .partySize(2)
                .build();

        when(bookingService.createBooking(any())).thenReturn(booking);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.confirmationCode").value("DINE123456"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.partySize").value(2));
    }

    @Test
    void createBooking_shouldReturn500_whenServiceThrows() throws Exception {
        BookingRequest request = BookingRequest.builder()
                .restaurantId(99L)
                .customerEmail("test@example.com")
                .date("2026-06-01")
                .time("18:00")
                .partySize(2)
                .build();

        when(bookingService.createBooking(any()))
                .thenThrow(new RuntimeException("Restaurant not found"));

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getBookingById_shouldReturn200_whenFound() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(bookingResponse);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.confirmationCode").value("DINE123456"))
                .andExpect(jsonPath("$.restaurantName").value("Test Restaurant"));
    }

    @Test
    void getBookingById_shouldReturn500_whenNotFound() throws Exception {
        when(bookingService.getBookingById(99L))
                .thenThrow(new IllegalArgumentException("Booking not found"));

        mockMvc.perform(get("/api/bookings/99"))
                .andExpect(status().isInternalServerError());
    }
}