package com.kummit.api_server.dto.response;

public record AttemptSummaryResponse(
        Long attempt_id,
        Long problem_id,
        String title,
        String status,   // 예: "completed", "progress"
        String attemptLanguage,
        String problemTier,
        byte problemLevel
) {}