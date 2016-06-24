DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `device_group` VARCHAR(32) DEFAULT NULL COMMENT '���',
  `device_id` VARCHAR(32) DEFAULT NULL COMMENT '��id',
  `device_type` VARCHAR(255) DEFAULT NULL COMMENT '�豸����',
  `meid` VARCHAR(32) DEFAULT NULL COMMENT 'meid',
  `ss_id` VARCHAR(32) DEFAULT NULL COMMENT 'ssid',
  `create_time` DATETIME DEFAULT NULL COMMENT '����ʱ��',
  `update_time` DATETIME DEFAULT NULL COMMENT '����ʱ��',
  `valid_flag` INT(11) DEFAULT NULL COMMENT '�Ƿ���Ч',
  PRIMARY KEY (`id`),
  KEY `group_id` (`device_group`,`device_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `t_offline_msg`;
CREATE TABLE `t_offline_msg` (
  `id` VARCHAR(64) NOT NULL,
  `msg_type` VARCHAR(32) DEFAULT NULL COMMENT '��Ϣ����',
  `content` TEXT COMMENT '��Ϣ����',
  `device_group` VARCHAR(255) DEFAULT NULL COMMENT '�豸��',
  `device_id` VARCHAR(32) DEFAULT NULL COMMENT '�豸id',
  `create_time` DATETIME DEFAULT NULL COMMENT '����ʱ��',
  `expire_time` DATETIME DEFAULT NULL COMMENT '����ʱ��',
  `is_single` INT(11) DEFAULT NULL COMMENT '�Ƿ񻥳�',
  PRIMARY KEY (`id`),
  KEY `group_id` (`device_group`,`device_id`),
  KEY `expire_time` (`expire_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;