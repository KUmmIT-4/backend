// src/main/java/com/kummit/api_server/service/AttemptHistoryService.java
package com.kummit.api_server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kummit.api_server.domain.Attempt;
import com.kummit.api_server.dto.response.*;
import com.kummit.api_server.dto.response.AttemptBrief;
import com.kummit.api_server.repository.AttemptHistoryRepository;
import com.kummit.api_server.repository.AttemptRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttemptHistoryService {

    private final AttemptHistoryRepository historyRepo;
    private final AttemptRepository        attemptRepo;
    private final ObjectMapper             objectMapper;

    public MyAttemptListResponse listAll(Long userId, int pageNo, int perPage) {
        Pageable pageable = PageRequest.of(pageNo - 1, perPage);
        Page<Attempt> page = historyRepo.findPageByUser_IdOrderByCreatedAtDesc(userId, pageable);

        List<AttemptSummaryResponse> attempts = page.getContent().stream()
                .map(a -> new AttemptSummaryResponse(
                        a.getId(),
                        a.getProblem().getId(),
                        a.getProblem().getTitle(),
                        a.getStatus().name().toLowerCase(),
                        a.getAttemptLanguage().toString(),
                        a.getProblem().getProblemTier().name(),
                        a.getProblem().getProblemLevel()
                ))
                .toList();

        return new MyAttemptListResponse(attempts, page.hasNext());
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
            p.getAnswerChoice(),
            att.getUserChoice() != null ? att.getUserChoice().intValue() : null,
            att.getAttemptLanguage(),
            att.getStatus().name().toLowerCase(),
            tierLevel, p.getRationale(), p.getQuizText()
        );
    }
}
