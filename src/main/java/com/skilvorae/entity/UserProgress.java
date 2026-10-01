package com.skilvorae.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "course_id", "lesson_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(nullable = false)
    private Boolean completed;

    private LocalDateTime completedAt;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        if (Boolean.TRUE.equals(completed) && completedAt == null) {
            completedAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }
    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public static UserProgressBuilder builder() { return new UserProgressBuilder(); }

    public static class UserProgressBuilder {
        private Long id;
        private User user;
        private Course course;
        private Lesson lesson;
        private Boolean completed;
        private LocalDateTime completedAt;

        public UserProgressBuilder id(Long id) { this.id = id; return this; }
        public UserProgressBuilder user(User user) { this.user = user; return this; }
        public UserProgressBuilder course(Course course) { this.course = course; return this; }
        public UserProgressBuilder lesson(Lesson lesson) { this.lesson = lesson; return this; }
        public UserProgressBuilder completed(Boolean completed) { this.completed = completed; return this; }
        public UserProgressBuilder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public UserProgress build() {
            return new UserProgress(id, user, course, lesson, completed, completedAt);
        }
    }
}
