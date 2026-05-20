package com.capstone.backend.domain.user.service;

import com.capstone.backend.domain.user.dto.UserDto;
import com.capstone.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto.Response getMyInfo(UUID userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public UserDto.Response updateMyInfo(UUID userId, UserDto.Request request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deleteUser(UUID userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
