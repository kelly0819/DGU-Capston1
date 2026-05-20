package com.capstone.backend.domain.registered.service;

import com.capstone.backend.domain.registered.repository.RegisteredRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisteredServiceImpl implements RegisteredService {

    private final RegisteredRepository registeredRepository;

    @Override
    public void register(Long userId, Long productId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void unregister(Long userId, Long registeredId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
