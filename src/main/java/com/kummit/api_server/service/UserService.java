package com.kummit.api_server.service;

import com.kummit.api_server.enums.PrimaryLanguage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kummit.api_server.domain.User;
import com.kummit.api_server.enums.CodingTier;
import com.kummit.api_server.dto.response.UserInfoResponse;
import com.kummit.api_server.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public UserInfoResponse getUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        return new UserInfoResponse(
            user.getId(),
            user.getUsername(),
            user.getCodingTier().name(),
            user.getPrimaryLanguage().name(),
            user.getRating(),
            user.getDailyStreak(),
            user.getLastChallengeDate(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
