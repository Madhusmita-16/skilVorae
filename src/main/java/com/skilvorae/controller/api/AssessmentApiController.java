package com.skilvorae.controller.api;

import com.skilvorae.dto.*;
import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentApiController {

    private final AssessmentService assessmentService;
    private final UserRepository userRepository;

    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<AssessmentDto>> getAssessment(
            @PathVariable Long courseId) {
        AssessmentDto dto = assessmentService.getAssessmentByCourseId(courseId, false);
        return ResponseEntity.ok(ApiResponse.success("Assessment questions loaded", dto));
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<TestResultDto>> submitAssessment(
            @RequestBody AssessmentSubmitDto submitDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required to submit assessment"));
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        TestResultDto result = assessmentService.submitAssessment(user.getId(), submitDto);
        return ResponseEntity.ok(ApiResponse.success("Assessment submitted successfully", result));
    }
}
