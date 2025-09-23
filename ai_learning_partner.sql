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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生学业成绩记录表';

-- 正在导出表  ai_learning_partner.academic_records 的数据：~0 rows (大约)

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
  `avatar_url` varchar(200) DEFAULT NULL COMMENT '头像URL',
  `learning_preferences` json DEFAULT NULL COMMENT '学习偏好设置',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`student_id`),
  KEY `idx_major` (`major`),
  KEY `idx_grade` (`grade`),
  KEY `idx_college` (`college`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生基本信息表';

-- 正在导出表  ai_learning_partner.students 的数据：~0 rows (大约)
REPLACE INTO `students` (`student_id`, `name`, `gender`, `major`, `college`, `grade`, `class_name`, `enrollment_date`, `graduation_date`, `email`, `phone`, `avatar_url`, `learning_preferences`, `created_at`, `updated_at`) VALUES
	('202101001', '李十三', 'M', '软件工程', '软件学院', '2021', '软工2101', '2021-09-01', NULL, 'lishisan@edu.cn', '13800138011', NULL, NULL, '2025-09-23 07:13:16', '2025-09-23 07:13:16'),
	('202101002', '张十四', 'F', '电子信息工程', '电子信息学院', '2021', '电信2101', '2021-09-01', NULL, 'zhangshisi@edu.cn', '13800138012', NULL, NULL, '2025-09-23 07:13:16', '2025-09-23 07:13:16'),
	('202201001', '郑十一', 'M', '计算机科学与技术', '计算机学院', '2022', '计科2201', '2022-09-01', NULL, 'zhengshiyi@edu.cn', '13800138009', NULL, NULL, '2025-09-23 07:13:16', '2025-09-23 07:13:16'),
	('202201002', '王十二', 'F', '计算机科学与技术', '计算机学院', '2022', '计科2201', '2022-09-01', NULL, 'wangshier@edu.cn', '13800138010', NULL, NULL, '2025-09-23 07:13:16', '2025-09-23 07:13:16'),
	('202301001', '张三', 'M', '计算机科学与技术', '计算机学院', '2023', '计科2301', '2023-09-01', NULL, 'zhangsan@edu.cn', '13800138001', NULL, NULL, '2025-09-23 07:13:16', '2025-09-23 07:13:16'),
	('202301002', '李四', 'M', '计算机科学与技术', '计算机学院', '2023', '计科2301', '2023-09-01', NULL, 'lisi@edu.cn', '13800138002', NULL, NULL, '2025-09-23 07:13:16', '2025-09-23 07:13:16'),
	('202301003', '王五', 'F', '计算机科学与技术', '计算机学院', '2023', '计科2301', '2023-09-01', NULL, 'wangwu@edu.cn', '13800138003', NULL, NULL, '2025-09-23 07:13:16', '2025-09-23 07:13:16'),
	('202302001', '赵六', 'M', '软件工程', '软件学院', '2023', '软工2301', '2023-09-01', NULL, 'zhaoliu@edu.cn', '13800138004', NULL, NULL, '2025-09-23 07:13:16', '2025-09-23 07:13:16'),
	('202302002', '钱七', 'F', '软件工程', '软件学院', '2023', '软工2301', '2023-09-01', NULL, 'qianqi@edu.cn', '13800138005', NULL, NULL, '2025-09-23 07:13:16', '2025-09-23 07:13:16'),
	('202302003', '孙八', 'M', '软件工程', '软件学院', '2023', '软工2301', '2023-09-01', NULL, 'sunba@edu.cn', '13800138006', NULL, NULL, '2025-09-23 07:13:16', '2025-09-23 07:13:16'),
	('202303001', '周九', 'F', '电子信息工程', '电子信息学院', '2023', '电信2301', '2023-09-01', NULL, 'zhoujiu@edu.cn', '13800138007', NULL, NULL, '2025-09-23 07:13:16', '2025-09-23 07:13:16'),
	('202303002', '吴十', 'M', '电子信息工程', '电子信息学院', '2023', '电信2301', '2023-09-01', NULL, 'wushi@edu.cn', '13800138008', NULL, NULL, '2025-09-23 07:13:16', '2025-09-23 07:13:16');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
