package com.skilvorae.service;

import com.skilvorae.dto.CertificateDto;
import com.skilvorae.entity.*;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public Certificate issueCertificate(Long userId, Long courseId) {
        Optional<Certificate> existing = certificateRepository.findByUserIdAndCourseId(userId, courseId);
        if (existing.isPresent()) {
            return existing.get();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        String certCode = generateUniqueCode();

        Certificate certificate = Certificate.builder()
                .certificateCode(certCode)
                .user(user)
                .course(course)
                .issuedAt(LocalDateTime.now())
                .build();

        return certificateRepository.save(certificate);
    }

    @Transactional(readOnly = true)
    public CertificateDto getCertificateByCode(String code) {
        Certificate cert = certificateRepository.findByCertificateCode(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found with code: " + code));
        return mapToDto(cert);
    }

    @Transactional(readOnly = true)
    public Optional<CertificateDto> getCertificateByCourse(Long userId, Long courseId) {
        return certificateRepository.findByUserIdAndCourseId(userId, courseId).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<CertificateDto> getUserCertificates(Long userId) {
        return certificateRepository.findByUserIdOrderByIssuedAtDesc(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private String generateUniqueCode() {
        SecureRandom random = new SecureRandom();
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder("SKV-2026-");
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public CertificateDto mapToDto(Certificate cert) {
        return CertificateDto.builder()
                .id(cert.getId())
                .certificateCode(cert.getCertificateCode())
                .studentName(cert.getUser().getFullName())
                .studentEmail(cert.getUser().getEmail())
                .courseId(cert.getCourse().getId())
                .courseTitle(cert.getCourse().getTitle())
                .instructorName(cert.getCourse().getInstructorName())
                .issuedAt(cert.getIssuedAt())
                .build();
    }
}
