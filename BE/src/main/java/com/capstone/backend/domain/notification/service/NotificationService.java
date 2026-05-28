package com.capstone.backend.domain.notification.service;

import com.capstone.backend.domain.notification.dto.response.NotificationListResponse;

import java.util.UUID;

public interface NotificationService {
    NotificationListResponse getNotifications(UUID userId, String type, int page, int size);
    int markAllAsRead(UUID userId);
    UUID markAsRead(UUID userId, UUID notificationId);
}
