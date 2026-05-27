package com.capstone.backend.domain.user.dto.response;

import com.capstone.backend.domain.user.entity.User;
import com.capstone.backend.domain.user.entity.UserPreference;
import com.capstone.backend.domain.user.entity.UserSkinProfile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class UserMeResponse {

    private UUID id;
    private String name;
    private String email;
    private String profileImageUrl;
    private String gender;
    private String provider;
    private boolean onboardingCompleted;
    private SkinProfileInfo skinProfile;
    private PreferenceInfo preferences;
    private StatsInfo stats;

    @Getter
    @Builder
    public static class SkinProfileInfo {
        private String personalColor;
        private String skinType;
        private List<String> skinConcerns;
        private List<String> notes;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class PreferenceInfo {
        private Integer priceTolerancePercent;
        private String searchPurpose;
    }

    @Getter
    @Builder
    public static class StatsInfo {
        private long wishlistCount;
        private long trackingCount;
        private long registeredCount;
    }

    public static UserMeResponse of(User user, UserSkinProfile skinProfile,
                                    UserPreference preference,
                                    long wishlistCount, long trackingCount, long registeredCount) {
        SkinProfileInfo skinInfo = null;
        if (skinProfile != null) {
            skinInfo = SkinProfileInfo.builder()
                    .personalColor(skinProfile.getPersonalColor())
                    .skinType(skinProfile.getSkinType())
                    .skinConcerns(skinProfile.getSkinConcerns() != null
                            ? Arrays.asList(skinProfile.getSkinConcerns()) : List.of())
                    .notes(skinProfile.getNotes() != null
                            ? Arrays.asList(skinProfile.getNotes()) : null)
                    .updatedAt(skinProfile.getUpdatedAt())
                    .build();
        }

        PreferenceInfo prefInfo = null;
        if (preference != null) {
            prefInfo = PreferenceInfo.builder()
                    .priceTolerancePercent(preference.getPriceTolerancePercent())
                    .searchPurpose(preference.getSearchPurpose())
                    .build();
        }

        return UserMeResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .gender(user.getGender())
                .provider(user.getProvider())
                .onboardingCompleted(user.isOnboardingCompleted())
                .skinProfile(skinInfo)
                .preferences(prefInfo)
                .stats(StatsInfo.builder()
                        .wishlistCount(wishlistCount)
                        .trackingCount(trackingCount)
                        .registeredCount(registeredCount)
                        .build())
                .build();
    }
}
