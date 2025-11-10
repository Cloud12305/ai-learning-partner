package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "learning_progress")
public class LearningProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Long progressId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private StudyPlan plan;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "chapter", nullable = false, length = 100)
    private String chapter;

    @Column(name = "completion_rate", precision = 5, scale = 2)
    private BigDecimal completionRate = BigDecimal.ZERO;

    @Column(name = "time_spent")
    private Integer timeSpent = 0;

    @Column(name = "study_date", nullable = false)
    private LocalDate studyDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}