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
}
