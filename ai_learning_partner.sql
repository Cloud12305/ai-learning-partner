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

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
