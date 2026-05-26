package com.capstone.backend.domain.notification.service;

import com.capstone.backend.domain.notification.dto.NotificationDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationDto.Response> getNotifications(UUID userId);
    void markAsRead(UUID userId, UUID notificationId);
    void markAllAsRead(UUID userId);
}
