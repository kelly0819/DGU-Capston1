package com.capstone.backend.domain.pricetracking.entity;

import com.capstone.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "price_tracking_alert_settings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PriceTrackingAlertSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "target_price_alert", nullable = false)
    private boolean targetPriceAlert;

    @Column(name = "weekly_report", nullable = false)
    private boolean weeklyReport;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void update(Boolean targetPriceAlert, Boolean weeklyReport) {
        if (targetPriceAlert != null) this.targetPriceAlert = targetPriceAlert;
        if (weeklyReport != null) this.weeklyReport = weeklyReport;
        this.updatedAt = LocalDateTime.now();
    }

    public static PriceTrackingAlertSettings defaultSettings(User user) {
        return PriceTrackingAlertSettings.builder()
                .user(user)
                .targetPriceAlert(true)
                .weeklyReport(false)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
