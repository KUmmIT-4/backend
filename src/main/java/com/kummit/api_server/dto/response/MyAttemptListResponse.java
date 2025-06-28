package com.kummit.api_server.dto.response;

import java.util.List;

public record MyAttemptListResponse(
        List<AttemptSummaryResponse> attempts,
        boolean hasNext
) {
}
