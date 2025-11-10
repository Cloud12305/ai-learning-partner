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

-- 导出  表 ai_learning_partner.courses 结构
CREATE TABLE IF NOT EXISTS `courses` (
  `course_id` varchar(20) NOT NULL COMMENT '课程ID',
  `course_name` varchar(100) NOT NULL COMMENT '课程名称',
  `course_name_en` varchar(100) DEFAULT NULL COMMENT '课程英文名称',
  `credits` decimal(3,1) NOT NULL COMMENT '学分',
  `department` varchar(50) NOT NULL COMMENT '开课院系',
  `course_category` varchar(30) DEFAULT NULL COMMENT '课程类别',
  `difficulty_level` tinyint DEFAULT '3' COMMENT '难度等级 1-5',
  `total_hours` smallint DEFAULT NULL COMMENT '总学时',
  `theory_hours` smallint DEFAULT NULL COMMENT '理论学时',
  `practice_hours` smallint DEFAULT NULL COMMENT '实践学时',
  `syllabus_text` text COMMENT '教学大纲',
  `course_description` text COMMENT '课程描述',
  `assessment_method` varchar(50) DEFAULT NULL COMMENT '考核方式',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否激活',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程表';

-- 正在导出表  ai_learning_partner.courses 的数据：~11 rows (大约)
INSERT INTO `courses` (`course_id`, `course_name`, `course_name_en`, `credits`, `department`, `course_category`, `difficulty_level`, `total_hours`, `theory_hours`, `practice_hours`, `syllabus_text`, `course_description`, `assessment_method`, `is_active`, `created_at`, `updated_at`) VALUES
	('CS101', '计算机导论', 'Introduction to Computer Science', 3.0, '计算机学院', '必修', 3, 64, 48, 16, NULL, '计算机科学基础知识入门，包括计算机组成、操作系统、网络等基础概念', '考试+作业', 1, '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('CS102', 'C语言程序设计', 'C Programming Language', 4.0, '计算机学院', '必修', 4, 80, 48, 32, NULL, 'C语言编程基础与算法，培养程序设计思维和编程能力', '考试+实验', 1, '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('CS201', '数据结构', 'Data Structures', 4.0, '计算机学院', '必修', 4, 80, 48, 32, NULL, '线性表、树、图等数据结构的基本原理和算法实现', '考试+编程作业', 1, '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('CS202', '操作系统', 'Operating Systems', 3.0, '计算机学院', '必修', 4, 64, 48, 16, NULL, '进程管理、内存管理、文件系统等操作系统核心概念', '考试+课程设计', 1, '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('GE101', '大学英语', 'College English', 2.0, '外语学院', '必修', 2, 48, 48, 0, NULL, '英语听说读写综合训练，提升英语应用能力', '考试+平时表现', 1, '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('GE102', '大学物理', 'College Physics', 3.0, '物理学院', '必修', 3, 64, 48, 16, NULL, '力学、热学、电磁学等物理学基础理论和实验', '考试+实验', 1, '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('MA101', '高等数学A', 'Advanced Mathematics A', 5.0, '数学学院', '必修', 4, 96, 96, 0, NULL, '微积分、极限、导数等数学基础，为后续课程奠定数学基础', '考试', 1, '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('MA102', '线性代数', 'Linear Algebra', 3.0, '数学学院', '必修', 3, 64, 64, 0, NULL, '矩阵、向量空间、线性变换等线性代数核心内容', '考试', 1, '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('SE101', '软件工程导论', 'Introduction to Software Engineering', 2.0, '软件学院', '必修', 2, 48, 48, 0, NULL, '软件开发流程与方法论，了解软件生命周期', '大作业+报告', 1, '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('SE201', 'Java程序设计', 'Java Programming', 4.0, '软件学院', '必修', 3, 80, 48, 32, NULL, '面向对象编程、Java核心技术、GUI编程等', '考试+项目', 1, '2025-11-07 09:31:33', '2025-11-07 09:31:33'),
	('SE301', 'Web开发技术', 'Web Development Technologies', 3.0, '软件学院', '选修', 3, 64, 32, 32, NULL, 'HTML/CSS/JavaScript前后端开发技术', '项目实战', 1, '2025-11-07 09:31:33', '2025-11-07 09:31:33');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
