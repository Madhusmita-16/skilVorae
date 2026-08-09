package com.skilvorae.controller.api;

import com.skilvorae.dto.ApiResponse;
import com.skilvorae.dto.NotificationDto;
import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<NotificationDto> notifications = notificationService.getRecentNotifications(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Notifications fetched", notifications));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUnreadCount(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.ok(ApiResponse.success("Count", Map.of("count", 0L)));
        }
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        long count = notificationService.getUnreadCount(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Unread count fetched", Map.of("count", count)));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        notificationService.markAsRead(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Marked as read", null));
    }

    @PostMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.ok(ApiResponse.success("All marked as read", null));
    }
}
