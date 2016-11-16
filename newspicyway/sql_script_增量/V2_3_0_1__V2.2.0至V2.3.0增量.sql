SET NAMES 'utf8';

ALTER TABLE `t_table`
ADD COLUMN `chargeOn`  tinyint(1) NULL DEFAULT '0' COMMENT '服务费开关 0关闭 1打开' AFTER `modifytime`,
ADD COLUMN `chargeType` tinyint(1) NULL DEFAULT NULL COMMENT '服务费计算方式 0比例 1 固定 2 时长' AFTER `chargeOn`,
ADD COLUMN `chargeRateRule` tinyint(1) NULL DEFAULT NULL  COMMENT '0:实收 1:应收' AFTER `chargeType`,
ADD COLUMN `chargeRate` smallint(2) NULL DEFAULT NULL COMMENT '比例计算方式 比率' AFTER `chargeRateRule`,
ADD COLUMN `chargeTime` varchar(50) NULL DEFAULT NULL  COMMENT '时长计算方式 时长(分钟单位)' AFTER `chargeRate`,
ADD COLUMN `chargeAmount` decimal(10,2) NULL DEFAULT NULL  COMMENT '服务费金额' AFTER `chargeTime`;

INSERT INTO t_dictionary(dictid, itemid, itemDesc, itemSort, status, type, typename, begin_time, end_time, charges_status, member_price, price, date_type, item_value) VALUES
  ('674a7e69-a7de-11e6-a925-00ffb1cf25e1', '0', '比例', 0, 0, 'TABLECHARGE', '餐台服务费', '2016-11-11 15:13:54', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO t_dictionary(dictid, itemid, itemDesc, itemSort, status, type, typename, begin_time, end_time, charges_status, member_price, price, date_type, item_value) VALUES
  ('90bce8ed-a7de-11e6-a925-00ffb1cf25e1', '1', '固定', 1, 0, 'TABLECHARGE', '餐台服务费', '2016-11-11 15:15:03', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO t_dictionary(dictid, itemid, itemDesc, itemSort, status, type, typename, begin_time, end_time, charges_status, member_price, price, date_type, item_value) VALUES
  ('975b17c0-a7de-11e6-a925-00ffb1cf25e1', '2', '时长', 2, 0, 'TABLECHARGE', '餐台服务费', '2016-11-11 15:15:15', NULL, NULL, NULL, NULL, NULL, NULL);

DROP TABLE IF EXISTS t_service_charge;
CREATE TABLE `t_service_charge` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `orderid` varchar(50) DEFAULT NULL COMMENT '订单号',
  `chargeOn` tinyint(1) DEFAULT NULL COMMENT '当前单是否开启服务费 0关闭 1开启',
   `chargeType` tinyint(1) NULL DEFAULT NULL COMMENT '服务费计算方式' ,
   `chargeRateRule` tinyint(1) NULL DEFAULT NULL  COMMENT '0:实收 1:应收' ,
   `chargeRate` smallint(2) NULL DEFAULT NULL COMMENT '比例计算方式 比率' ,
   `chargeTime` varchar(50) NULL DEFAULT NULL  COMMENT '时长计算方式 时长(分钟单位)' ,
   `chargeAmount` decimal(10,2) NULL DEFAULT NULL  COMMENT '服务费金额' ,
    `isCustom` tinyint(1) DEFAULT NULL COMMENT '是否自定义服务费 0否 1是',
    `autho` varchar(20) DEFAULT NULL COMMENT '授权人',
    `ctime` datetime DEFAULT NULL COMMENT '创建时间',
    `mtime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `i_o` (`orderid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
UPDATE `t_b_function` SET NAME = '反结算/服务费' WHERE CODE = '030203';
CODE = '030203';

