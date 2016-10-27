DELIMITER $$

USE `newspicyway`$$

DROP PROCEDURE IF EXISTS `p_report_yyfx_yysjtj`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_yyfx_yysjtj`(IN  pi_branchid INT(11), 
                                      IN  pi_xslx     SMALLINT, 
                                      IN  pi_ksrq     DATETIME, 
                                      IN  pi_jsrq     DATETIME, 
                                      OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '营业分析_营业数据统计'
label_main:
BEGIN
  
  
  
  
  
  

  DECLARE v_date_start        DATETIME;
  DECLARE v_date_end          DATETIME;
  DECLARE v_date_interval     DATETIME; 
  DECLARE v_loop_num          INT DEFAULT 0; 
  DECLARE v_statistictime     VARCHAR(15); 
  DECLARE v_shouldamount      DOUBLE(13, 2); 
  DECLARE v_paidinamount      DOUBLE(13, 2); 
  DECLARE v_inflated          DOUBLE(13, 2); 
  DECLARE v_person_con        DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_table_num         INT DEFAULT 0; 
  DECLARE v_sa_settlementnum  INT DEFAULT 0; 

  
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

  
  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;


  
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
    AND begintime BETWEEN v_date_start AND v_date_end 
    AND orderstatus = 3;


  
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);
  CREATE INDEX ix_t_temp_order_begintime ON t_temp_order (begintime);

  
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
    dishtype TINYINT
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_order_detail
  SELECT b.orderid
       , b.dishnum
       , b.orignalprice
       , b.begintime
       , b.childdishtype
       , b.primarykey
       , b.superkey
       , b.dishtype
  FROM
    t_temp_order a, t_order_detail b
  WHERE
    a.orderid = b.orderid
    AND orignalprice > 0;
 
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan SELECT superkey,SUM(dishnum*orignalprice) FROM t_temp_order_detail  WHERE dishtype = 2 AND superkey <> primarykey GROUP BY superkey;
  UPDATE t_temp_order_detail d,t_temp_taocan c SET d.orignalprice = c.orignalprice  WHERE c.primarykey = d.primarykey;
   

  
  DELETE FROM t_temp_order_detail WHERE dishtype =2 AND superkey <> primarykey;

  
  CREATE INDEX ix_t_temp_order_detail_begintime ON t_temp_order_detail (begintime);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
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

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  CREATE TEMPORARY TABLE t_temp_order_member
  (
    orderid VARCHAR(50),
    Inflated DOUBLE(13, 2),
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
       , a.begintime
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid;
  CREATE INDEX ix_t_temp_order_member_begintime ON t_temp_order_member (begintime);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    statistictime VARCHAR(15), 
    shouldamount DOUBLE(13, 2), 
    paidinamount DOUBLE(13, 2), 
    discountamount DOUBLE(13, 2), 
    personpercent  DOUBLE(13, 2), 
    tablecount   INT DEFAULT 0 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  WHILE v_loop_num > 0
  DO

    
    SELECT IFNULL(SUM(a.orignalprice * a.dishnum), 0)
    INTO
      v_shouldamount
    FROM
      t_temp_order_detail a LEFT JOIN t_temp_order b ON a.orderid = b.orderid
    WHERE
      b.begintime BETWEEN v_date_start AND v_date_interval;

    
    SELECT IFNULL(SUM(payamount), 0)
    INTO
      v_paidinamount
    FROM
      t_temp_settlement_detail
    WHERE
      begintime BETWEEN v_date_start AND v_date_interval;

    
    SELECT IFNULL(SUM(Inflated), 0)
    INTO
      v_inflated
    FROM
      t_temp_order_member
    WHERE
      begintime BETWEEN v_date_start AND v_date_interval;

    
    SELECT IFNULL(COUNT(orderid),0) 
         , IFNULL(SUM(womanNum + childNum + mannum),0) 
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

  
  SELECT *
  FROM
    t_temp_res;









END$$

DELIMITER ;