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
    private String name;

    @Column(nullable = false)
    private String password;

    private String gender;

    @Column(nullable = false)
    private String major;

    @Column(nullable = false)
    private String college;

    @Column(nullable = false)
    private String grade;

    @Column(name = "class_name")
    private String className;

    @Column(unique = true)
    private String email;

    private String phone;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate;

    @Column(name = "graduation_date")
    private LocalDateTime graduationDate;

    @Column(name = "learning_goal")
    private String learningGoal;

    @Column(name = "learning_preferences")
    private String learningPreferences;

    @Column(name = "account_status")
    private String accountStatus = "ACTIVE";

    @Column(name = "login_count")
    private Integer loginCount = 0;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.createTime = now;
        this.updateTime = now;

        if (this.loginCount == null) {
            this.loginCount = 0;
        }
        if (this.accountStatus == null) {
            this.accountStatus = "ACTIVE";
        }
        if (this.avatarUrl == null) {
            this.avatarUrl = "https://picsum.photos/100/100?random=" + (int)(Math.random() * 100);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}