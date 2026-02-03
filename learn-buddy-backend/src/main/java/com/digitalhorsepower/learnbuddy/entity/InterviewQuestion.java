package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_questions")
@Data
public class InterviewQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "option_a", columnDefinition = "TEXT")
    private String optionA;

    @Column(name = "option_b", columnDefinition = "TEXT")
    private String optionB;

    @Column(name = "option_c", columnDefinition = "TEXT")
    private String optionC;

    @Column(name = "option_d", columnDefinition = "TEXT")
    private String optionD;

    @Column(name = "correct_answer", nullable = false, length = 10)
    private String correctAnswer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgrammingLanguage language;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "difficulty_level", columnDefinition = "int default 1")
    private Integer difficultyLevel;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.updateTime = now;
        if (this.difficultyLevel == null) {
            this.difficultyLevel = 1;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }

    public enum QuestionType {
        SINGLE_CHOICE, MULTIPLE_CHOICE, FILL_BLANK, CODE
    }

    public enum QuestionCategory {
        FRONTEND, BACKEND, DATABASE, ALGORITHM, SYSTEM_DESIGN, OOP, NETWORK, SECURITY
    }

    public enum ProgrammingLanguage {
        JAVA, JAVASCRIPT, PYTHON, CPP, CSHARP, PHP, GO, RUST, SWIFT, KOTLIN
    }
}