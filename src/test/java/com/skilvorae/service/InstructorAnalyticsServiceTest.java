package com.skilvorae.service;

import com.skilvorae.dto.InstructorAnalyticsDto;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.Enrollment;
import com.skilvorae.entity.User;
import com.skilvorae.enums.Role;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.InstructorAnalyticsServiceImpl;
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
import static org.mockito.Mockito.*;

/**
 * JUnit 5 unit test suite for InstructorAnalyticsServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class InstructorAnalyticsServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InstructorAnalyticsServiceImpl analyticsService;

    private User instructor;
    private Course course1;
    private Course course2;

    @BeforeEach
    void setUp() {
        instructor = new User();
        instructor.setId(5L);
        instructor.setEmail("instructor@skilvorae.com");
        instructor.setRole(Role.INSTRUCTOR);

        course1 = new Course();
        course1.setId(101L);
        course1.setTitle("Spring Boot 3 Mastery");
        course1.setInstructor(instructor);
        course1.setPrice(BigDecimal.valueOf(50.00));
        course1.setRating(4.8);
        course1.setIsPublished(true);

        course2 = new Course();
        course2.setId(102L);
        course2.setTitle("React 18 Architecture");
        course2.setInstructor(instructor);
        course2.setPrice(BigDecimal.valueOf(40.00));
        course2.setRating(4.6);
        course2.setIsPublished(true);
    }

    @Test
    @DisplayName("Should aggregate instructor analytics statistics accurately")
    void getInstructorAnalytics_Success() {
        when(userRepository.findByEmail("instructor@skilvorae.com")).thenReturn(Optional.of(instructor));
        when(courseRepository.findByInstructorId(5L)).thenReturn(List.of(course1, course2));
        when(enrollmentRepository.countByCourseInstructorId(5L)).thenReturn(150L);

        InstructorAnalyticsDto analytics = analyticsService.getInstructorAnalytics("instructor@skilvorae.com");

        assertNotNull(analytics);
        assertEquals(150, analytics.getTotalEnrolledStudents());
        assertEquals(2, analytics.getActiveCoursesCount());
        assertEquals(4.7, analytics.getAverageCourseRating(), 0.1);
        verify(courseRepository, times(1)).findByInstructorId(5L);
    }
}
