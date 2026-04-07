package com.dineflex.controller;

import com.dineflex.dto.request.CustomerLoginRequest;
import com.dineflex.dto.request.CustomerRegisterRequest;
import com.dineflex.dto.response.CustomerInfoResponse;
import com.dineflex.repository.CustomerRepository;
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
        controllers = CustomerController.class,
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
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private CustomerRepository customerRepository;

    @Test
    void register_shouldReturn200_whenValidRequest() throws Exception {
        CustomerRegisterRequest request = new CustomerRegisterRequest();
        request.setCustomerName("Test User");
        request.setCustomerEmail("test@example.com");
        request.setPhone("+353 87 123 4567");
        request.setPassword("password123");

        CustomerInfoResponse response = CustomerInfoResponse.builder()
                .id(1L)
                .customerName("Test User")
                .customerEmail("test@example.com")
                .phone("+353 87 123 4567")
                .build();

        when(customerService.register(any())).thenReturn(response);

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Test User"))
                .andExpect(jsonPath("$.customerEmail").value("test@example.com"))
                .andExpect(jsonPath("$.phone").value("+353 87 123 4567"));
    }

    @Test
    void register_shouldReturn500_whenServiceThrows() throws Exception {
        CustomerRegisterRequest request = new CustomerRegisterRequest();
        request.setCustomerName("Test User");
        request.setCustomerEmail("duplicate@example.com");
        request.setPhone("+353 87 123 4567");
        request.setPassword("password123");

        when(customerService.register(any()))
                .thenThrow(new RuntimeException("Email already exists"));

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void login_shouldReturn200_whenValidCredentials() throws Exception {
        CustomerLoginRequest request = new CustomerLoginRequest();
        request.setCustomerEmail("test@example.com");
        request.setPassword("password123");

        CustomerInfoResponse response = CustomerInfoResponse.builder()
                .id(1L)
                .customerName("Test User")
                .customerEmail("test@example.com")
                .build();

        when(customerService.login(any())).thenReturn(response);

        mockMvc.perform(post("/api/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerEmail").value("test@example.com"))
                .andExpect(jsonPath("$.customerName").value("Test User"));
    }

    @Test
    void login_shouldReturn500_whenInvalidCredentials() throws Exception {
        CustomerLoginRequest request = new CustomerLoginRequest();
        request.setCustomerEmail("test@example.com");
        request.setPassword("wrongpassword");

        when(customerService.login(any()))
                .thenThrow(new RuntimeException("Invalid password"));

        mockMvc.perform(post("/api/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }
}