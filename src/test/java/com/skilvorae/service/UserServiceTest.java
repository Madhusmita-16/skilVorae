package com.skilvorae.service;

import com.skilvorae.dto.UserProfileDto;
import com.skilvorae.dto.UserUpdateRequest;
import com.skilvorae.entity.User;
import com.skilvorae.enums.Role;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 test suite for UserServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(10L);
        user.setEmail("student@skilvorae.com");
        user.setFirstName("Alex");
        user.setLastName("Morgan");
        user.setBio("Software engineering enthusiast.");
        user.setRole(Role.STUDENT);
        user.setIsEnabled(true);
    }

    @Nested
    @DisplayName("User Profile Management")
    class ProfileTests {

        @Test
        @DisplayName("Should return user profile DTO for authenticated email")
        void getUserProfile_Success() {
            when(userRepository.findByEmail("student@skilvorae.com")).thenReturn(Optional.of(user));

            UserProfileDto profile = userService.getUserProfile("student@skilvorae.com");

            assertNotNull(profile);
            assertEquals("Alex", profile.getFirstName());
            assertEquals("student@skilvorae.com", profile.getEmail());
            assertEquals(Role.STUDENT, profile.getRole());
        }

        @Test
        @DisplayName("Should update user profile details successfully")
        void updateUserProfile_Success() {
            UserUpdateRequest updateRequest = new UserUpdateRequest();
            updateRequest.setFirstName("Alexander");
            updateRequest.setLastName("Morgan Jr.");
            updateRequest.setBio("Updated bio description.");
            updateRequest.setHeadline("Full Stack Engineer");

            when(userRepository.findByEmail("student@skilvorae.com")).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

            UserProfileDto updated = userService.updateUserProfile("student@skilvorae.com", updateRequest);

            assertNotNull(updated);
            assertEquals("Alexander", updated.getFirstName());
            assertEquals("Updated bio description.", updated.getBio());
            verify(userRepository, times(1)).save(user);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user email does not exist")
        void getUserProfile_NotFound() {
            when(userRepository.findByEmail("missing@skilvorae.com")).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> userService.getUserProfile("missing@skilvorae.com"));
        }
    }

    @Nested
    @DisplayName("Admin User Operations")
    class AdminOperationsTests {

        @Test
        @DisplayName("Should fetch paginated list of all users")
        void getAllUsers_Success() {
            PageImpl<User> page = new PageImpl<>(List.of(user));
            when(userRepository.findAll(any(PageRequest.class))).thenReturn(page);

            Page<com.skilvorae.dto.UserDto> result = userService.getAllUsers(PageRequest.of(0, 10));

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals("student@skilvorae.com", result.getContent().get(0).getEmail());
        }

        @Test
        @DisplayName("Should toggle user enable/disable account status")
        void toggleUserAccountStatus_Success() {
            when(userRepository.findById(10L)).thenReturn(Optional.of(user));

            userService.toggleUserAccountStatus(10L, false);

            assertFalse(user.getIsEnabled());
            verify(userRepository, times(1)).save(user);
        }
    }
}
