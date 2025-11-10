package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.AcademicRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicRecordRepository extends JpaRepository<AcademicRecord, Long> {

    // 根据学生ID查询所有成绩记录
    List<AcademicRecord> findByStudentId(String studentId);

    // 根据学生ID和学年查询成绩
    List<AcademicRecord> findByStudentIdAndAcademicYear(String studentId, String academicYear);

    // 计算学生平均绩点
    @Query("SELECT AVG(a.gradePoint) FROM AcademicRecord a WHERE a.studentId = :studentId")
    Double calculateAverageGPA(@Param("studentId") String studentId);

    // 计算学生总学分
    @Query("SELECT SUM(a.creditEarned) FROM AcademicRecord a WHERE a.studentId = :studentId")
    Double calculateTotalCredits(@Param("studentId") String studentId);

    // 按学期统计GPA趋势
    @Query("SELECT a.academicYear, a.semester, AVG(a.gradePoint) as gpa " +
            "FROM AcademicRecord a WHERE a.studentId = :studentId " +
            "GROUP BY a.academicYear, a.semester " +
            "ORDER BY a.academicYear, a.semester")
    List<Object[]> findGPATrendByStudent(@Param("studentId") String studentId);
}