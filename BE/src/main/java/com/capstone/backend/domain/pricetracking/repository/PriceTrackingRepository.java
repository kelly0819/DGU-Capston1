package com.capstone.backend.domain.pricetracking.repository;

import com.capstone.backend.domain.pricetracking.entity.PriceTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PriceTrackingRepository extends JpaRepository<PriceTracking, UUID> {
    List<PriceTracking> findByUserId(UUID userId);
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);
}
