package com.skilvorae.repository;

import com.skilvorae.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUserIdAndCourseIdAndLessonId(Long userId, Long courseId, Long lessonId);
    List<UserProgress> findByUserIdAndCourseId(Long userId, Long courseId);
    long countByUserIdAndCourseIdAndCompletedTrue(Long userId, Long courseId);

    @Query("SELECT up.lesson.id FROM UserProgress up WHERE up.user.id = :userId AND up.course.id = :courseId AND up.completed = true")
    List<Long> findCompletedLessonIdsByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Query("SELECT up FROM UserProgress up WHERE up.user.id = :userId AND up.course.id = :courseId ORDER BY up.completedAt DESC LIMIT 1")
    Optional<UserProgress> findLastAccessedLesson(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
