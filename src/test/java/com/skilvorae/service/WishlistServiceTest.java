package com.skilvorae.service;

import com.skilvorae.dto.CourseDto;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.User;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.WishlistServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 unit test suite for WishlistServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class WishlistServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    private User user;
    private Course course;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(10L);
        user.setEmail("student@skilvorae.com");
        user.setWishlist(new HashSet<>());

        course = new Course();
        course.setId(5L);
        course.setTitle("Cloud Native Microservices");
    }

    @Test
    @DisplayName("Should add course to user wishlist")
    void addToWishlist_Success() {
        when(userRepository.findByEmail("student@skilvorae.com")).thenReturn(Optional.of(user));
        when(courseRepository.findById(5L)).thenReturn(Optional.of(course));

        wishlistService.addToWishlist(5L, "student@skilvorae.com");

        assertTrue(user.getWishlist().contains(course));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should remove course from user wishlist")
    void removeFromWishlist_Success() {
        user.getWishlist().add(course);
        when(userRepository.findByEmail("student@skilvorae.com")).thenReturn(Optional.of(user));
        when(courseRepository.findById(5L)).thenReturn(Optional.of(course));

        wishlistService.removeFromWishlist(5L, "student@skilvorae.com");

        assertFalse(user.getWishlist().contains(course));
        verify(userRepository, times(1)).save(user);
    }
}
