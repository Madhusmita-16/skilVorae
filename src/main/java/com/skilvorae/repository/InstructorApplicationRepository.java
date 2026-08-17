package com.skilvorae.repository;

import com.skilvorae.entity.InstructorApplication;
import com.skilvorae.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstructorApplicationRepository extends JpaRepository<InstructorApplication, Long> {
    List<InstructorApplication> findByStatus(ApplicationStatus status);
}
