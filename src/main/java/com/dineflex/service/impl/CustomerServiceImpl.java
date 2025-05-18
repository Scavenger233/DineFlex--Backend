package com.dineflex.service.impl;

import com.dineflex.dto.request.CustomerLoginRequest;
import com.dineflex.dto.request.CustomerRegisterRequest;
import com.dineflex.dto.response.CustomerInfoResponse;
import com.dineflex.entity.Customer;
import com.dineflex.repository.CustomerRepository;
import com.dineflex.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;


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
    public CustomerInfoResponse login(CustomerLoginRequest request) {
        Customer customer = customerRepository.findByCustomerEmail(request.getCustomerEmail())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!BCrypt.checkpw(request.getPassword(), customer.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        return CustomerInfoResponse.fromEntity(customer);
    }
}