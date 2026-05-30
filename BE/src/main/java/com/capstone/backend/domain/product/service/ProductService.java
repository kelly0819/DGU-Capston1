package com.capstone.backend.domain.product.service;

import com.capstone.backend.domain.product.dto.ProductDto;
import com.capstone.backend.domain.product.dto.response.ProductDetailResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDto.Response getProduct(Long productId);
    List<ProductDto.Response> searchProducts(String keyword);
    List<ProductDto.Response> getProductsByCategory(String category);
    ProductDetailResponse getProductDetail(UUID productId);
}
