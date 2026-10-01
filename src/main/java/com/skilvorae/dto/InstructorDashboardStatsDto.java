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

    public long getTotalCoursesCount() { return totalCoursesCount; }
    public void setTotalCoursesCount(long totalCoursesCount) { this.totalCoursesCount = totalCoursesCount; }
    public long getTotalStudentsCount() { return totalStudentsCount; }
    public void setTotalStudentsCount(long totalStudentsCount) { this.totalStudentsCount = totalStudentsCount; }
    public long getActiveLearnersCount() { return activeLearnersCount; }
    public void setActiveLearnersCount(long activeLearnersCount) { this.activeLearnersCount = activeLearnersCount; }
    public double getCourseCompletionRate() { return courseCompletionRate; }
    public void setCourseCompletionRate(double courseCompletionRate) { this.courseCompletionRate = courseCompletionRate; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public double getTotalEarnings() { return totalEarnings; }
    public void setTotalEarnings(double totalEarnings) { this.totalEarnings = totalEarnings; }
    public List<CourseDto> getInstructorCourses() { return instructorCourses; }
    public void setInstructorCourses(List<CourseDto> instructorCourses) { this.instructorCourses = instructorCourses; }
    public List<StudentRosterDto> getRecentStudents() { return recentStudents; }
    public void setRecentStudents(List<StudentRosterDto> recentStudents) { this.recentStudents = recentStudents; }
    public List<Integer> getMonthlyEnrollmentData() { return monthlyEnrollmentData; }
    public void setMonthlyEnrollmentData(List<Integer> monthlyEnrollmentData) { this.monthlyEnrollmentData = monthlyEnrollmentData; }
    public List<String> getMonthlyLabels() { return monthlyLabels; }
    public void setMonthlyLabels(List<String> monthlyLabels) { this.monthlyLabels = monthlyLabels; }

    public static InstructorDashboardStatsDtoBuilder builder() { return new InstructorDashboardStatsDtoBuilder(); }

    public static class InstructorDashboardStatsDtoBuilder {
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

        public InstructorDashboardStatsDtoBuilder totalCoursesCount(long totalCoursesCount) { this.totalCoursesCount = totalCoursesCount; return this; }
        public InstructorDashboardStatsDtoBuilder totalStudentsCount(long totalStudentsCount) { this.totalStudentsCount = totalStudentsCount; return this; }
        public InstructorDashboardStatsDtoBuilder activeLearnersCount(long activeLearnersCount) { this.activeLearnersCount = activeLearnersCount; return this; }
        public InstructorDashboardStatsDtoBuilder courseCompletionRate(double courseCompletionRate) { this.courseCompletionRate = courseCompletionRate; return this; }
        public InstructorDashboardStatsDtoBuilder averageRating(double averageRating) { this.averageRating = averageRating; return this; }
        public InstructorDashboardStatsDtoBuilder totalEarnings(double totalEarnings) { this.totalEarnings = totalEarnings; return this; }
        public InstructorDashboardStatsDtoBuilder instructorCourses(List<CourseDto> instructorCourses) { this.instructorCourses = instructorCourses; return this; }
        public InstructorDashboardStatsDtoBuilder recentStudents(List<StudentRosterDto> recentStudents) { this.recentStudents = recentStudents; return this; }
        public InstructorDashboardStatsDtoBuilder monthlyEnrollmentData(List<Integer> monthlyEnrollmentData) { this.monthlyEnrollmentData = monthlyEnrollmentData; return this; }
        public InstructorDashboardStatsDtoBuilder monthlyLabels(List<String> monthlyLabels) { this.monthlyLabels = monthlyLabels; return this; }

        public InstructorDashboardStatsDto build() {
            return new InstructorDashboardStatsDto(totalCoursesCount, totalStudentsCount, activeLearnersCount, courseCompletionRate, averageRating, totalEarnings, instructorCourses, recentStudents, monthlyEnrollmentData, monthlyLabels);
        }
    }

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

        public Long getStudentId() { return studentId; }
        public String getStudentName() { return studentName; }
        public String getStudentEmail() { return studentEmail; }
        public String getCourseTitle() { return courseTitle; }
        public String getEnrollmentDate() { return enrollmentDate; }
        public int getProgressPercentage() { return progressPercentage; }
        public Double getAssessmentScore() { return assessmentScore; }
        public String getStatus() { return status; }
        public String getLastActivity() { return lastActivity; }
    }
}
