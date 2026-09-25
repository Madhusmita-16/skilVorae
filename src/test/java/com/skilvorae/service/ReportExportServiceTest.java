package com.skilvorae.service;

import com.skilvorae.entity.Course;
import com.skilvorae.entity.Enrollment;
import com.skilvorae.entity.User;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.ReportExportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 unit test suite for ReportExportServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class ReportExportServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReportExportServiceImpl reportExportService;

    private Course course;
    private User user;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(10L);
        course.setTitle("Java Enterprise Masterclass");

        user = new User();
        user.setId(1L);
        user.setEmail("student@skilvorae.com");
        user.setFirstName("Alex");
        user.setLastName("Morgan");
    }

    @Test
    @DisplayName("Should generate non-empty CSV bytes for course analytics export")
    void exportCourseAnalyticsCsv_Success() {
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByCourseId(10L)).thenReturn(List.of());

        byte[] csvData = reportExportService.exportCourseAnalyticsCsv(10L, "instructor@skilvorae.com");

        assertNotNull(csvData);
        assertTrue(csvData.length > 0);
        String csvText = new String(csvData);
        assertTrue(csvText.contains("Student Email"));
    }

    @Test
    @DisplayName("Should generate non-empty PDF bytes for student progress report export")
    void exportStudentProgressPdf_Success() {
        when(userRepository.findByEmail("student@skilvorae.com")).thenReturn(Optional.of(user));
        when(enrollmentRepository.findByUserId(1L)).thenReturn(List.of());

        byte[] pdfData = reportExportService.exportStudentProgressPdf("student@skilvorae.com");

        assertNotNull(pdfData);
        assertTrue(pdfData.length > 0);
    }
}
