package com.skilvorae.repository;

import com.skilvorae.entity.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {

    List<CourseReview> findByCourseIdOrderByCreatedAtDesc(Long courseId);

    @Query("SELECT AVG(r.rating) FROM CourseReview r WHERE r.course.id = :courseId")
    Double getAverageRatingByCourseId(@Param("courseId") Long courseId);

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
}
