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
public class CertificateDto {
    private Long id;
    private String certificateCode;
    private String studentName;
    private String studentEmail;
    private Long courseId;
    private String courseTitle;
    private String instructorName;
    private LocalDateTime issuedAt;
}
