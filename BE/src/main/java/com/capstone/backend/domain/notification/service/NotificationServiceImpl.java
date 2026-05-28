package com.capstone.backend.domain.notification.service;

import com.capstone.backend.common.exception.BusinessException;
import com.capstone.backend.common.exception.ErrorCode;
import com.capstone.backend.domain.notification.dto.response.NotificationListResponse;
import com.capstone.backend.domain.notification.dto.response.NotificationResponse;
import com.capstone.backend.domain.notification.entity.Notification;
import com.capstone.backend.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional(readOnly = true)
    public NotificationListResponse getNotifications(UUID userId, String type, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);

        Page<Notification> pageResult;
        if (type != null && !type.isBlank()) {
            pageResult = notificationRepository.findByUserIdAndTypeOrderByCreatedAtDesc(userId, type, pageable);
        } else {
            pageResult = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        }

        long unreadCount = notificationRepository.countByUserIdAndIsReadFalse(userId);

        List<NotificationResponse> notifications = pageResult.getContent().stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());

        return new NotificationListResponse(unreadCount, notifications, pageResult.getTotalElements());
    }

    @Override
    @Transactional
    public int markAllAsRead(UUID userId) {
        return notificationRepository.markAllAsRead(userId);
    }

    @Override
    @Transactional
    public UUID markAsRead(UUID userId, UUID notificationId) {
        int updated = notificationRepository.markAsRead(notificationId, userId);
        if (updated == 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        return notificationId;
    }
}
