package com.capstone.backend.domain.user.service;

import com.capstone.backend.common.exception.BusinessException;
import com.capstone.backend.common.exception.ErrorCode;
import com.capstone.backend.domain.pricetracking.repository.PriceTrackingRepository;
import com.capstone.backend.domain.onboarding.repository.RegisteredRepository;
import com.capstone.backend.domain.user.dto.response.OnboardingCompleteResponse;
import com.capstone.backend.domain.user.dto.response.ProfileUpdateResponse;
import com.capstone.backend.domain.user.dto.response.UserMeResponse;
import com.capstone.backend.domain.user.entity.User;
import com.capstone.backend.domain.user.entity.UserPreference;
import com.capstone.backend.domain.user.entity.UserSkinProfile;
import com.capstone.backend.domain.user.repository.UserPreferenceRepository;
import com.capstone.backend.domain.user.repository.UserRepository;
import com.capstone.backend.domain.user.repository.UserSkinProfileRepository;
import com.capstone.backend.domain.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserSkinProfileRepository skinProfileRepository;
    private final UserPreferenceRepository preferenceRepository;
    private final WishlistRepository wishlistRepository;
    private final PriceTrackingRepository priceTrackingRepository;
    private final RegisteredRepository registeredRepository;
    private final WebClient webClient;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-role-key}")
    private String supabaseServiceRoleKey;

    @Value("${supabase.storage-url}")
    private String supabaseStorageUrl;

    @Override
    @Transactional(readOnly = true)
    public UserMeResponse getMyInfo(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        UserSkinProfile skinProfile = skinProfileRepository.findByUserId(userId).orElse(null);
        UserPreference preference = preferenceRepository.findByUserId(userId).orElse(null);

        long wishlistCount = wishlistRepository.countByUserId(userId);
        long trackingCount = priceTrackingRepository.countByUserId(userId);
        long registeredCount = registeredRepository.countByUserId(userId);

        return UserMeResponse.of(user, skinProfile, preference, wishlistCount, trackingCount, registeredCount);
    }

    @Override
    @Transactional
    public ProfileUpdateResponse updateMyProfile(UUID userId, String name, String gender, MultipartFile image) {
        if (name != null && (name.trim().length() < 2 || name.trim().length() > 20)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }
        if (image != null && !image.isEmpty() && image.getSize() > 5L * 1024 * 1024) {
            throw new BusinessException(ErrorCode.FILE_TOO_LARGE);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        String profileImageUrl = null;
        if (image != null && !image.isEmpty()) {
            profileImageUrl = uploadToSupabase(userId, image);
        }

        user.updateProfile(name != null ? name.trim() : null, gender, profileImageUrl);
        return ProfileUpdateResponse.from(userRepository.save(user));
    }

    @Override
    @Transactional
    public OnboardingCompleteResponse completeOnboarding(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        user.completeOnboarding();
        User saved = userRepository.save(user);
        return new OnboardingCompleteResponse(true, saved.getUpdatedAt());
    }

    private String uploadToSupabase(UUID userId, MultipartFile image) {
        try {
            byte[] bytes = image.getBytes();
            webClient.put()
                    .uri(supabaseStorageUrl + "/profiles/" + userId + ".jpg")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseServiceRoleKey)
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .bodyValue(bytes)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return supabaseUrl + "/storage/v1/object/public/profiles/" + userId + ".jpg";
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }
    }
}
