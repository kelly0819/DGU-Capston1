package com.capstone.backend.domain.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductSearchResponse {
    private List<ProductItem> products;

    @Getter
    @Builder
    public static class ProductItem {
        private UUID id;
        private String name;
        private String brand;
        private String category;
        private String imageUrl;
        private Integer originalPrice;
        private Integer currentLowestPrice;
    }
}
