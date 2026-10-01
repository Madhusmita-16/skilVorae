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

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public Double getDurationHours() { return durationHours; }
    public void setDurationHours(Double durationHours) { this.durationHours = durationHours; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(Double originalPrice) { this.originalPrice = originalPrice; }
    public boolean isPublish() { return publish; }
    public void setPublish(boolean publish) { this.publish = publish; }
    public List<ModulePayload> getModules() { return modules; }
    public void setModules(List<ModulePayload> modules) { this.modules = modules; }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModulePayload {
        private String title;
        private List<LessonPayload> lessons;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public List<LessonPayload> getLessons() { return lessons; }
        public void setLessons(List<LessonPayload> lessons) { this.lessons = lessons; }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LessonPayload {
        private String title;
        private Integer durationMinutes;
        private String content;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Integer getDurationMinutes() { return durationMinutes; }
        public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
