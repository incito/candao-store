/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : newspicyway

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-05-18 09:33:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_b_padconfig`
-- ----------------------------
DROP TABLE IF EXISTS `t_b_padconfig`;
CREATE TABLE `t_b_padconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `padloginpass` varchar(20) DEFAULT NULL,
  `social` tinyint(1) DEFAULT NULL,
  `seatimageurls` varchar(500) DEFAULT NULL,
  `seatimagenames` varchar(100) DEFAULT NULL,
  `vipstatus` tinyint(1) DEFAULT NULL,
  `viptype` varchar(8) DEFAULT NULL,
  `vipcandaourl` varchar(50) DEFAULT NULL,
  `vipotherurl` varchar(50) DEFAULT NULL,
  `clickimagedish` tinyint(1) DEFAULT NULL,
  `onepage` tinyint(1) DEFAULT NULL,
  `newplayer` tinyint(1) DEFAULT NULL,
  `chinaEnglish` tinyint(1) DEFAULT NULL,
  `indexad` tinyint(1) DEFAULT NULL,
  `invoice` tinyint(1) DEFAULT NULL,
  `hidecarttotal` tinyint(1) DEFAULT NULL,
  `adtimes` varchar(10) DEFAULT NULL,
  `waiterreward` tinyint(1) DEFAULT NULL,
  `rewardmoney` varchar(10) DEFAULT NULL,
  `youmengappkey` varchar(50) DEFAULT NULL,
  `youmengchinnal` varchar(50) DEFAULT NULL,
  `bigdatainterface` varchar(100) DEFAULT NULL,
  `braceletgappkey` varchar(50) DEFAULT NULL,
  `braceletchinnal` varchar(50) DEFAULT NULL,
  `weixintype` varchar(8) DEFAULT NULL,
  `personweixinurl` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_b_padconfig
-- ----------------------------
INSERT INTO `t_b_padconfig` VALUES ('1', '0', '1', null, null, '1', '1', 'http://10.66.21.6:8080', '10.66.21.4:8081', '1', '1', '1', '0', '1', '1', '1', '60', '1', '5', '', '', 'http://10.66.21.4:8080/', null, null, null, null);
