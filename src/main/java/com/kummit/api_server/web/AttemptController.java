package com.kummit.api_server.web;

import com.kummit.api_server.dto.AttemptResponse;
import com.kummit.api_server.dto.AttemptStartRequest;
import com.kummit.api_server.service.AttemptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

    private final AttemptService attemptService;

    /**
     * 문제 도전 시작
     * - 세션 기능이 완성되기 전까지는 userId 를 1L 로 고정
     */
    @PostMapping
    public ResponseEntity<AttemptResponse> startAttempt(
            @Valid @RequestBody AttemptStartRequest body) {

        Long userId = 1L;   // TODO: 로그인 완료 후 세션에서 꺼내도록 교체

        AttemptResponse res = attemptService.startAttempt(userId, body);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
