package com.kummit.api_server.dto;

import com.kummit.api_server.enums.ProblemTier;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** 요청 예시: { "tier": "BRONZE", "level": 1 } */
public record AttemptRequest(
        @NotNull ProblemTier tier,
        @Min(1) @Max(5) byte level
) {}
