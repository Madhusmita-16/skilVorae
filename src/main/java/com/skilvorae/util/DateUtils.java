package com.skilvorae.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility for date formatting, time ago calculations, and timestamp string conversions in SkilVorae.
 */
public final class DateUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateUtils() {
        // Private constructor
    }

    /**
     * Formats LocalDateTime into standard YYYY-MM-DD HH:mm:ss string.
     *
     * @param dateTime Target LocalDateTime.
     * @return Formatted string.
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(FORMATTER);
    }

    /**
     * Formats past timestamp into human-readable relative time string (e.g. "3 hours ago").
     *
     * @param dateTime Past LocalDateTime.
     * @return Human readable string.
     */
    public static String formatTimeAgo(LocalDateTime dateTime) {
        if (dateTime == null) return "Unknown";

        LocalDateTime now = LocalDateTime.now();
        long seconds = ChronoUnit.SECONDS.between(dateTime, now);
        if (seconds < 60) return "Just now";

        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        if (minutes < 60) return minutes + (minutes == 1 ? " minute ago" : " minutes ago");

        long hours = ChronoUnit.HOURS.between(dateTime, now);
        if (hours < 24) return hours + (hours == 1 ? " hour ago" : " hours ago");

        long days = ChronoUnit.DAYS.between(dateTime, now);
        return days + (days == 1 ? " day ago" : " days ago");
    }
}
