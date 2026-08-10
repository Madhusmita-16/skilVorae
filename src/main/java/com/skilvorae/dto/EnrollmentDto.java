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
}
