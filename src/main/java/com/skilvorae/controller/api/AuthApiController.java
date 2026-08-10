package com.skilvorae.controller.api;

import com.skilvorae.dto.*;
import com.skilvorae.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthResponse authResp = authService.register(request);
        setJwtCookie(response, authResp.getToken());
        return ResponseEntity.ok(ApiResponse.success("Account registered successfully", authResp));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResponse authResp = authService.login(request);
        setJwtCookie(response, authResp.getToken());
        return ResponseEntity.ok(ApiResponse.success("Logged in successfully", authResp));
    }

    private void setJwtCookie(HttpServletResponse response, String token) {
        if (token != null) {
            Cookie cookie = new Cookie("SKILVORAE_JWT", token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
            response.addCookie(cookie);
        }
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
