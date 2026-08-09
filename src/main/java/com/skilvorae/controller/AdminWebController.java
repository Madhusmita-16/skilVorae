package com.skilvorae.controller;

import com.skilvorae.entity.User;
import com.skilvorae.enums.Role;
import com.skilvorae.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AssessmentRepository assessmentRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        if (user.getRole() != Role.ADMIN) {
            return "redirect:/dashboard";
        }

        model.addAttribute("user", user);
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalCourses", courseRepository.count());
        model.addAttribute("totalEnrollments", enrollmentRepository.count());
        model.addAttribute("totalCategories", categoryRepository.count());
        model.addAttribute("usersList", userRepository.findAll());
        model.addAttribute("coursesList", courseRepository.findAll());
        model.addAttribute("activeTab", "admin");

        return "admin/dashboard";
    }
}
