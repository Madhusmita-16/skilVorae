package com.skilvorae.controller;

import com.skilvorae.entity.Course;
import com.skilvorae.entity.User;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/instructor/course")
@RequiredArgsConstructor
public class InstructorCourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @GetMapping("/create")
    public String showCreateCourseForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("course", new Course());
        return "instructor/create-course";
    }

    @PostMapping("/create")
    public String createCourse(@ModelAttribute("course") Course course, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        course.setInstructor(user);
        course.setInstructorName(user.getFullName());
        // Simple slug generation
        course.setSlug(course.getTitle().toLowerCase().replaceAll("[^a-z0-9]+", "-"));
        courseRepository.save(course);
        return "redirect:/instructor/courses";
    }

    private final com.skilvorae.repository.ModuleRepository moduleRepository;
    private final com.skilvorae.repository.LessonRepository lessonRepository;
    private final com.skilvorae.repository.AssignmentRepository assignmentRepository;
    private final com.skilvorae.repository.AssessmentRepository assessmentRepository;
    private final com.skilvorae.service.PdfParsingService pdfParsingService;

    @GetMapping("/{id}/edit")
    public String editCourse(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(id).orElseThrow();
        if(!course.getInstructor().getId().equals(user.getId())) {
            return "redirect:/instructor/courses";
        }
        model.addAttribute("user", user);
        model.addAttribute("course", course);
        return "instructor/course-editor";
    }

    @PostMapping("/{id}/modules")
    public String addModule(@PathVariable Long id, @RequestParam String title, @RequestParam Integer order, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(id).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            com.skilvorae.entity.Module module = new com.skilvorae.entity.Module();
            module.setCourse(course);
            module.setTitle(title);
            module.setModuleOrder(order);
            moduleRepository.save(module);
        }
        return "redirect:/instructor/course/" + id + "/edit";
    }

    @PostMapping("/{courseId}/modules/{moduleId}/lessons")
    public String addLesson(@PathVariable Long courseId, @PathVariable Long moduleId,
                            @RequestParam String title, @RequestParam Integer order,
                            @RequestParam String content, @RequestParam String videoUrl,
                            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            com.skilvorae.entity.Module module = moduleRepository.findById(moduleId).orElseThrow();
            if(module.getCourse().getId().equals(course.getId())) {
                com.skilvorae.entity.Lesson lesson = new com.skilvorae.entity.Lesson();
                lesson.setModule(module);
                lesson.setTitle(title);
                lesson.setLessonOrder(order);
                lesson.setContent(content);
                lesson.setVideoUrl(videoUrl);
                lessonRepository.save(lesson);
            }
        }
        return "redirect:/instructor/course/" + courseId + "/edit";
    }

    @PostMapping("/{courseId}/modules/{moduleId}/assignments")
    public String addAssignment(@PathVariable Long courseId, @PathVariable Long moduleId,
                                @RequestParam String title, @RequestParam String description,
                                @RequestParam Integer totalMarks, @RequestParam String attachmentUrl,
                                @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            com.skilvorae.entity.Module module = moduleRepository.findById(moduleId).orElseThrow();
            if(module.getCourse().getId().equals(course.getId())) {
                com.skilvorae.entity.Assignment assignment = new com.skilvorae.entity.Assignment();
                assignment.setModule(module);
                assignment.setTitle(title);
                assignment.setDescription(description);
                assignment.setTotalMarks(totalMarks);
                assignment.setAttachmentUrl(attachmentUrl);
                assignmentRepository.save(assignment);
            }
        }
        return "redirect:/instructor/course/" + courseId + "/edit";
    }

    @PostMapping("/{courseId}/assessment")
    public String addAssessment(@PathVariable Long courseId,
                                @RequestParam String title, @RequestParam Integer passingScore,
                                @RequestParam Integer timeLimitMinutes,
                                @RequestParam(required = false) org.springframework.web.multipart.MultipartFile pdfFile,
                                @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            com.skilvorae.entity.Assessment assessment = assessmentRepository.findByCourseId(courseId).orElse(new com.skilvorae.entity.Assessment());
            assessment.setCourse(course);
            assessment.setTitle(title);
            assessment.setPassingScore(passingScore);
            assessment.setTimeLimitMinutes(timeLimitMinutes);
            
            if(pdfFile != null && !pdfFile.isEmpty()) {
                try {
                    java.util.List<com.skilvorae.entity.Question> questions = pdfParsingService.parsePdfQuestions(pdfFile);
                    for (com.skilvorae.entity.Question q : questions) {
                        q.setAssessment(assessment);
                    }
                    assessment.getQuestions().clear();
                    assessment.getQuestions().addAll(questions);
                } catch (java.io.IOException e) {
                    // Handle error optionally using RedirectAttributes
                }
            }
            assessmentRepository.save(assessment);
        }
        return "redirect:/instructor/course/" + courseId + "/edit";
    }
}
