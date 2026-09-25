package com.skilvorae.service;

import com.skilvorae.dto.AssignmentDto;
import com.skilvorae.dto.AssignmentSubmissionDto;
import com.skilvorae.entity.Assignment;
import com.skilvorae.entity.AssignmentSubmission;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.User;
import com.skilvorae.repository.AssignmentRepository;
import com.skilvorae.repository.AssignmentSubmissionRepository;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.AssignmentServiceImpl;
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
 * JUnit 5 test suite for AssignmentServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
public class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private AssignmentSubmissionRepository submissionRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    private Course course;
    private User student;
    private Assignment assignment;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(10L);

        student = new User();
        student.setId(1L);

        assignment = new Assignment();
        assignment.setId(100L);
        assignment.setCourse(course);
        assignment.setTitle("Spring Boot Enterprise API Project");
    }

    @Test
    @DisplayName("Should create course assignment")
    void createAssignment_Success() {
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(assignmentRepository.save(any(Assignment.class))).thenAnswer(i -> {
            Assignment a = i.getArgument(0);
            a.setId(100L);
            return a;
        });

        AssignmentDto dto = assignmentService.createAssignment(10L, "Spring Boot Enterprise API Project", "Build REST APIs", 100);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals("Spring Boot Enterprise API Project", dto.getTitle());
    }

    @Test
    @DisplayName("Should submit student solution for assignment")
    void submitAssignment_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(assignmentRepository.findById(100L)).thenReturn(Optional.of(assignment));
        when(submissionRepository.save(any(AssignmentSubmission.class))).thenAnswer(i -> {
            AssignmentSubmission sub = i.getArgument(0);
            sub.setId(500L);
            return sub;
        });

        AssignmentSubmissionDto subDto = assignmentService.submitAssignment(1L, 100L, "https://github.com/student/repo", "Done");

        assertNotNull(subDto);
        assertEquals(500L, subDto.getId());
    }
}
