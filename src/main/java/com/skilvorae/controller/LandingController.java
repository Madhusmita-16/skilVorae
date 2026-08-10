package com.skilvorae.controller;

import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LandingController {

    private final CourseService courseService;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String landingPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = getCurrentUserId(userDetails);
        model.addAttribute("featuredCourses", courseService.getFeaturedCourses(currentUserId));
        model.addAttribute("allCourses", courseService.getFeaturedCourses(currentUserId));
        model.addAttribute("categories", courseService.getAllCategories());
        model.addAttribute("isLoggedIn", userDetails != null);
        if (userDetails != null) {
            userRepository.findByEmail(userDetails.getUsername()).ifPresent(user -> model.addAttribute("user", user));
        }
        return "index";
    }

    private Long getCurrentUserId(UserDetails userDetails) {
        if (userDetails == null) return null;
        return userRepository.findByEmail(userDetails.getUsername()).map(User::getId).orElse(null);
    }
}
