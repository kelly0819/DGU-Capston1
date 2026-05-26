package com.capstone.backend.domain.registered.service;

import java.util.UUID;

public interface RegisteredService {
    void register(UUID userId, UUID productId);
    void unregister(UUID userId, UUID registeredId);
}
