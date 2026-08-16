package com.skilvorae.controller.api;

import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.ReportExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exports")
@RequiredArgsConstructor
public class ExportApiController {

    private final ReportExportService reportExportService;
    private final UserRepository userRepository;

    @GetMapping("/instructor/enrollments")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<byte[]> exportInstructorEnrollments(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        Long instructorId = (user != null && user.getRole().name().equals("INSTRUCTOR")) ? user.getId() : null;

        String csvData = reportExportService.generateInstructorEnrollmentsCsv(instructorId);
        byte[] bytes = csvData.getBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=skilvorae_student_roster.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(bytes);
    }

    @GetMapping("/admin/audit-logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportAuditLogs() {
        String csvData = reportExportService.generateAdminAuditLogsCsv();
        byte[] bytes = csvData.getBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=skilvorae_audit_logs.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(bytes);
    }
}
