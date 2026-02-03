package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.dto.BaseResponse;
import com.digitalhorsepower.learnbuddy.entity.AcademicRecord;
import com.digitalhorsepower.learnbuddy.entity.LabAttendance;
import com.digitalhorsepower.learnbuddy.entity.LibraryRecord;
import com.digitalhorsepower.learnbuddy.entity.User;
import com.digitalhorsepower.learnbuddy.repository.AcademicRecordRepository;
import com.digitalhorsepower.learnbuddy.repository.LabAttendanceRepository;
import com.digitalhorsepower.learnbuddy.repository.LibraryRecordRepository;
import com.digitalhorsepower.learnbuddy.repository.UserRepository;
import com.digitalhorsepower.learnbuddy.service.*;
import com.digitalhorsepower.learnbuddy.util.JwtUtil;
import com.digitalhorsepower.learnbuddy.vo.AcademicProfileVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.digitalhorsepower.learnbuddy.vo.AcademicOverviewVO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;

@RestController
@RequestMapping("/api/academic-profile")
public class AcademicProfileController {

    private static final Logger logger = LoggerFactory.getLogger(AcademicProfileController.class);

    @Autowired
    private AcademicProfileService academicProfileService;

    @Autowired
    private AcademicRecordService academicRecordService;

    @Autowired
    private LabAttendanceService labAttendanceService;

    @Autowired
    private LibraryRecordService libraryRecordService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcademicRecordRepository academicRecordRepository;

    @Autowired
    private LabAttendanceRepository labAttendanceRepository;

    @Autowired
    private LibraryRecordRepository libraryRecordRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MockDataService mockDataService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 获取当前用户的学业画像
    @GetMapping("/current")
    public ResponseEntity<BaseResponse<AcademicProfileVO>> getCurrentUserAcademicProfile() {
        try {
            String token = extractTokenFromRequest();
            String studentId = extractStudentIdFromToken(token);
            if (studentId == null) {
                logger.warn("未授权访问: 无法提取token或学生ID");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.error("未授权访问"));
            }

            logger.info("开始获取学业画像，学生ID: {}", studentId);

            // 尝试获取真实数据，失败时使用模拟数据
            AcademicProfileVO profile;
            try {
                // 先检查用户是否存在
                Optional<User> userOpt = userRepository.findByStudentId(studentId);
                if (userOpt.isEmpty()) {
                    logger.warn("用户不存在，学生ID: {}", studentId);
                    profile = mockDataService.getMockAcademicProfile(studentId);
                } else {
                    profile = academicProfileService.getAcademicProfile(studentId);
                    if (profile == null) {
                        logger.warn("学业画像数据为空，使用模拟数据");
                        profile = mockDataService.getMockAcademicProfile(studentId);
                    }
                }
            } catch (Exception e) {
                logger.error("获取真实数据失败，使用模拟数据: {}", e.getMessage());
                profile = mockDataService.getMockAcademicProfile(studentId);
            }

            logger.info("成功获取学业画像数据");
            return ResponseEntity.ok(BaseResponse.success("获取学业画像成功", profile));
        } catch (Exception e) {
            logger.error("获取学业画像失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("获取学业画像失败"));
        }
    }

    // 获取当前用户的课程成绩
    @GetMapping("/current/courses")
    public ResponseEntity<BaseResponse<List<AcademicRecord>>> getCurrentUserCourses(
            @RequestParam(defaultValue = "5") int limit) {
        try {
            String token = extractTokenFromRequest();
            String studentId = extractStudentIdFromToken(token);
            logger.info("获取课程成绩请求 - 学生ID: {}, 限制: {}", studentId, limit);

            if (studentId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.error("未授权访问"));
            }

            List<AcademicRecord> courses;
            try {
                courses = academicProfileService.getCourses(studentId, limit);
                logger.info("真实数据查询结果: {} 条记录", courses != null ? courses.size() : 0);
            } catch (Exception e) {
                logger.error("获取课程数据失败: {}", e.getMessage());
                courses = mockDataService.getMockCourses();
                logger.info("使用模拟数据: {} 条记录", courses.size());
            }

            return ResponseEntity.ok(BaseResponse.success("获取课程成绩成功", courses));
        } catch (Exception e) {
            logger.error("获取课程成绩异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("获取课程成绩失败"));
        }
    }

    // 获取当前用户的所有课程成绩
    @GetMapping("/current/courses/all")
    public ResponseEntity<BaseResponse<List<AcademicRecord>>> getAllCurrentUserCourses() {
        try {
            String token = extractTokenFromRequest();
            String studentId = extractStudentIdFromToken(token);
            if (studentId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.error("未授权访问"));
            }

            List<AcademicRecord> courses;
            try {
                courses = academicRecordRepository.findByStudentIdOrderByAcademicYearDescSemesterDesc(studentId);
                logger.info("获取所有课程数据: {} 条记录", courses != null ? courses.size() : 0);
            } catch (Exception e) {
                logger.error("获取所有课程数据失败: {}", e.getMessage());
                courses = new ArrayList<>();  // 返回空数组，而不是模拟数据
            }

            return ResponseEntity.ok(BaseResponse.success("获取所有课程成绩成功", courses));
        } catch (Exception e) {
            logger.error("获取所有课程成绩失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("获取所有课程成绩失败"));
        }
    }

