package com.capstone.backend.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
public class SkinProfileRequest {

    private String personalColor;
    private String skinType;
    private List<String> skinConcerns;
    private List<String> notes;

    private static final Set<String> VALID_PERSONAL_COLORS = Set.of(
            "SPRING_WARM", "SUMMER_COOL", "AUTUMN_MUTE", "WINTER_COOL", "UNKNOWN");
    private static final Set<String> VALID_SKIN_TYPES = Set.of(
            "DRY", "NORMAL", "OILY", "COMBINATION", "DEHYDRATED_OILY");
    private static final Set<String> VALID_SKIN_CONCERNS = Set.of(
            "SENSITIVITY", "ACNE", "ATOPY", "WHITENING", "SEBUM",
            "PORE", "DARK_CIRCLE", "REDNESS", "TEXTURE", "WRINKLE");

    public boolean isValid() {
        if (personalColor != null && !VALID_PERSONAL_COLORS.contains(personalColor.trim())) return false;
        if (skinType != null && !VALID_SKIN_TYPES.contains(skinType.trim())) return false;
        if (skinConcerns != null) {
            for (String c : skinConcerns) {
                if (!VALID_SKIN_CONCERNS.contains(c.trim())) return false;
            }
        }
        return true;
    }
}
