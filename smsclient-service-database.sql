/*
SQLyog Community v13.1.5  (64 bit)
MySQL - 5.6.49-log : Database - smsclient
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`smsclient` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `smsclient`;

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
  `expirytime` int(10) NOT NULL,
  PRIMARY KEY (`slNo`),
  KEY `alerts` (`serviceid`,`subserviceid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `activealerts` */

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `alertlogs` */

insert  into `alertlogs`(`pid`,`name`,`cli`,`circle`,`baseSize`,`status`,`timestamp`,`expiresAt`) values 
(1,'Test_008','180','PRO',1,'Success','2020-06-08 12:41:00','2020-06-08 13:00:00');

/*Table structure for table `alertscontent` */

DROP TABLE IF EXISTS `alertscontent`;

CREATE TABLE `alertscontent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `msgMonth` int(2) DEFAULT '0',
  `msgDay` smallint(2) NOT NULL,
  `serviceId` varchar(50) NOT NULL,
  `subServiceId` varchar(50) DEFAULT NULL,
  `sendingTime` time NOT NULL,
  `msgText` text NOT NULL,
  `language` varchar(10) DEFAULT NULL,
  `msgFlag` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Namaz` (`msgDay`,`serviceId`,`subServiceId`,`sendingTime`),
  KEY `Combo` (`msgDay`,`serviceId`,`subServiceId`,`msgFlag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `alertscontent` */

/*Table structure for table `blackout_hours` */

DROP TABLE IF EXISTS `blackout_hours`;

CREATE TABLE `blackout_hours` (
  `id` int(11) NOT NULL,
  `blackout_end` time(6) NOT NULL,
  `blackout_start` time(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `blackout_hours` */

/*Table structure for table `blackouthours` */

DROP TABLE IF EXISTS `blackouthours`;

CREATE TABLE `blackouthours` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blackout_start` time NOT NULL,
  `blackout_end` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `blackouthours` */

/*Table structure for table `callback_details` */

DROP TABLE IF EXISTS `callback_details`;

CREATE TABLE `callback_details` (
  `id` int(11) NOT NULL,
  `action` varchar(255) NOT NULL,
  `additionals` varchar(255) DEFAULT NULL,
  `serviceid` varchar(255) NOT NULL,
  `sub_serviceid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `callback_details` */

/*Table structure for table `callback_details_seq` */

DROP TABLE IF EXISTS `callback_details_seq`;

CREATE TABLE `callback_details_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `callback_details_seq` */

insert  into `callback_details_seq`(`next_val`) values 
(1);

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

/*Data for the table `callbackdetails` */

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

/*Data for the table `doubleconsent` */

/*Table structure for table `language_specification` */

DROP TABLE IF EXISTS `language_specification`;

CREATE TABLE `language_specification` (
  `lid` int(11) NOT NULL,
  `data_coding` int(11) NOT NULL,
  `encoding` varchar(255) NOT NULL,
  `language` varchar(255) NOT NULL,
  `script` int(11) NOT NULL,
  `service_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`lid`),
  UNIQUE KEY `UKluhvp149jyls3elysejmylpw` (`language`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `language_specification` */

/*Table structure for table `language_specification_seq` */

DROP TABLE IF EXISTS `language_specification_seq`;

CREATE TABLE `language_specification_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `language_specification_seq` */

insert  into `language_specification_seq`(`next_val`) values 
(1);

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

/*Data for the table `languagespecification` */

insert  into `languagespecification`(`lid`,`language`,`dataCoding`,`serviceType`,`encoding`,`script`) values 
(1,'_F',8,'CMT','true',2);

/*Table structure for table `matchcontent` */

DROP TABLE IF EXISTS `matchcontent`;

CREATE TABLE `matchcontent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `content` text CHARACTER SET utf8,
  `language` varchar(10) NOT NULL DEFAULT '_E',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `matchcontent` */

/*Table structure for table `message_formats` */

DROP TABLE IF EXISTS `message_formats`;

CREATE TABLE `message_formats` (
  `id` int(11) NOT NULL,
  `argument1` varchar(255) DEFAULT NULL,
  `argument2` varchar(255) DEFAULT NULL,
  `argument3` varchar(255) DEFAULT NULL,
  `country_code` varchar(255) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `service_code` varchar(255) DEFAULT NULL,
  `serviceid` varchar(255) DEFAULT NULL,
  `subkey` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `message_formats` */

/*Table structure for table `message_formats_seq` */

DROP TABLE IF EXISTS `message_formats_seq`;

CREATE TABLE `message_formats_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `message_formats_seq` */

insert  into `message_formats_seq`(`next_val`) values 
(1);

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

/*Data for the table `messageactions` */

insert  into `messageactions`(`aid`,`moId`,`type`,`details`) values 
(1,1,'VGET','http://127.0.0.1:9070/Subs_Engine/subscription/sync?msisdn=$msisdn$&subServiceId=MVDaily&channel=sms&language=_F&serviceId=MagicVoice&cpId=1&autoRenew=true'),
(2,2,'VGET','http://127.0.0.1:9070/Subs_Engine/subscription/sync?msisdn=$msisdn$&subServiceId=MVWeekly&channel=sms&language=_F&serviceId=MagicVoice&cpId=1&autoRenew=true'),
(3,3,'VGET','http://127.0.0.1:9070/Subs_Engine/subscription/sync?msisdn=$msisdn$&subServiceId=MVMonthly&channel=sms&language=_F&serviceId=MagicVoice&cpId=1&autoRenew=true'),
(4,4,'VGET','http://127.0.0.1:9070/Subs_Engine/unSubscription/sync?msisdn=$msisdn$&subServiceId=MVDaily&channel=sms&language=_F&serviceId=MagicVoice&cpId=1&autoRenew=true'),
(5,5,'VGET','http://127.0.0.1:9070/Subs_Engine/subscription/sync?msisdn=$msisdn$&subServiceId=CHDaily&channel=sms&language=_F&serviceId=Christianity&cpId=1&autoRenew=true'),
(6,6,'VGET','http://127.0.0.1:9070/Subs_Engine/subscription/sync?msisdn=$msisdn$&subServiceId=CHWeekly&channel=sms&language=_F&serviceId=Christianity&cpId=1&autoRenew=true'),
(7,7,'VGET','http://127.0.0.1:9070/Subs_Engine/subscription/sync?msisdn=$msisdn$&subServiceId=CHMonthly&channel=sms&language=_F&serviceId=Christianity&cpId=1&autoRenew=true'),
(8,8,'VGET','http://127.0.0.1:9070/Subs_Engine/unSubscription/sync?msisdn=$msisdn$&subServiceId=CHDaily&channel=sms&language=_F&serviceId=Christianity&cpId=1&autoRenew=true');

/*Table structure for table `messageformats` */

DROP TABLE IF EXISTS `messageformats`;

CREATE TABLE `messageformats` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `serviceCode` varchar(10) NOT NULL,
  `keyword` varchar(100) NOT NULL,
  `countryCode` varchar(50) DEFAULT '*',
  `subkey` varchar(100) DEFAULT NULL,
  `argument1` varchar(100) DEFAULT NULL,
  `argument2` varchar(100) DEFAULT NULL,
  `argument3` varchar(100) DEFAULT NULL,
  `serviceid` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MO` (`serviceCode`,`keyword`,`subkey`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

/*Data for the table `messageformats` */

insert  into `messageformats`(`id`,`serviceCode`,`keyword`,`countryCode`,`subkey`,`argument1`,`argument2`,`argument3`,`serviceid`) values 
(1,'1080','1','*','#','#','#','#',NULL),
(2,'1080','2','*','#','#','#','#',NULL),
(3,'1080','3','*','#','#','#','#',NULL),
(4,'1080','STOP','*','#','#','#','#',NULL),
(5,'1089','1','*','#','#','#','#',NULL),
(6,'1089','2','*','#','#','#','#',NULL),
(7,'1089','3','*','#','#','#','#',NULL),
(8,'1089','STOP','*','#','#','#','#',NULL);

/*Table structure for table `msg_contents` */

DROP TABLE IF EXISTS `msg_contents`;

CREATE TABLE `msg_contents` (
  `id` int(11) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `event` varchar(255) DEFAULT NULL,
  `service` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `msg_contents` */

/*Table structure for table `msg_contents_seq` */

DROP TABLE IF EXISTS `msg_contents_seq`;

CREATE TABLE `msg_contents_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `msg_contents_seq` */

insert  into `msg_contents_seq`(`next_val`) values 
(1);

/*Table structure for table `msisdn_series` */

DROP TABLE IF EXISTS `msisdn_series`;

CREATE TABLE `msisdn_series` (
  `id` varchar(255) NOT NULL,
  `series` varchar(255) DEFAULT NULL,
  `circle_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKogx86o4txg056r8180d5n3bno` (`circle_id`),
  CONSTRAINT `FKogx86o4txg056r8180d5n3bno` FOREIGN KEY (`circle_id`) REFERENCES `smscconfigs` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `msisdn_series` */

/*Table structure for table `msisdn_series_seq` */

DROP TABLE IF EXISTS `msisdn_series_seq`;

CREATE TABLE `msisdn_series_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `msisdn_series_seq` */

insert  into `msisdn_series_seq`(`next_val`) values 
(1);

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

/*Data for the table `msisdnseries` */

/*Table structure for table `promotionmsisdn` */

DROP TABLE IF EXISTS `promotionmsisdn`;

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

/*Data for the table `promotionmsisdn` */

/*Table structure for table `smsblacklist` */

DROP TABLE IF EXISTS `smsblacklist`;

CREATE TABLE `smsblacklist` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(255) DEFAULT NULL,
  `isSeries` int(1) NOT NULL,
  `is_series` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `smsblacklist` */

/*Table structure for table `smsblacklist_seq` */

DROP TABLE IF EXISTS `smsblacklist_seq`;

CREATE TABLE `smsblacklist_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `smsblacklist_seq` */

insert  into `smsblacklist_seq`(`next_val`) values 
(1);

/*Table structure for table `smscconfigs` */

DROP TABLE IF EXISTS `smscconfigs`;

CREATE TABLE `smscconfigs` (
  `cid` int(10) NOT NULL AUTO_INCREMENT,
  `opId` int(10) NOT NULL,
  `circle` varchar(255) DEFAULT NULL,
  `serverIp` varchar(32) NOT NULL,
  `serverPort` int(7) NOT NULL,
  `serviceUri` text COMMENT 'Used for SOAP, Default value=#',
  `userid` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `timeout` int(10) DEFAULT '10000' COMMENT 'For SMPP=10000; HTTP/SOAP=40000',
  `systemType` varchar(50) DEFAULT NULL,
  `responseType` varchar(100) DEFAULT NULL,
  `maxConnections` varchar(100) DEFAULT '3',
  `contentType` varchar(100) DEFAULT NULL,
  `bindMode` int(10) NOT NULL,
  `bind_mode` int(11) DEFAULT NULL,
  `content_type` varchar(255) DEFAULT NULL,
  `max_connections` int(11) DEFAULT NULL,
  `op_id` varchar(255) DEFAULT NULL,
  `response_type` varchar(255) DEFAULT NULL,
  `server_ip` varchar(255) DEFAULT NULL,
  `server_port` int(11) DEFAULT NULL,
  `service_uri` varchar(255) DEFAULT NULL,
  `system_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cid`),
  UNIQUE KEY `circle` (`opId`,`circle`,`serverIp`,`serverPort`),
  CONSTRAINT `FK_SMSCConfig_opid` FOREIGN KEY (`opId`) REFERENCES `smscdetails` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `smscconfigs` */

insert  into `smscconfigs`(`cid`,`opId`,`circle`,`serverIp`,`serverPort`,`serviceUri`,`userid`,`password`,`timeout`,`systemType`,`responseType`,`maxConnections`,`contentType`,`bindMode`,`bind_mode`,`content_type`,`max_connections`,`op_id`,`response_type`,`server_ip`,`server_port`,`service_uri`,`system_type`) values 
(1,1,'PRO','154.73.20.105',2775,'#','blackgreen','Black!@#',10000,'','','3','',2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `smscconfigs_seq` */

DROP TABLE IF EXISTS `smscconfigs_seq`;

CREATE TABLE `smscconfigs_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `smscconfigs_seq` */

insert  into `smscconfigs_seq`(`next_val`) values 
(1);

/*Table structure for table `smscdetails` */

DROP TABLE IF EXISTS `smscdetails`;

CREATE TABLE `smscdetails` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `operator` varchar(20) NOT NULL,
  `country` varchar(25) NOT NULL,
  `protocol` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `smscdetails` */

insert  into `smscdetails`(`id`,`operator`,`country`,`protocol`) values 
(1,'Africell','DRC','SMPP');

/*Table structure for table `smscformats` */

DROP TABLE IF EXISTS `smscformats`;

CREATE TABLE `smscformats` (
  `rid` varchar(255) NOT NULL,
  `cid` int(10) NOT NULL,
  `requestFormat` text,
  `responseFormat` text,
  `mode` varchar(255) DEFAULT NULL,
  `register` varchar(255) DEFAULT NULL,
  `MORegisterFormat` text,
  `MTRegisterFormat` text,
  `options` varchar(255) DEFAULT NULL,
  `moregister_format` varchar(255) DEFAULT NULL,
  `mtregister_format` varchar(255) DEFAULT NULL,
  `request_format` varchar(255) DEFAULT NULL,
  `response_format` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`rid`),
  KEY `FK_SMSCFormats_cid` (`cid`),
  CONSTRAINT `FK_SMSCFormats_cid` FOREIGN KEY (`cid`) REFERENCES `smscconfigs` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `smscformats` */

insert  into `smscformats`(`rid`,`cid`,`requestFormat`,`responseFormat`,`mode`,`register`,`MORegisterFormat`,`MTRegisterFormat`,`options`,`moregister_format`,`mtregister_format`,`request_format`,`response_format`) values 
('1',1,'','','GET','0',NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `smscformats_seq` */

DROP TABLE IF EXISTS `smscformats_seq`;

CREATE TABLE `smscformats_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `smscformats_seq` */

insert  into `smscformats_seq`(`next_val`) values 
(1);

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

/*Data for the table `smslogs` */

/*Table structure for table `smspromotion` */

DROP TABLE IF EXISTS `smspromotion`;

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

/*Data for the table `smspromotion` */

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
  `param1` varchar(50) DEFAULT NULL,
  `param2` varchar(50) DEFAULT NULL,
  `enddate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Subscriptions` (`msisdn`,`serviceid`,`subserviceid`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

/*Data for the table `smssubscription` */

/*Table structure for table `smswhitelist` */

DROP TABLE IF EXISTS `smswhitelist`;

CREATE TABLE `smswhitelist` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `shortcode` varchar(255) DEFAULT NULL,
  `msisdn` varchar(255) DEFAULT NULL,
  `isSeries` int(1) NOT NULL DEFAULT '0',
  `msgType` varchar(2) NOT NULL DEFAULT 'MT',
  `is_series` tinyint(4) DEFAULT NULL,
  `msg_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `smswhitelist` */

/*Table structure for table `smswhitelist_seq` */

DROP TABLE IF EXISTS `smswhitelist_seq`;

CREATE TABLE `smswhitelist_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `smswhitelist_seq` */

insert  into `smswhitelist_seq`(`next_val`) values 
(1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
