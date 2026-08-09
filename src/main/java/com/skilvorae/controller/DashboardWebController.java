package com.skilvorae.controller;

import com.skilvorae.dto.CertificateDto;
import com.skilvorae.dto.DashboardStatsDto;
import com.skilvorae.dto.EnrollmentDto;
import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.CertificateService;
import com.skilvorae.service.DashboardService;
import com.skilvorae.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardWebController {

    private final DashboardService dashboardService;
    private final EnrollmentService enrollmentService;
    private final CertificateService certificateService;
    private final UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        DashboardStatsDto stats = dashboardService.getDashboardStats(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("stats", stats);
        model.addAttribute("activeTab", "dashboard");
        return "dashboard/index";
    }

    @GetMapping("/my-courses")
    public String myCourses(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<EnrollmentDto> enrollments = enrollmentService.getUserEnrollments(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("activeTab", "my-courses");
        return "dashboard/my-courses";
    }

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<EnrollmentDto> enrollments = enrollmentService.getUserEnrollments(user.getId());
        List<CertificateDto> certificates = certificateService.getUserCertificates(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("certificates", certificates);
        model.addAttribute("activeTab", "profile");
        return "dashboard/profile";
    }

    @GetMapping("/certificates")
    public String certificates(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<CertificateDto> certificates = certificateService.getUserCertificates(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("certificates", certificates);
        model.addAttribute("activeTab", "certificates");
        return "dashboard/profile";
    }
}
