package com.skilvorae.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class ForgotPasswordRequest {

    @Data
    public static class EmailStep {
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        private String email;
    }

    @Data
    public static class VerifyStep {
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        private String email;

        @NotBlank(message = "OTP is required")
        @Size(min = 6, max = 6, message = "OTP must be 6 digits")
        private String otp;
    }

    @Data
    public static class ResetStep {
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        private String email;

        @NotBlank(message = "OTP is required")
        private String otp;

        @NotBlank(message = "New password is required")
        @Size(min = 6, max = 50, message = "Password must be at least 6 characters long")
        private String newPassword;

        @NotBlank(message = "Password confirmation is required")
        private String confirmPassword;
    }
}
