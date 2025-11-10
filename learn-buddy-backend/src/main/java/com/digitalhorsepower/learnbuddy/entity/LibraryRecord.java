package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "library_records")
public class LibraryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private User student;

    @Column(name = "book_id", nullable = false, length = 30)
    private String bookId;

    @Column(name = "book_isbn", length = 20)
    private String bookIsbn;

    @Column(name = "book_title", nullable = false, length = 200)
    private String bookTitle;

    @Column(name = "book_author", length = 100)
    private String bookAuthor;

    @Column(name = "book_category", length = 50)
    private String bookCategory;

    @Column(name = "publisher", length = 100)
    private String publisher;

    @Column(name = "publish_year")
    private Integer publishYear;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "renewal_count")
    private Integer renewalCount = 0;

    @Column(name = "is_overdue")
    private Boolean isOverdue = false;

    @ManyToOne
    @JoinColumn(name = "related_course_id")
    private Course relatedCourse;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}