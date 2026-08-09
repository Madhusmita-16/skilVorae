package com.skilvorae.controller.api;

import com.skilvorae.dto.*;
import com.skilvorae.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Account registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Logged in successfully", response));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest.EmailStep request) {
        authService.generateForgotPasswordOtp(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("6-digit OTP code has been sent to your email address"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Boolean>> verifyOtp(@Valid @RequestBody ForgotPasswordRequest.VerifyStep request) {
        boolean valid = authService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(ApiResponse.success("OTP verified successfully", valid));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ForgotPasswordRequest.ResetStep request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully. You can now log in with your new password."));
    }
}
