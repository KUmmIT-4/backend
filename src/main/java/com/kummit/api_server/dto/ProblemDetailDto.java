package com.kummit.api_server.dto;


// 예시: 1024, 배낭 옮기기, 이건 뭐시기 저시기 해서 짜보세요 ,입력값 어쩌구 저쩌구, 출력값 어쩌구 저쩌구
public record ProblemDetailDto(
        int problemId,
        String titleKo,
        String problemContent,
        String inputDescription,
        String outputDescription
) {}