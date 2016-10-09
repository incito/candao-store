alter table `t_printobj` ADD `custnum` varchar(50)  DEFAULT NULL COMMENT '用餐人数';
alter table `t_order_detail_preferential` ADD `unit` VARCHAR(50) DEFAULT NULL COMMENT '菜品单位'; 
ALTER TABLE t_dish_dishtype ADD UNIQUE `unique` (`columnid`, `dishid`) USING BTREE;

-- ----------------------------
-- Table structure for `t_printer_device`
-- ----------------------------
DROP TABLE IF EXISTS `t_printer_device`;
CREATE TABLE `t_printer_device` (
  `deviceid` varchar(50) NOT NULL,
  `devicecode` varchar(50) NOT NULL COMMENT '设备编码 唯一',
  `devicename` varchar(50) DEFAULT NULL COMMENT '设备名称',
  `devicearea` varchar(50) DEFAULT NULL COMMENT '设备所属区域',
  `devicestatus` int(11) DEFAULT NULL COMMENT '设备状态',
  `devicetype` int(11) DEFAULT NULL COMMENT '设备类型',
  `deviceip` varchar(50) DEFAULT NULL COMMENT '设备IP',
  `inserttime` datetime DEFAULT NULL COMMENT '插入时间',
  `modifiedtime` datetime DEFAULT NULL COMMENT '修改时间',
  `deviceowner` varchar(50) DEFAULT NULL COMMENT '设备所属',
  `devicegroup` varchar(50) DEFAULT NULL COMMENT '设备所属组',
  PRIMARY KEY (`deviceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;

-- ----------------------------
-- Table structure for `t_printer_deviceprinter`
-- ----------------------------
DROP TABLE IF EXISTS `t_printer_deviceprinter`;
CREATE TABLE `t_printer_deviceprinter` (
  `id` varchar(50) NOT NULL COMMENT '设备主键',
  `devicecode` varchar(50) DEFAULT NULL COMMENT '设备编码 唯一',
  `devicename` varchar(50) DEFAULT NULL COMMENT '设备名称',
  `printerid` varchar(50) DEFAULT NULL COMMENT '打印机IP',
  `printername` varchar(50) DEFAULT NULL COMMENT '打印机名称',
  `printerip` varchar(50) DEFAULT NULL COMMENT '打印机ip',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;

DROP TABLE IF EXISTS `t_dish_sellout`;
CREATE TABLE `t_dish_sellout` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `dishid` varchar(50) DEFAULT NULL,
  `dishunit` varchar(255) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
