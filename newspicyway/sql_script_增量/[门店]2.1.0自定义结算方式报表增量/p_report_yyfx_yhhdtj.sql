DELIMITER $$


DROP PROCEDURE IF EXISTS `p_report_yyfx_yhhdtj`$$

CREATE PROCEDURE `p_report_yyfx_yhhdtj`(IN  pi_branchid INT(11), 
                                                IN  pi_xslx     SMALLINT, 
                                                IN  pi_ksrq     DATETIME, 
                                                IN  pi_jsrq     DATETIME, 
                                                IN  pi_tjx      SMALLINT, 
                                                IN  pi_hdlx     SMALLINT, 
                                                OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '营业分析_优惠活动统计'
label_main:
BEGIN
  
  
  
  
  
  
  
  
  
  
  
  

  DECLARE v_date_start    DATETIME;
  DECLARE v_date_end      DATETIME;
  DECLARE v_date_interval DATETIME; 
  DECLARE v_statistictime VARCHAR(15); 
  DECLARE v_loop_num      INT DEFAULT 0; 
  DECLARE v_fetch_done    BOOL DEFAULT FALSE;
  DECLARE v_pname         VARCHAR(128); 
  DECLARE v_ptype         CHAR(4); 
  DECLARE v_pcount        INT UNSIGNED; 
  DECLARE v_pamount       DOUBLE(13, 2); 
  DECLARE v_shouldamount  DOUBLE(13, 2); 
  DECLARE v_paidinacount  DOUBLE(13, 2); 
  DECLARE v_inflated      DOUBLE(13, 2); 
  DECLARE v_showtype      TINYINT; 
  DECLARE v_value_detail  VARCHAR(1000);

  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; 
  END;

  
  
  
  
  
  

  IF pi_branchid IS NULL THEN
    SELECT NULL;
    SET po_errmsg = '分店ID输入不能为空';
    LEAVE label_main;
  END IF;

  IF pi_tjx NOT IN (0, 1) THEN
    SET po_errmsg = '统计项参数输入有误：0:活动名称  1:活动类别';
    SELECT NULL;
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
    SET po_errmsg = '显示类别输入有误，0:日 1:月 2:年';
    LEAVE label_main;
  END IF;


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50),
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_order
  SELECT orderid
       , begintime
  FROM
    t_order USE INDEX (IX_t_order_begintime)
  WHERE
    branchid = pi_branchid
    AND begintime BETWEEN v_date_start AND v_date_end 
    AND orderstatus = 3;

  
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
    a.orderid = b.orderid  AND orignalprice > 0;

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
    AND b.payamount != 0;
  


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_paidinamount;
  CREATE TEMPORARY TABLE t_temp_settlement_paidinamount
  (
    orderid VARCHAR(50),
    payamount DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  INSERT INTO t_temp_settlement_paidinamount
  SELECT orderid
       , IFNULL(SUM(payamount), 0)
  FROM
    t_temp_settlement_detail
  WHERE
    payamount > 0
    AND payway IN (SELECT itemid FROM v_revenuepayway)
  GROUP BY
    orderid;
  CREATE INDEX ix_t_temp_settlement_paidinamount_orderid ON t_temp_settlement_paidinamount (orderid);

  
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

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new;
  CREATE TEMPORARY TABLE t_temp_settlement_new
  (
    orderid VARCHAR(50),
    payway INT, 
    payamount DOUBLE(13, 2), 
    couponNum INT, 
    pname VARCHAR(128), 
    ptype CHAR(4), 
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_sub;
  CREATE TEMPORARY TABLE t_temp_settlement_sub
  (
    orderid VARCHAR(50),
    couponNum INT, 
    pname VARCHAR(128) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;


  
  INSERT INTO t_temp_settlement_new
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

  INSERT INTO t_temp_settlement_sub
  SELECT orderid
       , couponNum
       , pname
  FROM
    t_temp_settlement_new
  WHERE
    payway = 6;


  
  UPDATE t_temp_settlement_new a, t_temp_settlement_sub b
  SET
    a.couponNum = 0
  WHERE
    a.orderid = b.orderid
    AND a.pname = b.pname
    AND a.couponNum = b.couponNum
    AND a.payway = 5;


  
  
  
  
  
  
  
  
  
  
  
  
  

  
  INSERT INTO t_temp_settlement_new
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

  CREATE INDEX ix_t_temp_settlement_new_begintime ON t_temp_settlement_new (begintime);


  

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new_sub;
  CREATE TEMPORARY TABLE t_temp_settlement_new_sub
  (
    orderid VARCHAR(50),
    payway INT, 
    payamount DOUBLE(13, 2), 
    couponNum INT, 
    pname VARCHAR(128), 
    ptype CHAR(4), 
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail;
  CREATE TEMPORARY TABLE t_temp_res_detail
  (
    statistictime VARCHAR(20),
    pname VARCHAR(50),
    ptype CHAR(4),
    pcount INT UNSIGNED,
    pamount DOUBLE(13, 2),
    pshouldamount DOUBLE(13, 2),
    ppaidinamount DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_shouldamount;
  CREATE TEMPORARY TABLE t_temp_shouldamount
  (
    statistictime VARCHAR(20),
    pname VARCHAR(50),
    shouldamount DOUBLE(13, 2) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid;
  CREATE TEMPORARY TABLE t_temp_orderid
  (
    orderid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  WHILE v_loop_num > 0
  DO

    
    TRUNCATE t_temp_settlement_new_sub;

    
    INSERT INTO t_temp_settlement_new_sub
    SELECT *
    FROM
      t_temp_settlement_new
    WHERE
      begintime BETWEEN v_date_start AND v_date_interval;


    BEGIN
      DECLARE cur_pname CURSOR FOR SELECT DISTINCT pname
                                   FROM
                                     t_temp_settlement_new_sub;

      SET v_fetch_done = FALSE;
      OPEN cur_pname;

    lable_fetch_loop:
      LOOP
        FETCH cur_pname INTO v_pname;
        IF v_fetch_done THEN
          LEAVE lable_fetch_loop;
        END IF;

        SELECT MAX(ptype)
             , IFNULL(SUM(couponNum), 0)
             , IFNULL(SUM(payamount), 0)
        INTO
          v_ptype, v_pcount, v_pamount
        FROM
          t_temp_settlement_new_sub
        WHERE
          pname = v_pname;

        TRUNCATE t_temp_orderid;
        INSERT INTO t_temp_orderid
        SELECT DISTINCT orderid
        FROM
          t_temp_settlement_new_sub
        WHERE
          pname = v_pname;

        
        SELECT IFNULL(SUM(a.orignalprice * a.dishnum), 0)
        INTO
          v_shouldamount
        FROM
          t_temp_order_detail a, t_temp_orderid b
        WHERE
          a.orderid = b.orderid;


        
        SELECT IFNULL(SUM(payamount), 0)
        INTO
          v_paidinacount
        FROM
          t_temp_settlement_paidinamount a, t_temp_orderid b
        WHERE
          a.orderid = b.orderid;


        
        SELECT IFNULL(SUM(Inflated), 0)
        INTO
          v_inflated
        FROM
          t_temp_order_member a, t_temp_orderid b
        WHERE
          a.orderid = b.orderid;

        INSERT INTO t_temp_res_detail VALUE (v_statistictime, v_pname, v_ptype, v_pcount, v_pamount, v_shouldamount, v_paidinacount - v_inflated);

      END LOOP;

      
      CLOSE cur_pname;
    END;

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

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    showtype TINYINT,
    pname VARCHAR(50),
    ptype VARCHAR(50),
    total_num DOUBLE(13, 2),
    detail_num VARCHAR(1000),
    PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;


  
  IF pi_tjx = 0 THEN

    
    INSERT INTO t_temp_res (showtype, pname, ptype, total_num)
    SELECT 0
         , pname
         , ptype
         , SUM(pcount)
    FROM
      t_temp_res_detail
    WHERE
      ptype = pi_hdlx
    GROUP BY
      pname;

    
    INSERT INTO t_temp_res (showtype, pname, ptype, total_num)
    SELECT 1
         , pname
         , ptype
         , SUM(pamount)
    FROM
      t_temp_res_detail
    WHERE
      ptype = pi_hdlx
    GROUP BY
      pname;

    
    INSERT INTO t_temp_res (showtype, pname, ptype, total_num)
    SELECT 2
         , pname
         , ptype
         , SUM(pshouldamount)
    FROM
      t_temp_res_detail
    WHERE
      ptype = pi_hdlx
    GROUP BY
      pname;

    
    INSERT INTO t_temp_res (showtype, pname, ptype, total_num)
    SELECT 3
         , pname
         , ptype
         , SUM(ppaidinamount)
    FROM
      t_temp_res_detail
    WHERE
      ptype = pi_hdlx
    GROUP BY
      pname;
  
  ELSE
    
    INSERT INTO t_temp_res (showtype, pname, total_num)
    SELECT 0
         , ptype
         , SUM(pcount)
    FROM
      t_temp_res_detail
    GROUP BY
      ptype;

    
    INSERT INTO t_temp_res (showtype, pname, total_num)
    SELECT 1
         , ptype
         , SUM(pamount)
    FROM
      t_temp_res_detail
    GROUP BY
      ptype;

    
    INSERT INTO t_temp_res (showtype, pname, total_num)
    SELECT 2
         , ptype
         , SUM(pshouldamount)
    FROM
      t_temp_res_detail
    GROUP BY
      ptype;

    
    INSERT INTO t_temp_res (showtype, pname, total_num)
    SELECT 3
         , ptype
         , SUM(ppaidinamount)
    FROM
      t_temp_res_detail
    GROUP BY
      ptype;
  END IF;


  
  SET v_loop_num = 1;

loop_lable1:
  LOOP
    SELECT showtype
         , pname
         , COUNT(1)
    INTO
      v_showtype, v_pname, @cnt
    FROM
      t_temp_res
    WHERE
      id = v_loop_num;

    
    IF @cnt = 0 THEN
      LEAVE loop_lable1;
    END IF;

    IF pi_tjx = 0 THEN
      IF v_showtype = 0 THEN
        SELECT GROUP_CONCAT(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT CONCAT(statistictime, ',', pcount) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            pname = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSEIF v_showtype = 1 THEN
        SELECT GROUP_CONCAT(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT CONCAT(statistictime, ',', pamount) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            pname = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSEIF v_showtype = 2 THEN
        SELECT GROUP_CONCAT(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT CONCAT(statistictime, ',', pshouldamount) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            pname = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSEIF v_showtype = 3 THEN
        SELECT GROUP_CONCAT(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT CONCAT(statistictime, ',', ppaidinamount) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            pname = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSE
        LEAVE loop_lable1;
      END IF;
    ELSE
      IF v_showtype = 0 THEN
        SELECT GROUP_CONCAT(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT CONCAT(MAX(statistictime), ',', SUM(pcount)) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            ptype = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSEIF v_showtype = 1 THEN
        SELECT GROUP_CONCAT(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT CONCAT(MAX(statistictime), ',', SUM(pamount)) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            ptype = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSEIF v_showtype = 2 THEN
        SELECT GROUP_CONCAT(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT CONCAT(MAX(statistictime), ',', SUM(pshouldamount)) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            ptype = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSEIF v_showtype = 3 THEN
        SELECT GROUP_CONCAT(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT CONCAT(MAX(statistictime), ',', SUM(ppaidinamount)) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            ptype = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSE
        LEAVE loop_lable1;
      END IF;
    END IF;



    
    UPDATE t_temp_res
    SET
      detail_num = v_value_detail
    WHERE
      id = v_loop_num;
    SET v_loop_num = v_loop_num + 1;
  END LOOP;

  
  IF pi_tjx = 1 THEN
    SELECT a.showtype
         , t.ptypename
         , a.total_num
         , a.detail_num
    FROM
      t_temp_res a, (SELECT DISTINCT b.ptype
                                   , b.ptypename
                     FROM
                       t_temp_preferential b) t
    WHERE
      a.pname = t.ptype;
  
  ELSE
    SELECT showtype
         , pname
         , ptype
         , total_num
         , detail_num
    FROM
      t_temp_res;
  END IF;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_paidinamount;
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  DROP TEMPORARY TABLE IF EXISTS t_temp_preferential;
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new;
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_sub;
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new_sub;
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail;
  DROP TEMPORARY TABLE IF EXISTS t_temp_shouldamount;
  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid;
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;



END$$

DELIMITER ;