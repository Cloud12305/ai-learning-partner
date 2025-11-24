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
    private ProjectType type;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty;

    private Integer estimatedDays;

    private Integer currentLearners;

    private String coverImage;

    @ElementCollection
    private List<String> teachingVideos;

    @ElementCollection
    private List<ProjectLink> excellentProjects;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    public enum ProjectType {
        FRONTEND, BACKEND, FULLSTACK, MOBILE, DATABASE, DEVOPS
    }

    public enum DifficultyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED
    }
}



