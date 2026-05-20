package com.capstone.backend.domain.product.service;

import com.capstone.backend.domain.product.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto.Response getProduct(Long productId);
    List<ProductDto.Response> searchProducts(String keyword);
    List<ProductDto.Response> getProductsByCategory(String category);
}
