GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'callback'@'%' IDENTIFIED BY 'callback';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'cdr'@'%' IDENTIFIED BY 'cdr';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'coreengine'@'%' IDENTIFIED BY 'coreengine';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, ALTER, SHOW DATABASES, EXECUTE ON *.* TO 'ivrsecurity'@'%' IDENTIFIED BY 'ivrsecurity';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'scheduler'@'%' IDENTIFIED BY 'scheduler';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'billing'@'%' IDENTIFIED BY 'billing';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'subsengine'@'%' IDENTIFIED BY 'subsengine';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'subsrenewal'@'%' IDENTIFIED BY 'subsrenewal';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'subsretry'@'%' IDENTIFIED BY 'subsretry';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'subsstatechanger'@'%' IDENTIFIED BY 'subsstatechanger';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'subsmsgs'@'%' IDENTIFIED BY 'subsmsgs';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'smsclient'@'%' IDENTIFIED BY 'sms@123';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'callback'@'localhost' IDENTIFIED BY 'callback';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'cdr'@'localhost' IDENTIFIED BY 'cdr';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'coreengine'@'localhost' IDENTIFIED BY 'coreengine';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, ALTER, SHOW DATABASES, EXECUTE ON *.* TO 'ivrsecurity'@'localhost' IDENTIFIED BY 'ivrsecurity';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'scheduler'@'localhost' IDENTIFIED BY 'scheduler';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'billing'@'localhost' IDENTIFIED BY 'billing';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'subsengine'@'localhost' IDENTIFIED BY 'subsengine';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'subsrenewal'@'localhost' IDENTIFIED BY 'subsrenewal';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'subsretry'@'localhost' IDENTIFIED BY 'subsretry';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'subsstatechanger'@'localhost' IDENTIFIED BY 'subsstatechanger';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'subsmsgs'@'localhost' IDENTIFIED BY 'subsmsgs';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, SHOW DATABASES, EXECUTE ON *.* TO 'smsclient'@'localhost' IDENTIFIED BY 'sms@123';
FLUSH PRIVILEGES;

/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - billing_engine
*********************************************************************
Server version : 5.5.34-log
*/


/* Variable Declaration*/

Set @operatorname='airtel';
Set @countryname='india';
Set @circle='';
Set @countrycode='011';
Set @systemip='127.0.0.1';
Set @msisdnlength='10';
Set @currency='INR';


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `billing_engine`;

USE `billing_engine`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `billingrespcodesndesc` */

DROP TABLE IF EXISTS `billingrespcodesndesc`;

CREATE TABLE `billingrespcodesndesc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `respCode` varchar(10) DEFAULT NULL,
  `respDesc` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

/*Table structure for table `circlemapper` */

DROP TABLE IF EXISTS `circlemapper`;

CREATE TABLE `circlemapper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `circle` varchar(15) DEFAULT NULL,
  `circleKey` varchar(3) DEFAULT NULL,
  `country` varchar(20) DEFAULT NULL,
  `countryKey` varchar(3) DEFAULT NULL,
  `defLang` varchar(3) DEFAULT NULL,
  `operator` varchar(15) DEFAULT NULL,
  `series` int(11) NOT NULL,
  `timeZone` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1` (`series`,`circleKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `cpdetail` */

DROP TABLE IF EXISTS `cpdetail`;

CREATE TABLE `cpdetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cpName` varchar(50) NOT NULL,
  `callbackUrl` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1002 DEFAULT CHARSET=utf8;

/*Table structure for table `failurebillingtransactionlog` */

DROP TABLE IF EXISTS `failurebillingtransactionlog`;

CREATE TABLE `failurebillingtransactionlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `transactionId` varchar(20) NOT NULL,
  `transStartTime` datetime NOT NULL,
  `msisdn` varchar(25) NOT NULL,
  `chargingAmount` varchar(5) DEFAULT NULL,
  `requestType` varchar(50) NOT NULL,
  `serviceid` varchar(50) NOT NULL,
  `subServiceid` varchar(50) NOT NULL,
  `cpId` varchar(10) NOT NULL,
  `circleid` int(11) DEFAULT NULL,
  `transStatus` varchar(20) DEFAULT NULL,
  `protocol` varchar(10) DEFAULT NULL,
  `inRespCode` varchar(10) DEFAULT NULL,
  `inRespDesc` varchar(250) DEFAULT NULL,
  `beRespCode` varchar(10) DEFAULT NULL,
  `beRespdesc` varchar(50) DEFAULT NULL,
  `msisdnType` varchar(20) DEFAULT NULL,
  `shortCode` varchar(10) DEFAULT NULL,
  `subDays` varchar(3) DEFAULT NULL,
  `action` varchar(10) DEFAULT NULL,
  `inReqTime` datetime DEFAULT NULL,
  `inRespTime` datetime DEFAULT NULL,
  `transEndtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=460113602 DEFAULT CHARSET=utf8;

/*Table structure for table `innodeconfig` */

DROP TABLE IF EXISTS `innodeconfig`;

CREATE TABLE `innodeconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inNodeDetailsId` int(11) NOT NULL,
  `circle` varchar(3) DEFAULT NULL,
  `serverIp` varchar(50) DEFAULT NULL,
  `serverPort` int(5) DEFAULT NULL,
  `serviceUri` varchar(100) NOT NULL,
  `userAgent` varchar(100) NOT NULL,
  `authorizationKey` varchar(100) NOT NULL,
  `conTimeOut` varchar(10) NOT NULL,
  `maxConn` int(3) NOT NULL,
  `originNodeName` varchar(100) NOT NULL,
  `password` varchar(100) DEFAULT NULL,
  `accept` varchar(250) DEFAULT NULL,
  `contentType` varchar(250) DEFAULT NULL,
  `soapAction` varchar(250) DEFAULT NULL,
  `priority` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_InNodeConfig_idNodeConfigId` (`inNodeDetailsId`),
  CONSTRAINT `FK_InNodeConfig_idNodeConfigId` FOREIGN KEY (`inNodeDetailsId`) REFERENCES `innodedetails` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Table structure for table `innodedetails` */

DROP TABLE IF EXISTS `innodedetails`;

CREATE TABLE `innodedetails` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operator` varchar(15) DEFAULT NULL,
  `inType` varchar(15) DEFAULT NULL,
  `country` varchar(20) DEFAULT NULL,
  `isIN` int(2) NOT NULL,
  `protocol` varchar(15) DEFAULT NULL,
  `currency` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `innodereqnrespformat` */

DROP TABLE IF EXISTS `innodereqnrespformat`;

CREATE TABLE `innodereqnrespformat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inNodeConfigId` int(11) NOT NULL,
  `reqId` int(11) NOT NULL,
  `reqFormat` text,
  `respFormat` text,
  `mode` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_InNodeReqNRespFormat_inNodeConfigId` (`inNodeConfigId`),
  KEY `FK_InNodeReqNRespFormat_reqId` (`reqId`),
  CONSTRAINT `FK_InNodeReqNRespFormat_inNodeConfigId` FOREIGN KEY (`inNodeConfigId`) REFERENCES `innodeconfig` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_InNodeReqNRespFormat_reqId` FOREIGN KEY (`reqId`) REFERENCES `requesttype` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Table structure for table `inrespcodes` */

DROP TABLE IF EXISTS `inrespcodes`;

CREATE TABLE `inrespcodes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inNodeReqNRespFormatid` int(11) NOT NULL,
  `inRespCode` varchar(10) DEFAULT NULL,
  `billingRespCodesNDescId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_BillingRespCodesNDesc` (`inNodeReqNRespFormatid`),
  KEY `FK_BillingRespCodesNDesc_1` (`billingRespCodesNDescId`),
  CONSTRAINT `FK_BillingRespCodesNDesc_1` FOREIGN KEY (`billingRespCodesNDescId`) REFERENCES `billingrespcodesndesc` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_InNodeReqNRespFormat_1` FOREIGN KEY (`inNodeReqNRespFormatid`) REFERENCES `innodereqnrespformat` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

/*Table structure for table `ipwhitelist` */

DROP TABLE IF EXISTS `ipwhitelist`;

CREATE TABLE `ipwhitelist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cpDetailId` int(11) NOT NULL,
  `whitelistedIp` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_CpDetailId` (`cpDetailId`),
  CONSTRAINT `FK_CpDetailId` FOREIGN KEY (`cpDetailId`) REFERENCES `cpdetail` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `smswhitelist` */

DROP TABLE IF EXISTS `smswhitelist`;

CREATE TABLE `smswhitelist` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `shortcode` varchar(10) DEFAULT NULL,
  `msisdn` varchar(20) NOT NULL,
  `isSeries` int(1) NOT NULL DEFAULT '0',
  `msgType` varchar(2) NOT NULL DEFAULT 'MT',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `mobillingcallback` */

DROP TABLE IF EXISTS `mobillingcallback`;

CREATE TABLE `mobillingcallback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `transactionId` varchar(500) DEFAULT NULL,
  `callbackReq` text,
  `callbackResp` varchar(500) DEFAULT NULL,
  `smsXml` text,
  `smsResp` varchar(500) DEFAULT NULL,
  `transEndtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `requesttype` */

DROP TABLE IF EXISTS `requesttype`;

CREATE TABLE `requesttype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `successbillingtransactionlog` */

DROP TABLE IF EXISTS `successbillingtransactionlog`;

CREATE TABLE `successbillingtransactionlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `transactionId` varchar(20) NOT NULL,
  `transStartTime` datetime NOT NULL,
  `msisdn` varchar(25) NOT NULL,
  `chargingAmount` varchar(5) DEFAULT NULL,
  `requestType` varchar(50) NOT NULL,
  `serviceid` varchar(50) NOT NULL,
  `subServiceid` varchar(50) NOT NULL,
  `cpId` varchar(10) NOT NULL,
  `circleid` int(11) DEFAULT NULL,
  `transStatus` varchar(20) DEFAULT NULL,
  `protocol` varchar(10) DEFAULT NULL,
  `inRespCode` varchar(10) DEFAULT NULL,
  `inRespDesc` varchar(10) DEFAULT NULL,
  `beRespCode` varchar(10) DEFAULT NULL,
  `beRespdesc` varchar(10) DEFAULT NULL,
  `msisdnType` varchar(20) DEFAULT NULL,
  `shortCode` varchar(10) DEFAULT NULL,
  `subDays` varchar(3) DEFAULT NULL,
  `action` varchar(10) DEFAULT NULL,
  `inReqTime` datetime DEFAULT NULL,
  `inRespTime` datetime DEFAULT NULL,
  `transEndtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53586900 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;



/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - cdrlog
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `cdrlog`;

USE `cdrlog`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `base` */

DROP TABLE IF EXISTS `base`;

CREATE TABLE `base` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `report_date` date DEFAULT NULL,
  `service` varchar(30) DEFAULT NULL,
  `active_base` int(11) DEFAULT NULL,
  `pending_base` int(11) DEFAULT NULL,
  `grace_base` int(11) DEFAULT NULL,
  `parking_base` int(11) DEFAULT NULL,
  `suspended_base` int(11) DEFAULT NULL,
  `total_base` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `billing_logs` */

DROP TABLE IF EXISTS `billing_logs`;

CREATE TABLE `billing_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `master_id` varchar(50) NOT NULL,
  `bs_node` varchar(30) DEFAULT NULL,
  `request` varchar(30) DEFAULT NULL,
  `billing_type` varchar(30) DEFAULT NULL,
  `request_type` int(3) DEFAULT NULL,
  `msisdn` varchar(30) DEFAULT NULL,
  `msisdn_type` varchar(30) DEFAULT NULL,
  `user_accbal` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `currency` varchar(30) DEFAULT NULL,
  `req_status` varchar(30) DEFAULT NULL,
  `be_respcode` varchar(100) DEFAULT NULL,
  `be_respdesc` varchar(30) DEFAULT NULL,
  `transaction_id` varchar(30) DEFAULT NULL,
  `be_mode` varchar(30) DEFAULT NULL,
  `be_reqrecvtime` datetime DEFAULT NULL,
  `be_respsenttime` datetime DEFAULT NULL,
  `be_duration` int(11) DEFAULT NULL,
  `protocol` varchar(30) DEFAULT NULL,
  `srv_name` varchar(30) DEFAULT NULL,
  `srv_key` varchar(30) DEFAULT NULL,
  `channel` varchar(30) DEFAULT NULL,
  `cp_id` varchar(30) DEFAULT NULL,
  `cp_name` varchar(30) DEFAULT NULL,
  `operator` varchar(30) DEFAULT NULL,
  `circle` varchar(30) DEFAULT NULL,
  `country` varchar(30) DEFAULT NULL,
  `time_offset` int(11) DEFAULT NULL,
  `client_ip` varchar(30) DEFAULT NULL,
  `be_sysip` varchar(30) DEFAULT NULL,
  `bs_ip` varchar(30) DEFAULT NULL,
  `bs_port` varchar(30) DEFAULT NULL,
  `bs_srvurl` text,
  `bs_reqsenttime` datetime DEFAULT NULL,
  `bs_resprecvtime` datetime DEFAULT NULL,
  `bs_duration` int(11) DEFAULT NULL,
  `bs_respcode` varchar(100) DEFAULT NULL,
  `bs_respdesc` text,
  `bs_transid` varchar(100) DEFAULT NULL,
  `bs_mode` varchar(30) DEFAULT NULL,
  `call_backurl` text,
  `call_backresp` varchar(100) DEFAULT NULL,
  `action` varchar(30) DEFAULT NULL,
  `callback_recv_time` datetime DEFAULT NULL,
  `callback_resp_time` datetime DEFAULT NULL,
  `additional_info` text,
  `user_profile_respcode` varchar(50) DEFAULT NULL,
  `user_profile_respdesc` varchar(50) DEFAULT NULL,
  `user_profile_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `master_id_UNIQUE` (`master_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `billing_logs_temp` */

DROP TABLE IF EXISTS `billing_logs_temp`;

