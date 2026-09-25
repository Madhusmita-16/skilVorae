package com.skilvorae.service.impl;

import com.skilvorae.dto.ProgressUpdateDto;
import com.skilvorae.entity.Enrollment;
import com.skilvorae.entity.Lesson;
import com.skilvorae.entity.UserProgress;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.LessonRepository;
import com.skilvorae.repository.UserProgressRepository;
import com.skilvorae.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Enterprise implementation of ProgressService managing student video playback progress,
 * lesson completion tracking, and course progress percentage updates.
 */
@Service
@Transactional
public class ProgressServiceImpl implements ProgressService {

    private final UserProgressRepository progressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public ProgressServiceImpl(UserProgressRepository progressRepository,
                               EnrollmentRepository enrollmentRepository,
                               LessonRepository lessonRepository) {
        this.progressRepository = progressRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public void updateLessonProgress(Long userId, Long lessonId, ProgressUpdateDto updateDto) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", lessonId));

        UserProgress progress = progressRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElseGet(() -> {
                    UserProgress p = new UserProgress();
                    p.setUserId(userId);
                    p.setLesson(lesson);
                    return p;
                });

        if (updateDto != null) {
            if (updateDto.getWatchTimeSeconds() != null) progress.setWatchTimeSeconds(updateDto.getWatchTimeSeconds());
            if (updateDto.getCompleted() != null && updateDto.getCompleted()) {
                progress.setCompleted(true);
                progress.setCompletedAt(LocalDateTime.now());
            }
        }

        progressRepository.save(progress);

        // Update overall course enrollment progress percentage
        if (lesson.getModule() != null && lesson.getModule().getCourse() != null) {
            Long courseId = lesson.getModule().getCourse().getId();
            Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId).orElse(null);
            if (enrollment != null) {
                int totalLessons = lessonRepository.countByCourseId(courseId);
                int completedLessons = progressRepository.countCompletedLessonsByUserIdAndCourseId(userId, courseId);
                int percentage = totalLessons > 0 ? (int) Math.round(((double) completedLessons / totalLessons) * 100) : 0;
                enrollment.setProgressPercentage(percentage);
                enrollmentRepository.save(enrollment);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int getCourseProgressPercentage(Long userId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId).orElse(null);
        return enrollment != null ? enrollment.getProgressPercentage() : 0;
    }
}
