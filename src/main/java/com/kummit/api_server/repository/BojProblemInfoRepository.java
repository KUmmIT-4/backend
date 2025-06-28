package com.kummit.api_server.repository;

import com.kummit.api_server.domain.BojProblemInfo;
import com.kummit.api_server.enums.ProblemTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BojProblemInfoRepository
        extends JpaRepository<BojProblemInfo, Integer> {


    @Query(value = """
        SELECT * FROM   (
                SELECT *
                FROM   bojprobleminfo
                WHERE  problem_tier = 'Gold'
                  AND  problem_level = :lv
                ORDER  BY RAND()
                LIMIT  20
               ) AS sample
        ORDER  BY accept_user_count DESC
        LIMIT  1
        """,nativeQuery = true)
    Optional<BojProblemInfo> pickRandom(
            @Param("tier") ProblemTier tier,
            @Param("lv") byte lv);
}