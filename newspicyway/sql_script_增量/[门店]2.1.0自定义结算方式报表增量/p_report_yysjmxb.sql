DELIMITER $$

USE `newspicyway`$$

DROP PROCEDURE IF EXISTS `p_report_yysjmxb`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_yysjmxb`(IN  pi_branchid INT(11), 
                                  IN  pi_sb       SMALLINT, 
                                  IN  pi_ksrq     DATETIME, 
                                  IN  pi_jsrq     DATETIME, 
                                  OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '营业数据明细表'
label_main:
BEGIN
  
  
  
  
  


  DECLARE v_date_start            DATETIME;
  DECLARE v_date_end              DATETIME;
  DECLARE v_paidinamount          DOUBLE(13, 2) DEFAULT 0; 

  
  DECLARE v_pa_cash               DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_pa_credit             DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_pa_card               DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_pa_icbc_card          DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_pa_paidinamount       DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_pa_weixin             DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_pa_zhifubao           DOUBLE(13, 2) DEFAULT 0; 

  
  DECLARE v_da_free               DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_da_integralconsum     DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_da_meberTicket        DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_da_discount           DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_da_fraction           DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_da_roundoff           DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_da_give               DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_da_mebervalueadd      DOUBLE(13, 2) DEFAULT 0; 

  
  DECLARE v_sa_ordercount         INT DEFAULT 0; 
  DECLARE v_sa_tableconsumption   DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_sa_settlementnum      INT DEFAULT 0; 
  DECLARE v_sa_shouldamount       DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_sa_shouldaverage      DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_sa_paidinaverage      DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_sa_attendance         DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_sa_overtaiwan         DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_sa_avgconsumtime      DOUBLE(13, 2) DEFAULT 0; 

  
  DECLARE v_ma_total              DOUBLE(13, 2) DEFAULT 0; 

  
  DECLARE v_oa_shouldamount       DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_oa_paidinamount       DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_oa_mebervalueadd      DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_oa_ordercount         INT(11) DEFAULT 0; 
  DECLARE v_oa_avgprice           DOUBLE(13, 2) DEFAULT 0; 

  
  DECLARE v_other_tablecount      INT DEFAULT 0; 
  DECLARE v_other_tableperson     INT DEFAULT 0; 
  DECLARE v_other_vipordercount   INT DEFAULT 0; 
  DECLARE v_other_viporderpercent DOUBLE(13, 2) DEFAULT 0; 

  
  DECLARE v_closed_bill_nums          INT DEFAULT 0; 
  DECLARE v_closed_bill_shouldamount  DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_closed_person_nums        INT DEFAULT 0; 
  DECLARE v_no_bill_nums          INT DEFAULT 0; 
  DECLARE v_no_bill_shouldamount  DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_no_person_nums        INT DEFAULT 0; 
  DECLARE v_bill_nums             INT DEFAULT 0; 
  DECLARE v_bill_shouldamount     DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_person_nums           INT DEFAULT 0; 
  DECLARE v_zaitaishu             INT DEFAULT 0; 
  DECLARE v_kaitaishu             INT DEFAULT 0; 



  
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


  
  SET v_date_start = pi_ksrq; 
  SET v_date_end = pi_jsrq; 

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50),
    womanNum TINYINT UNSIGNED,
    childNum TINYINT UNSIGNED,
    mannum TINYINT UNSIGNED,
    ordertype TINYINT,
    begintime DATETIME,
    endtime DATETIME,
    orderstatus TINYINT UNSIGNED,
    custnum TINYINT UNSIGNED,
    shiftid TINYINT UNSIGNED
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_sb > -1 THEN
    INSERT INTO t_temp_order
    SELECT orderid
         , womanNum
         , childNum
         , mannum
         , ordertype
         , begintime
         , endtime
         , orderstatus
         , custnum
         , shiftid
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      branchid = pi_branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND shiftid = pi_sb;
      
  ELSE
    INSERT INTO t_temp_order
    SELECT orderid
         , womanNum
         , childNum
         , mannum
         , ordertype
         , begintime
         , endtime
				 , orderstatus
         , custnum
         , shiftid
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      branchid = pi_branchid
      AND begintime BETWEEN v_date_start AND v_date_end; 
      
  END IF;
  
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
  CREATE TEMPORARY TABLE t_temp_order_detail
  (
    orderid VARCHAR(50),
    dishnum DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
    ordertype TINYINT,
    pricetype CHAR(1),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50),
	  dishtype TINYINT
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_order_detail
  SELECT b.orderid
       , b.dishnum
       , b.orignalprice
       , a.ordertype
       , b.pricetype
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

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    couponid VARCHAR(50),
    ordertype TINYINT,
    isviporder TINYINT,
    membercardno VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_settlement_detail
  SELECT b.orderid
       , b.payway
       , b.payamount
       , b.couponid
       , a.ordertype
       , CASE
         WHEN b.payway = 8 AND CHAR_LENGTH(b.membercardno) > 1 THEN
           1
         ELSE
           0
         END
       , b.membercardno
  FROM
    t_temp_order a, t_settlement_detail b
  WHERE
    a.orderid = b.orderid
  AND a.orderstatus = 3;


  
  DELETE
  FROM
    t_temp_settlement_detail
  WHERE
    isviporder = 0
    AND payamount = 0;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  CREATE TEMPORARY TABLE t_temp_order_member
  (
    orderid VARCHAR(50),
    Inflated DOUBLE(13, 2),
    netvalue DOUBLE(13, 2),
    ordertype TINYINT
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
       , b.netvalue
       , a.ordertype
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid
  AND a.orderstatus = 3;


  
  
  SELECT SUM(
         CASE ordertype
         WHEN 0 THEN
           1
         ELSE
           0
         END) 
         , IFNULL(SUM(
         CASE ordertype
         WHEN 0 THEN
           mannum + womanNum + childNum
         ELSE
           0
         END), 0) 
         , SUM(
         CASE ordertype
         WHEN 0 THEN
           0
         ELSE
           1
         END) 
  INTO
    v_sa_ordercount, v_sa_settlementnum, v_oa_ordercount
  FROM
    t_temp_order
  WHERE orderstatus = 3;


  
  SELECT IFNULL(SUM(
         CASE b.ordertype
         WHEN 0 THEN
           b.orignalprice * b.dishnum
         ELSE
           0
         END), 0) 
         , IFNULL(SUM(
         CASE b.ordertype
         WHEN 0 THEN
           0
         ELSE
           b.orignalprice * b.dishnum
         END), 0) 
         , IFNULL(SUM(
         CASE b.pricetype
         WHEN '1' THEN
           b.orignalprice * b.dishnum
         ELSE
           0
         END), 0) 
  INTO
    v_sa_shouldamount, v_oa_shouldamount, v_da_give
  FROM 
    t_temp_order a, t_temp_order_detail b
  WHERE
    a.orderid = b.orderid
    AND b.orignalprice > 0
    AND a.orderstatus = 3;

  
  SELECT IFNULL(SUM(Inflated), 0)
       , IFNULL(SUM(
         CASE ordertype
         WHEN 0 THEN
           0
         ELSE
           Inflated
         END), 0)
  INTO
    v_da_mebervalueadd, v_oa_mebervalueadd
  FROM
    t_temp_order_member;

  
  SELECT IFNULL(SUM(payamount), 0)
  INTO
    v_oa_paidinamount
  FROM
    t_temp_settlement_detail
  WHERE
    payway IN (SELECT itemid FROM v_revenuepayway)  
    AND ordertype > 0;

  
  SET v_oa_paidinamount = v_oa_paidinamount - v_oa_mebervalueadd;

  
  SELECT IFNULL(SUM(
         CASE
         WHEN payway = 0 THEN 
           payamount
         ELSE
           0
         END), 0)
       , IFNULL(SUM(
         CASE
         WHEN payway = 1 AND membercardno<>'1' AND payamount<>0 THEN 
           payamount
         ELSE
           0
         END), 0)
       , IFNULL(SUM(
         CASE
         WHEN payway = 1 AND membercardno='1' AND payamount<>0 THEN 
           payamount
         ELSE
           0
         END), 0)
       , IFNULL(SUM(
         CASE
         WHEN payway IN(5,13) AND payamount > 0 THEN 
           payamount
         ELSE
           0
         END), 0)
       , IFNULL(SUM(
         CASE
         WHEN payway = 8 THEN 
           payamount
         ELSE
           0
         END), 0)
       , IFNULL(SUM(
         CASE
         WHEN payway = 11 THEN 
           payamount
         ELSE
           0
         END), 0)
       , IFNULL(SUM(
         CASE
         WHEN payway = 12 THEN 
           payamount
         ELSE
           0
         END), 0)
       , IFNULL(SUM(
         CASE
         WHEN payway = 7 THEN 
           payamount
         ELSE
           0
         END), 0)
       , 0 - IFNULL(SUM(
         CASE
         WHEN payway = 20 THEN 
           payamount
         ELSE
           0
         END), 0)
       , SUM(isviporder) 
       , IFNULL(SUM(
         CASE
         WHEN payway = 17 THEN 
           payamount
         ELSE
           0
         END), 0)
       , IFNULL(SUM(
         CASE
         WHEN payway = 18 THEN 
           payamount
         ELSE
           0
         END), 0)
  INTO
    v_pa_cash, v_pa_card, v_pa_icbc_card, v_pa_credit, v_pa_paidinamount, v_da_integralconsum, v_da_meberTicket, v_da_fraction, v_da_roundoff, v_other_vipordercount, v_pa_weixin, v_pa_zhifubao
  FROM
    t_temp_settlement_detail;

  
  SET v_paidinamount = v_pa_cash + v_pa_credit + v_pa_card + v_pa_paidinamount - v_da_mebervalueadd + v_pa_icbc_card + v_pa_weixin + v_pa_zhifubao;

  
  SELECT IFNULL(SUM(a.payamount), 0)
  INTO
    v_da_discount
  FROM
    t_temp_settlement_detail a, t_p_preferential_activity b
  WHERE
    a.couponid = b.id
    AND b.type = '02';

  
  

  
  SET v_da_free = v_sa_shouldamount + v_oa_shouldamount - v_paidinamount - v_da_roundoff - v_da_integralconsum - v_da_meberTicket - v_da_discount - v_da_fraction - v_da_give - v_da_mebervalueadd;

  
  SELECT COUNT(1)
       , SUM(a.personNum)
  INTO
    v_other_tablecount, v_other_tableperson
  FROM
    t_table a, t_tablearea b
  WHERE
    a.areaid = b.areaid
    AND b.branchid = pi_branchid
    AND a.status IN (0, 1);

  
	SELECT IFNULL(SUM(too.custnum),0),    
    IFNULL(COUNT(too.orderid),0)
  INTO v_closed_person_nums,v_closed_bill_nums
	FROM
	  t_temp_order too
  WHERE too.orderstatus = 3;

  
  SELECT IFNULL(SUM(tod.orignalprice * tod.dishnum),0) 
  INTO v_closed_bill_shouldamount
  FROM t_temp_order_detail tod
  WHERE tod.orderid IN (SELECT orderid FROM t_temp_order WHERE orderstatus = 3);

  
  SELECT IFNULL(SUM(too.custnum),0),
    IFNULL(COUNT(too.orderid),0)
  INTO v_no_person_nums,v_no_bill_nums
  FROM
	  t_temp_order too
  WHERE too.orderstatus = 0;

  
  SELECT IFNULL(SUM(tod.orignalprice * tod.dishnum),0) 
  INTO v_no_bill_shouldamount
  FROM t_temp_order_detail tod
  WHERE tod.orderid IN (SELECT orderid FROM t_temp_order WHERE orderstatus = 0);

  
IF pi_sb > -1 THEN
  SELECT COUNT(orderid)
  INTO v_zaitaishu
  FROM t_order USE INDEX (IX_t_order_begintime)
  WHERE branchid = pi_branchid AND orderstatus <> 2 AND shiftid = pi_sb
  AND ((begintime BETWEEN v_date_start AND v_date_end) OR (endtime BETWEEN v_date_start AND v_date_end)); 
ELSE
  SELECT COUNT(orderid)
  INTO v_zaitaishu
  FROM t_order USE INDEX (IX_t_order_begintime)
  WHERE branchid = pi_branchid AND orderstatus <> 2
  AND ((begintime BETWEEN v_date_start AND v_date_end) OR (endtime BETWEEN v_date_start AND v_date_end)); 
END IF;

  
IF pi_sb > -1 THEN
  SELECT COUNT(orderid)
  INTO v_kaitaishu
  FROM t_temp_order
  WHERE orderstatus <> 2 AND shiftid = pi_sb; 
ELSE
  SELECT COUNT(orderid)
  INTO v_kaitaishu
  FROM t_temp_order
  WHERE orderstatus <> 2;
END IF;

  
  SET v_bill_nums = v_closed_bill_nums + v_no_bill_nums;
  
  
  SET v_bill_shouldamount = v_closed_bill_shouldamount + v_no_bill_shouldamount;
   
  
  SET v_person_nums = v_closed_person_nums + v_no_person_nums;

  IF v_sa_ordercount > 0 THEN
    
    SET v_sa_attendance = v_sa_settlementnum / (TIMESTAMPDIFF(DAY, v_date_start, v_date_end) + 1) / v_other_tableperson * 100;

    
    SET v_sa_overtaiwan = v_sa_ordercount / (TIMESTAMPDIFF(DAY, v_date_start, v_date_end) + 1) / v_other_tablecount * 100;

    
    SET v_sa_tableconsumption = (v_paidinamount - v_oa_paidinamount) / v_sa_ordercount;

    
    SET v_sa_shouldaverage = v_sa_shouldamount / v_sa_settlementnum;

    
    SET v_sa_paidinaverage = (v_paidinamount - v_oa_paidinamount) / v_sa_settlementnum; 

    
    SELECT SUM(TIMESTAMPDIFF(SECOND, begintime, endtime)) / v_sa_ordercount / 60
    INTO
      v_sa_avgconsumtime
    FROM
      t_temp_order
    WHERE
      ordertype = 0
      AND endtime IS NOT NULL
      AND orderstatus = 3;
  END IF;

  IF v_oa_ordercount > 0 THEN
    
    SET v_oa_avgprice = v_oa_paidinamount / v_oa_ordercount;
  END IF;

  
  SET v_ma_total = v_da_meberTicket + v_da_integralconsum + v_pa_paidinamount;

  
  SET v_other_viporderpercent = v_other_vipordercount / (v_sa_ordercount + v_oa_ordercount) * 100;

  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    shouldamount DOUBLE(13, 2), 
    paidinamount DOUBLE(13, 2), 
    discountamount DOUBLE(13, 2), 
    cash DOUBLE(13, 2), 
    credit DOUBLE(13, 2), 
    card DOUBLE(13, 2), 
    icbccard DOUBLE(13, 2), 
    weixin DOUBLE(13, 2), 
    zhifubao DOUBLE(13, 2), 
    merbervaluenet DOUBLE(13, 2), 
    free DOUBLE(13, 2), 
    integralconsum DOUBLE(13, 2), 
    meberTicket DOUBLE(13, 2), 
    discount DOUBLE(13, 2), 
    fraction DOUBLE(13, 2), 
    give DOUBLE(13, 2), 
    roundoff DOUBLE(13, 2), 
    mebervalueadd DOUBLE(13, 2), 
    tablecount DOUBLE(13, 2), 
    tableconsumption DOUBLE(13, 2), 
    settlementnum DOUBLE(13, 2), 
    shouldaverage DOUBLE(13, 2), 
    paidinaverage DOUBLE(13, 2), 
    attendance DOUBLE(13, 2), 
    overtaiwan DOUBLE(13, 2), 
    avgconsumtime DOUBLE(13, 2), 
    shouldamount_normal DOUBLE(13, 2), 
    shouldamount_takeout DOUBLE(13, 2), 
    paidinamount_takeout DOUBLE(13, 2), 
    ordercount_takeout DOUBLE(13, 2), 
    avgprice_takeout DOUBLE(13, 2), 
    vipordercount INT(11), 
    viporderpercent DOUBLE(13, 2), 
    membertotal DOUBLE(13, 2), 
    closedbillnums INT(11), 
    closedbillshouldamount DOUBLE(13, 2), 
    closedpersonnums  INT(11), 
    nobillnums    INT(11), 
		nobillshouldamount  DOUBLE(13, 2), 
		nopersonnums  INT(11), 
		billnums   INT(11), 
		billshouldamount  DOUBLE(13, 2), 
		personnums  DOUBLE(13, 2), 
		zaitaishu   INT(11), 
		kaitaishu    INT(11) 
	) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_res VALUES (v_sa_shouldamount + v_oa_shouldamount, v_paidinamount, v_sa_shouldamount + v_oa_shouldamount - v_paidinamount, v_pa_cash, v_pa_credit, v_pa_card, v_pa_icbc_card, v_pa_weixin, v_pa_zhifubao, v_pa_paidinamount - v_da_mebervalueadd, v_da_free, v_da_integralconsum, v_da_meberTicket, v_da_discount, v_da_fraction, v_da_give, v_da_roundoff, v_da_mebervalueadd, v_sa_ordercount, v_sa_tableconsumption, v_sa_settlementnum, v_sa_shouldaverage, v_sa_paidinaverage, v_sa_attendance, v_sa_overtaiwan, v_sa_avgconsumtime, v_sa_shouldamount, v_oa_shouldamount, v_oa_paidinamount, v_oa_ordercount, v_oa_avgprice, v_other_vipordercount, v_other_viporderpercent, v_ma_total,v_closed_bill_nums,v_closed_bill_shouldamount,v_closed_person_nums, v_no_bill_nums, v_no_bill_shouldamount, v_no_person_nums, v_bill_nums, v_bill_shouldamount, v_person_nums, v_zaitaishu, v_kaitaishu);
  SELECT *
  FROM
    t_temp_res;







END$$

DELIMITER ;