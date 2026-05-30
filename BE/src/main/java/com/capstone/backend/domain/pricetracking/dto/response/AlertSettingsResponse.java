package com.capstone.backend.domain.pricetracking.dto.response;

import com.capstone.backend.domain.pricetracking.entity.PriceTrackingAlertSettings;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AlertSettingsResponse {
    private boolean targetPriceAlert;
    private boolean weeklyReport;
    private LocalDateTime updatedAt;

    public static AlertSettingsResponse from(PriceTrackingAlertSettings s) {
        return AlertSettingsResponse.builder()
                .targetPriceAlert(s.isTargetPriceAlert())
                .weeklyReport(s.isWeeklyReport())
                .updatedAt(s.getUpdatedAt())
                .build();
    }

    public static AlertSettingsResponse defaultSettings() {
        return AlertSettingsResponse.builder()
                .targetPriceAlert(true)
                .weeklyReport(false)
                .updatedAt(null)
                .build();
    }
}