    // 获取当前用户的实验室记录
    @GetMapping("/current/labs")
    public ResponseEntity<BaseResponse<List<LabAttendance>>> getCurrentUserLabs(
            @RequestParam(defaultValue = "2") int limit) {
        try {
            String token = extractTokenFromRequest();
            String studentId = extractStudentIdFromToken(token);
            logger.info("获取实验室记录请求 - 学生ID: {}, 限制: {}", studentId, limit);

            if (studentId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.error("未授权访问"));
            }

            List<LabAttendance> labs;
            try {
                List<LabAttendance> allLabs = labAttendanceService.getStudentLabRecordsOrderByTime(studentId);

                if (limit > 0 && allLabs.size() > limit) {
                    labs = allLabs.subList(0, limit);
                } else {
                    labs = allLabs;
                }

                logger.info("真实实验室数据查询结果: {} 条记录", labs != null ? labs.size() : 0);
            } catch (Exception e) {
                logger.error("获取实验室数据失败: {}", e.getMessage());
                labs = mockDataService.getMockLabs();
                logger.info("使用模拟实验室数据: {} 条记录", labs.size());
            }

            return ResponseEntity.ok(BaseResponse.success("获取实验室记录成功", labs));
        } catch (Exception e) {
            logger.error("获取实验室记录异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("获取实验室记录失败"));
        }
    }

    // 获取当前用户的所有实验室记录
    @GetMapping("/current/labs/all")
    public ResponseEntity<BaseResponse<List<LabAttendance>>> getAllCurrentUserLabs() {
        try {
            String token = extractTokenFromRequest();
            String studentId = extractStudentIdFromToken(token);
            if (studentId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.error("未授权访问"));
            }

            List<LabAttendance> labs;
            try {
                labs = labAttendanceService.getStudentLabRecordsOrderByTime(studentId);
                logger.info("获取所有实验室数据: {} 条记录", labs != null ? labs.size() : 0);
            } catch (Exception e) {
                logger.error("获取所有实验室数据失败: {}", e.getMessage());
                labs = mockDataService.getMockLabs();
            }

            return ResponseEntity.ok(BaseResponse.success("获取所有实验室记录成功", labs));
        } catch (Exception e) {
            logger.error("获取所有实验室记录失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("获取所有实验室记录失败"));
        }
    }

    // 获取当前用户的借阅记录
    @GetMapping("/current/books")
    public ResponseEntity<BaseResponse<List<LibraryRecord>>> getCurrentUserBooks(
            @RequestParam(defaultValue = "4") int limit) {
        try {
            String token = extractTokenFromRequest();
            String studentId = extractStudentIdFromToken(token);
            logger.info("获取借阅记录请求 - 学生ID: {}, 限制: {}", studentId, limit);

            if (studentId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.error("未授权访问"));
            }

            List<LibraryRecord> books;
            try {
                books = academicProfileService.getBooks(studentId, limit);
                logger.info("真实图书数据查询结果: {} 条记录", books != null ? books.size() : 0);
            } catch (Exception e) {
                logger.error("获取图书数据失败: {}", e.getMessage());
                books = mockDataService.getMockBooks();
                logger.info("使用模拟图书数据: {} 条记录", books.size());
            }

            return ResponseEntity.ok(BaseResponse.success("获取借阅记录成功", books));
        } catch (Exception e) {
            logger.error("获取借阅记录异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("获取借阅记录失败"));
        }
    }

    // 获取当前用户的所有借阅记录
    @GetMapping("/current/books/all")
    public ResponseEntity<BaseResponse<List<LibraryRecord>>> getAllCurrentUserBooks() {
        try {
            String token = extractTokenFromRequest();
            String studentId = extractStudentIdFromToken(token);
            if (studentId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.error("未授权访问"));
            }

            List<LibraryRecord> books;
            try {
                books = academicProfileService.getAllBooks(studentId);
                logger.info("获取所有图书数据: {} 条记录", books != null ? books.size() : 0);
            } catch (Exception e) {
                logger.error("获取所有图书数据失败: {}", e.getMessage());
                books = mockDataService.getMockBooks();
            }

            return ResponseEntity.ok(BaseResponse.success("获取所有借阅记录成功", books));
        } catch (Exception e) {
            logger.error("获取所有借阅记录失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("获取所有借阅记录失败"));
        }
    }

