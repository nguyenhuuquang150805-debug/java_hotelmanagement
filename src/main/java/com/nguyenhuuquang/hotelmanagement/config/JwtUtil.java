package com.nguyenhuuquang.hotelmanagement.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret:mySecretKeyForHotelManagementSystemThatIsAtLeast256BitsLong12345678}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    public String generateToken(String email) {
        log.debug("Generating token for email: {}", email);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        log.debug("Token generated successfully, length: {}", token.length());
        return token;
    }

    public String extractUsername(String token) {
        try {
            String username = extractAllClaims(token).getSubject();
            log.debug("Extracted username: {}", username);
            return username;
        } catch (Exception e) {
            log.error("Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isValid = (username != null &&
                    username.equals(userDetails.getUsername()) &&
                    !isTokenExpired(token));

            log.debug("Token validation for {}: {}",
                    userDetails.getUsername(),
                    isValid ? "SUCCESS" : "FAILED");

            return isValid;
        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }

    public boolean validateToken(String token, String email) {
        try {
            final String username = extractUsername(token);
            boolean isValid = (username != null &&
                    username.equals(email) &&
                    !isTokenExpired(token));

            log.debug("Token validation for {}: {}", email, isValid ? "SUCCESS" : "FAILED");

            return isValid;
        } catch (Exception e) {
            log.error("Token validation error for {}: {}", email, e.getMessage());
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        try {
            Date expirationDate = extractAllClaims(token).getExpiration();
            boolean expired = expirationDate.before(new Date());

            if (expired) {
                log.debug("Token expired at: {}, current time: {}", expirationDate, new Date());
            }

            return expired;
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    public Date getExpirationDate(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Date getIssuedDate(String token) {
        return extractAllClaims(token).getIssuedAt();
    }
}