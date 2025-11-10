package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "study_plans")
public class StudyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "plan_name", nullable = false, length = 100)
    private String planName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "target_score", precision = 5, scale = 2)
    private BigDecimal targetScore;

    @Column(name = "study_hours_per_week")
    private Integer studyHoursPerWeek;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PlanStatus status = PlanStatus.PLANNING;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "course_name", length = 255)
    private String courseName;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum PlanStatus {
        PLANNING, IN_PROGRESS, COMPLETED, CANCELLED
    }
}