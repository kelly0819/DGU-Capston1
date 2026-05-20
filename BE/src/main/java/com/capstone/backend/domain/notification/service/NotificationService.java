package com.capstone.backend.domain.notification.service;

import com.capstone.backend.domain.notification.dto.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<NotificationDto.Response> getNotifications(Long userId);
    void markAsRead(Long userId, Long notificationId);
    void markAllAsRead(Long userId);
}
