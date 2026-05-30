package com.capstone.backend.domain.product.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.domain.product.dto.request.RecognizeRequest;
import com.capstone.backend.domain.product.dto.response.ProductDetailResponse;
import com.capstone.backend.domain.product.dto.response.ProductRecognizeResponse;
import com.capstone.backend.domain.product.service.ProductRecognizeService;
import com.capstone.backend.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "7. Product", description = "상품 관련 API")
public class ProductController {

    private final ProductService productService;
    private final ProductRecognizeService productRecognizeService;

    @Operation(
            summary = "제품 인식 (IMAGE / NFC / TEXT)",
            description = "FastAPI input_agent → product_agent 순차 호출 (동기). " +
                    "type: IMAGE(base64), NFC(URL), TEXT(평문). 결과로 productId 포함 상품 정보 반환."
    )
    @PostMapping("/products/recognize")
    public ResponseEntity<ApiResponse<ProductRecognizeResponse>> recognize(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody RecognizeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productRecognizeService.recognize(userId, request)));
    }

    @Operation(
            summary = "제품 상세 조회",
            description = "product_features(feature_json) + product_insights 를 합산해 반환. " +
                    "recognize 이후 productId로 호출하거나, 추천 결과 상품 확인 시 사용."
    )
    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductDetail(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID productId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductDetail(productId)));
    }

    // ── 추후 확장 예정 ──────────────────────────────────────────────────────────
    // @GetMapping("/products/text-search")        제품 텍스트 검색 (Naver 쇼핑)
    // @GetMapping("/api/products/{productId}")    단건 조회
    // @GetMapping("/api/products/search")         키워드 검색
    // @GetMapping("/api/products/category")       카테고리 필터
}
