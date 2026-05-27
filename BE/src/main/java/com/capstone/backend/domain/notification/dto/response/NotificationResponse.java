package com.capstone.backend.domain.notification.dto.response;

import com.capstone.backend.domain.notification.entity.Notification;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder
public class NotificationResponse {

    private UUID notificationId;
    private String type;
    private String title;
    private String body;

    @JsonProperty("isRead")
    private boolean isRead;

    private String actionUrl;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification n) {
        return NotificationResponse.builder()
                .notificationId(n.getId())
                .type(n.getType())
                .title(n.getTitle())
                .body(n.getBody())
                .isRead(n.isRead())
                .actionUrl(n.getActionUrl())
                .metadata(n.getMetadata())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
