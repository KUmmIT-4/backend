// src/main/java/com/kummit/api_server/dto/response/AttemptListResponse.java
package com.kummit.api_server.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "사용자 풀이 기록 목록 응답")
public class AttemptListResponse {
    @Schema(description = "풀이 기록 리스트")
    private List<AttemptBrief> attempts;
}
