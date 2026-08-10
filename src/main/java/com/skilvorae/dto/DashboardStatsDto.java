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
    private Long completedCoursesCount;
    private Double totalLearningHours;
    private Double averageAssessmentScore;
    private Long certificatesCount;
    private List<EnrollmentDto> activeCourses;
    private List<Integer> weeklyActivityData; // Lessons completed per day (Mon - Sun)
    private List<String> recentActivities;
}
