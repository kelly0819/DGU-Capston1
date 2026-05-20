package com.capstone.backend.domain.auth.service;

import com.capstone.backend.common.exception.BusinessException;
import com.capstone.backend.common.exception.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public OAuthUserInfo getUserInfo(String provider, String accessToken) {
        return switch (provider.toUpperCase()) {
            case "KAKAO" -> getKakaoUserInfo(accessToken);
            case "GOOGLE" -> getGoogleUserInfo(accessToken);
            case "APPLE" -> getAppleUserInfo(accessToken);
            default -> throw new BusinessException(ErrorCode.INVALID_OAUTH_TOKEN);
        };
    }

    @SuppressWarnings("unchecked")
    private OAuthUserInfo getKakaoUserInfo(String accessToken) {
        try {
            Map<String, Object> response = webClient.post()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            Object rawId = response.get("id");
            String providerId = rawId instanceof Number ? String.valueOf(((Number) rawId).longValue()) : String.valueOf(rawId);

            Map<String, Object> kakaoAccount = (Map<String, Object>) response.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            String email = (String) kakaoAccount.getOrDefault("email", null);
            String name = (String) profile.getOrDefault("nickname", "카카오 사용자");

            return new OAuthUserInfo(providerId, name, email);
        } catch (Exception e) {
            log.error("Kakao OAuth error: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_OAUTH_TOKEN);
        }
    }

    private OAuthUserInfo getGoogleUserInfo(String accessToken) {
        try {
            Map<String, Object> response = webClient.get()
                    .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            String sub = (String) response.get("sub");
            String email = (String) response.get("email");
            String name = (String) response.getOrDefault("name", "구글 사용자");

            return new OAuthUserInfo(sub, name, email);
        } catch (Exception e) {
            log.error("Google OAuth error: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_OAUTH_TOKEN);
        }
    }

    private OAuthUserInfo getAppleUserInfo(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length != 3) {
                throw new BusinessException(ErrorCode.INVALID_OAUTH_TOKEN);
            }

            byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
            Map<String, Object> claims = objectMapper.readValue(payloadBytes, new TypeReference<>() {});

            String sub = (String) claims.get("sub");
            String email = (String) claims.get("email");
            String name = email != null ? email.split("@")[0] : sub;

            return new OAuthUserInfo(sub, name, email);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Apple OAuth error: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_OAUTH_TOKEN);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class OAuthUserInfo {
        private String providerId;
        private String name;
        private String email;
    }
}
