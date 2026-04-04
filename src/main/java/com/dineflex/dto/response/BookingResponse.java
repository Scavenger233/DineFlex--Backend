package com.dineflex.dto.response;

import com.dineflex.entity.Booking;
import com.dineflex.entity.enums.BookingStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long id;
    private BookingStatus status;
    private Long restaurantId;
    private String restaurantName;
    private String date;
    private String time;
    private int partySize;
    private String customerName;
    private String confirmationCode;

    public static BookingResponse fromEntity(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .restaurantId(booking.getRestaurant().getId())
                .restaurantName(booking.getRestaurant().getName())
                .date(booking.getDate().toString())    // ✅ Converts to "YYYY-MM-DD"
                .time(booking.getTime().toString())    // ✅ Converts to "HH:mm"
                .partySize(booking.getPartySize())
                .customerName(booking.getCustomer().getCustomerName())
                .confirmationCode(booking.getConfirmationCode())
                .build();
    }

}