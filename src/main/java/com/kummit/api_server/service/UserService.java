package com.kummit.api_server.service;

import com.kummit.api_server.domain.Attempt;
import com.kummit.api_server.dto.request.UserUpdateRequest;
import com.kummit.api_server.dto.response.TodayAttemptListResponse;
import com.kummit.api_server.dto.response.AttemptSummaryResponse;
import com.kummit.api_server.dto.response.LeaderboardListResponse;
import com.kummit.api_server.dto.response.LeaderboardResponse;
import com.kummit.api_server.enums.PrimaryLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kummit.api_server.domain.User;
import com.kummit.api_server.enums.CodingTier;
import com.kummit.api_server.repository.UserRepository;
import com.kummit.api_server.repository.AttemptRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AttemptRepository attemptRepository;

    public UserService(UserRepository userRepository, AttemptRepository attemptRepository) {
        this.userRepository = userRepository;
        this.attemptRepository = attemptRepository;
    }

    @Transactional
    public User register(String username,
                         String password,
                         CodingTier codingTier,
                         byte codingLevel,
                         PrimaryLanguage language) {

        userRepository.findByUsername(username).ifPresent(user -> {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        });

        User user = new User(username, password, codingTier, codingLevel, language); // user 객체 생성
        user.setCreatedAt(LocalDateTime.now()); // 가입 시각 설정

        return userRepository.save(user); // DB에 사용자 저장
    }

    @Transactional
    public User login(String username, String password){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        if(!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("로그인 정보가 맞지 않습니다.");
        }
        return user;
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

    }

    public User updateUserInfo(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        CodingTier codingTier = CodingTier.valueOf(request.tier().toUpperCase());
        user.setCodingTier(codingTier);

        user.setCodingLevel(request.level());

        user.setPrimaryLanguage(PrimaryLanguage.valueOf(request.language().toUpperCase()));

        return userRepository.save(user);
    }

    public TodayAttemptListResponse getAttemptsByDate(Long userId, LocalDate date, int pageNo, int perPage) {
        Pageable pageable = PageRequest.of(pageNo - 1, perPage);
        Page<Attempt> page = attemptRepository.findByUserIdAndCreatedAtBetween(
                userId,
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay(),
                pageable
        );

        List<AttemptSummaryResponse> results = page.getContent().stream()
                .map(a -> new AttemptSummaryResponse(
                        a.getId(),
                        a.getProblem().getId(),
                        a.getProblem().getTitle(),
                        a.getStatus().name().toLowerCase(),
                        a.getAttemptLanguage().toString(),
                        a.getProblem().getProblemTier().name(),
                        a.getProblem().getProblemLevel()
                ))
                .toList();

        return new TodayAttemptListResponse(results, page.hasNext());
    }

    public LeaderboardListResponse getLeaderboard(int pageNo, int perPage) {
        Pageable pageable = PageRequest.of(pageNo - 1, perPage);
        Page<User> page = userRepository.findAllByOrderByRatingDesc(pageable);

        List<LeaderboardResponse> users = page.getContent().stream()
                .map(user -> new LeaderboardResponse(
                        user.getUsername(),
                        user.getRating()
                ))
                .toList();

        return new LeaderboardListResponse(users, page.hasNext());
    }

}