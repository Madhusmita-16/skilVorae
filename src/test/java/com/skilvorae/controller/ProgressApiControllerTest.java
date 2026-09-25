package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.LessonProgressDto;
import com.skilvorae.dto.LessonProgressRequest;
import com.skilvorae.service.ProgressService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for ProgressApiController in SkilVorae backend.
 */
@WebMvcTest(ProgressApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProgressApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProgressService progressService;

    private LessonProgressDto sampleProgress;

    @BeforeEach
    void setUp() {
        sampleProgress = LessonProgressDto.builder()
                .id(50L)
                .lessonId(5L)
                .isCompleted(true)
                .lastWatchedPositionSeconds(450)
                .completedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("POST /api/v1/progress/lesson/{lessonId}")
    class RecordProgressTests {

        @Test
        @DisplayName("Should record lesson progress successfully")
        void recordProgress_Success() throws Exception {
            LessonProgressRequest request = new LessonProgressRequest();
            request.setCompleted(true);
            request.setLastWatchedPositionSeconds(450);

            when(progressService.updateLessonProgress(eq(5L), any(LessonProgressRequest.class), any()))
                    .thenReturn(sampleProgress);

            mockMvc.perform(post("/api/v1/progress/lesson/5")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isCompleted").value(true))
                    .andExpect(jsonPath("$.lastWatchedPositionSeconds").value(450));

            verify(progressService, times(1))
                    .updateLessonProgress(eq(5L), any(LessonProgressRequest.class), any());
        }
    }
}
