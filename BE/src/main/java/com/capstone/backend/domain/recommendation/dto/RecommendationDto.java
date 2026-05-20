package com.capstone.backend.domain.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class RecommendationDto {

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long productId;
        private String productName;
        private Integer productPrice;
        private String productImageUrl;
        private Double score;
    }
}
