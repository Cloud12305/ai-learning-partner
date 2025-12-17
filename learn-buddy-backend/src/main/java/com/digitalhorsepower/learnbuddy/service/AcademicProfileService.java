package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.AcademicRecord;
import com.digitalhorsepower.learnbuddy.entity.LabAttendance;
import com.digitalhorsepower.learnbuddy.entity.LibraryRecord;
import com.digitalhorsepower.learnbuddy.entity.User;
import com.digitalhorsepower.learnbuddy.repository.AcademicRecordRepository;
import com.digitalhorsepower.learnbuddy.repository.LabAttendanceRepository;
import com.digitalhorsepower.learnbuddy.repository.LibraryRecordRepository;
import com.digitalhorsepower.learnbuddy.repository.UserRepository;
import com.digitalhorsepower.learnbuddy.vo.AcademicProfileVO;
import com.digitalhorsepower.learnbuddy.vo.AcademicStatsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.digitalhorsepower.learnbuddy.vo.AcademicOverviewVO;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AcademicProfileService {

    private final AcademicRecordRepository academicRecordRepository;
    private final LabAttendanceRepository labAttendanceRepository;
    private final LibraryRecordRepository libraryRecordRepository;
    private final UserRepository userRepository;
    private final MockDataService mockDataService;
    private static final Logger logger = LoggerFactory.getLogger(AcademicProfileService.class);

    public AcademicOverviewVO getAcademicOverview(String studentId) {
        try {
            logger.info("⚠️ 注意：使用简化的getAcademicOverview方法，完整逻辑在Controller中");
            return createEmptyOverview();

        } catch (Exception e) {
            logger.error("计算学业概览失败: {}", e.getMessage(), e);
            return createEmptyOverview();
        }
    }

    private AcademicOverviewVO createEmptyOverview() {
        AcademicOverviewVO emptyOverview = new AcademicOverviewVO();
        emptyOverview.setCurrentAcademicYear("--");
        emptyOverview.setCurrentSemester(0);
        emptyOverview.setCurrentSemesterGPA(0.0);
        emptyOverview.setOverallGPA(0.0);
        emptyOverview.setTotalCredits(0.0);
        emptyOverview.setLabProjectsCount(0L);
        emptyOverview.setBorrowedBooksCount(0L);
        emptyOverview.setSemesterGPAs(new ArrayList<>());
        return emptyOverview;
    }

    public AcademicProfileVO getAcademicProfile(String studentId) {
        try {
            User user = userRepository.findByStudentId(studentId)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            AcademicProfileVO profile = new AcademicProfileVO();
            profile.setUserInfo(user);
            profile.setAcademicStats(getAcademicStats(studentId));
            profile.setRecentCourses(getCourses(studentId, 5));
            profile.setRecentLabs(getLabProjects(studentId, 2));
            profile.setRecentBooks(getBooks(studentId, 4));

            return profile;
        } catch (Exception e) {
            System.out.println("获取真实数据失败，使用模拟数据: " + e.getMessage());
            return mockDataService.getMockAcademicProfile(studentId);
        }
    }

    public AcademicStatsVO getAcademicStats(String studentId) {
        // 计算平均GPA
        Optional<Double> avgGPAOpt = academicRecordRepository.findAverageGradePointByStudentId(studentId);
        Double averageGPA = avgGPAOpt.map(gpa ->
                Math.round(gpa * 100.0) / 100.0  // 保留两位小数
        ).orElse(0.0);

        // 计算实验项目数量
        Long labProjects = labAttendanceRepository.countByStudentId(studentId);

        // 计算当前借阅书籍数量
        Long borrowedBooks = libraryRecordRepository.countCurrentBorrowedByStudentId(studentId);

        AcademicStatsVO stats = new AcademicStatsVO();
        stats.setAverageGPA(averageGPA);
        stats.setLabProjects(labProjects != null ? labProjects : 0L);
        stats.setBorrowedBooks(borrowedBooks != null ? borrowedBooks : 0L);

        logger.info("学生 {} 的统计信息 - GPA: {}, 实验项目: {}, 借阅书籍: {}",
                studentId, averageGPA, stats.getLabProjects(), stats.getBorrowedBooks());

        return stats;
    }

    public List<AcademicRecord> getCourses(String studentId, int limit) {
        try {
            return academicRecordRepository.findByStudentIdOrderByAcademicYearDescSemesterDesc(studentId,
                    PageRequest.of(0, limit));
        } catch (Exception e) {
            System.out.println("获取课程数据失败，使用模拟数据: " + e.getMessage());
            return mockDataService.getMockCourses();
        }
    }

    public List<AcademicRecord> getAllCourses(String studentId) {
        try {
            return academicRecordRepository.findByStudentIdOrderByAcademicYearDescSemesterDesc(studentId);
        } catch (Exception e) {
            System.out.println("获取所有课程数据失败，使用模拟数据: " + e.getMessage());
            return mockDataService.getMockCourses();
        }
    }

    public List<AcademicRecord> getAllCourses() {
        try {
            List<AcademicRecord> courses = academicRecordRepository.findAll();

            // 🔍 调试：查看数据库直接返回的数据
            logger.info("🔍 从数据库获取到 {} 条记录", courses.size());
            if (!courses.isEmpty()) {
                AcademicRecord first = courses.get(0);
                logger.info("🔍 第一条记录 - ID: {}, 课程ID: {}, 课程名: {}, 课程名是否为null: {}",
                        first.getRecordId(), first.getCourseId(),
                        first.getCourseName(), first.getCourseName() == null);

                // 反射查看所有字段
                try {
                    Class<?> clazz = first.getClass();
                    java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
                    for (java.lang.reflect.Field field : fields) {
                        field.setAccessible(true);
                        logger.info("🔍 字段: {} = {}", field.getName(), field.get(first));
                    }
                } catch (Exception e) {
                    logger.error("反射查看字段失败: {}", e.getMessage());
                }
            }

            return courses;
        } catch (Exception e) {
            System.out.println("获取所有课程数据失败，使用模拟数据: " + e.getMessage());
            return mockDataService.getMockCourses();
        }
    }

    public List<LabAttendance> getLabProjects(String studentId, int limit) {
        try {
            return labAttendanceRepository.findByStudentIdOrderByCheckInTimeDesc(studentId,
                    PageRequest.of(0, limit));
        } catch (Exception e) {
            System.out.println("获取实验室数据失败，使用模拟数据: " + e.getMessage());
            return mockDataService.getMockLabs();
        }
    }

    public List<LabAttendance> getAllLabProjects(String studentId) {
        try {
            return labAttendanceRepository.findByStudentIdOrderByCheckInTimeDesc(studentId);
        } catch (Exception e) {
            System.out.println("获取所有实验室数据失败，使用模拟数据: " + e.getMessage());
            return mockDataService.getMockLabs();
        }
    }

    public List<LabAttendance> getAllLabProjects() {
        try {
            return labAttendanceRepository.findAll();
        } catch (Exception e) {
            System.out.println("获取所有实验室数据失败，使用模拟数据: " + e.getMessage());
            return mockDataService.getMockLabs();
        }
    }

    public List<LibraryRecord> getBooks(String studentId, int limit) {
        try {
            return libraryRecordRepository.findByStudentIdOrderByBorrowDateDesc(studentId,
                    PageRequest.of(0, limit));
        } catch (Exception e) {
            System.out.println("获取图书数据失败，使用模拟数据: " + e.getMessage());
            return mockDataService.getMockBooks();
        }
    }

    public List<LibraryRecord> getAllBooks(String studentId) {
        try {
            return libraryRecordRepository.findByStudentIdOrderByBorrowDateDesc(studentId);
        } catch (Exception e) {
            System.out.println("获取所有图书数据失败，使用模拟数据: " + e.getMessage());
            return mockDataService.getMockBooks();
        }
    }

    public List<LibraryRecord> getAllBooks() {
        try {
            return libraryRecordRepository.findAll();
        } catch (Exception e) {
            System.out.println("获取所有图书数据失败，使用模拟数据: " + e.getMessage());
            return mockDataService.getMockBooks();
        }
    }

    public Double calculateOverallGPA(String studentId) {
        try {
            List<AcademicRecord> courses = academicRecordRepository.findByStudentIdOrderByAcademicYearDescSemesterDesc(studentId);
            double totalCredits = 0.0;
            double totalGradePoints = 0.0;

            for (AcademicRecord course : courses) {
                if (course.getCreditEarned() != null && course.getGradePoint() != null) {
                    totalCredits += course.getCreditEarned();
                    totalGradePoints += (course.getGradePoint() * course.getCreditEarned());
                }
            }

            return totalCredits > 0 ? Math.round((totalGradePoints / totalCredits) * 100.0) / 100.0 : 0.0;
        } catch (Exception e) {
            logger.error("计算总GPA失败: {}", e.getMessage());
            return 0.0;
        }
    }
}