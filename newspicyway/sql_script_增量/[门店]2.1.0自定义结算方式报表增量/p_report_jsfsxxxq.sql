DELIMITER $$

USE `newspicyway`$$

DROP PROCEDURE IF EXISTS `p_report_jsfsxxxq`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_jsfsxxxq`(IN  pi_branchid INT(11), 
								  IN  pi_jsfs     INT(11),
								  IN  pi_hdmc     VARCHAR(50),
                                  IN  pi_sb       SMALLINT, 
                                  IN  pi_ksrq     DATETIME, 
                                  IN  pi_jsrq     DATETIME, 
								  IN `pi_brandid` INT(11),
								  IN `pi_marketid` INT(11),
								  IN `pi_areaid` INT(11),
                                  OUT po_errormsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '结算方式信息'
label_main:
BEGIN
  DECLARE v_date_start DATETIME;
  DECLARE v_date_end   DATETIME;
  DECLARE v_branchid   INT(11);
  DECLARE v_branchname VARCHAR(50);
  DECLARE v_brandid  VARCHAR(128); 
  DECLARE v_marketid  VARCHAR(128); 
  DECLARE v_areaid  VARCHAR(128); 
  DECLARE v_jde  VARCHAR(128); 
  DECLARE v_brand_name  VARCHAR(128); 
  DECLARE v_market_name  VARCHAR(128); 
  DECLARE v_area_name  VARCHAR(128); 
  DECLARE v_datetime  VARCHAR(128); 
  
  DECLARE v_pname        VARCHAR(50);
  DECLARE v_ptype        CHAR(4);
  DECLARE v_payway       INT;
  DECLARE v_payways       INT;
  DECLARE v_shouldamount DOUBLE(13, 2);
  DECLARE v_paidinamount DOUBLE(13, 2);
  DECLARE v_inflated     DOUBLE(13, 2); 
  
  DECLARE v_fetch_done          BOOL DEFAULT FALSE; 
  
  
  DECLARE cur_p_detail CURSOR FOR SELECT DISTINCT pname
                                                , ptype
                                                , payway
                                  FROM
                                    t_temp_res_detail;

  
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SELECT NULL;
    GET DIAGNOSTICS CONDITION 1 po_errormsg = MESSAGE_TEXT;
  END;
  
  
  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; 
  END;
  
     
  IF pi_branchid IS NULL THEN
    SELECT NULL;
    SET po_errormsg = '分店ID输入不能为空';
    LEAVE label_main;
  END IF;
  
  IF pi_brandid > -1 THEN
  
	SELECT id,NAME INTO v_brandid,v_brand_name  FROM t_c_brand WHERE id = pi_brandid;
  ELSE
	SELECT id,NAME INTO v_brandid,v_brand_name FROM t_c_brand LIMIT 1;
  END IF;  
  
  IF pi_marketid > -1 THEN
  
	SELECT id,NAME INTO v_marketid,v_market_name  FROM t_c_market WHERE id = pi_marketid;
  ELSE
	SELECT id,NAME INTO v_marketid,v_market_name FROM t_c_market LIMIT 1;
  END IF;  
  
  IF pi_areaid > -1 THEN
  
	SELECT id,NAME INTO v_areaid,v_area_name  FROM t_c_area WHERE id = pi_areaid;
  ELSE
	SELECT id,NAME INTO v_areaid,v_area_name FROM t_c_area LIMIT 1;
  END IF;  
  
  SET v_branchid = pi_branchid;
  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;
  
  SELECT IFNULL(branch_id,0)
  INTO v_jde
  FROM t_branch_code a WHERE a.branch_id_code = v_branchid;

  
  SET v_date_start = pi_ksrq;
  SET v_date_end = pi_jsrq;

  SELECT branchname INTO v_branchname 
  FROM t_branch WHERE branchid = v_branchid;
 
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50),
	begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_sb > -1 THEN
    INSERT INTO t_temp_order
    SELECT orderid,begintime
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      v_branchid = t_order.branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND shiftid = pi_sb
      AND orderstatus = 3;
  ELSE
    INSERT INTO t_temp_order
    SELECT orderid,begintime
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      v_branchid = t_order.branchid
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
  
  CREATE INDEX ix_t_temp_settlement_detail_payway ON t_temp_settlement_detail (payway);  
 
  
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
    ptypename VARCHAR(32), 
	begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  INSERT INTO t_temp_res_detail
  SELECT a.orderid
       , a.payway
       , a.payamount
       , a.couponNum
       , b.pname
       , b.ptype
       , b.ptypename
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
       , '雅座优惠券'
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
  CREATE INDEX ix_t_temp_res_detail ON t_temp_res_detail (pname, ptype, payway);
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
	DATETIME DATE,
	brandid VARCHAR(50),
	brandname VARCHAR(50),
	marketid VARCHAR(50),
	market  VARCHAR(50),
	areaid VARCHAR(50),
	AREA  VARCHAR(50),
	managePattern  VARCHAR(50),
	managePatternid VARCHAR(50),
	jde VARCHAR(50),
	branchid VARCHAR(50),
	branchname VARCHAR(50),
    pname VARCHAR(128), 
    orderid VARCHAR(50), 
    ptype CHAR(4), 
    ptypename VARCHAR(32), 
    couponNum INT, 
    payamount DECIMAL(13, 2), 
    shouldamount DECIMAL(13, 2), 
    paidinamount DECIMAL(13, 2), 
    PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
 
  
  
  BEGIN
			
			OPEN cur_p_detail;
			
			lable_fetch_loop:
			  LOOP
				FETCH cur_p_detail INTO v_pname, v_ptype, v_payway;
				IF v_fetch_done THEN
				  SET v_fetch_done = FALSE;
				  LEAVE lable_fetch_loop;
				END IF;
				
				INSERT INTO t_temp_res (DATETIME, orderid,pname, payamount, couponNum, shouldamount, paidinamount)
				SELECT DATE_FORMAT(MAX(begintime), '%Y-%m-%d')
					 , orderid
					 , pname
					 , IFNULL(SUM(payamount), 0)
					 , IFNULL(SUM(couponNum), 0)
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

			  
			  UPDATE t_temp_res
			  SET branchid = v_branchid,branchname = v_branchname,
				brandid=v_brandid,brandname=v_brand_name,marketid=v_marketid,market=v_market_name,areaid=v_areaid,
			    AREA=v_area_name,managePattern='1',managePatternid ='自营',jde=v_jde;
			
			  
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
		  
  END;
  
  
		  SELECT 
				DATETIME durdate,
				brandid,
				brandname,
				marketid,
				market ,
				areaid,
				AREA ,
				managePattern,
				managePatternid ,
				jde jdecode,
				branchid,
				branchname store,
				pname activityname,
				orderid orderno,
				couponNum NO,
				payamount happenmoney,
				shouldamount pullreceivable,
				paidinamount pullactive
		  FROM
			t_temp_res;
END$$

DELIMITER ;