package com.kummit.api_server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import com.kummit.api_server.enums.ProblemTier;

/**
 * solved.ac / BOJ 문제 메타데이터 LOOK-UP 테이블 매핑
 *
 *  • 문제를 티어·레벨 조건으로 “랜덤 추출”하기 위해 사용
 *  • 실제 문제 상세(TEXT 컬럼)까지 담고 있는 {@link Problem} 과는 분리하여
 *    가벼운 조회 전용 엔티티로 설계
 */
@Entity
@Table(name = "boj_problem_info")
@Getter
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
    @Column(name = "solved_user_count", nullable = false)
    private Integer solvedUserCount;

    /* ---------- 생성자 ---------- */

    /** JPA 기본 생성자 (Proxy용) – 직접 호출 금지 */
    protected BojProblemInfo() { }

    public BojProblemInfo(Integer problemNum,
                          ProblemTier problemTier,
                          Byte problemLevel,
                          Integer solvedUserCount) {
        this.problemNum = problemNum;
        this.problemTier = problemTier;
        this.problemLevel = problemLevel;
        this.solvedUserCount = solvedUserCount;
    }
}