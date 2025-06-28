package com.kummit.api_server.dto.response;

public record UserInfoResponse (
        Long id,
        String username,
        String codingTier,
        Byte codingLevel,
        String primaryLanguage,
        Integer rating,
        Integer dailyStreak
){
}
