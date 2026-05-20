package com.capstone.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserDto {

    @Getter
    public static class Request {
        private String email;
        private String password;
        private String nickname;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String email;
        private String nickname;
    }
}
