package com.dineflex.dto.request;

import lombok.Data;

@Data
public class CustomerRegisterRequest {
    private String customerName;
    private String customerEmail;
    private String phone;
    private String password;
}