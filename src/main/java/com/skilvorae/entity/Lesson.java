package com.skilvorae.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private Integer durationMinutes;

    @Column(nullable = false)
    private Integer lessonOrder;

    private String videoUrl;
    
    private String pdfUrl;
    private String pptUrl;
    private String bookUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Module getModule() { return module; }
    public void setModule(Module module) { this.module = module; }
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
    public String getPdfUrl() { return pdfUrl; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }
    public String getPptUrl() { return pptUrl; }
    public void setPptUrl(String pptUrl) { this.pptUrl = pptUrl; }
    public String getBookUrl() { return bookUrl; }
    public void setBookUrl(String bookUrl) { this.bookUrl = bookUrl; }

    public static LessonBuilder builder() { return new LessonBuilder(); }

    public static class LessonBuilder {
        private Long id;
        private Module module;
        private String title;
        private String content;
        private Integer durationMinutes;
        private Integer lessonOrder;
        private String videoUrl;
        private String pdfUrl;
        private String pptUrl;
        private String bookUrl;

        public LessonBuilder id(Long id) { this.id = id; return this; }
        public LessonBuilder module(Module module) { this.module = module; return this; }
        public LessonBuilder title(String title) { this.title = title; return this; }
        public LessonBuilder content(String content) { this.content = content; return this; }
        public LessonBuilder durationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; return this; }
        public LessonBuilder lessonOrder(Integer lessonOrder) { this.lessonOrder = lessonOrder; return this; }
        public LessonBuilder videoUrl(String videoUrl) { this.videoUrl = videoUrl; return this; }
        public LessonBuilder pdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; return this; }
        public LessonBuilder pptUrl(String pptUrl) { this.pptUrl = pptUrl; return this; }
        public LessonBuilder bookUrl(String bookUrl) { this.bookUrl = bookUrl; return this; }

        public Lesson build() {
            return new Lesson(id, module, title, content, durationMinutes, lessonOrder, videoUrl, pdfUrl, pptUrl, bookUrl);
        }
    }
}
