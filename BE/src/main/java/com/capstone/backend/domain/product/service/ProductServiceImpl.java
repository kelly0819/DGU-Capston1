package com.capstone.backend.domain.product.service;

import com.capstone.backend.domain.product.dto.ProductDto;
import com.capstone.backend.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductDto.Response getProduct(Long productId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<ProductDto.Response> searchProducts(String keyword) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<ProductDto.Response> getProductsByCategory(String category) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
