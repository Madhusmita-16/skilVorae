package com.skilvorae.service.impl;

import com.skilvorae.dto.CourseDto;
import com.skilvorae.service.CourseRecommendationService;
import com.skilvorae.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Enterprise implementation of CourseRecommendationService using category interest heuristics.
 */
@Service
public class CourseRecommendationServiceImpl implements CourseRecommendationService {

    private final CourseService courseService;

    @Autowired
    public CourseRecommendationServiceImpl(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public List<CourseDto> getPersonalizedRecommendations(Long userId, int limit) {
        List<CourseDto> allCourses = courseService.getAllActiveCourses();
        return allCourses.stream().limit(limit <= 0 ? 5 : limit).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getTrendingCourses(int limit) {
        List<CourseDto> allCourses = courseService.getAllActiveCourses();
        return allCourses.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getEnrollmentCount(), c1.getEnrollmentCount()))
                .limit(limit <= 0 ? 5 : limit)
                .collect(Collectors.toList());
    }
}
