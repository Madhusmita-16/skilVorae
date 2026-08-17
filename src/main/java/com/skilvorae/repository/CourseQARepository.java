package com.skilvorae.repository;

import com.skilvorae.entity.CourseQA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseQARepository extends JpaRepository<CourseQA, Long> {
    List<CourseQA> findByCourseIdOrderByAskedAtDesc(Long courseId);
}
