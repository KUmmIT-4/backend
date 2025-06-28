package com.kummit.api_server.repository;

import com.kummit.api_server.domain.Attempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.time.LocalDateTime;


/**
 * 풀이 기록 저장 및 조회를 위한 레포지토리
 */
@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    // 유저 소유의 특정 풀이 기록 찾기
    Optional<Attempt> findByIdAndUser_Id(Long attemptId, Long userId);
    Page<Attempt> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);
}

