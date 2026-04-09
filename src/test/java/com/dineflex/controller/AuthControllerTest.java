package com.dineflex.controller;

import com.dineflex.dto.request.LoginRequest;
import com.dineflex.dto.response.LoginResponse;
import com.dineflex.exception.InvalidCredentialsException;
import com.dineflex.security.JwtAuthenticationFilter;
import com.dineflex.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ClientAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@WithMockUser
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void login_shouldReturn200_andToken_whenValidCredentials() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setCustomerEmail("test@example.com");
        request.setPassword("password123");

        LoginResponse loginResponse = LoginResponse.builder()
                .token("mock-jwt-token")
                .customerName("Test User")
                .customerEmail("test@example.com")
                .build();

        when(customerService.login(any())).thenReturn(loginResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.customerName").value("Test User"))
                .andExpect(jsonPath("$.customerEmail").value("test@example.com"));
    }

    @Test
    void login_shouldReturn500_whenUserNotFound() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setCustomerEmail("unknown@example.com");
        request.setPassword("password123");

        when(customerService.login(any()))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void login_shouldReturn401_whenPasswordIncorrect() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setCustomerEmail("test@example.com");
        request.setPassword("wrongpassword");

        when(customerService.login(any()))
                .thenThrow(new InvalidCredentialsException("Email or password is incorrect."));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}