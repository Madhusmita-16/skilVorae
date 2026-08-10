package com.skilvorae.controller.api;

import com.skilvorae.dto.*;
import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.CertificateService;
import com.skilvorae.service.CourseService;
import com.skilvorae.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardApiController {

    private final DashboardService dashboardService;
    private final CourseService courseService;
    private final CertificateService certificateService;
    private final UserRepository userRepository;

    @GetMapping("/student")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getStudentStats(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required"));
        }
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        DashboardStatsDto stats = dashboardService.getDashboardStats(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Student dashboard loaded", stats));
    }

    @GetMapping("/instructor")
    public ResponseEntity<ApiResponse<InstructorDashboardStatsDto>> getInstructorStats(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required"));
        }
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        InstructorDashboardStatsDto stats = dashboardService.getInstructorDashboardStats(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Instructor dashboard loaded", stats));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminDashboardStatsDto>> getAdminStats(@AuthenticationPrincipal UserDetails userDetails) {
        AdminDashboardStatsDto stats = dashboardService.getAdminDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Admin dashboard loaded", stats));
    }

    @PostMapping("/courses")
    public ResponseEntity<ApiResponse<CourseDto>> createCourse(
            @RequestBody CourseCreateRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required"));
        }

        if (request.getInstructorName() == null || request.getInstructorName().isBlank()) {
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
            request.setInstructorName(user.getFullName());
        }

        CourseDto created = courseService.createCourse(request);
        dashboardService.logAudit(userDetails.getUsername(), "COURSE_CREATED", "COURSE", created.getId(), "Created new course: " + created.getTitle());
        return ResponseEntity.ok(ApiResponse.success("Course created successfully!", created));
    }

    @DeleteMapping("/courses/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        courseService.deleteCourse(id);
        dashboardService.logAudit(userDetails.getUsername(), "COURSE_DELETED", "COURSE", id, "Deleted course ID: " + id);
        return ResponseEntity.ok(ApiResponse.success("Course deleted successfully!", "Course removed"));
    }

    @GetMapping("/verify-certificate/{certId}")
    public ResponseEntity<ApiResponse<CertificateDto>> verifyCertificate(@PathVariable String certId) {
        CertificateDto cert = certificateService.getCertificateByNumber(certId);
        if (cert != null) {
            return ResponseEntity.ok(ApiResponse.success("Certificate verified successfully!", cert));
        } else {
            return ResponseEntity.status(404).body(ApiResponse.error("Invalid or unverified Certificate ID"));
        }
    }
}
