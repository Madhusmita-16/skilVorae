package com.skilvorae.controller;

import com.skilvorae.service.ReportExportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for ExportApiController in SkilVorae backend.
 */
@WebMvcTest(ExportApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ExportApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportExportService exportService;

    @Nested
    @DisplayName("GET /api/v1/export/course/{courseId}/analytics/csv")
    class ExportCourseAnalyticsCsvTests {

        @Test
        @DisplayName("Should export course analytics CSV binary attachment")
        void exportCsv_Success() throws Exception {
            byte[] csvBytes = "Course Title,Total Students,Revenue\nJava Masterclass,120,$6000\n".getBytes();
            when(exportService.exportCourseAnalyticsCsv(eq(1L), any())).thenReturn(csvBytes);

            mockMvc.perform(get("/api/v1/export/course/1/analytics/csv"))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=course-1-analytics.csv"))
                    .andExpect(content().contentType("text/csv"))
                    .andExpect(content().bytes(csvBytes));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/export/student/progress/pdf")
    class ExportStudentProgressPdfTests {

        @Test
        @DisplayName("Should export student progress report as PDF attachment")
        void exportPdf_Success() throws Exception {
            byte[] pdfBytes = "%PDF-1.4 Mock PDF Binary Data".getBytes();
            when(exportService.exportStudentProgressPdf(any())).thenReturn(pdfBytes);

            mockMvc.perform(get("/api/v1/export/student/progress/pdf"))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student-progress-report.pdf"))
                    .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                    .andExpect(content().bytes(pdfBytes));
        }
    }
}
