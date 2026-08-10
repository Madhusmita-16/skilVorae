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
public class AdminDashboardStatsDto {
    private long totalUsersCount;
    private long totalStudentsCount;
    private long totalInstructorsCount;
    private long totalCoursesCount;
    private long activeEnrollmentsCount;
    private long completedCoursesCount;
    private long certificatesIssuedCount;
    private double platformRevenue;
    private List<UserManagementDto> recentUsers;
    private List<CourseDto> recentCourses;
    private List<AuditLogDto> recentAuditLogs;
    private List<Integer> userGrowthData;
    private List<Integer> enrollmentGrowthData;
    private List<String> monthLabels;
}
