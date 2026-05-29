package com.capstone.backend.domain.product.service;

import com.capstone.backend.common.exception.BusinessException;
import com.capstone.backend.common.exception.ErrorCode;
import com.capstone.backend.domain.product.dto.request.RecognizeRequest;
import com.capstone.backend.domain.product.dto.response.ProductSearchResponse;
import com.capstone.backend.domain.product.dto.response.RecognizeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductRecognizeService {

    private final ProductTextSearchService productTextSearchService;

    @Transactional
    public RecognizeResponse recognize(RecognizeRequest request) {
        String type = request.getType().toUpperCase();

        if (!"TEXT".equals(type)) {
            return RecognizeResponse.notSupported(type);
        }

        if (request.getKeyword() == null || request.getKeyword().isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }

        ProductSearchResponse searchResult = productTextSearchService.search(request.getKeyword(), 5);

        if (searchResult.getProducts().isEmpty()) {
            return RecognizeResponse.notFound(type);
        }

        List<RecognizeResponse.ProductInfo> products = searchResult.getProducts().stream()
                .map(item -> RecognizeResponse.ProductInfo.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .brand(item.getBrand())
                        .category(item.getCategory())
                        .imageUrl(item.getImageUrl())
                        .lowestPrice(item.getCurrentLowestPrice())
                        .originalPrice(item.getOriginalPrice())
                        .build())
                .collect(Collectors.toList());

        return RecognizeResponse.found(type, products);
    }
}
