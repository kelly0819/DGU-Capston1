package com.capstone.backend.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
public class PreferencesRequest {

    private Integer priceTolerancePercent;

    private static final Set<Integer> VALID_VALUES = Set.of(0, 5, 10, 20);

    public boolean isValid() {
        return priceTolerancePercent == null || VALID_VALUES.contains(priceTolerancePercent);
    }
}
