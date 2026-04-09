package com.dineflex.service;

import com.dineflex.dto.request.CustomerLoginRequest;
import com.dineflex.dto.request.CustomerRegisterRequest;
import com.dineflex.dto.request.LoginRequest;
import com.dineflex.dto.response.CustomerInfoResponse;
import com.dineflex.dto.response.LoginResponse;

public interface CustomerService {
    CustomerInfoResponse register(CustomerRegisterRequest request);
    CustomerInfoResponse getMe(String email);
    LoginResponse login(LoginRequest request);
}