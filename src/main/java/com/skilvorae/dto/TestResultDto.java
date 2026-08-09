package com.skilvorae.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultDto {
    private Long attemptId;
    private Long assessmentId;
    private String assessmentTitle;
    private Long courseId;
    private String courseTitle;
    private Double score;
    private Integer passingScore;
    private Boolean passed;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private LocalDateTime attemptedAt;
    private String certificateCode;
}
