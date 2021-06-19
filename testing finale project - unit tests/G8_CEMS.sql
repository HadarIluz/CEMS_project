-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cems
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `active_exam`
--

DROP TABLE IF EXISTS `active_exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `active_exam` (
  `exam` varchar(45) NOT NULL,
  `startTime` time DEFAULT NULL,
  `timeAllotedForTest` int DEFAULT NULL,
  `examCode` varchar(45) DEFAULT NULL,
  `examType` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`exam`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `active_exam`
--

LOCK TABLES `active_exam` WRITE;
/*!40000 ALTER TABLE `active_exam` DISABLE KEYS */;
INSERT INTO `active_exam` VALUES ('010205','21:40:00',123,'MA12','computerized');
/*!40000 ALTER TABLE `active_exam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `profession` varchar(45) NOT NULL,
  `courseID` varchar(45) NOT NULL,
  `courseName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`profession`,`courseID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES ('01','01','Calculus'),('01','02','Algebra'),('01','03','Differential Equations'),('02','01','Intro to Computer Science'),('02','02','Algorithms'),('02','03','Data structures'),('02','04','Advanced Programming'),('03','01','Electricity'),('03','02','Physics IE1'),('03','03','Physics IE2'),('03','04','Physics IE3'),('04','01','Basic English'),('04','02','Advanced English A2'),('04','03','Advanced English B2');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam`
--

DROP TABLE IF EXISTS `exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam` (
  `examID` varchar(45) NOT NULL,
  `profession` varchar(45) NOT NULL,
  `course` varchar(45) NOT NULL,
  `timeAllotedForTest` int DEFAULT NULL,
  `commentForTeacher` varchar(150) DEFAULT NULL,
  `commentForStudents` varchar(150) DEFAULT NULL,
  `author` int DEFAULT NULL,
  `status` enum('inActive','active') DEFAULT 'inActive',
  `examType` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`examID`,`profession`,`course`),
  KEY `profession_idx` (`profession`),
  KEY `course_idx` (`course`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam`
--

