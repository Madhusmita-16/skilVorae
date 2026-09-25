package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.DashboardSummaryDto;
import com.skilvorae.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for DashboardApiController in SkilVorae backend.
 */
@WebMvcTest(DashboardApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DashboardApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DashboardService dashboardService;

    private DashboardSummaryDto sampleSummary;

    @BeforeEach
    void setUp() {
        sampleSummary = DashboardSummaryDto.builder()
                .activeCoursesCount(3)
                .completedCoursesCount(2)
                .certificatesEarnedCount(2)
                .totalHoursSpent(42.5)
                .overallProgressPercent(68.4)
                .recentActivities(List.of())
                .enrolledCoursesSummary(List.of())
                .recommendedCourses(List.of())
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/dashboard/student")
    class GetStudentDashboardTests {

        @Test
        @DisplayName("Should return student dashboard summary for logged-in user")
        void getStudentDashboard_Success() throws Exception {
            when(dashboardService.getStudentDashboardSummary(any())).thenReturn(sampleSummary);

            mockMvc.perform(get("/api/v1/dashboard/student"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.activeCoursesCount").value(3))
                    .andExpect(jsonPath("$.completedCoursesCount").value(2))
                    .andExpect(jsonPath("$.overallProgressPercent").value(68.4));

            verify(dashboardService, times(1)).getStudentDashboardSummary(any());
        }
    }
}
