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
public class ModuleDto {
    private Long id;
    private Long courseId;
    private String title;
    private Integer moduleOrder;
    private List<LessonDto> lessons;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getModuleOrder() { return moduleOrder; }
    public void setModuleOrder(Integer moduleOrder) { this.moduleOrder = moduleOrder; }
    public List<LessonDto> getLessons() { return lessons; }
    public void setLessons(List<LessonDto> lessons) { this.lessons = lessons; }

    public static ModuleDtoBuilder builder() { return new ModuleDtoBuilder(); }

    public static class ModuleDtoBuilder {
        private Long id;
        private Long courseId;
        private String title;
        private Integer moduleOrder;
        private List<LessonDto> lessons;

        public ModuleDtoBuilder id(Long id) { this.id = id; return this; }
        public ModuleDtoBuilder courseId(Long courseId) { this.courseId = courseId; return this; }
        public ModuleDtoBuilder title(String title) { this.title = title; return this; }
        public ModuleDtoBuilder moduleOrder(Integer moduleOrder) { this.moduleOrder = moduleOrder; return this; }
        public ModuleDtoBuilder lessons(List<LessonDto> lessons) { this.lessons = lessons; return this; }

        public ModuleDto build() {
            return new ModuleDto(id, courseId, title, moduleOrder, lessons);
        }
    }
}
