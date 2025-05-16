package com.dineflex.dto.request;

import lombok.Data;

@Data
public class CustomerLoginRequest {
    private String customerEmail;
    private String password;
}