package com.capstone.backend.domain.registered.service;

import com.capstone.backend.domain.registered.repository.RegisteredRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisteredServiceImpl implements RegisteredService {

    private final RegisteredRepository registeredRepository;

    @Override
    public void register(UUID userId, UUID productId) {
    }

    @Override
    public void unregister(UUID userId, UUID registeredId) {
    }
}
