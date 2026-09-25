package com.skilvorae.service;

import com.skilvorae.dto.EnrollmentDto;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.Enrollment;
import com.skilvorae.entity.User;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.EnrollmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 test suite for EnrollmentServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private User student;
    private Course course;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(10L);

        course = new Course();
        course.setId(50L);
        course.setTitle("Spring Boot 3 Masterclass");
        course.setEnrollmentCount(10);
    }

    @Test
    @DisplayName("Should enroll student in course and increment course enrollment count")
    void enrollCourse_Success() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(50L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByUserIdAndCourseId(10L, 50L)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(i -> {
            Enrollment e = i.getArgument(0);
            e.setId(999L);
            return e;
        });

        EnrollmentDto result = enrollmentService.enrollCourse(10L, 50L);

        assertNotNull(result);
        assertEquals(999L, result.getId());
        assertEquals(11, course.getEnrollmentCount());
    }

    @Test
    @DisplayName("Should throw BadRequestException if user is already enrolled in course")
    void enrollCourse_AlreadyEnrolled_ThrowsException() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(50L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByUserIdAndCourseId(10L, 50L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> enrollmentService.enrollCourse(10L, 50L));
    }
}
