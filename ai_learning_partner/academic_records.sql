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

-- 导出  表 ai_learning_partner.academic_records 结构
CREATE TABLE IF NOT EXISTS `academic_records` (
  `record_id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) NOT NULL COMMENT '学号',
  `course_id` varchar(20) NOT NULL COMMENT '课程ID',
  `academic_year` varchar(9) NOT NULL COMMENT '学年',
  `semester` tinyint NOT NULL COMMENT '学期',
  `score` decimal(5,2) DEFAULT NULL COMMENT '成绩',
  `grade_point` decimal(3,2) DEFAULT NULL COMMENT '绩点',
  `grade_level` varchar(10) DEFAULT NULL COMMENT '等级',
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
  KEY `fk_academic_course` (`course_id`),
  CONSTRAINT `fk_academic_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`),
  CONSTRAINT `fk_academic_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学业成绩表';

-- 正在导出表  ai_learning_partner.academic_records 的数据：~0 rows (大约)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
