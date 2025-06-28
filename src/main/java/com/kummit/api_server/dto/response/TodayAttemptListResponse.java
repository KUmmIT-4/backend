package com.kummit.api_server.dto.response;

import java.util.List;

public record TodayAttemptListResponse(
        List<AttemptSummaryResponse> attempts,
        boolean hasNext
) {}