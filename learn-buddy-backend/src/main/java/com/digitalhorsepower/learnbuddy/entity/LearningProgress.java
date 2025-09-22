package com.digitalhorsepower.learnbuddy.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "learning_progress")
public class LearningProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private StudyPlan studyPlan;

    private String chapter;        // 章节名称
    private Double completionRate; // 完成率 0-100
    private Integer timeSpent;     // 学习时长（分钟）
    private String notes;          // 学习笔记

    private LocalDateTime studyDate; // 学习日期

    @Column(updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (studyDate == null) {
            studyDate = LocalDateTime.now();
        }
    }
}