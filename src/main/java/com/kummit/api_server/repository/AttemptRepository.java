package com.kummit.api_server.repository;

import com.kummit.api_server.domain.Attempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


/**
 * Attempt 엔티티 전용 기본 CRUD 리포지토리.
 */
public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    Page<Attempt> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);
}

