package com.capstone.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_preferences")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "search_purpose", length = 20)
    private String searchPurpose;

    @Column(name = "price_tolerance_percent")
    private Integer priceTolerancePercent;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void update(String searchPurpose, Integer priceTolerancePercent) {
        if (searchPurpose != null) this.searchPurpose = searchPurpose;
        if (priceTolerancePercent != null) this.priceTolerancePercent = priceTolerancePercent;
        this.updatedAt = LocalDateTime.now();
    }
}
