package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.CourseDto;
import com.skilvorae.dto.CreateCourseRequest;
import com.skilvorae.dto.InstructorAnalyticsDto;
import com.skilvorae.enums.CourseCategory;
import com.skilvorae.enums.DifficultyLevel;
import com.skilvorae.service.CourseService;
import com.skilvorae.service.InstructorAnalyticsService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for InstructorCourseController in SkilVorae backend.
 */
@WebMvcTest(InstructorCourseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InstructorCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    @MockBean
    private InstructorAnalyticsService analyticsService;

    private CourseDto sampleCourse;
    private InstructorAnalyticsDto sampleAnalytics;

    @BeforeEach
    void setUp() {
        sampleCourse = CourseDto.builder()
                .id(10L)
                .title("Spring Security 6 Enterprise Mastery")
                .slug("spring-security-6-mastery")
                .shortDescription("Master modern OAuth2 and JWT authentication.")
                .category(CourseCategory.CYBERSECURITY)
                .level(DifficultyLevel.ADVANCED)
                .price(BigDecimal.valueOf(54.99))
                .isPublished(false)
                .build();

        sampleAnalytics = InstructorAnalyticsDto.builder()
                .totalEnrolledStudents(450)
                .activeCoursesCount(5)
                .totalRevenue(BigDecimal.valueOf(14500.00))
                .averageCourseRating(4.9)
                .monthlyEnrollments(List.of())
                .coursePerformances(List.of())
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/instructor/courses")
    class GetInstructorCoursesTests {

        @Test
        @DisplayName("Should return list of courses authored by logged-in instructor")
        void getInstructorCourses_Success() throws Exception {
            when(courseService.getInstructorCourses(any())).thenReturn(List.of(sampleCourse));

            mockMvc.perform(get("/api/v1/instructor/courses"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].title").value("Spring Security 6 Enterprise Mastery"))
                    .andExpect(jsonPath("$[0].isPublished").value(false));

            verify(courseService, times(1)).getInstructorCourses(any());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/instructor/courses")
    class CreateCourseTests {

        @Test
        @DisplayName("Should create a new course draft")
        void createCourse_Success() throws Exception {
            CreateCourseRequest request = new CreateCourseRequest();
            request.setTitle("Spring Security 6 Enterprise Mastery");
            request.setShortDescription("Master modern OAuth2 and JWT authentication.");
            request.setCategory(CourseCategory.CYBERSECURITY);
            request.setLevel(DifficultyLevel.ADVANCED);
            request.setPrice(BigDecimal.valueOf(54.99));

            when(courseService.createCourse(any(CreateCourseRequest.class), any())).thenReturn(sampleCourse);

            mockMvc.perform(post("/api/v1/instructor/courses")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(10))
                    .andExpect(jsonPath("$.price").value(54.99));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/instructor/analytics")
    class GetAnalyticsTests {

        @Test
        @DisplayName("Should return instructor performance analytics summary")
        void getAnalytics_Success() throws Exception {
            when(analyticsService.getInstructorAnalytics(any())).thenReturn(sampleAnalytics);

            mockMvc.perform(get("/api/v1/instructor/analytics"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalEnrolledStudents").value(450))
                    .andExpect(jsonPath("$.totalRevenue").value(14500.00));
        }
    }
}
