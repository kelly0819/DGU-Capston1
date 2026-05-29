package com.capstone.backend.domain.pricetracking.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.domain.pricetracking.dto.request.AlertSettingsUpdateRequest;
import com.capstone.backend.domain.pricetracking.dto.request.PriceTrackingCreateRequest;
import com.capstone.backend.domain.pricetracking.dto.request.PriceTrackingUpdateRequest;
import com.capstone.backend.domain.pricetracking.dto.response.AlertSettingsResponse;
import com.capstone.backend.domain.pricetracking.dto.response.PriceTrackingCreateResponse;
import com.capstone.backend.domain.pricetracking.dto.response.PriceTrackingDetailResponse;
import com.capstone.backend.domain.pricetracking.dto.response.PriceTrackingListResponse;
import com.capstone.backend.domain.pricetracking.dto.response.PriceTrackingUpdateResponse;
import com.capstone.backend.domain.pricetracking.service.PriceTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "5. PriceTracking", description = "가격 추적 API")
public class PriceTrackingController {

    private final PriceTrackingService priceTrackingService;

    // ⚠️ alert-settings가 {trackingId}보다 먼저 배치되어야 라우팅 충돌 방지

    @Operation(summary = "추적 알림 설정 변경 (전역)")
    @PatchMapping("/price-trackings/alert-settings")
    public ResponseEntity<ApiResponse<AlertSettingsResponse>> updateAlertSettings(
            @AuthenticationPrincipal UUID userId,
            @RequestBody AlertSettingsUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(priceTrackingService.updateAlertSettings(userId, request)));
    }

    @Operation(summary = "목표가 수정 / 알림 토글 (개별)", description = "alertEnabled만 보내면 알림 토글, targetPrice만 보내면 목표가 수정")
    @PatchMapping("/price-trackings/{trackingId}")
    public ResponseEntity<ApiResponse<PriceTrackingUpdateResponse>> updateTracking(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID trackingId,
            @RequestBody PriceTrackingUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                priceTrackingService.updateTracking(userId, trackingId, request)));
    }

    @Operation(summary = "가격 추적 목록 조회")
    @GetMapping("/price-trackings")
    public ResponseEntity<ApiResponse<PriceTrackingListResponse>> getPriceTrackings(
            @AuthenticationPrincipal UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(priceTrackingService.getPriceTrackings(userId)));
    }

    @Operation(summary = "가격 추적 상세 조회")
    @GetMapping("/price-trackings/{trackingId}")
    public ResponseEntity<ApiResponse<PriceTrackingDetailResponse>> getTrackingDetail(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID trackingId,
            @RequestParam String period) {
        return ResponseEntity.ok(ApiResponse.success(
                priceTrackingService.getTrackingDetail(userId, trackingId, period)));
    }

    @Operation(summary = "가격 추적 시작")
    @PostMapping("/price-trackings")
    public ResponseEntity<ApiResponse<PriceTrackingCreateResponse>> startTracking(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody PriceTrackingCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(priceTrackingService.startTracking(userId, request)));
    }

    @Operation(summary = "가격 추적 삭제")
    @DeleteMapping("/price-trackings/{trackingId}")
    public ResponseEntity<ApiResponse<Void>> deleteTracking(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID trackingId) {
        priceTrackingService.deleteTracking(userId, trackingId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
