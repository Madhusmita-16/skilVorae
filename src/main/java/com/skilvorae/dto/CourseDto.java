package com.skilvorae.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private String instructorName;
    private Long categoryId;
    private String categoryName;
    private String difficulty;
    private Double durationHours;
    private String thumbnailUrl;
    private Double rating;
    private Integer enrollmentCount;
    private Double price;
    private Double originalPrice;
    private Integer discountPercentage;
    private String formattedPrice;
    private Integer totalModules;
    private Integer totalLessons;
    private Boolean isEnrolled;
    private Integer progressPercentage;
    private List<ModuleDto> modules;
    private List<CourseReviewDto> reviews;
}
