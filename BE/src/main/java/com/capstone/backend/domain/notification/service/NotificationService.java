package com.capstone.backend.domain.notification.service;

import com.capstone.backend.domain.notification.dto.response.NotificationListResponse;
import com.capstone.backend.domain.notification.dto.response.NotificationResponse;

import java.util.UUID;

public interface NotificationService {
    NotificationListResponse getNotifications(UUID userId, String type, int page, int size);
    int markAllAsRead(UUID userId);
    UUID markAsRead(UUID userId, UUID notificationId);
    NotificationResponse createTestNotification(UUID userId, String type, String title, String body, String actionUrl);
}
