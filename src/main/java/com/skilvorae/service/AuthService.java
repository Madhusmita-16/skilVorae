package com.skilvorae.service;

import com.skilvorae.dto.*;
import com.skilvorae.entity.User;
import com.skilvorae.enums.Role;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.exception.InvalidOtpException;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final MailService mailService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("An account with this email already exists");
        }

        Role userRole = Role.STUDENT;
        if (request.getRole() != null) {
            try {
                userRole = Role.valueOf(request.getRole().toUpperCase().trim());
            } catch (IllegalArgumentException ignored) {
                userRole = Role.STUDENT;
            }
        }

        if (userRole == Role.ADMIN) {
            String adminCode = request.getAdminCode();
            if (adminCode == null || (!adminCode.trim().equalsIgnoreCase("SKILVORAE-ADMIN-2026") && !adminCode.trim().equalsIgnoreCase("ADM-SECRET-2026"))) {
                throw new BadRequestException("Invalid or missing Admin Verification Code. Admin accounts require authorization.");
            }
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .phone(request.getPhone())
                .qualification(request.getQualification())
                .areaOfInterest(request.getAreaOfInterest())
                .expertise(request.getExpertise())
                .yearsOfExperience(request.getYearsOfExperience())
                .bio(request.getBio())
                .build();

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String jwtToken = jwtUtils.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .tokenType("Bearer")
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase().trim(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String jwtToken = jwtUtils.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .tokenType("Bearer")
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Transactional
    public void generateForgotPasswordOtp(String email) {
        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with this email address"));

        // Generate 6-digit OTP
        SecureRandom random = new SecureRandom();
        String otp = String.format("%06d", random.nextInt(1000000));

        user.setResetOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        mailService.sendOtpEmail(user.getEmail(), otp);
    }

    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getResetOtp() == null || !user.getResetOtp().equals(otp)) {
            throw new InvalidOtpException("Invalid OTP code");
        }

        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP code has expired. Please request a new one.");
        }

        return true;
    }

    @Transactional
    public void resetPassword(ForgotPasswordRequest.ResetStep request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        verifyOtp(request.getEmail(), request.getOtp());

        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
    }
}
