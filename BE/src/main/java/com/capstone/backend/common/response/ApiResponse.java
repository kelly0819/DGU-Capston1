package com.capstone.backend.common.response;

import com.capstone.backend.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final Meta meta;
    private final ErrorInfo error;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> success(T data, Meta meta) {
        return new ApiResponse<>(true, data, meta, null);
    }

    public static ApiResponse<Void> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, null, null,
                new ErrorInfo(errorCode.getCode(), errorCode.getMessage(), errorCode.getStatus().value()));
    }

    public static ApiResponse<Void> error(String code, String message, int status) {
        return new ApiResponse<>(false, null, null, new ErrorInfo(code, message, status));
    }

    @Getter
    @AllArgsConstructor
    public static class Meta {
        private final int page;
        private final int size;
        private final long total;
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorInfo {
        private final String code;
        private final String message;
        private final int status;
    }
}
