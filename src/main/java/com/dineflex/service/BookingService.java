package com.dineflex.service;

import com.dineflex.dto.request.BookingRequest;
import com.dineflex.dto.response.BookingResponse;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);
    BookingResponse getBookingById(Long id);
}
