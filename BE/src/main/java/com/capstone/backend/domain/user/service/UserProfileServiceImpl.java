package com.capstone.backend.domain.user.service;

import com.capstone.backend.common.exception.BusinessException;
import com.capstone.backend.common.exception.ErrorCode;
import com.capstone.backend.domain.user.dto.request.PreferencesRequest;
import com.capstone.backend.domain.user.dto.request.PreferenceUpdateRequest;
import com.capstone.backend.domain.user.dto.request.SkinProfileRequest;
import com.capstone.backend.domain.user.dto.request.SkinProfileUpdateRequest;
import com.capstone.backend.domain.user.dto.response.PreferenceResponse;
import com.capstone.backend.domain.user.dto.response.SkinProfileResponse;
import com.capstone.backend.domain.user.entity.UserPreference;
import com.capstone.backend.domain.user.entity.UserSkinProfile;
import com.capstone.backend.domain.user.repository.UserPreferenceRepository;
import com.capstone.backend.domain.user.repository.UserSkinProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserSkinProfileRepository skinProfileRepository;
    private final UserPreferenceRepository preferenceRepository;

    @Override
    @Transactional
    public SkinProfileResponse saveSkinProfile(UUID userId, SkinProfileRequest request) {
        if (!request.isValid()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }

        UserSkinProfile profile = skinProfileRepository.findByUserId(userId).orElse(null);
        String[] concerns = toArray(request.getSkinConcerns());
        String[] notes = toArray(request.getNotes());

        if (profile == null) {
            LocalDateTime now = LocalDateTime.now();
            profile = UserSkinProfile.builder()
                    .userId(userId)
                    .personalColor(request.getPersonalColor())
                    .skinType(request.getSkinType())
                    .skinConcerns(concerns != null ? concerns : new String[0])
                    .notes(notes)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
        } else {
            profile.update(request.getPersonalColor(), request.getSkinType(), concerns, notes);
        }

        return SkinProfileResponse.from(skinProfileRepository.save(profile));
    }

    @Override
    @Transactional
    public SkinProfileResponse updateSkinProfile(UUID userId, SkinProfileUpdateRequest request) {
        if (!request.isValid()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }

        UserSkinProfile profile = skinProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        profile.update(request.getPersonalColor(), request.getSkinType(),
                toArray(request.getSkinConcerns()), toArray(request.getNotes()));

        return SkinProfileResponse.from(skinProfileRepository.save(profile));
    }

    @Override
    @Transactional
    public PreferenceResponse savePreferences(UUID userId, PreferencesRequest request) {
        if (!request.isValid()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }

        UserPreference pref = preferenceRepository.findByUserId(userId).orElse(null);

        if (pref == null) {
            LocalDateTime now = LocalDateTime.now();
            pref = UserPreference.builder()
                    .userId(userId)
                    .priceTolerancePercent(request.getPriceTolerancePercent())
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
        } else {
            pref.update(null, request.getPriceTolerancePercent());
        }

        return PreferenceResponse.from(preferenceRepository.save(pref));
    }

    @Override
    @Transactional
    public PreferenceResponse updatePreferences(UUID userId, PreferenceUpdateRequest request) {
        if (!request.isValid()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }

        UserPreference pref = preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        pref.update(request.getSearchPurpose(), request.getPriceTolerancePercent());
        return PreferenceResponse.from(preferenceRepository.save(pref));
    }

    private String[] toArray(List<String> list) {
        return list == null ? null : list.toArray(new String[0]);
    }
}
