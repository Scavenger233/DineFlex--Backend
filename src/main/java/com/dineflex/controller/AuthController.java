package com.dineflex.controller;

import com.dineflex.dto.request.LoginRequest;
import com.dineflex.dto.response.LoginResponse;
import com.dineflex.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CustomerService customerService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return customerService.login(loginRequest);
    }
}