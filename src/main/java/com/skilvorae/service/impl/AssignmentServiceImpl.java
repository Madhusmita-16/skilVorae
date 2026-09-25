package com.skilvorae.service.impl;

import com.skilvorae.dto.AssignmentDto;
import com.skilvorae.dto.AssignmentSubmissionDto;
import com.skilvorae.entity.Assignment;
import com.skilvorae.entity.AssignmentSubmission;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.User;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.AssignmentRepository;
import com.skilvorae.repository.AssignmentSubmissionRepository;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enterprise implementation of AssignmentService.
 */
@Service
@Transactional
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository submissionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public AssignmentServiceImpl(AssignmentRepository assignmentRepository,
                                 AssignmentSubmissionRepository submissionRepository,
                                 CourseRepository courseRepository,
                                 UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AssignmentDto createAssignment(Long courseId, String title, String instructions, Integer maxScore) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        Assignment a = new Assignment();
        a.setCourse(course);
        a.setTitle(title);
        a.setInstructions(instructions);
        a.setMaxScore(maxScore != null ? maxScore : 100);

        Assignment saved = assignmentRepository.save(a);
        return mapToDto(saved);
    }

    @Override
    public AssignmentSubmissionDto submitAssignment(Long userId, Long assignmentId, String submissionUrl, String notes) {
        User student = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment", "id", assignmentId));

        AssignmentSubmission sub = new AssignmentSubmission();
        sub.setStudent(student);
        sub.setAssignment(assignment);
        sub.setSubmissionUrl(submissionUrl);
        sub.setNotes(notes);
        sub.setSubmittedAt(LocalDateTime.now());

        AssignmentSubmission saved = submissionRepository.save(sub);
        return mapToSubmissionDto(saved);
    }

    @Override
    public AssignmentSubmissionDto gradeSubmission(Long submissionId, Integer score, String feedback) {
        AssignmentSubmission sub = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("AssignmentSubmission", "id", submissionId));

        sub.setScore(score);
        sub.setInstructorFeedback(feedback);

        AssignmentSubmission saved = submissionRepository.save(sub);
        return mapToSubmissionDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentDto> getCourseAssignments(Long courseId) {
        return assignmentRepository.findByCourseId(courseId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private AssignmentDto mapToDto(Assignment a) {
        AssignmentDto dto = new AssignmentDto();
        dto.setId(a.getId());
        dto.setCourseId(a.getCourse() != null ? a.getCourse().getId() : null);
        dto.setTitle(a.getTitle());
        dto.setInstructions(a.getInstructions());
        dto.setMaxScore(a.getMaxScore());
        dto.setDueDate(a.getDueDate());
        return dto;
    }

    private AssignmentSubmissionDto mapToSubmissionDto(AssignmentSubmission sub) {
        AssignmentSubmissionDto dto = new AssignmentSubmissionDto();
        dto.setId(sub.getId());
        dto.setAssignmentId(sub.getAssignment() != null ? sub.getAssignment().getId() : null);
        dto.setAssignmentTitle(sub.getAssignment() != null ? sub.getAssignment().getTitle() : "");
        dto.setStudentId(sub.getStudent() != null ? sub.getStudent().getId() : null);
        dto.setStudentName(sub.getStudent() != null ? sub.getStudent().getFirstName() + " " + sub.getStudent().getLastName() : "");
        dto.setSubmissionUrl(sub.getSubmissionUrl());
        dto.setNotes(sub.getNotes());
        dto.setScore(sub.getScore());
        dto.setInstructorFeedback(sub.getInstructorFeedback());
        dto.setSubmittedAt(sub.getSubmittedAt());
        return dto;
    }
}
