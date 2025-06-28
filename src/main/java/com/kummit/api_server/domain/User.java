package com.kummit.api_server.domain;

import com.kummit.api_server.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** 회원(계정) 도메인 엔티티 */
@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class User {

    /* ---------- PK ---------- */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /* ---------- 계정 ---------- */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /* ---------- 프로필 ---------- */
    @Enumerated(EnumType.STRING)
    @Column(name = "coding_tier", nullable = false, length = 6)
    private CodingTier codingTier;

    @Column(name = "coding_level", nullable = false)
    private Byte codingLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_language", nullable = false, length = 12)
    private PrimaryLanguage primaryLanguage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(nullable = false)
    private Integer rating = 0;

    @Column(name = "daily_streak", nullable = false)
    private Integer dailyStreak = 0;

    @Column(name = "last_challenge_date")
    private LocalDate lastChallengeDate;

    /* ---------- 메타 ---------- */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* ---------- 연관 관계 ---------- */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attempt> attempts = new ArrayList<>();

    /* ---------- 생성자(비즈니스용) ---------- */
    public User(String username, String password, String email,
                CodingTier codingTier, Byte codingLevel,
                PrimaryLanguage primaryLanguage) {
        this.username        = username;
        this.password        = password;
        this.email           = email;
        this.codingTier      = codingTier;
        this.codingLevel     = codingLevel;
        this.primaryLanguage = primaryLanguage;
    }
}
