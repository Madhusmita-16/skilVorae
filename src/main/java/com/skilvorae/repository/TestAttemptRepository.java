package com.skilvorae.repository;

import com.skilvorae.entity.TestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {
    List<TestAttempt> findByUserIdAndAssessmentIdOrderByAttemptedAtDesc(Long userId, Long assessmentId);
    List<TestAttempt> findByUserIdOrderByAttemptedAtDesc(Long userId);
    
    @Query("SELECT AVG(ta.score) FROM TestAttempt ta WHERE ta.user.id = :userId")
    Double findAverageScoreByUserId(@Param("userId") Long userId);
    
    Optional<TestAttempt> findTopByUserIdAndAssessmentIdAndPassedTrue(Long userId, Long assessmentId);
}
