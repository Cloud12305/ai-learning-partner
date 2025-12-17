-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.42 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  12.5.0.6677
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
  `academic_year` varchar(9) NOT NULL COMMENT '学年',
  `semester` tinyint NOT NULL COMMENT '学期',
  `score` decimal(5,2) DEFAULT NULL COMMENT '成绩',
  `grade_point` decimal(3,2) DEFAULT NULL COMMENT '绩点',
  `credit_earned` decimal(3,1) DEFAULT NULL COMMENT '学分',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `uk_student_course_semester` (`student_id`,`course_id`,`academic_year`,`semester`),
  KEY `fk_academic_course` (`course_id`),
  CONSTRAINT `fk_academic_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`),
  CONSTRAINT `fk_academic_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学业成绩表';

-- 正在导出表  ai_learning_partner.academic_records 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.courses 结构
CREATE TABLE IF NOT EXISTS `courses` (
  `course_id` varchar(20) NOT NULL COMMENT '课程ID',
  `course_name` varchar(100) NOT NULL COMMENT '课程名称',
  `credits` decimal(3,1) NOT NULL COMMENT '学分',
  `department` varchar(50) NOT NULL COMMENT '开课院系',
  `course_category` varchar(30) DEFAULT NULL COMMENT '课程类别',
  `total_hours` smallint DEFAULT NULL COMMENT '总学时',
  `course_description` text COMMENT '课程描述',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程表';

-- 正在导出表  ai_learning_partner.courses 的数据：~11 rows (大约)
INSERT INTO `courses` (`course_id`, `course_name`, `credits`, `department`, `course_category`, `total_hours`, `course_description`, `created_at`, `updated_at`) VALUES
	('CS101', '计算机导论', 3.0, '计算机学院', '必修', 64, '计算机科学基础知识入门，包括计算机组成、操作系统、网络等基础概念', '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('CS102', 'C语言程序设计', 4.0, '计算机学院', '必修', 80, 'C语言编程基础与算法，培养程序设计思维和编程能力', '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('CS201', '数据结构', 4.0, '计算机学院', '必修', 80, '线性表、树、图等数据结构的基本原理和算法实现', '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('CS202', '操作系统', 3.0, '计算机学院', '必修', 64, '进程管理、内存管理、文件系统等操作系统核心概念', '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('GE101', '大学英语', 2.0, '外语学院', '必修', 48, '英语听说读写综合训练，提升英语应用能力', '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('GE102', '大学物理', 3.0, '物理学院', '必修', 64, '力学、热学、电磁学等物理学基础理论和实验', '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('MA101', '高等数学A', 5.0, '数学学院', '必修', 96, '微积分、极限、导数等数学基础，为后续课程奠定数学基础', '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('MA102', '线性代数', 3.0, '数学学院', '必修', 64, '矩阵、向量空间、线性变换等线性代数核心内容', '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('SE101', '软件工程导论', 2.0, '软件学院', '必修', 48, '软件开发流程与方法论，了解软件生命周期', '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('SE201', 'Java程序设计', 4.0, '软件学院', '必修', 80, '面向对象编程、Java核心技术、GUI编程等', '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('SE301', 'Web开发技术', 3.0, '软件学院', '选修', 64, 'HTML/CSS/JavaScript前后端开发技术', '2025-11-07 09:31:33', '2025-11-07 09:31:33');

-- 导出  表 ai_learning_partner.emotion_record 结构
CREATE TABLE IF NOT EXISTS `emotion_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` varchar(255) DEFAULT NULL,
  `emotion` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.emotion_record 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.knowledge_points 结构
CREATE TABLE IF NOT EXISTS `knowledge_points` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `definition` text,
  `kp_name` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k0uqrmn0f3jpdvnanyd2d1tpt` (`kp_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.knowledge_points 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.knowledge_relations 结构
CREATE TABLE IF NOT EXISTS `knowledge_relations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `relation_type` varchar(255) NOT NULL,
  `kp1_id` bigint NOT NULL,
  `kp2_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7610gd6g66vnhqx3bc4q9bvh` (`kp1_id`),
  KEY `FKh49ysedtcnndmfegcpwdmfhay` (`kp2_id`),
  CONSTRAINT `FK7610gd6g66vnhqx3bc4q9bvh` FOREIGN KEY (`kp1_id`) REFERENCES `knowledge_points` (`id`),
  CONSTRAINT `FKh49ysedtcnndmfegcpwdmfhay` FOREIGN KEY (`kp2_id`) REFERENCES `knowledge_points` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.knowledge_relations 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.lab_attendance 结构
CREATE TABLE IF NOT EXISTS `lab_attendance` (
  `attendance_id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) NOT NULL COMMENT '学号',
  `lab_id` varchar(30) NOT NULL COMMENT '实验室ID',
  `lab_name` varchar(100) NOT NULL COMMENT '实验室名称',
  `experiment_name` varchar(100) DEFAULT NULL COMMENT '实验名称',
  `check_in_time` datetime NOT NULL COMMENT '签到时间',
  `check_out_time` datetime DEFAULT NULL COMMENT '签退时间',
  `duration_minutes` int DEFAULT NULL COMMENT '停留时长',
  `notes` text COMMENT '备注',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`attendance_id`),
  KEY `fk_lab_student` (`student_id`),
  CONSTRAINT `fk_lab_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='实验室考勤表';

-- 正在导出表  ai_learning_partner.lab_attendance 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.learning_progress 结构
CREATE TABLE IF NOT EXISTS `learning_progress` (
  `progress_id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) NOT NULL COMMENT '学号',
  `plan_id` bigint DEFAULT NULL COMMENT '学习计划ID',
  `course_id` varchar(20) DEFAULT NULL COMMENT '课程ID',
  `chapter` varchar(100) NOT NULL COMMENT '章节名称',
  `completion_rate` decimal(5,2) DEFAULT '0.00' COMMENT '完成进度',
  `time_spent` int DEFAULT '0' COMMENT '学习时长(分钟)',
  `study_date` date NOT NULL COMMENT '学习日期',
  `notes` text COMMENT '学习笔记',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `id` bigint NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`progress_id`),
  KEY `fk_progress_student` (`student_id`),
  KEY `fk_progress_plan` (`plan_id`),
  KEY `fk_progress_course` (`course_id`),
  KEY `FKkex4pny6sl5affwroon8lfbjt` (`user_id`),
  CONSTRAINT `fk_progress_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`),
  CONSTRAINT `fk_progress_plan` FOREIGN KEY (`plan_id`) REFERENCES `study_plans` (`plan_id`),
  CONSTRAINT `fk_progress_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`student_id`),
  CONSTRAINT `FKkex4pny6sl5affwroon8lfbjt` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学习进度表';

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
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`record_id`),
  KEY `fk_library_student` (`student_id`),
  CONSTRAINT `fk_library_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图书馆借阅表';

-- 正在导出表  ai_learning_partner.library_records 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.qa_history 结构
CREATE TABLE IF NOT EXISTS `qa_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `answer` text,
  `created_at` datetime(6) DEFAULT NULL,
  `knowledge_graph_data` json DEFAULT NULL,
  `question` text NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.qa_history 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.study_plans 结构
CREATE TABLE IF NOT EXISTS `study_plans` (
  `plan_id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) NOT NULL COMMENT '学号',
  `plan_name` varchar(100) NOT NULL COMMENT '计划名称',
  `course_id` varchar(20) DEFAULT NULL COMMENT '关联课程ID',
  `description` text COMMENT '计划描述',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `target_score` decimal(5,2) DEFAULT NULL COMMENT '目标成绩',
  `study_hours_per_week` int DEFAULT NULL COMMENT '每周学习时长',
  `status` enum('PLANNING','IN_PROGRESS','COMPLETED','CANCELLED') DEFAULT 'PLANNING' COMMENT '状态',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `id` bigint NOT NULL,
  `course_name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`plan_id`),
  KEY `fk_plan_student` (`student_id`),
  KEY `fk_plan_course` (`course_id`),
  KEY `FKsmw1bta843doo6k3a23lskryk` (`user_id`),
  CONSTRAINT `fk_plan_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`),
  CONSTRAINT `fk_plan_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`student_id`),
  CONSTRAINT `FKsmw1bta843doo6k3a23lskryk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学习计划表';

