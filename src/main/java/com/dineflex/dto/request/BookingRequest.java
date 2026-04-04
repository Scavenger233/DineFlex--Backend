package com.dineflex.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {
    private Long restaurantId;
    @NotBlank
    private String date; // Format: yyyy-MM-dd
    @NotBlank
    private String time; // Format: HH:mm
    private int partySize;
    private String customerName;
    @NotBlank
    private String customerEmail;
    private String customerPhone;
}