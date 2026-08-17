package com.skilvorae.repository;

import com.skilvorae.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCourseBatchId(Long batchId);
}
