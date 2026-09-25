package com.skilvorae.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test suite verifying enum domain value coverage across SkilVorae backend.
 */
public class EnumCoverageTest {

    @Test
    @DisplayName("Should verify Role values and descriptions")
    void roleEnum_Coverage() {
        assertEquals(3, Role.values().length);
        assertEquals(Role.STUDENT, Role.valueOf("STUDENT"));
        assertEquals(Role.INSTRUCTOR, Role.valueOf("INSTRUCTOR"));
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
    }

    @Test
    @DisplayName("Should verify CourseCategory domain enum values")
    void courseCategoryEnum_Coverage() {
        assertTrue(CourseCategory.values().length > 0);
        assertNotNull(CourseCategory.WEB_DEVELOPMENT);
        assertNotNull(CourseCategory.CLOUD_COMPUTING);
        assertNotNull(CourseCategory.DATA_SCIENCE);
        assertNotNull(CourseCategory.CYBERSECURITY);
    }

    @Test
    @DisplayName("Should verify DifficultyLevel domain enum values")
    void difficultyLevelEnum_Coverage() {
        assertEquals(4, DifficultyLevel.values().length);
        assertEquals(DifficultyLevel.BEGINNER, DifficultyLevel.valueOf("BEGINNER"));
    }
}
