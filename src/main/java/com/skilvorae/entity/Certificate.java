package com.skilvorae.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificates", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "course_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String certificateCode; // e.g. SKV-2026-A8F2K

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @PrePersist
    protected void onCreate() {
        if (this.issuedAt == null) {
            this.issuedAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCertificateCode() { return certificateCode; }
    public void setCertificateCode(String certificateCode) { this.certificateCode = certificateCode; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }

    public static CertificateBuilder builder() { return new CertificateBuilder(); }

    public static class CertificateBuilder {
        private Long id;
        private String certificateCode;
        private User user;
        private Course course;
        private LocalDateTime issuedAt;

        public CertificateBuilder id(Long id) { this.id = id; return this; }
        public CertificateBuilder certificateCode(String certificateCode) { this.certificateCode = certificateCode; return this; }
        public CertificateBuilder user(User user) { this.user = user; return this; }
        public CertificateBuilder course(Course course) { this.course = course; return this; }
        public CertificateBuilder issuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; return this; }

        public Certificate build() {
            Certificate cert = new Certificate(id, certificateCode, user, course, issuedAt);
            if (cert.getIssuedAt() == null) {
                cert.setIssuedAt(LocalDateTime.now());
            }
            return cert;
        }
    }
}
