package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.CourseDto;
import com.skilvorae.enums.CourseCategory;
import com.skilvorae.enums.DifficultyLevel;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for CourseApiController in SkilVorae backend.
 */
@WebMvcTest(CourseApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CourseApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    private CourseDto sampleCourse;

    @BeforeEach
    void setUp() {
        sampleCourse = CourseDto.builder()
                .id(1L)
                .title("Full Stack Web Development with React & Spring Boot")
                .slug("full-stack-react-spring-boot")
                .shortDescription("Master web application development from scratch.")
                .description("Comprehensive guide to React 18, Spring Boot 3, PostgreSQL, Docker, and CI/CD pipelines.")
                .category(CourseCategory.WEB_DEVELOPMENT)
                .level(DifficultyLevel.INTERMEDIATE)
                .price(BigDecimal.valueOf(49.99))
                .isPublished(true)
                .rating(4.8)
                .totalStudents(1250)
                .instructorName("Dr. Alan Turing")
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/courses")
    class GetAllCoursesTests {

        @Test
        @DisplayName("Should return paginated course list")
        void getAllCourses_Success() throws Exception {
            PageImpl<CourseDto> page = new PageImpl<>(List.of(sampleCourse));
            when(courseService.searchCourses(any(), any(), any(), any(), any(), any(PageRequest.class)))
                    .thenReturn(page);

            mockMvc.perform(get("/api/v1/courses")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].title").value("Full Stack Web Development with React & Spring Boot"))
                    .andExpect(jsonPath("$.content[0].price").value(49.99));
        }

        @Test
        @DisplayName("Should search courses with keyword filter")
        void searchCourses_WithKeyword() throws Exception {
            PageImpl<CourseDto> page = new PageImpl<>(List.of(sampleCourse));
            when(courseService.searchCourses(eq("React"), any(), any(), any(), any(), any(PageRequest.class)))
                    .thenReturn(page);

            mockMvc.perform(get("/api/v1/courses")
                            .param("keyword", "React"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].slug").value("full-stack-react-spring-boot"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/courses/{id}")
    class GetCourseByIdTests {

        @Test
        @DisplayName("Should return course details by valid ID")
        void getCourseById_Success() throws Exception {
            when(courseService.getCourseById(1L)).thenReturn(sampleCourse);

            mockMvc.perform(get("/api/v1/courses/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.instructorName").value("Dr. Alan Turing"));
        }

        @Test
        @DisplayName("Should return 404 Not Found when course ID does not exist")
        void getCourseById_NotFound() throws Exception {
            when(courseService.getCourseById(99L))
                    .thenThrow(new ResourceNotFoundException("Course", "id", 99L));

            mockMvc.perform(get("/api/v1/courses/99"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/courses/slug/{slug}")
    class GetCourseBySlugTests {

        @Test
        @DisplayName("Should return course details by valid slug")
        void getCourseBySlug_Success() throws Exception {
            when(courseService.getCourseBySlug("full-stack-react-spring-boot")).thenReturn(sampleCourse);

            mockMvc.perform(get("/api/v1/courses/slug/full-stack-react-spring-boot"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.slug").value("full-stack-react-spring-boot"));
        }
    }
}
