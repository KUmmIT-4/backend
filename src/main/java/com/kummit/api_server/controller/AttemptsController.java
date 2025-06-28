package com.kummit.api_server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kummit.api_server.domain.BojProblemInfo;
import com.kummit.api_server.dto.AttemptRequest;
import com.kummit.api_server.dto.ProblemDetailDto;
import com.kummit.api_server.service.GeminiService;
import com.kummit.api_server.service.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class AttemptsController {

    private final ProblemService problemService;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    String queryTemplate1 = """
            (역할)
            당신은 Baekjoon 온라인 저지에서 문제 번호를 활용해 문제 정보를 추출하여 JSON 객체로만 반환하는 시스템입니다.
            
            (문제 번호)
            {problemNumber}
            
            (목표)
            아래 JSON 스키마에 완벽히 맞춰서, 응답을 JSON 객체로 출력하세요.
            
            (제약)
            추가적인 설명을 포함해서는 안 됩니다.
            Latex 문법을 사용하면 안 되고, 유니코드 수식을 사용합니다.
            
            {
              "problem_title":          { "type": "string"  },
              "problem_explanation":    { "type": "string"  },
              "input_format":   { "type": "string" },
              "output_format":  { "type": "string" },
              "input_example":  { "type": "string"  },
              "output_example": { "type": "string"  }
            }
            """;

    String queryTemplate2 = """
        (역할)
        당신은 Baekjoon 온라인 저지 문제의 C++ 해답 코드를 활용해, 코드의 일부를 빈칸으로 만드는 객관식 퀴즈를 JSON 형식으로 생성하는 시스템입니다.

        (문제 번호)
        {problemNumber}

        (목표)
        주어진 문제 번호의 C++ 언어 정답 코드 중 하나를 찾아, 주석을 모두 제거하고 핵심 로직 중 한 줄 이내를 `__BLANK__`로 대체하세요.
        그 후, 아래 JSON 스키마에 완벽히 맞춰서, 응답을 JSON 객체로만 출력하세요.

        (제약)
        - 별도 설명은 제공하지 않고, 무조건 JSON 형식에 따라 답변해야 합니다.
        - 4개의 선지(`choices`)를 제공하며, 정답 1개와 매력적인 오답 3개로 구성해야 합니다.
        - 각 선지는 번호를 붙이지 말고, `choices` 배열에 문자열로 하나씩 담아야 합니다.
        - `answer_choice`의 값은 `choices` 배열에 있는 정답 문자열과 정확히 일치해야 합니다.
        - 수식이 존재할 경우, LaTeX으로 절대 표현하지 말고, 유니코드를 사용해야 합니다.
        - JSON의 `code`를 포함한 모든 문자열 필드 내 줄바꿈은 `\\n`으로 표현해야 합니다.

        {
          "code": { "type": "string" },
          "choices": { "type": "array", "items": { "type": "string" } },
          "answer_choice": { "type": "string" }
        }
        """;

    /** POST /api/attempts */
    /* 시간 남으면 비동기로 할지 생각해보자 */
    @PostMapping
    public void /*ResponseEntity<String>*/ attempt (@Valid @RequestBody AttemptRequest req) throws Exception{
//        System.out.println(req.tier());
        // 클라이언트가 요청한 티어와 레벨 (ex: 골드 4)
        BojProblemInfo bojProblemInfo = problemService.pickProblem(req.tier(), req.level());

        int problemNumber = bojProblemInfo.getProblemNum();
        String query1 = queryTemplate1
                .replace("{problemNumber}", Integer.toString(problemNumber));
        String query2 = queryTemplate2
                .replace("{problemNumber}", Integer.toString(problemNumber));

        List<String> prompts = List.of(query1,query2);
        List<String> answers = parallelChat(prompts);
        List<String> cleanedAnswers = answers.stream()
                .map(s -> stripJsonCodeFence(s)) // 메소드 참조(Method Reference)로 더 간결하게 표현 가능
                .collect(Collectors.toList());
        cleanedAnswers.forEach(System.out::println);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        TypeReference<LinkedHashMap<String, Object>> typeRef = new TypeReference<>() {};
        Map<String, Object> problemMap = objectMapper.readValue(cleanedAnswers.get(0), typeRef);
        Map<String, Object> quizMap = objectMapper.readValue(cleanedAnswers.get(1), typeRef);
        Map<String, Object> flatMergedMap = new LinkedHashMap<>(problemMap);
        flatMergedMap.putAll(quizMap);
        String flatMergedJson = objectMapper.writeValueAsString(flatMergedMap);
        System.out.println("flatMergedJson = " + flatMergedJson);
//        JsonNode node = objectMapper.readTree(cleanedAnswers.get(0));
//        JsonNode node1 = objectMapper.readTree(cleanedAnswers.get(1));
//        String title         = node.get("title").asText();
//        String description   = node.get("description").asText();
//        String inputExample  = node.get("input_example").asText();
//        String outputExample = node.get("output_example").asText();
//        String inputFormat = node.get("input_format").asText();
//        String outputFormat = node.get("output_format").asText();
//        String code = node1.get("code").asText();
//        JsonNode choicesNode = node1.get("choices");
//        List<String> choicesList = new ArrayList<>();
//
//        String answerChoice = node1.get("answer_choice").asText();
//
//        System.out.println("title = " + title);
//        System.out.println("description = " + description);
//        System.out.println("inputExample = " + inputExample);
//        System.out.println("outputExample = " + outputExample);
//        System.out.println("inputFormat = " + inputFormat);
//        System.out.println("outputFormat = " + outputFormat);
//        System.out.println("code = " + code);
//        System.out.println("choices = " + choicesList);
//        System.out.println("answerChoice = " + answerChoice);
         //return ResponseEntity.ok(answer);
    }

    // 기존 parallelChat 예시
    public List<String> parallelChat(List<String> prompts) {
        CompletableFuture<String>[] futures = prompts.stream()
                .map(geminiService::askAsync)  // CompletableFuture<String> 반환 :contentReference[oaicite:1]{index=1}
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join(); // 모든 비동기 완료 대기 :contentReference[oaicite:2]{index=2}

        return Arrays.stream(futures)
                .map(CompletableFuture::join) // Future<String> → String :contentReference[oaicite:3]{index=3}
                .toList();
    }

    public static String stripJsonCodeFence(String apiResponse) {
        if (apiResponse == null || apiResponse.isEmpty()) {
            return apiResponse;
        }

        // 앞뒤 공백을 제거하여 정규식 매칭을 용이하게 한다.
        String trimmedResponse = apiResponse.trim();

        // 코드 펜스 패턴을 찾기 위한 정규 표현식
        // 유연성을 위해 `json` 식별자와 개행 문자는 선택 사항으로 처리한다.
        String regex = "(?s)^```(?:json)?\\n?(.*?)\\n?```$";

        // 패턴에 맞는 부분을 캡처된 그룹 1($1), 즉 순수 JSON 내용으로 교체한다.
        return trimmedResponse.replaceAll(regex, "$1");
    }

}