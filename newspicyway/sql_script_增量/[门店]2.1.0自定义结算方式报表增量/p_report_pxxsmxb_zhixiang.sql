DELIMITER $$


DROP PROCEDURE IF EXISTS `p_report_pxxsmxb_zhixiang`$$

CREATE PROCEDURE `p_report_pxxsmxb_zhixiang`(IN  pi_branchid INT(11),IN  pi_sb       SMALLINT,IN  pi_ksrq     DATETIME,IN  pi_jsrq     DATETIME,IN  pi_pl       VARCHAR(50),IN  pi_pxlx     INT,IN isPOS INT,OUT po_errmsg   VARCHAR(100))
label_main:
BEGIN 
  DECLARE v_total_count DOUBLE(13, 2) DEFAULT 0;
  DECLARE v_title       VARCHAR(300); 
  DECLARE v_dishNo      VARCHAR(50); 
  DECLARE v_price       DOUBLE(13, 2); 
  DECLARE v_unit        VARCHAR(300); 
  DECLARE v_number      DOUBLE(13, 2); 
	DECLARE v_danpin_number DOUBLE(13, 2);
	DECLARE v_taocan_number DOUBLE(13, 2);
  DECLARE v_share       DOUBLE(13, 2); 
  DECLARE v_sum_price   DOUBLE(13, 2); 
	DECLARE v_debitamount DOUBLE(13, 2);
  DECLARE v_fetch_done  INT DEFAULT FALSE;
  DECLARE v_dishid      VARCHAR(50);
  DECLARE v_dishclass   VARCHAR(50);
  DECLARE v_dishtype    INT;
  DECLARE v_date_start  DATETIME;
  DECLARE v_date_end    DATETIME;
  DECLARE v_total_custnum_count    DOUBLE(13, 2); -- 来客总人数
  DECLARE v_total_shouldmount_count    DOUBLE(13, 2); -- 应收总额
  DECLARE v_canju_mount DOUBLE(13, 2); -- 餐具金额
	DECLARE v_canju_amount DOUBLE(13, 2);
  DECLARE cur_dish_detail CURSOR FOR SELECT a.dishid
                                          , a.dishunit
                                          , a.dishtype
                                          , b.columnid
                                          , IFNULL(SUM(CASE WHEN a.dishtype <> 2 THEN a.dishnum ELSE 0 END), 0)
                                          , IFNULL(SUM(CASE WHEN a.dishtype = 2 THEN a.dishnum ELSE 0 END), 0)
                                          , IFNULL(MAX(a.orignalprice), 0)
																					, IFNULL(SUM(a.orignalprice*a.dishnum), 0)
																					, IFNULL(SUM(a.debitamount),0)
                                     FROM
                                       t_temp_order_detail a, t_dish_dishtype b
                                     WHERE
                                       a.dishid = b.dishid
                                     GROUP BY
                                       a.dishid
                                     , a.dishunit
                                     , a.dishtype
                                     , b.columnid
                                     ORDER BY
                                       NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; 
  END;
  
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SELECT NULL;
    GET DIAGNOSTICS CONDITION 1 po_errmsg = MESSAGE_TEXT;
  END;
  
  IF pi_sb IS NULL OR pi_ksrq IS NULL OR pi_jsrq IS NULL OR pi_pl IS NULL THEN
    SELECT NULL;
    SET po_errmsg = '传入参数不能为空';
    LEAVE label_main;
  END IF;
  IF pi_branchid IS NULL THEN
    SELECT NULL;
    SET po_errmsg = '分店ID输入不能为空';
    LEAVE label_main;
  END IF;
  SET @@max_heap_table_size = 1024 * 1024 * 400;
  SET @@tmp_table_size = 1024 * 1024 * 400;
  
  SET v_date_start = pi_ksrq; 
  SET v_date_end = pi_jsrq; 
  
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
    dishunit VARCHAR(300),
    orignalprice DOUBLE(13, 2),
		debitamount DOUBLE(13, 2),
    ispot TINYINT,
    parentkey VARCHAR(40),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  IF isPOS=0 THEN
		INSERT INTO t_temp_order_detail
		SELECT a.orderid
				 , a.dishnum
				 , a.dishid
				 , a.dishtype
				 , a.dishunit
				 , a.orignalprice
				 , a.debitamount
				 , a.ispot
				 , a.parentkey
				 , a.childdishtype
				 , a.primarykey
				 , a.superkey
		FROM
			t_temp_order b, t_order_detail a
		WHERE
			b.orderid = a.orderid;
	ELSE
		INSERT INTO t_temp_order_detail
		SELECT a.orderid
				 , a.dishnum
				 , a.dishid
				 , a.dishtype
				 , a.dishunit
				 , a.orignalprice
				 , a.debitamount
				 , a.ispot
				 , a.parentkey
				 , a.childdishtype
				 , a.primarykey
				 , a.superkey
		FROM
			t_temp_order b, t_order_detail a
		WHERE
			b.orderid = a.orderid
			AND (a.dishtype<>2 OR (a.dishtype=2 AND a.superkey<>a.primarykey));
	END IF;
	
    #AND a.orignalprice > 0;
   -- 计算套餐金额开始
   #DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   #CREATE TEMPORARY TABLE t_temp_taocan
  #(
    #primarykey VARCHAR(50),
    #orignalprice DOUBLE(13, 2)
 # ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
 # INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
 # update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 
  # 删除套餐明细
  # delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;
  CREATE INDEX ix_t_tmp_order_detail_dishid ON t_temp_order_detail (dishid);
  
  
  UPDATE t_temp_order_detail a, t_dish b
  SET
    a.dishtype = b.dishtype
  WHERE
    a.dishid = b.dishid
    AND a.dishtype IS NULL;
  
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
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    columnid VARCHAR(50), 
    dishtype INT, 
    title VARCHAR(300), 
    dishNo VARCHAR(50), 
    price DOUBLE(13, 2), 
    unit VARCHAR(300), 
    danpinnumber DOUBLE(13, 2), 
    taocannumber DOUBLE(13, 2), 
    thousandstimes DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
		debitamount DOUBLE(13, 2),
    turnover DOUBLE(13, 2),
    SHARE DOUBLE(13, 2) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  OPEN cur_dish_detail;
