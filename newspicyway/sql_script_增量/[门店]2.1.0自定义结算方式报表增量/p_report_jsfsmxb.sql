DELIMITER $$


DROP PROCEDURE IF EXISTS `p_report_jsfsmxb`$$

CREATE PROCEDURE `p_report_jsfsmxb`(IN  pi_branchid INT(11), 
                                  IN  pi_sb       SMALLINT, 
                                  IN  pi_ksrq     DATETIME, 
                                  IN  pi_jsrq     DATETIME, 
                                  OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '结算方式明细表'
label_main:
BEGIN

  DECLARE v_date_start DATETIME;
  DECLARE v_date_end   DATETIME;

  
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

  
  SET v_date_start = pi_ksrq;
  SET v_date_end = pi_jsrq;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_sb > -1 THEN
    INSERT INTO t_temp_order
    SELECT orderid
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      branchid = pi_branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND shiftid = pi_sb
      AND orderstatus = 3;
  ELSE
    INSERT INTO t_temp_order
    SELECT orderid
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      branchid = pi_branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND orderstatus = 3;
  END IF;

  
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    membercardno VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_settlement_detail
  SELECT b.orderid
       , b.payway
       , b.payamount
       , b.membercardno
  FROM
    t_temp_order a, t_settlement_detail b
  WHERE
    a.orderid = b.orderid
    AND b.payamount > 0
    AND b.payway IN (SELECT itemid FROM v_revenuepayway);
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    payway VARCHAR(50),
    nums INT,
    prices DOUBLE(13, 2),
    membercardno VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_res
  SELECT payway
       , COUNT(1)
       , IFNULL(SUM(payamount), 0)
       , CASE payway WHEN 1 THEN membercardno ELSE NULL END
  FROM
    t_temp_settlement_detail
  GROUP BY
    payway,membercardno;
  
  SELECT (CASE payway WHEN 1 THEN (SELECT c.itemDesc FROM t_dictionary c WHERE c.type ='BANK' AND c.itemid = a.membercardno) ELSE b.itemDesc END)  AS payway
       , b.itemid
       , SUM(a.nums) AS nums
       , SUM(a.prices) AS prices
       , a.membercardno
  FROM
    t_temp_res a LEFT JOIN t_dictionary b ON a.payway = b.itemid 
  WHERE
    b.type = 'PAYWAY'   
  GROUP BY
    b.itemDesc,a.membercardno
  ORDER BY
    payway;
END$$

DELIMITER ;