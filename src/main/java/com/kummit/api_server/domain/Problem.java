package com.kummit.api_server.domain;

import com.kummit.api_server.enums.ProblemTier;
import jakarta.persistence.*;
import lombok.Getter;
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
@Table(
        name = "problem",
        indexes = { @Index(name = "idx_problem_num", columnList = "problem_num") }
)
@Getter
public class Problem {

    /* ---------- PK ---------- */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long id;

    /* ---------- FK : boj_problem_info.problem_num ---------- */

    /**
     * BOJ 원본 문제 번호.  
     * insertable/updatable = false → 값은 애플리케이션에서 세팅하지만
     * JPA 가 FK 컬럼을 직접 변경하지는 않도록 함.
     */
    @Column(name = "problem_num", nullable = false, unique = true)
    private Integer problemNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "problem_num",
            referencedColumnName = "problem_num",
            insertable = false,
            updatable   = false
    )
    private BojProblemInfo meta;   // (옵션) 가벼운 메타 정보

    /* ---------- 본문 ---------- */

    @Column(name = "problem_title", nullable = false, length = 255)
    private String title;

    @Column(name = "problem_explanation", nullable = false, columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "input_format",  nullable = false, columnDefinition = "TEXT")
    private String inputFormat;

    @Column(name = "output_format", nullable = false, columnDefinition = "TEXT")
    private String outputFormat;

    @Column(name = "input_example",  nullable = false, columnDefinition = "TEXT")
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

    /** 해설(JSON) */
    @Column(name = "rationale", nullable = false, columnDefinition = "JSON")
    private String rationale = "[]"; // NOT NULL DEFAULT '[]'

    /** 퀴즈 지문 */
    @Column(name = "quiz_text", nullable = false, length = 255)
    private String quizText = "";    // NOT NULL DEFAULT ''

    /* ---------- 메타 ---------- */

    @Enumerated(EnumType.STRING)
    @Column(name = "problem_tier", nullable = false, length = 6)
    private ProblemTier problemTier;     // BRONZE / SILVER / GOLD

    @Column(name = "problem_level", nullable = false)
    private Byte problemLevel;           // 1 ~ 5

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* ---------- 연관 관계 ---------- */

    @OneToMany(
            mappedBy     = "problem",
            cascade      = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Attempt> attempts = new ArrayList<>();

    /* ---------- 생성자 ---------- */

    protected Problem() { } // JPA 기본 생성자

    public Problem(Integer problemNum,
                   String title,
                   String explanation,
                   String inputFormat,
                   String outputFormat,
                   String inputExample,
                   String outputExample,
                   String code,
                   String choices,
                   String answerChoice,
                   String rationale,
                   String quizText,
                   ProblemTier problemTier,
                   Byte problemLevel) {

        this.problemNum    = problemNum;
        this.title         = title;
        this.explanation   = explanation;
        this.inputFormat   = inputFormat;
        this.outputFormat  = outputFormat;
        this.inputExample  = inputExample;
        this.outputExample = outputExample;
        this.code          = code;
        this.choices       = choices;
        this.answerChoice  = answerChoice;
        this.rationale     = rationale != null ? rationale : "[]";
        this.quizText      = quizText != null ? quizText : "";
        this.problemTier   = problemTier;
        this.problemLevel  = problemLevel;
    }
}
