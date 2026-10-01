package com.skilvorae.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto {
    private Long id;
    private Long courseId;
    private String courseTitle;
    private String courseThumbnailUrl;
    private String categoryName;
    private String instructorName;
    private String status;
    private Integer progressPercentage;
    private Long lastLessonId;
    private String lastLessonTitle;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }
    public String getCourseThumbnailUrl() { return courseThumbnailUrl; }
    public void setCourseThumbnailUrl(String courseThumbnailUrl) { this.courseThumbnailUrl = courseThumbnailUrl; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }
    public Long getLastLessonId() { return lastLessonId; }
    public void setLastLessonId(Long lastLessonId) { this.lastLessonId = lastLessonId; }
    public String getLastLessonTitle() { return lastLessonTitle; }
    public void setLastLessonTitle(String lastLessonTitle) { this.lastLessonTitle = lastLessonTitle; }
    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public static EnrollmentDtoBuilder builder() { return new EnrollmentDtoBuilder(); }

    public static class EnrollmentDtoBuilder {
        private Long id;
        private Long courseId;
        private String courseTitle;
        private String courseThumbnailUrl;
        private String categoryName;
        private String instructorName;
        private String status;
        private Integer progressPercentage;
        private Long lastLessonId;
        private String lastLessonTitle;
        private LocalDateTime enrolledAt;
        private LocalDateTime completedAt;

        public EnrollmentDtoBuilder id(Long id) { this.id = id; return this; }
        public EnrollmentDtoBuilder courseId(Long courseId) { this.courseId = courseId; return this; }
        public EnrollmentDtoBuilder courseTitle(String courseTitle) { this.courseTitle = courseTitle; return this; }
        public EnrollmentDtoBuilder courseThumbnailUrl(String courseThumbnailUrl) { this.courseThumbnailUrl = courseThumbnailUrl; return this; }
        public EnrollmentDtoBuilder categoryName(String categoryName) { this.categoryName = categoryName; return this; }
        public EnrollmentDtoBuilder instructorName(String instructorName) { this.instructorName = instructorName; return this; }
        public EnrollmentDtoBuilder status(String status) { this.status = status; return this; }
        public EnrollmentDtoBuilder progressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; return this; }
        public EnrollmentDtoBuilder lastLessonId(Long lastLessonId) { this.lastLessonId = lastLessonId; return this; }
        public EnrollmentDtoBuilder lastLessonTitle(String lastLessonTitle) { this.lastLessonTitle = lastLessonTitle; return this; }
        public EnrollmentDtoBuilder enrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; return this; }
        public EnrollmentDtoBuilder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public EnrollmentDto build() {
            return new EnrollmentDto(id, courseId, courseTitle, courseThumbnailUrl, categoryName, instructorName, status, progressPercentage, lastLessonId, lastLessonTitle, enrolledAt, completedAt);
        }
    }
}
