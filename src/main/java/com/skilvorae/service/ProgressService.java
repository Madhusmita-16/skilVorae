package com.skilvorae.service;

import com.skilvorae.dto.ProgressUpdateDto;
import com.skilvorae.entity.*;
import com.skilvorae.enums.EnrollmentStatus;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final UserProgressRepository userProgressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    public int updateProgress(Long userId, Long courseId, ProgressUpdateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        Optional<UserProgress> existingProgress = userProgressRepository.findByUserIdAndCourseIdAndLessonId(
                userId, courseId, dto.getLessonId()
        );

        UserProgress userProgress;
        if (existingProgress.isPresent()) {
            userProgress = existingProgress.get();
            userProgress.setCompleted(dto.getCompleted());
            if (dto.getCompleted()) {
                userProgress.setCompletedAt(LocalDateTime.now());
            }
        } else {
            userProgress = UserProgress.builder()
                    .user(user)
                    .course(course)
                    .lesson(lesson)
                    .completed(dto.getCompleted())
                    .completedAt(dto.getCompleted() ? LocalDateTime.now() : null)
                    .build();
        }

        userProgressRepository.save(userProgress);

        // Calculate progress percentage
        long totalLessons = lessonRepository.countLessonsByCourseId(courseId);
        long completedLessons = userProgressRepository.countByUserIdAndCourseIdAndCompletedTrue(userId, courseId);
        int percentage = (totalLessons > 0) ? (int) Math.round(((double) completedLessons / totalLessons) * 100) : 0;

        // Auto-update enrollment status if 100% completed
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
        if (enrollmentOpt.isPresent()) {
            Enrollment enrollment = enrollmentOpt.get();
            if (percentage >= 100 && enrollment.getStatus() != EnrollmentStatus.COMPLETED) {
                enrollment.setStatus(EnrollmentStatus.COMPLETED);
                enrollment.setCompletedAt(LocalDateTime.now());
                enrollmentRepository.save(enrollment);
            }
        }

        return percentage;
    }

    public int getCourseProgressPercentage(Long userId, Long courseId) {
        long totalLessons = lessonRepository.countLessonsByCourseId(courseId);
        long completedLessons = userProgressRepository.countByUserIdAndCourseIdAndCompletedTrue(userId, courseId);
        return (totalLessons > 0) ? (int) Math.round(((double) completedLessons / totalLessons) * 100) : 0;
    }
}
