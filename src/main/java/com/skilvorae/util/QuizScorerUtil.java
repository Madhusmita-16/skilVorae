package com.skilvorae.util;

import java.util.Map;

/**
 * Enterprise utility for computing quiz scores, weighted penalties, and grade letter conversions.
 */
public final class QuizScorerUtil {

    private QuizScorerUtil() {
        // Private constructor
    }

    /**
     * Calculates percentage score given correct answers and total questions.
     *
     * @param correctCount Count of correct answers.
     * @param totalCount Count of total questions.
     * @return Calculated integer percentage (0-100).
     */
    public static int calculatePercentage(int correctCount, int totalCount) {
        if (totalCount <= 0) return 0;
        return (int) Math.round(((double) correctCount / totalCount) * 100);
    }

    /**
     * Converts numeric percentage score to letter grade (A+, A, B, C, F).
     *
     * @param percentage Integer percentage.
     * @return Letter grade string.
     */
    public static String toLetterGrade(int percentage) {
        if (percentage >= 95) return "A+";
        if (percentage >= 85) return "A";
        if (percentage >= 75) return "B";
        if (percentage >= 65) return "C";
        if (percentage >= 50) return "D";
        return "F";
    }
}
