-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.40 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 ai_learning_partner 的数据库结构
CREATE DATABASE IF NOT EXISTS `ai_learning_partner` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ai_learning_partner`;

-- 导出  表 ai_learning_partner.academic_records 结构
CREATE TABLE IF NOT EXISTS `academic_records` (
  `record_id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) NOT NULL COMMENT '学号',
  `course_id` varchar(20) NOT NULL COMMENT '课程ID',
  `academic_year` varchar(9) NOT NULL COMMENT '学年，如: 2023-2024',
  `semester` tinyint NOT NULL COMMENT '学期: 1第一学期, 2第二学期',
  `score` decimal(5,2) DEFAULT NULL COMMENT '成绩(百分制)',
  `grade_point` decimal(3,2) DEFAULT NULL COMMENT '绩点',
  `grade_level` varchar(10) DEFAULT NULL COMMENT '等级: 优秀/良好/中等/及格/不及格',
  `class_rank` smallint DEFAULT NULL COMMENT '班级排名',
  `major_rank` smallint DEFAULT NULL COMMENT '专业排名',
  `exam_date` date DEFAULT NULL COMMENT '考试日期',
  `credit_earned` decimal(3,1) DEFAULT NULL COMMENT '获得学分',
  `is_retake` tinyint(1) DEFAULT '0' COMMENT '是否重修',
  `teacher_evaluation` text COMMENT '教师评价',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `uk_student_course_semester` (`student_id`,`course_id`,`academic_year`,`semester`),
  KEY `course_id` (`course_id`),
  KEY `idx_academic_year` (`academic_year`),
  KEY `idx_semester` (`semester`),
  KEY `idx_score` (`score`),
  CONSTRAINT `academic_records_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
  CONSTRAINT `academic_records_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生学业成绩记录表';

