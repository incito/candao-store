ALTER TABLE `t_basicdata`
MODIFY COLUMN `itemid`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '编号' AFTER `id`;
ALTER TABLE `t_dish`
MODIFY COLUMN `cantakeout`  varchar(2) NOT NULL DEFAULT b'0' AFTER `py`;