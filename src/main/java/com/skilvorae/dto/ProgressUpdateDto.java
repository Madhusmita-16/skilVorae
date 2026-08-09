package com.skilvorae.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressUpdateDto {

    @NotNull(message = "Lesson ID is required")
    private Long lessonId;

    @NotNull(message = "Completion status is required")
    private Boolean completed;
}
