package com.skilvorae.service;

import com.skilvorae.dto.CertificateDto;
import com.skilvorae.entity.Certificate;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.Enrollment;
import com.skilvorae.entity.User;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.repository.CertificateRepository;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.CertificateServiceImpl;
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
 * JUnit 5 test suite for CertificateServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
public class CertificateServiceTest {

    @Mock
    private CertificateRepository certificateRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CertificateServiceImpl certificateService;

    private User student;
    private Course course;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);
        student.setFirstName("Alex");
        student.setLastName("Morgan");

        course = new Course();
        course.setId(10L);
        course.setTitle("Advanced Microservices");

        enrollment = new Enrollment();
        enrollment.setUser(student);
        enrollment.setCourse(course);
        enrollment.setProgressPercentage(100);
    }

    @Test
    @DisplayName("Should generate certificate for 100% course completion")
    void generateCertificate_Success() {
        when(enrollmentRepository.findByUserIdAndCourseId(1L, 10L)).thenReturn(Optional.of(enrollment));
        when(certificateRepository.findByUserIdAndCourseId(1L, 10L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(certificateRepository.save(any(Certificate.class))).thenAnswer(i -> {
            Certificate c = i.getArgument(0);
            c.setId(888L);
            return c;
        });

        CertificateDto result = certificateService.generateCertificate(1L, 10L);

        assertNotNull(result);
        assertEquals(888L, result.getId());
        assertEquals("Alex Morgan", result.getRecipientName());
        assertEquals("Advanced Microservices", result.getCourseTitle());
    }

    @Test
    @DisplayName("Should throw BadRequestException if course progress is under 100%")
    void generateCertificate_Incomplete_ThrowsException() {
        enrollment.setProgressPercentage(65);
        when(enrollmentRepository.findByUserIdAndCourseId(1L, 10L)).thenReturn(Optional.of(enrollment));

        assertThrows(BadRequestException.class, () -> certificateService.generateCertificate(1L, 10L));
    }
}
