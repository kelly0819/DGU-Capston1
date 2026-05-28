package com.capstone.backend.domain.onboarding.service;

import com.capstone.backend.domain.onboarding.dto.RegisteredProductAddRequest;
import com.capstone.backend.domain.onboarding.dto.RegisteredProductAddResponse;

import java.util.UUID;

public interface RegisteredProductService {
    RegisteredProductAddResponse addFavoriteItem(UUID userId, RegisteredProductAddRequest request);
    void removeFavoriteItem(UUID userId, UUID registeredId);
}
