package com.skilvorae.service.impl;

import com.skilvorae.dto.AssessmentDto;
import com.skilvorae.dto.AssessmentSubmitDto;
import com.skilvorae.dto.TestResultDto;
import com.skilvorae.entity.Assessment;
import com.skilvorae.entity.Question;
import com.skilvorae.entity.QuestionOption;
import com.skilvorae.entity.TestAttempt;
import com.skilvorae.entity.User;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.AssessmentRepository;
import com.skilvorae.repository.TestAttemptRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enterprise implementation of AssessmentService for SkilVorae quiz testing engine,
 * score calculation, test attempt history, and pass/fail evaluations.
 */
@Service
@Transactional
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final TestAttemptRepository testAttemptRepository;
    private final UserRepository userRepository;

    @Autowired
    public AssessmentServiceImpl(AssessmentRepository assessmentRepository,
                                 TestAttemptRepository testAttemptRepository,
                                 UserRepository userRepository) {
        this.assessmentRepository = assessmentRepository;
        this.testAttemptRepository = testAttemptRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentDto getAssessmentById(Long id) {
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment", "id", id));

        return mapToDto(assessment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentDto> getAssessmentsByCourse(Long courseId) {
        return assessmentRepository.findByCourseId(courseId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TestResultDto submitAssessment(Long userId, Long assessmentId, AssessmentSubmitDto submitDto) {
        if (submitDto == null) {
            throw new BadRequestException("Assessment submission data cannot be null.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment", "id", assessmentId));

        int totalQuestions = assessment.getQuestions() != null ? assessment.getQuestions().size() : 0;
        int correctAnswers = 0;

        Map<Long, Long> userAnswers = submitDto.getAnswers();
        if (userAnswers != null && assessment.getQuestions() != null) {
            for (Question q : assessment.getQuestions()) {
                Long selectedOptionId = userAnswers.get(q.getId());
                if (selectedOptionId != null && q.getOptions() != null) {
                    for (QuestionOption opt : q.getOptions()) {
                        if (opt.getId().equals(selectedOptionId) && opt.isCorrect()) {
                            correctAnswers++;
                            break;
                        }
                    }
                }
            }
        }

        int scorePercentage = totalQuestions > 0 ? (int) Math.round(((double) correctAnswers / totalQuestions) * 100) : 0;
        int passScore = assessment.getPassPercentage() != null ? assessment.getPassPercentage() : 70;
        boolean passed = scorePercentage >= passScore;

        TestAttempt attempt = new TestAttempt();
        attempt.setUser(user);
        attempt.setAssessment(assessment);
        attempt.setScorePercentage(scorePercentage);
        attempt.setCorrectAnswers(correctAnswers);
        attempt.setTotalQuestions(totalQuestions);
        attempt.setPassed(passed);
        attempt.setAttemptedAt(LocalDateTime.now());

        testAttemptRepository.save(attempt);

        TestResultDto result = new TestResultDto();
        result.setAssessmentId(assessmentId);
        result.setAssessmentTitle(assessment.getTitle());
        result.setScorePercentage(scorePercentage);
        result.setCorrectAnswers(correctAnswers);
        result.setTotalQuestions(totalQuestions);
        result.setPassed(passed);
        result.setPassPercentage(passScore);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestResultDto> getUserTestHistory(Long userId) {
        List<TestAttempt> attempts = testAttemptRepository.findByUserId(userId);
        return attempts.stream().map(a -> {
            TestResultDto dto = new TestResultDto();
            dto.setAssessmentId(a.getAssessment() != null ? a.getAssessment().getId() : null);
            dto.setAssessmentTitle(a.getAssessment() != null ? a.getAssessment().getTitle() : "");
            dto.setScorePercentage(a.getScorePercentage());
            dto.setCorrectAnswers(a.getCorrectAnswers());
            dto.setTotalQuestions(a.getTotalQuestions());
            dto.setPassed(a.isPassed());
            return dto;
        }).collect(Collectors.toList());
    }

    private AssessmentDto mapToDto(Assessment assessment) {
        AssessmentDto dto = new AssessmentDto();
        dto.setId(assessment.getId());
        dto.setTitle(assessment.getTitle());
        dto.setDescription(assessment.getDescription());
        dto.setTimeLimitMinutes(assessment.getTimeLimitMinutes());
        dto.setPassPercentage(assessment.getPassPercentage());
        dto.setTotalQuestions(assessment.getQuestions() != null ? assessment.getQuestions().size() : 0);
        return dto;
    }
}