    // 管理员获取所有课程数据
    @GetMapping("/admin/courses")
    public ResponseEntity<BaseResponse<List<Map<String, Object>>>> getAllCourses() {
        try {
            // 使用SQL查询，确保包含courseName字段
            String sql = "SELECT " +
                    "record_id as recordId, " +
                    "student_id as studentId, " +
                    "course_id as courseId, " +
                    "COALESCE(course_name, CONCAT(course_id, '课程')) as courseName, " +
                    "academic_year as academicYear, " +
                    "semester, " +
                    "score, " +
                    "grade_point as gradePoint, " +
                    "credit_earned as creditEarned, " +
                    "created_at as createdAt, " +
                    "updated_at as updatedAt " +
                    "FROM academic_records " +
                    "ORDER BY record_id DESC";

            List<Map<String, Object>> courses = jdbcTemplate.queryForList(sql);

            logger.info(" 成功获取 {} 条课程记录", courses.size());

            return ResponseEntity.ok(BaseResponse.success("获取所有课程数据成功", courses));

        } catch (Exception e) {
            logger.error(" 获取所有课程数据失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("获取所有课程数据失败"));
        }
    }

    // 管理员获取所有实验室数据
    @GetMapping("/admin/labs")
    public ResponseEntity<BaseResponse<List<Map<String, Object>>>> getAllLabs() {
        try {
            // 使用SQL查询，根据实际字段进行调整
            String sql = "SELECT " +
                    "attendance_id as attendanceId, " +
                    "student_id as studentId, " +
                    "lab_id as labId, " +
                    "lab_name as labName, " +
                    "experiment_name as experimentName, " +
                    "check_in_time as checkInTime, " +
                    "check_out_time as checkOutTime, " +
                    "duration_minutes as durationMinutes, " +
                    "notes, " +
                    "created_at as createdAt " +
                    "FROM lab_attendance " +
                    "ORDER BY attendance_id DESC";

            List<Map<String, Object>> labs = jdbcTemplate.queryForList(sql);

            logger.info(" 成功获取 {} 条实验室记录", labs.size());

            return ResponseEntity.ok(BaseResponse.success("获取所有实验室数据成功", labs));

        } catch (Exception e) {
            logger.error("获取所有实验室数据失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("获取所有实验室数据失败"));
        }
    }


    // 管理员获取所有图书数据
    @GetMapping("/admin/books")
    public ResponseEntity<BaseResponse<List<Map<String, Object>>>> getAllBooks() {
        try {
            // 使用SQL查询，确保包含所有必要字段并格式化日期
            String sql = "SELECT " +
                    "record_id as recordId, " +
                    "student_id as studentId, " +
                    "book_id as bookId, " +
                    "book_isbn as bookIsbn, " +
                    "book_title as bookTitle, " +
                    "book_author as bookAuthor, " +
                    "book_category as bookCategory, " +
                    "publisher, " +
                    "publish_year as publishYear, " +
                    "DATE_FORMAT(borrow_date, '%Y-%m-%d') as borrowDate, " +  // 格式化日期
                    "DATE_FORMAT(due_date, '%Y-%m-%d') as dueDate, " +        // 格式化日期
                    "DATE_FORMAT(return_date, '%Y-%m-%d') as returnDate, " +  // 格式化日期
                    "created_at as createdAt " +
                    "FROM library_records " +
                    "ORDER BY record_id DESC";

            List<Map<String, Object>> books = jdbcTemplate.queryForList(sql);

            logger.info(" 成功获取 {} 条图书记录", books.size());

            // 调试：打印第一条数据的日期字段
            if (!books.isEmpty()) {
                Map<String, Object> firstBook = books.get(0);
                logger.info("第一条记录日期字段: borrow_date={}, due_date={}, return_date={}",
                        firstBook.get("borrowDate"),
                        firstBook.get("dueDate"),
                        firstBook.get("returnDate"));
            }

            return ResponseEntity.ok(BaseResponse.success("获取所有图书数据成功", books));

        } catch (Exception e) {
            logger.error(" 获取所有图书数据失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("获取所有图书数据失败"));
        }
    }

    // 管理员创建课程记录
    @PostMapping("/admin/courses")
    public ResponseEntity<BaseResponse<AcademicRecord>> createCourse(@RequestBody AcademicRecord record) {
        try {
            logger.info("创建课程成绩记录: 学号={}, 课程={}", record.getStudentId(), record.getCourseId());

            // 1. 验证必要字段
            if (record.getStudentId() == null || record.getStudentId().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("学号不能为空"));
            }
            if (record.getCourseId() == null || record.getCourseId().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("课程编号不能为空"));
            }
            if (record.getCourseName() == null || record.getCourseName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("课程名称不能为空"));
            }
            if (record.getAcademicYear() == null || record.getAcademicYear().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("学年不能为空"));
            }
            if (record.getSemester() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("学期不能为空"));
            }
            if (record.getScore() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("成绩不能为空"));
            }
            if (record.getCreditEarned() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("学分不能为空"));
            }

            // 2. 自动处理学分逻辑：如果成绩<60，学分强制设为0
            double originalCredit = record.getCreditEarned();
            if (record.getScore() < 60) {
                logger.info("成绩低于60分，自动将学分设为0: 学号={}, 课程={}, 成绩={}, 原学分={}",
                        record.getStudentId(), record.getCourseId(),
                        record.getScore(), originalCredit);
                record.setCreditEarned(0.0);
            }

            // 3. 检查记录是否已存在（根据唯一约束）
            boolean recordExists = academicRecordService.recordExists(
                    record.getStudentId(),
                    record.getCourseId(),
                    record.getAcademicYear(),
                    record.getSemester()
            );

            if (recordExists) {
                logger.warn("课程成绩记录已存在: 学号={}, 课程={}, 学年={}, 学期={}",
                        record.getStudentId(), record.getCourseId(),
                        record.getAcademicYear(), record.getSemester());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(BaseResponse.error("该学生在此学年的该课程成绩已存在"));
            }

            // 4. 计算绩点
            record.setGradePoint(calculateGradePoint(record.getScore()));

            // 5. 设置创建和更新时间
            record.setCreatedAt(LocalDateTime.now());
            record.setUpdatedAt(LocalDateTime.now());

            // 6. 保存记录
            AcademicRecord savedRecord = academicRecordService.saveRecord(record);

            logger.info("课程成绩记录创建成功: ID={}, 学号={}, 课程={}, 成绩={}, 绩点={}, 学分={}",
                    savedRecord.getRecordId(), savedRecord.getStudentId(),
                    savedRecord.getCourseId(), savedRecord.getScore(),
                    savedRecord.getGradePoint(), savedRecord.getCreditEarned());

            return ResponseEntity.ok(BaseResponse.success("创建课程成绩记录成功", savedRecord));
        } catch (Exception e) {
            logger.error("创建课程成绩记录失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("创建课程成绩记录失败: " + e.getMessage()));
        }
    }

    // 管理员更新课程记录
    @PutMapping("/admin/courses/{id}")
    public ResponseEntity<BaseResponse<AcademicRecord>> updateCourse(@PathVariable Long id, @RequestBody AcademicRecord record) {
        try {
            logger.info("更新课程成绩记录: ID={}", id);

            // 1. 检查记录是否存在
            Optional<AcademicRecord> existingRecord = academicRecordService.getRecordById(id);
            if (existingRecord.isEmpty()) {
                logger.warn("课程成绩记录不存在: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(BaseResponse.error("课程成绩记录不存在"));
            }

            // 2. 验证必要字段
            if (record.getStudentId() == null || record.getStudentId().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("学号不能为空"));
            }
            if (record.getCourseId() == null || record.getCourseId().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("课程编号不能为空"));
            }
            if (record.getCourseName() == null || record.getCourseName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("课程名称不能为空"));
            }
            if (record.getAcademicYear() == null || record.getAcademicYear().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("学年不能为空"));
            }
            if (record.getSemester() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("学期不能为空"));
            }
            if (record.getScore() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("成绩不能为空"));
            }
            if (record.getCreditEarned() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("学分不能为空"));
            }

            // 3. 自动处理学分逻辑：如果成绩<60，学分强制设为0
            double originalCredit = record.getCreditEarned();
            if (record.getScore() < 60) {
                logger.info("成绩低于60分，自动将学分设为0: 学号={}, 课程={}, 成绩={}, 原学分={}",
                        record.getStudentId(), record.getCourseId(),
                        record.getScore(), originalCredit);
                record.setCreditEarned(0.0);
            }

            // 4. 设置ID
            record.setRecordId(id);

            // 5. 计算绩点
            record.setGradePoint(calculateGradePoint(record.getScore()));

            // 6. 设置更新时间
            record.setUpdatedAt(LocalDateTime.now());
            // 保留创建时间
            record.setCreatedAt(existingRecord.get().getCreatedAt());

            // 7. 保存更新
            AcademicRecord updatedRecord = academicRecordService.saveRecord(record);

            logger.info("课程成绩记录更新成功: ID={}, 学号={}, 课程={}, 成绩={}, 绩点={}, 学分={}",
                    updatedRecord.getRecordId(), updatedRecord.getStudentId(),
                    updatedRecord.getCourseId(), updatedRecord.getScore(),
                    updatedRecord.getGradePoint(), updatedRecord.getCreditEarned());

            return ResponseEntity.ok(BaseResponse.success("更新课程成绩记录成功", updatedRecord));
        } catch (Exception e) {
            logger.error("更新课程成绩记录失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("更新课程成绩记录失败: " + e.getMessage()));
        }
    }

    // 绩点计算方法
    private Double calculateGradePoint(Double score) {
        if (score == null) return 0.0;
        if (score == 100) return 5.0;
        if (score >= 95) return 4.5;
        if (score >= 90) return 4.0;
        if (score >= 85) return 3.5;
        if (score >= 80) return 3.0;
        if (score >= 75) return 2.5;
        if (score >= 70) return 2.0;
        if (score >= 65) return 1.5;
        if (score >= 60) return 1.0;
        return 0.0;
    }

    // 管理员删除课程记录
    @DeleteMapping("/admin/courses/{id}")
    public ResponseEntity<BaseResponse<String>> deleteCourse(@PathVariable Long id) {
        try {
            logger.info("删除课程记录: {}", id);
            academicRecordService.deleteRecord(id);
            return ResponseEntity.ok(BaseResponse.success("删除课程记录成功"));
        } catch (Exception e) {
            logger.error("删除课程记录失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("删除课程记录失败"));
        }
    }

    // 管理员创建实验室记录 - 根据实际字段修改
    @PostMapping("/admin/labs")
    public ResponseEntity<BaseResponse<Map<String, Object>>> createLabRecord(@RequestBody Map<String, Object> request) {
        try {
            logger.info("创建实验室记录请求: {}", request);

            // 1. 提取并验证必要字段
            String studentId = (String) request.get("studentId");
            String labId = (String) request.get("labId");
            String labName = (String) request.get("labName");
            String experimentName = (String) request.get("experimentName");
            String checkInTimeStr = (String) request.get("checkInTime");
            String checkOutTimeStr = (String) request.get("checkOutTime");
            Integer durationMinutes = null;

            // 处理 durationMinutes 字段
            if (request.get("durationMinutes") != null) {
                Object durationObj = request.get("durationMinutes");
                if (durationObj instanceof Integer) {
                    durationMinutes = (Integer) durationObj;
                } else if (durationObj instanceof Number) {
                    durationMinutes = ((Number) durationObj).intValue();
                } else if (durationObj instanceof String) {
                    try {
                        durationMinutes = Integer.parseInt((String) durationObj);
                    } catch (NumberFormatException e) {
                        // 忽略转换错误
                    }
                }
            }

            // 2. 验证必要字段
            if (studentId == null || studentId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("学号不能为空"));
            }
            if (labId == null || labId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("实验室编号不能为空"));
            }
            if (labName == null || labName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("实验室名称不能为空"));
            }
            if (checkInTimeStr == null || checkInTimeStr.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("签到时间不能为空"));
            }

            // 3. 解析时间字符串
            LocalDateTime checkInTime;
            LocalDateTime checkOutTime = null;

            try {
                // 处理前端传来的 datetime-local 格式 (YYYY-MM-DDTHH:mm)
                checkInTimeStr = checkInTimeStr.replace(" ", "T");
                if (!checkInTimeStr.contains("T")) {
                    // 如果没有T，说明可能不是标准格式，尝试添加
                    checkInTimeStr = checkInTimeStr.replaceFirst(" ", "T");
                }
                checkInTime = LocalDateTime.parse(checkInTimeStr);

                if (checkOutTimeStr != null && !checkOutTimeStr.isEmpty()) {
                    checkOutTimeStr = checkOutTimeStr.replace(" ", "T");
                    if (!checkOutTimeStr.contains("T")) {
                        checkOutTimeStr = checkOutTimeStr.replaceFirst(" ", "T");
                    }
                    checkOutTime = LocalDateTime.parse(checkOutTimeStr);
                }
            } catch (Exception e) {
                logger.error("时间解析失败: {}, 输入时间: {}", e.getMessage(), checkInTimeStr);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("时间格式不正确，请使用 YYYY-MM-DD HH:mm 或 YYYY-MM-DDTHH:mm 格式"));
            }

            // 4. 验证时间逻辑
            if (checkOutTime != null && checkOutTime.isBefore(checkInTime)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("签退时间不能早于签到时间"));
            }

            // 5. 创建实验室记录实体
            LabAttendance labRecord = new LabAttendance();
            labRecord.setStudentId(studentId);
            labRecord.setLabId(labId);
            labRecord.setLabName(labName);
            labRecord.setExperimentName(experimentName);
            labRecord.setCheckInTime(checkInTime);
            labRecord.setCheckOutTime(checkOutTime);
            labRecord.setDurationMinutes(durationMinutes);
            labRecord.setNotes((String) request.get("notes") != null ? (String) request.get("notes") : (String) request.get("remark"));
            labRecord.setCreatedAt(LocalDateTime.now());

            // 6. 保存记录
            LabAttendance savedRecord = labAttendanceService.saveLabRecord(labRecord);

            logger.info("实验室记录创建成功: ID={}, 学号={}, 实验室={}",
                    savedRecord.getAttendanceId(), studentId, labName);

            // 7. 返回格式化后的数据
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("attendanceId", savedRecord.getAttendanceId());
            responseData.put("studentId", savedRecord.getStudentId());
            responseData.put("labId", savedRecord.getLabId());
            responseData.put("labName", savedRecord.getLabName());
            responseData.put("experimentName", savedRecord.getExperimentName());
            responseData.put("checkInTime", savedRecord.getCheckInTime());
            responseData.put("checkOutTime", savedRecord.getCheckOutTime());
            responseData.put("durationMinutes", savedRecord.getDurationMinutes());
            responseData.put("notes", savedRecord.getNotes());
            responseData.put("createdAt", savedRecord.getCreatedAt());

            return ResponseEntity.ok(BaseResponse.success("创建实验室记录成功", responseData));
        } catch (Exception e) {
            logger.error("创建实验室记录失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("创建实验室记录失败: " + e.getMessage()));
        }
    }

    // 管理员更新实验室记录
    @PutMapping("/admin/labs/{id}")
    public ResponseEntity<BaseResponse<Map<String, Object>>> updateLabRecord(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        try {
            logger.info("更新实验室记录: ID={}, 请求数据: {}", id, request);

            // 1. 检查记录是否存在
            Optional<LabAttendance> existingRecord = labAttendanceService.getLabRecordById(id);
            if (existingRecord.isEmpty()) {
                logger.warn("实验室记录不存在: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(BaseResponse.error("实验室记录不存在"));
            }

            // 2. 提取并验证字段
            String studentId = (String) request.get("studentId");
            String labId = (String) request.get("labId");
            String labName = (String) request.get("labName");
            String experimentName = (String) request.get("experimentName");
            String checkInTimeStr = (String) request.get("checkInTime");
            String checkOutTimeStr = (String) request.get("checkOutTime");
            Integer durationMinutes = null;

            // 处理 durationMinutes 字段
            if (request.get("durationMinutes") != null) {
                Object durationObj = request.get("durationMinutes");
                if (durationObj instanceof Integer) {
                    durationMinutes = (Integer) durationObj;
                } else if (durationObj instanceof Number) {
                    durationMinutes = ((Number) durationObj).intValue();
                } else if (durationObj instanceof String) {
                    try {
                        durationMinutes = Integer.parseInt((String) durationObj);
                    } catch (NumberFormatException e) {
                        // 忽略转换错误
                    }
                }
            }

            // 3. 验证必要字段
            if (studentId == null || studentId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("学号不能为空"));
            }
            if (labId == null || labId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("实验室编号不能为空"));
            }
            if (labName == null || labName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("实验室名称不能为空"));
            }
            if (checkInTimeStr == null || checkInTimeStr.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("签到时间不能为空"));
            }

            // 4. 解析时间字符串
            LocalDateTime checkInTime;
            LocalDateTime checkOutTime = null;

            try {
                // 处理前端传来的 datetime-local 格式
                checkInTimeStr = checkInTimeStr.replace(" ", "T");
                if (!checkInTimeStr.contains("T")) {
                    checkInTimeStr = checkInTimeStr.replaceFirst(" ", "T");
                }
                checkInTime = LocalDateTime.parse(checkInTimeStr);

                if (checkOutTimeStr != null && !checkOutTimeStr.isEmpty()) {
                    checkOutTimeStr = checkOutTimeStr.replace(" ", "T");
                    if (!checkOutTimeStr.contains("T")) {
                        checkOutTimeStr = checkOutTimeStr.replaceFirst(" ", "T");
                    }
                    checkOutTime = LocalDateTime.parse(checkOutTimeStr);
                }
            } catch (Exception e) {
                logger.error("时间解析失败: {}, 输入时间: {}", e.getMessage(), checkInTimeStr);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("时间格式不正确，请使用 YYYY-MM-DD HH:mm 或 YYYY-MM-DDTHH:mm 格式"));
            }

            // 5. 验证时间逻辑
            if (checkOutTime != null && checkOutTime.isBefore(checkInTime)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("签退时间不能早于签到时间"));
            }

            // 6. 更新记录
            LabAttendance labRecord = existingRecord.get();
            labRecord.setStudentId(studentId);
            labRecord.setLabId(labId);
            labRecord.setLabName(labName);
            labRecord.setExperimentName(experimentName);
            labRecord.setCheckInTime(checkInTime);
            labRecord.setCheckOutTime(checkOutTime);
            labRecord.setDurationMinutes(durationMinutes);
            labRecord.setNotes((String) request.get("notes") != null ? (String) request.get("notes") : (String) request.get("remark"));

            // 7. 保存更新
            LabAttendance updatedRecord = labAttendanceService.saveLabRecord(labRecord);

            logger.info("实验室记录更新成功: ID={}, 学号={}, 实验室={}",
                    id, studentId, labName);

            // 8. 返回格式化后的数据
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("attendanceId", updatedRecord.getAttendanceId());
            responseData.put("studentId", updatedRecord.getStudentId());
            responseData.put("labId", updatedRecord.getLabId());
            responseData.put("labName", updatedRecord.getLabName());
            responseData.put("experimentName", updatedRecord.getExperimentName());
            responseData.put("checkInTime", updatedRecord.getCheckInTime());
            responseData.put("checkOutTime", updatedRecord.getCheckOutTime());
            responseData.put("durationMinutes", updatedRecord.getDurationMinutes());
            responseData.put("notes", updatedRecord.getNotes());
            responseData.put("createdAt", updatedRecord.getCreatedAt());

            return ResponseEntity.ok(BaseResponse.success("更新实验室记录成功", responseData));
        } catch (Exception e) {
            logger.error("更新实验室记录失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("更新实验室记录失败: " + e.getMessage()));
        }
    }

    // 管理员删除实验室记录
    @DeleteMapping("/admin/labs/{id}")
    public ResponseEntity<BaseResponse<String>> deleteLabRecord(@PathVariable Long id) {
        try {
            logger.info("删除实验室记录: {}", id);

            // 检查记录是否存在
            Optional<LabAttendance> existingRecord = labAttendanceService.getLabRecordById(id);
            if (existingRecord.isEmpty()) {
                logger.warn("实验室记录不存在: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(BaseResponse.error("实验室记录不存在"));
            }

            // 获取记录信息用于日志
            LabAttendance labRecord = existingRecord.get();
            logger.info("删除实验室记录 - 学号: {}, 实验室: {}, 实验项目: {}",
                    labRecord.getStudentId(), labRecord.getLabName(), labRecord.getExperimentName());

            // 删除记录
            labAttendanceService.deleteLabRecord(id);

            logger.info("实验室记录删除成功: {}", id);
            return ResponseEntity.ok(BaseResponse.success("删除实验室记录成功"));
        } catch (Exception e) {
            logger.error("删除实验室记录失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("删除实验室记录失败"));
        }
    }

    // 管理员创建借阅记录
    @PostMapping("/admin/books")
    public ResponseEntity<BaseResponse<Map<String, Object>>> createLibraryRecord(@RequestBody Map<String, Object> request) {
        try {
            logger.info("创建借阅记录请求: {}", request);

            // 1. 提取并验证字段
            String studentId = (String) request.get("student_id");
            String bookId = (String) request.get("book_id");
            String bookTitle = (String) request.get("book_title");
            String borrowDateStr = (String) request.get("borrow_date");
            String dueDateStr = (String) request.get("due_date");
            String returnDateStr = (String) request.get("return_date");

            // 2. 验证必要字段
            if (studentId == null || studentId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("学号不能为空"));
            }
            if (bookId == null || bookId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("图书ID不能为空"));
            }
            if (bookTitle == null || bookTitle.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("书名不能为空"));
            }
            if (borrowDateStr == null || borrowDateStr.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("借阅日期不能为空"));
            }
            if (dueDateStr == null || dueDateStr.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("应还日期不能为空"));
            }

            // 3. 使用SQL直接插入记录
            String sql = "INSERT INTO library_records (" +
                    "student_id, book_id, book_isbn, book_title, book_author, " +
                    "book_category, publisher, publish_year, borrow_date, due_date, return_date" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try {
                jdbcTemplate.update(sql,
                        studentId,
                        bookId,
                        request.get("book_isbn"),
                        bookTitle,
                        request.get("book_author"),
                        request.get("book_category"),
                        request.get("publisher"),
                        request.get("publish_year"),
                        borrowDateStr,
                        dueDateStr,
                        returnDateStr
                );
            } catch (Exception e) {
                logger.error("插入图书记录失败: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(BaseResponse.error("创建借阅记录失败: " + e.getMessage()));
            }

            // 4. 查询刚创建的记录返回
            String selectSql = "SELECT " +
                    "record_id as recordId, " +
                    "student_id as studentId, " +
                    "book_id as bookId, " +
                    "book_isbn as bookIsbn, " +
                    "book_title as bookTitle, " +
                    "book_author as bookAuthor, " +
                    "book_category as bookCategory, " +
                    "publisher, " +
                    "publish_year as publishYear, " +
                    "DATE_FORMAT(borrow_date, '%Y-%m-%d') as borrowDate, " +
                    "DATE_FORMAT(due_date, '%Y-%m-%d') as dueDate, " +
                    "DATE_FORMAT(return_date, '%Y-%m-%d') as returnDate, " +
                    "created_at as createdAt " +
                    "FROM library_records " +
                    "WHERE student_id = ? AND book_id = ? " +
                    "ORDER BY record_id DESC " +
                    "LIMIT 1";

            List<Map<String, Object>> createdRecords = jdbcTemplate.queryForList(selectSql, studentId, bookId);

            if (createdRecords.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(BaseResponse.error("创建成功但无法查询到记录"));
            }

            logger.info("借阅记录创建成功: 学号={}, 书名={}", studentId, bookTitle);
            return ResponseEntity.ok(BaseResponse.success("创建借阅记录成功", createdRecords.get(0)));

        } catch (Exception e) {
            logger.error("创建借阅记录失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("创建借阅记录失败: " + e.getMessage()));
        }
    }

    // 管理员更新借阅记录
    @PutMapping("/admin/books/{id}")
    public ResponseEntity<BaseResponse<Map<String, Object>>> updateLibraryRecord(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        try {
            logger.info("更新借阅记录: ID={}, 请求数据: {}", id, request);

            // 1. 检查记录是否存在
            String checkSql = "SELECT COUNT(*) FROM library_records WHERE record_id = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
            if (count == null || count == 0) {
                logger.warn("借阅记录不存在: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(BaseResponse.error("借阅记录不存在"));
            }

            // 2. 验证必要字段
            String studentId = (String) request.get("student_id");
            String bookId = (String) request.get("book_id");
            String bookTitle = (String) request.get("book_title");
            String borrowDateStr = (String) request.get("borrow_date");
            String dueDateStr = (String) request.get("due_date");
            String returnDateStr = (String) request.get("return_date");

            if (studentId == null || studentId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("学号不能为空"));
            }
            if (bookId == null || bookId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("图书ID不能为空"));
            }
            if (bookTitle == null || bookTitle.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("书名不能为空"));
            }
            if (borrowDateStr == null || borrowDateStr.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("借阅日期不能为空"));
            }
            if (dueDateStr == null || dueDateStr.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("应还日期不能为空"));
            }

            // 3. 更新记录
            String updateSql = "UPDATE library_records SET " +
                    "student_id = ?, " +
                    "book_id = ?, " +
                    "book_isbn = ?, " +
                    "book_title = ?, " +
                    "book_author = ?, " +
                    "book_category = ?, " +
                    "publisher = ?, " +
                    "publish_year = ?, " +
                    "borrow_date = ?, " +
                    "due_date = ?, " +
                    "return_date = ? " +
                    "WHERE record_id = ?";

            try {
                jdbcTemplate.update(updateSql,
                        studentId,
                        bookId,
                        request.get("book_isbn"),
                        bookTitle,
                        request.get("book_author"),
                        request.get("book_category"),
                        request.get("publisher"),
                        request.get("publish_year"),
                        borrowDateStr,
                        dueDateStr,
                        returnDateStr,
                        id
                );
            } catch (Exception e) {
                logger.error("更新图书记录失败: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(BaseResponse.error("更新借阅记录失败: " + e.getMessage()));
            }

            // 4. 查询更新后的记录
            String selectSql = "SELECT " +
                    "record_id as recordId, " +
                    "student_id as studentId, " +
                    "book_id as bookId, " +
                    "book_isbn as bookIsbn, " +
                    "book_title as bookTitle, " +
                    "book_author as bookAuthor, " +
                    "book_category as bookCategory, " +
                    "publisher, " +
                    "publish_year as publishYear, " +
                    "DATE_FORMAT(borrow_date, '%Y-%m-%d') as borrowDate, " +
                    "DATE_FORMAT(due_date, '%Y-%m-%d') as dueDate, " +
                    "DATE_FORMAT(return_date, '%Y-%m-%d') as returnDate, " +
                    "created_at as createdAt " +
                    "FROM library_records " +
                    "WHERE record_id = ?";

            List<Map<String, Object>> updatedRecords = jdbcTemplate.queryForList(selectSql, id);

            if (updatedRecords.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(BaseResponse.error("更新成功但无法查询到记录"));
            }

            logger.info("借阅记录更新成功: ID={}, 学号={}, 书名={}", id, studentId, bookTitle);
            return ResponseEntity.ok(BaseResponse.success("更新借阅记录成功", updatedRecords.get(0)));

        } catch (Exception e) {
            logger.error("更新借阅记录失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("更新借阅记录失败: " + e.getMessage()));
        }
    }
    @DeleteMapping("/admin/books/{id}")
    public ResponseEntity<BaseResponse<String>> deleteBookRecord(@PathVariable Long id) {
        try {
            String deleteSql = "DELETE FROM library_records WHERE record_id = ?";
            jdbcTemplate.update(deleteSql, id);
            return ResponseEntity.ok(BaseResponse.success("删除借阅记录成功"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("删除借阅记录失败"));
        }
    }
    // 从请求中提取token
    private String extractTokenFromRequest() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        logger.warn("Authorization header 不存在或格式错误");
        return null;
    }

    // 从token中提取学生ID的方法
    private String extractStudentIdFromToken(String token) {
        try {
            if (token == null) {
                logger.warn("Token为空");
                return null;
            }

            // 使用 JwtUtil 解析用户名，然后从数据库获取学生ID
            String username = jwtUtil.getUsernameFromToken(token);
            if (username != null) {
                // 根据用户名查询用户信息
                Optional<User> userOpt = userRepository.findByUsername(username);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    // 如果用户没有学生ID，使用用户ID作为默认值
                    if (user.getStudentId() != null && !user.getStudentId().isEmpty()) {
                        return user.getStudentId();
                    } else {
                        // 如果没有设置学生ID，使用用户ID作为默认值
                        String generatedStudentId = "STU" + String.format("%06d", user.getId());
                        logger.info("为用户生成学生ID: {}", generatedStudentId);
                        return generatedStudentId;
                    }
                } else {
                    logger.warn("用户不存在: {}", username);
                }
            } else {
                logger.warn("无法从token中解析用户名");
            }

            // 如果无法从数据库获取，返回默认学生ID
            logger.info("使用默认学生ID");
            return "2023001001";
        } catch (Exception e) {
            logger.error("提取学生ID异常: {}", e.getMessage());
            // 发生异常时返回默认学生ID
            return "2023001001";
        }
    }

    @GetMapping("/current/overview")
    public ResponseEntity<BaseResponse<AcademicOverviewVO>> getAcademicOverview() {
        try {
            String token = extractTokenFromRequest();
            String studentId = extractStudentIdFromToken(token);

            if (studentId == null) {
                logger.warn("未授权访问学业概览接口");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.error("未授权访问"));
            }

            logger.info("获取学业概览，学生ID: {}", studentId);

            // 1. 直接获取课程数据（不调用有问题的Service方法）
            List<AcademicRecord> courses;
            try {
                courses = academicRecordRepository.findByStudentIdOrderByAcademicYearDescSemesterDesc(studentId);
                logger.info("直接查询数据库获取到 {} 条课程记录", courses.size());

                // 打印验证数据
                for (AcademicRecord course : courses) {
                    logger.info("课程数据 - 学年: {}, 学期: {}, 课程: {}",
                            course.getAcademicYear(), course.getSemester(), course.getCourseId());
                }
            } catch (Exception e) {
                logger.error("查询课程数据失败: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(BaseResponse.error("获取课程数据失败"));
            }

            if (courses == null || courses.isEmpty()) {
                logger.warn("学生 {} 没有课程数据", studentId);
                return ResponseEntity.ok(BaseResponse.success("获取学业概览成功", createEmptyOverview()));
            }

            // 2. 直接计算，不使用有问题的Service方法
            AcademicOverviewVO overview = calculateOverviewFromCoursesDirectly(studentId, courses);

            logger.info("计算完成 - 当前学期: {} 第{}学期",
                    overview.getCurrentAcademicYear(), overview.getCurrentSemester());

            return ResponseEntity.ok(BaseResponse.success("获取学业概览成功", overview));

        } catch (Exception e) {
            logger.error("获取学业概览异常: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("获取学业概览失败"));
        }
    }

    private AcademicOverviewVO calculateOverviewFromCoursesDirectly(String studentId, List<AcademicRecord> courses) {
        AcademicOverviewVO overview = new AcademicOverviewVO();

        // 使用特殊分隔符分组
        Map<String, SemesterData> semesterMap = new HashMap<>();

        for (AcademicRecord course : courses) {
            String academicYear = course.getAcademicYear();
            Integer semester = course.getSemester();

            if (academicYear == null || semester == null) {
                continue;
            }

            String key = academicYear + "|SEM|" + semester;
            SemesterData semesterData = semesterMap.get(key);

            if (semesterData == null) {
                semesterData = new SemesterData();
                semesterData.academicYear = academicYear;
                semesterData.semester = semester;
                semesterMap.put(key, semesterData);
            }

            // 累加数据 - 直接使用 Double
            if (course.getCreditEarned() != null && course.getGradePoint() != null) {
                try {
                    semesterData.totalCredits += course.getCreditEarned();
                    semesterData.totalGradePoints += (course.getGradePoint() * course.getCreditEarned());
                    semesterData.courseCount++;
                } catch (Exception e) {
                    logger.error("累加课程数据失败: {}", e.getMessage());
                }
            }
        }

        logger.info("分组后的学期数: {}", semesterMap.size());

        List<AcademicOverviewVO.SemesterGPA> semesterGPAs = new ArrayList<>();

        // 计算每个学期的数据
        for (SemesterData data : semesterMap.values()) {
            AcademicOverviewVO.SemesterGPA semesterGPA = new AcademicOverviewVO.SemesterGPA();
            semesterGPA.setAcademicYear(data.academicYear);
            semesterGPA.setSemester(data.semester);
            semesterGPA.setTotalCredits(data.totalCredits);
            semesterGPA.setCourseCount(data.courseCount);
            semesterGPA.setTotalGradePoints(data.totalGradePoints);

            if (data.totalCredits > 0) {
                double avgGPA = Math.round(data.totalGradePoints / data.totalCredits * 100.0) / 100.0;
                semesterGPA.setAverageGPA(avgGPA);
            } else {
                semesterGPA.setAverageGPA(0.0);
            }

            semesterGPAs.add(semesterGPA);

            logger.info("学期统计 - 学年: {}, 学期: {}, 课程数: {}, 学分: {}, GPA: {}",
                    data.academicYear, data.semester, data.courseCount,
                    data.totalCredits, semesterGPA.getAverageGPA());
        }

        // 排序：按学年学期倒序
        semesterGPAs.sort((a, b) -> {
            int yearCompare = b.getAcademicYear().compareTo(a.getAcademicYear());
            if (yearCompare != 0) {
                return yearCompare;
            }
            return b.getSemester().compareTo(a.getSemester());
        });

        // 设置当前学期
        if (!semesterGPAs.isEmpty()) {
            AcademicOverviewVO.SemesterGPA current = semesterGPAs.get(0);
            overview.setCurrentAcademicYear(current.getAcademicYear());
            overview.setCurrentSemester(current.getSemester());
            overview.setCurrentSemesterGPA(current.getAverageGPA());

            logger.info("当前学期设置 - 学年: {}, 学期: {}, GPA: {}",
                    current.getAcademicYear(), current.getSemester(), current.getAverageGPA());
        }

        // 计算总学分和总GPA
        double totalAllCredits = 0.0;
        double totalAllGradePoints = 0.0;

        for (AcademicOverviewVO.SemesterGPA semester : semesterGPAs) {
            totalAllCredits += semester.getTotalCredits();
            totalAllGradePoints += semester.getTotalGradePoints();
        }

        overview.setTotalCredits(totalAllCredits);
        overview.setSemesterGPAs(semesterGPAs);

        if (totalAllCredits > 0) {
            double overallGPA = Math.round(totalAllGradePoints / totalAllCredits * 100.0) / 100.0;
            overview.setOverallGPA(overallGPA);

            logger.info("总GPA计算 - 总学分: {}, 总积点: {}, GPA: {}",
                    totalAllCredits, totalAllGradePoints, overallGPA);
        } else {
            overview.setOverallGPA(0.0);
        }

        // 获取其他统计数据
        try {
            Long labCount = labAttendanceRepository.countByStudentId(studentId);
            overview.setLabProjectsCount(labCount != null ? labCount : 0L);
            logger.info("实验室项目数: {}", overview.getLabProjectsCount());

            Long borrowedBooks = libraryRecordRepository.countCurrentBorrowedByStudentId(studentId);
            overview.setBorrowedBooksCount(borrowedBooks != null ? borrowedBooks : 0L);
            logger.info("借阅书籍数: {}", overview.getBorrowedBooksCount());
        } catch (Exception e) {
            logger.error("获取统计数据失败: {}", e.getMessage());
            overview.setLabProjectsCount(0L);
            overview.setBorrowedBooksCount(0L);
        }

        return overview;
    }

    // 内部辅助类 - 修改为使用 Double
    private static class SemesterData {
        String academicYear;
        Integer semester;
        double totalCredits = 0.0;
        double totalGradePoints = 0.0;
        int courseCount = 0;
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
}