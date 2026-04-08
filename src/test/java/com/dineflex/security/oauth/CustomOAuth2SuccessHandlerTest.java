package com.dineflex.security.oauth;

import com.dineflex.entity.Customer;
import com.dineflex.repository.CustomerRepository;
import com.dineflex.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2SuccessHandlerTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomOAuth2SuccessHandler successHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2User oAuth2User;

    private Customer existingCustomer;

    @BeforeEach
    void setUp() {
        existingCustomer = Customer.builder()
                .id(1L)
                .customerName("Test User")
                .customerEmail("test@example.com")
                .phone("N/A")
                .passwordHash("OAUTH2_USER")
                .build();
    }

    // ───── Log in (existing user) ─────

    @Test
    void onAuthenticationSuccess_shouldRedirectWithToken_whenExistingCustomer() throws Exception {
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("test@example.com");
        when(oAuth2User.getAttribute("name")).thenReturn("Test User");
        when(customerRepository.findByCustomerEmail("test@example.com"))
                .thenReturn(Optional.of(existingCustomer));
        when(jwtUtil.generateToken("test@example.com")).thenReturn("mock-jwt-token");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(jwtUtil).generateToken("test@example.com");
        verify(response).sendRedirect(contains("token=mock-jwt-token"));
        verify(customerRepository, never()).save(any());
    }

    // ───── Automatic registration for new users ─────

    @Test
    void onAuthenticationSuccess_shouldRegisterNewCustomer_whenNotExists() throws Exception {
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("new@example.com");
        when(oAuth2User.getAttribute("name")).thenReturn("New User");
        when(customerRepository.findByCustomerEmail("new@example.com"))
                .thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);
        when(jwtUtil.generateToken("new@example.com")).thenReturn("new-jwt-token");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(customerRepository).save(any(Customer.class));
        verify(jwtUtil).generateToken("new@example.com");
        verify(response).sendRedirect(contains("token=new-jwt-token"));
    }

    // ───── Use the default name when `name` is null ─────

    @Test
    void onAuthenticationSuccess_shouldUseDefaultName_whenNameIsNull() throws Exception {
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("noname@example.com");
        when(oAuth2User.getAttribute("name")).thenReturn(null);
        when(customerRepository.findByCustomerEmail("noname@example.com"))
                .thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer saved = invocation.getArgument(0);
            assert saved.getCustomerName().equals("GoogleUser");
            return saved;
        });
        when(jwtUtil.generateToken("noname@example.com")).thenReturn("token");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(customerRepository).save(argThat(c -> c.getCustomerName().equals("GoogleUser")));
    }

    // ───── exception handling ─────

    @Test
    void onAuthenticationSuccess_shouldSendError_whenExceptionOccurs() throws Exception {
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("test@example.com");
        when(oAuth2User.getAttribute("name")).thenReturn("Test User");
        when(customerRepository.findByCustomerEmail("test@example.com"))
                .thenThrow(new RuntimeException("Database error"));

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendError(
                eq(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
                contains("OAuth2 login error"));
        verify(response, never()).sendRedirect(any());
    }
}