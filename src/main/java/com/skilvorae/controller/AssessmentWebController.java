package com.skilvorae.controller;

import com.skilvorae.dto.AssessmentDto;
import com.skilvorae.dto.TestResultDto;
import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class AssessmentWebController {

    private final AssessmentService assessmentService;
    private final UserRepository userRepository;

    @GetMapping("/assessments/{courseId}")
    public String quizPage(@PathVariable Long courseId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        AssessmentDto assessment = assessmentService.getAssessmentByCourseId(courseId, false);

        model.addAttribute("assessment", assessment);
        model.addAttribute("user", user);
        return "assessment/quiz";
    }

    @GetMapping("/assessments/{courseId}/results/{attemptId}")
    public String resultPage(
            @PathVariable Long courseId,
            @PathVariable Long attemptId,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        TestResultDto result = assessmentService.getAttemptResult(attemptId, user.getId());

        model.addAttribute("result", result);
        model.addAttribute("user", user);
        return "assessment/result";
    }
}
