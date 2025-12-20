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
  `course_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '课程名称（关联courses表）',
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
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学业成绩表';

-- 正在导出表  ai_learning_partner.academic_records 的数据：~10 rows (大约)
INSERT INTO `academic_records` (`record_id`, `student_id`, `course_id`, `course_name`, `academic_year`, `semester`, `score`, `grade_point`, `credit_earned`, `created_at`, `updated_at`) VALUES
	(29, '202301001', 'MA101', '高等数学A', '2023-2024', 1, 88.00, 3.50, 4.5, '2025-11-17 01:48:31', '2025-12-15 09:39:46'),
	(30, '202301001', 'GE101', '大学英语', '2024-2025', 1, 85.00, 3.50, 2.0, '2025-11-17 01:48:31', '2025-12-15 05:57:31'),
	(31, '202301001', 'CS102', 'C语言程序设计', '2023-2024', 2, 78.00, 2.50, 4.0, '2025-11-17 01:48:31', '2025-12-15 05:57:31'),
	(32, '202301001', 'MA102', '线性代数', '2024-2025', 1, 77.00, 2.50, 3.0, '2025-11-17 01:48:31', '2025-12-15 01:59:13'),
	(33, '202201001', 'CS201', '数据结构', '2023-2024', 1, 95.00, 4.50, 4.0, '2025-11-17 01:48:31', '2025-12-15 05:57:31'),
	(34, '202201001', 'CS202', '操作系统', '2025-2026', 2, 98.00, 4.50, 3.5, '2025-11-17 01:48:31', '2025-12-17 10:14:19'),
	(35, '202301002', 'MA101', '高等数学A', '2023-2024', 1, 80.00, 3.00, 4.5, '2025-12-15 02:18:57', '2025-12-15 10:19:15'),
	(36, '202201001', 'MA102', '线性代数', '2024-2025', 1, 100.00, 5.00, 3.0, '2025-12-15 02:23:47', '2025-12-15 02:23:47'),
	(37, '202301002', 'MA102', '线性代数', '2023-2024', 1, 60.00, 1.00, 3.0, '2025-12-16 05:25:17', '2025-12-16 05:25:17'),
	(38, '202201001', 'GE101', '大学英语', '2023-2024', 2, 0.00, 0.00, 0.0, '2025-12-16 05:47:23', '2025-12-16 05:47:23');

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
  `id` bigint NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  `course_link` varchar(255) DEFAULT NULL,
  `difficulty` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `instructor` varchar(255) DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `university` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程表';

-- 正在导出表  ai_learning_partner.courses 的数据：~11 rows (大约)
INSERT INTO `courses` (`course_id`, `course_name`, `credits`, `department`, `course_category`, `total_hours`, `course_description`, `created_at`, `updated_at`, `id`, `category`, `course_link`, `difficulty`, `image_url`, `instructor`, `platform`, `title`, `university`) VALUES
	('CS101', '计算机导论', 3.0, '计算机学院', '必修', 64, '计算机科学基础知识入门，包括计算机组成、操作系统、网络等基础概念', '2025-11-07 09:31:33', '2025-11-07 09:31:33', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('CS102', 'C语言程序设计', 4.0, '计算机学院', '必修', 80, 'C语言编程基础与算法，培养程序设计思维和编程能力', '2025-11-07 09:31:33', '2025-11-07 09:31:33', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('CS201', '数据结构', 4.0, '计算机学院', '必修', 80, '线性表、树、图等数据结构的基本原理和算法实现', '2025-11-07 09:31:33', '2025-11-07 09:31:33', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('CS202', '操作系统', 3.5, '计算机学院', '必修', 64, '进程管理、内存管理、文件系统等操作系统核心概念', '2025-11-07 09:31:33', '2025-12-17 20:08:53', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('GE101', '大学英语', 2.0, '外语学院', '必修', 48, '英语听说读写综合训练，提升英语应用能力', '2025-11-07 09:31:33', '2025-11-07 09:31:33', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('GE102', '大学物理', 3.0, '物理学院', '必修', 64, '力学、热学、电磁学等物理学基础理论和实验', '2025-11-07 09:31:33', '2025-11-07 09:31:33', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('MA101', '高等数学A', 4.5, '数学学院', '必修', 96, '进程管理、内存管理、文件系统等操作系统核心概念', '2025-11-07 09:31:33', '2025-12-17 20:08:53', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('MA102', '线性代数', 3.0, '数学学院', '必修', 64, '矩阵、向量空间、线性变换等线性代数核心内容', '2025-11-07 09:31:33', '2025-11-07 09:31:33', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('SE101', '软件工程导论', 2.0, '软件学院', '必修', 48, '软件开发流程与方法论，了解软件生命周期', '2025-11-07 09:31:33', '2025-11-07 09:31:33', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('SE201', 'Java程序设计', 4.0, '软件学院', '必修', 80, '面向对象编程、Java核心技术、GUI编程等', '2025-11-07 09:31:33', '2025-11-07 09:31:33', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('SE301', 'Web开发技术', 3.0, '软件学院', '选修', 64, 'HTML/CSS/JavaScript前后端开发技术', '2025-11-07 09:31:33', '2025-11-07 09:31:33', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

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

-- 导出  表 ai_learning_partner.interview_questions 结构
CREATE TABLE IF NOT EXISTS `interview_questions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(255) NOT NULL,
  `correct_answer` varchar(10) NOT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `difficulty_level` int DEFAULT '1',
  `explanation` text,
  `language` varchar(255) NOT NULL,
  `option_a` text,
  `option_b` text,
  `option_c` text,
  `option_d` text,
  `question` text NOT NULL,
  `type` varchar(255) NOT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.interview_questions 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.job_positions 结构
