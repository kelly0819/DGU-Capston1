package com.capstone.backend.domain.pricetracking.dto.response;

import com.capstone.backend.domain.pricetracking.entity.PriceTracking;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class PriceTrackingCreateResponse {
    private UUID trackingId;
    private UUID productId;
    private Integer targetPrice;
    private boolean alertEnabled;
    private LocalDateTime createdAt;

    public static PriceTrackingCreateResponse from(PriceTracking pt) {
        return PriceTrackingCreateResponse.builder()
                .trackingId(pt.getId())
                .productId(pt.getProduct().getId())
                .targetPrice(pt.getTargetPrice())
                .alertEnabled(pt.isAlertEnabled())
                .createdAt(pt.getCreatedAt())
                .build();
    }
}
