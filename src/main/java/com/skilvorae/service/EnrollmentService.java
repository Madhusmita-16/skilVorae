package com.skilvorae.service;

import com.skilvorae.dto.EnrollmentDto;
import com.skilvorae.entity.*;
import com.skilvorae.enums.EnrollmentStatus;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserProgressRepository userProgressRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    public EnrollmentDto enrollCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (enrollmentRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new BadRequestException("You are already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .status(EnrollmentStatus.ACTIVE)
                .build();

        enrollment = enrollmentRepository.save(enrollment);

        // Increment enrollment count
        course.setEnrollmentCount((course.getEnrollmentCount() != null ? course.getEnrollmentCount() : 0) + 1);
        courseRepository.save(course);

        return mapToDto(enrollment);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDto> getUserEnrollments(Long userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);
        return enrollments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public EnrollmentDto mapToDto(Enrollment enrollment) {
        Long userId = enrollment.getUser().getId();
        Long courseId = enrollment.getCourse().getId();

        long totalLessons = lessonRepository.countLessonsByCourseId(courseId);
        long completedLessons = userProgressRepository.countByUserIdAndCourseIdAndCompletedTrue(userId, courseId);
        int progressPercentage = (totalLessons > 0) ? (int) Math.round(((double) completedLessons / totalLessons) * 100) : 0;

        Optional<UserProgress> lastProgress = userProgressRepository.findLastAccessedLesson(userId, courseId);
        Long lastLessonId = lastProgress.map(p -> p.getLesson().getId()).orElse(null);
        String lastLessonTitle = lastProgress.map(p -> p.getLesson().getTitle()).orElse(null);

        if (lastLessonId == null) {
            Optional<Lesson> firstLesson = lessonRepository.findFirstLessonByCourseId(courseId);
            lastLessonId = firstLesson.map(Lesson::getId).orElse(null);
            lastLessonTitle = firstLesson.map(Lesson::getTitle).orElse(null);
        }

        return EnrollmentDto.builder()
                .id(enrollment.getId())
                .courseId(courseId)
                .courseTitle(enrollment.getCourse().getTitle())
                .courseThumbnailUrl(enrollment.getCourse().getThumbnailUrl())
                .categoryName(enrollment.getCourse().getCategory() != null ? enrollment.getCourse().getCategory().getName() : "General")
                .instructorName(enrollment.getCourse().getInstructorName())
                .status(enrollment.getStatus().name())
                .progressPercentage(progressPercentage)
                .lastLessonId(lastLessonId)
                .lastLessonTitle(lastLessonTitle)
                .enrolledAt(enrollment.getEnrolledAt())
                .completedAt(enrollment.getCompletedAt())
                .build();
    }
}
