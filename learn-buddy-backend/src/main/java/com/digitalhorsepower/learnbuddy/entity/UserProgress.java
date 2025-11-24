// src/main/java/com/digitalhorsepower/learnbuddy/entity/UserProgress.java
package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_progress")
@Data
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long projectId;

    private Integer completedModules;

    private Integer totalModules;

    private Double progressPercentage;

    private LocalDateTime startDate;

    private LocalDateTime expectedCompletionDate;

    private Integer submissionCount;

    @Enumerated(EnumType.STRING)
    private ProgressStatus status;

    public enum ProgressStatus {
        NOT_STARTED, IN_PROGRESS, COMPLETED, PAUSED
    }
}