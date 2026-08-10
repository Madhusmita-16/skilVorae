package com.skilvorae.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;

    private String role;

    private String phone;

    private String qualification;

    private String areaOfInterest;

    private String expertise;

    private Integer yearsOfExperience;

    private String bio;

    private String adminCode;
}
