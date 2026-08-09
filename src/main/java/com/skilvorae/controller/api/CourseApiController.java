package com.skilvorae.controller.api;

import com.skilvorae.dto.ApiResponse;
import com.skilvorae.dto.CourseDto;
import com.skilvorae.dto.EnrollmentDto;
import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.CourseService;
import com.skilvorae.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseApiController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CourseDto>>> getCourses(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false, defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long currentUserId = getCurrentUserId(userDetails);
        Page<CourseDto> courses = courseService.getFilteredCourses(search, categoryId, difficulty, minRating, sortBy, page, size, currentUserId);
        return ResponseEntity.ok(ApiResponse.success("Courses fetched successfully", courses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> getCourseDetails(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long currentUserId = getCurrentUserId(userDetails);
        CourseDto course = courseService.getCourseDetails(id, currentUserId);
        return ResponseEntity.ok(ApiResponse.success("Course details fetched", course));
    }

    @PostMapping("/{id}/enroll")
    public ResponseEntity<ApiResponse<EnrollmentDto>> enrollInCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required to enroll"));
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        EnrollmentDto enrollment = enrollmentService.enrollCourse(user.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Enrolled in course successfully", enrollment));
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<ApiResponse<?>> addReview(
            @PathVariable Long id,
            @RequestParam Integer rating,
            @RequestParam String comment,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required to post a review"));
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        courseService.addReview(id, user.getId(), rating, comment);
        return ResponseEntity.ok(ApiResponse.success("Review submitted successfully", null));
    }

    private Long getCurrentUserId(UserDetails userDetails) {
        if (userDetails == null) return null;
        return userRepository.findByEmail(userDetails.getUsername()).map(User::getId).orElse(null);
    }
}
