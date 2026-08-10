package com.skilvorae.controller;

import com.skilvorae.dto.InstructorDashboardStatsDto;
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
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorWebController {

    private final UserRepository userRepository;
    private final DashboardService dashboardService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/dashboard")
    public String instructorDashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        if (user.getRole() != Role.INSTRUCTOR && user.getRole() != Role.ADMIN) {
            return "redirect:/dashboard";
        }

        InstructorDashboardStatsDto stats = dashboardService.getInstructorDashboardStats(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("stats", stats);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("activeTab", "instructor");

        return "instructor/dashboard";
    }

    @GetMapping("/courses")
    public String instructorCourses(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        return instructorDashboard(model, userDetails);
    }

    @GetMapping("/students")
    public String instructorStudents(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        return instructorDashboard(model, userDetails);
    }
}
