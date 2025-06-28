package com.kummit.api_server;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 간단한 인메모리 세션 저장소
 * session_id → user_id 매핑 관리
 */
@Component
public class SessionStore {

    private final Map<String, Long> sessions = new ConcurrentHashMap<>();

    public void put(String sessionId, Long userId) {
        sessions.put(sessionId, userId);
    }

    public Long getUserId(String sessionId) {
        return sessions.get(sessionId);
    }

    public void remove(String sessionId) {
        sessions.remove(sessionId);
    }

    public boolean exists(String sessionId) {
        return sessions.containsKey(sessionId);
    }
}
