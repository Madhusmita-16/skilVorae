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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCertificateCode() { return certificateCode; }
    public void setCertificateCode(String certificateCode) { this.certificateCode = certificateCode; }
    public String getStudentName() { return studentName; }
    public String getRecipientName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }
    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }

    public static CertificateDtoBuilder builder() { return new CertificateDtoBuilder(); }

    public static class CertificateDtoBuilder {
        private Long id;
        private String certificateCode;
        private String studentName;
        private String studentEmail;
        private Long courseId;
        private String courseTitle;
        private String instructorName;
        private LocalDateTime issuedAt;

        public CertificateDtoBuilder id(Long id) { this.id = id; return this; }
        public CertificateDtoBuilder certificateCode(String certificateCode) { this.certificateCode = certificateCode; return this; }
        public CertificateDtoBuilder studentName(String studentName) { this.studentName = studentName; return this; }
        public CertificateDtoBuilder studentEmail(String studentEmail) { this.studentEmail = studentEmail; return this; }
        public CertificateDtoBuilder courseId(Long courseId) { this.courseId = courseId; return this; }
        public CertificateDtoBuilder courseTitle(String courseTitle) { this.courseTitle = courseTitle; return this; }
        public CertificateDtoBuilder instructorName(String instructorName) { this.instructorName = instructorName; return this; }
        public CertificateDtoBuilder issuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; return this; }

        public CertificateDto build() {
            return new CertificateDto(id, certificateCode, studentName, studentEmail, courseId, courseTitle, instructorName, issuedAt);
        }
    }
}
