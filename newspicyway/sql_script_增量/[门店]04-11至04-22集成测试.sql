-- 文件描述：本文件为本次集成测试所需增量sql脚本 

-- 描述：小费 ;
-- 作者： 李宗仁; 
-- 时间：2016-04-21

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


-- 描述：重复下单 ，修正重复下单需要  执行   call p_get_primarykeys();
-- 作者： 肖凯; 
-- 时间：2016-04-21
ALTER TABLE t_order_detail ADD INDEX idx_orderprice (orderprice); 
ALTER TABLE t_order_detail ADD INDEX idx_dishid (dishid); 
alter table t_order_detail add UNIQUE key(primarykey);

SET NAMES 'utf8';

DELIMITER $$
DROP PROCEDURE IF EXISTS p_update_primarykey$$
CREATE PROCEDURE p_update_primarykey(in primarykey varchar(100))
COMMENT '更新重复的primarykey'

BEGIN

DECLARE done int DEFAULT 0;

DECLARE v_detail_id varchar(100);

DECLARE  temp_uuid varchar(100);

DECLARE cur_detailids CURSOR FOR SELECT orderdetailid from t_order_detail d where  d.primarykey=primarykey;

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;


  OPEN cur_detailids;

  REPEAT

    FETCH cur_detailids INTO v_detail_id;
			
				IF done <> 1 THEN
								SELECT  UUID() into temp_uuid;
								UPDATE t_order_detail set primarykey=temp_uuid where orderdetailid=v_detail_id;
				 END IF;
									


     UNTIL done = 1
  END REPEAT;

END
$$

DROP PROCEDURE IF EXISTS p_get_primarykeys$$
CREATE PROCEDURE p_get_primarykeys()
COMMENT '获取重复的primarykey'

BEGIN
DECLARE done int DEFAULT 0;
DECLARE  v_total varchar(100);
DECLARE  v_primarykey varchar(100);

###定义一个游标表示所有重复的primarykey
DECLARE cur_repat_keys CURSOR FOR SELECT count(1) total,t.primarykey
			                          from t_order_detail t
	                                          GROUP BY t.primarykey
					          HAVING total>1
			                          ORDER BY total desc;
##先更新以前没有用到primarykey字段的数据
update t_order_detail set primarykey=UUID() where primarykey='';
OPEN cur_repat_keys;

  REPEAT

    FETCH cur_repat_keys INTO v_total,v_primarykey;
				IF done <> 1 THEN

				call p_update_primarykey(v_primarykey);
				END IF;

  UNTIL done = 1
  END REPEAT;

END
$$


