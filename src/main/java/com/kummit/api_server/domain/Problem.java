package com.kummit.api_server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 실제 문제의 전문(제목·설명·코드·선택지 등)을 저장하는 테이블.
 * 문제 난이도(티어 / 레벨)·해결자 수 등
 * 가벼운 메타 정보는 {@link BojProblemInfo} 가 담당한다.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Table(
    name = "problem",
    indexes = {
        @Index(name = "idx_problem_num", columnList = "problem_num")
    }
)
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_num", referencedColumnName = "problem_num",
                insertable = false, updatable = false)
    private BojProblemInfo problemNum;         // 조회용 (옵션)

    /* ---------- 본문 ---------- */

    @Column(name = "problem_title", nullable = false, length = 255)
    private String title;

    @Column(name = "problem_explanation", nullable = false, columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "input_format", nullable = false, columnDefinition = "TEXT")
    private String inputFormat;

    @Column(name = "output_format", nullable = false, columnDefinition = "TEXT")
    private String outputFormat;

    @Column(name = "input_example", nullable = false, columnDefinition = "TEXT")
    private String inputExample;

    @Column(name = "output_example", nullable = false, columnDefinition = "TEXT")
    private String outputExample;

    /** 빈칸 뚫린 코드 스니펫 */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    /** 보기를 JSON 문자열로 저장 */
    @Column(nullable = false, columnDefinition = "JSON")
    private String choices;

    @Column(name = "answer_choice", nullable = false, length = 255)
    private String answerChoice;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* ---------- 연관 관계 ---------- */

    @OneToMany(mappedBy = "problem",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<Record> records = new ArrayList<>();

    /* ---------- ENUM ---------- */
    public enum ProblemTier { BRONZE, SILVER, GOLD }
}
