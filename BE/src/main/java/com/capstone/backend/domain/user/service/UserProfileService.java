package com.capstone.backend.domain.user.service;

import com.capstone.backend.domain.user.dto.request.PreferencesRequest;
import com.capstone.backend.domain.user.dto.request.PreferenceUpdateRequest;
import com.capstone.backend.domain.user.dto.request.SkinProfileRequest;
import com.capstone.backend.domain.user.dto.request.SkinProfileUpdateRequest;
import com.capstone.backend.domain.user.dto.response.PreferenceResponse;
import com.capstone.backend.domain.user.dto.response.SkinProfileResponse;

import java.util.UUID;

public interface UserProfileService {
    SkinProfileResponse saveSkinProfile(UUID userId, SkinProfileRequest request);
    SkinProfileResponse updateSkinProfile(UUID userId, SkinProfileUpdateRequest request);
    PreferenceResponse savePreferences(UUID userId, PreferencesRequest request);
    PreferenceResponse updatePreferences(UUID userId, PreferenceUpdateRequest request);
}
