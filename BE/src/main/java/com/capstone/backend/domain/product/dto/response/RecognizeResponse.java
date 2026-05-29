package com.capstone.backend.domain.product.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecognizeResponse {

    private boolean recognized;
    private String inputType;
    private List<ProductInfo> products;
    private String message;

    @Getter
    @Builder
    public static class ProductInfo {
        private UUID id;
        private String name;
        private String brand;
        private String category;
        private String imageUrl;
        private Integer lowestPrice;
        private Integer originalPrice;
    }

    public static RecognizeResponse notSupported(String inputType) {
        return RecognizeResponse.builder()
                .recognized(false)
                .inputType(inputType)
                .products(List.of())
                .message("이미지/NFC 인식은 준비 중이에요. 텍스트로 검색해주세요.")
                .build();
    }

    public static RecognizeResponse notFound(String inputType) {
        return RecognizeResponse.builder()
                .recognized(false)
                .inputType(inputType)
                .products(List.of())
                .message("검색 결과가 없어요.")
                .build();
    }

    public static RecognizeResponse found(String inputType, List<ProductInfo> products) {
        return RecognizeResponse.builder()
                .recognized(true)
                .inputType(inputType)
                .products(products)
                .build();
    }
}
