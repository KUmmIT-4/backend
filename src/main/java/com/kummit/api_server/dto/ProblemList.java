package com.kummit.api_server.dto;

import java.util.List;

// /search/problem 결과 전체를 담는 record
public record ProblemList(
        int count,
        List<ProblemItem> items   // 필수!
) {}