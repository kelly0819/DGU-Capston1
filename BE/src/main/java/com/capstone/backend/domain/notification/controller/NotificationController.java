package com.capstone.backend.domain.notification.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.domain.notification.dto.response.NotificationListResponse;
import com.capstone.backend.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "4. Notification", description = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<NotificationListResponse>> getNotifications(
            @AuthenticationPrincipal UUID userId,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        NotificationListResponse result = notificationService.getNotifications(userId, type, page, size);
        ApiResponse.Meta meta = new ApiResponse.Meta(page, size, result.getTotal());
        return ResponseEntity.ok(ApiResponse.success(result, meta));
    }

    @Operation(summary = "전체 알림 읽음 처리")
    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> markAllAsRead(
            @AuthenticationPrincipal UUID userId) {
        int updatedCount = notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("updatedCount", updatedCount)));
    }

    @Operation(summary = "알림 읽음 처리")
    @PatchMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> markAsRead(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID notificationId) {
        UUID readId = notificationService.markAsRead(userId, notificationId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("notificationId", readId, "isRead", true)));
    }
}
