package com.skilvorae.dto;

import com.skilvorae.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateRequestDto {
    private String title;
    private String slug;
    private String description;
    private String instructorName;
    private Long categoryId;
    private Difficulty difficulty;
    private Double durationHours;
    private String thumbnailUrl;
    private Double price;
    private Double originalPrice;
    private boolean publish;

    private List<ModulePayload> modules;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModulePayload {
        private String title;
        private List<LessonPayload> lessons;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LessonPayload {
        private String title;
        private Integer durationMinutes;
        private String content;
    }
}
