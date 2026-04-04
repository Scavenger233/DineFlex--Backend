package com.dineflex.service;

import com.dineflex.dto.request.CustomerLoginRequest;
import com.dineflex.dto.request.CustomerRegisterRequest;
import com.dineflex.dto.response.CustomerInfoResponse;

public interface CustomerService {
    CustomerInfoResponse register(CustomerRegisterRequest request);
    CustomerInfoResponse login(CustomerLoginRequest request);
}