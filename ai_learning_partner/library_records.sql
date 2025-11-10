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
  `is_overdue` tinyint(1) DEFAULT '0' COMMENT '是否逾期',
  `related_course_id` varchar(20) DEFAULT NULL COMMENT '关联课程ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`record_id`),
  KEY `fk_library_student` (`student_id`),
  KEY `fk_library_course` (`related_course_id`),
  CONSTRAINT `fk_library_course` FOREIGN KEY (`related_course_id`) REFERENCES `courses` (`course_id`),
  CONSTRAINT `fk_library_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图书馆借阅表';

-- 正在导出表  ai_learning_partner.library_records 的数据：~0 rows (大约)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
