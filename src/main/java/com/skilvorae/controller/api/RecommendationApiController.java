package com.skilvorae.controller.api;

import com.skilvorae.dto.ApiResponse;
import com.skilvorae.dto.CourseDto;
import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.CourseRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationApiController {

    private final CourseRecommendationService recommendationService;
    private final UserRepository userRepository;

    @GetMapping("/related/{courseId}")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getRelatedCourses(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "3") int limit,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getCurrentUserId(userDetails);
        List<CourseDto> related = recommendationService.getRelatedCourses(courseId, userId, limit);
        return ResponseEntity.ok(ApiResponse.success("Related courses fetched", related));
    }

    @GetMapping("/personalized")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getPersonalizedRecommendations(
            @RequestParam(defaultValue = "4") int limit,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getCurrentUserId(userDetails);
        List<CourseDto> recommendations = recommendationService.getPersonalizedRecommendations(userId, limit);
        return ResponseEntity.ok(ApiResponse.success("Personalized recommendations fetched", recommendations));
    }

    @GetMapping("/trending")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getTrendingCourses(
            @RequestParam(defaultValue = "4") int limit,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getCurrentUserId(userDetails);
        List<CourseDto> trending = recommendationService.getTopTrendingCourses(limit, userId);
        return ResponseEntity.ok(ApiResponse.success("Trending courses fetched", trending));
    }

    private Long getCurrentUserId(UserDetails userDetails) {
        if (userDetails == null) return null;
        return userRepository.findByEmail(userDetails.getUsername()).map(User::getId).orElse(null);
    }
}
