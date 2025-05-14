package com.dineflex.repository;

import com.dineflex.entity.AvailabilitySlot;
import com.dineflex.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByRestaurantIdAndDate(Long restaurantId, LocalDate date);
}