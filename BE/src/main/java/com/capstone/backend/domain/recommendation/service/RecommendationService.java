package com.capstone.backend.domain.recommendation.service;

import com.capstone.backend.domain.recommendation.dto.RecommendationDto;

import java.util.List;

public interface RecommendationService {
    List<RecommendationDto.Response> getRecommendations(Long userId);
}
