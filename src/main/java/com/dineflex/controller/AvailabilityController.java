package com.dineflex.controller;

import com.dineflex.dto.response.AvailabilitySlotDTO;
import com.dineflex.dto.response.RestaurantAvailabilityResponse;
import com.dineflex.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @GetMapping("/{restaurantId}/availability")
    public RestaurantAvailabilityResponse getAvailability(
            @PathVariable Long restaurantId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<AvailabilitySlotDTO> slots = availabilityService.getAvailability(restaurantId, date);

        return RestaurantAvailabilityResponse.builder()
                .restaurantId(String.valueOf(restaurantId))
                .date(date.toString())
                .availableSlots(slots)
                .build();
    }
}