package com.skilvorae.service;

import com.skilvorae.dto.NotificationDto;
import com.skilvorae.entity.Notification;
import com.skilvorae.entity.User;
import com.skilvorae.enums.NotificationType;
import com.skilvorae.repository.NotificationRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 unit test suite for NotificationServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User user;
    private Notification notification;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(10L);
        user.setEmail("student@skilvorae.com");

        notification = new Notification();
        notification.setId(1L);
        notification.setUser(user);
        notification.setTitle("Course Completed");
        notification.setMessage("Congratulations! You completed Java Enterprise.");
        notification.setType(NotificationType.CERTIFICATE_ISSUED);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should fetch user notifications ordered by date descending")
    void getUserNotifications_Success() {
        when(userRepository.findByEmail("student@skilvorae.com")).thenReturn(Optional.of(user));
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(10L)).thenReturn(List.of(notification));

        List<NotificationDto> notifications = notificationService.getUserNotifications("student@skilvorae.com");

        assertNotNull(notifications);
        assertEquals(1, notifications.size());
        assertEquals("Course Completed", notifications.get(0).getTitle());
    }

    @Test
    @DisplayName("Should mark notification as read")
    void markAsRead_Success() {
        when(userRepository.findByEmail("student@skilvorae.com")).thenReturn(Optional.of(user));
        when(notificationRepository.findByIdAndUserId(1L, 10L)).thenReturn(Optional.of(notification));

        notificationService.markAsRead(1L, "student@skilvorae.com");

        assertTrue(notification.getIsRead());
        verify(notificationRepository, times(1)).save(notification);
    }
}
