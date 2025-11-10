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
  `login_count` int DEFAULT '0' COMMENT '登录次数',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_id` (`student_id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_major` (`major`),
  KEY `idx_grade` (`grade`),
  KEY `idx_college` (`college`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表（合并学生信息）';

-- 正在导出表  ai_learning_partner.users 的数据：~7 rows (大约)
INSERT INTO `users` (`id`, `student_id`, `username`, `name`, `password`, `gender`, `major`, `college`, `grade`, `class_name`, `email`, `phone`, `avatar_url`, `enrollment_date`, `graduation_date`, `learning_goal`, `learning_preferences`, `account_status`, `login_count`, `last_login_time`, `created_at`, `updated_at`, `create_time`, `update_time`) VALUES
	(1, '202301001', 'zhangsan', '张三', '123456', 'M', '计算机科学与技术', '计算机学院', '2023', '计科2301', 'zhangsan@edu.cn', '13800138001', 'https://picsum.photos/100/100?random=1', '2023-08-15', '2027-06-13', '成为全栈工程师，掌握AI开发技术', '{"learning_style": "visual", "difficulty_level": "medium", "preferred_subjects": ["编程", "算法"], "preferred_study_time": "evening"}', 'ACTIVE', 173, '2025-11-10 04:40:16', '2023-08-31 16:00:00', '2025-11-09 20:40:16', NULL, '2025-11-10 04:40:16'),
	(2, '202301002', 'lisi', '李四', '123456', 'M', '计算机科学与技术', '计算机学院', '2023', '计科2301', 'lisi@edu.cn', '13800138002', 'https://picsum.photos/100/100?random=2', '2023-09-01', '2027-06-30', '深入算法研究，参加ACM竞赛并获得奖项', '{"learning_style": "logical", "difficulty_level": "high", "preferred_subjects": ["算法", "数学"], "preferred_study_time": "morning"}', 'ACTIVE', 203, '2024-02-20 09:15:00', '2023-08-31 16:00:00', '2025-11-07 09:37:55', NULL, NULL),
	(3, '202301003', 'wangwu', '王五', '123456', 'F', '计算机科学与技术', '计算机学院', '2023', '计科2301', 'wangwu@edu.cn', '13800138003', 'https://picsum.photos/100/100?random=3', '2023-09-01', '2027-06-30', '提升编程能力，学习数据科学和机器学习', '{"learning_style": "practical", "difficulty_level": "medium", "preferred_subjects": ["数据科学", "统计学"], "preferred_study_time": "afternoon"}', 'ACTIVE', 89, '2024-02-19 14:20:00', '2023-08-31 16:00:00', '2025-11-07 09:37:57', NULL, NULL),
	(4, '202302001', 'zhaoliu', '赵六', '123456', 'M', '软件工程', '软件学院', '2023', '软工2301', 'zhaoliu@edu.cn', '13800138004', 'https://picsum.photos/100/100?random=4', '2023-09-01', '2027-06-30', '掌握软件开发全流程，参与大型项目实战', '{"learning_style": "practical", "difficulty_level": "medium", "preferred_subjects": ["软件开发", "项目管理"], "preferred_study_time": "evening"}', 'ACTIVE', 134, '2024-02-20 10:05:00', '2023-08-31 16:00:00', '2025-11-07 09:38:02', NULL, NULL),
	(5, '202302002', 'qianqi', '钱七', '123456', 'F', '软件工程', '软件学院', '2023', '软工2301', 'qianqi@edu.cn', '13800138005', 'https://picsum.photos/100/100?random=5', '2023-09-01', '2027-06-30', '学习前端开发技术，成为专业的UI/UX设计师', '{"learning_style": "creative", "difficulty_level": "low", "preferred_subjects": ["设计", "前端开发"], "preferred_study_time": "flexible"}', 'ACTIVE', 78, '2024-02-18 16:45:00', '2023-08-31 16:00:00', '2025-11-07 09:38:04', NULL, NULL),
	(6, '202201001', 'zhengshiyi', '郑十一', '123456', 'M', '计算机科学与技术', '计算机学院', '2022', '计科2201', 'zhengshiyi@edu.cn', '13800138009', 'https://picsum.photos/100/100?random=6', '2022-09-01', '2026-06-30', '深入学习人工智能和机器学习，为研究生阶段做准备', '{"learning_style": "theoretical", "difficulty_level": "high", "preferred_subjects": ["人工智能", "机器学习"], "preferred_study_time": "morning"}', 'ACTIVE', 287, '2024-02-20 07:50:00', '2022-08-31 16:00:00', '2025-11-07 09:38:06', NULL, NULL),
	(7, '202101001', 'lishisan', '李十三', '123456', 'M', '软件工程', '软件学院', '2021', '软工2101', 'lishisan@edu.cn', '13800138011', 'https://picsum.photos/100/100?random=7', '2021-09-01', '2025-06-30', '准备考研，深入研究分布式系统和云计算技术', '{"learning_style": "comprehensive", "difficulty_level": "high", "preferred_subjects": ["分布式系统", "云计算"], "preferred_study_time": "whole_day"}', 'ACTIVE', 345, '2024-02-20 11:20:00', '2021-08-31 16:00:00', '2025-11-07 09:38:10', NULL, NULL),
	(29, '202301110', 'Jasmine', '孙梦云', '123456', 'F', '软件工程', '计算机学院', '2023', '软工2301', '15268660761@163.com', NULL, 'https://picsum.photos/100/100?random=57', NULL, NULL, NULL, NULL, 'ACTIVE', 4, '2025-11-10 06:48:09', '2025-11-09 19:58:49', '2025-11-09 22:48:09', '2025-11-10 03:58:49', '2025-11-10 06:48:09');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
