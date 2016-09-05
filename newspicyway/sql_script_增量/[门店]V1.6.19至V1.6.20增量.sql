-- ----------------------------
-- Table structure for t_order_detail_preferential
-- ----------------------------
DROP TABLE IF EXISTS `t_order_detail_preferential`;
CREATE TABLE `t_order_detail_preferential` (
  `id` varchar(50) NOT NULL COMMENT 'UUID主键由门店后台生成',
  `orderid` varchar(50) NOT NULL COMMENT '订单号',
  `dishid` varchar(50) DEFAULT NULL COMMENT '菜品编号',
  `dishNum` varchar(20) NOT NULL COMMENT '菜品数量',
  `preferential` varchar(50) NOT NULL COMMENT '优惠表主键',
  `deAmount` decimal(30,7) DEFAULT '1.0000000' COMMENT '使用优惠卷优惠金额',
  `discount` decimal(6,4) DEFAULT '0.0000' COMMENT '折扣信息',
  `isCustom` tinyint(1) DEFAULT '0' COMMENT '是否是自己设定（服务员自定义）',
  `isGroup` tinyint(1) DEFAULT '0' COMMENT '是否是全局使用优惠',
  `isUse` tinyint(1) DEFAULT '1' COMMENT '当前菜单对应的优惠卷是否使用中（1使用0菜品被删除）',
  `toalFreeAmount` decimal(10,4) DEFAULT NULL COMMENT '优免总金额',
  `toalDebitAmount` decimal(10,4) DEFAULT NULL COMMENT '挂账总金额',
  `insertime` timestamp NULL DEFAULT NULL COMMENT '当前插入时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table t_order MODIFY `gzcode`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL;
alter table t_order_history MODIFY `gzcode`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL;

ALTER TABLE `t_basicdata`
MODIFY COLUMN `itemid`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '编号' AFTER `id`;
ALTER TABLE `t_dish`
MODIFY COLUMN `cantakeout`  varchar(2) NOT NULL DEFAULT b'0' AFTER `py`;

alter table `t_order`  ADD `isfree` TINYINT(1)   DEFAULT 0 COMMENT '是否餐具需要收费0不收费1收费';
alter table `t_order` ADD `num_of_meals` INT(2)  DEFAULT 0 COMMENT '用餐人数 pad专用';
 
