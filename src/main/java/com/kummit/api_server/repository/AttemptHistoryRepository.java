package com.kummit.api_server.repository;

import com.kummit.api_server.domain.Attempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자 풀이 기록 조회용 레포지토리
 */
@Repository
public interface AttemptHistoryRepository extends JpaRepository<Attempt, Long> {

    // 기존: 전체 리스트 반환
    List<Attempt> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

    // ✅ 추가: 페이징 지원 메서드
    Page<Attempt> findPageByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // 기존: 기간 필터링 리스트 반환
    List<Attempt> findAllByUser_IdAndCreatedAtBetweenOrderByCreatedAtDesc(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );

    // ✅ 추가: 페이징 + 기간 필터링 지원 메서드
    Page<Attempt> findPageByUser_IdAndCreatedAtBetweenOrderByCreatedAtDesc(
            Long userId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );
}

