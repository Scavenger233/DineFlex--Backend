package com.dineflex.controller;

import com.dineflex.dto.request.LoginRequest;
import com.dineflex.dto.request.LoginRequest;
import com.dineflex.entity.Customer;
import com.dineflex.repository.CustomerRepository;
import com.dineflex.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        // Search for customers
        Customer customer = customerRepository.findByCustomerEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Verifying passwords
        if (passwordEncoder.matches(loginRequest.getPassword(), customer.getPasswordHash())) {
            return jwtUtil.generateToken(customer.getCustomerEmail());
        } else {
            throw new RuntimeException("Invalid password");
        }
    }
}