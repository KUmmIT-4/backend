package com.kummit.api_server.repository;

import com.kummit.api_server.domain.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Attempt 엔티티 전용 기본 CRUD 리포지토리.
 */
public interface AttemptRepository extends JpaRepository<Attempt, Long> { }
