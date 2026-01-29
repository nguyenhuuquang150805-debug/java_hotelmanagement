package com.nguyenhuuquang.hotelmanagement.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nguyenhuuquang.hotelmanagement.service.impl.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            log.debug("Processing request: {} {}", request.getMethod(), request.getRequestURI());

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                log.debug("JWT token found, length: {}", token.length());

                String email = jwtUtil.extractUsername(token);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    log.debug("Extracted email: {}, loading user details...", email);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    log.debug("User details loaded, authorities: {}", userDetails.getAuthorities());

                    if (jwtUtil.isTokenValid(token, userDetails)) {
                        log.info("✅ Token is valid for user: {}", email);

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        log.info("✅ Authentication set in SecurityContext for user: {} with authorities: {}",
                                email, userDetails.getAuthorities());
                    } else {
                        log.warn("❌ Token validation failed for user: {}", email);
                    }
                } else if (email == null) {
                    log.warn("❌ Could not extract email from token");
                } else {
                    log.debug("Authentication already exists in SecurityContext");
                }
            } else {
                log.debug("No Bearer token found in request");
            }
        } catch (Exception e) {
            log.error("❌ Error processing JWT authentication: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}