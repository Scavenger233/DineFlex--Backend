package com.dineflex.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {
    private Long restaurantId;
    private String date; // Format: yyyy-MM-dd
    private String time; // Format: HH:mm
    private int partySize;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
}