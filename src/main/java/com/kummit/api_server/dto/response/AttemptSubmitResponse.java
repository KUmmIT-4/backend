// src/main/java/com/kummit/api_server/dto/response/AttemptSubmitResponse.java
package com.kummit.api_server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttemptSubmitResponse {
    @JsonProperty("attemptId")
    private Long attemptId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("score")
    private Integer score;
    @JsonProperty("submittedAt")
    private String submittedAt; // ISO 8601 (LocalDateTime의 .toString()이면 OK)
}