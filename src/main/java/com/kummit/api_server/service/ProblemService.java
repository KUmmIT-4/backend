package com.kummit.api_server.service;

import com.kummit.api_server.domain.BojProblemInfo;
import com.kummit.api_server.dto.ProblemDetailDto;
import com.kummit.api_server.enums.ProblemTier;
import com.kummit.api_server.repository.BojProblemInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final BojProblemInfoRepository repository;
    private final BaekjoonClient client;

    public ProblemDetailDto pickProblemAndFetchDetail(
            ProblemTier tier, byte level) {

        // ① DB에서 랜덤 한 문제 뽑기 (blocking → Mono.just)
        BojProblemInfo info = repository.pickRandom(tier, level)
                .orElseThrow();

        // ② 외부 API 호출 (비동기)
        return client.fetchDetail(info.getProblemNum());
    }
}
