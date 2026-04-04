package com.dineflex.controller;

import com.dineflex.dto.request.LoginRequest;
import com.dineflex.dto.request.LoginRequest;
import com.dineflex.dto.response.LoginResponse;
import com.dineflex.entity.Customer;
import com.dineflex.repository.CustomerRepository;
import com.dineflex.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.dineflex.exception.InvalidCredentialsException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        // Search for customers
        Customer customer = customerRepository.findByCustomerEmail(loginRequest.getCustomerEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Verifying passwords
        if (passwordEncoder.matches(loginRequest.getPassword(), customer.getPasswordHash())) {
            String token = jwtUtil.generateToken(customer.getCustomerEmail());

            return LoginResponse.builder()
                    .token(token)
                    .customerName(customer.getCustomerName())
                    .customerEmail(customer.getCustomerEmail())
                    .build();
        } else {
            throw new InvalidCredentialsException("Email or password is incorrect.");
        }
    }

    @GetMapping("/api/customers/me")
    public ResponseEntity<Customer> getCurrentCustomer(Authentication authentication) {
        String email = authentication.getName(); // comes from the JWT's subject
        return customerRepository.findByCustomerEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}