LOCK TABLES `exam` WRITE;
/*!40000 ALTER TABLE `exam` DISABLE KEYS */;
INSERT INTO `exam` VALUES ('010102','01','01',180,'The test contains the same question as another question solved in one of the lectures.\"\"','The test includes 10 American questions.\\n\"',222222222,'active','computerized'),('010203','01','02',120,'The test contains the same question as another question solved in one of the lectures.\"\"','The test includes 10 American questions.\\n\"',222222222,'inActive','computerized'),('010204','01','02',123,NULL,NULL,222222222,'inActive','computerized'),('010205','01','02',123,'asd','as',222222222,'active','computerized'),('010206','01','02',111,NULL,NULL,222222222,'inActive','manual'),('010207','01','02',111,NULL,NULL,222222222,'inActive','manual'),('010208','01','02',111,NULL,NULL,222222222,'inActive','manual'),('010209','01','02',111,NULL,NULL,222222222,'inActive','manual'),('020101','02','01',100,'The test contains the same question as another question solved in one of the lectures.\"\"','The test includes 10 American questions.\\n\"',333333333,'inActive','computerized'),('020102','02','01',120,'The test contains the same question as another question solved in one of the lectures.\"\"','The test includes 10 American questions.\\n\"',333333333,'inActive','computerized'),('020401','02','04',60,'The test contains the same question as another question solved in one of the lectures.\"\"','The test includes 8 American questions.\\n\"',333333333,'inActive','computerized'),('030101','03','01',180,'The test contains the same question as another question solved in one of the lectures.\"\"','The test includes 10 American questions.\\n\"',222222222,'inActive','computerized'),('030201','03','02',70,'The test contains the same question as another question solved in one of the lectures.\"\"','The test includes 10 American questions.\\n\"',222222222,'inActive','computerized'),('030202','03','03',120,'The test contains the same question as another question solved in one of the lectures.\"\"','The test includes 10 American questions.\\n\"',222222222,'inActive','computerized'),('030402','03','04',111,NULL,NULL,222222222,'inActive','manual'),('030403','03','04',111,NULL,NULL,222222222,'inActive','manual'),('040101','04','01',60,'The test contains the same question as another question solved in one of the lectures.\"\"','The test includes 8 American questions.\\n\"',555555555,'inActive','manual'),('040201','04','02',120,'The test contains the same question as another question solved in one of the lectures.\"\"','The test includes 4 American questions.\\n\"',555555555,'inActive','computerized'),('040301','04','03',100,'The test contains the same question as another question solved in one of the lectures.\"\"','The test includes 4 American questions.\\n\"',555555555,'inActive','computerized');
/*!40000 ALTER TABLE `exam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam_of_student`
--

DROP TABLE IF EXISTS `exam_of_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam_of_student` (
  `student` int NOT NULL,
  `exam` varchar(45) NOT NULL,
  `examType` varchar(45) DEFAULT NULL,
  `score` int DEFAULT NULL,
  `updateReason` varchar(250) DEFAULT NULL,
  `totalTime` int DEFAULT NULL,
  `reason_of_submit` enum('forced','initiated') DEFAULT NULL,
  PRIMARY KEY (`student`,`exam`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam_of_student`
--

LOCK TABLES `exam_of_student` WRITE;
/*!40000 ALTER TABLE `exam_of_student` DISABLE KEYS */;
INSERT INTO `exam_of_student` VALUES (111111111,'010202','computerized',100,NULL,NULL,NULL),(111111111,'010203','computerized',40,NULL,0,NULL),(123456789,'010202',NULL,50,NULL,NULL,NULL),(987654321,'010202',NULL,80,NULL,NULL,NULL);
/*!40000 ALTER TABLE `exam_of_student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam_records`
--

DROP TABLE IF EXISTS `exam_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam_records` (
  `examID` varchar(45) NOT NULL,
  `endTime` time NOT NULL,
  `timeAlloted` int DEFAULT NULL,
  `actualTime` int DEFAULT NULL,
  `initiatedSubmit` int DEFAULT NULL,
  `forcedSubmit` int DEFAULT NULL,
  `totalStudents` int DEFAULT NULL,
  PRIMARY KEY (`examID`,`endTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam_records`
--

LOCK TABLES `exam_records` WRITE;
/*!40000 ALTER TABLE `exam_records` DISABLE KEYS */;
INSERT INTO `exam_records` VALUES ('010203','11:49:55',0,79,1,0,1),('010204','20:01:21',123,11,0,0,0),('010204','20:03:27',123,3,1,0,1),('010204','20:17:11',123,7,2,0,2),('010204','20:23:43',123,3,2,0,2),('010205','20:09:53',123,9,2,0,2),('010205','20:31:24',123,1,2,0,2);
/*!40000 ALTER TABLE `exam_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `extension_request`
--

DROP TABLE IF EXISTS `extension_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `extension_request` (
  `exam` varchar(45) NOT NULL,
  `additional time` varchar(45) DEFAULT NULL,
  `reason` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`exam`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extension_request`
--

LOCK TABLES `extension_request` WRITE;
/*!40000 ALTER TABLE `extension_request` DISABLE KEYS */;
INSERT INTO `extension_request` VALUES ('010203','30','BLA'),('020401','11','11');
/*!40000 ALTER TABLE `extension_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profession`
--

DROP TABLE IF EXISTS `profession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profession` (
  `professionID` varchar(45) NOT NULL,
  `professionName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`professionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profession`
--

LOCK TABLES `profession` WRITE;
/*!40000 ALTER TABLE `profession` DISABLE KEYS */;
INSERT INTO `profession` VALUES ('01','Mathematics'),('02','Computer Science'),('03','Physics'),('04','English');
/*!40000 ALTER TABLE `profession` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `teacher` int NOT NULL,
  `questionID` varchar(45) NOT NULL,
  `profession` varchar(45) DEFAULT NULL,
  `question` varchar(250) DEFAULT NULL,
  `answer1` varchar(150) DEFAULT NULL,
  `answer2` varchar(150) DEFAULT NULL,
  `answer3` varchar(150) DEFAULT NULL,
  `answer4` varchar(150) DEFAULT NULL,
  `correctAnswerIndex` int DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`teacher`,`questionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (222222222,'01001','01','Solve X where 5X = 20','4','5','10','15',1,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(222222222,'01002','01','Solve X where X - 5 = 15        ','10','15','20','25',3,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(222222222,'01003','01','Solve X where 3X + 7 - 2 = 8','1','2','3','4',1,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(222222222,'01004','01','3x + 6x = ','3x','9 + 2x','9x+2','9x',4,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(222222222,'01005','01','5a + 2b - b + 4a =','9a + b','a + b','7a - 3b','6a',1,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(222222222,'01006','01','Find the value of 3abc, where a = 2, b = 3 and c = 4','12','27','72','82',3,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(222222222,'01007','01','The sum of a certain number and 3 times the number is 40. What is the number?','8','9','10','11',3,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(222222222,'01008','01','In V = 2m - 3, V is which of the following?','Constant','Dependent Variable','Independent Variable','Exponent',2,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(222222222,'01009','01','In B = 3 + 4w, 3 is which of the following?','Constant','Dependent Variable','Independent Variable','Exponent',1,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(222222222,'01010','01','6x - 10 = 4(x + 3). Solve for x','x = 9','x = 10','x = 11','x = 12',3,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(222222222,'01012','01','2 +2','2','22','5','4',4,'shit'),(333333333,'02001','02','During program development, software requirements specify','How the program will accomplish the task','What the task is that the program must perform','How to divide the task into subtasks','How to test the program when it is done',2,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(333333333,'02002','02','Of the following, if statements, which one correctly executes three instructions if the condition is true?','If (x < 0) a = b * 2; y = x; z = a ג€“ y;','{ if (x < 0) a = b * 2; y = x; z = a ג€“ y; }','If{ (x < 0) a = b * 2; y = x; z = a ג€“ y ; }',' If (x < 0) { a = b * 2; y = x; z = a ג€“ y; }',4,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(333333333,'02003','02','Which of the sets of statements below will add 1 to x if x is positive and subtract 1 from x if x is negative but leave x alone if x is 0?','If (x > 0) x++; else x--;','If (x > 0) x++; else if (x < 0) x--;',' If (x == 0) x = 0; else x++; x--;','X++; x--;',3,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(333333333,'02004','02','_______ is the process of finding errors and fixing them within a program.','Compiling','Executing','Debugging','Scanning',3,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(333333333,'02005','02','Sal needs to execute a section of code ten times within a program. Compare the selection structures below and select which one meets the needs identified.','If-Else','For','While','If',2,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(333333333,'02006','02','A loop that never ends is referred to as a(n)_________.','While loop','Infinite loop','Recursive loop','for loop',2,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(333333333,'02007','02','Kim has just constructed her first for loop within the Java language. Which of the following is not a required part of a for loop?','Initialization','Condition','Variable','increment',3,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(333333333,'02008','02','Which command will stop an infinite loop?','Alt - C','Shift - C','Esc','Ctrl - C',4,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(555555555,'04001','04','Who works in the hospital?','Students','Doctors','Farmers','Teachers',2,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(555555555,'04002','04','Sunday is the _________ day of the week.','first','third','second','last',1,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(555555555,'04003','04','A: How old is Jack?','He has ten years old.','He is years ten old','He is ten year old.','He is ten years old.',4,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(555555555,'04004','04','A:  _________ are you?','Who','How','What','Where',2,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(555555555,'04005','04','December is the last _________ of the year.','time','month','day','week',2,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(555555555,'04006','04','How many days are there in a week?','twelve days','thirty day','five days','seven days',4,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(555555555,'04007','04','There are _________  months in a year.','seven','ten','twelve','twenty',3,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"'),(555555555,'04008','04','If yesterday was Tuesday, what day is today?','Tuesday','Friday','Thursday','Wednesday',4,'The possible answers are only final answers. You can use a draft page. If there is an incorrect answer choice as a result of a calculation error you still get zero points.\"\"');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_in_exam`
--

DROP TABLE IF EXISTS `question_in_exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_in_exam` (
  `question` varchar(45) NOT NULL,
  `score` int DEFAULT NULL,
  `exam` varchar(45) NOT NULL,
  PRIMARY KEY (`question`,`exam`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_in_exam`
--

LOCK TABLES `question_in_exam` WRITE;
/*!40000 ALTER TABLE `question_in_exam` DISABLE KEYS */;
INSERT INTO `question_in_exam` VALUES ('01001',50,'010203'),('01001',50,'010205'),('01002',10,'010203'),('01003',10,'010203'),('01004',10,'010203'),('01005',10,'010203'),('01006',10,'010203'),('01007',10,'010203'),('01008',10,'010203'),('01008',50,'010204'),('01009',10,'010203'),('01010',10,'010203'),('01010',25,'010204'),('01011',25,'010204'),('01012',50,'010205'),('02001',12,'020401'),('02002',12,'020401'),('02003',12,'020401'),('02004',12,'020401'),('02005',12,'020401'),('02006',12,'020401'),('02007',14,'020401'),('02008',14,'020401'),('04001',15,'040101'),('04002',15,'040101'),('04003',15,'040101'),('04004',15,'040101'),('04005',15,'040101'),('04006',15,'040101'),('04007',5,'040101'),('04008',5,'040101');
/*!40000 ALTER TABLE `question_in_exam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` int NOT NULL,
  `AVG` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (111111111,66.7),(123456789,92),(987654321,80.4);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_answers_in_exam`
--

DROP TABLE IF EXISTS `student_answers_in_exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_answers_in_exam` (
  `student` int NOT NULL,
  `exam` varchar(45) NOT NULL,
  `question` varchar(45) NOT NULL,
  `answer` int DEFAULT NULL,
  `correct` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`student`,`exam`,`question`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_answers_in_exam`
--

LOCK TABLES `student_answers_in_exam` WRITE;
/*!40000 ALTER TABLE `student_answers_in_exam` DISABLE KEYS */;
INSERT INTO `student_answers_in_exam` VALUES (111111111,'010203','01001',1,0),(111111111,'010203','01002',2,0),(111111111,'010203','01003',3,0),(111111111,'010203','01004',4,0),(111111111,'010203','01005',3,0),(111111111,'010203','01006',2,0),(111111111,'010203','01007',0,1),(111111111,'010203','01008',0,1),(111111111,'010203','01009',0,1),(111111111,'010203','01010',0,1);
/*!40000 ALTER TABLE `student_answers_in_exam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_in_course`
--

DROP TABLE IF EXISTS `student_in_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_in_course` (
  `student` int NOT NULL,
  `course` varchar(45) NOT NULL,
  PRIMARY KEY (`student`,`course`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_in_course`
--

LOCK TABLES `student_in_course` WRITE;
/*!40000 ALTER TABLE `student_in_course` DISABLE KEYS */;
INSERT INTO `student_in_course` VALUES (111111111,'Electricity'),(123456789,'Physics IE3'),(987654321,'Data structures');
/*!40000 ALTER TABLE `student_in_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher_in_profession`
--

DROP TABLE IF EXISTS `teacher_in_profession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher_in_profession` (
  `teacher` int NOT NULL,
  `profession` varchar(45) NOT NULL,
  PRIMARY KEY (`teacher`,`profession`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher_in_profession`
--

LOCK TABLES `teacher_in_profession` WRITE;
/*!40000 ALTER TABLE `teacher_in_profession` DISABLE KEYS */;
INSERT INTO `teacher_in_profession` VALUES (222222222,'01'),(222222222,'03'),(333333333,'02'),(555555555,'04');
/*!40000 ALTER TABLE `teacher_in_profession` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL,
  `password` varchar(45) DEFAULT NULL,
  `firstName` varchar(45) DEFAULT NULL,
  `lastName` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `userType` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (111111111,'123123','Student3','soso','stud3@gmail.com','Student'),(123456789,'pass123','Student1','sasa','stud1@gmail.com','Student'),(222222222,'PASS123','teacher1','tete','Teacher1@gmail.com','Teacher'),(333333333,'password','teacher2','tata','Teacher2@gmail.com','Teacher'),(444444444,'1234','principal1','papa','Principal1@gmail.com','Principal'),(555555555,'a1b2c3d4','teacher3','toto','Teacher3@gmail.com','Teacher'),(987654321,'123','Student2','sisi','stud2@gmail.com','Student');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-16 20:44:26
