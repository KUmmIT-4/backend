package com.kummit.api_server.controller;

import com.kummit.api_server.dto.AttemptRequest;
import com.kummit.api_server.dto.ProblemDetailDto;
import com.kummit.api_server.service.GeminiService;
import com.kummit.api_server.service.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class AttemptsController {

    private final ProblemService problemService;
    private final GeminiService geminiService;

    /** POST /api/attempts */
    /* 시간 남으면 비동기로 할지 생각해보자 */
    @PostMapping
    public void /*ResponseEntity<String>*/ attempt(@Valid @RequestBody AttemptRequest req) {

        // ① DB + solved.ac ⇒ 문제 메타 획득
        ProblemDetailDto detail = problemService
                .pickProblemAndFetchDetail(req.tier(), req.level());

        // ② Gemini 호출 (예: 문제 설명 주입)
        // String answer = geminiService.ask(detail);
        // return ResponseEntity.ok(answer);
    }
}