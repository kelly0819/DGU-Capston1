package com.capstone.backend.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증 토큰 없음 또는 만료"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "권한 없음"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "리소스 없음"),
    VALIDATION_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "VALIDATION_ERROR", "요청 파라미터 오류"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS", "이미 사용 중인 이메일"),
    ALREADY_TRACKING(HttpStatus.CONFLICT, "ALREADY_TRACKING", "이미 추적 중인 제품"),
    ALREADY_WISHLISTED(HttpStatus.CONFLICT, "ALREADY_WISHLISTED", "이미 찜한 상품입니다"),
    ALREADY_REGISTERED(HttpStatus.CONFLICT, "ALREADY_REGISTERED", "이미 등록된 상품입니다"),
    INVALID_OAUTH_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_OAUTH_TOKEN", "소셜 OAuth 토큰 오류"),
    IMAGE_PARSE_FAILED(HttpStatus.UNPROCESSABLE_ENTITY, "IMAGE_PARSE_FAILED", "이미지 제품 인식 실패"),
    AI_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AI_SERVER_ERROR", "AI 서버 내부 오류"),
    AI_SERVER_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "AI_SERVER_TIMEOUT", "FastAPI 응답 타임아웃"),
    JOB_NOT_COMPLETED(HttpStatus.CONFLICT, "JOB_NOT_COMPLETED", "추천 job이 아직 완료되지 않음"),
    FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "FILE_TOO_LARGE", "파일 크기가 초과되었습니다"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 내부 오류");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
