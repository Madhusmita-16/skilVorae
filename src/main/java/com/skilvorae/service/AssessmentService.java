package com.skilvorae.service;

import com.skilvorae.dto.*;
import com.skilvorae.entity.*;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final TestAttemptRepository testAttemptRepository;
    private final UserRepository userRepository;
    private final CertificateService certificateService;

    @Transactional(readOnly = true)
    public AssessmentDto getAssessmentByCourseId(Long courseId, boolean includeCorrectAnswers) {
        Assessment assessment = assessmentRepository.findByCourseId(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("No assessment found for this course"));

        List<QuestionDto> questionDtos = assessment.getQuestions().stream().map(question -> {
            List<QuestionOptionDto> optionDtos = question.getOptions().stream().map(option -> QuestionOptionDto.builder()
                    .id(option.getId())
                    .optionText(option.getOptionText())
                    .isCorrect(includeCorrectAnswers ? option.getIsCorrect() : null)
                    .build()).collect(Collectors.toList());

            return QuestionDto.builder()
                    .id(question.getId())
                    .questionText(question.getQuestionText())
                    .points(question.getPoints())
                    .options(optionDtos)
                    .build();
        }).collect(Collectors.toList());

        return AssessmentDto.builder()
                .id(assessment.getId())
                .courseId(courseId)
                .courseTitle(assessment.getCourse().getTitle())
                .title(assessment.getTitle())
                .passingScore(assessment.getPassingScore())
                .timeLimitMinutes(assessment.getTimeLimitMinutes())
                .questions(questionDtos)
                .build();
    }

    @Transactional
    public TestResultDto submitAssessment(Long userId, AssessmentSubmitDto submitDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Assessment assessment = assessmentRepository.findById(submitDto.getAssessmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));

        Map<Long, Long> userAnswers = submitDto.getAnswers() != null ? submitDto.getAnswers() : Collections.emptyMap();

        int totalQuestions = assessment.getQuestions().size();
        int correctAnswers = 0;

        for (Question question : assessment.getQuestions()) {
            Long selectedOptionId = userAnswers.get(question.getId());
            if (selectedOptionId != null) {
                Optional<QuestionOption> optionOpt = questionOptionRepository.findById(selectedOptionId);
                if (optionOpt.isPresent() && Boolean.TRUE.equals(optionOpt.get().getIsCorrect())) {
                    correctAnswers++;
                }
            }
        }

        double scorePercentage = totalQuestions > 0 ? ((double) correctAnswers / totalQuestions) * 100.0 : 0.0;
        boolean passed = scorePercentage >= assessment.getPassingScore();

        TestAttempt attempt = TestAttempt.builder()
                .user(user)
                .assessment(assessment)
                .score(scorePercentage)
                .passed(passed)
                .totalQuestions(totalQuestions)
                .correctAnswers(correctAnswers)
                .build();

        attempt = testAttemptRepository.save(attempt);

        String certCode = null;
        if (passed) {
            Certificate cert = certificateService.issueCertificate(userId, assessment.getCourse().getId());
            certCode = cert.getCertificateCode();
        }

        return TestResultDto.builder()
                .attemptId(attempt.getId())
                .assessmentId(assessment.getId())
                .assessmentTitle(assessment.getTitle())
                .courseId(assessment.getCourse().getId())
                .courseTitle(assessment.getCourse().getTitle())
                .score(scorePercentage)
                .passingScore(assessment.getPassingScore())
                .passed(passed)
                .totalQuestions(totalQuestions)
                .correctAnswers(correctAnswers)
                .attemptedAt(attempt.getAttemptedAt())
                .certificateCode(certCode)
                .build();
    }

    @Transactional(readOnly = true)
    public TestResultDto getAttemptResult(Long attemptId, Long userId) {
        TestAttempt attempt = testAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt result not found"));

        if (!attempt.getUser().getId().equals(userId)) {
            throw new BadRequestException("Unauthorized to view this attempt result");
        }

        String certCode = certificateService.getCertificateByCourse(userId, attempt.getAssessment().getCourse().getId())
                .map(CertificateDto::getCertificateCode)
                .orElse(null);

        return TestResultDto.builder()
                .attemptId(attempt.getId())
                .assessmentId(attempt.getAssessment().getId())
                .assessmentTitle(attempt.getAssessment().getTitle())
                .courseId(attempt.getAssessment().getCourse().getId())
                .courseTitle(attempt.getAssessment().getCourse().getTitle())
                .score(attempt.getScore())
                .passingScore(attempt.getAssessment().getPassingScore())
                .passed(attempt.getPassed())
                .totalQuestions(attempt.getTotalQuestions())
                .correctAnswers(attempt.getCorrectAnswers())
                .attemptedAt(attempt.getAttemptedAt())
                .certificateCode(certCode)
                .build();
    }
}
