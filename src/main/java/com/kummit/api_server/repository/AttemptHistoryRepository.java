package com.kummit.api_server.repository;

import com.kummit.api_server.domain.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자 풀이 기록 조회용 레포지토리
 */
@Repository
public interface AttemptHistoryRepository extends JpaRepository<Attempt, Long> {

    /**
     * 주어진 사용자(userId)의 모든 풀이 기록을 createdAt 내림차순으로 조회
     */
    List<Attempt> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

    /**
     * 주어진 사용자(userId)가 주어진 기간(start~end)에 생성한 풀이 기록을
     * createdAt 내림차순으로 조회
     */
    List<Attempt> findAllByUser_IdAndCreatedAtBetweenOrderByCreatedAtDesc(
        Long userId,
        LocalDateTime start,
        LocalDateTime end
    );
}
