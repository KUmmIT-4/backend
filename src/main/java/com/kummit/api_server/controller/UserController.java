package com.kummit.api_server.controller;

import com.kummit.api_server.SessionStore;
import com.kummit.api_server.domain.User;
import com.kummit.api_server.dto.request.AttemptQueryRequest;
import com.kummit.api_server.dto.request.UserLoginRequest;
import com.kummit.api_server.dto.request.UserUpdateRequest;
import com.kummit.api_server.dto.response.*;
import com.kummit.api_server.enums.CodingTier;
import com.kummit.api_server.enums.PrimaryLanguage;
import com.kummit.api_server.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.kummit.api_server.dto.request.UserRegisterRequest;
import com.kummit.api_server.service.UserService;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SessionStore sessionStore;
    private final UserRepository userRepository;

    public UserController(UserService userService, SessionStore sessionStore, UserRepository userRepository) {
        this.userService = userService;
        this.sessionStore = sessionStore;
        this.userRepository = userRepository;
    }

    @PostMapping("/register") // 회원 가입
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegisterRequest request) {
        try {
            CodingTier codingTier = CodingTier.valueOf(request.tier().toUpperCase());
            byte codingLevel = request.level();
            PrimaryLanguage language = PrimaryLanguage.valueOf(request.language().toUpperCase());

            User user = userService.register( // 회원 등록
                    request.username(),
                    request.password(),
                    codingTier,
                    codingLevel,
                    language
            );

            return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername()));

        } catch (IllegalArgumentException e) {
            // 중복 아이디 등의 예외 처리
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new Object() {
                        public final String error = e.getMessage();
                    }
            );
        }
    }

    @PostMapping("/login") // 로그인
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request,
                                   HttpServletResponse response) {
        try {
            User user = userService.login(request.username(), request.password());

            String sessionId = UUID.randomUUID().toString();
            sessionStore.put(sessionId, user.getId());

            ResponseCookie cookie = ResponseCookie.from("session_id", sessionId)
                    .path("/")
                    .httpOnly(true)
                    .sameSite("Lax")
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());

            return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(
                    new Object() {
                        public final String error = "아이디 또는 비밀번호가 올바르지 않습니다.";
                    }
            );
        }
    }

    @PostMapping("/logout") // 로그아웃
    public ResponseEntity<?> logout(@CookieValue(value = "session_id", required = false) String sessionId,
                                    HttpServletResponse response) {

        // 세션 저장소에서 제거
        if (sessionId != null) {
            sessionStore.remove(sessionId);
        }

        // 쿠키 만료 (브라우저에서 삭제되도록)
        ResponseCookie expiredCookie = ResponseCookie.from("session_id", "")
                .path("/")
                .maxAge(0)         // 즉시 만료
                .httpOnly(true)
                .build();

        response.addHeader("Set-Cookie", expiredCookie.toString());

        return ResponseEntity.ok(
                new Object() {
                    public final String message = "Logged out successfully";
                }
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@CookieValue(value = "session_id", required = false) String sessionId) {
        if (sessionId == null || !sessionStore.exists(sessionId)) {
            return ResponseEntity.status(401).body("쿠키에 사용자 정보가 없습니다.");
        }

        Long userId = sessionStore.getUserId(sessionId);
        User user = userService.getUser(userId);

        if (user == null) {
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
        }

        UserInfoResponse response = new UserInfoResponse(
                user.getId(),
                user.getUsername(),
                user.getCodingTier().name(),
                user.getCodingLevel(),
                user.getPrimaryLanguage().name(),
                user.getRating(),
                user.getDailyStreak()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateUserInfo( // 사용자 정보 수정
                                             @RequestBody UserUpdateRequest request,
                                             @CookieValue(value = "session_id", required = false) String sessionId
    ) {
        if (sessionId == null || !sessionStore.exists(sessionId)) {
            return ResponseEntity.status(401).body("쿠키에 사용자 정보가 없습니다.");
        }

        Long userId = sessionStore.getUserId(sessionId);

        try {
            User user = userService.getUser(userId);
            User updatedUser = userService.updateUserInfo(user.getId(), request);

            return ResponseEntity.ok(
                    new UserUpdateResponse(
                            updatedUser.getId(),
                            updatedUser.getCodingTier().name(),
                            updatedUser.getCodingLevel(),
                            updatedUser.getPrimaryLanguage().name()
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/attempts/me")
    public ResponseEntity<?> getAttemptsByDate(
            @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestBody AttemptQueryRequest request,
            @CookieValue(value = "session_id", required = false) String sessionId
    ) {
        if (sessionId == null || !sessionStore.exists(sessionId)) {
            return ResponseEntity.status(401).body("쿠키에 사용자 정보가 없습니다.");
        }

        Long userId = sessionStore.getUserId(sessionId);

        try {
            TodayAttemptListResponse response = userService.getAttemptsByDate(
                    userId,
                    date.toLocalDate(),  // 날짜만 사용
                    request.pageNo(),
                    request.perPage()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("날짜 형식이 잘못되었습니다. (예: 2025-06-28T00:00:00)");
        }
    }

    @PostMapping("/leaderboard")
    public ResponseEntity<?> getLeaderboard(@RequestBody AttemptQueryRequest request) {
        LeaderboardListResponse response = userService.getLeaderboard(request.pageNo(), request.perPage());
        return ResponseEntity.ok(response);
    }
}