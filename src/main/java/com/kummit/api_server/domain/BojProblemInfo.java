package com.kummit.api_server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * solved.ac / BOJ 문제 메타데이터 LOOK-UP 테이블 매핑
 *
 *  • 문제를 티어·레벨 조건으로 “랜덤 추출”하기 위해 사용  
 *  • 실제 문제 상세(TEXT 컬럼)까지 담고 있는 {@link Problem} 과는 분리하여
 *    가벼운 조회 전용 엔티티로 설계
 */
@Entity
@Table(name = "bojprobleminfo")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class BojProblemInfo {

    /* ---------- PK ---------- */

    /** BOJ 원본 문제 번호 (예: 1000, 2557 …)  */
    @Id
    @Column(name = "problem_num")
    private Integer problemNum;

    /* ---------- 메타데이터 ---------- */

    @Enumerated(EnumType.STRING)
    @Column(name = "problem_tier", nullable = false, length = 6)
    private ProblemTier problemTier;          // BRONZE / SILVER / GOLD

    @Column(name = "problem_level", nullable = false)
    private Byte problemLevel;                // 1 ~ 5

    /** solved.ac 기준 해결자 수 (통계용) */
    @Column(name = "accept_user_count", nullable = false)
    private Integer acceptUserCount;


    public enum ProblemTier { BRONZE, SILVER, GOLD }
}
