-- MariaDB dump 10.19  Distrib 10.6.16-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: juditon
-- ------------------------------------------------------
-- Server version	10.6.16-MariaDB-0ubuntu0.22.04.1

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
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounttransactions`
--

LOCK TABLES `accounttransactions` WRITE;
/*!40000 ALTER TABLE `accounttransactions` DISABLE KEYS */;
INSERT INTO `accounttransactions` VALUES (1,7,0,'RIQ6TP2AU0',0,'2023-09-26',1523,'iron Vaccination and teeth pricking ','JULIUS NJIRU VET','PV1','Approved',3,1),(2,7,0,'RIU86VD702',0,'2023-09-30',4257,'iron Vaccination and teeth pricking ','JULIUS NJIRU VET','PV2','Approved',3,1),(3,7,0,'RJ32HUZ0AA',0,'2023-10-03',6978,'iron Vaccination and teeth pricking ','JULIUS NJIRU VET','PV3','Approved',3,1),(4,7,0,'RJ500HLQ52',0,'2023-10-05',1933,'iron Vaccination and teeth pricking ','JULIUS NJIRU VET','PV4','Approved',3,1),(5,7,0,'RJB88001KU',0,'2023-10-11',3503,'iron Vaccination and teeth pricking ','JULIUS NJIRU VET','PV5','Approved',3,1),(6,7,0,'RJD2DXHSJ2',0,'2023-10-13',3453,'iron Vaccination and teeth pricking ','JULIUS NJIRU VET','PV6','Approved',3,1),(7,7,0,'RJU9YSPW1J',0,'2023-10-30',6478,'iron Vaccination and teeth pricking ','JULIUS NJIRU VET','PV7','Approved',3,1),(8,7,0,'RL66FGCT0K',0,'2023-12-06',7078,'iron Vaccination and teeth pricking ','JULIUS NJIRU VET','PV8','Approved',3,1),(9,7,0,'SA73B91Z2D',0,'2024-01-07',1883,'iron Vaccination and teeth pricking ','JULIUS NJIRU VET','PV9','Approved',3,1),(10,1,0,'',182000,'2023-12-23',0,'SALE OF PIGLETS','MANY PEOPLE','RCT1','Approved',3,1),(11,1,0,'',80000,'2023-12-23',0,'SALE OF 2 MOTHERS','MANY PEOPLE','RCT2','Approved',3,1),(12,4,0,'',0,'2024-01-07',206200,'PIG FEEDS','THIKA LIMITED','PV10','Approved',3,1),(13,4,0,'SAB8QB50DS',0,'2024-01-11',28000,'PIG FEEDS ACQUISITION','THIKA FARMERS GROUP LIMITED','PV11','Approved',3,1),(14,4,0,'SA32XFE2W6',0,'2024-01-03',3850,'PIG FEEDS AND MEDICINE','LIMAZONE ENTERPRISES','PV12','Approved',3,1),(15,1,0,'SAE61GPS0Q',8000,'2024-01-14',0,'SALE OF PIGLETS','CATHERINE GITHINJI','RCT3','Approved',1,1),(16,5,0,'',0,'2023-11-30',71350,'FULL RICE COSTS','MANY PLACES','PV13','Approved',2,1),(17,9,0,'RLK1PC628X',0,'2023-12-22',1633,'WEEDING ON 21 AND 22','MANY PEOPLE','PV14','Approved',3,1),(18,11,0,'',0,'2023-12-28',800,'BUDDING','MANY PEOPLE','PV15','Approved',3,1),(19,5,0,'',0,'2023-12-23',7475,'FERTILIZER ACQUISITION','MAZAO LIMITED','PV16','Approved',3,1),(20,5,0,'SAF737DZM7',0,'2024-01-15',3050,'FERTILIZER ACQUISITION','MAZAO LIMITED','PV17','Approved',1,1),(22,14,0,'SAF33HFA5D',0,'2024-01-15',2033,'RICE TRANSPORT TO RICE MILLERS','EVAN MUSA','PV18','Approved',1,1),(23,14,0,'SAF739AB2R',0,'2024-01-15',500,'FUEL FOR TRANSPORT OF RICE TO RICE MILLERS','KOBIL COMPANY','PV19','Approved',1,1),(24,16,NULL,'SAF33H6JYJ',100,'2024-01-15',NULL,'Cash From Bank','Cash From Bank','RF1','Approved',1,1),(25,17,NULL,'SAF33H6JYJ',NULL,'2024-01-15',100,'Cash Withdraw','Cash Withdraw','RF1','Approved',4,1),(26,16,NULL,'SAG67LYYCI',NULL,'2024-01-16',200,'Cash Withdraw','Cash Withdraw','RF2','Approved',1,1),(27,17,NULL,'SAG67LYYCI',200,'2024-01-16',NULL,'Cash From Bank','Cash From Bank','RF2','Approved',4,1),(28,15,0,'SAG17M8X47',0,'2024-01-16',5888,'DRYING, MILLING OF RICE','EVAN MUSA','PV20','Approved',4,1),(29,18,0,'',0,'2024-01-02',1400,'GENERATOR DIESEL ACQUISITION','BENSON MWAURA MWANGI','PV21','Approved',2,1),(30,19,0,'SA39XKFPTH',0,'2024-01-03',357,'TRANSPORT OF FEEDS FROM TOWN','IBRAHIM KIMOTHO (NJIRU)','PV22','Approved',4,1),(31,19,0,'',0,'2024-01-04',300,'TRANSPORT OF FEEDS FROM GATE','KIBIGII','PV23','Approved',4,1),(41,3,0,'SDVVJD',1000,'2024-06-02',0,'PHONE TESTING','SJFJS','RCT11','Approved',1,1),(42,2,0,'SDVVJD',3000,'2024-06-02',0,'PHONE TESTING','SJFJS','RCT11','Approved',1,1),(43,2,0,'DDU',3000,'2024-06-03',0,'SHFFJ','DFJDG','RCT12','Approved',1,1),(44,3,0,'DDU',2500,'2024-06-03',0,'SHFFJ','DFJDG','RCT12','Approved',1,1),(45,3,0,'DGFR',2300,'2024-06-03',0,'DDJD','DFJF','RCT14','Approved',1,1),(46,3,0,'DDJD',10000,'2024-06-03',0,'DFBSF','DDJF','RCT15','Approved',1,1),(47,3,0,'DDJD',10000,'2024-06-03',0,'DFBSF','DDJF','RCT16','Approved',1,1),(48,3,0,'CGEFG',20000,'2024-06-03',0,'FGFG','DGFF','RCT17','Approved',1,1),(49,3,0,'CGFCV',555,'2024-06-05',0,'CBDC','XVDC','RCT20','Approved',1,1),(50,3,0,'ERJDJ',255,'2024-06-06',0,'DJDFJ','DJFJF','RCT21','Approved',1,1),(51,3,0,'FNTBT',859,'2024-06-06',0,'GNYNT','TNYNT','RCT22','Approved',1,1),(52,3,0,'FNTBT',859,'2024-06-06',0,'GNYNT','TNYNT','RCT23','Approved',1,1),(53,3,0,'FNTBT',859,'2024-06-06',0,'GNYNT','TNYNT','RCT24','Approved',1,1),(54,3,0,'FNTBT',859,'2024-06-06',0,'GNYNT','TNYNT','RCT25','Approved',1,1),(55,3,0,'RHTNTV',585,'2024-06-06',0,'FHTBG','FBFG','RCT26','Approved',1,1),(56,3,0,'RHTNTV',255,'2024-06-06',0,'FHTBG','FBFG','RCT26','Approved',1,1),(57,3,0,'RHTNTV',585,'2024-06-06',0,'FHTBG','FBFG','RCT27','Approved',1,1),(58,3,0,'RHTNTV',255,'2024-06-06',0,'FHTBG','FBFG','RCT27','Approved',1,1),(59,3,0,'SSHF',10000,'2024-06-08',0,'SSHFJ','SJDCH','RCT30','Approved',1,1),(60,10,0,'SSHF',5388,'2024-06-08',0,'SSHFJ','SJDCH','RCT30','Approved',1,1),(61,3,0,'SSJCJD',2500,'2024-07-02',0,'DFDJFJD','SKDDJC','RCT31','Approved',1,1),(62,3,0,'SSJCJD',2500,'2024-07-02',0,'DFDJFJD','SKDDJC','RCT32','Approved',1,1),(63,3,0,'SAF33H6JYJ',0,'2024-06-22',25100,'AQUISITION OF BOAR TO SERVE','STEPHEN KABUI','PV24','Approved',3,1),(64,3,0,'SAG67LYYCI',0,'2024-06-04',10000,'TRANSPORT OF FEEDS FROM TOWN','STEPHEN KABUI','PV25','Approved',1,1),(65,3,0,'SAG67LYYCI',134422,'2024-04-17',0,'TRANSPORT OF FEEDS FROM TOWN','STEPHEN KABUI','RCT33','Approved',1,1),(66,3,0,'WDHD',58,'2024-06-06',0,'XJXDH','DDJF','RCT34','Approved',1,1),(67,3,0,'SAF33H6JYJ',1000,'2024-06-11',0,'TRANSPORT OF FEEDS FROM TOWN','STEPHEN KABUI','RCT35','Approved',1,1),(68,3,0,'SAF33H6JYJ',30000,'2024-06-12',0,'TRANSPORT OF FEEDS FROM TOWN','STEPHEN KABUI','RCT37','Approved',1,1),(69,7,0,'SAF33H6JYJ',0,'2024-04-17',4000,'INSULIN','STEPHEN KABUI','PV26','Approved',3,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activities`
--

