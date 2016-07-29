DROP TABLE IF EXISTS `t_order_detail_preferential`;
CREATE TABLE `t_order_detail_preferential` (
  `id` varchar(50) NOT NULL COMMENT 'UUID主键由门店后台生成',
  `orderid` varchar(50) NOT NULL COMMENT '订单号',
  `dishid` varchar(50) DEFAULT NULL COMMENT '菜品编号',
  `dishNum` varchar(20) NOT NULL COMMENT '菜品数量',
  `preferential` varchar(50) NOT NULL COMMENT '优惠表主键',
  `deAmount` decimal(30,7) DEFAULT '1.0000000' COMMENT '使用优惠卷张数',
  `discount` decimal(6,4) DEFAULT '0.0000' COMMENT '折扣信息',
  `isCustom` tinyint(1) DEFAULT '0' COMMENT '是否是自己设定',
  `isGroup` tinyint(1) DEFAULT '0' COMMENT '是否是全局使用优惠',
  `isUse` tinyint(1) DEFAULT '1' COMMENT '当前菜单对应的优惠卷是否使用中（1使用0菜品被删除）',
  `insertime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '当前插入时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
