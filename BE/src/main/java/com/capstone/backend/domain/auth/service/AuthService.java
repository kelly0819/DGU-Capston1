package com.capstone.backend.domain.auth.service;

import com.capstone.backend.common.exception.BusinessException;
import com.capstone.backend.common.exception.ErrorCode;
import com.capstone.backend.common.util.JwtUtil;
import com.capstone.backend.domain.auth.dto.LoginRequest;
import com.capstone.backend.domain.auth.dto.SignupRequest;
import com.capstone.backend.domain.auth.dto.SocialLoginRequest;
import com.capstone.backend.domain.auth.dto.TokenResponse;
import com.capstone.backend.domain.user.entity.User;
import com.capstone.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OAuthService oAuthService;

    @Transactional
    public TokenResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .provider("EMAIL")
                .fcmToken(request.getFcmToken())
                .build();

        userRepository.save(user);
        return buildTokenResponse(user, true);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        return buildTokenResponse(user, false);
    }

    @Transactional
    public TokenResponse socialLogin(SocialLoginRequest request) {
        OAuthService.OAuthUserInfo oAuthUserInfo =
                oAuthService.getUserInfo(request.getProvider(), request.getAccessToken());

        boolean[] isNewUser = {false};

        User user = userRepository
                .findByProviderAndProviderId(request.getProvider(), oAuthUserInfo.getProviderId())
                .orElseGet(() -> {
                    isNewUser[0] = true;
                    User newUser = User.builder()
                            .email(oAuthUserInfo.getEmail())
                            .name(oAuthUserInfo.getName())
                            .provider(request.getProvider().toUpperCase())
                            .providerId(oAuthUserInfo.getProviderId())
                            .fcmToken(request.getFcmToken())
                            .build();
                    return userRepository.save(newUser);
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
        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isNewUser(isNewUser)
                .user(TokenResponse.UserInfo.from(user))
                .build();
    }
}
