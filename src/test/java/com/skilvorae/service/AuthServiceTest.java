package com.skilvorae.service;

import com.skilvorae.dto.AuthResponse;
import com.skilvorae.dto.LoginRequest;
import com.skilvorae.dto.RegisterRequest;
import com.skilvorae.entity.User;
import com.skilvorae.enums.Role;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.security.JwtUtils;
import com.skilvorae.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 test suite for AuthServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private MailService mailService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(10L);
        user.setEmail("student@skilvorae.com");
        user.setFirstName("Alex");
        user.setLastName("Morgan");
        user.setRole(Role.STUDENT);
    }

    @Test
    @DisplayName("Should successfully authenticate user login and return JWT AuthResponse")
    void login_Success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("student@skilvorae.com");
        request.setPassword("Password123!");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByEmail("student@skilvorae.com")).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(authentication)).thenReturn("jwt.token.value");
        when(jwtUtils.generateRefreshToken("student@skilvorae.com")).thenReturn("refresh.token.value");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt.token.value", response.getToken());
        assertEquals("student@skilvorae.com", response.getEmail());
    }

    @Test
    @DisplayName("Should register new student account")
    void register_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("newstudent@skilvorae.com");
        request.setPassword("Password123!");
        request.setFirstName("Taylor");
        request.setLastName("Swift");

        when(userRepository.existsByEmail("newstudent@skilvorae.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(20L);
            return u;
        });
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateToken(authentication)).thenReturn("jwt.token");
        when(jwtUtils.generateRefreshToken(any())).thenReturn("refresh.token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("newstudent@skilvorae.com", response.getEmail());
    }
}
