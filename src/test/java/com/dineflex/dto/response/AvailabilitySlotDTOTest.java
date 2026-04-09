package com.dineflex.dto.response;

import com.dineflex.entity.AvailabilitySlot;
import com.dineflex.entity.Offer;
import com.dineflex.entity.Restaurant;
import com.dineflex.entity.enums.OfferType;
import com.dineflex.entity.enums.SlotType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class AvailabilitySlotDTOTest {

    private Restaurant restaurant;
    private Offer offer;
    private AvailabilitySlot slotWithOffer;
    private AvailabilitySlot slotWithoutOffer;

    @BeforeEach
    void setUp() {
        restaurant = Restaurant.builder()
                .id(1L).name("Test Restaurant")
                .hasEarlyBird(true).hasLastMinute(false).build();

        offer = Offer.builder()
                .id(1L).title("Early Bird")
                .discount("20%").type(OfferType.EARLY_BIRD)
                .restaurant(restaurant).build();

        slotWithOffer = AvailabilitySlot.builder()
                .id(1L)
                .date(LocalDate.of(2026, 6, 1))
                .time(LocalTime.of(17, 0))
                .type(SlotType.EARLY_BIRD)
                .isAvailable(true)
                .restaurant(restaurant)
                .offer(offer)
                .build();

        slotWithoutOffer = AvailabilitySlot.builder()
                .id(2L)
                .date(LocalDate.of(2026, 6, 1))
                .time(LocalTime.of(18, 0))
                .type(SlotType.REGULAR)
                .isAvailable(true)
                .restaurant(restaurant)
                .build();
    }

    @Test
    void fromEntity_withOffer_shouldMapAllFields() {
        AvailabilitySlotDTO dto = AvailabilitySlotDTO.fromEntity(slotWithOffer);

        assertThat(dto.getTime()).isEqualTo("17:00");
        assertThat(dto.getType()).isEqualTo("EARLY_BIRD");
        assertThat(dto.getOfferId()).isEqualTo("1");
        assertThat(dto.getDiscount()).isEqualTo("20%");
    }

    @Test
    void fromEntity_withoutOffer_shouldHaveNullOfferFields() {
        AvailabilitySlotDTO dto = AvailabilitySlotDTO.fromEntity(slotWithoutOffer);

        assertThat(dto.getTime()).isEqualTo("18:00");
        assertThat(dto.getType()).isEqualTo("REGULAR");
        assertThat(dto.getOfferId()).isNull();
        assertThat(dto.getDiscount()).isNull();
    }

    @Test
    void builder_shouldSetAllFields() {
        AvailabilitySlotDTO dto = AvailabilitySlotDTO.builder()
                .time("18:00")
                .type("REGULAR")
                .offerId("1")
                .discount("10%")
                .build();

        assertThat(dto.getTime()).isEqualTo("18:00");
        assertThat(dto.getType()).isEqualTo("REGULAR");
        assertThat(dto.getOfferId()).isEqualTo("1");
        assertThat(dto.getDiscount()).isEqualTo("10%");
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyObject() {
        AvailabilitySlotDTO dto = new AvailabilitySlotDTO();
        assertThat(dto).isNotNull();
        assertThat(dto.getTime()).isNull();
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        AvailabilitySlotDTO dto = new AvailabilitySlotDTO("18:00", "REGULAR", "1", "10%");

        assertThat(dto.getTime()).isEqualTo("18:00");
        assertThat(dto.getType()).isEqualTo("REGULAR");
        assertThat(dto.getOfferId()).isEqualTo("1");
        assertThat(dto.getDiscount()).isEqualTo("10%");
    }

    @Test
    void setter_shouldUpdateFields() {
        AvailabilitySlotDTO dto = new AvailabilitySlotDTO();
        dto.setTime("19:00");
        dto.setType("LAST_MINUTE");

        assertThat(dto.getTime()).isEqualTo("19:00");
        assertThat(dto.getType()).isEqualTo("LAST_MINUTE");
    }
}