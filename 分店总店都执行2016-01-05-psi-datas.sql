
INSERT INTO `t_b_function` VALUES ('2d4f4a66-b2c2-11e5-8ac5-00ff59e0a7b2', '进销存', '0315', '1', 2, '575dcbd4-28ce-4f53-b01a-e7e96be38ed8', 0, NULL, NULL, NULL, '2016-1-4 17:32:36', NULL, '03', '所有门店', 38);
INSERT INTO `t_b_function` VALUES ('9dfab887-b2c2-11e5-8ac5-00ff59e0a7b2', '基本信息管理', '031501', '1', 3, '2d4f4a66-b2c2-11e5-8ac5-00ff59e0a7b2', 1, NULL, NULL, NULL, '2016-1-4 17:33:30', NULL, '03', '所有门店', 37);
INSERT INTO `t_b_function` VALUES ('ea4a1d29-b2c5-11e5-8ac5-00ff59e0a7b2', '入库管理', '031502', '1', 3, '2d4f4a66-b2c2-11e5-8ac5-00ff59e0a7b2', 1, NULL, NULL, NULL, '2016-1-4 17:34:40', NULL, '03', '所有门店', 63);
INSERT INTO `t_b_function` VALUES ('10781aad-b2c6-11e5-8ac5-00ff59e0a7b2', '出库管理', '031503', '1', 3, '2d4f4a66-b2c2-11e5-8ac5-00ff59e0a7b2', 1, NULL, NULL, NULL, '2016-1-4 17:35:43', NULL, '03', '所有门店', 64);
INSERT INTO `t_b_function` VALUES ('2c59880e-b2c6-11e5-8ac5-00ff59e0a7b2', '库存一览', '031504', '1', 3, '2d4f4a66-b2c2-11e5-8ac5-00ff59e0a7b2', 1, NULL, NULL, NULL, '2016-1-4 17:36:36', NULL, '03', '所有门店', 65);
INSERT INTO `t_b_function` VALUES ('47d8258a-b2c6-11e5-8ac5-00ff59e0a7b2', '库存盘点', '031505', '1', 3, '2d4f4a66-b2c2-11e5-8ac5-00ff59e0a7b2', 1, NULL, NULL, NULL, '2016-1-4 17:37:15', NULL, '03', '所有门店', 66);
INSERT INTO `t_b_function` VALUES ('5f5f8848-b2c6-11e5-8ac5-00ff59e0a7b2', '销售管理', '031506', '1', 3, '2d4f4a66-b2c2-11e5-8ac5-00ff59e0a7b2', 1, NULL, NULL, NULL, '2016-1-4 17:39:04', NULL, '03', '所有门店', 67);
INSERT INTO `t_b_function` VALUES ('2944211e-b2da-11e5-8ac5-00ff59e0a7b2', '原料出入库明细表', '031507', '1', 3, '2d4f4a66-b2c2-11e5-8ac5-00ff59e0a7b2', 1, NULL, NULL, NULL, '2016-1-4 19:59:46', NULL, '03', '所有门店', 68);

INSERT INTO `t_b_url` VALUES ('869b858a-b2db-11e5-8ac5-00ff59e0a7b2', '报表查看_原料出入库明细表', 'psi/chart/main', NULL, NULL, '2016-1-4 20:09:07', NULL);
INSERT INTO `t_b_url` VALUES ('c00e92df-b2da-11e5-8ac5-00ff59e0a7b2', '进销存_入库管理', 'psi/basic/inStorage', NULL, NULL, '2016-1-4 20:04:45', NULL);
INSERT INTO `t_b_url` VALUES ('24cc66a9-b2db-11e5-8ac5-00ff59e0a7b2', '进销存_出库管理', 'psi/basic/outStorage', NULL, NULL, '2016-1-4 20:06:23', NULL);
INSERT INTO `t_b_url` VALUES ('fd01115f-b2da-11e5-8ac5-00ff59e0a7b2', '进销存_基本信息管理', 'psi/basic/container', NULL, NULL, '2016-1-4 20:05:49', NULL);
INSERT INTO `t_b_url` VALUES ('6f4890da-b2db-11e5-8ac5-00ff59e0a7b2', '进销存_库存一览', 'psi/basic/inventory', NULL, NULL, '2016-1-4 20:06:48', NULL);
INSERT INTO `t_b_url` VALUES ('669c400c-b2db-11e5-8ac5-00ff59e0a7b2', '进销存_库存盘点', 'psi/basic/inventoryBill', NULL, NULL, '2016-1-4 20:07:19', NULL);
INSERT INTO `t_b_url` VALUES ('5fd495e0-b2db-11e5-8ac5-00ff59e0a7b2', '进销存_销售管理', 'psi/basic/order', NULL, NULL, '2016-1-4 20:08:05', NULL);


INSERT INTO `t_b_function_url` VALUES ('54aa4c0d-b2dc-11e5-8ac5-00ff59e0a7b2', 'ea4a1d29-b2c5-11e5-8ac5-00ff59e0a7b2', 'c00e92df-b2da-11e5-8ac5-00ff59e0a7b2');
INSERT INTO `t_b_function_url` VALUES ('55f91646-b358-11e5-b2d9-002522370b01', '9dfab887-b2c2-11e5-8ac5-00ff59e0a7b2', 'fd01115f-b2da-11e5-8ac5-00ff59e0a7b2');
INSERT INTO `t_b_function_url` VALUES ('601f9a5c-b2dc-11e5-8ac5-00ff59e0a7b2', '10781aad-b2c6-11e5-8ac5-00ff59e0a7b2', '24cc66a9-b2db-11e5-8ac5-00ff59e0a7b2');
INSERT INTO `t_b_function_url` VALUES ('663ae26b-b2dc-11e5-8ac5-00ff59e0a7b2', '2c59880e-b2c6-11e5-8ac5-00ff59e0a7b2', '6f4890da-b2db-11e5-8ac5-00ff59e0a7b2');
INSERT INTO `t_b_function_url` VALUES ('6e8012b2-b2dc-11e5-8ac5-00ff59e0a7b2', '5f5f8848-b2c6-11e5-8ac5-00ff59e0a7b2', '5fd495e0-b2db-11e5-8ac5-00ff59e0a7b2');
INSERT INTO `t_b_function_url` VALUES ('74673403-b2dc-11e5-8ac5-00ff59e0a7b2', '2944211e-b2da-11e5-8ac5-00ff59e0a7b2', '869b858a-b2db-11e5-8ac5-00ff59e0a7b2');
INSERT INTO `t_b_function_url` VALUES ('7a723946-b2dc-11e5-8ac5-00ff59e0a7b2', '47d8258a-b2c6-11e5-8ac5-00ff59e0a7b2', '669c400c-b2db-11e5-8ac5-00ff59e0a7b2');
