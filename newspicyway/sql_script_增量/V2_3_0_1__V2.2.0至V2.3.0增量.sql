ALTER TABLE `t_table`
ADD COLUMN `chargeOn`  char(1) NULL DEFAULT '0' COMMENT '服务费开关 0关闭 1打开' AFTER `modifytime`,
ADD COLUMN `chargeType`  smallint NULL COMMENT '服务费计算方式 1比例 2固定 3时长' AFTER `chargeOn`,
ADD COLUMN `chargeRule`  varchar(200) NULL COMMENT '服务费详细规则，业务自己控制存储格式' AFTER `chargeType`;

DROP TABLE IF EXISTS t_service_charge;
CREATE TABLE `t_service_charge` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `orderid` varchar(50) DEFAULT NULL COMMENT '订单号',
  `chargeOn` char(1) DEFAULT NULL COMMENT '当前单是否开启服务费 0关闭 1开启',
  `chargeRule` varchar(200) DEFAULT NULL COMMENT '服务费规则',
  `charge` decimal(10,2) DEFAULT NULL COMMENT '服务费',
  `isCustom` char(1) DEFAULT NULL COMMENT '是否自定义服务费 0否 1是',
  `autho` varchar(20) DEFAULT NULL COMMENT '授权人',
  `ctime` datetime DEFAULT NULL COMMENT '创建时间',
  `mtime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `i_o` (`orderid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

