package com.kummit.api_server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kummit.api_server.domain.Attempt;
import com.kummit.api_server.domain.BojProblemInfo;
import com.kummit.api_server.domain.Problem;
import com.kummit.api_server.domain.User;
import com.kummit.api_server.dto.AttemptResponse;
import com.kummit.api_server.dto.AttemptStartRequest;
import com.kummit.api_server.enums.ProblemTier;
import com.kummit.api_server.enums.Status;
import com.kummit.api_server.repository.AttemptRepository;
import com.kummit.api_server.repository.BojProblemInfoRepository;
import com.kummit.api_server.repository.ProblemRepository;
import com.kummit.api_server.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class AttemptServiceImpl implements AttemptService {

    private final BojProblemInfoRepository infoRepo;
    private final ProblemRepository       problemRepo;
    private final AttemptRepository       attemptRepo;
    private final UserRepository          userRepo;
    private final ObjectMapper            objectMapper;

    @Override
    public AttemptResponse startAttempt(Long userId, AttemptStartRequest req) {

        /* ---------- 1. 조건에 맞는 랜덤 문제 번호 ---------- */
        BojProblemInfo info = infoRepo.pickRandom(
                ProblemTier.valueOf(req.tier()),
                req.level()
        ).orElseThrow(() -> new IllegalStateException("조건에 맞는 문제가 없습니다."));

        /* ---------- 2. 실제 문제 본문 ---------- */
        Problem p = problemRepo.findByProblemNum(info.getProblemNum())
                .orElseThrow(() -> new EntityNotFoundException("문제 본문이 없습니다."));

        /* ---------- 3. Attempt(= 풀이 기록) 저장 ---------- */
        User u   = userRepo.getReferenceById(userId);
        Attempt att = attemptRepo.save(
                new Attempt(u, p, Status.ATTEMPTING, null, null));

        /* ---------- 4. choices(JSON) → List<String> ---------- */
        List<String> choiceList;
        try {
            choiceList = objectMapper.readValue(
                    p.getChoices(), new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("문제 보기를 파싱할 수 없습니다.", e);
        }

        /* ---------- 5. DTO 생성 & 반환 ---------- */
        return new AttemptResponse(
                att.getId(),
                att.getStatus().name(),
                att.getCreatedAt(),

                p.getId(),
                p.getTitle(),
                p.getExplanation(),
                p.getCode(),
                choiceList,
                // 티어 첫 글자 + 레벨 (예: B5)
                String.valueOf(p.getProblemTier().name().charAt(0)) + p.getProblemLevel()
        );
    }
}
