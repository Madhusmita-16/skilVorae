package com.skilvorae.repository;

import com.skilvorae.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByModuleIdOrderByLessonOrderAsc(Long moduleId);
    
    @Query("SELECT l FROM Lesson l WHERE l.module.course.id = :courseId ORDER BY l.module.moduleOrder ASC, l.lessonOrder ASC")
    List<Lesson> findAllLessonsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.module.course.id = :courseId")
    long countLessonsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT l FROM Lesson l WHERE l.module.course.id = :courseId ORDER BY l.module.moduleOrder ASC, l.lessonOrder ASC LIMIT 1")
    Optional<Lesson> findFirstLessonByCourseId(@Param("courseId") Long courseId);
}
