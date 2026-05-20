package com.capstone.backend.domain.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class WishlistDto {

    @Getter
    public static class Request {
        private Long productId;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long wishlistId;
        private Long productId;
        private String productName;
        private Integer productPrice;
        private String productImageUrl;
    }
}
