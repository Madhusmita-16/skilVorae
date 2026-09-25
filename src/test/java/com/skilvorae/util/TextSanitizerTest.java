package com.skilvorae.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test suite for TextSanitizer in SkilVorae backend.
 */
public class TextSanitizerTest {

    @Test
    @DisplayName("Should strip XSS HTML script tags from user input string")
    void sanitizeHtml_ScriptTags() {
        String input = "<script>alert('xss');</script>Hello World";
        String clean = TextSanitizer.sanitizeHtml(input);

        assertFalse(clean.contains("<script>"));
        assertTrue(clean.contains("Hello World"));
    }

    @Test
    @DisplayName("Should convert plain text into clean URL slug format")
    void toSlug_Success() {
        String title = "Full Stack Web Development with React & Spring Boot!";
        String slug = TextSanitizer.toSlug(title);

        assertEquals("full-stack-web-development-with-react-spring-boot", slug);
    }
}
