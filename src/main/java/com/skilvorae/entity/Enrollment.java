package com.skilvorae.entity;

import com.skilvorae.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "course_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

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
    @JoinColumn(name = "course_batch_id")
    private CourseBatch courseBatch;

    @Column(nullable = false)
    private LocalDateTime enrolledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status;

    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        if (this.enrolledAt == null) {
            this.enrolledAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = EnrollmentStatus.ACTIVE;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public CourseBatch getCourseBatch() { return courseBatch; }
    public void setCourseBatch(CourseBatch courseBatch) { this.courseBatch = courseBatch; }
    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public static EnrollmentBuilder builder() { return new EnrollmentBuilder(); }

    public static class EnrollmentBuilder {
        private Long id;
        private User user;
        private Course course;
        private CourseBatch courseBatch;
        private LocalDateTime enrolledAt;
        private EnrollmentStatus status;
        private LocalDateTime completedAt;

        public EnrollmentBuilder id(Long id) { this.id = id; return this; }
        public EnrollmentBuilder user(User user) { this.user = user; return this; }
        public EnrollmentBuilder course(Course course) { this.course = course; return this; }
        public EnrollmentBuilder courseBatch(CourseBatch courseBatch) { this.courseBatch = courseBatch; return this; }
        public EnrollmentBuilder enrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; return this; }
        public EnrollmentBuilder status(EnrollmentStatus status) { this.status = status; return this; }
        public EnrollmentBuilder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public Enrollment build() {
            Enrollment enrollment = new Enrollment(id, user, course, courseBatch, enrolledAt, status, completedAt);
            if (enrollment.getEnrolledAt() == null) {
                enrollment.setEnrolledAt(LocalDateTime.now());
            }
            if (enrollment.getStatus() == null) {
                enrollment.setStatus(EnrollmentStatus.ACTIVE);
            }
            return enrollment;
        }
    }
}
