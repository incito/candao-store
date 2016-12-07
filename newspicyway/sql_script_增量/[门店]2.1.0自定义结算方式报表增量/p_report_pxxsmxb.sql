DELIMITER $$


DROP PROCEDURE IF EXISTS `p_report_pxxsmxb`$$

CREATE PROCEDURE `p_report_pxxsmxb`(IN  pi_branchid INT(11),IN  pi_sb       SMALLINT,IN  pi_ksrq     DATETIME,IN  pi_jsrq     DATETIME,IN  pi_pl       VARCHAR(50),IN  pi_lx       VARCHAR(10),OUT po_errmsg   VARCHAR(100))
label_main:
BEGIN
  DECLARE v_date_start  DATETIME;
  DECLARE v_date_end    DATETIME;
  DECLARE v_sum         INT;
	DECLARE v_total_count DOUBLE(13, 2);
	DECLARE v_total_custnum_count DOUBLE(13, 2);
	DECLARE v_total_shouldmount_count DOUBLE(13, 2);
  DECLARE v_canju_mount DOUBLE(13, 2); -- 餐具金额
	DECLARE v_canju_orignalprice DOUBLE(13, 2);
	DECLARE v_canju_debitamount DOUBLE(13, 2);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SELECT NULL;
    GET DIAGNOSTICS CONDITION 1 po_errmsg = MESSAGE_TEXT;
  END;
	
	SET v_date_start = pi_ksrq; 
  SET v_date_end = pi_jsrq; 
  SET @@max_heap_table_size = 1024 * 1024 * 400;
  SET @@tmp_table_size = 1024 * 1024 * 400;

  IF pi_branchid IS NULL THEN
    SELECT NULL;
    SET po_errmsg = '分店ID输入不能为空';
    LEAVE label_main;
  END IF;
	
	DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    id VARCHAR(50),
    itemDesc VARCHAR(50),
    dishtype INT,
    dishtypetitle VARCHAR(50),
    number DOUBLE(13, 2),
    thousandstimes DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
		debitamount DOUBLE(13, 2),
    turnover DOUBLE(13, 2),
    SHARE DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
	
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
    ispot TINYINT,
    parentkey VARCHAR(40),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50),
    orignalprice DOUBLE(13, 2),
		debitamount DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_order_detail
  SELECT a.orderid
       , a.dishnum
       , a.dishid
       , a.dishtype
       , a.ispot
       , a.parentkey
       , a.childdishtype
       , a.primarykey
       , a.superkey
       , a.orignalprice 
			 , a.debitamount
  FROM
    t_temp_order b, t_order_detail a
  WHERE
    b.orderid = a.orderid;

	SELECT IFNULL(SUM(dishnum), 0)
  INTO
    v_total_count
  FROM
    t_temp_order_detail;

	IF v_total_count <= 0 THEN
    SELECT NULL;
    SET po_errmsg = '数据为空，无查询结果';
    LEAVE label_main;
  END IF;
	SELECT IFNULL(SUM(custnum), 0)
  INTO
    v_total_custnum_count
  FROM
    t_temp_order;
	
	IF v_total_custnum_count <= 0 THEN
    SELECT NULL;
    SET po_errmsg = '数据为空，无查询结果';
    LEAVE label_main;
  END IF;
	SELECT IFNULL(SUM(dishnum*orignalprice), 0)
  INTO
    v_total_shouldmount_count
  FROM
    t_temp_order_detail
	WHERE dishtype <> 2 OR ( dishtype = 2 AND superkey <> primarykey);
	IF v_total_shouldmount_count <= 0 THEN
    SELECT NULL;
    SET po_errmsg = '数据为空，无查询结果';
    LEAVE label_main;
  END IF;

	INSERT INTO t_temp_res (id, number,orignalprice,debitamount)
  SELECT b.columnid
       , IFNULL(SUM(a.dishnum), 0), IFNULL(SUM(a.dishnum*a.orignalprice), 0),IFNULL(SUM(a.debitamount),0)
  FROM
    t_temp_order_detail a, t_dish_dishtype b
  WHERE
    a.dishid = b.dishid
  GROUP BY
    b.columnid
  ORDER BY b.columnid;
	
	UPDATE t_temp_res t, t_basicdata a
  SET
    t.itemDesc = a.itemDesc
  WHERE
    t.Id = a.id
    AND a.status = 1;
	UPDATE t_temp_res
  SET
    SHARE = number / v_total_count * 100,thousandstimes= number/v_total_custnum_count * 1000,turnover = orignalprice/v_total_shouldmount_count*100;

	IF pi_pl = -1 OR pi_pl = 'DISHES_98' THEN
    SELECT IFNULL(SUM(dishnum), 0),IFNULL(SUM(dishnum*orignalprice), 0),IFNULL(SUM(debitamount),0)
    INTO
      v_sum,v_canju_mount,v_canju_debitamount
    FROM
      t_temp_order_detail
    WHERE
      dishid = 'DISHES_98';
    IF v_sum > 0 THEN
      INSERT INTO t_temp_res VALUES ('DISHES_98', '餐具', NULL, NULL, v_sum,v_sum/v_total_custnum_count*1000,v_canju_mount,v_canju_debitamount,v_canju_mount/v_total_shouldmount_count*100,v_sum / v_total_count * 100); -- 字段问题
    END IF;
  END IF;
  COMMIT;


	IF pi_pl = -1 THEN
    SELECT *
    FROM
      t_temp_res;
  ELSE 
		SELECT *
		FROM
			t_temp_res
		WHERE id = pi_pl;
  END IF;
END$$

DELIMITER ;