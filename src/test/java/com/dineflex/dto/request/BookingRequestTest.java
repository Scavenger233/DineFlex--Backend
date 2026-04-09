package com.dineflex.dto.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookingRequestTest {

    @Test
    void builder_shouldSetAllFields() {
        BookingRequest request = BookingRequest.builder()
                .restaurantId(1L)
                .date("2026-06-01")
                .time("18:00")
                .partySize(2)
                .customerName("Test User")
                .customerEmail("test@example.com")
                .customerPhone("+353 87 123 4567")
                .build();

        assertThat(request.getRestaurantId()).isEqualTo(1L);
        assertThat(request.getDate()).isEqualTo("2026-06-01");
        assertThat(request.getTime()).isEqualTo("18:00");
        assertThat(request.getPartySize()).isEqualTo(2);
        assertThat(request.getCustomerName()).isEqualTo("Test User");
        assertThat(request.getCustomerEmail()).isEqualTo("test@example.com");
        assertThat(request.getCustomerPhone()).isEqualTo("+353 87 123 4567");
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyObject() {
        BookingRequest request = new BookingRequest();
        assertThat(request).isNotNull();
        assertThat(request.getRestaurantId()).isNull();
        assertThat(request.getCustomerEmail()).isNull();
        assertThat(request.getPartySize()).isEqualTo(0);
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        BookingRequest request = new BookingRequest(
                1L, "2026-06-01", "18:00", 2,
                "Test User", "test@example.com", "+353 87 123 4567");

        assertThat(request.getRestaurantId()).isEqualTo(1L);
        assertThat(request.getDate()).isEqualTo("2026-06-01");
        assertThat(request.getPartySize()).isEqualTo(2);
        assertThat(request.getCustomerEmail()).isEqualTo("test@example.com");
    }

    @Test
    void setter_shouldUpdateFields() {
        BookingRequest request = new BookingRequest();
        request.setRestaurantId(1L);
        request.setDate("2026-06-01");
        request.setTime("18:00");
        request.setPartySize(4);
        request.setCustomerName("Test User");
        request.setCustomerEmail("test@example.com");
        request.setCustomerPhone("+353 87 123 4567");

        assertThat(request.getRestaurantId()).isEqualTo(1L);
        assertThat(request.getDate()).isEqualTo("2026-06-01");
        assertThat(request.getPartySize()).isEqualTo(4);
        assertThat(request.getCustomerEmail()).isEqualTo("test@example.com");
    }

    @Test
    void equals_shouldWorkCorrectly() {
        BookingRequest r1 = BookingRequest.builder()
                .restaurantId(1L)
                .customerEmail("test@example.com")
                .partySize(2)
                .build();

        BookingRequest r2 = BookingRequest.builder()
                .restaurantId(1L)
                .customerEmail("test@example.com")
                .partySize(2)
                .build();

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    void toString_shouldContainFields() {
        BookingRequest request = BookingRequest.builder()
                .restaurantId(1L)
                .customerEmail("test@example.com")
                .partySize(2)
                .build();

        assertThat(request.toString()).contains("test@example.com");
    }
}