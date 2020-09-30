/*
Navicat MySQL Data Transfer

Source Server         : yun-root
Source Server Version : 80015
Source Host           : 193.112.44.170:13306
Source Database       : postBar_db

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2019-03-26 17:38:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for chatroom
-- ----------------------------
DROP TABLE IF EXISTS `chatroom`;
CREATE TABLE `chatroom` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `post_Count` int(11) DEFAULT NULL,
  `is_Delete` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of chatroom
-- ----------------------------
INSERT INTO `chatroom` VALUES ('16', '聊天室3', '1', '1');
INSERT INTO `chatroom` VALUES ('17', '聊天室2', '1', '0');
INSERT INTO `chatroom` VALUES ('18', '聊天室1', '0', '0');

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `click_Times` int(11) DEFAULT NULL,
  `content` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_Delete` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `room_Id` int(11) DEFAULT NULL,
  `is_Delete` int(11) DEFAULT NULL,
  `publish_Date` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES ('11', '2', '帖子内容1', null, '帖子1', '16', '0', '2019-03-26');
INSERT INTO `post` VALUES ('12', '1', '帖子2', null, '帖子2', '17', '0', '2019-03-26');

-- ----------------------------
-- Table structure for post_click
-- ----------------------------
DROP TABLE IF EXISTS `post_click`;
CREATE TABLE `post_click` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ip` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `post_Id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `room_Id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of post_click
-- ----------------------------
INSERT INTO `post_click` VALUES ('1', '2019-03-26', null, null, '16');
INSERT INTO `post_click` VALUES ('2', '2019-03-26', null, null, '16');
INSERT INTO `post_click` VALUES ('3', '2019-03-26', null, null, '16');
INSERT INTO `post_click` VALUES ('4', '2019-03-26', null, null, '16');
INSERT INTO `post_click` VALUES ('5', '2019-03-26', null, null, '17');
SET FOREIGN_KEY_CHECKS=1;
