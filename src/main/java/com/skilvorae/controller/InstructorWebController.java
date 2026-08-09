package com.skilvorae.controller;

import com.skilvorae.entity.User;
import com.skilvorae.enums.Role;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.UserRepository;
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
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @GetMapping("/dashboard")
    public String instructorDashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        if (user.getRole() != Role.INSTRUCTOR && user.getRole() != Role.ADMIN) {
            return "redirect:/dashboard";
        }

        model.addAttribute("user", user);
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("totalStudents", enrollmentRepository.count());
        model.addAttribute("activeTab", "instructor");

        return "instructor/dashboard";
    }
}
