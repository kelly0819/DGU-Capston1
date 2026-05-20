package com.capstone.backend.domain.pricetracking.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.domain.pricetracking.dto.PriceTrackingDto;
import com.capstone.backend.domain.pricetracking.service.PriceTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price-tracking")
@RequiredArgsConstructor
public class PriceTrackingController {

    private final PriceTrackingService priceTrackingService;

    @GetMapping("/{productId}/history")
    public ResponseEntity<ApiResponse<List<PriceTrackingDto.Response>>> getPriceHistory(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(priceTrackingService.getPriceHistory(productId)));
    }
}
