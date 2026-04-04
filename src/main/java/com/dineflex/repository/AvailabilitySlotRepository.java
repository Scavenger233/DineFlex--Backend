package com.dineflex.repository;

import com.dineflex.entity.AvailabilitySlot;
import com.dineflex.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByRestaurantIdAndDate(Long restaurantId, LocalDate date);

    Optional<AvailabilitySlot> findByRestaurantIdAndDateAndTime(Long restaurantId, LocalDate date, LocalTime time);

}