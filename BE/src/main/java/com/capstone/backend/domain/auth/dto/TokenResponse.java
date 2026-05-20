package com.capstone.backend.domain.auth.dto;

import com.capstone.backend.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TokenResponse {

    private String accessToken;
    private String refreshToken;

    @JsonProperty("isNewUser")
    private boolean isNewUser;

    private UserInfo user;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserInfo {
        private UUID id;
        private String name;
        private String email;
        private boolean onboardingCompleted;

        public static UserInfo from(User user) {
            return UserInfo.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .onboardingCompleted(user.isOnboardingCompleted())
                    .build();
        }
    }
}
