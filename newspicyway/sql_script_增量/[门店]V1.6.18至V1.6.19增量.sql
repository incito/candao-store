alter table t_b_weixin add COLUMN weixintype int;
alter table t_b_weixin add COLUMN personweixinurl varchar(100);
alter table t_b_weixin add COLUMN status int;

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
  `youmengappkey` varchar(150) DEFAULT NULL,
  `youmengchinnal` varchar(150) DEFAULT NULL,
  `bigdatainterface` varchar(150) DEFAULT NULL,
  `braceletgappkey` varchar(150) DEFAULT NULL,
  `braceletchinnal` varchar(150) DEFAULT NULL,
  `weixintype` varchar(8) DEFAULT NULL,
  `personweixinurl` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_b_padconfig
-- ----------------------------
INSERT INTO `t_b_padconfig` VALUES ('1', '0', '1', null,null, '1', '1', 'http://member.candaochina.com', null, '1', '1', '1', '1', '1', '1', '1', '60', '1', '5', null, null, null, null, null, 1, null);
