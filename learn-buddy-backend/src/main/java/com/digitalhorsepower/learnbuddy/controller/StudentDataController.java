package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.dto.*;
import com.digitalhorsepower.learnbuddy.entity.User;
import com.digitalhorsepower.learnbuddy.service.StudentDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-data")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentDataController {

    private static final Logger logger = LoggerFactory.getLogger(StudentDataController.class);

    @Autowired
    private StudentDataService studentDataService;

    // 测试接口 - 检查连接
    @GetMapping("/test/{studentId}")
    public ApiResponse<String> testConnection(@PathVariable String studentId) {
        logger.info("测试连接: {}", studentId);
        try {
            User student = studentDataService.getStudentInfo(studentId);
            List<CourseDTO> courses = studentDataService.getCourses(studentId);
            return ApiResponse.success("测试成功",
                    String.format("学生: %s, 课程数: %d", student.getName(), courses.size()));
        } catch (Exception e) {
            logger.error("测试失败: {}", e.getMessage());
            return ApiResponse.error("测试失败: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}/info")
    public ApiResponse<User> getStudentInfo(@PathVariable String studentId) {
        logger.info("获取学生基本信息: {}", studentId);
        try {
            User student = studentDataService.getStudentInfo(studentId);
            return ApiResponse.success("获取学生信息成功", student);
        } catch (Exception e) {
            logger.error("获取学生信息失败: {}", e.getMessage());
            return ApiResponse.error("获取学生信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}/overall-scores")
    public ApiResponse<OverallScoresDTO> getOverallScores(@PathVariable String studentId) {
        logger.info("获取学生总体学业评分: {}", studentId);
        try {
            OverallScoresDTO scores = studentDataService.getOverallScores(studentId);
            return ApiResponse.success("获取学业评分成功", scores);
        } catch (Exception e) {
            logger.error("获取学业评分失败: {}", e.getMessage());
            return ApiResponse.error("获取学业评分失败: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}/courses")
    public ApiResponse<List<CourseDTO>> getCourses(@PathVariable String studentId) {
        logger.info("获取学生课程成绩: {}", studentId);
        try {
            List<CourseDTO> courses = studentDataService.getCourses(studentId);
            return ApiResponse.success("获取课程成绩成功", courses);
        } catch (Exception e) {
            logger.error("获取课程成绩失败: {}", e.getMessage());
            return ApiResponse.error("获取课程成绩失败: " + e.getMessage());
        }
    }
    @GetMapping("/{studentId}/gpa-trend")
    public ApiResponse<GPATrendDTO> getGpaTrend(@PathVariable String studentId) {
        logger.info("获取GPA趋势: {}", studentId);
        try {
            GPATrendDTO trend = studentDataService.getGpaTrend(studentId);
            return ApiResponse.success("获取GPA趋势成功", trend);
        } catch (Exception e) {
            logger.error("获取GPA趋势失败: {}", e.getMessage());
            return ApiResponse.error("获取GPA趋势失败: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}/skills")
    public ApiResponse<List<SkillDTO>> getSkills(@PathVariable String studentId) {
        logger.info("获取学生技能数据: {}", studentId);
        try {
            List<SkillDTO> skills = studentDataService.getSkills(studentId);
            return ApiResponse.success("获取技能数据成功", skills);
        } catch (Exception e) {
            logger.error("获取技能数据失败: {}", e.getMessage());
            return ApiResponse.error("获取技能数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}/lab-participation")
    public ApiResponse<LabParticipationDTO> getLabParticipation(@PathVariable String studentId) {
        logger.info("获取实验室参与统计: {}", studentId);
        try {
            LabParticipationDTO participation = studentDataService.getLabParticipation(studentId);
            return ApiResponse.success("获取实验室参与统计成功", participation);
        } catch (Exception e) {
            logger.error("获取实验室参与统计失败: {}", e.getMessage());
            return ApiResponse.error("获取实验室参与统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}/lab-projects")
    public ApiResponse<List<LabProjectDTO>> getLabProjects(@PathVariable String studentId) {
        logger.info("获取实验项目: {}", studentId);
        try {
            List<LabProjectDTO> projects = studentDataService.getLabProjects(studentId);
            return ApiResponse.success("获取实验项目成功", projects);
        } catch (Exception e) {
            logger.error("获取实验项目失败: {}", e.getMessage());
            return ApiResponse.error("获取实验项目失败: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}/book-categories")
    public ApiResponse<BookCategoryDTO> getBookCategories(@PathVariable String studentId) {
        logger.info("获取借阅类别分布: {}", studentId);
        try {
            BookCategoryDTO categories = studentDataService.getBookCategories(studentId);
            return ApiResponse.success("获取借阅类别分布成功", categories);
        } catch (Exception e) {
            logger.error("获取借阅类别分布失败: {}", e.getMessage());
            return ApiResponse.error("获取借阅类别分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}/borrowing-trend")
    public ApiResponse<BorrowingTrendDTO> getBorrowingTrend(@PathVariable String studentId) {
        logger.info("获取借阅趋势: {}", studentId);
        try {
            BorrowingTrendDTO trend = studentDataService.getBorrowingTrend(studentId);
            return ApiResponse.success("获取借阅趋势成功", trend);
        } catch (Exception e) {
            logger.error("获取借阅趋势失败: {}", e.getMessage());
            return ApiResponse.error("获取借阅趋势失败: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}/recent-books")
    public ApiResponse<List<BookDTO>> getRecentBooks(@PathVariable String studentId) {
        logger.info("获取最近借阅书籍: {}", studentId);
        try {
            List<BookDTO> books = studentDataService.getRecentBooks(studentId);
            return ApiResponse.success("获取最近借阅书籍成功", books);
        } catch (Exception e) {
            logger.error("获取最近借阅书籍失败: {}", e.getMessage());
            return ApiResponse.error("获取最近借阅书籍失败: " + e.getMessage());
        }
    }
}