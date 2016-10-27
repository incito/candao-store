DELIMITER $$

USE `newspicyway`$$

DROP PROCEDURE IF EXISTS `p_report_xxsjtjb`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_xxsjtjb`(IN  pi_branchid INT(11), 
                                  IN  pi_sb       SMALLINT, 
                                  IN  pi_ksrq     DATETIME, 
                                  IN  pi_jsrq     DATETIME, 
                                  IN  pi_cxlx     SMALLINT, 
                                  IN  pi_qy       VARCHAR(50), 
                                  OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '详细数据统计表'
label_main:
BEGIN
  
  
  
  
  


  DECLARE v_date_start    DATETIME;
  DECLARE v_date_end      DATETIME;
  DECLARE v_date_interval DATETIME; 
  DECLARE v_loop_num      INT DEFAULT 0; 
  DECLARE v_table_index   INT;
  DECLARE v_table_id      VARCHAR(50);
  DECLARE v_sql           VARCHAR(5000) DEFAULT '';

  
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

  
  SET v_date_start = STR_TO_DATE(CONCAT(DATE_FORMAT(pi_ksrq, '%Y-%m-%d'), '00:00:00'), '%Y-%m-%d %H:%i:%s');
  SET v_date_end = STR_TO_DATE(CONCAT(DATE_FORMAT(pi_jsrq, '%Y-%m-%d'), '23:59:59'), '%Y-%m-%d %H:%i:%s');
  SET v_date_interval = DATE_SUB(DATE_ADD(v_date_start, INTERVAL 1 DAY), INTERVAL 1 SECOND);
  SET v_loop_num = TIMESTAMPDIFF(DAY, v_date_start, v_date_end) + 1;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50),
    begintime DATETIME,
    tableid VARCHAR(50),
    womanNum INT,
    childNum INT,
    mannum INT
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_sb > -1 THEN
    INSERT INTO t_temp_order
    SELECT orderid
         , begintime
         , currenttableid
         , womanNum
         , childNum
         , mannum
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
         , currenttableid
         , womanNum
         , childNum
         , mannum
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      branchid = pi_branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND orderstatus = 3;
  END IF;

  
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);


  IF pi_cxlx = 1 THEN
    DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
    CREATE TEMPORARY TABLE t_temp_order_detail
    (
      orderid VARCHAR(50),
      dishnum DOUBLE(13, 2),
      orignalprice DOUBLE(13, 2),
      tableid VARCHAR(50),
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
         , a.tableid
         , a.begintime
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

    CREATE INDEX ix_t_temp_order_detail_begintime ON t_temp_order_detail (begintime);

  
  ELSEIF pi_cxlx = 2 THEN
    DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
    CREATE TEMPORARY TABLE t_temp_settlement_detail
    (
      orderid VARCHAR(50),
      payway INT,
      payamount DOUBLE(13, 2),
      begintime DATETIME,
      tableid VARCHAR(50)
    ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

    
    INSERT INTO t_temp_settlement_detail
    SELECT b.orderid
         , b.payway
         , b.payamount
         , a.begintime
         , a.tableid
    FROM
      t_temp_order a, t_settlement_detail b
    WHERE
      a.orderid = b.orderid
      AND b.payway IN (SELECT itemid FROM v_revenuepayway)   
      AND b.payamount > 0;

    CREATE INDEX ix_t_temp_settlement_detail_begintime ON t_temp_settlement_detail (begintime);

    
    DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
    CREATE TEMPORARY TABLE t_temp_order_member
    (
      orderid VARCHAR(50),
      Inflated DOUBLE(13, 2),
      begintime DATETIME,
      tableid VARCHAR(50)
    ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

    
    INSERT INTO t_temp_order_member
    SELECT b.orderid
         , b.Inflated
         , a.begintime
         , a.tableid
    FROM
      t_temp_order a, t_order_member b
    WHERE
      a.orderid = b.orderid;
    CREATE INDEX ix_t_temp_order_member_begintime ON t_temp_order_member (begintime);
  END IF;


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_table;
  CREATE TEMPORARY TABLE t_temp_table
  (
    areaname VARCHAR(10),
    tableid VARCHAR(50),
    tableNo VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_qy = '-1' THEN
    INSERT INTO t_temp_table (areaname, tableid, tableNo)
    SELECT b.areaname
         , a.tableid
         , a.tableNo
    FROM
      t_table a, t_tablearea b
    WHERE
      a.areaid = b.areaid
      AND b.branchid = pi_branchid;
  ELSE
    INSERT INTO t_temp_table (areaname, tableid, tableNo)
    SELECT b.areaname
         , a.tableid
         , a.tableNo
    FROM
      t_table a, t_tablearea b
    WHERE
      a.areaid = b.areaid
      AND b.areaid = pi_qy
      AND b.branchid = pi_branchid;
  END IF;


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    areaname VARCHAR(10),
    tableid VARCHAR(50),
    stime VARCHAR(10),
    svalue DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_inflated;
  CREATE TEMPORARY TABLE t_temp_inflated
  (
    tableid VARCHAR(50),
    stime VARCHAR(10),
    svalue DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  WHILE v_loop_num > 0
  DO

    
    IF pi_cxlx = 1 THEN
      INSERT INTO t_temp_res (tableid, stime, svalue)
      SELECT tableid
           , DATE_FORMAT(v_date_start, '%Y/%m/%d')
           , SUM(dishnum * orignalprice)
      FROM
        t_temp_order_detail
      WHERE
        begintime BETWEEN v_date_start AND v_date_interval
      GROUP BY
        tableid;

    
    ELSEIF pi_cxlx = 2 THEN
      INSERT INTO t_temp_res (tableid, stime, svalue)
      SELECT tableid
           , DATE_FORMAT(v_date_start, '%Y/%m/%d')
           , IFNULL(SUM(payamount), 0)
      FROM
        t_temp_settlement_detail
      WHERE
        begintime BETWEEN v_date_start AND v_date_interval
      GROUP BY
        tableid;

      INSERT INTO t_temp_inflated
      SELECT tableid
           , DATE_FORMAT(v_date_start, '%Y/%m/%d')
           , IFNULL(SUM(Inflated), 0)
      FROM
        t_temp_order_member
      WHERE
        begintime BETWEEN v_date_start AND v_date_interval
      GROUP BY
        tableid;

    

    
    ELSEIF pi_cxlx = 3 THEN
      INSERT INTO t_temp_res (tableid, stime, svalue)
      SELECT tableid
           , DATE_FORMAT(v_date_start, '%Y/%m/%d')
           , IFNULL(SUM(mannum + womanNum + childNum), 0)
      FROM
        t_temp_order
      WHERE
        begintime BETWEEN v_date_start AND v_date_interval
      GROUP BY
        tableid;

    
    ELSEIF pi_cxlx = 4 THEN
      INSERT INTO t_temp_res (tableid, stime, svalue)
      SELECT tableid
           , DATE_FORMAT(v_date_start, '%Y/%m/%d')
           , COUNT(1)
      FROM
        t_temp_order
      WHERE
        begintime BETWEEN v_date_start AND v_date_interval
      GROUP BY
        tableid;
    
    ELSE
      SELECT NULL;
      SET po_errmsg = '显示类型输入有误 1:应收金额 2:实收金额 3:结算人数 4:开台数';
      LEAVE label_main;
    END IF;

    
    SET @date_start = DATE_FORMAT(v_date_start, '%Y/%m/%d');
    SET v_sql = CONCAT(v_sql, ',max(case stime when \'', @date_start, '\' then svalue else 0 end) \'', @date_start, '\'');

    SET v_date_start = DATE_ADD(v_date_start, INTERVAL 1 DAY);
    SET v_date_interval = DATE_ADD(v_date_interval, INTERVAL 1 DAY);
    SET v_loop_num = v_loop_num - 1;
  END WHILE;


  
  DELETE t_temp_res
  FROM
    t_temp_res
  LEFT JOIN t_temp_table
  ON t_temp_res.tableid = t_temp_table.tableid
  WHERE
    t_temp_table.tableid IS NULL;

  
  UPDATE t_temp_res a, t_temp_inflated b
  SET
    a.svalue = a.svalue - b.svalue
  WHERE
    a.tableid = b.tableid
    AND a.stime = b.stime;

  
  UPDATE t_temp_res a, t_temp_table b
  SET
    a.areaname = b.areaname, a.tableid = b.tableNo
  WHERE
    a.tableid = b.tableid;

  
  SET v_sql = CONCAT('SELECT areaname, tableid', v_sql);
  SET v_sql = CONCAT(v_sql, ' FROM t_temp_res GROUP BY areaname, tableid order by tableid-0,areaname');
  SET @sql_xxsj = v_sql;
  PREPARE s1 FROM @sql_xxsj;
  EXECUTE s1;
  DEALLOCATE PREPARE s1;










END$$

DELIMITER ;