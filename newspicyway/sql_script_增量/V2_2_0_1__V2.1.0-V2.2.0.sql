-- status:启用：1，禁用：0 ；charges_status：计算实收：1，不计算实收：0；
UPDATE t_dictionary SET charges_status ='0'
WHERE TYPE='PAYWAY'
AND itemid NOT IN(0,1,5,8,13,17,18,30);

UPDATE t_dictionary SET STATUS = 1 , charges_status ='1'
WHERE TYPE='PAYWAY'
AND itemid IN(0,1,5,8,13,17,18,30);
-- 更新文案
UPDATE t_dictionary SET itemDesc='会员卡' where type='PAYWAY' and itemid='8';
UPDATE t_dictionary SET itemDesc='挂账支付' where type='PAYWAY' and itemid='13';
-- 新辣道配置优惠特殊优惠
INSERT IGNORE INTO `t_dictionary` (`dictid`,`itemid`,`itemDesc`,`itemSort`,`status`,`type`,`typename`,`item_value`)
VALUES 
('15074F08-29A9-5728-B156-CABEA64201E9','0','辛辣道特殊优惠',	'1','1','NEWSPICYWAYPRE','双拼立减','50781820d9e64c778cec80eecc40e238');

DROP TABLE IF EXISTS `t_payway_set`;
CREATE TABLE `t_payway_set` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(11) DEFAULT NULL COMMENT '支付方式ID',
  `status` tinyint(4) DEFAULT NULL COMMENT '是否显示 0隐藏 1显示',
  `sort` int(11) DEFAULT NULL COMMENT '排序字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
-- 清空t_device表 部分门店缺少t_device表，在这里补充
DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_group` varchar(32) DEFAULT NULL COMMENT '组号',
  `device_id` varchar(32) DEFAULT NULL COMMENT '组id',
  `device_type` varchar(255) DEFAULT NULL COMMENT '设备类型',
  `meid` varchar(32) DEFAULT NULL COMMENT 'meid',
  `ss_id` varchar(32) DEFAULT NULL COMMENT 'ssid',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `valid_flag` int(11) DEFAULT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `group_id` (`device_group`,`device_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `t_offline_msg`;
CREATE TABLE `t_offline_msg` (
  `id` varchar(64) NOT NULL,
  `msg_type` varchar(32) DEFAULT NULL COMMENT '消息类型',
  `content` text COMMENT '消息内容',
  `device_group` varchar(255) DEFAULT NULL COMMENT '设备组',
  `device_id` varchar(32) DEFAULT NULL COMMENT '设备id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `is_single` int(11) DEFAULT NULL COMMENT '是否互斥',
  PRIMARY KEY (`id`),
  KEY `group_id` (`device_group`,`device_id`),
  KEY `expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 修改会员地址
UPDATE `t_b_padconfig` SET vipcandaourl='http://chainstore.candaochina.com' WHERE id=1;