-- 正在导出表  ai_learning_partner.academic_records 的数据：~29 rows (大约)
REPLACE INTO `academic_records` (`record_id`, `student_id`, `course_id`, `academic_year`, `semester`, `score`, `grade_point`, `grade_level`, `class_rank`, `major_rank`, `exam_date`, `credit_earned`, `is_retake`, `teacher_evaluation`, `created_at`, `updated_at`) VALUES
	(1, '202301001', 'CS101', '2023-2024', 1, 85.50, 3.50, '良好', 5, 12, '2024-01-15', 3.0, 0, '学习认真，基础扎实', '2025-09-29 07:09:17', '2025-09-29 07:09:42'),
	(2, '202301001', 'MA101', '2023-2024', 1, 92.00, 4.00, '优秀', 3, 8, '2024-01-20', 5.0, 0, '数学思维能力强', '2025-09-29 07:09:17', '2025-09-29 07:09:45'),
	(3, '202301001', 'GE101', '2023-2024', 1, 78.00, 2.80, '中等', 8, 25, '2024-01-10', 2.0, 0, '英语需要加强练习', '2025-09-29 07:09:17', '2025-09-29 07:09:47'),
	(4, '202301001', 'CS102', '2023-2024', 2, 88.00, 3.80, '良好', 4, 10, '2024-06-20', 4.0, 0, '编程能力突出', '2025-09-29 07:09:17', '2025-09-29 07:09:49'),
	(5, '202301001', 'MA102', '2023-2024', 2, 90.50, 4.00, '优秀', 2, 6, '2024-06-25', 3.0, 0, '线性代数掌握很好', '2025-09-29 07:09:17', '2025-09-29 07:09:52'),
	(6, '202301001', 'GE102', '2023-2024', 2, 82.00, 3.20, '良好', 6, 18, '2024-06-15', 3.0, 0, '物理实验表现优秀', '2025-09-29 07:09:17', '2025-09-29 07:09:54'),
	(7, '202301002', 'CS101', '2023-2024', 1, 91.00, 4.00, '优秀', 2, 5, '2024-01-15', 3.0, 0, '计算机基础很好', '2025-09-29 07:09:17', '2025-09-29 07:09:56'),
	(8, '202301002', 'MA101', '2023-2024', 1, 87.50, 3.70, '良好', 4, 11, '2024-01-20', 5.0, 0, '计算准确，思路清晰', '2025-09-29 07:09:17', '2025-09-29 07:09:57'),
	(9, '202301002', 'GE101', '2023-2024', 1, 85.00, 3.50, '良好', 5, 15, '2024-01-10', 2.0, 0, '英语口语表达流利', '2025-09-29 07:09:17', '2025-09-29 07:09:59'),
	(10, '202301002', 'CS102', '2023-2024', 2, 94.00, 4.00, '优秀', 1, 3, '2024-06-20', 4.0, 0, 'C语言编程能力很强', '2025-09-29 07:09:17', '2025-09-29 07:10:01'),
	(11, '202301002', 'MA102', '2023-2024', 2, 89.00, 3.90, '良好', 3, 9, '2024-06-25', 3.0, 0, '矩阵运算掌握熟练', '2025-09-29 07:09:17', '2025-09-29 07:10:03'),
	(12, '202301002', 'GE102', '2023-2024', 2, 83.50, 3.30, '良好', 5, 16, '2024-06-15', 3.0, 0, '实验操作规范', '2025-09-29 07:09:17', '2025-09-29 07:10:04'),
	(13, '202301003', 'CS101', '2023-2024', 1, 76.00, 2.60, '中等', 10, 30, '2024-01-15', 3.0, 0, '需要加强基础知识学习', '2025-09-29 07:09:17', '2025-09-29 07:10:07'),
	(14, '202301003', 'MA101', '2023-2024', 1, 81.00, 3.10, '良好', 7, 22, '2024-01-20', 5.0, 0, '进步明显，继续努力', '2025-09-29 07:09:17', '2025-09-29 07:10:09'),
	(15, '202301003', 'GE101', '2023-2024', 1, 79.50, 2.90, '中等', 9, 28, '2024-01-10', 2.0, 0, '英语阅读能力较好', '2025-09-29 07:09:17', '2025-09-29 07:10:11'),
	(16, '202302001', 'CS101', '2023-2024', 1, 88.50, 3.80, '良好', 3, 8, '2024-01-15', 3.0, 0, '对计算机有浓厚兴趣', '2025-09-29 07:09:17', '2025-09-29 07:10:12'),
	(17, '202302001', 'MA101', '2023-2024', 1, 84.00, 3.40, '良好', 6, 17, '2024-01-20', 5.0, 0, '微积分概念理解到位', '2025-09-29 07:09:17', '2025-09-29 07:10:14'),
	(18, '202302001', 'SE101', '2023-2024', 1, 92.50, 4.00, '优秀', 1, 2, '2024-01-18', 2.0, 0, '软件工程思维很好', '2025-09-29 07:09:17', '2025-09-29 07:10:15'),
	(19, '202302002', 'CS101', '2023-2024', 1, 95.00, 4.00, '优秀', 1, 1, '2024-01-15', 3.0, 0, '计算机基础知识全面', '2025-09-29 07:09:17', '2025-09-29 07:10:17'),
	(20, '202302002', 'MA101', '2023-2024', 1, 93.00, 4.00, '优秀', 2, 4, '2024-01-20', 5.0, 0, '数学成绩优异', '2025-09-29 07:09:17', '2025-09-29 07:10:19'),
	(21, '202302002', 'SE101', '2023-2024', 1, 90.00, 4.00, '优秀', 2, 5, '2024-01-18', 2.0, 0, '软件开发流程理解深入', '2025-09-29 07:09:17', '2025-09-29 07:10:21'),
	(22, '202201001', 'CS101', '2022-2023', 1, 65.00, 1.50, '及格', 25, 60, '2023-01-20', 3.0, 0, '基础知识薄弱', '2025-09-29 07:09:17', '2025-09-29 07:10:25'),
	(23, '202201001', 'CS101', '2023-2024', 1, 82.00, 3.20, '良好', 6, 20, '2024-01-15', 3.0, 1, '重修后进步明显', '2025-09-29 07:09:17', '2025-09-29 07:10:32'),
	(24, '202201001', 'CS201', '2022-2023', 2, 87.00, 3.70, '良好', 4, 12, '2023-06-25', 4.0, 0, '数据结构掌握扎实', '2025-09-29 07:09:17', '2025-09-29 07:10:35'),
	(25, '202201001', 'CS202', '2022-2023', 2, 79.00, 2.90, '中等', 8, 25, '2023-06-28', 3.0, 0, '操作系统概念需要加强', '2025-09-29 07:09:17', '2025-09-29 07:10:36'),
	(26, '202201002', 'CS201', '2022-2023', 2, 91.00, 4.00, '优秀', 2, 6, '2023-06-25', 4.0, 0, '算法设计能力突出', '2025-09-29 07:09:17', '2025-09-29 07:10:39'),
	(27, '202201002', 'CS202', '2022-2023', 2, 94.00, 4.00, '优秀', 1, 3, '2023-06-28', 3.0, 0, '操作系统理解深入', '2025-09-29 07:09:17', '2025-09-29 07:10:41'),
	(28, '202101001', 'SE201', '2021-2022', 1, 89.00, 3.90, '良好', 3, 10, '2022-01-18', 4.0, 0, 'Java面向对象编程熟练', '2025-09-29 07:09:17', '2025-09-29 07:10:42'),
	(29, '202101001', 'SE301', '2021-2022', 2, 85.00, 3.50, '良好', 5, 15, '2022-06-20', 3.0, 0, 'Web开发技术应用能力较强', '2025-09-29 07:09:17', '2025-09-29 07:10:44');

