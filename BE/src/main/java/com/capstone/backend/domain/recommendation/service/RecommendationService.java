package com.capstone.backend.domain.recommendation.service;

import com.capstone.backend.domain.recommendation.dto.RecommendationDto;

import java.util.List;
import java.util.UUID;

public interface RecommendationService {
    List<RecommendationDto.Response> getRecommendations(UUID userId);
}
