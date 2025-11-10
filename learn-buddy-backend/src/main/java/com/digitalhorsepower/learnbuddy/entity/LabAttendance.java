package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "lab_attendance")
public class LabAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private User student;

    @Column(name = "lab_id", nullable = false, length = 30)
    private String labId;

    @Column(name = "lab_name", nullable = false, length = 100)
    private String labName;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "experiment_name", length = 100)
    private String experimentName;

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_type")
    private AttendanceType attendanceType;

    @Column(name = "supervisor", length = 50)
    private String supervisor;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum AttendanceType {
        course, self_study, competition
    }
}