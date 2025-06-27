package com.kummit.api_server.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "problem")
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long id;

    @Column(name = "problem_num", nullable = false)
    private Integer problemNum;

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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    @Column(nullable = false, columnDefinition = "JSON")
    private String choices;

    @Column(name = "answer_choice", nullable = false, length = 255)
    private String answerChoice;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "problem_tier", nullable = false)
    private ProblemTier problemTier;

    @Column(name = "problem_level", nullable = false)
    private Byte problemLevel;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records = new ArrayList<>();

    protected Problem() { }

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
                   ProblemTier problemTier,
                   Byte problemLevel) {
        this.problemNum = problemNum;
        this.title = title;
        this.explanation = explanation;
        this.inputFormat = inputFormat;
        this.outputFormat = outputFormat;
        this.inputExample = inputExample;
        this.outputExample = outputExample;
        this.code = code;
        this.choices = choices;
        this.answerChoice = answerChoice;
        this.problemTier = problemTier;
        this.problemLevel = problemLevel;
    }

    @PreUpdate
    public void touchUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- enums ---
    public enum ProblemTier { BRONZE, SILVER, GOLD }

    // --- getters ---
    public Long getId() {
        return id;
    }

    public Integer getProblemNum() {
        return problemNum;
    }

    public String getTitle() {
        return title;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getInputFormat() {
        return inputFormat;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public String getInputExample() {
        return inputExample;
    }

    public String getOutputExample() {
        return outputExample;
    }

    public String getCode() {
        return code;
    }

    public String getChoices() {
        return choices;
    }

    public String getAnswerChoice() {
        return answerChoice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ProblemTier getProblemTier() {
        return problemTier;
    }

    public Byte getProblemLevel() {
        return problemLevel;
    }

    public List<Record> getRecords() {
        return records;
    }
}
