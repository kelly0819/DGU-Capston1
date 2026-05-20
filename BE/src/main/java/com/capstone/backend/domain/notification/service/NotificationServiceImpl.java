package com.capstone.backend.domain.notification.service;

import com.capstone.backend.domain.notification.dto.NotificationDto;
import com.capstone.backend.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationDto.Response> getNotifications(Long userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void markAsRead(Long userId, Long notificationId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void markAllAsRead(Long userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
