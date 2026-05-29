package com.capstone.backend.domain.product.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.domain.product.dto.ProductDto;
import com.capstone.backend.domain.product.dto.request.RecognizeRequest;
import com.capstone.backend.domain.product.dto.response.ProductSearchResponse;
import com.capstone.backend.domain.product.dto.response.RecognizeResponse;
import com.capstone.backend.domain.product.service.ProductRecognizeService;
import com.capstone.backend.domain.product.service.ProductService;
import com.capstone.backend.domain.product.service.ProductTextSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "7. Product", description = "상품 관련 API")
public class ProductController {

    private final ProductService productService;
    private final ProductTextSearchService productTextSearchService;
    private final ProductRecognizeService productRecognizeService;

    @Operation(summary = "상품 조회")
    @GetMapping("/api/products/{productId}")
    public ResponseEntity<ApiResponse<ProductDto.Response>> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProduct(productId)));
    }

    @Operation(summary = "상품 검색 (키워드)")
    @GetMapping("/api/products/search")
    public ResponseEntity<ApiResponse<List<ProductDto.Response>>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(productService.searchProducts(keyword)));
    }

    @Operation(summary = "카테고리별 상품 조회")
    @GetMapping("/api/products/category")
    public ResponseEntity<ApiResponse<List<ProductDto.Response>>> getProductsByCategory(@RequestParam String category) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductsByCategory(category)));
    }

    @Operation(summary = "제품 인식 (TEXT / IMAGE / NFC)", description = "현재 TEXT 타입만 지원. IMAGE/NFC는 추후 구현.")
    @PostMapping("/products/recognize")
    public ResponseEntity<ApiResponse<RecognizeResponse>> recognize(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody RecognizeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productRecognizeService.recognize(request)));
    }

    @Operation(summary = "제품 텍스트 검색 (Naver 쇼핑 연동)")
    @GetMapping("/products/text-search")
    public ResponseEntity<ApiResponse<ProductSearchResponse>> textSearch(
            @AuthenticationPrincipal UUID userId,
            @RequestParam String q,
            @RequestParam(defaultValue = "20") int size) {
        ProductSearchResponse result = productTextSearchService.search(q, size);
        ApiResponse.Meta meta = new ApiResponse.Meta(1, size, result.getProducts().size());
        return ResponseEntity.ok(ApiResponse.success(result, meta));
    }
}
