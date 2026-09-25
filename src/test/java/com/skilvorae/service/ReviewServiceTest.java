package com.skilvorae.service;

import com.skilvorae.dto.CreateReviewRequest;
import com.skilvorae.dto.ReviewDto;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.Review;
import com.skilvorae.entity.User;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.ReviewRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 test suite for ReviewServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private User user;
    private Course course;
    private Review review;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(10L);
        user.setEmail("student@skilvorae.com");
        user.setFirstName("Alex");

        course = new Course();
        course.setId(1L);
        course.setTitle("Spring Boot 3 Mastery");

        review = new Review();
        review.setId(100L);
        review.setUser(user);
        review.setCourse(course);
        review.setRating(5);
        review.setComment("Exceptional course! Highly recommended for backend developers.");
    }

    @Test
    @DisplayName("Should create course review and update average course rating")
    void createReview_Success() {
        CreateReviewRequest request = new CreateReviewRequest();
        request.setRating(5);
        request.setComment("Exceptional course! Highly recommended for backend developers.");

        when(userRepository.findByEmail("student@skilvorae.com")).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(reviewRepository.existsByUserIdAndCourseId(10L, 1L)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewDto created = reviewService.createReview(1L, request, "student@skilvorae.com");

        assertNotNull(created);
        assertEquals(5, created.getRating());
        assertEquals("Exceptional course! Highly recommended for backend developers.", created.getComment());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("Should fetch paginated reviews for a given course ID")
    void getCourseReviews_Success() {
        when(reviewRepository.findByCourseId(eq(1L), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(review)));

        var page = reviewService.getCourseReviews(1L, PageRequest.of(0, 10));

        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        assertEquals(5, page.getContent().get(0).getRating());
    }
}