CREATE TABLE IF NOT EXISTS `job_positions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `job_title` varchar(100) NOT NULL,
  `company_name` varchar(100) NOT NULL,
  `location` varchar(50) DEFAULT NULL,
  `required_skills` json DEFAULT NULL,
  `job_description` text,
  `salary_range` varchar(50) DEFAULT NULL,
  `experience_level` varchar(20) DEFAULT NULL,
  `education_requirements` varchar(100) DEFAULT NULL,
  `match_score` double DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.job_positions 的数据：~20 rows (大约)
INSERT INTO `job_positions` (`id`, `job_title`, `company_name`, `location`, `required_skills`, `job_description`, `salary_range`, `experience_level`, `education_requirements`, `match_score`, `created_at`, `updated_at`) VALUES
	(1, '前端开发工程师', '字节跳动', '北京', '["JavaScript", "HTML/CSS", "Vue", "React", "Webpack"]', '负责公司Web前端开发工作，参与产品需求分析和技术方案设计，与后端开发人员协作，完成功能模块的开发与维护。', '15-30K', '中级', NULL, 0.92, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(2, '全栈开发工程师', '阿里巴巴', '杭州', '["JavaScript", "Node.js", "Vue", "MongoDB", "Express"]', '负责公司产品的前后端开发工作，参与技术架构设计和优化，解决开发过程中的技术难题，持续提升产品性能和用户体验。', '20-35K', '高级', NULL, 0.85, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(3, 'H5开发工程师', '腾讯', '深圳', '["JavaScript", "HTML/CSS", "响应式设计", "微信小程序"]', '负责公司移动端H5页面和小程序的开发工作，优化移动端体验，解决各种浏览器和设备兼容性问题，提升页面加载速度。', '18-32K', '中级', NULL, 0.88, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(4, '后端开发工程师', '阿里健康', '北京', '["Java", "Spring Boot", "MySQL", "Redis", "Docker"]', '负责医疗健康平台后端服务开发，设计高可用架构，保障医疗数据安全和系统稳定性，参与医疗信息化系统建设。', '20-35K', '中级', '计算机相关专业本科', 0.88, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(5, 'Java开发工程师', '平安好医生', '北京', '["Java", "Spring Cloud", "MySQL", "Redis", "微服务"]', '参与医疗信息化系统开发，负责核心业务模块的设计与实现，优化系统性能，保障医疗数据安全。', '18-32K', '中级', '计算机相关专业', 0.82, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(6, 'Node.js后端工程师', '微医', '杭州', '["JavaScript", "Node.js", "Express", "MongoDB", "Redis"]', '负责在线医疗平台Node.js后端开发，参与微服务架构设计，提升系统性能，优化医生和患者的使用体验。', '16-28K', '中级', NULL, 0.85, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(7, '医疗后端开发工程师', '丁香园', '北京', '["Java", "Spring Boot", "PostgreSQL", "Docker", "Kubernetes"]', '负责医疗知识平台后端架构设计，开发高并发医疗信息服务系统，保障系统的高可用性。', '22-38K', '高级', '计算机相关专业硕士', 0.9, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(8, '后端开发工程师', '腾讯', '深圳', '["Go", "Python", "MySQL", "Redis", "Docker"]', '负责内容推荐系统后端开发，优化算法服务性能，参与大规模分布式系统架构设计。', '20-35K', '中级', '计算机相关专业', 0.75, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(9, 'Java后端工程师', '京东', '北京', '["Java", "Spring Cloud", "MySQL", "Redis", "分布式系统"]', '负责电商平台核心交易系统开发，参与高并发系统架构设计，保障系统稳定性和性能。', '18-30K', '中级', NULL, 0.78, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(10, 'Python后端开发', '百度', '北京', '["Python", "Django", "MySQL", "Redis", "Linux"]', '负责AI平台后端服务开发，参与机器学习系统架构设计，优化算法服务性能。', '18-32K', '中级', '计算机相关专业', 0.72, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(11, '全栈开发工程师', '腾讯医疗', '深圳', '["JavaScript", "Node.js", "Vue", "Java", "MySQL"]', '负责医疗AI平台全栈开发，参与智慧医疗系统建设，前后端协同开发。', '18-30K', '中级', NULL, 0.8, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(12, '全栈工程师', '美团', '北京', '["JavaScript", "React", "Node.js", "Java", "MySQL"]', '负责本地生活服务平台全栈开发，参与系统架构优化，提升用户体验。', '16-28K', '中级', '计算机相关专业', 0.76, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(13, '数据开发工程师', '阿里健康', '杭州', '["Python", "SQL", "Hadoop", "Spark", "数据仓库"]', '负责医疗健康数据平台开发，构建数据仓库，支持业务数据分析和决策。', '20-35K', '中级', '计算机/统计相关专业', 0.68, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(14, '移动端开发工程师', '平安好医生', '上海', '["Java", "Kotlin", "Android", "RxJava", "MVVM"]', '负责医疗健康APP移动端开发，优化用户体验，参与产品功能设计和实现。', '16-28K', '中级', NULL, 0.65, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(15, '云计算开发工程师', '腾讯云', '深圳', '["Go", "Docker", "Kubernetes", "Linux", "网络协议"]', '负责云计算平台后端开发，参与云原生技术架构设计，优化资源调度算法。', '22-40K', '高级', '计算机相关专业', 0.7, '2025-11-16 23:01:09', '2025-11-16 23:01:09'),
	(16, '后端开发工程师', '腾讯游戏', '北京', '["Java", "Spring Boot", "MySQL", "Redis", "Netty"]', '负责游戏平台后端服务开发，设计高并发架构，优化游戏服务性能，保障游戏数据安全。', '22-40K', '中级', '计算机相关专业', 0.92, '2025-11-17 08:15:39', '2025-11-17 08:15:39'),
	(17, 'Java游戏服务器开发', '网易游戏', '广州', '["Java", "Netty", "Redis", "MySQL", "多线程"]', '负责游戏服务器架构设计与开发，优化网络通信性能，处理高并发游戏请求。', '20-35K', '中级', NULL, 0.88, '2025-11-17 08:15:39', '2025-11-17 08:15:39'),
	(18, 'Node.js后端工程师', '米哈游', '上海', '["JavaScript", "Node.js", "TypeScript", "Redis", "MongoDB"]', '负责游戏社区平台后端开发，参与游戏数据服务架构设计，优化用户体验。', '18-32K', '中级', '计算机相关专业', 0.85, '2025-11-17 08:15:39', '2025-11-17 08:15:39'),
	(19, '游戏后端开发工程师', '完美世界', '北京', '["C++", "Java", "Linux", "网络编程", "Redis"]', '负责游戏核心服务端开发，设计游戏逻辑架构，优化服务器性能。', '18-30K', '中级', NULL, 0.82, '2025-11-17 08:15:39', '2025-11-17 08:15:39'),
	(20, '全栈开发工程师', '巨人网络', '上海', '["Java", "Spring Boot", "Vue", "MySQL", "Redis"]', '负责游戏运营平台全栈开发，前后端协同开发，提升运营效率。', '16-28K', '中级', '计算机相关专业', 0.78, '2025-11-17 08:15:39', '2025-11-17 08:15:39');

-- 导出  表 ai_learning_partner.knowledge_points 结构
CREATE TABLE IF NOT EXISTS `knowledge_points` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `kp_name` varchar(255) NOT NULL,
  `definition` text,
  `category` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `kp_name` (`kp_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.knowledge_points 的数据：~5 rows (大约)
