package com.dineflex.service;

import com.dineflex.dto.request.BookingRequest;
import com.dineflex.dto.response.BookingResponse;
import com.dineflex.entity.Booking;

public interface BookingService {
    Booking createBooking(BookingRequest request);
    BookingResponse getBookingById(Long id);
}
