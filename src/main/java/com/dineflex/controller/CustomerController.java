package com.dineflex.controller;

import com.dineflex.dto.request.CustomerLoginRequest;
import com.dineflex.dto.request.CustomerRegisterRequest;
import com.dineflex.dto.response.CustomerInfoResponse;
import com.dineflex.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"})
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<CustomerInfoResponse> register(@RequestBody CustomerRegisterRequest request) {
        CustomerInfoResponse response = customerService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomerInfoResponse> login(@RequestBody CustomerLoginRequest request) {
        CustomerInfoResponse response = customerService.login(request);
        return ResponseEntity.ok(response);
    }

}