INSERT INTO `knowledge_points` (`id`, `kp_name`, `definition`, `category`, `created_at`, `updated_at`) VALUES
	(1, '二叉树', '每个节点最多有两个子节点的树形数据结构', '数据结构', '2025-11-17 03:54:34', '2025-11-17 03:54:34'),
	(2, '链表', '通过指针连接节点的线性数据结构', '数据结构', '2025-11-17 03:54:34', '2025-11-17 03:54:34'),
	(3, '数组', '连续内存空间的线性数据结构', '数据结构', '2025-11-17 03:54:34', '2025-11-17 03:54:34'),
	(4, '快速排序', '基于分治策略的高效排序算法', '算法', '2025-11-17 03:54:34', '2025-11-17 03:54:34'),
	(5, '面向对象编程', '以对象为核心的编程范式', '编程语言', '2025-11-17 03:54:34', '2025-11-17 03:54:34');

-- 导出  表 ai_learning_partner.knowledge_relations 结构
CREATE TABLE IF NOT EXISTS `knowledge_relations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `kp1_id` bigint NOT NULL,
  `kp2_id` bigint NOT NULL,
  `relation_type` varchar(100) NOT NULL,
  `description` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_relation` (`kp1_id`,`kp2_id`,`relation_type`),
  KEY `kp2_id` (`kp2_id`),
  CONSTRAINT `knowledge_relations_ibfk_1` FOREIGN KEY (`kp1_id`) REFERENCES `knowledge_points` (`id`),
  CONSTRAINT `knowledge_relations_ibfk_2` FOREIGN KEY (`kp2_id`) REFERENCES `knowledge_points` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.knowledge_relations 的数据：~3 rows (大约)
