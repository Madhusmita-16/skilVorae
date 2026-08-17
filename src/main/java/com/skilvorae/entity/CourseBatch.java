package com.skilvorae.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course_batches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;

    @Builder.Default
    private Integer capacity = 50;

    @Builder.Default
    private Integer enrolledCount = 0;

    @OneToMany(mappedBy = "courseBatch", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Schedule> schedules = new ArrayList<>();
}
