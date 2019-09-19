-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: localhost    Database: ocps
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `account` (
  `ACC_NO` int(11) NOT NULL,
  `BANK_ID` int(10) NOT NULL,
  `NAME` varchar(30) DEFAULT NULL,
  `PIN` int(4) DEFAULT NULL,
  `BALANCE` float(10,2) DEFAULT NULL,
  PRIMARY KEY (`ACC_NO`,`BANK_ID`),
  KEY `BANK_ID` (`BANK_ID`),
  CONSTRAINT `account_ibfk_1` FOREIGN KEY (`BANK_ID`) REFERENCES `bank` (`BANK_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (124764357,748912,'Kshitish',1247,98501.00),(240894527,478956,'MAHESH BHATT',1966,98642.00),(297851263,483312,'AKANSHA AGARWAL',2976,47855.00),(457059051,597842,'KARTIK GUPTA',4379,497924.41),(457891206,697459,'GAURI PANDEY',1970,987564.00),(457981464,457896,'ANSHUL BRIJWASI',4592,4049.35),(477851364,469757,'MUSKAN AHUJA',4785,7648.00),(578401604,789568,'AKASH JOSHI',9422,980000.00),(678945230,578912,'TINA GOENKA',5037,75849.00),(879512680,378945,'MAHESH KULKARNI',7890,7618.00);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_keys`
--

DROP TABLE IF EXISTS `auth_keys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `auth_keys` (
  `USER_ID` varchar(25) NOT NULL,
  `TYPE` varchar(15) NOT NULL,
  `VALUE` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`TYPE`,`USER_ID`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `fk_userid` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_keys`
--

LOCK TABLES `auth_keys` WRITE;
/*!40000 ALTER TABLE `auth_keys` DISABLE KEYS */;
INSERT INTO `auth_keys` VALUES ('HOPE.KARTIK','ecg','v30r5vYTcOuykXk'),('MAHESH123','ecg','TxLskzjyLk0oBEJ'),('mishti','ecg','sDHabq#3MdJlMM7'),('AGARWAL9422','face','bEaAgM99bg!OSMz'),('AKASH8122','face','!63hvlcXA#SUX%u'),('gau','face','FsCQu%t6W@VXjER'),('MUSKAAN','face','Wa@DZmvm!@i3g%a'),('GOENKA101','fingerprint','WXS8xHb2@a#ge8j'),('ksdfg','fingerprint','Bav84U@gfEQmJeU'),('KULKARNI23','fingerprint','N6gsXitlOYfS8pn');
/*!40000 ALTER TABLE `auth_keys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authorizes`
--

DROP TABLE IF EXISTS `authorizes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `authorizes` (
  `USER_ID` varchar(25) NOT NULL,
  `ACC_NO` int(10) NOT NULL,
  PRIMARY KEY (`ACC_NO`,`USER_ID`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `fk_authorizes_acc` FOREIGN KEY (`ACC_NO`) REFERENCES `account` (`ACC_NO`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_authorizes_userid` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authorizes`
--

LOCK TABLES `authorizes` WRITE;
/*!40000 ALTER TABLE `authorizes` DISABLE KEYS */;
INSERT INTO `authorizes` VALUES ('AGARWAL9422',297851263),('AKASH8122',578401604),('gau',457891206),('GOENKA101',678945230),('HOPE.KARTIK',457059051),('ksdfg',124764357),('ksdfg',477851364),('KULKARNI23',879512680),('MAHESH123',240894527),('mishti',457981464),('MUSKAAN',477851364);
/*!40000 ALTER TABLE `authorizes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank`
--

DROP TABLE IF EXISTS `bank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bank` (
  `BANK_ID` int(10) NOT NULL,
  `NAME` varchar(30) DEFAULT NULL,
  `CITY` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`BANK_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank`
--

LOCK TABLES `bank` WRITE;
/*!40000 ALTER TABLE `bank` DISABLE KEYS */;
INSERT INTO `bank` VALUES (378945,'CANARA BANK ','BANGALORE'),(457896,'HDFC BANK','PUNE'),(469757,'BANK OF MAHARASHTRA','AGRA'),(478956,'STATE BANK OF INDIA','DELHI'),(483312,'AXIS BANK','MUMBAI'),(578912,'PUNJAB NATIONAL BANK ','DELHI'),(597842,'ICICI BANK','KOLKATA'),(697459,'IDBI BANK','JAIPUR'),(748912,'INDUSIND BANK','LUCKNOW'),(789568,'SBI BANK','PUNE');
/*!40000 ALTER TABLE `bank` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `current`
--

DROP TABLE IF EXISTS `current`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `current` (
  `SERVICE_CHG` float(10,2) DEFAULT NULL,
  `ACC_NO` int(11) NOT NULL,
  PRIMARY KEY (`ACC_NO`),
  CONSTRAINT `fk_current_acc` FOREIGN KEY (`ACC_NO`) REFERENCES `account` (`ACC_NO`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `current`
--

LOCK TABLES `current` WRITE;
/*!40000 ALTER TABLE `current` DISABLE KEYS */;
INSERT INTO `current` VALUES (2.36,124764357),(11.80,457891206),(11.80,457981464),(11.80,578401604),(2.36,678945230);
/*!40000 ALTER TABLE `current` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `savings`
--

DROP TABLE IF EXISTS `savings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `savings` (
  `ACC_NO` int(11) NOT NULL,
  `INTEREST_RATE` float(3,2) DEFAULT NULL,
  PRIMARY KEY (`ACC_NO`),
  CONSTRAINT `fk_savings_acc` FOREIGN KEY (`ACC_NO`) REFERENCES `account` (`ACC_NO`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `savings`
--

LOCK TABLES `savings` WRITE;
/*!40000 ALTER TABLE `savings` DISABLE KEYS */;
INSERT INTO `savings` VALUES (240894527,1.50),(297851263,1.40),(457059051,2.50),(477851364,2.11),(879512680,2.60);
/*!40000 ALTER TABLE `savings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `transactions` (
  `TRANS_ID` int(11) NOT NULL,
  `RECEIVER` int(11) DEFAULT NULL,
  `SENDER` int(11) DEFAULT NULL,
  `AMOUNT` float(10,2) DEFAULT NULL,
  `TIMESTAMP` datetime DEFAULT NULL,
  PRIMARY KEY (`TRANS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (123456789,124764357,240894527,10.01,'2019-09-19 20:32:17'),(350827661,879512680,124764357,54.00,'2019-09-19 23:04:53'),(561406084,457059051,124764357,100.00,'2019-09-19 23:18:32'),(1482198676,124764357,457059051,0.60,'2019-09-19 23:20:59'),(1914810633,124764357,477851364,69.00,'2019-09-19 22:54:54');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `USER_ID` varchar(15) NOT NULL,
  `NAME` varchar(15) DEFAULT NULL,
  `PASSWORD` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('AGARWAL9422','AKANSHA AGARWAL','VEWBIBXEW17'),('AKASH8122',' AKASH JOSHI','ASTFBHB78WR'),('gau','GAURI PANDEY','AYUBUVFUCDQ'),('GOENKA101',' TINA GOENKA','JVSAYVTCT5JB'),('HOPE.KARTIK','KARTIK GUPTA','woof'),('ksdfg','Kshitish','miura'),('KULKARNI23','MAHESH KULKARNI','CSTY1472GC'),('MAHESH123','MAHESH BHATT','XRTBIH@35BY'),('mishti','ANSHUL BRIJWASI','MISHTI101'),('MUSKAAN',' MUSKAN AHUJA','OM235DDGCB');
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

-- Dump completed on 2019-09-20  0:43:36
