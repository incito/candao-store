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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;