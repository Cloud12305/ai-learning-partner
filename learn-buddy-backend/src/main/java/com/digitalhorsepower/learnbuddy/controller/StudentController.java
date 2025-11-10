package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @GetMapping("/{studentId}/overall-scores")
    public ApiResponse<Map<String, Object>> getOverallScores(@PathVariable String studentId) {
        logger.info("获取学生总体学业评分: {}", studentId);

        Map<String, Object> scores = new HashMap<>();
        scores.put("comprehensiveScore", 88);
        scores.put("averageGPA", 3.7);
        scores.put("labProjects", 12);
        scores.put("borrowedBooks", 36);

        return ApiResponse.success("获取学业评分成功", scores);
    }

    @GetMapping("/{studentId}/courses")
    public ApiResponse<List<Map<String, Object>>> getCourses(@PathVariable String studentId) {
        logger.info("获取学生课程成绩: {}", studentId);

        List<Map<String, Object>> courses = Arrays.asList(
                createCourse(1L, "数据结构与算法", 4, 92, "A", "大二上"),
                createCourse(2L, "计算机组成原理", 4, 85, "A-", "大二上"),
                createCourse(3L, "操作系统", 4, 88, "A-", "大二下"),
                createCourse(4L, "计算机网络", 3, 80, "B+", "大二下"),
                createCourse(5L, "人工智能导论", 3, 95, "A", "大三上")
        );

        return ApiResponse.success("获取课程成绩成功", courses);
    }

    @GetMapping("/{studentId}/gpa-trend")
    public ApiResponse<Map<String, Object>> getGpaTrend(@PathVariable String studentId) {
        logger.info("获取GPA趋势: {}", studentId);

        Map<String, Object> trend = new HashMap<>();
        trend.put("labels", Arrays.asList("大一上", "大一下", "大二上", "大二下", "大三上"));
        trend.put("data", Arrays.asList(3.2, 3.5, 3.6, 3.7, 3.8));

        return ApiResponse.success("获取GPA趋势成功", trend);
    }

    @GetMapping("/{studentId}/skills")
    public ApiResponse<List<Map<String, Object>>> getSkills(@PathVariable String studentId) {
        logger.info("获取学生技能数据: {}", studentId);

        List<Map<String, Object>> skills = Arrays.asList(
                createSkill("Python编程", 90),
                createSkill("机器学习", 75),
                createSkill("数据可视化", 80),
                createSkill("深度学习框架", 65),
                createSkill("数据库设计", 85)
        );

        return ApiResponse.success("获取技能数据成功", skills);
    }

    @GetMapping("/{studentId}/lab-participation")
    public ApiResponse<Map<String, Object>> getLabParticipation(@PathVariable String studentId) {
        logger.info("获取实验室参与统计: {}", studentId);

        Map<String, Object> participation = new HashMap<>();
        participation.put("labels", Arrays.asList("算法实验室", "AI实验室", "大数据实验室", "网络实验室"));
        participation.put("data", Arrays.asList(4, 3, 3, 2));

        return ApiResponse.success("获取实验室参与统计成功", participation);
    }

    @GetMapping("/{studentId}/lab-projects")
    public ApiResponse<List<Map<String, Object>>> getLabProjects(@PathVariable String studentId) {
        logger.info("获取实验项目: {}", studentId);

        List<Map<String, Object>> projects = Arrays.asList(
                createLabProject(1L, "智能推荐系统", "基于协同过滤算法的个性化推荐系统设计与实现", "completed", "2023.09-2023.12", 3),
                createLabProject(2L, "图像识别应用", "基于CNN的图像分类与识别系统开发", "completed", "2024.03-2024.06", 4),
                createLabProject(3L, "大数据分析平台", "基于Spark的分布式数据处理与分析平台", "inProgress", "2024.09-至今", 5)
        );

        return ApiResponse.success("获取实验项目成功", projects);
    }

    @GetMapping("/{studentId}/book-categories")
    public ApiResponse<Map<String, Object>> getBookCategories(@PathVariable String studentId) {
        logger.info("获取借阅类别分布: {}", studentId);

        Map<String, Object> categories = new HashMap<>();
        categories.put("labels", Arrays.asList("计算机科学", "人工智能", "编程语言", "数学", "其他"));
        categories.put("data", Arrays.asList(12, 8, 6, 5, 5));

        return ApiResponse.success("获取借阅类别分布成功", categories);
    }

    @GetMapping("/{studentId}/borrowing-trend")
    public ApiResponse<Map<String, Object>> getBorrowingTrend(@PathVariable String studentId) {
        logger.info("获取借阅趋势: {}", studentId);

        Map<String, Object> trend = new HashMap<>();
        trend.put("labels", Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月"));
        trend.put("data", Arrays.asList(2, 1, 4, 3, 5, 3, 6, 4, 2));

        return ApiResponse.success("获取借阅趋势成功", trend);
    }

    @GetMapping("/{studentId}/recent-books")
    public ApiResponse<List<Map<String, Object>>> getRecentBooks(@PathVariable String studentId) {
        logger.info("获取最近借阅书籍: {}", studentId);

        List<Map<String, Object>> books = Arrays.asList(
                createBook(1L, "深度学习", "Ian Goodfellow 等", "计算机科学", "2024.09.15", "https://picsum.photos/200/300?random=20"),
                createBook(2L, "Python编程：从入门到实践", "Eric Matthes", "编程语言", "2024.08.22", "https://picsum.photos/200/300?random=21"),
                createBook(3L, "数据结构与算法分析", "Mark Allen Weiss", "计算机科学", "2024.07.30", "https://picsum.photos/200/300?random=22"),
                createBook(4L, "人工智能：一种现代方法", "Stuart Russell", "人工智能", "2024.07.15", "https://picsum.photos/200/300?random=23")
        );

        return ApiResponse.success("获取最近借阅书籍成功", books);
    }

    private Map<String, Object> createCourse(Long id, String name, Integer credit, Integer score, String grade, String semester) {
        Map<String, Object> course = new HashMap<>();
        course.put("id", id);
        course.put("name", name);
        course.put("credit", credit);
        course.put("score", score);
        course.put("grade", grade);
        course.put("semester", semester);
        return course;
    }

    private Map<String, Object> createSkill(String name, Integer level) {
        Map<String, Object> skill = new HashMap<>();
        skill.put("name", name);
        skill.put("level", level);
        return skill;
    }

    private Map<String, Object> createLabProject(Long id, String title, String description, String status, String date, Integer members) {
        Map<String, Object> project = new HashMap<>();
        project.put("id", id);
        project.put("title", title);
        project.put("description", description);
        project.put("status", status);
        project.put("date", date);
        project.put("members", members);
        return project;
    }

    private Map<String, Object> createBook(Long id, String title, String author, String category, String date, String image) {
        Map<String, Object> book = new HashMap<>();
        book.put("id", id);
        book.put("title", title);
        book.put("author", author);
        book.put("category", category);
        book.put("date", date);
        book.put("image", image);
        return book;
    }
}