package com.kummit.api_server.dto.response;

// 사용자 회원가입 및 로그인 응답 DTO
public record UserResponse(
        Long userId,
        String username
) {}
