package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.CourseDto;
import com.skilvorae.enums.CourseCategory;
import com.skilvorae.enums.DifficultyLevel;
import com.skilvorae.service.CourseRecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for RecommendationApiController in SkilVorae backend.
 */
@WebMvcTest(RecommendationApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RecommendationApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseRecommendationService recommendationService;

    private CourseDto recommendedCourse;

    @BeforeEach
    void setUp() {
        recommendedCourse = CourseDto.builder()
                .id(2L)
                .title("Advanced Microservices with Spring Cloud & Kubernetes")
                .slug("advanced-spring-cloud-kubernetes")
                .category(CourseCategory.CLOUD_COMPUTING)
                .level(DifficultyLevel.ADVANCED)
                .price(BigDecimal.valueOf(59.99))
                .rating(4.9)
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/recommendations/personalized")
    class PersonalizedRecommendationsTests {

        @Test
        @DisplayName("Should return AI personalized course recommendations for user")
        void getPersonalizedRecommendations_Success() throws Exception {
            when(recommendationService.getPersonalizedRecommendations(any(), eq(5)))
                    .thenReturn(List.of(recommendedCourse));

            mockMvc.perform(get("/api/v1/recommendations/personalized")
                            .param("limit", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].title").value("Advanced Microservices with Spring Cloud & Kubernetes"))
                    .andExpect(jsonPath("$[0].category").value("CLOUD_COMPUTING"));

            verify(recommendationService, times(1)).getPersonalizedRecommendations(any(), eq(5));
        }
    }
}
