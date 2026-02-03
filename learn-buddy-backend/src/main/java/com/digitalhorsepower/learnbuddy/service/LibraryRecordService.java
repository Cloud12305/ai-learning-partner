package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.LibraryRecord;
import com.digitalhorsepower.learnbuddy.repository.LibraryRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryRecordService {

    @Autowired
    private LibraryRecordRepository libraryRecordRepository;

    public List<LibraryRecord> getStudentLibraryRecords(String studentId) {
        return libraryRecordRepository.findByStudentId(studentId);
    }

    public List<LibraryRecord> getCurrentBorrowedBooks(String studentId) {
        return libraryRecordRepository.findCurrentBorrowedBooks(studentId);
    }

    public Long getStudentBorrowedCount(String studentId) {
        return libraryRecordRepository.countByStudentId(studentId);
    }

    public Long getStudentCurrentBorrowedCount(String studentId) {
        return libraryRecordRepository.countCurrentBorrowedByStudentId(studentId);
    }

    public List<LibraryRecord> getRecentBorrowedBooks(String studentId) {
        return libraryRecordRepository.findTop10ByStudentIdOrderByBorrowDateDesc(studentId);
    }

    public LibraryRecord saveLibraryRecord(LibraryRecord record) {
        return libraryRecordRepository.save(record);
    }

    public void deleteLibraryRecord(Long id) {
        libraryRecordRepository.deleteById(id);
    }

    public List<LibraryRecord> getAllLibraryRecords() {
        return libraryRecordRepository.findAll();
    }
}