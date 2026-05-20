package com.capstone.backend.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SocialLoginRequest {

    @NotBlank(message = "provider는 필수입니다.")
    private String provider;  // KAKAO | GOOGLE | APPLE

    @NotBlank(message = "accessToken은 필수입니다.")
    private String accessToken;

    private String fcmToken;
}
