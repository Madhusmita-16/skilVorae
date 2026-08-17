package com.skilvorae.repository;

import com.skilvorae.entity.CourseBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseBatchRepository extends JpaRepository<CourseBatch, Long> {
    List<CourseBatch> findByCourseId(Long courseId);
}
