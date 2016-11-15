-- status:启用：1，禁用：0 ；charges_status：计算实收：1，不计算实收：0；
UPDATE t_dictionary SET STATUS = 0 , charges_status ='0'
WHERE TYPE='PAYWAY'
AND itemid NOT IN(0,1,5,8,13,17,18,30);

UPDATE t_dictionary SET STATUS = 1 , charges_status ='1'
WHERE TYPE='PAYWAY'
AND itemid IN(0,1,5,8,13,17,18,30);
-- 更新文案
UPDATE t_dictionary SET itemDesc='会员卡' where type='PAYWAY' and itemid='8';
UPDATE t_dictionary SET itemDesc='挂账支付' where type='PAYWAY' and itemid='13';
--新辣道配置优惠特殊优惠
INSERT INTO `t_dictionary` (`dictid`,`itemid`,`itemDesc`,`itemSort`,`status`,`type`,`typename`,`item_value`)
VALUES 
('15074F08-29A9-5728-B156-CABEA64201E9','0','辛辣道特殊优惠',	'1','1','NEWSPICYWAYPRE','双拼立减','50781820d9e64c778cec80eecc40e238');

CREATE TABLE `t_payway_set` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(11) DEFAULT NULL COMMENT '支付方式ID',
  `status` tinyint(4) DEFAULT NULL COMMENT '是否显示 0隐藏 1显示',
  `sort` int(11) DEFAULT NULL COMMENT '排序字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
