package com.dineflex.dto.response;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantAvailabilityResponseTest {

    @Test
    void builder_shouldSetAllFields() {
        AvailabilitySlotDTO slot = AvailabilitySlotDTO.builder()
                .time("18:00")
                .type("REGULAR")
                .build();

        RestaurantAvailabilityResponse response = RestaurantAvailabilityResponse.builder()
                .restaurantId("1")
                .date("2026-06-01")
                .availableSlots(List.of(slot))
                .build();

        assertThat(response.getRestaurantId()).isEqualTo("1");
        assertThat(response.getDate()).isEqualTo("2026-06-01");
        assertThat(response.getAvailableSlots()).hasSize(1);
        assertThat(response.getAvailableSlots().get(0).getTime()).isEqualTo("18:00");
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyObject() {
        RestaurantAvailabilityResponse response = new RestaurantAvailabilityResponse();
        assertThat(response).isNotNull();
        assertThat(response.getRestaurantId()).isNull();
        assertThat(response.getAvailableSlots()).isNull();
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        AvailabilitySlotDTO slot = AvailabilitySlotDTO.builder()
                .time("18:00").type("REGULAR").build();

        RestaurantAvailabilityResponse response = new RestaurantAvailabilityResponse(
                "1", "2026-06-01", List.of(slot));

        assertThat(response.getRestaurantId()).isEqualTo("1");
        assertThat(response.getDate()).isEqualTo("2026-06-01");
        assertThat(response.getAvailableSlots()).hasSize(1);
    }

    @Test
    void setter_shouldUpdateFields() {
        RestaurantAvailabilityResponse response = new RestaurantAvailabilityResponse();
        response.setRestaurantId("2");
        response.setDate("2026-07-01");
        response.setAvailableSlots(List.of());

        assertThat(response.getRestaurantId()).isEqualTo("2");
        assertThat(response.getDate()).isEqualTo("2026-07-01");
        assertThat(response.getAvailableSlots()).isEmpty();
    }
}