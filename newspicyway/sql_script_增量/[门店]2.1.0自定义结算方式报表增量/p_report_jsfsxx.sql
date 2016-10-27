DELIMITER $$

USE `newspicyway`$$

DROP PROCEDURE IF EXISTS `p_report_jsfsxx`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_jsfsxx`(IN  pi_branchid VARCHAR(50), 
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
  DECLARE v_branchid   VARCHAR(50) DEFAULT NULL;
  DECLARE v_branchid_cur   VARCHAR(50);
  DECLARE v_branchname_cur   VARCHAR(50);
  DECLARE v_brandid  VARCHAR(128); 
  DECLARE v_marketid  VARCHAR(128); 
  DECLARE v_areaid  VARCHAR(128); 
  DECLARE v_jde  VARCHAR(128); 
  DECLARE v_brand_name  VARCHAR(128); 
  DECLARE v_market_name  VARCHAR(128); 
  DECLARE v_area_name  VARCHAR(128); 
  DECLARE v_datetime  VARCHAR(128); 
  DECLARE v_hdmc  VARCHAR(50); 
  
  
  DECLARE v_pname        VARCHAR(50);
  DECLARE v_ptype        CHAR(4);
  DECLARE v_payway       INT;
  DECLARE v_payways       INT;
  DECLARE v_shouldamount DECIMAL(13, 2);
  DECLARE v_paidinamount DECIMAL(13, 2);
  DECLARE v_inflated     DECIMAL(13, 2); 
  DECLARE v_shouldamount_total     DECIMAL(13, 2); 
  DECLARE v_paidinamount_total     DECIMAL(13, 2); 
  DECLARE v_payways_total     	   DECIMAL(13, 2); 
  DECLARE v_inflated_total     	   DECIMAL(13, 2); 
  
  DECLARE v_fetch_done          BOOL DEFAULT FALSE; 
  
  DECLARE v_paywaypercent DECIMAL(5, 2); 
  DECLARE v_shouldpercent DECIMAL(5, 2); 
  DECLARE v_paidinpercent DECIMAL(5, 2); 
  
  
  DECLARE cur_p_detail CURSOR FOR SELECT DISTINCT pname
                                                , ptype
                                                , payway
                                  FROM
                                    t_temp_res_detail_sub;

  
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SELECT NULL;
    GET DIAGNOSTICS CONDITION 1 po_errormsg = MESSAGE_TEXT;
  END;
  
    
  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; 
  END;
  
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

  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;

  
  SET v_date_start = pi_ksrq;
  SET v_date_end = pi_jsrq;
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_branch;
  CREATE TEMPORARY TABLE t_temp_branch
  (
	branchid VARCHAR(50),
    branchname VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  
  CREATE UNIQUE INDEX ix_t_temp_branch_branchid ON t_temp_branch (branchid);  
  
   IF pi_branchid > -1 THEN
	
	INSERT INTO t_temp_branch SELECT branchid,branchname  FROM t_branch WHERE branchid =CONCAT(pi_branchid) OR branchname LIKE CONCAT('%',pi_branchid,'%'); 
	SELECT IFNULL(COUNT(1),0) INTO v_branchid FROM t_temp_branch;
	IF v_branchid = 0 THEN 
		SELECT NULL;
		SET po_errormsg = '分店ID不存在';
		LEAVE label_main;
	END IF;
  END IF;
  
  
  IF v_branchid IS NULL THEN 
	  INSERT INTO t_temp_branch
	  SELECT branchid
		   , branchname
	  FROM
		t_branch;
  END IF;
 
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50),
	branchid INT(11),
	begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_sb > -1 THEN
    INSERT INTO t_temp_order
    SELECT orderid,t_temp_branch.branchid,begintime
    FROM
      t_order USE INDEX (IX_t_order_begintime), t_temp_branch
    WHERE
      t_temp_branch.branchid = t_order.branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND shiftid = pi_sb
      AND orderstatus = 3;
  ELSE
    INSERT INTO t_temp_order
    SELECT orderid,t_temp_branch.branchid,begintime
    FROM
      t_order USE INDEX (IX_t_order_begintime), t_temp_branch
    WHERE
      t_temp_branch.branchid = t_order.branchid
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
    coupondetailid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_settlement_detail
  SELECT b.orderid
       , b.payway
       , b.payamount
       , b.couponNum
       , b.bankcardno
       , b.coupondetailid
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
    ptypename VARCHAR(32) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  INSERT INTO t_temp_res_detail
  SELECT a.orderid
       , a.payway
       , a.payamount
       , a.couponNum
       , b.pname
       , b.ptype
       , b.ptypename
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
  FROM
    t_temp_settlement_detail a
  WHERE
    a.payway = 12;
	
	CREATE INDEX ix_t_temp_res_detail ON t_temp_res_detail (pname, ptype, payway);
  
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid;
  CREATE TEMPORARY TABLE t_temp_orderid
  (
	orderid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_orderid_orderid ON t_temp_orderid (orderid);
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail_sub;
  CREATE TEMPORARY TABLE t_temp_order_detail_sub
  (
    orderid VARCHAR(50),
    dishnum DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_order_detail_sub_orderid ON t_temp_order_detail_sub (orderid);
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail_sub_new;
  CREATE TEMPORARY TABLE t_temp_order_detail_sub_new
  (
    orderid VARCHAR(50),
    dishnum DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_order_detail_sub_new_orderid ON t_temp_order_detail_sub_new (orderid);
  
    
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail_sub;
  CREATE TEMPORARY TABLE t_temp_settlement_detail_sub
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    couponNum INT,
    bankcardno VARCHAR(50),
    coupondetailid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  CREATE INDEX ix_t_temp_settlement_detail_sub_payway ON t_temp_settlement_detail_sub (payway); 
  
  CREATE INDEX ix_t_temp_settlement_detail_sub_orderid ON t_temp_settlement_detail_sub (orderid);  
  
    
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail_sub_new;
  CREATE TEMPORARY TABLE t_temp_settlement_detail_sub_new
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    couponNum INT,
    bankcardno VARCHAR(50),
    coupondetailid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_settlement_detail_sub_new_payway ON t_temp_settlement_detail_sub_new (payway); 
  
  CREATE INDEX ix_t_temp_settlement_detail_sub_new_orderid ON t_temp_settlement_detail_sub_new (orderid);   
  
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail_sub;
  CREATE TEMPORARY TABLE t_temp_res_detail_sub
  (
    orderid VARCHAR(50),
    payway INT, 
    payamount DOUBLE(13, 2), 
    couponNum INT, 
    pname VARCHAR(128), 
    ptype CHAR(4), 
    ptypename VARCHAR(32) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  CREATE INDEX ix_t_temp_res_detail_sub ON t_temp_res_detail_sub (pname, ptype, payway);
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
	DATETIME VARCHAR(50),
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
    ptype CHAR(4), 
    ptypename VARCHAR(32), 
    payway INT, 
    paywaydesc VARCHAR(50), 
    couponNum INT, 
    payamount DECIMAL(13, 2), 
    shouldamount DECIMAL(13, 2)DEFAULT 0.00, 
    paidinamount DECIMAL(13, 2)DEFAULT 0.00, 
	paywaypercent DECIMAL(5, 2)DEFAULT 0.00, 
	shouldpercent DECIMAL(5, 2)DEFAULT 0.00, 
	paidinpercent DECIMAL(5, 2) DEFAULT 0.00, 
    PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_sub;
  CREATE TEMPORARY TABLE t_temp_res_sub
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
	DATETIME VARCHAR(50),
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
    ptype CHAR(4), 
    ptypename VARCHAR(32), 
    payway INT, 
    paywaydesc VARCHAR(50), 
    couponNum INT, 
    payamount DECIMAL(13, 2), 
    shouldamount DECIMAL(13, 2) DEFAULT 0.00, 
    paidinamount DECIMAL(13, 2) DEFAULT 0.00, 
	paywaypercent DECIMAL(5, 2) DEFAULT 0.00, 
	shouldpercent DECIMAL(5, 2) DEFAULT 0.00, 
	paidinpercent DECIMAL(5, 2) DEFAULT 0.00, 
    PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
 
  BEGIN
	DECLARE cur_branchid CURSOR FOR SELECT branchid,branchname FROM t_temp_branch;
	
	OPEN cur_branchid;
	
	
	IF DATEDIFF(v_date_end,v_date_start) > 0 THEN 
		SET v_datetime = CONCAT(DATE_FORMAT(v_date_start,'%Y-%m-%d'),' - ',DATE_FORMAT(v_date_end,'%Y-%m-%d'));
	ELSE
		SET v_datetime = CONCAT(DATE_FORMAT(v_date_start,'%Y-%m-%d'));
	END IF;
	
	
	lable_fetch_branchid_loop:
		LOOP
		
			FETCH cur_branchid INTO v_branchid_cur,v_branchname_cur;
			IF v_fetch_done THEN
				  LEAVE lable_fetch_branchid_loop;
				END IF;
				
			
			SELECT IFNULL(branch_id,0)
			INTO v_jde
			FROM t_branch_code a WHERE a.branch_id_code = v_branchid_cur;
				
			
			TRUNCATE t_temp_orderid;
			
			
			INSERT INTO t_temp_orderid
			SELECT a.orderid  FROM t_temp_order a
			WHERE a.branchid = v_branchid_cur;
			
			
			
			
			TRUNCATE t_temp_order_detail_sub;
			
			INSERT INTO t_temp_order_detail_sub
				SELECT a.* FROM t_temp_order_detail a,t_temp_orderid b
				WHERE a.orderid = b.orderid;

			
			TRUNCATE t_temp_settlement_detail_sub;
			
			INSERT INTO t_temp_settlement_detail_sub
				SELECT a.* FROM t_temp_settlement_detail a,t_temp_orderid b
				WHERE a.orderid = b.orderid;
				
			
			TRUNCATE t_temp_res_detail_sub;

			INSERT INTO t_temp_res_detail_sub
				SELECT a.* FROM t_temp_res_detail a,t_temp_orderid b
				WHERE a.orderid = b.orderid;
			
			
			TRUNCATE t_temp_orderid;
			
			INSERT INTO t_temp_orderid 
				SELECT DISTINCT orderid
				FROM t_temp_res_detail_sub;
				
			
			SELECT IFNULL(SUM(a.orignalprice * a.dishnum), 0)
			INTO
			  v_shouldamount_total
			FROM
			  t_temp_order_detail_sub a , t_temp_orderid b
			WHERE
			  a.orderid = b.orderid;
					  
			
			SELECT IFNULL(SUM(a.payamount), 0)
			INTO
				v_paidinamount_total
			FROM
				t_temp_settlement_detail_sub a,t_temp_orderid b
			WHERE
				a.orderid = b.orderid
			AND
				a.payway IN (SELECT itemid FROM v_revenuepayway); 
			  
			
			SELECT IFNULL(SUM(a.Inflated), 0)
			INTO
			  v_inflated_total
			FROM
			  t_temp_order_member a, t_temp_orderid b
			WHERE
			  a.orderid = b.orderid;
			  
			
			SELECT IFNULL(COUNT(DISTINCT orderid ),0)
			INTO 
				v_payways_total
			FROM
				t_temp_orderid a;
			
			TRUNCATE t_temp_res_sub;
			
			
			IF pi_hdmc != '-1' THEN
				SELECT pname INTO v_hdmc FROM t_temp_preferential WHERE pid = pi_hdmc ;
				SET v_fetch_done = FALSE;
				IF v_hdmc IS  NULL THEN
					SET v_hdmc = pi_hdmc;
				END IF;
				DELETE
				FROM
					t_temp_res_detail_sub
				WHERE
					pname NOT LIKE CONCAT('%',pi_hdmc,'%') AND pname NOT LIKE CONCAT('%',v_hdmc,'%');
			END IF;

			IF pi_jsfs != -1 THEN
				DELETE
				FROM
					t_temp_res_detail_sub
				WHERE
					payway != pi_jsfs;
			END IF;
			
			
			OPEN cur_p_detail;
			
			lable_fetch_loop:
			  LOOP

				FETCH cur_p_detail INTO v_pname, v_ptype, v_payway;
				IF v_fetch_done THEN
				  SET v_fetch_done = FALSE;
				  LEAVE lable_fetch_loop;
				END IF;
				
			    INSERT INTO t_temp_res_sub (pname, ptype, payway, couponNum, payamount)
			    SELECT pname
				   , ptype
				   , payway
				   , IFNULL(SUM(couponNum), 0)
				   , IFNULL(SUM(payamount), 0)
			    FROM
				  t_temp_res_detail_sub
			    GROUP BY
				  pname
			    , ptype
			    , payway
				HAVING pname = v_pname AND ptype = v_ptype AND payway = v_payway;
				  
			  END LOOP;

			  
			  CLOSE cur_p_detail;
			  
		  TRUNCATE t_temp_orderid;

		  
		  SET @offset = @@auto_increment_offset;
		  SET @increment = @@auto_increment_increment;
		  SELECT IFNULL(MAX(id), 0)
		  INTO
			@maxid
		  FROM
			t_temp_res_sub;
			
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
			  t_temp_res_sub
			WHERE
			  id = @offset;

			TRUNCATE t_temp_orderid;

			
			INSERT INTO t_temp_orderid
			SELECT DISTINCT orderid
			FROM
			  t_temp_res_detail_sub
			WHERE
			  pname = v_pname
			  AND ptype = v_ptype
			  AND payway = v_payway;
			
			TRUNCATE t_temp_order_detail_sub_new;
			
			INSERT INTO t_temp_order_detail_sub_new
			SELECT a.*
			FROM
			  t_temp_order_detail_sub a USE INDEX (ix_t_temp_order_detail_sub_orderid) JOIN t_temp_orderid b USE INDEX (ix_t_temp_orderid_orderid)
			ON
			  a.orderid = b.orderid;
			  
			TRUNCATE t_temp_settlement_detail_sub_new;
			
			INSERT INTO t_temp_settlement_detail_sub_new
			SELECT a.*
			FROM
			  t_temp_settlement_detail_sub a USE INDEX (ix_t_temp_settlement_detail_sub_orderid,ix_t_temp_settlement_detail_sub_payway) JOIN t_temp_orderid b USE INDEX (ix_t_temp_orderid_orderid)
			ON
			  a.orderid = b.orderid
			WHERE a.payway IN (SELECT itemid FROM v_revenuepayway);
			
			
			SELECT IFNULL(SUM(a.orignalprice * a.dishnum), 0)
			INTO
			  v_shouldamount
			FROM
			  t_temp_order_detail_sub_new a;

			
			SELECT IFNULL(SUM(a.payamount), 0)
			INTO
			  v_paidinamount
			FROM
			  t_temp_settlement_detail_sub_new a;

			
			SELECT IFNULL(SUM(a.Inflated), 0)
			INTO
			  v_inflated
			FROM
			  t_temp_order_member a JOIN t_temp_orderid b USE INDEX (ix_t_temp_orderid_orderid)
			ON
			  a.orderid = b.orderid;
		  
			
			SELECT IFNULL(COUNT(DISTINCT orderid),0)
			INTO v_payways
			FROM t_temp_orderid a;
		
			
			SET v_paywaypercent = v_payways / v_payways_total ;
			
			
			SET v_shouldpercent = v_shouldamount / v_shouldamount_total ;
			
			
			SET v_paidinpercent = (v_paidinamount - v_inflated ) / ( v_paidinamount_total - v_inflated_total ) ;

			
			UPDATE t_temp_res_sub
			SET
			  brandid=v_brandid,brandname=v_brand_name,marketid=v_marketid,market=v_market_name,areaid=v_areaid,
			  AREA=v_area_name,managePattern='1',managePatternid ='自营',jde=v_jde,DATETIME=v_datetime,
			  shouldamount = v_shouldamount, paidinamount = v_paidinamount - v_inflated, branchid = v_branchid_cur,
			  branchname = v_branchname_cur, paywaypercent = v_paywaypercent, shouldpercent = v_shouldpercent,
			  paidinpercent = v_paidinpercent
			WHERE
			  id = @offset;

			
			SET @offset = @offset + @increment;
		  END LOOP;

		  
		  UPDATE t_temp_res_sub a, t_dictionary b
		  SET
			a.paywaydesc = b.itemDesc
		  WHERE
			a.payway = b.itemid
			AND TYPE = 'PAYWAY';

		  
		  UPDATE t_temp_res_sub a, t_temp_preferential b
		  SET
			a.ptypename = b.ptypename
		  WHERE
			a.ptype = b.ptype;

		  
		  UPDATE t_temp_res_sub a
		  SET
			a.ptypename = '雅座优惠券'
		  WHERE
			a.ptype = '99';
		  
		INSERT INTO t_temp_res (brandid,brandname,marketid,market,areaid,AREA,managePattern,managePatternid ,jde,DATETIME,
			branchid,branchname,pname,ptype,ptypename,
			payway,paywaydesc,couponNum,payamount,shouldamount,paidinamount,
			paywaypercent,shouldpercent,paidinpercent)
		SELECT  
			a.brandid,a.brandname,a.marketid,a.market,a.areaid,a.area,a.managePattern,a.managePatternid ,a.jde,a.datetime,
			a.branchid,a.branchname,a.pname,a.ptype,a.ptypename,
			a.payway,a.paywaydesc,a.couponNum,a.payamount,a.shouldamount,a.paidinamount,
			a.paywaypercent,a.shouldpercent,a.paidinpercent
		FROM t_temp_res_sub a;
		  
		END LOOP lable_fetch_branchid_loop;
	
	CLOSE cur_branchid;
	
  END;
  
		  SELECT 
				brandid,
				brandname,
				marketid,
				market,
				areaid,
				AREA,
				managePattern,
				managePatternid,
				jde jdecode,
				DATETIME durdate,
				branchid storeid,
				branchname store,
				paywaydesc settermenttype,
				pname activityid,
				pname activityname,
				ptype,
				payway settlementno,
				couponNum NO,
				payamount happenmoney,
				shouldamount pullreceivable,
				paidinamount pullactive,
				paywaypercent oftotalsetter,
				shouldpercent ofreceivable,
				paidinpercent ofpullactive
		  FROM
			t_temp_res;
END$$

DELIMITER ;