package com.kummit.api_server.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {
    @Bean
    Client geminiClient(@Value("${google.ai.api-key}") String key) {
        return Client.builder().apiKey(key).build();
    }
}