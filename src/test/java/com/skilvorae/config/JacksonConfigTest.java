package com.skilvorae.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test suite verifying Jackson ObjectMapper configuration in SkilVorae backend.
 */
@SpringBootTest
public class JacksonConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should serialize LocalDateTime using ISO-8601 string format")
    void objectMapper_LocalDateTimeSerialization() throws Exception {
        LocalDateTime now = LocalDateTime.of(2026, 9, 25, 12, 0, 0);
        String json = objectMapper.writeValueAsString(now);

        assertNotNull(json);
        assertTrue(json.contains("2026-09-25T12:00:00"));
    }
}
