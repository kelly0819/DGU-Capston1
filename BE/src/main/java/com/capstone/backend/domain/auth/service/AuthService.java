package com.capstone.backend.domain.auth.service;

import com.capstone.backend.common.exception.BusinessException;
import com.capstone.backend.common.exception.ErrorCode;
import com.capstone.backend.common.util.JwtUtil;
import com.capstone.backend.domain.auth.dto.SocialLoginRequest;
import com.capstone.backend.domain.auth.dto.TokenResponse;
import com.capstone.backend.domain.user.entity.User;
import com.capstone.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final OAuthService oAuthService;

    @Transactional
    public TokenResponse socialLogin(SocialLoginRequest request) {
        OAuthService.OAuthUserInfo info = oAuthService.getKakaoUserInfo(request.getCode());

        boolean[] isNewUser = {false};

        User user = userRepository
                .findByProviderAndProviderId("KAKAO", info.getProviderId())
                .orElseGet(() -> {
                    isNewUser[0] = true;
                    return userRepository.save(User.builder()
                            .email(info.getEmail())
                            .name(info.getName())
                            .provider("KAKAO")
                            .providerId(info.getProviderId())
                            .fcmToken(request.getFcmToken())
                            .build());
                });

        if (request.getFcmToken() != null) {
            user.updateFcmToken(request.getFcmToken());
        }

        return buildTokenResponse(user, isNewUser[0]);
    }

    @Transactional(readOnly = true)
    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        UUID userId = jwtUtil.getUserId(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        return buildTokenResponse(user, false);
    }

    private TokenResponse buildTokenResponse(User user, boolean isNewUser) {
        return TokenResponse.builder()
                .accessToken(jwtUtil.generateAccessToken(user.getId()))
                .refreshToken(jwtUtil.generateRefreshToken(user.getId()))
                .isNewUser(isNewUser)
                .user(TokenResponse.UserInfo.from(user))
                .build();
    }
}
