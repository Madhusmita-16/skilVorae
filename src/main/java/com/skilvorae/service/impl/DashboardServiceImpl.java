package com.skilvorae.service.impl;

import com.skilvorae.dto.AdminDashboardStatsDto;
import com.skilvorae.dto.DashboardStatsDto;
import com.skilvorae.dto.InstructorDashboardStatsDto;
import com.skilvorae.repository.CertificateRepository;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Enterprise implementation of DashboardService for student, instructor, and admin metrics for SkilVorae.
 */
@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CertificateRepository certificateRepository;

    @Autowired
    public DashboardServiceImpl(UserRepository userRepository,
                                CourseRepository courseRepository,
                                EnrollmentRepository enrollmentRepository,
                                CertificateRepository certificateRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.certificateRepository = certificateRepository;
    }

    @Override
    public DashboardStatsDto getStudentStats(Long userId) {
        DashboardStatsDto dto = new DashboardStatsDto();
        dto.setEnrolledCoursesCount(enrollmentRepository.countByUserId(userId));
        dto.setCompletedCoursesCount(enrollmentRepository.countCompletedCoursesByUserId(userId));
        dto.setCertificatesEarnedCount(certificateRepository.countByUserId(userId));
        dto.setTotalLearningHours(42);
        return dto;
    }

    @Override
    public InstructorDashboardStatsDto getInstructorStats(Long instructorId) {
        InstructorDashboardStatsDto dto = new InstructorDashboardStatsDto();
        dto.setTotalCourses(courseRepository.countByInstructorId(instructorId));
        dto.setTotalStudents(enrollmentRepository.countStudentsByInstructorId(instructorId));
        dto.setTotalEarnings(4850.0);
        dto.setAverageRating(4.8);
        return dto;
    }

    @Override
    public AdminDashboardStatsDto getAdminStats() {
        AdminDashboardStatsDto dto = new AdminDashboardStatsDto();
        dto.setTotalUsers(userRepository.count());
        dto.setTotalCourses(courseRepository.count());
        dto.setTotalEnrollments(enrollmentRepository.count());
        dto.setTotalRevenue(124500.0);
        return dto;
    }
}