-- 正在导出表  ai_learning_partner.study_plans 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.users 结构
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) NOT NULL COMMENT '学号',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `password` varchar(100) NOT NULL DEFAULT '123456' COMMENT '密码',
  `gender` enum('M','F','U') DEFAULT NULL COMMENT '性别',
  `major` varchar(50) NOT NULL COMMENT '专业',
  `college` varchar(50) NOT NULL COMMENT '学院',
  `grade` varchar(10) NOT NULL COMMENT '年级',
  `class_name` varchar(30) DEFAULT NULL COMMENT '班级',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar_url` varchar(200) DEFAULT NULL COMMENT '头像URL',
  `enrollment_date` date DEFAULT NULL COMMENT '入学日期',
  `graduation_date` date DEFAULT NULL COMMENT '毕业日期',
  `learning_goal` text COMMENT '学习目标',
  `learning_preferences` json DEFAULT NULL COMMENT '学习偏好',
  `account_status` varchar(20) DEFAULT 'ACTIVE' COMMENT '账户状态',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime(6) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `login_count` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_id` (`student_id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `UK_qh3otyipv2k9hqte4a1abcyhq` (`student_id`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_major` (`major`),
  KEY `idx_grade` (`grade`),
  KEY `idx_college` (`college`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- 正在导出表  ai_learning_partner.users 的数据：~8 rows (大约)
INSERT INTO `users` (`id`, `student_id`, `username`, `name`, `password`, `gender`, `major`, `college`, `grade`, `class_name`, `email`, `phone`, `avatar_url`, `enrollment_date`, `graduation_date`, `learning_goal`, `learning_preferences`, `account_status`, `last_login_time`, `create_time`, `update_time`, `created_at`, `login_count`, `updated_at`) VALUES
	(1, '202301001', 'zhangsan', '张三', '123456', 'M', '计算机科学与技术', '计算机学院', '2023', '计科2301', 'zhangsan@edu.cn', '13800138001', 'https://picsum.photos/100/100?random=1', '2023-08-08', '2027-06-06', '成为全栈工程师，掌握AI开发技术', '{"learning_style": "visual", "difficulty_level": "medium", "preferred_subjects": ["编程", "算法"], "preferred_study_time": "evening"}', 'ACTIVE', '2025-11-16 18:19:34', NULL, '2025-11-16 18:19:33.763327', NULL, 4, '2025-11-16 18:19:33.763327'),
	(2, '202301002', 'lisi', '李四', '123456', 'M', '计算机科学与技术', '计算机学院', '2023', '计科2301', 'lisi@edu.cn', '13800138002', 'https://picsum.photos/100/100?random=2', '2023-08-31', '2027-06-29', '深入算法研究，参加ACM竞赛并获得奖项', '{"learning_style": "logical", "difficulty_level": "high", "preferred_subjects": ["算法", "数学"], "preferred_study_time": "morning"}', 'ACTIVE', '2025-11-10 10:27:07', NULL, NULL, NULL, NULL, NULL),
	(3, '202301003', 'wangwu', '王五', '123456', 'F', '计算机科学与技术', '计算机学院', '2023', '计科2301', 'wangwu@edu.cn', '13800138003', 'https://picsum.photos/100/100?random=3', '2023-09-01', '2027-06-30', '提升编程能力，学习数据科学和机器学习', '{"learning_style": "practical", "difficulty_level": "medium", "preferred_subjects": ["数据科学", "统计学"], "preferred_study_time": "afternoon"}', 'ACTIVE', '2024-02-19 14:20:00', NULL, NULL, NULL, NULL, NULL),
	(4, '202302001', 'zhaoliu', '赵六', '123456', 'M', '软件工程', '软件学院', '2023', '软工2301', 'zhaoliu@edu.cn', '13800138004', 'https://picsum.photos/100/100?random=4', '2023-09-01', '2027-06-30', '掌握软件开发全流程，参与大型项目实战', '{"learning_style": "practical", "difficulty_level": "medium", "preferred_subjects": ["软件开发", "项目管理"], "preferred_study_time": "evening"}', 'ACTIVE', '2024-02-20 10:05:00', NULL, NULL, NULL, NULL, NULL),
	(5, '202302002', 'qianqi', '钱七', '123456', 'F', '软件工程', '软件学院', '2023', '软工2301', 'qianqi@edu.cn', '13800138005', 'https://picsum.photos/100/100?random=5', '2023-09-01', '2027-06-30', '学习前端开发技术，成为专业的UI/UX设计师', '{"learning_style": "creative", "difficulty_level": "low", "preferred_subjects": ["设计", "前端开发"], "preferred_study_time": "flexible"}', 'ACTIVE', '2024-02-18 16:45:00', NULL, NULL, NULL, NULL, NULL),
	(6, '202201001', 'zhengshiyi', '郑十一', '123456', 'M', '计算机科学与技术', '计算机学院', '2022', '计科2201', 'zhengshiyi@edu.cn', '13800138009', 'https://picsum.photos/100/100?random=6', '2022-09-01', '2026-06-30', '深入学习人工智能和机器学习，为研究生阶段做准备', '{"learning_style": "theoretical", "difficulty_level": "high", "preferred_subjects": ["人工智能", "机器学习"], "preferred_study_time": "morning"}', 'ACTIVE', '2024-02-20 07:50:00', NULL, NULL, NULL, NULL, NULL),
	(7, '202101001', 'lishisan', '李十三', '123456', 'M', '软件工程', '软件学院', '2021', '软工2101', 'lishisan@edu.cn', '13800138011', 'https://picsum.photos/100/100?random=7', '2021-09-01', '2025-06-30', '准备考研，深入研究分布式系统和云计算技术', '{"learning_style": "comprehensive", "difficulty_level": "high", "preferred_subjects": ["分布式系统", "云计算"], "preferred_study_time": "whole_day"}', 'ACTIVE', '2024-02-20 11:20:00', NULL, NULL, NULL, NULL, NULL),
	(29, '202301110', 'Jasmine', '孙梦云', '123456', 'F', '软件工程', '计算机学院', '2023', '软工2301', '15268660761@163.com', NULL, 'https://picsum.photos/100/100?random=57', NULL, NULL, NULL, NULL, 'ACTIVE', '2025-11-10 08:39:59', NULL, NULL, NULL, NULL, NULL),
	(30, 'ADMIN001', 'admin', '系统管理员', '123456', 'M', '计算机科学', '计算机学院', '2023', '管理员', 'admin@edu.cn', '13800138000', 'https://picsum.photos/100/100?random=99', '2023-09-01', '2027-06-30', NULL, NULL, 'ACTIVE', NULL, NULL, NULL, NULL, NULL, NULL),
	(31, '202301026', 'peng', '彭雨淇', '123456', 'F', '软件工程', '计算机学院', '2023', '软工231', '1235698256@qq.com', NULL, 'https://picsum.photos/100/100?random=19', NULL, NULL, NULL, NULL, 'ACTIVE', '2025-11-17 07:06:48', '2025-11-17 06:41:04.180004', '2025-11-17 07:06:47.749426', '2025-11-17 06:41:04.180004', 3, '2025-11-17 07:06:47.749426');

-- 导出  表 ai_learning_partner.user_role 结构
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role` enum('STUDENT','ADMIN') NOT NULL DEFAULT 'STUDENT' COMMENT '角色',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`),
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色表';

-- 正在导出表  ai_learning_partner.user_role 的数据：~9 rows (大约)
INSERT INTO `user_role` (`id`, `user_id`, `role`, `created_at`) VALUES
	(1, 1, 'STUDENT', '2025-11-16 15:54:38'),
	(2, 2, 'STUDENT', '2025-11-16 15:54:38'),
	(3, 3, 'STUDENT', '2025-11-16 15:54:38'),
	(4, 4, 'STUDENT', '2025-11-16 15:54:38'),
	(5, 5, 'STUDENT', '2025-11-16 15:54:38'),
	(6, 6, 'STUDENT', '2025-11-16 15:54:38'),
	(7, 7, 'STUDENT', '2025-11-16 15:54:38'),
	(8, 29, 'STUDENT', '2025-11-16 15:54:38'),
	(9, 30, 'ADMIN', '2025-11-16 15:54:38'),
	(10, 31, 'STUDENT', '2025-11-16 22:41:04');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
