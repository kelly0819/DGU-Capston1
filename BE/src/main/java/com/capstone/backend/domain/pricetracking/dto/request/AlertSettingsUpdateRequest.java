package com.capstone.backend.domain.pricetracking.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertSettingsUpdateRequest {
    private Boolean targetPriceAlert;
    private Boolean weeklyReport;
}
