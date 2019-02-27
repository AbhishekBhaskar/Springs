CREATE SCHEMA `autocg`;

CREATE TABLE `autocg`.`remotedb` (

  `id` int(11) NOT NULL AUTO_INCREMENT,

  `remotedbname` varchar(45) NOT NULL,

  `hostip` varchar(45) NOT NULL,

  `username` varchar(45) NOT NULL,

  `password` varchar(45) NOT NULL,

  `appid` int not NULL,                                                                                     /*FK - Application*/

  `orgid` int not NULL,                                                                                      /*FK - Organization*/
  
  `active` char(1) NOT NULL DEFAULT 'Y',

  PRIMARY KEY (`id`),

  UNIQUE KEY(`hostip`),

  UNIQUE KEY(`remotedbname`));

 

CREATE TABLE `autocg`.`codegendetails` (

  `id` int(11) NOT NULL AUTO_INCREMENT,

  `hostip` varchar(45) NOT NULL,

  `schemaname` varchar(45) NOT NULL,

  `gentables` varchar(150) NOT NULL,

  `gendate` datetime DEFAULT now(),

  `downloadlink` varchar(60) DEFAULT NULL,

  `appname` varchar(45) NOT NULL,

  `packagename` varchar(55) NOT NULL,

  `appid` int not NULL,                                                                                     /*FK - Application*/

  `orgid` int not NULL,                                                                                      /*FK - Organization*/
  
  `active` char(1) NOT NULL DEFAULT 'Y',

  PRIMARY KEY (`id`));

 

ALTER TABLE `autocg`.`codegendetails` ADD FOREIGN KEY host_ip_idx(`hostip`) REFERENCES `remotedb`(`hostip`);

 

INSERT INTO `autocg`.`remotedb`(remotedbname,hostip,username,password,appid,orgid) VALUES('test','10.16.70.147','root','password',-1,-1);

INSERT INTO `autocg`.`codegendetails`(hostip,schemaname,gentables,downloadlink,appname,packagename,appid,orgid,active) VALUES('10.16.70.147','apipost','ads_targetpage,product','c:\Desktop\abhi\springs','automate','com.automate',-1,-1,'Y'); 