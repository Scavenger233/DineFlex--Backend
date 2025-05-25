package com.dineflex.security.oauth;

import com.dineflex.entity.Customer;
import com.dineflex.repository.CustomerRepository;
import com.dineflex.security.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final CustomerRepository customerRepository;

    // Match the one in application.properties (frontend page to redirect to)
    private final String REDIRECT_URI = "https://dine-flex-frontend.vercel.app/oauth2/redirect";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");

            System.out.println("OAuth2 login success. Email: " + email + ", Name: " + name);

            // Auto-register or fetch user
            Customer customer = customerRepository.findByCustomerEmail(email)
                    .orElseGet(() -> {
                        Customer newCustomer = Customer.builder()
                                .customerName(name != null ? name : "GoogleUser")
                                .customerEmail(email)
                                .phone("N/A")
                                .passwordHash("OAUTH2_USER")
                                .build();
                        System.out.println("Registering new customer: " + email);
                        return customerRepository.save(newCustomer);
                    });

            String token = jwtUtil.generateToken(email);
            System.out.println("Generated token: " + token);

            String redirectUrl = REDIRECT_URI + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            e.printStackTrace(); // This will be visible in Render logs
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "OAuth2 login error: " + e.getMessage());
        }
    }

}