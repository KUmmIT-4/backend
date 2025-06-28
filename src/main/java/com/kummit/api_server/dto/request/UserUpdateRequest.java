package com.kummit.api_server.dto.request;

public record UserUpdateRequest(
        String tier,
        byte level,
        String language
) {}
