package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "academic_records")
public class AcademicRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "academic_year", nullable = false, length = 9)
    private String academicYear;

    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "grade_point", precision = 3, scale = 2)
    private BigDecimal gradePoint;

    @Column(name = "grade_level", length = 10)
    private String gradeLevel;

    @Column(name = "class_rank")
    private Integer classRank;

    @Column(name = "major_rank")
    private Integer majorRank;

    @Column(name = "exam_date")
    private LocalDate examDate;

    @Column(name = "credit_earned", precision = 3, scale = 1)
    private BigDecimal creditEarned;

    @Column(name = "is_retake")
    private Boolean isRetake = false;

    @Column(name = "teacher_evaluation", columnDefinition = "TEXT")
    private String teacherEvaluation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}