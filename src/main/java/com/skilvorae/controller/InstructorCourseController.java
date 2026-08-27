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
    private final com.skilvorae.service.FileStorageService fileStorageService;

    @GetMapping("/create")
    public String showCreateCourseForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("course", new Course());
        return "instructor/create-course";
    }

    @PostMapping("/create")
    public String createCourse(@ModelAttribute("course") Course course, 
                               @RequestParam(value = "thumbnailFile", required = false) org.springframework.web.multipart.MultipartFile thumbnailFile,
                               @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        course.setInstructor(user);
        course.setInstructorName(user.getFullName());
        // Simple slug generation
        course.setSlug(course.getTitle().toLowerCase().replaceAll("[^a-z0-9]+", "-"));
        
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            course.setThumbnailUrl(fileStorageService.saveFile(thumbnailFile, "thumbnails"));
        }
        
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
        
        com.skilvorae.entity.Assessment assessment = assessmentRepository.findByCourseId(id).orElse(null);
        model.addAttribute("assessment", assessment);
        
        return "instructor/course-editor";
    }

    @PostMapping("/{id}/edit")
    public String updateCourseDetails(@PathVariable Long id, @ModelAttribute("course") Course courseUpdate, 
                                      @RequestParam(value = "thumbnailFile", required = false) org.springframework.web.multipart.MultipartFile thumbnailFile,
                                      @RequestParam(value = "removeThumbnail", required = false) boolean removeThumbnail,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(id).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            course.setTitle(courseUpdate.getTitle());
            course.setDescription(courseUpdate.getDescription());
            course.setDifficulty(courseUpdate.getDifficulty());
            course.setPrice(courseUpdate.getPrice());
            
            if (removeThumbnail) {
                if (course.getThumbnailUrl() != null && course.getThumbnailUrl().startsWith("/uploads/")) {
                    fileStorageService.deleteFile(course.getThumbnailUrl());
                }
                course.setThumbnailUrl(null);
            } else if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
                course.setThumbnailUrl(fileStorageService.saveFile(thumbnailFile, "thumbnails"));
            } else if (courseUpdate.getThumbnailUrl() != null && !courseUpdate.getThumbnailUrl().isEmpty()) {
                course.setThumbnailUrl(courseUpdate.getThumbnailUrl());
            }
            
            course.setInstructorName(courseUpdate.getInstructorName());
            courseRepository.save(course);
        }
        return "redirect:/instructor/course/" + id + "/edit";
    }

    @PostMapping("/{id}/delete")
    public String deleteCourse(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(id).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            courseRepository.delete(course);
        }
        return "redirect:/instructor/courses";
    }

    @PostMapping("/{id}/archive")
    public String archiveCourse(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(id).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            course.setIsArchived(!course.getIsArchived());
            courseRepository.save(course);
        }
        return "redirect:/instructor/courses";
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
                            @RequestParam String content, 
                            @RequestParam(required = false) org.springframework.web.multipart.MultipartFile videoFile,
                            @RequestParam(required = false) org.springframework.web.multipart.MultipartFile pdfFile,
                            @RequestParam(required = false) org.springframework.web.multipart.MultipartFile pptFile,
                            @RequestParam(required = false) org.springframework.web.multipart.MultipartFile bookFile,
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
                
                if (videoFile != null && !videoFile.isEmpty()) {
                    lesson.setVideoUrl(fileStorageService.saveFile(videoFile, "lessons/videos"));
                }
                if (pdfFile != null && !pdfFile.isEmpty()) {
                    lesson.setPdfUrl(fileStorageService.saveFile(pdfFile, "lessons/pdfs"));
                }
                if (pptFile != null && !pptFile.isEmpty()) {
                    lesson.setPptUrl(fileStorageService.saveFile(pptFile, "lessons/ppts"));
                }
                if (bookFile != null && !bookFile.isEmpty()) {
                    lesson.setBookUrl(fileStorageService.saveFile(bookFile, "lessons/books"));
                }
                
                lessonRepository.save(lesson);
            }
        }
        return "redirect:/instructor/course/" + courseId + "/edit";
    }

    @PostMapping("/{courseId}/modules/{moduleId}/assignments")
    public String addAssignment(@PathVariable Long courseId, @PathVariable Long moduleId,
                                @RequestParam String title, @RequestParam String description,
                                @RequestParam Integer totalMarks, 
                                @RequestParam(required = false) org.springframework.web.multipart.MultipartFile attachmentFile,
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
                
                if (attachmentFile != null && !attachmentFile.isEmpty()) {
                    assignment.setAttachmentUrl(fileStorageService.saveFile(attachmentFile, "assignments"));
                }
                
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
    @PostMapping("/{courseId}/modules/{moduleId}/edit")
    public String editModule(@PathVariable Long courseId, @PathVariable Long moduleId,
                             @RequestParam String title, @RequestParam Integer order,
                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            com.skilvorae.entity.Module module = moduleRepository.findById(moduleId).orElseThrow();
            if(module.getCourse().getId().equals(course.getId())) {
                module.setTitle(title);
                module.setModuleOrder(order);
                moduleRepository.save(module);
            }
        }
        return "redirect:/instructor/course/" + courseId + "/edit";
    }

    @PostMapping("/{courseId}/modules/{moduleId}/delete")
    public String deleteModule(@PathVariable Long courseId, @PathVariable Long moduleId,
                               @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            com.skilvorae.entity.Module module = moduleRepository.findById(moduleId).orElseThrow();
            if(module.getCourse().getId().equals(course.getId())) {
                moduleRepository.delete(module);
            }
        }
        return "redirect:/instructor/course/" + courseId + "/edit";
    }

    @PostMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/edit")
    public String editLesson(@PathVariable Long courseId, @PathVariable Long moduleId, @PathVariable Long lessonId,
                             @RequestParam String title, @RequestParam Integer order,
                             @RequestParam String content, 
                             @RequestParam(required = false) org.springframework.web.multipart.MultipartFile videoFile,
                             @RequestParam(required = false) org.springframework.web.multipart.MultipartFile pdfFile,
                             @RequestParam(required = false) org.springframework.web.multipart.MultipartFile pptFile,
                             @RequestParam(required = false) org.springframework.web.multipart.MultipartFile bookFile,
                             @RequestParam(required = false) boolean removeVideo,
                             @RequestParam(required = false) boolean removePdf,
                             @RequestParam(required = false) boolean removePpt,
                             @RequestParam(required = false) boolean removeBook,
                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            com.skilvorae.entity.Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
            if(lesson.getModule().getId().equals(moduleId) && lesson.getModule().getCourse().getId().equals(courseId)) {
                lesson.setTitle(title);
                lesson.setLessonOrder(order);
                lesson.setContent(content);
                
                if (removeVideo) {
                    if (lesson.getVideoUrl() != null && lesson.getVideoUrl().startsWith("/uploads/")) {
                        fileStorageService.deleteFile(lesson.getVideoUrl());
                    }
                    lesson.setVideoUrl(null);
                } else if (videoFile != null && !videoFile.isEmpty()) {
                    lesson.setVideoUrl(fileStorageService.saveFile(videoFile, "lessons/videos"));
                }
                
                if (removePdf) {
                    if (lesson.getPdfUrl() != null && lesson.getPdfUrl().startsWith("/uploads/")) {
                        fileStorageService.deleteFile(lesson.getPdfUrl());
                    }
                    lesson.setPdfUrl(null);
                } else if (pdfFile != null && !pdfFile.isEmpty()) {
                    lesson.setPdfUrl(fileStorageService.saveFile(pdfFile, "lessons/pdfs"));
                }
                
                if (removePpt) {
                    if (lesson.getPptUrl() != null && lesson.getPptUrl().startsWith("/uploads/")) {
                        fileStorageService.deleteFile(lesson.getPptUrl());
                    }
                    lesson.setPptUrl(null);
                } else if (pptFile != null && !pptFile.isEmpty()) {
                    lesson.setPptUrl(fileStorageService.saveFile(pptFile, "lessons/ppts"));
                }
                
                if (removeBook) {
                    if (lesson.getBookUrl() != null && lesson.getBookUrl().startsWith("/uploads/")) {
                        fileStorageService.deleteFile(lesson.getBookUrl());
                    }
                    lesson.setBookUrl(null);
                } else if (bookFile != null && !bookFile.isEmpty()) {
                    lesson.setBookUrl(fileStorageService.saveFile(bookFile, "lessons/books"));
                }
                
                lessonRepository.save(lesson);
            }
        }
        return "redirect:/instructor/course/" + courseId + "/edit";
    }

    @PostMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/delete")
    public String deleteLesson(@PathVariable Long courseId, @PathVariable Long moduleId, @PathVariable Long lessonId,
                               @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            com.skilvorae.entity.Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
            if(lesson.getModule().getId().equals(moduleId) && lesson.getModule().getCourse().getId().equals(courseId)) {
                lessonRepository.delete(lesson);
            }
        }
        return "redirect:/instructor/course/" + courseId + "/edit";
    }

    @PostMapping("/{courseId}/assignments/{assignmentId}/edit")
    public String editAssignment(@PathVariable Long courseId, @PathVariable Long assignmentId,
                                 @RequestParam String title, @RequestParam String description,
                                 @RequestParam Integer totalMarks, 
                                 @RequestParam(required = false) org.springframework.web.multipart.MultipartFile attachmentFile,
                                 @RequestParam(required = false) boolean removeAttachment,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            com.skilvorae.entity.Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();
            if(assignment.getModule().getCourse().getId().equals(courseId)) {
                assignment.setTitle(title);
                assignment.setDescription(description);
                assignment.setTotalMarks(totalMarks);
                
                if (removeAttachment) {
                    if (assignment.getAttachmentUrl() != null && assignment.getAttachmentUrl().startsWith("/uploads/")) {
                        fileStorageService.deleteFile(assignment.getAttachmentUrl());
                    }
                    assignment.setAttachmentUrl(null);
                } else if (attachmentFile != null && !attachmentFile.isEmpty()) {
                    assignment.setAttachmentUrl(fileStorageService.saveFile(attachmentFile, "assignments"));
                }
                
                assignmentRepository.save(assignment);
            }
        }
        return "redirect:/instructor/course/" + courseId + "/edit";
    }

    @PostMapping("/{courseId}/assignments/{assignmentId}/delete")
    public String deleteAssignment(@PathVariable Long courseId, @PathVariable Long assignmentId,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            com.skilvorae.entity.Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();
            if(assignment.getModule().getCourse().getId().equals(courseId)) {
                assignmentRepository.delete(assignment);
            }
        }
        return "redirect:/instructor/course/" + courseId + "/edit";
    }

    @PostMapping("/{courseId}/assessment/delete")
    public String deleteAssessment(@PathVariable Long courseId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        if(course.getInstructor().getId().equals(user.getId())) {
            com.skilvorae.entity.Assessment assessment = assessmentRepository.findByCourseId(courseId).orElse(null);
            if(assessment != null) {
                assessmentRepository.delete(assessment);
            }
        }
        return "redirect:/instructor/course/" + courseId + "/edit";
    }
}
