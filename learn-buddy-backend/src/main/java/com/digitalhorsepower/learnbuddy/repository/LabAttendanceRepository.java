package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.LabAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LabAttendanceRepository extends JpaRepository<LabAttendance, Long> {

    // 根据学生ID查找实验室记录
    List<LabAttendance> findByUserStudentId(String studentId);

    // 根据实验室名称查找记录
    List<LabAttendance> findByLabName(String labName);

    // 根据实验名称查找记录
    List<LabAttendance> findByExperimentName(String experimentName);

    // 根据学生ID和实验室名称查找记录
    List<LabAttendance> findByUserStudentIdAndLabName(String studentId, String labName);

    // 根据签到时间范围查找记录
    List<LabAttendance> findByCheckInTimeBetween(LocalDateTime start, LocalDateTime end);

    // 根据学生ID和签到时间范围查找记录
    List<LabAttendance> findByUserStudentIdAndCheckInTimeBetween(String studentId, LocalDateTime start, LocalDateTime end);

    // 获取学生的实验室总时长（分钟）
    @Query("SELECT COALESCE(SUM(a.durationMinutes), 0) FROM LabAttendance a WHERE a.user.studentId = :studentId")
    Integer findTotalLabMinutesByStudentId(@Param("studentId") String studentId);

    // 获取学生的实验室访问次数
    @Query("SELECT COUNT(a) FROM LabAttendance a WHERE a.user.studentId = :studentId")
    Long countByStudentId(@Param("studentId") String studentId);

    // 获取实验室使用统计（按实验室分组）
    @Query("SELECT a.labName, COUNT(a), COALESCE(SUM(a.durationMinutes), 0) FROM LabAttendance a " +
            "WHERE a.user.studentId = :studentId " +
            "GROUP BY a.labName")
    List<Object[]> findLabUsageStatsByStudentId(@Param("studentId") String studentId);

    // 获取学生最近几次实验室记录
    List<LabAttendance> findTop5ByUserStudentIdOrderByCheckInTimeDesc(String studentId);

    // 检查是否存在未签退的记录
    @Query("SELECT a FROM LabAttendance a WHERE a.user.studentId = :studentId AND a.checkOutTime IS NULL")
    List<LabAttendance> findUncheckedOutRecords(@Param("studentId") String studentId);

    // 获取实验室使用频率统计（按时间段）
    @Query("SELECT FUNCTION('HOUR', a.checkInTime), COUNT(a) FROM LabAttendance a " +
            "WHERE a.user.studentId = :studentId " +
            "GROUP BY FUNCTION('HOUR', a.checkInTime) " +
            "ORDER BY FUNCTION('HOUR', a.checkInTime)")
    List<Object[]> findLabUsageByHour(@Param("studentId") String studentId);
}