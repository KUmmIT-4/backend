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

    public BojProblemInfo pickProblem(
            ProblemTier tier, byte level) {

        // DB에서 랜덤 한 문제 뽑기
        BojProblemInfo info = repository.pickRandom(tier.getDisplayName(), level)
                .orElseThrow();

        System.out.println("info = " + info.getProblemLevel() + ", " + info.getProblemNum() + ", " + info.getProblemTier() + ", " + info.getSolvedUserCount());
        return info;
    }

//        System.out.println(info.getProblemLevel());
//        System.out.println(info.getProblemNum());
//        System.out.println(info.getProblemTier());
//        System.out.println(info.getSolvedUserCount());

}