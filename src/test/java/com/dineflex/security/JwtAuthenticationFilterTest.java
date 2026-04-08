package com.dineflex.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSkipFilter_forAuthEndpoint() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/auth/login");
        when(request.getMethod()).thenReturn("POST");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtil, never()).extractUsername(any());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldSkipFilter_forRegisterEndpoint() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/customers/register");
        when(request.getMethod()).thenReturn("POST");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtil, never()).extractUsername(any());
    }

    @Test
    void shouldProceedUnauthenticated_whenNoAuthorizationHeader() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/restaurants");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtil, never()).extractUsername(any());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldProceedUnauthenticated_whenAuthHeaderNotBearer() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/restaurants");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Basic sometoken");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtil, never()).extractUsername(any());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldSetAuthentication_whenValidBearerToken() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/bookings");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtUtil.extractUsername("valid-token")).thenReturn("test@example.com");
        when(jwtUtil.isTokenValid("valid-token")).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName())
                .isEqualTo("test@example.com");
    }

    @Test
    void shouldNotSetAuthentication_whenInvalidToken() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/bookings");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
        when(jwtUtil.extractUsername("invalid-token")).thenReturn("test@example.com");
        when(jwtUtil.isTokenValid("invalid-token")).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldNotSetAuthentication_whenExtractedEmailIsNull() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/bookings");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer some-token");
        when(jwtUtil.extractUsername("some-token")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldReturn401_whenTokenIsExpired() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/bookings");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer expired-token");
        when(jwtUtil.extractUsername("expired-token"))
                .thenThrow(new ExpiredJwtException(null, null, "Token expired"));

        PrintWriter writer = new PrintWriter(new StringWriter());
        when(response.getWriter()).thenReturn(writer);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void shouldReturn401_whenTokenIsInvalid() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/bookings");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer bad-token");
        when(jwtUtil.extractUsername("bad-token"))
                .thenThrow(new RuntimeException("Invalid JWT"));

        PrintWriter writer = new PrintWriter(new StringWriter());
        when(response.getWriter()).thenReturn(writer);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void shouldNotOverrideAuthentication_whenAlreadyAuthenticated() throws Exception {
        UsernamePasswordAuthenticationToken existingAuth =
                new UsernamePasswordAuthenticationToken("existing@example.com", null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(existingAuth);

        when(request.getRequestURI()).thenReturn("/api/bookings");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtUtil.extractUsername("valid-token")).thenReturn("test@example.com");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName())
                .isEqualTo("existing@example.com");
    }
}