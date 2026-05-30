package com.capstone.backend.domain.pricetracking.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PriceTrackingUpdateRequest {
    private Integer targetPrice;
    private Boolean alertEnabled;
}
