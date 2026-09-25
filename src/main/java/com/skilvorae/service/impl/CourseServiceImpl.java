package com.skilvorae.service.impl;

import com.skilvorae.dto.CourseCreateRequestDto;
import com.skilvorae.dto.CourseDto;
import com.skilvorae.entity.Category;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.User;
import com.skilvorae.enums.Difficulty;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.CategoryRepository;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enterprise implementation of CourseService managing course catalogs, instructor course creation,
 * module structures, difficulty filters, and search discovery for SkilVorae.
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             CategoryRepository categoryRepository,
                             UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        return mapToDto(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getAllActiveCourses() {
        return courseRepository.findAll().stream()
                .filter(Course::isPublished)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getCoursesByCategory(Long categoryId) {
        return courseRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto createCourse(Long instructorId, CourseCreateRequestDto request) {
        if (request == null || request.getTitle() == null) {
            throw new BadRequestException("Course title must be specified.");
        }

        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor User", "id", instructorId));

        Course course = new Course();
        course.setTitle(request.getTitle().trim());
        course.setSubtitle(request.getSubtitle() != null ? request.getSubtitle().trim() : "");
        course.setDescription(request.getDescription() != null ? request.getDescription().trim() : "");
        course.setPrice(request.getPrice() != null ? request.getPrice() : 0.0);
        course.setInstructor(instructor);

        if (request.getDifficulty() != null) {
            try {
                course.setDifficulty(Difficulty.valueOf(request.getDifficulty().toUpperCase().trim()));
            } catch (Exception e) {
                course.setDifficulty(Difficulty.BEGINNER);
            }
        }

        if (request.getCategoryId() != null) {
            Category cat = categoryRepository.findById(request.getCategoryId()).orElse(null);
            course.setCategory(cat);
        }

        course.setPublished(true);
        course.setCreatedAt(LocalDateTime.now());

        Course saved = courseRepository.save(course);
        return mapToDto(saved);
    }

    @Override
    public CourseDto updateCourse(Long instructorId, Long courseId, CourseCreateRequestDto request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        if (request.getTitle() != null) course.setTitle(request.getTitle().trim());
        if (request.getSubtitle() != null) course.setSubtitle(request.getSubtitle().trim());
        if (request.getDescription() != null) course.setDescription(request.getDescription().trim());
        if (request.getPrice() != null) course.setPrice(request.getPrice());

        Course updated = courseRepository.save(course);
        return mapToDto(updated);
    }

    @Override
    public void deleteCourse(Long instructorId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        courseRepository.delete(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> searchCourses(String query) {
        if (query == null || query.isBlank()) return getAllActiveCourses();

        String q = query.toLowerCase().trim();
        return courseRepository.findAll().stream()
                .filter(Course::isPublished)
                .filter(c -> (c.getTitle() != null && c.getTitle().toLowerCase().contains(q)) ||
                             (c.getDescription() != null && c.getDescription().toLowerCase().contains(q)))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CourseDto mapToDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setSubtitle(course.getSubtitle());
        dto.setDescription(course.getDescription());
        dto.setPrice(course.getPrice());
        dto.setThumbnailUrl(course.getThumbnailUrl());
        dto.setDifficulty(course.getDifficulty() != null ? course.getDifficulty().name() : "BEGINNER");
        dto.setPublished(course.isPublished());
        dto.setAverageRating(course.getAverageRating());
        dto.setEnrollmentCount(course.getEnrollmentCount());

        if (course.getInstructor() != null) {
            dto.setInstructorId(course.getInstructor().getId());
            dto.setInstructorName(course.getInstructor().getFirstName() + " " + course.getInstructor().getLastName());
            dto.setInstructorAvatar(course.getInstructor().getAvatarUrl());
        }

        if (course.getCategory() != null) {
            dto.setCategoryId(course.getCategory().getId());
            dto.setCategoryName(course.getCategory().getName());
        }

        return dto;
    }
}
