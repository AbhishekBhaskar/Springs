CREATE SCHEMA `e-comm`;

CREATE TABLE `e-comm`.`organization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `country` varchar(45) NOT NULL,
  `ph_no` varchar(10) NOT NULL,
  `no_of_emps` int(11) NOT NULL,
  `profits` float NOT NULL,
  `officeaddress` varchar(50) DEFAULT NULL,
  `websiteurl` varchar(50) NOT NULL,
  PRIMARY KEY (`id`), UNIQUE KEY (`code`));

CREATE TABLE `e-comm`.`products` (
  `pr_id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `pr_code` varchar(45) NOT NULL,
  `pr_name` varchar(45) NOT NULL,
  `pr_url` varchar(50) NOT NULL,
  `image` longblob NULL,
  `license_number` varchar(50) NOT NULL,
  `cost_per_item` float NOT NULL,
  `created` date NULL,
  `warranty_period` int(11) NOT NULL,
  PRIMARY KEY (`pr_id`),
  UNIQUE KEY (`pr_code`));

CREATE TABLE `e-comm`.`vendor_org` (
  `vendor_id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `org_name` varchar(50) NOT NULL,
  `name` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `ph_no` varchar(10) NOT NULL,
  `domain` varchar(45) NOT NULL,
  PRIMARY KEY (`vendor_id`),
  UNIQUE KEY (`email`));

ALTER TABLE `e-comm`.`products` 
ADD FOREIGN KEY fk_prod(`org_id`)
REFERENCES `organization`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE `e-comm`.`vendor_org` 
ADD FOREIGN KEY fk_vendor(`org_id`)
REFERENCES `organization`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;

INSERT INTO `e-comm`.`organization`(code,name,country,ph_no,no_of_emps,profits,officeaddress,websiteurl) VALUES('LG_0132','LG','USA','6191691312',20,35000.0,'12,2nd cross,koramangla','www.lg.org');

INSERT INTO `e-comm`.`organization`(code,name,country,ph_no,no_of_emps,profits,officeaddress,websiteurl) VALUES('Samsung_0141','Samsung','South Korea','4081363432',50,60000.0,'34,10nd cross,Indiranagar','www.samsung.org');

INSERT INTO `e-comm`.`products`(org_id,pr_code,pr_name,pr_url,license_number,cost_per_item,created,warranty_period) VALUES(1,'PR_0012','T.V','www.e-comm/pr1.com','2412141',35000.0,'2019-02-24',2);

INSERT INTO `e-comm`.`products`(org_id,pr_code,pr_name,pr_url,license_number,cost_per_item,created,warranty_period) VALUES(2,'PR_0054','Headphones','www.e-comm/pr2.com','1431412',12000.0,'2019-10-12',1);

INSERT INTO `e-comm`.`vendor_org`(org_id,org_name,name,username,email,password,ph_no,domain)VALUES(2,'Samsung','Shashank','tomcat','shashank@gmail.com','tomcat123','9162162531','Billing');

INSERT INTO `e-comm`.`vendor_org`(org_id,org_name,name,username,email,password,ph_no,domain)VALUES(1,'LG','Prateek','prateek','prateek@gmail.com','random@20','8273613131','Orders');