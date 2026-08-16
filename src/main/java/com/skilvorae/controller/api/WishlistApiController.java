package com.skilvorae.controller.api;

import com.skilvorae.dto.ApiResponse;
import com.skilvorae.dto.CourseDto;
import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistApiController {

    private final WishlistService wishlistService;
    private final UserRepository userRepository;

    @PostMapping("/toggle/{courseId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggleWishlist(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required"));
        }
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isSaved = wishlistService.toggleWishlist(user.getId(), courseId);
        long count = wishlistService.getWishlistCount(user.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("courseId", courseId);
        data.put("isWishlisted", isSaved);
        data.put("totalWishlistCount", count);
        data.put("message", isSaved ? "Course saved to wishlist" : "Course removed from wishlist");

        return ResponseEntity.ok(ApiResponse.success(isSaved ? "Saved to Wishlist" : "Removed from Wishlist", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseDto>>> getWishlist(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required"));
        }
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CourseDto> wishlist = wishlistService.getUserWishlist(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Wishlist courses fetched", wishlist));
    }

    @GetMapping("/check/{courseId}")
    public ResponseEntity<ApiResponse<Boolean>> checkWishlist(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.ok(ApiResponse.success("Status checked", false));
        }
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.ok(ApiResponse.success("Status checked", false));
        }
        boolean isSaved = wishlistService.isWishlisted(user.getId(), courseId);
        return ResponseEntity.ok(ApiResponse.success("Status checked", isSaved));
    }
}
