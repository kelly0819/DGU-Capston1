package com.capstone.backend.domain.pricetracking.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class PriceTrackingCreateRequest {

    @NotNull
    private UUID productId;

    @NotNull
    @Positive
    private Integer targetPrice;

    @NotNull
    private Boolean alertEnabled;
}
