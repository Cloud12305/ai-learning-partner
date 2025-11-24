package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_answer_details")
@Data
public class UserAnswerDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quiz_record_id", nullable = false)
    private Long quizRecordId;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "user_answer", length = 500)
    private String userAnswer;

    @Column(name = "is_correct", columnDefinition = "tinyint(1) default 0")
    private Boolean isCorrect;

    @Column(name = "time_spent")
    private Integer timeSpent;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    // 关联题目信息（非数据库字段）
    @Transient
    private InterviewQuestion question;

    @PrePersist
    protected void onCreate() {
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now();
        }
        if (this.isCorrect == null) {
            this.isCorrect = false;
        }
    }
}