package com.capstone.backend.domain.recommendation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class RecommendationRequest {

    @NotNull(message = "baseProductId는 필수입니다.")
    private UUID baseProductId;

    private String searchPurpose;       // DAILY | GIFT | TRAVEL | SPECIAL
    private Integer priceTolerancePercent;  // 0 | 5 | 10 | 20 | null
}
