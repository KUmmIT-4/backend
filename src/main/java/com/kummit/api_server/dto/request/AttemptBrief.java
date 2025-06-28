// src/main/java/com/kummit/api_server/dto/response/AttemptBrief.java
package com.kummit.api_server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "한 번의 풀이 기록 요약 정보")
public class AttemptBrief {
    @JsonProperty("attempt_id")
    @Schema(description = "도전 기록 ID")
    private Long attemptId;

    @JsonProperty("problem_id")
    @Schema(description = "문제 ID")
    private Long problemId;

    @JsonProperty("title")
    @Schema(description = "문제 제목")
    private String title;

    @JsonProperty("status")
    @Schema(description = "상태 (ATTEMPTING, SOLVED, ABANDONED)")
    private String status;

    @JsonProperty("level")
    @Schema(description = "티어+레벨 (예: B5)")
    private String level;
}
