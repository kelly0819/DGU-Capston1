package com.capstone.backend.domain.pricetracking.service;

import com.capstone.backend.common.exception.BusinessException;
import com.capstone.backend.common.exception.ErrorCode;
import com.capstone.backend.domain.pricetracking.dto.PricePeriod;
import com.capstone.backend.domain.pricetracking.dto.request.AlertSettingsUpdateRequest;
import com.capstone.backend.domain.pricetracking.dto.request.PriceTrackingCreateRequest;
import com.capstone.backend.domain.pricetracking.dto.request.PriceTrackingUpdateRequest;
import com.capstone.backend.domain.pricetracking.dto.response.AlertSettingsResponse;
import com.capstone.backend.domain.pricetracking.dto.response.PriceTrackingCreateResponse;
import com.capstone.backend.domain.pricetracking.dto.response.PriceTrackingDetailResponse;
import com.capstone.backend.domain.pricetracking.dto.response.PriceTrackingListResponse;
import com.capstone.backend.domain.pricetracking.dto.response.PriceTrackingUpdateResponse;
import com.capstone.backend.domain.pricetracking.entity.PriceTracking;
import com.capstone.backend.domain.pricetracking.entity.PriceTrackingAlertSettings;
import com.capstone.backend.domain.pricetracking.repository.PriceTrackingAlertSettingsRepository;
import com.capstone.backend.domain.pricetracking.repository.PriceTrackingRepository;
import com.capstone.backend.domain.product.entity.Product;
import com.capstone.backend.domain.product.entity.ProductInsight;
import com.capstone.backend.domain.product.repository.PriceHistoryRepository;
import com.capstone.backend.domain.product.repository.ProductInsightRepository;
import com.capstone.backend.domain.product.repository.ProductRepository;
import com.capstone.backend.domain.user.entity.User;
import com.capstone.backend.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceTrackingServiceImpl implements PriceTrackingService {

    private final PriceTrackingRepository trackingRepository;
    private final PriceTrackingAlertSettingsRepository alertSettingsRepository;
    private final ProductRepository productRepository;
    private final ProductInsightRepository productInsightRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public PriceTrackingListResponse getPriceTrackings(UUID userId) {
        List<PriceTracking> allTrackings = trackingRepository.findByUserIdOrderByCreatedAtDesc(userId);

        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        Long monthlySavings = trackingRepository.calculateMonthlySavings(userId, monthStart);

        List<PriceTrackingListResponse.AchievedItemDto> achieved = new ArrayList<>();
        List<PriceTrackingListResponse.TrackingItemDto> tracking = new ArrayList<>();

        for (PriceTracking pt : allTrackings) {
            Product p = pt.getProduct();
            ProductInsight insight = productInsightRepository.findByProduct_Id(p.getId()).orElse(null);
            Integer lowestPrice = insight != null ? insight.getLowestPrice() : null;

            PriceTrackingListResponse.ProductInfo productInfo = PriceTrackingListResponse.ProductInfo.builder()
                    .id(p.getId()).name(p.getName()).brand(p.getBrand()).imageUrl(p.getImageUrl())
                    .build();

            if (pt.isAchieved()) {
                String lowestStoreUrl = extractLowestStoreUrl(insight);
                achieved.add(PriceTrackingListResponse.AchievedItemDto.builder()
                        .trackingId(pt.getId())
                        .product(productInfo)
                        .targetPrice(pt.getTargetPrice())
                        .currentLowestPrice(lowestPrice)
                        .lowestStoreUrl(lowestStoreUrl)
                        .build());
            } else {
                Integer historicalLowest = priceHistoryRepository.findHistoricalLowest(p.getId());
                tracking.add(PriceTrackingListResponse.TrackingItemDto.builder()
                        .trackingId(pt.getId())
                        .product(productInfo)
                        .targetPrice(pt.getTargetPrice())
                        .currentLowestPrice(lowestPrice)
                        .historicalLowest(historicalLowest)
                        .build());
            }
        }

        PriceTrackingAlertSettings alertSettings = alertSettingsRepository.findByUser_Id(userId).orElse(null);
        AlertSettingsResponse alertSettingsDto = alertSettings != null
                ? AlertSettingsResponse.from(alertSettings)
                : AlertSettingsResponse.defaultSettings();

        return PriceTrackingListResponse.builder()
                .summary(PriceTrackingListResponse.SummaryDto.builder()
                        .totalTracking(allTrackings.size())
                        .monthlySavings(monthlySavings != null ? monthlySavings : 0L)
                        .build())
                .achieved(achieved)
                .tracking(tracking)
                .alertSettings(alertSettingsDto)
                .build();
    }

    @Override
    @Transactional
    public PriceTrackingCreateResponse startTracking(UUID userId, PriceTrackingCreateRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        if (trackingRepository.existsByUserIdAndProductId(userId, request.getProductId())) {
            throw new BusinessException(ErrorCode.ALREADY_TRACKING);
        }

        ProductInsight insight = productInsightRepository.findByProduct_Id(request.getProductId()).orElse(null);
        if (insight != null && insight.getLowestPrice() != null
                && request.getTargetPrice() >= insight.getLowestPrice()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        PriceTracking priceTracking = PriceTracking.builder()
                .user(user)
                .product(product)
                .targetPrice(request.getTargetPrice())
                .alertEnabled(request.getAlertEnabled())
                .isAchieved(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        PriceTracking saved = trackingRepository.save(priceTracking);

        alertSettingsRepository.findByUser_Id(userId).orElseGet(() -> {
            PriceTrackingAlertSettings settings = PriceTrackingAlertSettings.defaultSettings(user);
            return alertSettingsRepository.save(settings);
        });

        return PriceTrackingCreateResponse.from(saved);
    }

    @Override
    @Transactional
    public void deleteTracking(UUID userId, UUID trackingId) {
        int deleted = trackingRepository.deleteByIdAndUserId(trackingId, userId);
        if (deleted == 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public AlertSettingsResponse updateAlertSettings(UUID userId, AlertSettingsUpdateRequest request) {
        PriceTrackingAlertSettings settings = alertSettingsRepository.findByUser_Id(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        alertSettingsRepository.updateAlertSettings(userId, request.getTargetPriceAlert(), request.getWeeklyReport());

        PriceTrackingAlertSettings updated = alertSettingsRepository.findByUser_Id(userId).orElseThrow();
        return AlertSettingsResponse.from(updated);
    }

    @Override
    @Transactional
    public PriceTrackingUpdateResponse updateTracking(UUID userId, UUID trackingId, PriceTrackingUpdateRequest request) {
        PriceTracking pt = trackingRepository.findByIdAndUserId(trackingId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        if (request.getTargetPrice() != null) {
            ProductInsight insight = productInsightRepository.findByProduct_Id(pt.getProduct().getId()).orElse(null);
            if (insight != null && insight.getLowestPrice() != null
                    && request.getTargetPrice() >= insight.getLowestPrice()) {
                throw new BusinessException(ErrorCode.VALIDATION_ERROR);
            }
        }

        int updated = trackingRepository.updateTracking(
                trackingId, userId, request.getTargetPrice(), request.getAlertEnabled());
        if (updated == 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        PriceTracking refreshed = trackingRepository.findByIdAndUserId(trackingId, userId).orElseThrow();
        return PriceTrackingUpdateResponse.from(refreshed);
    }

    @Override
    @Transactional(readOnly = true)
    public PriceTrackingDetailResponse getTrackingDetail(UUID userId, UUID trackingId, String period) {
        PricePeriod pricePeriod;
        try {
            pricePeriod = PricePeriod.valueOf(period.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }

        PriceTracking pt = trackingRepository.findByIdAndUserId(trackingId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        Product product = pt.getProduct();
        ProductInsight insight = productInsightRepository.findByProduct_Id(product.getId()).orElse(null);

        Integer currentLowestPrice = insight != null ? insight.getLowestPrice() : null;
        Integer originalPrice = insight != null ? insight.getOriginalPrice() : product.getOriginalPrice();
        Integer historicalLowest = priceHistoryRepository.findHistoricalLowest(product.getId());

        double changePercent = 0.0;
        int changeAmount = 0;
        if (originalPrice != null && currentLowestPrice != null && originalPrice > 0) {
            changeAmount = currentLowestPrice - originalPrice;
            changePercent = Math.round((changeAmount / (double) originalPrice) * 1000.0) / 10.0;
        }

        LocalDateTime startDate = calculateStartDate(pricePeriod);
        List<Object[]> rawHistory = fetchHistory(pricePeriod, product.getId(), startDate);

        List<PriceTrackingDetailResponse.PriceHistoryEntry> priceHistory = rawHistory.stream()
                .map(row -> PriceTrackingDetailResponse.PriceHistoryEntry.builder()
                        .date(row[0].toString().substring(0, 10))
                        .price(((Number) row[1]).intValue())
                        .build())
                .collect(Collectors.toList());

        List<PriceTrackingDetailResponse.StoreInfo> stores = parseStores(insight);

        return PriceTrackingDetailResponse.builder()
                .trackingId(pt.getId())
                .product(PriceTrackingDetailResponse.ProductInfo.builder()
                        .id(product.getId()).name(product.getName())
                        .brand(product.getBrand()).imageUrl(product.getImageUrl())
                        .build())
                .stats(PriceTrackingDetailResponse.StatsInfo.builder()
                        .originalPrice(originalPrice)
                        .currentLowestPrice(currentLowestPrice)
                        .historicalLowest(historicalLowest)
                        .changePercent(changePercent)
                        .changeAmount(changeAmount)
                        .build())
                .targetPrice(pt.getTargetPrice())
                .alertEnabled(pt.isAlertEnabled())
                .period(period.toUpperCase())
                .priceHistory(priceHistory)
                .stores(stores)
                .build();
    }

    private LocalDateTime calculateStartDate(PricePeriod period) {
        return switch (period) {
            case DAILY -> LocalDateTime.now().minusMonths(1);
            case WEEKLY -> LocalDateTime.now().minusMonths(3);
            case MONTHLY -> LocalDateTime.now().minusMonths(6);
        };
    }

    private List<Object[]> fetchHistory(PricePeriod period, UUID productId, LocalDateTime startDate) {
        return switch (period) {
            case DAILY -> priceHistoryRepository.findDailyHistory(productId, startDate);
            case WEEKLY -> priceHistoryRepository.findWeeklyHistory(productId, startDate);
            case MONTHLY -> priceHistoryRepository.findMonthlyHistory(productId, startDate);
        };
    }

    private String extractLowestStoreUrl(ProductInsight insight) {
        if (insight == null || insight.getStores() == null) return null;
        try {
            List<Map<String, Object>> storeList = objectMapper.readValue(
                    insight.getStores(), new TypeReference<>() {});
            return storeList.stream()
                    .filter(s -> Boolean.TRUE.equals(s.get("isLowest")))
                    .map(s -> (String) s.get("purchaseUrl"))
                    .findFirst().orElse(null);
        } catch (Exception e) {
            log.warn("Failed to parse stores JSON: {}", e.getMessage());
            return null;
        }
    }

    private List<PriceTrackingDetailResponse.StoreInfo> parseStores(ProductInsight insight) {
        if (insight == null || insight.getStores() == null) return List.of();
        try {
            List<Map<String, Object>> storeList = objectMapper.readValue(
                    insight.getStores(), new TypeReference<>() {});
            return storeList.stream().map(s -> PriceTrackingDetailResponse.StoreInfo.builder()
                    .storeName((String) s.get("storeName"))
                    .price(((Number) s.get("price")).intValue())
                    .isLowest(Boolean.TRUE.equals(s.get("isLowest")))
                    .purchaseUrl((String) s.get("purchaseUrl"))
                    .build()).collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Failed to parse stores JSON: {}", e.getMessage());
            return List.of();
        }
    }
}
