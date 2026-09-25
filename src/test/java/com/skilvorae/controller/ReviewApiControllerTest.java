package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.CreateReviewRequest;
import com.skilvorae.dto.ReviewDto;
import com.skilvorae.service.ReviewService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for ReviewApiController in SkilVorae backend.
 */
@WebMvcTest(ReviewApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReviewApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    private ReviewDto sampleReview;

    @BeforeEach
    void setUp() {
        sampleReview = ReviewDto.builder()
                .id(100L)
                .courseId(1L)
                .userFullName("Alex Morgan")
                .rating(5)
                .comment("Outstanding course on Spring Boot 3 enterprise features.")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/reviews/course/{courseId}")
    class GetCourseReviewsTests {

        @Test
        @DisplayName("Should return paginated list of course reviews")
        void getCourseReviews_Success() throws Exception {
            PageImpl<ReviewDto> page = new PageImpl<>(List.of(sampleReview));
            when(reviewService.getCourseReviews(eq(1L), any(PageRequest.class))).thenReturn(page);

            mockMvc.perform(get("/api/v1/reviews/course/1")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].rating").value(5))
                    .andExpect(jsonPath("$.content[0].userFullName").value("Alex Morgan"));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/reviews/course/{courseId}")
    class CreateReviewTests {

        @Test
        @DisplayName("Should post new course review")
        void createReview_Success() throws Exception {
            CreateReviewRequest request = new CreateReviewRequest();
            request.setRating(5);
            request.setComment("Outstanding course on Spring Boot 3 enterprise features.");

            when(reviewService.createReview(eq(1L), any(CreateReviewRequest.class), any()))
                    .thenReturn(sampleReview);

            mockMvc.perform(post("/api/v1/reviews/course/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(100))
                    .andExpect(jsonPath("$.rating").value(5));
        }
    }
}
