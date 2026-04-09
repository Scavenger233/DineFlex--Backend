package com.dineflex.dto.response;

import com.dineflex.entity.Booking;
import com.dineflex.entity.Customer;
import com.dineflex.entity.Restaurant;
import com.dineflex.entity.enums.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingResponseTest {

    private Booking booking;

    @BeforeEach
    void setUp() {
        Restaurant restaurant = Restaurant.builder()
                .id(1L).name("Test Restaurant")
                .hasEarlyBird(false).hasLastMinute(false).build();

        Customer customer = Customer.builder()
                .id(1L).customerName("Test User")
                .customerEmail("test@example.com")
                .passwordHash("hashed").build();

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
    }

    @Test
    void fromEntity_shouldMapAllFields() {
        BookingResponse response = BookingResponse.fromEntity(booking);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(response.getRestaurantId()).isEqualTo(1L);
        assertThat(response.getRestaurantName()).isEqualTo("Test Restaurant");
        assertThat(response.getDate()).isEqualTo("2026-06-01");
        assertThat(response.getTime()).isEqualTo("18:00");
        assertThat(response.getPartySize()).isEqualTo(2);
        assertThat(response.getCustomerName()).isEqualTo("Test User");
        assertThat(response.getConfirmationCode()).isEqualTo("DINE123456");
    }

    @Test
    void builder_shouldSetAllFields() {
        BookingResponse response = BookingResponse.builder()
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

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(response.getConfirmationCode()).isEqualTo("DINE123456");
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyObject() {
        BookingResponse response = new BookingResponse();
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNull();
    }

    @Test
    void setter_shouldUpdateFields() {
        BookingResponse response = new BookingResponse();
        response.setId(1L);
        response.setStatus(BookingStatus.CANCELLED);
        response.setConfirmationCode("DINE999");

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(BookingStatus.CANCELLED);
        assertThat(response.getConfirmationCode()).isEqualTo("DINE999");
    }
}