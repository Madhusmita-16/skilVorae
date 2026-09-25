package com.skilvorae.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test suite verifying bean validation constraints across DTO objects in SkilVorae backend.
 */
public class DtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should detect validation errors for invalid LoginRequest DTO")
    void loginRequest_ValidationFailure() {
        LoginRequest request = new LoginRequest();
        request.setEmail("not-an-email");
        request.setPassword("");

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should pass validation for valid RegisterRequest DTO")
    void registerRequest_ValidationSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("student@skilvorae.com");
        request.setPassword("Password123!");
        request.setFirstName("Alex");
        request.setLastName("Morgan");

        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }
}
