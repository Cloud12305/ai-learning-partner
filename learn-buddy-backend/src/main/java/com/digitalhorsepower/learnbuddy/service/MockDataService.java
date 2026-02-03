package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.AcademicRecord;
import com.digitalhorsepower.learnbuddy.entity.LabAttendance;
import com.digitalhorsepower.learnbuddy.entity.LibraryRecord;
import com.digitalhorsepower.learnbuddy.entity.User;
import com.digitalhorsepower.learnbuddy.vo.AcademicProfileVO;
import com.digitalhorsepower.learnbuddy.vo.AcademicStatsVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class MockDataService {

    public AcademicProfileVO getMockAcademicProfile(String studentId) {
        AcademicProfileVO profile = new AcademicProfileVO();

        // 模拟用户信息
        User user = new User();
        user.setStudentId(studentId);
        user.setName("测试学生");
        user.setUsername("peng");
        user.setCollege("计算机学院");
        user.setMajor("计算机科学与技术");
        user.setGrade("2023级");
        user.setClassName("计算机1班");
        user.setEmail("peng@example.com");
        user.setPhone("13800138000");
        user.setEnrollmentDate(LocalDateTime.of(2023, 9, 1, 0, 0));
        user.setRole("STUDENT");
        user.setAvatarUrl("https://picsum.photos/100/100?random=10");
        profile.setUserInfo(user);

        // 模拟学业统计
        AcademicStatsVO stats = new AcademicStatsVO();
        stats.setAverageGPA(3.75);
        stats.setLabProjects(5L);
        stats.setBorrowedBooks(3L);
        profile.setAcademicStats(stats);

        // 模拟课程数据
        profile.setRecentCourses(getMockCourses());

        // 模拟实验室数据
        profile.setRecentLabs(getMockLabs());

        // 模拟图书数据
        profile.setRecentBooks(getMockBooks());

        return profile;
    }

    public List<AcademicRecord> getMockCourses() {
        AcademicRecord course1 = new AcademicRecord();
        course1.setRecordId(1L);
        course1.setStudentId("2023001001"); // 添加学生ID，保持数据一致性
        course1.setCourseId("CS101");
        course1.setCourseName("计算机科学导论"); // 新增：设置课程名称
        course1.setAcademicYear("2023-2024");
        course1.setSemester(1);
        course1.setScore(92.5);
        course1.setGradePoint(4.0);
        course1.setCreditEarned(3.0);
        course1.setCreatedAt(LocalDateTime.now().minusMonths(2));
        course1.setUpdatedAt(LocalDateTime.now().minusMonths(2));

        AcademicRecord course2 = new AcademicRecord();
        course2.setRecordId(2L);
        course2.setStudentId("2023001001"); // 添加学生ID，保持数据一致性
        course2.setCourseId("MA101");
        course2.setCourseName("高等数学A"); // 新增：设置课程名称
        course2.setAcademicYear("2023-2024");
        course2.setSemester(1);
        course2.setScore(88.0);
        course2.setGradePoint(3.7);
        course2.setCreditEarned(4.0);
        course2.setCreatedAt(LocalDateTime.now().minusMonths(2));
        course2.setUpdatedAt(LocalDateTime.now().minusMonths(2));

        AcademicRecord course3 = new AcademicRecord();
        course3.setRecordId(3L);
        course3.setStudentId("2023001001"); // 添加学生ID，保持数据一致性
        course3.setCourseId("PH101");
        course3.setCourseName("大学物理"); // 新增：设置课程名称
        course3.setAcademicYear("2023-2024");
        course3.setSemester(1);
        course3.setScore(85.0);
        course3.setGradePoint(3.3);
        course3.setCreditEarned(3.0);
        course3.setCreatedAt(LocalDateTime.now().minusMonths(2));
        course3.setUpdatedAt(LocalDateTime.now().minusMonths(2));

        // 可以添加更多模拟课程数据
        AcademicRecord course4 = new AcademicRecord();
        course4.setRecordId(4L);
        course4.setStudentId("2023001001");
        course4.setCourseId("EN101");
        course4.setCourseName("大学英语"); // 新增：设置课程名称
        course4.setAcademicYear("2023-2024");
        course4.setSemester(1);
        course4.setScore(95.0);
        course4.setGradePoint(4.5);
        course4.setCreditEarned(2.0);
        course4.setCreatedAt(LocalDateTime.now().minusMonths(2));
        course4.setUpdatedAt(LocalDateTime.now().minusMonths(2));

        AcademicRecord course5 = new AcademicRecord();
        course5.setRecordId(5L);
        course5.setStudentId("2023001001");
        course5.setCourseId("CS102");
        course5.setCourseName("数据结构"); // 新增：设置课程名称
        course5.setAcademicYear("2023-2024");
        course5.setSemester(2);
        course5.setScore(90.0);
        course5.setGradePoint(4.0);
        course5.setCreditEarned(3.5);
        course5.setCreatedAt(LocalDateTime.now().minusMonths(1));
        course5.setUpdatedAt(LocalDateTime.now().minusMonths(1));

        return Arrays.asList(course1, course2, course3, course4, course5);
    }

    public List<LabAttendance> getMockLabs() {
        LabAttendance lab1 = new LabAttendance();
        lab1.setAttendanceId(1L);
        lab1.setStudentId("2023001001");
        lab1.setLabId("LAB001");
        lab1.setLabName("计算机基础实验室");
        lab1.setExperimentName("Java编程实验");
        lab1.setCheckInTime(LocalDateTime.now().minusDays(2));
        lab1.setCheckOutTime(LocalDateTime.now().minusDays(2).plusHours(2));
        lab1.setDurationMinutes(120);
        lab1.setCreatedAt(LocalDateTime.now().minusDays(2));

        LabAttendance lab2 = new LabAttendance();
        lab2.setAttendanceId(2L);
        lab2.setStudentId("2023001001");
        lab2.setLabId("LAB002");
        lab2.setLabName("网络实验室");
        lab2.setExperimentName("网络配置实验");
        lab2.setCheckInTime(LocalDateTime.now().minusDays(1));
        lab2.setCheckOutTime(LocalDateTime.now().minusDays(1).plusHours(3));
        lab2.setDurationMinutes(180);
        lab2.setCreatedAt(LocalDateTime.now().minusDays(1));

        return Arrays.asList(lab1, lab2);
    }

    public List<LibraryRecord> getMockBooks() {
        LibraryRecord book1 = new LibraryRecord();
        book1.setRecordId(1L);
        book1.setStudentId("2023001001");
        book1.setBookId("BOOK001");
        book1.setBookIsbn("978-7-111-213-1");
        book1.setBookTitle("深入理解计算机系统");
        book1.setBookAuthor("Randal E. Bryant");
        book1.setBookCategory("计算机科学");
        book1.setPublisher("机械工业出版社");
        book1.setPublishYear(2016);
        book1.setBorrowDate(LocalDate.now().minusDays(10));
        book1.setDueDate(LocalDate.now().plusDays(20));
        book1.setCreatedAt(LocalDateTime.now().minusDays(10));

        LibraryRecord book2 = new LibraryRecord();
        book2.setRecordId(2L);
        book2.setStudentId("2023001001");
        book2.setBookId("BOOK002");
        book2.setBookIsbn("978-7-111-187-2");
        book2.setBookTitle("算法导论");
        book2.setBookAuthor("Thomas H. Cormen");
        book2.setBookCategory("计算机科学");
        book2.setPublisher("机械工业出版社");
        book2.setPublishYear(2013);
        book2.setBorrowDate(LocalDate.now().minusDays(5));
        book2.setDueDate(LocalDate.now().plusDays(25));
        book2.setCreatedAt(LocalDateTime.now().minusDays(5));

        LibraryRecord book3 = new LibraryRecord();
        book3.setRecordId(3L);
        book3.setStudentId("2023001001");
        book3.setBookId("BOOK003");
        book3.setBookIsbn("978-7-115-123-4");
        book3.setBookTitle("计算机网络");
        book3.setBookAuthor("Andrew S. Tanenbaum");
        book3.setBookCategory("计算机科学");
        book3.setPublisher("人民邮电出版社");
        book3.setPublishYear(2018);
        book3.setBorrowDate(LocalDate.now().minusDays(3));
        book3.setDueDate(LocalDate.now().plusDays(27));
        book3.setCreatedAt(LocalDateTime.now().minusDays(3));

        return Arrays.asList(book1, book2, book3);
    }
}