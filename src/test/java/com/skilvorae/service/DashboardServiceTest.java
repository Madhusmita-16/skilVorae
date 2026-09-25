package com.skilvorae.service;

import com.skilvorae.dto.AdminDashboardStatsDto;
import com.skilvorae.dto.DashboardStatsDto;
import com.skilvorae.dto.InstructorDashboardStatsDto;
import com.skilvorae.repository.CertificateRepository;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.DashboardServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 test suite for DashboardServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CertificateRepository certificateRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    @DisplayName("Should return student dashboard statistics")
    void getStudentStats_Success() {
        when(enrollmentRepository.countByUserId(1L)).thenReturn(4L);
        when(enrollmentRepository.countCompletedCoursesByUserId(1L)).thenReturn(2L);
        when(certificateRepository.countByUserId(1L)).thenReturn(2L);

        DashboardStatsDto stats = dashboardService.getStudentStats(1L);

        assertNotNull(stats);
        assertEquals(4L, stats.getEnrolledCoursesCount());
        assertEquals(2L, stats.getCompletedCoursesCount());
    }

    @Test
    @DisplayName("Should return instructor dashboard statistics")
    void getInstructorStats_Success() {
        when(courseRepository.countByInstructorId(5L)).thenReturn(3L);
        when(enrollmentRepository.countStudentsByInstructorId(5L)).thenReturn(150L);

        InstructorDashboardStatsDto stats = dashboardService.getInstructorStats(5L);

        assertNotNull(stats);
        assertEquals(3L, stats.getTotalCourses());
        assertEquals(150L, stats.getTotalStudents());
    }
}
