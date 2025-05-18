package com.dineflex.service.impl;

import com.dineflex.dto.request.BookingRequest;
import com.dineflex.dto.response.BookingResponse;
import com.dineflex.entity.*;
import com.dineflex.entity.enums.BookingStatus;
import com.dineflex.entity.enums.BookingStatus;
import com.dineflex.repository.*;
import com.dineflex.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;


    @Override
    public BookingResponse createBooking(BookingRequest request) {
        System.out.println("📨 [Booking Start] Email from frontend: " + request.getCustomerEmail());

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("❌ Restaurant not found: ID " + request.getRestaurantId()));

        System.out.println("✅ Found restaurant: " + restaurant.getName());

        System.out.println("🔍 Checking if customer exists in DB for email: " + request.getCustomerEmail());
        Customer customer = customerRepository.findByCustomerEmail(request.getCustomerEmail())
                .orElseThrow(() -> new IllegalArgumentException("❌ Customer not found. Please log in first."));

        System.out.println("✅ Customer found: ID=" + customer.getId() + ", Name=" + customer.getCustomerName());

        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime time = LocalTime.parse(request.getTime());

        System.out.println("🔍 Checking slot for date=" + date + " and time=" + time);

        AvailabilitySlot slot = availabilitySlotRepository
                .findByRestaurantIdAndDateAndTime(request.getRestaurantId(), date, time)
                .orElseThrow(() -> new IllegalArgumentException("❌ Slot not found for restaurant ID=" + request.getRestaurantId() + ", date=" + date + ", time=" + time));

        if (!slot.isAvailable()) {
            throw new IllegalStateException("❌ Slot already booked at " + time);
        }

        System.out.println("✅ Slot available. Proceeding with booking...");

        slot.setAvailable(false);
        availabilitySlotRepository.save(slot);

        Booking booking = Booking.builder()
                .restaurant(restaurant)
                .customer(customer)
                .availableSlot(slot)
                .date(date)
                .time(time)
                .partySize(request.getPartySize())
                .status(BookingStatus.CONFIRMED)
                .confirmationCode("DINE" + System.currentTimeMillis())
                .build();

        Booking saved = bookingRepository.save(booking);

        System.out.println("✅ Booking successful! Booking ID=" + saved.getId() + ", Confirmation Code=" + saved.getConfirmationCode());

        return BookingResponse.fromEntity(saved);
    }


    @Override
    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        return BookingResponse.fromEntity(booking);
    }

}