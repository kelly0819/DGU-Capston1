package com.capstone.backend.domain.user.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.common.util.SecurityUtil;
import com.capstone.backend.domain.user.dto.UserDto;
import com.capstone.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto.Response>> getMyInfo() {
        UUID userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(userService.getMyInfo(userId)));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserDto.Response>> updateMyInfo(@RequestBody UserDto.Request request) {
        UUID userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(userService.updateMyInfo(userId, request)));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteUser() {
        UUID userId = SecurityUtil.getCurrentUserId();
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
