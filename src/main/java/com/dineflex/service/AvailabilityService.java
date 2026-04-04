package com.dineflex.service;

import com.dineflex.dto.response.AvailabilitySlotDTO;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {
    List<AvailabilitySlotDTO> getAvailability(Long restaurantId, LocalDate date);
}