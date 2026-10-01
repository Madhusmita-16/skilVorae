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

    public long getTotalUsersCount() { return totalUsersCount; }
    public void setTotalUsersCount(long totalUsersCount) { this.totalUsersCount = totalUsersCount; }
    public long getTotalStudentsCount() { return totalStudentsCount; }
    public void setTotalStudentsCount(long totalStudentsCount) { this.totalStudentsCount = totalStudentsCount; }
    public long getTotalInstructorsCount() { return totalInstructorsCount; }
    public void setTotalInstructorsCount(long totalInstructorsCount) { this.totalInstructorsCount = totalInstructorsCount; }
    public long getTotalCoursesCount() { return totalCoursesCount; }
    public void setTotalCoursesCount(long totalCoursesCount) { this.totalCoursesCount = totalCoursesCount; }
    public long getActiveEnrollmentsCount() { return activeEnrollmentsCount; }
    public void setActiveEnrollmentsCount(long activeEnrollmentsCount) { this.activeEnrollmentsCount = activeEnrollmentsCount; }
    public long getCompletedCoursesCount() { return completedCoursesCount; }
    public void setCompletedCoursesCount(long completedCoursesCount) { this.completedCoursesCount = completedCoursesCount; }
    public long getCertificatesIssuedCount() { return certificatesIssuedCount; }
    public void setCertificatesIssuedCount(long certificatesIssuedCount) { this.certificatesIssuedCount = certificatesIssuedCount; }
    public double getPlatformRevenue() { return platformRevenue; }
    public void setPlatformRevenue(double platformRevenue) { this.platformRevenue = platformRevenue; }
    public List<UserManagementDto> getRecentUsers() { return recentUsers; }
    public void setRecentUsers(List<UserManagementDto> recentUsers) { this.recentUsers = recentUsers; }
    public List<CourseDto> getRecentCourses() { return recentCourses; }
    public void setRecentCourses(List<CourseDto> recentCourses) { this.recentCourses = recentCourses; }
    public List<AuditLogDto> getRecentAuditLogs() { return recentAuditLogs; }
    public void setRecentAuditLogs(List<AuditLogDto> recentAuditLogs) { this.recentAuditLogs = recentAuditLogs; }
    public List<Integer> getUserGrowthData() { return userGrowthData; }
    public void setUserGrowthData(List<Integer> userGrowthData) { this.userGrowthData = userGrowthData; }
    public List<Integer> getEnrollmentGrowthData() { return enrollmentGrowthData; }
    public void setEnrollmentGrowthData(List<Integer> enrollmentGrowthData) { this.enrollmentGrowthData = enrollmentGrowthData; }
    public List<String> getMonthLabels() { return monthLabels; }
    public void setMonthLabels(List<String> monthLabels) { this.monthLabels = monthLabels; }

    public static AdminDashboardStatsDtoBuilder builder() { return new AdminDashboardStatsDtoBuilder(); }

    public static class AdminDashboardStatsDtoBuilder {
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

        public AdminDashboardStatsDtoBuilder totalUsersCount(long totalUsersCount) { this.totalUsersCount = totalUsersCount; return this; }
        public AdminDashboardStatsDtoBuilder totalStudentsCount(long totalStudentsCount) { this.totalStudentsCount = totalStudentsCount; return this; }
        public AdminDashboardStatsDtoBuilder totalInstructorsCount(long totalInstructorsCount) { this.totalInstructorsCount = totalInstructorsCount; return this; }
        public AdminDashboardStatsDtoBuilder totalCoursesCount(long totalCoursesCount) { this.totalCoursesCount = totalCoursesCount; return this; }
        public AdminDashboardStatsDtoBuilder activeEnrollmentsCount(long activeEnrollmentsCount) { this.activeEnrollmentsCount = activeEnrollmentsCount; return this; }
        public AdminDashboardStatsDtoBuilder completedCoursesCount(long completedCoursesCount) { this.completedCoursesCount = completedCoursesCount; return this; }
        public AdminDashboardStatsDtoBuilder certificatesIssuedCount(long certificatesIssuedCount) { this.certificatesIssuedCount = certificatesIssuedCount; return this; }
        public AdminDashboardStatsDtoBuilder platformRevenue(double platformRevenue) { this.platformRevenue = platformRevenue; return this; }
        public AdminDashboardStatsDtoBuilder recentUsers(List<UserManagementDto> recentUsers) { this.recentUsers = recentUsers; return this; }
        public AdminDashboardStatsDtoBuilder recentCourses(List<CourseDto> recentCourses) { this.recentCourses = recentCourses; return this; }
        public AdminDashboardStatsDtoBuilder recentAuditLogs(List<AuditLogDto> recentAuditLogs) { this.recentAuditLogs = recentAuditLogs; return this; }
        public AdminDashboardStatsDtoBuilder userGrowthData(List<Integer> userGrowthData) { this.userGrowthData = userGrowthData; return this; }
        public AdminDashboardStatsDtoBuilder enrollmentGrowthData(List<Integer> enrollmentGrowthData) { this.enrollmentGrowthData = enrollmentGrowthData; return this; }
        public AdminDashboardStatsDtoBuilder monthLabels(List<String> monthLabels) { this.monthLabels = monthLabels; return this; }

        public AdminDashboardStatsDto build() {
            return new AdminDashboardStatsDto(totalUsersCount, totalStudentsCount, totalInstructorsCount, totalCoursesCount, activeEnrollmentsCount, completedCoursesCount, certificatesIssuedCount, platformRevenue, recentUsers, recentCourses, recentAuditLogs, userGrowthData, enrollmentGrowthData, monthLabels);
        }
    }
}
