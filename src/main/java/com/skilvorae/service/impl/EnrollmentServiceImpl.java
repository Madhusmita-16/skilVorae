package com.skilvorae.service.impl;

import com.skilvorae.dto.EnrollmentDto;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.Enrollment;
import com.skilvorae.entity.User;
import com.skilvorae.enums.EnrollmentStatus;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enterprise implementation of EnrollmentService managing student course enrollments,
 * payment verifications, and active course subscriptions for SkilVorae.
 */
@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 CourseRepository courseRepository,
                                 UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public EnrollmentDto enrollCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        if (enrollmentRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new BadRequestException("You are already enrolled in this course.");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setProgressPercentage(0);

        course.setEnrollmentCount(course.getEnrollmentCount() + 1);
        courseRepository.save(course);

        Enrollment saved = enrollmentRepository.save(enrollment);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDto> getUserEnrollments(Long userId) {
        return enrollmentRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserEnrolled(Long userId, Long courseId) {
        return enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
    }

    private EnrollmentDto mapToDto(Enrollment e) {
        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(e.getId());
        dto.setUserId(e.getUser() != null ? e.getUser().getId() : null);
        dto.setCourseId(e.getCourse() != null ? e.getCourse().getId() : null);
        dto.setCourseTitle(e.getCourse() != null ? e.getCourse().getTitle() : "");
        dto.setCourseThumbnail(e.getCourse() != null ? e.getCourse().getThumbnailUrl() : "");
        dto.setStatus(e.getStatus() != null ? e.getStatus().name() : "ACTIVE");
        dto.setEnrolledAt(e.getEnrolledAt());
        dto.setProgressPercentage(e.getProgressPercentage());
        return dto;
    }
}
