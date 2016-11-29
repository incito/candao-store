-- 清机修改字段
ALTER TABLE `t_nodeclass`
ADD COLUMN `preferenceDetail`  varchar(256) NULL COMMENT '优惠明细，|隔开' AFTER `authorizer`;
