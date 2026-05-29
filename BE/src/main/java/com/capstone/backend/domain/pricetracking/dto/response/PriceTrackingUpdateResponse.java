package com.capstone.backend.domain.pricetracking.dto.response;

import com.capstone.backend.domain.pricetracking.entity.PriceTracking;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class PriceTrackingUpdateResponse {
    private UUID trackingId;
    private Integer targetPrice;
    private boolean alertEnabled;
    private LocalDateTime updatedAt;

    public static PriceTrackingUpdateResponse from(PriceTracking pt) {
        return PriceTrackingUpdateResponse.builder()
                .trackingId(pt.getId())
                .targetPrice(pt.getTargetPrice())
                .alertEnabled(pt.isAlertEnabled())
                .updatedAt(pt.getUpdatedAt())
                .build();
    }
}
