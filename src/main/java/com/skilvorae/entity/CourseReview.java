package com.skilvorae.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static CourseReviewBuilder builder() { return new CourseReviewBuilder(); }

    public static class CourseReviewBuilder {
        private Long id;
        private User user;
        private Course course;
        private Integer rating;
        private String comment;
        private LocalDateTime createdAt;

        public CourseReviewBuilder id(Long id) { this.id = id; return this; }
        public CourseReviewBuilder user(User user) { this.user = user; return this; }
        public CourseReviewBuilder course(Course course) { this.course = course; return this; }
        public CourseReviewBuilder rating(Integer rating) { this.rating = rating; return this; }
        public CourseReviewBuilder comment(String comment) { this.comment = comment; return this; }
        public CourseReviewBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public CourseReview build() {
            CourseReview review = new CourseReview(id, user, course, rating, comment, createdAt);
            if (review.getCreatedAt() == null) {
                review.setCreatedAt(LocalDateTime.now());
            }
            return review;
        }
    }
}
