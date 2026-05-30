package com.capstone.backend.domain.recommendation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class RecommendationJobResponse {
    private UUID jobId;
    private int estimatedSeconds;
    private String statusUrl;

    public static RecommendationJobResponse of(UUID jobId) {
        return RecommendationJobResponse.builder()
                .jobId(jobId)
                .estimatedSeconds(10)
                .statusUrl("/recommendations/" + jobId + "/status")
                .build();
    }
}
