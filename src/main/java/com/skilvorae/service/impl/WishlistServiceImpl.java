package com.skilvorae.service.impl;

import com.skilvorae.dto.CourseDto;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.User;
import com.skilvorae.entity.Wishlist;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.repository.WishlistRepository;
import com.skilvorae.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enterprise implementation of WishlistService for SkilVorae platform.
 */
@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public WishlistServiceImpl(WishlistRepository wishlistRepository,
                               CourseRepository courseRepository,
                               UserRepository userRepository) {
        this.wishlistRepository = wishlistRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addToWishlist(Long userId, Long courseId) {
        if (wishlistRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new BadRequestException("Course is already in your wishlist.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        Wishlist w = new Wishlist();
        w.setUser(user);
        w.setCourse(course);
        w.setCreatedAt(LocalDateTime.now());

        wishlistRepository.save(w);
    }

    @Override
    public void removeFromWishlist(Long userId, Long courseId) {
        Wishlist w = wishlistRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist Item", "courseId", courseId));

        wishlistRepository.delete(w);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getUserWishlist(Long userId) {
        List<Wishlist> items = wishlistRepository.findByUserId(userId);
        return items.stream()
                .filter(w -> w.getCourse() != null)
                .map(w -> mapToCourseDto(w.getCourse()))
                .collect(Collectors.toList());
    }

    private CourseDto mapToCourseDto(Course c) {
        CourseDto dto = new CourseDto();
        dto.setId(c.getId());
        dto.setTitle(c.getTitle());
        dto.setSubtitle(c.getSubtitle());
        dto.setPrice(c.getPrice());
        dto.setThumbnailUrl(c.getThumbnailUrl());
        return dto;
    }
}
