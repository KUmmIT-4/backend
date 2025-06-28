package com.kummit.api_server.dto;


// 예시: 1024, SILVER, 2, 20391
public record ProblemDetailDto(
        int problemId,
        String titleKo,
        String inputDescription,
        String outputDescription
) {}