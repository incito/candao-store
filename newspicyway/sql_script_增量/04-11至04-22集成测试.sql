--
DROP TABLE IF EXISTS `t_b_tip`;

CREATE TABLE `t_b_tip` (
  `waiter_number` varchar(50) NOT NULL COMMENT '服务员编号',
  `receivables` int(11) NOT NULL COMMENT '应收金额',
  `paid` int(11) DEFAULT NULL COMMENT '实收金额',
  `orderid` varchar(50) NOT NULL COMMENT '订单编号',
  `insertime` datetime NOT NULL COMMENT '创建时间',
  `branchid` int(11) NOT NULL COMMENT '门店编号',
  PRIMARY KEY (`orderid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

