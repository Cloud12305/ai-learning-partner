package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.AcademicRecord;
import com.digitalhorsepower.learnbuddy.repository.AcademicRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AcademicRecordService {

    @Autowired
    private AcademicRecordRepository academicRecordRepository;

    public List<AcademicRecord> getStudentRecords(String studentId) {
        return academicRecordRepository.findByUserStudentId(studentId);
    }

    public List<AcademicRecord> getRecordsByCourse(String courseId) {
        return academicRecordRepository.findByCourseId(courseId);
    }

    public Optional<Double> getStudentGPA(String studentId) {
        return academicRecordRepository.findAverageGradePointByStudentId(studentId);
    }

    public Optional<Double> getStudentTotalCredits(String studentId) {
        return academicRecordRepository.findTotalCreditsByStudentId(studentId);
    }

    public Long getStudentCourseCount(String studentId) {
        return academicRecordRepository.countByStudentId(studentId);
    }

    public List<Object[]> getStudentGPATrend(String studentId) {
        return academicRecordRepository.findGPATrendByStudentId(studentId);
    }

    public AcademicRecord saveRecord(AcademicRecord record) {
        return academicRecordRepository.save(record);
    }

    public void deleteRecord(Long id) {
        academicRecordRepository.deleteById(id);
    }

    public boolean recordExists(String studentId, String courseId, String academicYear, Integer semester) {
        return academicRecordRepository.existsByUserStudentIdAndCourseIdAndAcademicYearAndSemester(
                studentId, courseId, academicYear, semester);
    }
}