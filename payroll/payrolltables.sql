/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;


alter table farm.farms
    add Employercode varchar(300) default '' null;

alter table farm.farms
    add TaxRelief double default 0 null;


create table farm.subregistration
(
    id                  int auto_increment
        primary key,
    farm              int          default 1        not null,
    biometricsid        int          default 1        not null,
    `FIRST NAME`        varchar(300) default ''       null,
    Idnu                varchar(300) default ''       null,
    `SECOND NAME`       varchar(1300)                 null,
    SURNAME             varchar(300) default ''       null,
    `ID NU`             varchar(300) default ''       null,
    GENDER              varchar(300) default ''       null,
    `DATE OF JOINING`   date                          null,
    `PHONE NU`          varchar(300) default ''       null,
    EMAIL               varchar(300) default ''       null,
    RESIDENCE           varchar(300) default ''       null,
    RELIGION            varchar(300) default ''       null,
    `MEDICAL CONDITION` varchar(300) default ''       null,
    SIMAGE              varchar(300) default ''       null,
    Position            varchar(300) default ''       null,
    OCCUPATION          varchar(300) default ''       null,
    BLOODGROUP          varchar(300) default ''       null,
    ReasonForRemoval    varchar(300) default ''       null,
    whoapproved         varchar(300) default ''       null,
    `BOOKS BORROWED`    varchar(300) default ''       null,
    `BOOK RETURNED`     varchar(300) default ''       null,
    RemovalDate         date                          null,
    Status              varchar(300) default 'Active' null,
    currentstatus       varchar(300) default 'In'     null,
    gadgetstatus        int          default 0        null,
    constraint FK_subregistration_churchdetails
        foreign key (farm) references farm.farms (churchid)
            on update cascade on delete cascade
);

create table farm.teacherregistration
(
    id                int auto_increment
        primary key,
    farm            int           default 0            not null,
    biometricsid      int           default 0            not null,
    Trnu              varchar(300)  default ''           null,
    `TSC NU`          varchar(300)  default ''           null,
    `FIRST NAME`      varchar(1300) default ''           null,
    `SECOND NAME`     varchar(1300) default ''           null,
    SURNAME           varchar(300)  default ''           null,
    GENDER            varchar(300)  default 'Female'     null,
    `DATE OF JOINING` date          default '2023-01-01' null,
    `PHONE NU`        varchar(300)  default ''           null,
    EMAIL             varchar(300)  default ''           null,
    RESIDENCE         varchar(300)  default ''           null,
    RELIGION          varchar(300)  default ''           null,
    UPI               varchar(300)  default ''           null,
    ADDRESS           varchar(300)  default ''           null,
    COUNTY            varchar(300)  default ''           null,
    `SUB COUNTY`      varchar(300)  default ''           null,
    CONSTITUENCY      varchar(300)  default ''           null,
    WARD              varchar(300)  default ''           null,
    SERVICE           varchar(500)  default ''           null,
    KRA               varchar(300)  default ''           null,
    idnumber          varchar(300)  default ''           null,
    TITLE             varchar(300)  default ''           null,
    MARITAL           varchar(300)  default ''           null,
    ETHNICITY         varchar(300)  default ''           null,
    SIMAGE            varchar(3000) default ''           null,
    MAINSUB           varchar(3000) default ''           null,
    PASSWORD          varchar(3000) default '1234@#'     null,
    status            varchar(3000) default 'Active'     null,
    json              varchar(255)  default ''           null,
    currentstatus     varchar(255)  default 'In'         null,
    gadgetstatus      int           default 0            null,
    constraint EMAIL
        unique (EMAIL),
    constraint church_biometricsid
        unique (farm, biometricsid),
    constraint FK_teacherregistration_churchdetails
        foreign key (farm) references farm.farms (churchid)
            on update cascade on delete cascade
);




