package com.skilvorae.service;

import com.skilvorae.dto.*;
import com.skilvorae.entity.*;
import com.skilvorae.enums.Difficulty;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserProgressRepository userProgressRepository;
    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;
    private final CourseReviewRepository courseReviewRepository;
    private final UserRepository userRepository;

    public Page<CourseDto> getFilteredCourses(String search, Long categoryId, String difficultyStr, Double minRating, String sortBy, int page, int size, Long currentUserId) {
        Difficulty difficulty = null;
        if (difficultyStr != null && !difficultyStr.isBlank() && !"ALL".equalsIgnoreCase(difficultyStr)) {
            try {
                difficulty = Difficulty.valueOf(difficultyStr.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        Sort sort = Sort.by("rating").descending();
        if ("popular".equalsIgnoreCase(sortBy)) {
            sort = Sort.by("enrollmentCount").descending();
        } else if ("newest".equalsIgnoreCase(sortBy)) {
            sort = Sort.by("createdAt").descending();
        } else if ("priceLow".equalsIgnoreCase(sortBy)) {
            sort = Sort.by("price").ascending();
        } else if ("priceHigh".equalsIgnoreCase(sortBy)) {
            sort = Sort.by("price").descending();
        } else if ("title".equalsIgnoreCase(sortBy)) {
            sort = Sort.by("title").ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Course> courses = courseRepository.findFilteredCourses(
                (search != null && search.isBlank()) ? null : search,
                categoryId,
                difficulty,
                minRating,
                pageable
        );

        return courses.map(course -> mapToDto(course, currentUserId, false));
    }

    public CourseDto getCourseDetails(Long courseId, Long currentUserId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        return mapToDto(course, currentUserId, true);
    }

    public CourseDto getCourseDetailsBySlug(String slug, Long currentUserId) {
        Course course = courseRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with slug: " + slug));
        return mapToDto(course, currentUserId, true);
    }

    public List<CourseDto> getFeaturedCourses(Long currentUserId) {
        return courseRepository.findTop6ByOrderByEnrollmentCountDesc()
                .stream()
                .map(c -> mapToDto(c, currentUserId, false))
                .collect(Collectors.toList());
    }

    public List<CourseDto> getTopCourses12(Long currentUserId) {
        return courseRepository.findAll(PageRequest.of(0, 48, Sort.by("rating").descending()))
                .getContent()
                .stream()
                .map(c -> mapToDto(c, currentUserId, false))
                .collect(Collectors.toList());
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public CourseDto mapToDto(Course course) {
        return mapToDto(course, null, false);
    }

    @Transactional
    public CourseDto createCourse(CourseCreateRequestDto req) {
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseGet(() -> categoryRepository.findAll().get(0));

        Course course = Course.builder()
                .title(req.getTitle())
                .slug(req.getSlug() != null ? req.getSlug() : req.getTitle().toLowerCase().replaceAll("[^a-z0-9]", "-"))
                .description(req.getDescription())
                .instructorName(req.getInstructorName())
                .category(category)
                .difficulty(req.getDifficulty())
                .durationHours(req.getDurationHours())
                .thumbnailUrl(req.getThumbnailUrl())
                .rating(4.8)
                .price(req.getPrice())
                .originalPrice(req.getOriginalPrice() != null ? req.getOriginalPrice() : req.getPrice() * 2)
                .discountPercentage(50)
                .enrollmentCount(0)
                .build();

        Course savedCourse = courseRepository.save(course);

        if (req.getModules() != null) {
            int mIdx = 1;
            for (CourseCreateRequestDto.ModulePayload mPayload : req.getModules()) {
                com.skilvorae.entity.Module module = com.skilvorae.entity.Module.builder()
                        .course(savedCourse)
                        .title(mPayload.getTitle())
                        .moduleOrder(mIdx++)
                        .build();
                com.skilvorae.entity.Module savedModule = moduleRepository.save(module);

                if (mPayload.getLessons() != null) {
                    int lIdx = 1;
                    for (CourseCreateRequestDto.LessonPayload lPayload : mPayload.getLessons()) {
                        Lesson lesson = Lesson.builder()
                                .module(savedModule)
                                .title(lPayload.getTitle())
                                .durationMinutes(lPayload.getDurationMinutes() != null ? lPayload.getDurationMinutes() : 30)
                                .lessonOrder(lIdx++)
                                .content(lPayload.getContent() != null ? lPayload.getContent() : "Lesson content for " + lPayload.getTitle())
                                .build();
                        lessonRepository.save(lesson);
                    }
                }
            }
        }

        return mapToDto(savedCourse, null, true);
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    @Transactional
    public CourseReviewDto addReview(Long courseId, Long userId, Integer rating, String comment) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        CourseReview review = CourseReview.builder()
                .course(course)
                .user(user)
                .rating(rating)
                .comment(comment)
                .build();
        courseReviewRepository.save(review);

        // Recalculate average rating
        Double avgRating = courseReviewRepository.getAverageRatingByCourseId(courseId);
        if (avgRating != null) {
            course.setRating(Math.round(avgRating * 10.0) / 10.0);
            courseRepository.save(course);
        }

        return CourseReviewDto.builder()
                .id(review.getId())
                .courseId(courseId)
                .userId(userId)
                .userName(user.getFullName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public List<CourseReviewDto> getCourseReviews(Long courseId) {
        return courseReviewRepository.findByCourseIdOrderByCreatedAtDesc(courseId)
                .stream()
                .map(r -> CourseReviewDto.builder()
                        .id(r.getId())
                        .courseId(courseId)
                        .userId(r.getUser().getId())
                        .userName(r.getUser().getFullName())
                        .rating(r.getRating())
                        .comment(r.getComment())
                        .createdAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public CourseDto mapToDto(Course course, Long currentUserId, boolean includeModules) {
        boolean isEnrolled = false;
        int progressPercentage = 0;

        if (currentUserId != null) {
            isEnrolled = enrollmentRepository.existsByUserIdAndCourseId(currentUserId, course.getId());
            if (isEnrolled) {
                long totalLessons = lessonRepository.countLessonsByCourseId(course.getId());
                long completedLessons = userProgressRepository.countByUserIdAndCourseIdAndCompletedTrue(currentUserId, course.getId());
                if (totalLessons > 0) {
                    progressPercentage = (int) Math.round(((double) completedLessons / totalLessons) * 100);
                }
            }
        }

        int totalModules = course.getModules() != null ? course.getModules().size() : 0;
        int totalLessons = (int) lessonRepository.countLessonsByCourseId(course.getId());

        List<ModuleDto> moduleDtos = null;
        if (includeModules && course.getModules() != null) {
            List<Long> completedLessonIds = (currentUserId != null)
                    ? userProgressRepository.findCompletedLessonIdsByUserAndCourse(currentUserId, course.getId())
                    : List.of();

            moduleDtos = course.getModules().stream().map(module -> {
                List<LessonDto> lessonDtos = module.getLessons().stream().map(lesson -> LessonDto.builder()
                        .id(lesson.getId())
                        .moduleId(module.getId())
                        .title(lesson.getTitle())
                        .content(lesson.getContent())
                        .durationMinutes(lesson.getDurationMinutes())
                        .lessonOrder(lesson.getLessonOrder())
                        .videoUrl(lesson.getVideoUrl())
                        .isCompleted(completedLessonIds.contains(lesson.getId()))
                        .build()).collect(Collectors.toList());

                return ModuleDto.builder()
                        .id(module.getId())
                        .courseId(course.getId())
                        .title(module.getTitle())
                        .moduleOrder(module.getModuleOrder())
                        .lessons(lessonDtos)
                        .build();
            }).collect(Collectors.toList());
        }

        List<CourseReviewDto> reviewDtos = null;
        if (includeModules) {
            reviewDtos = getCourseReviews(course.getId());
        }

        Double price = course.getPrice() != null ? course.getPrice() : 1499.0;
        Double originalPrice = course.getOriginalPrice() != null ? course.getOriginalPrice() : 2999.0;
        Integer discount = course.getDiscountPercentage() != null ? course.getDiscountPercentage() : 50;
        String formattedPrice = "₹" + String.format("%,.0f", price) + " + taxes";

        return CourseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .slug(course.getSlug())
                .description(course.getDescription())
                .instructorName(course.getInstructorName())
                .categoryId(course.getCategory().getId())
                .categoryName(course.getCategory().getName())
                .difficulty(course.getDifficulty().name())
                .durationHours(course.getDurationHours())
                .thumbnailUrl(course.getThumbnailUrl())
                .rating(course.getRating())
                .enrollmentCount(course.getEnrollmentCount() != null ? course.getEnrollmentCount() : 0)
                .price(price)
                .originalPrice(originalPrice)
                .discountPercentage(discount)
                .formattedPrice(formattedPrice)
                .totalModules(totalModules)
                .totalLessons(totalLessons)
                .isEnrolled(isEnrolled)
                .progressPercentage(progressPercentage)
                .modules(moduleDtos)
                .reviews(reviewDtos)
                .build();
    }
}
