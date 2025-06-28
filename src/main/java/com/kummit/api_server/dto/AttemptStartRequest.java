package com.kummit.api_server.dto;

import jakarta.validation.constraints.*;

public record AttemptStartRequest(
        @NotBlank(message = "tier는 필수입니다.")
        @Pattern(regexp = "BRONZE|SILVER|GOLD")
        String tier,

        @Min(value = 1) @Max(value = 5)
        byte level
) { }
