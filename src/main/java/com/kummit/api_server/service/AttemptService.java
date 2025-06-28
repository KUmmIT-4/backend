package com.kummit.api_server.service;

import com.kummit.api_server.dto.AttemptStartRequest;
import com.kummit.api_server.dto.AttemptResponse;

public interface AttemptService {
    AttemptResponse startAttempt(Long userId, AttemptStartRequest req);
}
