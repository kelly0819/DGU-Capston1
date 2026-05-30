package com.capstone.backend.domain.pricetracking.repository;

import com.capstone.backend.domain.pricetracking.entity.PriceTrackingAlertSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface PriceTrackingAlertSettingsRepository extends JpaRepository<PriceTrackingAlertSettings, UUID> {

    Optional<PriceTrackingAlertSettings> findByUser_Id(UUID userId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE price_tracking_alert_settings
            SET target_price_alert = COALESCE(:targetPriceAlert, target_price_alert),
                weekly_report = COALESCE(:weeklyReport, weekly_report),
                updated_at = now()
            WHERE user_id = :userId
            """, nativeQuery = true)
    void updateAlertSettings(@Param("userId") UUID userId,
                             @Param("targetPriceAlert") Boolean targetPriceAlert,
                             @Param("weeklyReport") Boolean weeklyReport);
}
