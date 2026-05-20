package com.capstone.backend.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ProductDto {

    @Getter
    public static class Request {
        private String name;
        private Integer price;
        private String imageUrl;
        private String productUrl;
        private String category;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private Integer price;
        private String imageUrl;
        private String productUrl;
        private String category;
    }
}
