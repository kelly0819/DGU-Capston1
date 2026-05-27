package com.capstone.backend.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
public class PreferenceUpdateRequest {

    private String searchPurpose;
    private Integer priceTolerancePercent;

    private static final Set<String> VALID_PURPOSES = Set.of("DAILY", "GIFT", "TRAVEL", "SPECIAL");
    private static final Set<Integer> VALID_TOLERANCE = Set.of(0, 5, 10, 20);

    public boolean isValid() {
        if (searchPurpose != null && !VALID_PURPOSES.contains(searchPurpose)) return false;
        return priceTolerancePercent == null || VALID_TOLERANCE.contains(priceTolerancePercent);
    }
}
