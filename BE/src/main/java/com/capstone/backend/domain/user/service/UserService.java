package com.capstone.backend.domain.user.service;

import com.capstone.backend.domain.user.dto.UserDto;

import java.util.UUID;

public interface UserService {
    UserDto.Response getMyInfo(UUID userId);
    UserDto.Response updateMyInfo(UUID userId, UserDto.Request request);
    void deleteUser(UUID userId);
}
