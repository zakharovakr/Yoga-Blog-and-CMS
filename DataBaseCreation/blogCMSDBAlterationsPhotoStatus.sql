USE blogCMSDB;

ALTER TABLE blogpost
ADD COLUMN `status` VARCHAR(10) AFTER `type`,
ADD COLUMN photoFileName VARCHAR(255) AFTER timePosted;

USE blogCMSDBTest;
ALTER TABLE blogpost
ADD COLUMN `status` VARCHAR(10) AFTER `type`,
ADD COLUMN photoFileName VARCHAR(255) AFTER timePosted;