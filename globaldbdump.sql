/*
SQLyog Community v13.3.0 (64 bit)
MySQL - 8.0.43 : Database - global
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`global` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `global`;

/*Table structure for table `billingengineproperty` */

DROP TABLE IF EXISTS `billingengineproperty`;

CREATE TABLE `billingengineproperty` (
  `id` int NOT NULL AUTO_INCREMENT,
  `property` varchar(50) NOT NULL,
  `value` text NOT NULL,
  `relatedTo` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb3;

/*Data for the table `billingengineproperty` */

insert  into `billingengineproperty`(`id`,`property`,`value`,`relatedTo`) values 
(1,'CDR.Path','D:\\IVR\\Cdr_json','Generic'),
(2,'addCountryCodeToReq','true','Generic'),
(3,'countryCode','91','Generic'),
(4,'operator','None','Generic'),
(5,'country','India','Generic'),
(6,'timeIntervalforNextReq','-10','Event based req'),
(7,'maxIdleTimeout','1000','IN/SDP-Socket API'),
(8,'soTimeOutVal','60000','IN/SDP-Socket API'),
(9,'callbackUrl','','SDP'),
(10,'spId','2680110000254','SDP'),
(11,'spPassword','bmeB400','SDP'),
(12,'keyStore.Path','C:\\Java\\jdk1.7.0_45\\bin\\cacert.keystore','TWSS'),
(13,'trustStore.Path','C:\\Java\\jdk1.7.0_45\\bin\\cacert.truststore','TWSS'),
(14,'useSocket','false','IN/SDP'),
(15,'priorityType','primary','Generic'),
(16,'subsMoCallback','http://localhost:9090/Subs_Engine/billingCallbackStatus?msisdn=$msisdn$&transactionID=$transactionid$&subServiceId=$serviceid$&action=$action$&channel=$channel$','SDP-MO'),
(17,'debitReqContainsBalance','false','OCS'),
(18,'hashAlgo','Saurab','Etisalat/Movicel'),
(19,'subscribeUrl','http://localhost:9090/Subs_Engine/billingCallbackStatus?msisdn=$msisdn$&transactionID=$transactionid$&subServiceId=$serviceid$&action=$action$&channel=$channel$','AIS'),
(20,'sendFailureCallback','false','Digicel'),
(21,'sendCallbackOnNotification','false','Digicel'),
(22,'sendRenewSms','false','Digicel'),
(23,'protocol','HTTP_Post','HTTP_get');

/*Table structure for table `billingexecuter_properties` */

DROP TABLE IF EXISTS `billingexecuter_properties`;

CREATE TABLE `billingexecuter_properties` (
  `id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(100) DEFAULT NULL,
  `value` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3;

/*Data for the table `billingexecuter_properties` */

/*Table structure for table `blacklisted` */

DROP TABLE IF EXISTS `blacklisted`;

CREATE TABLE `blacklisted` (
  `id` int NOT NULL AUTO_INCREMENT,
  `msisdn` int DEFAULT NULL,
  `isSeries` tinyint(1) DEFAULT NULL,
  `serviceId` int DEFAULT NULL,
  `blacklistedMsg` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `serviceId` (`serviceId`),
  KEY `serviceinfofk_idx` (`serviceId`),
  CONSTRAINT `serviceinfofk` FOREIGN KEY (`serviceId`) REFERENCES `service_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `blacklisted` */

/*Table structure for table `blackouthours` */

DROP TABLE IF EXISTS `blackouthours`;

CREATE TABLE `blackouthours` (
  `id` int NOT NULL AUTO_INCREMENT,
  `subServiceId` int NOT NULL,
  `retryStartHour` time DEFAULT NULL,
  `retryEndHour` time DEFAULT NULL,
  `renewalStartHour` time DEFAULT NULL,
  `renewalEndHour` time DEFAULT NULL,
  `renewalMsgStartHour` time DEFAULT NULL,
  `renewalMsgEndHour` time DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `subServiceId` (`subServiceId`),
  KEY `subserviceinfofk_idx` (`subServiceId`),
  CONSTRAINT `subserviceinfofk` FOREIGN KEY (`subServiceId`) REFERENCES `sub_service_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;

/*Data for the table `blackouthours` */

/*Table structure for table `cci_properties` */

DROP TABLE IF EXISTS `cci_properties`;

CREATE TABLE `cci_properties` (
  `id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(100) DEFAULT NULL,
  `value` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;

/*Data for the table `cci_properties` */

insert  into `cci_properties`(`id`,`key`,`value`) values 
(1,'HostName','127.0.0.1'),
(2,'ReportDB','callcdr_ken'),
(3,'seMode','sync'),
(4,'readTimeOut','60000'),
(5,'subscription_url','http://127.0.0.1:9070/Subs_Engine/subscription/sync?msisdn=|MSISDN|&subServiceId=|SUBSERVICEID|&serviceId=|SERVICEID|&cpId=100&reqStatus=active&channel=cci&operator=airtel&country=india&circle=na'),
(6,'unsubscription_url','http://127.0.0.1:9070/Subs_Engine/unSubscription/sync?msisdn=|MSISDN|&subServiceId=|SUBSERVICEID|&serviceId=|SERVICEID|&cpId=100&reqStatus=deactivate&channel=cci&operator=airtel&country=india&circle=na'),
(7,'username','admin'),
(8,'password','admin'),
(9,'dbusername','root'),
(10,'dbpassword','r00t'),
(11,'config_status','1');

/*Table structure for table `cdrexecuter_properties` */

DROP TABLE IF EXISTS `cdrexecuter_properties`;

CREATE TABLE `cdrexecuter_properties` (
  `id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(100) DEFAULT NULL,
  `value` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;

/*Data for the table `cdrexecuter_properties` */

/*Table structure for table `cdruploader_properties` */

DROP TABLE IF EXISTS `cdruploader_properties`;

CREATE TABLE `cdruploader_properties` (
  `id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(100) DEFAULT NULL,
  `value` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb3;

/*Data for the table `cdruploader_properties` */

/*Table structure for table `channels_info` */

DROP TABLE IF EXISTS `channels_info`;

CREATE TABLE `channels_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `channel` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `channels_info_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;

/*Data for the table `channels_info` */

insert  into `channels_info`(`id`,`channel`) values 
(1,'IVR'),
(2,'SMS'),
(3,'WAP'),
(4,'USSD'),
(5,'ivr'),
(6,'sms'),
(7,'wap'),
(8,'ussd'),
(9,'app'),
(10,'cci');

/*Table structure for table `channelsallowed` */

DROP TABLE IF EXISTS `channelsallowed`;

CREATE TABLE `channelsallowed` (
  `id` int NOT NULL AUTO_INCREMENT,
  `CP_Id` int NOT NULL,
  `Channel_Id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_channelsallowed` (`CP_Id`),
  KEY `FK_channelsallowed_channel` (`Channel_Id`),
  CONSTRAINT `FK_channelsallowed` FOREIGN KEY (`CP_Id`) REFERENCES `cpdetail` (`id`),
  CONSTRAINT `FK_channelsallowed_channel` FOREIGN KEY (`Channel_Id`) REFERENCES `channels_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;

/*Data for the table `channelsallowed` */

insert  into `channelsallowed`(`id`,`CP_Id`,`Channel_Id`) values 
(1,100,1),
(2,100,2),
(3,100,3),
(4,100,4),
(5,100,5),
(6,100,7),
(7,100,8),
(8,100,9),
(9,100,10);

/*Table structure for table `charging_details` */

DROP TABLE IF EXISTS `charging_details`;

CREATE TABLE `charging_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `step_detail` varchar(15) NOT NULL,
  `charging_url_sync` text NOT NULL,
  `getBalance_url_sync` text NOT NULL,
  `charging_first` int NOT NULL,
  `sub_service_id` int NOT NULL,
  `charging_url_async` text NOT NULL,
  `delete_url_async` text NOT NULL,
  `billingMode` varchar(15) NOT NULL,
  `topUpUrl` varchar(500) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_charging_details` (`sub_service_id`),
  CONSTRAINT `FK_charging_details` FOREIGN KEY (`sub_service_id`) REFERENCES `sub_service_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

/*Data for the table `charging_details` */

/*Table structure for table `circle` */

DROP TABLE IF EXISTS `circle`;

CREATE TABLE `circle` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `circle_code` varchar(255) DEFAULT NULL,
  `circle_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `circle` */

/*Table structure for table `circle_mapping` */

DROP TABLE IF EXISTS `circle_mapping`;

CREATE TABLE `circle_mapping` (
  `id` int NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(30) DEFAULT NULL,
  `isserise` varchar(1) DEFAULT NULL,
  `circle` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

/*Data for the table `circle_mapping` */

/*Table structure for table `coreengine_properties` */

DROP TABLE IF EXISTS `coreengine_properties`;

CREATE TABLE `coreengine_properties` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `KEY` varchar(25) NOT NULL,
  `VALUE` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb3;

/*Data for the table `coreengine_properties` */

insert  into `coreengine_properties`(`ID`,`KEY`,`VALUE`) values 
(1,'filePath.recordFile','/data/recording/$service$/'),
(2,'CDR.path','/data/cdr/ivr'),
(3,'updateurl','http://127.0.0.1:9070/Subs_Engine/update?msisdn=$apartymsisdn$&subServiceId=$subsservicename$&serviceId=$servicenameforsubscription$&language=$language$&subTimeLeft=$remainingfreeminutes$&cpId=100'),
(4,'checksuburl','http://127.0.0.1:9070/Subs_Engine/checkSubscription?msisdn=$apartymsisdn$&serviceId=$servicenameforsubscription$&cpId=100'),
(5,'LoadService.url.scp','http://127.0.0.1:8080/IVRSecurity/pending?servicename=$servicename$&shortcode=$shortcode$&calltype=$calltype$'),
(6,'CountryCodes','011'),
(7,'dialout.mscCode','011'),
(8,'bParty.prefix',''),
(9,'bParty.suffix',''),
(10,'aParty.prefix',''),
(11,'aParty.suffix',''),
(12,'scheduletimegap','0'),
(13,'trydurationminutes','2880'),
(14,'gapminutes','15'),
(15,'transactionidlength','20'),
(16,'setCallstatus','http://127.0.0.1:8080/CoreEngine/setCallstatus?chatid=$apartychatid$,$bpartychatid$#callstatus=0');

/*Table structure for table `country` */

DROP TABLE IF EXISTS `country`;

CREATE TABLE `country` (
  `id` int NOT NULL AUTO_INCREMENT,
  `country_name` varchar(255) DEFAULT NULL,
  `dbname` varchar(255) DEFAULT NULL,
  `contry_id` varchar(255) DEFAULT NULL,
  `contry_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

/*Data for the table `country` */

insert  into `country`(`id`,`country_name`,`dbname`,`contry_id`,`contry_name`) values 
(1,'india','',NULL,NULL);

/*Table structure for table `country_info` */

DROP TABLE IF EXISTS `country_info`;

CREATE TABLE `country_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `country` varchar(9) DEFAULT NULL,
  `currency` varchar(9) DEFAULT NULL,
  `countryCode` varchar(9) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

/*Data for the table `country_info` */

insert  into `country_info`(`id`,`country`,`currency`,`countryCode`) values 
(1,'india','INR','011');

/*Table structure for table `coutrydetails` */

DROP TABLE IF EXISTS `coutrydetails`;

CREATE TABLE `coutrydetails` (
  `id` int NOT NULL AUTO_INCREMENT,
  `country` varchar(50) NOT NULL,
  `countryCode` varchar(3) NOT NULL,
  `currencyCode` varchar(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=191 DEFAULT CHARSET=utf8mb3;

/*Data for the table `coutrydetails` */

insert  into `coutrydetails`(`id`,`country`,`countryCode`,`currencyCode`) values 
(1,'Afghanistan','AFG','AFN'),
(2,'Albania','ALB','ALL'),
(3,'Algeria','DZA','DZD'),
(4,'American Samoa','ASM','USD'),
(5,'Andorra','AND','EUR'),
(6,'Angola','AGO','AOA'),
(7,'Anguilla','AIA','XCD'),
(9,'Antigua and Barbuda','ATG','XCD'),
(10,'Argentina','ARG','ARS'),
(11,'Armenia','ARM','AMD'),
(12,'Aruba','ABW','AWG'),
(13,'Australia','AUS','AUD'),
(14,'Austria','AUT','EUR'),
(15,'Azerbaijan','AZE','AZN'),
(16,'Bahamas','BHS','BSD'),
(17,'Bahrain','BHR','BHD'),
(18,'Bangladesh','BGD','BDT'),
(19,'Barbados','BRB','BBD'),
(20,'Belarus','BLR','BYR'),
(21,'Belgium','BEL','EUR'),
(22,'Belize','BLZ','BZD'),
(23,'Benin','BEN','XOF'),
(24,'Bermuda','BMU','BMD'),
(25,'Bhutan','BTN','INR'),
(26,'Bolivia, Plurinational State of','BOL','BOB'),
(27,'Bonaire, Sint Eustatius and Saba','BES','USD'),
(28,'Bosnia and Herzegovina','BIH','BAM'),
(29,'Botswana','BWA','BWP'),
(30,'Bouvet Island','BVT','NOK'),
(31,'Brazil','BRA','BRL'),
(32,'British Indian Ocean Territory','IOT','USD'),
(33,'Brunei Darussalam','BRN','BND'),
(34,'Bulgaria','BGR','BGN'),
(35,'Burkina Faso','BFA','XOF'),
(36,'Burundi','BDI','BIF'),
(37,'Cambodia','KHM','KHR'),
(38,'Cameroon','CMR','XAF'),
(39,'Canada','CAN','CAD'),
(40,'Cape Verde','CPV','CVE'),
(41,'Cayman Islands','CYM','KYD'),
(42,'Central African Republic','CAF','XAF'),
(43,'Chad','TCD','XAF'),
(44,'Chile','CHL','CLP'),
(45,'China','CHN','CNY'),
(46,'Christmas Island','CXR','AUD'),
(47,'Cocos (Keeling) Islands','CCK','AUD'),
(48,'Colombia','COL','COP'),
(49,'Comoros','COM','KMF'),
(50,'Congo','COG','XAF'),
(51,'Congo, the Democratic Republic of the','COD','CDF'),
(52,'Cook Islands','COK','NZD'),
(53,'Costa Rica','CRI','CRC'),
(54,'Croatia','HRV','HRK'),
(55,'Cuba','CUB','CUP'),
(56,'CuraE7ao','CUW','ANG'),
(57,'Cyprus','CYP','EUR'),
(58,'Czech Republic','CZE','CZK'),
(59,'Korea, Republic of','KOR','KRW'),
(60,'Kuwait','KWT','KWD'),
(61,'Kyrgyzstan','KGZ','KGS'),
(62,'Latvia','LVA','LVL'),
(63,'Lebanon','LBN','LBP'),
(64,'Lesotho','LSO','ZAR'),
(65,'Liberia','LBR','LRD'),
(66,'Libya','LBY','LYD'),
(67,'Liechtenstein','LIE','CHF'),
(68,'Lithuania','LTU','LTL'),
(69,'Luxembourg','LUX','EUR'),
(70,'Macao','MAC','MOP'),
(71,'Macedonia, the Former Yugoslav Republic of','MKD','MKD'),
(72,'Madagascar','MDG','MGA'),
(73,'Malawi','MWI','MWK'),
(74,'Malaysia','MYS','MYR'),
(75,'Maldives','MDV','MVR'),
(76,'Mali','MLI','XOF'),
(77,'Malta','MLT','EUR'),
(78,'Marshall Islands','MHL','USD'),
(79,'Martinique','MTQ','EUR'),
(80,'Mauritania','MRT','MRO'),
(81,'Mauritius','MUS','MUR'),
(82,'Mayotte','MYT','EUR'),
(83,'Mexico','MEX','MXN'),
(84,'Micronesia, Federated States of','FSM','USD'),
(85,'Moldova, Republic of','MDA','MDL'),
(86,'Monaco','MCO','EUR'),
(87,'Mongolia','MNG','MNT'),
(88,'Montenegro','MNE','EUR'),
(89,'Montserrat','MSR','XCD'),
(90,'Morocco','MAR','MAD'),
(91,'Mozambique','MOZ','MZN'),
(92,'Myanmar','MMR','MMK'),
(93,'Namibia','NAM','ZAR'),
(94,'Nauru','NRU','AUD'),
(95,'Nepal','NPL','NPR'),
(96,'Netherlands','NLD','EUR'),
(97,'New Caledonia','NCL','XPF'),
(98,'New Zealand','NZL','NZD'),
(99,'Nicaragua','NIC','NIO'),
(100,'Niger','NER','XOF'),
(101,'Nigeria','NGA','NGN'),
(102,'Niue','NIU','NZD'),
(103,'Norfolk Island','NFK','AUD'),
(104,'Northern Mariana Islands','MNP','USD'),
(105,'Norway','NOR','NOK'),
(106,'Oman','OMN','OMR'),
(107,'Pakistan','PAK','PKR'),
(108,'Palau','PLW','USD'),
(110,'Panama','PAN','USD'),
(111,'Papua New Guinea','PNG','PGK'),
(112,'Paraguay','PRY','PYG'),
(113,'Peru','PER','PEN'),
(114,'Philippines','PHL','PHP'),
(115,'Pitcairn','PCN','NZD'),
(116,'Poland','POL','PLN'),
(117,'Portugal','PRT','EUR'),
(118,'Puerto Rico','PRI','USD'),
(119,'Qatar','QAT','QAR'),
(120,'Romania','ROU','RON'),
(121,'Russian Federation','RUS','RUB'),
(122,'Rwanda','RWA','RWF'),
(123,'RE9union','REU','EUR'),
(124,'Saint BarthE9lemy','BLM','EUR'),
(125,'Saint Helena, Ascension and Tristan da Cunha','SHN','SHP'),
(126,'Saint Kitts and Nevis','KNA','XCD'),
(127,'Saint Lucia','LCA','XCD'),
(128,'Saint Martin (French part)','MAF','EUR'),
(129,'Saint Pierre and Miquelon','SPM','EUR'),
(130,'Saint Vincent and the Grenadines','VCT','XCD'),
(131,'Samoa','WSM','WST'),
(132,'San Marino','SMR','EUR'),
(133,'Sao Tome and Principe','STP','STD'),
(134,'Saudi Arabia','SAU','SAR'),
(135,'Senegal','SEN','XOF'),
(136,'Serbia','SRB','RSD'),
(137,'Seychelles','SYC','SCR'),
(138,'Sierra Leone','SLE','SLL'),
(139,'Singapore','SGP','SGD'),
(140,'Sint Maarten (Dutch part)','SXM','ANG'),
(141,'Slovakia','SVK','EUR'),
(142,'Slovenia','SVN','EUR'),
(143,'Solomon Islands','SLB','SBD'),
(144,'Somalia','SOM','SOS'),
(145,'South Africa','ZAF','ZAR'),
(147,'South Sudan','SSD','SSP'),
(148,'Spain','ESP','EUR'),
(149,'Sri Lanka','LKA','LKR'),
(150,'Sudan','SDN','SDG'),
(151,'Suriname','SUR','SRD'),
(152,'Svalbard and Jan Mayen','SJM','NOK'),
(153,'Swaziland','SWZ','SZL'),
(154,'Sweden','SWE','SEK'),
(155,'Switzerland','CHE','CHF'),
(156,'Syrian Arab Republic','SYR','SYP'),
(157,'Taiwan, Province of China','TWN','TWD'),
(158,'Tajikistan','TJK','TJS'),
(159,'Tanzania, United Republic of','TZA','TZS'),
(160,'Thailand','THA','THB'),
(161,'Timor-Leste','TLS','USD'),
(162,'Togo','TGO','XOF'),
(163,'Tokelau','TKL','NZD'),
(164,'Tonga','TON','TOP'),
(165,'Trinidad and Tobago','TTO','TTD'),
(166,'Tunisia','TUN','TND'),
(167,'Turkey','TUR','TRY'),
(168,'Turkmenistan','TKM','TMT'),
(169,'Turks and Caicos Islands','TCA','USD'),
(170,'Tuvalu','TUV','AUD'),
(171,'Uganda','UGA','UGX'),
(172,'Ukraine','UKR','UAH'),
(173,'United Arab Emirates','ARE','AED'),
(174,'United Kingdom','GBR','GBP'),
(175,'United States','USA','USD'),
(176,'United States Minor Outlying Islands','UMI','USD'),
(177,'Uruguay','URY','UYU'),
(178,'Uzbekistan','UZB','UZS'),
(179,'Vanuatu','VUT','VUV'),
(180,'Venezuela, Bolivarian Republic of','VEN','VEF'),
(181,'Viet Nam','VNM','VND'),
(182,'Virgin Islands, British','VGB','USD'),
(183,'Virgin Islands, U.S.','VIR','USD'),
(184,'Wallis and Futuna','WLF','XPF'),
(185,'Western Sahara','ESH','MAD'),
(186,'Yemen','YEM','YER'),
(187,'Zambia','ZMB','ZMW'),
(188,'Zimbabwe','ZWE','ZWL'),
(189,'C5land Islands','ALA','EUR');

/*Table structure for table `cpdetail` */

DROP TABLE IF EXISTS `cpdetail`;

CREATE TABLE `cpdetail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cpName` varchar(50) NOT NULL,
  `callbackUrl` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpName` (`cpName`),
  KEY `cpdetail_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb3;

/*Data for the table `cpdetail` */

insert  into `cpdetail`(`id`,`cpName`,`callbackUrl`) values 
(100,'BNG','');

/*Table structure for table `filezippercdr_properties` */

DROP TABLE IF EXISTS `filezippercdr_properties`;

CREATE TABLE `filezippercdr_properties` (
  `id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(100) DEFAULT NULL,
  `value` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3;

/*Data for the table `filezippercdr_properties` */

/*Table structure for table `ipwhitelist` */

DROP TABLE IF EXISTS `ipwhitelist`;

CREATE TABLE `ipwhitelist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cpDetailId` int NOT NULL,
  `whitelistedIp` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_CpDetail` (`cpDetailId`),
  KEY `ipwtlist_cpdid_wtlip` (`cpDetailId`,`whitelistedIp`),
  CONSTRAINT `FK_CpDetail` FOREIGN KEY (`cpDetailId`) REFERENCES `cpdetail` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3;

/*Data for the table `ipwhitelist` */

/*Table structure for table `language` */

DROP TABLE IF EXISTS `language`;

CREATE TABLE `language` (
  `id` int NOT NULL AUTO_INCREMENT,
  `language` varchar(25) NOT NULL,
  `lang_name` varchar(25) NOT NULL,
  `language_code` varchar(255) DEFAULT NULL,
  `language_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `language` (`language`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

/*Data for the table `language` */

insert  into `language`(`id`,`language`,`lang_name`,`language_code`,`language_name`) values 
(1,'_E','English',NULL,NULL);

/*Table structure for table `logcounter_properties` */

DROP TABLE IF EXISTS `logcounter_properties`;

CREATE TABLE `logcounter_properties` (
  `id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(100) DEFAULT NULL,
  `value` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;

/*Data for the table `logcounter_properties` */

/*Table structure for table `misreport_properties` */

DROP TABLE IF EXISTS `misreport_properties`;

CREATE TABLE `misreport_properties` (
  `id` int DEFAULT NULL,
  `key` varchar(50) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `misreport_properties` */

/*Table structure for table `msisdn_series_mapping` */

DROP TABLE IF EXISTS `msisdn_series_mapping`;

CREATE TABLE `msisdn_series_mapping` (
  `id` int NOT NULL AUTO_INCREMENT,
  `msisdn_series` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `msisdn_series_mapping` */

/*Table structure for table `operator` */

DROP TABLE IF EXISTS `operator`;

CREATE TABLE `operator` (
  `id` int NOT NULL AUTO_INCREMENT,
  `operator_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

/*Data for the table `operator` */

insert  into `operator`(`id`,`operator_name`) values 
(1,'airtel');

/*Table structure for table `price_point` */

DROP TABLE IF EXISTS `price_point`;

CREATE TABLE `price_point` (
  `id` int NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `validity` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `price_point` */

/*Table structure for table `price_validity_mapping` */

DROP TABLE IF EXISTS `price_validity_mapping`;

CREATE TABLE `price_validity_mapping` (
  `id` int NOT NULL AUTO_INCREMENT,
  `price` int NOT NULL,
  `validity` int NOT NULL,
  `subServiceId` int NOT NULL,
  `serviceId` int DEFAULT NULL,
  `time_perday` int DEFAULT NULL,
  `packId` varchar(50) DEFAULT NULL,
  `validityunithourly` tinyint(1) DEFAULT '0',
  `postprice` float DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_priceValidityMap` (`subServiceId`),
  KEY `serviceId` (`serviceId`),
  CONSTRAINT `FK_priceValidityMap` FOREIGN KEY (`subServiceId`) REFERENCES `sub_service_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `price_validity_mapping_ibfk_1` FOREIGN KEY (`serviceId`) REFERENCES `service_info` (`id`),
  CONSTRAINT `price_validity_mapping_ibfk_2` FOREIGN KEY (`serviceId`) REFERENCES `service_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb3;

/*Data for the table `price_validity_mapping` */

insert  into `price_validity_mapping`(`id`,`price`,`validity`,`subServiceId`,`serviceId`,`time_perday`,`packId`,`validityunithourly`,`postprice`) values 
(29,5,1,20,199,30,'123',0,0),
(30,35,7,21,199,30,'124',0,0);

/*Table structure for table `renewalretry` */

DROP TABLE IF EXISTS `renewalretry`;

CREATE TABLE `renewalretry` (
  `id` int NOT NULL AUTO_INCREMENT,
  `SubServiceId` int NOT NULL,
  `endDate` datetime DEFAULT NULL,
  `hours` int DEFAULT NULL,
  `lastBillingPrice` int DEFAULT NULL,
  `fallback` int DEFAULT NULL,
  `renewNotification` int DEFAULT NULL,
  `notifyChannel` int DEFAULT NULL,
  `renewalUserConsent` tinyint(1) DEFAULT NULL,
  `consentChannel` int DEFAULT NULL,
  `url` text CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `startTime` time DEFAULT NULL,
  `endTime` time DEFAULT NULL,
  `priority` int DEFAULT '1',
  `renewalStatus` varchar(25) DEFAULT 'scheduled',
  `retryStatus` varchar(25) DEFAULT 'scheduled',
  PRIMARY KEY (`id`),
  UNIQUE KEY `FK_renewalretry` (`SubServiceId`),
  KEY `FK_renewalretry_channel1` (`notifyChannel`),
  KEY `FK_renewalretry_channel2` (`consentChannel`),
  CONSTRAINT `FK_renewalretry` FOREIGN KEY (`SubServiceId`) REFERENCES `sub_service_info` (`id`),
  CONSTRAINT `FK_renewalretry_channel1` FOREIGN KEY (`notifyChannel`) REFERENCES `channels_info` (`id`),
  CONSTRAINT `FK_renewalretry_channel2` FOREIGN KEY (`consentChannel`) REFERENCES `channels_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;

/*Data for the table `renewalretry` */

/*Table structure for table `scheduler_properties` */

DROP TABLE IF EXISTS `scheduler_properties`;

CREATE TABLE `scheduler_properties` (
  `id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(1000) DEFAULT NULL,
  `value` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

/*Data for the table `scheduler_properties` */

insert  into `scheduler_properties`(`id`,`key`,`value`) values 
(1,'activemq.ip','failover://tcp://127.0.0.1:61616'),
(2,'activemq.telephonyQueueName','telephony2'),
(3,'scheduler.hardwareUrl','function=1&argument=3&service=1'),
(4,'scheduler.basePath','http://127.0.0.1/cgi-bin/rpcclient?'),
(5,'telephony.ip','127.0.0.1'),
(6,'scheduler.tps','10'),
(7,'SystemIP','127.0.0.1'),
(8,'scheduler.tpsUrl','function=2&argument=0');

/*Table structure for table `scheduler_subs_msgs` */

DROP TABLE IF EXISTS `scheduler_subs_msgs`;

CREATE TABLE `scheduler_subs_msgs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `starttime` time DEFAULT NULL,
  `endtime` time DEFAULT NULL,
  `status` varchar(25) DEFAULT 'scheduled',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

/*Data for the table `scheduler_subs_msgs` */

/*Table structure for table `scp_properties` */

DROP TABLE IF EXISTS `scp_properties`;

CREATE TABLE `scp_properties` (
  `id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(25) DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;

/*Data for the table `scp_properties` */

insert  into `scp_properties`(`id`,`key`,`value`) values 
(1,'filePath_uploadfile','/data/scp/ftppath'),
(2,'filestore','/data/filestore/services/template/'),
(3,'outputfolder','/data/scp/temp'),
(4,'CountryCodes','011'),
(5,'cdrPath','/data/cdr/ivr'),
(6,'obdMsisdnLength','10'),
(7,'servers','127.0.0.1'),
(8,'Sub_url','http://localhost:8080/SubscriptionEngineUI/welcome.htm'),
(9,'url_display','http://localhost:9070/Subs_Engine/getTotalSubscriber/'),
(10,'Ivr_url','http://localhost:8080/IVRSecurity/welcome.htm');

/*Table structure for table `service` */

DROP TABLE IF EXISTS `service`;

CREATE TABLE `service` (
  `id` int NOT NULL AUTO_INCREMENT,
  `service_id` varchar(255) DEFAULT NULL,
  `service_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `service` */

/*Table structure for table `service_channel_mapping` */

DROP TABLE IF EXISTS `service_channel_mapping`;

CREATE TABLE `service_channel_mapping` (
  `id` int NOT NULL AUTO_INCREMENT,
  `subServiceId` int DEFAULT NULL,
  `channelId` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_serviceChannelMap` (`subServiceId`),
  CONSTRAINT `FK_serviceChannelMap` FOREIGN KEY (`subServiceId`) REFERENCES `channels_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `service_channel_mapping` */

/*Table structure for table `service_info` */

DROP TABLE IF EXISTS `service_info`;

CREATE TABLE `service_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `serviceName` varchar(25) NOT NULL,
  `serviceId` varchar(25) NOT NULL,
  `status` int NOT NULL,
  `addedOn` datetime NOT NULL,
  `unsubReqToBilling` int NOT NULL,
  `chargingType` varchar(25) NOT NULL,
  `cpId` int NOT NULL,
  `notify` int DEFAULT NULL,
  `notifychannelid` int DEFAULT NULL,
  `alias` varchar(255) DEFAULT NULL,
  `shortCode` varchar(255) DEFAULT NULL,
  `operator` varchar(255) DEFAULT NULL,
  `country_code` int DEFAULT NULL,
  `actionOnBillingResp` int DEFAULT '1',
  `appCdr` tinyint(1) DEFAULT '0',
  `pendingUnsubToSdp` tinyint(1) DEFAULT '1',
  `additionalUrl` longtext,
  `countryCode` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1` (`serviceName`,`serviceId`),
  KEY `FK_service_info` (`cpId`),
  KEY `FK_service_info_channel` (`notifychannelid`),
  KEY `FK_service_info_country` (`country_code`),
  KEY `service_info_srvid` (`serviceId`),
  CONSTRAINT `FK_service_info` FOREIGN KEY (`cpId`) REFERENCES `cpdetail` (`id`),
  CONSTRAINT `FK_service_info_channel` FOREIGN KEY (`notifychannelid`) REFERENCES `channels_info` (`id`),
  CONSTRAINT `FK_service_info_country` FOREIGN KEY (`country_code`) REFERENCES `coutrydetails` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb3;

/*Data for the table `service_info` */

insert  into `service_info`(`id`,`serviceName`,`serviceId`,`status`,`addedOn`,`unsubReqToBilling`,`chargingType`,`cpId`,`notify`,`notifychannelid`,`alias`,`shortCode`,`operator`,`country_code`,`actionOnBillingResp`,`appCdr`,`pendingUnsubToSdp`,`additionalUrl`,`countryCode`) values 
(199,'News','NW',0,'2025-09-14 11:25:36',0,'SYNC',100,NULL,2,'NW','5560','IND',91,1,0,1,NULL,NULL);

/*Table structure for table `service_lang_mapping` */

DROP TABLE IF EXISTS `service_lang_mapping`;

CREATE TABLE `service_lang_mapping` (
  `id` int NOT NULL AUTO_INCREMENT,
  `service_id` int NOT NULL,
  `lang_id` int NOT NULL,
  `langId` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_service_lang_mapping_service` (`service_id`),
  KEY `FK_service_lang_mapping_lang` (`lang_id`),
  CONSTRAINT `FK_service_lang_mapping_lang` FOREIGN KEY (`lang_id`) REFERENCES `language` (`id`),
  CONSTRAINT `FK_service_lang_mapping_service` FOREIGN KEY (`service_id`) REFERENCES `service_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

/*Data for the table `service_lang_mapping` */

insert  into `service_lang_mapping`(`id`,`service_id`,`lang_id`,`langId`) values 
(1,199,1,0);

/*Table structure for table `service_url` */

DROP TABLE IF EXISTS `service_url`;

CREATE TABLE `service_url` (
  `i_id` double NOT NULL AUTO_INCREMENT,
  `service_id` varchar(225) DEFAULT NULL,
  `sub_url` varchar(765) DEFAULT NULL,
  `unsub_url` varchar(765) DEFAULT NULL,
  PRIMARY KEY (`i_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `service_url` */

/*Table structure for table `sms_properties` */

DROP TABLE IF EXISTS `sms_properties`;

CREATE TABLE `sms_properties` (
  `id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(25) NOT NULL,
  `value` varchar(250) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

/*Data for the table `sms_properties` */

insert  into `sms_properties`(`id`,`key`,`value`) values 
(1,'callerID','702'),
(2,'operator','airtel'),
(3,'protocol','HTTP'),
(4,'country','india'),
(5,'countryCodes','011'),
(6,'msisdnLength','10'),
(7,'TPS','20'),
(8,'cdrPath','/data/cdr/sms/'),
(9,'transactionTimer','8000'),
(10,'callbackUrl','http://127.0.0.1:8080/SMSClient/$msisdn$/'),
(11,'failureRetries','3'),
(12,'retryPeriod','10'),
(13,'encoding','UTF-16BE'),
(14,'smsMaxLength','66'),
(15,'msisdnSeriesLength','4');

/*Table structure for table `store_renew_notification` */

DROP TABLE IF EXISTS `store_renew_notification`;

CREATE TABLE `store_renew_notification` (
  `id` int NOT NULL AUTO_INCREMENT,
  `RenewUrl` text,
  `currentTime` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `store_renew_notification` */

/*Table structure for table `store_sub_url` */

DROP TABLE IF EXISTS `store_sub_url`;

CREATE TABLE `store_sub_url` (
  `id` int NOT NULL AUTO_INCREMENT,
  `subUrl` varchar(500) DEFAULT NULL,
  `currentDate` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `store_sub_url` */

/*Table structure for table `sub_service_info` */

DROP TABLE IF EXISTS `sub_service_info`;

CREATE TABLE `sub_service_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `subServiceName` varchar(25) NOT NULL,
  `subServiceId` varchar(25) DEFAULT NULL,
  `serviceId` int NOT NULL,
  `subServiceStatus` tinyint(1) NOT NULL,
  `doCharging` tinyint(1) NOT NULL,
  `addedOn` datetime NOT NULL,
  `sub_type` varchar(25) NOT NULL,
  `time_perday` int NOT NULL,
  `demo` tinyint(1) NOT NULL,
  `demoDays` int NOT NULL,
  `makeOnlyCdr` tinyint(1) NOT NULL,
  `retry` tinyint(1) NOT NULL,
  `renewal` tinyint(1) NOT NULL,
  `notification` int NOT NULL,
  `url` longtext,
  `smsAlert` tinyint(1) NOT NULL,
  `smsUrl` longtext,
  `giftId` int DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb3;

/*Data for the table `sub_service_info` */

insert  into `sub_service_info`(`id`,`subServiceName`,`subServiceId`,`serviceId`,`subServiceStatus`,`doCharging`,`addedOn`,`sub_type`,`time_perday`,`demo`,`demoDays`,`makeOnlyCdr`,`retry`,`renewal`,`notification`,`url`,`smsAlert`,`smsUrl`,`giftId`,`topUp`,`topUpPerDay`,`stateChanger`,`msisdnseriescheck`,`notificationchannel`) values 
(20,'NewsDaily','NWDaily',199,1,1,'2025-09-14 11:28:46','sync',0,1,0,0,0,0,0,'',0,'',NULL,0,0,1,0,'sms'),
(21,'NewsWeekly','NWWeekly',199,1,1,'2025-09-14 13:39:54','sync',0,0,0,0,0,0,0,'',0,'',NULL,0,0,1,0,'sms');

/*Table structure for table `sub_service_msgs` */

DROP TABLE IF EXISTS `sub_service_msgs`;

CREATE TABLE `sub_service_msgs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `subServiceId` int NOT NULL,
  `demoStartedMessage` varchar(255) DEFAULT NULL,
  `demoEndMessage` varchar(255) DEFAULT NULL,
  `subscriptionSuccessMessage` varchar(255) DEFAULT NULL,
  `subscriptionFailureMessage` varchar(255) DEFAULT NULL,
  `subscriptionPendingMessage` varchar(255) DEFAULT NULL,
  `alreadySubscribedMessage` varchar(255) DEFAULT NULL,
  `alreadyUnsubscribedMessage` varchar(255) DEFAULT NULL,
  `preRenewalMessage` varchar(255) DEFAULT NULL,
  `renewalMessage` varchar(255) DEFAULT NULL,
  `unsubscriptionSuccessMessage` varchar(255) DEFAULT NULL,
  `unsubscriptionFailureMessage` varchar(255) DEFAULT NULL,
  `lang_id` int DEFAULT NULL,
  `renewalFailure` varchar(255) DEFAULT NULL,
  `subscriptionSuccessNoRenewal` varchar(255) DEFAULT NULL,
  `unsubscriptionSuccessNoRenewal` varchar(255) DEFAULT NULL,
  `expirationMessage` varchar(255) DEFAULT NULL,
  `expirationNotification` varchar(255) DEFAULT NULL,
  `consentMsg` varchar(255) DEFAULT NULL,
  `topUpSuccess` varchar(255) DEFAULT NULL,
  `topUpFailure` varchar(255) DEFAULT NULL,
  `alreadySubscribedNonActiveMessage` varchar(255) DEFAULT NULL,
  `subRegistartionFailureMsg` varchar(255) DEFAULT NULL,
  `subRetryFailureMsg` varchar(255) DEFAULT NULL,
  `renewRetryFailureMsg` varchar(255) DEFAULT NULL,
  `autoRenewEnableMsg` varchar(255) DEFAULT NULL,
  `autoRenewDisablMsg` varchar(255) DEFAULT NULL,
  `msgIntervel` varchar(255) DEFAULT NULL,
  `stopAllBlockMSG` varchar(255) DEFAULT NULL,
  `dndBlockMSG` varchar(255) DEFAULT NULL,
  `langId` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_lang` (`subServiceId`,`lang_id`),
  KEY `FK_sub_service_msgs` (`subServiceId`),
  KEY `FK_sub_service_lang` (`lang_id`),
  KEY `sub_srv_msgs_sid_lang_id` (`subServiceId`,`lang_id`),
  CONSTRAINT `FK_sub_service_info_msgs` FOREIGN KEY (`subServiceId`) REFERENCES `sub_service_info` (`id`),
  CONSTRAINT `FK_sub_service_lang` FOREIGN KEY (`lang_id`) REFERENCES `language` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;

/*Data for the table `sub_service_msgs` */

insert  into `sub_service_msgs`(`id`,`subServiceId`,`demoStartedMessage`,`demoEndMessage`,`subscriptionSuccessMessage`,`subscriptionFailureMessage`,`subscriptionPendingMessage`,`alreadySubscribedMessage`,`alreadyUnsubscribedMessage`,`preRenewalMessage`,`renewalMessage`,`unsubscriptionSuccessMessage`,`unsubscriptionFailureMessage`,`lang_id`,`renewalFailure`,`subscriptionSuccessNoRenewal`,`unsubscriptionSuccessNoRenewal`,`expirationMessage`,`expirationNotification`,`consentMsg`,`topUpSuccess`,`topUpFailure`,`alreadySubscribedNonActiveMessage`,`subRegistartionFailureMsg`,`subRetryFailureMsg`,`renewRetryFailureMsg`,`autoRenewEnableMsg`,`autoRenewDisablMsg`,`msgIntervel`,`stopAllBlockMSG`,`dndBlockMSG`,`langId`) values 
(5,20,'Your demo has started. Enjoy the service NWDaily free for 1 Day!','Your demo period for NWDaily has ended. Subscribe now to continue enjoying the service.','You have successfully subscribed to NWDaily. Enjoy!','Subscription to NWDaily failed. Please try again later','Your subscription request is pending for NWDAily. We will notify you once it is confirmed','You are already subscribed to NWDaily','You have an inactive subscription. Please renew to continue','Your subscription will renew soon. Ensure enough balance to continue using NWDaily','Your subscription has been renewed successfully. Enjoy uninterrupted service!','You have successfully unsubscribed from NWDaily. We’re sad to see you go!','Unsubscription failed. Please try again later',NULL,'Your subscription renewal failed. Please check your balance or try again.',NULL,'You are unsubscribed from NWDaily. Renewal has been disabled.','Your subscription has expired. Renew now to continue enjoying','Reminder: Your subscription will expire soon. Renew to avoid service interruption','Reply YES to confirm your consent for subscription to NWDaily',NULL,NULL,'You are not currently subscribed to',NULL,NULL,'Retry attempt for renewal failed. Please try later','Auto-renewal has been enabled for your subscription.','Auto-renewal has been disabled for your subscription','You will receive notifications as per the set interval','All promotional messages have been stopped as per your request.','You are on DND. Subscription messages cannot be delivered.',1),
(6,21,'Your demo has started. Enjoy NWWeekly free for 7 Days!','Your demo period has ended. Subscribe now to continue enjoying NWWeekly.','You have successfully subscribed to NWWeekly. Enjoy!','Subscription to NWWeekly failed. Please try again later.','Your subscription request is pending. We will notify you once it is confirmed.','You are already subscribed to NWWeekly.','You are not currently subscribed to NWWeekly.','Your subscription will renew soon. Ensure enough balance to continue using NWWeekly.','Your subscription to NWWeekly has been renewed successfully.','You have successfully unsubscribed from NWWeekly. We’re sad to see you go!','Unsubscription from NWWeekly failed. Please try again later.',1,'Your subscription renewal for NWWeekly failed. Please check your balance.','You are subscribed to NWWeekly. Renewal is not enabled.','You are unsubscribed from NWWeekly. Renewal has been disabled.','Your subscription to NWWeekly has expired. Renew now to continue.','Reminder: Your NWWeekly subscription will expire soon. Renew to avoid service interruption.','Reply YES to confirm your consent for subscription to NWWeekly.','Your top-up for NWWeekly was successful. Balance updated.','Top-up for NWWeekly failed. Please try again later.','You have an inactive subscription to NWWeekly. Please renew to continue.','We could not register your NWWeekly subscription. Please try again.','Retry attempt for NWWeekly subscription failed. Contact support if the issue continues.','Retry attempt for NWWeekly renewal failed. Please try later.','Auto-renewal has been enabled for your NWWeekly subscription.','Auto-renewal has been disabled for your NWWeekly subscription.','You will receive NWWeekly notifications as per the set interval.','All promotional messages for NWWeekly have been stopped as per your request.','You are on DND. NWWeekly subscription messages cannot be delivered.',1);

/*Table structure for table `subs_properties` */

DROP TABLE IF EXISTS `subs_properties`;

CREATE TABLE `subs_properties` (
  `id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(50) DEFAULT NULL,
  `value` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=latin1;

/*Data for the table `subs_properties` */

insert  into `subs_properties`(`id`,`key`,`value`) values 
(1,'countryCodes','india'),
(2,'msisdnLength','10'),
(3,'countryCodesForSms','011'),
(4,'minBalance','5'),
(5,'ivrShortCode','221'),
(6,'readTimeout','140000'),
(7,'timeout','10000'),
(8,'cpId','100'),
(9,'language','_F'),
(10,'priceMultiply','1'),
(11,'CDR.path','/data/cdr/subscription/'),
(12,'country','india'),
(13,'operator','airtel'),
(14,'currency','INR'),
(15,'blackListCheck','0'),
(16,'smsDateFormat','dd-MM-yyyy HH:mm'),
(17,'ussdResponse','$msisdn$|$status$|$reason$'),
(18,'appendTextFor_EMsg',''),
(19,'appendTextFor_EMsg',''),
(20,'calendarDay','false'),
(21,'ip','127.0.0.1'),
(22,'appendTextFor_OtherMsg',''),
(23,'sleepTime','5'),
(24,'additionalUrlCallOnUnsub',''),
(25,'cpCallbackRenew','false'),
(26,'cpCallback','false'),
(27,'deductReqForPostpaid','false'),
(28,'numberSeriesCheck','false'),
(29,'countryCodeLength','3'),
(30,'cdrXml','true'),
(31,'countryCodeLength','3'),
(32,'cdrXml','true'),
(33,'msgToPrepaidOnly','true'),
(34,'postpaidPrice','1200'),
(35,'validity','30'),
(36,'SdpStateChangerCDR','false'),
(37,'forceUnsubStatus','pending'),
(38,'checkClass','false'),
(39,'chargingType','IN'),
(40,'seriseLength','5'),
(41,'demoCheck','false'),
(81,'BlockChannelNotification',NULL),
(82,'NumberOfHourluckPeriodForSub','0'),
(83,'ActivatioRetryFallback','false'),
(84,'RenewalRetryFallback','false'),
(85,'ActivationFallback','false');

/*Table structure for table `subscription` */

DROP TABLE IF EXISTS `subscription`;

CREATE TABLE `subscription` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nextAction` varchar(25) DEFAULT NULL,
  `autoRenew` varchar(255) DEFAULT NULL,
  `CALL_ATTEMPTS` int DEFAULT NULL,
  `CIRCLE` varchar(3) DEFAULT NULL,
  `COUNTRY` varchar(3) DEFAULT NULL,
  `CPID` int DEFAULT NULL,
  `END_DATE` datetime(6) DEFAULT NULL,
  `ERROR_MSG` varchar(25) DEFAULT NULL,
  `GIFT_ID` varchar(25) DEFAULT NULL,
  `LANGUAGE` varchar(3) DEFAULT NULL,
  `LAST_CALL_DATE` datetime(6) DEFAULT NULL,
  `LAST_RENEW_DATE` datetime(6) DEFAULT NULL,
  `MSISDN` varchar(25) DEFAULT NULL,
  `NEXT_MSG_DATE` datetime(6) DEFAULT NULL,
  `NEXT_RETRY_DATE` datetime(6) DEFAULT NULL,
  `OPERATOR` varchar(25) DEFAULT NULL,
  `option1` varchar(255) DEFAULT NULL,
  `PRICE` int DEFAULT NULL,
  `PRIMARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `RETRY_COUNT` int DEFAULT NULL,
  `scheduler_renewal_status` varchar(25) NOT NULL,
  `SCHEDULER_RETRY_STATUS` varchar(45) DEFAULT NULL,
  `scheduler_statechanger_status` varchar(25) DEFAULT NULL,
  `scheduler_subs_msgs_status` varchar(255) DEFAULT NULL,
  `SECONDARY_ACT_MODE` varchar(25) DEFAULT NULL,
  `SERVICE_ID` varchar(25) DEFAULT NULL,
  `serviceName` varchar(25) DEFAULT NULL,
  `START_DATE` datetime(6) DEFAULT NULL,
  `STATUS` varchar(20) DEFAULT NULL,
  `SUB_SERVICE_NAME` varchar(25) DEFAULT NULL,
  `SUB_TIME_LEFT` int DEFAULT NULL,
  `SUB_TYPE` varchar(10) DEFAULT NULL,
  `SUBSERVICE_ID` varchar(25) DEFAULT NULL,
  `transactionId` varchar(25) DEFAULT NULL,
  `TRANSACTION_STATUS` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UKmcxb239f1xefs4b69d8xrhycd` (`MSISDN`,`SUBSERVICE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `subscription` */

/*Table structure for table `subscriptionmsgs_properties` */

DROP TABLE IF EXISTS `subscriptionmsgs_properties`;

CREATE TABLE `subscriptionmsgs_properties` (
  `id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(1000) DEFAULT NULL,
  `value` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;

/*Data for the table `subscriptionmsgs_properties` */

/*Table structure for table `subservice` */

DROP TABLE IF EXISTS `subservice`;

CREATE TABLE `subservice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sub_service_id` varchar(255) DEFAULT NULL,
  `sub_service_name` varchar(255) DEFAULT NULL,
  `service_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk1m388ili0xsqkoxvqd9x5q54` (`service_id`),
  CONSTRAINT `FKk1m388ili0xsqkoxvqd9x5q54` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `subservice` */

/*Table structure for table `topup` */

DROP TABLE IF EXISTS `topup`;

CREATE TABLE `topup` (
  `id` int NOT NULL AUTO_INCREMENT,
  `price` float DEFAULT NULL,
  `minutes` int DEFAULT NULL,
  `subServiceId` int DEFAULT NULL,
  `topUpId` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_topup` (`subServiceId`),
  CONSTRAINT `FK_topup` FOREIGN KEY (`subServiceId`) REFERENCES `sub_service_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `topup` */

/*Table structure for table `user_mapping_table` */

DROP TABLE IF EXISTS `user_mapping_table`;

CREATE TABLE `user_mapping_table` (
  `id` int NOT NULL AUTO_INCREMENT,
  `country_id` int DEFAULT NULL,
  `operator_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_arf8m675jenl60rm5ax8wj142` (`country_id`),
  KEY `FK_2sbu0u3lgqjkxjbp8dkl0rdyq` (`operator_id`),
  KEY `FK_cjfjxcabmjfrg7a31d4ahuya9` (`user_id`),
  CONSTRAINT `FK_2sbu0u3lgqjkxjbp8dkl0rdyq` FOREIGN KEY (`operator_id`) REFERENCES `operator` (`id`),
  CONSTRAINT `FK_arf8m675jenl60rm5ax8wj142` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`),
  CONSTRAINT `FK_cjfjxcabmjfrg7a31d4ahuya9` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

/*Data for the table `user_mapping_table` */

insert  into `user_mapping_table`(`id`,`country_id`,`operator_id`,`user_id`) values 
(1,1,1,1);

/*Table structure for table `user_roles` */

DROP TABLE IF EXISTS `user_roles`;

CREATE TABLE `user_roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `authority` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_user_roles` (`user_id`),
  CONSTRAINT `FK_user_roles` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;

/*Data for the table `user_roles` */

insert  into `user_roles`(`id`,`user_id`,`authority`) values 
(1,1,'ADMIN');

/*Table structure for table `user_status` */

DROP TABLE IF EXISTS `user_status`;

CREATE TABLE `user_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` varchar(25) DEFAULT NULL,
  `nextState` varchar(25) DEFAULT NULL,
  `maxNoOfDays` int DEFAULT NULL,
  `maxRetryCount` int DEFAULT NULL,
  `subServiceId` int DEFAULT NULL,
  `upgrade` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_user_status_subServiceInfo` (`subServiceId`),
  CONSTRAINT `FK_user_status_subServiceInfo` FOREIGN KEY (`subServiceId`) REFERENCES `sub_service_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

/*Data for the table `user_status` */

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3;

/*Data for the table `users` */

insert  into `users`(`id`,`username`,`password`,`status`) values 
(1,'admin','$2a$10$u8BSWsgGLMbb04OZ4SN/auC6N3UWXZNKKgFvyzOFxWYd5VMl3H.2e',1);

/*Table structure for table `ussd_message` */

DROP TABLE IF EXISTS `ussd_message`;

CREATE TABLE `ussd_message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reason` varchar(50) DEFAULT NULL,
  `ussdReason` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;

/*Data for the table `ussd_message` */

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
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
