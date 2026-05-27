package com.capstone.backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OnboardingCompleteResponse {
    private boolean onboardingCompleted;
    private LocalDateTime updatedAt;
}
