package com.skilvorae.service;

import com.skilvorae.dto.AssessmentDto;
import com.skilvorae.entity.Assessment;
import com.skilvorae.repository.AssessmentRepository;
import com.skilvorae.repository.TestAttemptRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.AssessmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 test suite for AssessmentServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
public class AssessmentServiceTest {

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private TestAttemptRepository testAttemptRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AssessmentServiceImpl assessmentService;

    private Assessment assessment;

    @BeforeEach
    void setUp() {
        assessment = new Assessment();
        assessment.setId(10L);
        assessment.setTitle("Java Fundamentals Quiz");
        assessment.setPassPercentage(70);
        assessment.setQuestions(new ArrayList<>());
    }

    @Test
    @DisplayName("Should retrieve assessment by ID")
    void getAssessmentById_Success() {
        when(assessmentRepository.findById(10L)).thenReturn(Optional.of(assessment));

        AssessmentDto result = assessmentService.getAssessmentById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Java Fundamentals Quiz", result.getTitle());
    }
}
