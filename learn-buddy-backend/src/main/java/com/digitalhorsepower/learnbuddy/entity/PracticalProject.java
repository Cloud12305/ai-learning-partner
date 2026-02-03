package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "practical_projects")
@Data
public class PracticalProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String detailedContent;

    @Column(columnDefinition = "TEXT")
    private String tutorial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficulty;

    private Integer estimatedDays;

    @Column(columnDefinition = "int default 0")
    private Integer currentLearners;

    @Column(length = 500)
    private String coverImage;

    // 使用 @ElementCollection 存储教学视频
    @ElementCollection
    @CollectionTable(
            name = "practical_project_teaching_videos",
            joinColumns = @JoinColumn(name = "practical_project_id")
    )
    @Column(name = "teaching_videos")
    private List<String> teachingVideos;

    // 使用 @ElementCollection 存储优秀项目
    @ElementCollection
    @CollectionTable(
            name = "practical_project_excellent_projects",
            joinColumns = @JoinColumn(name = "practical_project_id")
    )
    private List<ProjectLink> excellentProjects;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.updateTime = now;
        this.createdAt = now;
        this.updatedAt = now;
        if (this.currentLearners == null) {
            this.currentLearners = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public enum ProjectType {
        FRONTEND, BACKEND, FULLSTACK, MOBILE, DATABASE, DEVOPS
    }

    public enum DifficultyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED
    }

    // 内嵌类用于存储优秀项目链接
    @Embeddable
    @Data
    public static class ProjectLink {
        private String name;
        private String url;
        private String description;
    }
}