DROP TABLE IF EXISTS `allowances`;
CREATE TABLE IF NOT EXISTS `allowances`
(
    `PAY NO`    int(10) NOT NULL,
    `payroll`   int(10) NOT NULL,
    `ALLOWANCE` int(10)  DEFAULT NULL,
    `AMOUNT`    double   DEFAULT NULL,
    `date`      datetime DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `deductions`;
CREATE TABLE IF NOT EXISTS `deductions`
(
    `id`              int(11) NOT NULL AUTO_INCREMENT,
    `farm`          int(11) NOT NULL,
    `Deduction`       varchar(300)     DEFAULT NULL,
    `type`            int(11) NOT NULL DEFAULT 0,
    `calculationtype` int(11) NOT NULL DEFAULT 0,
    `costperunit`     double  NOT NULL DEFAULT 0,
    `visible`         varchar(10)      DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `church_Deduction` (`farm`, `Deduction`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 80
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `deductionsandearningsexpirydat`;
CREATE TABLE IF NOT EXISTS `deductionsandearningsexpirydat`
(
    `id`         int(11)      NOT NULL AUTO_INCREMENT,
    `payno`      int(11)      NOT NULL,
    `item`       int(11)      NOT NULL,
    `TYPE`       varchar(500) NOT NULL,
    `amount`     double      DEFAULT 0,
    `startdate`  date        DEFAULT NULL,
    `enddate`    date        DEFAULT NULL,
    `status`     varchar(50) DEFAULT 'Pending Approval',
    `reccurrent` int(11)     DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `payno_item_type` (`payno`, `item`, `TYPE`),
    CONSTRAINT `FK_deductionsandearningsexpirydat_employees` FOREIGN KEY (`payno`) REFERENCES `employees` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 16840
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `earnings`;
CREATE TABLE IF NOT EXISTS `earnings`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `farm`      int(11) NOT NULL,
    `earning`     varchar(300) DEFAULT '',
    `type`        int(11)      DEFAULT 0,
    `costperunit` double       DEFAULT 0,
    `visible`     varchar(10)  DEFAULT '',
    PRIMARY KEY (`id`),
    UNIQUE KEY `church_earning` (`farm`, `earning`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 44
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `employees`;
CREATE TABLE IF NOT EXISTS `employees`
(
    `id`              int(11) NOT NULL AUTO_INCREMENT,
    `farm`          int(11)       DEFAULT NULL,
    `payno`           varchar(50)   DEFAULT '',
    `employeetype`    varchar(50)   DEFAULT '',
    `fname`           varchar(50)   DEFAULT '',
    `sname`           varchar(1000) DEFAULT '',
    `surname`         varchar(50)   DEFAULT '',
    `bankname`        varchar(500)  DEFAULT '',
    `accountnumber`   varchar(500)  DEFAULT '',
    `idnu`            varchar(1000) DEFAULT '',
    `kra pin`         varchar(50)   DEFAULT '',
    `linktoteacher`   varchar(50)   DEFAULT '',
    `linktostaff`     varchar(50)   DEFAULT '',
    `dob`             varchar(500)  DEFAULT '',
    `gender`          varchar(50)   DEFAULT 'Male',
    `welfare`         varchar(50)   DEFAULT '',
    `sacco`           varchar(50)   DEFAULT '',
    `nssf`            int(11)       DEFAULT 0,
    `nssf number`     varchar(50)   DEFAULT '',
    `nhif`            int(11)       DEFAULT 0,
    `nhif number`     varchar(50)   DEFAULT '',
    `helb`            int(11)       DEFAULT 0,
    `paymeth`         varchar(500)  DEFAULT '',
    `branchname`      varchar(500)  DEFAULT '',
    `accounttype`     varchar(500)  DEFAULT '',
    `accholdername`   varchar(500)  DEFAULT '',
    `phone`           varchar(500)  DEFAULT '',
    `raddress`        varchar(500)  DEFAULT '',
    `postaladdress`   varchar(500)  DEFAULT '',
    `postalcode`      varchar(500)  DEFAULT '',
    `paye`            int(11)       DEFAULT 0,
    `streetnum`       varchar(500)  DEFAULT '',
    `streetname`      varchar(500)  DEFAULT '',
    `city`            varchar(500)  DEFAULT '',
    `email`           varchar(500)  DEFAULT '',
    `pay cycle`       varchar(500)  DEFAULT 'MONTHLY',
    `housed`          int(11)       DEFAULT 0,
    `housinglev`      int(11)       DEFAULT 1,
    `occupation`      varchar(500)  DEFAULT '',
    `nextofkin`       varchar(500)  DEFAULT '',
    `nextofkinnumber` varchar(500)  DEFAULT '',
    `startdate`       date          DEFAULT '2023-01-01',
    `status`          varchar(500)  DEFAULT 'Pending Approval',
    `SIMAGE`          varchar(500)  DEFAULT '',
    `enddate`         date          DEFAULT '2030-01-01',
    PRIMARY KEY (`id`),
    UNIQUE KEY `church_payno` (`farm`, `payno`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 538
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `housing`;
CREATE TABLE IF NOT EXISTS `housing`
(
    `id`                int(11) NOT NULL AUTO_INCREMENT,
    `farm`            int(11) NOT NULL DEFAULT 0,
    `low`               int(11) NOT NULL DEFAULT 0,
    `top`               int(11) NOT NULL DEFAULT 0,
    `ratio`             double  NOT NULL DEFAULT 0,
    `amount`            double  NOT NULL DEFAULT 0,
    `nssfoptionperband` int(11) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `kplcrates`;
CREATE TABLE IF NOT EXISTS `kplcrates`
(
    `id`     int(11) NOT NULL AUTO_INCREMENT,
    `farm` int(11) DEFAULT 0,
    `rate`   double  DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `krarates`;
CREATE TABLE IF NOT EXISTS `krarates`
(
    `id`     int(10) unsigned NOT NULL AUTO_INCREMENT,
    `farm` int(11)                   DEFAULT 0,
    `low`    int(11)          NOT NULL DEFAULT 0,
    `top`    int(11)          NOT NULL DEFAULT 0,
    `ratio`  double           NOT NULL DEFAULT 0,
    `amount` double           NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `church_low_top` (`farm`, `low`, `top`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 32
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `monthly pays`;
CREATE TABLE IF NOT EXISTS `monthly pays`
(
    `ID`           int(11)   NOT NULL AUTO_INCREMENT,
    `payroll`      int(11)   NOT NULL DEFAULT 0,
    `PAYNO`        int(11)   NOT NULL DEFAULT 0,
    `MONTH`        int(11)   NOT NULL DEFAULT 0,
    `YEAR`         int(11)   NOT NULL DEFAULT 0,
    `NET SALARY`   double    NOT NULL DEFAULT 0,
    `GROSS SALARY` double    NOT NULL DEFAULT 0,
    `TAXABLE`      double    NOT NULL DEFAULT 0,
    `NHIF`         double    NOT NULL DEFAULT 0,
    `NSSF`         double    NOT NULL DEFAULT 0,
    `HELB`         double    NOT NULL DEFAULT 0,
    `KRA TAX`      double    NOT NULL DEFAULT 0,
    `HOUSING`      double    NOT NULL DEFAULT 0,
    `PAID ON`      timestamp NOT NULL DEFAULT current_timestamp(),
    `HOUSE`        varchar(50)        DEFAULT '',
    `status`       varchar(50)        DEFAULT 'Pending Approval',
    PRIMARY KEY (`ID`),
    UNIQUE KEY `payroll_PAYNO_MONTH_YEAR` (`payroll`, `PAYNO`, `MONTH`, `YEAR`),
    CONSTRAINT `FK_monthly pays_payrolls` FOREIGN KEY (`payroll`) REFERENCES `payrolls` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 28280
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `nhif`;
CREATE TABLE IF NOT EXISTS `nhif`
(
    `id`     int(11) NOT NULL AUTO_INCREMENT,
    `farm` int(11) NOT NULL DEFAULT 0,
    `low`    int(11) NOT NULL DEFAULT 0,
    `top`    int(11) NOT NULL DEFAULT 0,
    `ratio`  double  NOT NULL DEFAULT 0,
    `amount` double  NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `church_low_top` (`farm`, `low`, `top`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 154
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `nssf`;
CREATE TABLE IF NOT EXISTS `nssf`
(
    `id`                int(11) NOT NULL AUTO_INCREMENT,
    `farm`            int(11) NOT NULL DEFAULT 0,
    `low`               int(11) NOT NULL DEFAULT 0,
    `top`               int(11) NOT NULL DEFAULT 0,
    `ratio`             double  NOT NULL DEFAULT 0,
    `amount`            double  NOT NULL DEFAULT 0,
    `nssfoptionperband` int(11) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `church_low_top` (`farm`, `low`, `top`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 29
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `other deductions`;
CREATE TABLE IF NOT EXISTS `other deductions`
(
    `PAYNO`     int(10) NOT NULL,
    `payroll`   int(10) NOT NULL,
    `DEDUCTION` int(10) NOT NULL,
    `AMOUNT`    double  NOT NULL DEFAULT 0,
    `date`      datetime         DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `payrollpayments`;
CREATE TABLE IF NOT EXISTS `payrollpayments`
(
    `id`      int(11)      NOT NULL AUTO_INCREMENT,
    `payroll` int(11)      NOT NULL,
    `bank`    varchar(500) NOT NULL DEFAULT '',
    `amount`  double       NOT NULL DEFAULT 0,
    `transnu` varchar(100) NOT NULL DEFAULT '',
    `date`    date         NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `payroll_bank` (`payroll`, `bank`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 26
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `payrolls`;
CREATE TABLE IF NOT EXISTS `payrolls`
(
    `id`     int(11) NOT NULL AUTO_INCREMENT,
    `farm` int(11) NOT NULL DEFAULT 0,
    `date`   date    NOT NULL,
    `status` varchar(50)      DEFAULT 'Pending approval',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 148
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `readingbasedallowances`;
CREATE TABLE IF NOT EXISTS `readingbasedallowances`
(
    `id`         int(11) NOT NULL AUTO_INCREMENT,
    `item`       int(11) DEFAULT 0,
    `employeeid` int(11) DEFAULT 0,
    `month`      int(11) DEFAULT 0,
    `year`       int(11) DEFAULT 0,
    `readings`   double  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `item_employeeid_month_year` (`item`, `employeeid`, `month`, `year`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `readingbaseddeductions`;

CREATE TABLE IF NOT EXISTS farm.`leave`
(
    id       int auto_increment
        primary key,
    payno    varchar(50)                             not null,
    category varchar(500) default ''                 not null,
    `from`   date                                    null,
    `to`     date                                    null,
    comments varchar(500) default ''                 null,
    status   varchar(500) default 'Pending Approval' null
);


CREATE TABLE IF NOT EXISTS `readingbaseddeductions`
(
    `id`         int(11) NOT NULL AUTO_INCREMENT,
    `item`       int(11) DEFAULT 0,
    `employeeid` int(11) DEFAULT 0,
    `month`      int(11) DEFAULT 0,
    `year`       int(11) DEFAULT 0,
    `readings`   double  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `item_employeeid_month_year` (`item`, `employeeid`, `month`, `year`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 129
  DEFAULT CHARSET = latin1
  COLLATE = latin1_swedish_ci;

/*!40103 SET TIME_ZONE = IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE = IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS = IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES = IFNULL(@OLD_SQL_NOTES, 1) */;
