package com.kummit.api_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean("geminiExecutor")
    public Executor geminiExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(2);         // 최소 스레드 2개
        exec.setMaxPoolSize(5);          // 최대 스레드 5개
        exec.setQueueCapacity(50);       // 대기 큐 크기
        exec.setThreadNamePrefix("gemini-");
        exec.initialize();               // 초기화 필요 :contentReference[oaicite:1]{index=1}
        return exec;
    }
}