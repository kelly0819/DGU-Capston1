package com.capstone.backend.domain.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NotificationListResponse {
    private long unreadCount;
    private List<NotificationResponse> notifications;
    private long total;
}
