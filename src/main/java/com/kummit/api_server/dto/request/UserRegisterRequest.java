package com.kummit.api_server.dto.request;

import com.kummit.api_server.enums.PrimaryLanguage;

// 사용자 회원가입 요청 DTO
public record UserRegisterRequest(
        String username,
        String password,
        String tier,
        byte level,
        PrimaryLanguage language    // 예: Java, Python, C++
) {}
