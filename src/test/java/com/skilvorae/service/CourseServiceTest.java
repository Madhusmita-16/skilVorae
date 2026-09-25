package com.skilvorae.service;

import com.skilvorae.dto.CourseCreateRequestDto;
import com.skilvorae.dto.CourseDto;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.User;
import com.skilvorae.repository.CategoryRepository;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.CourseServiceImpl;
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
 * JUnit 5 test suite for CourseServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private User instructor;

    @BeforeEach
    void setUp() {
        instructor = new User();
        instructor.setId(5L);
        instructor.setFirstName("Sarah");
        instructor.setLastName("Instructor");

        course = new Course();
        course.setId(100L);
        course.setTitle("Full Stack Spring Boot 3 & React Masterclass");
        course.setInstructor(instructor);
        course.setPublished(true);
    }

    @Test
    @DisplayName("Should retrieve course by ID")
    void getCourseById_Success() {
        when(courseRepository.findById(100L)).thenReturn(Optional.of(course));

        CourseDto result = courseService.getCourseById(100L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Full Stack Spring Boot 3 & React Masterclass", result.getTitle());
    }

    @Test
    @DisplayName("Should create course for instructor")
    void createCourse_Success() {
        CourseCreateRequestDto req = new CourseCreateRequestDto();
        req.setTitle("Mastering Microservices with Java & Docker");
        req.setPrice(99.99);

        when(userRepository.findById(5L)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenAnswer(i -> {
            Course c = i.getArgument(0);
            c.setId(200L);
            return c;
        });

        CourseDto created = courseService.createCourse(5L, req);

        assertNotNull(created);
        assertEquals(200L, created.getId());
        assertEquals("Mastering Microservices with Java & Docker", created.getTitle());
    }
}
