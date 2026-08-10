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
public class InstructorDashboardStatsDto {
    private long totalCoursesCount;
    private long totalStudentsCount;
    private long activeLearnersCount;
    private double courseCompletionRate;
    private double averageRating;
    private double totalEarnings;
    private List<CourseDto> instructorCourses;
    private List<StudentRosterDto> recentStudents;
    private List<Integer> monthlyEnrollmentData;
    private List<String> monthlyLabels;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentRosterDto {
        private Long studentId;
        private String studentName;
        private String studentEmail;
        private String courseTitle;
        private String enrollmentDate;
        private int progressPercentage;
        private Double assessmentScore;
        private String status;
        private String lastActivity;
    }
}
