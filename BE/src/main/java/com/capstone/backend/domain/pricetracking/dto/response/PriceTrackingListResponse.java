package com.capstone.backend.domain.pricetracking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class PriceTrackingListResponse {

    private SummaryDto summary;
    private List<AchievedItemDto> achieved;
    private List<TrackingItemDto> tracking;
    private AlertSettingsResponse alertSettings;

    @Getter
    @Builder
    public static class SummaryDto {
        private int totalTracking;
        private long monthlySavings;
    }

    @Getter
    @Builder
    public static class AchievedItemDto {
        private UUID trackingId;
        private ProductInfo product;
        private Integer targetPrice;
        private Integer currentLowestPrice;
        private String lowestStoreUrl;
    }

    @Getter
    @Builder
    public static class TrackingItemDto {
        private UUID trackingId;
        private ProductInfo product;
        private Integer targetPrice;
        private Integer currentLowestPrice;
        private Integer historicalLowest;
    }

    @Getter
    @Builder
    public static class ProductInfo {
        private UUID id;
        private String name;
        private String brand;
        private String imageUrl;
    }
}
