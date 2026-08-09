package com.skilvorae.controller.api;

import com.skilvorae.dto.ApiResponse;
import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileApiController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Data
    public static class ProfileUpdateRequest {
        private String fullName;
        private String currentPassword;
        private String newPassword;
    }

    @PutMapping
    public ResponseEntity<ApiResponse<String>> updateProfile(
            @RequestBody ProfileUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required"));
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
        }

        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (request.getCurrentPassword() == null || !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Current password is incorrect"));
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully"));
    }
}
