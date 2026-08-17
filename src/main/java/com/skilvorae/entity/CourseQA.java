package com.skilvorae.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_qas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseQA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 1000)
    private String questionText;

    @Column(length = 2000)
    private String answerText;

    @Column(nullable = false, updatable = false)
    private LocalDateTime askedAt;

    private LocalDateTime answeredAt;

    @PrePersist
    protected void onCreate() {
        this.askedAt = LocalDateTime.now();
    }
}
