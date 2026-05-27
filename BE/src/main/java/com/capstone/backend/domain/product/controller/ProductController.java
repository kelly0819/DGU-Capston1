package com.capstone.backend.domain.product.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.domain.product.dto.ProductDto;
import com.capstone.backend.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "7. Product", description = "상품 관련 API")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto.Response>> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProduct(productId)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDto.Response>>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(productService.searchProducts(keyword)));
    }

    @GetMapping("/category")
    public ResponseEntity<ApiResponse<List<ProductDto.Response>>> getProductsByCategory(@RequestParam String category) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductsByCategory(category)));
    }
}
