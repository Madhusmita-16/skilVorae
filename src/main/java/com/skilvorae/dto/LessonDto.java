package com.skilvorae.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private Long id;
    private Long moduleId;
    private String title;
    private String content;
    private Integer durationMinutes;
    private Integer lessonOrder;
    private String videoUrl;
    private Boolean isCompleted;
}
