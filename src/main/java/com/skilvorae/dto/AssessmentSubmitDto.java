package com.skilvorae.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentSubmitDto {
    private Long assessmentId;
    // Map of questionId -> selectedOptionId
    private Map<Long, Long> answers;
}
