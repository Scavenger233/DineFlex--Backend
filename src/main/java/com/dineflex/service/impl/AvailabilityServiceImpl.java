package com.dineflex.service.impl;

import com.dineflex.dto.response.AvailabilitySlotDTO;
import com.dineflex.entity.AvailabilitySlot;
import com.dineflex.repository.AvailabilitySlotRepository;
import com.dineflex.repository.RestaurantRepository;
import com.dineflex.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public List<AvailabilitySlotDTO> getAvailability(Long restaurantId, LocalDate date) {
        List<AvailabilitySlot> slots = availabilitySlotRepository
                .findByRestaurantIdAndDate(restaurantId, date);

        return slots.stream()
                .filter(AvailabilitySlot::isAvailable)
                .map(AvailabilitySlotDTO::fromEntity)
                .collect(Collectors.toList());
    }
}