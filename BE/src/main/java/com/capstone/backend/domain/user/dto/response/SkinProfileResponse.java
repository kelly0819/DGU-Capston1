package com.capstone.backend.domain.user.dto.response;

import com.capstone.backend.domain.user.entity.UserSkinProfile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class SkinProfileResponse {

    private UUID skinProfileId;
    private String personalColor;
    private String skinType;
    private List<String> skinConcerns;
    private List<String> notes;
    private LocalDateTime updatedAt;

    public static SkinProfileResponse from(UserSkinProfile p) {
        return SkinProfileResponse.builder()
                .skinProfileId(p.getId())
                .personalColor(p.getPersonalColor())
                .skinType(p.getSkinType())
                .skinConcerns(p.getSkinConcerns() != null ? Arrays.asList(p.getSkinConcerns()) : List.of())
                .notes(p.getNotes() != null ? Arrays.asList(p.getNotes()) : null)
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
