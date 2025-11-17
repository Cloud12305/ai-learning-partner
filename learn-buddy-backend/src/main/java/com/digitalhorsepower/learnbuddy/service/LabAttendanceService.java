package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.LabAttendance;
import com.digitalhorsepower.learnbuddy.repository.LabAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LabAttendanceService {

    @Autowired
    private LabAttendanceRepository labAttendanceRepository;

    public List<LabAttendance> getStudentLabRecords(String studentId) {
        return labAttendanceRepository.findByUserStudentId(studentId);
    }

    public Integer getStudentTotalLabMinutes(String studentId) {
        return labAttendanceRepository.findTotalLabMinutesByStudentId(studentId);
    }

    public Long getStudentLabSessionCount(String studentId) {
        return labAttendanceRepository.countByStudentId(studentId);
    }

    public List<Object[]> getStudentLabUsageStats(String studentId) {
        return labAttendanceRepository.findLabUsageStatsByStudentId(studentId);
    }

    public List<LabAttendance> getRecentLabRecords(String studentId) {
        return labAttendanceRepository.findTop5ByUserStudentIdOrderByCheckInTimeDesc(studentId);
    }

    public LabAttendance saveLabRecord(LabAttendance record) {
        return labAttendanceRepository.save(record);
    }

    public void deleteLabRecord(Long id) {
        labAttendanceRepository.deleteById(id);
    }

    public List<LabAttendance> getUncheckedOutRecords(String studentId) {
        return labAttendanceRepository.findUncheckedOutRecords(studentId);
    }
}