package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.AcademicRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicRecordRepository extends JpaRepository<AcademicRecord, Long> {

    // 根据学生ID查找学业记录
    List<AcademicRecord> findByUserStudentId(String studentId);

    // 根据学生ID和课程ID查找学业记录
    List<AcademicRecord> findByUserStudentIdAndCourseId(String studentId, String courseId);

    // 根据学生ID和学年查找学业记录
    List<AcademicRecord> findByUserStudentIdAndAcademicYear(String studentId, String academicYear);

    // 根据学生ID、学年和学期查找学业记录
    List<AcademicRecord> findByUserStudentIdAndAcademicYearAndSemester(String studentId, String academicYear, Integer semester);

    // 根据课程ID查找学业记录
    List<AcademicRecord> findByCourseId(String courseId);

    // 检查是否存在相同的学业记录（学生+课程+学年+学期）
    boolean existsByUserStudentIdAndCourseIdAndAcademicYearAndSemester(
            String studentId, String courseId, String academicYear, Integer semester);

    // 获取学生的平均绩点
    @Query("SELECT AVG(a.gradePoint) FROM AcademicRecord a WHERE a.user.studentId = :studentId AND a.gradePoint IS NOT NULL")
    Optional<Double> findAverageGradePointByStudentId(@Param("studentId") String studentId);

    // 获取学生已获得的总学分
    @Query("SELECT SUM(a.creditEarned) FROM AcademicRecord a WHERE a.user.studentId = :studentId AND a.creditEarned IS NOT NULL")
    Optional<Double> findTotalCreditsByStudentId(@Param("studentId") String studentId);

    // 获取学生已修课程数量
    @Query("SELECT COUNT(a) FROM AcademicRecord a WHERE a.user.studentId = :studentId")
    Long countByStudentId(@Param("studentId") String studentId);

    // 获取学生各学期的GPA趋势
    @Query("SELECT a.academicYear, a.semester, AVG(a.gradePoint) FROM AcademicRecord a " +
            "WHERE a.user.studentId = :studentId AND a.gradePoint IS NOT NULL " +
            "GROUP BY a.academicYear, a.semester " +
            "ORDER BY a.academicYear, a.semester")
    List<Object[]> findGPATrendByStudentId(@Param("studentId") String studentId);

    // 获取课程的平均成绩
    @Query("SELECT AVG(a.score) FROM AcademicRecord a WHERE a.courseId = :courseId AND a.score IS NOT NULL")
    Optional<Double> findAverageScoreByCourseId(@Param("courseId") String courseId);

    // 根据多个学生ID查找学业记录（用于管理员批量查询）
    List<AcademicRecord> findByUserStudentIdIn(List<String> studentIds);
}