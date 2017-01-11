SET NAMES 'utf8';
CREATE TABLE `t_member_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `system` tinyint(4) DEFAULT NULL COMMENT '会员系统标示',
  `ctime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  `operator_id` varchar(32) DEFAULT NULL COMMENT '操作员ID',
  `operator_name` varchar(32) DEFAULT NULL COMMENT '操作员名字',
  `operate_type` tinyint(4) DEFAULT NULL COMMENT '操作ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '真实金额',
  `added_amount` decimal(10,2) DEFAULT NULL COMMENT '其他虚拟金额',
  `payway` tinyint(4) DEFAULT NULL COMMENT '支付方式',
  `serial` varchar(32) DEFAULT NULL COMMENT '序列号',
  `cardno` varchar(32) DEFAULT NULL COMMENT '卡号',
  PRIMARY KEY (`id`),
  KEY `i_ctime` (`ctime`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='会员消费记录';

