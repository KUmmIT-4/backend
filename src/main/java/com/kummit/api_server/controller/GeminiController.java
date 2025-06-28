package com.kummit.api_server.controller;

import com.kummit.api_server.dto.ProblemList;
import com.kummit.api_server.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
public class GeminiController {

//    private final GeminiService geminiService;
//
//    // 문제 도전
//    /** POST /api/gemini  { "prompt": "..." } */
//    @PostMapping
//    public ResponseEntity<String> chat(@RequestBody PromptDto body) {
//
//        WebClient web = WebClient.builder()
//                .baseUrl("https://solved.ac/api/v3").build();
//        long start = System.nanoTime();
//        int page = ThreadLocalRandom.current().nextInt(1, 50);
//        String uri = "/search/problem?query=level:1..5&sort=random&page=" + page;
//        ProblemList list = web.get().uri(uri)
//                .header("x-solvedac-language","ko")
//                .retrieve().bodyToMono(ProblemList.class).block();
//        long end = System.nanoTime();
//        System.out.println(end - start);
//        System.out.println(list);
//
//        String answer = geminiService.ask(body.prompt());
//        return ResponseEntity.ok(answer);
//    }
//
//    /** 간단한 DTO (record 문법, Java 21+) */
//    public record PromptDto(String prompt) {}
}