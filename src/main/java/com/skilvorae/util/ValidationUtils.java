package com.skilvorae.util;

import java.util.regex.Pattern;

/**
 * Enterprise validation utility for SkilVorae emails, passwords, and URLs.
 */
public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    private ValidationUtils() {
        // Private constructor
    }

    /**
     * Validates email address format.
     *
     * @param email Target email address string.
     * @return true if valid email syntax.
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
}
