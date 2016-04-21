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

-- 描述：服务员销售统计表
-- 作者： 志芳; 
-- 时间：2016-04-21
DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_fwyxstjb$$
CREATE PROCEDURE p_report_fwyxstjb (IN pi_branchid int(11),
IN pi_ksrq datetime, -- 开始日期
IN pi_jsrq datetime, -- 结束日期
IN pi_fwyxm varchar(30), -- 服务员姓名
IN pi_smcp varchar(300), -- 菜品名称
IN pi_dqym int, -- 当前页码 第一次进入时从0开始
IN pi_myts int, -- 每页显示的条数
OUT po_errmsg varchar(100))
SQL SECURITY INVOKER
COMMENT '服务员销售统计表'
label_main:
BEGIN
  DECLARE v_waiter_name varchar(300);
  DECLARE v_dish_name varchar(30);
  DECLARE v_date_start datetime;
  DECLARE v_date_end datetime;
  DECLARE v_current_page int;
  DECLARE v_nums_page int;

  IF pi_branchid IS NULL THEN
    SELECT
      NULL;
    SET po_errmsg = '分店ID输入不能为空';
    LEAVE label_main;
  END IF;

  IF pi_myts < 1 THEN
    SELECT
      NULL;
    SET po_errmsg = '每页显示记录条数不能少于1';
    LEAVE label_main;
  END IF;

  IF pi_dqym < - 1 THEN
    SELECT
      NULL;
    SET po_errmsg = '当前页面只能输入大于-1的正整数';
    LEAVE label_main;
  END IF;

  SET @@max_heap_table_size = 1024 * 1024 * 500;
  SET @@tmp_table_size = 1024 * 1024 * 500;

  SET v_date_start = pi_ksrq;
  SET v_date_end = pi_jsrq;
  SET v_waiter_name = pi_fwyxm;
  SET v_dish_name = pi_smcp;
  SET v_current_page = pi_dqym;
  SET v_nums_page = pi_myts;

  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order (
    orderid varchar(50),
    userid varchar(50)
  ) ENGINE = MEMORY DEFAULT charset = utf8;

  INSERT INTO t_temp_order
    SELECT
      orderid,
      userid
    FROM t_order USE INDEX (IX_t_order_begintime)
    WHERE branchid = pi_branchid
    AND begintime BETWEEN v_date_start AND v_date_end
    AND orderstatus = 3;

  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);

  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
  CREATE TEMPORARY TABLE t_temp_order_detail (
    orderid varchar(50),
    dishid varchar(50),
    primarykey varchar(50),
    superkey varchar(50),
    dishnum varchar(50),
    dishtype int,
    orderprice DECIMAL(10, 2),
    dishunit varchar(100)
  ) ENGINE = MEMORY DEFAULT charset = utf8;

  INSERT INTO t_temp_order_detail
    SELECT
      tod.orderid,
      tod.dishid,
      tod.primarykey,
      tod.superkey,
      tod.dishnum,
      tod.dishtype,
      tod.orderprice,
      tod.dishunit
    FROM t_order_detail tod,
         t_temp_order too
    WHERE tod.orderid = too.orderid;

  #删除套餐中的明细
  DELETE
    FROM t_temp_order_detail
  WHERE dishtype = '2' AND primarykey <> superkey;

  #删除鱼锅名称
  DELETE
    FROM t_temp_order_detail
  WHERE orderprice IS NULL;

  SELECT
    too.orderid,
    too.userid,
    tbu.NAME,
    td.title,
    tod.dishunit,
    td.dishid,
    SUM(tod.dishnum) AS num,
    tod.dishtype
  FROM t_temp_order too,
       t_temp_order_detail tod,
       t_dish td,
       t_b_user tbu,
       t_b_employee tbe
  WHERE too.orderid = tod.orderid
  AND tbe.user_id = tbu.id
  AND tbe.job_number = too.userid
  AND tod.dishid = td.dishid
  AND td.title LIKE CONCAT('%', v_dish_name, '%')
  AND tbu.name LIKE CONCAT('%', v_waiter_name, '%')
  GROUP BY tbu.id,
           tod.dishid,
           tod.dishunit,
           tod.dishtype
  LIMIT v_current_page, v_nums_page;
END
$$

DELIMITER ;


