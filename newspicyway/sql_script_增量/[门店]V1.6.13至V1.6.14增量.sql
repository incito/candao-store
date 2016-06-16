DROP TABLE IF EXISTS `t_printer_state`;
CREATE TABLE `t_printer_state` (
  `ip` varchar(15) NOT NULL DEFAULT '' COMMENT '打印机IP 主键',
  `workstatus` smallint(6) DEFAULT '1' COMMENT '打印机状态',
  PRIMARY KEY (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
