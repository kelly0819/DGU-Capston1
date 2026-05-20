package com.capstone.backend.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class NotificationDto {

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String message;
        private boolean isRead;
        private LocalDateTime createdAt;
    }
}
