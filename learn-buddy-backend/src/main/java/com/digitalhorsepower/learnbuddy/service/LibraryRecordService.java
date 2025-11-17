package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.LibraryRecord;
import com.digitalhorsepower.learnbuddy.repository.LibraryRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LibraryRecordService {

    @Autowired
    private LibraryRecordRepository libraryRecordRepository;

    public List<LibraryRecord> getStudentLibraryRecords(String studentId) {
        return libraryRecordRepository.findByUserStudentId(studentId);
    }

    public List<LibraryRecord> getCurrentBorrowedBooks(String studentId) {
        return libraryRecordRepository.findCurrentBorrowedBooks(studentId);
    }

    public List<LibraryRecord> getOverdueBooks(String studentId) {
        return libraryRecordRepository.findOverdueBooks(studentId, LocalDate.now());
    }

    public Long getStudentTotalBooks(String studentId) {
        return libraryRecordRepository.countByStudentId(studentId);
    }

    public Long getStudentCurrentBorrowedCount(String studentId) {
        return libraryRecordRepository.countCurrentBorrowedByStudentId(studentId);
    }

    public List<Object[]> getStudentBookCategoryStats(String studentId) {
        return libraryRecordRepository.findBookCategoryStatsByStudentId(studentId);
    }

    public List<Object[]> getStudentBorrowingTrend(String studentId) {
        return libraryRecordRepository.findBorrowingTrendByStudentId(studentId);
    }

    public List<LibraryRecord> getRecentBorrowedBooks(String studentId) {
        return libraryRecordRepository.findTop10ByUserStudentIdOrderByBorrowDateDesc(studentId);
    }

    public LibraryRecord saveLibraryRecord(LibraryRecord record) {
        return libraryRecordRepository.save(record);
    }

    public void deleteLibraryRecord(Long id) {
        libraryRecordRepository.deleteById(id);
    }

    public boolean isBookCurrentlyBorrowed(String studentId, String bookId) {
        return libraryRecordRepository.isBookCurrentlyBorrowed(studentId, bookId);
    }
}