package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.CreateDiscussionRequest;
import com.skilvorae.dto.DiscussionThreadDto;
import com.skilvorae.service.DiscussionService;
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
 * Controller integration unit test suite for DiscussionApiController in SkilVorae backend.
 */
@WebMvcTest(DiscussionApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DiscussionApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DiscussionService discussionService;

    private DiscussionThreadDto sampleThread;

    @BeforeEach
    void setUp() {
        sampleThread = DiscussionThreadDto.builder()
                .id(50L)
                .courseId(1L)
                .title("Question about Spring Security filter order")
                .content("Should JwtAuthenticationFilter execute before UsernamePasswordAuthenticationFilter?")
                .authorName("Alex Morgan")
                .repliesCount(3)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/discussions/course/{courseId}")
    class GetCourseDiscussionsTests {

        @Test
        @DisplayName("Should return list of discussion threads for a course")
        void getCourseDiscussions_Success() throws Exception {
            when(discussionService.getCourseDiscussions(1L)).thenReturn(List.of(sampleThread));

            mockMvc.perform(get("/api/v1/discussions/course/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].title").value("Question about Spring Security filter order"))
                    .andExpect(jsonPath("$[0].authorName").value("Alex Morgan"));

            verify(discussionService, times(1)).getCourseDiscussions(1L);
        }
    }

    @Nested
    @DisplayName("POST /api/v1/discussions/course/{courseId}")
    class CreateDiscussionTests {

        @Test
        @DisplayName("Should post new discussion question thread")
        void createDiscussion_Success() throws Exception {
            CreateDiscussionRequest request = new CreateDiscussionRequest();
            request.setTitle("Question about Spring Security filter order");
            request.setContent("Should JwtAuthenticationFilter execute before UsernamePasswordAuthenticationFilter?");

            when(discussionService.createDiscussion(eq(1L), any(CreateDiscussionRequest.class), any()))
                    .thenReturn(sampleThread);

            mockMvc.perform(post("/api/v1/discussions/course/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(50))
                    .andExpect(jsonPath("$.repliesCount").value(3));
        }
    }
}
