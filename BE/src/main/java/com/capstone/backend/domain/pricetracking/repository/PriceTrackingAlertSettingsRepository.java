package com.capstone.backend.domain.pricetracking.repository;

import com.capstone.backend.domain.pricetracking.entity.PriceTrackingAlertSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PriceTrackingAlertSettingsRepository extends JpaRepository<PriceTrackingAlertSettings, UUID> {
    Optional<PriceTrackingAlertSettings> findByUser_Id(UUID userId);
}
