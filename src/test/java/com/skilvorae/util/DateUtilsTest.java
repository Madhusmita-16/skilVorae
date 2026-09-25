package com.skilvorae.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test suite for DateUtils in SkilVorae backend.
 */
public class DateUtilsTest {

    @Test
    @DisplayName("Should format LocalDateTime to ISO String format")
    void formatIso_Success() {
        LocalDateTime date = LocalDateTime.of(2026, 9, 25, 14, 30, 0);
        String formatted = DateUtils.formatIso(date);

        assertEquals("2026-09-25T14:30:00", formatted);
    }

    @Test
    @DisplayName("Should format LocalDateTime to human readable date string")
    void formatHumanReadable_Success() {
        LocalDateTime date = LocalDateTime.of(2026, 9, 25, 14, 30, 0);
        String formatted = DateUtils.formatHumanReadable(date);

        assertEquals("September 25, 2026", formatted);
    }

    @Test
    @DisplayName("Should calculate difference in minutes accurately")
    void calculateMinutesBetween_Success() {
        LocalDateTime start = LocalDateTime.of(2026, 9, 25, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 9, 25, 11, 45, 0);

        long minutes = DateUtils.calculateMinutesBetween(start, end);

        assertEquals(105, minutes);
    }
}
