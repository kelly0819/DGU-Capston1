package com.capstone.backend.domain.registered.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.common.util.SecurityUtil;
import com.capstone.backend.domain.registered.service.RegisteredService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registered")
@RequiredArgsConstructor
public class RegisteredController {

    private final RegisteredService registeredService;

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> register(@PathVariable Long productId) {
        Long userId = SecurityUtil.getCurrentUserId();
        registeredService.register(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{registeredId}")
    public ResponseEntity<ApiResponse<Void>> unregister(@PathVariable Long registeredId) {
        Long userId = SecurityUtil.getCurrentUserId();
        registeredService.unregister(userId, registeredId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
