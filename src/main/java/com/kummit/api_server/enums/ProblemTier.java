package com.kummit.api_server.enums;

import lombok.Getter;

@Getter
public enum ProblemTier {
    BRONZE("BRONZE"),
    SILVER("SILVER"),
    GOLD("GOLD");

    // 1. 필드 (Fields)
    private final String displayName;

    // 2. 생성자 (Constructor)
    ProblemTier(String displayName) {
        this.displayName = displayName;
    }
}

