DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `device_group` VARCHAR(32) DEFAULT NULL COMMENT '组号',
  `device_id` VARCHAR(32) DEFAULT NULL COMMENT '组id',
  `device_type` VARCHAR(255) DEFAULT NULL COMMENT '设备类型',
  `meid` VARCHAR(32) DEFAULT NULL COMMENT 'meid',
  `ss_id` VARCHAR(32) DEFAULT NULL COMMENT 'ssid',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `valid_flag` INT(11) DEFAULT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `group_id` (`device_group`,`device_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `t_offline_msg`;
CREATE TABLE `t_offline_msg` (
  `id` VARCHAR(64) NOT NULL,
  `msg_type` VARCHAR(32) DEFAULT NULL COMMENT '消息类型',
  `content` TEXT COMMENT '消息内容',
  `device_group` VARCHAR(255) DEFAULT NULL COMMENT '设备组',
  `device_id` VARCHAR(32) DEFAULT NULL COMMENT '设备id',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间',
  `is_single` INT(11) DEFAULT NULL COMMENT '是否互斥',
  PRIMARY KEY (`id`),
  KEY `group_id` (`device_group`,`device_id`),
  KEY `expire_time` (`expire_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;