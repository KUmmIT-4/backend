package com.kummit.api_server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FirstStringParseDto(
        @JsonProperty("problem_title") String problemTitle,
        @JsonProperty("problem_explanation") String problemExplanation,
        @JsonProperty("input_format") String inputFormat,
        @JsonProperty("output_format") String outputFormat,
        @JsonProperty("input_example") String inputExample,
        @JsonProperty("output_example") String outputExample
) {
}
