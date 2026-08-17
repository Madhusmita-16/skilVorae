package com.skilvorae.controller;

import com.skilvorae.entity.Assessment;
import com.skilvorae.entity.Question;
import com.skilvorae.entity.User;
import com.skilvorae.repository.AssessmentRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.PdfParsingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/instructor/assessment")
@RequiredArgsConstructor
public class InstructorAssessmentController {

    private final AssessmentRepository assessmentRepository;
    private final UserRepository userRepository;
    private final PdfParsingService pdfParsingService;

    @GetMapping("/create")
    public String showCreateAssessmentForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("assessment", new Assessment());
        return "instructor/create-assessment";
    }

    @PostMapping("/parse-pdf")
    public String parsePdfQuestions(@RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<Question> parsedQuestions = pdfParsingService.parsePdfQuestions(file);
            model.addAttribute("parsedQuestions", parsedQuestions);
            model.addAttribute("assessment", new Assessment());
            return "instructor/create-assessment";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to parse PDF: " + e.getMessage());
            return "redirect:/instructor/assessment/create";
        }
    }
}
