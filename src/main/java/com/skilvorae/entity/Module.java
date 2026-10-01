package com.skilvorae.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer moduleOrder;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("lessonOrder ASC")
    @Builder.Default
    private List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    @Builder.Default
    private List<Assignment> assignments = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getModuleOrder() { return moduleOrder; }
    public void setModuleOrder(Integer moduleOrder) { this.moduleOrder = moduleOrder; }
    public List<Lesson> getLessons() { return lessons; }
    public void setLessons(List<Lesson> lessons) { this.lessons = lessons; }
    public List<Assignment> getAssignments() { return assignments; }
    public void setAssignments(List<Assignment> assignments) { this.assignments = assignments; }

    public static ModuleBuilder builder() { return new ModuleBuilder(); }

    public static class ModuleBuilder {
        private Long id;
        private Course course;
        private String title;
        private Integer moduleOrder;
        private List<Lesson> lessons = new ArrayList<>();
        private List<Assignment> assignments = new ArrayList<>();

        public ModuleBuilder id(Long id) { this.id = id; return this; }
        public ModuleBuilder course(Course course) { this.course = course; return this; }
        public ModuleBuilder title(String title) { this.title = title; return this; }
        public ModuleBuilder moduleOrder(Integer moduleOrder) { this.moduleOrder = moduleOrder; return this; }
        public ModuleBuilder lessons(List<Lesson> lessons) { this.lessons = lessons; return this; }
        public ModuleBuilder assignments(List<Assignment> assignments) { this.assignments = assignments; return this; }

        public Module build() {
            return new Module(id, course, title, moduleOrder, lessons, assignments);
        }
    }
}
