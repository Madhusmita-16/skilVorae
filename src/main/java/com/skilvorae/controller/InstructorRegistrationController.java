package com.skilvorae.controller;

import com.skilvorae.entity.InstructorApplication;
import com.skilvorae.service.InstructorRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/instructor/apply")
@RequiredArgsConstructor
public class InstructorRegistrationController {

    private final InstructorRegistrationService registrationService;

    @GetMapping
    public String showApplicationForm(@ModelAttribute("application") InstructorApplication application) {
        return "instructor-register";
    }

    @PostMapping
    public String submitApplication(@ModelAttribute("application") InstructorApplication application, RedirectAttributes redirectAttributes) {
        try {
            registrationService.submitApplication(application);
            redirectAttributes.addFlashAttribute("successMessage", "Application submitted successfully! Our team will review it shortly.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/instructor/apply";
        }
    }
}
