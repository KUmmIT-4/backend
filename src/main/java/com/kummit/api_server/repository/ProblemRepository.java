package com.kummit.api_server.repository;

import com.kummit.api_server.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findByProblemNum(Integer problemNum);
}
