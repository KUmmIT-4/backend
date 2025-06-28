package com.kummit.api_server.domain;

import com.kummit.api_server.enums.PrimaryLanguage;
import com.kummit.api_server.enums.Status;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 한 사용자가 한 문제를 풀이(시도)한 단일 기록.
 *  - 제출 시각 · 선택지 · 채점 결과 등을 저장한다.
 */
@Getter
@Setter
@Entity
@Table(name = "record")              // DB 테이블명: attempt  (기존 record 를 그대로 쓰려면 "record")
public class Attempt {

    /* ---------- PK ---------- */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")      // 컬럼명: attempt_id (바꾸기 싫으면 record_id 유지 가능)
    private Long id;

    /* ---------- FK ---------- */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    /* ---------- 풀이 상태 ---------- */
    @Enumerated(EnumType.STRING)
    @Column(name = "attempt_language", nullable = false, length = 12)
    private PrimaryLanguage attemptLanguage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private Status status = Status.ATTEMPTING;

    /** 실제 코드 제출 시각 (null : 아직 미제출) */
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    /** 사용자가 고른 정답 보기(0 ~ n) — null : 아직 선택 전 */
    @Column(name = "user_choice")
    private Byte userChoice;

    /* ---------- 메타 ---------- */

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* ---------- 생성자 ---------- */

    protected Attempt() { }        // JPA 기본 생성자

    @Builder
    public Attempt(User user,
                   Problem problem,
                   Status status,
                   LocalDateTime submittedAt,
                   Byte userChoice,
                   PrimaryLanguage attemptLanguage) {

        this.user = user;
        this.problem = problem;
        this.status = status;
        this.submittedAt = submittedAt;
        this.userChoice = userChoice;
        this.attemptLanguage = attemptLanguage;
    }
}
