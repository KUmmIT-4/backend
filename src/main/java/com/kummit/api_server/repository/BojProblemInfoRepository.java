package com.kummit.api_server.repository;

import com.kummit.api_server.domain.BojProblemInfo;
import com.kummit.api_server.enums.ProblemTier;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BojProblemInfoRepository extends JpaRepository<BojProblemInfo, Long> {

    // tier + level 로 조건 검색 후 1건 랜덤 반환
    @Query(value = """
        SELECT * FROM boj_problem_info
        WHERE problem_tier = :tier
          AND problem_level = :level
        ORDER BY RAND()
        LIMIT 1
        """, nativeQuery = true)
    Optional<BojProblemInfo> pickRandom(
            @Param("tier") ProblemTier tier,
            @Param("level") byte level);
}
