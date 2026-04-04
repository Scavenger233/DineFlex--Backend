package com.dineflex.controller;

import com.dineflex.dto.request.BookingRequest;
import com.dineflex.dto.response.BookingResponse;
import com.dineflex.entity.Booking;
import com.dineflex.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dineflex.entity.Booking;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(request);
        BookingResponse response = BookingResponse.fromEntity(booking);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }
}