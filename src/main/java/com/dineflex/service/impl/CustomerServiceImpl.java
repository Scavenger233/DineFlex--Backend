package com.dineflex.service.impl;

import com.dineflex.dto.request.CustomerRegisterRequest;
import com.dineflex.dto.request.LoginRequest;
import com.dineflex.dto.response.CustomerInfoResponse;
import com.dineflex.dto.response.LoginResponse;
import com.dineflex.entity.Customer;
import com.dineflex.exception.UserNotFoundException;
import com.dineflex.repository.CustomerRepository;
import com.dineflex.security.JwtUtil;
import com.dineflex.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.dineflex.exception.InvalidCredentialsException;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public CustomerInfoResponse register(CustomerRegisterRequest request) {
        Customer customer = Customer.builder()
                .customerName(request.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();
        customerRepository.save(customer);
        return CustomerInfoResponse.fromEntity(customer);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Customer customer = customerRepository.findByCustomerEmail(request.getCustomerEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), customer.getPasswordHash())) {
            throw new InvalidCredentialsException("Email or password is incorrect.");
        }

        String token = jwtUtil.generateToken(customer.getCustomerEmail());
        return LoginResponse.builder()
                .token(token)
                .customerName(customer.getCustomerName())
                .customerEmail(customer.getCustomerEmail())
                .build();
    }

    @Override
    public CustomerInfoResponse getMe(String email) {
        Customer customer = customerRepository.findByCustomerEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Customer not found"));
        return CustomerInfoResponse.fromEntity(customer);
    }
}