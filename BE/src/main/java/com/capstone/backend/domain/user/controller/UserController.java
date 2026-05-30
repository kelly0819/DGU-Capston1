package com.capstone.backend.domain.user.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.domain.user.dto.request.PreferencesRequest;
import com.capstone.backend.domain.user.dto.request.PreferenceUpdateRequest;
import com.capstone.backend.domain.user.dto.request.SkinProfileUpdateRequest;
import com.capstone.backend.domain.user.dto.response.PreferenceResponse;
import com.capstone.backend.domain.user.dto.response.ProfileUpdateResponse;
import com.capstone.backend.domain.user.dto.response.SkinProfileResponse;
import com.capstone.backend.domain.user.dto.response.UserMeResponse;
import com.capstone.backend.domain.user.service.UserProfileService;
import com.capstone.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "2. User", description = "사용자 API")
public class UserController {

    private final UserService userService;
    private final UserProfileService userProfileService;

    @Operation(summary = "내 정보 조회")
    @GetMapping("/users/profile")
    public ResponseEntity<ApiResponse<UserMeResponse>> getMyInfo(
            @AuthenticationPrincipal UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getMyInfo(userId)));
    }

    @Operation(summary = "프로필 설정/수정")
    @PatchMapping(value = "/users/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProfileUpdateResponse>> updateMyProfile(
            @AuthenticationPrincipal UUID userId,
            @RequestPart(value = "name", required = false) String name,
            @RequestPart(value = "gender", required = false) String gender,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateMyProfile(userId, name, gender, image)));
    }

    @Operation(summary = "피부 정보 수정")
    @PatchMapping("/users/me/skin-profile")
    public ResponseEntity<ApiResponse<SkinProfileResponse>> updateSkinProfile(
            @AuthenticationPrincipal UUID userId,
            @RequestBody SkinProfileUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userProfileService.updateSkinProfile(userId, request)));
    }

    @Operation(summary = "선호 정보 저장 (최초)")
    @PostMapping("/users/me/preferences")
    public ResponseEntity<ApiResponse<PreferenceResponse>> savePreferences(
            @AuthenticationPrincipal UUID userId,
            @RequestBody PreferencesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(userProfileService.savePreferences(userId, request)));
    }

    @Operation(summary = "선호 정보 수정")
    @PatchMapping("/users/me/preferences")
    public ResponseEntity<ApiResponse<PreferenceResponse>> updatePreferences(
            @AuthenticationPrincipal UUID userId,
            @RequestBody PreferenceUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userProfileService.updatePreferences(userId, request)));
    }

}