-- 导出  表 ai_learning_partner.courses 结构
CREATE TABLE IF NOT EXISTS `courses` (
  `course_id` varchar(20) NOT NULL COMMENT '课程ID，如: CS101',
  `course_name` varchar(100) NOT NULL COMMENT '课程名称',
  `course_name_en` varchar(100) DEFAULT NULL COMMENT '课程英文名称',
  `credits` decimal(3,1) NOT NULL COMMENT '学分',
  `department` varchar(50) NOT NULL COMMENT '开课院系',
  `course_category` varchar(30) DEFAULT NULL COMMENT '课程类别: 必修/选修/通识',
  `difficulty_level` tinyint DEFAULT '3' COMMENT '难度等级 1-5',
  `total_hours` smallint DEFAULT NULL COMMENT '总学时',
  `theory_hours` smallint DEFAULT NULL COMMENT '理论学时',
  `practice_hours` smallint DEFAULT NULL COMMENT '实践学时',
  `syllabus_text` text COMMENT '教学大纲内容',
  `course_description` text COMMENT '课程描述',
  `assessment_method` varchar(50) DEFAULT NULL COMMENT '考核方式',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否激活',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`course_id`),
  KEY `idx_department` (`department`),
  KEY `idx_category` (`course_category`),
  KEY `idx_difficulty` (`difficulty_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程基本信息表';

-- 正在导出表  ai_learning_partner.courses 的数据：~15 rows (大约)
REPLACE INTO `courses` (`course_id`, `course_name`, `course_name_en`, `credits`, `department`, `course_category`, `difficulty_level`, `total_hours`, `theory_hours`, `practice_hours`, `syllabus_text`, `course_description`, `assessment_method`, `is_active`, `created_at`, `updated_at`) VALUES
	('CS101', '计算机导论', NULL, 3.0, '计算机学院', '必修', 3, 64, NULL, NULL, NULL, '计算机科学基础知识入门', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('CS102', 'C语言程序设计', NULL, 4.0, '计算机学院', '必修', 4, 80, NULL, NULL, NULL, 'C语言编程基础与算法', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('CS201', '数据结构', NULL, 4.0, '计算机学院', '必修', 4, 80, NULL, NULL, NULL, '线性表、树、图等数据结构', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('CS202', '操作系统', NULL, 3.0, '计算机学院', '必修', 4, 64, NULL, NULL, NULL, '进程管理、内存管理、文件系统', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('CS301', '数据库系统', NULL, 3.0, '计算机学院', '必修', 3, 64, NULL, NULL, NULL, 'SQL、数据库设计与优化', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('CS302', '计算机网络', NULL, 3.0, '计算机学院', '必修', 4, 64, NULL, NULL, NULL, 'TCP/IP协议、网络编程', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('GE101', '大学英语', NULL, 2.0, '外语学院', '必修', 2, 48, NULL, NULL, NULL, '英语听说读写训练', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('GE102', '大学物理', NULL, 3.0, '物理学院', '必修', 3, 64, NULL, NULL, NULL, '力学、热学、电磁学', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('GE201', '毛泽东思想和中国特色社会主义理论体系概论', NULL, 2.0, '马克思主义学院', '必修', 2, 48, NULL, NULL, NULL, '政治理论课程', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('MA101', '高等数学A', NULL, 5.0, '数学学院', '必修', 4, 96, NULL, NULL, NULL, '微积分、极限、导数', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('MA102', '线性代数', NULL, 3.0, '数学学院', '必修', 3, 64, NULL, NULL, NULL, '矩阵、向量空间、线性变换', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('MA201', '概率论与数理统计', NULL, 3.0, '数学学院', '必修', 4, 64, NULL, NULL, NULL, '概率分布、假设检验', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('SE101', '软件工程导论', NULL, 2.0, '软件学院', '必修', 2, 48, NULL, NULL, NULL, '软件开发流程与方法论', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('SE201', 'Java程序设计', NULL, 4.0, '软件学院', '必修', 3, 80, NULL, NULL, NULL, '面向对象编程、Java核心技术', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02'),
	('SE301', 'Web开发技术', NULL, 3.0, '软件学院', '选修', 3, 64, NULL, NULL, NULL, 'HTML/CSS/JavaScript前后端开发', NULL, 1, '2025-09-23 07:16:02', '2025-09-23 07:16:02');

-- 导出  表 ai_learning_partner.lab_attendance 结构
CREATE TABLE IF NOT EXISTS `lab_attendance` (
  `attendance_id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) NOT NULL COMMENT '学号',
  `lab_id` varchar(30) NOT NULL COMMENT '实验室ID',
  `lab_name` varchar(100) NOT NULL COMMENT '实验室名称',
  `course_id` varchar(20) DEFAULT NULL COMMENT '关联课程ID',
  `experiment_name` varchar(100) DEFAULT NULL COMMENT '实验名称',
  `check_in_time` datetime NOT NULL COMMENT '签到时间',
  `check_out_time` datetime DEFAULT NULL COMMENT '签退时间',
  `duration_minutes` int DEFAULT NULL COMMENT '停留时长(分钟)',
  `attendance_type` enum('course','self_study','competition') DEFAULT NULL COMMENT '类型: 课程实验/自主学习/竞赛',
  `equipment_used` json DEFAULT NULL COMMENT '使用设备列表',
  `supervisor` varchar(50) DEFAULT NULL COMMENT '指导老师',
  `notes` text COMMENT '备注',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`attendance_id`),
  KEY `student_id` (`student_id`),
  KEY `idx_check_in_time` (`check_in_time`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_attendance_type` (`attendance_type`),
  CONSTRAINT `lab_attendance_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
  CONSTRAINT `lab_attendance_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='实验室签到记录表';

-- 正在导出表  ai_learning_partner.lab_attendance 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.learning_progress 结构
CREATE TABLE IF NOT EXISTS `learning_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `chapter` varchar(255) DEFAULT NULL,
  `completion_rate` double DEFAULT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `study_date` datetime(6) DEFAULT NULL,
  `time_spent` int DEFAULT NULL,
  `plan_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK43twrx1cnnnjw5maaatcwc919` (`plan_id`),
  KEY `FKkex4pny6sl5affwroon8lfbjt` (`user_id`),
  CONSTRAINT `FK43twrx1cnnnjw5maaatcwc919` FOREIGN KEY (`plan_id`) REFERENCES `study_plans` (`id`),
  CONSTRAINT `FKkex4pny6sl5affwroon8lfbjt` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.learning_progress 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.library_records 结构
CREATE TABLE IF NOT EXISTS `library_records` (
  `record_id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) NOT NULL COMMENT '学号',
  `book_id` varchar(30) NOT NULL COMMENT '图书ID',
  `book_isbn` varchar(20) DEFAULT NULL COMMENT 'ISBN号',
  `book_title` varchar(200) NOT NULL COMMENT '书名',
  `book_author` varchar(100) DEFAULT NULL COMMENT '作者',
  `book_category` varchar(50) DEFAULT NULL COMMENT '图书分类',
  `publisher` varchar(100) DEFAULT NULL COMMENT '出版社',
  `publish_year` year DEFAULT NULL COMMENT '出版年份',
  `borrow_date` date NOT NULL COMMENT '借阅日期',
  `due_date` date NOT NULL COMMENT '应还日期',
  `return_date` date DEFAULT NULL COMMENT '实际归还日期',
  `renewal_count` tinyint DEFAULT '0' COMMENT '续借次数',
  `borrow_duration` int DEFAULT NULL COMMENT '借阅天数',
  `is_overdue` tinyint(1) DEFAULT '0' COMMENT '是否逾期',
  `related_course_id` varchar(20) DEFAULT NULL COMMENT '关联课程ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`record_id`),
  KEY `student_id` (`student_id`),
  KEY `idx_borrow_date` (`borrow_date`),
  KEY `idx_book_category` (`book_category`),
  KEY `idx_related_course` (`related_course_id`),
  CONSTRAINT `library_records_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
  CONSTRAINT `library_records_ibfk_2` FOREIGN KEY (`related_course_id`) REFERENCES `courses` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图书馆借阅记录表';

-- 正在导出表  ai_learning_partner.library_records 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.students 结构
CREATE TABLE IF NOT EXISTS `students` (
  `student_id` varchar(20) NOT NULL COMMENT '学号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `gender` enum('M','F','U') DEFAULT NULL COMMENT '性别: M男, F女, U未知',
  `major` varchar(50) NOT NULL COMMENT '专业',
  `college` varchar(50) NOT NULL COMMENT '学院',
  `grade` varchar(10) NOT NULL COMMENT '年级，如: 2023',
  `class_name` varchar(30) DEFAULT NULL COMMENT '班级',
  `enrollment_date` date DEFAULT NULL COMMENT '入学日期',
  `graduation_date` date DEFAULT NULL COMMENT '预计毕业日期',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `password` varchar(100) NOT NULL DEFAULT '123456' COMMENT '登录密码（明文存储）',
  `avatar_url` varchar(200) DEFAULT NULL COMMENT '头像URL',
  `learning_preferences` json DEFAULT NULL COMMENT '学习偏好设置',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`student_id`),
  KEY `idx_major` (`major`),
  KEY `idx_grade` (`grade`),
  KEY `idx_college` (`college`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生基本信息表';

-- 正在导出表  ai_learning_partner.students 的数据：~12 rows (大约)
REPLACE INTO `students` (`student_id`, `name`, `gender`, `major`, `college`, `grade`, `class_name`, `enrollment_date`, `graduation_date`, `email`, `phone`, `password`, `avatar_url`, `learning_preferences`, `created_at`, `updated_at`) VALUES
	('202101001', '李十三', 'M', '软件工程', '软件学院', '2021', '软工2101', '2021-09-01', NULL, 'lishisan@edu.cn', '13800138011', 'lishisan123', NULL, NULL, '2025-09-23 07:13:16', '2025-09-29 07:23:19'),
	('202101002', '张十四', 'F', '电子信息工程', '电子信息学院', '2021', '电信2101', '2021-09-01', NULL, 'zhangshisi@edu.cn', '13800138012', 'zhangshisi456', NULL, NULL, '2025-09-23 07:13:16', '2025-09-29 07:23:21'),
	('202201001', '郑十一', 'M', '计算机科学与技术', '计算机学院', '2022', '计科2201', '2022-09-01', NULL, 'zhengshiyi@edu.cn', '13800138009', 'zhengshiyi567', NULL, NULL, '2025-09-23 07:13:16', '2025-09-29 07:23:19'),
	('202201002', '王十二', 'F', '计算机科学与技术', '计算机学院', '2022', '计科2201', '2022-09-01', NULL, 'wangshier@edu.cn', '13800138010', 'wangshier890', NULL, NULL, '2025-09-23 07:13:16', '2025-09-29 07:23:19'),
	('202301001', '张三', 'M', '计算机科学与技术', '计算机学院', '2023', '计科2301', '2023-09-01', NULL, 'zhangsan@edu.cn', '13800138001', 'zhangsan123', NULL, NULL, '2025-09-23 07:13:16', '2025-09-29 07:23:19'),
	('202301002', '李四', 'M', '计算机科学与技术', '计算机学院', '2023', '计科2301', '2023-09-01', NULL, 'lisi@edu.cn', '13800138002', 'lisi456', NULL, NULL, '2025-09-23 07:13:16', '2025-09-29 07:23:19'),
	('202301003', '王五', 'F', '计算机科学与技术', '计算机学院', '2023', '计科2301', '2023-09-01', NULL, 'wangwu@edu.cn', '13800138003', 'wangwu789', NULL, NULL, '2025-09-23 07:13:16', '2025-09-29 07:23:19'),
	('202302001', '赵六', 'M', '软件工程', '软件学院', '2023', '软工2301', '2023-09-01', NULL, 'zhaoliu@edu.cn', '13800138004', 'zhaoliu012', NULL, NULL, '2025-09-23 07:13:16', '2025-09-29 07:23:19'),
	('202302002', '钱七', 'F', '软件工程', '软件学院', '2023', '软工2301', '2023-09-01', NULL, 'qianqi@edu.cn', '13800138005', 'qianqi345', NULL, NULL, '2025-09-23 07:13:16', '2025-09-29 07:23:19'),
	('202302003', '孙八', 'M', '软件工程', '软件学院', '2023', '软工2301', '2023-09-01', NULL, 'sunba@edu.cn', '13800138006', 'sunba678', NULL, NULL, '2025-09-23 07:13:16', '2025-09-29 07:23:19'),
	('202303001', '周九', 'F', '电子信息工程', '电子信息学院', '2023', '电信2301', '2023-09-01', NULL, 'zhoujiu@edu.cn', '13800138007', 'zhoujiu901', NULL, NULL, '2025-09-23 07:13:16', '2025-09-29 07:23:19'),
	('202303002', '吴十', 'M', '电子信息工程', '电子信息学院', '2023', '电信2301', '2023-09-01', NULL, 'wushi@edu.cn', '13800138008', 'wushi234', NULL, NULL, '2025-09-23 07:13:16', '2025-09-29 07:23:19');

-- 导出  表 ai_learning_partner.study_plans 结构
CREATE TABLE IF NOT EXISTS `study_plans` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_name` varchar(255) DEFAULT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `plan_name` varchar(255) NOT NULL,
  `start_date` date DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsmw1bta843doo6k3a23lskryk` (`user_id`),
  CONSTRAINT `FKsmw1bta843doo6k3a23lskryk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.study_plans 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.users 结构
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `student_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `account_status` varchar(20) DEFAULT 'ACTIVE',
  `avatar_url` varchar(255) DEFAULT NULL,
  `class_name` varchar(255) DEFAULT NULL,
  `college` varchar(255) DEFAULT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `grade` varchar(255) DEFAULT NULL,
  `last_login_time` datetime(6) DEFAULT NULL,
  `learning_goal` varchar(255) DEFAULT NULL,
  `login_count` int DEFAULT '0',
  `major` varchar(255) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKqh3otyipv2k9hqte4a1abcyhq` (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.users 的数据：~4 rows (大约)
REPLACE INTO `users` (`id`, `username`, `name`, `student_id`, `password`, `account_status`, `avatar_url`, `class_name`, `college`, `create_time`, `email`, `grade`, `last_login_time`, `learning_goal`, `login_count`, `major`, `update_time`) VALUES
	(1, '202301001', '张三', '202301001', '123456', 'ACTIVE', 'https://picsum.photos/100/100?random=10', '计科2301', '计算机学院', '2025-09-29 11:06:47.374407', 'zhangsan@edu.cn', '大三', NULL, '成为全栈工程师', 0, '计算机科学与技术', '2025-09-29 11:06:47.375593'),
	(2, '202101001', '李十三', '202101001', '123456', 'ACTIVE', NULL, '软工2101', '软件学院', '2025-09-29 11:06:47.440725', 'lishisan@edu.cn', '大四', NULL, '深入学习人工智能', 0, '软件工程', '2025-09-29 11:06:47.440725'),
	(3, '202301002', '李四', NULL, '123456', 'ACTIVE', NULL, NULL, NULL, '2025-11-03 11:55:44.043000', 'lisi@edu.cn', '2023', NULL, '提升学习效率', 0, '计算机科学与技术', '2025-11-03 11:55:44.043000'),
	(4, '202301003', '王五', NULL, '123456', 'ACTIVE', NULL, NULL, NULL, '2025-11-04 07:41:12.610000', 'wangwu@edu.cn', '2023', NULL, '提升学习效率', 0, '计算机科学与技术', '2025-11-04 07:41:12.610000');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
