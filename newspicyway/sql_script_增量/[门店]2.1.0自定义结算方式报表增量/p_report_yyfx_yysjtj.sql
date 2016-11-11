DELIMITER $$


DROP PROCEDURE IF EXISTS `p_report_yyfx_yysjtj`$$

CREATE PROCEDURE `p_report_yyfx_yysjtj`(IN  pi_branchid INT(11), 
                                      IN  pi_xslx     SMALLINT, 
                                      IN  pi_ksrq     DATETIME, 
                                      IN  pi_jsrq     DATETIME, 
                                      OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '营业分析_营业数据统计'
label_main:
BEGIN
  -- 返回字段说明如下：
  -- 时间，应收总额，实收总额， 折扣总额
  --
  -- 返回数据举例（多行数据）：
  -- 2015-05  3636.00  3630.00  6.00
  -- 2015-06  4636.00  4630.00  6.00

  DECLARE v_date_start        DATETIME;
  DECLARE v_date_end          DATETIME;
  DECLARE v_date_interval     DATETIME; #时间间隔
  DECLARE v_loop_num          INT DEFAULT 0; #根据开始结束时间和显示类型，来设置循环次数
  DECLARE v_statistictime     VARCHAR(15); #统计日期
  DECLARE v_shouldamount      DOUBLE(13, 2); #应收
  DECLARE v_paidinamount      DOUBLE(13, 2); #实收(含虚增)
  DECLARE v_inflated          DOUBLE(13, 2); #虚增
  DECLARE v_person_con        DOUBLE(13, 2) DEFAULT 0; #人均
  DECLARE v_table_num         INT DEFAULT 0; #桌数
  DECLARE v_sa_settlementnum  INT DEFAULT 0; #总人数

  -- 异常处理模块，出现异常返回null
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SELECT NULL;
    GET DIAGNOSTICS CONDITION 1 po_errmsg = MESSAGE_TEXT;
  END;

  IF pi_branchid IS NULL THEN
    SELECT NULL;
    SET po_errmsg = '分店ID输入不能为空';
    LEAVE label_main;
  END IF;

  #设置临时内存表
  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;


  #设置循环次数,处理开始结算时间
  IF pi_xslx = 0 THEN
    SET v_statistictime = DATE_FORMAT(pi_ksrq, '%Y-%m-%d');
    SET v_date_start = STR_TO_DATE(CONCAT(v_statistictime, '00:00:00'), '%Y-%m-%d %H:%i:%s');
    SET v_date_end = STR_TO_DATE(CONCAT(DATE_FORMAT(pi_jsrq, '%Y-%m-%d'), '23:59:59'), '%Y-%m-%d %H:%i:%s');
    SET v_date_interval = DATE_SUB(DATE_ADD(v_date_start, INTERVAL 1 DAY), INTERVAL 1 SECOND);
    SET v_loop_num = TIMESTAMPDIFF(DAY, v_date_start, v_date_end) + 1;
  ELSEIF pi_xslx = 1 THEN
    SET v_statistictime = DATE_FORMAT(pi_ksrq, '%Y-%m');
    SET v_date_start = STR_TO_DATE(CONCAT(v_statistictime, '-01 00:00:00'), '%Y-%m-%d %H:%i:%s');
    SET v_date_interval = DATE_SUB(DATE_ADD(v_date_start, INTERVAL 1 MONTH), INTERVAL 1 SECOND);
    SET v_date_end = DATE_SUB(DATE_ADD(STR_TO_DATE(CONCAT(DATE_FORMAT(pi_jsrq, '%Y-%m'), '-01 00:00:00'), '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH), INTERVAL 1 SECOND);
    SET v_loop_num = TIMESTAMPDIFF(MONTH, v_date_start, v_date_end) + 1;
  ELSE
    SELECT NULL;
    LEAVE label_main;
  END IF;

  #创建订单临时内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50),
		womanNum TINYINT UNSIGNED,
    childNum TINYINT UNSIGNED,
    mannum TINYINT UNSIGNED,
    ordertype TINYINT,
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_order
  SELECT orderid
       , womanNum
       , childNum
       , mannum
       , ordertype
       , begintime
  FROM
    t_order USE INDEX (IX_t_order_begintime)
  WHERE
    branchid = pi_branchid
    AND begintime BETWEEN v_date_start AND v_date_end # 需要创建索引IX_t_order_begintime  
    AND orderstatus = 3;


  #为订单内存表创建索引
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);
  CREATE INDEX ix_t_temp_order_begintime ON t_temp_order (begintime);

  #创建订单详情临时内存表（方便计算应收）
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
  CREATE TEMPORARY TABLE t_temp_order_detail
  (
    orderid VARCHAR(50),
    dishnum DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
    begintime DATETIME,
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50),
    dishtype TINYINT,
		pricetype DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  # 向临时订单详情内存表中放值
  INSERT INTO t_temp_order_detail
  SELECT b.orderid
       , b.dishnum
       , b.orignalprice
       , b.begintime
       , b.childdishtype
       , b.primarykey
       , b.superkey
       , b.dishtype
			 , b.pricetype
  FROM
    t_temp_order a, t_order_detail b
  WHERE
    a.orderid = b.orderid;
    #AND orignalprice > 0;
 -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan SELECT superkey,SUM(dishnum*orignalprice) FROM t_temp_order_detail  WHERE dishtype = 2 AND superkey <> primarykey GROUP BY superkey;
  UPDATE t_temp_order_detail d,t_temp_taocan c SET d.orignalprice = c.orignalprice  WHERE c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
  DELETE FROM t_temp_order_detail WHERE dishtype =2 AND superkey <> primarykey;

  #为订单详情表创建索引
  CREATE INDEX ix_t_temp_order_detail_begintime ON t_temp_order_detail (begintime);

  #创建结算明细内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  #生产临时结算明细表数据
  INSERT INTO t_temp_settlement_detail
  SELECT b.orderid
       , b.payway
       , b.payamount
       , a.begintime
  FROM
    t_temp_order a, t_settlement_detail b
  WHERE
    a.orderid = b.orderid
    AND b.payamount > 0
    AND b.payway IN (SELECT itemid FROM v_revenuepayway);
  CREATE INDEX ix_t_temp_settlement_detail_begintime ON t_temp_settlement_detail (begintime);

  #创建会员消费内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  CREATE TEMPORARY TABLE t_temp_order_member
  (
    orderid VARCHAR(50),
    Inflated DOUBLE(13, 2),
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  #生成临时会员结算数据  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
       , a.begintime
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid;
  CREATE INDEX ix_t_temp_order_member_begintime ON t_temp_order_member (begintime);

  #创建结果内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    statistictime VARCHAR(15), #统计日期
    shouldamount DOUBLE(13, 2), #应收
    paidinamount DOUBLE(13, 2), #实收
    discountamount DOUBLE(13, 2), #折扣
    personpercent  DOUBLE(13, 2), #人均
    tablecount   INT DEFAULT 0 #桌数
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  #循环计算所需的数据
  WHILE v_loop_num > 0
  DO

    #计算应收
    SELECT IFNULL(SUM(a.orignalprice * a.dishnum), 0)
    INTO
      v_shouldamount
    FROM
      t_temp_order_detail a LEFT JOIN t_temp_order b ON a.orderid = b.orderid
    WHERE
      b.begintime BETWEEN v_date_start AND v_date_interval
		AND (a.dishtype <>2 OR (a.dishtype = 2 AND a.superkey = a.primarykey))
		AND a.pricetype = 0;

    #计算实收（含虚增）
    SELECT IFNULL(SUM(payamount), 0)
    INTO
      v_paidinamount
    FROM
      t_temp_settlement_detail
    WHERE
      begintime BETWEEN v_date_start AND v_date_interval;

    #计算虚增
    SELECT IFNULL(SUM(Inflated), 0)
    INTO
      v_inflated
    FROM
      t_temp_order_member
    WHERE
      begintime BETWEEN v_date_start AND v_date_interval;

    #设置订单数和人数
    SELECT IFNULL(COUNT(orderid),0) #堂吃的订单数量
         , IFNULL(SUM(womanNum + childNum + mannum),0) #堂吃的就餐总人数
    INTO
      v_table_num, v_sa_settlementnum
    FROM
      t_temp_order
    WHERE
      begintime BETWEEN v_date_start AND v_date_interval;

    IF v_table_num > 0 THEN
      SET v_person_con = (v_paidinamount - v_inflated) / v_sa_settlementnum;
    END IF;

    INSERT INTO t_temp_res VALUES (v_statistictime, v_shouldamount, v_paidinamount - v_inflated, v_shouldamount - v_paidinamount + v_inflated,v_person_con,v_table_num);

    IF pi_xslx = 0 THEN
      SET v_date_start = DATE_ADD(v_date_start, INTERVAL 1 DAY);
      SET v_date_interval = DATE_ADD(v_date_interval, INTERVAL 1 DAY);
      SET v_statistictime = DATE_FORMAT(v_date_start, '%Y-%m-%d');
    ELSE
      SET v_date_start = DATE_ADD(v_date_start, INTERVAL 1 MONTH);
      SET v_date_interval = DATE_SUB(DATE_ADD(v_date_start, INTERVAL 1 MONTH), INTERVAL 1 SECOND);
      SET v_statistictime = DATE_FORMAT(v_date_start, '%Y-%m');
    END IF;

    SET v_loop_num = v_loop_num - 1;
  END WHILE;
  COMMIT;

  #返回结果集
  SELECT *
  FROM
    t_temp_res;

#清空内存表
--   DROP TEMPORARY TABLE IF EXISTS t_temp_order;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_res;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_keys;

END$$

DELIMITER ;