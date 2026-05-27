package com.capstone.backend.domain.onboarding.service;

import java.util.UUID;

public interface RegisteredService {
    void register(UUID userId, UUID productId);
    void unregister(UUID userId, UUID registeredId);
}
