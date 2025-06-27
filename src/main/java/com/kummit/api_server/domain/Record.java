package com.kummit.api_server.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "record")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "user_choice")
    private Byte userChoice;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    protected Record() { }

    public Record(User user,
                  Problem problem,
                  Status status,
                  LocalDateTime submittedAt,
                  Byte userChoice) {
        this.user = user;
        this.problem = problem;
        this.status = status;
        this.submittedAt = submittedAt;
        this.userChoice = userChoice;
    }

    @PreUpdate
    public void touchUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum Status { solved, attempting, abandoned }

    // --- getters ---
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Problem getProblem() {
        return problem;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public Byte getUserChoice() {
        return userChoice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
