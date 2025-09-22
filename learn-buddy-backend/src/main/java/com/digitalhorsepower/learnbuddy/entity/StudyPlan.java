package com.digitalhorsepower.learnbuddy.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "study_plans")
public class StudyPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String planName;       // 计划名称

    private String description;    // 计划描述

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;             // 关联用户

    private String courseName;     // 课程名称
    private LocalDate startDate;   // 开始日期
    private LocalDate endDate;     // 结束日期
    private String status;         // 状态：进行中/已完成/已过期

    @Column(updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (status == null) {
            status = "进行中";
        }
    }
}
