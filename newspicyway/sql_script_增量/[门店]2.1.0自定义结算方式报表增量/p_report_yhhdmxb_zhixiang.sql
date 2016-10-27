DELIMITER $$

USE `newspicyway`$$

DROP PROCEDURE IF EXISTS `p_report_yhhdmxb_zhixiang`$$

CREATE PROCEDURE `p_report_yhhdmxb_zhixiang`(IN  pi_branchid INT(11), 
                                           IN  pi_sb       SMALLINT, 
                                           IN  pi_ksrq     DATETIME, 
                                           IN  pi_jsrq     DATETIME, 
                                           IN  pi_hdmc     VARCHAR(50), 
                                           IN  pi_jsfs     INT, 
                                           IN  pi_hdlx     VARCHAR(10), 
                                           IN  pi_dqy      INT, 
                                           IN  pi_myts     INT, 
                                           OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '优惠活动明细表-子项'
label_main:
BEGIN 
  
  
  
  
  

  DECLARE v_date_start          DATETIME;
  DECLARE v_date_end            DATETIME;
  DECLARE v_loop_index          INT;
  DECLARE v_pname               VARCHAR(50);
  DECLARE v_ptype               CHAR(4);
  DECLARE v_payway              INT;
  DECLARE v_shouldamount        DOUBLE(13, 2);
  DECLARE v_paidinamount        DOUBLE(13, 2);
  DECLARE v_inflated            DOUBLE(13, 2); 
  DECLARE v_fetch_done          BOOL DEFAULT FALSE; 
  DECLARE v_increment_offset    INT;
  DECLARE v_increment_increment INT;



  
  DECLARE cur_p_detail CURSOR FOR SELECT DISTINCT pname
                                                , ptype
                                                , payway
                                  FROM
                                    t_temp_res_detail;

  
  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; 
  END;

  
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

  IF pi_myts < 1 THEN
    SELECT NULL;
    SET po_errmsg = '每页显示记录条数不能少于1';
    LEAVE label_main;
  END IF;

  IF pi_dqy < -1 THEN
    SELECT NULL;
    SET po_errmsg = '当前页面只能输入大于-1的正整数';
    LEAVE label_main;
  END IF;


  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;
  SET v_increment_offset = @@auto_increment_offset;
  SET v_increment_increment = @@auto_increment_increment;

  
  SET v_date_start = pi_ksrq; 
  SET v_date_end = pi_jsrq; 

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50),
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_sb > -1 THEN
    INSERT INTO t_temp_order
    SELECT orderid
         , begintime
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
         , begintime
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      branchid = pi_branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND orderstatus = 3;
  END IF;
  
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
  CREATE TEMPORARY TABLE t_temp_order_detail
  (
    orderid VARCHAR(50),
    dishnum DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50),
    dishtype TINYINT
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_order_detail
  SELECT b.orderid
       , b.dishnum
       , b.orignalprice
       , b.childdishtype
       , b.primarykey
       , b.superkey
       , b.dishtype
  FROM
    t_temp_order a, t_order_detail b
  WHERE
    a.orderid = b.orderid;

   
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan SELECT superkey,SUM(dishnum*orignalprice) FROM t_temp_order_detail  WHERE dishtype = 2 AND superkey <> primarykey GROUP BY superkey;
  UPDATE t_temp_order_detail d,t_temp_taocan c SET d.orignalprice = c.orignalprice  WHERE c.primarykey = d.primarykey;
   

  
   DELETE FROM t_temp_order_detail WHERE dishtype =2 AND superkey <> primarykey;

  CREATE INDEX ix_t_temp_order_detail_orderid ON t_temp_order_detail (orderid);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    couponNum INT,
    bankcardno VARCHAR(50),
    coupondetailid VARCHAR(50),
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_settlement_detail
  SELECT b.orderid
       , b.payway
       , b.payamount
       , b.couponNum
       , b.bankcardno
       , b.coupondetailid
       , a.begintime
  FROM
    t_temp_order a, t_settlement_detail b
  WHERE
    a.orderid = b.orderid
    AND b.payamount > 0;
  
  CREATE INDEX ix_t_temp_settlement_detail_orderid ON t_temp_settlement_detail (orderid);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  CREATE TEMPORARY TABLE t_temp_order_member
  (
    orderid VARCHAR(50),
    Inflated DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid;
  CREATE INDEX ix_t_temp_order_member_orderid ON t_temp_order_member (orderid);


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_preferential;
  CREATE TEMPORARY TABLE t_temp_preferential
  (
    pid VARCHAR(50),
    pname VARCHAR(128),
    ptype CHAR(4),
    ptypename VARCHAR(32)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  INSERT INTO t_temp_preferential
  SELECT a.id
       , (
         CASE
         WHEN a.preferential = a.id THEN
           b.name
         ELSE
           IFNULL(IFNULL(a.free_reason, a.company_name), b.name)
         END) AS NAME
       , CASE
         WHEN b.type = '06' THEN
           b.sub_type
         ELSE
           b.type
         END
       , CASE
         WHEN b.type = '06' THEN
           b.sub_type_name
         ELSE
           b.type_name
         END
  FROM
    t_p_preferential_detail a, t_p_preferential_activity b
  WHERE
    a.preferential = b.id;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail;
  CREATE TEMPORARY TABLE t_temp_res_detail
  (
    orderid VARCHAR(50),
    payway INT, 
    payamount DOUBLE(13, 2), 
    couponNum INT, 
    pname VARCHAR(128), 
    ptype CHAR(4), 
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  INSERT INTO t_temp_res_detail
  SELECT a.orderid
       , a.payway
       , a.payamount
       , a.couponNum
       , b.pname
       , b.ptype
       , a.begintime
  FROM
    t_temp_settlement_detail a, t_temp_preferential b
  WHERE
    a.payway IN (5, 6)
    AND a.coupondetailid = b.pid;


  INSERT INTO t_temp_res_detail
  SELECT a.orderid
       , 6
       , a.payamount
       , a.couponNum
       , a.bankcardno
       , '99'
       , a.begintime
  FROM
    t_temp_settlement_detail a
  WHERE
    a.payway = 12;

  
  IF pi_hdmc != '-1' THEN
    DELETE
    FROM
      t_temp_res_detail
    WHERE
      pname != pi_hdmc;
  END IF;

  IF pi_jsfs != -1 THEN
    DELETE
    FROM
      t_temp_res_detail
    WHERE
      payway != pi_jsfs;
  END IF;

  IF pi_hdlx != '-1' THEN
    DELETE
    FROM
      t_temp_res_detail
    WHERE
      ptype != pi_hdlx;
  END IF;
  CREATE INDEX ix_t_temp_res_detail ON t_temp_res_detail (pname, ptype, payway);


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    temp_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    begintime CHAR(10),
    orderid VARCHAR(50),
    price DOUBLE(13, 2),
    couponNum INT, 
    payamount DOUBLE(13, 2), 
    shouldamount DOUBLE(13, 2), 
    paidinamount DOUBLE(13, 2), 
    PRIMARY KEY (temp_id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;



  
  OPEN cur_p_detail;

lable_fetch_loop:
  LOOP

    FETCH cur_p_detail INTO v_pname, v_ptype, v_payway;
    IF v_fetch_done THEN
      LEAVE lable_fetch_loop;
    END IF;
    INSERT INTO t_temp_res (begintime, orderid, price, couponNum, payamount, shouldamount, paidinamount)
    SELECT DATE_FORMAT(MAX(begintime), '%Y-%m-%d')
         , orderid
         , MAX(payamount)
         , IFNULL(SUM(couponNum), 0)
         , IFNULL(SUM(payamount), 0)
         , 0
         , 0
    FROM
      t_temp_res_detail
    WHERE
      pname = v_pname
      AND ptype = v_ptype
      AND payway = v_payway
    GROUP BY
      orderid;
  END LOOP;
  
  CLOSE cur_p_detail;


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid;
  CREATE TEMPORARY TABLE t_temp_orderid
  (
    orderid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_orderid
  SELECT DISTINCT orderid
  FROM
    t_temp_res;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_shouldamount;
  CREATE TEMPORARY TABLE t_temp_shouldamount
  (
    orderid VARCHAR(50),
    shouldamount DOUBLE(13, 2) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_shouldamount
  SELECT a.orderid
       , SUM(a.orignalprice * a.dishnum)
  FROM
    t_temp_order_detail a, t_temp_orderid b
  WHERE
    a.orderid = b.orderid
  GROUP BY
    a.orderid;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_inflated;
  CREATE TEMPORARY TABLE t_temp_inflated
  (
    orderid VARCHAR(50),
    inflated DOUBLE(13, 2) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_inflated
  SELECT a.orderid
       , IFNULL(SUM(a.Inflated), 0)
  FROM
    t_temp_order_member a, t_temp_orderid b
  WHERE
    a.orderid = b.orderid
  GROUP BY
    a.orderid;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_paidinamount;
  CREATE TEMPORARY TABLE t_temp_paidinamount
  (
    orderid VARCHAR(50),
    paidinamount DOUBLE(13, 2) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_paidinamount
  SELECT a.orderid
       , IFNULL(SUM(a.payamount), 0)
  FROM
    t_temp_settlement_detail a, t_temp_orderid b
  WHERE
    a.orderid = b.orderid
    AND payway IN (SELECT itemid FROM v_revenuepayway)  
  GROUP BY
    a.orderid;

  
  UPDATE t_temp_paidinamount a, t_temp_inflated b
  SET
    a.paidinamount = a.paidinamount - b.inflated
  WHERE
    a.orderid = b.orderid;


  
  UPDATE t_temp_res a, t_temp_shouldamount b
  SET
    a.shouldamount = b.shouldamount
  WHERE
    a.orderid = b.orderid;

  
  UPDATE t_temp_res a, t_temp_paidinamount b
  SET
    a.paidinamount = b.paidinamount
  WHERE
    a.orderid = b.orderid;


  
  
  IF pi_dqy > -1 THEN
    SET @a = v_increment_offset + pi_dqy * pi_myts * v_increment_increment - v_increment_increment;
    SET @b = pi_myts;
    PREPARE s1 FROM 'SELECT begintime,orderid,price,couponNum,payamount,shouldamount,paidinamount FROM t_temp_res where temp_id > ? limit ?';
    EXECUTE s1 USING @a, @b;
  ELSE
    PREPARE s1 FROM 'SELECT begintime,orderid,price,couponNum,payamount,shouldamount,paidinamount FROM t_temp_res';
    EXECUTE s1;
  END IF;
  DEALLOCATE PREPARE s1;
















END$$

DELIMITER ;