INSERT INTO `knowledge_relations` (`id`, `kp1_id`, `kp2_id`, `relation_type`, `description`, `created_at`) VALUES
	(1, 1, 2, '对比关系', '二叉树与链表在结构上的区别', '2025-11-17 03:54:34'),
	(2, 1, 3, '对比关系', '二叉树与数组在存储方式上的差异', '2025-11-17 03:54:34'),
	(3, 4, 1, '应用关系', '快速排序可以用于二叉树节点的排序', '2025-11-17 03:54:34');

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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='实验室考勤表';

-- 正在导出表  ai_learning_partner.lab_attendance 的数据：~11 rows (大约)
INSERT INTO `lab_attendance` (`attendance_id`, `student_id`, `lab_id`, `lab_name`, `experiment_name`, `check_in_time`, `check_out_time`, `duration_minutes`, `notes`, `created_at`) VALUES
	(18, '202301001', 'LAB001', '计算机基础实验室', '数据结构实验', '2024-01-15 10:00:00', '2024-01-15 14:00:00', 120, NULL, '2025-11-17 01:48:31'),
	(19, '202301001', 'LAB002', '网络工程实验室', '网络协议分析实验', '2024-01-20 14:00:00', '2024-01-20 16:30:00', 150, NULL, '2025-11-17 01:48:31'),
	(20, '202301002', 'LAB003', '软件工程实验室', 'Web应用开发实验', '2025-12-10 19:00:00', NULL, 150, NULL, '2025-11-17 01:48:31'),
	(21, '202301001', 'LAB004', '人工智能实验室', '机器学习实验', '2025-12-10 10:00:00', NULL, 120, NULL, '2025-11-17 01:48:31'),
	(22, '202301001', 'LAB005', '数据库实验室', '数据库设计实验', '2024-03-01 10:00:00', '2024-03-01 12:00:00', 120, NULL, '2025-11-17 01:48:31'),
	(23, '202301002', 'LAB001', '计算机基础实验室', '数据结构实验', '2025-12-16 20:00:00', NULL, 90, '', '2025-12-15 20:18:49'),
	(24, '202301002', 'LAB002', '网络工程实验室', '网络协议分析实验', '2025-12-16 20:30:00', '2025-12-16 21:00:00', 120, '很简单', '2025-12-15 20:44:01'),
	(25, '202301002', 'LAB002', '网络工程实验室', '网络协议分析实验', '2025-12-16 12:20:00', '2025-12-16 13:00:00', 30, '太难了', '2025-12-15 20:54:07'),
	(26, '202301002', 'LAB003', '软件工程实验室', 'Web应用开发实验', '2025-12-15 19:00:00', '2025-12-15 21:00:00', 120, '', '2025-12-16 05:02:40'),
	(27, '202301003', 'LAB001', '计算机基础实验室', '数据结构实验', '2025-12-14 16:00:00', '2025-12-14 17:10:00', 70, '', '2025-12-16 05:10:54'),
	(28, '202301003', 'LAB004', '人工智能实验室', '机器学习实验', '2025-12-05 15:19:00', NULL, 90, '', '2025-12-16 05:19:56'),
	(29, '202301003', 'LAB004', '人工智能实验室', '机器学习实验', '2025-12-20 16:00:00', NULL, 90, '', '2025-12-20 08:18:54');

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
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图书馆借阅表';

