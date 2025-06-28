package com.kummit.api_server.dto;

import java.util.List;

// 문제 1개를 담는 record (필드는 필요한 만큼만 선언)
public record ProblemItem(
        int problemId,
        String titleKo,
        int level,
        boolean isSolvable,
        long acceptedUserCount
) {}

