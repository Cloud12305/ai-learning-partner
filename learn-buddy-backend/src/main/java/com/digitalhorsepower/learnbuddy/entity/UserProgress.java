package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_project_progress")
@Data
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "completed_modules", columnDefinition = "int default 0")
    private Integer completedModules;

    @Column(name = "total_modules", nullable = false)
    private Integer totalModules;

    @Column(name = "progress_percentage", precision = 5, scale = 2)
    private Double progressPercentage;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "expected_completion_date")
    private LocalDateTime expectedCompletionDate;

    @Column(name = "submission_count", columnDefinition = "int default 0")
    private Integer submissionCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressStatus status;

    @Column(name = "last_activity_time")
    private LocalDateTime lastActivityTime;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    // 关联项目信息（非数据库字段）
    @Transient
    private PracticalProject project;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.updateTime = now;
        this.lastActivityTime = now;

        if (this.completedModules == null) {
            this.completedModules = 0;
        }
        if (this.submissionCount == null) {
            this.submissionCount = 0;
        }
        if (this.status == null) {
            this.status = ProgressStatus.NOT_STARTED;
        }
        if (this.progressPercentage == null) {
            this.progressPercentage = 0.0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
        this.lastActivityTime = LocalDateTime.now();

        // 自动计算进度百分比
        if (this.totalModules != null && this.totalModules > 0) {
            this.progressPercentage = (this.completedModules.doubleValue() / this.totalModules.doubleValue()) * 100;
        }
    }

    public enum ProgressStatus {
        NOT_STARTED, IN_PROGRESS, COMPLETED, PAUSED
    }
}