CREATE TABLE `billing_logs_temp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `master_id` varchar(50) NOT NULL,
  `bs_node` varchar(30) DEFAULT NULL,
  `request` varchar(30) DEFAULT NULL,
  `billing_type` varchar(30) DEFAULT NULL,
  `request_type` int(3) DEFAULT NULL,
  `msisdn` varchar(30) DEFAULT NULL,
  `msisdn_type` varchar(30) DEFAULT NULL,
  `user_accbal` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `currency` varchar(30) DEFAULT NULL,
  `req_status` varchar(30) DEFAULT NULL,
  `be_respcode` varchar(100) DEFAULT NULL,
  `be_respdesc` varchar(30) DEFAULT NULL,
  `transaction_id` varchar(30) DEFAULT NULL,
  `be_mode` varchar(30) DEFAULT NULL,
  `be_reqrecvtime` datetime DEFAULT NULL,
  `be_respsenttime` datetime DEFAULT NULL,
  `be_duration` int(11) DEFAULT NULL,
  `protocol` varchar(30) DEFAULT NULL,
  `srv_name` varchar(30) DEFAULT NULL,
  `srv_key` varchar(30) DEFAULT NULL,
  `channel` varchar(30) DEFAULT NULL,
  `cp_id` varchar(30) DEFAULT NULL,
  `cp_name` varchar(30) DEFAULT NULL,
  `operator` varchar(30) DEFAULT NULL,
  `circle` varchar(30) DEFAULT NULL,
  `country` varchar(30) DEFAULT NULL,
  `time_offset` int(11) DEFAULT NULL,
  `client_ip` varchar(30) DEFAULT NULL,
  `be_sysip` varchar(30) DEFAULT NULL,
  `bs_ip` varchar(30) DEFAULT NULL,
  `bs_port` varchar(30) DEFAULT NULL,
  `bs_srvurl` varchar(500) DEFAULT NULL,
  `bs_reqsenttime` datetime DEFAULT NULL,
  `bs_resprecvtime` datetime DEFAULT NULL,
  `bs_duration` int(11) DEFAULT NULL,
  `bs_respcode` varchar(100) DEFAULT NULL,
  `bs_respdesc` varchar(500) DEFAULT NULL,
  `bs_transid` varchar(100) DEFAULT NULL,
  `bs_mode` varchar(30) DEFAULT NULL,
  `call_backurl` varchar(1500) DEFAULT NULL,
  `call_backresp` varchar(100) DEFAULT NULL,
  `action` varchar(30) DEFAULT NULL,
  `callback_recv_time` datetime DEFAULT NULL,
  `callback_resp_time` datetime DEFAULT NULL,
  `additional_info` varchar(500) DEFAULT NULL,
  `user_profile_respcode` varchar(50) DEFAULT NULL,
  `user_profile_respdesc` varchar(50) DEFAULT NULL,
  `user_profile_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `master_id_UNIQUE` (`master_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `cci_login_details` */

DROP TABLE IF EXISTS `cci_login_details`;

CREATE TABLE `cci_login_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(40) NOT NULL,
  `password` varchar(40) NOT NULL,
  `accessType` varchar(2) NOT NULL DEFAULT 'E',
  `user_status` int(1) NOT NULL DEFAULT '1',
  `lastLoginTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `menu_permissions` varchar(20) NOT NULL DEFAULT '1_1_1_1_1',
  `service_permissions` varchar(20) NOT NULL DEFAULT '_1_2_3_',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

/*Table structure for table `content_report` */

DROP TABLE IF EXISTS `content_report`;

CREATE TABLE `content_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reportdate` date DEFAULT NULL,
  `master_id` varchar(30) DEFAULT NULL,
  `a_party` varchar(30) DEFAULT NULL,
  `media_file` text,
  `duration` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86029 DEFAULT CHARSET=utf8;

/*Table structure for table `content_report_new` */

DROP TABLE IF EXISTS `content_report_new`;

CREATE TABLE `content_report_new` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `content_type` varchar(30) NOT NULL,
  `sub_type` varchar(30) NOT NULL,
  `total` decimal(50,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10455 DEFAULT CHARSET=utf8;

/*Table structure for table `currency_mapping` */

DROP TABLE IF EXISTS `currency_mapping`;

CREATE TABLE `currency_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `yearmonth` varchar(6) DEFAULT NULL,
  `one_usd` decimal(11,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

/*Table structure for table `daily_report_new` */

DROP TABLE IF EXISTS `daily_report_new`;

CREATE TABLE `daily_report_new` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ReportDate` date DEFAULT NULL,
  `ServiceName` varchar(50) DEFAULT NULL,
  `Summary` varchar(50) DEFAULT NULL,
  `Sub_Service_Name` varchar(50) DEFAULT NULL,
  `Type` varchar(50) DEFAULT NULL,
  `type_sub_param` varchar(100) DEFAULT NULL,
  `Price` varchar(50) DEFAULT NULL,
  `Count` decimal(50,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=259382 DEFAULT CHARSET=utf8;

/*Table structure for table `dailyreportver02` */

DROP TABLE IF EXISTS `dailyreportver02`;

CREATE TABLE `dailyreportver02` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ReportDate` date DEFAULT NULL,
  `ServiceName` varchar(50) DEFAULT NULL,
  `Summary` varchar(50) DEFAULT NULL,
  `Sub_Service_Name` varchar(50) DEFAULT NULL,
  `Type` varchar(50) DEFAULT NULL,
  `type_sub_param` varchar(50) DEFAULT NULL,
  `Price` varchar(50) DEFAULT NULL,
  `Count` decimal(50,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=317506 DEFAULT CHARSET=utf8;

/*Table structure for table `dtmf_mastercalllogs` */

DROP TABLE IF EXISTS `dtmf_mastercalllogs`;

CREATE TABLE `dtmf_mastercalllogs` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MASTER_ID` varchar(255) NOT NULL,
  `CALL_START_DATETIME` datetime DEFAULT NULL,
  `DTMF_INPUT_DATETIME` datetime DEFAULT NULL,
  `DTMF_INPUT_DURATION` int(11) DEFAULT NULL,
  `DTMF_INPUT_PULSES` int(6) DEFAULT NULL,
  `DTMF_INPUT_KEYS` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `dtmfkey` (`DTMF_INPUT_KEYS`)
) ENGINE=InnoDB AUTO_INCREMENT=9003617 DEFAULT CHARSET=utf8;

/*Table structure for table `ivr_confcalllogs` */

DROP TABLE IF EXISTS `ivr_confcalllogs`;

CREATE TABLE `ivr_confcalllogs` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MASTER_ID` varchar(255) NOT NULL,
  `CONF_ID` varchar(20) DEFAULT NULL,
  `SYS_IP` varchar(20) DEFAULT NULL,
  `PLUGIN` varchar(20) DEFAULT NULL,
  `PROTOCOL` varchar(15) DEFAULT NULL,
  `CALL_TYPE` varchar(15) DEFAULT NULL,
  `DIRECT_CALL` varchar(10) DEFAULT NULL,
  `A_PARTY` varchar(25) DEFAULT NULL,
  `B_PARTY` varchar(25) DEFAULT NULL,
  `DIAL_DATETIME` datetime DEFAULT NULL,
  `START_DATETIME` datetime DEFAULT NULL,
  `END_DATETIME` datetime DEFAULT NULL,
  `DURATION` int(11) DEFAULT NULL,
  `PULSES` int(11) DEFAULT NULL,
  `SERVICE` varchar(50) DEFAULT NULL,
  `CIRCLE` varchar(10) DEFAULT NULL,
  `COUNTRY` varchar(10) DEFAULT NULL,
  `STATUS` varchar(15) DEFAULT NULL,
  `REASON` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `confcaller` (`A_PARTY`),
  KEY `confcalled` (`B_PARTY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ivr_confcalllogs_temp` */

DROP TABLE IF EXISTS `ivr_confcalllogs_temp`;

CREATE TABLE `ivr_confcalllogs_temp` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MASTER_ID` varchar(255) NOT NULL,
  `CONF_ID` varchar(20) DEFAULT NULL,
  `SYS_IP` varchar(20) DEFAULT NULL,
  `PLUGIN` varchar(20) DEFAULT NULL,
  `PROTOCOL` varchar(15) DEFAULT NULL,
  `CALL_TYPE` varchar(15) DEFAULT NULL,
  `DIRECT_CALL` varchar(10) DEFAULT NULL,
  `A_PARTY` varchar(25) DEFAULT NULL,
  `B_PARTY` varchar(25) DEFAULT NULL,
  `DIAL_DATETIME` datetime DEFAULT NULL,
  `START_DATETIME` datetime DEFAULT NULL,
  `END_DATETIME` datetime DEFAULT NULL,
  `DURATION` int(11) DEFAULT NULL,
  `PULSES` int(11) DEFAULT NULL,
  `SERVICE` varchar(50) DEFAULT NULL,
  `CIRCLE` varchar(10) DEFAULT NULL,
  `COUNTRY` varchar(10) DEFAULT NULL,
  `STATUS` varchar(15) DEFAULT NULL,
  `REASON` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `confcaller` (`A_PARTY`),
  KEY `confcalled` (`B_PARTY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ivr_mastercalllogs` */

DROP TABLE IF EXISTS `ivr_mastercalllogs`;

CREATE TABLE `ivr_mastercalllogs` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MASTER_ID` varchar(255) NOT NULL,
  `TPSYS_IP` varchar(20) DEFAULT NULL,
  `CESYS_IP` varchar(20) DEFAULT NULL,
  `MSC_IP` varchar(100) DEFAULT NULL,
  `LANGUAGE` varchar(20) DEFAULT NULL,
  `PLUGIN` varchar(20) DEFAULT NULL,
  `PROTOCOL` varchar(15) DEFAULT NULL,
  `CALL_TYPE` varchar(15) DEFAULT NULL,
  `CIC` int(8) DEFAULT NULL,
  `CALL_ID` int(10) DEFAULT NULL,
  `A_PARTY` varchar(20) DEFAULT NULL,
  `B_PARTY` varchar(20) DEFAULT NULL,
  `SHORT_CODE` varchar(20) DEFAULT NULL,
  `START_DATETIME` datetime DEFAULT NULL,
  `PICKUP_DATETIME` datetime DEFAULT NULL,
  `END_DATETIME` datetime DEFAULT NULL,
  `DURATION` int(11) DEFAULT NULL,
  `PULSES` int(11) DEFAULT NULL,
  `SERVICE` varchar(50) DEFAULT NULL,
  `CIRCLE` varchar(10) DEFAULT NULL,
  `OPERATOR` varchar(30) DEFAULT NULL,
  `TIMEZONE` varchar(6) DEFAULT NULL,
  `COUNTRY` varchar(10) DEFAULT NULL,
  `CALL_STATUS` varchar(15) DEFAULT NULL,
  `RLC_REASON` varchar(50) DEFAULT NULL,
  `SUB_STATUS` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `caller` (`A_PARTY`)
) ENGINE=InnoDB AUTO_INCREMENT=35554402 DEFAULT CHARSET=utf8;

/*Table structure for table `ivr_mastercalllogs_temp` */

DROP TABLE IF EXISTS `ivr_mastercalllogs_temp`;

CREATE TABLE `ivr_mastercalllogs_temp` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MASTER_ID` varchar(255) NOT NULL,
  `TPSYS_IP` varchar(20) DEFAULT NULL,
  `CESYS_IP` varchar(20) DEFAULT NULL,
  `MSC_IP` varchar(100) DEFAULT NULL,
  `LANGUAGE` varchar(20) DEFAULT NULL,
  `PLUGIN` varchar(20) DEFAULT NULL,
  `PROTOCOL` varchar(15) DEFAULT NULL,
  `CALL_TYPE` varchar(15) DEFAULT NULL,
  `CIC` int(8) DEFAULT NULL,
  `CALL_ID` int(10) DEFAULT NULL,
  `A_PARTY` varchar(20) DEFAULT NULL,
  `B_PARTY` varchar(20) DEFAULT NULL,
  `SHORT_CODE` varchar(20) DEFAULT NULL,
  `START_DATETIME` datetime DEFAULT NULL,
  `PICKUP_DATETIME` datetime DEFAULT NULL,
  `END_DATETIME` datetime DEFAULT NULL,
  `DURATION` int(11) DEFAULT NULL,
  `PULSES` int(11) DEFAULT NULL,
  `SERVICE` varchar(50) DEFAULT NULL,
  `CIRCLE` varchar(10) DEFAULT NULL,
  `OPERATOR` varchar(30) DEFAULT NULL,
  `TIMEZONE` varchar(6) DEFAULT NULL,
  `COUNTRY` varchar(10) DEFAULT NULL,
  `CALL_STATUS` varchar(15) DEFAULT NULL,
  `RLC_REASON` varchar(50) DEFAULT NULL,
  `SUB_STATUS` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `caller` (`A_PARTY`)
) ENGINE=InnoDB AUTO_INCREMENT=35554396 DEFAULT CHARSET=utf8;

/*Table structure for table `ivr_specialeffect` */

DROP TABLE IF EXISTS `ivr_specialeffect`;

CREATE TABLE `ivr_specialeffect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `master_id` varchar(50) DEFAULT NULL,
  `special_effect_time` datetime DEFAULT NULL,
  `special_effect_value` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `login` */

DROP TABLE IF EXISTS `login`;

CREATE TABLE `login` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `application` varchar(50) DEFAULT NULL,
  `authority` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `media_mastercalllogs` */

DROP TABLE IF EXISTS `media_mastercalllogs`;

CREATE TABLE `media_mastercalllogs` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MASTER_ID` varchar(255) NOT NULL,
  `START_DATETIME` datetime DEFAULT NULL,
  `END_DATETIME` datetime DEFAULT NULL,
  `DURATION` int(8) DEFAULT NULL,
  `PULSES` int(6) DEFAULT NULL,
  `MEDIA_TYPE` varchar(20) DEFAULT NULL,
  `media_file` varchar(200) DEFAULT NULL,
  `MEDIA_CODE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `mfile` (`media_file`),
  KEY `mcode` (`MEDIA_CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=18306235 DEFAULT CHARSET=utf8;

/*Table structure for table `newe1utilization` */

DROP TABLE IF EXISTS `newe1utilization`;

CREATE TABLE `newe1utilization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `mbh` int(11) DEFAULT NULL,
  `incoming_count` int(11) DEFAULT NULL,
  `outcoing_count` int(11) DEFAULT NULL,
  `conf_count` int(11) DEFAULT NULL,
  `incoming_mou` int(11) DEFAULT NULL,
  `outgoing_mou` int(11) DEFAULT NULL,
  `conf_mou` int(11) DEFAULT NULL,
  `total_mou` int(11) DEFAULT NULL,
  `channel_count` int(11) DEFAULT NULL,
  `erlang_capacity` int(11) DEFAULT NULL,
  `erlang` decimal(11,2) DEFAULT NULL,
  `utilization` decimal(11,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=626 DEFAULT CHARSET=latin1;

/*Table structure for table `smslogs` */

DROP TABLE IF EXISTS `smslogs`;

CREATE TABLE `smslogs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `master_id` varchar(50) NOT NULL,
  `sender` varchar(30) DEFAULT NULL,
  `receiver` varchar(30) DEFAULT NULL,
  `msg_id` varchar(30) DEFAULT NULL,
  `mode` varchar(30) DEFAULT NULL,
  `content` text,
  `status` text,
  `msg_type` varchar(30) DEFAULT NULL,
  `circle` varchar(30) DEFAULT NULL,
  `submit_time` datetime DEFAULT NULL,
  `response_time` datetime DEFAULT NULL,
  `service_type` varchar(30) DEFAULT NULL,
  `receive_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38611378 DEFAULT CHARSET=utf8;

/*Table structure for table `subscriberbase` */

DROP TABLE IF EXISTS `subscriberbase`;

CREATE TABLE `subscriberbase` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `report_date` date DEFAULT NULL,
  `service` varchar(30) DEFAULT NULL,
  `sub_service` varchar(30) DEFAULT NULL,
  `status` varchar(30) DEFAULT NULL,
  `base` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85223 DEFAULT CHARSET=utf8;

/*Table structure for table `subscription_logs` */

DROP TABLE IF EXISTS `subscription_logs`;

CREATE TABLE `subscription_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `master_id` varchar(50) NOT NULL,
  `request_type` int(3) DEFAULT NULL,
  `req_desc` varchar(30) DEFAULT NULL,
  `msisdn` varchar(30) DEFAULT NULL,
  `amount` varchar(30) DEFAULT NULL,
  `status` varchar(30) DEFAULT NULL,
  `resp_code` varchar(30) DEFAULT NULL,
  `resp_desc` varchar(50) DEFAULT NULL,
  `channel` varchar(30) DEFAULT NULL,
  `transaction_id` varchar(100) DEFAULT NULL,
  `se_mode` varchar(30) DEFAULT NULL,
  `be_mode` varchar(30) DEFAULT NULL,
  `sub_type` varchar(30) DEFAULT NULL,
  `sub_timeleft` int(11) DEFAULT NULL,
  `srv_name` varchar(30) DEFAULT NULL,
  `srv_id` varchar(30) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `srv_validity` int(11) DEFAULT NULL,
  `operator` varchar(30) DEFAULT NULL,
  `circle` varchar(30) DEFAULT NULL,
  `country` varchar(30) DEFAULT NULL,
  `time_offset` int(11) DEFAULT NULL,
  `retry_count` int(11) DEFAULT NULL,
  `user_status` varchar(30) DEFAULT NULL,
  `previous_state` varchar(30) DEFAULT NULL,
  `se_reqrecvtime` datetime DEFAULT NULL,
  `se_respsenttime` datetime DEFAULT NULL,
  `be_reqsenttime` datetime DEFAULT NULL,
  `be_resprecvtime` datetime DEFAULT NULL,
  `cp_id` varchar(30) DEFAULT NULL,
  `cp_name` varchar(30) DEFAULT NULL,
  `sub_srvname` varchar(30) DEFAULT NULL,
  `sub_srvid` varchar(30) DEFAULT NULL,
  `msg_senderid` varchar(30) DEFAULT NULL,
  `msg_text` text,
  `call_back_url` text,
  `call_back_resp` varchar(100) DEFAULT NULL,
  `client_ip` varchar(30) DEFAULT NULL,
  `se_sysIp` varchar(30) DEFAULT NULL,
  `language` varchar(30) DEFAULT NULL,
  `cp_callbackurlhittime` datetime DEFAULT NULL,
  `action` varchar(45) DEFAULT NULL,
  `alert` varchar(45) DEFAULT NULL,
  `notification_url` text,
  `notification_resp` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `master_id_UNIQUE` (`master_id`)
) ENGINE=InnoDB AUTO_INCREMENT=674893857 DEFAULT CHARSET=utf8;

/*Table structure for table `subscription_logs_temp` */

DROP TABLE IF EXISTS `subscription_logs_temp`;

CREATE TABLE `subscription_logs_temp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `master_id` varchar(50) NOT NULL,
  `request_type` int(3) DEFAULT NULL,
  `req_desc` varchar(30) DEFAULT NULL,
  `msisdn` varchar(30) DEFAULT NULL,
  `amount` varchar(30) DEFAULT NULL,
  `status` varchar(30) DEFAULT NULL,
  `resp_code` varchar(30) DEFAULT NULL,
  `resp_desc` varchar(50) DEFAULT NULL,
  `channel` varchar(30) DEFAULT NULL,
  `transaction_id` varchar(100) DEFAULT NULL,
  `se_mode` varchar(30) DEFAULT NULL,
  `be_mode` varchar(30) DEFAULT NULL,
  `sub_type` varchar(30) DEFAULT NULL,
  `sub_timeleft` int(11) DEFAULT NULL,
  `srv_name` varchar(30) DEFAULT NULL,
  `srv_id` varchar(30) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `srv_validity` int(11) DEFAULT NULL,
  `operator` varchar(30) DEFAULT NULL,
  `circle` varchar(30) DEFAULT NULL,
  `country` varchar(30) DEFAULT NULL,
  `time_offset` int(11) DEFAULT NULL,
  `retry_count` int(11) DEFAULT NULL,
  `user_status` varchar(30) DEFAULT NULL,
  `previous_state` varchar(30) DEFAULT NULL,
  `se_reqrecvtime` datetime DEFAULT NULL,
  `se_respsenttime` datetime DEFAULT NULL,
  `be_reqsenttime` datetime DEFAULT NULL,
  `be_resprecvtime` datetime DEFAULT NULL,
  `cp_id` varchar(30) DEFAULT NULL,
  `cp_name` varchar(30) DEFAULT NULL,
  `sub_srvname` varchar(30) DEFAULT NULL,
  `sub_srvid` varchar(30) DEFAULT NULL,
  `msg_senderid` varchar(30) DEFAULT NULL,
  `msg_text` text,
  `call_back_url` varchar(1500) DEFAULT NULL,
  `call_back_resp` varchar(100) DEFAULT NULL,
  `client_ip` varchar(30) DEFAULT NULL,
  `se_sysIp` varchar(30) DEFAULT NULL,
  `language` varchar(30) DEFAULT NULL,
  `cp_callbackurlhittime` datetime DEFAULT NULL,
  `action` varchar(45) DEFAULT NULL,
  `alert` varchar(45) DEFAULT NULL,
  `notification_url` varchar(1000) DEFAULT NULL,
  `notification_resp` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `master_id_UNIQUE` (`master_id`)
) ENGINE=InnoDB AUTO_INCREMENT=674780195 DEFAULT CHARSET=utf8;

/*Table structure for table `url_mastercalllogs` */

DROP TABLE IF EXISTS `url_mastercalllogs`;

CREATE TABLE `url_mastercalllogs` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MASTER_ID` varchar(255) NOT NULL,
  `CALL_START_DATETIME` datetime DEFAULT NULL,
  `URL_HIT_DATETIME` datetime DEFAULT NULL,
  `URL_HIT_DURATION` int(11) DEFAULT NULL,
  `URL_HIT_PULSES` int(6) DEFAULT NULL,
  `URL_HIT_RESPONSE` varchar(1500) DEFAULT NULL,
  `URL_HIT_METHOD` varchar(15) DEFAULT NULL,
  `URL_TYPE` varchar(15) DEFAULT NULL,
  `URL_STRING` varchar(500) DEFAULT NULL,
  `URL_OPTION` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `urlresp` (`URL_HIT_RESPONSE`(255)),
  KEY `urltype` (`URL_TYPE`)
) ENGINE=InnoDB AUTO_INCREMENT=461412 DEFAULT CHARSET=utf8;

/*Table structure for table `url_mastercalllogs_temp` */

DROP TABLE IF EXISTS `url_mastercalllogs_temp`;

CREATE TABLE `url_mastercalllogs_temp` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MASTER_ID` varchar(255) NOT NULL,
  `CALL_START_DATETIME` datetime DEFAULT NULL,
  `URL_HIT_DATETIME` datetime DEFAULT NULL,
  `URL_HIT_DURATION` int(11) DEFAULT NULL,
  `URL_HIT_PULSES` int(6) DEFAULT NULL,
  `URL_HIT_RESPONSE` varchar(1500) DEFAULT NULL,
  `URL_HIT_METHOD` varchar(15) DEFAULT NULL,
  `URL_TYPE` varchar(15) DEFAULT NULL,
  `URL_STRING` varchar(500) DEFAULT NULL,
  `URL_OPTION` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `urlresp` (`URL_HIT_RESPONSE`(255)),
  KEY `urltype` (`URL_TYPE`)
) ENGINE=InnoDB AUTO_INCREMENT=461411 DEFAULT CHARSET=utf8;

/* Procedure structure for procedure `ActivePendingBase` */

/*!50003 DROP PROCEDURE IF EXISTS  `ActivePendingBase` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `ActivePendingBase`()
BEGIN

SELECT DATE(DATE_SUB(NOW(), INTERVAL 1 DAY)) AS DATE,IF(SUB_SERVICE_NAME LIKE '%Soccer%','Football',IF(SUB_SERVICE_NAME LIKE '%islamic%','Islamic',IF(SUB_SERVICE_NAME LIKE '%BGM%','BGM',SUB_SERVICE_NAME))) AS service,IF(SUB_SERVICE_NAME LIKE '%Daily%','Daily',IF(SUB_SERVICE_NAME LIKE '%Monthly%','Monthly',IF(SUB_SERVICE_NAME LIKE '%Weekly%','Weekly',SUB_SERVICE_NAME))) AS SUB_SERVICE_NAME,STATUS,msisdn FROM subs_engine0.subscription where status in ('active','pending')  UNION ALL

SELECT DATE(DATE_SUB(NOW(), INTERVAL 1 DAY)) AS DATE,IF(SUB_SERVICE_NAME LIKE '%Soccer%','Football',IF(SUB_SERVICE_NAME LIKE '%islamic%','Islamic',IF(SUB_SERVICE_NAME LIKE '%BGM%','BGM',SUB_SERVICE_NAME))) AS service,IF(SUB_SERVICE_NAME LIKE '%Daily%','Daily',IF(SUB_SERVICE_NAME LIKE '%Monthly%','Monthly',IF(SUB_SERVICE_NAME LIKE '%Weekly%','Weekly',SUB_SERVICE_NAME))) AS SUB_SERVICE_NAME,STATUS,msisdn FROM subs_engine1.subscription where status in ('active','pending')  UNION ALL

SELECT DATE(DATE_SUB(NOW(), INTERVAL 1 DAY)) AS DATE,IF(SUB_SERVICE_NAME LIKE '%Soccer%','Football',IF(SUB_SERVICE_NAME LIKE '%islamic%','Islamic',IF(SUB_SERVICE_NAME LIKE '%BGM%','BGM',SUB_SERVICE_NAME))) AS service,IF(SUB_SERVICE_NAME LIKE '%Daily%','Daily',IF(SUB_SERVICE_NAME LIKE '%Monthly%','Monthly',IF(SUB_SERVICE_NAME LIKE '%Weekly%','Weekly',SUB_SERVICE_NAME))) AS SUB_SERVICE_NAME,STATUS,msisdn FROM subs_engine2.subscription where status in ('active','pending')  UNION ALL

SELECT DATE(DATE_SUB(NOW(), INTERVAL 1 DAY)) AS DATE,IF(SUB_SERVICE_NAME LIKE '%Soccer%','Football',IF(SUB_SERVICE_NAME LIKE '%islamic%','Islamic',IF(SUB_SERVICE_NAME LIKE '%BGM%','BGM',SUB_SERVICE_NAME))) AS service,IF(SUB_SERVICE_NAME LIKE '%Daily%','Daily',IF(SUB_SERVICE_NAME LIKE '%Monthly%','Monthly',IF(SUB_SERVICE_NAME LIKE '%Weekly%','Weekly',SUB_SERVICE_NAME))) AS SUB_SERVICE_NAME,STATUS,msisdn FROM subs_engine3.subscription where status in ('active','pending')  UNION ALL

SELECT DATE(DATE_SUB(NOW(), INTERVAL 1 DAY)) AS DATE,IF(SUB_SERVICE_NAME LIKE '%Soccer%','Football',IF(SUB_SERVICE_NAME LIKE '%islamic%','Islamic',IF(SUB_SERVICE_NAME LIKE '%BGM%','BGM',SUB_SERVICE_NAME))) AS service,IF(SUB_SERVICE_NAME LIKE '%Daily%','Daily',IF(SUB_SERVICE_NAME LIKE '%Monthly%','Monthly',IF(SUB_SERVICE_NAME LIKE '%Weekly%','Weekly',SUB_SERVICE_NAME))) AS SUB_SERVICE_NAME,STATUS,msisdn FROM subs_engine4.subscription where status in ('active','pending')  UNION ALL

SELECT DATE(DATE_SUB(NOW(), INTERVAL 1 DAY)) AS DATE,IF(SUB_SERVICE_NAME LIKE '%Soccer%','Football',IF(SUB_SERVICE_NAME LIKE '%islamic%','Islamic',IF(SUB_SERVICE_NAME LIKE '%BGM%','BGM',SUB_SERVICE_NAME))) AS service,IF(SUB_SERVICE_NAME LIKE '%Daily%','Daily',IF(SUB_SERVICE_NAME LIKE '%Monthly%','Monthly',IF(SUB_SERVICE_NAME LIKE '%Weekly%','Weekly',SUB_SERVICE_NAME))) AS SUB_SERVICE_NAME,STATUS,msisdn FROM subs_engine5.subscription where status in ('active','pending')  UNION ALL

SELECT DATE(DATE_SUB(NOW(), INTERVAL 1 DAY)) AS DATE,IF(SUB_SERVICE_NAME LIKE '%Soccer%','Football',IF(SUB_SERVICE_NAME LIKE '%islamic%','Islamic',IF(SUB_SERVICE_NAME LIKE '%BGM%','BGM',SUB_SERVICE_NAME))) AS service,IF(SUB_SERVICE_NAME LIKE '%Daily%','Daily',IF(SUB_SERVICE_NAME LIKE '%Monthly%','Monthly',IF(SUB_SERVICE_NAME LIKE '%Weekly%','Weekly',SUB_SERVICE_NAME))) AS SUB_SERVICE_NAME,STATUS,msisdn FROM subs_engine6.subscription where status in ('active','pending')  UNION ALL

SELECT DATE(DATE_SUB(NOW(), INTERVAL 1 DAY)) AS DATE,IF(SUB_SERVICE_NAME LIKE '%Soccer%','Football',IF(SUB_SERVICE_NAME LIKE '%islamic%','Islamic',IF(SUB_SERVICE_NAME LIKE '%BGM%','BGM',SUB_SERVICE_NAME))) AS service,IF(SUB_SERVICE_NAME LIKE '%Daily%','Daily',IF(SUB_SERVICE_NAME LIKE '%Monthly%','Monthly',IF(SUB_SERVICE_NAME LIKE '%Weekly%','Weekly',SUB_SERVICE_NAME))) AS SUB_SERVICE_NAME,STATUS,msisdn FROM subs_engine7.subscription where status in ('active','pending')  UNION ALL

SELECT DATE(DATE_SUB(NOW(), INTERVAL 1 DAY)) AS DATE,IF(SUB_SERVICE_NAME LIKE '%Soccer%','Football',IF(SUB_SERVICE_NAME LIKE '%islamic%','Islamic',IF(SUB_SERVICE_NAME LIKE '%BGM%','BGM',SUB_SERVICE_NAME))) AS service,IF(SUB_SERVICE_NAME LIKE '%Daily%','Daily',IF(SUB_SERVICE_NAME LIKE '%Monthly%','Monthly',IF(SUB_SERVICE_NAME LIKE '%Weekly%','Weekly',SUB_SERVICE_NAME))) AS SUB_SERVICE_NAME,STATUS,msisdn FROM subs_engine8.subscription where status in ('active','pending')  UNION ALL

SELECT DATE(DATE_SUB(NOW(), INTERVAL 1 DAY)) AS DATE,IF(SUB_SERVICE_NAME LIKE '%Soccer%','Football',IF(SUB_SERVICE_NAME LIKE '%islamic%','Islamic',IF(SUB_SERVICE_NAME LIKE '%BGM%','BGM',SUB_SERVICE_NAME))) AS service,IF(SUB_SERVICE_NAME LIKE '%Daily%','Daily',IF(SUB_SERVICE_NAME LIKE '%Monthly%','Monthly',IF(SUB_SERVICE_NAME LIKE '%Weekly%','Weekly',SUB_SERVICE_NAME))) AS SUB_SERVICE_NAME,STATUS,msisdn FROM subs_engine9.subscription where status in ('active','pending') 

INTO OUTFILE 'E:/ftpfiles/bi/report/subscriperbase/ActivePendingBaseData.csv'

FIELDS TERMINATED BY ','

ENCLOSED BY '"'

LINES TERMINATED BY '\n';

 END */$$
DELIMITER ;

/* Procedure structure for procedure `call_on_00` */

/*!50003 DROP PROCEDURE IF EXISTS  `call_on_00` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `call_on_00`()
BEGIN
	CALL cill_base('mv',date(DATE_SUB(NOW(), INTERVAL 1 DAY)));
	CALL cill_base('islamic',date(DATE_SUB(NOW(), INTERVAL 1 DAY)));
	CALL cill_base('christianity',date(DATE_SUB(NOW(), INTERVAL 1 DAY)));
    END */$$
DELIMITER ;

/* Procedure structure for procedure `Content_Report` */

/*!50003 DROP PROCEDURE IF EXISTS  `Content_Report` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `Content_Report`(IN a INT)
BEGIN
SET @DayInterval=a;
insert into content_report_new(date,content_type,sub_type,total)
SELECT DATE(start_datetime) AS DATE,
IF(media_file LIKE '%RP_Revamp_2017%Christainity%Content%BibleStoriesForKids%','BibleStoriesForKids',
IF(media_file LIKE '%RP_Revamp_2017%Christainity%Content%ChristainPrayers%','ChristainPrayers',
IF(media_file LIKE '%RP_Revamp_2017%Christainity%Content%HolyRosary%','HolyRosary',
IF(media_file LIKE '%RP_Revamp_2017%Christainity%Content%HymnsAndPrayers%','HymnsAndPrayers',
IF(media_file LIKE '%RP_Revamp_2017%Christainity%Content%WisdomForProsperityTips%','WisdomForProsperityTips',
IF(media_file LIKE '%RP_Revamp_2017%Christainity%Content%RevOgbahmeyTetteh%','RevOgbahmeyTetteh',media_file)))))) AS content_type,
'Content Calls' AS sub_type,
COUNT(DISTINCT master_id) AS total
FROM media_mastercalllogs WHERE DATE(start_datetime)=DATE(NOW()-INTERVAL @DayInterval DAY) 
AND (media_file LIKE '%RP_Revamp_2017%Christainity%Content%BibleStoriesForKids%'
OR media_file LIKE '%RP_Revamp_2017%Christainity%Content%ChristainPrayers%'
OR media_file LIKE '%RP_Revamp_2017%Christainity%Content%HolyRosary%'
OR media_file LIKE '%RP_Revamp_2017%Christainity%Content%HymnsAndPrayers%'
OR media_file LIKE '%RP_Revamp_2017%Christainity%Content%WisdomForProsperityTips%'
OR media_file LIKE '%RP_Revamp_2017%Christainity%Content%RevOgbahmeyTetteh%') GROUP BY 1,2,3
UNION ALL
SELECT DATE(start_datetime) AS DATE,
IF(media_file LIKE '%RP_Revamp_2017%Christainity%Content%BibleStoriesForKids%','BibleStoriesForKids',
IF(media_file LIKE '%RP_Revamp_2017%Christainity%Content%ChristainPrayers%','ChristainPrayers',
IF(media_file LIKE '%RP_Revamp_2017%Christainity%Content%HolyRosary%','HolyRosary',
IF(media_file LIKE '%RP_Revamp_2017%Christainity%Content%HymnsAndPrayers%','HymnsAndPrayers',
IF(media_file LIKE '%RP_Revamp_2017%Christainity%Content%WisdomForProsperityTips%','WisdomForProsperityTips',
IF(media_file LIKE '%RP_Revamp_2017%Christainity%Content%RevOgbahmeyTetteh%','RevOgbahmeyTetteh',media_file)))))) AS content_type,
'Content MOU' AS sub_type,
ROUND(SUM(duration/60)) AS total
FROM media_mastercalllogs WHERE DATE(start_datetime)=DATE(NOW()-INTERVAL @DayInterval DAY) 
AND (media_file LIKE '%RP_Revamp_2017%Christainity%Content%BibleStoriesForKids%'
OR media_file LIKE '%RP_Revamp_2017%Christainity%Content%ChristainPrayers%'
OR media_file LIKE '%RP_Revamp_2017%Christainity%Content%HolyRosary%'
OR media_file LIKE '%RP_Revamp_2017%Christainity%Content%HymnsAndPrayers%'
OR media_file LIKE '%RP_Revamp_2017%Christainity%Content%WisdomForProsperityTips%'
OR media_file LIKE '%RP_Revamp_2017%Christainity%Content%RevOgbahmeyTetteh%') GROUP BY 1,2,3;
END */$$
DELIMITER ;

/* Procedure structure for procedure `dailyreport` */

/*!50003 DROP PROCEDURE IF EXISTS  `dailyreport` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `dailyreport`()
BEGIN
SET @Date = DATE(NOW())-INTERVAL @DayInterval DAY;
SET @DayInterval='1';
truncate table billing_logs_temp;
truncate table ivr_confcalllogs_temp;
truncate table ivr_mastercalllogs_temp;
truncate table subscription_logs_temp;
truncate table url_mastercalllogs_temp;
insert into  billing_logs_temp select * from cilling_logs where date(be_reqrecvtime) = (DATE(NOW())-INTERVAL @DayInterval DAY);
insert into  ivr_confcalllogs_temp select * from cvr_confcalllogs where date(END_DATETIME) = (DATE(NOW())-INTERVAL @DayInterval DAY);
insert into  ivr_mastercalllogs_temp select * from cvr_mastercalllogs where date(END_DATETIME) = (DATE(NOW())-INTERVAL @DayInterval DAY);
insert into  subscription_logs_temp select * from cubscription_logs where date(se_reqrecvtime) = (DATE(NOW())-INTERVAL @DayInterval DAY);
insert into  url_mastercalllogs_temp select * from crl_mastercalllogs where date(URL_HIT_DATETIME) = (DATE(NOW())-INTERVAL @DayInterval DAY);
insert into dailyreportver02(ReportDate,ServiceName,Summary,Sub_Service_Name,Type,type_sub_param,price,Count)
select report_date,service,'SUBSCRIBERS' as summary,sub_service as sub_service,'Subscriber Base' as Type,if(status='active','Active Base',if(status='grace','Grace Base',if(status='pending','Pending Base',if(status='parking','Parking Base',if(status='suspended','Suspended Base',status))))) as status,'',sum(base) as count from subscriberbase where date(report_date) = (DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select report_date,service,'SUBSCRIBERS' as summary,sub_service as sub_service,'Subscriber Base' as Type,'Total Base' as status,'',sum(base) as count from subscriberbase where date(report_date) = (DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select report_date,'Total Subscriber Base' service,'SUBSCRIBERS' as summary,'','' as Type,'','',sum(base)as count from subscriberbase where status='active' and  date(report_date) = (DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7  union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,if(req_desc='subscribe','Activation Revenue',if(req_desc='renewal','Renewal Revenue',req_desc)) as type ,concat('Rev @',amount/10000,'GHC') as Type_Sub_Param,Amount as amount,(count(*)*amount/10000) from subscription_logs_temp where   status='successful' and request_type in ('1','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,if(action='act','Activation Revenue-Retry',if(action='renew','Renewals Revenue-Retry',req_desc)) as type ,concat('Rev @',amount/10000,'GHC') as Type_Sub_Param,Amount as amount,(count(*)*amount/10000) from subscription_logs_temp where  status='successful' and request_type = '3' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,if(action='act','Activation Count-Retry',if(action='renew','Renewals Count-Retry',req_desc)) as type ,concat(if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))),'@',amount/10000) as Type_Sub_Param,Amount as amount,count(*) from subscription_logs_temp where   status='successful' and request_type ='3' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,if(req_desc='subscribe','Activation Count',if(req_desc='renewal','Renewals Count',req_desc)) as type ,concat(if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))),'@',amount/10000) as Type_Sub_Param,Amount as amount,count(*) from subscription_logs_temp where   status='successful' and request_type in ('1','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,'CHANNEL WISE ACTIVATION' as type ,concat('Act @',UPPER(channel)) as Type_Sub_Param,Amount as amount,count(*) from subscription_logs_temp where   status='successful' and request_type ='3' and action='act'  and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,'CHANNEL WISE ACTIVATION' as type ,concat('Act @',UPPER(channel)) as Type_Sub_Param,Amount as amount,count(*) from subscription_logs_temp where   status='successful' and request_type in ('1','4')  and req_desc='subscribe' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as subservice,'Churn Count' as type,if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as Type_Sub_Param,'0' as amount,count(*) from subscription_logs_temp where  status='successful' and request_type ='2' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as subservice,'Churn Count' as type,if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as Type_Sub_Param,'0' as amount,count(*) from subscription_logs_temp where  req_desc = 'statechanger' and user_status like 'unsub' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as subservice,'CHANNEL WISE�CHURN' as type,concat('Churn @',UPPER(channel)) as Type_Sub_Param,'0' as amount,count(*) from subscription_logs_temp where   status='successful' and request_type ='2' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as subservice,'CHANNEL WISE�CHURN' as type,concat('Churn @',UPPER(channel)) as Type_Sub_Param,'0' as amount,count(*) from subscription_logs_temp where   req_desc = 'statechanger' and user_status like 'unsub' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',"NOSUBSERVICE" as subservice,"Success Billing Request" as Type,if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) Type_Sub_Param,"0",count(*) from subscription_logs_temp where request_type in('1','3','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and status='successful' group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',"NOSUBSERVICE" as subservice,"Billing Request Failed Reasons" as Type,if(resp_desc like 'low%',"Low Balance",if(resp_desc like '%TimeOut%',"Network Error",if(resp_desc like '%Connect%',"Network Error",resp_desc))) as param_type,"0",count(distinct msisdn) from subscription_logs_temp where request_type in('1','3','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and status<>'successful' group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'','' as subservice,"Billing Request" as Type,if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) Type_Sub_Param,"0",count(*) from subscription_logs_temp  where  request_type in('1','3','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'','' as subservice,"Unique Billing Request" as Type,if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) Type_Sub_Param,"0",count(distinct msisdn) from subscription_logs_temp  where  request_type in('1','3','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like 'mv%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'na','Other',service))))) as service,'WELCOME USERS','' as subservice,"Unique Caller" type,"New Users Unique @Day" sub_type,"0",count(distinct A_PARTY) as UUOfDay from ivr_mastercalllogs_temp where call_type='incoming' and SUB_STATUS='new' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like 'mv%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'na','Other',service))))) as service,'WELCOME USERS','' as subservice,"Unique Caller" type,"Unique Caller @Day" sub_type,"0",count(distinct A_PARTY) as UUOfDay from ivr_mastercalllogs_temp where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left((NOW()-INTERVAL @DayInterval DAY),10) as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like 'mv%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'na','Other',service))))) as service,'WELCOME USERS','' as subservice,"Unique Caller" type,"Unique Caller @Month" sub_type,"0",count(distinct A_PARTY) from ivr_mastercalllogs_temp where call_type='incoming' and month(end_datetime)=month(NOW()-INTERVAL @DayInterval DAY) and year(end_datetime)=year(NOW()-INTERVAL @DayInterval DAY) and day(end_datetime)<=day(NOW()-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select (DATE(NOW())-INTERVAL @DayInterval DAY) as ReportDate,if(UTS.service like 'magicvoice%','Magicvoice',if(UTS.service like '%Religious%','Religious',if(UTS.service like '%christianity%','Christianity',UTS.service))) as service,'', '',"New User Stats | IVR" type,"% New User Attempted to Subscribe" subtype,"0",round((UTS.tryed/A.attempt)*100) as '%AttToSub' from (select M.service,count(*) as tryed from ivr_mastercalllogs_temp M, url_mastercalllogs_temp U where date(M.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.SUB_STATUS='new' and  M.call_type='incoming' and date(U.URL_HIT_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.master_id=U.master_id group by 1) UTS RIGHT JOIN (select M.service,count(*) as attempt from ivr_mastercalllogs_temp M where date(M.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.SUB_STATUS='new' and  call_type='incoming'  group by 1) A ON A.service=UTS.service union all
select left(END_DATETIME,10) as Date,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',service))) as service,'','' subservice,"New User Stats | IVR" type,CASE WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) <=5 THEN '000-005' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 6 and 10 THEN '006-010' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 11 and 20 THEN '011-020' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 21 and 30 THEN '021-030' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 31 and 60 THEN '031-060' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 61 and 90 THEN '061-090' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 91 and 120 THEN '091-120' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 121 and 150 THEN '121-150' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 151 and 180 THEN '151-180' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 181 and 480 THEN '181-480' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) >= 480 THEN '480+sec' END as param_type,"0",count(*) from ivr_mastercalllogs_temp where call_status IS NOT NULL AND date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and call_status='success' and SUB_STATUS ='new' group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'CALLS & MOU',"NOSUBSERVICE" as subservice,"Calls" type,"Total Calls"  sub_type,"0", count(*) from ivr_mastercalllogs_temp where call_type='incoming' and SUB_STATUS is not null and date(END_DATETIME) = (DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(SHORT_CODE='3071','Female',if(SHORT_CODE='3072','King',if(SHORT_CODE='3073','Cartoon',if(SHORT_CODE='3074','Monster',if(SHORT_CODE='3075','Bunny',if(SHORT_CODE='3079','Romantic',if(SHORT_CODE='3078','Traffic',if(SHORT_CODE='30723','Airport',SHORT_CODE)))))))) as service,'CALLS & MOU',"NOSUBSERVICE" as subservice,"Calls" type,"Total Calls"  sub_type,"0", count(*) from ivr_mastercalllogs_temp where call_type='incoming' and date(END_DATETIME) = (DATE(NOW())-INTERVAL @DayInterval DAY)  and SHORT_CODE in ('3071','3072','3073','3074','3075','3079','3078','30723') group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'',"NOSUBSERVICE" as subservice,"Calls" type,case when (((length(B_PARTY)<8 and service like '%magic%') or service not like '%magic%') and SUB_STATUS='active') then 'Sub Short Code Calls' when (((length(B_PARTY)<8 and service like '%magic%') or service not like '%magic%') and SUB_STATUS='new') then 'New Sub Short Code Calls' when (((length(B_PARTY)<8 and service like '%magic%') or service not like '%magic%') and SUB_STATUS not in ('active','new')) then 'Non Sub Short Code Calls' when (length(B_PARTY)>=8 and service like '%magic%' and SUB_STATUS='active') then 'Sub Friend Number Calls' when (length(B_PARTY)>=8 and service like '%magic%' and SUB_STATUS='new') then 'New Sub Friend Number Calls' when (length(B_PARTY)>=8 and service like '%magic%' and SUB_STATUS not in ('active','new')) then 'Non Sub Friend Number Calls' end as sub_type,"0", count(*) from ivr_mastercalllogs_temp where call_type='incoming'  and call_status='success' and date(END_DATETIME) like (DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'',"NOSUBSERVICE" as subservice,"MOU/ PULSES" type,case when length(B_PARTY)>=8 and SUB_STATUS='active' then 'Susbcriber Friend No Calls MOU' when length(B_PARTY)>=8 and SUB_STATUS<>'active' then 'Non Susbcriber Friend No MOU' when length(B_PARTY)<8 and SUB_STATUS='active' then 'Subscriber Short Code MOU' when length(B_PARTY)<8 and SUB_STATUS<>'active' then 'Non Susbcriber� Short Code MOU' end as type_param,"0", round(sum(DURATION)/60) as MOU from ivr_mastercalllogs_temp where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and (service like '%mv%' or service like '%magic%') group by 1,2,3,4,5,6,7 union all
/*select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'',"NOSUBSERVICE" as subservice,"Calls" type,case when SUB_STATUS='active' then 'Sub Short Code Calls' when SUB_STATUS<>'active' then 'Non Sub Short Code Calls' end as sub_type,"0", count(*) from ivr_mastercalllogs_temp where call_type='incoming' and date(END_DATETIME) like (DATE(NOW())-INTERVAL @DayInterval DAY)  and (service not like '%mv%' and service not like '%magic%') group by 1,2,3,4,5,6,7 union all*/
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'',"NOSUBSERVICE" as subservice,"MOU/ PULSES" type,case when  SUB_STATUS='active' then 'Subscriber Short Code MOU' when SUB_STATUS<>'active' then 'Non Susbcriber� Short Code MOU' end as type_param,"0", round(sum(DURATION)/60) as MOU from ivr_mastercalllogs_temp where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and (service not like '%mv%' and service not like '%magic%') group by 1,2,3,4,5,6,7 union all
select left(C.END_DATETIME,10)as ReportDate,if(M.service like 'magicvoice%','Magicvoice',if(M.service like '%Religious%','Religious',if(M.service like '%christianity%','christianity',M.service))) as service,'',"NOSUBSERVICE" as subservice,"MOU/ PULSES" type, case when  SUB_STATUS='active' then 'Subscriber Calls before Dialout   MOU' when  SUB_STATUS<>'active' then 'Non Subscriber Calls before Dialout   MOU'  end as type,"0", round(sum(TIMESTAMPDIFF(SECOND,M.PICKUP_DATETIME,C.DIAL_DATETIME))/60) as befor_dialout from ivr_mastercalllogs_temp M,ivr_confcalllogs_temp C where M.call_type='incoming' and date(M.END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and date(C.END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.master_id=C.master_id group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'CALLS & MOU',"NOSUBSERVICE" as subservice,"MOU/ PULSES" type,"Total� MOU" as type_param,"0", round(sum(DURATION)/60) as MOU from ivr_mastercalllogs_temp where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10) as Date,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%bible%','Christianity',service)))) as service,'',"NOSUBSERVICE" subservice,'Language','English' as type_param,'',count(*) from ivr_mastercalllogs_temp where CALL_STATUS like 'success' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and call_type='incoming' group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like '%magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'mv%','Magicvoice',service)))) as service,'MAGIC VOICE STATISTICS','' as subservice,"Call Conference Ratio" type,if(STATUS like 'failure',"Call Conference Failure",if(STATUS like 'success',"Call Conference Success",STATUS)) as sub_type,"0",count(*) from ivr_confcalllogs_temp where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and STATUS != ' ' group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like '%magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'mv%','Magicvoice',service)))) as service,'MAGIC VOICE STATISTICS',"NOSUBSERVICE" as subservice,"Call Conference Ratio" type,"Call Conference Success Ratio" sub_type,"0",round((select count(*) from ivr_confcalllogs_temp where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and STATUS = 'success')/count(*)*100) as count from ivr_confcalllogs_temp where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and STATUS != ' ' group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%bible%','Christianity',service)))) as service,'',"NOSUBSERVICE" as subservice,"Conf Call Failure Reason" type,if(REASON like 'noanswer',"No Answer",if(REASON like 'rlcsent',"A Party Hangup","MSC Disconnect")) as sub_type,"0",count(*)  from ivr_confcalllogs_temp where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and status<>'success' group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like '%magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'',"NOSUBSERVICE" as subservice,"Service Usage in Seconds" type,CASE WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) <=5 THEN '000-005' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 6 and 10 THEN '006-010' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 11 and 20 THEN '011-020' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 21 and 30 THEN '021-030' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 31 and 60 THEN '031-060' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 61 and 90 THEN '061-090' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 91 and 120 THEN '091-120' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 121 and 150 THEN '121-150' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 151 and 180 THEN '151-180' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 181 and 480 THEN '181-480' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) >= 480 THEN '480+sec' END as param_type,"0",count(*) from ivr_mastercalllogs_temp where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and call_status='success'  group by 1,2,3,4,5,6,7 union all
select left(end_datetime,10)as date,'OBD','',if(service like '%mv%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'OBD COUNT','OBD Attempt','',count(*) from ivr_mastercalllogs_temp where call_type='outgoing' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'Magicvoice%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%bible%','Christianity',service)))) as service,'',"NOSUBSERVICE" as subservice,"Conf Call Failure Reason" type,if(REASON like 'noanswer',"No Answer",if(REASON like 'rlcsent',"A Party Hangup","MSC Disconnect")) as sub_type,"0",count(*)  from ivr_confcalllogs_temp where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and status<>'success' group by 1,2,3,4,5,6,7 union all
select left(end_datetime,10)as date,'OBD','',if(service like '%mv%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'OBD COUNT',if(call_status='success','OBD Picked','OBD Not Picked') as type,'',count(*) from ivr_mastercalllogs_temp where call_type='outgoing' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,2,3,4,5,6,7 union all
select left(ivm.end_datetime,10)as date,'OBD','',if(ivm.service like '%mv%','Magicvoice',if(ivm.service like '%religious%','Religious',if(ivm.service like '%christianity%','christianity',if(ivm.service like '%Ibadat%','Ibadat',ivm.service)))) as service,'OBD COUNT','OBD SUCESS %','',round((select count(*) from ivr_mastercalllogs_temp as im where im.call_type='outgoing' and im.call_status='success' and ivm.service = im.service and date(im.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY))/count(*)*100)as count from ivr_mastercalllogs_temp as ivm where ivm.call_type='outgoing' and date(ivm.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all
select left(end_datetime,10)as date,'OBD','',if(service like '%mv%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'OBD Failure Reasons',RLC_REASON,'',count(*) from ivr_mastercalllogs_temp where call_type='outgoing' and call_status<>'success' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as date,'OBD','',if(Sub_Srvname like 'mv%','Magicvoice',if(Sub_Srvname like '%religious%','Religious',if(Sub_Srvname like '%christianity%','Religious',if(Sub_Srvname like '%islamic%','Religious',Sub_Srvname)))) as service,'OBD Subscription','OBD Subscription' type,'',count(*) from subscription_logs_temp where action ='act' and status='successful' and request_type in ('1','3') and channel='OBD' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(imc.end_datetime,10)as date,'OBD','',if(imc.service like '%mv%','Magicvoice',if(imc.service like '%religious%','Religious',if(imc.service like '%christianity%','christianity',if(imc.service like '%Ibadat%','Ibadat',imc.service)))) as service,'OBD Subscription','OBD Subscription %','',round((select count(*) from subscription_logs_temp as im where im.action = 'act' and status='successful' and im.request_type in ('1','3') and im.channel='OBD' and im.srv_name=SUBSTRING_INDEX(imc.service,'obd',-1)  and date(im.se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY))/count(*)*100)as count from ivr_mastercalllogs_temp as imc where imc.call_type='outgoing' and imc.call_status='success' and date(imc.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and imc.service not like '%mv%'  group by 1,2,3,4,5,6,7 union all
select left(imc.end_datetime,10)as date,'OBD','',if(imc.service like '%mv%','Magicvoice',if(imc.service like '%religious%','Religious',if(imc.service like '%christianity%','christianity',if(imc.service like '%Ibadat%','Ibadat',imc.service)))) as service,'OBD Subscription','OBD Subscription %','',round((select count(*) from subscription_logs_temp as im where im.action = 'act' and status='successful' and im.request_type in ('1','3') and im.channel='OBD' and im.srv_name='Magicvoice'  and date(im.se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY))/count(*)*100)as count from ivr_mastercalllogs_temp as imc where imc.call_type='outgoing' and imc.call_status='success' and date(imc.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and imc.service  like '%mv%'  group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as date,'OBD','',if(Sub_Srvname like 'mv%','Magicvoice',if(Sub_Srvname like '%religious%','Religious',if(Sub_Srvname like '%christianity%','Religious',if(Sub_Srvname like '%islamic%','Religious',Sub_Srvname)))) as service,'OBD Subscription Revenue',if(req_desc='renewal','OBD Renewals Revenue','OBD Activation Revenue') type,'',ROUND((count(*)*(amount/100))) as revenue from subscription_logs_temp where  status='successful' and request_type in ('1','3') and channel='OBD' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as date,'OBD','',if(Sub_Srvname like 'mv%','Magicvoice',if(Sub_Srvname like '%religious%','Religious',if(Sub_Srvname like '%christianity%','Religious',if(Sub_Srvname like '%islamic%','Religious',Sub_Srvname)))) as service,'OBD Subscription Attempt','' as type_sub_param,'',count(*) as cnt from subscription_logs_temp where request_type ='1' and channel='OBD' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select date(URL_HIT_DATETIME)as date,'OBD' Servicename,'' as summary,if(substring_index(SUBSTRING_INDEX(URL_STRING,'serviceid=',-1),'&',1)='religious','Religious',if(substring_index(SUBSTRING_INDEX(URL_STRING,'serviceid=',-1),'&',1)='229012000003233','Christianity',if(substring_index(SUBSTRING_INDEX(URL_STRING,'serviceid=',-1),'&',1)='229012000003232','Islamic',substring_index(SUBSTRING_INDEX(URL_STRING,'serviceid=',-1),'&',1)))) as subservice,'OBD PRESS SUB DTMF' type,'OBD PRESS SUB DTMF' sub_type,'' type,count(*) from url_mastercalllogs_temp where substring_index(SUBSTRING_INDEX(URL_STRING,'channel=',-1),'&',1) ='obd' and date(URL_HIT_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(end_datetime,10)as date,'OBD','',if(service like '%mv%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'Duration Stats',case when DURATION<=10 then '000-010' when DURATION>10 and DURATION<=20 then '011-020' when DURATION>20 and DURATION<=30 then '021-030' when DURATION>30 and DURATION<=45 then '031-045' when DURATION>45 and DURATION<=60 then '046-060' when DURATION>60 and DURATION<=80 then '061-80'  when DURATION>80 and DURATION<=120 then '081-120' when DURATION>120 and DURATION<=180 then '121-180' when DURATION>180 then '180+sec' end  as ran,'', count(*) from ivr_mastercalllogs_temp as im where call_type='outgoing' and call_status='success' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7;
delete from dailyreportver02  where count is null;
call new_e1();
Call Daily_Report_New();

Call Content_Report(1);
END */$$
DELIMITER ;

/* Procedure structure for procedure `DailyReport_obd` */

/*!50003 DROP PROCEDURE IF EXISTS  `DailyReport_obd` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `DailyReport_obd`(IN inter varchar(3))
BEGIN
SET @Date = DATE(NOW())-INTERVAL @DayInterval DAY;
SET @DayInterval=inter;
insert into dailyreportver02(ReportDate,ServiceName,Summary,Sub_Service_Name,Type,type_sub_param,price,Count)

select left(end_datetime,10)as date,'OBD','',if(service like '%mv%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'OBD COUNT','OBD Attempt','',count(*) from ivr_mastercalllogs where call_type='outgoing' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(service like 'Magicvoice%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%bible%','Christianity',service)))) as service,'',"NOSUBSERVICE" as subservice,"Conf Call Failure Reason" type,if(REASON like 'noanswer',"No Answer",if(REASON like 'rlcsent',"A Party Hangup","MSC Disconnect")) as sub_type,"0",count(*)  from ivr_confcalllogs where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and status<>'success' group by 1,2,3,4,5,6,7 union all

select left(end_datetime,10)as date,'OBD','',if(service like '%mv%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'OBD COUNT',if(call_status='success','OBD Picked','OBD Not Picked') as type,'',count(*) from ivr_mastercalllogs where call_type='outgoing' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,2,3,4,5,6,7 union all

select left(ivm.end_datetime,10)as date,'OBD','',if(ivm.service like '%mv%','Magicvoice',if(ivm.service like '%religious%','Religious',if(ivm.service like '%christianity%','christianity',if(ivm.service like '%Ibadat%','Ibadat',ivm.service)))) as service,'OBD COUNT','OBD SUCESS %','',round((select count(*) from ivr_mastercalllogs as im where im.call_type='outgoing' and im.call_status='success' and ivm.service = im.service and date(im.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY))/count(*)*100)as count from ivr_mastercalllogs as ivm where ivm.call_type='outgoing' and date(ivm.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all

select left(end_datetime,10)as date,'OBD','',if(service like '%mv%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'OBD Failure Reasons',RLC_REASON,'',count(*) from ivr_mastercalllogs where call_type='outgoing' and call_status<>'success' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all

select left(se_reqrecvtime,10)as date,'OBD','',if(Sub_Srvname like 'mv%','Magicvoice',if(Sub_Srvname like '%religious%','Religious',if(Sub_Srvname like '%christianity%','christianity',if(Sub_Srvname like '%Ibadat%','Ibadat',Sub_Srvname)))) as service,'OBD Subscription','OBD Subscription' type,'',count(*) from subscription_logs where action ='act' and status='successful' and request_type in ('1','3') and channel='OBD' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all



select left(imc.end_datetime,10)as date,'OBD','',if(imc.service like '%mv%','Magicvoice',if(imc.service like '%religious%','Religious',if(imc.service like '%christianity%','christianity',if(imc.service like '%Ibadat%','Ibadat',imc.service)))) as service,'OBD Subscription','OBD Subscription %','',round((select count(*) from subscription_logs as im where im.action = 'act' and status='successful' and im.request_type in ('1','3') and im.channel='OBD' and im.srv_name=SUBSTRING_INDEX(imc.service,'obd',-1)  and date(im.se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY))/count(*)*100)as count from ivr_mastercalllogs as imc where imc.call_type='outgoing' and imc.call_status='success' and date(imc.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and imc.service not like '%mv%'  group by 1,2,3,4,5,6,7 union all

select left(imc.end_datetime,10)as date,'OBD','',if(imc.service like '%mv%','Magicvoice',if(imc.service like '%religious%','Religious',if(imc.service like '%christianity%','christianity',if(imc.service like '%Ibadat%','Ibadat',imc.service)))) as service,'OBD Subscription','OBD Subscription %','',round((select count(*) from subscription_logs as im where im.action = 'act' and status='successful' and im.request_type in ('1','3') and im.channel='OBD' and im.srv_name='Magicvoice'  and date(im.se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY))/count(*)*100)as count from ivr_mastercalllogs as imc where imc.call_type='outgoing' and imc.call_status='success' and date(imc.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and imc.service  like '%mv%'  group by 1,2,3,4,5,6,7 union all



select left(se_reqrecvtime,10)as date,'OBD','',if(Sub_Srvname like 'mv%','Magicvoice',if(Sub_Srvname like '%religious%','Religious',if(Sub_Srvname like '%christianity%','christianity',if(Sub_Srvname like '%Ibadat%','Ibadat',Sub_Srvname)))) as service,'OBD Subscription Revenue',if(req_desc='renewal','OBD Renewals Revenue','OBD Activation Revenue') type,'',ROUND((count(*)*(amount/100))) as revenue from subscription_logs where  status='successful' and request_type in ('1','3') and channel='OBD' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(end_datetime,10)as date,'OBD','',if(service like '%mv%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'Duration Stats', case when DURATION<=5 then '000-005' when DURATION>5 and DURATION<=10 then '006-010' when DURATION>10 and DURATION<=15 then '011-015' when DURATION>15 and DURATION<=20 then '016-020' when DURATION>20 and DURATION<31 then '021-030' when DURATION>30 and DURATION<=180 then '031-180' when DURATION>480 then '480+sec' when DURATION>180 and DURATION<=480 then '181-480' end as ran,'', count(*) from ivr_mastercalllogs as im where call_type='outgoing' and call_status='success' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7;





END */$$
DELIMITER ;

/* Procedure structure for procedure `DailyReport_old` */

/*!50003 DROP PROCEDURE IF EXISTS  `DailyReport_old` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `DailyReport_old`()
BEGIN
SET @Date = DATE(NOW())-INTERVAL @DayInterval DAY;
SET @DayInterval='1';
insert into dailyreportver02(ReportDate,ServiceName,Summary,Sub_Service_Name,Type,type_sub_param,price,Count)
select report_date,service,'SUBSCRIBERS' as summary,sub_service as sub_service,'Subscriber Base' as Type,if(status='active','Active Base',if(status='grace','Grace Base',if(status='pending','Pending Base',if(status='parking','Parking Base',if(status='suspended','Suspended Base',status))))) as status,'',sum(base) as count from subscriberbase where date(report_date) = (DATE(NOW())-INTERVAL @DayInterval DAY) and service='Religious' group by 1,2,3,4,5,6,7 union all

select report_date,service,'SUBSCRIBERS' as summary,sub_service as sub_service,'Subscriber Base' as Type,'Total Base' as status,'',sum(base) as count from subscriberbase where date(report_date) = (DATE(NOW())-INTERVAL @DayInterval DAY) and service ='Religious' group by 1,2,3,4,5,6,7 union all

select report_date,'Total Subscriber Base' service,'SUBSCRIBERS' as summary,'','' as Type,'','',sum(base)as count from subscriberbase where status='active' and  date(report_date) = (DATE(NOW())-INTERVAL @DayInterval DAY) and service ='Religious' group by 1,2,3,4,5,6,7  union all



select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,if(req_desc='subscribe','Activation Revenue',if(req_desc='renewal','Renewal Revenue',req_desc)) as type ,concat('Rev @',amount/10000,'GHC') as Type_Sub_Param,Amount as amount,(count(*)*amount/10000) from subscription_logs where   status='successful' and request_type in ('1','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,if(action='act','Activation Revenue-Retry',if(action='renew','Renewals Revenue-Retry',req_desc)) as type ,concat('Rev @',amount/10000,'GHC') as Type_Sub_Param,Amount as amount,(count(*)*amount/10000) from subscription_logs where  status='successful' and request_type = '3' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,if(action='act','Activation Count-Retry',if(action='renew','Renewals Count-Retry',req_desc)) as type ,concat(if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))),'@',amount/10000) as Type_Sub_Param,Amount as amount,count(*) from subscription_logs where   status='successful' and request_type ='3' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,if(req_desc='subscribe','Activation Count',if(req_desc='renewal','Renewals Count',req_desc)) as type ,concat(if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))),'@',amount/10000) as Type_Sub_Param,Amount as amount,count(*) from subscription_logs where   status='successful' and request_type in ('1','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,'CHANNEL WISE ACTIVATION' as type ,concat('Act @',UPPER(channel)) as Type_Sub_Param,Amount as amount,count(*) from subscription_logs where   status='successful' and request_type ='3' and action='act'  and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,'CHANNEL WISE ACTIVATION' as type ,concat('Act @',UPPER(channel)) as Type_Sub_Param,Amount as amount,count(*) from subscription_logs where   status='successful' and request_type in ('1','4')  and req_desc='subscribe' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all



select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as subservice,'Churn Count' as type,if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as Type_Sub_Param,'0' as amount,count(*) from subscription_logs where  status='successful' and request_type ='2' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as subservice,'Churn Count' as type,if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as Type_Sub_Param,'0' as amount,count(*) from subscription_logs where  req_desc = 'statechanger' and user_status like 'unsub' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as subservice,'CHANNEL WISE�CHURN' as type,concat('Churn @',UPPER(channel)) as Type_Sub_Param,'0' as amount,count(*) from subscription_logs where   status='successful' and request_type ='2' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as subservice,'CHANNEL WISE�CHURN' as type,concat('Churn @',UPPER(channel)) as Type_Sub_Param,'0' as amount,count(*) from subscription_logs where   req_desc = 'statechanger' and user_status like 'unsub' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(be_reqrecvtime,10)as ReportDate,srv_name as service,'',"NOSUBSERVICE" as subservice,"Success Billing Request" as Type,if(srv_key like '%Daily%','DailyPack',if(srv_key like '%Weekly%','WeeklyPack',if(srv_key like '%month%','MonthlyPack',srv_key))) Type_Sub_Param,"0",count(*) from billing_logs where request_type='1' and date(be_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and req_status='success' group by 1,2,3,4,5,6,7 union all

select left(be_reqrecvtime,10)as ReportDate,srv_name as service,'',"NOSUBSERVICE" as subservice,"Billing Request Failed Reasons" as Type,if(be_respdesc like 'low%',"Low Balance",if(be_respdesc like '%TimeOut%',"Network Error",if(be_respdesc like '%Connect%',"Network Error","Other Error"))) as param_type,"0",count(distinct msisdn) from billing_logs where request_type='1' and date(be_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and req_status<>'success' group by 1,2,3,4,5,6,7 union all

select left(be_reqrecvtime,10)as ReportDate,srv_name as service,'','' as subservice,"Billing Request" as Type,if(srv_key like '%Daily%','DailyPack',if(srv_key like '%Weekly%','WeeklyPack',if(srv_key like '%month%','MonthlyPack',srv_key))) Type_Sub_Param,"0",count(*) from billing_logs  where  request_type='1' and date(be_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(be_reqrecvtime,10)as ReportDate,srv_name as service,'','' as subservice,"Unique Billing Request" as Type,if(srv_key like '%Daily%','DailyPack',if(srv_key like '%Weekly%','WeeklyPack',if(srv_key like '%month%','MonthlyPack',srv_key))) Type_Sub_Param,"0",count(distinct msisdn) from billing_logs  where  request_type='1' and date(be_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like 'mv%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'na','Other',service))))) as service,'WELCOME USERS','' as subservice,"Unique Caller" type,"New Users Unique @Day" sub_type,"0",count(distinct A_PARTY) as UUOfDay from ivr_mastercalllogs where call_type='incoming' and SUB_STATUS='new' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like 'mv%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'na','Other',service))))) as service,'WELCOME USERS','' as subservice,"Unique Caller" type,"Unique Caller @Day" sub_type,"0",count(distinct A_PARTY) as UUOfDay from ivr_mastercalllogs where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left((NOW()-INTERVAL @DayInterval DAY),10) as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like 'mv%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'na','Other',service))))) as service,'WELCOME USERS','' as subservice,"Unique Caller" type,"Unique Caller @Month" sub_type,"0",count(distinct A_PARTY) from ivr_mastercalllogs where call_type='incoming' and month(end_datetime)=month(NOW()-INTERVAL @DayInterval DAY) and year(end_datetime)=year(NOW()-INTERVAL @DayInterval DAY) and day(end_datetime)<=day(NOW()-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select (DATE(NOW())-INTERVAL @DayInterval DAY) as ReportDate,if(UTS.service like 'magicvoice%','Magicvoice',if(UTS.service like '%Religious%','Religious',if(UTS.service like '%christianity%','Christianity',UTS.service))) as service,'', '',"New User Stats | IVR" type,"% New User Attempted to Subscribe" subtype,"0",round((UTS.tryed/A.attempt)*100) as '%AttToSub' from (select M.service,count(*) as tryed from ivr_mastercalllogs M, url_mastercalllogs U where date(M.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.SUB_STATUS='new' and  M.call_type='incoming' and date(U.URL_HIT_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.master_id=U.master_id group by 1) UTS RIGHT JOIN (select M.service,count(*) as attempt from ivr_mastercalllogs M where date(M.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.SUB_STATUS='new' and  call_type='incoming'  group by 1) A ON A.service=UTS.service union all

select left(END_DATETIME,10) as Date,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',service))) as service,'','' subservice,"New User Stats | IVR" type,CASE WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) <=5 THEN '000-005' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 6 and 10 THEN '006-010' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 11 and 20 THEN '011-020' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 21 and 30 THEN '021-030' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 31 and 180 THEN '031-180' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 181 and 480 THEN '181-480' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) >= 480 THEN '480+sec'  END as call_dur,"0",count(*) from ivr_mastercalllogs where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and call_status='success' and SUB_STATUS ='new' group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'CALLS & MOU',"NOSUBSERVICE" as subservice,"Calls" type,"Total Calls"  sub_type,"0", count(*) from ivr_mastercalllogs where call_type='incoming' and date(END_DATETIME) = (DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(SHORT_CODE='3071','Female',if(SHORT_CODE='3072','King',if(SHORT_CODE='3073','Cartoon',if(SHORT_CODE='3074','Monster',if(SHORT_CODE='3075','Bunny',if(SHORT_CODE='3079','Romantic',if(SHORT_CODE='3078','Traffic',if(SHORT_CODE='30723','Airport',SHORT_CODE)))))))) as service,'CALLS & MOU',"NOSUBSERVICE" as subservice,"Calls" type,"Total Calls"  sub_type,"0", count(*) from ivr_mastercalllogs where call_type='incoming' and date(END_DATETIME) = (DATE(NOW())-INTERVAL @DayInterval DAY)  and SHORT_CODE in ('3071','3072','3073','3074','3075','3079','3078','30723') group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'',"NOSUBSERVICE" as subservice,"Calls" type,case when (length(B_PARTY)<8 and SUB_STATUS='active') then 'Sub Short Code Calls' when (length(B_PARTY)<8 and SUB_STATUS<>'active') then 'Non Sub Short Code Calls' when (length(B_PARTY)>=8 and SUB_STATUS='active') then 'Sub Friend Number Calls' when (length(B_PARTY)>=8 and SUB_STATUS<>'active') then 'Non Sub Friend Number Calls'  end as sub_type,"0", count(*) from ivr_mastercalllogs where call_type='incoming' and date(END_DATETIME) like (DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(C.END_DATETIME,10)as ReportDate,if(M.service like 'magicvoice%','Magicvoice',if(M.service like '%Religious%','Religious',if(M.service like '%christianity%','christianity',M.service))) as service,'',"NOSUBSERVICE" as subservice,"MOU/ PULSES" type, case when  SUB_STATUS='active' then 'Subscriber Calls before Dialout   MOU' when  SUB_STATUS<>'active' then 'Non Subscriber Calls before Dialout   MOU'  end as type,"0", round(sum(TIMESTAMPDIFF(SECOND,M.PICKUP_DATETIME,C.DIAL_DATETIME))/60) as befor_dialout from ivr_mastercalllogs M,ivr_confcalllogs C where M.call_type='incoming' and date(M.END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and date(C.END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.master_id=C.master_id group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'CALLS & MOU',"NOSUBSERVICE" as subservice,"MOU/ PULSES" type,"Total� MOU" as type_param,"0", round(sum(DURATION)/60) as MOU from ivr_mastercalllogs where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'',"NOSUBSERVICE" as subservice,"MOU/ PULSES" type,case when length(B_PARTY)>=8 and SUB_STATUS='active' then 'Susbcriber Friend No Calls MOU' when length(B_PARTY)>=8 and SUB_STATUS<>'active' then 'Non Susbcriber Friend No MOU' when length(B_PARTY)<8 and SUB_STATUS='active' then 'Subscriber Short Code MOU' when length(B_PARTY)<8 and SUB_STATUS<>'active' then 'Non Susbcriber� Short Code MOU' end as type_param,"0", round(sum(DURATION)/60) as MOU from ivr_mastercalllogs where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10) as Date,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%bible%','Christianity',service)))) as service,'',"NOSUBSERVICE" subservice,'Language','English' as type_param,'',count(*) from ivr_mastercalllogs where CALL_STATUS like 'success' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and call_type='incoming' group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Bible%','Christianity',service)))) as service,'',"NOSUBSERVICE" as subservice,"Friend No. Calls" type,"Friend No Calls" sub_type,"0", count(*) from ivr_mastercalllogs where call_type='incoming' and length(B_PARTY)>=8 and date(END_DATETIME) = (DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Bible%','Christianity',service)))) as service,'',"NOSUBSERVICE" as subservice,"Friend No. Calls" type,"Friend No MOU" type_param,"0", round(sum(DURATION)/60) as MOU from ivr_mastercalllogs where length(B_PARTY)>=8 and call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(service like '%magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'mv%','Magicvoice',service)))) as service,'MAGIC VOICE STATISTICS','' as subservice,"Call Conference Ratio" type,if(STATUS like 'failure',"Call Conference Failure",if(STATUS like 'success',"Call Conference Success",STATUS)) as sub_type,"0",count(*) from ivr_confcalllogs where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and STATUS != ' ' group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(service like '%magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'mv%','Magicvoice',service)))) as service,'MAGIC VOICE STATISTICS',"NOSUBSERVICE" as subservice,"Call Conference Ratio" type,"Call Conference Success Ratio" sub_type,"0",round((select count(*) from ivr_confcalllogs where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and STATUS = 'success')/count(*)*100) as count from ivr_confcalllogs where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and STATUS != ' ' group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%bible%','Christianity',service)))) as service,'',"NOSUBSERVICE" as subservice,"Conf Call Failure Reason" type,if(REASON like 'noanswer',"No Answer",if(REASON like 'rlcsent',"A Party Hangup","MSC Disconnect")) as sub_type,"0",count(*)  from ivr_confcalllogs where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and status<>'success' group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10)as ReportDate,if(service like '%magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'',"NOSUBSERVICE" as subservice,"Service Usage in Seconds" type,CASE WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) <=5 THEN '000-005' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 6 and 10 THEN '006-010' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 11 and 20 THEN '011-020' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 21 and 30 THEN '021-030' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 31 and 180 THEN '031-180'  WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 181 and 480 THEN '181-480' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) >= 481 THEN '480+sec' END as param_type,"0",count(*) from ivr_mastercalllogs where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and call_status='success' and call_type='incoming'  group by 1,2,3,4,5,6,7 union all

select left(end_datetime,10)as date,'OBD','',service,'OBD COUNT','OBD Attempt','',count(*) from ivr_mastercalllogs where call_type='outgoing' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all

select left(end_datetime,10)as date,'OBD','',service,'OBD COUNT',if(call_status='success','OBD Picked','OBD Not Picked') as type,'',count(*) from ivr_mastercalllogs where call_type='outgoing' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,2,3,4,5,6,7 union all

select left(end_datetime,10)as date,'OBD','',service,'OBD Failure Reasons',RLC_REASON,'',count(*) from ivr_mastercalllogs where call_type='outgoing' and call_status<>'success' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all

select left(se_reqrecvtime,10)as date,'OBD','',srv_id as service,'OBD Subscription','OBD Subscription' type,'',count(*) from subscription_logs where status='successful' and channel='obd' and request_type='8' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all

select left(end_datetime,10)as date,'OBD','',service,'Duration Stats', case when DURATION<=5 then '000-005' when DURATION>5 and DURATION<=10 then '006-010' when DURATION>10 and DURATION<=15 then '011-015' when DURATION>15 and DURATION<=20 then '016-020' when DURATION>21 and DURATION<31 then '021-030' when DURATION>30 and DURATION<=180 then '031-180'  when DURATION>180 and DURATION<=480 then '181-480' when DURATION>480 then '480+sec' end as ran,'', count(*) from ivr_mastercalllogs as im where call_type='outgoing' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7;

 

END */$$
DELIMITER ;

/* Procedure structure for procedure `Daily_Report_New` */

/*!50003 DROP PROCEDURE IF EXISTS  `Daily_Report_New` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `Daily_Report_New`()
BEGIN
SET @Date = DATE(NOW())-INTERVAL @DayInterval DAY;
SET @DayInterval='1';
INSERT INTO daily_report_new(ReportDate,ServiceName,Summary,Sub_Service_Name,Type,type_sub_param,price,Count)
/* Subscriber Base */
select report_date,service,'SUBSCRIBERS' as summary,sub_service as sub_service,'Subscriber Base' as Type,'Total Base' as status,'',sum(base) as count from subscriberbase where date(report_date) = (DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select report_date,service,'SUBSCRIBERS' as summary,sub_service as sub_service,'Subscriber Base' as Type,if(status='active','Active Base',if(status='grace','Grace Base',if(status='pending','Pending Base',if(status='parking','Parking Base',if(status='suspended','Suspended Base',status))))) as status,'',sum(base) as count from subscriberbase where date(report_date) = (DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
/* Channel Wise Churn/Activation */
select left(se_reqrecvtime,10)as ReportDate,if(sub_srvname like '%islamic','Religious',sub_srvname) as servicename,'' as summary,if(sub_srvname like '%Daily%','Daily Pack',if(sub_srvname like '%Weekly%','Weekly Pack',if(sub_srvname like '%month%','Monthly Pack',sub_srvname))) as sub_service_name,'Channel Wise Churn' as type,concat('Churn @',UPPER(channel)) as Type_Sub_Param,'0' as price,count(*) as total from subscription_logs_temp where status='successful' and request_type='2' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,if(sub_srvname like '%islamic','Religious',sub_srvname) as servicename,'' as summary,if(sub_srvname like '%Daily%','Daily Pack',if(sub_srvname like '%Weekly%','Weekly Pack',if(sub_srvname like '%month%','Monthly Pack',sub_srvname))) as sub_service_name,'Channel Wise Churn' as type,concat('Churn @',UPPER(channel)) as Type_Sub_Param,'0' as price,count(*) as total from subscription_logs_temp where req_desc = 'statechanger' and user_status like 'unsub' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,if(sub_srvname like '%islamic','Religious',sub_srvname) as servicename,'' as summary,if(Sub_Srvname like '%Daily%','Daily Pack',if(Sub_Srvname like '%Weekly%','Weekly Pack',if(Sub_Srvname like '%month%','Monthly Pack',Sub_Srvname))) as sub_service_name,'Channel Wise Activation' as type ,concat('Act @',UPPER(channel)) as Type_Sub_Param,Amount/10000 as price,count(*) as total from subscription_logs_temp where status='successful' and request_type ='3' and action='act'  and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,if(sub_srvname like '%islamic','Religious',sub_srvname) as servicename,'' as summary,if(Sub_Srvname like '%Daily%','Daily Pack',if(Sub_Srvname like '%Weekly%','Weekly Pack',if(Sub_Srvname like '%month%','Monthly Pack',Sub_Srvname))) as sub_service_name,'Channel Wise Activation' as type ,concat('Act @',UPPER(channel)) as Type_Sub_Param,Amount/10000 as price,count(*) as total from subscription_logs_temp where status='successful' and req_desc='subscribe' and request_type in ('1','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
/* Calls */
select left(end_datetime,10) as reportdate,if(service like 'religious%','Religious',service) as servicename,'' as summary,'' as sub_service_name,'Calls' as type,'Total Calls' as  type_sub_param,'0' as price,count(*) from ivr_mastercalllogs_temp where call_type='incoming' and SUB_STATUS is not null and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(end_datetime,10) as ReportDate,if(service like 'religious%','Religious',service) as ServiceName,'' as Summary,'' as Sub_service_name,'Calls' as type,case when length(b_party)>=8 then concat(if(sub_status='active','Active User',if(sub_status='new','First Time User','Low Balance User')),' (Friend Number Call)') when length(b_party)<8 then concat(if(sub_status='active','Active User',if(sub_status='new','First Time User','Low Balance User')),' (Short Code Call)') end as type_sub_param,'0' as price,count(*) as total from ivr_mastercalllogs_temp where call_type='incoming' and SUB_STATUS is not null and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all 
select left(end_datetime,10) as ReportDate,if(service like 'religious%','Religious',service) as ServiceName,'' as Summary,'' as Sub_service_name,'Calls' as type,concat('Unique ',case when length(b_party)>=8 then concat(if(sub_status='active','Active User',if(sub_status='new','First Time User','Low Balance User')),' (Friend Number Call)') when length(b_party)<8 then concat(if(sub_status='active','Active User',if(sub_status='new','First Time User','Low Balance User')),' (Short Code Call)') end) as type_sub_param,'0' as price,count(distinct a_party) as total from ivr_mastercalllogs_temp where call_type='incoming' and SUB_STATUS is not null and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select I.date as reportdate,if(I.service like 'religious%','Religious',I.service) as servicename,'' as summary,'' as sub_service_name,'Calls' as type,if(length(I.b_party)>=8,'First Time User Got Subscribed (Friend Number Call)','First Time User Got Subscribed (Short Code Call)') as type_sub_param,'0' as price,count(*) from (select left(end_datetime,10) as date,service,a_party as msisdn,b_party from ivr_mastercalllogs_temp where call_type='incoming' and sub_status='new' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)) as I join (select msisdn from subscription_logs_temp where status='successful' and channel='ivr' and request_type='1' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY)) as S on I.msisdn=S.msisdn group by 1,2,3,4,5,6,7 union all
select I.date as reportdate,if(I.service like 'religious%','Religious',I.service) as servicename,'' as summary,'' as sub_service_name,'Calls' as type,concat(if(I.sub_status='new',(case when C.status='success' then 'First Time User Call Connected' else 'First Time User Call Failed' end),(case when C.status='success' then 'Active User Call Connected' else 'Active User Call Failed' end)),' (Friend Number Call)') as type_sub_param,'0' as price,count(*) from (select left(end_datetime,10) as date,service,sub_status,master_id from ivr_mastercalllogs_temp where call_type='incoming' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)) as I join (select master_id,status from ivr_confcalllogs_temp where date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)) as C on I.master_id=C.master_id group by 1,2,3,4,5,6,7 union all
select C1.date as reportdate,if(C1.service like 'religious%','Religious',C1.service) as servicename,'' as summary,'' as sub_service_name,'Calls' as type,'Call Conference Success Ratio' as type_sub_param,'0' as price,round(C1.total/C2.total*100) as total from (select left(end_datetime,10) as date,service,status,count(*) as total from ivr_confcalllogs_temp where status='success' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)) C1 join (select left(end_datetime,10) as date,count(*) as total from ivr_confcalllogs_temp where date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)) C2 on C1.date=C2.date group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'religious%','Religious',service) as servicename,'' as summary,'' as sub_service_name,'Conf Call Failure Reason' as type,if(REASON like 'noanswer','No Answer',if(REASON like 'rlcsent','A Party Hangup','MSC Disconnect')) as type_sub_param,'0' as price,count(*) as total  from ivr_confcalllogs_temp where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and status<>'success' group by 1,2,3,4,5,6,7 union all
/* MOU */
select left(END_DATETIME,10) as ReportDate,if(service like 'religious%','Religious',service) as servicename,'' as summary,'' as sub_service_name,'MOU' as type,'Total MOU' as type_param,'0' as price, round(sum(DURATION)/60) as total from ivr_mastercalllogs_temp where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10) as ReportDate,if(service like 'religious%','Religious',service) as servicename,'' as summary,'' as sub_service_name,'MOU' as type,case when length(B_PARTY)>=8 and SUB_STATUS='active' then 'Active User MOU (Friend No Call)' when length(B_PARTY)>=8 and SUB_STATUS='new' then 'First Time User MOU (Friend No Call)' when length(B_PARTY)>=8 and SUB_STATUS not in ('new','active') then 'Low Balance User MOU (Friend No Call)' when length(B_PARTY)<8 and SUB_STATUS='active' then 'Active User MOU (Short Code Call)' when length(B_PARTY)<8 and SUB_STATUS='new' then 'First Time User MOU (Short Code Call)' when length(B_PARTY)<8 and SUB_STATUS not in ('new','active') then 'Low Balance User MOU (Short Code Call)' end as type_param,'0' as price, round(sum(DURATION)/60) as Total from ivr_mastercalllogs_temp where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
/* New User Stats | IVR */
select (DATE(NOW())-INTERVAL @DayInterval DAY) as ReportDate,if(UTS.service like 'religious%','Religious',UTS.service) as servicename,'' as summary, '' as sub_service_name,'First Time User Call Stats | IVR' as type,'% First Time User Attempted to Subscribe' as type_sub_param,'0' as price,round((UTS.tryed/A.attempt)*100) as Total from (select M.service,count(*) as tryed from ivr_mastercalllogs_temp M, url_mastercalllogs_temp U where date(M.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.SUB_STATUS='new' and  M.call_type='incoming' and date(U.URL_HIT_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.master_id=U.master_id group by 1) UTS RIGHT JOIN (select M.service,count(*) as attempt from ivr_mastercalllogs_temp M where date(M.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.SUB_STATUS='new' and  call_type='incoming'  group by 1) A ON A.service=UTS.service union all
select left(END_DATETIME,10) as ReportDate,if(service like 'religious%','Religious',service) as servicename,'' as summary,'' as sub_service_name,'First Time User Call Stats | IVR' as type,CASE WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) <=5 THEN '000-005' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 6 and 10 THEN '006-010' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 11 and 20 THEN '011-020' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 21 and 30 THEN '021-030' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 31 and 60 THEN '031-060' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 61 and 90 THEN '061-090' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 91 and 120 THEN '091-120' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 121 and 150 THEN '121-150' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 151 and 180 THEN '151-180' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 181 and 480 THEN '181-480' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) >= 480 THEN '480+sec' END as param_type,'0' as price,count(*) as total from ivr_mastercalllogs_temp where call_type='incoming' AND date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and call_status='success' and SUB_STATUS='new' group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10) as ReportDate,if(service like 'religious%','Religious',service) as servicename,'' as summary,'' as sub_service_name,'Active User Call Stats | IVR' as type,CASE WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) <=5 THEN '000-005' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 6 and 10 THEN '006-010' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 11 and 20 THEN '011-020' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 21 and 30 THEN '021-030' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 31 and 60 THEN '031-060' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 61 and 90 THEN '061-090' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 91 and 120 THEN '091-120' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 121 and 150 THEN '121-150' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 151 and 180 THEN '151-180' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 181 and 480 THEN '181-480' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) >= 480 THEN '480+sec' END as param_type,'0' as price,count(*) as total from ivr_mastercalllogs_temp where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and call_status='success' and SUB_STATUS='active' group by 1,2,3,4,5,6,7 union all

