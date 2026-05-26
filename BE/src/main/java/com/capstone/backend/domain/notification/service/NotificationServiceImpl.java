package com.capstone.backend.domain.notification.service;

import com.capstone.backend.domain.notification.dto.NotificationDto;
import com.capstone.backend.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationDto.Response> getNotifications(UUID userId) {
        return Collections.emptyList();
    }

    @Override
    public void markAsRead(UUID userId, UUID notificationId) {
    }

    @Override
    public void markAllAsRead(UUID userId) {
    }
}
