package com.skilvorae.entity;

import com.skilvorae.enums.Difficulty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(length = 2000, nullable = false)
    private String description;

    @Column(nullable = false)
    private String instructorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    private Double durationHours;

    private String thumbnailUrl;

    private Double rating;

    @Column(name = "enrollment_count")
    @Builder.Default
    private Integer enrollmentCount = 0;

    private Double price;
    private Double originalPrice;
    private Integer discountPercentage;

    @Builder.Default
    private Boolean isArchived = false;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("moduleOrder ASC")
    @Builder.Default
    private List<Module> modules = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    @Builder.Default
    private List<CourseReview> reviews = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.rating == null) {
            this.rating = 4.8;
        }
        if (this.enrollmentCount == null) {
            this.enrollmentCount = 0;
        }
    }

    public Boolean getIsArchived() {
        return isArchived != null ? isArchived : false;
    }
}
