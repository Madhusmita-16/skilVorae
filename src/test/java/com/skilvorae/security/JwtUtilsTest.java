package com.skilvorae.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 unit test suite for JwtUtils security utility in SkilVorae backend.
 */
public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // set secret and expiration for testing using Reflection / test values
    }

    @Test
    @DisplayName("Should generate valid JWT token for authenticated principal")
    void generateToken_Success() {
        User principal = new User("student@skilvorae.com", "password", List.of(new SimpleGrantedAuthority("ROLE_STUDENT")));
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        String token = jwtUtils.generateToken(auth);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("student@skilvorae.com", jwtUtils.getUserNameFromJwtToken(token));
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    @DisplayName("Should generate valid Refresh Token for given email username")
    void generateRefreshToken_Success() {
        String refreshToken = jwtUtils.generateRefreshToken("student@skilvorae.com");

        assertNotNull(refreshToken);
        assertEquals("student@skilvorae.com", jwtUtils.getUserNameFromJwtToken(refreshToken));
        assertTrue(jwtUtils.validateJwtToken(refreshToken));
    }

    @Test
    @DisplayName("Should return false for invalid or tampered JWT string")
    void validateJwtToken_TamperedToken() {
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.invalidPayload.invalidSignature";

        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }
}
