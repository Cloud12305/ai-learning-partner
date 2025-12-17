package com.digitalhorsepower.learnbuddy.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "academic_records")
public class AcademicRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "course_id", nullable = false)
    private String courseId;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "semester")
    private Integer semester;

    @Column(name = "score")
    private Double score;

    @Column(name = "grade_point")
    private Double gradePoint;

    @Column(name = "credit_earned")
    private Double creditEarned;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 构造函数
    public AcademicRecord() {
    }

    // Getter 和 Setter 方法
    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(Double gradePoint) {
        this.gradePoint = gradePoint;
    }

    public Double getCreditEarned() {
        return creditEarned;
    }

    public void setCreditEarned(Double creditEarned) {
        this.creditEarned = creditEarned;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public String toString() {
        return "AcademicRecord{" +
                "recordId=" + recordId +
                ", studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", academicYear='" + academicYear + '\'' +
                ", semester=" + semester +
                ", score=" + score +
                ", gradePoint=" + gradePoint +
                ", creditEarned=" + creditEarned +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}