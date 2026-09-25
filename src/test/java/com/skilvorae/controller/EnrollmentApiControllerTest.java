package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.EnrollmentDto;
import com.skilvorae.enums.EnrollmentStatus;
import com.skilvorae.service.EnrollmentService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for EnrollmentApiController in SkilVorae backend.
 */
@WebMvcTest(EnrollmentApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EnrollmentApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EnrollmentService enrollmentService;

    private EnrollmentDto sampleEnrollment;

    @BeforeEach
    void setUp() {
        sampleEnrollment = EnrollmentDto.builder()
                .id(100L)
                .userId(10L)
                .courseId(1L)
                .courseTitle("Full Stack Web Development with React & Spring Boot")
                .enrolledAt(LocalDateTime.now())
                .status(EnrollmentStatus.ACTIVE)
                .progressPercent(35.0)
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/enrollments/my-courses")
    class GetMyEnrollmentsTests {

        @Test
        @DisplayName("Should return list of enrollments for authenticated student")
        void getMyEnrollments_Success() throws Exception {
            when(enrollmentService.getUserEnrollments(any())).thenReturn(List.of(sampleEnrollment));

            mockMvc.perform(get("/api/v1/enrollments/my-courses"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(100))
                    .andExpect(jsonPath("$[0].progressPercent").value(35.0));

            verify(enrollmentService, times(1)).getUserEnrollments(any());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/enrollments/course/{courseId}")
    class EnrollInCourseTests {

        @Test
        @DisplayName("Should enroll student into specified course")
        void enroll_Success() throws Exception {
            when(enrollmentService.enrollUserInCourse(eq(1L), any())).thenReturn(sampleEnrollment);

            mockMvc.perform(post("/api/v1/enrollments/course/1"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.courseId").value(1))
                    .andExpect(jsonPath("$.status").value("ACTIVE"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/enrollments/course/{courseId}/check")
    class CheckEnrollmentStatusTests {

        @Test
        @DisplayName("Should return boolean indicating if user is enrolled")
        void isEnrolled_Success() throws Exception {
            when(enrollmentService.isUserEnrolled(eq(1L), any())).thenReturn(true);

            mockMvc.perform(get("/api/v1/enrollments/course/1/check"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        }
    }
}
