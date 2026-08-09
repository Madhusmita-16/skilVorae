package com.skilvorae.controller;

import com.skilvorae.dto.CourseDto;
import com.skilvorae.dto.LessonDto;
import com.skilvorae.dto.ModuleDto;
import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.CourseService;
import com.skilvorae.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class CourseWebController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final UserRepository userRepository;

    @GetMapping("/courses")
    public String catalog(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false, defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long currentUserId = getCurrentUserId(userDetails);
        Page<CourseDto> coursePage = courseService.getFilteredCourses(search, categoryId, difficulty, minRating, sortBy, page, 9, currentUserId);

        model.addAttribute("coursePage", coursePage);
        model.addAttribute("categories", courseService.getAllCategories());
        model.addAttribute("selectedSearch", search);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedDifficulty", difficulty);
        model.addAttribute("selectedMinRating", minRating);
        model.addAttribute("selectedSortBy", sortBy);
        model.addAttribute("isLoggedIn", userDetails != null);
        if (userDetails != null) {
            userRepository.findByEmail(userDetails.getUsername()).ifPresent(user -> model.addAttribute("user", user));
        }

        return "course/catalog";
    }

    @GetMapping("/courses/{id}")
    public String details(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = getCurrentUserId(userDetails);
        CourseDto course = courseService.getCourseDetails(id, currentUserId);

        model.addAttribute("course", course);
        model.addAttribute("isLoggedIn", userDetails != null);
        if (userDetails != null) {
            userRepository.findByEmail(userDetails.getUsername()).ifPresent(user -> model.addAttribute("user", user));
        }

        return "course/details";
    }

    @GetMapping("/courses/{id}/learn")
    public String player(
            @PathVariable Long id,
            @RequestParam(required = false) Long lessonId,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        CourseDto course = courseService.getCourseDetails(id, user.getId());

        if (!Boolean.TRUE.equals(course.getIsEnrolled())) {
            enrollmentService.enrollCourse(user.getId(), id);
            course = courseService.getCourseDetails(id, user.getId());
        }

        LessonDto currentLesson = null;
        if (course.getModules() != null) {
            for (ModuleDto module : course.getModules()) {
                for (LessonDto lesson : module.getLessons()) {
                    if (lessonId != null && lesson.getId().equals(lessonId)) {
                        currentLesson = lesson;
                        break;
                    }
                    if (currentLesson == null) {
                        currentLesson = lesson; // Default to first lesson
                    }
                }
                if (lessonId != null && currentLesson != null && Objects.equals(currentLesson.getId(), lessonId)) {
                    break;
                }
            }
        }

        model.addAttribute("course", course);
        model.addAttribute("currentLesson", currentLesson);
        model.addAttribute("user", user);

        return "learn/player";
    }

    private Long getCurrentUserId(UserDetails userDetails) {
        if (userDetails == null) return null;
        return userRepository.findByEmail(userDetails.getUsername()).map(User::getId).orElse(null);
    }
}
