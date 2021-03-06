USE enrollment;

CREATE TABLE IF NOT EXISTS `enrollment`.`MEMBER_MASTER` (
 `SEQ_MEMB_ID` int NOT NULL AUTO_INCREMENT,
 `SUBSCRIBER_ID` VARCHAR(50) NOT NULL,
 `PERSON_NUMBER` CHAR(2) NOT NULL,
 `LAST_NAME` VARCHAR(60) NOT NULL,
 `FIRST_NAME` VARCHAR(30),
 `DATE_OF_BIRTH` DATE NOT NULL,
 `ELIG_STAT` VARCHAR(1) NOT NULL,
 `HOME_PHONE_NUMBER` VARCHAR(10) NOT NULL,
PRIMARY KEY (`SEQ_MEMB_ID`,`SUBSCRIBER_ID`),
INDEX `IDX_SEQ_MEMB_ID` (`SEQ_MEMB_ID` ASC),
INDEX `IDX_SUBSCRIBER_ID` (`SUBSCRIBER_ID` ASC)
);

ALTER TABLE `enrollment`.`MEMBER_MASTER` AUTO_INCREMENT = 2000;