-- 正在导出表  ai_learning_partner.library_records 的数据：~8 rows (大约)
INSERT INTO `library_records` (`record_id`, `student_id`, `book_id`, `book_isbn`, `book_title`, `book_author`, `book_category`, `publisher`, `publish_year`, `borrow_date`, `due_date`, `return_date`, `created_at`) VALUES
	(15, '202301001', 'B001', '9787115546081', '深入理解计算机系统', 'Randal E. Bryant', '计算机系统 / 组成', NULL, NULL, '2024-01-10', '2024-02-10', '2024-01-19', '2025-11-17 01:48:32'),
	(16, '202301001', 'B002', '9787115427472', '算法导论', 'Thomas H. Cormen', '算法与数据结构', NULL, NULL, '2024-01-05', '2024-04-05', '2024-04-07', '2025-11-17 01:48:32'),
	(17, '202301001', 'B003', '9787121372983', '计算机网络', 'Andrew S. Tanenbaum', '计算机软件基础', '电子工业出版社', '2020', '2024-01-03', '2024-04-03', '2024-02-28', '2025-11-17 01:48:32'),
	(18, '202301002', 'B004', '9787111636664', '操作系统概念', 'Abraham Silberschatz', '计算机软件基础', '机械工业出版社', '2019', '2023-12-28', '2024-03-28', '2024-08-03', '2025-11-17 01:48:32'),
	(19, '202201001', 'B005', '9787111612729', '数据库系统概念', 'Abraham Silberschatz', '数据库', NULL, NULL, '2023-12-20', '2026-01-01', '2025-12-12', '2025-11-17 01:48:32'),
	(20, '202301001', 'B006', '9787111558058', '编译原理', 'Alfred V. Aho', '计算机软件基础', NULL, NULL, '2023-12-15', '2024-03-15', '2024-03-09', '2025-11-17 01:48:32'),
	(21, '202301001', 'B007', '9787111547434', '计算机组成与设计', 'David A. Patterson', '计算机系统 / 组成', '机械工业出版社', '2019', '2023-12-10', '2024-03-10', '2024-02-05', '2025-11-17 01:48:32'),
	(22, '202301002', 'B001', '9787115546081', '深入理解计算机系统', 'Randal E. Bryant', '计算机系统 / 组成', NULL, NULL, '2025-12-17', '2026-01-23', '2025-12-20', '2025-12-17 10:17:47'),
	(23, '202301002', 'B007', '9787111547434', '计算机组成与设计', 'David A. Patterson', '计算机系统 / 组成', NULL, NULL, '2025-12-19', '2026-01-22', NULL, '2025-12-20 08:19:55');

