package com.dineflex.controller;

import com.dineflex.dto.request.CustomerLoginRequest;
import com.dineflex.dto.request.CustomerRegisterRequest;
import com.dineflex.dto.response.CustomerInfoResponse;
import com.dineflex.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public CustomerInfoResponse register(@RequestBody CustomerRegisterRequest request) {
        return customerService.register(request);
    }

    @PostMapping("/login")
    public CustomerInfoResponse login(@RequestBody CustomerLoginRequest request) {
        return customerService.login(request);
    }
}