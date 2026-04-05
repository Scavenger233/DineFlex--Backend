package com.dineflex.service.impl;

import com.dineflex.dto.request.CustomerLoginRequest;
import com.dineflex.dto.request.CustomerRegisterRequest;
import com.dineflex.dto.response.CustomerInfoResponse;
import com.dineflex.entity.Customer;
import com.dineflex.repository.CustomerRepository;
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

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRegisterRequest registerRequest;
    private CustomerLoginRequest loginRequest;
    private Customer savedCustomer;

    @BeforeEach
    void setUp() {
        registerRequest = new CustomerRegisterRequest();
        registerRequest.setCustomerName("Test User");
        registerRequest.setCustomerEmail("test@example.com");
        registerRequest.setPhone("+353 87 123 4567");
        registerRequest.setPassword("password123");

        loginRequest = new CustomerLoginRequest();
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

    @Test
    void register_shouldSaveCustomerWithCorrectFields() {
        when(passwordEncoder.encode(any())).thenReturn("$2a$10$hashedpassword");
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer c = invocation.getArgument(0);
            assertThat(c.getCustomerName()).isEqualTo("Test User");
            assertThat(c.getCustomerEmail()).isEqualTo("test@example.com");
            assertThat(c.getPhone()).isEqualTo("+353 87 123 4567");
            assertThat(c.getPasswordHash()).isEqualTo("$2a$10$hashedpassword");
            return savedCustomer;
        });

        customerService.register(registerRequest);
    }

    // ───── login() ─────

    @Test
    void login_shouldReturnCustomerInfoResponse_whenCredentialsAreValid() {
        // Use real encoder to generate a real hash for this test
        BCryptPasswordEncoder realEncoder = new BCryptPasswordEncoder();
        String realHash = realEncoder.encode("password123");
        savedCustomer.setPasswordHash(realHash);

        when(customerRepository.findByCustomerEmail("test@example.com"))
                .thenReturn(Optional.of(savedCustomer));

        CustomerInfoResponse response = customerService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getCustomerEmail()).isEqualTo("test@example.com");
        assertThat(response.getCustomerName()).isEqualTo("Test User");
    }

    @Test
    void login_shouldThrowException_whenCustomerNotFound() {
        when(customerRepository.findByCustomerEmail("test@example.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.login(loginRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Customer not found");
    }

    @Test
    void login_shouldThrowException_whenPasswordIsIncorrect() {
        BCryptPasswordEncoder realEncoder = new BCryptPasswordEncoder();
        savedCustomer.setPasswordHash(realEncoder.encode("correctpassword"));

        when(customerRepository.findByCustomerEmail("test@example.com"))
                .thenReturn(Optional.of(savedCustomer));

        loginRequest.setPassword("wrongpassword");

        assertThatThrownBy(() -> customerService.login(loginRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid password");
    }

    @Test
    void login_shouldCallRepository_withCorrectEmail() {
        BCryptPasswordEncoder realEncoder = new BCryptPasswordEncoder();
        savedCustomer.setPasswordHash(realEncoder.encode("password123"));

        when(customerRepository.findByCustomerEmail("test@example.com"))
                .thenReturn(Optional.of(savedCustomer));

        customerService.login(loginRequest);

        verify(customerRepository, times(1)).findByCustomerEmail("test@example.com");
    }
}