// src/main/java/com/digitalhorsepower/learnbuddy/entity/InterviewQuestion.java
package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "interview_questions")
@Data
public class InterviewQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String optionA;

    @Column(columnDefinition = "TEXT")
    private String optionB;

    @Column(columnDefinition = "TEXT")
    private String optionC;

    @Column(columnDefinition = "TEXT")
    private String optionD;

    @Column(nullable = false)
    private String correctAnswer;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Enumerated(EnumType.STRING)
    private QuestionCategory category;

    @Enumerated(EnumType.STRING)
    private ProgrammingLanguage language;

    private String explanation;

    private Integer difficultyLevel;

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
