package com.skilvorae.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 unit test suite for RateLimitingFilter in SkilVorae security layer.
 */
@ExtendWith(MockitoExtension.class)
public class RateLimitingFilterTest {

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private RateLimitingFilter rateLimitingFilter;

    @Test
    @DisplayName("Should allow normal API HTTP requests within rate limit bounds")
    void doFilter_Allowed() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("192.168.1.50");
        request.setRequestURI("/api/v1/courses");
        MockHttpServletResponse response = new MockHttpServletResponse();

        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNotEquals(429, response.getStatus());
    }

    @Test
    @DisplayName("Should block request with HTTP 429 Too Many Requests when limit exceeded")
    void doFilter_RateLimited() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("10.0.0.99");
        request.setRequestURI("/api/v1/auth/login");

        // Simulate exceeding max requests threshold
        for (int i = 0; i < 120; i++) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            rateLimitingFilter.doFilterInternal(request, response, filterChain);
        }

        MockHttpServletResponse blockedResponse = new MockHttpServletResponse();
        rateLimitingFilter.doFilterInternal(request, blockedResponse, filterChain);

        assertEquals(429, blockedResponse.getStatus());
    }
}
