package com.capstone.backend.domain.pricetracking.service;

import com.capstone.backend.domain.pricetracking.dto.PriceTrackingDto;

import java.util.List;

public interface PriceTrackingService {
    List<PriceTrackingDto.Response> getPriceHistory(Long productId);
}
