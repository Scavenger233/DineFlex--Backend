package com.dineflex.controller;

import com.dineflex.dto.request.LoginRequest;
import com.dineflex.entity.Customer;
import com.dineflex.repository.CustomerRepository;
import com.dineflex.security.JwtAuthenticationFilter;
import com.dineflex.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.Collections;

import java.util.Optional;

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
    private CustomerRepository customerRepository;

    @MockitoBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtUtil jwtUtil;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .customerName("Test User")
                .customerEmail("test@example.com")
                .phone("+353 87 123 4567")
                .passwordHash("hashed")
                .build();
    }

    @Test
    void login_shouldReturn200_andToken_whenValidCredentials() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setCustomerEmail("test@example.com");
        request.setPassword("password123");

        when(customerRepository.findByCustomerEmail("test@example.com"))
                .thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("password123", "hashed")).thenReturn(true);
        when(jwtUtil.generateToken("test@example.com")).thenReturn("mock-jwt-token");

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

        when(customerRepository.findByCustomerEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void login_shouldReturn500_whenPasswordIncorrect() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setCustomerEmail("test@example.com");
        request.setPassword("wrongpassword");

        when(customerRepository.findByCustomerEmail("test@example.com"))
                .thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("wrongpassword", "hashed")).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getCurrentCustomer_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/auth/customers/me"))
                .andExpect(status().isUnauthorized());
    }
}