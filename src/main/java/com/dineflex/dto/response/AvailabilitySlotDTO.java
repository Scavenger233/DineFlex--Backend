package com.dineflex.dto.response;

import com.dineflex.entity.AvailabilitySlot;
import com.dineflex.entity.enums.SlotType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilitySlotDTO {
    private String time;         // "17:30"
    private String type;       // EARLY_BIRD, REGULAR, LAST_MINUTE
    private String offerId;      // may be null
    private String discount;     // e.g. "20%", may be null

    public static AvailabilitySlotDTO fromEntity(AvailabilitySlot slot) {
        return AvailabilitySlotDTO.builder()
                .time(slot.getTime().toString())
                .type(slot.getType().name()) // Get string
                .offerId(slot.getOffer() != null ? String.valueOf(slot.getOffer().getId()) : null)
                .discount(slot.getOffer() != null ? slot.getOffer().getDiscount() : null)
                .build();
    }
}