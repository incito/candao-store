SET NAMES 'utf8';

ALTER TABLE `t_order_detail_preferential`
ADD INDEX `i_orderid` (`orderid`) ;

INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),1,'t_table',1,1,'SYNTABLES','门店上传配置','t_table');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),2,'t_tablearea',2,1,'SYNTABLES','门店上传配置','t_tablearea');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),3,'t_order',3,1,'SYNTABLES','门店上传配置','t_order:begintime');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),4,'t_order_detail',4,1,'SYNTABLES','门店上传配置','t_order_detail:begintime');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),5,'t_order_detail_discard',5,1,'SYNTABLES','门店上传配置','t_order_detail_discard:begintime');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),6,'t_settlement',6,1,'SYNTABLES','门店上传配置','t_settlement:inserttime');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),7,'t_settlement_history',7,1,'SYNTABLES','门店上传配置','t_settlement_history:inserttime');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),8,'t_settlement_detail',8,1,'SYNTABLES','门店上传配置','t_settlement_detail:inserttime');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),9,'t_settlement_detail_history',9,1,'SYNTABLES','门店上传配置','t_settlement_detail_history:inserttime');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),10,'t_biz_log',10,1,'SYNTABLES','门店上传配置','t_biz_log:inserttime');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),11,'t_order_member',11,1,'SYNTABLES','门店上传配置','t_order_member:ordertime');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),12,'t_branch_biz_log',12,1,'SYNTABLES','门店上传配置','t_branch_biz_log:inserttime');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),13,'t_order_history',13,1,'SYNTABLES','门店上传配置','t_order_history:begintime');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),14,'t_order_detail_history',14,1,'SYNTABLES','门店上传配置','t_order_detail_history:begintime');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),15,'t_nodeclass',15,1,'SYNTABLES','门店上传配置','t_nodeclass:pritertime');
INSERT into t_dictionary(dictid,itemid,itemDesc,itemSort,status,type,typename,item_value)VALUES (REPLACE(UUID(),'-',''),16,'t_service_charge',16,1,'SYNTABLES','门店上传配置','t_service_charge:ctime');
-- PAD注册会员开关
ALTER TABLE `t_b_padconfig`
ADD COLUMN `registerswitch`  tinyint(1) NULL DEFAULT 0 COMMENT 'PAD会员注册开关 0 关闭 1打开' AFTER `personweixinurl`;