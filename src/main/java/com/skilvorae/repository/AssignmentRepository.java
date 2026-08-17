package com.skilvorae.repository;

import com.skilvorae.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByModuleId(Long moduleId);
}
