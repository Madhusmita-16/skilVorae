package com.skilvorae.service;

import com.skilvorae.dto.AssignmentDto;
import com.skilvorae.dto.AssignmentSubmissionDto;

import java.util.List;

/**
 * Service interface for managing course homework assignments and student submission grading for SkilVorae.
 */
public interface AssignmentService {

    /**
     * Creates a new course assignment.
     *
     * @param courseId Target Course ID.
     * @param title Assignment title.
     * @param instructions Markdown instructions.
     * @param maxScore Maximum score points.
     * @return AssignmentDto created payload.
     */
    AssignmentDto createAssignment(Long courseId, String title, String instructions, Integer maxScore);

    /**
     * Submits student solution for assignment.
     *
     * @param userId Student User ID.
     * @param assignmentId Target Assignment ID.
     * @param submissionUrl File/document submission URL.
     * @param notes Student notes.
     * @return AssignmentSubmissionDto submitted payload.
     */
    AssignmentSubmissionDto submitAssignment(Long userId, Long assignmentId, String submissionUrl, String notes);

    /**
     * Grades student assignment submission.
     *
     * @param submissionId Submission ID.
     * @param score Awarded score.
     * @param feedback Instructor feedback.
     * @return AssignmentSubmissionDto graded payload.
     */
    AssignmentSubmissionDto gradeSubmission(Long submissionId, Integer score, String feedback);

    /**
     * Retrieves all assignments for a course.
     *
     * @param courseId Course ID.
     * @return List of AssignmentDto.
     */
    List<AssignmentDto> getCourseAssignments(Long courseId);
}
