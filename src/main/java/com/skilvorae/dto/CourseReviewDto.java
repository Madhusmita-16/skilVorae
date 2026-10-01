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
public class CourseReviewDto {
    private Long id;
    private Long courseId;
    private Long userId;
    private String userName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static CourseReviewDtoBuilder builder() { return new CourseReviewDtoBuilder(); }

    public static class CourseReviewDtoBuilder {
        private Long id;
        private Long courseId;
        private Long userId;
        private String userName;
        private Integer rating;
        private String comment;
        private LocalDateTime createdAt;

        public CourseReviewDtoBuilder id(Long id) { this.id = id; return this; }
        public CourseReviewDtoBuilder courseId(Long courseId) { this.courseId = courseId; return this; }
        public CourseReviewDtoBuilder userId(Long userId) { this.userId = userId; return this; }
        public CourseReviewDtoBuilder userName(String userName) { this.userName = userName; return this; }
        public CourseReviewDtoBuilder rating(Integer rating) { this.rating = rating; return this; }
        public CourseReviewDtoBuilder comment(String comment) { this.comment = comment; return this; }
        public CourseReviewDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public CourseReviewDto build() {
            return new CourseReviewDto(id, courseId, userId, userName, rating, comment, createdAt);
        }
    }
}
