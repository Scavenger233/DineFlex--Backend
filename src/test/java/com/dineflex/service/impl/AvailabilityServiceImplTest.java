package com.dineflex.service.impl;

import com.dineflex.dto.response.AvailabilitySlotDTO;
import com.dineflex.entity.AvailabilitySlot;
import com.dineflex.entity.Offer;
import com.dineflex.entity.Restaurant;
import com.dineflex.entity.enums.SlotType;
import com.dineflex.repository.AvailabilitySlotRepository;
import com.dineflex.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceImplTest {

    @Mock
    private AvailabilitySlotRepository availabilitySlotRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private AvailabilityServiceImpl availabilityService;

    private Restaurant restaurant;
    private LocalDate date;
    private AvailabilitySlot availableSlot;
    private AvailabilitySlot unavailableSlot;
    private AvailabilitySlot earlyBirdSlot;

    @BeforeEach
    void setUp() {
        restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .hasEarlyBird(true)
                .hasLastMinute(false)
                .build();

        date = LocalDate.of(2026, 6, 1);

        availableSlot = AvailabilitySlot.builder()
                .id(1L)
                .date(date)
                .time(LocalTime.of(18, 0))
                .type(SlotType.REGULAR)
                .isAvailable(true)
                .restaurant(restaurant)
                .build();

        unavailableSlot = AvailabilitySlot.builder()
                .id(2L)
                .date(date)
                .time(LocalTime.of(19, 0))
                .type(SlotType.REGULAR)
                .isAvailable(false)
                .restaurant(restaurant)
                .build();

        Offer offer = Offer.builder()
                .id(1L)
                .title("Early Bird Special")
                .discount("20%")
                .restaurant(restaurant)
                .build();

        earlyBirdSlot = AvailabilitySlot.builder()
                .id(3L)
                .date(date)
                .time(LocalTime.of(17, 0))
                .type(SlotType.EARLY_BIRD)
                .isAvailable(true)
                .restaurant(restaurant)
                .offer(offer)
                .build();
    }

    // ───── getAvailability() ─────

    @Test
    void getAvailability_shouldReturnOnlyAvailableSlots() {
        when(availabilitySlotRepository.findByRestaurantIdAndDate(1L, date))
                .thenReturn(List.of(availableSlot, unavailableSlot));

        List<AvailabilitySlotDTO> result = availabilityService.getAvailability(1L, date);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTime()).isEqualTo("18:00");
    }

    @Test
    void getAvailability_shouldReturnEmptyList_whenNoSlotsAvailable() {
        when(availabilitySlotRepository.findByRestaurantIdAndDate(1L, date))
                .thenReturn(List.of(unavailableSlot));

        List<AvailabilitySlotDTO> result = availabilityService.getAvailability(1L, date);

        assertThat(result).isEmpty();
    }

    @Test
    void getAvailability_shouldReturnEmptyList_whenNoSlotsExist() {
        when(availabilitySlotRepository.findByRestaurantIdAndDate(1L, date))
                .thenReturn(List.of());

        List<AvailabilitySlotDTO> result = availabilityService.getAvailability(1L, date);

        assertThat(result).isEmpty();
    }

    @Test
    void getAvailability_shouldMapSlotTypeCorrectly() {
        when(availabilitySlotRepository.findByRestaurantIdAndDate(1L, date))
                .thenReturn(List.of(availableSlot));

        List<AvailabilitySlotDTO> result = availabilityService.getAvailability(1L, date);

        assertThat(result.get(0).getType()).isEqualTo("REGULAR");
    }

    @Test
    void getAvailability_shouldIncludeOfferDetails_forEarlyBirdSlot() {
        when(availabilitySlotRepository.findByRestaurantIdAndDate(1L, date))
                .thenReturn(List.of(earlyBirdSlot));

        List<AvailabilitySlotDTO> result = availabilityService.getAvailability(1L, date);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo("EARLY_BIRD");
        assertThat(result.get(0).getDiscount()).isEqualTo("20%");
        assertThat(result.get(0).getOfferId()).isEqualTo("1");
    }

    @Test
    void getAvailability_shouldReturnNullOfferFields_forRegularSlot() {
        when(availabilitySlotRepository.findByRestaurantIdAndDate(1L, date))
                .thenReturn(List.of(availableSlot));

        List<AvailabilitySlotDTO> result = availabilityService.getAvailability(1L, date);

        assertThat(result.get(0).getOfferId()).isNull();
        assertThat(result.get(0).getDiscount()).isNull();
    }

    @Test
    void getAvailability_shouldCallRepositoryWithCorrectParams() {
        when(availabilitySlotRepository.findByRestaurantIdAndDate(1L, date))
                .thenReturn(List.of());

        availabilityService.getAvailability(1L, date);

        verify(availabilitySlotRepository, times(1)).findByRestaurantIdAndDate(1L, date);
    }
}