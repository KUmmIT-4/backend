// src/main/java/com/kummit/api_server/dto/response/AttemptDetailResponse.java
package com.kummit.api_server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kummit.api_server.enums.PrimaryLanguage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "단일 풀이 기록 상세 응답")
public class AttemptDetailResponse {
    @JsonProperty("attempt_id")
    @Schema(description = "도전 기록 ID")
    private Long attemptId;

    @JsonProperty("problem_id")
    @Schema(description = "문제 ID")
    private Long problemId;

    @Schema(description = "문제 제목")
    private String title;

    @Schema(description = "문제 설명")
    private String description;

    @Schema(description = "제공된 코드")
    private String code;

    @Schema(description = "선택지 리스트")
    private List<String> choices;

    @Schema(description = "정답 인덱스 (0-based)")
    private String answer;

    @Schema(description = "사용자 선택 인덱스 (0-based)")
    private Integer pick;

    @Schema(description = "시도 언어")
    private PrimaryLanguage attemptLanguage;

    @Schema(description = "상태 (completed, progress, abandoned 등)")
    private String status;

    @Schema(description = "티어+레벨 (예: B5)")
    private String level;
}
