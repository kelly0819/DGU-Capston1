package com.capstone.backend.domain.pricetracking.service;

import com.capstone.backend.domain.pricetracking.dto.request.AlertSettingsUpdateRequest;
import com.capstone.backend.domain.pricetracking.dto.request.PriceTrackingCreateRequest;
import com.capstone.backend.domain.pricetracking.dto.request.PriceTrackingUpdateRequest;
import com.capstone.backend.domain.pricetracking.dto.response.AlertSettingsResponse;
import com.capstone.backend.domain.pricetracking.dto.response.PriceTrackingCreateResponse;
import com.capstone.backend.domain.pricetracking.dto.response.PriceTrackingDetailResponse;
import com.capstone.backend.domain.pricetracking.dto.response.PriceTrackingListResponse;
import com.capstone.backend.domain.pricetracking.dto.response.PriceTrackingUpdateResponse;

import java.util.UUID;

public interface PriceTrackingService {
    PriceTrackingListResponse getPriceTrackings(UUID userId);
    PriceTrackingCreateResponse startTracking(UUID userId, PriceTrackingCreateRequest request);
    void deleteTracking(UUID userId, UUID trackingId);
    AlertSettingsResponse updateAlertSettings(UUID userId, AlertSettingsUpdateRequest request);
    PriceTrackingUpdateResponse updateTracking(UUID userId, UUID trackingId, PriceTrackingUpdateRequest request);
    PriceTrackingDetailResponse getTrackingDetail(UUID userId, UUID trackingId, String period);
}
