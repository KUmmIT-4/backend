package com.kummit.api_server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ProblemResponseDto(
        @JsonProperty("problem_id")
        Long problemId,

        String tier,
        String level,
        String language,

        @JsonProperty("problem_title")
        String problemTitle,

        @JsonProperty("problem_explanation")
        String problemExplanation,

        @JsonProperty("quiz_text")
        String quizText,

        String code,

        List<String> choices,

        @JsonProperty("answer_choice")
        Integer answerChoice,

        List<String> rationale
){}