-- 导出  表 ai_learning_partner.practical_projects 结构
CREATE TABLE IF NOT EXISTS `practical_projects` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cover_image` varchar(500) DEFAULT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `current_learners` int DEFAULT '0',
  `description` text,
  `detailed_content` text,
  `difficulty` varchar(255) NOT NULL,
  `estimated_days` int DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `tutorial` text,
  `type` varchar(255) NOT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.practical_projects 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.practical_project_excellent_projects 结构
CREATE TABLE IF NOT EXISTS `practical_project_excellent_projects` (
  `practical_project_id` bigint NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  KEY `FK5mpbpxd7hrjh6bde8mfisvi` (`practical_project_id`),
  CONSTRAINT `FK5mpbpxd7hrjh6bde8mfisvi` FOREIGN KEY (`practical_project_id`) REFERENCES `practical_projects` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.practical_project_excellent_projects 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.practical_project_teaching_videos 结构
CREATE TABLE IF NOT EXISTS `practical_project_teaching_videos` (
  `practical_project_id` bigint NOT NULL,
  `teaching_videos` varchar(255) DEFAULT NULL,
  KEY `FKla3hpsr48j1b9vin0xlusad9s` (`practical_project_id`),
  CONSTRAINT `FKla3hpsr48j1b9vin0xlusad9s` FOREIGN KEY (`practical_project_id`) REFERENCES `practical_projects` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.practical_project_teaching_videos 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.qa_history 结构
CREATE TABLE IF NOT EXISTS `qa_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) DEFAULT NULL,
  `question` text NOT NULL,
  `answer` text,
  `knowledge_graph_data` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
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
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_major` (`major`),
  KEY `idx_grade` (`grade`),
  KEY `idx_college` (`college`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- 正在导出表  ai_learning_partner.users 的数据：~11 rows (大约)
INSERT INTO `users` (`id`, `student_id`, `username`, `name`, `password`, `gender`, `major`, `college`, `grade`, `class_name`, `email`, `phone`, `avatar_url`, `enrollment_date`, `graduation_date`, `learning_goal`, `learning_preferences`, `account_status`, `last_login_time`, `create_time`, `update_time`, `created_at`, `login_count`, `updated_at`) VALUES
	(30, '202301001', 'zhangsan', '张三', '123456', 'M', '01-计算机科学与技术', '01-计算机科学与技术学院', '大三', '计科2301', 'zhansan@edu.cn', '13800132536', 'https://picsum.photos/100/100?random=1', '2023-09-01', '2027-06-30', '成为一名软件工程师\n', '{"learning_style": "visual", "difficulty_level": "medium", "preferred_subjects": ["编程", "算法"], "preferred_study_time": "evening"}', 'ACTIVE', '2025-12-20 16:15:09', NULL, '2025-12-20 16:15:35.806344', NULL, 43, '2025-12-20 16:15:35.806344'),
	(31, '202301002', 'lisi', '李四', '123456', 'M', '01-计算机科学与技术', '01-计算机科学与技术学院', '大三', '计科2301', 'lisi@edu.cn', '13800138002', 'https://picsum.photos/100/100?random=2', '2023-09-01', '2027-06-30', '深入算法研究，参加ACM竞赛并获得奖项', '{"learning_style": "logical", "difficulty_level": "high", "preferred_subjects": ["算法", "数学"], "preferred_study_time": "morning"}', 'ACTIVE', '2025-11-10 10:27:07', NULL, NULL, NULL, NULL, NULL),
	(32, '202301003', 'wangwu', '王五', '123456', 'F', '01-计算机科学与技术', '01-计算机科学与技术学院', '大三', '计科2301', 'wangwu@edu.cn', '13800138003', 'https://picsum.photos/100/100?random=3', '2023-09-01', '2027-06-30', '提升编程能力，学习数据科学和机器学习', '{"learning_style": "practical", "difficulty_level": "medium", "preferred_subjects": ["数据科学", "统计学"], "preferred_study_time": "afternoon"}', 'ACTIVE', '2024-02-19 14:20:00', NULL, NULL, NULL, NULL, NULL),
	(33, '202301021', 'zhaoliu', '赵六', '123456', 'M', '02-软件工程', '01-计算机科学与技术学院', '大三', '软工2301', 'zhaoliu@edu.cn', '13800138004', 'https://picsum.photos/100/100?random=4', '2023-09-01', '2027-06-30', '掌握软件开发全流程，参与大型项目实战', '{"learning_style": "practical", "difficulty_level": "medium", "preferred_subjects": ["软件开发", "项目管理"], "preferred_study_time": "evening"}', 'INACTIVE', '2024-02-20 10:05:00', NULL, NULL, NULL, NULL, NULL),
	(34, '202201001', 'zhengshiyi', '郑十一', '123456', 'M', '01-计算机科学与技术', '01-计算机科学与技术学院', '大四', '计科2201', 'zhengshiyi@edu.cn', '13800138009', 'https://picsum.photos/100/100?random=6', '2022-09-01', '2026-06-30', '深入学习人工智能和机器学习，为研究生阶段做准备', '{"learning_style": "theoretical", "difficulty_level": "high", "preferred_subjects": ["人工智能", "机器学习"], "preferred_study_time": "morning"}', 'ACTIVE', '2025-12-16 21:47:59', NULL, '2025-12-16 21:47:59.007996', NULL, 3, '2025-12-16 21:47:59.007996'),
	(35, '202102011', 'lishisan', '李十三', '123456', 'M', '03-新闻学', '15-文学院', '大一', '新闻2201', 'lishisan@edu.cn', '13800138014', 'https://picsum.photos/100/100?random=7', '2021-09-01', '2026-06-30', '准备考研，深入研究分布式系统和云计算技术', '{"learning_style": "comprehensive", "difficulty_level": "high", "preferred_subjects": ["分布式系统", "云计算"], "preferred_study_time": "whole_day"}', 'ACTIVE', '2024-02-20 11:20:00', NULL, '2025-12-20 16:22:06.334255', NULL, NULL, '2025-12-20 16:22:06.334255'),
	(36, '202301023', 'Jasmine', '孙梦云', '123456', 'F', '02-应用化学', '11-化学学院', '大一', '软工2301', '15268660761@163.com', '13965823245', 'https://picsum.photos/100/100?random=57', '2023-09-01', '2027-06-30', '实习', NULL, 'ACTIVE', '2025-11-10 08:39:59', NULL, '2025-12-20 16:20:46.689957', NULL, NULL, '2025-12-20 16:20:46.689957'),
	(37, 'ADMIN001', 'admin', '系统管理员', '123456', 'M', '01-计算机科学与技术', '01-计算机科学与技术学院', '大三', '管理员', 'admin@edu.cn', '13800138012', 'https://picsum.photos/100/100?random=99', '2020-08-10', NULL, NULL, NULL, 'ACTIVE', '2025-12-20 16:16:24', NULL, '2025-12-20 16:16:40.953689', NULL, 24, '2025-12-20 16:16:40.953689'),
	(38, '202301024', 'peng', '彭雨淇', '123456', 'F', '02-软件工程', '01-计算机科学与技术学院', '大三', '软工2301', '1235698256@qq.com', '19586235496', 'https://picsum.photos/100/100?random=19', '2023-09-01', '2027-06-30', '实习', NULL, 'ACTIVE', '2025-11-18 17:21:31', '2025-11-17 06:41:04.180004', '2025-11-18 17:21:31.078363', '2025-11-17 06:41:04.180004', 8, '2025-11-18 17:21:31.078363'),
	(39, '202301027', 'gugugu', '顾时夜', '123456', 'M', '02-软件工程', '01-计算机科学与技术学院', '大三', '软工2301', '15743722263@qq.com', '19625321253', 'https://picsum.photos/100/100?random=94', '2023-09-01', NULL, '加油', NULL, 'ACTIVE', '2025-12-18 02:25:44', '2025-12-18 02:22:12.440809', '2025-12-18 02:26:17.984853', '2025-12-18 02:22:12.440809', 1, '2025-12-18 02:26:17.984853'),
	(40, '202301009', 'wang', '王明', '123456', 'M', '03-国际经济与贸易', '12-经济管理学院', '大三', '国贸2301', '1574372639@qq.com', '19632563256', 'https://picsum.photos/100/100?random=15', '2023-09-01', NULL, NULL, NULL, 'ACTIVE', NULL, '2025-12-20 16:21:56.778571', '2025-12-20 16:21:56.778571', '2025-12-20 16:21:56.778571', 0, '2025-12-20 16:21:56.778571');

-- 导出  表 ai_learning_partner.user_answer_details 结构
CREATE TABLE IF NOT EXISTS `user_answer_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `is_correct` tinyint(1) DEFAULT '0',
  `question_id` bigint NOT NULL,
  `quiz_record_id` bigint NOT NULL,
  `time_spent` int DEFAULT NULL,
  `user_answer` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.user_answer_details 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.user_career_preferences 结构
