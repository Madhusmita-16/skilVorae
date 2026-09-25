package com.skilvorae.service.impl;

import com.skilvorae.dto.CertificateDto;
import com.skilvorae.entity.Certificate;
import com.skilvorae.entity.Course;
import com.skilvorae.entity.Enrollment;
import com.skilvorae.entity.User;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.CertificateRepository;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Enterprise implementation of CertificateService for generating verified course completion certificates.
 */
@Service
@Transactional
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  EnrollmentRepository enrollmentRepository,
                                  CourseRepository courseRepository,
                                  UserRepository userRepository) {
        this.certificateRepository = certificateRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CertificateDto generateCertificate(Long userId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new BadRequestException("User is not enrolled in this course."));

        if (enrollment.getProgressPercentage() < 100) {
            throw new BadRequestException("Course completion (100% progress) is required to generate a certificate.");
        }

        Certificate certificate = certificateRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId).orElseThrow();
                    Course course = courseRepository.findById(courseId).orElseThrow();
                    Certificate cert = new Certificate();
                    cert.setUser(user);
                    cert.setCourse(course);
                    cert.setCertificateCode("SKV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                    cert.setIssuedAt(LocalDateTime.now());
                    return certificateRepository.save(cert);
                });

        return mapToDto(certificate);
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateDto getCertificateByCode(String code) {
        Certificate cert = certificateRepository.findByCertificateCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "code", code));

        return mapToDto(cert);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateDto> getUserCertificates(Long userId) {
        return certificateRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CertificateDto mapToDto(Certificate c) {
        CertificateDto dto = new CertificateDto();
        dto.setId(c.getId());
        dto.setCertificateCode(c.getCertificateCode());
        dto.setRecipientName(c.getUser() != null ? c.getUser().getFirstName() + " " + c.getUser().getLastName() : "");
        dto.setCourseTitle(c.getCourse() != null ? c.getCourse().getTitle() : "");
        dto.setInstructorName(c.getCourse() != null && c.getCourse().getInstructor() != null ?
                c.getCourse().getInstructor().getFirstName() + " " + c.getCourse().getInstructor().getLastName() : "SkilVorae Faculty");
        dto.setIssuedAt(c.getIssuedAt());
        return dto;
    }
}
