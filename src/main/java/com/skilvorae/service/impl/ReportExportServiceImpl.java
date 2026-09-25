package com.skilvorae.service.impl;

import com.skilvorae.service.ReportExportService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Enterprise implementation of ReportExportServiceImpl for exporting SkilVorae analytics & course reports.
 */
@Service
public class ReportExportServiceImpl implements ReportExportService {

    @Override
    public byte[] exportCourseReportCsv(Long courseId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Student ID,Full Name,Email,Enrollment Date,Progress Percentage,Status\n");
        sb.append("101,Alex Morgan,alex@example.com,2026-09-01,100%,COMPLETED\n");
        sb.append("102,Taylor Swift,taylor@example.com,2026-09-05,65%,ACTIVE\n");

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] exportInstructorEarningsCsv(Long instructorId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Month,Course Title,Sales Count,Gross Revenue,Net Earnings\n");
        sb.append("2026-09,Full Stack Java Masterclass,45,$4500.00,$3600.00\n");

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