-- 描述：品项销售明细表-子项 ，v_title 修改字段长度为300，title 修改字段长度为300
-- 作者： 志芳; 
-- 时间：2016-04-21

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_pxxsmxb_zhixiang$$
CREATE PROCEDURE p_report_pxxsmxb_zhixiang(IN  pi_branchid INT(11), 
                                           IN  pi_sb       SMALLINT, 
                                           IN  pi_ksrq     DATETIME, 
                                           IN  pi_jsrq     DATETIME, 
                                           IN  pi_pl       VARCHAR(50), 
                                           IN  pi_pxlx     INT, 
                                           OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '品项销售明细表'
label_main:
BEGIN 
  
  
  
  

  DECLARE v_total_count DOUBLE(13, 2) DEFAULT 0;
  DECLARE v_title       VARCHAR(300); 
  DECLARE v_dishNo      VARCHAR(50); 
  DECLARE v_price       DOUBLE(13, 2); 
  DECLARE v_unit        VARCHAR(50); 
  DECLARE v_number      DOUBLE(13, 2); 
  DECLARE v_share       DOUBLE(13, 2); 
  DECLARE v_sum_price   DOUBLE(13, 2); 
  DECLARE v_fetch_done  INT DEFAULT FALSE;
  DECLARE v_dishid      VARCHAR(50);
  DECLARE v_dishclass   VARCHAR(50);
  DECLARE v_dishtype    INT;
  DECLARE v_date_start  DATETIME;
  DECLARE v_date_end    DATETIME;
  DECLARE v_total_custnum_count    DOUBLE(13, 2); -- 来客总人数
  DECLARE v_total_shouldmount_count    DOUBLE(13, 2); -- 应收总额
  DECLARE v_canju_mount DOUBLE(13, 2); -- 餐具金额
  DECLARE cur_dish_detail CURSOR FOR SELECT a.dishid
                                          , a.dishunit
                                          , a.dishtype
                                          , b.columnid
                                          , ifnull(sum(a.dishnum), 0)
                                          , ifnull(max(a.orignalprice), 0)
																					, ifnull(sum(a.orignalprice*a.dishnum), 0)
                                     FROM
                                       t_temp_order_detail a, t_dish_dishtype b
                                     WHERE
                                       a.dishid = b.dishid
                                     GROUP BY
                                       a.dishid
                                     , a.dishunit
                                     , a.dishtype
                                     , b.columnid
                                     ORDER BY
                                       NULL;

  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; 
  END;


  
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SELECT NULL;
    GET DIAGNOSTICS CONDITION 1 po_errmsg = MESSAGE_TEXT;
  END;

  
  IF pi_sb IS NULL OR pi_ksrq IS NULL OR pi_jsrq IS NULL OR pi_pl IS NULL OR pi_pxlx IS NULL THEN
    SELECT NULL;
    SET po_errmsg = '传入参数不能为空';
    LEAVE label_main;
  END IF;

  IF pi_branchid IS NULL THEN
    SELECT NULL;
    SET po_errmsg = '分店ID输入不能为空';
    LEAVE label_main;
  END IF;

  SET @@max_heap_table_size = 1024 * 1024 * 400;
  SET @@tmp_table_size = 1024 * 1024 * 400;

  
  SET v_date_start = pi_ksrq; 
  SET v_date_end = pi_jsrq; 

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50),
    custnum INT(11),
    PRIMARY KEY (orderid)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_sb > -1 THEN
    INSERT INTO t_temp_order
    SELECT orderid,custnum
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      branchid = pi_branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND shiftid = pi_sb
      AND orderstatus = 3;
  ELSE
    INSERT INTO t_temp_order
    SELECT orderid,custnum
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      branchid = pi_branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND orderstatus = 3;
  END IF;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
  CREATE TEMPORARY TABLE t_temp_order_detail
  (
    orderid VARCHAR(40),
    dishnum DOUBLE(13, 2),
    dishid VARCHAR(40),
    dishtype INT,
    dishunit VARCHAR(10),
    orignalprice DOUBLE(13, 2),
    ispot TINYINT,
    parentkey VARCHAR(40),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_order_detail
  SELECT a.orderid
       , a.dishnum
       , a.dishid
       , a.dishtype
       , a.dishunit
       , a.orignalprice
       , a.ispot
       , a.parentkey
       , a.childdishtype
       , a.primarykey
       , a.superkey
  FROM
    t_temp_order b, t_order_detail a
  WHERE
    b.orderid = a.orderid
    AND a.orignalprice > 0;

   -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

  CREATE INDEX ix_t_tmp_order_detail_dishid ON t_temp_order_detail (dishid);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_parentkey;
  CREATE TEMPORARY TABLE t_temp_parentkey
  (
    parentkey VARCHAR(40)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_parentkey
  SELECT DISTINCT parentkey
  FROM
    t_temp_order_detail
  WHERE
    ispot = 1
    AND dishnum = 0;

  
  DELETE
  FROM
    t_temp_order_detail
  WHERE
    ispot = 1
    AND dishnum = 0;

  
  UPDATE t_temp_order_detail a, t_temp_parentkey b
  SET
    a.dishtype = 0
  WHERE
    a.parentkey = b.parentkey;

  

  
  UPDATE t_temp_order_detail a, t_dish b
  SET
    a.dishtype = b.dishtype
  WHERE
    a.dishid = b.dishid
    AND a.dishtype IS NULL;

  
  SELECT ifnull(sum(dishnum), 0)
  INTO
    v_total_count
  FROM
    t_temp_order_detail;

  IF v_total_count <= 0 THEN
    SELECT NULL;
    SET po_errmsg = '数据为空，无查询结果';
    LEAVE label_main;
  END IF;

  SELECT ifnull(sum(custnum), 0)
  INTO
    v_total_custnum_count
  FROM
    t_temp_order;

  IF v_total_custnum_count <= 0 THEN
    SELECT NULL;
    SET po_errmsg = '数据为空，无查询结果';
    LEAVE label_main;
  END IF;

  SELECT ifnull(sum(dishnum*orignalprice), 0)
  INTO
    v_total_shouldmount_count
  FROM
    t_temp_order_detail;

  IF v_total_shouldmount_count <= 0 THEN
    SELECT NULL;
    SET po_errmsg = '数据为空，无查询结果';
    LEAVE label_main;
  END IF;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    columnid VARCHAR(50), 
    dishtype INT, 
    title VARCHAR(300), 
    dishNo VARCHAR(50), 
    price DOUBLE(13, 2), 
    unit VARCHAR(50), 
    number DOUBLE(13, 2), 
    thousandstimes DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
    turnover DOUBLE(13, 2),
    share DOUBLE(13, 2) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;


  
  OPEN cur_dish_detail;

read_loop:
  LOOP
    FETCH cur_dish_detail INTO v_dishid, v_unit, v_dishtype, v_dishclass, v_number, v_price,v_sum_price;

    IF v_fetch_done THEN
      LEAVE read_loop;
    END IF;

    
    SELECT dishNo
         , title
    INTO
      v_dishNo, v_title
    FROM
      t_dish
    WHERE
      dishid = v_dishid
    LIMIT
      1;

    INSERT INTO t_temp_res VALUES (v_dishclass, v_dishtype, v_title, v_dishNo, v_price, v_unit, v_number,round(v_number / v_total_custnum_count * 1000, 2),v_sum_price,v_sum_price/v_total_shouldmount_count*100, round(v_number / v_total_count * 100, 2));
  END LOOP;
  COMMIT;
  
  CLOSE cur_dish_detail;

  
  IF pi_pl = -1 OR pi_pl = 'DISHES_98' THEN
    SELECT sum(dishnum)
         , ifnull(max(orignalprice), 0),ifnull(sum(dishnum*orignalprice), 0)
    INTO
      @cnt, @price,@sumprice
    FROM
      t_temp_order_detail
    WHERE
      dishid = 'DISHES_98';

    SELECT ifnull(title, '餐具')
         , ifnull(dishno, '')
    INTO
      @title, @dishno
    FROM
      t_dish
    WHERE
      dishid = 'DISHES_98'
    LIMIT
      1;

    IF @cnt > 0 THEN
      INSERT INTO t_temp_res VALUES ('DISHES_98', 0, @title, @dishno, @price, '份', @cnt,round(@cnt / v_total_custnum_count * 1000, 2),@sumprice,@sumprice/v_total_shouldmount_count*100, round(@cnt / v_total_count * 100, 2));
    END IF;
  END IF;


  
  IF pi_pl != '-1' AND pi_pxlx != -1 THEN
    SELECT title
         , dishNo
         , price
         , unit
         , number
         , share
         , thousandstimes
         , orignalprice
         , turnover
    FROM
      t_temp_res
    WHERE
      columnid = pi_pl
      AND dishtype = pi_pxlx;

  ELSEIF pi_pl = '-1' AND pi_pxlx != -1 THEN
    SELECT title
         , dishNo
         , price
         , unit
         , number
         , share
         , thousandstimes
         , orignalprice
         , turnover
    FROM
      t_temp_res
    WHERE
      dishtype = pi_pxlx;

  ELSEIF pi_pl != '-1' AND pi_pxlx = -1 THEN
    SELECT title
         , dishNo
         , price
         , unit
         , number
         , share
         , thousandstimes
         , orignalprice
         , turnover
    FROM
      t_temp_res
    WHERE
      columnid = pi_pl;

  ELSE
    SELECT title
         , dishNo
         , price
         , unit
         , number
         , share
         , thousandstimes
         , orignalprice
         , turnover
    FROM
      t_temp_res;

  END IF;

END$$
DELIMITER ;

