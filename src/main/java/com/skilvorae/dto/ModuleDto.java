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
public class ModuleDto {
    private Long id;
    private Long courseId;
    private String title;
    private Integer moduleOrder;
    private List<LessonDto> lessons;
}
