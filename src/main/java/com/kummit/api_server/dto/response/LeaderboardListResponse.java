package com.kummit.api_server.dto.response;

import java.util.List;

public record LeaderboardListResponse(
        List<LeaderboardResponse> users,
        boolean hasNext
) { }