package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.AssessmentDto;
import com.skilvorae.dto.QuizSubmissionRequest;
import com.skilvorae.dto.QuizSubmissionResult;
import com.skilvorae.enums.AssessmentType;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.service.AssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for AssessmentApiController in SkilVorae backend.
 */
@WebMvcTest(AssessmentApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AssessmentApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssessmentService assessmentService;

    private AssessmentDto sampleAssessment;
    private QuizSubmissionResult sampleResult;

    @BeforeEach
    void setUp() {
        sampleAssessment = AssessmentDto.builder()
                .id(10L)
                .title("Midterm Quiz: React Fundamentals")
                .type(AssessmentType.QUIZ)
                .timeLimitMinutes(30)
                .passingScore(70.0)
                .totalQuestions(10)
                .totalPoints(100)
                .courseId(1L)
                .build();

        sampleResult = QuizSubmissionResult.builder()
                .assessmentId(10L)
                .score(85.0)
                .passed(true)
                .earnedPoints(85)
                .totalPoints(100)
                .correctAnswers(8)
                .totalQuestions(10)
                .feedback("Great job! You passed the midterm assessment.")
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/assessments/course/{courseId}")
    class GetCourseAssessmentsTests {

        @Test
        @DisplayName("Should return list of assessments for course")
        void getAssessmentsByCourse_Success() throws Exception {
            when(assessmentService.getAssessmentsByCourse(1L)).thenReturn(List.of(sampleAssessment));

            mockMvc.perform(get("/api/v1/assessments/course/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].title").value("Midterm Quiz: React Fundamentals"))
                    .andExpect(jsonPath("$[0].passingScore").value(70.0));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/assessments/{id}")
    class GetAssessmentByIdTests {

        @Test
        @DisplayName("Should return assessment by ID")
        void getAssessmentById_Success() throws Exception {
            when(assessmentService.getAssessmentById(10L)).thenReturn(sampleAssessment);

            mockMvc.perform(get("/api/v1/assessments/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(10))
                    .andExpect(jsonPath("$.type").value("QUIZ"));
        }

        @Test
        @DisplayName("Should return 404 Not Found when assessment ID is invalid")
        void getAssessmentById_NotFound() throws Exception {
            when(assessmentService.getAssessmentById(999L))
                    .thenThrow(new ResourceNotFoundException("Assessment", "id", 999L));

            mockMvc.perform(get("/api/v1/assessments/999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/assessments/{id}/submit")
    class SubmitQuizTests {

        @Test
        @DisplayName("Should process quiz submission and return scoring result")
        void submitQuiz_Success() throws Exception {
            QuizSubmissionRequest request = new QuizSubmissionRequest();
            request.setAnswers(Map.of(101L, 0, 102L, 2));

            when(assessmentService.submitQuiz(eq(10L), any(QuizSubmissionRequest.class), any()))
                    .thenReturn(sampleResult);

            mockMvc.perform(post("/api/v1/assessments/10/submit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.score").value(85.0))
                    .andExpect(jsonPath("$.passed").value(true));
        }
    }
}
