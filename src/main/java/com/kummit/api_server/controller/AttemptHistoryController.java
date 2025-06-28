// src/main/java/com/kummit/api_server/controller/AttemptHistoryController.java
package com.kummit.api_server.controller;

import com.kummit.api_server.SessionStore;
import com.kummit.api_server.dto.request.AttemptQueryRequest;
import com.kummit.api_server.dto.response.AttemptDetailResponse;
import com.kummit.api_server.dto.response.AttemptListResponse;
import com.kummit.api_server.dto.response.MyAttemptListResponse;
import com.kummit.api_server.service.AttemptHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class AttemptHistoryController {

    private final AttemptHistoryService attemptHistoryService;
    private final SessionStore          sessionStore;

    /** 내 풀이 기록 목록 조회 (페이징) */
    @PostMapping("/me")
    public ResponseEntity<MyAttemptListResponse> getMyAttempts(
            @CookieValue(value = "session_id", required = false) String sessionId,
            @RequestBody AttemptQueryRequest request
    ) {
        if (sessionId == null || !sessionStore.exists(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = sessionStore.getUserId(sessionId);
        return ResponseEntity.ok(
                attemptHistoryService.listAll(userId, request.pageNo(), request.perPage())
        );
    }

    /** 내 특정 풀이 기록 상세 조회 */
    @GetMapping("/me/{attemptId}")
    public ResponseEntity<AttemptDetailResponse> getMyAttemptDetail(
            @CookieValue(value = "session_id", required = false) String sessionId,
            @PathVariable Long attemptId
    ) {
        if (sessionId == null || !sessionStore.exists(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = sessionStore.getUserId(sessionId);
        AttemptDetailResponse detail = attemptHistoryService.getDetail(userId, attemptId);
        return ResponseEntity.ok(detail);
    }
}
