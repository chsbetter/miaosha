/*
MySQL Backup
Database: miaosha
Backup Time: 2020-04-05 22:24:49
*/

SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `miaosha`.`item`;
DROP TABLE IF EXISTS `miaosha`.`item_stock`;
DROP TABLE IF EXISTS `miaosha`.`order_info`;
DROP TABLE IF EXISTS `miaosha`.`promo`;
DROP TABLE IF EXISTS `miaosha`.`sequence_info`;
DROP TABLE IF EXISTS `miaosha`.`user_info`;
DROP TABLE IF EXISTS `miaosha`.`user_password`;
CREATE TABLE `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `price` double(10,2) NOT NULL DEFAULT '0.00',
  `description` varchar(500) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `sales` int(255) NOT NULL DEFAULT '0',
  `img_url` varchar(3000) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
CREATE TABLE `item_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) NOT NULL DEFAULT '0',
  `stock` int(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
CREATE TABLE `order_info` (
  `id` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `item_id` int(11) NOT NULL DEFAULT '0',
  `item_price` double(10,2) NOT NULL DEFAULT '0.00',
  `amount` int(255) NOT NULL DEFAULT '0',
  `order_price` double(10,2) NOT NULL DEFAULT '0.00',
  `promo_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
CREATE TABLE `promo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `promo_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `start_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `item_id` int(11) NOT NULL DEFAULT '0',
  `promo_item_price` double(10,2) NOT NULL DEFAULT '0.00',
  `end_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
CREATE TABLE `sequence_info` (
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `current_value` int(255) NOT NULL DEFAULT '0',
  `step` int(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
CREATE TABLE `user_info` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `gender` tinyint(255) NOT NULL COMMENT '//1代表男性，2代表女性',
  `telephone` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `register_mode` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '//byphone,bywechat,byalipay',
  `third_party_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `age` varchar(11) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `telephone_unique_index` (`telephone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
CREATE TABLE `user_password` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `encrpt_password` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `user_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
BEGIN;
LOCK TABLES `miaosha`.`item` WRITE;
DELETE FROM `miaosha`.`item`;
INSERT INTO `miaosha`.`item` (`id`,`title`,`price`,`description`,`sales`,`img_url`) VALUES (6, '小米10Pro', 4999.00, '最新小米手机', 201, 'https://img.pconline.com.cn/images/product/1234200/20202/14/15816539037927684_800.jpg'),(7, '荣耀V30Pro', 3999.00, '荣耀旗舰标杆', 100, 'https://img.pconline.com.cn/images/product/1223327/201911/26/15747627670512376_800.jpg'),(8, '一加7TPro', 4599.00, '首发上手，不止好看', 52, 'https://img.pconline.com.cn/images/product/1220/1220513/0.jpg');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `miaosha`.`item_stock` WRITE;
DELETE FROM `miaosha`.`item_stock`;
INSERT INTO `miaosha`.`item_stock` (`id`,`item_id`,`stock`) VALUES (6, 6, 7),(7, 7, 20),(8, 8, 28);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `miaosha`.`order_info` WRITE;
DELETE FROM `miaosha`.`order_info`;
INSERT INTO `miaosha`.`order_info` (`id`,`user_id`,`item_id`,`item_price`,`amount`,`order_price`,`promo_id`) VALUES ('2020040500000000', 22, 6, 0.00, 1, 0.00, 0),('2020040500000100', 22, 6, 4999.00, 1, 4999.00, 0),('2020040500000200', 22, 6, 4999.00, 1, 4999.00, 0),('2020040500000300', 22, 8, 4599.00, 1, 4599.00, 0),('2020040500000400', 22, 8, 1999.00, 1, 1999.00, 1);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `miaosha`.`promo` WRITE;
DELETE FROM `miaosha`.`promo`;
INSERT INTO `miaosha`.`promo` (`id`,`promo_name`,`start_date`,`item_id`,`promo_item_price`,`end_date`) VALUES (1, '1加7TPro抢购活动', '2020-04-05 21:18:00', 8, 1999.00, '2020-04-10 23:59:59');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `miaosha`.`sequence_info` WRITE;
DELETE FROM `miaosha`.`sequence_info`;
INSERT INTO `miaosha`.`sequence_info` (`name`,`current_value`,`step`) VALUES ('order_info', 5, 1);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `miaosha`.`user_info` WRITE;
DELETE FROM `miaosha`.`user_info`;
INSERT INTO `miaosha`.`user_info` (`id`,`name`,`gender`,`telephone`,`register_mode`,`third_party_id`,`age`) VALUES (1, 'chs', 1, '13685240115', 'byphone', '', '30'),(22, '111', 1, '111', 'byphone', '', '20'),(24, 'qqq', 1, '222', 'byphone', '', '22');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `miaosha`.`user_password` WRITE;
DELETE FROM `miaosha`.`user_password`;
INSERT INTO `miaosha`.`user_password` (`id`,`encrpt_password`,`user_id`) VALUES (1, 'fsdfdsfd', 1),(4, 'aY1RoZ2KEhzlgUmde3AWaA==', 22),(5, 'vL4zZeasleosA0OiOVg03Q==', 24);
UNLOCK TABLES;
COMMIT;