read_loop:
  LOOP
    FETCH cur_dish_detail INTO v_dishid, v_unit, v_dishtype, v_dishclass, v_danpin_number,v_taocan_number, v_price,v_sum_price,v_debitamount;
    IF v_fetch_done THEN
      LEAVE read_loop;
    END IF;
    
    SELECT dishNo
         , title
    INTO
      v_dishNo, v_title
    FROM
      t_dish
    WHERE
      dishid = v_dishid
    LIMIT
      1;
		SET v_number = v_danpin_number + v_taocan_number;
    INSERT INTO t_temp_res VALUES (v_dishclass, v_dishtype, v_title, v_dishNo, v_price, v_unit, v_danpin_number,v_taocan_number,ROUND(v_number / v_total_custnum_count * 1000, 2),v_sum_price,v_debitamount,v_sum_price/v_total_shouldmount_count*100, ROUND(v_number / v_total_count * 100, 2));
  END LOOP;
  COMMIT;
  
  CLOSE cur_dish_detail;
  
  IF pi_pl = -1 OR pi_pl = 'DISHES_98' THEN
    SELECT SUM(CASE WHEN dishtype <> 2 THEN dishnum ELSE 0 END) , SUM(CASE WHEN dishtype = 2 THEN dishnum ELSE 0 END)
         , IFNULL(MAX(orignalprice), 0),IFNULL(SUM(dishnum*orignalprice), 0),IFNULL(SUM(debitamount),0)
    INTO
      @danpin,@taocan, @price,@sumprice,@debit
    FROM
      t_temp_order_detail
    WHERE
      dishid = 'DISHES_98';
    SELECT IFNULL(title, '餐具')
         , IFNULL(dishno, '')
    INTO
      @title, @dishno
    FROM
      t_dish
    WHERE
      dishid = 'DISHES_98'
    LIMIT
      1;
		SET @cnt = @danpin + @taocan;
    IF @cnt > 0 THEN
      INSERT INTO t_temp_res VALUES ('DISHES_98', 0, @title, @dishno, @price, '份', @danpin,@taocan,ROUND(@cnt / v_total_custnum_count * 1000, 2),@sumprice,@debit,@sumprice/v_total_shouldmount_count*100, ROUND(@cnt / v_total_count * 100, 2));
    END IF;
  END IF;
  
  IF pi_pl != '-1' THEN
    SELECT title
         , dishNo
         , price
         , unit
         , danpinnumber
				 , taocannumber
         , SHARE
         , thousandstimes
         , orignalprice
				 , debitamount
         , turnover
    FROM
      t_temp_res
    WHERE
      columnid = pi_pl;
  ELSE
    SELECT title
         , dishNo
         , price
         , unit
         , danpinnumber
				 , taocannumber
         , SHARE
         , thousandstimes
         , orignalprice
				 , debitamount
         , turnover
    FROM
      t_temp_res;
  END IF;
END$$

DELIMITER ;