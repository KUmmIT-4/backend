package com.kummit.api_server.dto.request;

public record UserLoginRequest(
        String username,
        String password
) {}
