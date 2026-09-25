package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.NotificationDto;
import com.skilvorae.enums.NotificationType;
import com.skilvorae.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for NotificationApiController in SkilVorae backend.
 */
@WebMvcTest(NotificationApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class NotificationApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    private NotificationDto sampleNotification;

    @BeforeEach
    void setUp() {
        sampleNotification = NotificationDto.builder()
                .id(1L)
                .title("New Assignment Posted")
                .message("Instructor has posted Assignment 2: Spring Security Filters.")
                .type(NotificationType.ASSIGNMENT_DUE)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/notifications")
    class GetNotificationsTests {

        @Test
        @DisplayName("Should return notifications list for current user")
        void getNotifications_Success() throws Exception {
            when(notificationService.getUserNotifications(any())).thenReturn(List.of(sampleNotification));

            mockMvc.perform(get("/api/v1/notifications"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].title").value("New Assignment Posted"))
                    .andExpect(jsonPath("$[0].isRead").value(false));

            verify(notificationService, times(1)).getUserNotifications(any());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/notifications/{id}/read")
    class MarkAsReadTests {

        @Test
        @DisplayName("Should mark notification as read")
        void markAsRead_Success() throws Exception {
            doNothing().when(notificationService).markAsRead(eq(1L), any());

            mockMvc.perform(put("/api/v1/notifications/1/read"))
                    .andExpect(status().isOk());

            verify(notificationService, times(1)).markAsRead(eq(1L), any());
        }
    }
}
