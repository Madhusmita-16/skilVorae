package com.skilvorae.service.impl;

import com.skilvorae.dto.AuthResponse;
import com.skilvorae.dto.ForgotPasswordRequest;
import com.skilvorae.dto.LoginRequest;
import com.skilvorae.dto.RegisterRequest;
import com.skilvorae.entity.User;
import com.skilvorae.enums.Role;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.exception.InvalidOtpException;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.security.JwtUtils;
import com.skilvorae.service.AuthService;
import com.skilvorae.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * Enterprise implementation of AuthService for SkilVorae EdTech platform managing user authentication,
 * JWT token generation, student/instructor registration, OTP email verification, and password resets.
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final MailService mailService;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils,
                           MailService mailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.mailService = mailService;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new BadRequestException("Email and password credentials must be provided.");
        }

        String normalizedEmail = request.getEmail().toLowerCase().trim();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", normalizedEmail));

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtils.generateToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setTokenType("Bearer");
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFirstName() + " " + user.getLastName());
        response.setRole(user.getRole().name());

        return response;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new BadRequestException("Registration request payload cannot be empty.");
        }

        String normalizedEmail = request.getEmail().toLowerCase().trim();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException("An account with email address " + normalizedEmail + " already exists.");
        }

        User user = new User();
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName() != null ? request.getFirstName().trim() : "Student");
        user.setLastName(request.getLastName() != null ? request.getLastName().trim() : "Learner");

        Role role = Role.STUDENT;
        if (request.getRole() != null) {
            try {
                role = Role.valueOf(request.getRole().toUpperCase().trim());
            } catch (Exception e) {
                role = Role.STUDENT;
            }
        }
        user.setRole(role);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.generateToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(savedUser.getEmail());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setTokenType("Bearer");
        response.setUserId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setFullName(savedUser.getFirstName() + " " + savedUser.getLastName());
        response.setRole(savedUser.getRole().name());

        return response;
    }

    @Override
    public void sendOtp(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email address is required to send verification OTP.");
        }

        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        String otp = String.format("%06d", new Random().nextInt(900000) + 100000);
        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        mailService.sendOtpEmail(user.getEmail(), otp);
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        if (email == null || otp == null) {
            throw new BadRequestException("Email and OTP code must be provided.");
        }

        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (user.getOtpCode() == null || !user.getOtpCode().equals(otp.trim())) {
            throw new InvalidOtpException("Invalid OTP code provided.");
        }

        if (user.getOtpExpiry() != null && LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            throw new InvalidOtpException("OTP code has expired. Please request a new one.");
        }

        user.setOtpCode(null);
        user.setOtpExpiry(null);
        user.setEmailVerified(true);
        userRepository.save(user);

        return true;
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        if (request == null || request.getEmail() == null) {
            throw new BadRequestException("Email address is required for password reset.");
        }

        sendOtp(request.getEmail());
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new BadRequestException("New password must be at least 6 characters long.");
        }
        // Reset password logic
    }
}
