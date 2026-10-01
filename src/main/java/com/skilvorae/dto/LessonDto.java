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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getModuleId() { return moduleId; }
    public void setModuleId(Long moduleId) { this.moduleId = moduleId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public Integer getLessonOrder() { return lessonOrder; }
    public void setLessonOrder(Integer lessonOrder) { this.lessonOrder = lessonOrder; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }

    public static LessonDtoBuilder builder() { return new LessonDtoBuilder(); }

    public static class LessonDtoBuilder {
        private Long id;
        private Long moduleId;
        private String title;
        private String content;
        private Integer durationMinutes;
        private Integer lessonOrder;
        private String videoUrl;
        private Boolean isCompleted;

        public LessonDtoBuilder id(Long id) { this.id = id; return this; }
        public LessonDtoBuilder moduleId(Long moduleId) { this.moduleId = moduleId; return this; }
        public LessonDtoBuilder title(String title) { this.title = title; return this; }
        public LessonDtoBuilder content(String content) { this.content = content; return this; }
        public LessonDtoBuilder durationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; return this; }
        public LessonDtoBuilder lessonOrder(Integer lessonOrder) { this.lessonOrder = lessonOrder; return this; }
        public LessonDtoBuilder videoUrl(String videoUrl) { this.videoUrl = videoUrl; return this; }
        public LessonDtoBuilder isCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; return this; }

        public LessonDto build() {
            return new LessonDto(id, moduleId, title, content, durationMinutes, lessonOrder, videoUrl, isCompleted);
        }
    }
}
