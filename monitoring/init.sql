CREATE DATABASE IF NOT EXISTS library_db;

USE library_db;

CREATE TABLE IF NOT EXISTS library_db.customer_t (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `version` bigint DEFAULT NULL,
  `FIRST_NAME` varchar(255) DEFAULT NULL,
  `MIDDLE_NAME` varchar(255) DEFAULT NULL,
  `LAST_NAME` varchar(255) DEFAULT NULL,
  `EMAIL_ADDRESS` varchar(255) NOT NULL,
  `country_code` varchar(255) DEFAULT NULL,
  `area_code` varchar(255) DEFAULT NULL,
  `prefix` varchar(255) DEFAULT NULL,
  `line_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `EMAIL_ADDRESS` (`EMAIL_ADDRESS`),
  UNIQUE KEY `UK6ybcamxpthh34shoht62y48h0` (`EMAIL_ADDRESS`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO library_db.customer_t
(`version`,
`FIRST_NAME`,
`MIDDLE_NAME`,
`LAST_NAME`,
`EMAIL_ADDRESS`,
`country_code`,
`area_code`,
`prefix`,
`line_number`)
VALUES (0,'Dollie','R.','Adams','dollie.adams@gmail.com','+1',301,583,2304);

INSERT INTO library_db.customer_t
(`version`,
`FIRST_NAME`,
`MIDDLE_NAME`,
`LAST_NAME`,
`EMAIL_ADDRESS`,
`country_code`,
`area_code`,
`prefix`,
`line_number`)
VALUES (1,'Cornelia','J.','Andresen','cornelia.andresen@gmail.com','+1',240,322,1234);
INSERT INTO library_db.customer_t
(`version`,
`FIRST_NAME`,
`MIDDLE_NAME`,
`LAST_NAME`,
`EMAIL_ADDRESS`,
`country_code`,
`area_code`,
`prefix`,
`line_number`)
VALUES (0,'Coral','Villareal','Betancourt','coral.betancourt@yahoo.com','+1',813,234,6442);
INSERT INTO library_db.customer_t
(`version`,
`FIRST_NAME`,
`MIDDLE_NAME`,
`LAST_NAME`,
`EMAIL_ADDRESS`,
`country_code`,
`area_code`,
`prefix`,
`line_number`)
VALUES (0,'Michael','J.','Grover','michael.grover@gmail.com','+1',240,576,2369);
INSERT INTO library_db.customer_t
(`version`,
`FIRST_NAME`,
`MIDDLE_NAME`,
`LAST_NAME`,
`EMAIL_ADDRESS`,
`country_code`,
`area_code`,
`prefix`,
`line_number`)
VALUES (0,'John','D.','Mudra','john.mudra@gmail.com','+1',301,684,9273);