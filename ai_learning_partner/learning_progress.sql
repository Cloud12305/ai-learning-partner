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

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
