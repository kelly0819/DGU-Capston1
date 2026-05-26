package com.capstone.backend.domain.notification.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.common.util.SecurityUtil;
import com.capstone.backend.domain.notification.dto.NotificationDto;
import com.capstone.backend.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationDto.Response>>> getNotifications() {
        UUID userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(notificationService.getNotifications(userId)));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable UUID notificationId) {
        UUID userId = SecurityUtil.getCurrentUserId();
        notificationService.markAsRead(userId, notificationId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead() {
        UUID userId = SecurityUtil.getCurrentUserId();
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
