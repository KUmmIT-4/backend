package com.kummit.api_server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** 플랫폼 회원(사용자) 엔티티 */
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
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
    private CodingTier codingTier;

    @Column(name = "coding_level", nullable = false)
    private Byte codingLevel;      // TINYINT(0 ~ 255)

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_language", nullable = false, length = 12)
    private PrimaryLanguage primaryLanguage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 9)
    private Status status = Status.ACTIVE;

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

    /* ---------- 연관 ---------- */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records = new ArrayList<>();


    /* ---------- ENUM ---------- */

    public enum CodingTier { BRONZE, SILVER, GOLD, EXPERT }

    public enum PrimaryLanguage {
        C("C"),
        CPP("C++"),          // DB 값 "C++"
        PYTHON("Python"),
        JAVA("Java"),
        JAVASCRIPT("JavaScript");

        private final String dbValue;
        PrimaryLanguage(String dbValue) { this.dbValue = dbValue; }
        @Override public String toString() { return dbValue; }   // Enum → DB 문자열
    }

    public enum Status { ACTIVE, DELETED, SUSPENDED }

}
