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
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        Customer customer = customerRepository.findByCustomerEmail(request.getCustomerEmail())
                .orElseGet(() -> {
                    Customer newCustomer = Customer.builder()
                            .customerName(request.getCustomerName())
                            .customerEmail(request.getCustomerEmail())
                            .phone(request.getCustomerPhone())
                            .build();
                    return customerRepository.save(newCustomer);
                });

        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime time = LocalTime.parse(request.getTime());


        AvailabilitySlot slot = availabilitySlotRepository
                .findByRestaurantIdAndDateAndTime(request.getRestaurantId(), date, time)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        if (!slot.isAvailable()) {
            throw new IllegalStateException("Slot already booked");
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

        return BookingResponse.fromEntity(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        return BookingResponse.fromEntity(booking);
    }
}