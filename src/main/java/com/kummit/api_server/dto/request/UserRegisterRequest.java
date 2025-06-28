package com.kummit.api_server.dto.request;

// 사용자 회원가입 요청 DTO
public record UserRegisterRequest(
        String username,
        String password,
        String tier,
        byte level,
        String language
) {}
