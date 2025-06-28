package com.kummit.api_server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kummit.api_server.domain.User;
import com.kummit.api_server.domain.User.CodingTier;
import com.kummit.api_server.domain.User.PrimaryLanguage;
import com.kummit.api_server.dto.UserRegisterRequest;
import com.kummit.api_server.dto.UserInfoResponse;
import com.kummit.api_server.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Long register(UserRegisterRequest req) {
        CodingTier tier = CodingTier.valueOf(req.getCodingTier());
        PrimaryLanguage lang = PrimaryLanguage.valueOf(req.getPrimaryLanguage());
        // Integer to Byte conversion for codingLevel
        Byte level = req.getCodingLevel() == null ? null : req.getCodingLevel().byteValue();

        //User user = new User(req.getUsername(),req.getPassword(),req.getCodingTier(),req.getCodingLevel(),req.getPrimaryLanguage(),);
        return userRepository.save(user).getId();
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
            user.getStatus().name(),
            user.getRating(),
            user.getDailyStreak(),
            user.getLastChallengeDate(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
