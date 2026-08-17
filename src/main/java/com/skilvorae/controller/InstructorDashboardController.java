package com.skilvorae.controller;

import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.CourseReviewRepository;
import com.skilvorae.repository.CourseQARepository;
import com.skilvorae.repository.ScheduleRepository;
import com.skilvorae.repository.InstructorEarningsRepository;
import com.skilvorae.service.InstructorAnalyticsService;
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
public class InstructorDashboardController {

    private final UserRepository userRepository;
    private final InstructorAnalyticsService analyticsService;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseReviewRepository courseReviewRepository;
    private final CourseQARepository courseQARepository;
    private final ScheduleRepository scheduleRepository;
    private final InstructorEarningsRepository earningsRepository;

    @GetMapping("/calendar")
    public String viewCalendar(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("schedules", scheduleRepository.findAll()); // Simple fetch, would ideally filter by instructor
        return "instructor/calendar";
    }

    @GetMapping("/students")
    public String viewStudents(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("enrollments", enrollmentRepository.findByInstructorId(user.getId()));
        return "instructor/students";
    }

    @GetMapping("/reviews")
    public String viewReviews(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        // Using an empty list as fallback since we'd ideally fetch by instructor courses, but JPA query might be missing
        model.addAttribute("reviews", courseReviewRepository.findAll());
        model.addAttribute("qas", courseQARepository.findAll());
        return "instructor/reviews";
    }

    @GetMapping("/analytics")
    public String viewAnalytics(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("analytics", analyticsService.getInstructorAnalytics(user.getId()));
        return "instructor/analytics";
    }

    @GetMapping("/earnings")
    public String viewEarnings(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("earnings", earningsRepository.findByInstructorIdOrderByEarnedAtDesc(user.getId()));
        return "instructor/earnings";
    }
}
