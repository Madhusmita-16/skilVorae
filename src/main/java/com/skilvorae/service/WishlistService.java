package com.skilvorae.service;

import com.skilvorae.dto.CourseDto;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.User;
import com.skilvorae.entity.Wishlist;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseService courseService;

    @Transactional
    public boolean toggleWishlist(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));

        Optional<Wishlist> existing = wishlistRepository.findByUserIdAndCourseId(userId, courseId);
        if (existing.isPresent()) {
            wishlistRepository.delete(existing.get());
            return false; // Removed from wishlist
        } else {
            Wishlist wishlist = Wishlist.builder()
                    .user(user)
                    .course(course)
                    .build();
            wishlistRepository.save(wishlist);
            return true; // Added to wishlist
        }
    }

    @Transactional(readOnly = true)
    public boolean isWishlisted(Long userId, Long courseId) {
        if (userId == null) return false;
        return wishlistRepository.existsByUserIdAndCourseId(userId, courseId);
    }

    @Transactional(readOnly = true)
    public List<CourseDto> getUserWishlist(Long userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return wishlists.stream()
                .map(w -> courseService.getCourseById(w.getCourse().getId(), userId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getWishlistCount(Long userId) {
        return wishlistRepository.countByUserId(userId);
    }
}