CREATE TABLE IF NOT EXISTS `user_career_preferences` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `expected_position` varchar(100) DEFAULT NULL,
  `expected_industry` varchar(50) DEFAULT NULL,
  `expected_location` varchar(50) DEFAULT NULL,
  `expected_salary` varchar(50) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.user_career_preferences 的数据：~1 rows (大约)
INSERT INTO `user_career_preferences` (`id`, `user_id`, `expected_position`, `expected_industry`, `expected_location`, `expected_salary`, `created_at`, `updated_at`) VALUES
	(1, 1, '后端开发工程师', '游戏', '北京', NULL, '2025-11-17 07:01:12', '2025-11-17 00:08:43');

-- 导出  表 ai_learning_partner.user_project_progress 结构
CREATE TABLE IF NOT EXISTS `user_project_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `completed_modules` int DEFAULT '0',
  `create_time` datetime(6) DEFAULT NULL,
  `expected_completion_date` datetime(6) DEFAULT NULL,
  `last_activity_time` datetime(6) DEFAULT NULL,
  `progress_percentage` double DEFAULT NULL,
  `project_id` bigint NOT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `submission_count` int DEFAULT '0',
  `total_modules` int NOT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.user_project_progress 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.user_quiz_records 结构
CREATE TABLE IF NOT EXISTS `user_quiz_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `correct_answers` int NOT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `details` json DEFAULT NULL,
  `quiz_date` datetime(6) DEFAULT NULL,
  `quiz_type` varchar(50) DEFAULT NULL,
  `score` double NOT NULL,
  `time_spent` int DEFAULT NULL,
  `total_questions` int NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.user_quiz_records 的数据：~0 rows (大约)

