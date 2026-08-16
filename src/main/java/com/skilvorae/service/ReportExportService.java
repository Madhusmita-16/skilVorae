package com.skilvorae.service;

import com.skilvorae.entity.AuditLog;
import com.skilvorae.entity.Enrollment;
import com.skilvorae.repository.AuditLogRepository;
import com.skilvorae.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportExportService {

    private final EnrollmentRepository enrollmentRepository;
    private final AuditLogRepository auditLogRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional(readOnly = true)
    public String generateInstructorEnrollmentsCsv(Long instructorId) {
        StringBuilder csv = new StringBuilder();
        csv.append("Enrollment ID,Student Name,Student Email,Course Title,Enrolled Date,Progress (%),Status\n");

        List<Enrollment> enrollments = enrollmentRepository.findAll();
        for (Enrollment e : enrollments) {
            if (instructorId == null || e.getCourse().getInstructor().getId().equals(instructorId)) {
                csv.append(e.getId()).append(",")
                   .append(escapeCsv(e.getUser().getFullName())).append(",")
                   .append(escapeCsv(e.getUser().getEmail())).append(",")
                   .append(escapeCsv(e.getCourse().getTitle())).append(",")
                   .append(e.getEnrolledAt().format(DATE_FORMATTER)).append(",")
                   .append(e.getProgressPercentage()).append(",")
                   .append(e.isCompleted() ? "COMPLETED" : "ACTIVE").append("\n");
            }
        }
        return csv.toString();
    }

    @Transactional(readOnly = true)
    public String generateAdminAuditLogsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("Log ID,Action,User Email,IP Address,Details,Timestamp\n");

        List<AuditLog> logs = auditLogRepository.findTop50ByOrderByCreatedAtDesc();
        for (AuditLog log : logs) {
            csv.append(log.getId()).append(",")
               .append(escapeCsv(log.getAction())).append(",")
               .append(escapeCsv(log.getUserEmail() != null ? log.getUserEmail() : "GUEST")).append(",")
               .append(escapeCsv(log.getIpAddress() != null ? log.getIpAddress() : "127.0.0.1")).append(",")
               .append(escapeCsv(log.getDetails() != null ? log.getDetails() : "")).append(",")
               .append(log.getCreatedAt().format(DATE_FORMATTER)).append("\n");
        }
        return csv.toString();
    }

    private String escapeCsv(String data) {
        if (data == null) return "\"\"";
        String escaped = data.replaceAll("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
