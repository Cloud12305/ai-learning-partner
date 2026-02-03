package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "library_records")
@Data
public class LibraryRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "book_id", nullable = false)
    private String bookId;

    @Column(name = "book_isbn")
    private String bookIsbn;

    @Column(name = "book_title", nullable = false)
    private String bookTitle;

    @Column(name = "book_author")
    private String bookAuthor;

    @Column(name = "book_category")
    private String bookCategory;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "publish_year")
    private Integer publishYear;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}