package com.skilvorae.util;

import com.skilvorae.dto.QuizSubmissionResult;
import com.skilvorae.entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test suite for QuizScorerUtil in SkilVorae backend.
 */
public class QuizScorerUtilTest {

    private Question q1;
    private Question q2;

    @BeforeEach
    void setUp() {
        q1 = new Question();
        q1.setId(101L);
        q1.setPoints(10);
        q1.setCorrectOptionIndex(1);

        q2 = new Question();
        q2.setId(102L);
        q2.setPoints(15);
        q2.setCorrectOptionIndex(3);
    }

    @Test
    @DisplayName("Should score quiz accurately when all answers are correct")
    void scoreQuiz_AllCorrect() {
        Map<Long, Integer> userAnswers = Map.of(101L, 1, 102L, 3);
        List<Question> questions = List.of(q1, q2);

        QuizSubmissionResult result = QuizScorerUtil.calculateQuizScore(1L, userAnswers, questions, 70.0);

        assertNotNull(result);
        assertEquals(100.0, result.getScore());
        assertTrue(result.getPassed());
        assertEquals(25, result.getEarnedPoints());
        assertEquals(25, result.getTotalPoints());
    }

    @Test
    @DisplayName("Should score quiz and detect failing grade below passing percentage")
    void scoreQuiz_PartialCorrect_Failed() {
        Map<Long, Integer> userAnswers = Map.of(101L, 1, 102L, 0); // q2 wrong answer
        List<Question> questions = List.of(q1, q2);

        QuizSubmissionResult result = QuizScorerUtil.calculateQuizScore(1L, userAnswers, questions, 70.0);

        assertNotNull(result);
        assertEquals(40.0, result.getScore(), 0.1);
        assertFalse(result.getPassed());
        assertEquals(10, result.getEarnedPoints());
    }
}
