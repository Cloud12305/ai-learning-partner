package com.digitalhorsepower.learnbuddy.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    private String major;
    private String grade;

    @Column(name = "learning_goal")
    private String learningGoal;

    private String college;

    @Column(name = "class_name")
    private String className;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private String gender;

    private String phone;

    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate;

    @Column(name = "graduation_date")
    private LocalDateTime graduationDate;

    @Column(name = "account_status")
    private String accountStatus = "ACTIVE";

    @Column(name = "login_count")
    private Integer loginCount = 0;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (loginCount == null) {
            loginCount = 0;
        }
        if (accountStatus == null) {
            accountStatus = "ACTIVE";
        }
        // 如果 studentId 为空，根据用户名生成一个临时的
        if (studentId == null || studentId.trim().isEmpty()) {
            studentId = "TEMP_" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}