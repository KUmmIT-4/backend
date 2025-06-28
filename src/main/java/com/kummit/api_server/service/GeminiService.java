package com.kummit.api_server.service;

import com.google.genai.Chat;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final Client client;                // Geminiconfig에서 주입
    // "gemini-2.5-flash"
    private static final String MODEL = "gemini-2.0-flash-lite"; // 자유롭게 변경

    public String ask(String prompt) {
        Chat chat = client.chats.create(MODEL);        // 새 Chat 인스턴스
        GenerateContentResponse resp = chat.sendMessage(prompt);
        return resp.text();                              // 간편 접근자
    }
}