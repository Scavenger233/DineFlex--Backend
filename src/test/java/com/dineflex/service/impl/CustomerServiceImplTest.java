package com.dineflex.service.impl;

import com.dineflex.dto.request.CustomerRegisterRequest;
import com.dineflex.dto.request.LoginRequest;
import com.dineflex.dto.response.CustomerInfoResponse;
import com.dineflex.dto.response.LoginResponse;
import com.dineflex.entity.Customer;
import com.dineflex.exception.InvalidCredentialsException;
import com.dineflex.exception.UserNotFoundException;
import com.dineflex.repository.CustomerRepository;
import com.dineflex.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private Customer savedCustomer;

    @BeforeEach
    void setUp() {
        registerRequest = new CustomerRegisterRequest();
        registerRequest.setCustomerName("Test User");
        registerRequest.setCustomerEmail("test@example.com");
        registerRequest.setPhone("+353 87 123 4567");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setCustomerEmail("test@example.com");
        loginRequest.setPassword("password123");

        savedCustomer = Customer.builder()
                .id(1L)
                .customerName("Test User")
                .customerEmail("test@example.com")
                .phone("+353 87 123 4567")
                .passwordHash("$2a$10$hashedpassword")
                .build();
    }

    // ───── register() ─────

    @Test
    void register_shouldReturnCustomerInfoResponse_whenValidRequest() {
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$hashedpassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerInfoResponse response = customerService.register(registerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getCustomerName()).isEqualTo("Test User");
        assertThat(response.getCustomerEmail()).isEqualTo("test@example.com");
        assertThat(response.getPhone()).isEqualTo("+353 87 123 4567");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void register_shouldEncodePassword_beforeSaving() {
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$hashedpassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        customerService.register(registerRequest);

        verify(passwordEncoder, times(1)).encode("password123");
    }

    // ───── login() ─────

    @Test
    void login_shouldReturnLoginResponse_whenValidCredentials() {
        when(customerRepository.findByCustomerEmail("test@example.com"))
                .thenReturn(Optional.of(savedCustomer));
        when(passwordEncoder.matches("password123", "$2a$10$hashedpassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@example.com")).thenReturn("mock-jwt-token");

        LoginResponse response = customerService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mock-jwt-token");
        assertThat(response.getCustomerName()).isEqualTo("Test User");
        assertThat(response.getCustomerEmail()).isEqualTo("test@example.com");
    }

    @Test
    void login_shouldThrowException_whenCustomerNotFound() {
        when(customerRepository.findByCustomerEmail("test@example.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.login(loginRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void login_shouldThrowInvalidCredentialsException_whenPasswordIsIncorrect() {
        when(customerRepository.findByCustomerEmail("test@example.com"))
                .thenReturn(Optional.of(savedCustomer));
        when(passwordEncoder.matches("password123", "$2a$10$hashedpassword")).thenReturn(false);

        assertThatThrownBy(() -> customerService.login(loginRequest))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Email or password is incorrect.");
    }

    @Test
    void login_shouldCallRepository_withCorrectEmail() {
        when(customerRepository.findByCustomerEmail("test@example.com"))
                .thenReturn(Optional.of(savedCustomer));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtUtil.generateToken(any())).thenReturn("token");

        customerService.login(loginRequest);

        verify(customerRepository, times(1)).findByCustomerEmail("test@example.com");
    }

    // ───── getMe() ─────

    @Test
    void getMe_shouldReturnCustomerInfoResponse_whenCustomerExists() {
        when(customerRepository.findByCustomerEmail("test@example.com"))
                .thenReturn(Optional.of(savedCustomer));

        CustomerInfoResponse response = customerService.getMe("test@example.com");

        assertThat(response).isNotNull();
        assertThat(response.getCustomerName()).isEqualTo("Test User");
        assertThat(response.getCustomerEmail()).isEqualTo("test@example.com");
    }

    @Test
    void getMe_shouldThrowUserNotFoundException_whenCustomerNotFound() {
        when(customerRepository.findByCustomerEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getMe("unknown@example.com"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Customer not found");
    }
}