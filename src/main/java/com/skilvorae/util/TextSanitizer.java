package com.skilvorae.util;

/**
 * Text processing utility for sanitizing user-submitted HTML in course reviews, Q&A forums, and assignments.
 */
public final class TextSanitizer {

    private TextSanitizer() {
        // Private constructor
    }

    /**
     * Strips dangerous script tags and HTML elements.
     *
     * @param input Raw text input.
     * @return Clean text string.
     */
    public static String stripHtml(String input) {
        if (input == null) return "";
        return input.replaceAll("<[^>]*>", "").trim();
    }
}
