// src/main/java/com/kummit/api_server/controller/AttemptHistoryController.java
package com.kummit.api_server.controller;
import java.time.LocalDateTime;
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
import com.kummit.api_server.dto.request.AttemptSubmitRequest;
import com.kummit.api_server.dto.response.AttemptSubmitResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.Map;


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

    /** 답안 제출 (채점) */
    @PatchMapping("/{attemptId}")
    public ResponseEntity<?> submitAttempt(
            @PathVariable Long attemptId,
            @CookieValue(value = "session_id", required = false) String sessionId,
            @RequestBody AttemptSubmitRequest req
    ) {
        // 1. 세션 검사
        if (sessionId == null || !sessionStore.exists(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = sessionStore.getUserId(sessionId);

        // 2. pick 값 유효성
        Integer pick = req.getPick();
        if (pick == null || pick < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "timestamp", java.time.LocalDateTime.now().toString(),
                            "status", 400,
                            "error", "Bad Request",
                            "message", "selectedChoice는 0 이상의 정수여야 합니다.",
                            "path", "/api/attempts/" + attemptId
                    )
            );
        }

        // 3. 서비스 호출 및 예외 처리
        try {
            AttemptSubmitResponse res = attemptHistoryService.submitAttempt(userId, attemptId, pick);
            return ResponseEntity.ok(res);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "timestamp", java.time.LocalDateTime.now().toString(),
                            "status", 404,
                            "error", "Not Found",
                            "message", e.getMessage(),
                            "path", "/api/attempts/" + attemptId
                    )
            );
        }
    }

    /** 풀이 포기 */
    @PatchMapping("/{attemptId}/abandon")
    public ResponseEntity<?> abandonAttempt(
            @PathVariable Long attemptId,
            @CookieValue(value = "session_id", required = false) String sessionId
    ) {
        // 1. 세션 검사
        if (sessionId == null || !sessionStore.exists(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = sessionStore.getUserId(sessionId);

        // 2. 서비스 호출
        try {
            attemptHistoryService.abandonAttempt(userId, attemptId);
            // 204 No Content, 메시지 Body 포함 (권장 X지만, 명시적으로 포함 시 200 or 204 둘 다 쓸 수 있음)
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Map.of("message", "포기 상태로 처리되었습니다."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                    "timestamp", LocalDateTime.now().toString(),
                    "status", 404,
                    "error", "Not Found",
                    "message", e.getMessage(),
                    "path", "/api/attempts/" + attemptId + "/abandon"
                )
            );
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                    "timestamp", LocalDateTime.now().toString(),
                    "status", 409,
                    "error", "Conflict",
                    "message", e.getMessage(),
                    "path", "/api/attempts/" + attemptId + "/abandon"
                )
            );
        }
    }

}
