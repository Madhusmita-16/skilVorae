package com.skilvorae.service;

import com.skilvorae.dto.CreateDiscussionRequest;
import com.skilvorae.dto.DiscussionThreadDto;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.DiscussionThread;
import com.skilvorae.entity.User;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.DiscussionThreadRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.DiscussionServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 test suite for DiscussionServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class DiscussionServiceTest {

    @Mock
    private DiscussionThreadRepository discussionThreadRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DiscussionServiceImpl discussionService;

    private User user;
    private Course course;
    private DiscussionThread thread;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(10L);
        user.setEmail("student@skilvorae.com");
        user.setFirstName("Alex");

        course = new Course();
        course.setId(1L);

        thread = new DiscussionThread();
        thread.setId(50L);
        thread.setCourse(course);
        thread.setUser(user);
        thread.setTitle("How to configure Spring Security JWT filters?");
        thread.setContent("Could someone explain filter order in SecurityFilterChain?");
    }

    @Test
    @DisplayName("Should create new discussion thread under a course")
    void createDiscussion_Success() {
        CreateDiscussionRequest request = new CreateDiscussionRequest();
        request.setTitle("How to configure Spring Security JWT filters?");
        request.setContent("Could someone explain filter order in SecurityFilterChain?");

        when(userRepository.findByEmail("student@skilvorae.com")).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(discussionThreadRepository.save(any(DiscussionThread.class))).thenReturn(thread);

        DiscussionThreadDto created = discussionService.createDiscussion(1L, request, "student@skilvorae.com");

        assertNotNull(created);
        assertEquals("How to configure Spring Security JWT filters?", created.getTitle());
        verify(discussionThreadRepository, times(1)).save(any(DiscussionThread.class));
    }

    @Test
    @DisplayName("Should retrieve discussion threads for a given course ID")
    void getCourseDiscussions_Success() {
        when(discussionThreadRepository.findByCourseIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(thread));

        List<DiscussionThreadDto> threads = discussionService.getCourseDiscussions(1L);

        assertNotNull(threads);
        assertEquals(1, threads.size());
        assertEquals("How to configure Spring Security JWT filters?", threads.get(0).getTitle());
    }
}
