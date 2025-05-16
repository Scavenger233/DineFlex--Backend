package com.dineflex.dto.response;

import com.dineflex.entity.Customer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerInfoResponse {
    private Long id;
    private String customerName;
    private String customerEmail;
    private String phone;

    public static CustomerInfoResponse fromEntity(Customer customer) {
        return CustomerInfoResponse.builder()
                .id(customer.getId())
                .customerName(customer.getCustomerName())
                .customerEmail(customer.getCustomerEmail())
                .phone(customer.getPhone())
                .build();
    }
}