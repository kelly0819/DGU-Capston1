package com.capstone.backend.domain.registered.service;

public interface RegisteredService {
    void register(Long userId, Long productId);
    void unregister(Long userId, Long registeredId);
}
