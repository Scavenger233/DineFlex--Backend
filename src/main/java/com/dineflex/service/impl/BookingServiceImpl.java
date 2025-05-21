package com.dineflex.service.impl;

import com.dineflex.dto.request.BookingRequest;
import com.dineflex.dto.response.BookingResponse;
import com.dineflex.entity.*;
import com.dineflex.entity.enums.BookingStatus;
import com.dineflex.entity.enums.BookingStatus;
import com.dineflex.exception.UserNotFoundException;
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
    public Booking createBooking(BookingRequest request) {
        System.out.println("📨 [Booking Start] Email from frontend: " + request.getCustomerEmail());

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        System.out.println("✅ Found restaurant: " + restaurant.getName());

        System.out.println("🔍 Checking if customer exists in DB for email: " + request.getCustomerEmail());

        Customer customer = customerRepository.findByCustomerEmail(request.getCustomerEmail())
                .orElseThrow(() -> new UserNotFoundException("Customer not found"));

        System.out.println("✅ Customer found: ID=" + customer.getId() + ", Name=" + customer.getCustomerName());

        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime time = LocalTime.parse(request.getTime());

        System.out.println("🔍 Checking slot for date=" + date + " and time=" + time);

        AvailabilitySlot slot = availabilitySlotRepository
                .findByRestaurantIdAndDateAndTime(request.getRestaurantId(), date, time)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (!slot.isAvailable()) {
            throw new RuntimeException("Slot not available");
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


        return bookingRepository.save(booking);
    }


    @Override
    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        return BookingResponse.fromEntity(booking);
    }

}