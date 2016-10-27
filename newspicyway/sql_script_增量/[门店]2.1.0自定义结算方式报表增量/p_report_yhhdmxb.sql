DELIMITER $$


DROP PROCEDURE IF EXISTS `p_report_yhhdmxb`$$

CREATE PROCEDURE `p_report_yhhdmxb`(IN  pi_branchid INT(11), 
                                  IN  pi_sb       SMALLINT, 
                                  IN  pi_ksrq     DATETIME, 
                                  IN  pi_jsrq     DATETIME, 
                                  IN  pi_hdmc     VARCHAR(50), 
                                  IN  pi_jsfs     INT, 
                                  IN  pi_hdlx     VARCHAR(10), 
                                  OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '优惠活动明细表'
label_main:
BEGIN
  
  
  
  
  
  

  DECLARE v_date_start   DATETIME;
  DECLARE v_date_end     DATETIME;
  DECLARE v_loop_index   INT;
  DECLARE v_pname        VARCHAR(50);
  DECLARE v_ptype        CHAR(4);
  DECLARE v_payway       INT;
  DECLARE v_shouldamount DOUBLE(13, 2);
  DECLARE v_paidinamount DOUBLE(13, 2);
  DECLARE v_inflated     DOUBLE(13, 2); 
  
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SELECT NULL;
  
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
    orderid VARCHAR(50),
    mannum INT,
    womanNum INT,
    childNum INT
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_sb > -1 THEN
    INSERT INTO t_temp_order
    SELECT orderid
         , mannum
         , womanNum
         , childNum
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
         , mannum
         , womanNum
         , childNum
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
    a.orderid = b.orderid
    AND b.orignalprice > 0;

  
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
    singular INT, 
    perCapita DOUBLE(13, 2), 
    payamount DOUBLE(13, 2),
    couponNum INT,
    bankcardno VARCHAR(50),
    coupondetailid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_settlement_detail (orderid, payway, payamount, couponNum, bankcardno, coupondetailid, singular, perCapita)
  SELECT b.orderid
       , b.payway
       , SUM(b.payamount)
       , SUM(b.couponNum)
       , b.bankcardno
       , b.coupondetailid
       , '1'
       , (a.mannum + a.womanNum + a.childNum)
  FROM
    t_temp_order a, t_settlement_detail b
  WHERE
    a.orderid = b.orderid GROUP BY b.orderid,b.payway,b.coupondetailid;
  
  CREATE INDEX ix_t_temp_settlement_detail_payway ON t_temp_settlement_detail (payway);


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_paidinamout;
  CREATE TEMPORARY TABLE t_temp_paidinamout
  (
    orderid VARCHAR(50),
    payamount DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_paidinamout
  SELECT orderid
       , payamount
  FROM
    t_temp_settlement_detail
  WHERE
    payway IN (SELECT itemid FROM v_revenuepayway);
  CREATE INDEX ix_t_temp_paidinamout_orderid ON t_temp_paidinamout (orderid);


  
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
    singular INT, 
    payamount DOUBLE(13, 2), 
    couponNum INT, 
    pname VARCHAR(128), 
    ptype CHAR(4), 
    perCapita DOUBLE(13, 2), 
    ptypename VARCHAR(32) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  INSERT INTO t_temp_res_detail (orderid, payway, payamount, couponNum, pname, ptype, ptypename, singular, perCapita)
  SELECT a.orderid
       , a.payway
       , a.payamount
       , a.couponNum
       , b.pname
       , b.ptype
       , b.ptypename
       , a.singular
       , a.perCapita
  FROM
    t_temp_settlement_detail a, t_temp_preferential b
  WHERE
    a.payway IN (5, 6)
    AND a.coupondetailid = b.pid;

  
  
  
  
  
  
  
  
  
  
  
  
  

  
  INSERT INTO t_temp_res_detail (orderid, payway, payamount, couponNum, pname, ptype, ptypename, singular, perCapita)
  SELECT a.orderid
       , 6
       , a.payamount
       , a.couponNum
       , a.bankcardno
       , '99'
       , '雅座优惠券'
       , a.singular
       , a.perCapita
  FROM
    t_temp_settlement_detail a
  WHERE
    a.payway = 12;
  CREATE INDEX ix_t_temp_res_detail ON t_temp_res_detail (pname, ptype, payway);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    pname VARCHAR(128), 
    ptype CHAR(4), 
    ptypename VARCHAR(32), 
    payway INT, 
    paywaydesc VARCHAR(50), 
    couponNum INT, 
    singular INT, 
    payamount DOUBLE(13, 2), 
    perCapita DOUBLE(13, 2), 
    shouldamount DOUBLE(13, 2), 
    paidinamount DOUBLE(13, 2), 
    PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;


  INSERT INTO t_temp_res (pname, ptype, payway, couponNum, payamount, singular, perCapita)
  SELECT pname
       , ptype
       , payway
       , IFNULL(SUM(couponNum), 0)
       , IFNULL(SUM(payamount), 0)
       , IFNULL(COUNT(1), 0)
       , IFNULL(SUM(perCapita), 0)
  FROM
    t_temp_res_detail
  GROUP BY
    pname
  , ptype
  , payway;

  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid;
  CREATE TEMPORARY TABLE t_temp_orderid
  (
    orderid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;


  
  SET @offset = @@auto_increment_offset;
  SET @increment = @@auto_increment_increment;
  SELECT IFNULL(MAX(id), 0)
  INTO
    @maxid
  FROM
    t_temp_res;

lable_loop:
  LOOP
    
    IF @offset > @maxid THEN
      LEAVE lable_loop;
    END IF;

    SELECT pname
         , ptype
         , payway
    INTO
      v_pname, v_ptype, v_payway
    FROM
      t_temp_res
    WHERE
      id = @offset;


    TRUNCATE t_temp_orderid;

    
    INSERT INTO t_temp_orderid
    SELECT DISTINCT orderid
    FROM
      t_temp_res_detail
    WHERE
      pname = v_pname
      AND ptype = v_ptype
      AND payway = v_payway;

    
    SELECT IFNULL(SUM(a.orignalprice * a.dishnum), 0)
    INTO
      v_shouldamount
    FROM
      t_temp_order_detail a, t_temp_orderid b
    WHERE
      a.orderid = b.orderid;

    
    SELECT IFNULL(SUM(a.payamount), 0)
    INTO
      v_paidinamount
    FROM
      t_temp_paidinamout a, t_temp_orderid b
    WHERE
      a.orderid = b.orderid;

    
    SELECT IFNULL(SUM(a.Inflated), 0)
    INTO
      v_inflated
    FROM
      t_temp_order_member a, t_temp_orderid b
    WHERE
      a.orderid = b.orderid;

    
    UPDATE t_temp_res
    SET
      shouldamount = v_shouldamount, paidinamount = v_paidinamount - v_inflated
    WHERE
      id = @offset;

    
    SET @offset = @offset + @increment;
  END LOOP;


  
  UPDATE t_temp_res a, t_dictionary b
  SET
    a.paywaydesc = b.itemDesc
  WHERE
    a.payway = b.itemid
    AND TYPE = 'PAYWAY';

  
  UPDATE t_temp_res a, t_temp_preferential b
  SET
    a.ptypename = b.ptypename
  WHERE
    a.ptype = b.ptype;

  
  UPDATE t_temp_res a
  SET
    a.ptypename = '雅座优惠券'
  WHERE
    a.ptype = '99';

  
  IF pi_hdmc != '-1' THEN
    DELETE
    FROM
      t_temp_res
    WHERE
      pname != pi_hdmc;
  END IF;

  IF pi_jsfs != -1 THEN
    DELETE
    FROM
      t_temp_res
    WHERE
      payway != pi_jsfs;
  END IF;

  IF pi_hdlx != '-1' THEN
    DELETE
    FROM
      t_temp_res
    WHERE
      ptype != pi_hdlx;
  END IF;


  
  SELECT pname
     , 
       ptype
     , 
       ptypename
     , 
       payway
     , 
       paywaydesc
     , 
       couponNum
     , 
       payamount
     , 
       shouldamount
     , 
       paidinamount
     
       , ROUND(paidinamount / perCapita, 2) AS perCapita
     , singular
FROM
  t_temp_res;

















END$$

DELIMITER ;