LOCK TABLES `activities` WRITE;
/*!40000 ALTER TABLE `activities` DISABLE KEYS */;
INSERT INTO `activities` VALUES (1,'PIG SALES',1,1),(2,'LOAN IN',5,1),(3,'ASSET ACQUISITION',6,1),(4,'PIG FEEDS',2,1),(5,'RICE FERTILIZER',4,1),(6,'SAVINGS',6,1),(7,'PIG DRUGS/MEDICATION',2,1),(8,'RICE SALES',3,1),(9,'RICE WEEDING',4,1),(10,'KURIRA',4,1),(11,'BARRIER MAINTENANCE',4,1),(12,'RICE PESTICIDES/HERBICIDES',4,1),(13,'LOAN OUT',6,1),(14,'RICE TRANSPORT',4,1),(15,'MILLING',4,1),(16,'WITHDRAW',7,1),(17,'DEPOSIT',7,1),(18,'DIESEL',6,1),(19,'FEEDS TRANSPORT',2,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitygroups`
--

LOCK TABLES `activitygroups` WRITE;
/*!40000 ALTER TABLE `activitygroups` DISABLE KEYS */;
INSERT INTO `activitygroups` VALUES (1,'PIGS INCOME',1),(2,'PIGS EXPENSES',1),(3,'RICE INCOME',1),(4,'RICE EXPENSES',1),(5,'GENERAL INCOME',1),(6,'GENERAL EXPENSES',1),(7,'OTHERS',1);
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
INSERT INTO `allowances` VALUES (7,1,50000,'2023-12-27 00:00:00.000000',1,3),(8,1,70000,'2023-12-27 00:00:00.000000',2,3),(9,1,50000,'2024-01-08 00:00:00.000000',1,4),(10,1,70000,'2024-01-08 00:00:00.000000',2,4),(12,1,50000,'2024-02-08 00:00:00.000000',1,5),(13,1,70000,'2024-02-08 00:00:00.000000',2,5),(14,1,50000,'2024-04-14 00:00:00.000000',1,6),(15,1,70000,'2024-04-14 00:00:00.000000',2,6);
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bankaccounts`
--

LOCK TABLES `bankaccounts` WRITE;
/*!40000 ALTER TABLE `bankaccounts` DISABLE KEYS */;
INSERT INTO `bankaccounts` VALUES (1,'0759065744','ERICK MURIITHI WANJOHI MPESA','Mpesa','0001','MOBILE',0,'','Bank',1),(2,'00000000001','PETTY CASH','CASH','','MOBILE',0,'','Cash',1),(3,'0721660482','JUDY WAINOI MPESA','MPESA','','',0,'','Bank',1),(4,'0710443317','PETER MAINA MPESA','MPESA','','',0,'','Bank',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `closing_balance`
--

LOCK TABLES `closing_balance` WRITE;
/*!40000 ALTER TABLE `closing_balance` DISABLE KEYS */;
INSERT INTO `closing_balance` VALUES (1,0,'2023-01-01',1),(2,0,'2023-02-01',1),(3,0,'2023-03-01',1),(4,0,'2023-04-01',1),(5,0,'2023-05-01',1),(6,0,'2023-06-01',1),(7,0,'2023-07-01',1),(8,0,'2023-08-01',1),(9,-5780,'2023-09-01',1),(10,-28125,'2023-10-01',1),(11,-99475,'2023-11-01',1),(12,145539,'2023-12-01',1),(13,-99922,'2024-01-01',1),(14,-99922,'2024-02-01',1),(15,-99922,'2024-03-01',1),(16,-99922,'2024-04-01',1),(17,-99922,'2024-05-01',1),(18,-99922,'2024-06-01',1),(19,-99922,'2024-07-01',1),(20,-99922,'2024-08-01',1),(21,-99922,'2024-09-01',1),(22,-99922,'2024-10-01',1),(23,-99922,'2024-11-01',1),(24,-99922,'2024-12-01',1),(25,0,'2014-01-01',1),(26,0,'2014-02-01',1),(27,0,'2014-03-01',1),(28,0,'2014-04-01',1),(29,0,'2014-05-01',1),(30,0,'2014-06-01',1),(31,0,'2014-07-01',1),(32,0,'2014-08-01',1),(33,0,'2014-09-01',1),(34,0,'2014-10-01',1),(35,0,'2014-11-01',1),(36,0,'2014-12-01',1);
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
INSERT INTO `companyofficials` VALUES (1,1,1,1,1,1),(2,5,2,5,5,5),(3,6,3,6,6,6);
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
INSERT INTO `deductionsandearningsexpirydat` VALUES (1,50000,'2050-01-01',1,1,'2023-12-05','Approved','EARNING',1),(2,70000,'2050-01-01',1,1,'2023-12-05','Approved','EARNING',2);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farm`
--

LOCK TABLES `farm` WRITE;
/*!40000 ALTER TABLE `farm` DISABLE KEYS */;
INSERT INTO `farm` VALUES (1,'2','Kenya','wanjohierick07@gmail.com','static/fgck/MemberImages/full-gospel.jpg','JUDITON FARM',NULL,NULL,NULL,'700258330','KIRINYAGA','HostPinacle','WALGOTECHSC','nuC1tAk3','kenmutwiri','fgck',NULL,'10200','','0',NULL,1,6),(2,'5 - KERUGOYA','Kenya','stbath@gmail.com',NULL,'ST BARTHOLOMEW CATHOLIC CHURCH',NULL,NULL,NULL,'0716930769','KIRINYAGA',NULL,NULL,NULL,NULL,'stBath',NULL,'10100','','0',NULL,NULL,NULL),(3,'214 - 01000 THIKA','Kenya','STANDREWSCATHEDRALSTHIKA@gmail.com','static/thika/MemberImages/ack-logo.jpg','ST. ANDREW\'S CATHEDRAL THIKA',NULL,NULL,NULL,'0721941704/073922557','THIKA',NULL,NULL,NULL,NULL,'thika',NULL,'10200','','0',NULL,NULL,NULL);
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
INSERT INTO `issued_items` VALUES (1,NULL,1,'member','','root','2024-04-17 20:58:15.000000',1,1,NULL,NULL,NULL,'Non Refundable');
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
INSERT INTO `item_group` VALUES (1,1,'OTHERS');
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
INSERT INTO `item_stock` VALUES (1,10,'Opening Stock',1,1,'2024-04-17 20:41:06.000000'),(2,-1,'Stock issued to  on 2024-04-17',1,1,'2024-04-17 20:58:16.000000'),(3,10,'Goods received for purchase order item #1  on 2024-04-18',1,1,'2024-04-18 00:00:00.000000');
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
INSERT INTO `monthly pays` VALUES (7,50000,0,NULL,0,0,12,0,0,0,'0000-00-00 00:00:00.000000',1,NULL,50000,2023,3),(8,70000,0,NULL,0,0,12,0,0,0,'0000-00-00 00:00:00.000000',2,NULL,70000,2023,3),(9,50000,0,NULL,0,0,1,0,0,0,'0000-00-00 00:00:00.000000',1,NULL,50000,2024,4),(10,70000,0,NULL,0,0,1,0,0,0,'0000-00-00 00:00:00.000000',2,NULL,70000,2024,4),(12,50000,0,NULL,0,6600,2,0,1000,0,'0000-00-00 00:00:00.000000',1,NULL,50000,2024,5),(13,70000,0,NULL,0,12600,2,0,1000,0,'0000-00-00 00:00:00.000000',2,NULL,70000,2024,5),(14,50000,0,NULL,0,6600,4,0,1000,0,'0000-00-00 00:00:00.000000',1,NULL,50000,2024,6),(15,70000,0,NULL,0,12600,4,0,1000,0,'0000-00-00 00:00:00.000000',2,NULL,70000,2024,6);
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
INSERT INTO `payment_request` VALUES (1,10000,NULL,NULL,'1','2024-04-17 21:08:53.000000','','',NULL,1,NULL,'',NULL,'Pending');
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
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentvoucherdetails`
--

LOCK TABLES `paymentvoucherdetails` WRITE;
/*!40000 ALTER TABLE `paymentvoucherdetails` DISABLE KEYS */;
INSERT INTO `paymentvoucherdetails` VALUES (1,'PAID AMOUNT',1,1500,1,1),(2,'TRANSACTION COST',1,23,1,1),(9,'PAID AMOUNT',1,4200,1,2),(10,'TRANSACTION COST',1,57,1,2),(11,'PAID AMOUNT',1,6900,1,3),(12,'TRANSACTION COST',1,78,1,3),(13,'PAID AMOUNT',1,1900,1,4),(14,'TRANSACTION COST',1,33,1,4),(15,'PAID AMOUNT',1,3450,1,5),(16,'TRANSACTION COST',1,53,1,5),(17,'PAID AMOUNT',1,3400,1,6),(18,'TRANSACTION COST',1,53,1,6),(19,'PAID AMOUNT',1,6400,1,7),(20,'TRANSACTION COST',1,78,1,7),(21,'PAID AMOUNT',1,7000,1,8),(22,'TRANSACTION COST',1,78,1,8),(23,'PAID AMOUNT',1,1850,1,9),(24,'TRANSACTION COST',1,33,1,9),(25,'SOW AND WEANER',1,206200,1,10),(26,'SOW & WEANER',10,2800,1,11),(27,'SOW & WEANER',1,2550,1,12),(28,'DISINFECTANT AND ANTIBIOTIC SPRAY',1,1300,1,12),(29,'ALL COSTS',1,71350,1,13),(32,'BUDDING',1,800,1,15),(34,'1.5 FERTILIZERS',1,7375,1,16),(35,'LABOUR',1,100,1,16),(36,'AS (FALCON)',1,2950,1,17),(37,'LABOUR',1,100,1,17),(38,'DAY 1',1,800,1,14),(39,'DAY 2',1,800,1,14),(40,'TRANSACTION COST',1,33,1,14),(44,'TRANSPORT',1,2000,1,18),(45,'TRANSACTION COST',1,33,1,18),(46,'FUEL',1,500,1,19),(47,'ALL COSTS',1,5810,1,20),(48,'TRANSACTION COST',1,78,1,20),(49,'FUEL',7,200,1,21),(52,'TRANSPORT',1,300,1,23),(53,'TRANSPORT',1,350,1,22),(54,'TRANSACTION COST',1,7,1,22),(55,'BOAR',1,20000,1,24),(56,'TRANSPORT',1,5000,1,24),(57,'TRANSACTION COST',1,100,1,24),(58,'ALL COSTS',1,10000,1,25),(59,'ALL COSTS',1,1000,1,26),(60,'FUEL',1,3000,1,26);
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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentvouchers`
--

LOCK TABLES `paymentvouchers` WRITE;
/*!40000 ALTER TABLE `paymentvouchers` DISABLE KEYS */;
INSERT INTO `paymentvouchers` VALUES (1,7,'2023-09-26','iron Vaccination and teeth pricking ','JULIUS NJIRU VET','Approved','PV1',1),(2,7,'2023-09-30','iron Vaccination and teeth pricking ','JULIUS NJIRU VET','Approved','PV2',1),(3,7,'2023-10-03','iron Vaccination and teeth pricking ','JULIUS NJIRU VET','Approved','PV3',1),(4,7,'2023-10-05','iron Vaccination and teeth pricking ','JULIUS NJIRU VET','Approved','PV4',1),(5,7,'2023-10-11','iron Vaccination and teeth pricking ','JULIUS NJIRU VET','Approved','PV5',1),(6,7,'2023-10-13','iron Vaccination and teeth pricking ','JULIUS NJIRU VET','Approved','PV6',1),(7,7,'2023-10-30','iron Vaccination and teeth pricking ','JULIUS NJIRU VET','Approved','PV7',1),(8,7,'2023-12-06','iron Vaccination and teeth pricking ','JULIUS NJIRU VET','Approved','PV8',1),(9,7,'2024-01-07','iron Vaccination and teeth pricking ','JULIUS NJIRU VET','Approved','PV9',1),(10,4,'2024-01-07','PIG FEEDS','THIKA LIMITED','Approved','PV10',1),(11,4,'2024-01-11','PIG FEEDS ACQUISITION','THIKA FARMERS GROUP LIMITED','Approved','PV11',1),(12,4,'2024-01-03','PIG FEEDS AND MEDICINE','LIMAZONE ENTERPRISES','Approved','PV12',1),(13,5,'2023-11-30','FULL RICE COSTS','MANY PLACES','Approved','PV13',1),(14,9,'2023-12-22','WEEDING ON 21 AND 22','MANY PEOPLE','Approved','PV14',1),(15,11,'2023-12-28','BUDDING','MANY PEOPLE','Approved','PV15',1),(16,5,'2023-12-23','FERTILIZER ACQUISITION','MAZAO LIMITED','Approved','PV16',1),(17,5,'2024-01-15','FERTILIZER ACQUISITION','MAZAO LIMITED','Approved','PV17',1),(18,14,'2024-01-15','RICE TRANSPORT TO RICE MILLERS','EVAN MUSA','Approved','PV18',1),(19,14,'2024-01-15','FUEL FOR TRANSPORT OF RICE TO RICE MILLERS','KOBIL COMPANY','Approved','PV19',1),(20,15,'2024-01-16','DRYING, MILLING OF RICE','EVAN MUSA','Approved','PV20',1),(21,18,'2024-01-02','GENERATOR DIESEL ACQUISITION','BENSON MWAURA MWANGI','Approved','PV21',1),(22,19,'2024-01-03','TRANSPORT OF FEEDS FROM TOWN','IBRAHIM KIMOTHO (NJIRU)','Approved','PV22',1),(23,19,'2024-01-04','TRANSPORT OF FEEDS FROM GATE','KIBIGII','Approved','PV23',1),(24,3,'2024-06-22','AQUISITION OF BOAR TO SERVE','STEPHEN KABUI','Approved','PV24',1),(25,3,'2024-06-04','TRANSPORT OF FEEDS FROM TOWN','STEPHEN KABUI','Approved','PV25',1),(26,7,'2024-04-17','INSULIN','STEPHEN KABUI','Approved','PV26',1);
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
INSERT INTO `payrollpayments` VALUES (1,3,'MPESA',120000,'3455465EFRGF','2023-12-27'),(2,4,'MPESA',120000,'dfgjiori4t59','2024-01-08'),(3,5,'MPESA',100000,'fgikdfjior','2024-01-08'),(4,6,'MPESA',120000,'Fhgg','2024-04-14');
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payrolls`
--

LOCK TABLES `payrolls` WRITE;
/*!40000 ALTER TABLE `payrolls` DISABLE KEYS */;
INSERT INTO `payrolls` VALUES (3,'2023-12-27',1,'Paid'),(4,'2024-01-08',1,'Paid'),(5,'2024-02-08',1,'Paid'),(6,'2024-04-14',1,'Paid');
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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
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
INSERT INTO `purchase_order` VALUES (1,'','root','2024-04-18 00:00:24.000000','2024-04-18',1,1,'Fully Received',1);
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
INSERT INTO `purchase_order_items` VALUES (1,1,2800,10,1,1);
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
INSERT INTO `purchase_order_receives` VALUES (1,NULL,1,NULL,10,'root',NULL,NULL,1);
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
INSERT INTO `purchasesbills` VALUES (1,'',2800,'2024-04-18','BILL-1','2024-04-18','',1,'',10,1,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `references`
--

LOCK TABLES `references` WRITE;
/*!40000 ALTER TABLE `references` DISABLE KEYS */;
INSERT INTO `references` VALUES (1,26,38,2,1);
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
INSERT INTO `requisition` VALUES (1,'PIGS','MAISH','2024-04-17 23:59:51.000000','Ordered',1);
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
INSERT INTO `requisition_items` VALUES (1,'',1,2800,10,'',1,'',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_history`
--

LOCK TABLES `sms_history` WRITE;
/*!40000 ALTER TABLE `sms_history` DISABLE KEYS */;
INSERT INTO `sms_history` VALUES (1,'Wed 17th Apr 2024 05:42 PM',NULL,NULL,'Hello','759065744','[ALL MEMBERS]',NULL,NULL,1,1,'M5TJ5SZt9u2VXk4','FAILED','RECEIPT','2645004457198372952',1),(2,'Wed 17th Apr 2024 05:42 PM',NULL,NULL,'Hello','759065744','[ALL MEMBERS]',NULL,NULL,1,1,'h6WOVlOEXSnGCpZ','FAILED','RECEIPT','7384334355687623505',1),(3,'Wed 17th Apr 2024 06:03 PM',NULL,NULL,'Hello','759065744','[ALL MEMBERS]',NULL,NULL,1,2,'VHe1uQa5Qo3lpso','FAILED','RECEIPT','6813485902074497496',1),(4,'Wed 17th Apr 2024 06:05 PM',NULL,NULL,'Hello','2540759065744','[ALL MEMBERS]',NULL,NULL,1,3,'vHWZInWO86vvLhW','FAILED','RECEIPT','673722070650472559',1),(5,'Wed 17th Apr 2024 06:05 PM',NULL,NULL,'Hello','2540710443317','[ALL MEMBERS]',NULL,NULL,1,4,'4oSvaDOI7XF1m1V','SUBMITTED','RECEIPT','2970685135995424196',1),(6,'Wed 17th Apr 2024 06:05 PM',NULL,NULL,'Hello','2540759065744','[ALL MEMBERS]',NULL,NULL,1,4,'sNgVLoxgxBTYxI2','SUBMITTED','RECEIPT','2970685135995424196',1),(7,'Wed 17th Apr 2024 06:11 PM',NULL,NULL,'Hello','254710443317','[ALL MEMBERS]',NULL,NULL,1,5,'3VCtz8buuEZdggQ','SUBMITTED','RECEIPT','3617544819628263710',1),(8,'Wed 17th Apr 2024 06:11 PM',NULL,NULL,'Hello','254759065744','[ALL MEMBERS]',NULL,NULL,1,5,'KZrDpMRbK9t7q6y','SUBMITTED','RECEIPT','3617544819628263710',1),(9,'Wed 17th Apr 2024 06:11 PM',NULL,NULL,'Hello','254710443317','[ALL MEMBERS]',NULL,NULL,1,5,'1yJmwYseeYfO6bJ','SUBMITTED','RECEIPT','8077920227087632114',1),(10,'Wed 17th Apr 2024 06:11 PM',NULL,NULL,'Hello','254759065744','[ALL MEMBERS]',NULL,NULL,1,5,'Ad6ChHgiEuX7fij','SUBMITTED','RECEIPT','8077920227087632114',1),(11,'Wed 17th Apr 2024 06:25 PM',NULL,NULL,'Received: \nfrom JUDITON FARM, \nDetails: DEVELOPMENT OF SOFTWARE , \nBank: 0759065744, \nRef: SGFERGH355DGH.\n\nTotal Amount: KES 0.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,6,'Nydn10JakKzw8lG','SUBMITTED','RECEIPT','5639017989765745382',1),(12,'Wed 17th Apr 2024 06:25 PM',NULL,NULL,'Received: \nfrom JUDITON FARM, \nDetails: DEVELOPMENT OF SOFTWARE , \nBank: 0759065744, \nRef: SGFERGH355DGH.\n\nTotal Amount: KES 0.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,6,'g4WHLGR6hPoatDS','SUBMITTED','RECEIPT','5639017989765745382',1),(13,'Wed 17th Apr 2024 06:25 PM',NULL,NULL,'Received: \nfrom JUDITON FARM, \nDetails: DEVELOPMENT OF SOFTWARE , \nBank: 0759065744, \nRef: SGFERGH355DGH.\n\nTotal Amount: KES 0.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,6,'GKy3Hw0Et7mRXpl','SUBMITTED','RECEIPT','2617416240357590507',1),(14,'Wed 17th Apr 2024 06:25 PM',NULL,NULL,'Received: \nfrom JUDITON FARM, \nDetails: DEVELOPMENT OF SOFTWARE , \nBank: 0759065744, \nRef: SGFERGH355DGH.\n\nTotal Amount: KES 0.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,6,'wZWPxU0n7pEOZXM','SUBMITTED','RECEIPT','2617416240357590507',1),(16,'Wed 17th Apr 2024 06:44 PM',NULL,NULL,'Received: \nfrom SKDDJC, \nDetails: DFDJFJD, \nBank: ERICK MURIITHI WANJOHI MPESA NO: ERICK MURIITHI WANJOHI MPESA, \nRef: SSJCJD.\n\nTotal Amount: KES 2500.0','254710303973','[ALL MEMBERS]',NULL,NULL,1,7,'3A8aH3t43daoIKT','SUBMITTED','RECEIPT','4351298359563077117',1),(17,'Wed 17th Apr 2024 06:44 PM',NULL,NULL,'Received: \nfrom SKDDJC, \nDetails: DFDJFJD, \nBank: ERICK MURIITHI WANJOHI MPESA NO: ERICK MURIITHI WANJOHI MPESA, \nRef: SSJCJD.\n\nTotal Amount: KES 2500.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,7,'T9uDEztGZEwmCLV','SUBMITTED','RECEIPT','4351298359563077117',1),(20,'Wed 17th Apr 2024 06:44 PM',NULL,NULL,'Received: \nfrom SKDDJC, \nDetails: DFDJFJD, \nBank: ERICK MURIITHI WANJOHI MPESA NO: ERICK MURIITHI WANJOHI MPESA, \nRef: SSJCJD.\n\nTotal Amount: KES 2500.0','254718845069','[ALL MEMBERS]',NULL,NULL,1,7,'eQyx9KFAi2gAp0k','SUBMITTED','RECEIPT','4351298359563077117',1),(22,'Wed 17th Apr 2024 06:44 PM',NULL,NULL,'Received: \nfrom SKDDJC, \nDetails: DFDJFJD, \nBank: ERICK MURIITHI WANJOHI MPESA NO: ERICK MURIITHI WANJOHI MPESA, \nRef: SSJCJD.\n\nTotal Amount: KES 2500.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,7,'mQlpbXPoVWgmAVb','SUBMITTED','RECEIPT','4351298359563077117',1),(23,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254710303973','[ALL MEMBERS]',NULL,NULL,1,8,'48958XHwRcl6MXP','SUBMITTED','RECEIPT','7174727990307867037',1),(24,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,8,'4I9hnXDsYTgWdAG','SUBMITTED','RECEIPT','7174727990307867037',1),(25,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,8,'RpulkQCGMQxdfzm','SUBMITTED','RECEIPT','7174727990307867037',1),(26,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254718845069','[ALL MEMBERS]',NULL,NULL,1,8,'T9Ugvqh0JnEfS9Z','SUBMITTED','RECEIPT','7174727990307867037',1),(27,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom STEPHEN KABUI, \nDetails: TRANSPORT OF FEEDS FROM TOWN, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 30000.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,9,'744tWRdzR6S7tRS','SUBMITTED','RECEIPT','5675654438371214604',1),(28,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom STEPHEN KABUI, \nDetails: TRANSPORT OF FEEDS FROM TOWN, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 30000.0','254718845069','[ALL MEMBERS]',NULL,NULL,1,9,'PWRLZGnieIh8C2Q','SUBMITTED','RECEIPT','5675654438371214604',1),(29,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom STEPHEN KABUI, \nDetails: TRANSPORT OF FEEDS FROM TOWN, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 30000.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,9,'Z2tWRajfr678N3A','SUBMITTED','RECEIPT','5675654438371214604',1),(30,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom STEPHEN KABUI, \nDetails: TRANSPORT OF FEEDS FROM TOWN, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 30000.0','254710303973','[ALL MEMBERS]',NULL,NULL,1,9,'e1ArjAlXNbdG7ku','SUBMITTED','RECEIPT','5675654438371214604',1),(31,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,10,'9Qi863r9F1Muq7w','SUBMITTED','RECEIPT','7243283969068714736',1),(32,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254718845069','[ALL MEMBERS]',NULL,NULL,1,10,'j7m49N8xQ219Lmu','SUBMITTED','RECEIPT','7243283969068714736',1),(33,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,10,'rBvzZqpTjVQ9aQQ','SUBMITTED','RECEIPT','7243283969068714736',1),(34,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Received: \nfrom SJDKC, \nDetails: DDJFJV, \nBank: ERICK MURIITHI WANJOHI MPESA NO: 0759065744, \nRef: SJDJD.\n\nTotal Amount: KES 0.0','254710303973','[ALL MEMBERS]',NULL,NULL,1,10,'wv4Q3dhr56vqkqq','SUBMITTED','RECEIPT','7243283969068714736',1),(35,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Paid: \nTo STEPHEN KABUI, \nDetails: INSULIN, \nBank: JUDY WAINOI MPESA NO: 0721660482, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 4000.0','254718845069','[ALL MEMBERS]',NULL,NULL,1,11,'C2xQdqnJJqenqvz','SUBMITTED','RECEIPT','5814899746144929128',1),(36,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Paid: \nTo STEPHEN KABUI, \nDetails: INSULIN, \nBank: JUDY WAINOI MPESA NO: 0721660482, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 4000.0','254759065744','[ALL MEMBERS]',NULL,NULL,1,11,'Q8E4by1bQlcsz2M','SUBMITTED','RECEIPT','5814899746144929128',1),(37,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Paid: \nTo STEPHEN KABUI, \nDetails: INSULIN, \nBank: JUDY WAINOI MPESA NO: 0721660482, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 4000.0','254710303973','[ALL MEMBERS]',NULL,NULL,1,11,'QUqVSiKeiEhhl9t','SUBMITTED','RECEIPT','5814899746144929128',1),(38,'Wed 17th Apr 2024 07:57 PM',NULL,NULL,'Paid: \nTo STEPHEN KABUI, \nDetails: INSULIN, \nBank: JUDY WAINOI MPESA NO: 0721660482, \nRef: SAF33H6JYJ.\n\nTotal Amount: KES 4000.0','254710443317','[ALL MEMBERS]',NULL,NULL,1,11,'mfN4fiJYNktesjx','SUBMITTED','RECEIPT','5814899746144929128',1),(39,'Wed 17th Apr 2024 09:19 PM',NULL,NULL,'Dear Erick  Muriithi  Wanjohi, your payment has been processed. Basic Income: 50,000, Gross Income: 50,000. Please confirm receipt. Thank you!','254759065744','[ALL MEMBERS]',NULL,NULL,1,12,'wmCLPRtLvdZB1O7','SUBMITTED','RECEIPT','8246625176078074736',1),(40,'Wed 17th Apr 2024 09:19 PM',NULL,NULL,'Dear PETER  MAINA  MURIITHI, your payment has been processed. Basic Income: 70,000, Gross Income: 70,000. Please confirm receipt. Thank you!','254710443317','[ALL MEMBERS]',NULL,NULL,1,13,'uSqHYRpiyz1mGzY','SUBMITTED','RECEIPT','3550918080897371976',1),(41,'Wed 17th Apr 2024 10:05 PM',NULL,NULL,'Dear Erick  Muriithi  Wanjohi, \nyour payment has been processed. \nBasic Income: 50,000, \nGross Income: 50,000. \nPlease confirm receipt. Thank you!','254759065744','[ALL MEMBERS]',NULL,NULL,1,14,'I7WYIWS9WE8wAI5','SUBMITTED','RECEIPT','1520650437720621200',1),(42,'Wed 17th Apr 2024 10:05 PM',NULL,NULL,'Dear PETER  MAINA  MURIITHI, \nyour payment has been processed. \nBasic Income: 70,000, \nGross Income: 70,000. \nPlease confirm receipt. Thank you!','254710443317','[ALL MEMBERS]',NULL,NULL,1,15,'ygNeDyHkypEA8sF','SUBMITTED','RECEIPT','3317584540473969024',1),(43,'Wed 17th Apr 2024 10:05 PM',NULL,NULL,'Dear Erick  Muriithi  Wanjohi, \nyour payment has been processed. \nBasic Income: 50,000, \nGross Income: 50,000. \nPlease confirm receipt. Thank you!','254759065744','[ALL MEMBERS]',NULL,NULL,1,16,'k9anHLofcDu3zlA','SUBMITTED','RECEIPT','3713477068806769878',1),(44,'Wed 17th Apr 2024 10:05 PM',NULL,NULL,'Dear PETER  MAINA  MURIITHI, \nyour payment has been processed. \nBasic Income: 70,000, \nGross Income: 70,000. \nPlease confirm receipt. Thank you!','254710443317','[ALL MEMBERS]',NULL,NULL,1,17,'2Gw65VMLXs6hpyY','SUBMITTED','RECEIPT','6599615027503777437',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin@gmail.com','static/fgck/MemberImages/full-gospel.jpg',241,'$2a$10$3iPbdhq.0eUM.2zyDZadeuVPiCBPsTkOx6IJsIttNmPZDN6YgljZu','AAE81EA2990FEDD0070F3CC6158E0470',NULL,1,'admin',1,'0759065744'),(2,'wanjohijudy4@gmail.com',NULL,3,'$2a$10$3iPbdhq.0eUM.2zyDZadeuVPiCBPsTkOx6IJsIttNmPZDN6YgljZu','B6487D795BEB4F5371B3971D4ECFAAD9',NULL,1,'judy',1,'0718845069'),(3,'anthonykaroki69@gmail.com',NULL,144,'$2a$10$3iPbdhq.0eUM.2zyDZadeuVPiCBPsTkOx6IJsIttNmPZDN6YgljZu','931BED9E594A9B54A9A347B907D4325D',NULL,1,'anthony',1,'0710303973'),(4,'maishpeter22@gmail.com',NULL,0,'$2a$10$3iPbdhq.0eUM.2zyDZadeuVPiCBPsTkOx6IJsIttNmPZDN6YgljZu',NULL,NULL,1,'maish',1,'0710443317'),(5,'admin2@church.com',NULL,24,'$2a$10$t7eF.2e2xezNSX8TlG7iJuBVaEMav2UiH4IQRQsC6LAfLMPORQfR2','D52F7B7FCB81F92E31213829C4FDBBF8',NULL,1,'admin@2',2,NULL),(6,'admin3@church.com',NULL,115,'$2a$10$ey2eDAYhMS5s57RM5H1kduM1.jmLhAUupd6/r3/KOdzPLre/x0wCu','8DA2DDE98C554C401C40176A28512503',NULL,1,'admin@3',3,NULL),(7,'erick@stand',NULL,0,'$2a$10$QUvAY5Boctl8YU06PHYXSuIZcR1ayXBMsEB8zutxaWKD65CnNcx5q',NULL,NULL,1,'erick@st',3,NULL),(9,'admin@standrews',NULL,7,'$2a$10$/UWsPbhFkq8Z/0/poYnEzOyTFbbbf85nv8PLDJF/2OXFFwmXAd7j.','243FFDD50BF88F41F3B130D047E2A10C',NULL,1,'admin@standrews',3,NULL),(10,'registration@standrews',NULL,36,'$2a$10$OOndEvCyn/swO5B/FOitiej1Jxr6iJzITfvLaOLIz5hMr5vKUsDnm','625DB2B90A403AB5A987E4F4C744878A',NULL,0,'registration@standrews',3,NULL);
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
INSERT INTO `vendors` VALUES (1,'5','RICKIEY N GAMBO','Erick Wanjohi','wanjohierick07@gmail.com',1,'OTHERS','A34FRTEER45332','0759065744','2024-04-17 20:36:48.000000');
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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voucher_signatory`
--

LOCK TABLES `voucher_signatory` WRITE;
/*!40000 ALTER TABLE `voucher_signatory` DISABLE KEYS */;
INSERT INTO `voucher_signatory` VALUES (1,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,1),(2,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,2),(3,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,3),(4,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,4),(5,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,5),(6,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,6),(7,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,7),(8,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,8),(9,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,9),(10,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,10),(11,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,11),(12,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,12),(13,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,13),(14,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,14),(15,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,15),(16,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,16),(17,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,17),(18,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,18),(19,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,19),(20,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,20),(21,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,21),(22,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,22),(23,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,23),(24,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,24),(25,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,25),(26,1,'2024-04-17',1,1,'2024-04-17','2024-04-17','2024-04-17',1,26);
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

-- Dump completed on 2024-04-18  9:53:23
