package com.dineflex.controller;

import com.dineflex.dto.request.CustomerLoginRequest;
import com.dineflex.dto.request.CustomerRegisterRequest;
import com.dineflex.dto.response.CustomerInfoResponse;
import com.dineflex.entity.Customer;
import com.dineflex.repository.CustomerRepository;
import com.dineflex.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"})
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;

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

    @GetMapping("/me")
    public ResponseEntity<Customer> getCurrentCustomer(Authentication authentication) {
        String email = authentication.getName();
        return customerRepository.findByCustomerEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}