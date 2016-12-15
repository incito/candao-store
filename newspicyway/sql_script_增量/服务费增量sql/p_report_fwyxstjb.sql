DELIMITER $$


DROP PROCEDURE IF EXISTS `p_report_fwyxstjb`$$

CREATE PROCEDURE `p_report_fwyxstjb`(IN pi_branchid INT(11),
IN pi_ksrq DATETIME, 
IN pi_jsrq DATETIME, 
IN pi_fwyxm VARCHAR(30), 
IN pi_smcp VARCHAR(300), 
IN pi_dqym INT, 
IN pi_myts INT, 
OUT po_errmsg VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '服务员销售统计表'
label_main:
BEGIN
  DECLARE v_waiter_name VARCHAR(300);
  DECLARE v_dish_name VARCHAR(30);
  DECLARE v_date_start DATETIME;
  DECLARE v_date_end DATETIME;
  DECLARE v_current_page INT;
  DECLARE v_nums_page INT;

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
    orderid VARCHAR(50),
    userid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

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
    orderid VARCHAR(50),
    dishid VARCHAR(50),
    primarykey VARCHAR(50),
    superkey VARCHAR(50),
    dishnum DOUBLE(13,2),
    dishtype INT,
    orderprice DECIMAL(10, 2),
    dishunit VARCHAR(300),
    begintime VARCHAR(50),
    pricetype VARCHAR(50),
    debitamount DOUBLE(13,2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_order_detail
    SELECT
      tod.orderid,
      tod.dishid,
      tod.primarykey,
      tod.superkey,
      tod.dishnum,
      tod.dishtype,
      tod.orderprice,
      tod.dishunit,
      tod.begintime,
      tod.pricetype,
      tod.debitamount
    FROM t_order_detail tod,
         t_temp_order too
    WHERE tod.orderid = too.orderid;

  
  DELETE
    FROM t_temp_order_detail
  WHERE dishtype = '2' AND primarykey <> superkey;

  
  DELETE
    FROM t_temp_order_detail
  WHERE orderprice IS NULL;

  SELECT
    DATE_FORMAT(tod.begintime,'%Y-%m-%d') AS currdate,
    too.orderid,
    too.userid,
    tbu.NAME,
    td.title,
    tod.orderprice,
    tod.dishunit,
    td.dishid,
    SUM(tod.pricetype = 1) AS present,
    SUM(tod.dishnum*tod.orderprice<>tod.debitamount) AS discount,
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
END$$

DELIMITER ;