package com.kummit.api_server.dto.response;

public record UserUpdateResponse(
        Long id,
        String codingTier,
        Byte codingLevel,
        String primaryLanguage
) {
}
