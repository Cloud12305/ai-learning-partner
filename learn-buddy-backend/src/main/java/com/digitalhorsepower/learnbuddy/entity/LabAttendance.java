package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lab_attendance")
@Data
public class LabAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "lab_id", nullable = false)
    private String labId;

    @Column(name = "lab_name", nullable = false)
    private String labName;

    @Column(name = "experiment_name")
    private String experimentName;

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}