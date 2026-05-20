package com.capstone.backend.domain.pricetracking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class PriceTrackingDto {

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long productId;
        private Integer price;
        private LocalDateTime trackedAt;
    }
}
