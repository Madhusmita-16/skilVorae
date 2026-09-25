package com.skilvorae.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 unit test suite for GlobalExceptionHandler in SkilVorae backend.
 */
public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException and return 404 response")
    void handleResourceNotFoundException_Success() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Course", "id", 99L);
        ResponseEntity<ErrorDetails> response = exceptionHandler.handleResourceNotFoundException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Course not found with id : '99'", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle BadRequestException and return 400 response")
    void handleBadRequestException_Success() {
        BadRequestException ex = new BadRequestException("Invalid input parameter");
        ResponseEntity<ErrorDetails> response = exceptionHandler.handleBadRequestException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input parameter", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle UnauthorizedException and return 401 response")
    void handleUnauthorizedException_Success() {
        UnauthorizedException ex = new UnauthorizedException("Bad credentials");
        ResponseEntity<ErrorDetails> response = exceptionHandler.handleUnauthorizedException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Bad credentials", response.getBody().getMessage());
    }
}
