package com.skilvorae.service;

import com.skilvorae.dto.InstructorRegistrationRequest;
import com.skilvorae.dto.InstructorRegistrationResponse;
import com.skilvorae.entity.User;
import com.skilvorae.enums.Role;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.InstructorRegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 unit test suite for InstructorRegistrationServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class InstructorRegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;

    @InjectMocks
    private InstructorRegistrationServiceImpl registrationService;

    private InstructorRegistrationRequest request;

    @BeforeEach
    void setUp() {
        request = new InstructorRegistrationRequest();
        request.setEmail("prof.turing@skilvorae.com");
        request.setPassword("SecurePass123!");
        request.setFirstName("Alan");
        request.setLastName("Turing");
        request.setBio("Computer Scientist and Cryptanalyst.");
        request.setExpertise("Algorithms, Cryptography & Software Architecture");
        request.setHeadline("Pioneer of Modern Computing");
    }

    @Test
    @DisplayName("Should register new instructor application successfully")
    void registerInstructor_Success() {
        when(userRepository.existsByEmail("prof.turing@skilvorae.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(99L);
            return u;
        });

        InstructorRegistrationResponse response = registrationService.registerInstructor(request);

        assertNotNull(response);
        assertEquals(99L, response.getInstructorId());
        assertEquals("prof.turing@skilvorae.com", response.getEmail());
        assertEquals(Role.INSTRUCTOR, response.getRole());
        verify(mailService, times(1)).sendWelcomeEmail(eq("prof.turing@skilvorae.com"), eq("Alan"));
    }

    @Test
    @DisplayName("Should throw BadRequestException when registering existing email")
    void registerInstructor_DuplicateEmail() {
        when(userRepository.existsByEmail("prof.turing@skilvorae.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> registrationService.registerInstructor(request));
        verify(userRepository, never()).save(any());
    }
}
