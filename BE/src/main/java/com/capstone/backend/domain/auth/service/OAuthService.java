package com.capstone.backend.domain.auth.service;

import com.capstone.backend.common.exception.BusinessException;
import com.capstone.backend.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final WebClient webClient;

    @SuppressWarnings("unchecked")
    public OAuthUserInfo getKakaoUserInfo(String accessToken) {
        try {
            Map<String, Object> response = webClient.post()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            Object rawId = response.get("id");
            String providerId = rawId instanceof Number
                    ? String.valueOf(((Number) rawId).longValue())
                    : String.valueOf(rawId);

            Map<String, Object> kakaoAccount = (Map<String, Object>) response.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            String email = (String) kakaoAccount.getOrDefault("email", null);
            String name = (String) profile.getOrDefault("nickname", "카카오 사용자");
            String profileImageUrl = (String) profile.getOrDefault("profile_image_url", null);

            return new OAuthUserInfo(providerId, name, email, profileImageUrl);
        } catch (Exception e) {
            log.error("Kakao OAuth error: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_OAUTH_TOKEN);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class OAuthUserInfo {
        private String providerId;
        private String name;
        private String email;
        private String profileImageUrl;
    }
}
