package com.capstone.backend.domain.pricetracking.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class PriceTrackingDetailResponse {

    private UUID trackingId;
    private ProductInfo product;
    private StatsInfo stats;
    private Integer targetPrice;
    private boolean alertEnabled;
    private String period;
    private List<PriceHistoryEntry> priceHistory;
    private List<StoreInfo> stores;

    @Getter
    @Builder
    public static class ProductInfo {
        private UUID id;
        private String name;
        private String brand;
        private String imageUrl;
    }

    @Getter
    @Builder
    public static class StatsInfo {
        private Integer originalPrice;
        private Integer currentLowestPrice;
        private Integer historicalLowest;
        private Double changePercent;
        private Integer changeAmount;
    }

    @Getter
    @Builder
    public static class PriceHistoryEntry {
        private String date;
        private Integer price;
    }

    @Getter
    @Builder
    public static class StoreInfo {
        private String storeName;
        private Integer price;

        @JsonProperty("isLowest")
        private boolean isLowest;

        private String purchaseUrl;
    }
}
