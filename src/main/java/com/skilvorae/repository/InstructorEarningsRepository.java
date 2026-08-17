package com.skilvorae.repository;

import com.skilvorae.entity.InstructorEarnings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstructorEarningsRepository extends JpaRepository<InstructorEarnings, Long> {
    List<InstructorEarnings> findByInstructorId(Long instructorId);
    List<InstructorEarnings> findByInstructorIdOrderByEarnedAtDesc(Long instructorId);
}
