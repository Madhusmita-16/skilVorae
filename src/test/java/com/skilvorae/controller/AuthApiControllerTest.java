package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.AuthResponse;
import com.skilvorae.dto.LoginRequest;
import com.skilvorae.dto.RefreshTokenRequest;
import com.skilvorae.dto.RegisterRequest;
import com.skilvorae.enums.Role;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.exception.UnauthorizedException;
import com.skilvorae.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for AuthApiController in SkilVorae Spring Boot backend.
 */
@WebMvcTest(AuthApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        authResponse = AuthResponse.builder()
                .token("jwt.sample.token")
                .refreshToken("refresh.sample.token")
                .userId(100L)
                .email("student@skilvorae.com")
                .firstName("Alex")
                .lastName("Morgan")
                .role(Role.STUDENT)
                .build();
    }

    @Nested
    @DisplayName("POST /api/v1/auth/login")
    class LoginEndpointTests {

        @Test
        @DisplayName("Should return 200 OK and JWT tokens for valid credentials")
        void login_Success() throws Exception {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("student@skilvorae.com");
            loginRequest.setPassword("Password123!");

            when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt.sample.token"))
                    .andExpect(jsonPath("$.email").value("student@skilvorae.com"))
                    .andExpect(jsonPath("$.role").value("STUDENT"));

            verify(authService, times(1)).login(any(LoginRequest.class));
        }

        @Test
        @DisplayName("Should return 401 Unauthorized for invalid user credentials")
        void login_Failure_InvalidCredentials() throws Exception {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("student@skilvorae.com");
            loginRequest.setPassword("WrongPassword!");

            when(authService.login(any(LoginRequest.class)))
                    .thenThrow(new UnauthorizedException("Invalid email or password."));

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return 400 Bad Request when email format is invalid")
        void login_Failure_InvalidEmailFormat() throws Exception {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("invalid-email-address");
            loginRequest.setPassword("Password123!");

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/auth/register")
    class RegisterEndpointTests {

        @Test
        @DisplayName("Should return 201 Created upon successful student registration")
        void register_Success() throws Exception {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail("newuser@skilvorae.com");
            registerRequest.setPassword("Password123!");
            registerRequest.setFirstName("Sarah");
            registerRequest.setLastName("Connor");

            when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.email").value("student@skilvorae.com"));

            verify(authService, times(1)).register(any(RegisterRequest.class));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when email is already registered")
        void register_Failure_DuplicateEmail() throws Exception {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail("existing@skilvorae.com");
            registerRequest.setPassword("Password123!");
            registerRequest.setFirstName("Sarah");
            registerRequest.setLastName("Connor");

            when(authService.register(any(RegisterRequest.class)))
                    .thenThrow(new BadRequestException("Email is already registered."));

            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/auth/refresh")
    class RefreshTokenEndpointTests {

        @Test
        @DisplayName("Should return new JWT access token when refresh token is valid")
        void refreshToken_Success() throws Exception {
            RefreshTokenRequest request = new RefreshTokenRequest();
            request.setRefreshToken("valid.refresh.token");

            when(authService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(authResponse);

            mockMvc.perform(post("/api/v1/auth/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt.sample.token"));

            verify(authService, times(1)).refreshToken(any(RefreshTokenRequest.class));
        }
    }
}
