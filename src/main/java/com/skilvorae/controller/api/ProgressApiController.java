package com.skilvorae.controller.api;

import com.skilvorae.dto.ApiResponse;
import com.skilvorae.dto.ProgressUpdateDto;
import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.ProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressApiController {

    private final ProgressService progressService;
    private final UserRepository userRepository;

    @PutMapping("/{courseId}")
    public ResponseEntity<ApiResponse<Integer>> updateProgress(
            @PathVariable Long courseId,
            @Valid @RequestBody ProgressUpdateDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required"));
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        int updatedPercentage = progressService.updateProgress(user.getId(), courseId, dto);
        return ResponseEntity.ok(ApiResponse.success("Lesson progress saved", updatedPercentage));
    }
}
