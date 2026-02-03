package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_quiz_records")
@Data
public class UserQuizRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "quiz_type", length = 50)
    private String quizType;

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestions;

    @Column(name = "correct_answers", nullable = false)
    private Integer correctAnswers;

    @Column(nullable = false, precision = 5, scale = 2)
    private Double score;

    @Column(name = "time_spent")
    private Integer timeSpent;

    @Column(name = "quiz_date")
    private LocalDateTime quizDate;

    @Column(columnDefinition = "json")
    private String details;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createTime == null) {
            this.createTime = now;
        }
        if (this.quizDate == null) {
            this.quizDate = now;
        }
    }
}