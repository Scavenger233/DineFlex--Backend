package com.dineflex.service.impl;

import com.dineflex.dto.request.BookingRequest;
import com.dineflex.dto.response.BookingResponse;
import com.dineflex.entity.*;
import com.dineflex.entity.enums.BookingStatus;
import com.dineflex.exception.UserNotFoundException;
import com.dineflex.repository.*;
import com.dineflex.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

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

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));



        Customer customer = customerRepository.findByCustomerEmail(request.getCustomerEmail())
                .orElseThrow(() -> new UserNotFoundException("Customer not found"));


        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime time = LocalTime.parse(request.getTime());


        AvailabilitySlot slot = availabilitySlotRepository
                .findByRestaurantIdAndDateAndTime(request.getRestaurantId(), date, time)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (!slot.isAvailable()) {
            throw new RuntimeException("Slot not available");
        }


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
    public BookingResponse getBookingById(Long bookingId, String customerEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getCustomer().getCustomerEmail().equals(customerEmail)) {
            throw new AccessDeniedException("This booking does not belong to the customer");
        }

        return BookingResponse.fromEntity(booking);
    }

}
