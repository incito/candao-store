DELIMITER $$


DROP PROCEDURE IF EXISTS `p_report_yyfx_pxxstj`$$

CREATE PROCEDURE `p_report_yyfx_pxxstj`(IN  pi_branchid INT(11), 
                                      IN  pi_xslx     SMALLINT, 
                                      IN  pi_ksrq     DATETIME, 
                                      IN  pi_jsrq     DATETIME, 
                                      OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '营业分析_品项销售统计'
label_main:
BEGIN
  
  DECLARE v_date_start    DATETIME;
  DECLARE v_date_end      DATETIME;
  DECLARE v_date_interval DATETIME; 
  DECLARE v_statistictime VARCHAR(15); 
  DECLARE v_dishid        VARCHAR(50);
  DECLARE v_dishunit      VARCHAR(300);
  DECLARE v_showtype      TINYINT;
  DECLARE v_value_detail  VARCHAR(1000);
  DECLARE v_loop_num      INT DEFAULT 0; 

  
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
  CREATE INDEX ix_t_temp_order_begintime ON t_temp_order (begintime);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
  CREATE TEMPORARY TABLE t_temp_order_detail
  (
    orderid VARCHAR(50),
    dishid VARCHAR(50),
    dishunit VARCHAR(300),
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
       , b.dishid
       , b.dishunit
       , b.dishnum
       , b.orignalprice
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
  
  CREATE INDEX ix_t_temp_order_detail_begintime ON t_temp_order_detail (begintime);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail;
  CREATE TEMPORARY TABLE t_temp_res_detail
  (
    statistictime VARCHAR(20),
    dishid VARCHAR(50),
    dishunit VARCHAR(300),
    dishnum DOUBLE(13,2),
    dishprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  WHILE v_loop_num > 0
  DO
    INSERT INTO t_temp_res_detail
    SELECT v_statistictime
         , dishid
         , dishunit
         , SUM(dishnum)
         , SUM(dishnum * orignalprice)
    FROM
      t_temp_order_detail USE INDEX (ix_t_temp_order_detail_begintime)
    WHERE
      begintime BETWEEN v_date_start AND v_date_interval
    GROUP BY
      dishid
    , dishunit
    ORDER BY
      NULL;


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
  
  CREATE INDEX ix_t_temp_res_detail_dishid ON t_temp_res_detail (dishid);

  
  DELETE
  FROM
    t_temp_res_detail
  WHERE
    dishid = 'DISHES_98';


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    showtype TINYINT,
    dishid VARCHAR(50),
    dishunit VARCHAR(300),
    total_num DOUBLE(13,2),
    detail_num VARCHAR(1000),
    PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  INSERT INTO t_temp_res (showtype, dishid, dishunit, total_num)
  SELECT 0
       , dishid
       , dishunit
       , SUM(dishnum)
  FROM
    t_temp_res_detail
  GROUP BY
    dishid
  , dishunit
  ORDER BY
    SUM(dishnum) DESC
  LIMIT
    10;

  
  INSERT INTO t_temp_res (showtype, dishid, dishunit, total_num)
  SELECT 1
       , dishid
       , dishunit
       , SUM(dishprice)
  FROM
    t_temp_res_detail
  GROUP BY
    dishid
  , dishunit
  ORDER BY
    SUM(dishprice) DESC
  LIMIT
    10;


  
  SET @offset = @@auto_increment_offset;
  SET @increment = @@auto_increment_increment;
  SELECT IFNULL(MAX(id), 0)
  INTO
    @maxid
  FROM
    t_temp_res;


  loop_lable1:
  LOOP

    
    IF @offset > @maxid THEN
      LEAVE loop_lable1;
    END IF;

    SELECT showtype
         , dishid
         , dishunit
    INTO
      v_showtype, v_dishid, v_dishunit
    FROM
      t_temp_res
    WHERE
      id = @offset;


    IF v_showtype = 0 THEN
      SELECT GROUP_CONCAT(t.value_detail SEPARATOR '|')
      INTO
        v_value_detail
      FROM
        (
        SELECT CONCAT(statistictime, ',', dishnum) AS value_detail
        FROM
          t_temp_res_detail
        WHERE
          dishid = v_dishid
          AND dishunit = v_dishunit
        ORDER BY
          statistictime ASC) t;

    ELSEIF v_showtype = 1 THEN
      SELECT GROUP_CONCAT(t.value_detail SEPARATOR '|')
      INTO
        v_value_detail
      FROM
        (
        SELECT CONCAT(statistictime, ',', dishprice) AS value_detail
        FROM
          t_temp_res_detail
        WHERE
          dishid = v_dishid
          AND dishunit = v_dishunit
        ORDER BY
          statistictime ASC) t;
    ELSE
      LEAVE loop_lable1;
    END IF;

    
    UPDATE t_temp_res
    SET
      detail_num = v_value_detail
    WHERE
      id = @offset;

    
    SET @offset = @offset + @increment;
  END LOOP;
  

  
  SELECT a.showtype
       , CONCAT(b.title, '(', a.dishunit, ')') title
       , a.total_num
       , a.detail_num
  FROM
    t_temp_res a, t_dish b
  WHERE
    a.dishid = b.dishid;










END$$

DELIMITER ;