-- status:启用：1，禁用：0 ；charges_status：计算实收：1，不计算实收：0；
UPDATE t_dictionary SET STATUS = 0 , charges_status ='0'
WHERE TYPE='PAYWAY'
AND itemid NOT IN(0,1,5,8,13,17,18,30);

UPDATE t_dictionary SET STATUS = 1 , charges_status ='1'
WHERE TYPE='PAYWAY'
AND itemid IN(0,1,5,8,13,17,18,30);

CREATE TABLE `t_payway_set` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(11) DEFAULT NULL COMMENT '支付方式ID',
  `status` tinyint(4) DEFAULT NULL COMMENT '是否显示 0隐藏 1显示',
  `sort` int(11) DEFAULT NULL COMMENT '排序字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
