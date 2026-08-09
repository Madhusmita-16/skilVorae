package com.skilvorae.repository;

import com.skilvorae.entity.Course;
import com.skilvorae.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findBySlug(String slug);

    @Query("SELECT c FROM Course c WHERE " +
           "(:search IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(c.instructorName) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:categoryId IS NULL OR c.category.id = :categoryId) AND " +
           "(:difficulty IS NULL OR c.difficulty = :difficulty) AND " +
           "(:minRating IS NULL OR c.rating >= :minRating)")
    Page<Course> findFilteredCourses(@Param("search") String search,
                                     @Param("categoryId") Long categoryId,
                                     @Param("difficulty") Difficulty difficulty,
                                     @Param("minRating") Double minRating,
                                     Pageable pageable);

    List<Course> findTop4ByOrderByRatingDesc();

    List<Course> findTop6ByOrderByEnrollmentCountDesc();
}