-- 导出  表 ai_learning_partner.user_role 结构
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role` enum('STUDENT','ADMIN') NOT NULL DEFAULT 'STUDENT' COMMENT '角色',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`),
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色表';

-- 正在导出表  ai_learning_partner.user_role 的数据：~10 rows (大约)
INSERT INTO `user_role` (`id`, `user_id`, `role`, `created_at`) VALUES
	(1, 35, 'STUDENT', '2025-11-16 07:54:38'),
	(2, 34, 'STUDENT', '2025-11-16 07:54:38'),
	(3, 30, 'STUDENT', '2025-11-16 07:54:38'),
	(4, 31, 'STUDENT', '2025-11-16 07:54:38'),
	(5, 32, 'STUDENT', '2025-11-16 07:54:38'),
	(6, 33, 'STUDENT', '2025-11-16 07:54:38'),
	(7, 36, 'STUDENT', '2025-11-16 07:54:38'),
	(8, 38, 'STUDENT', '2025-11-16 07:54:38'),
	(9, 39, 'STUDENT', '2025-11-16 07:54:38'),
	(16, 37, 'ADMIN', '2025-11-16 07:54:38'),
	(17, 40, 'STUDENT', '2025-12-20 08:21:57');

-- 导出  表 ai_learning_partner.user_skills 结构
CREATE TABLE IF NOT EXISTS `user_skills` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `skill_name` varchar(255) NOT NULL,
  `proficiency_level` varchar(50) DEFAULT NULL,
  `experience_months` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  ai_learning_partner.user_skills 的数据：~9 rows (大约)
INSERT INTO `user_skills` (`id`, `user_id`, `skill_name`, `proficiency_level`, `experience_months`, `created_at`) VALUES
	(1, 1, 'JavaScript', 'ADVANCED', 24, '2025-11-16 23:23:55'),
	(2, 1, 'HTML/CSS', 'ADVANCED', 24, '2025-11-16 23:23:55'),
	(3, 1, 'Vue', 'INTERMEDIATE', 12, '2025-11-16 23:23:55'),
	(4, 1, 'React', 'BEGINNER', 6, '2025-11-16 23:23:55'),
	(5, 1, 'Node.js', 'INTERMEDIATE', 18, '2025-11-16 23:23:55'),
	(6, 1, '数据结构', 'INTERMEDIATE', 12, '2025-11-16 23:23:55'),
	(7, 1, 'Java', 'BEGINNER', 3, '2025-11-16 23:23:55'),
	(8, 1, 'MySQL', 'INTERMEDIATE', 8, '2025-11-16 23:23:55'),
	(9, 1, 'Redis', 'BEGINNER', 2, '2025-11-16 23:23:55');

-- 导出  触发器 ai_learning_partner.trg_academic_name_to_course 结构
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `trg_academic_name_to_course` AFTER UPDATE ON `academic_records` FOR EACH ROW BEGIN
    -- 仅当课程名发生变化时，同步到courses表
    IF OLD.course_name != NEW.course_name THEN
        UPDATE courses
        SET course_name = NEW.course_name
        WHERE course_id = NEW.course_id;
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
