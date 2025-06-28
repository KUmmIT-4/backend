package com.kummit.api_server.dto.response;

public record LeaderboardResponse(
        String username,
        int rating
) { }