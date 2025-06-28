package com.kummit.api_server.domain;

import com.kummit.api_server.enums.CodingTier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.kummit.api_server.enums.PrimaryLanguage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** 플랫폼 회원(사용자) 엔티티 */
@Entity
@Table(name = "user")
@Getter
public class User {

    /* ---------- PK ---------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /* ---------- 기본 정보 ---------- */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    /* ---------- 실력/통계 ---------- */
    @Enumerated(EnumType.STRING)
    @Column(name = "coding_tier", nullable = false, length = 6)
    @Setter
    private CodingTier codingTier;

    @Column(name = "coding_level", nullable = false)
    @Setter
    private Byte codingLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_language", nullable = false, length = 12)
    @Setter
    private PrimaryLanguage primaryLanguage;

    @Column(nullable = false)
    private Integer rating = 0;

    @Column(name = "daily_streak", nullable = false)
    private Integer dailyStreak = 0;

    @Column(name = "last_challenge_date")
    private LocalDate lastChallengeDate;

    /* ---------- 메타 ---------- */
    @CreationTimestamp
    @Setter
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Setter
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* ---------- 연관 ---------- */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attempt> attempts = new ArrayList<>();

    /* ---------- 생성자 ---------- */
    protected User() { }

    public User(String username, String password,
                CodingTier codingTier, Byte codingLevel,
                PrimaryLanguage primaryLanguage) {

        this.username         = username;
        this.password         = password;
        this.codingTier = codingTier;
        this.codingLevel      = codingLevel;
        this.primaryLanguage  = primaryLanguage;
    }
}