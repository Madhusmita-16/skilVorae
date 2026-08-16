package com.skilvorae.service;

import com.skilvorae.dto.CourseDto;
import com.skilvorae.entity.Course;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseRecommendationService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseService courseService;

    @Transactional(readOnly = true)
    public List<CourseDto> getRelatedCourses(Long courseId, Long userId, int limit) {
        Course currentCourse = courseRepository.findById(courseId).orElse(null);
        if (currentCourse == null) {
            return Collections.emptyList();
        }

        // Fetch top rated courses in same category excluding current course
        List<Course> related = courseRepository.findByCategoryIdOrderByRatingDesc(
                currentCourse.getCategory().getId(), PageRequest.of(0, limit + 1)
        ).getContent();

        return related.stream()
                .filter(c -> !c.getId().equals(courseId))
                .limit(limit)
                .map(c -> courseService.getCourseById(c.getId(), userId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseDto> getPersonalizedRecommendations(Long userId, int limit) {
        if (userId == null) {
            return getTopTrendingCourses(limit, null);
        }

        // Find user's enrolled categories
        var enrollments = enrollmentRepository.findByUserIdOrderByEnrolledAtDesc(userId);
        if (enrollments.isEmpty()) {
            return getTopTrendingCourses(limit, userId);
        }

        Long topCategoryId = enrollments.get(0).getCourse().getCategory().getId();
        List<Course> recommended = courseRepository.findByCategoryIdOrderByRatingDesc(
                topCategoryId, PageRequest.of(0, limit)
        ).getContent();

        return recommended.stream()
                .map(c -> courseService.getCourseById(c.getId(), userId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseDto> getTopTrendingCourses(int limit, Long userId) {
        return courseRepository.findAllByOrderByEnrollmentCountDesc(PageRequest.of(0, limit))
                .getContent()
                .stream()
                .map(c -> courseService.getCourseById(c.getId(), userId))
                .collect(Collectors.toList());
    }
}
