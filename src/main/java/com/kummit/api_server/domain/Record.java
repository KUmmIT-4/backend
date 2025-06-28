package com.kummit.api_server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.kummit.api_server.enums.Status;

import java.time.LocalDateTime;

/**
 * 한 사용자가 한 문제를 풀이(시도)한 단일 기록.
 *  - 제출 시각 · 선택지(픽) · 채점 결과 등을 저장한다.
 */
@Entity
@Table(name = "record")
@Getter
public class Record {

    /* ---------- PK ---------- */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
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
    @Column(nullable = false, length = 10)
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

    protected Record() { }         // JPA 기본 생성자

    public Record(User user,
                  Problem problem,
                  Status status,
                  LocalDateTime submittedAt,
                  Byte userChoice) {

        this.user         = user;
        this.problem      = problem;
        this.status       = status;
        this.submittedAt  = submittedAt;
        this.userChoice   = userChoice;
    }
}