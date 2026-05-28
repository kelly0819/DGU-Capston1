package com.capstone.backend.domain.user.dto.response;

import com.capstone.backend.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ProfileUpdateResponse {

    private UUID id;
    private String name;
    private String profileImageUrl;
    private LocalDateTime updatedAt;

    public static ProfileUpdateResponse from(User user) {
        return ProfileUpdateResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
