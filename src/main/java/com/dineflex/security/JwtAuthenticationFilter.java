package com.dineflex.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("🔔 Jwt Filter triggered for: " + request.getMethod() + " " + request.getRequestURI());

        System.out.println("✅ JWT Filter loaded and active");

        String path = request.getRequestURI();
        System.out.println("➡️ [JWT Filter] Request URI: " + path);

        if (path.startsWith("/api/auth") || path.startsWith("/api/customers/register")) {
            System.out.println("🟢 [JWT Filter] Skipping auth for public endpoint.");
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("🔎 [JWT Filter] Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("⚠️ [JWT Filter] No Bearer token found. Proceeding unauthenticated.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        System.out.println("📧 [JWT Filter] Extracted email from token: " + email);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.isTokenValid(token)) {
                System.out.println("✅ [JWT Filter] Token valid. Setting security context.");

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                email, null, Collections.emptyList());

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                System.out.println("❌ [JWT Filter] Invalid token.");
            }
        } else {
            System.out.println("⚠️ [JWT Filter] Email null or authentication already exists.");
        }

        filterChain.doFilter(request, response);
    }
}