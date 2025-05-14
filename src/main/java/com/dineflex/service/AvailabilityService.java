package com.dineflex.service;

import com.dineflex.dto.response.AvailabilitySlotDTO;
import com.dineflex.entity.AvailabilitySlot;
import com.dineflex.entity.Restaurant;
import com.dineflex.repository.AvailabilitySlotRepository;
import com.dineflex.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final RestaurantRepository restaurantRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    public List<AvailabilitySlotDTO> getAvailability(Long restaurantId, LocalDate date) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        // To fix date and availability not matching problem
        List<AvailabilitySlot> slots = availabilitySlotRepository.findByRestaurantIdAndDate(restaurantId, date);

        return slots.stream()
                .filter(AvailabilitySlot::isAvailable)
                .map(AvailabilitySlotDTO::fromEntity)
                .collect(Collectors.toList());
    }
}