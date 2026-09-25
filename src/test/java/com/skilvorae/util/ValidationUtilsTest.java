package com.skilvorae.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test suite for ValidationUtils in SkilVorae backend.
 */
public class ValidationUtilsTest {

    @Test
    @DisplayName("Should validate correct email addresses")
    void isValidEmail_Valid() {
        assertTrue(ValidationUtils.isValidEmail("alex.morgan@skilvorae.com"));
        assertTrue(ValidationUtils.isValidEmail("user+test@domain.co.uk"));
    }

    @Test
    @DisplayName("Should invalidate malformed email addresses")
    void isValidEmail_Invalid() {
        assertFalse(ValidationUtils.isValidEmail("plainaddress"));
        assertFalse(ValidationUtils.isValidEmail("@domain.com"));
        assertFalse(ValidationUtils.isValidEmail("user@.com"));
        assertFalse(ValidationUtils.isValidEmail(null));
    }

    @Test
    @DisplayName("Should validate strong password compliance")
    void isStrongPassword_Valid() {
        assertTrue(ValidationUtils.isStrongPassword("P@ssword123!"));
        assertFalse(ValidationUtils.isStrongPassword("short"));
        assertFalse(ValidationUtils.isStrongPassword("alllowercase123!"));
    }
}
