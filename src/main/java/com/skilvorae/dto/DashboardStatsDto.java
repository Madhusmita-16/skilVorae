package com.skilvorae.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private Long enrolledCoursesCount;
    private Long inProgressCoursesCount;
    private Long completedCoursesCount;
    private Double totalLearningHours;
    private Double averageAssessmentScore;
    private Long certificatesCount;
    private List<EnrollmentDto> activeCourses;
    private List<EnrollmentDto> inProgressCourses;
    private List<EnrollmentDto> notStartedCourses;
    private List<EnrollmentDto> completedCourses;
    private List<Integer> weeklyActivityData; // Lessons completed per day (Mon - Sun)
    private List<String> recentActivities;

    // Streak & Gamification Features
    private int learningStreakDays;
    private List<Boolean> streakDaysActive; // M, T, W, T, F, S, S
    private List<AchievementDto> achievements;
    private List<UpcomingAssessmentDto> upcomingAssessments;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AchievementDto {
        private String id;
        private String title;
        private String description;
        private String icon;
        private boolean unlocked;
        private String unlockedDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpcomingAssessmentDto {
        private Long assessmentId;
        private String title;
        private String courseTitle;
        private int totalQuestions;
        private int durationMinutes;
        private String dueDate;
    }
}
