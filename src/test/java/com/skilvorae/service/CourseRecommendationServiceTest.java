package com.skilvorae.service;

import com.skilvorae.dto.CourseDto;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.Enrollment;
import com.skilvorae.entity.User;
import com.skilvorae.enums.CourseCategory;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.CourseRecommendationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 test suite for CourseRecommendationServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class CourseRecommendationServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseRecommendationServiceImpl recommendationService;

    private User user;
    private Course course1;
    private Course course2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(10L);
        user.setEmail("student@skilvorae.com");

        course1 = new Course();
        course1.setId(101L);
        course1.setTitle("Spring Boot Enterprise");
        course1.setCategory(CourseCategory.WEB_DEVELOPMENT);
        course1.setRating(4.8);
        course1.setPrice(BigDecimal.valueOf(49.99));
        course1.setIsPublished(true);

        course2 = new Course();
        course2.setId(102L);
        course2.setTitle("Microservices Architecture");
        course2.setCategory(CourseCategory.CLOUD_COMPUTING);
        course2.setRating(4.9);
        course2.setPrice(BigDecimal.valueOf(59.99));
        course2.setIsPublished(true);
    }

    @Test
    @DisplayName("Should return personalized recommendations filtered from enrolled courses")
    void getPersonalizedRecommendations_Success() {
        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course1);
        enrollment.setUser(user);

        when(userRepository.findByEmail("student@skilvorae.com")).thenReturn(Optional.of(user));
        when(enrollmentRepository.findByUserId(10L)).thenReturn(List.of(enrollment));
        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));

        List<CourseDto> recommendations = recommendationService.getPersonalizedRecommendations("student@skilvorae.com", 5);

        assertNotNull(recommendations);
        assertEquals(1, recommendations.size());
        assertEquals(102L, recommendations.get(0).getId());
        assertEquals("Microservices Architecture", recommendations.get(0).getTitle());
    }
}
