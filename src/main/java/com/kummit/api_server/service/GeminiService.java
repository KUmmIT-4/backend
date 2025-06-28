package com.kummit.api_server.service;

import com.google.genai.Chat;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final Client client;                // Geminiconfig에서 주입
    // "gemini-2.5-flash"
    private static final String MODEL = "gemini-2.5-flash"; /*-lite";*/ // 자유롭게 변경

    @Async("geminiExecutor")
    public CompletableFuture<String> askAsync(String prompt) {
        Chat chat = client.chats.create(MODEL);        // 새 Chat 인스턴스
        String text = chat.sendMessage(prompt).text();
        return CompletableFuture.completedFuture(text);
    }
}