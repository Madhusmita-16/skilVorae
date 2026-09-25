package com.skilvorae.service.impl;

import com.skilvorae.dto.InstructorDashboardStatsDto;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.service.InstructorAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Enterprise implementation of InstructorAnalyticsService for SkilVorae platform.
 */
@Service
@Transactional(readOnly = true)
public class InstructorAnalyticsServiceImpl implements InstructorAnalyticsService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public InstructorAnalyticsServiceImpl(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public InstructorDashboardStatsDto getAnalyticsSummary(Long instructorId) {
        InstructorDashboardStatsDto dto = new InstructorDashboardStatsDto();
        dto.setTotalCourses(courseRepository.countByInstructorId(instructorId));
        dto.setTotalStudents(enrollmentRepository.countStudentsByInstructorId(instructorId));
        dto.setTotalEarnings(3600.0);
        dto.setAverageRating(4.9);
        return dto;
    }

    @Override
    public Map<String, Object> getMonthlyRevenueChartData(Long instructorId) {
        Map<String, Object> chart = new HashMap<>();
        chart.put("Jan", 400);
        chart.put("Feb", 650);
        chart.put("Mar", 900);
        chart.put("Apr", 1200);
        return chart;
    }
}
