package com.capstone.backend.domain.user.dto.response;

import com.capstone.backend.domain.user.entity.UserPreference;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class PreferenceResponse {

    private UUID preferenceId;
    private String searchPurpose;
    private Integer priceTolerancePercent;
    private LocalDateTime updatedAt;

    public static PreferenceResponse from(UserPreference p) {
        return PreferenceResponse.builder()
                .preferenceId(p.getId())
                .searchPurpose(p.getSearchPurpose())
                .priceTolerancePercent(p.getPriceTolerancePercent())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
