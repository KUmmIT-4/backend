// src/main/java/com/kummit/api_server/service/AttemptHistoryService.java
package com.kummit.api_server.service;
import com.kummit.api_server.enums.Status;
import com.kummit.api_server.dto.response.AttemptSubmitResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kummit.api_server.domain.Attempt;
import com.kummit.api_server.dto.response.AttemptBrief;
import com.kummit.api_server.dto.response.AttemptDetailResponse;
import com.kummit.api_server.dto.response.AttemptListResponse;
import com.kummit.api_server.repository.AttemptHistoryRepository;
import com.kummit.api_server.repository.AttemptRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.kummit.api_server.domain.User;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttemptHistoryService {

    private final AttemptHistoryRepository historyRepo;
    private final AttemptRepository        attemptRepo;
    private final ObjectMapper             objectMapper;

    private int calcScore(String tier, int level) {
        tier = tier.toUpperCase();
        // 브론즈 5~1 → 1~5
        if (tier.equals("BRONZE")) return level;
        // 실버 5~1 → 6~10
        if (tier.equals("SILVER")) return 5 + level;
        // 골드 5~1 → 11~15
        if (tier.equals("GOLD"))   return 10 + level;
        // 그 외: 0점
        return 0;
    }

    /** 내 풀이 기록 전체 조회 */
    public AttemptListResponse listAll(Long userId) {
        List<Attempt> attempts = historyRepo.findAllByUser_IdOrderByCreatedAtDesc(userId);
        return new AttemptListResponse(toBriefs(attempts));
    }

    /** 특정 날짜(YYYY-MM-DD) 조회 */
    public AttemptListResponse listByDate(Long userId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay();
        List<Attempt> attempts = historyRepo
                .findAllByUser_IdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, start, end);
        return new AttemptListResponse(toBriefs(attempts));
    }

    private List<AttemptBrief> toBriefs(List<Attempt> attempts) {
        return attempts.stream()
                .map(att -> {
                    // char + int 이 아닌, String으로 변환한 뒤 이어붙이기
                    String tierLevel = String.valueOf(
                            att.getProblem().getProblemTier().name().charAt(0)
                    ) + att.getProblem().getProblemLevel();

                    return new AttemptBrief(
                            att.getId(),
                            att.getProblem().getId(),
                            att.getProblem().getTitle(),
                            att.getStatus().name().toLowerCase(),
                            tierLevel
                    );
                })
                .collect(Collectors.toList());
    }

    /** 단일 풀이 기록 상세 조회 */
    public AttemptDetailResponse getDetail(Long userId, Long attemptId) {
        Attempt att = attemptRepo
                .findByIdAndUser_Id(attemptId, userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 풀이 기록을 찾을 수 없습니다."));

        var p = att.getProblem();

        List<String> choices;
        try {
            choices = objectMapper.readValue(
                    p.getChoices(),
                    new TypeReference<List<String>>() {}
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("문제 보기를 파싱할 수 없습니다.", e);
        }

        // String으로 변환 후 레벨 붙이기
        String tierLevel = String.valueOf(p.getProblemTier().name().charAt(0))
                + p.getProblemLevel();

        return new AttemptDetailResponse(
                att.getId(),
                p.getId(),
                p.getTitle(),
                p.getExplanation(),
                p.getCode(),
                choices,
                Integer.parseInt(p.getAnswerChoice()),                     // 0-based
                att.getUserChoice() != null ? att.getUserChoice().intValue() : null,
                att.getStatus().name().toLowerCase(),
                tierLevel
        );
    }

    /**
     * 답안 제출 및 채점 처리
     */
    @Transactional
    public AttemptSubmitResponse submitAttempt(Long userId, Long attemptId, int pick) {
        Attempt attempt = attemptRepo.findByIdAndUser_Id(attemptId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Attempt ID " + attemptId + "를 찾을 수 없습니다."));

        var problem = attempt.getProblem();
        int answerIdx = Integer.parseInt(problem.getAnswerChoice());
        String tier = problem.getProblemTier().name(); // Enum이면 .name() 사용
        int level = problem.getProblemLevel();         // 5,4,3,2,1

        int score = 0;
        String status;

        if (pick == answerIdx) {
            score = calcScore(tier, level);
            status = "solved";
        } else {
            score = 0;
            status = "abandoned";
        }

        attempt.setUserChoice((byte) pick);
        attempt.setStatusFromString(status);
        attempt.setSubmittedAt(java.time.LocalDateTime.now());

        return new AttemptSubmitResponse(
                attempt.getId(),
                status.toLowerCase(),
                score,
                attempt.getSubmittedAt() != null ? attempt.getSubmittedAt().toString() : null  // 여기!
        );

    }

    @Transactional
    public void abandonAttempt(Long userId, Long attemptId) {
        Attempt attempt = attemptRepo
                .findByIdAndUser_Id(attemptId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Attempt ID " + attemptId + "를 찾을 수 없습니다."));

        if (attempt.getStatus() == Status.SOLVED || attempt.getStatus() == Status.ABANDONED) {
            throw new IllegalStateException("이미 완료된 또는 포기된 시도입니다.");
        }

        // 1. 도전 상태 변경
        attempt.setStatus(Status.ABANDONED);
        attempt.setSubmittedAt(java.time.LocalDateTime.now());
        attemptRepo.save(attempt);

        // 2. streak(연속 도전 일수) 0으로 리셋
        User user = attempt.getUser();
        user.setDailyStreak(0);
        // userRepo.save(user); // JPA dirty checking이 동작하므로 명시적으로 save하지 않아도 됨(권장)
    }



}
