package com.kummit.api_server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SecondStringParseDto(
        @JsonProperty("question_number") Integer questionNumber,
        String code,
        @JsonProperty("quiz_text") String quizText,
        List<String> choices,
        @JsonProperty("answer_choice") Integer answerChoice,
        String hint,
        List<String> rationale
) {
}
