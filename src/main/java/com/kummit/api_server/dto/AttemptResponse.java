package com.kummit.api_server.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AttemptResponse(
        Long attemptId,
        String status,
        LocalDateTime startedAt,

        Long problemId,
        String title,
        String description,
        String code,
        List<String> choices,
        String level
) { }
