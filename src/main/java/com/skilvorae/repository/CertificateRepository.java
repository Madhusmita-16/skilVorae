package com.skilvorae.repository;

import com.skilvorae.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    Optional<Certificate> findByCertificateCode(String certificateCode);
    Optional<Certificate> findByUserIdAndCourseId(Long userId, Long courseId);
    List<Certificate> findByUserIdOrderByIssuedAtDesc(Long userId);
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
}
