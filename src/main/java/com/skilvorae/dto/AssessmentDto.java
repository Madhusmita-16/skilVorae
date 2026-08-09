package com.skilvorae.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDto {
    private Long id;
    private Long courseId;
    private String courseTitle;
    private String title;
    private Integer passingScore;
    private Integer timeLimitMinutes;
    private List<QuestionDto> questions;
}
