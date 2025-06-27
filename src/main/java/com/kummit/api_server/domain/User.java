package com.kummit.api_server.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "coding_tier", nullable = false)
    private CodingTier codingTier;

    @Column(name = "coding_level", nullable = false)
    private Byte codingLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_language", nullable = false)
    private PrimaryLanguage primaryLanguage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @Column(nullable = false)
    private Integer rating = 0;

    @Column(name = "daily_streak", nullable = false)
    private Integer dailyStreak = 0;

    @Column(name = "last_challenge_date")
    private LocalDate lastChallengeDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records = new ArrayList<>();

    protected User() { }

    public User(String username, String password, String email,
                CodingTier codingTier, Byte codingLevel,
                PrimaryLanguage primaryLanguage) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.codingTier = codingTier;
        this.codingLevel = codingLevel;
        this.primaryLanguage = primaryLanguage;
    }

    @PreUpdate
    public void touchUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- enums ---
    public enum CodingTier { BRONZE, SILVER, GOLD, EXPERT }
    public enum PrimaryLanguage { C, C_PLUS_PLUS, Python, Java, JavaScript }
    public enum Status { ACTIVE, DELETED, SUSPENDED }

    // --- getters ---
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public CodingTier getCodingTier() { return codingTier; }
    public Byte getCodingLevel() { return codingLevel; }
    public PrimaryLanguage getPrimaryLanguage() { return primaryLanguage; }
    public Status getStatus() { return status; }
    public Integer getRating() { return rating; }
    public Integer getDailyStreak() { return dailyStreak; }
    public LocalDate getLastChallengeDate() { return lastChallengeDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<Record> getRecords() { return records; }
}