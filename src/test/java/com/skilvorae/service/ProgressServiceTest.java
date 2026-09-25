package com.skilvorae.service;

import com.skilvorae.dto.ProgressUpdateDto;
import com.skilvorae.entity.Enrollment;
import com.skilvorae.entity.Lesson;
import com.skilvorae.entity.Module;
import com.skilvorae.entity.UserProgress;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.LessonRepository;
import com.skilvorae.repository.UserProgressRepository;
import com.skilvorae.service.impl.ProgressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 test suite for ProgressServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
public class ProgressServiceTest {

    @Mock
    private UserProgressRepository progressRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private ProgressServiceImpl progressService;

    private Lesson lesson;

    @BeforeEach
    void setUp() {
        lesson = new Lesson();
        lesson.setId(20L);
    }

    @Test
    @DisplayName("Should update lesson watch progress and save user progress")
    void updateLessonProgress_Success() {
        ProgressUpdateDto updateDto = new ProgressUpdateDto();
        updateDto.setWatchTimeSeconds(300);
        updateDto.setCompleted(true);

        when(lessonRepository.findById(20L)).thenReturn(Optional.of(lesson));
        when(progressRepository.findByUserIdAndLessonId(1L, 20L)).thenReturn(Optional.empty());

        progressService.updateLessonProgress(1L, 20L, updateDto);

        verify(progressRepository, times(1)).save(any(UserProgress.class));
    }
}
