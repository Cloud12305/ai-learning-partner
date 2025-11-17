package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.LibraryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LibraryRecordRepository extends JpaRepository<LibraryRecord, Long> {

    // 根据学生ID查找图书馆记录
    List<LibraryRecord> findByUserStudentId(String studentId);

    // 根据学生ID和图书分类查找记录
    List<LibraryRecord> findByUserStudentIdAndBookCategory(String studentId, String bookCategory);

    // 根据图书标题查找记录
    List<LibraryRecord> findByBookTitleContaining(String bookTitle);

    // 根据作者查找记录
    List<LibraryRecord> findByBookAuthorContaining(String author);

    // 根据图书分类查找记录
    List<LibraryRecord> findByBookCategory(String category);

    // 查找当前借阅中的图书（未归还）
    @Query("SELECT l FROM LibraryRecord l WHERE l.user.studentId = :studentId AND l.returnDate IS NULL")
    List<LibraryRecord> findCurrentBorrowedBooks(@Param("studentId") String studentId);

    // 查找超期未还的图书
    @Query("SELECT l FROM LibraryRecord l WHERE l.user.studentId = :studentId AND l.returnDate IS NULL AND l.dueDate < :currentDate")
    List<LibraryRecord> findOverdueBooks(@Param("studentId") String studentId, @Param("currentDate") LocalDate currentDate);

    // 获取学生的借阅书籍总数
    @Query("SELECT COUNT(l) FROM LibraryRecord l WHERE l.user.studentId = :studentId")
    Long countByStudentId(@Param("studentId") String studentId);

    // 获取学生当前借阅数量
    @Query("SELECT COUNT(l) FROM LibraryRecord l WHERE l.user.studentId = :studentId AND l.returnDate IS NULL")
    Long countCurrentBorrowedByStudentId(@Param("studentId") String studentId);

    // 获取学生的图书分类统计
    @Query("SELECT l.bookCategory, COUNT(l) FROM LibraryRecord l " +
            "WHERE l.user.studentId = :studentId " +
            "GROUP BY l.bookCategory " +
            "ORDER BY COUNT(l) DESC")
    List<Object[]> findBookCategoryStatsByStudentId(@Param("studentId") String studentId);

    // 获取学生最喜欢的作者（借阅次数最多的作者）
    @Query("SELECT l.bookAuthor, COUNT(l) FROM LibraryRecord l " +
            "WHERE l.user.studentId = :studentId AND l.bookAuthor IS NOT NULL " +
            "GROUP BY l.bookAuthor " +
            "ORDER BY COUNT(l) DESC")
    List<Object[]> findFavoriteAuthorsByStudentId(@Param("studentId") String studentId);

    // 获取学生的借阅趋势（按月统计）
    @Query("SELECT YEAR(l.borrowDate), MONTH(l.borrowDate), COUNT(l) FROM LibraryRecord l " +
            "WHERE l.user.studentId = :studentId " +
            "GROUP BY YEAR(l.borrowDate), MONTH(l.borrowDate) " +
            "ORDER BY YEAR(l.borrowDate), MONTH(l.borrowDate)")
    List<Object[]> findBorrowingTrendByStudentId(@Param("studentId") String studentId);

    // 获取最近借阅的书籍
    List<LibraryRecord> findTop10ByUserStudentIdOrderByBorrowDateDesc(String studentId);

    // 根据借阅日期范围查找记录
    List<LibraryRecord> findByUserStudentIdAndBorrowDateBetween(String studentId, LocalDate start, LocalDate end);

    // 检查图书是否已被特定学生借阅且未归还
    @Query("SELECT COUNT(l) > 0 FROM LibraryRecord l WHERE l.user.studentId = :studentId AND l.bookId = :bookId AND l.returnDate IS NULL")
    boolean isBookCurrentlyBorrowed(@Param("studentId") String studentId, @Param("bookId") String bookId);
}