/*!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.6.18-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: juditon
-- ------------------------------------------------------
-- Server version	10.6.18-MariaDB-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounttransactions`
--

DROP TABLE IF EXISTS `accounttransactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accounttransactions` (
  `transaction id` int(11) NOT NULL AUTO_INCREMENT,
  `account` int(11) DEFAULT NULL,
  `activity` int(11) DEFAULT NULL,
  `cheque #` varchar(500) DEFAULT NULL,
  `credit` double DEFAULT NULL,
  `date` date DEFAULT NULL,
  `debit` double DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `payee/payer` varchar(500) DEFAULT NULL,
  `ref #` varchar(500) DEFAULT NULL,
  `status` varchar(500) DEFAULT NULL,
  `bank` int(11) NOT NULL,
  `farm` int(11) DEFAULT NULL,
  PRIMARY KEY (`transaction id`),
  KEY `FKb1rhmie081gh2pao6ciqadtea` (`bank`),
  KEY `FKkr8bi48ot3bfambndl62t45l` (`farm`),
  CONSTRAINT `FKb1rhmie081gh2pao6ciqadtea` FOREIGN KEY (`bank`) REFERENCES `bankaccounts` (`acc id`) ON DELETE CASCADE,
  CONSTRAINT `FKkr8bi48ot3bfambndl62t45l` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounttransactions`
--

LOCK TABLES `accounttransactions` WRITE;
/*!40000 ALTER TABLE `accounttransactions` DISABLE KEYS */;
INSERT INTO `accounttransactions` VALUES (72,20,0,'SH654E1HLN',80,'2024-08-06',0,'ROADTRIP #1 - GITAU','DANIEL GITAU','RCT2','Approved',5,2),(73,20,0,'SH749JJGOM',160,'2024-08-07',0,'ROADTRIP #1 - RICKIEY & KIKI','ERICK BABES','RCT3','Approved',5,2),(74,24,0,'',0,'2024-02-08',11700,'RICE SEEDS','AGROVET','PV1','Approved',1,1),(75,25,0,'',0,'2024-02-08',1000,'NURSERY PREPARATION','FARMERS','PV2','Approved',1,1),(76,25,0,'',0,'2024-02-09',1000,'NURSERY PREPARATION','FARMERS','PV3','Approved',1,1),(77,25,0,'',0,'2024-02-13',1000,'NURSERY PREPARATION','FARMERS','PV4','Approved',1,1),(78,25,0,'',0,'2024-02-14',1000,'NURSERY PREPARATION','FARMERS','PV5','Approved',1,1),(79,18,0,'',0,'2024-02-14',2000,'AQUISITION OF DIESEL','PETROL STATION','PV6','Approved',1,1),(80,18,0,'',0,'2024-02-15',2000,'AQUISITION OF PETROL','PETROL STATION','PV7','Approved',1,1),(81,26,0,'',0,'2024-03-13',28500,'PLOUGHING(ROTAVATOR) 6K/ACRE','ROTAVATOR','PV8','Approved',1,1),(82,26,0,'',0,'2024-03-13',3600,'LEVELLING','LEVELLERS','PV9','Approved',1,1),(83,27,0,'',0,'2024-03-13',14495,'DAY 1 PLANTING (INCLUSIVE OF ALL LABOUR)','FARMERS','PV10','Approved',1,1),(84,27,0,'',0,'2024-03-14',11090,'DAY 2 PLANTING','FARMERS','PV11','Approved',1,1),(85,27,0,'',0,'2024-03-20',400,'GAP FILLING','FARMERS','PV12','Approved',1,1),(86,5,0,'',0,'2024-03-20',22880,'FERTILIZER ACQUISITION AND APPLICATION','AGROVET','PV13','Approved',1,1),(87,9,0,'',0,'2024-03-31',4950,'WEEDING (27, 28, 29, 30)','FARMERS','PV14','Approved',1,1),(88,5,0,'',0,'2024-03-29',9550,'FERTILIZER AQUIZITION(27TH) & APPLICATION(29TH)','AGROVET','PV15','Approved',1,1),(89,28,0,'SHM7XCFB3N',120,'2024-08-22',0,'SPOTIFY MCHANGO','DANIEL GITAU','RCT4','Approved',5,2),(90,28,0,'SHM2XE01KO',120,'2024-08-22',0,'SPOTIFY','ERICK WANJOHI','RCT5','Approved',5,2),(91,28,0,'SHM2XE01KO',0,'2024-08-22',495.77,'SPOTIFY FAMILY SUBSCRIPTION','SPOTIFY','PV1','Approved',5,2);
/*!40000 ALTER TABLE `accounttransactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activities`
--

DROP TABLE IF EXISTS `activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activities` (
  `account id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(500) NOT NULL,
  `accountgroup` int(11) NOT NULL,
  `farm` int(11) DEFAULT NULL,
  PRIMARY KEY (`account id`),
  KEY `FK5t8aitra2guhj6oyif7s6tta3` (`accountgroup`),
  KEY `FK1alu444atyfh69g9092e6ux4u` (`farm`),
  CONSTRAINT `FK1alu444atyfh69g9092e6ux4u` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK5t8aitra2guhj6oyif7s6tta3` FOREIGN KEY (`accountgroup`) REFERENCES `activitygroups` (`group id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activities`
--

LOCK TABLES `activities` WRITE;
/*!40000 ALTER TABLE `activities` DISABLE KEYS */;
INSERT INTO `activities` VALUES (1,'PIG SALES',1,1),(2,'LOAN IN',5,1),(3,'ASSET ACQUISITION',6,1),(4,'PIG FEEDS',2,1),(5,'RICE FERTILIZER',4,1),(6,'SAVINGS',6,1),(7,'PIG DRUGS/MEDICATION',2,1),(8,'RICE SALES',3,1),(9,'RICE WEEDING',4,1),(10,'KURIRA',4,1),(11,'BARRIER MAINTENANCE',4,1),(12,'RICE PESTICIDES/HERBICIDES',4,1),(13,'LOAN OUT',6,1),(14,'RICE TRANSPORT',4,1),(15,'MILLING',4,1),(16,'WITHDRAW',7,1),(17,'DEPOSIT',7,1),(18,'FUEL',6,1),(19,'FEEDS TRANSPORT',2,1),(20,'MCHANGO',8,2),(21,'RAHA',9,2),(22,'INVESTMENTS',9,2),(23,'SAVINGS',9,2),(24,'RICE SEEDS',4,1),(25,'NURSERY PREPARATION',4,1),(26,'PLOUGHING',4,1),(27,'RICE PLANTING',4,1),(28,'SPOTIFY',9,2);
/*!40000 ALTER TABLE `activities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activitygroups`
--

DROP TABLE IF EXISTS `activitygroups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activitygroups` (
  `group id` int(11) NOT NULL AUTO_INCREMENT,
  `group name` varchar(500) NOT NULL,
  `farm` int(11) DEFAULT NULL,
  PRIMARY KEY (`group id`),
  KEY `FKr5bxkdbrnbdrr62cq8cdges0v` (`farm`),
  CONSTRAINT `FKr5bxkdbrnbdrr62cq8cdges0v` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitygroups`
--

LOCK TABLES `activitygroups` WRITE;
/*!40000 ALTER TABLE `activitygroups` DISABLE KEYS */;
INSERT INTO `activitygroups` VALUES (1,'PIGS INCOME',1),(2,'PIGS EXPENSES',1),(3,'RICE INCOME',1),(4,'RICE EXPENSES',1),(5,'GENERAL INCOME',1),(6,'GENERAL EXPENSES',1),(7,'OTHERS',1),(8,'INCOME',2),(9,'EXPENSES',2);
/*!40000 ALTER TABLE `activitygroups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `allowances`
--

DROP TABLE IF EXISTS `allowances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `allowances` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `allowance` int(11) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `pay no` int(11) NOT NULL,
  `payroll` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `allowances`
--

LOCK TABLES `allowances` WRITE;
/*!40000 ALTER TABLE `allowances` DISABLE KEYS */;
/*!40000 ALTER TABLE `allowances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset_depreciation`
--

DROP TABLE IF EXISTS `asset_depreciation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asset_depreciation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `asset_id` int(11) NOT NULL,
  `date` date DEFAULT NULL,
  `depreciated_on` datetime(6) DEFAULT NULL,
  `farm` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `asset_depreciation_assets_id_fk` (`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_depreciation`
--

LOCK TABLES `asset_depreciation` WRITE;
/*!40000 ALTER TABLE `asset_depreciation` DISABLE KEYS */;
/*!40000 ALTER TABLE `asset_depreciation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assets`
--

DROP TABLE IF EXISTS `assets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `asset_life` varchar(50) DEFAULT NULL,
  `balance_as_at` date DEFAULT NULL,
  `depreciation` varchar(200) NOT NULL,
  `description` varchar(200) NOT NULL,
  `location` varchar(200) NOT NULL,
  `name` varchar(200) NOT NULL,
  `opening_balance` double NOT NULL,
  `period` varchar(200) NOT NULL,
  `rate` double NOT NULL,
  `serial` varchar(200) NOT NULL,
  `status` varchar(200) NOT NULL,
  `type` varchar(200) NOT NULL,
  `used_by` varchar(200) NOT NULL,
  `farm` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm8jdguplluotadgdpafu0boa4` (`farm`),
  CONSTRAINT `FKm8jdguplluotadgdpafu0boa4` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assets`
--

LOCK TABLES `assets` WRITE;
/*!40000 ALTER TABLE `assets` DISABLE KEYS */;
/*!40000 ALTER TABLE `assets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authority`
--

DROP TABLE IF EXISTS `authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authority` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authority` varchar(500) NOT NULL,
  `name` varchar(50) NOT NULL,
  `status` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authority`
--

LOCK TABLES `authority` WRITE;
/*!40000 ALTER TABLE `authority` DISABLE KEYS */;
/*!40000 ALTER TABLE `authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bankaccounts`
--

DROP TABLE IF EXISTS `bankaccounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bankaccounts` (
  `acc id` int(11) NOT NULL AUTO_INCREMENT,
  `account #` varchar(50) NOT NULL,
  `account name` varchar(50) NOT NULL,
  `bank name` varchar(50) NOT NULL,
  `bankcode` varchar(50) NOT NULL,
  `branch` varchar(50) NOT NULL,
  `opening balance` double NOT NULL,
  `swiftcode` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `farm` int(11) DEFAULT NULL,
  PRIMARY KEY (`acc id`),
  KEY `FK9284k2880ex5ewkvncus7fn92` (`farm`),
  CONSTRAINT `FK9284k2880ex5ewkvncus7fn92` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bankaccounts`
--

LOCK TABLES `bankaccounts` WRITE;
/*!40000 ALTER TABLE `bankaccounts` DISABLE KEYS */;
INSERT INTO `bankaccounts` VALUES (1,'0759065744','ERICK MURIITHI WANJOHI MPESA','Mpesa','0001','MOBILE',0,'','Bank',1),(2,'00000000001','PETTY CASH','CASH','','MOBILE',0,'','Cash',1),(3,'0721660482','JUDY WAINOI MPESA','MPESA','','',0,'','Bank',1),(4,'0710443317','PETER MAINA MPESA','MPESA','','',0,'','Bank',1),(5,'759065744','GROUP MPESA','MPESA','','',0,'','Bank',2);
/*!40000 ALTER TABLE `bankaccounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `budgetactivities`
--

DROP TABLE IF EXISTS `budgetactivities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `budgetactivities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` int(11) NOT NULL,
  `alloc to men` double DEFAULT NULL,
  `alloc to women` double DEFAULT NULL,
  `fras criteria` varchar(500) DEFAULT NULL,
  `no_ of men` int(11) DEFAULT NULL,
  `no_ of women` int(11) DEFAULT NULL,
  `scc contribution` double DEFAULT NULL,
  `output` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnot2xauid37ves4pmcv6bhnyo` (`output`),
  CONSTRAINT `FKnot2xauid37ves4pmcv6bhnyo` FOREIGN KEY (`output`) REFERENCES `outputs` (`output id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `budgetactivities`
--

LOCK TABLES `budgetactivities` WRITE;
/*!40000 ALTER TABLE `budgetactivities` DISABLE KEYS */;
/*!40000 ALTER TABLE `budgetactivities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `budgets`
--

DROP TABLE IF EXISTS `budgets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `budgets` (
  `budget id` int(11) NOT NULL,
  `budget currency` varchar(500) DEFAULT NULL,
  `budget period` varchar(500) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `exchange rate` double DEFAULT NULL,
  `programme` varchar(500) DEFAULT NULL,
  `project` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`budget id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `budgets`
--

LOCK TABLES `budgets` WRITE;
/*!40000 ALTER TABLE `budgets` DISABLE KEYS */;
/*!40000 ALTER TABLE `budgets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `closing_balance`
--

DROP TABLE IF EXISTS `closing_balance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `closing_balance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `balance` float NOT NULL,
  `date` date NOT NULL,
  `farm` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkyqfp78djw7bn6jn0td5xnlbh` (`farm`),
  CONSTRAINT `FKkyqfp78djw7bn6jn0td5xnlbh` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `closing_balance`
--

LOCK TABLES `closing_balance` WRITE;
/*!40000 ALTER TABLE `closing_balance` DISABLE KEYS */;
INSERT INTO `closing_balance` VALUES (37,0,'2024-01-01',2),(38,0,'2024-02-01',2),(39,0,'2024-03-01',2),(40,0,'2024-04-01',2),(41,0,'2024-05-01',2),(42,0,'2024-06-01',2),(43,0,'2024-07-01',2),(44,240,'2024-08-01',2),(45,240,'2024-09-01',2),(46,240,'2024-10-01',2),(47,240,'2024-11-01',2),(48,240,'2024-12-01',2),(49,0,'2024-01-01',1),(50,-19700,'2024-02-01',1),(51,-115165,'2024-03-01',1),(52,-115165,'2024-04-01',1),(53,-115165,'2024-05-01',1),(54,-115165,'2024-06-01',1),(55,-115165,'2024-07-01',1),(56,-115165,'2024-08-01',1),(57,-115165,'2024-09-01',1),(58,-115165,'2024-10-01',1),(59,-115165,'2024-11-01',1),(60,-115165,'2024-12-01',1);
/*!40000 ALTER TABLE `closing_balance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `companyofficials`
--

DROP TABLE IF EXISTS `companyofficials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `companyofficials` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountant` bigint(20) DEFAULT NULL,
  `farm` int(11) DEFAULT NULL,
  `second_signatory` bigint(20) DEFAULT NULL,
  `senior_pastor` bigint(20) DEFAULT NULL,
  `treasurer` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfxew5bvbbpqqjvf45dl8gvafx` (`accountant`),
  KEY `FKmlrje3tby4f786jq9b4dv08r3` (`farm`),
  KEY `FKdn552mbvcgsrum3mf1anft793` (`second_signatory`),
  KEY `FK6idx5njeefld1ygdnw0v0gsa0` (`senior_pastor`),
  KEY `FKj964kapjy0c77jnf4ca1uyrf` (`treasurer`),
  CONSTRAINT `FK6idx5njeefld1ygdnw0v0gsa0` FOREIGN KEY (`senior_pastor`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKdn552mbvcgsrum3mf1anft793` FOREIGN KEY (`second_signatory`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKfxew5bvbbpqqjvf45dl8gvafx` FOREIGN KEY (`accountant`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKj964kapjy0c77jnf4ca1uyrf` FOREIGN KEY (`treasurer`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKmlrje3tby4f786jq9b4dv08r3` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companyofficials`
--

LOCK TABLES `companyofficials` WRITE;
/*!40000 ALTER TABLE `companyofficials` DISABLE KEYS */;
INSERT INTO `companyofficials` VALUES (1,1,1,1,1,1),(2,NULL,2,NULL,NULL,NULL);
/*!40000 ALTER TABLE `companyofficials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deductions`
--

DROP TABLE IF EXISTS `deductions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deductions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deduction` varchar(300) DEFAULT NULL,
  `calculationtype` double NOT NULL DEFAULT 0,
  `costperunit` double NOT NULL DEFAULT 0,
  `type` double NOT NULL DEFAULT 0,
  `visible` varchar(10) DEFAULT NULL,
  `farm` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKclfeptrp7v688v8e8ixr4kuk9` (`farm`),
  CONSTRAINT `FKclfeptrp7v688v8e8ixr4kuk9` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deductions`
--

LOCK TABLES `deductions` WRITE;
/*!40000 ALTER TABLE `deductions` DISABLE KEYS */;
INSERT INTO `deductions` VALUES (1,'LOAN',0,0,0,'Visible',1);
/*!40000 ALTER TABLE `deductions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deductionsandearningsexpirydat`
--

DROP TABLE IF EXISTS `deductionsandearningsexpirydat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deductionsandearningsexpirydat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `enddate` date DEFAULT NULL,
  `item` int(11) NOT NULL,
  `reccurrent` int(11) DEFAULT NULL,
  `startdate` date DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `type` varchar(500) NOT NULL,
  `payno` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6b196x0q9v1wvuy1cf5ly0pss` (`payno`),
  CONSTRAINT `FK6b196x0q9v1wvuy1cf5ly0pss` FOREIGN KEY (`payno`) REFERENCES `employees` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deductionsandearningsexpirydat`
--

LOCK TABLES `deductionsandearningsexpirydat` WRITE;
/*!40000 ALTER TABLE `deductionsandearningsexpirydat` DISABLE KEYS */;
/*!40000 ALTER TABLE `deductionsandearningsexpirydat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `earnings`
--

DROP TABLE IF EXISTS `earnings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `earnings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `earning` varchar(300) DEFAULT NULL,
  `farm` int(11) NOT NULL,
  `costperunit` double NOT NULL DEFAULT 0,
  `type` double NOT NULL DEFAULT 0,
  `visible` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `earnings_farm_id_fk` (`farm`),
  CONSTRAINT `FKa17043osj4pbit1wna83sf25v` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE,
  CONSTRAINT `earnings_farm_id_fk` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `earnings`
--

LOCK TABLES `earnings` WRITE;
/*!40000 ALTER TABLE `earnings` DISABLE KEYS */;
INSERT INTO `earnings` VALUES (1,'Basic income',1,0,0,'Visible');
/*!40000 ALTER TABLE `earnings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employees` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accholdername` varchar(500) DEFAULT NULL,
  `accountnumber` varchar(500) DEFAULT NULL,
  `accounttype` varchar(500) DEFAULT NULL,
  `bankname` varchar(500) DEFAULT NULL,
  `branchname` varchar(500) DEFAULT NULL,
  `city` varchar(500) DEFAULT NULL,
  `dob` varchar(500) DEFAULT NULL,
  `email` varchar(500) DEFAULT NULL,
  `employeetype` varchar(50) DEFAULT NULL,
  `enddate` date DEFAULT NULL,
  `fname` varchar(50) DEFAULT NULL,
  `gender` varchar(50) DEFAULT NULL,
  `helb` int(11) DEFAULT NULL,
  `housed` int(11) DEFAULT NULL,
  `housinglev` int(11) DEFAULT NULL,
  `idnu` varchar(1000) DEFAULT NULL,
  `kra pin` varchar(50) DEFAULT NULL,
  `linktostaff` varchar(50) DEFAULT NULL,
  `linktoteacher` varchar(50) DEFAULT NULL,
  `nextofkin` varchar(500) DEFAULT NULL,
  `nextofkinnumber` varchar(500) DEFAULT NULL,
  `nhif` int(11) DEFAULT NULL,
  `nhif number` varchar(50) DEFAULT NULL,
  `nssf` int(11) DEFAULT NULL,
  `nssf number` varchar(50) DEFAULT NULL,
  `occupation` varchar(500) DEFAULT NULL,
  `pay cycle` varchar(500) DEFAULT NULL,
  `paye` int(11) DEFAULT NULL,
  `paymeth` varchar(500) DEFAULT NULL,
  `payno` varchar(50) DEFAULT NULL,
  `phone` varchar(500) DEFAULT NULL,
  `postaladdress` varchar(500) DEFAULT NULL,
  `postalcode` varchar(500) DEFAULT NULL,
  `raddress` varchar(500) DEFAULT NULL,
  `sacco` varchar(50) DEFAULT NULL,
  `simage` varchar(500) DEFAULT NULL,
  `sname` varchar(1000) DEFAULT NULL,
  `startdate` date DEFAULT NULL,
  `status` varchar(500) DEFAULT NULL,
  `streetname` varchar(500) DEFAULT NULL,
  `streetnum` varchar(500) DEFAULT NULL,
  `surname` varchar(50) DEFAULT NULL,
  `welfare` varchar(50) DEFAULT NULL,
  `farm` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2fvkt667gwfa25j94kdtaex05` (`farm`),
  CONSTRAINT `FK2fvkt667gwfa25j94kdtaex05` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'ERICK WANJOHI','0759065744',NULL,'MPESA','rtrtthf','Kerugoya','2002-03-11','wanjohierick07@gmail.com','Other',NULL,'Erick','Male',NULL,0,1,'39422264','4657674545',NULL,NULL,'ghgjhhgjgh','gfhghg565676',1,'334232',NULL,'35667564','Dev',NULL,1,'Mobile Money','0001','+254759065744',NULL,NULL,'2',NULL,NULL,'Muriithi',NULL,'Active','- 01000 THIKA','214','Wanjohi',NULL,1),(2,'PETER MAINA','0710443317',NULL,'MPESA','rtrtthf','Kerugoya','2000-01-01','maishpeter@gmail.com','Support staff',NULL,'PETER','Male',NULL,0,1,'56755567','4657674545',NULL,NULL,'ERICK WANJOHI','0759065744',1,'4345656767',1,'245456546533423','Dev',NULL,1,'Mobile Money','0002','0710443317',NULL,NULL,'5',NULL,NULL,'MAINA',NULL,'Active','01000 THIKA','214','MURIITHI',NULL,1);
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `farm`
--

DROP TABLE IF EXISTS `farm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `farm` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(500) NOT NULL,
  `country` varchar(500) NOT NULL,
  `email` varchar(500) NOT NULL,
  `logo` varchar(500) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `nhifoption` int(11) DEFAULT NULL,
  `nssfoption` int(11) DEFAULT NULL,
  `payeeoption` int(11) DEFAULT NULL,
  `phone` varchar(500) NOT NULL,
  `region` varchar(500) NOT NULL,
  `sms_account` varchar(300) DEFAULT NULL,
  `smsid` varchar(300) DEFAULT NULL,
  `smskey` varchar(300) DEFAULT NULL,
  `smsusername` varchar(300) DEFAULT NULL,
  `upload_path` varchar(500) NOT NULL,
  `usebiometricsforpayroll` int(11) DEFAULT NULL,
  `zip` varchar(500) NOT NULL,
  `employercode` varchar(500) DEFAULT NULL,
  `TaxRelief` varchar(500) DEFAULT NULL,
  `tax_relief` varchar(500) DEFAULT NULL,
  `cost_per_sms` double DEFAULT NULL,
  `sms_balance` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farm`
--

LOCK TABLES `farm` WRITE;
/*!40000 ALTER TABLE `farm` DISABLE KEYS */;
INSERT INTO `farm` VALUES (1,'2','Kenya','juditon@gmail.com','static/fgck/MemberImages/full-gospel.jpg','JUDITON FARM',NULL,NULL,NULL,'700258330','KIRINYAGA','HostPinacle','WALGOTECHSC','nuC1tAk3','kenmutwiri','juditon',NULL,'10200','','0',NULL,1,6),(2,'2','Kenya','wanjohierick07@gmail.com','static/fgck/MemberImages/kujibamba.jpeg','KUJIBAMBA SIO KUKATA TU',NULL,NULL,NULL,'759065744','KIAMBU','HostPinacle','WALGOTECHSC','nuC1tAk3','kenmutwiri','focus',NULL,'10200','','0',NULL,1,5);
/*!40000 ALTER TABLE `farm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `housing`
--

DROP TABLE IF EXISTS `housing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `housing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `farm` int(11) NOT NULL DEFAULT 0,
  `low` int(11) NOT NULL DEFAULT 0,
  `top` int(11) NOT NULL DEFAULT 0,
  `ratio` double NOT NULL DEFAULT 0,
  `amount` double NOT NULL DEFAULT 0,
  `nssfoptionperband` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `housing`
--

LOCK TABLES `housing` WRITE;
/*!40000 ALTER TABLE `housing` DISABLE KEYS */;
/*!40000 ALTER TABLE `housing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issued_items`
--

DROP TABLE IF EXISTS `issued_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issued_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comments` varchar(500) DEFAULT NULL,
  `farm` int(11) NOT NULL,
  `individual_category` varchar(100) NOT NULL,
  `individual_id` varchar(100) NOT NULL,
  `issued_by` varchar(50) DEFAULT NULL,
  `issued_on` datetime(6) NOT NULL,
  `item_id` int(11) NOT NULL,
  `quantity` double NOT NULL,
  `return_date` date DEFAULT NULL,
  `returned_by` varchar(50) DEFAULT NULL,
  `returned_on` date DEFAULT NULL,
  `status` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_issued_items_items` (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issued_items`
--

LOCK TABLES `issued_items` WRITE;
/*!40000 ALTER TABLE `issued_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `issued_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_adjustment`
--

DROP TABLE IF EXISTS `item_adjustment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_adjustment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(100) NOT NULL,
  `adjusted_by` varchar(100) NOT NULL,
  `adjusted_value` double NOT NULL,
  `adjustment_type` varchar(50) NOT NULL,
  `date` datetime(6) NOT NULL,
  `description` varchar(500) NOT NULL,
  `farm` int(11) NOT NULL,
  `initial_value` double NOT NULL,
  `item_id` int(11) NOT NULL,
  `reason` varchar(500) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_item_adjustment_items` (`item_id`),
  CONSTRAINT `FKt3595j6v5myrtr8tkx7ib740v` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_adjustment`
--

LOCK TABLES `item_adjustment` WRITE;
/*!40000 ALTER TABLE `item_adjustment` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_adjustment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_group`
--

DROP TABLE IF EXISTS `item_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `farm` int(11) NOT NULL,
  `group_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_name` (`group_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_group`
--

LOCK TABLES `item_group` WRITE;
/*!40000 ALTER TABLE `item_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_stock`
--

DROP TABLE IF EXISTS `item_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` int(11) NOT NULL,
  `description` varchar(500) NOT NULL,
  `farm` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `reg_date` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_stock`
--

LOCK TABLES `item_stock` WRITE;
/*!40000 ALTER TABLE `item_stock` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime(6) NOT NULL,
  `farm` int(11) DEFAULT NULL,
  `image` varchar(500) DEFAULT NULL,
  `item_category` varchar(50) NOT NULL,
  `item_group` varchar(50) DEFAULT NULL,
  `item_name` varchar(100) NOT NULL,
  `item_price` double NOT NULL,
  `preferred_vendor` int(11) DEFAULT NULL,
  `reoder_level` int(11) NOT NULL,
  `returnable` int(11) DEFAULT NULL,
  `uom` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_items_item_group` (`item_group`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES (1,'2024-04-17 20:41:06.000000',1,'','consumable','OTHERS','SOW & WEANER',2800,0,2,0,'Bags');
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kplcrates`
--

DROP TABLE IF EXISTS `kplcrates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kplcrates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `farm` int(11) DEFAULT 0,
  `rate` double DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kplcrates`
--

LOCK TABLES `kplcrates` WRITE;
/*!40000 ALTER TABLE `kplcrates` DISABLE KEYS */;
/*!40000 ALTER TABLE `kplcrates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `krarates`
--

DROP TABLE IF EXISTS `krarates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `krarates` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `farm` int(11) DEFAULT 0,
  `low` int(11) NOT NULL DEFAULT 0,
  `top` int(11) NOT NULL DEFAULT 0,
  `ratio` double NOT NULL DEFAULT 0,
  `amount` double NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `farm_low_top` (`farm`,`low`,`top`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `krarates`
--

LOCK TABLES `krarates` WRITE;
/*!40000 ALTER TABLE `krarates` DISABLE KEYS */;
INSERT INTO `krarates` VALUES (1,1,0,24000,0,0),(2,1,24001,30000,10,0),(3,1,30001,100000,30,0);
/*!40000 ALTER TABLE `krarates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave`
--

DROP TABLE IF EXISTS `leave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `leave` (
  `id` int(11) NOT NULL,
  `category` varchar(500) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `from` date DEFAULT NULL,
  `payno` varchar(50) NOT NULL,
  `status` varchar(500) DEFAULT NULL,
  `to` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave`
--

LOCK TABLES `leave` WRITE;
/*!40000 ALTER TABLE `leave` DISABLE KEYS */;
/*!40000 ALTER TABLE `leave` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `monthly pays`
--

DROP TABLE IF EXISTS `monthly pays`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `monthly pays` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gross salary` double NOT NULL,
  `helb` double NOT NULL,
  `house` varchar(50) DEFAULT NULL,
  `housing` double NOT NULL,
  `kra tax` double NOT NULL,
  `month` int(11) NOT NULL,
  `net salary` double NOT NULL,
  `nhif` double NOT NULL,
  `nssf` double NOT NULL,
  `paid on` datetime(6) NOT NULL,
  `payno` int(11) NOT NULL,
  `status` varchar(50) DEFAULT NULL,
  `taxable` double NOT NULL,
  `year` int(11) NOT NULL,
  `payroll` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl4xcw37wknpoymxg8ywl38s28` (`payroll`),
  CONSTRAINT `FKl4xcw37wknpoymxg8ywl38s28` FOREIGN KEY (`payroll`) REFERENCES `payrolls` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `monthly pays`
--

LOCK TABLES `monthly pays` WRITE;
/*!40000 ALTER TABLE `monthly pays` DISABLE KEYS */;
/*!40000 ALTER TABLE `monthly pays` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhif`
--

DROP TABLE IF EXISTS `nhif`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nhif` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `farm` int(11) NOT NULL DEFAULT 0,
  `low` int(11) NOT NULL DEFAULT 0,
  `top` int(11) NOT NULL DEFAULT 0,
  `ratio` double NOT NULL DEFAULT 0,
  `amount` double NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `farm_low_top` (`farm`,`low`,`top`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhif`
--

LOCK TABLES `nhif` WRITE;
/*!40000 ALTER TABLE `nhif` DISABLE KEYS */;
INSERT INTO `nhif` VALUES (1,1,10000,100000,0,1000);
/*!40000 ALTER TABLE `nhif` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nssf`
--

DROP TABLE IF EXISTS `nssf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nssf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `farm` int(11) NOT NULL DEFAULT 0,
  `low` int(11) NOT NULL DEFAULT 0,
  `top` int(11) NOT NULL DEFAULT 0,
  `ratio` double NOT NULL DEFAULT 0,
  `amount` double NOT NULL DEFAULT 0,
  `nssfoptionperband` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `farm_low_top` (`farm`,`low`,`top`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nssf`
--

LOCK TABLES `nssf` WRITE;
/*!40000 ALTER TABLE `nssf` DISABLE KEYS */;
/*!40000 ALTER TABLE `nssf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `other deductions`
--

DROP TABLE IF EXISTS `other deductions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `other deductions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `deduction` int(11) NOT NULL,
  `payno` int(11) NOT NULL,
  `payroll` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `other deductions`
--

LOCK TABLES `other deductions` WRITE;
/*!40000 ALTER TABLE `other deductions` DISABLE KEYS */;
/*!40000 ALTER TABLE `other deductions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `outcomes`
--

DROP TABLE IF EXISTS `outcomes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `outcomes` (
  `outcome id` int(11) NOT NULL,
  `outcome #` varchar(500) NOT NULL,
  `outcome` varchar(500) NOT NULL,
  `budget` int(11) NOT NULL,
  PRIMARY KEY (`outcome id`),
  KEY `FKgsbxd08klpp0gc7ysuy25fvaw` (`budget`),
  CONSTRAINT `FKgsbxd08klpp0gc7ysuy25fvaw` FOREIGN KEY (`budget`) REFERENCES `budgets` (`budget id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `outcomes`
--

LOCK TABLES `outcomes` WRITE;
/*!40000 ALTER TABLE `outcomes` DISABLE KEYS */;
/*!40000 ALTER TABLE `outcomes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `outputs`
--

DROP TABLE IF EXISTS `outputs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `outputs` (
  `output id` int(11) NOT NULL,
  `output #` varchar(500) NOT NULL,
  `output` varchar(500) NOT NULL,
  `outcome` int(11) NOT NULL,
  PRIMARY KEY (`output id`),
  KEY `FKngglt07wd0sigebshg7n689cm` (`outcome`),
  CONSTRAINT `FKngglt07wd0sigebshg7n689cm` FOREIGN KEY (`outcome`) REFERENCES `outcomes` (`outcome id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `outputs`
--

LOCK TABLES `outputs` WRITE;
/*!40000 ALTER TABLE `outputs` DISABLE KEYS */;
/*!40000 ALTER TABLE `outputs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_request`
--

DROP TABLE IF EXISTS `payment_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `approved_by` varchar(200) DEFAULT NULL,
  `approved_on` datetime(6) DEFAULT NULL,
  `created_by` varchar(200) NOT NULL,
  `created_on` datetime(6) DEFAULT NULL,
  `details` varchar(500) NOT NULL,
  `disbursed_by` varchar(200) DEFAULT NULL,
  `disbursed_on` datetime(6) DEFAULT NULL,
  `farm` int(11) NOT NULL,
  `pv` varchar(50) DEFAULT NULL,
  `reviewed_by` varchar(200) DEFAULT NULL,
  `reviewed_on` datetime(6) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_request`
--

LOCK TABLES `payment_request` WRITE;
/*!40000 ALTER TABLE `payment_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paymentvoucherdetails`
--

DROP TABLE IF EXISTS `paymentvoucherdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paymentvoucherdetails` (
  `detailid` int(11) NOT NULL AUTO_INCREMENT,
  `particulars` varchar(1000) NOT NULL,
  `qty` double NOT NULL,
  `rate` double NOT NULL,
  `farm` int(11) DEFAULT NULL,
  `pv#` int(11) NOT NULL,
  PRIMARY KEY (`detailid`),
  KEY `FK1uqhx2lmsmgaph4i9omoxq7v6` (`farm`),
  KEY `FK3gkxnetsjkh8ui5k6ey82ktj1` (`pv#`),
  CONSTRAINT `FK1uqhx2lmsmgaph4i9omoxq7v6` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK3gkxnetsjkh8ui5k6ey82ktj1` FOREIGN KEY (`pv#`) REFERENCES `paymentvouchers` (`pv id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentvoucherdetails`
--

LOCK TABLES `paymentvoucherdetails` WRITE;
/*!40000 ALTER TABLE `paymentvoucherdetails` DISABLE KEYS */;
INSERT INTO `paymentvoucherdetails` VALUES (62,'SEEDS',90,130,1,28),(63,'PREP',1,1000,1,29),(64,'PREP',1,1000,1,30),(65,'PREP',1,1000,1,31),(66,'PREP',1,1000,1,32),(67,'DIESEL',1,2000,1,33),(68,'PETROL',1,2000,1,34),(71,'ACRE',3,1200,1,36),(72,'FEET',118,70,1,37),(73,'SOMETHING ELSE',1,6200,1,37),(74,'ALL COSTS',1,11090,1,38),(75,'GAP FILLING',1,400,1,39),(76,'XARAMILLA POWER',1,18210,1,40),(77,'MOB',1,4370,1,40),(78,'FERTILIZER APPLICATION',1,300,1,40),(80,'AMMONIUM SULPHATE',1,9300,1,42),(81,'APPLICATION',1,250,1,42),(82,'WEEDING',1,4950,1,41),(83,'ACRE',3,6000,1,35),(84,'REPEAT',3,3500,1,35),(86,'SUBSCRIPTION',1,495.77,2,43);
/*!40000 ALTER TABLE `paymentvoucherdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paymentvouchers`
--

DROP TABLE IF EXISTS `paymentvouchers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paymentvouchers` (
  `pv id` int(11) NOT NULL AUTO_INCREMENT,
  `activity` int(11) NOT NULL,
  `date` date DEFAULT NULL,
  `details` varchar(5000) NOT NULL,
  `payee name` varchar(500) NOT NULL,
  `status` varchar(5000) NOT NULL,
  `voucher #` varchar(255) NOT NULL,
  `farm` int(11) DEFAULT NULL,
  PRIMARY KEY (`pv id`),
  KEY `FKbc6h2wcnap6jiogps2heaphkg` (`farm`),
  CONSTRAINT `FKbc6h2wcnap6jiogps2heaphkg` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentvouchers`
--

LOCK TABLES `paymentvouchers` WRITE;
/*!40000 ALTER TABLE `paymentvouchers` DISABLE KEYS */;
INSERT INTO `paymentvouchers` VALUES (28,24,'2024-02-08','RICE SEEDS','AGROVET','Approved','PV1',1),(29,25,'2024-02-08','NURSERY PREPARATION','FARMERS','Approved','PV2',1),(30,25,'2024-02-09','NURSERY PREPARATION','FARMERS','Approved','PV3',1),(31,25,'2024-02-13','NURSERY PREPARATION','FARMERS','Approved','PV4',1),(32,25,'2024-02-14','NURSERY PREPARATION','FARMERS','Approved','PV5',1),(33,18,'2024-02-14','AQUISITION OF DIESEL','PETROL STATION','Approved','PV6',1),(34,18,'2024-02-15','AQUISITION OF PETROL','PETROL STATION','Approved','PV7',1),(35,26,'2024-03-13','PLOUGHING(ROTAVATOR) 6K/ACRE','ROTAVATOR','Approved','PV8',1),(36,26,'2024-03-13','LEVELLING','LEVELLERS','Approved','PV9',1),(37,27,'2024-03-13','DAY 1 PLANTING (INCLUSIVE OF ALL LABOUR)','FARMERS','Approved','PV10',1),(38,27,'2024-03-14','DAY 2 PLANTING','FARMERS','Approved','PV11',1),(39,27,'2024-03-20','GAP FILLING','FARMERS','Approved','PV12',1),(40,5,'2024-03-20','FERTILIZER ACQUISITION AND APPLICATION','AGROVET','Approved','PV13',1),(41,9,'2024-03-31','WEEDING (27, 28, 29, 30)','FARMERS','Approved','PV14',1),(42,5,'2024-03-29','FERTILIZER AQUIZITION(27TH) & APPLICATION(29TH)','AGROVET','Approved','PV15',1),(43,28,'2024-08-22','SPOTIFY FAMILY SUBSCRIPTION','SPOTIFY','Approved','PV1',2);
/*!40000 ALTER TABLE `paymentvouchers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payrollpayments`
--

DROP TABLE IF EXISTS `payrollpayments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payrollpayments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `payroll` int(11) NOT NULL,
  `bank` varchar(500) NOT NULL DEFAULT '',
  `amount` double NOT NULL DEFAULT 0,
  `transnu` varchar(100) NOT NULL DEFAULT '',
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `payroll_bank` (`payroll`,`bank`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payrollpayments`
--

LOCK TABLES `payrollpayments` WRITE;
/*!40000 ALTER TABLE `payrollpayments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payrollpayments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payrolls`
--

DROP TABLE IF EXISTS `payrolls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payrolls` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `farm` int(11) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgt6couv7ohbessxm9vm3xo562` (`farm`),
  CONSTRAINT `FKgt6couv7ohbessxm9vm3xo562` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payrolls`
--

LOCK TABLES `payrolls` WRITE;
/*!40000 ALTER TABLE `payrolls` DISABLE KEYS */;
INSERT INTO `payrolls` VALUES (7,'2024-08-10',1,'Pending'),(8,'2024-07-10',1,'Processed');
/*!40000 ALTER TABLE `payrolls` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pending_transaction`
--

DROP TABLE IF EXISTS `pending_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pending_transaction` (
  `transaction id` int(11) NOT NULL AUTO_INCREMENT,
  `account` int(11) DEFAULT NULL,
  `activity` int(11) DEFAULT NULL,
  `cheque #` varchar(500) DEFAULT NULL,
  `credit` double DEFAULT NULL,
  `date` date DEFAULT NULL,
  `debit` double DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `payee/payer` varchar(500) DEFAULT NULL,
  `ref #` varchar(500) DEFAULT NULL,
  `status` varchar(500) DEFAULT NULL,
  `bank` int(11) NOT NULL,
  `farm` int(11) DEFAULT NULL,
  `voucher` int(11) DEFAULT NULL,
  PRIMARY KEY (`transaction id`),
  KEY `FK5u67x65dwrb4748fu8kco38ji` (`bank`),
  KEY `FK7ch7m5cc0u49lxebym2syc175` (`farm`),
  KEY `FK616pm72s88jd7vjlyngk337uq` (`voucher`),
  CONSTRAINT `FK5u67x65dwrb4748fu8kco38ji` FOREIGN KEY (`bank`) REFERENCES `bankaccounts` (`acc id`) ON DELETE CASCADE,
  CONSTRAINT `FK616pm72s88jd7vjlyngk337uq` FOREIGN KEY (`voucher`) REFERENCES `paymentvouchers` (`pv id`) ON DELETE CASCADE,
  CONSTRAINT `FK7ch7m5cc0u49lxebym2syc175` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pending_transaction`
--

LOCK TABLES `pending_transaction` WRITE;
/*!40000 ALTER TABLE `pending_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `pending_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order`
--

DROP TABLE IF EXISTS `purchase_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comments` varchar(500) NOT NULL,
  `created_by` varchar(100) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `expected_date` date DEFAULT NULL,
  `farm` int(11) NOT NULL,
  `requisition_id` int(11) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `vendor_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_purchase_order_requisition` (`requisition_id`),
  KEY `FK_purchase_order_vendors` (`vendor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order`
--

LOCK TABLES `purchase_order` WRITE;
/*!40000 ALTER TABLE `purchase_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order_items`
--

DROP TABLE IF EXISTS `purchase_order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_order_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) DEFAULT NULL,
  `item_price` double DEFAULT NULL,
  `item_quantity` int(11) DEFAULT NULL,
  `purchase_order_id` int(11) DEFAULT NULL,
  `farm` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlxje3kbtc9kgjwv31gk94kjkb` (`farm`),
  CONSTRAINT `FKlxje3kbtc9kgjwv31gk94kjkb` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order_items`
--

LOCK TABLES `purchase_order_items` WRITE;
/*!40000 ALTER TABLE `purchase_order_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order_receives`
--

DROP TABLE IF EXISTS `purchase_order_receives`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_order_receives` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comments` varchar(500) DEFAULT NULL,
  `purchase_order_item_id` int(11) DEFAULT NULL,
  `pv_no` varchar(100) DEFAULT NULL,
  `quantity_received` double DEFAULT NULL,
  `received_by` varchar(100) DEFAULT NULL,
  `received_on` datetime(6) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `farm` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4ge03et0gqntdesxvhu0l36oo` (`farm`),
  CONSTRAINT `FK4ge03et0gqntdesxvhu0l36oo` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order_receives`
--

LOCK TABLES `purchase_order_receives` WRITE;
/*!40000 ALTER TABLE `purchase_order_receives` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_order_receives` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchasesbills`
--

DROP TABLE IF EXISTS `purchasesbills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchasesbills` (
  `billid` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(300) NOT NULL,
  `amt` double NOT NULL,
  `billdate` varchar(300) DEFAULT NULL,
  `billno` varchar(300) DEFAULT NULL,
  `duedate` varchar(300) DEFAULT NULL,
  `indentify` varchar(300) NOT NULL,
  `item_code` int(11) DEFAULT NULL,
  `notes` varchar(300) NOT NULL,
  `qty` int(11) DEFAULT NULL,
  `suplier` int(11) DEFAULT NULL,
  `farm` int(11) DEFAULT NULL,
  PRIMARY KEY (`billid`),
  KEY `FKlp2pdv78vvohlb6u8vryk3sdr` (`farm`),
  CONSTRAINT `FKlp2pdv78vvohlb6u8vryk3sdr` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchasesbills`
--

LOCK TABLES `purchasesbills` WRITE;
/*!40000 ALTER TABLE `purchasesbills` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchasesbills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `readingbasedallowances`
--

DROP TABLE IF EXISTS `readingbasedallowances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `readingbasedallowances` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item` int(11) DEFAULT 0,
  `employeeid` int(11) DEFAULT 0,
  `month` int(11) DEFAULT 0,
  `year` int(11) DEFAULT 0,
  `readings` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `item_employeeid_month_year` (`item`,`employeeid`,`month`,`year`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `readingbasedallowances`
--

LOCK TABLES `readingbasedallowances` WRITE;
/*!40000 ALTER TABLE `readingbasedallowances` DISABLE KEYS */;
/*!40000 ALTER TABLE `readingbasedallowances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `readingbaseddeductions`
--

DROP TABLE IF EXISTS `readingbaseddeductions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `readingbaseddeductions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item` int(11) DEFAULT 0,
  `employeeid` int(11) DEFAULT 0,
  `month` int(11) DEFAULT 0,
  `year` int(11) DEFAULT 0,
  `readings` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `item_employeeid_month_year` (`item`,`employeeid`,`month`,`year`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `readingbaseddeductions`
--

LOCK TABLES `readingbaseddeductions` WRITE;
/*!40000 ALTER TABLE `readingbaseddeductions` DISABLE KEYS */;
/*!40000 ALTER TABLE `readingbaseddeductions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `references`
--

DROP TABLE IF EXISTS `references`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `references` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pv` int(11) DEFAULT NULL,
  `rct` int(11) DEFAULT NULL,
  `ref` int(11) DEFAULT NULL,
  `farm` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqokc46l7dbdr9joq0rcbla9yq` (`farm`),
  CONSTRAINT `FKqokc46l7dbdr9joq0rcbla9yq` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `references`
--

LOCK TABLES `references` WRITE;
/*!40000 ALTER TABLE `references` DISABLE KEYS */;
INSERT INTO `references` VALUES (1,15,0,0,1),(2,1,5,0,2);
/*!40000 ALTER TABLE `references` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requisition`
--

DROP TABLE IF EXISTS `requisition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `requisition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department` varchar(100) NOT NULL,
  `requested_by` varchar(100) NOT NULL,
  `requested_on` datetime(6) NOT NULL,
  `status` varchar(100) NOT NULL,
  `farm` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk46sm8wanv3oba21eao39ie5v` (`farm`),
  CONSTRAINT `FKk46sm8wanv3oba21eao39ie5v` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requisition`
--

LOCK TABLES `requisition` WRITE;
/*!40000 ALTER TABLE `requisition` DISABLE KEYS */;
/*!40000 ALTER TABLE `requisition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requisition_items`
--

DROP TABLE IF EXISTS `requisition_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `requisition_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `details` varchar(500) NOT NULL,
  `item_id` int(11) NOT NULL,
  `price` double NOT NULL,
  `quantity` int(11) NOT NULL,
  `removal_reason` varchar(500) NOT NULL,
  `requisition_id` int(11) NOT NULL,
  `status` varchar(50) NOT NULL,
  `farm` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrse08eivv2tkdd44m3k5psxst` (`farm`),
  CONSTRAINT `FKrse08eivv2tkdd44m3k5psxst` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requisition_items`
--

LOCK TABLES `requisition_items` WRITE;
/*!40000 ALTER TABLE `requisition_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `requisition_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` enum('ADMIN','FARM_MANAGER','SHAREHOLDER','STAFF') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'STAFF'),(2,'SHAREHOLDER'),(3,'ADMIN'),(4,'FARM_MANAGER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_history`
--

DROP TABLE IF EXISTS `sms_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date_sent` varchar(200) DEFAULT NULL,
  `delivered` int(11) DEFAULT NULL,
  `failed` int(11) DEFAULT NULL,
  `message` varchar(10000) NOT NULL,
  `phones` tinytext DEFAULT NULL,
  `recipients` varchar(500) NOT NULL,
  `sending_params` varchar(200) DEFAULT NULL,
  `sent` int(11) DEFAULT NULL,
  `sent_by` int(11) DEFAULT NULL,
  `sms_group` int(11) DEFAULT NULL,
  `sms_id` varchar(600) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `type` varchar(400) DEFAULT NULL,
  `uuid` varchar(600) DEFAULT NULL,
  `farm` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl4s242e3fcx0ep5uamf5pdn8i` (`farm`),
  CONSTRAINT `FKl4s242e3fcx0ep5uamf5pdn8i` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_history`
--

LOCK TABLES `sms_history` WRITE;
/*!40000 ALTER TABLE `sms_history` DISABLE KEYS */;
INSERT INTO `sms_history` VALUES (1,'Wed 17th Apr 2024 05:42 PM',NULL,NULL,'Hello','759065744','[ALL MEMBERS]',NULL,NULL,1,1,'M5TJ5SZt9u2VXk4','FAILED','RECEIPT','2645004457198372952',1),(2,'Wed 17th Apr 2024 05:42 PM',NULL,NULL,'Hello','759065744','[ALL MEMBERS]',NULL,NULL,1,1,'h6WOVlOEXSnGCpZ','FAILED','RECEIPT','7384334355687623505',1),(3,'Wed 17th Apr 2024 06:03 PM',NULL,NULL,'Hello','759065744','[ALL MEMBERS]',NULL,NULL,1,2,'VHe1uQa5Qo3lpso','FAILED','RECEIPT','6813485902074497496',1),(4,'Wed 17th Apr 2024 06:05 PM',NULL,NULL,'Hello','2540759065744','[ALL MEMBERS]',NULL,NULL,1,3,'vHWZInWO86vvLhW','FAILED','RECEIPT','673722070650472559',1),(5,'Wed 17th Apr 2024 06:05 PM',NULL,NULL,'Hello','2540710443317','[ALL MEMBERS]',NULL,NULL,1,4,'4oSvaDOI7XF1m1V','SUBMITTED','RECEIPT','2970685135995424196',1),(6,'Wed 17th Apr 2024 06:05 PM',NULL,NULL,'Hello','2540759065744','[ALL MEMBERS]',NULL,NULL,1,4,'sNgVLoxgxBTYxI2','SUBMITTED','RECEIPT','2970685135995424196',1),(7,'Wed 17th Apr 2024 06:11 PM',NULL,NULL,'Hello','254710443317','[ALL MEMBERS]',NULL,NULL,1,5,'3VCtz8buuEZdggQ','SUBMITTED','RECEIPT','3617544819628263710',1),(8,'Wed 17th Apr 2024 06:11 PM',NULL,NULL,'Hello','254759065744','[ALL MEMBERS]',NULL,NULL,1,5,'KZrDpMRbK9t7q6y','SUBMITTED','RECEIPT','3617544819628263710',1),(9,'Wed 17th Apr 2024 06:11 PM',NULL,NULL,'Hello','254710443317','[ALL MEMBERS]',NULL,NULL,1,5,'1yJmwYseeYfO6bJ','SUBMITTED','RECEIPT','8077920227087632114',1),(10,'Wed 17th Apr 2024 06:11 PM',NULL,NULL,'Hello','254759065744','[ALL MEMBERS]',NULL,NULL,1,5,'Ad6ChHgiEuX7fij','SUBMITTED','RECEIPT','8077920227087632114',1),(11,'Wed 17th Apr 2024 06:25 PM',NULL,NULL,'Received: \nfrom JUDITON FARM, \nDetails: DEVELOPMENT OF SOFTWARE , \nBank: 0759065744, \nRef: SGFERGH355DGH.\n\nTotal Amount: KES 0.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,6,'Nydn10JakKzw8lG','SUBMITTED','RECEIPT','5639017989765745382',1),(12,'Wed 17th Apr 2024 06:25 PM',NULL,NULL,'Received: \nfrom JUDITON FARM, \nDetails: DEVELOPMENT OF SOFTWARE , \nBank: 0759065744, \nRef: SGFERGH355DGH.\n\nTotal Amount: KES 0.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,6,'g4WHLGR6hPoatDS','SUBMITTED','RECEIPT','5639017989765745382',1),(13,'Wed 17th Apr 2024 06:25 PM',NULL,NULL,'Received: \nfrom JUDITON FARM, \nDetails: DEVELOPMENT OF SOFTWARE , \nBank: 0759065744, \nRef: SGFERGH355DGH.\n\nTotal Amount: KES 0.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,6,'GKy3Hw0Et7mRXpl','SUBMITTED','RECEIPT','2617416240357590507',1),(14,'Wed 17th Apr 2024 06:25 PM',NULL,NULL,'Received: \nfrom JUDITON FARM, \nDetails: DEVELOPMENT OF SOFTWARE , \nBank: 0759065744, \nRef: SGFERGH355DGH.\n\nTotal Amount: KES 0.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,6,'wZWPxU0n7pEOZXM','SUBMITTED','RECEIPT','2617416240357590507',1),(16,'Wed 17th Apr 2024 06:44 PM',NULL,NULL,'Received: \nfrom SKDDJC, \nDetails: DFDJFJD, \nBank: ERICK MURIITHI WANJOHI MPESA NO: ERICK MURIITHI WANJOHI MPESA, \nRef: SSJCJD.\n\nTotal Amount: KES 2500.0','254710303973','[ALL MEMBERS]',NULL,NULL,1,7,'3A8aH3t43daoIKT','SUBMITTED','RECEIPT','4351298359563077117',1),(17,'Wed 17th Apr 2024 06:44 PM',NULL,NULL,'Received: \nfrom SKDDJC, \nDetails: DFDJFJD, \nBank: ERICK MURIITHI WANJOHI MPESA NO: ERICK MURIITHI WANJOHI MPESA, \nRef: SSJCJD.\n\nTotal Amount: KES 2500.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,7,'T9uDEztGZEwmCLV','SUBMITTED','RECEIPT','4351298359563077117',1),(20,'Wed 17th Apr 2024 06:44 PM',NULL,NULL,'Received: \nfrom SKDDJC, \nDetails: DFDJFJD, \nBank: ERICK MURIITHI WANJOHI MPESA NO: ERICK MURIITHI WANJOHI MPESA, \nRef: SSJCJD.\n\nTotal Amount: KES 2500.0','254718845069','[ALL MEMBERS]',NULL,NULL,1,7,'eQyx9KFAi2gAp0k','SUBMITTED','RECEIPT','4351298359563077117',1),(22,'Wed 17th Apr 2024 06:44 PM',NULL,NULL,'Received: \nfrom SKDDJC, \nDetails: DFDJFJD, \nBank: ERICK MURIITHI WANJOHI MPESA NO: ERICK MURIITHI WANJOHI MPESA, \nRef: SSJCJD.\n\nTotal Amount: KES 2500.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,7,'mQlpbXPoVWgmAVb','SUBMITTED','RECEIPT','4351298359563077117',1),(23,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254710303973','[ALL MEMBERS]',NULL,NULL,1,8,'48958XHwRcl6MXP','SUBMITTED','RECEIPT','7174727990307867037',1),(24,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,8,'4I9hnXDsYTgWdAG','SUBMITTED','RECEIPT','7174727990307867037',1),(25,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,8,'RpulkQCGMQxdfzm','SUBMITTED','RECEIPT','7174727990307867037',1),(26,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254718845069','[ALL MEMBERS]',NULL,NULL,1,8,'T9Ugvqh0JnEfS9Z','SUBMITTED','RECEIPT','7174727990307867037',1),(27,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom STEPHEN KABUI, \nDetails: TRANSPORT OF FEEDS FROM TOWN, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 30000.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,9,'744tWRdzR6S7tRS','SUBMITTED','RECEIPT','5675654438371214604',1),(28,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom STEPHEN KABUI, \nDetails: TRANSPORT OF FEEDS FROM TOWN, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 30000.0','254718845069','[ALL MEMBERS]',NULL,NULL,1,9,'PWRLZGnieIh8C2Q','SUBMITTED','RECEIPT','5675654438371214604',1),(29,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom STEPHEN KABUI, \nDetails: TRANSPORT OF FEEDS FROM TOWN, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 30000.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,9,'Z2tWRajfr678N3A','SUBMITTED','RECEIPT','5675654438371214604',1),(30,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom STEPHEN KABUI, \nDetails: TRANSPORT OF FEEDS FROM TOWN, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 30000.0','254710303973','[ALL MEMBERS]',NULL,NULL,1,9,'e1ArjAlXNbdG7ku','SUBMITTED','RECEIPT','5675654438371214604',1),(31,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,10,'9Qi863r9F1Muq7w','SUBMITTED','RECEIPT','7243283969068714736',1),(32,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254718845069','[ALL MEMBERS]',NULL,NULL,1,10,'j7m49N8xQ219Lmu','SUBMITTED','RECEIPT','7243283969068714736',1),(33,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,10,'rBvzZqpTjVQ9aQQ','SUBMITTED','RECEIPT','7243283969068714736',1),(34,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254710303973','[ALL MEMBERS]',NULL,NULL,1,10,'wv4Q3dhr56vqkqq','SUBMITTED','RECEIPT','7243283969068714736',1),(35,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Paid: \nTo STEPHEN KABUI, \nDetails: INSULIN, \nBank: JUDY WAINOI MPESA NO: 0721660482, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 4000.0','254718845069','[ALL MEMBERS]',NULL,NULL,1,11,'C2xQdqnJJqenqvz','SUBMITTED','RECEIPT','5814899746144929128',1),(36,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Paid: \nTo STEPHEN KABUI, \nDetails: INSULIN, \nBank: JUDY WAINOI MPESA NO: 0721660482, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 4000.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,11,'Q8E4by1bQlcsz2M','SUBMITTED','RECEIPT','5814899746144929128',1),(37,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Paid: \nTo STEPHEN KABUI, \nDetails: INSULIN, \nBank: JUDY WAINOI MPESA NO: 0721660482, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 4000.0','254710303973','[ALL MEMBERS]',NULL,NULL,1,11,'QUqVSiKeiEhhl9t','SUBMITTED','RECEIPT','5814899746144929128',1),(38,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Paid: \nTo STEPHEN KABUI, \nDetails: INSULIN, \nBank: JUDY WAINOI MPESA NO: 0721660482, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 4000.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,11,'mfN4fiJYNktesjx','SUBMITTED','RECEIPT','5814899746144929128',1),(39,'Wed 17th Apr 2024 09:19 PM',NULL,NULL,'Dear Erick  Muriithi  Wanjohi, your payment has been processed. Basic Income: 50,000, Gross Income: 50,000. Please confirm receipt. Thank you!','254759065744','[ALL MEMBERS]',NULL,NULL,1,12,'wmCLPRtLvdZB1O7','SUBMITTED','RECEIPT','8246625176078074736',1),(40,'Wed 17th Apr 2024 09:19 PM',NULL,NULL,'Dear PETER  MAINA  MURIITHI, your payment has been processed. Basic Income: 70,000, Gross Income: 70,000. Please confirm receipt. Thank you!','254710443317','[ALL MEMBERS]',NULL,NULL,1,13,'uSqHYRpiyz1mGzY','SUBMITTED','RECEIPT','3550918080897371976',1),(41,'Wed 17th Apr 2024 10:05 PM',NULL,NULL,'Dear Erick  Muriithi  Wanjohi, \nyour payment has been processed. \nBasic Income: 50,000, \nGross Income: 50,000. \nPlease confirm receipt. Thank you!','254759065744','[ALL MEMBERS]',NULL,NULL,1,14,'I7WYIWS9WE8wAI5','SUBMITTED','RECEIPT','1520650437720621200',1),(42,'Wed 17th Apr 2024 10:05 PM',NULL,NULL,'Dear PETER  MAINA  MURIITHI, \nyour payment has been processed. \nBasic Income: 70,000, \nGross Income: 70,000. \nPlease confirm receipt. Thank you!','254710443317','[ALL MEMBERS]',NULL,NULL,1,15,'ygNeDyHkypEA8sF','SUBMITTED','RECEIPT','3317584540473969024',1),(43,'Wed 17th Apr 2024 10:05 PM',NULL,NULL,'Dear Erick  Muriithi  Wanjohi, \nyour payment has been processed. \nBasic Income: 50,000, \nGross Income: 50,000. \nPlease confirm receipt. Thank you!','254759065744','[ALL MEMBERS]',NULL,NULL,1,16,'k9anHLofcDu3zlA','SUBMITTED','RECEIPT','3713477068806769878',1),(44,'Wed 17th Apr 2024 10:05 PM',NULL,NULL,'Dear PETER  MAINA  MURIITHI, \nyour payment has been processed. \nBasic Income: 70,000, \nGross Income: 70,000. \nPlease confirm receipt. Thank you!','254710443317','[ALL MEMBERS]',NULL,NULL,1,17,'2Gw65VMLXs6hpyY','SUBMITTED','RECEIPT','6599615027503777437',1),(45,'Tue 6th Aug 2024 11:03 AM',NULL,NULL,'Received: \nfrom DANIEL GITAU, \nDetails: ROADTRIP #1, \nBank: GROUP MPESA NO: 759065744, \nRef: SH654E1HLN.\n\nTotal Amount: KES 80.0','254759065744','[ALL MEMBERS]',NULL,NULL,5,1,'wrhgbBUAWJgKRrY','SUBMITTED','RECEIPT','128359639163292935',2);
/*!40000 ALTER TABLE `sms_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subregistration`
--

DROP TABLE IF EXISTS `subregistration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subregistration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `biometricsid` int(11) NOT NULL,
  `bloodgroup` varchar(300) DEFAULT NULL,
  `book returned` varchar(300) DEFAULT NULL,
  `books borrowed` varchar(300) DEFAULT NULL,
  `currentstatus` varchar(300) DEFAULT NULL,
  `date of joining` date DEFAULT NULL,
  `email` varchar(300) DEFAULT NULL,
  `first name` varchar(300) DEFAULT NULL,
  `gadgetstatus` int(11) DEFAULT NULL,
  `gender` varchar(300) DEFAULT NULL,
  `id nu` varchar(300) DEFAULT NULL,
  `idnu` varchar(300) DEFAULT NULL,
  `medical condition` varchar(300) DEFAULT NULL,
  `occupation` varchar(300) DEFAULT NULL,
  `phone nu` varchar(300) DEFAULT NULL,
  `position` varchar(300) DEFAULT NULL,
  `reason_for_removal` varchar(300) DEFAULT NULL,
  `religion` varchar(300) DEFAULT NULL,
  `removal_date` date DEFAULT NULL,
  `residence` varchar(300) DEFAULT NULL,
  `second name` varchar(1300) DEFAULT NULL,
  `simage` varchar(300) DEFAULT NULL,
  `status` varchar(300) DEFAULT NULL,
  `surname` varchar(300) DEFAULT NULL,
  `whoapproved` varchar(300) DEFAULT NULL,
  `farm` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsm0kltp9t4ay49mw5bp6wslnw` (`farm`),
  CONSTRAINT `FKsm0kltp9t4ay49mw5bp6wslnw` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subregistration`
--

LOCK TABLES `subregistration` WRITE;
/*!40000 ALTER TABLE `subregistration` DISABLE KEYS */;
/*!40000 ALTER TABLE `subregistration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tax`
--

DROP TABLE IF EXISTS `tax`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tax` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `date` date DEFAULT NULL,
  `paymentpv` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT 'Pending payment',
  `type` varchar(500) NOT NULL,
  `pv#` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmpukewmc572ra6vuhjvs6k9mw` (`pv#`),
  CONSTRAINT `FKmpukewmc572ra6vuhjvs6k9mw` FOREIGN KEY (`pv#`) REFERENCES `paymentvouchers` (`pv id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tax`
--

LOCK TABLES `tax` WRITE;
/*!40000 ALTER TABLE `tax` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacherregistration`
--

DROP TABLE IF EXISTS `teacherregistration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacherregistration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(300) DEFAULT NULL,
  `biometricsid` int(11) NOT NULL,
  `constituency` varchar(300) DEFAULT NULL,
  `county` varchar(300) DEFAULT NULL,
  `currentstatus` varchar(255) DEFAULT NULL,
  `date of joining` date DEFAULT NULL,
  `email` varchar(300) DEFAULT NULL,
  `ethnicity` varchar(300) DEFAULT NULL,
  `first name` varchar(1300) DEFAULT NULL,
  `gadgetstatus` int(11) DEFAULT NULL,
  `gender` varchar(300) DEFAULT NULL,
  `idnumber` varchar(300) DEFAULT NULL,
  `json` varchar(255) DEFAULT NULL,
  `kra` varchar(300) DEFAULT NULL,
  `mainsub` varchar(3000) DEFAULT NULL,
  `marital` varchar(300) DEFAULT NULL,
  `password` varchar(3000) DEFAULT NULL,
  `phone nu` varchar(300) DEFAULT NULL,
  `religion` varchar(300) DEFAULT NULL,
  `residence` varchar(300) DEFAULT NULL,
  `second name` varchar(1300) DEFAULT NULL,
  `service` varchar(500) DEFAULT NULL,
  `simage` varchar(3000) DEFAULT NULL,
  `status` varchar(3000) DEFAULT NULL,
  `sub county` varchar(300) DEFAULT NULL,
  `surname` varchar(300) DEFAULT NULL,
  `title` varchar(300) DEFAULT NULL,
  `trnu` varchar(300) DEFAULT NULL,
  `tsc nu` varchar(300) DEFAULT NULL,
  `upi` varchar(300) DEFAULT NULL,
  `ward` varchar(300) DEFAULT NULL,
  `farm` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKaikccej8qvf9k0fuguts3dbxe` (`farm`),
  CONSTRAINT `FKaikccej8qvf9k0fuguts3dbxe` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacherregistration`
--

LOCK TABLES `teacherregistration` WRITE;
/*!40000 ALTER TABLE `teacherregistration` DISABLE KEYS */;
/*!40000 ALTER TABLE `teacherregistration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`),
  KEY `FKhfh9dx7w3ubf1co1vdev94g3f` (`user_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,2);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT '',
  `logins` int(11) DEFAULT 0,
  `password` varchar(255) DEFAULT NULL,
  `sessionid` varchar(600) DEFAULT NULL,
  `signature` varchar(255) DEFAULT '',
  `theme` int(11) DEFAULT 0,
  `username` varchar(255) DEFAULT NULL,
  `farm` int(11) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  KEY `FKn16vo2r5kfh53odunc2qrk2ql` (`farm`),
  CONSTRAINT `FKn16vo2r5kfh53odunc2qrk2ql` FOREIGN KEY (`farm`) REFERENCES `farm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin@gmail.com','static/fgck/MemberImages/full-gospel.jpg',255,'$2a$10$3iPbdhq.0eUM.2zyDZadeuVPiCBPsTkOx6IJsIttNmPZDN6YgljZu','0F444C27F5F4A1BB2AF23B9D07A221B4',NULL,1,'admin',1,'0759065744'),(2,'wanjohijudy4@gmail.com',NULL,4,'$2a$10$3iPbdhq.0eUM.2zyDZadeuVPiCBPsTkOx6IJsIttNmPZDN6YgljZu','952ACB4740AC4F1A2060729832409530',NULL,1,'judy',1,'0718845069'),(3,'anthonykaroki69@gmail.com',NULL,144,'$2a$10$3iPbdhq.0eUM.2zyDZadeuVPiCBPsTkOx6IJsIttNmPZDN6YgljZu','931BED9E594A9B54A9A347B907D4325D',NULL,1,'anthony',1,'0710303973'),(4,'maishpeter22@gmail.com',NULL,0,'$2a$10$3iPbdhq.0eUM.2zyDZadeuVPiCBPsTkOx6IJsIttNmPZDN6YgljZu',NULL,NULL,1,'maish',1,'0710443317'),(5,'wanjohierick07@gmail.com','',13,'$2a$10$3iPbdhq.0eUM.2zyDZadeuVPiCBPsTkOx6IJsIttNmPZDN6YgljZu','74C843186FDB8EF67B7892ECB12EBA95','',0,'admin@mbogi',2,'0759065744');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendors`
--

DROP TABLE IF EXISTS `vendors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vendors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(100) NOT NULL,
  `company` varchar(100) NOT NULL,
  `contact_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `farm` int(11) NOT NULL,
  `item_group` varchar(100) NOT NULL,
  `kra_pin` varchar(100) NOT NULL,
  `phone` varchar(100) NOT NULL,
  `reg_date` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vendors`
--

LOCK TABLES `vendors` WRITE;
/*!40000 ALTER TABLE `vendors` DISABLE KEYS */;
/*!40000 ALTER TABLE `vendors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voucher_signatory`
--

DROP TABLE IF EXISTS `voucher_signatory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `voucher_signatory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountant` int(11) NOT NULL,
  `acc_date` date DEFAULT NULL,
  `second_signatory` int(11) NOT NULL,
  `senior_pastor` int(11) NOT NULL,
  `sp_date` date DEFAULT NULL,
  `ss_date` date DEFAULT NULL,
  `tr_date` date DEFAULT NULL,
  `treasurer` int(11) NOT NULL,
  `voucher_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6vgeu1msifd6dw4x7epiie6pw` (`voucher_id`),
  CONSTRAINT `FK6vgeu1msifd6dw4x7epiie6pw` FOREIGN KEY (`voucher_id`) REFERENCES `paymentvouchers` (`pv id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voucher_signatory`
--

LOCK TABLES `voucher_signatory` WRITE;
/*!40000 ALTER TABLE `voucher_signatory` DISABLE KEYS */;
INSERT INTO `voucher_signatory` VALUES (28,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,28),(29,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,29),(30,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,30),(31,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,31),(32,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,32),(33,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,33),(34,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,34),(35,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,35),(36,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,36),(37,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,37),(38,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,38),(39,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,39),(40,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,40),(41,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,41),(42,1,'2024-08-11',1,1,'2024-08-11','2024-08-11','2024-08-11',1,42),(43,1,'2024-08-26',1,1,'2024-08-26','2024-08-26','2024-08-26',1,43);
/*!40000 ALTER TABLE `voucher_signatory` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-16 10:09:06
