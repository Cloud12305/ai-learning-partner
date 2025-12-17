package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.LabAttendance;
import com.digitalhorsepower.learnbuddy.repository.LabAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LabAttendanceService {

    @Autowired
    private LabAttendanceRepository labAttendanceRepository;

    // 根据ID获取实验室记录
    public Optional<LabAttendance> getLabRecordById(Long id) {
        return labAttendanceRepository.findById(id);
    }

    // 根据学生ID获取实验室记录
    public List<LabAttendance> getStudentLabRecords(String studentId) {
        return labAttendanceRepository.findByStudentId(studentId);
    }

    // 根据学生ID获取并按时间排序
    public List<LabAttendance> getStudentLabRecordsOrderByTime(String studentId) {
        return labAttendanceRepository.findByStudentIdOrderByCheckInTimeDesc(studentId);
    }

    // 根据学生ID分页获取
    public List<LabAttendance> getStudentLabRecords(String studentId, Pageable pageable) {
        return labAttendanceRepository.findByStudentIdOrderByCheckInTimeDesc(studentId, pageable);
    }

    // 根据实验室名称获取记录
    public List<LabAttendance> getRecordsByLabName(String labName) {
        return labAttendanceRepository.findByLabName(labName);
    }

    // 根据实验名称获取记录
    public List<LabAttendance> getRecordsByExperimentName(String experimentName) {
        return labAttendanceRepository.findByExperimentName(experimentName);
    }

    // 根据学生ID和实验室名称获取记录
    public List<LabAttendance> getRecordsByStudentAndLab(String studentId, String labName) {
        return labAttendanceRepository.findByStudentIdAndLabName(studentId, labName);
    }

    // 根据时间范围获取记录
    public List<LabAttendance> getRecordsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return labAttendanceRepository.findByCheckInTimeBetween(start, end);
    }

    // 根据学生ID和时间范围获取记录
    public List<LabAttendance> getRecordsByStudentAndTimeRange(String studentId, LocalDateTime start, LocalDateTime end) {
        return labAttendanceRepository.findByStudentIdAndCheckInTimeBetween(studentId, start, end);
    }

    // 获取学生的实验室总时长
    public Integer getStudentTotalLabMinutes(String studentId) {
        return labAttendanceRepository.findTotalLabMinutesByStudentId(studentId);
    }

    // 获取学生的实验室记录数量
    public Long getStudentLabSessionCount(String studentId) {
        return labAttendanceRepository.countByStudentId(studentId);
    }

    // 获取学生完成的实验数量
    public Long getStudentCompletedLabsCount(String studentId) {
        return labAttendanceRepository.countCompletedLabs(studentId);
    }

    // 获取学生的实验室使用统计
    public List<Object[]> getStudentLabUsageStats(String studentId) {
        return labAttendanceRepository.findLabUsageStatsByStudentId(studentId);
    }

    // 获取学生最近的实验室记录
    public List<LabAttendance> getRecentLabRecords(String studentId) {
        return labAttendanceRepository.findTop5ByStudentIdOrderByCheckInTimeDesc(studentId);
    }

    // 获取学生未签退的记录
    public List<LabAttendance> getUncheckedOutRecords(String studentId) {
        return labAttendanceRepository.findUncheckedOutRecords(studentId);
    }

    // 获取学生实验室使用时间分布
    public List<Object[]> getLabUsageByHour(String studentId) {
        return labAttendanceRepository.findLabUsageByHour(studentId);
    }

    // 保存或更新实验室记录
    public LabAttendance saveLabRecord(LabAttendance record) {
        return labAttendanceRepository.save(record);
    }

    // 删除实验室记录
    public void deleteLabRecord(Long id) {
        labAttendanceRepository.deleteById(id);
    }

    // 获取所有实验室记录
    public List<LabAttendance> getAllLabRecords() {
        return labAttendanceRepository.findAll();
    }
}