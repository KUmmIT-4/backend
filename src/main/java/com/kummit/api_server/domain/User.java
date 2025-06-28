package com.kummit.api_server.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** 플랫폼 회원(사용자) 엔티티 */
@Entity
@Table(name = "user")
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

    @Column(nullable = false, unique = true, length = 255)
    private String email;

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

    /* ---------- 생성자 ---------- */
    protected User() { }

    public User(String username, String password, String email,
                CodingTier codingTier, Byte codingLevel,
                PrimaryLanguage primaryLanguage) {

        this.username         = username;
        this.password         = password;
        this.email            = email;
        this.codingTier       = codingTier;
        this.codingLevel      = codingLevel;
        this.primaryLanguage  = primaryLanguage;
    }

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

    /* ---------- Getter ---------- */
    public Long             getId()                { return id; }
    public String           getUsername()          { return username; }
    public String           getPassword()          { return password; }
    public String           getEmail()             { return email; }
    public CodingTier       getCodingTier()        { return codingTier; }
    public Byte             getCodingLevel()       { return codingLevel; }
    public PrimaryLanguage  getPrimaryLanguage()   { return primaryLanguage; }
    public Status           getStatus()            { return status; }
    public Integer          getRating()            { return rating; }
    public Integer          getDailyStreak()       { return dailyStreak; }
    public LocalDate        getLastChallengeDate() { return lastChallengeDate; }
    public LocalDateTime    getCreatedAt()         { return createdAt; }
    public LocalDateTime    getUpdatedAt()         { return updatedAt; }
    public List<Record>     getRecords()           { return records; }
}
