package com.skilvorae.repository;

import com.skilvorae.entity.Enrollment;
import com.skilvorae.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    List<Enrollment> findByCourseId(Long courseId);
    List<Enrollment> findByUserId(Long userId);
    List<Enrollment> findByUserIdAndStatus(Long userId, EnrollmentStatus status);
    long countByUserId(Long userId);
    long countByUserIdAndStatus(Long userId, EnrollmentStatus status);
    long countByStatus(EnrollmentStatus status);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course.instructor.id = :instructorId")
    List<Enrollment> findByInstructorId(@Param("instructorId") Long instructorId);
}
