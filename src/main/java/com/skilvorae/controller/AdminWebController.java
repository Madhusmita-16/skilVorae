package com.skilvorae.controller;

import com.skilvorae.dto.AdminDashboardStatsDto;
import com.skilvorae.entity.User;
import com.skilvorae.enums.Role;
import com.skilvorae.repository.CategoryRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminWebController {

    private final UserRepository userRepository;
    private final DashboardService dashboardService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        if (user.getRole() != Role.ADMIN) {
            return "redirect:/dashboard";
        }

        AdminDashboardStatsDto stats = dashboardService.getAdminDashboardStats();

        model.addAttribute("user", user);
        model.addAttribute("stats", stats);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("activeTab", "admin");

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String adminUsers(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        return adminDashboard(model, userDetails);
    }

    @GetMapping("/courses")
    public String adminCourses(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        return adminDashboard(model, userDetails);
    }

    @GetMapping("/certificates")
    public String adminCertificates(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        return adminDashboard(model, userDetails);
    }

    @GetMapping("/audit-logs")
    public String adminAuditLogs(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        return adminDashboard(model, userDetails);
    }
}
