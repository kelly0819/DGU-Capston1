package com.capstone.backend.domain.pricetracking.service;

import com.capstone.backend.domain.pricetracking.dto.PriceTrackingDto;
import com.capstone.backend.domain.pricetracking.repository.PriceTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceTrackingServiceImpl implements PriceTrackingService {

    private final PriceTrackingRepository priceTrackingRepository;

    @Override
    public List<PriceTrackingDto.Response> getPriceHistory(Long productId) {
        return Collections.emptyList();
    }
}
