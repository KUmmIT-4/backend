package com.kummit.api_server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kummit.api_server.SessionStore;
import com.kummit.api_server.domain.Attempt;
import com.kummit.api_server.domain.BojProblemInfo;
import com.kummit.api_server.domain.Problem;
import com.kummit.api_server.dto.*;
import com.kummit.api_server.enums.PrimaryLanguage;
import com.kummit.api_server.enums.ProblemTier;
import com.kummit.api_server.enums.Status;
import com.kummit.api_server.repository.AttemptRepository;
import com.kummit.api_server.repository.ProblemRepository;
import com.kummit.api_server.repository.UserRepository;
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

    private final SessionStore sessionStore;

    final UserRepository userRepository;
    final ProblemRepository problemRepository;
    final AttemptRepository attemptRepository;


    String queryTemplate1 = """
(역할)
당신은 Baekjoon 사이트(www.acmicpc.net) 문제 번호를 기반으로 정보를 추출해 **JSON 객체만** 반환하는 시스템입니다.

(문제 번호)
{problem_id}

(목표)
아래 JSON 스키마에 맞춰 문제 정보를 추출하여 출력하세요.

(제약)
- 문제는 인터넷에서 찾으세요
- 응답은 JSON 객체만 출력하세요. **추가 설명 금지.**
- Latex 문법 사용 금지. 수식은 **유니코드**로 작성하세요.
- 각 항목은 Baekjoon HTML 구조의 다음 요소에서 추출하세요:
(JSON 스키마)
{
  "problem_title": "string",
  "problem_explanation": "string",
  "input_format": "string",
  "output_format": "string",
  "input_example": "string",
  "output_example": "string"
}
""";


    String queryTemplate2 = """
(역할)
당신은 Baekjoon 사이트(www.acmicpc.net)의 문제를 풀고,해답코드를 작성한 뒤,  해답 코드를 활용해, **주어진 문제의 알고리즘적 복잡성과 핵심 로직의 개수를 고려하여 최대 4개의 독립적인 객관식 퀴즈**를 JSON 형식으로 생성하는 시스템입니다.
**응답을 JSON 배열 객체로만 출력**하세요.

(문제 번호)
{problem_id}

(코드 풀이 언어)
{language}

(목표)
주어진 문제 번호의 정답 코드 중 하나를 찾아 핵심 로직 중 **한 줄 이내의 코드**를 `[_____]`로 대체하세요.
하나의 문제만을 만들며 하나의 빈칸에 대한 퀴즈를 만듭니다.
퀴즈는 해당 빈칸이 포함된 코드 스니펫과 해당 빈칸에 대한 4개의 선지, 그리고 정답으로 구성됩니다.
**퀴즈는 코드의 논리적 흐름과 알고리즘의 주요 단계**를 대표해야 합니다.

그 후, 아래 JSON 스키마에 완벽히 맞춰서, **응답을 JSON 배열 객체로만 출력**하세요.

(제약)
- 문제는 인터넷에서 찾으세요.
- 답변은 무조건 JSON 형식만을 답변해야 합니다.
- 퀴즈 객체는 `question_number`, `code`, `quiz_text`, `choices`, `answer_choice`, `hint`, `rationale` 필드를 포함해야 합니다.
-code와 모든 문자열 값 내 줄바꿈은 \\n으로 처리해야 합니다
- `quiz_text`는 빈칸이 포함된 코드 스니펫의 목적이나 해당 로직의 의미를 설명하는 질문이어야 합니다. 수식이 존재할 경우, LaTeX으로 절대 표현하지 말고, 유니코드를 사용해야 합니다.
- 각 퀴즈는 4개의 선지(`choices`)를 제공하며, 정답 1개와 매력적인 오답 3개로 구성해야 합니다.
- 각 선지는 번호를 붙이지 말고, `choices` 배열에 문자열로 하나씩 담아야 합니다.
- `answer_choice`의 값은 `choices` 배열에 있는 정답 배열의 인덱스와 정확히 일치해야합니다. (인덱스는 0부터 시작합니다.)
- `rationale`은 정답과 오답 선지에 대한 간략한 해설을 포함해야 합니다.

```json
[
  {
    "code": "코드 스니펫을 줄바꿈 포함하여 문자열로 표현",
    "quiz_text": "이 빈칸은 무엇을 하는 코드일까요?",
    "choices": ["선지1", "선지2", "선지3", "선지4"],
    "answer_choice": "정답 인덱스 번호",
    "rationale": ["1번 선지에 대한 해설", "2번 선지에 대한 해설", "3번 선지에 대한 해설", "4번 선지에 대한 해설"] 
  },
]
``` 
""";

    /** POST /api/attempts */
    /* 시간 남으면 비동기로 할지 생각해보자 */
    @PostMapping
    public ProblemResponseDto attempt (@Valid @RequestBody AttemptRequest req) throws Exception{
//        System.out.println(req.tier());
        // 클라이언트가 요청한 티어와 레벨 (ex: 골드 4)
        BojProblemInfo bojProblemInfo = problemService.pickProblem(req.tier(), req.level());

        int problemNumber = bojProblemInfo.getProblemNum();
        System.out.println("problemNumber = " + problemNumber);
        String query1 = queryTemplate1
                .replace("{problem_id}", Integer.toString(problemNumber));
        String query2 = queryTemplate2
                .replace("{problem_id}", Integer.toString(problemNumber))
                .replace("{language}",req.language());

        List<String> prompts = List.of(query1,query2);

        prompts.forEach(System.out::println);
        List<String> answers = parallelChat(prompts);
        List<String> cleanedAnswers = answers.stream()
                .map(s -> stripJsonCodeFence(s)) // 메소드 참조(Method Reference)로 더 간결하게 표현 가능
                .collect(Collectors.toList());
        cleanedAnswers.forEach(System.out::println);

        ObjectMapper objectMapper = new ObjectMapper();
        FirstStringParseDto problemDetail = objectMapper.readValue(cleanedAnswers.get(0), FirstStringParseDto.class);
        List<SecondStringParseDto> quizzes = objectMapper.readValue(cleanedAnswers.get(1), new TypeReference<List<SecondStringParseDto>>() {});

        if (quizzes == null || quizzes.isEmpty()) {
            throw new IllegalStateException("Quiz data is missing.");
        }
        SecondStringParseDto targetQuiz = quizzes.get(0);

        ProblemResponseDto responseBody = new ProblemResponseDto(
                (long) problemNumber,
                req.tier().getDisplayName(),
                String.valueOf(req.level()),
                req.language(),
                problemDetail.problemTitle(),            // s1에서 추출
                problemDetail.problemExplanation(),      // s1에서 추출
                targetQuiz.quizText(),                   // s2[0]에서 추출
                targetQuiz.code(),                       // s2[0]에서 추출
                targetQuiz.choices(),                    // s2[0]에서 추출
                targetQuiz.answerChoice(),               // s2[0]에서 추출
                targetQuiz.rationale()                   // s2[0]에서 추출
        );

        System.out.println(String.valueOf(targetQuiz.choices()));
        String choicesJson = objectMapper.writeValueAsString(targetQuiz.choices());
        String rationaleJson = objectMapper.writeValueAsString(targetQuiz.rationale());
        Problem p = Problem.builder()
                .problemNum(problemNumber)
                .title(problemDetail.problemTitle())
                .explanation(problemDetail.problemExplanation())
                .inputFormat(problemDetail.inputFormat())
                .outputFormat(problemDetail.outputFormat())
                .inputExample(problemDetail.inputExample())
                .outputExample(problemDetail.outputExample())
                .code(targetQuiz.code())
                .choices(choicesJson)
                .answerChoice(String.valueOf(targetQuiz.answerChoice()))
                .rationale(rationaleJson)
                .quizText(targetQuiz.quizText())
                .problemTier(ProblemTier.valueOf(req.tier().getDisplayName()))
                .problemLevel(req.level()).build();

        problemRepository.save(p);

        Attempt a = Attempt.builder()
                .user(userRepository.findById((long) req.userId()).get())
                .problem(p)
                .status(Status.ATTEMPTING)
                .attemptLanguage(PrimaryLanguage.valueOf(req.language()))
                .build();

        attemptRepository.save(a);

        return responseBody;

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