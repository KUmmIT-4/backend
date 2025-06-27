package com.kummit.api_server.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 유저 정보 조회 응답용 DTO
 */
public class UserInfoResponse {

    private Long id;
    private String username;
    private String codingTier;
    private String primaryLanguage;
    private String status;
    private Integer rating;
    private Integer dailyStreak;
    private LocalDate lastChallengeDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserInfoResponse(Long id,
                            String username,
                            String codingTier,
                            String primaryLanguage,
                            String status,
                            Integer rating,
                            Integer dailyStreak,
                            LocalDate lastChallengeDate,
                            LocalDateTime createdAt,
                            LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.codingTier = codingTier;
        this.primaryLanguage = primaryLanguage;
        this.status = status;
        this.rating = rating;
        this.dailyStreak = dailyStreak;
        this.lastChallengeDate = lastChallengeDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getCodingTier() {
        return codingTier;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public String getStatus() {
        return status;
    }

    public Integer getRating() {
        return rating;
    }

    public Integer getDailyStreak() {
        return dailyStreak;
    }

    public LocalDate getLastChallengeDate() {
        return lastChallengeDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