select left(END_DATETIME,10) as ReportDate,if(service like 'religious%','Religious',service) as servicename,'' as summary,'' as sub_service_name,'Low Balance User Call Stats | IVR' as type,CASE WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) <=5 THEN '000-005' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 6 and 10 THEN '006-010' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 11 and 20 THEN '011-020' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 21 and 30 THEN '021-030' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 31 and 60 THEN '031-060' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 61 and 90 THEN '061-090' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 91 and 120 THEN '091-120' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 121 and 150 THEN '121-150' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 151 and 180 THEN '151-180' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 181 and 480 THEN '181-480' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) >= 480 THEN '480+sec' END as param_type,'0' as price,count(*) as total from ivr_mastercalllogs_temp where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and call_status='success' and SUB_STATUS not in ('active','new') group by 1,2,3,4,5,6,7 union all
/* Language, Ambience, Voice Frequencies */
select left(END_DATETIME,10) as ReportDate,if(service like 'religious%','Religious',service) as service_name,'' as summary,'' sub_service_name,'Language' as type,if(Language='_e','English',if(Language='_a','Arabic',Language)) as type_sub_param,'' as price,count(*) as total from ivr_mastercalllogs_temp where CALL_STATUS = 'success' and call_type='incoming' and SUB_STATUS is not null and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
/*SELECT LEFT(`special_effect_time`,10) AS ReportDATE,'Magicvoice' AS servicename,'' as summary,'' sub_service_name,'Voice Frequencies' as type,`special_effect_value` AS type_sub_param,'' as price,COUNT(*) as total FROM `ivr_specialeffect` WHERE DATE(`special_effect_time`)=(DATE(NOW())-INTERVAL @DayInterval DAY) GROUP BY 1,2,3,4,5,6,7 union all
SELECT LEFT(`start_datetime`,10) AS DATE,'Magicvoice' AS servicename,'' as summary,"NOSUBSERVICE" as sub_service_name,'Ambiance',IF(media_file LIKE '%hbd%','BirthDay',IF(media_file LIKE '%airport%','Airport',IF(media_file LIKE '%traffic%','Traffic',IF(media_file LIKE '%football%','Football',media_file)))) AS ambi,'',COUNT(*) FROM `media_mastercalllogs` WHERE (media_file like '%ambiance%' or media_file like '%ambience%') AND  DATE(`start_datetime`)=(DATE(NOW())-INTERVAL @DayInterval DAY) GROUP BY 1,2,3,4,5,6,7 union all */
/* Revenue/Count */
select left(se_reqrecvtime,10)as ReportDate,if(sub_srvname like '%islamic','Religious',sub_srvname) as servicename,'',if(Sub_Srvname like '%Daily%','Daily Pack',if(Sub_Srvname like '%Weekly%','Weekly Pack',if(Sub_Srvname like '%month%','Monthly Pack',Sub_Srvname))) as sub_service_name,if(req_desc='subscribe','Activation Revenue',if(req_desc='renewal','Renewal Revenue',req_desc)) as type ,concat('Rev @',amount/10000,'GHS') as Type_Sub_Param,Amount/10000 as price,round(count(*)*amount/10000) from subscription_logs_temp where status='successful' and request_type in ('1','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,if(sub_srvname like '%islamic','Religious',sub_srvname) as servicename,'',if(Sub_Srvname like '%Daily%','Daily Pack',if(Sub_Srvname like '%Weekly%','Weekly Pack',if(Sub_Srvname like '%month%','Monthly Pack',Sub_Srvname))) as sub_service_name,if(action='act','Activation Revenue Retry',if(action='renew','Renewal Revenue Retry',req_desc)) as type ,concat('Rev @',amount/10000,'GHS') as Type_Sub_Param,Amount/10000 as price,round(count(*)*amount/10000) from subscription_logs_temp where status='successful' and request_type='3' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,if(sub_srvname like '%islamic','Religious',sub_srvname) as servicename,'',if(Sub_Srvname like '%Daily%','Daily Pack',if(Sub_Srvname like '%Weekly%','Weekly Pack',if(Sub_Srvname like '%month%','Monthly Pack',Sub_Srvname))) as sub_service_name,if(req_desc='subscribe','Activation Count',if(req_desc='renewal','Renewal Count',req_desc)) as type ,concat(if(Sub_Srvname like '%Daily%','Daily Pack',if(Sub_Srvname like '%Weekly%','Weekly Pack',if(Sub_Srvname like '%month%','Monthly Pack',Sub_Srvname))),' @',amount/10000) as Type_Sub_Param,Amount/10000 as amount,count(*) from subscription_logs_temp where status='successful' and request_type in ('1','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,if(sub_srvname like '%islamic','Religious',sub_srvname) as servicename,'',if(Sub_Srvname like '%Daily%','Daily Pack',if(Sub_Srvname like '%Weekly%','Weekly Pack',if(Sub_Srvname like '%month%','Monthly Pack',Sub_Srvname))) as sub_service_name,if(action='act','Activation Count Retry',if(action='renew','Renewal Count Retry',req_desc)) as type ,concat(if(Sub_Srvname like '%Daily%','Daily Pack',if(Sub_Srvname like '%Weekly%','Weekly Pack',if(Sub_Srvname like '%month%','Monthly Pack',Sub_Srvname))),' @',amount/10000) as Type_Sub_Param,Amount/10000 as amount,count(*) from subscription_logs_temp where status='successful' and request_type='3' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
/* Billing Details */
select left(se_reqrecvtime,10)as ReportDate,if(sub_srvname like '%islamic','Religious',sub_srvname) as servicename,'' as summary,'' as sub_service_name,'Success Billing Requests' as Type,if(sub_srvname like '%Daily%','Daily Pack',if(sub_srvname like '%Weekly%','Weekly Pack',if(sub_srvname like '%month%','Monthly Pack',sub_srvname))) Type_Sub_Param,"0" as price,count(*) from subscription_logs_temp where request_type in ('1','3','4') and status='successful' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,if(sub_srvname like '%islamic','Religious',sub_srvname) as servicename,'' as summary,'' as sub_service_name,'Billing Request Failed Reasons' as Type,resp_desc as type_sub_param,"0" as price,count(*) from subscription_logs_temp where request_type in ('1','3','4') and status!='successful' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,if(sub_srvname like '%islamic','Religious',sub_srvname) as servicename,'' as summary,'' as sub_service_name,'Billing Requests' as Type,if(sub_srvname like '%Daily%','Daily Pack',if(sub_srvname like '%Weekly%','Weekly Pack',if(sub_srvname like '%month%','Monthly Pack',sub_srvname))) Type_Sub_Param,"0" as price,count(*) from subscription_logs_temp where request_type in ('1','3','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,if(sub_srvname like '%islamic','Religious',sub_srvname) as servicename,'' as summary,'' as sub_service_name,'Unique Billing Requests' as Type,if(sub_srvname like '%Daily%','Daily Pack',if(sub_srvname like '%Weekly%','Weekly Pack',if(sub_srvname like '%month%','Monthly Pack',sub_srvname))) Type_Sub_Param,"0" as price,count(distinct msisdn) from subscription_logs_temp where request_type in ('1','3','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
/* OBD */
select left(end_datetime,10)as date,'OBD','',if(service like 'religious%','Religious',service) as service,'OBD COUNT','OBD Attempt','',count(*) from ivr_mastercalllogs_temp where call_type='outgoing' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all
select left(end_datetime,10)as date,'OBD','',if(service like 'religious%','Religious',service) as service,'OBD COUNT',if(call_status='success','OBD Picked','OBD Not Picked') as type,'',count(*) from ivr_mastercalllogs_temp where call_type='outgoing' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,2,3,4,5,6,7 union all
select left(ivm.end_datetime,10)as date,'OBD','',if(ivm.service like 'religious%','Religious',ivm.service) as service,'OBD COUNT','OBD SUCESS %','',round((select count(*) from ivr_mastercalllogs_temp as im where im.call_type='outgoing' and im.call_status='success' and ivm.service = im.service and date(im.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY))/count(*)*100)as count from ivr_mastercalllogs_temp as ivm where ivm.call_type='outgoing' and date(ivm.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all
select left(end_datetime,10)as date,'OBD','',if(service like 'religious%','Religious',service) as service,'OBD Failure Reasons',RLC_REASON,'',count(*) from ivr_mastercalllogs_temp where call_type='outgoing' and call_status<>'success' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as date,'OBD','',if(sub_srvname like '%islamic','Religious',sub_srvname) as service,'OBD Subscription','' type,'',count(*) from subscription_logs_temp where status='successful' and request_type='1' and channel='OBD' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
/*select left(imc.end_datetime,10)as date,'OBD','',if(imc.service like 'religious%','Religious',imc.service) as service,'OBD Subscription','OBD Subscription %','',round((select count(*) from subscription_logs_temp as im where im.action = 'act' and status='successful' and im.request_type in ('1') and im.channel='OBD' and im.srv_name=SUBSTRING_INDEX(imc.service,'obd',-1)  and date(im.se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY))/count(*)*100,2)as count from ivr_mastercalllogs_temp as imc where imc.call_type='outgoing' and imc.call_status='success' and date(imc.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all*/
select left(se_reqrecvtime,10)as date,'OBD','',if(sub_srvname like '%islamic','Religious',sub_srvname) as service,'OBD Subscription Revenue',if(req_desc='renewal','OBD Renewal Revenue','OBD Activation Revenue') type,amount,ROUND((count(*)*(amount/10000))) as revenue from subscription_logs_temp where  status='successful' and request_type in ('1','4') and channel='OBD' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as date,'OBD','',if(sub_srvname like '%islamic','Religious',sub_srvname) as service,'OBD Subscription Attempt','' as type_sub_param,'',count(*) as cnt from subscription_logs_temp where request_type ='1' and channel='OBD' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select date(URL_HIT_DATETIME)as date,'OBD' Servicename,'' as summary,if(substring_index(SUBSTRING_INDEX(URL_STRING,'serviceid=',-1),'&',1)='religious','Religious',if(substring_index(SUBSTRING_INDEX(URL_STRING,'serviceid=',-1),'&',1)='229012000003233','Christianity',if(substring_index(SUBSTRING_INDEX(URL_STRING,'serviceid=',-1),'&',1)='229012000003232','Islamic',substring_index(SUBSTRING_INDEX(URL_STRING,'serviceid=',-1),'&',1)))) as subservice,'OBD PRESS SUB DTMF' type,'OBD PRESS SUB DTMF' sub_type,'' type,count(*) from url_mastercalllogs_temp where substring_index(SUBSTRING_INDEX(URL_STRING,'channel=',-1),'&',1) ='obd' and date(URL_HIT_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(end_datetime,10)as date,'OBD','',if(service like 'religious%','Religious',service),'Duration Stats', case when DURATION<=5 then '000-005' when DURATION>5 and DURATION<=10 then '006-010' when DURATION>10 and DURATION<=15 then '011-015' when DURATION>15 and DURATION<=20 then '016-020' when DURATION>20 and DURATION<=30 then '021-030' when DURATION>30 and DURATION<=45 then '031-045' when DURATION>45 and DURATION<=60 then '046-060' when DURATION>60 and DURATION<=75 then '061-075' when DURATION>75 and DURATION<=90 then '076-090' when DURATION>90 and DURATION<=120 then '091-120' when DURATION>120 and DURATION<=180 then '121-180' when DURATION>180 and DURATION<=480 then '181-480' when DURATION>480 then '480+' end as ran,'', count(*) from ivr_mastercalllogs_temp as im where call_type='outgoing' and CALL_STATUS='success' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7;
delete from daily_report_new where count is null;

update daily_report_new set servicename='LiveFather' where servicename in ('DailyFather','livefather','LiveFather');

update daily_report_new set ServiceName='Religious' where ServiceName='rp revamp 2017';
END */$$
DELIMITER ;

/* Procedure structure for procedure `Databasebackup` */

/*!50003 DROP PROCEDURE IF EXISTS  `Databasebackup` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `Databasebackup`()
BEGIN
Set @Nextmonth=replace(left(DATE(NOW()) - INTERVAL 2 DAY,7),'-','');
SET @tablename1=concat('insert into cdrlog_backup.billing_logs_',@Nextmonth,' select * from cilling_logs where be_reqrecvtime < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d")');
SET @tablename2=concat('insert into cdrlog_backup.dtmf_mastercalllogs_',@Nextmonth,' select * from ctmf_mastercalllogs where DTMF_INPUT_DATETIME < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d")');
SET @tablename3=concat('insert into  cdrlog_backup.ivr_confcalllogs_',@Nextmonth,' select * from cvr_confcalllogs where END_DATETIME < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d")');
SET @tablename4=concat('insert into  cdrlog_backup.ivr_mastercalllogs_',@Nextmonth,' select * from cvr_mastercalllogs where END_DATETIME < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d")');
SET @tablename5=concat('insert into  cdrlog_backup.ivr_specialeffect_',@Nextmonth,' select * from cvr_specialeffect where special_effect_time < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d")');
SET @tablename6=concat('insert into  cdrlog_backup.media_mastercalllogs_',@Nextmonth,' select * from cedia_mastercalllogs where START_DATETIME < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d")');
SET @tablename7=concat('insert into  cdrlog_backup.subscription_logs_',@Nextmonth,' select * from cubscription_logs where se_reqrecvtime < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d")');
SET @tablename8=concat('insert into  cdrlog_backup.url_mastercalllogs_',@Nextmonth,' select * from crl_mastercalllogs where URL_HIT_DATETIME < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d")');

SET @tablename9=concat('insert into cdrlog_backup.smslogs_',@Nextmonth,' select * from cmslogs where SUBMIT_TIME < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d") or receive_time  < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d")');
Prepare stmt1 FROM @tablename1;
Execute stmt1;
DEALLOCATE PREPARE stmt1;
Prepare stmt2 FROM @tablename2;
Execute stmt2;
DEALLOCATE PREPARE stmt2;
Prepare stmt3 FROM @tablename3;
Execute stmt3;
DEALLOCATE PREPARE stmt3;
Prepare stmt4 FROM @tablename4;
Execute stmt4;
DEALLOCATE PREPARE stmt4;
Prepare stmt5 FROM @tablename5;
Execute stmt5;
DEALLOCATE PREPARE stmt5;
Prepare stmt6 FROM @tablename6;
Execute stmt6;
DEALLOCATE PREPARE stmt6;
Prepare stmt7 FROM @tablename7;
Execute stmt7;
DEALLOCATE PREPARE stmt7;
Prepare stmt8 FROM @tablename8;
Execute stmt8;
DEALLOCATE PREPARE stmt8;

Prepare stmt9 FROM @tablename9;

Execute stmt9;

DEALLOCATE PREPARE stmt9;
delete  from cilling_logs where be_reqrecvtime < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d");
delete  from ctmf_mastercalllogs where DTMF_INPUT_DATETIME < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d");
delete  from cvr_confcalllogs where END_DATETIME < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d");
delete  from cvr_mastercalllogs where END_DATETIME < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d");
delete  from cvr_specialeffect where special_effect_time < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d");
delete  from cedia_mastercalllogs where START_DATETIME < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d");
delete  from crl_mastercalllogs where URL_HIT_DATETIME < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d");
delete from cubscription_logs where  se_reqrecvtime < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d");

delete from cmslogs where submit_time  < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d") OR receive_time  < DATE_FORMAT(Date(NOW()) - INTERVAL 1 Day,"%Y-%m-%d");
END */$$
DELIMITER ;

/* Procedure structure for procedure `fill_base` */

/*!50003 DROP PROCEDURE IF EXISTS  `fill_base` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `fill_base`()
BEGIN

insert into subscriberbase (report_date,service,sub_service,status,base)

select date(DATE_SUB(NOW(), INTERVAL 1 DAY)) as date,if(service_id like '%mv%','Magicvoice',if(service_id like '0300399644%','Islamic',if(service_id like '%Chris%','Christianity',service_id))) as service,if(subservice_id like '%Daily%','Daily',if(subservice_id like '%Monthly%','Monthly',if(subservice_id like '%Weekly%','Weekly',subservice_id))) as subservice_id,status,count(*) from subs_engine0.subscription group by 1,2,3,4 union all

select date(DATE_SUB(NOW(), INTERVAL 1 DAY)) as date,if(service_id like '%mv%','Magicvoice',if(service_id like '0300399644%','Islamic',if(service_id like '%Chris%','Christianity',service_id))) as service,if(subservice_id like '%Daily%','Daily',if(subservice_id like '%Monthly%','Monthly',if(subservice_id like '%Weekly%','Weekly',subservice_id))) as subservice_id,status,count(*) from subs_engine1.subscription group by 1,2,3,4 union all

select date(DATE_SUB(NOW(), INTERVAL 1 DAY)) as date,if(service_id like '%mv%','Magicvoice',if(service_id like '0300399644%','Islamic',if(service_id like '%Chris%','Christianity',service_id))) as service,if(subservice_id like '%Daily%','Daily',if(subservice_id like '%Monthly%','Monthly',if(subservice_id like '%Weekly%','Weekly',subservice_id))) as subservice_id,status,count(*) from subs_engine2.subscription group by 1,2,3,4 union all

select date(DATE_SUB(NOW(), INTERVAL 1 DAY)) as date,if(service_id like '%mv%','Magicvoice',if(service_id like '0300399644%','Islamic',if(service_id like '%Chris%','Christianity',service_id))) as service,if(subservice_id like '%Daily%','Daily',if(subservice_id like '%Monthly%','Monthly',if(subservice_id like '%Weekly%','Weekly',subservice_id))) as subservice_id,status,count(*) from subs_engine3.subscription group by 1,2,3,4 union all

select date(DATE_SUB(NOW(), INTERVAL 1 DAY)) as date,if(service_id like '%mv%','Magicvoice',if(service_id like '0300399644%','Islamic',if(service_id like '%Chris%','Christianity',service_id))) as service,if(subservice_id like '%Daily%','Daily',if(subservice_id like '%Monthly%','Monthly',if(subservice_id like '%Weekly%','Weekly',subservice_id))) as subservice_id,status,count(*) from subs_engine4.subscription group by 1,2,3,4 union all

select date(DATE_SUB(NOW(), INTERVAL 1 DAY)) as date,if(service_id like '%mv%','Magicvoice',if(service_id like '0300399644%','Islamic',if(service_id like '%Chris%','Christianity',service_id))) as service,if(subservice_id like '%Daily%','Daily',if(subservice_id like '%Monthly%','Monthly',if(subservice_id like '%Weekly%','Weekly',subservice_id))) as subservice_id,status,count(*) from subs_engine5.subscription group by 1,2,3,4 union all

select date(DATE_SUB(NOW(), INTERVAL 1 DAY)) as date,if(service_id like '%mv%','Magicvoice',if(service_id like '0300399644%','Islamic',if(service_id like '%Chris%','Christianity',service_id))) as service,if(subservice_id like '%Daily%','Daily',if(subservice_id like '%Monthly%','Monthly',if(subservice_id like '%Weekly%','Weekly',subservice_id))) as subservice_id,status,count(*) from subs_engine6.subscription group by 1,2,3,4 union all

select date(DATE_SUB(NOW(), INTERVAL 1 DAY)) as date,if(service_id like '%mv%','Magicvoice',if(service_id like '0300399644%','Islamic',if(service_id like '%Chris%','Christianity',service_id))) as service,if(subservice_id like '%Daily%','Daily',if(subservice_id like '%Monthly%','Monthly',if(subservice_id like '%Weekly%','Weekly',subservice_id))) as subservice_id,status,count(*) from subs_engine7.subscription group by 1,2,3,4 union all

select date(DATE_SUB(NOW(), INTERVAL 1 DAY)) as date,if(service_id like '%mv%','Magicvoice',if(service_id like '0300399644%','Islamic',if(service_id like '%Chris%','Christianity',service_id))) as service,if(subservice_id like '%Daily%','Daily',if(subservice_id like '%Monthly%','Monthly',if(subservice_id like '%Weekly%','Weekly',subservice_id))) as subservice_id,status,count(*) from subs_engine8.subscription group by 1,2,3,4 union all

select date(DATE_SUB(NOW(), INTERVAL 1 DAY)) as date,if(service_id like '%mv%','Magicvoice',if(service_id like '0300399644%','Islamic',if(service_id like '%Chris%','Christianity',service_id))) as service,if(subservice_id like '%Daily%','Daily',if(subservice_id like '%Monthly%','Monthly',if(subservice_id like '%Weekly%','Weekly',subservice_id))) as subservice_id,status,count(*) from subs_engine9.subscription group by 1,2,3,4;

END */$$
DELIMITER ;

/* Procedure structure for procedure `new_e1` */

/*!50003 DROP PROCEDURE IF EXISTS  `new_e1` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `new_e1`()
BEGIN
DECLARE DayInterval int(11);
DECLARE ondate date; 
DECLARE mbh int(11);
DECLARE incoming_count int(11);
DECLARE outcoing_count int(11);
DECLARE conf_count int(11);
DECLARE incoming_mou int(11);
DECLARE outgoing_mou int(11);
DECLARE conf_mou int(11);
DECLARE total_mou int(11);
DECLARE channel_count int(11);
DECLARE erlang_capacity	 int(11);
DECLARE erlang DECIMAL(11,2);
DECLARE utilization DECIMAL(11,2);
SET channel_count=480;
SET erlang_capacity=466;
SET DayInterval=1;
SET ondate=(DATE(NOW())-INTERVAL DayInterval DAY);
select h into mbh from ( select h,sum(d) from (
select hour(end_Datetime) as h,sum(duration) as d from ivr_mastercalllogs_temp where date(END_DATETIME)=ondate group by 1 union all
select hour(end_Datetime) as h,sum(duration) as d from ivr_confcalllogs_temp where date(END_DATETIME)=ondate   group by 1
) A group by 1 order by 2 desc) B limit 1;
select count(*) into incoming_count from ivr_mastercalllogs_temp where date(END_DATETIME)=ondate and hour(END_DATETIME)=mbh and call_type='incoming';
select count(*) into outcoing_count from ivr_mastercalllogs_temp where date(END_DATETIME)=ondate and hour(END_DATETIME)=mbh and call_type='outgoing';
select count(*) into conf_count from ivr_confcalllogs_temp where date(END_DATETIME)=ondate and hour(END_DATETIME)=mbh;
select sum(duration)/60 into incoming_mou from ivr_mastercalllogs_temp where date(END_DATETIME)=ondate and hour(END_DATETIME)=mbh and call_type='incoming';
select sum(duration)/60 into outgoing_mou from ivr_mastercalllogs_temp where date(END_DATETIME)=ondate and hour(END_DATETIME)=mbh and call_type='outgoing';
select sum(duration)/60 into conf_mou from ivr_confcalllogs_temp where date(END_DATETIME)=ondate and hour(END_DATETIME)=mbh;
IF(incoming_mou is null) then set incoming_mou=0; END IF;
IF(outgoing_mou is null) then set outgoing_mou=0; END IF;
IF(conf_mou is null) then set conf_mou=0; END IF;
SET total_mou=incoming_mou+outgoing_mou+conf_mou;
set erlang=total_mou/60;
set utilization=erlang*100/erlang_capacity;
insert into newe1utilization(date,mbh,incoming_count,outcoing_count,conf_count,incoming_mou,outgoing_mou,conf_mou,total_mou,channel_count,erlang_capacity,erlang,utilization)
select ondate,mbh,incoming_count,outcoing_count,conf_count,incoming_mou,outgoing_mou,conf_mou,total_mou,channel_count,erlang_capacity,erlang,utilization;
END */$$
DELIMITER ;

/* Procedure structure for procedure `new_e1_temp` */

/*!50003 DROP PROCEDURE IF EXISTS  `new_e1_temp` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `new_e1_temp`(in inter int(11))
BEGIN
DECLARE DayInterval int(11);
DECLARE ondate date; 
DECLARE mbh int(11);
DECLARE incoming_count int(11);
DECLARE outcoing_count int(11);
DECLARE conf_count int(11);
DECLARE incoming_mou int(11);
DECLARE outgoing_mou int(11);
DECLARE conf_mou int(11);
DECLARE total_mou int(11);
DECLARE channel_count int(11);
DECLARE erlang_capacity	 int(11);
DECLARE erlang DECIMAL(11,2);
DECLARE utilization DECIMAL(11,2);
SET channel_count=1200;
SET erlang_capacity=1195;
SET DayInterval=inter;
SET ondate=(DATE(NOW())-INTERVAL DayInterval DAY);
select h into mbh from ( select h,sum(d) from (
select hour(end_Datetime) as h,sum(duration) as d from cdrlog_backup.ivr_mastercalllogs_201503 where date(END_DATETIME)=ondate group by 1 union all
select hour(end_Datetime) as h,sum(duration) as d from cdrlog_backup.ivr_confcalllogs_201503 where date(END_DATETIME)=ondate   group by 1
) A group by 1 order by 2 desc) B limit 1;
select count(*) into incoming_count from cdrlog_backup.ivr_mastercalllogs_201503 where date(END_DATETIME)=ondate and hour(END_DATETIME)=mbh and call_type='incoming';
select count(*) into outcoing_count from cdrlog_backup.ivr_mastercalllogs_201503 where date(END_DATETIME)=ondate and hour(END_DATETIME)=mbh and call_type='outgoing';
select count(*) into conf_count from cdrlog_backup.ivr_confcalllogs_201503 where date(END_DATETIME)=ondate and hour(END_DATETIME)=mbh;
select sum(duration)/60 into incoming_mou from cdrlog_backup.ivr_mastercalllogs_201503 where date(END_DATETIME)=ondate and hour(END_DATETIME)=mbh and call_type='incoming';
select sum(duration)/60 into outgoing_mou from cdrlog_backup.ivr_mastercalllogs_201503 where date(END_DATETIME)=ondate and hour(END_DATETIME)=mbh and call_type='outgoing';
select sum(duration)/60 into conf_mou from cdrlog_backup.ivr_confcalllogs_201503 where date(END_DATETIME)=ondate and hour(END_DATETIME)=mbh;
IF(incoming_mou is null) then set incoming_mou=0; END IF;
IF(outgoing_mou is null) then set outgoing_mou=0; END IF;
IF(conf_mou is null) then set conf_mou=0; END IF;
SET total_mou=incoming_mou+outgoing_mou+conf_mou;
set erlang=total_mou/60;
set utilization=erlang*100/erlang_capacity;
insert into newe1utilization(date,mbh,incoming_count,outcoing_count,conf_count,incoming_mou,outgoing_mou,conf_mou,total_mou,channel_count,erlang_capacity,erlang,utilization)
select ondate,mbh,incoming_count,outcoing_count,conf_count,incoming_mou,outgoing_mou,conf_mou,total_mou,channel_count,erlang_capacity,erlang,utilization;
END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;



/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - global
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `global`;

USE `global`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `billingengineproperty` */

DROP TABLE IF EXISTS `billingengineproperty`;

CREATE TABLE `billingengineproperty` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `property` varchar(50) NOT NULL,
  `value` text NOT NULL,
  `relatedTo` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

/*Table structure for table `billingexecuter_properties` */

DROP TABLE IF EXISTS `billingexecuter_properties`;

CREATE TABLE `billingexecuter_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(100) DEFAULT NULL,
  `value` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

/*Table structure for table `blacklisted` */

DROP TABLE IF EXISTS `blacklisted`;

CREATE TABLE `blacklisted` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdn` int(15) DEFAULT NULL,
  `isSeries` tinyint(1) DEFAULT NULL,
  `serviceId` int(11) DEFAULT NULL,
  `blacklistedMsg` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `serviceId` (`serviceId`),
  KEY `serviceinfofk_idx` (`serviceId`),
  CONSTRAINT `serviceinfofk` FOREIGN KEY (`serviceId`) REFERENCES `service_info` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `blackouthours` */

DROP TABLE IF EXISTS `blackouthours`;

CREATE TABLE `blackouthours` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subServiceId` int(11) NOT NULL,
  `retryStartHour` time DEFAULT NULL,
  `retryEndHour` time DEFAULT NULL,
  `renewalStartHour` time DEFAULT NULL,
  `renewalEndHour` time DEFAULT NULL,
  `renewalMsgStartHour` time DEFAULT NULL,
  `renewalMsgEndHour` time DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `subServiceId` (`subServiceId`),
  KEY `subserviceinfofk_idx` (`subServiceId`),
  CONSTRAINT `subserviceinfofk` FOREIGN KEY (`subServiceId`) REFERENCES `sub_service_info` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `cci_properties` */

DROP TABLE IF EXISTS `cci_properties`;

CREATE TABLE `cci_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(100) DEFAULT NULL,
  `value` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/*Table structure for table `cdrexecuter_properties` */

DROP TABLE IF EXISTS `cdrexecuter_properties`;

CREATE TABLE `cdrexecuter_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(100) DEFAULT NULL,
  `value` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `cdruploader_properties` */

DROP TABLE IF EXISTS `cdruploader_properties`;

CREATE TABLE `cdruploader_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(100) DEFAULT NULL,
  `value` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

/*Table structure for table `channels_info` */

DROP TABLE IF EXISTS `channels_info`;

CREATE TABLE `channels_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `channels_info_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `channelsallowed` */

DROP TABLE IF EXISTS `channelsallowed`;

CREATE TABLE `channelsallowed` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `CP_Id` int(11) NOT NULL,
  `Channel_Id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_channelsallowed` (`CP_Id`),
  KEY `FK_channelsallowed_channel` (`Channel_Id`),
  CONSTRAINT `FK_channelsallowed` FOREIGN KEY (`CP_Id`) REFERENCES `cpdetail` (`id`),
  CONSTRAINT `FK_channelsallowed_channel` FOREIGN KEY (`Channel_Id`) REFERENCES `channels_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `charging_details` */

DROP TABLE IF EXISTS `charging_details`;

CREATE TABLE `charging_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `step_detail` varchar(15) NOT NULL,
  `charging_url_sync` text NOT NULL,
  `getBalance_url_sync` text NOT NULL,
  `charging_first` int(2) NOT NULL,
  `sub_service_id` int(11) NOT NULL,
  `charging_url_async` text NOT NULL,
  `delete_url_async` text NOT NULL,
  `billingMode` varchar(15) NOT NULL,
  `topUpUrl` varchar(500) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_charging_details` (`sub_service_id`),
  CONSTRAINT `FK_charging_details` FOREIGN KEY (`sub_service_id`) REFERENCES `sub_service_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

/*Table structure for table `circle_mapping` */

DROP TABLE IF EXISTS `circle_mapping`;

CREATE TABLE `circle_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(30) DEFAULT NULL,
  `isserise` varchar(1) DEFAULT NULL,
  `circle` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `coreengine_properties` */

DROP TABLE IF EXISTS `coreengine_properties`;

CREATE TABLE `coreengine_properties` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `KEY` varchar(25) NOT NULL,
  `VALUE` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

/*Table structure for table `country` */

DROP TABLE IF EXISTS `country`;

CREATE TABLE `country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country_name` varchar(255) DEFAULT NULL,
  `dbname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `country_info` */

DROP TABLE IF EXISTS `country_info`;

CREATE TABLE `country_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country` varchar(9) DEFAULT NULL,
  `currency` varchar(9) DEFAULT NULL,
  `countryCode` varchar(9) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `coutrydetails` */

DROP TABLE IF EXISTS `coutrydetails`;

CREATE TABLE `coutrydetails` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country` varchar(50) NOT NULL,
  `countryCode` varchar(3) NOT NULL,
  `currencyCode` varchar(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=191 DEFAULT CHARSET=utf8;

/*Table structure for table `cpdetail` */

DROP TABLE IF EXISTS `cpdetail`;

CREATE TABLE `cpdetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cpName` varchar(50) NOT NULL,
  `callbackUrl` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpName` (`cpName`),
  KEY `cpdetail_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;

/*Table structure for table `filezippercdr_properties` */

DROP TABLE IF EXISTS `filezippercdr_properties`;

CREATE TABLE `filezippercdr_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(100) DEFAULT NULL,
  `value` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

/*Table structure for table `ipwhitelist` */

DROP TABLE IF EXISTS `ipwhitelist`;

CREATE TABLE `ipwhitelist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cpDetailId` int(11) NOT NULL,
  `whitelistedIp` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_CpDetail` (`cpDetailId`),
  KEY `ipwtlist_cpdid_wtlip` (`cpDetailId`,`whitelistedIp`),
  CONSTRAINT `FK_CpDetail` FOREIGN KEY (`cpDetailId`) REFERENCES `cpdetail` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

/*Table structure for table `language` */

DROP TABLE IF EXISTS `language`;

CREATE TABLE `language` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `language` varchar(25) NOT NULL,
  `lang_name` varchar(25) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `language` (`language`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `logcounter_properties` */

DROP TABLE IF EXISTS `logcounter_properties`;

CREATE TABLE `logcounter_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(100) DEFAULT NULL,
  `value` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Table structure for table `misreport_properties` */

DROP TABLE IF EXISTS `misreport_properties`;

CREATE TABLE `misreport_properties` (
  `id` int(11) DEFAULT NULL,
  `key` varchar(50) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `msisdn_series_mapping` */

DROP TABLE IF EXISTS `msisdn_series_mapping`;

CREATE TABLE `msisdn_series_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdn_series` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `operator` */

DROP TABLE IF EXISTS `operator`;

CREATE TABLE `operator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operator_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `price_validity_mapping` */

DROP TABLE IF EXISTS `price_validity_mapping`;

CREATE TABLE `price_validity_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `price` float NOT NULL,
  `validity` int(3) NOT NULL,
  `subServiceId` int(11) NOT NULL,
  `serviceId` int(11) DEFAULT NULL,
  `time_perday` int(11) DEFAULT NULL,
  `packId` varchar(50) DEFAULT NULL,
  `validityunithourly` tinyint(1) DEFAULT '0',
  `postprice` float DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_priceValidityMap` (`subServiceId`),
  KEY `serviceId` (`serviceId`),
  CONSTRAINT `FK_priceValidityMap` FOREIGN KEY (`subServiceId`) REFERENCES `sub_service_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `price_validity_mapping_ibfk_1` FOREIGN KEY (`serviceId`) REFERENCES `service_info` (`id`),
  CONSTRAINT `price_validity_mapping_ibfk_2` FOREIGN KEY (`serviceId`) REFERENCES `service_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

/*Table structure for table `renewalretry` */

DROP TABLE IF EXISTS `renewalretry`;

CREATE TABLE `renewalretry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `SubServiceId` int(11) NOT NULL,
  `endDate` datetime DEFAULT NULL,
  `hours` int(11) DEFAULT NULL,
  `lastBillingPrice` int(2) DEFAULT NULL,
  `fallback` int(11) DEFAULT NULL,
  `renewNotification` int(11) DEFAULT NULL,
  `notifyChannel` int(11) DEFAULT NULL,
  `renewalUserConsent` tinyint(1) DEFAULT NULL,
  `consentChannel` int(11) DEFAULT NULL,
  `url` text CHARACTER SET latin1,
  `startTime` time DEFAULT NULL,
  `endTime` time DEFAULT NULL,
  `priority` int(11) DEFAULT '1',
  `renewalStatus` varchar(25) DEFAULT 'scheduled',
  `retryStatus` varchar(25) DEFAULT 'scheduled',
  PRIMARY KEY (`id`),
  UNIQUE KEY `FK_renewalretry` (`SubServiceId`),
  KEY `FK_renewalretry_channel1` (`notifyChannel`),
  KEY `FK_renewalretry_channel2` (`consentChannel`),
  CONSTRAINT `FK_renewalretry` FOREIGN KEY (`SubServiceId`) REFERENCES `sub_service_info` (`id`),
  CONSTRAINT `FK_renewalretry_channel1` FOREIGN KEY (`notifyChannel`) REFERENCES `channels_info` (`id`),
  CONSTRAINT `FK_renewalretry_channel2` FOREIGN KEY (`consentChannel`) REFERENCES `channels_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `scheduler_properties` */

DROP TABLE IF EXISTS `scheduler_properties`;

CREATE TABLE `scheduler_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(1000) DEFAULT NULL,
  `value` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

/*Table structure for table `scheduler_subs_msgs` */

DROP TABLE IF EXISTS `scheduler_subs_msgs`;

CREATE TABLE `scheduler_subs_msgs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `starttime` time DEFAULT NULL,
  `endtime` time DEFAULT NULL,
  `status` varchar(25) DEFAULT 'scheduled',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `scp_properties` */

DROP TABLE IF EXISTS `scp_properties`;

CREATE TABLE `scp_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(25) DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Table structure for table `service_channel_mapping` */

DROP TABLE IF EXISTS `service_channel_mapping`;

CREATE TABLE `service_channel_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subServiceId` int(11) DEFAULT NULL,
  `channelId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_serviceChannelMap` (`subServiceId`),
  CONSTRAINT `FK_serviceChannelMap` FOREIGN KEY (`subServiceId`) REFERENCES `channels_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `service_info` */

DROP TABLE IF EXISTS `service_info`;

CREATE TABLE `service_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serviceName` varchar(25) NOT NULL,
  `serviceId` varchar(25) NOT NULL,
  `status` int(2) NOT NULL,
  `addedOn` datetime NOT NULL,
  `unsubReqToBilling` int(2) NOT NULL,
  `chargingType` varchar(25) NOT NULL,
  `cpId` int(11) NOT NULL,
  `notify` int(11) DEFAULT NULL,
  `notifychannelid` int(11) DEFAULT NULL,
  `alias` varchar(5) DEFAULT NULL,
  `shortCode` varchar(11) DEFAULT NULL,
  `operator` varchar(25) DEFAULT NULL,
  `country_code` int(25) DEFAULT NULL,
  `actionOnBillingResp` int(2) DEFAULT '1',
  `appCdr` tinyint(1) DEFAULT '0',
  `pendingUnsubToSdp` tinyint(1) DEFAULT '1',
  `additionalUrl` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1` (`serviceName`,`serviceId`),
  KEY `FK_service_info` (`cpId`),
  KEY `FK_service_info_channel` (`notifychannelid`),
  KEY `FK_service_info_country` (`country_code`),
  KEY `service_info_srvid` (`serviceId`),
  CONSTRAINT `FK_service_info` FOREIGN KEY (`cpId`) REFERENCES `cpdetail` (`id`),
  CONSTRAINT `FK_service_info_channel` FOREIGN KEY (`notifychannelid`) REFERENCES `channels_info` (`id`),
  CONSTRAINT `FK_service_info_country` FOREIGN KEY (`country_code`) REFERENCES `coutrydetails` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=199 DEFAULT CHARSET=utf8;

/*Table structure for table `service_lang_mapping` */

DROP TABLE IF EXISTS `service_lang_mapping`;

CREATE TABLE `service_lang_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` int(11) NOT NULL,
  `lang_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_service_lang_mapping_service` (`service_id`),
  KEY `FK_service_lang_mapping_lang` (`lang_id`),
  CONSTRAINT `FK_service_lang_mapping_lang` FOREIGN KEY (`lang_id`) REFERENCES `language` (`id`),
  CONSTRAINT `FK_service_lang_mapping_service` FOREIGN KEY (`service_id`) REFERENCES `service_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `service_url` */

DROP TABLE IF EXISTS `service_url`;

CREATE TABLE `service_url` (
  `i_id` double NOT NULL AUTO_INCREMENT,
  `service_id` varchar(225) DEFAULT NULL,
  `sub_url` varchar(765) DEFAULT NULL,
  `unsub_url` varchar(765) DEFAULT NULL,
  PRIMARY KEY (`i_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `sms_properties` */

DROP TABLE IF EXISTS `sms_properties`;

CREATE TABLE `sms_properties` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `key` varchar(25) NOT NULL,
  `value` varchar(250) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

/*Table structure for table `store_renew_notification` */

DROP TABLE IF EXISTS `store_renew_notification`;

CREATE TABLE `store_renew_notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `RenewUrl` text,
  `currentTime` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `store_sub_url` */

DROP TABLE IF EXISTS `store_sub_url`;

CREATE TABLE `store_sub_url` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subUrl` varchar(500) DEFAULT NULL,
  `currentDate` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `sub_service_info` */

DROP TABLE IF EXISTS `sub_service_info`;

CREATE TABLE `sub_service_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subServiceName` varchar(25) NOT NULL,
  `subServiceId` varchar(25) DEFAULT NULL,
  `serviceId` int(11) NOT NULL,
  `subServiceStatus` tinyint(1) NOT NULL,
  `doCharging` tinyint(1) NOT NULL,
  `addedOn` datetime NOT NULL,
  `sub_type` varchar(25) NOT NULL,
  `time_perday` int(11) NOT NULL,
  `demo` tinyint(1) NOT NULL,
  `demoDays` int(2) NOT NULL,
  `makeOnlyCdr` tinyint(1) NOT NULL,
  `retry` tinyint(1) NOT NULL,
  `renewal` tinyint(1) NOT NULL,
  `notification` int(2) NOT NULL,
  `url` text NOT NULL,
  `smsAlert` tinyint(1) NOT NULL,
  `smsUrl` text NOT NULL,
  `giftId` int(11) DEFAULT NULL,
  `topUp` tinyint(1) DEFAULT '0',
  `topUpPerDay` tinyint(1) DEFAULT '0',
  `stateChanger` tinyint(1) DEFAULT '1',
  `msisdnseriescheck` tinyint(1) DEFAULT '0',
  `notificationchannel` varchar(50) DEFAULT 'ivr',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_2` (`subServiceName`,`subServiceId`),
  KEY `FK_subService` (`serviceId`),
  KEY `FK_sub_service_info` (`giftId`),
  CONSTRAINT `FK_sub_service_info` FOREIGN KEY (`giftId`) REFERENCES `service_info` (`id`),
  CONSTRAINT `FK_sub_service_info_gift` FOREIGN KEY (`giftId`) REFERENCES `service_info` (`id`),
  CONSTRAINT `FK_sub_service_info_s` FOREIGN KEY (`serviceId`) REFERENCES `service_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

/*Table structure for table `sub_service_msgs` */

DROP TABLE IF EXISTS `sub_service_msgs`;

CREATE TABLE `sub_service_msgs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subServiceId` int(11) NOT NULL,
  `demoStartedMessage` text,
  `demoEndMessage` text,
  `subscriptionSuccessMessage` text,
  `subscriptionFailureMessage` text,
  `subscriptionPendingMessage` text,
  `alreadySubscribedMessage` text,
  `alreadyUnsubscribedMessage` text,
  `preRenewalMessage` text,
  `renewalMessage` text,
  `unsubscriptionSuccessMessage` text,
  `unsubscriptionFailureMessage` text,
  `lang_id` int(11) DEFAULT NULL,
  `renewalFailure` text,
  `subscriptionSuccessNoRenewal` text,
  `unsubscriptionSuccessNoRenewal` text,
  `expirationMessage` text,
  `expirationNotification` text,
  `consentMsg` varchar(500) DEFAULT NULL,
  `topUpSuccess` varchar(500) DEFAULT NULL,
  `topUpFailure` varchar(500) DEFAULT NULL,
  `alreadySubscribedNonActiveMessage` varchar(500) DEFAULT NULL,
  `subRegistartionFailureMsg` varchar(500) DEFAULT NULL,
  `subRetryFailureMsg` varchar(500) DEFAULT NULL,
  `renewRetryFailureMsg` varchar(500) DEFAULT NULL,
  `autoRenewEnableMsg` varchar(500) DEFAULT NULL,
  `autoRenewDisablMsg` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_lang` (`subServiceId`,`lang_id`),
  KEY `FK_sub_service_msgs` (`subServiceId`),
  KEY `FK_sub_service_lang` (`lang_id`),
  KEY `sub_srv_msgs_sid_lang_id` (`subServiceId`,`lang_id`),
  CONSTRAINT `FK_sub_service_info_msgs` FOREIGN KEY (`subServiceId`) REFERENCES `sub_service_info` (`id`),
  CONSTRAINT `FK_sub_service_lang` FOREIGN KEY (`lang_id`) REFERENCES `language` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `subs_properties` */

DROP TABLE IF EXISTS `subs_properties`;

CREATE TABLE `subs_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(50) DEFAULT NULL,
  `value` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=latin1;

/*Table structure for table `subscriptionmsgs_properties` */

DROP TABLE IF EXISTS `subscriptionmsgs_properties`;

CREATE TABLE `subscriptionmsgs_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(1000) DEFAULT NULL,
  `value` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;

/*Table structure for table `topup` */

DROP TABLE IF EXISTS `topup`;

CREATE TABLE `topup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `price` float DEFAULT NULL,
  `minutes` int(5) DEFAULT NULL,
  `subServiceId` int(11) DEFAULT NULL,
  `topUpId` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_topup` (`subServiceId`),
  CONSTRAINT `FK_topup` FOREIGN KEY (`subServiceId`) REFERENCES `sub_service_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `user_mapping_table` */

DROP TABLE IF EXISTS `user_mapping_table`;

CREATE TABLE `user_mapping_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country_id` int(11) DEFAULT NULL,
  `operator_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_arf8m675jenl60rm5ax8wj142` (`country_id`),
  KEY `FK_2sbu0u3lgqjkxjbp8dkl0rdyq` (`operator_id`),
  KEY `FK_cjfjxcabmjfrg7a31d4ahuya9` (`user_id`),
  CONSTRAINT `FK_2sbu0u3lgqjkxjbp8dkl0rdyq` FOREIGN KEY (`operator_id`) REFERENCES `operator` (`id`),
  CONSTRAINT `FK_arf8m675jenl60rm5ax8wj142` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`),
  CONSTRAINT `FK_cjfjxcabmjfrg7a31d4ahuya9` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `user_roles` */

DROP TABLE IF EXISTS `user_roles`;

CREATE TABLE `user_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `authority` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_user_roles` (`user_id`),
  CONSTRAINT `FK_user_roles` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Table structure for table `user_status` */

DROP TABLE IF EXISTS `user_status`;

CREATE TABLE `user_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(25) DEFAULT NULL,
  `nextState` varchar(25) DEFAULT NULL,
  `maxNoOfDays` int(11) DEFAULT NULL,
  `maxRetryCount` int(11) DEFAULT NULL,
  `subServiceId` int(11) DEFAULT NULL,
  `upgrade` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_user_status_subServiceInfo` (`subServiceId`),
  CONSTRAINT `FK_user_status_subServiceInfo` FOREIGN KEY (`subServiceId`) REFERENCES `sub_service_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

/*Table structure for table `ussd_message` */

DROP TABLE IF EXISTS `ussd_message`;

CREATE TABLE `ussd_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reason` varchar(50) DEFAULT NULL,
  `ussdReason` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/* Procedure structure for procedure `DailyReport` */

/*!50003 DROP PROCEDURE IF EXISTS  `DailyReport` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `DailyReport`()
BEGIN
SET @Date = DATE(NOW())-INTERVAL @DayInterval DAY;
SET @DayInterval='1';
insert into dailyreportver02(ReportDate,ServiceName,Summary,Sub_Service_Name,Type,type_sub_param,price,Count)
select report_date,service,'SUBSCRIBERS' as summary,sub_service as sub_service,'Subscriber Base' as Type,if(status='active','Active Base',if(status='grace','Grace Base',if(status='pending','Pending Base',if(status='parking','Parking Base',if(status='suspended','Suspended Base',status))))) as status,'',sum(base) as count from subscriberbase where date(report_date) = (DATE(NOW())-INTERVAL @DayInterval DAY) and service='Religious' group by 1,2,3,4,5,6,7 union all
select report_date,service,'SUBSCRIBERS' as summary,sub_service as sub_service,'Subscriber Base' as Type,'Total Base' as status,'',sum(base) as count from subscriberbase where date(report_date) = (DATE(NOW())-INTERVAL @DayInterval DAY) and service ='Religious' group by 1,2,3,4,5,6,7 union all
select report_date,'Total Subscriber Base' service,'SUBSCRIBERS' as summary,'','' as Type,'','',sum(base)as count from subscriberbase where status='active' and  date(report_date) = (DATE(NOW())-INTERVAL @DayInterval DAY) and service ='Religious' group by 1,2,3,4,5,6,7  union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,if(req_desc='subscribe','Activation Revenue',if(req_desc='renewal','Renewal Revenue',req_desc)) as type ,concat('Rev @',amount/10000,'GHC') as Type_Sub_Param,Amount as amount,(count(*)*amount/10000) from subscription_logs where   status='successful' and request_type in ('1','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,if(action='act','Activation Revenue-Retry',if(action='renew','Renewals Revenue-Retry',req_desc)) as type ,concat('Rev @',amount/10000,'GHC') as Type_Sub_Param,Amount as amount,(count(*)*amount/10000) from subscription_logs where  status='successful' and request_type = '3' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,if(action='act','Activation Count-Retry',if(action='renew','Renewals Count-Retry',req_desc)) as type ,concat(if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))),'@',amount/10000) as Type_Sub_Param,Amount as amount,count(*) from subscription_logs where   status='successful' and request_type ='3' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,if(req_desc='subscribe','Activation Count',if(req_desc='renewal','Renewals Count',req_desc)) as type ,concat(if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))),'@',amount/10000) as Type_Sub_Param,Amount as amount,count(*) from subscription_logs where   status='successful' and request_type in ('1','4') and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,'CHANNEL WISE ACTIVATION' as type ,concat('Act @',UPPER(channel)) as Type_Sub_Param,Amount as amount,count(*) from subscription_logs where   status='successful' and request_type ='3' and action='act'  and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(Sub_Srvname like '%Daily%','DailyPack',if(Sub_Srvname like '%Weekly%','WeeklyPack',if(Sub_Srvname like '%month%','MonthlyPack',Sub_Srvname))) as subservice,'CHANNEL WISE ACTIVATION' as type ,concat('Act @',UPPER(channel)) as Type_Sub_Param,Amount as amount,count(*) from subscription_logs where   status='successful' and request_type in ('1','4')  and req_desc='subscribe' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as subservice,'Churn Count' as type,if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as Type_Sub_Param,'0' as amount,count(*) from subscription_logs where  status='successful' and request_type ='2' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as subservice,'Churn Count' as type,if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as Type_Sub_Param,'0' as amount,count(*) from subscription_logs where  req_desc = 'statechanger' and user_status like 'unsub' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as subservice,'CHANNEL WISE CHURN' as type,concat('Churn @',UPPER(channel)) as Type_Sub_Param,'0' as amount,count(*) from subscription_logs where   status='successful' and request_type ='2' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as ReportDate,srv_id as service,'',if(sub_srvid like '%Daily%','DailyPack',if(sub_srvid like '%Weekly%','WeeklyPack',if(sub_srvid like '%month%','MonthlyPack',sub_srvid))) as subservice,'CHANNEL WISE CHURN' as type,concat('Churn @',UPPER(channel)) as Type_Sub_Param,'0' as amount,count(*) from subscription_logs where   req_desc = 'statechanger' and user_status like 'unsub' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(be_reqrecvtime,10)as ReportDate,srv_name as service,'',"NOSUBSERVICE" as subservice,"Success Billing Request" as Type,if(srv_key like '%Daily%','DailyPack',if(srv_key like '%Weekly%','WeeklyPack',if(srv_key like '%month%','MonthlyPack',srv_key))) Type_Sub_Param,"0",count(*) from billing_logs where request_type='1' and date(be_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and req_status='success' group by 1,2,3,4,5,6,7 union all
select left(be_reqrecvtime,10)as ReportDate,srv_name as service,'',"NOSUBSERVICE" as subservice,"Billing Request Failed Reasons" as Type,if(be_respdesc like 'low%',"Low Balance",if(be_respdesc like '%TimeOut%',"Network Error",if(be_respdesc like '%Connect%',"Network Error","Other Error"))) as param_type,"0",count(distinct msisdn) from billing_logs where request_type='1' and date(be_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and req_status<>'success' group by 1,2,3,4,5,6,7 union all
select left(be_reqrecvtime,10)as ReportDate,srv_name as service,'','' as subservice,"Billing Request" as Type,if(srv_key like '%Daily%','DailyPack',if(srv_key like '%Weekly%','WeeklyPack',if(srv_key like '%month%','MonthlyPack',srv_key))) Type_Sub_Param,"0",count(*) from billing_logs  where  request_type='1' and date(be_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(be_reqrecvtime,10)as ReportDate,srv_name as service,'','' as subservice,"Unique Billing Request" as Type,if(srv_key like '%Daily%','DailyPack',if(srv_key like '%Weekly%','WeeklyPack',if(srv_key like '%month%','MonthlyPack',srv_key))) Type_Sub_Param,"0",count(distinct msisdn) from billing_logs  where  request_type='1' and date(be_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like 'mv%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'na','Other',service))))) as service,'WELCOME USERS','' as subservice,"Unique Caller" type,"New Users Unique @Day" sub_type,"0",count(distinct A_PARTY) as UUOfDay from ivr_mastercalllogs where call_type='incoming' and SUB_STATUS='new' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like 'mv%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'na','Other',service))))) as service,'WELCOME USERS','' as subservice,"Unique Caller" type,"Unique Caller @Day" sub_type,"0",count(distinct A_PARTY) as UUOfDay from ivr_mastercalllogs where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left((NOW()-INTERVAL @DayInterval DAY),10) as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like 'mv%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'na','Other',service))))) as service,'WELCOME USERS','' as subservice,"Unique Caller" type,"Unique Caller @Month" sub_type,"0",count(distinct A_PARTY) from ivr_mastercalllogs where call_type='incoming' and month(end_datetime)=month(NOW()-INTERVAL @DayInterval DAY) and year(end_datetime)=year(NOW()-INTERVAL @DayInterval DAY) and day(end_datetime)<=day(NOW()-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select (DATE(NOW())-INTERVAL @DayInterval DAY) as ReportDate,if(UTS.service like 'magicvoice%','Magicvoice',if(UTS.service like '%Religious%','Religious',if(UTS.service like '%christianity%','Christianity',UTS.service))) as service,'', '',"New User Stats | IVR" type,"% New User Attempted to Subscribe" subtype,"0",round((UTS.tryed/A.attempt)*100) as '%AttToSub' from (select M.service,count(*) as tryed from ivr_mastercalllogs M, url_mastercalllogs U where date(M.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.SUB_STATUS='new' and  M.call_type='incoming' and date(U.URL_HIT_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.master_id=U.master_id group by 1) UTS RIGHT JOIN (select M.service,count(*) as attempt from ivr_mastercalllogs M where date(M.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.SUB_STATUS='new' and  call_type='incoming'  group by 1) A ON A.service=UTS.service union all
select left(END_DATETIME,10) as Date,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',service))) as service,'','' subservice,"New User Stats | IVR" type,CASE WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) <=5 THEN '000-005' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 6 and 10 THEN '006-010' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 11 and 20 THEN '011-020' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 21 and 30 THEN '021-030' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 31 and 180 THEN '031-180' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) BETWEEN 181 and 480 THEN '181-480' WHEN TimeStampDiff(second,START_DATETIME,END_DATETIME) >= 480 THEN '480+sec'  END as call_dur,"0",count(*) from ivr_mastercalllogs where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and call_status='success' and SUB_STATUS ='new' group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'CALLS & MOU',"NOSUBSERVICE" as subservice,"Calls" type,"Total Calls"  sub_type,"0", count(*) from ivr_mastercalllogs where call_type='incoming' and date(END_DATETIME) = (DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(SHORT_CODE='3071','Female',if(SHORT_CODE='3072','King',if(SHORT_CODE='3073','Cartoon',if(SHORT_CODE='3074','Monster',if(SHORT_CODE='3075','Bunny',if(SHORT_CODE='3079','Romantic',if(SHORT_CODE='3078','Traffic',if(SHORT_CODE='30723','Airport',SHORT_CODE)))))))) as service,'CALLS & MOU',"NOSUBSERVICE" as subservice,"Calls" type,"Total Calls"  sub_type,"0", count(*) from ivr_mastercalllogs where call_type='incoming' and date(END_DATETIME) = (DATE(NOW())-INTERVAL @DayInterval DAY)  and SHORT_CODE in ('3071','3072','3073','3074','3075','3079','3078','30723') group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'',"NOSUBSERVICE" as subservice,"Calls" type,case when (length(B_PARTY)<8 and SUB_STATUS='active') then 'Sub Short Code Calls' when (length(B_PARTY)<8 and SUB_STATUS<>'active') then 'Non Sub Short Code Calls' when (length(B_PARTY)>=8 and SUB_STATUS='active') then 'Sub Friend Number Calls' when (length(B_PARTY)>=8 and SUB_STATUS<>'active') then 'Non Sub Friend Number Calls'  end as sub_type,"0", count(*) from ivr_mastercalllogs where call_type='incoming' and date(END_DATETIME) like (DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(C.END_DATETIME,10)as ReportDate,if(M.service like 'magicvoice%','Magicvoice',if(M.service like '%Religious%','Religious',if(M.service like '%christianity%','christianity',M.service))) as service,'',"NOSUBSERVICE" as subservice,"MOU/ PULSES" type, case when  SUB_STATUS='active' then 'Subscriber Calls before Dialout   MOU' when  SUB_STATUS<>'active' then 'Non Subscriber Calls before Dialout   MOU'  end as type,"0", round(sum(TIMESTAMPDIFF(SECOND,M.PICKUP_DATETIME,C.DIAL_DATETIME))/60) as befor_dialout from ivr_mastercalllogs M,ivr_confcalllogs C where M.call_type='incoming' and date(M.END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and date(C.END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and M.master_id=C.master_id group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'CALLS & MOU',"NOSUBSERVICE" as subservice,"MOU/ PULSES" type,"Total  MOU" as type_param,"0", round(sum(DURATION)/60) as MOU from ivr_mastercalllogs where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'',"NOSUBSERVICE" as subservice,"MOU/ PULSES" type,case when length(B_PARTY)>=8 and SUB_STATUS='active' then 'Susbcriber Friend No Calls MOU' when length(B_PARTY)>=8 and SUB_STATUS<>'active' then 'Non Susbcriber Friend No MOU' when length(B_PARTY)<8 and SUB_STATUS='active' then 'Subscriber Short Code MOU' when length(B_PARTY)<8 and SUB_STATUS<>'active' then 'Non Susbcriber  Short Code MOU' end as type_param,"0", round(sum(DURATION)/60) as MOU from ivr_mastercalllogs where call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10) as Date,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%bible%','Christianity',service)))) as service,'',"NOSUBSERVICE" subservice,'Language','English' as type_param,'',count(*) from ivr_mastercalllogs where CALL_STATUS like 'success' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and call_type='incoming' group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Bible%','Christianity',service)))) as service,'',"NOSUBSERVICE" as subservice,"Friend No. Calls" type,"Friend No Calls" sub_type,"0", count(*) from ivr_mastercalllogs where call_type='incoming' and length(B_PARTY)>=8 and date(END_DATETIME) = (DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%Bible%','Christianity',service)))) as service,'',"NOSUBSERVICE" as subservice,"Friend No. Calls" type,"Friend No MOU" type_param,"0", round(sum(DURATION)/60) as MOU from ivr_mastercalllogs where length(B_PARTY)>=8 and call_type='incoming' and date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like '%magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'mv%','Magicvoice',service)))) as service,'MAGIC VOICE STATISTICS','' as subservice,"Call Conference Ratio" type,if(STATUS like 'failure',"Call Conference Failure",if(STATUS like 'success',"Call Conference Success",STATUS)) as sub_type,"0",count(*) from ivr_confcalllogs where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and STATUS != ' ' group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like '%magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like 'mv%','Magicvoice',service)))) as service,'MAGIC VOICE STATISTICS',"NOSUBSERVICE" as subservice,"Call Conference Ratio" type,"Call Conference Success Ratio" sub_type,"0",round((select count(*) from ivr_confcalllogs where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and STATUS = 'success')/count(*)*100) as count from ivr_confcalllogs where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and STATUS != ' ' group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'magicvoice%','Magicvoice',if(service like '%Religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%bible%','Christianity',service)))) as service,'',"NOSUBSERVICE" as subservice,"Conf Call Failure Reason" type,if(REASON like 'noanswer',"No Answer",if(REASON like 'rlcsent',"A Party Hangup","MSC Disconnect")) as sub_type,"0",count(*)  from ivr_confcalllogs where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and status<>'success' group by 1,2,3,4,5,6,7 union all
select left(end_datetime,10)as date,'OBD','',if(service like '%mv%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'OBD COUNT','OBD Attempt','',count(*) from ivr_mastercalllogs where call_type='outgoing' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all
select left(END_DATETIME,10)as ReportDate,if(service like 'Magicvoice%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','Christianity',if(service like '%bible%','Christianity',service)))) as service,'',"NOSUBSERVICE" as subservice,"Conf Call Failure Reason" type,if(REASON like 'noanswer',"No Answer",if(REASON like 'rlcsent',"A Party Hangup","MSC Disconnect")) as sub_type,"0",count(*)  from ivr_confcalllogs where date(END_DATETIME)=(DATE(NOW())-INTERVAL @DayInterval DAY) and status<>'success' group by 1,2,3,4,5,6,7 union all
select left(end_datetime,10)as date,'OBD','',if(service like '%mv%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'OBD COUNT',if(call_status='success','OBD Picked','OBD Not Picked') as type,'',count(*) from ivr_mastercalllogs where call_type='outgoing' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,2,3,4,5,6,7 union all
select left(ivm.end_datetime,10)as date,'OBD','',if(ivm.service like '%mv%','Magicvoice',if(ivm.service like '%religious%','Religious',if(ivm.service like '%christianity%','christianity',if(ivm.service like '%Ibadat%','Ibadat',ivm.service)))) as service,'OBD COUNT','OBD SUCESS %','',round((select count(*) from ivr_mastercalllogs as im where im.call_type='outgoing' and im.call_status='success' and ivm.service = im.service and date(im.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY))/count(*)*100)as count from ivr_mastercalllogs as ivm where ivm.call_type='outgoing' and date(ivm.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all
select left(end_datetime,10)as date,'OBD','',if(service like '%mv%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'OBD Failure Reasons',RLC_REASON,'',count(*) from ivr_mastercalllogs where call_type='outgoing' and call_status<>'success' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as date,'OBD','',if(Sub_Srvname like 'mv%','Magicvoice',if(Sub_Srvname like '%religious%','Religious',if(Sub_Srvname like '%christianity%','Religious',if(Sub_Srvname like '%islamic%','Religious',Sub_Srvname)))) as service,'OBD Subscription','OBD Subscription' type,'',count(*) from subscription_logs where action ='act' and status='successful' and request_type in ('1','3') and channel='OBD' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(imc.end_datetime,10)as date,'OBD','',if(imc.service like '%mv%','Magicvoice',if(imc.service like '%religious%','Religious',if(imc.service like '%christianity%','christianity',if(imc.service like '%Ibadat%','Ibadat',imc.service)))) as service,'OBD Subscription','OBD Subscription %','',round((select count(*) from subscription_logs as im where im.action = 'act' and status='successful' and im.request_type in ('1','3') and im.channel='OBD' and im.srv_name=SUBSTRING_INDEX(imc.service,'obd',-1)  and date(im.se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY))/count(*)*100)as count from ivr_mastercalllogs as imc where imc.call_type='outgoing' and imc.call_status='success' and date(imc.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and imc.service not like '%mv%'  group by 1,2,3,4,5,6,7 union all
select left(imc.end_datetime,10)as date,'OBD','',if(imc.service like '%mv%','Magicvoice',if(imc.service like '%religious%','Religious',if(imc.service like '%christianity%','christianity',if(imc.service like '%Ibadat%','Ibadat',imc.service)))) as service,'OBD Subscription','OBD Subscription %','',round((select count(*) from subscription_logs as im where im.action = 'act' and status='successful' and im.request_type in ('1','3') and im.channel='OBD' and im.srv_name='Magicvoice'  and date(im.se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY))/count(*)*100)as count from ivr_mastercalllogs as imc where imc.call_type='outgoing' and imc.call_status='success' and date(imc.end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY) and imc.service  like '%mv%'  group by 1,2,3,4,5,6,7 union all
select left(se_reqrecvtime,10)as date,'OBD','',if(Sub_Srvname like 'mv%','Magicvoice',if(Sub_Srvname like '%religious%','Religious',if(Sub_Srvname like '%christianity%','Religious',if(Sub_Srvname like '%islamic%','Religious',Sub_Srvname)))) as service,'OBD Subscription Revenue',if(req_desc='renewal','OBD Renewals Revenue','OBD Activation Revenue') type,'',ROUND((count(*)*(amount/100))) as revenue from subscription_logs where  status='successful' and request_type in ('1','3') and channel='OBD' and date(se_reqrecvtime)=(DATE(NOW())-INTERVAL @DayInterval DAY) group by 1,2,3,4,5,6,7 union all
select left(end_datetime,10)as date,'OBD','',if(service like '%mv%','Magicvoice',if(service like '%religious%','Religious',if(service like '%christianity%','christianity',if(service like '%Ibadat%','Ibadat',service)))) as service,'Duration Stats', case when DURATION<=5 then '000-005' when DURATION>5 and DURATION<=10 then '006-010' when DURATION>10 and DURATION<=15 then '011-015' when DURATION>15 and DURATION<=20 then '016-020' when DURATION>20 and DURATION<31 then '021-030' when DURATION>30 and DURATION<=180 then '031-180' when DURATION>480 then '480+sec' when DURATION>180 and DURATION<=480 then '181-480' end as ran,'', count(*) from ivr_mastercalllogs as im where call_type='outgoing' and call_status='success' and date(end_datetime)=(DATE(NOW())-INTERVAL @DayInterval DAY)  group by 1,2,3,4,5,6,7;
END */$$
DELIMITER ;

/* Procedure structure for procedure `UpdateSchedulerStatus` */

/*!50003 DROP PROCEDURE IF EXISTS  `UpdateSchedulerStatus` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateSchedulerStatus`()
BEGIN
             
  update subs_engine0.subscription set SCHEDULER_RETRY_STATUS='scheduled',SCHEDULER_RENEWAL_STATUS='scheduled';
  update subs_engine1.subscription set SCHEDULER_RETRY_STATUS='scheduled',SCHEDULER_RENEWAL_STATUS='scheduled';
  update subs_engine2.subscription set SCHEDULER_RETRY_STATUS='scheduled',SCHEDULER_RENEWAL_STATUS='scheduled';
  update subs_engine3.subscription set SCHEDULER_RETRY_STATUS='scheduled',SCHEDULER_RENEWAL_STATUS='scheduled';
  update subs_engine4.subscription set SCHEDULER_RETRY_STATUS='scheduled',SCHEDULER_RENEWAL_STATUS='scheduled';
  update subs_engine5.subscription set SCHEDULER_RETRY_STATUS='scheduled',SCHEDULER_RENEWAL_STATUS='scheduled';
  update subs_engine6.subscription set SCHEDULER_RETRY_STATUS='scheduled',SCHEDULER_RENEWAL_STATUS='scheduled';
  update subs_engine7.subscription set SCHEDULER_RETRY_STATUS='scheduled',SCHEDULER_RENEWAL_STATUS='scheduled';
  update subs_engine8.subscription set SCHEDULER_RETRY_STATUS='scheduled',SCHEDULER_RENEWAL_STATUS='scheduled';
  update subs_engine9.subscription set SCHEDULER_RETRY_STATUS='scheduled',SCHEDULER_RENEWAL_STATUS='scheduled';
    
END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;



/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - ivr_data
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `ivr_data`;

USE `ivr_data`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `bookmark` */

DROP TABLE IF EXISTS `bookmark`;

CREATE TABLE `bookmark` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BYTES_PLAYED` varchar(100) DEFAULT NULL,
  `FILE_PLAYED` varchar(100) DEFAULT NULL,
  `MSISDN` varchar(25) DEFAULT NULL,
  `PROMPT_ID` int(11) NOT NULL,
  `PLAYLIST_ID` int(11) DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=250 DEFAULT CHARSET=latin1;

/*Table structure for table `call_count` */

DROP TABLE IF EXISTS `call_count`;

CREATE TABLE `call_count` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `hour` int(2) DEFAULT NULL,
  `service` varchar(30) DEFAULT NULL,
  `jobname` varchar(50) DEFAULT NULL,
  `obd_attempt` int(7) DEFAULT NULL,
  `obd_success` int(7) DEFAULT NULL,
  `obd_fail` int(7) DEFAULT NULL,
  `obd_success_remotehangupfirst` int(7) DEFAULT NULL,
  `obd_success_rlcsent` int(7) DEFAULT NULL,
  `obd_success_other` int(7) DEFAULT NULL,
  `obd_fail_userabsent` int(7) DEFAULT NULL,
  `obd_fail_ringingtimeout` int(7) DEFAULT NULL,
  `obd_fail_noanswer` int(7) DEFAULT NULL,
  `obd_fail_misc` int(7) DEFAULT NULL,
  `obd_fail_other` int(7) DEFAULT NULL,
  `obd_00_05` int(7) DEFAULT NULL,
  `obd_06_10` int(7) DEFAULT NULL,
  `obd_11_15` int(7) DEFAULT NULL,
  `obd_16_20` int(7) DEFAULT NULL,
  `obd_20_pluse` int(7) DEFAULT NULL,
  `obd_subscription_url_hit` int(7) DEFAULT NULL,
  `obd_dtmf_0` int(7) DEFAULT NULL,
  `obd_dtmf_1` int(7) DEFAULT NULL,
  `obd_dtmf_2` int(7) DEFAULT NULL,
  `obd_dtmf_3` int(7) DEFAULT NULL,
  `obd_dtmf_4` int(7) DEFAULT NULL,
  `obd_dtmf_5` int(7) DEFAULT NULL,
  `obd_dtmf_6` int(7) DEFAULT NULL,
  `obd_dtmf_7` int(7) DEFAULT NULL,
  `obd_dtmf_8` int(7) DEFAULT NULL,
  `obd_dtmf_9` int(7) DEFAULT NULL,
  `obd_dtmf_star` int(7) DEFAULT NULL,
  `obd_dtmf_hash` int(7) DEFAULT NULL,
  `obd_dtmf_noinput` int(7) DEFAULT NULL,
  `obd_sou` int(7) DEFAULT NULL,
  `obd_pulses` int(7) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `circle_mapper` */

DROP TABLE IF EXISTS `circle_mapper`;

CREATE TABLE `circle_mapper` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CIRCLE` varchar(15) DEFAULT NULL,
  `CIRCLE_KEY` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(20) DEFAULT NULL,
  `COUNTRY_KEY` varchar(3) DEFAULT NULL,
  `DEF_LANG` varchar(3) DEFAULT NULL,
  `OPERATOR` varchar(15) DEFAULT NULL,
  `SERIES` int(11) NOT NULL,
  `TIME_ZONE` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_9l5xpt89fb1iyn9no82xv9ye9` (`SERIES`,`CIRCLE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `content` */

DROP TABLE IF EXISTS `content`;

CREATE TABLE `content` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(2000) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `expiredate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1495 DEFAULT CHARSET=utf8;

/*Table structure for table `content_category` */

DROP TABLE IF EXISTS `content_category`;

CREATE TABLE `content_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Table structure for table `content_label` */

DROP TABLE IF EXISTS `content_label`;

CREATE TABLE `content_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `label_name` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Table structure for table `content_mapper` */

DROP TABLE IF EXISTS `content_mapper`;

CREATE TABLE `content_mapper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `operator_id` int(11) DEFAULT NULL,
  `download_url` varchar(2000) DEFAULT NULL,
  `crbt_url` varchar(2000) DEFAULT NULL,
  `billing_url` varchar(2000) DEFAULT NULL,
  `user_mapping_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country_id` (`country_id`),
  KEY `operator_id` (`operator_id`),
  KEY `FK12F03CC7842663DE` (`content_id`),
  CONSTRAINT `content_mapper_ibfk_1` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`),
  CONSTRAINT `content_mapper_ibfk_2` FOREIGN KEY (`country_id`) REFERENCES `global`.`country` (`id`),
  CONSTRAINT `content_mapper_ibfk_3` FOREIGN KEY (`operator_id`) REFERENCES `global`.`operator` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1495 DEFAULT CHARSET=utf8;

/*Table structure for table `content_meta` */

DROP TABLE IF EXISTS `content_meta`;

CREATE TABLE `content_meta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_provider_id` int(11) DEFAULT NULL,
  `content_label_id` int(11) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `songcode` varchar(255) DEFAULT NULL,
  `songname` varchar(255) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `content_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK31917E0B49AE01B1` (`content_provider_id`),
  KEY `FK31917E0B842663DE` (`content_id`),
  KEY `FK31917E0BB3C8BB03` (`content_label_id`),
  KEY `FK31917E0B55C0E557` (`category_id`),
  CONSTRAINT `content_meta_ibfk_1` FOREIGN KEY (`content_provider_id`) REFERENCES `content_provider` (`id`),
  CONSTRAINT `content_meta_ibfk_2` FOREIGN KEY (`content_label_id`) REFERENCES `content_label` (`id`),
  CONSTRAINT `content_meta_ibfk_3` FOREIGN KEY (`category_id`) REFERENCES `content_category` (`id`),
  CONSTRAINT `content_meta_ibfk_4` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1495 DEFAULT CHARSET=utf8;

/*Table structure for table `content_meta_extra` */

DROP TABLE IF EXISTS `content_meta_extra`;

CREATE TABLE `content_meta_extra` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_meta_id` int(11) DEFAULT NULL,
  `options` text,
  PRIMARY KEY (`id`),
  KEY `FK3FC380DCC6A37CB1` (`content_meta_id`),
  CONSTRAINT `content_meta_extra_ibfk_1` FOREIGN KEY (`content_meta_id`) REFERENCES `content_meta` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1495 DEFAULT CHARSET=utf8;

/*Table structure for table `content_playlist` */

DROP TABLE IF EXISTS `content_playlist`;

CREATE TABLE `content_playlist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `playlist_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `content_playlist_mapper` */

DROP TABLE IF EXISTS `content_playlist_mapper`;

CREATE TABLE `content_playlist_mapper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_id` int(11) NOT NULL,
  `playlist_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_content` (`content_id`),
  KEY `FK_playlist` (`playlist_id`),
  CONSTRAINT `FK_content` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`),
  CONSTRAINT `FK_playlist` FOREIGN KEY (`playlist_id`) REFERENCES `content_playlist` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=967 DEFAULT CHARSET=utf8;

/*Table structure for table `content_provider` */

DROP TABLE IF EXISTS `content_provider`;

CREATE TABLE `content_provider` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provider_name` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `fingerfootball` */

DROP TABLE IF EXISTS `fingerfootball`;

CREATE TABLE `fingerfootball` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) NOT NULL,
  `TOTALMATCH` int(10) NOT NULL,
  `MATCHWON` int(10) NOT NULL,
  `TOTALPOINTS` int(10) NOT NULL,
  `CURRENTDATE` date NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ivr_blacklist` */

DROP TABLE IF EXISTS `ivr_blacklist`;

CREATE TABLE `ivr_blacklist` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DATE` datetime DEFAULT NULL,
  `IS_SERIES` int(1) DEFAULT NULL,
  `MSISDN` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `ivr_expertlist` */

DROP TABLE IF EXISTS `ivr_expertlist`;

CREATE TABLE `ivr_expertlist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `StartTime` time DEFAULT NULL,
  `EndTime` time DEFAULT NULL,
  `MSISDN` varchar(25) DEFAULT 'NULL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `ivr_recorddedicationlist` */

DROP TABLE IF EXISTS `ivr_recorddedicationlist`;

CREATE TABLE `ivr_recorddedicationlist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `CLI` varchar(10) DEFAULT NULL,
  `MSISDN` varchar(10) DEFAULT NULL,
  `STATUS` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `CLI` (`CLI`,`MSISDN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `ivr_redcarpetlist` */

DROP TABLE IF EXISTS `ivr_redcarpetlist`;

CREATE TABLE `ivr_redcarpetlist` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DATE` datetime DEFAULT NULL,
  `IS_SERIES` int(1) DEFAULT NULL,
  `MSISDN` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ivr_whitelist` */

DROP TABLE IF EXISTS `ivr_whitelist`;

CREATE TABLE `ivr_whitelist` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DATE` datetime DEFAULT NULL,
  `IS_SERIES` int(1) DEFAULT NULL,
  `MSISDN` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `mxdata` */

DROP TABLE IF EXISTS `mxdata`;

CREATE TABLE `mxdata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` longblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Table structure for table `mxgraph` */

DROP TABLE IF EXISTS `mxgraph`;

CREATE TABLE `mxgraph` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(255) NOT NULL,
  `mxdata_id` int(11) NOT NULL,
  `prodution_flag` tinyint(1) NOT NULL,
  `production_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `type` varchar(255) NOT NULL,
  `shortcode` varchar(15) NOT NULL,
  `call_type` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `service_name_short_code` (`service_name`,`shortcode`),
  KEY `FK_mxgraph` (`mxdata_id`),
  CONSTRAINT `FK_mxgraph` FOREIGN KEY (`mxdata_id`) REFERENCES `mxdata` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Table structure for table `mxgraph_key_mapping` */

DROP TABLE IF EXISTS `mxgraph_key_mapping`;

CREATE TABLE `mxgraph_key_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mxgraph_id` int(11) NOT NULL,
  `shortcode` varchar(15) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_mxgraph_key_mapping` (`mxgraph_id`),
  CONSTRAINT `FK_mxgraph_key_mapping` FOREIGN KEY (`mxgraph_id`) REFERENCES `mxgraph` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `mxgraph_version` */

DROP TABLE IF EXISTS `mxgraph_version`;

CREATE TABLE `mxgraph_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mxgraph_id` int(11) DEFAULT NULL,
  `mxdata_id` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `user_mapping_id` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_mxgraph_version_mxgraph` (`mxgraph_id`),
  KEY `FK_mxgraph_version_mxdata` (`mxdata_id`),
  CONSTRAINT `FK_mxgraph_version_mxdata` FOREIGN KEY (`mxdata_id`) REFERENCES `mxdata` (`id`),
  CONSTRAINT `FK_mxgraph_version_mxgraph` FOREIGN KEY (`mxgraph_id`) REFERENCES `mxgraph` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Table structure for table `obd_blackout_hours` */

DROP TABLE IF EXISTS `obd_blackout_hours`;

CREATE TABLE `obd_blackout_hours` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blackout_start` time NOT NULL,
  `blackout_end` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `obd_cli` */

DROP TABLE IF EXISTS `obd_cli`;

CREATE TABLE `obd_cli` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `cli_id` int(10) NOT NULL,
  `cli` varchar(25) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_obd_cli` (`cli_id`),
  CONSTRAINT `FK_obd_cli` FOREIGN KEY (`cli_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `obd_failure_reasons` */

DROP TABLE IF EXISTS `obd_failure_reasons`;

CREATE TABLE `obd_failure_reasons` (
  `reason_id` int(11) NOT NULL,
  `reason_value` varchar(100) NOT NULL,
  `ce_reason` varchar(100) NOT NULL,
  PRIMARY KEY (`reason_id`),
  UNIQUE KEY `reason_value` (`reason_value`,`ce_reason`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `obd_mscip` */

DROP TABLE IF EXISTS `obd_mscip`;

CREATE TABLE `obd_mscip` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `obd_mscip_id` int(11) DEFAULT NULL,
  `mscip` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_obd_mscip` (`obd_mscip_id`),
  CONSTRAINT `FK_obd_mscip` FOREIGN KEY (`obd_mscip_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `obd_msisdn_2017_05_24_24may2017_1_0` */

DROP TABLE IF EXISTS `obd_msisdn_2017_05_24_24may2017_1_0`;

CREATE TABLE `obd_msisdn_2017_05_24_24may2017_1_0` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(25) DEFAULT NULL,
  `status` varchar(25) DEFAULT NULL,
  `reason` varchar(100) DEFAULT NULL,
  `failedreason_status` varchar(100) DEFAULT 'scheduled',
  `autotimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `STATUS` (`status`),
  KEY `MSISDNSTATUS` (`msisdn`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `obd_msisdn_2017_05_24_24may2017_2_0` */

DROP TABLE IF EXISTS `obd_msisdn_2017_05_24_24may2017_2_0`;

CREATE TABLE `obd_msisdn_2017_05_24_24may2017_2_0` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(25) DEFAULT NULL,
  `status` varchar(25) DEFAULT NULL,
  `reason` varchar(100) DEFAULT NULL,
  `failedreason_status` varchar(100) DEFAULT 'scheduled',
  `autotimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `STATUS` (`status`),
  KEY `MSISDNSTATUS` (`msisdn`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `predictionfootball` */

DROP TABLE IF EXISTS `predictionfootball`;

CREATE TABLE `predictionfootball` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Questions` varchar(500) NOT NULL,
  `EndDate` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `predictionfootballuserrecord` */

DROP TABLE IF EXISTS `predictionfootballuserrecord`;

CREATE TABLE `predictionfootballuserrecord` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) NOT NULL,
  `QuestionId` text NOT NULL,
  `AnswerGiven` text NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `MSISDN` (`MSISDN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `quizquestionbank` */

DROP TABLE IF EXISTS `quizquestionbank`;

CREATE TABLE `quizquestionbank` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `QuestionFilePath` varchar(200) DEFAULT NULL,
  `CorrectAnswer` int(11) NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `AnswerPrompt` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=485 DEFAULT CHARSET=latin1;

/*Table structure for table `quizuserdetails` */

DROP TABLE IF EXISTS `quizuserdetails`;

CREATE TABLE `quizuserdetails` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) CHARACTER SET utf8 NOT NULL,
  `QuestionIdDetails` text CHARACTER SET utf8 NOT NULL,
  `AnswerRecord` text CHARACTER SET utf8 NOT NULL,
  `CorrectAnswerCount` int(11) NOT NULL,
  `Date` date NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `MSISDN` (`MSISDN`,`Date`)
) ENGINE=InnoDB AUTO_INCREMENT=8542 DEFAULT CHARSET=latin1;

/*Table structure for table `service` */

DROP TABLE IF EXISTS `service`;

CREATE TABLE `service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` int(11) NOT NULL,
  `jobname` varchar(25) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `priority` int(11) NOT NULL,
  `status` varchar(15) NOT NULL,
  `blackout_hours` int(11) NOT NULL,
  `max_retry` int(11) NOT NULL DEFAULT '0',
  `remaining_retry` int(11) NOT NULL DEFAULT '0',
  `starcopy` tinyint(1) NOT NULL DEFAULT '0',
  `recorddedication` tinyint(1) NOT NULL,
  `ServerIP` varchar(15) DEFAULT '127.0.0.1',
  `daywise` int(10) DEFAULT '0',
  `max_obd_count` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `jobname` (`jobname`,`ServerIP`),
  KEY `FK_service` (`service_id`),
  KEY `FK_blackout` (`blackout_hours`),
  CONSTRAINT `FK_blackout` FOREIGN KEY (`blackout_hours`) REFERENCES `obd_blackout_hours` (`id`),
  CONSTRAINT `FK_service` FOREIGN KEY (`service_id`) REFERENCES `mxgraph` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `soccer_details` */

DROP TABLE IF EXISTS `soccer_details`;

CREATE TABLE `soccer_details` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `ContentType` varchar(25) CHARACTER SET utf8 NOT NULL,
  `MaxQuestions` int(10) NOT NULL,
  `QuestionPoints` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Table structure for table `sub_update` */

DROP TABLE IF EXISTS `sub_update`;

CREATE TABLE `sub_update` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `SERVICE_ID` varchar(30) DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `LAST_RENEW_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  `MDN_TYPE` varchar(2) DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `PRICE` int(2) DEFAULT NULL,
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  `STATUS` varchar(20) DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUB_TIME_LEFT` int(3) DEFAULT NULL,
  `ALERT` int(1) DEFAULT NULL,
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `subscription` */

DROP TABLE IF EXISTS `subscription`;

CREATE TABLE `subscription` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `SERVICE_ID` varchar(30) DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `LAST_RENEW_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  `MDN_TYPE` varchar(2) DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `PRICE` int(2) DEFAULT NULL,
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  `STATUS` varchar(20) DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUB_TIME_LEFT` int(3) DEFAULT NULL,
  `ALERT` int(1) DEFAULT NULL,
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  `FirstCallerOfMonth` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_1` (`MSISDN`,`SERVICE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `subscription_count` */

DROP TABLE IF EXISTS `subscription_count`;

CREATE TABLE `subscription_count` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `hour` int(3) DEFAULT NULL,
  `service` varchar(50) DEFAULT NULL,
  `subservice` varchar(50) DEFAULT NULL,
  `sub_attempt_obd` int(11) DEFAULT NULL,
  `sub_success_obd` int(11) DEFAULT NULL,
  `sub_fail_obd` int(11) DEFAULT NULL,
  `sub_fail_low_obd` int(11) DEFAULT NULL,
  `sub_fail_other_obd` int(11) DEFAULT NULL,
  `sub_rev_obd` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `tbl_mapping` */

DROP TABLE IF EXISTS `tbl_mapping`;

CREATE TABLE `tbl_mapping` (
  `i_map_id` int(100) NOT NULL AUTO_INCREMENT,
  `v_campaign_name` varchar(100) NOT NULL,
  `v_service_name` varchar(100) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  PRIMARY KEY (`i_map_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `tbl_obd_summary` */

DROP TABLE IF EXISTS `tbl_obd_summary`;

CREATE TABLE `tbl_obd_summary` (
  `i_id` int(100) NOT NULL AUTO_INCREMENT,
  `v_campaign_name` varchar(100) NOT NULL,
  `v_service_name` varchar(100) NOT NULL,
  `v_service` varchar(100) NOT NULL,
  `v_campaign_type` varchar(100) NOT NULL,
  `v_start_date` varchar(100) NOT NULL,
  `v_end_date` varchar(100) NOT NULL,
  `v_start_time` varchar(100) NOT NULL,
  `v_end_time` varchar(100) NOT NULL,
  `v_cli` varchar(100) NOT NULL,
  `v_protocol` varchar(100) NOT NULL,
  `v_msc_ip` varchar(100) NOT NULL,
  `v_msisdn_file_name` varchar(100) NOT NULL,
  `v_user` varchar(100) NOT NULL,
  `max_obd_count` int(10) DEFAULT NULL,
  PRIMARY KEY (`i_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




/*Table structure for table `tbl_seo_info` */

DROP TABLE IF EXISTS `tbl_seo_info`;

CREATE TABLE `tbl_seo_info` (
  `v_page` varchar(200) DEFAULT NULL,
  `v_page_title` varchar(255) DEFAULT NULL,
  `v_sub_title` varchar(255) NOT NULL,
  `v_h1_keywords` varchar(255) NOT NULL,
  `v_window_title` varchar(200) DEFAULT NULL,
  `v_meta_keywords` text,
  `v_meta_description` text,
  `dt_lastmodify` datetime DEFAULT NULL,
  `v_modifiedBy` varchar(50) DEFAULT NULL,
  UNIQUE KEY `v_page` (`v_page`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `tbl_users` */

DROP TABLE IF EXISTS `tbl_users`;

CREATE TABLE `tbl_users` (
  `i_userID` int(5) NOT NULL AUTO_INCREMENT,
  `v_username` varchar(100) CHARACTER SET utf8 NOT NULL,
  `v_password` varchar(40) DEFAULT NULL,
  `v_email` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `v_orgnization` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  `v_firstName` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `v_lastName` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `v_address` varchar(225) CHARACTER SET utf8 DEFAULT NULL,
  `v_country` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `v_country_code` varchar(50) CHARACTER SET utf8 NOT NULL,
  `v_phone` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `v_joined_date` date NOT NULL,
  `v_regnIPAddress` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `v_activation_code` int(10) NOT NULL DEFAULT '0',
  `v_user_activated` int(1) NOT NULL DEFAULT '0',
  `t_lastLoginTimeStamp` int(10) NOT NULL,
  `v_type` int(1) NOT NULL DEFAULT '0' COMMENT '0-admin,1-Technical,2-Marketing',
  `c_status` char(1) CHARACTER SET utf8 NOT NULL DEFAULT 'U',
  PRIMARY KEY (`i_userID`),
  UNIQUE KEY `v_email` (`v_email`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

/*Table structure for table `unsubscription` */

DROP TABLE IF EXISTS `unsubscription`;

CREATE TABLE `unsubscription` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `SERVICE_ID` varchar(50) DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `LAST_RENEW_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  `PAID_TIME_LEFT` int(6) DEFAULT NULL,
  `FREE_TIME_LEFT` int(6) DEFAULT NULL,
  `MDN_TYPE` varchar(2) DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `PRICE` int(2) DEFAULT NULL,
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `DEACT_MODE` varchar(25) DEFAULT NULL,
  `DEACT_DATE` datetime DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUB_TIME_LEFT` int(3) DEFAULT NULL,
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `visitors` */

DROP TABLE IF EXISTS `visitors`;

CREATE TABLE `visitors` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `SERVICE_ID` varchar(50) DEFAULT NULL,
  `SHORT_CODE` varchar(10) DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_2` (`MSISDN`,`SERVICE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `voicechat_userrecords` */

DROP TABLE IF EXISTS `voicechat_userrecords`;

CREATE TABLE `voicechat_userrecords` (
  `chatid` bigint(20) NOT NULL,
  `msisdn` bigint(20) NOT NULL,
  `usertype` tinyint(4) NOT NULL,
  `profilemessage` text NOT NULL,
  `gender` varchar(10) NOT NULL,
  `language` varchar(5) NOT NULL,
  `interest` varchar(10) NOT NULL,
  `age` int(10) DEFAULT NULL,
  `maritalstatus` varchar(10) DEFAULT NULL,
  `profession` varchar(20) DEFAULT NULL,
  `callstatus` tinyint(4) NOT NULL,
  `loginstatus` tinyint(4) NOT NULL,
  `lastupdated` datetime NOT NULL,
  PRIMARY KEY (`chatid`),
  UNIQUE KEY `msisdn` (`msisdn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Saves User details for Voice Chat';


CREATE TABLE `voicefrequency` (
  `voiceid` VARCHAR(100) DEFAULT NULL,
  `frequency` VARCHAR(100) DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;


/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - smsclient
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `smsclient`;

USE `smsclient`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `activealerts` */

DROP TABLE IF EXISTS `activealerts`;

CREATE TABLE `activealerts` (
  `slNo` int(10) NOT NULL AUTO_INCREMENT,
  `serviceid` varchar(50) NOT NULL,
  `subserviceid` varchar(50) DEFAULT NULL,
  `type` smallint(2) NOT NULL DEFAULT '0' COMMENT 'Namaz=1; Combo=2',
  `protocol` smallint(2) NOT NULL DEFAULT '0' COMMENT 'SMPP=0; SOAP=1; HTTP=3',
  `priority` smallint(2) NOT NULL DEFAULT '0' COMMENT 'High=0; Medium=1; Low=2',
  `circle` varchar(25) NOT NULL COMMENT 'default value minimun SMSCConfigs.cid',
  `cli` varchar(25) DEFAULT NULL COMMENT 'default value in sms_properties table',
  `language` varchar(10) DEFAULT '' COMMENT ' Its NOT User Language. Used for Language based configurations',
  PRIMARY KEY (`slNo`),
  KEY `alerts` (`serviceid`,`subserviceid`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

/*Table structure for table `alertlogs` */

DROP TABLE IF EXISTS `alertlogs`;

CREATE TABLE `alertlogs` (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `cli` varchar(100) NOT NULL,
  `circle` varchar(25) NOT NULL,
  `baseSize` int(11) NOT NULL,
  `status` varchar(100) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `expiresAt` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`pid`),
  KEY `promo` (`name`,`status`,`timestamp`)
) ENGINE=InnoDB AUTO_INCREMENT=701 DEFAULT CHARSET=latin1;

/*Table structure for table `alertscontent` */

DROP TABLE IF EXISTS `alertscontent`;

CREATE TABLE `alertscontent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `msgDay` smallint(2) NOT NULL,
  `serviceId` varchar(50) NOT NULL,
  `subServiceId` varchar(50) DEFAULT NULL,
  `sendingTime` time NOT NULL,
  `msgText` text NOT NULL,
  `msgFlag` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Namaz` (`msgDay`,`serviceId`,`subServiceId`,`sendingTime`),
  KEY `Combo` (`msgDay`,`serviceId`,`subServiceId`,`msgFlag`)
) ENGINE=InnoDB AUTO_INCREMENT=20728 DEFAULT CHARSET=utf8;

/*Table structure for table `blackouthours` */

DROP TABLE IF EXISTS `blackouthours`;

CREATE TABLE `blackouthours` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blackout_start` time NOT NULL,
  `blackout_end` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `callbackdetails` */

DROP TABLE IF EXISTS `callbackdetails`;

CREATE TABLE `callbackdetails` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `action` varchar(25) DEFAULT NULL,
  `serviceid` varchar(50) NOT NULL,
  `subServiceid` varchar(50) DEFAULT NULL,
  `additionals` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `doubleconsent` */

DROP TABLE IF EXISTS `doubleconsent`;

CREATE TABLE `doubleconsent` (
  `msisdn` varchar(20) NOT NULL DEFAULT '',
  `shortcode` varchar(25) NOT NULL,
  `message` text NOT NULL,
  `expiresAt` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `timestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`msisdn`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='latin1_swedish_ci';

/*Table structure for table `languagespecification` */

DROP TABLE IF EXISTS `languagespecification`;

CREATE TABLE `languagespecification` (
  `lid` int(11) NOT NULL AUTO_INCREMENT,
  `language` varchar(7) NOT NULL DEFAULT '_E',
  `dataCoding` int(5) NOT NULL DEFAULT '8',
  `serviceType` varchar(25) DEFAULT 'CMT',
  `encoding` varchar(25) NOT NULL DEFAULT 'false',
  `script` int(10) DEFAULT '0',
  PRIMARY KEY (`lid`),
  UNIQUE KEY `Language` (`language`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COMMENT='latin1_swedish_ci';

/*Table structure for table `messageactions` */

DROP TABLE IF EXISTS `messageactions`;

CREATE TABLE `messageactions` (
  `aid` int(10) NOT NULL AUTO_INCREMENT,
  `moId` int(10) NOT NULL,
  `type` varchar(10) NOT NULL DEFAULT 'get' COMMENT 'HTTP Method (GET/POST)',
  `details` text COMMENT 'URL',
  PRIMARY KEY (`aid`),
  KEY `FK_MessageActions` (`moId`),
  CONSTRAINT `FK_MessageActions` FOREIGN KEY (`moId`) REFERENCES `messageformats` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Table structure for table `messageformats` */

DROP TABLE IF EXISTS `messageformats`;

CREATE TABLE `messageformats` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `serviceCode` varchar(10) NOT NULL,
  `keyword` varchar(100) NOT NULL,
  `subkey` varchar(100) DEFAULT NULL,
  `argument1` varchar(100) DEFAULT NULL,
  `argument2` varchar(100) DEFAULT NULL,
  `argument3` varchar(100) DEFAULT NULL,
  `serviceid` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MO` (`serviceCode`,`keyword`,`subkey`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Table structure for table `msisdnseries` */

DROP TABLE IF EXISTS `msisdnseries`;

CREATE TABLE `msisdnseries` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `series` varchar(6) NOT NULL,
  `circleId` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_MsisdnSeries` (`circleId`),
  CONSTRAINT `FK_MsisdnSeries` FOREIGN KEY (`circleId`) REFERENCES `smscconfigs` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;


CREATE TABLE `msgcontents` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `service` varchar(20) NOT NULL,
  `event` varchar(20) NOT NULL,
  `content` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `smsblacklist` */

DROP TABLE IF EXISTS `smsblacklist`;

CREATE TABLE `smsblacklist` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(20) NOT NULL,
  `isSeries` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=198838 DEFAULT CHARSET=latin1;

/*Table structure for table `smscconfigs` */

DROP TABLE IF EXISTS `smscconfigs`;

CREATE TABLE `smscconfigs` (
  `cid` int(10) NOT NULL AUTO_INCREMENT,
  `opId` int(10) NOT NULL,
  `circle` varchar(5) NOT NULL COMMENT 'In Caps, should be unique for every SMSC',
  `serverIp` varchar(32) NOT NULL,
  `serverPort` int(7) NOT NULL,
  `serviceUri` text COMMENT 'Used for SOAP, Default value=#',
  `userid` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `timeout` int(10) DEFAULT '10000' COMMENT 'For SMPP=10000; HTTP/SOAP=40000',
  `systemType` varchar(50) DEFAULT NULL,
  `responseType` varchar(100) DEFAULT NULL,
  `maxConnections` varchar(100) DEFAULT '3',
  `contentType` varchar(100) DEFAULT NULL,
  `bindMode` int(10) NOT NULL,
  PRIMARY KEY (`cid`),
  UNIQUE KEY `circle` (`opId`,`circle`,`serverIp`,`serverPort`),
  CONSTRAINT `FK_SMSCConfig_opid` FOREIGN KEY (`opId`) REFERENCES `smscdetails` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Table structure for table `smscdetails` */

DROP TABLE IF EXISTS `smscdetails`;

CREATE TABLE `smscdetails` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `operator` varchar(20) NOT NULL,
  `country` varchar(25) NOT NULL,
  `protocol` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Table structure for table `smscformats` */

DROP TABLE IF EXISTS `smscformats`;

CREATE TABLE `smscformats` (
  `rid` int(10) NOT NULL AUTO_INCREMENT,
  `cid` int(10) NOT NULL,
  `requestFormat` text,
  `responseFormat` text,
  `mode` varchar(15) DEFAULT 'get' COMMENT 'HTTP Method (GET/POST)',
  `register` int(1) DEFAULT '0',
  `MORegisterFormat` text,
  `MTRegisterFormat` text,
  PRIMARY KEY (`rid`),
  KEY `FK_SMSCFormats_cid` (`cid`),
  CONSTRAINT `FK_SMSCFormats_cid` FOREIGN KEY (`cid`) REFERENCES `smscconfigs` (`cid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Table structure for table `smslogs` */

DROP TABLE IF EXISTS `smslogs`;

CREATE TABLE `smslogs` (
  `messageId` varchar(25) NOT NULL,
  `sender` varchar(25) NOT NULL,
  `receiver` varchar(25) NOT NULL,
  `circle` varchar(10) NOT NULL,
  `transId` text NOT NULL,
  `price` double NOT NULL,
  `callback` int(10) NOT NULL DEFAULT '0',
  `autotimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`messageId`,`autotimestamp`),
  KEY `MT` (`messageId`,`receiver`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `smssubscription` */

DROP TABLE IF EXISTS `smssubscription`;

CREATE TABLE `smssubscription` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(20) NOT NULL,
  `serviceid` varchar(50) NOT NULL,
  `subserviceid` varchar(50) NOT NULL,
  `status` varchar(10) NOT NULL,
  `startdate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `language` varchar(10) NOT NULL,
  `shortcode` varchar(15) NOT NULL,
  `operator` varchar(10) NOT NULL,
  `country` varchar(10) NOT NULL,
  `lastprocessed` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `msgflag` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Subscriptions` (`msisdn`,`serviceid`,`subserviceid`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=24910 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;


CREATE TABLE `matchcontent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `content` text CHARACTER SET utf8,
  `language` varchar(10) NOT NULL DEFAULT '_E',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `smspromotion` (
  `promoId` bigint(20) NOT NULL AUTO_INCREMENT,
  `promotionName` varchar(50) NOT NULL,
  `startDateTime` datetime NOT NULL,
  `expiryDateTime` datetime NOT NULL,
  `MessageFrom` date DEFAULT NULL,
  `MessageTo` date DEFAULT NULL,
  `MostActive` int(11) DEFAULT '0',
  `userStatus` varchar(10) DEFAULT NULL,
  `msgText` text NOT NULL,
  `language` varchar(10) DEFAULT NULL,
  `status` varchar(50) DEFAULT 'new',
  `service` varchar(20) DEFAULT NULL,
  `callerId` varchar(20) DEFAULT NULL,
  `circle` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`promoId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `promotionmsisdn` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `promoId` bigint(20) DEFAULT NULL,
  `promotionName` varchar(50) NOT NULL,
  `msisdn` varchar(50) NOT NULL,
  `status` varchar(10) DEFAULT 'new',
  PRIMARY KEY (`id`),
  KEY `promoId` (`promoId`),
  CONSTRAINT `promotionMsisdn_ibfk_1` FOREIGN KEY (`promoId`) REFERENCES `smspromotion` (`promoId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `smswhitelist` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `shortcode` varchar(10) DEFAULT NULL,
  `msisdn` varchar(20) NOT NULL,
  `isSeries` int(1) NOT NULL DEFAULT '0',
  `msgType` varchar(2) NOT NULL DEFAULT 'MT',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;



/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - subs_engine0
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `subs_engine0`;

USE `subs_engine0`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `subscription` */

DROP TABLE IF EXISTS `subscription`;

CREATE TABLE `subscription` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `subservice_id` varchar(25) DEFAULT NULL,
  `SUB_START_DATE` datetime DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `scheduler_renewal_status` varchar(25) NOT NULL DEFAULT 'scheduled',
  `STATUS` varchar(20) DEFAULT NULL,
  `LAST_RENEW_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `price` float DEFAULT '0',
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUB_TIME_LEFT` int(3) DEFAULT '0',
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  `OPERATOR` varchar(25) DEFAULT NULL,
  `GIFT_ID` varchar(25) DEFAULT NULL,
  `service_id` varchar(25) DEFAULT NULL,
  `RETRY_COUNT` int(2) NOT NULL DEFAULT '0',
  `NEXT_RETRY_DATE` datetime DEFAULT NULL,
  `CPID` int(11) DEFAULT NULL,
  `SCHEDULER_RETRY_STATUS` varchar(25) DEFAULT 'scheduled',
  `scheduler_statechanger_status` varchar(25) DEFAULT 'scheduled',
  `scheduler_subs_msgs_status` varchar(25) DEFAULT 'scheduled',
  `serviceName` varchar(25) DEFAULT NULL,
  `transactionId` varchar(50) DEFAULT NULL,
  `nextAction` varchar(25) DEFAULT NULL,
  `topUpPrice` int(3) DEFAULT NULL,
  `topUp_time_left` int(5) NOT NULL DEFAULT '0',
  `autoRenew` varchar(10) DEFAULT NULL,
  `option1` varchar(25) DEFAULT NULL,
  `callBackRecvFlag` varchar(10) DEFAULT NULL,
  `last_msg_date` date DEFAULT NULL,
  `accountType` varchar(10) DEFAULT 'prepaid',
  `successCallCount` int(3) DEFAULT '0',
  `preferredFrequency` int(5) DEFAULT NULL,
  `preferredBgm` varchar(500) DEFAULT NULL,
  `next_msg_date` datetime DEFAULT NULL,
  `firstActiveDate` datetime DEFAULT NULL,
  `lastsuccesscalltopastor` datetime DEFAULT NULL,
  `church` varchar(20) DEFAULT '0',
  `renewal_count` int(11) DEFAULT '0',
  `stations` varchar(50) DEFAULT '0',
  `bucket` varchar(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_3` (`MSISDN`,`subservice_id`),
  KEY `MSISDN` (`MSISDN`,`service_id`)
)ENGINE=InnoDB AUTO_INCREMENT=473592 DEFAULT CHARSET=utf8;

/*Table structure for table `unsubscription` */

DROP TABLE IF EXISTS `unsubscription`;

CREATE TABLE `unsubscription` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` VARCHAR(25) DEFAULT NULL,
  `subservice_id` VARCHAR(25) DEFAULT NULL,
  `SUB_START_DATE` DATETIME DEFAULT NULL,
  `SUB_END_DATE` DATETIME DEFAULT NULL,
  `LAST_RENEW_DATE` DATETIME DEFAULT NULL,
  `LAST_CALL_DATE` DATETIME DEFAULT NULL,
  `TRANSACTION_STATUS` VARCHAR(15) DEFAULT NULL,
  `PAID_TIME_LEFT` INT(6) DEFAULT NULL,
  `FREE_TIME_LEFT` INT(6) DEFAULT NULL,
  `CIRCLE` VARCHAR(3) DEFAULT NULL,
  `COUNTRY` VARCHAR(3) DEFAULT NULL,
  `LANGUAGE` VARCHAR(3) DEFAULT NULL,
  `PRICE` INT(2) DEFAULT NULL,
  `CALL_ATTEMPTS` INT(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_DATE` DATETIME DEFAULT NULL,
  `ERROR_MSG` VARCHAR(25) DEFAULT NULL,
  `SUB_TYPE` VARCHAR(10) DEFAULT NULL,
  `SUB_TIME_LEFT` INT(3) DEFAULT '0',
  `SUB_SERVICE_NAME` VARCHAR(25) DEFAULT NULL,
  `UNSUB_DATE` DATETIME DEFAULT NULL,
  `service_id` VARCHAR(25) DEFAULT NULL,
  `GIFT_ID` VARCHAR(25) DEFAULT NULL,
  `RETRY_COUNT` INT(2) DEFAULT NULL,
  `NEXT_RETRY_DATE` DATETIME DEFAULT NULL,
  `CPID` INT(11) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  `firstActiveDate` DATETIME DEFAULT NULL,
  `firstSubDate` DATETIME DEFAULT NULL,
  `previousState` VARCHAR(50) DEFAULT '0',
  `stations` VARCHAR(50) DEFAULT '0',
  `bucket` VARCHAR(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `msdnsvc` (`MSISDN`,`service_id`)
)  ENGINE=InnoDB AUTO_INCREMENT=415683 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;



/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - subs_engine1
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `subs_engine1`;

USE `subs_engine1`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `subscription` */

DROP TABLE IF EXISTS `subscription`;

CREATE TABLE `subscription` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `subservice_id` varchar(25) DEFAULT NULL,
  `SUB_START_DATE` datetime DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `scheduler_renewal_status` varchar(25) NOT NULL DEFAULT 'scheduled',
  `STATUS` varchar(20) DEFAULT NULL,
  `LAST_RENEW_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `price` float DEFAULT '0',
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUB_TIME_LEFT` int(3) DEFAULT '0',
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  `OPERATOR` varchar(25) DEFAULT NULL,
  `GIFT_ID` varchar(25) DEFAULT NULL,
  `service_id` varchar(25) DEFAULT NULL,
  `RETRY_COUNT` int(2) NOT NULL DEFAULT '0',
  `NEXT_RETRY_DATE` datetime DEFAULT NULL,
  `CPID` int(11) DEFAULT NULL,
  `SCHEDULER_RETRY_STATUS` varchar(25) DEFAULT 'scheduled',
  `scheduler_statechanger_status` varchar(25) DEFAULT 'scheduled',
  `scheduler_subs_msgs_status` varchar(25) DEFAULT 'scheduled',
  `serviceName` varchar(25) DEFAULT NULL,
  `transactionId` varchar(50) DEFAULT NULL,
  `nextAction` varchar(25) DEFAULT NULL,
  `topUpPrice` int(3) DEFAULT NULL,
  `topUp_time_left` int(5) NOT NULL DEFAULT '0',
  `autoRenew` varchar(10) DEFAULT NULL,
  `option1` varchar(25) DEFAULT NULL,
  `callBackRecvFlag` varchar(10) DEFAULT NULL,
  `last_msg_date` date DEFAULT NULL,
  `accountType` varchar(10) DEFAULT 'prepaid',
  `successCallCount` int(3) DEFAULT '0',
  `preferredFrequency` int(5) DEFAULT NULL,
  `preferredBgm` varchar(500) DEFAULT NULL,
  `next_msg_date` datetime DEFAULT NULL,
  `firstActiveDate` datetime DEFAULT NULL,
  `lastsuccesscalltopastor` datetime DEFAULT NULL,
  `church` varchar(20) DEFAULT '0',
  `renewal_count` int(11) DEFAULT '0',
  `stations` varchar(50) DEFAULT '0',
  `bucket` varchar(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_3` (`MSISDN`,`subservice_id`),
  KEY `MSISDN` (`MSISDN`,`service_id`))
 ENGINE=InnoDB AUTO_INCREMENT=473592 DEFAULT CHARSET=utf8;

/*Table structure for table `unsubscription` */

DROP TABLE IF EXISTS `unsubscription`;

CREATE TABLE `unsubscription` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` VARCHAR(25) DEFAULT NULL,
  `subservice_id` VARCHAR(25) DEFAULT NULL,
  `SUB_START_DATE` DATETIME DEFAULT NULL,
  `SUB_END_DATE` DATETIME DEFAULT NULL,
  `LAST_RENEW_DATE` DATETIME DEFAULT NULL,
  `LAST_CALL_DATE` DATETIME DEFAULT NULL,
  `TRANSACTION_STATUS` VARCHAR(15) DEFAULT NULL,
  `PAID_TIME_LEFT` INT(6) DEFAULT NULL,
  `FREE_TIME_LEFT` INT(6) DEFAULT NULL,
  `CIRCLE` VARCHAR(3) DEFAULT NULL,
  `COUNTRY` VARCHAR(3) DEFAULT NULL,
  `LANGUAGE` VARCHAR(3) DEFAULT NULL,
  `PRICE` INT(2) DEFAULT NULL,
  `CALL_ATTEMPTS` INT(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_DATE` DATETIME DEFAULT NULL,
  `ERROR_MSG` VARCHAR(25) DEFAULT NULL,
  `SUB_TYPE` VARCHAR(10) DEFAULT NULL,
  `SUB_TIME_LEFT` INT(3) DEFAULT '0',
  `SUB_SERVICE_NAME` VARCHAR(25) DEFAULT NULL,
  `UNSUB_DATE` DATETIME DEFAULT NULL,
  `service_id` VARCHAR(25) DEFAULT NULL,
  `GIFT_ID` VARCHAR(25) DEFAULT NULL,
  `RETRY_COUNT` INT(2) DEFAULT NULL,
  `NEXT_RETRY_DATE` DATETIME DEFAULT NULL,
  `CPID` INT(11) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  `firstActiveDate` DATETIME DEFAULT NULL,
  `firstSubDate` DATETIME DEFAULT NULL,
  `previousState` VARCHAR(50) DEFAULT '0',
  `stations` VARCHAR(50) DEFAULT '0',
  `bucket` VARCHAR(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `msdnsvc` (`MSISDN`,`service_id`)
)  ENGINE=InnoDB AUTO_INCREMENT=415683 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;


/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - subs_engine2
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `subs_engine2`;

USE `subs_engine2`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `subscription` */

DROP TABLE IF EXISTS `subscription`;

CREATE TABLE `subscription` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `subservice_id` varchar(25) DEFAULT NULL,
  `SUB_START_DATE` datetime DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `scheduler_renewal_status` varchar(25) NOT NULL DEFAULT 'scheduled',
  `STATUS` varchar(20) DEFAULT NULL,
  `LAST_RENEW_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `price` float DEFAULT '0',
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUB_TIME_LEFT` int(3) DEFAULT '0',
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  `OPERATOR` varchar(25) DEFAULT NULL,
  `GIFT_ID` varchar(25) DEFAULT NULL,
  `service_id` varchar(25) DEFAULT NULL,
  `RETRY_COUNT` int(2) NOT NULL DEFAULT '0',
  `NEXT_RETRY_DATE` datetime DEFAULT NULL,
  `CPID` int(11) DEFAULT NULL,
  `SCHEDULER_RETRY_STATUS` varchar(25) DEFAULT 'scheduled',
  `scheduler_statechanger_status` varchar(25) DEFAULT 'scheduled',
  `scheduler_subs_msgs_status` varchar(25) DEFAULT 'scheduled',
  `serviceName` varchar(25) DEFAULT NULL,
  `transactionId` varchar(50) DEFAULT NULL,
  `nextAction` varchar(25) DEFAULT NULL,
  `topUpPrice` int(3) DEFAULT NULL,
  `topUp_time_left` int(5) NOT NULL DEFAULT '0',
  `autoRenew` varchar(10) DEFAULT NULL,
  `option1` varchar(25) DEFAULT NULL,
  `callBackRecvFlag` varchar(10) DEFAULT NULL,
  `last_msg_date` date DEFAULT NULL,
  `accountType` varchar(10) DEFAULT 'prepaid',
  `successCallCount` int(3) DEFAULT '0',
  `preferredFrequency` int(5) DEFAULT NULL,
  `preferredBgm` varchar(500) DEFAULT NULL,
  `next_msg_date` datetime DEFAULT NULL,
  `firstActiveDate` datetime DEFAULT NULL,
  `lastsuccesscalltopastor` datetime DEFAULT NULL,
  `church` varchar(20) DEFAULT '0',
  `renewal_count` int(11) DEFAULT '0',
  `stations` varchar(50) DEFAULT '0',
  `bucket` varchar(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_3` (`MSISDN`,`subservice_id`),
  KEY `MSISDN` (`MSISDN`,`service_id`)
)ENGINE=InnoDB AUTO_INCREMENT=473592 DEFAULT CHARSET=utf8;

/*Table structure for table `unsubscription` */

DROP TABLE IF EXISTS `unsubscription`;

CREATE TABLE `unsubscription` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` VARCHAR(25) DEFAULT NULL,
  `subservice_id` VARCHAR(25) DEFAULT NULL,
  `SUB_START_DATE` DATETIME DEFAULT NULL,
  `SUB_END_DATE` DATETIME DEFAULT NULL,
  `LAST_RENEW_DATE` DATETIME DEFAULT NULL,
  `LAST_CALL_DATE` DATETIME DEFAULT NULL,
  `TRANSACTION_STATUS` VARCHAR(15) DEFAULT NULL,
  `PAID_TIME_LEFT` INT(6) DEFAULT NULL,
  `FREE_TIME_LEFT` INT(6) DEFAULT NULL,
  `CIRCLE` VARCHAR(3) DEFAULT NULL,
  `COUNTRY` VARCHAR(3) DEFAULT NULL,
  `LANGUAGE` VARCHAR(3) DEFAULT NULL,
  `PRICE` INT(2) DEFAULT NULL,
  `CALL_ATTEMPTS` INT(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_DATE` DATETIME DEFAULT NULL,
  `ERROR_MSG` VARCHAR(25) DEFAULT NULL,
  `SUB_TYPE` VARCHAR(10) DEFAULT NULL,
  `SUB_TIME_LEFT` INT(3) DEFAULT '0',
  `SUB_SERVICE_NAME` VARCHAR(25) DEFAULT NULL,
  `UNSUB_DATE` DATETIME DEFAULT NULL,
  `service_id` VARCHAR(25) DEFAULT NULL,
  `GIFT_ID` VARCHAR(25) DEFAULT NULL,
  `RETRY_COUNT` INT(2) DEFAULT NULL,
  `NEXT_RETRY_DATE` DATETIME DEFAULT NULL,
  `CPID` INT(11) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  `firstActiveDate` DATETIME DEFAULT NULL,
  `firstSubDate` DATETIME DEFAULT NULL,
  `previousState` VARCHAR(50) DEFAULT '0',
  `stations` VARCHAR(50) DEFAULT '0',
  `bucket` VARCHAR(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `msdnsvc` (`MSISDN`,`service_id`)
)  ENGINE=InnoDB AUTO_INCREMENT=415683 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;


/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - subs_engine3
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `subs_engine3`;

USE `subs_engine3`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `subscription` */

CREATE TABLE `subscription` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `subservice_id` varchar(25) DEFAULT NULL,
  `SUB_START_DATE` datetime DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `scheduler_renewal_status` varchar(25) NOT NULL DEFAULT 'scheduled',
  `STATUS` varchar(20) DEFAULT NULL,
  `LAST_RENEW_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `price` float DEFAULT '0',
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUB_TIME_LEFT` int(3) DEFAULT '0',
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  `OPERATOR` varchar(25) DEFAULT NULL,
  `GIFT_ID` varchar(25) DEFAULT NULL,
  `service_id` varchar(25) DEFAULT NULL,
  `RETRY_COUNT` int(2) NOT NULL DEFAULT '0',
  `NEXT_RETRY_DATE` datetime DEFAULT NULL,
  `CPID` int(11) DEFAULT NULL,
  `SCHEDULER_RETRY_STATUS` varchar(25) DEFAULT 'scheduled',
  `scheduler_statechanger_status` varchar(25) DEFAULT 'scheduled',
  `scheduler_subs_msgs_status` varchar(25) DEFAULT 'scheduled',
  `serviceName` varchar(25) DEFAULT NULL,
  `transactionId` varchar(50) DEFAULT NULL,
  `nextAction` varchar(25) DEFAULT NULL,
  `topUpPrice` int(3) DEFAULT NULL,
  `topUp_time_left` int(5) NOT NULL DEFAULT '0',
  `autoRenew` varchar(10) DEFAULT NULL,
  `option1` varchar(25) DEFAULT NULL,
  `callBackRecvFlag` varchar(10) DEFAULT NULL,
  `last_msg_date` date DEFAULT NULL,
  `accountType` varchar(10) DEFAULT 'prepaid',
  `successCallCount` int(3) DEFAULT '0',
  `preferredFrequency` int(5) DEFAULT NULL,
  `preferredBgm` varchar(500) DEFAULT NULL,
  `next_msg_date` datetime DEFAULT NULL,
  `firstActiveDate` datetime DEFAULT NULL,
  `lastsuccesscalltopastor` datetime DEFAULT NULL,
  `church` varchar(20) DEFAULT '0',
  `renewal_count` int(11) DEFAULT '0',
  `stations` varchar(50) DEFAULT '0',
  `bucket` varchar(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_3` (`MSISDN`,`subservice_id`),
  KEY `MSISDN` (`MSISDN`,`service_id`)
)ENGINE=InnoDB AUTO_INCREMENT=473592 DEFAULT CHARSET=utf8;

/*Table structure for table `unsubscription` */

DROP TABLE IF EXISTS `unsubscription`;

CREATE TABLE `unsubscription` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` VARCHAR(25) DEFAULT NULL,
  `subservice_id` VARCHAR(25) DEFAULT NULL,
  `SUB_START_DATE` DATETIME DEFAULT NULL,
  `SUB_END_DATE` DATETIME DEFAULT NULL,
  `LAST_RENEW_DATE` DATETIME DEFAULT NULL,
  `LAST_CALL_DATE` DATETIME DEFAULT NULL,
  `TRANSACTION_STATUS` VARCHAR(15) DEFAULT NULL,
  `PAID_TIME_LEFT` INT(6) DEFAULT NULL,
  `FREE_TIME_LEFT` INT(6) DEFAULT NULL,
  `CIRCLE` VARCHAR(3) DEFAULT NULL,
  `COUNTRY` VARCHAR(3) DEFAULT NULL,
  `LANGUAGE` VARCHAR(3) DEFAULT NULL,
  `PRICE` INT(2) DEFAULT NULL,
  `CALL_ATTEMPTS` INT(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_DATE` DATETIME DEFAULT NULL,
  `ERROR_MSG` VARCHAR(25) DEFAULT NULL,
  `SUB_TYPE` VARCHAR(10) DEFAULT NULL,
  `SUB_TIME_LEFT` INT(3) DEFAULT '0',
  `SUB_SERVICE_NAME` VARCHAR(25) DEFAULT NULL,
  `UNSUB_DATE` DATETIME DEFAULT NULL,
  `service_id` VARCHAR(25) DEFAULT NULL,
  `GIFT_ID` VARCHAR(25) DEFAULT NULL,
  `RETRY_COUNT` INT(2) DEFAULT NULL,
  `NEXT_RETRY_DATE` DATETIME DEFAULT NULL,
  `CPID` INT(11) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  `firstActiveDate` DATETIME DEFAULT NULL,
  `firstSubDate` DATETIME DEFAULT NULL,
  `previousState` VARCHAR(50) DEFAULT '0',
  `stations` VARCHAR(50) DEFAULT '0',
  `bucket` VARCHAR(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `msdnsvc` (`MSISDN`,`service_id`)
)  ENGINE=InnoDB AUTO_INCREMENT=415683 DEFAULT CHARSET=utf8;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;



/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - subs_engine4
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `subs_engine4`;

USE `subs_engine4`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `subscription` */

DROP TABLE IF EXISTS `subscription`;

CREATE TABLE `subscription` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `subservice_id` varchar(25) DEFAULT NULL,
  `SUB_START_DATE` datetime DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `scheduler_renewal_status` varchar(25) NOT NULL DEFAULT 'scheduled',
  `STATUS` varchar(20) DEFAULT NULL,
  `LAST_RENEW_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `price` float DEFAULT '0',
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUB_TIME_LEFT` int(3) DEFAULT '0',
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  `OPERATOR` varchar(25) DEFAULT NULL,
  `GIFT_ID` varchar(25) DEFAULT NULL,
  `service_id` varchar(25) DEFAULT NULL,
  `RETRY_COUNT` int(2) NOT NULL DEFAULT '0',
  `NEXT_RETRY_DATE` datetime DEFAULT NULL,
  `CPID` int(11) DEFAULT NULL,
  `SCHEDULER_RETRY_STATUS` varchar(25) DEFAULT 'scheduled',
  `scheduler_statechanger_status` varchar(25) DEFAULT 'scheduled',
  `scheduler_subs_msgs_status` varchar(25) DEFAULT 'scheduled',
  `serviceName` varchar(25) DEFAULT NULL,
  `transactionId` varchar(50) DEFAULT NULL,
  `nextAction` varchar(25) DEFAULT NULL,
  `topUpPrice` int(3) DEFAULT NULL,
  `topUp_time_left` int(5) NOT NULL DEFAULT '0',
  `autoRenew` varchar(10) DEFAULT NULL,
  `option1` varchar(25) DEFAULT NULL,
  `callBackRecvFlag` varchar(10) DEFAULT NULL,
  `last_msg_date` date DEFAULT NULL,
  `accountType` varchar(10) DEFAULT 'prepaid',
  `successCallCount` int(3) DEFAULT '0',
  `preferredFrequency` int(5) DEFAULT NULL,
  `preferredBgm` varchar(500) DEFAULT NULL,
  `next_msg_date` datetime DEFAULT NULL,
  `firstActiveDate` datetime DEFAULT NULL,
  `lastsuccesscalltopastor` datetime DEFAULT NULL,
  `church` varchar(20) DEFAULT '0',
  `renewal_count` int(11) DEFAULT '0',
  `stations` varchar(50) DEFAULT '0',
  `bucket` varchar(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_3` (`MSISDN`,`subservice_id`),
  KEY `MSISDN` (`MSISDN`,`service_id`)
)ENGINE=InnoDB AUTO_INCREMENT=473592 DEFAULT CHARSET=utf8;

/*Table structure for table `unsubscription` */

DROP TABLE IF EXISTS `unsubscription`;

CREATE TABLE `unsubscription` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` VARCHAR(25) DEFAULT NULL,
  `subservice_id` VARCHAR(25) DEFAULT NULL,
  `SUB_START_DATE` DATETIME DEFAULT NULL,
  `SUB_END_DATE` DATETIME DEFAULT NULL,
  `LAST_RENEW_DATE` DATETIME DEFAULT NULL,
  `LAST_CALL_DATE` DATETIME DEFAULT NULL,
  `TRANSACTION_STATUS` VARCHAR(15) DEFAULT NULL,
  `PAID_TIME_LEFT` INT(6) DEFAULT NULL,
  `FREE_TIME_LEFT` INT(6) DEFAULT NULL,
  `CIRCLE` VARCHAR(3) DEFAULT NULL,
  `COUNTRY` VARCHAR(3) DEFAULT NULL,
  `LANGUAGE` VARCHAR(3) DEFAULT NULL,
  `PRICE` INT(2) DEFAULT NULL,
  `CALL_ATTEMPTS` INT(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_DATE` DATETIME DEFAULT NULL,
  `ERROR_MSG` VARCHAR(25) DEFAULT NULL,
  `SUB_TYPE` VARCHAR(10) DEFAULT NULL,
  `SUB_TIME_LEFT` INT(3) DEFAULT '0',
  `SUB_SERVICE_NAME` VARCHAR(25) DEFAULT NULL,
  `UNSUB_DATE` DATETIME DEFAULT NULL,
  `service_id` VARCHAR(25) DEFAULT NULL,
  `GIFT_ID` VARCHAR(25) DEFAULT NULL,
  `RETRY_COUNT` INT(2) DEFAULT NULL,
  `NEXT_RETRY_DATE` DATETIME DEFAULT NULL,
  `CPID` INT(11) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  `firstActiveDate` DATETIME DEFAULT NULL,
  `firstSubDate` DATETIME DEFAULT NULL,
  `previousState` VARCHAR(50) DEFAULT '0',
  `stations` VARCHAR(50) DEFAULT '0',
  `bucket` VARCHAR(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `msdnsvc` (`MSISDN`,`service_id`)
)  ENGINE=InnoDB AUTO_INCREMENT=415683 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;


/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - subs_engine5
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `subs_engine5`;

USE `subs_engine5`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `subscription` */

DROP TABLE IF EXISTS `subscription`;

CREATE TABLE `subscription` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `subservice_id` varchar(25) DEFAULT NULL,
  `SUB_START_DATE` datetime DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `scheduler_renewal_status` varchar(25) NOT NULL DEFAULT 'scheduled',
  `STATUS` varchar(20) DEFAULT NULL,
  `LAST_RENEW_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `price` float DEFAULT '0',
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUB_TIME_LEFT` int(3) DEFAULT '0',
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  `OPERATOR` varchar(25) DEFAULT NULL,
  `GIFT_ID` varchar(25) DEFAULT NULL,
  `service_id` varchar(25) DEFAULT NULL,
  `RETRY_COUNT` int(2) NOT NULL DEFAULT '0',
  `NEXT_RETRY_DATE` datetime DEFAULT NULL,
  `CPID` int(11) DEFAULT NULL,
  `SCHEDULER_RETRY_STATUS` varchar(25) DEFAULT 'scheduled',
  `scheduler_statechanger_status` varchar(25) DEFAULT 'scheduled',
  `scheduler_subs_msgs_status` varchar(25) DEFAULT 'scheduled',
  `serviceName` varchar(25) DEFAULT NULL,
  `transactionId` varchar(50) DEFAULT NULL,
  `nextAction` varchar(25) DEFAULT NULL,
  `topUpPrice` int(3) DEFAULT NULL,
  `topUp_time_left` int(5) NOT NULL DEFAULT '0',
  `autoRenew` varchar(10) DEFAULT NULL,
  `option1` varchar(25) DEFAULT NULL,
  `callBackRecvFlag` varchar(10) DEFAULT NULL,
  `last_msg_date` date DEFAULT NULL,
  `accountType` varchar(10) DEFAULT 'prepaid',
  `successCallCount` int(3) DEFAULT '0',
  `preferredFrequency` int(5) DEFAULT NULL,
  `preferredBgm` varchar(500) DEFAULT NULL,
  `next_msg_date` datetime DEFAULT NULL,
  `firstActiveDate` datetime DEFAULT NULL,
  `lastsuccesscalltopastor` datetime DEFAULT NULL,
  `church` varchar(20) DEFAULT '0',
  `renewal_count` int(11) DEFAULT '0',
  `stations` varchar(50) DEFAULT '0',
  `bucket` varchar(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_3` (`MSISDN`,`subservice_id`),
  KEY `MSISDN` (`MSISDN`,`service_id`)
)ENGINE=InnoDB AUTO_INCREMENT=473592 DEFAULT CHARSET=utf8;

/*Table structure for table `unsubscription` */

DROP TABLE IF EXISTS `unsubscription`;

CREATE TABLE `unsubscription` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` VARCHAR(25) DEFAULT NULL,
  `subservice_id` VARCHAR(25) DEFAULT NULL,
  `SUB_START_DATE` DATETIME DEFAULT NULL,
  `SUB_END_DATE` DATETIME DEFAULT NULL,
  `LAST_RENEW_DATE` DATETIME DEFAULT NULL,
  `LAST_CALL_DATE` DATETIME DEFAULT NULL,
  `TRANSACTION_STATUS` VARCHAR(15) DEFAULT NULL,
  `PAID_TIME_LEFT` INT(6) DEFAULT NULL,
  `FREE_TIME_LEFT` INT(6) DEFAULT NULL,
  `CIRCLE` VARCHAR(3) DEFAULT NULL,
  `COUNTRY` VARCHAR(3) DEFAULT NULL,
  `LANGUAGE` VARCHAR(3) DEFAULT NULL,
  `PRICE` INT(2) DEFAULT NULL,
  `CALL_ATTEMPTS` INT(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_DATE` DATETIME DEFAULT NULL,
  `ERROR_MSG` VARCHAR(25) DEFAULT NULL,
  `SUB_TYPE` VARCHAR(10) DEFAULT NULL,
  `SUB_TIME_LEFT` INT(3) DEFAULT '0',
  `SUB_SERVICE_NAME` VARCHAR(25) DEFAULT NULL,
  `UNSUB_DATE` DATETIME DEFAULT NULL,
  `service_id` VARCHAR(25) DEFAULT NULL,
  `GIFT_ID` VARCHAR(25) DEFAULT NULL,
  `RETRY_COUNT` INT(2) DEFAULT NULL,
  `NEXT_RETRY_DATE` DATETIME DEFAULT NULL,
  `CPID` INT(11) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  `firstActiveDate` DATETIME DEFAULT NULL,
  `firstSubDate` DATETIME DEFAULT NULL,
  `previousState` VARCHAR(50) DEFAULT '0',
  `stations` VARCHAR(50) DEFAULT '0',
  `bucket` VARCHAR(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `msdnsvc` (`MSISDN`,`service_id`)
)  ENGINE=InnoDB AUTO_INCREMENT=415683 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;



/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - subs_engine6
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `subs_engine6`;

USE `subs_engine6`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `subscription` */

DROP TABLE IF EXISTS `subscription`;

CREATE TABLE `subscription` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `subservice_id` varchar(25) DEFAULT NULL,
  `SUB_START_DATE` datetime DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `scheduler_renewal_status` varchar(25) NOT NULL DEFAULT 'scheduled',
  `STATUS` varchar(20) DEFAULT NULL,
  `LAST_RENEW_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `price` float DEFAULT '0',
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUB_TIME_LEFT` int(3) DEFAULT '0',
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  `OPERATOR` varchar(25) DEFAULT NULL,
  `GIFT_ID` varchar(25) DEFAULT NULL,
  `service_id` varchar(25) DEFAULT NULL,
  `RETRY_COUNT` int(2) NOT NULL DEFAULT '0',
  `NEXT_RETRY_DATE` datetime DEFAULT NULL,
  `CPID` int(11) DEFAULT NULL,
  `SCHEDULER_RETRY_STATUS` varchar(25) DEFAULT 'scheduled',
  `scheduler_statechanger_status` varchar(25) DEFAULT 'scheduled',
  `scheduler_subs_msgs_status` varchar(25) DEFAULT 'scheduled',
  `serviceName` varchar(25) DEFAULT NULL,
  `transactionId` varchar(50) DEFAULT NULL,
  `nextAction` varchar(25) DEFAULT NULL,
  `topUpPrice` int(3) DEFAULT NULL,
  `topUp_time_left` int(5) NOT NULL DEFAULT '0',
  `autoRenew` varchar(10) DEFAULT NULL,
  `option1` varchar(25) DEFAULT NULL,
  `callBackRecvFlag` varchar(10) DEFAULT NULL,
  `last_msg_date` date DEFAULT NULL,
  `accountType` varchar(10) DEFAULT 'prepaid',
  `successCallCount` int(3) DEFAULT '0',
  `preferredFrequency` int(5) DEFAULT NULL,
  `preferredBgm` varchar(500) DEFAULT NULL,
  `next_msg_date` datetime DEFAULT NULL,
  `firstActiveDate` datetime DEFAULT NULL,
  `lastsuccesscalltopastor` datetime DEFAULT NULL,
  `church` varchar(20) DEFAULT '0',
  `renewal_count` int(11) DEFAULT '0',
  `stations` varchar(50) DEFAULT '0',
  `bucket` varchar(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_3` (`MSISDN`,`subservice_id`),
  KEY `MSISDN` (`MSISDN`,`service_id`)
)ENGINE=InnoDB AUTO_INCREMENT=473592 DEFAULT CHARSET=utf8;

/*Table structure for table `unsubscription` */

DROP TABLE IF EXISTS `unsubscription`;

CREATE TABLE `unsubscription` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` VARCHAR(25) DEFAULT NULL,
  `subservice_id` VARCHAR(25) DEFAULT NULL,
  `SUB_START_DATE` DATETIME DEFAULT NULL,
  `SUB_END_DATE` DATETIME DEFAULT NULL,
  `LAST_RENEW_DATE` DATETIME DEFAULT NULL,
  `LAST_CALL_DATE` DATETIME DEFAULT NULL,
  `TRANSACTION_STATUS` VARCHAR(15) DEFAULT NULL,
  `PAID_TIME_LEFT` INT(6) DEFAULT NULL,
  `FREE_TIME_LEFT` INT(6) DEFAULT NULL,
  `CIRCLE` VARCHAR(3) DEFAULT NULL,
  `COUNTRY` VARCHAR(3) DEFAULT NULL,
  `LANGUAGE` VARCHAR(3) DEFAULT NULL,
  `PRICE` INT(2) DEFAULT NULL,
  `CALL_ATTEMPTS` INT(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_DATE` DATETIME DEFAULT NULL,
  `ERROR_MSG` VARCHAR(25) DEFAULT NULL,
  `SUB_TYPE` VARCHAR(10) DEFAULT NULL,
  `SUB_TIME_LEFT` INT(3) DEFAULT '0',
  `SUB_SERVICE_NAME` VARCHAR(25) DEFAULT NULL,
  `UNSUB_DATE` DATETIME DEFAULT NULL,
  `service_id` VARCHAR(25) DEFAULT NULL,
  `GIFT_ID` VARCHAR(25) DEFAULT NULL,
  `RETRY_COUNT` INT(2) DEFAULT NULL,
  `NEXT_RETRY_DATE` DATETIME DEFAULT NULL,
  `CPID` INT(11) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  `firstActiveDate` DATETIME DEFAULT NULL,
  `firstSubDate` DATETIME DEFAULT NULL,
  `previousState` VARCHAR(50) DEFAULT '0',
  `stations` VARCHAR(50) DEFAULT '0',
  `bucket` VARCHAR(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `msdnsvc` (`MSISDN`,`service_id`)
)  ENGINE=InnoDB AUTO_INCREMENT=415683 DEFAULT CHARSET=utf8;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;


/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - subs_engine7
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `subs_engine7`;

USE `subs_engine7`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `subscription` */

DROP TABLE IF EXISTS `subscription`;

CREATE TABLE `subscription` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `subservice_id` varchar(25) DEFAULT NULL,
  `SUB_START_DATE` datetime DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `scheduler_renewal_status` varchar(25) NOT NULL DEFAULT 'scheduled',
  `STATUS` varchar(20) DEFAULT NULL,
  `LAST_RENEW_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `price` float DEFAULT '0',
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUB_TIME_LEFT` int(3) DEFAULT '0',
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  `OPERATOR` varchar(25) DEFAULT NULL,
  `GIFT_ID` varchar(25) DEFAULT NULL,
  `service_id` varchar(25) DEFAULT NULL,
  `RETRY_COUNT` int(2) NOT NULL DEFAULT '0',
  `NEXT_RETRY_DATE` datetime DEFAULT NULL,
  `CPID` int(11) DEFAULT NULL,
  `SCHEDULER_RETRY_STATUS` varchar(25) DEFAULT 'scheduled',
  `scheduler_statechanger_status` varchar(25) DEFAULT 'scheduled',
  `scheduler_subs_msgs_status` varchar(25) DEFAULT 'scheduled',
  `serviceName` varchar(25) DEFAULT NULL,
  `transactionId` varchar(50) DEFAULT NULL,
  `nextAction` varchar(25) DEFAULT NULL,
  `topUpPrice` int(3) DEFAULT NULL,
  `topUp_time_left` int(5) NOT NULL DEFAULT '0',
  `autoRenew` varchar(10) DEFAULT NULL,
  `option1` varchar(25) DEFAULT NULL,
  `callBackRecvFlag` varchar(10) DEFAULT NULL,
  `last_msg_date` date DEFAULT NULL,
  `accountType` varchar(10) DEFAULT 'prepaid',
  `successCallCount` int(3) DEFAULT '0',
  `preferredFrequency` int(5) DEFAULT NULL,
  `preferredBgm` varchar(500) DEFAULT NULL,
  `next_msg_date` datetime DEFAULT NULL,
  `firstActiveDate` datetime DEFAULT NULL,
  `lastsuccesscalltopastor` datetime DEFAULT NULL,
  `church` varchar(20) DEFAULT '0',
  `renewal_count` int(11) DEFAULT '0',
  `stations` varchar(50) DEFAULT '0',
  `bucket` varchar(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_3` (`MSISDN`,`subservice_id`),
  KEY `MSISDN` (`MSISDN`,`service_id`)
)ENGINE=InnoDB AUTO_INCREMENT=473592 DEFAULT CHARSET=utf8;

/*Table structure for table `unsubscription` */

DROP TABLE IF EXISTS `unsubscription`;

CREATE TABLE `unsubscription` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` VARCHAR(25) DEFAULT NULL,
  `subservice_id` VARCHAR(25) DEFAULT NULL,
  `SUB_START_DATE` DATETIME DEFAULT NULL,
  `SUB_END_DATE` DATETIME DEFAULT NULL,
  `LAST_RENEW_DATE` DATETIME DEFAULT NULL,
  `LAST_CALL_DATE` DATETIME DEFAULT NULL,
  `TRANSACTION_STATUS` VARCHAR(15) DEFAULT NULL,
  `PAID_TIME_LEFT` INT(6) DEFAULT NULL,
  `FREE_TIME_LEFT` INT(6) DEFAULT NULL,
  `CIRCLE` VARCHAR(3) DEFAULT NULL,
  `COUNTRY` VARCHAR(3) DEFAULT NULL,
  `LANGUAGE` VARCHAR(3) DEFAULT NULL,
  `PRICE` INT(2) DEFAULT NULL,
  `CALL_ATTEMPTS` INT(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_DATE` DATETIME DEFAULT NULL,
  `ERROR_MSG` VARCHAR(25) DEFAULT NULL,
  `SUB_TYPE` VARCHAR(10) DEFAULT NULL,
  `SUB_TIME_LEFT` INT(3) DEFAULT '0',
  `SUB_SERVICE_NAME` VARCHAR(25) DEFAULT NULL,
  `UNSUB_DATE` DATETIME DEFAULT NULL,
  `service_id` VARCHAR(25) DEFAULT NULL,
  `GIFT_ID` VARCHAR(25) DEFAULT NULL,
  `RETRY_COUNT` INT(2) DEFAULT NULL,
  `NEXT_RETRY_DATE` DATETIME DEFAULT NULL,
  `CPID` INT(11) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  `firstActiveDate` DATETIME DEFAULT NULL,
  `firstSubDate` DATETIME DEFAULT NULL,
  `previousState` VARCHAR(50) DEFAULT '0',
  `stations` VARCHAR(50) DEFAULT '0',
  `bucket` VARCHAR(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `msdnsvc` (`MSISDN`,`service_id`)
)  ENGINE=InnoDB AUTO_INCREMENT=415683 DEFAULT CHARSET=utf8;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;


/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - subs_engine8
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `subs_engine8`;

USE `subs_engine8`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `subscription` */

DROP TABLE IF EXISTS `subscription`;

CREATE TABLE `subscription` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `subservice_id` varchar(25) DEFAULT NULL,
  `SUB_START_DATE` datetime DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `scheduler_renewal_status` varchar(25) NOT NULL DEFAULT 'scheduled',
  `STATUS` varchar(20) DEFAULT NULL,
  `LAST_RENEW_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `price` float DEFAULT '0',
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUB_TIME_LEFT` int(3) DEFAULT '0',
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  `OPERATOR` varchar(25) DEFAULT NULL,
  `GIFT_ID` varchar(25) DEFAULT NULL,
  `service_id` varchar(25) DEFAULT NULL,
  `RETRY_COUNT` int(2) NOT NULL DEFAULT '0',
  `NEXT_RETRY_DATE` datetime DEFAULT NULL,
  `CPID` int(11) DEFAULT NULL,
  `SCHEDULER_RETRY_STATUS` varchar(25) DEFAULT 'scheduled',
  `scheduler_statechanger_status` varchar(25) DEFAULT 'scheduled',
  `scheduler_subs_msgs_status` varchar(25) DEFAULT 'scheduled',
  `serviceName` varchar(25) DEFAULT NULL,
  `transactionId` varchar(50) DEFAULT NULL,
  `nextAction` varchar(25) DEFAULT NULL,
  `topUpPrice` int(3) DEFAULT NULL,
  `topUp_time_left` int(5) NOT NULL DEFAULT '0',
  `autoRenew` varchar(10) DEFAULT NULL,
  `option1` varchar(25) DEFAULT NULL,
  `callBackRecvFlag` varchar(10) DEFAULT NULL,
  `last_msg_date` date DEFAULT NULL,
  `accountType` varchar(10) DEFAULT 'prepaid',
  `successCallCount` int(3) DEFAULT '0',
  `preferredFrequency` int(5) DEFAULT NULL,
  `preferredBgm` varchar(500) DEFAULT NULL,
  `next_msg_date` datetime DEFAULT NULL,
  `firstActiveDate` datetime DEFAULT NULL,
  `lastsuccesscalltopastor` datetime DEFAULT NULL,
  `church` varchar(20) DEFAULT '0',
  `renewal_count` int(11) DEFAULT '0',
  `stations` varchar(50) DEFAULT '0',
  `bucket` varchar(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_3` (`MSISDN`,`subservice_id`),
  KEY `MSISDN` (`MSISDN`,`service_id`)
)ENGINE=InnoDB AUTO_INCREMENT=473592 DEFAULT CHARSET=utf8;

/*Table structure for table `unsubscription` */

DROP TABLE IF EXISTS `unsubscription`;

CREATE TABLE `unsubscription` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` VARCHAR(25) DEFAULT NULL,
  `subservice_id` VARCHAR(25) DEFAULT NULL,
  `SUB_START_DATE` DATETIME DEFAULT NULL,
  `SUB_END_DATE` DATETIME DEFAULT NULL,
  `LAST_RENEW_DATE` DATETIME DEFAULT NULL,
  `LAST_CALL_DATE` DATETIME DEFAULT NULL,
  `TRANSACTION_STATUS` VARCHAR(15) DEFAULT NULL,
  `PAID_TIME_LEFT` INT(6) DEFAULT NULL,
  `FREE_TIME_LEFT` INT(6) DEFAULT NULL,
  `CIRCLE` VARCHAR(3) DEFAULT NULL,
  `COUNTRY` VARCHAR(3) DEFAULT NULL,
  `LANGUAGE` VARCHAR(3) DEFAULT NULL,
  `PRICE` INT(2) DEFAULT NULL,
  `CALL_ATTEMPTS` INT(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_DATE` DATETIME DEFAULT NULL,
  `ERROR_MSG` VARCHAR(25) DEFAULT NULL,
  `SUB_TYPE` VARCHAR(10) DEFAULT NULL,
  `SUB_TIME_LEFT` INT(3) DEFAULT '0',
  `SUB_SERVICE_NAME` VARCHAR(25) DEFAULT NULL,
  `UNSUB_DATE` DATETIME DEFAULT NULL,
  `service_id` VARCHAR(25) DEFAULT NULL,
  `GIFT_ID` VARCHAR(25) DEFAULT NULL,
  `RETRY_COUNT` INT(2) DEFAULT NULL,
  `NEXT_RETRY_DATE` DATETIME DEFAULT NULL,
  `CPID` INT(11) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  `firstActiveDate` DATETIME DEFAULT NULL,
  `firstSubDate` DATETIME DEFAULT NULL,
  `previousState` VARCHAR(50) DEFAULT '0',
  `stations` VARCHAR(50) DEFAULT '0',
  `bucket` VARCHAR(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `msdnsvc` (`MSISDN`,`service_id`)
)  ENGINE=InnoDB AUTO_INCREMENT=415683 DEFAULT CHARSET=utf8;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;


/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - subs_engine9
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `subs_engine9`;

USE `subs_engine9`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `subscription` */

DROP TABLE IF EXISTS `subscription`;

CREATE TABLE `subscription` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(25) DEFAULT NULL,
  `subservice_id` varchar(25) DEFAULT NULL,
  `SUB_START_DATE` datetime DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `scheduler_renewal_status` varchar(25) NOT NULL DEFAULT 'scheduled',
  `STATUS` varchar(20) DEFAULT NULL,
  `LAST_RENEW_DATE` datetime DEFAULT NULL,
  `LAST_CALL_DATE` datetime DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `price` float DEFAULT '0',
  `CALL_ATTEMPTS` int(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUB_TIME_LEFT` int(3) DEFAULT '0',
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  `OPERATOR` varchar(25) DEFAULT NULL,
  `GIFT_ID` varchar(25) DEFAULT NULL,
  `service_id` varchar(25) DEFAULT NULL,
  `RETRY_COUNT` int(2) NOT NULL DEFAULT '0',
  `NEXT_RETRY_DATE` datetime DEFAULT NULL,
  `CPID` int(11) DEFAULT NULL,
  `SCHEDULER_RETRY_STATUS` varchar(25) DEFAULT 'scheduled',
  `scheduler_statechanger_status` varchar(25) DEFAULT 'scheduled',
  `scheduler_subs_msgs_status` varchar(25) DEFAULT 'scheduled',
  `serviceName` varchar(25) DEFAULT NULL,
  `transactionId` varchar(50) DEFAULT NULL,
  `nextAction` varchar(25) DEFAULT NULL,
  `topUpPrice` int(3) DEFAULT NULL,
  `topUp_time_left` int(5) NOT NULL DEFAULT '0',
  `autoRenew` varchar(10) DEFAULT NULL,
  `option1` varchar(25) DEFAULT NULL,
  `callBackRecvFlag` varchar(10) DEFAULT NULL,
  `last_msg_date` date DEFAULT NULL,
  `accountType` varchar(10) DEFAULT 'prepaid',
  `successCallCount` int(3) DEFAULT '0',
  `preferredFrequency` int(5) DEFAULT NULL,
  `preferredBgm` varchar(500) DEFAULT NULL,
  `next_msg_date` datetime DEFAULT NULL,
  `firstActiveDate` datetime DEFAULT NULL,
  `lastsuccesscalltopastor` datetime DEFAULT NULL,
  `church` varchar(20) DEFAULT '0',
  `renewal_count` int(11) DEFAULT '0',
  `stations` varchar(50) DEFAULT '0',
  `bucket` varchar(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_3` (`MSISDN`,`subservice_id`),
  KEY `MSISDN` (`MSISDN`,`service_id`)
)ENGINE=InnoDB AUTO_INCREMENT=473592 DEFAULT CHARSET=utf8;

/*Table structure for table `unsubscription` */

DROP TABLE IF EXISTS `unsubscription`;

CREATE TABLE `unsubscription` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` VARCHAR(25) DEFAULT NULL,
  `subservice_id` VARCHAR(25) DEFAULT NULL,
  `SUB_START_DATE` DATETIME DEFAULT NULL,
  `SUB_END_DATE` DATETIME DEFAULT NULL,
  `LAST_RENEW_DATE` DATETIME DEFAULT NULL,
  `LAST_CALL_DATE` DATETIME DEFAULT NULL,
  `TRANSACTION_STATUS` VARCHAR(15) DEFAULT NULL,
  `PAID_TIME_LEFT` INT(6) DEFAULT NULL,
  `FREE_TIME_LEFT` INT(6) DEFAULT NULL,
  `CIRCLE` VARCHAR(3) DEFAULT NULL,
  `COUNTRY` VARCHAR(3) DEFAULT NULL,
  `LANGUAGE` VARCHAR(3) DEFAULT NULL,
  `PRICE` INT(2) DEFAULT NULL,
  `CALL_ATTEMPTS` INT(3) DEFAULT NULL,
  `PRIMARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `SECONDARY_ACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_MODE` VARCHAR(25) DEFAULT NULL,
  `DEACT_DATE` DATETIME DEFAULT NULL,
  `ERROR_MSG` VARCHAR(25) DEFAULT NULL,
  `SUB_TYPE` VARCHAR(10) DEFAULT NULL,
  `SUB_TIME_LEFT` INT(3) DEFAULT '0',
  `SUB_SERVICE_NAME` VARCHAR(25) DEFAULT NULL,
  `UNSUB_DATE` DATETIME DEFAULT NULL,
  `service_id` VARCHAR(25) DEFAULT NULL,
  `GIFT_ID` VARCHAR(25) DEFAULT NULL,
  `RETRY_COUNT` INT(2) DEFAULT NULL,
  `NEXT_RETRY_DATE` DATETIME DEFAULT NULL,
  `CPID` INT(11) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  `firstActiveDate` DATETIME DEFAULT NULL,
  `firstSubDate` DATETIME DEFAULT NULL,
  `previousState` VARCHAR(50) DEFAULT '0',
  `stations` VARCHAR(50) DEFAULT '0',
  `bucket` VARCHAR(50) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `msdnsvc` (`MSISDN`,`service_id`)
)  ENGINE=InnoDB AUTO_INCREMENT=415683 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;



/*
SQLyog Enterprise - MySQL GUI v6.0
Host - 5.5.34-log : Database - ussd_data
*********************************************************************
Server version : 5.5.34-log
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `ussd_data`;

USE `ussd_data`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `ussd_config` */

DROP TABLE IF EXISTS `ussd_config`;

CREATE TABLE `ussd_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ussd_code` varchar(20) DEFAULT NULL,
  `operator` varchar(10) DEFAULT NULL,
  `protocol` varchar(10) DEFAULT NULL,
  `pack` varchar(20) DEFAULT NULL,
  `service` varchar(20) DEFAULT NULL,
  `response` text,
  `response_url` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

/*Insert Data Into cci_properties table*/

Use global;

insert  into `billingengineproperty`(`id`,`property`,`value`,`relatedTo`) values (1,'CDR.Path','D:\\IVR\\Cdr_json','Generic'),(2,'addCountryCodeToReq','true','Generic'),(3,'countryCode','91','Generic'),(4,'operator','None','Generic'),(5,'country','India','Generic'),(6,'timeIntervalforNextReq','-10','Event based req'),(7,'maxIdleTimeout','1000','IN/SDP-Socket API'),(8,'soTimeOutVal','60000','IN/SDP-Socket API'),(9,'callbackUrl','','SDP'),(10,'spId','2680110000254','SDP'),(11,'spPassword','bmeB400','SDP'),(12,'keyStore.Path','C:\\Java\\jdk1.7.0_45\\bin\\cacert.keystore','TWSS'),(13,'trustStore.Path','C:\\Java\\jdk1.7.0_45\\bin\\cacert.truststore','TWSS'),(14,'useSocket','false','IN/SDP'),(15,'priorityType','primary','Generic'),(16,'subsMoCallback','http://localhost:9090/Subs_Engine/billingCallbackStatus?msisdn=$msisdn$&transactionID=$transactionid$&subServiceId=$serviceid$&action=$action$&channel=$channel$','SDP-MO'),(17,'debitReqContainsBalance','false','OCS'),(18,'hashAlgo','Saurab','Etisalat/Movicel'),(19,'subscribeUrl','http://localhost:9090/Subs_Engine/billingCallbackStatus?msisdn=$msisdn$&transactionID=$transactionid$&subServiceId=$serviceid$&action=$action$&channel=$channel$','AIS'),(20,'sendFailureCallback','false','Digicel'),(21,'sendCallbackOnNotification','false','Digicel'),(22,'sendRenewSms','false','Digicel'),(23,'protocol','HTTP_Post','HTTP_get');


insert  into `channels_info`(`id`,`channel`) values (1,'IVR'),(2,'SMS'),(3,'WAP'),(4,'USSD'),(5,'ivr'),(6,'sms'),(7,'wap'),(8,'ussd'),(9,'app'),(10,'cci');


/*Insert Data Into cci_properties table*/

insert  into `cci_properties`(`id`,`key`,`value`) values (1,'HostName','127.0.0.1'),(2,'ReportDB','callcdr_ken'),(3,'seMode','sync'),(4,'readTimeOut','60000'),(5,'subscription_url',concat('http://127.0.0.1:9070/Subs_Engine/subscription/sync?msisdn=|MSISDN|&subServiceId=|SUBSERVICEID|&serviceId=|SERVICEID|&cpId=100&reqStatus=active&channel=cci&operator=',@operatorname,'&country=',@countryname,'&circle=na')),(6,'unsubscription_url',concat('http://127.0.0.1:9070/Subs_Engine/unSubscription/sync?msisdn=|MSISDN|&subServiceId=|SUBSERVICEID|&serviceId=|SERVICEID|&cpId=100&reqStatus=deactivate&channel=cci&operator=',@operatorname,'&country=',@countryname,'&circle=na')),(7,'username','admin'),(8,'password','admin'),(9,'dbusername','root'),(10,'dbpassword','r00t'),(11,'config_status','1');

/*Insert Data Into coreengine_properties table*/

insert  into `coreengine_properties`(`ID`,`KEY`,`VALUE`) values (1,'filePath.recordFile','/data/recording/$service$/'),(2,'CDR.path','/data/cdr/ivr'),(3,'updateurl','http://127.0.0.1:9070/Subs_Engine/update?msisdn=$apartymsisdn$&subServiceId=$subsservicename$&serviceId=$servicenameforsubscription$&language=$language$&subTimeLeft=$remainingfreeminutes$&cpId=100'),(4,'checksuburl','http://127.0.0.1:9070/Subs_Engine/checkSubscription?msisdn=$apartymsisdn$&serviceId=$servicenameforsubscription$&cpId=100'),(5,'LoadService.url.scp','http://127.0.0.1:8080/IVRSecurity/pending?servicename=$servicename$&shortcode=$shortcode$&calltype=$calltype$'),(6,'CountryCodes',@countrycode),(7,'dialout.mscCode',@countrycode),(8,'bParty.prefix',''),(9,'bParty.suffix',''),(10,'aParty.prefix',''),(11,'aParty.suffix',''),(12,'scheduletimegap','0'),(13,'trydurationminutes','2880'),(14,'gapminutes','15'),(15,'transactionidlength','20'),(16,'setCallstatus','http://127.0.0.1:8080/CoreEngine/setCallstatus?chatid=$apartychatid$,$bpartychatid$#callstatus=0');

/*Insert Data Into coutrydetails table*/

insert  into `coutrydetails`(`id`,`country`,`countryCode`,`currencyCode`) values (1,'Afghanistan','AFG','AFN'),(2,'Albania','ALB','ALL'),(3,'Algeria','DZA','DZD'),(4,'American Samoa','ASM','USD'),(5,'Andorra','AND','EUR'),(6,'Angola','AGO','AOA'),(7,'Anguilla','AIA','XCD'),(9,'Antigua and Barbuda','ATG','XCD'),(10,'Argentina','ARG','ARS'),(11,'Armenia','ARM','AMD'),(12,'Aruba','ABW','AWG'),(13,'Australia','AUS','AUD'),(14,'Austria','AUT','EUR'),(15,'Azerbaijan','AZE','AZN'),(16,'Bahamas','BHS','BSD'),(17,'Bahrain','BHR','BHD'),(18,'Bangladesh','BGD','BDT'),(19,'Barbados','BRB','BBD'),(20,'Belarus','BLR','BYR'),(21,'Belgium','BEL','EUR'),(22,'Belize','BLZ','BZD'),(23,'Benin','BEN','XOF'),(24,'Bermuda','BMU','BMD'),(25,'Bhutan','BTN','INR'),(26,'Bolivia, Plurinational State of','BOL','BOB'),(27,'Bonaire, Sint Eustatius and Saba','BES','USD'),(28,'Bosnia and Herzegovina','BIH','BAM'),(29,'Botswana','BWA','BWP'),(30,'Bouvet Island','BVT','NOK'),(31,'Brazil','BRA','BRL'),(32,'British Indian Ocean Territory','IOT','USD'),(33,'Brunei Darussalam','BRN','BND'),(34,'Bulgaria','BGR','BGN'),(35,'Burkina Faso','BFA','XOF'),(36,'Burundi','BDI','BIF'),(37,'Cambodia','KHM','KHR'),(38,'Cameroon','CMR','XAF'),(39,'Canada','CAN','CAD'),(40,'Cape Verde','CPV','CVE'),(41,'Cayman Islands','CYM','KYD'),(42,'Central African Republic','CAF','XAF'),(43,'Chad','TCD','XAF'),(44,'Chile','CHL','CLP'),(45,'China','CHN','CNY'),(46,'Christmas Island','CXR','AUD'),(47,'Cocos (Keeling) Islands','CCK','AUD'),(48,'Colombia','COL','COP'),(49,'Comoros','COM','KMF'),(50,'Congo','COG','XAF'),(51,'Congo, the Democratic Republic of the','COD','CDF'),(52,'Cook Islands','COK','NZD'),(53,'Costa Rica','CRI','CRC'),(54,'Croatia','HRV','HRK'),(55,'Cuba','CUB','CUP'),(56,'CuraE7ao','CUW','ANG'),(57,'Cyprus','CYP','EUR'),(58,'Czech Republic','CZE','CZK'),(59,'Korea, Republic of','KOR','KRW'),(60,'Kuwait','KWT','KWD'),(61,'Kyrgyzstan','KGZ','KGS'),(62,'Latvia','LVA','LVL'),(63,'Lebanon','LBN','LBP'),(64,'Lesotho','LSO','ZAR'),(65,'Liberia','LBR','LRD'),(66,'Libya','LBY','LYD'),(67,'Liechtenstein','LIE','CHF'),(68,'Lithuania','LTU','LTL'),(69,'Luxembourg','LUX','EUR'),(70,'Macao','MAC','MOP'),(71,'Macedonia, the Former Yugoslav Republic of','MKD','MKD'),(72,'Madagascar','MDG','MGA'),(73,'Malawi','MWI','MWK'),(74,'Malaysia','MYS','MYR'),(75,'Maldives','MDV','MVR'),(76,'Mali','MLI','XOF'),(77,'Malta','MLT','EUR'),(78,'Marshall Islands','MHL','USD'),(79,'Martinique','MTQ','EUR'),(80,'Mauritania','MRT','MRO'),(81,'Mauritius','MUS','MUR'),(82,'Mayotte','MYT','EUR'),(83,'Mexico','MEX','MXN'),(84,'Micronesia, Federated States of','FSM','USD'),(85,'Moldova, Republic of','MDA','MDL'),(86,'Monaco','MCO','EUR'),(87,'Mongolia','MNG','MNT'),(88,'Montenegro','MNE','EUR'),(89,'Montserrat','MSR','XCD'),(90,'Morocco','MAR','MAD'),(91,'Mozambique','MOZ','MZN'),(92,'Myanmar','MMR','MMK'),(93,'Namibia','NAM','ZAR'),(94,'Nauru','NRU','AUD'),(95,'Nepal','NPL','NPR'),(96,'Netherlands','NLD','EUR'),(97,'New Caledonia','NCL','XPF'),(98,'New Zealand','NZL','NZD'),(99,'Nicaragua','NIC','NIO'),(100,'Niger','NER','XOF'),(101,'Nigeria','NGA','NGN'),(102,'Niue','NIU','NZD'),(103,'Norfolk Island','NFK','AUD'),(104,'Northern Mariana Islands','MNP','USD'),(105,'Norway','NOR','NOK'),(106,'Oman','OMN','OMR'),(107,'Pakistan','PAK','PKR'),(108,'Palau','PLW','USD'),(110,'Panama','PAN','USD'),(111,'Papua New Guinea','PNG','PGK'),(112,'Paraguay','PRY','PYG'),(113,'Peru','PER','PEN'),(114,'Philippines','PHL','PHP'),(115,'Pitcairn','PCN','NZD'),(116,'Poland','POL','PLN'),(117,'Portugal','PRT','EUR'),(118,'Puerto Rico','PRI','USD'),(119,'Qatar','QAT','QAR'),(120,'Romania','ROU','RON'),(121,'Russian Federation','RUS','RUB'),(122,'Rwanda','RWA','RWF'),(123,'RE9union','REU','EUR'),(124,'Saint BarthE9lemy','BLM','EUR'),(125,'Saint Helena, Ascension and Tristan da Cunha','SHN','SHP'),(126,'Saint Kitts and Nevis','KNA','XCD'),(127,'Saint Lucia','LCA','XCD'),(128,'Saint Martin (French part)','MAF','EUR'),(129,'Saint Pierre and Miquelon','SPM','EUR'),(130,'Saint Vincent and the Grenadines','VCT','XCD'),(131,'Samoa','WSM','WST'),(132,'San Marino','SMR','EUR'),(133,'Sao Tome and Principe','STP','STD'),(134,'Saudi Arabia','SAU','SAR'),(135,'Senegal','SEN','XOF'),(136,'Serbia','SRB','RSD'),(137,'Seychelles','SYC','SCR'),(138,'Sierra Leone','SLE','SLL'),(139,'Singapore','SGP','SGD'),(140,'Sint Maarten (Dutch part)','SXM','ANG'),(141,'Slovakia','SVK','EUR'),(142,'Slovenia','SVN','EUR'),(143,'Solomon Islands','SLB','SBD'),(144,'Somalia','SOM','SOS'),(145,'South Africa','ZAF','ZAR'),(147,'South Sudan','SSD','SSP'),(148,'Spain','ESP','EUR'),(149,'Sri Lanka','LKA','LKR'),(150,'Sudan','SDN','SDG'),(151,'Suriname','SUR','SRD'),(152,'Svalbard and Jan Mayen','SJM','NOK'),(153,'Swaziland','SWZ','SZL'),(154,'Sweden','SWE','SEK'),(155,'Switzerland','CHE','CHF'),(156,'Syrian Arab Republic','SYR','SYP'),(157,'Taiwan, Province of China','TWN','TWD'),(158,'Tajikistan','TJK','TJS'),(159,'Tanzania, United Republic of','TZA','TZS'),(160,'Thailand','THA','THB'),(161,'Timor-Leste','TLS','USD'),(162,'Togo','TGO','XOF'),(163,'Tokelau','TKL','NZD'),(164,'Tonga','TON','TOP'),(165,'Trinidad and Tobago','TTO','TTD'),(166,'Tunisia','TUN','TND'),(167,'Turkey','TUR','TRY'),(168,'Turkmenistan','TKM','TMT'),(169,'Turks and Caicos Islands','TCA','USD'),(170,'Tuvalu','TUV','AUD'),(171,'Uganda','UGA','UGX'),(172,'Ukraine','UKR','UAH'),(173,'United Arab Emirates','ARE','AED'),(174,'United Kingdom','GBR','GBP'),(175,'United States','USA','USD'),(176,'United States Minor Outlying Islands','UMI','USD'),(177,'Uruguay','URY','UYU'),(178,'Uzbekistan','UZB','UZS'),(179,'Vanuatu','VUT','VUV'),(180,'Venezuela, Bolivarian Republic of','VEN','VEF'),(181,'Viet Nam','VNM','VND'),(182,'Virgin Islands, British','VGB','USD'),(183,'Virgin Islands, U.S.','VIR','USD'),(184,'Wallis and Futuna','WLF','XPF'),(185,'Western Sahara','ESH','MAD'),(186,'Yemen','YEM','YER'),(187,'Zambia','ZMB','ZMW'),(188,'Zimbabwe','ZWE','ZWL'),(189,'C5land Islands','ALA','EUR');

/*Insert Data Into scheduler_properties table*/

insert  into `scheduler_properties`(`id`,`key`,`value`) values (1,'activemq.ip','failover://tcp://127.0.0.1:61616'),(2,'activemq.telephonyQueueName','telephony2'),(3,'scheduler.hardwareUrl','function=1&argument=3&service=1'),(4,'scheduler.basePath','http://127.0.0.1/cgi-bin/rpcclient?'),(5,'telephony.ip','127.0.0.1'),(6,'scheduler.tps','10'),(7,'SystemIP',@systemip),(8,'scheduler.tpsUrl','function=2&argument=0');

/*Insert Data Into scp_properties table*/

insert  into `scp_properties`(`id`,`key`,`value`) values (1,'filePath_uploadfile','/data/scp/ftppath'),(2,'filestore','/data/filestore/services/template/'),(3,'outputfolder','/data/scp/temp'),(4,'CountryCodes',@countrycode),(5,'cdrPath','/data/cdr/ivr'),(6,'obdMsisdnLength',@msisdnlength),(7,'servers',@systemip),(8,'Sub_url','http://localhost:8080/SubscriptionEngineUI/welcome.htm'),(9,'url_display','http://localhost:9070/Subs_Engine/getTotalSubscriber/'),(10,'Ivr_url','http://localhost:8080/IVRSecurity/welcome.htm');

/*Insert Data Into sms_properties table*/

insert  into `sms_properties`(`id`,`key`,`value`) values (1,'callerID','702'),(2,'operator',@operatorname),(3,'protocol','SMPP'),(4,'country',@countryname),(5,'countryCodes',@countrycode),(6,'msisdnLength',@msisdnlength),(7,'TPS','20'),(8,'cdrPath','/data/cdr/sms/'),(9,'transactionTimer','8000'),(10,'callbackUrl','http://127.0.0.1:8080/SMSClient/$msisdn$/'),(11,'failureRetries','3'),(12,'retryPeriod','10'),(13,'encoding','UTF-16BE'),(14,'smsMaxLength','66'),(15,'msisdnSeriesLength','4');

/*Insert Data Into subs_properties table*/

insert  into `subs_properties`(`id`,`key`,`value`) values (1,'countryCodes',@countryname),(2,'msisdnLength',@msisdnlength),(3,'countryCodesForSms',@countrycode),(4,'minBalance','5'),(5,'ivrShortCode','221'),(6,'readTimeout','140000'),(7,'timeout','10000'),(8,'cpId','100'),(9,'language','_F'),(10,'priceMultiply','1'),(11,'CDR.path','/data/cdr/subscription/'),(12,'country',@countryname),(13,'operator',@operatorname),(14,'currency',@currency),(15,'blackListCheck','0'),(16,'smsDateFormat','dd-MM-yyyy HH:mm'),(17,'ussdResponse','$msisdn$|$status$|$reason$'),(18,'appendTextFor_EMsg',''),(19,'appendTextFor_EMsg',''),(20,'calendarDay','false'),(21,'ip','127.0.0.1'),(22,'appendTextFor_OtherMsg',''),(23,'sleepTime','5'),(24,'additionalUrlCallOnUnsub',''),(25,'cpCallbackRenew','false'),(26,'cpCallback','false'),(27,'deductReqForPostpaid','false'),(28,'numberSeriesCheck','false'),(29,'countryCodeLength','3'),(30,'cdrXml','true'),(31,'countryCodeLength','3'),(32,'cdrXml','true'),(33,'msgToPrepaidOnly','true'),(34,'postpaidPrice','1200'),(35,'validity','30'),(36,'SdpStateChangerCDR','false'),(37,'forceUnsubStatus','pending'),(38,'checkClass','false'),(39,'chargingType','IN'),(40,'seriseLength','5'),(41,'demoCheck','false');

/*Insert Data Into users table*/

insert  into `users`(`id`,`username`,`password`,`status`) values (1,'admin','$2a$10$u8BSWsgGLMbb04OZ4SN/auC6N3UWXZNKKgFvyzOFxWYd5VMl3H.2e',1);


/*Data for the table `cpdetail` */

insert  into `cpdetail`(`id`,`cpName`,`callbackUrl`) values (100,'BNG','');

/*Data for the table `channelsallowed` */

insert  into `channelsallowed`(`id`,`CP_Id`,`Channel_Id`) values (1,100,1), (2,100,2), (3,100,3), (4,100,4), (5,100,5), (6,100,7),(7,100,8),(8,100,9),(9,100,10);


/*Data for the table `language` */

insert  into `language`(`id`,`language`,`lang_name`) values (1,'_E','English');


/*Data for the table `operator` */

insert  into `operator`(`id`,`operator_name`) values (1,@operatorname);


/*Data for the table `user_mapping_table` */

insert  into `user_mapping_table`(`id`,`country_id`,`operator_id`,`user_id`) values (1,1,1,1);


/*Data for the table `user_roles` */

insert  into `user_roles`(`id`,`user_id`,`authority`) values (1,1,'ADMIN');


/*Data for the table `country` */

insert  into `country`(`id`,`country_name`,`dbname`) values (1,@countryname,'');


/*Data for the table `country_info` */

insert  into `country_info`(`id`,`country`,`currency`,`countryCode`) values 
(1,@countryname,@currency,@countrycode);



insert  into billing_engine.`billingrespcodesndesc`(`id`,`respCode`,`respDesc`) values 
(19,'1001','Low balance'),
(20,'1002','Mandatory field missing'),
(21,'1003','Postpaid user'),
(22,'1004','Incorrect fields'),
(23,'1005','Product not exist'),
(24,'1006','Invalid Msisdn'),
(25,'1007','User not found'),
(26,'1008','User already subscribed'),
(27,'1009','Unknown Error'),
(28,'1010','Invalid Format'),
(29,'1011','Database Error'),
(30,'1012','Timeout'),
(31,'1013','I/O Error'),
(32,'1014','Token Expired'),
(33,'1015','Connection Timeout'),
(34,'1016','Not Implemented'),
(35,'1001','INSUFFICIENT_FUNDS'),
(36,'1018','SUBSCRIBER_NOT_FOUND'),
(37,'1019','FAILED'),
(38,'1020','TEMPORARY_BLOCKED'),
(39,'1021','BLACKLISTED'),
(40,'1022','DUPLICATE_TRANSACTION'),
(41,'1023','DAILY_CUSTOMER_TRANS_EXCEEDED'),
(42,'1024','SUBSCRIPTION_BLACKLISTED'),
(43,'1025','Subscription list information was not found'),
(44,'1026','Error: You are not allowed to send messages from this shortcode..');

insert  into billing_engine.`requesttype`(`id`,`type`) values 
(1,'Debit'),
(2,'GetBalance'),
(3,'Subscription'),
(4,'Unsubscription'),
(5,'SyncOrderRelation'),
(6,'Callback'),
(7,'GetUserProfile');


/* IOBD Scripts */

USE ivr_data;

-- MySQL dump 10.13  Distrib 5.7.18, for Linux (x86_64)
--
-- Host: localhost    Database: ivr_data
-- ------------------------------------------------------
-- Server version	5.7.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `iobd_users`
--

DROP TABLE IF EXISTS `iobd_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iobd_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `email` varchar(150) NOT NULL,
  `company_email` varchar(150) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `permission` varchar(45) DEFAULT NULL,
  `superuserid` varchar(150) DEFAULT NULL,
  `superuser_name` varchar(45) DEFAULT NULL,
  `company_name` varchar(150) DEFAULT NULL,
  `call_rate` int(11) DEFAULT NULL,
  `minimum_balance` int(11) DEFAULT NULL,
  `debit` int(11) DEFAULT NULL,
  `credit` int(11) DEFAULT NULL,
  `isactive` tinyint(1) DEFAULT NULL,
  `issubuser` tinyint(1) DEFAULT NULL,
  `superuser_operation` tinyint(1) DEFAULT NULL,
  `joined_date` datetime DEFAULT NULL,
  `msisdn_length` int(11) DEFAULT NULL,
  `country_code` varchar(10) DEFAULT NULL,
  `append_countrycode` tinyint(1) DEFAULT NULL,
  `retry` tinyint(1) DEFAULT NULL,
  `mscip` varchar(45) DEFAULT NULL,
  `voicechannel` varchar(45) DEFAULT NULL,
  `tps` int(100) DEFAULT NULL,
  `cli` varchar(45) DEFAULT NULL,
  `blacklist` tinyint(1) DEFAULT NULL,
  `operator` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `submenu_permission` varchar(45) DEFAULT NULL,
  `verify_mail` tinyint(1) DEFAULT NULL,
  `userdata` varchar(200) DEFAULT NULL,
  `current_pin` varchar(45) DEFAULT NULL,
  `enterprise` tinyint(1) DEFAULT NULL,
  `mobile` varchar(45) DEFAULT NULL,
  `uid` text,
  `currency` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `iobd_users`
--

LOCK TABLES `iobd_users` WRITE;
/*!40000 ALTER TABLE `iobd_users` DISABLE KEYS */;
INSERT INTO `iobd_users` VALUES (68,'mansi','mansi@gmail.com',NULL,'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=','1,2,3,4,20,6,7','acdc701f-2b1a-456e-8fb8-79f107adc9d2','mansi Rajora',NULL,2,9,0,0,0,0,1,'2020-04-14 02:14:33',8,'91',0,1,'41.138.58.185','ew',10,'1010',1,'india','usa','6,9',0,'56','1011',1,NULL,'a78ce202-b198-400e-9460-d1e50872b37e','CFA'),(78,'mansitest','newtestuser@gmail.com','mansi@blackngreen.com','FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=','1,2,3,20,4,6,7','68',NULL,'bng',1,2,0,10,1,0,0,'2020-04-30 18:53:31',8,'91',0,1,'41.138.58.185','89',12,'1010',1,'bng','India','6,9',0,'http://www.reachezy.com/portal/#!/demoid/9373b5ad-6a14-4ee0-a6a7-292ee7882c36',NULL,0,NULL,'9373b5ad-6a14-4ee0-a6a7-292ee7882c36','CFA'),(79,'MansiTest Rajora','mansitest@gmail.com','mansi@blackngreen.com','FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=','1,2,3,20,4,6,7','68',NULL,'bng',1,2,0,10,1,0,0,'2020-05-08 07:35:47',8,'91',0,1,'41.138.58.185','12',12,'1010',1,'bng','India','6,9',0,'http://www.reachezy.com/portal/#!/demoid/9497de55-3ba3-4e74-b6cc-dba89344adb2',NULL,0,NULL,'9497de55-3ba3-4e74-b6cc-dba89344adb2','CFA'),(81,'Mansi Rajora','mansirajora09@gmail.com','mansi@blackngreen.com','FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=','3,2,20,100','68',NULL,'bng',1,2,0,10,1,0,0,'2020-06-10 11:58:25',8,'12',0,1,'41.138.58.185','2',12,'1010',1,'bng','India','6,9',0,'http://www.reachezy.com/portal/#!/demoid/b14836eb-7f56-485e-9bbe-f2eb61f8fa2a',NULL,0,NULL,'b14836eb-7f56-485e-9bbe-f2eb61f8fa2a','CFA');
/*!40000 ALTER TABLE `iobd_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_id` varchar(11) NOT NULL,
  `menu_name` varchar(45) NOT NULL,
  `menu_displayname` varchar(45) NOT NULL,
  `display` tinyint(1) NOT NULL,
  `submenu_id` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'1','dashboard','Home',1,'1'),(2,'2','campaign','Campaigns',1,'2'),(3,'3','report','Report',1,'3'),(4,'4','DND','Manage DND',1,'4'),(6,'6','setting','Settings',1,'6'),(7,'7','help','Help',1,'7'),(20,'20','manageuser','Manage Users',1,'20');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submenu`
--

DROP TABLE IF EXISTS `submenu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `submenu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `submenu_id` int(11) DEFAULT NULL,
  `submenu_name` varchar(45) DEFAULT NULL,
  `submenu_displayname` varchar(145) DEFAULT NULL,
  `display` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_submenu` (`submenu_id`),
  CONSTRAINT `FK_submenu` FOREIGN KEY (`submenu_id`) REFERENCES `menu` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submenu`
--

LOCK TABLES `submenu` WRITE;
/*!40000 ALTER TABLE `submenu` DISABLE KEYS */;
INSERT INTO `submenu` VALUES (3,2,'camp_list','Campaign List',1),(5,20,'create_user','Create User',1),(6,20,'user_info','Users List',1),(7,7,'contact us','Contact Us',1),(8,7,'faq','FAQ',1),(9,7,'tc','T&C',1),(10,2,'create_camp','Create New Campaign',1),(11,2,'create_advance_camp','Create Advance Campiagn',1),(12,3,'detailreport','All Campaigns',1),(13,6,'update_profile','Update Profile',1);
/*!40000 ALTER TABLE `submenu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unique_users`
--

DROP TABLE IF EXISTS `unique_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unique_users` (
  `msisdn` varchar(20) NOT NULL,
  `shortcode` varchar(20) NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`msisdn`,`shortcode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unique_users`
--

LOCK TABLES `unique_users` WRITE;
/*!40000 ALTER TABLE `unique_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `unique_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `iobd_error_code`
--

DROP TABLE IF EXISTS `iobd_error_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iobd_error_code` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `errortype_id` int(4) NOT NULL,
  `errortype` varchar(20) NOT NULL,
  `errorcode` varchar(10) NOT NULL,
  `error_reason` varchar(70) DEFAULT NULL,
  `reason` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `errortype_id` (`errortype_id`),
  CONSTRAINT `iobd_error_code_ibfk_1` FOREIGN KEY (`errortype_id`) REFERENCES `iobd_error_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `iobd_error_code`
--

LOCK TABLES `iobd_error_code` WRITE;
/*!40000 ALTER TABLE `iobd_error_code` DISABLE KEYS */;
INSERT INTO `iobd_error_code` VALUES (1,1,'user_issue','17',NULL,NULL),(2,1,'user_issue','18',NULL,NULL),(3,1,'user_issue','19',NULL,NULL),(4,1,'user_issue','20',NULL,NULL),(5,1,'user_issue','21',NULL,NULL),(6,2,'network_issue','1',NULL,NULL),(7,2,'network_issue','2',NULL,NULL),(8,2,'network_issue','3',NULL,NULL),(9,2,'network_issue','4',NULL,NULL),(10,2,'network_issue','5',NULL,NULL),(11,2,'network_issue','6',NULL,NULL),(12,2,'network_issue','7',NULL,NULL),(13,2,'network_issue','8',NULL,NULL),(14,2,'network_issue','9',NULL,NULL),(15,2,'network_issue','27',NULL,NULL),(16,2,'network_issue','29',NULL,NULL),(17,2,'network_issue','31',NULL,NULL),(18,2,'network_issue','34',NULL,NULL),(19,2,'network_issue','38',NULL,NULL),(20,2,'network_issue','39',NULL,NULL),(21,2,'network_issue','40',NULL,NULL),(22,2,'network_issue','41',NULL,NULL),(23,2,'network_issue','42',NULL,NULL),(24,2,'network_issue','43',NULL,NULL),(25,2,'network_issue','44',NULL,NULL),(26,2,'network_issue','46',NULL,NULL),(27,2,'network_issue','47',NULL,NULL),(28,2,'network_issue','49',NULL,NULL),(29,2,'network_issue','57',NULL,NULL),(30,2,'network_issue','58',NULL,NULL),(31,2,'network_issue','62',NULL,NULL),(32,2,'network_issue','63',NULL,NULL),(33,2,'network_issue','65',NULL,NULL),(34,2,'network_issue','69',NULL,NULL),(35,2,'network_issue','70',NULL,NULL),(36,2,'network_issue','82',NULL,NULL),(37,2,'network_issue','86',NULL,NULL),(38,2,'network_issue','87',NULL,NULL),(39,2,'network_issue','88',NULL,NULL),(40,2,'network_issue','90',NULL,NULL),(41,2,'network_issue','91',NULL,NULL),(42,2,'network_issue','102',NULL,NULL),(43,2,'network_issue','111',NULL,NULL),(44,2,'network_issue','127',NULL,NULL),(45,2,'network_issue','132',NULL,NULL),(46,2,'network_issue','133',NULL,NULL),(47,2,'network_issue','134',NULL,NULL),(48,2,'network_issue','135',NULL,NULL),(49,2,'network_issue','136',NULL,NULL),(50,2,'network_issue','137',NULL,NULL),(51,1,'user_issue','16',NULL,NULL),(52,3,'application_issue','30',NULL,NULL),(53,3,'application_issue','53',NULL,NULL),(54,3,'application_issue','55',NULL,NULL),(55,3,'application_issue','81',NULL,NULL),(56,3,'application_issue','83',NULL,NULL),(57,3,'application_issue','84',NULL,NULL),(58,3,'application_issue','85',NULL,NULL),(59,3,'application_issue','95',NULL,NULL),(60,3,'application_issue','96',NULL,NULL),(61,3,'application_issue','97',NULL,NULL),(62,3,'application_issue','98',NULL,NULL),(63,3,'application_issue','99',NULL,NULL),(64,3,'application_issue','100',NULL,NULL),(65,3,'application_issue','101',NULL,NULL),(66,3,'application_issue','103',NULL,NULL),(67,3,'application_issue','110',NULL,NULL),(68,3,'application_issue','128',NULL,NULL),(69,3,'application_issue','129',NULL,NULL),(70,3,'application_issue','130',NULL,NULL),(71,3,'application_issue','131',NULL,NULL),(72,4,'other_issue','5',NULL,NULL),(73,4,'other_issue','22',NULL,NULL),(74,4,'other_issue','26',NULL,NULL),(75,4,'other_issue','28',NULL,NULL),(76,4,'other_issue','50',NULL,NULL),(77,4,'other_issue','79',NULL,NULL),(78,1,'user_issue','1000',NULL,NULL);
/*!40000 ALTER TABLE `iobd_error_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `iobd_error_type`
--

DROP TABLE IF EXISTS `iobd_error_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iobd_error_type` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `errortype` varchar(20) NOT NULL,
  `dObdCount` int(3) DEFAULT '0',
  `dTryAfter` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `errorType` (`errortype`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `iobd_error_type`
--

LOCK TABLES `iobd_error_type` WRITE;
/*!40000 ALTER TABLE `iobd_error_type` DISABLE KEYS */;
INSERT INTO `iobd_error_type` VALUES (1,'user_issue',1,10),(2,'network_issue',1,5),(3,'application_issue',1,2),(4,'other_issue',1,2);
/*!40000 ALTER TABLE `iobd_error_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `iobd_retry_rule`
--

DROP TABLE IF EXISTS `iobd_retry_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iobd_retry_rule` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `userid` int(4) NOT NULL,
  `status` varchar(100) DEFAULT NULL,
  `errortype` varchar(100) DEFAULT NULL,
  `obdCount` int(3) DEFAULT '0',
  `tryAfter` int(11) DEFAULT NULL,
  `maxObdCount` int(6) DEFAULT '0',
  `flow` int(2) DEFAULT '0',
  `doRetry` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  CONSTRAINT `iobd_retry_rule_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `iobd_users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `iobd_retry_rule`
--

LOCK TABLES `iobd_retry_rule` WRITE;
/*!40000 ALTER TABLE `iobd_retry_rule` DISABLE KEYS */;
INSERT INTO `iobd_retry_rule` VALUES (4,68,'failure','user_issue',1,1,7,0,1),(5,68,'failure','network_issue',1,1,7,0,1),(6,68,'failure','application_issue',1,1,7,0,1),(7,68,'failure','other_issue',1,1,7,0,1);
/*!40000 ALTER TABLE `iobd_retry_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `obd_retry_rule`
--

DROP TABLE IF EXISTS `obd_retry_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `obd_retry_rule` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `jobName` varchar(100) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `duration` varchar(100) DEFAULT NULL,
  `relCode` varchar(100) DEFAULT NULL,
  `obdCount` varchar(100) DEFAULT NULL,
  `tryAfter` int(11) DEFAULT NULL,
  `flow` int(2) DEFAULT NULL,
  `doRetry` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `obd_retry_rule`
--

LOCK TABLES `obd_retry_rule` WRITE;
/*!40000 ALTER TABLE `obd_retry_rule` DISABLE KEYS */;
INSERT INTO `obd_retry_rule` VALUES (1,'2019_04_02_17_01_47','failure','all','all','lt,3',5,0,1);
/*!40000 ALTER TABLE `obd_retry_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_blackout_hours`
--

DROP TABLE IF EXISTS `user_blackout_hours`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_blackout_hours` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(4) NOT NULL,
  `blackout_start` time NOT NULL,
  `blackout_end` time NOT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  CONSTRAINT `user_blackout_hours_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `iobd_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_blackout_hours`
--

LOCK TABLES `user_blackout_hours` WRITE;
/*!40000 ALTER TABLE `user_blackout_hours` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_blackout_hours` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `call_count`
--

DROP TABLE IF EXISTS `call_count`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `call_count` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `hour` int(2) DEFAULT NULL,
  `service` varchar(30) DEFAULT NULL,
  `jobname` varchar(50) DEFAULT NULL,
  `obd_attempt` int(7) DEFAULT '0',
  `obd_success` int(7) DEFAULT '0',
  `obd_fail` int(7) DEFAULT '0',
  `obd_success_remotehangupfirst` int(7) DEFAULT '0',
  `obd_fail_rlcsent` int(7) DEFAULT '0',
  `obd_success_other` int(7) DEFAULT '0',
  `obd_fail_userabsent` int(7) DEFAULT '0',
  `obd_fail_ringingtimeout` int(7) DEFAULT '0',
  `obd_fail_noanswer` int(7) DEFAULT '0',
  `obd_fail_misc` int(7) DEFAULT '0',
  `obd_fail_other` int(7) DEFAULT '0',
  `obd_00_05` int(7) DEFAULT '0',
  `obd_06_10` int(7) DEFAULT '0',
  `obd_11_15` int(7) DEFAULT '0',
  `obd_16_20` int(7) DEFAULT '0',
  `obd_20_plus` int(7) DEFAULT '0',
  `obd_subscription_url_hit` int(7) DEFAULT '0',
  `obd_dtmf_0` int(7) DEFAULT '0',
  `obd_dtmf_1` int(7) DEFAULT '0',
  `obd_dtmf_2` int(7) DEFAULT '0',
  `obd_dtmf_3` int(7) DEFAULT '0',
  `obd_dtmf_4` int(7) DEFAULT '0',
  `obd_dtmf_5` int(7) DEFAULT '0',
  `obd_dtmf_6` int(7) DEFAULT '0',
  `obd_dtmf_7` int(7) DEFAULT '0',
  `obd_dtmf_8` int(7) DEFAULT '0',
  `obd_dtmf_9` int(7) DEFAULT '0',
  `obd_dtmf_star` int(7) DEFAULT '0',
  `obd_dtmf_hash` int(7) DEFAULT '0',
  `obd_dtmf_noinput` int(7) DEFAULT '0',
  `obd_unsubscription_url_hit` int(7) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `rep` (`date`,`hour`,`service`,`jobname`)
) ENGINE=InnoDB AUTO_INCREMENT=45148 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;




/*!40000 ALTER TABLE `call_count` DISABLE KEYS */;

--
-- Table structure for table `subscription_count`
--

DROP TABLE IF EXISTS `subscription_count`;

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscription_count` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `hour` int(2) DEFAULT NULL,
  `service` varchar(100) DEFAULT NULL,
  `call_scheduled` int(11) DEFAULT '0',
  `call_success` int(11) DEFAULT '0',
  `call_fail` int(11) DEFAULT '0',
  `sub_success_daily` int(11) DEFAULT '0',
  `sub_fail_daily` int(11) DEFAULT '0',
  `sub_fail_weekly` int(11) DEFAULT '0',
  `sub_success_weekly` int(11) DEFAULT '0',
  `sub_fail_monthly` int(11) DEFAULT '0',
  `sub_success_monthly` int(11) DEFAULT '0',
  `sub_rev_daily` int(11) DEFAULT '0',
  `sub_rev_weekly` int(11) DEFAULT '0',
  `sub_rev_monthly` int(11) DEFAULT '0',
  `sub_rev_obd` int(11) DEFAULT '0',
  `jobname` varchar(150) DEFAULT NULL,
  `call_attempted` int(11) DEFAULT '0',
  `call_unattempted` int(11) DEFAULT '0',
  `userid` varchar(145) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `repsub` (`date`,`hour`,`jobname`)
) ENGINE=InnoDB AUTO_INCREMENT=39207 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription_count`
--



--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(145) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `request` text,
  `responce` text,
  `name` varchar(145) DEFAULT NULL,
  `logdata` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4301 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


 

ALTER  TABLE ivr_data.`mxgraph` ADD COLUMN `service` VARCHAR(145) DEFAULT 'MagicVoice';
ALTER  TABLE ivr_data.`mxgraph` ADD COLUMN  `service_display` TINYINT(1) DEFAULT '1';
ALTER  TABLE ivr_data.`mxgraph` ADD COLUMN  `normal_camp` TINYINT(1) DEFAULT '0'; 
ALTER  TABLE ivr_data.`service` ADD COLUMN  `repflag` TINYINT(1) NOT NULL DEFAULT '0'; 
ALTER  TABLE ivr_data.`service` ADD COLUMN  `enckey` VARCHAR(20) DEFAULT NULL; 
 ALTER  TABLE ivr_data.`service` ADD COLUMN   `flowdata` TEXT; 
 ALTER  TABLE ivr_data.`service` ADD COLUMN   `userid` INT(4) NOT NULL; 
 ALTER  TABLE ivr_data.`service` ADD COLUMN  `jobdisplayname` VARCHAR(100) DEFAULT NULL;
 ALTER  TABLE ivr_data.`service` ADD COLUMN  `camptype` VARCHAR(20) DEFAULT NULL;
 
 
 ALTER  TABLE cdrlog.`ivr_mastercalllogs` ADD COLUMN  `networkReleaseMsg` VARCHAR(255) DEFAULT NULL;  
 ALTER  TABLE cdrlog.`ivr_mastercalllogs` ADD COLUMN  `rawReleaseMsg` VARCHAR(255) DEFAULT NULL;    
 
 
ALTER  TABLE cdrlog.`ivr_mastercalllogs_temp` ADD COLUMN  `networkReleaseMsg` VARCHAR(255) DEFAULT NULL;  
ALTER  TABLE cdrlog.`ivr_mastercalllogs_temp` ADD COLUMN  `rawReleaseMsg` VARCHAR(255) DEFAULT NULL;





/*Modification Scripts */

insert into global.subs_properties(`key`,`value`) values ('BlockChannelNotification',null);
insert into global.subs_properties(`key`,`value`) values ('NumberOfHourluckPeriodForSub','0');

INSERT INTO global.subs_properties(`key`,`value`) VALUES ('ActivatioRetryFallback','false');
INSERT INTO global.subs_properties(`key`,`value`) VALUES ('RenewalRetryFallback','false');
INSERT INTO global.subs_properties(`key`,`value`) VALUES ('ActivationFallback','false');


ALTER TABLE global.`sub_service_msgs` ADD msgIntervel VARCHAR(10) DEFAULT '0';
ALTER TABLE global.`sub_service_msgs` ADD stopAllBlockMSG VARCHAR(500) DEFAULT NULL;
ALTER TABLE global.`sub_service_msgs` ADD dndBlockMSG VARCHAR(500) DEFAULT NULL;
