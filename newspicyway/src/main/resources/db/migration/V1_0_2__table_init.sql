/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.6.25-log : Database - newspicyway
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
 

/*!50106 set global event_scheduler = 1*/;

/* Event structure for event `event1` */

/*!50106 DROP EVENT IF EXISTS `event1`*/;

DELIMITER $$

/*!50106 CREATE DEFINER=`root`@`localhost` EVENT `event1` ON SCHEDULE EVERY 3 MINUTE STARTS '2015-08-04 17:53:28' ON COMPLETION PRESERVE ENABLE DO BEGIN
  declare branchcount int; 
  declare v_branchid int;
  declare v_menuid varchar(50);
  select count(1) into branchcount from t_branch_info;
  if branchcount>0 then
    select branchid into v_branchid from t_branch_info;
    update t_menu tm SET tm.status='1' WHERE tm.status='2' AND tm.effecttime<=NOW() and tm.menuid IN (SELECT tmb.menuid FROM t_menu_branch tmb WHERE tmb.branchid=v_branchid);
    select menuid into v_menuid from v_currmenu;
    update t_menu set status='4' where status='1' and menuid!=v_menuid and menuid IN (SELECT tmb.menuid FROM t_menu_branch tmb WHERE tmb.branchid=v_branchid);
  else
    UPDATE t_menu tm SET tm.status='1' WHERE tm.status='2' AND tm.effecttime<=NOW();
    end if;
END */$$
DELIMITER ;

/* Function  structure for function  `fristPinyin` */

/*!50003 DROP FUNCTION IF EXISTS `fristPinyin` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `fristPinyin`(P_NAME VARCHAR(255)) RETURNS varchar(255) CHARSET utf8
    SQL SECURITY INVOKER
BEGIN

    DECLARE V_RETURN VARCHAR(255);

    SET V_RETURN = ELT(INTERVAL(CONV(HEX(left(CONVERT(P_NAME USING gbk),1)),16,10), 

        0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7, 

        0xBFA6,0xC0AC,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,

        0xC8F6,0xCBFA,0xCDDA,0xCEF4,0xD1B9,0xD4D1),    

    'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P','Q','R','S','T','W','X','Y','Z');

    RETURN V_RETURN;

END */$$
DELIMITER ;

/* Function  structure for function  `f_cal_dish_count` */

/*!50003 DROP FUNCTION IF EXISTS `f_cal_dish_count` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `f_cal_dish_count`(i_coupondetailid varchar(100), i_orderid varchar(50)) RETURNS int(11)
    SQL SECURITY INVOKER
BEGIN
  DECLARE v_count int;
  SELECT
    COUNT(1) INTO v_count
  FROM t_order_detail tod
  WHERE tod.orderid = i_orderid
  AND tod.debitamount != NULL
  AND tod.debitamount > 0
  AND FIND_IN_SET(tod.dishid, (SELECT
      dishid
    FROM t_dish_cal_factor_temp tdcft
    WHERE tdcft.coupondetailid = i_coupondetailid));

  RETURN v_count;
END */$$
DELIMITER ;

/* Function  structure for function  `getPY` */

/*!50003 DROP FUNCTION IF EXISTS `getPY` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `getPY`(in_string VARCHAR(1000)) RETURNS text CHARSET utf8
    SQL SECURITY INVOKER
BEGIN

DECLARE tmp_str VARCHAR(1000) charset utf8 DEFAULT '' ; 

DECLARE tmp_len SMALLINT DEFAULT 0;

DECLARE tmp_char VARCHAR(2) charset gbk DEFAULT '';

DECLARE tmp_rs VARCHAR(1000) charset gbk DEFAULT '';

DECLARE tmp_cc VARCHAR(2) charset gbk DEFAULT '';

SET tmp_str = in_string;

SET tmp_len = LENGTH(tmp_str);

WHILE tmp_len > 0 DO 

SET tmp_char = LEFT(tmp_str,1);

SET tmp_cc = tmp_char;

IF LENGTH(tmp_char)>1 THEN

SELECT ELT(INTERVAL(CONV(HEX(tmp_char),16,10),0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7,0xBFA6,0xC0AC 

,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,0xC8F6,0xCBFA,0xCDDA ,0xCEF4,0xD1B9,0xD4D1), 

'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P','Q','R','S','T','W','X','Y','Z') INTO tmp_cc; 

END IF; 

SET tmp_rs = CONCAT(tmp_rs,tmp_cc);

SET tmp_str = SUBSTRING(tmp_str,2);

SET tmp_len = LENGTH(tmp_str);

END WHILE; 

IF ISNULL(tmp_rs) THEN
RETURN '';
ELSE
RETURN tmp_rs;
END IF;

END */$$
DELIMITER ;

/* Function  structure for function  `getseqno` */

/*!50003 DROP FUNCTION IF EXISTS `getseqno` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `getseqno`(seq_name varchar(20)) RETURNS varchar(100) CHARSET utf8
    SQL SECURITY INVOKER
BEGIN

  DECLARE valuestr varchar(200);

  DECLARE maxvalues int;

  DECLARE v_branchid varchar(10);

  DECLARE v_current_date varchar(20);

  DECLARE v_max_order_id varchar(30);

  DECLARE v_order_id_suffix varchar(10);

  DECLARE v_order_id_seq varchar(30);

  DECLARE v_count_order int;



  SET v_current_date = DATE_FORMAT(NOW(), '%Y%m%d');
  SELECT
    LPAD(branchid, 6, '0') INTO v_branchid
  FROM t_branch_info;

  SELECT
    COUNT(1) INTO v_count_order
  FROM t_order
  WHERE orderid LIKE '%' || CONCAT('H', DATE_FORMAT(NOW(), '%Y%m%d'), v_branchid) || '%';

  IF v_count_order > 0 THEN
    SELECT
      MAX(orderid) INTO v_max_order_id
    FROM t_order
    WHERE orderid LIKE '%' || CONCAT('H', DATE_FORMAT(NOW(), '%Y%m%d'), v_branchid) || '%';
	
	IF LOCATE('-', v_max_order_id) > 0 THEN
		SELECT substring_index(v_max_order_id, '-', 1) INTO v_max_order_id FROM dual;
		SELECT
			RIGHT(v_max_order_id, 6) INTO v_max_order_id
		FROM dual;
	ELSE
		SELECT
			RIGHT(v_max_order_id, 6) INTO v_max_order_id
		FROM dual;
	END IF;

    SELECT
      v_max_order_id + 1 INTO v_order_id_seq
    FROM dual;
    UPDATE sequence
    SET val = CAST(v_order_id_seq AS SIGNED)
    WHERE name = seq_name;
    SET valuestr = CONCAT('H', DATE_FORMAT(NOW(), '%Y%m%d'), v_branchid, LPAD(v_order_id_seq, 6, '0'));
  ELSE

    SELECT
      MAX(val) INTO maxvalues
    FROM sequence
    WHERE name = seq_name;
    SELECT
      LPAD(maxvalues + 1, 6, '0') INTO valuestr;
    UPDATE sequence
    SET val = maxvalues + 1
    WHERE name = seq_name;
    SET valuestr = CONCAT('H', DATE_FORMAT(NOW(), '%Y%m%d'), v_branchid, valuestr);
  END IF;

  RETURN valuestr;

END */$$
DELIMITER ;

/* Function  structure for function  `tableseqno` */

/*!50003 DROP FUNCTION IF EXISTS `tableseqno` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `tableseqno`(seq_name char (20)) RETURNS int(11)
    SQL SECURITY INVOKER
begin 

 UPDATE tableseqno SET val=last_insert_id(val+1) WHERE name=seq_name; 

 RETURN last_insert_id(); 

end */$$
DELIMITER ;

/* Procedure structure for procedure `p_calcDishPrice` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_calcDishPrice` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_calcDishPrice`(IN i_orderId varchar(255))
    SQL SECURITY INVOKER
BEGIN


  DECLARE v_message varchar(255);
  DECLARE v_flag int;

  CALL newspicyway.p_cal_dish_debit_amount(i_orderId, v_message, v_flag);

END */$$
DELIMITER ;

/* Procedure structure for procedure `p_calcDishSetPrice` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_calcDishSetPrice` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_calcDishSetPrice`(IN i_orderId varchar(255))
    SQL SECURITY INVOKER
BEGIN
      DECLARE v_message varchar(255);
  DECLARE v_flag int;

  CALL newspicyway.p_cal_dish_debit_amount(i_orderId, v_message, v_flag);
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_caletableamount` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_caletableamount` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_caletableamount`(IN v_orderid varchar(50))
    SQL SECURITY INVOKER
BEGIN
  DECLARE v_couponname varchar(100);
  DECLARE v_fulldiscountrate decimal(10,2);
  declare v_dueamount  decimal(10,2);
  declare v_ssamount decimal(10,2);
  declare v_gzamount decimal(10,2);
  declare v_ymamount decimal(10,2);
  declare v_originalOrderAmount decimal(10,2);
  select a.fulldiscountrate,couponname INTO  v_fulldiscountrate,v_couponname from t_order a    where orderid=v_orderid;
  
  set v_fulldiscountrate = 1;
  update t_order_detail set discountrate=1 where orderid=v_orderid; 
  update t_order_detail set payamount=0, discountamount=0,predisamount=0  where ((status<>5 and  (not (orderprice>0))) or (status=5))  and orderid=v_orderid;
  update t_order_detail set payamount=orderprice*dishnum*(case when discountrate<=0 then 1 else discountrate end), discountamount=orderprice*dishnum*(1-case when discountrate<=0 then 1 else discountrate end),predisamount=orderprice*dishnum  where    status<>5 and  orderprice>0  and orderid=v_orderid;
  select IFNULL(sum(payamount),0) into v_dueamount from t_order_detail where   status<>5 and  orderid=v_orderid ;
  select IFNULL(sum(payamount),0) into v_ssamount from t_settlement_detail where orderid=v_orderid and payway in(0,1,5,8,13,17,18,30);
  select IFNULL(sum(payamount),0) into v_gzamount from t_settlement_detail where orderid=v_orderid and payway in(5,13);
  select IFNULL(sum(payamount),0) into v_ymamount from t_settlement_detail where orderid=v_orderid and payway in(6,12);
  select IFNULL(sum( tod.dishnum * tod.orignalprice ),0) INTO v_originalOrderAmount FROM t_order_detail AS tod WHERE ( tod.dishtype = 0 OR ( tod.dishtype = 2 AND EXISTS ( SELECT tdg.dishid FROM t_dish_group tdg WHERE tod.dishid = tdg.dishid ) ) ) AND tod.orderid = v_orderid;
  update t_order set dueamount=v_dueamount,wipeamount=v_dueamount-floor(v_dueamount),ssamount=v_ssamount,gzamount=v_gzamount,ymamount=v_ymamount,freeamount=v_originalOrderAmount where orderid=v_orderid;
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_cal_dishset_price` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_cal_dishset_price` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_cal_dishset_price`()
    SQL SECURITY INVOKER
    COMMENT '计算套餐的实收金额'
BEGIN
  DECLARE v_fetch_done           NUMERIC DEFAULT 0;
  DECLARE v_primarykey           VARCHAR(50);
  DECLARE v_dishset_debitamount  DECIMAL(10, 2);
  DECLARE v_dishset_orignalprice DECIMAL(10, 2);

  DECLARE cur_dish_set CURSOR FOR
  SELECT *
  FROM
    t_temp_dishset tod;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_fetch_done = 1;


  
  UPDATE t_temp_order_detail
  SET
    debitamount = dishnum * orderprice
  WHERE
    dishtype = '2'
    AND primarykey = superkey
    AND debitamount IS NULL;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_dishset;
  CREATE TEMPORARY TABLE t_temp_dishset
  (
    primarykey VARCHAR(50),
    debitamount DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  INSERT INTO t_temp_dishset
  SELECT primarykey
       , debitamount
  FROM
    t_temp_order_detail tod
  WHERE
    dishtype = '2'
    AND primarykey = superkey;

  OPEN cur_dish_set;

loop_lable:
  LOOP
    FETCH cur_dish_set INTO v_primarykey, v_dishset_debitamount;

    
    SELECT sum(ifnull(dishnum * orignalprice, 0))
    INTO
      v_dishset_orignalprice
    FROM
      t_temp_order_detail
    WHERE
      superkey = v_primarykey
      AND primarykey != v_primarykey;

    
    IF v_fetch_done THEN
      LEAVE loop_lable;
    END IF;

    UPDATE t_temp_order_detail
    SET
      debitamount = dishnum * orignalprice / v_dishset_orignalprice * v_dishset_debitamount
    WHERE
      superkey = v_primarykey
      AND primarykey != v_primarykey;
  END LOOP;
  CLOSE cur_dish_set;
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_cal_dish_column` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_cal_dish_column` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_cal_dish_column`(IN i_cloumnId  VARCHAR(800),
                                   IN i_flag      INT(2),
                                   IN i_dz_amount DECIMAL(10, 2)
                                   )
    SQL SECURITY INVOKER
    COMMENT '处理dish 分类的实收字段'
BEGIN
  DECLARE v_sum_dishprice DECIMAL(10, 2);

  
  


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_primarykey;
  CREATE TEMPORARY TABLE t_temp_primarykey
  (
    primarykey VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_primarykey
  SELECT a.primarykey
  FROM
    t_temp_order_detail a, t_dish_dishtype b
  WHERE
    a.dishid = b.dishid
    AND find_in_set(b.columnid, i_cloumnId)
    AND a.orderprice IS NULL
    AND a.dishtype = '1';

  DROP TEMPORARY TABLE IF EXISTS t_temp_dish_hotpot;
  CREATE TEMPORARY TABLE t_temp_dish_hotpot
  (
    primarykey VARCHAR(50),
    orderprice DECIMAL(10, 2),
    debitprice DECIMAL(10, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  INSERT INTO t_temp_dish_hotpot
  SELECT a.primarykey
       , a.dishnum * a.orderprice
       , a.debitamount
  FROM
    t_temp_order_detail a, t_temp_primarykey b
  WHERE
    a.parentkey = b.primarykey;


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_dish;
  CREATE TEMPORARY TABLE t_temp_dish
  (
    primarykey VARCHAR(50),
    orderprice DECIMAL(10, 2),
    debitprice DECIMAL(10, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  INSERT INTO t_temp_dish
  
  SELECT a.primarykey
       , a.dishnum * a.orderprice
       , a.debitamount
  FROM
    t_temp_order_detail a, t_dish_dishtype b
  WHERE
    a.dishid = b.dishid
    AND find_in_set(b.columnid, i_cloumnId)
    AND orderprice > 0
  UNION
  SELECT primarykey
       , orderprice
       , debitprice
  FROM
    t_temp_dish_hotpot;


  
  IF i_flag = 0 THEN

    
    SELECT sum(
           CASE
           WHEN debitprice IS NOT NULL THEN
             debitprice
           ELSE
             orderprice
           END)
    INTO
      v_sum_dishprice
    FROM
      t_temp_dish;

    
    IF v_sum_dishprice IS NOT NULL AND v_sum_dishprice != 0 THEN
      UPDATE t_temp_order_detail a, t_temp_dish b
      SET
        a.debitamount =
        CASE
        WHEN a.debitamount IS NOT NULL THEN
          a.debitamount - (a.debitamount / v_sum_dishprice) * i_dz_amount
        ELSE
          a.orderprice * a.dishnum - (a.dishnum * a.orderprice / v_sum_dishprice) * i_dz_amount
        END
      WHERE
        a.primarykey = b.primarykey;
    END IF;

  
  ELSE
    
    SELECT sum(
           CASE
           WHEN debitamount IS NOT NULL THEN
             debitamount
           ELSE
             dishnum * orderprice
           END)
    INTO
      v_sum_dishprice
    FROM
      t_temp_order_detail
    WHERE
      orderprice > 0
      AND primarykey NOT IN (SELECT primarykey
                             FROM
                               t_temp_dish);


    
    IF v_sum_dishprice IS NOT NULL AND v_sum_dishprice != 0 THEN

      UPDATE t_temp_order_detail a
      SET
        a.debitamount =
        CASE
        WHEN a.debitamount IS NOT NULL THEN
          a.debitamount - (a.debitamount / v_sum_dishprice) * i_dz_amount
        ELSE
          a.orderprice * a.dishnum - (a.dishnum * a.orderprice / v_sum_dishprice) * i_dz_amount
        END
      WHERE
        orderprice > 0
        AND primarykey NOT IN (SELECT primarykey
                               FROM
                                 t_temp_dish);
    END IF;
  END IF;


END */$$
DELIMITER ;

/* Procedure structure for procedure `p_cal_dish_debit_amount` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_cal_dish_debit_amount` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_cal_dish_debit_amount`(IN  i_orderid VARCHAR(255),
                                         OUT o_message VARCHAR(255),
                                         OUT o_flag    INT
                                         )
    SQL SECURITY INVOKER
    COMMENT '计算单品的实收金额'
label_main:
BEGIN DECLARE v_fetch_done     NUMERIC DEFAULT 0;
  DECLARE v_coupondetailid VARCHAR(100);
  DECLARE v_caltype        INT(2);
  DECLARE v_dishtype       INT(2);
  DECLARE v_dishnum        INT(11);
  DECLARE v_dishid         VARCHAR(5000);
  DECLARE v_columnid       VARCHAR(5000);
  DECLARE v_orderdetailid  VARCHAR(100);
  DECLARE v_primarykey     VARCHAR(100);

  DECLARE v_sdetailid      VARCHAR(50);
  DECLARE v_payamount      DECIMAL(10, 2);
  DECLARE v_payway         INT(2);
  DECLARE v_couponNum      INT(3);
  DECLARE v_ys_amount      DECIMAL(10, 2);
  DECLARE v_bankcardno     VARCHAR(100);
  DECLARE v_coupon_count   INT;
  DECLARE v_xz_amount      DECIMAL(10, 2);

  DECLARE cur_order CURSOR FOR
  SELECT t.sdetailid
       , t.payamount
       , t.bankcardno
       , t.payway
       , t.couponNum
       , t.coupondetailid
       , CASE
         WHEN caltype IS NULL THEN
           99
         ELSE
           caltype
         END caltype
       , b.dishtype
       , b.dishid
       , b.columnid
  FROM
    t_temp_settlement_detail t
  LEFT JOIN t_dish_cal_factor_temp b
  ON
  t.coupondetailid = b.coupondetailid
  ORDER BY
    caltype;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_fetch_done = 1;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
  CREATE TEMPORARY TABLE t_temp_order_detail
  (
    orderid VARCHAR(50),
    dishid VARCHAR(50),
    dishtype CHAR(1),
    dishnum DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
    orderprice DOUBLE(13, 2),
    debitamount DOUBLE(13, 2),
    pricetype CHAR(1),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    parentkey VARCHAR(50),
    dishunit VARCHAR(100),
    ismaster VARCHAR(3),
    superkey VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  INSERT INTO t_temp_order_detail
  SELECT orderid
       , dishid
       , dishtype
       , dishnum
       , orignalprice
       , orderprice
       , NULL
       , pricetype
       , childdishtype
       , primarykey
       , parentkey
       , dishunit
       , ismaster
       , superkey
  FROM
    t_order_detail t
  WHERE
    t.orderid = i_orderid;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail(
    sdetailid VARCHAR(50),
    orderid VARCHAR(50),
    payamount DECIMAL(10, 2),
    bankcardno VARCHAR(100),
    payway INT(2),
    couponNum INT(3),
    coupondetailid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_settlement_detail
  SELECT sdetailid
       , orderid
       , payamount
       , bankcardno
       , payway
       , couponNum
       , coupondetailid
  FROM
    t_settlement_detail tsd
  WHERE
    tsd.orderid = i_orderid
    AND tsd.payway IN (6, 7, 11, 12)
    AND tsd.payamount != 0;

  
  INSERT INTO t_temp_settlement_detail 
  SELECT sdetailid
       , orderid
       , payamount
       , bankcardno
       , payway
       , couponNum
       , coupondetailid
  FROM
    t_settlement_detail tsd
  WHERE
    tsd.orderid = i_orderid
    AND tsd.payway = 5
    AND tsd.payamount < 0;

  
  OPEN cur_order;

loop_label:
  LOOP

    FETCH cur_order INTO v_sdetailid, v_payamount, v_bankcardno, v_payway, v_couponNum, v_coupondetailid, v_caltype, v_dishtype, v_dishid, v_columnid;

    
    IF v_fetch_done THEN
      LEAVE loop_label;
    END IF;



    
    
    IF v_caltype = '0' THEN
      CALL p_cal_dish_single(v_dishid, '0', v_payamount);
    
    ELSEIF v_caltype = '1' THEN
      CALL p_cal_double_hotpot(v_payamount);
    
    ELSEIF v_caltype = '2' THEN
      CALL p_cal_dish_single(v_dishid, '1', v_payamount);
    
    ELSEIF v_caltype = '3' THEN
      CALL p_cal_dish_column(v_columnid, '0', v_payamount);
    
    ELSEIF v_caltype = '4' THEN
      CALL p_cal_dish_column(v_columnid, '1', v_payamount);
    
    ELSEIF v_caltype = '5' THEN
      CALL p_cal_whole_order(v_payamount);
    
    ELSE
      CALL p_cal_whole_order(v_payamount);
    END IF;
  END LOOP;
  CLOSE cur_order;

  
  SELECT ifnull(sum(Inflated), 0)
  INTO
    v_xz_amount
  FROM
    t_order_member
  WHERE
    orderid = i_orderId;

  IF v_xz_amount > 0 THEN
    CALL p_cal_whole_order(v_xz_amount);
  END IF;

  
  CALL p_cal_dishset_price();

  
  UPDATE t_temp_order_detail
  SET
    debitamount = orderprice * dishnum
  WHERE
    debitamount IS NULL;

  
  SELECT ifnull(sum(t.payamount) - v_xz_amount, 0)
  INTO
    @amount1
  FROM
    t_settlement_detail t
  WHERE
    t.orderid = i_orderid
    AND t.payamount > 0
    AND t.payway IN (0, 1, 5, 8, 13, 17, 18);

  SELECT ifnull(sum(t.debitamount), 0)
  INTO
    @amount2
  FROM
    t_temp_order_detail t
  WHERE
    t.dishtype = 2
    AND t.primarykey = t.superkey;

  SELECT ifnull(sum(t.debitamount), 0)
  INTO
    @amount3
  FROM
    t_temp_order_detail t;

  SET @amount4 = @amount1 - (@amount3 - @amount2);

  IF @amount4 != 0 THEN
    SELECT primarykey
    INTO
      @key
    FROM
      t_temp_order_detail t
    WHERE
      t.dishtype IN (0, 1)
      OR (t.dishtype = 2
      AND t.primarykey != t.superkey)
    LIMIT
      1;

    UPDATE t_temp_order_detail
    SET
      debitamount = debitamount + @amount4
    WHERE
      primarykey = @key;
  END IF;

  
  UPDATE t_order_detail a, t_temp_order_detail b
  SET
    a.debitamount = b.debitamount
  WHERE
    a.primarykey = b.primarykey;

END */$$
DELIMITER ;

/* Procedure structure for procedure `p_cal_dish_single` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_cal_dish_single` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_cal_dish_single`(IN i_dishid    VARCHAR(800),
                                   IN i_flag      INT(2),
                                   IN i_dz_amount DECIMAL(10, 2)
                                   )
    SQL SECURITY INVOKER
    COMMENT '处理单品的实收字段'
BEGIN
  DECLARE v_sum_dishprice DECIMAL(10, 2);

  
  

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_primarykey;
  CREATE TEMPORARY TABLE t_temp_primarykey
  (
    primarykey VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_primarykey
  SELECT primarykey
  FROM
    t_temp_order_detail
  WHERE
    find_in_set(dishid, i_dishid)
    AND orderprice IS NULL
    AND dishtype = '1';

  DROP TEMPORARY TABLE IF EXISTS t_temp_dish_hotpot;
  CREATE TEMPORARY TABLE t_temp_dish_hotpot
  (
    primarykey VARCHAR(50),
    orderprice DECIMAL(10, 2),
    debitprice DECIMAL(10, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  INSERT INTO t_temp_dish_hotpot
  SELECT a.primarykey
       , a.dishnum * a.orderprice
       , a.debitamount
  FROM
    t_temp_order_detail a, t_temp_primarykey b
  WHERE
    a.parentkey = b.primarykey;


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_dish;
  CREATE TEMPORARY TABLE t_temp_dish
  (
    primarykey VARCHAR(50),
    orderprice DECIMAL(10, 2),
    debitprice DECIMAL(10, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  INSERT INTO t_temp_dish
  
  SELECT primarykey
       , dishnum * orderprice
       , debitamount
  FROM
    t_temp_order_detail
  WHERE
    find_in_set(dishid, i_dishid)
    AND orderprice > 0
  UNION
  SELECT primarykey
       , orderprice
       , debitprice
  FROM
    t_temp_dish_hotpot;

  
  IF i_flag = 0 THEN

    
    SELECT sum(
           CASE
           WHEN debitprice IS NOT NULL THEN
             debitprice
           ELSE
             orderprice
           END)
    INTO
      v_sum_dishprice
    FROM
      t_temp_dish;

    
    IF v_sum_dishprice IS NOT NULL AND v_sum_dishprice != 0 THEN
      UPDATE t_temp_order_detail a, t_temp_dish b
      SET
        a.debitamount =
        CASE
        WHEN a.debitamount IS NOT NULL THEN
          a.debitamount - (a.debitamount / v_sum_dishprice) * i_dz_amount
        ELSE
          a.orderprice * a.dishnum - (a.dishnum * a.orderprice / v_sum_dishprice) * i_dz_amount
        END
      WHERE
        a.primarykey = b.primarykey;
    END IF;


  
  ELSE
    
    SELECT sum(
           CASE
           WHEN debitamount IS NOT NULL THEN
             debitamount
           ELSE
             dishnum * orderprice
           END)
    INTO
      v_sum_dishprice
    FROM
      t_temp_order_detail
    WHERE
      orderprice > 0
      AND primarykey NOT IN (SELECT primarykey
                             FROM
                               t_temp_dish);


    
    IF v_sum_dishprice IS NOT NULL AND v_sum_dishprice != 0 THEN

      UPDATE t_temp_order_detail a
      SET
        a.debitamount =
        CASE
        WHEN a.debitamount IS NOT NULL THEN
          a.debitamount - (a.debitamount / v_sum_dishprice) * i_dz_amount
        ELSE
          a.orderprice * a.dishnum - (a.dishnum * a.orderprice / v_sum_dishprice) * i_dz_amount
        END
      WHERE
        orderprice > 0
        AND primarykey NOT IN (SELECT primarykey
                               FROM
                                 t_temp_dish);
    END IF;
  END IF;
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_cal_double_hotpot` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_cal_double_hotpot` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_cal_double_hotpot`(IN i_dz_amount DECIMAL(10, 2))
    SQL SECURITY INVOKER
    COMMENT '处理整单优惠的情况'
BEGIN

  DECLARE v_sum_dishprice     DECIMAL(10, 2);
  DECLARE v_double_hotpot_key VARCHAR(50) DEFAULT NULL;

  
  SELECT parentkey
  INTO
    v_double_hotpot_key
  FROM
    t_temp_order_detail
  WHERE
    dishtype = 1
    AND orderprice > 0
  GROUP BY
    parentkey
  HAVING
    count(1) = 4
  LIMIT
    1;

  
  IF v_double_hotpot_key IS NOT NULL THEN
    UPDATE t_temp_order_detail
    SET
      debitamount =
      CASE
      WHEN debitamount IS NOT NULL THEN
        debitamount - i_dz_amount / 4
      ELSE
        orderprice * dishnum - i_dz_amount / 4
      END
    WHERE
      parentkey = v_double_hotpot_key;
  END IF;

END */$$
DELIMITER ;

/* Procedure structure for procedure `p_cal_whole_order` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_cal_whole_order` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_cal_whole_order`(IN i_dz_amount DECIMAL(10, 2))
    SQL SECURITY INVOKER
    COMMENT '处理整单优惠的情况'
BEGIN

  DECLARE v_sum_dishprice DECIMAL(10, 2);

  
  SELECT sum(
         CASE
         WHEN debitamount IS NOT NULL THEN
           debitamount
         ELSE
           dishnum * orderprice
         END)
  INTO
    v_sum_dishprice
  FROM
    t_temp_order_detail
  WHERE
    orderprice > 0;

  
  IF v_sum_dishprice IS NOT NULL AND v_sum_dishprice != 0 THEN
    UPDATE t_temp_order_detail
    SET
      debitamount =
      CASE
      WHEN debitamount IS NOT NULL THEN
        debitamount - (debitamount / v_sum_dishprice) * i_dz_amount
      ELSE
        orderprice * dishnum - (dishnum * orderprice / v_sum_dishprice) * i_dz_amount
      END
    WHERE
      orderprice > 0;
  END IF;
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_check_source_data` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_check_source_data` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_check_source_data`(IN pi_branchid  INT,
                                              IN pi_begintime DATETIME,
                                              IN pi_endtime   DATETIME
                                              )
    SQL SECURITY INVOKER
    COMMENT '生成JDE数据前,对原始数据的校验：\n    检查项1：结算方式汇总额（每单） = 品项销售汇总额（每单）'
BEGIN
  DECLARE v_row_count       INT;
  DECLARE v_fetch_times     INT DEFAULT 0;
  DECLARE v_orderid         VARCHAR(50);
  DECLARE v_debitamount_all NUMERIC(8, 2);
  DECLARE cur_order CURSOR FOR SELECT orderid
                               FROM
                                 t_temp_order_check t;

  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    GET DIAGNOSTICS CONDITION 1 @error = MESSAGE_TEXT;
    SELECT 0 AS returnvalue
         , concat('校验异常:', v_orderid, @error) AS errorinfo;
  END;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_check;
  CREATE TEMPORARY TABLE t_temp_order_check
  (
    orderid VARCHAR(50)
  ) ENGINE = HEAP DEFAULT CHARSET = utf8;
  INSERT INTO t_temp_order_check
  SELECT orderid
  FROM
    t_order t
  WHERE
    t.begintime BETWEEN pi_begintime AND pi_endtime
    AND t.orderstatus = 3
    AND t.branchid = pi_branchid;

  SELECT count(1)
  INTO
    v_row_count
  FROM
    t_temp_order_check; 

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail_check;
  CREATE TEMPORARY TABLE t_temp_order_detail_check
  (
    orderid VARCHAR(50),
    dishid VARCHAR(50),
    dishtype INT,
    dishunit VARCHAR(100),
    superkey VARCHAR(255),
    primarykey VARCHAR(255),
    debitamount DECIMAL(20, 2)
  ) ENGINE = HEAP DEFAULT CHARSET = utf8;
  INSERT INTO t_temp_order_detail_check
  SELECT t.orderid
       , t.dishid
       , t.dishtype
       , t.dishunit
       , t.superkey
       , t.primarykey
       , t.debitamount
  FROM
    t_order_detail t, t_temp_order_check b
  WHERE
    t.orderid = b.orderid;

  
  OPEN cur_order;
  WHILE v_fetch_times < v_row_count
  DO
    FETCH cur_order INTO v_orderid;
    SET v_fetch_times = v_fetch_times + 1;

    SELECT ifnull(sum(a.debitamount),0)
    INTO
      v_debitamount_all
    FROM
      t_temp_order_detail_check a
    WHERE
      a.orderid = v_orderid;

    IF v_debitamount_all = 0 THEN
      CALL p_cal_dish_debit_amount(v_orderid,@a,@b);
    END IF;

  END WHILE;
  CLOSE cur_order;

END */$$
DELIMITER ;

/* Procedure structure for procedure `p_endwork` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_endwork` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_endwork`(OUT v_endfinish BIT)
    SQL SECURITY INVOKER
BEGIN
  DECLARE t_error    INTEGER DEFAULT 0;
  DECLARE v_branchid VARCHAR(50);
  DECLARE v_tenantid VARCHAR(50);
  DECLARE v_now      DATETIME;
  DECLARE v_opendate DATETIME;
  DECLARE v_username VARCHAR(20);

  
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
  	set t_error = 1;
  	set v_endfinish = false;
    ROLLBACK;
  END;


  
  SET v_now = now();
  
  SELECT branchid
       , tenantid
  INTO
    v_branchid, v_tenantid
  FROM
    t_branch_info
  LIMIT
    1;

  
  SELECT opendate
       , username
  INTO
    v_opendate, v_username
  FROM
    t_open_log
  LIMIT
    1;



  START TRANSACTION;

  INSERT INTO t_biz_log (id, branchid, tenantid, syn_status, inserttime, open_date, end_date, open_user) VALUES (uuid(), v_branchid, v_tenantid, 1, v_now, v_opendate, v_now, v_username);

  DELETE
  FROM
    t_branch_biz_log;

  INSERT INTO t_branch_biz_log (id, opendate, enddate, openuser, enduser, inserttime, branchid) VALUES (uuid(), v_opendate, v_now, v_username, NULL, v_now, v_branchid);

  DELETE
  FROM
    t_open_log;


  DELETE
  FROM
    t_json_record;

  UPDATE tableseqno
  SET
    val = 2
  WHERE
    name IN ('printobjid', 'printdishid');

  DELETE
  FROM
    t_printobj;

  DELETE
  FROM
    t_printdish;

  UPDATE t_printer
  SET
    printnum = 0;

  UPDATE t_table
  SET
    orderid = '';

  UPDATE t_b_user
  SET
    order_num = NULL, lastlogintime = NULL;

  UPDATE sequence
  SET
    val = 1
  WHERE
    name = 'one';

  INSERT INTO t_syn_sql_history
  SELECT *
  FROM
    t_syn_sql;

  DELETE
  FROM
    t_syn_sql;

  
  
  DELETE
  FROM
    t_syncclient;
  DELETE
  FROM
    t_syncmsg;



  IF t_error = 1 THEN
    SET v_endfinish = FALSE;
    ROLLBACK;
  ELSE
    SET v_endfinish = TRUE;
    COMMIT;
	
	CALL p_check_source_data(v_branchid, v_opendate, v_now); 
  END IF;

END */$$
DELIMITER ;

/* Procedure structure for procedure `p_get_primarykeys` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_get_primarykeys` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_get_primarykeys`()
    COMMENT '获取重复的primarykey'
BEGIN
DECLARE done int DEFAULT 0;
DECLARE  v_total varchar(100);
DECLARE  v_primarykey varchar(100);

--
DECLARE cur_repat_keys CURSOR FOR SELECT count(1) total,t.primarykey
			                          from t_order_detail t
	                                   GROUP BY t.primarykey
					                    HAVING total>1
			                          ORDER BY total desc;
--
update t_order_detail set primarykey=UUID() where primarykey='';
OPEN cur_repat_keys;
  REPEAT
    FETCH cur_repat_keys INTO v_total,v_primarykey;
				IF done <> 1 THEN
				call p_update_primarykey(v_primarykey);
				END IF;
  UNTIL done = 1
  END REPEAT;

END */$$
DELIMITER ;

/* Procedure structure for procedure `p_mergetable` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_mergetable` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_mergetable`(IN i_targettableno varchar(50), IN i_orignaltableno varchar(50), INOUT o_result varchar(50))
    SQL SECURITY INVOKER
BEGIN

  DECLARE v_count int;
  DECLARE v_r_status varchar(2);
  DECLARE v_r_order varchar(100);
  DECLARE v_target_order_id varchar(100);
  DECLARE v_d_orderid varchar(100);
  DECLARE v_status varchar(2);
  DECLARE v_orderid varchar(100);
  DECLARE v_tableid varchar(100);
  declare v_man_num  int;
  declare v_woman_num  int;
  declare v_person_num int;
  declare v_child_num int;
  declare v_ageperiod  varchar(100);
  declare v_t_man_num  int;
  declare v_t_woman_num  int;
  declare v_t_person_num int;
  declare v_t_ageperiod  varchar(100);
  declare v_t_child_num int;

  DECLARE v_t_tableid varchar(100);
  DECLARE v_t_tableid_t varchar(100);
  DECLARE v_t_status varchar(2);
  DECLARE v_t_orderid varchar(100);
  DECLARE v_orderstatus int;
  DECLARE v_t_orderstatus int;
  DECLARE v_relateorderid varchar(3000);
  DECLARE v_t_relateorderid varchar(300);
  declare v_memberno  varchar(50);
  declare v_t_memberno  varchar(50);
  declare v_menuid  varchar(100);

  
   
SELECT
    COUNT(1) INTO v_count
  FROM t_table
  WHERE tableno = i_orignaltableno;

 
   select menuid into v_menuid   from v_currmenu;

  
 
IF v_count = 1 THEN
    
 
SELECT
      COUNT(1) INTO v_count
    FROM t_table
    WHERE tableno = i_targettableno;
 

    
 
IF v_count = 1 THEN


     
SELECT
        status,
        orderid,
        tableid INTO v_status, v_orderid, v_tableid
      FROM t_table
      WHERE tableno = i_orignaltableno;
 

      

    
SELECT
        status,
        orderid,
        tableid INTO v_t_status, v_t_orderid, v_t_tableid_t
      FROM t_table
      WHERE tableno = i_targettableno;
 
 
if v_orderid = v_t_orderid then
         
SET o_result = '0';
 
      else
             
     
IF v_orderid IS NOT NULL THEN
           
        
SELECT
          t.relateorderid,
          orderstatus,custnum,womanNum,childNum,mannum,ageperiod,memberno
       INTO v_relateorderid, v_orderstatus,v_person_num,v_woman_num,v_child_num,v_man_num,v_ageperiod,v_memberno
        FROM t_order t
        WHERE t.orderid = v_orderid;
 
        
    
IF v_orderstatus != 3 AND v_orderstatus != 2 THEN
          
         
IF v_t_orderid IS NOT NULL THEN

       
         
SELECT
              t.relateorderid,
              orderstatus ,custnum,womanNum,childNum,mannum,ageperiod,memberno
            INTO v_t_relateorderid, v_t_orderstatus,v_t_person_num,v_t_woman_num,
                 v_t_child_num,v_t_man_num,v_t_ageperiod,v_t_memberno
            FROM t_order t
            WHERE t.orderid = v_t_orderid;
 
IF v_t_orderstatus != 3  and v_t_orderstatus != 2  THEN

             
       if v_memberno is null and v_t_memberno is not null then
          
           update t_order set memberno = v_t_memberno 
           where orderid = v_orderid;

          call newspicyway.p_update2vipprice(v_orderid);
 
       else 
           if v_memberno is not null  and v_t_memberno is null then
             
             update t_order set memberno = v_memberno 
             where orderid = v_t_orderid;

            call newspicyway.p_update2vipprice(v_t_orderid);

           end if;
       end if; 
 
IF v_t_relateorderid IS NULL
                OR v_t_relateorderid = '' THEN

   
          
        UPDATE t_order_detail
													SET orderid = v_orderid
													WHERE orderid = v_t_orderid;


           update t_printdish set printobjid = (select id from t_printobj where orderno =v_orderid)
           where printobjid = (select id from t_printobj where orderno = v_t_orderid);
					 
													
												 
					UPDATE t_order_detail_discard
													SET orderid = v_orderid
													WHERE orderid = v_t_orderid;
 


											
										 
			UPDATE t_table
											SET orderid = v_orderid,status ='1'
											WHERE tableid = v_t_tableid_t;
			 

											
											
			UPDATE t_order
											SET payway = '1',
													relateorderid = CONCAT(v_tableid, ',', v_t_tableid_t),
													custnum = custnum + ifnull(v_t_person_num,0),
													womanNum = womanNum + ifnull(v_t_woman_num,0),
													 childNum = childNum + ifnull(v_t_child_num,0),
													mannum = mannum + ifnull(v_t_man_num,0),
													ageperiod = CONCAT(ageperiod,ifnull(v_t_ageperiod,'')),
													memberno = ifNUll(v_memberno,v_t_memberno)
											WHERE orderid = v_orderid;

    
 
					 
IF v_t_orderid!=v_orderid AND v_t_orderid IS NOT NULL THEN
                
				DELETE
													FROM t_order
												WHERE orderid = v_t_orderid;
				 
											END IF;

 ELSE

                
         
			UPDATE t_table
											SET orderid = v_orderid
											WHERE orderid = v_t_orderid;
 

                
             
UPDATE t_order_detail
                SET orderid = v_orderid
                WHERE orderid = v_t_orderid;
 

                
   
UPDATE t_order_detail_discard
                SET orderid = v_orderid
                WHERE orderid = v_t_orderid;

           update t_printdish set printobjid = (select id from t_printobj where orderno =v_orderid)
           where printobjid = (select id from t_printobj where orderno = v_t_orderid);
 

                

              
UPDATE t_order
                SET relateorderid = CONCAT(IFNULL(v_relateorderid, v_tableid), ',', v_t_relateorderid),
                    custnum = custnum + ifnull(v_t_person_num,0),
                    womanNum = womanNum + ifnull(v_t_woman_num,0),
                     childNum = childNum + ifnull(v_t_child_num,0),
                    mannum = mannum + ifnull(v_t_man_num,0),
                    ageperiod = CONCAT(ageperiod,ifnull(v_t_ageperiod,'')),
                    memberno = ifNUll(v_memberno,v_t_memberno)
                WHERE orderid = v_orderid;
 

                
							 
IF v_t_orderid!=v_orderid AND v_t_orderid IS NOT NULL THEN
            
                  DELETE
                  FROM t_order
                WHERE orderid = v_t_orderid;
 
								END IF;
              END IF;
ELSE
              
 
            
        UPDATE t_table
              SET status = '1',orderid = v_orderid
              WHERE tableid = v_t_tableid_t;
 

              
              
        UPDATE t_order
              SET payway = '1',
                  relateorderid = CONCAT(v_tableid, ',', v_t_tableid_t),
                    custnum = custnum + ifnull(v_t_person_num,0),
                    womanNum = womanNum + ifnull(v_t_woman_num,0),
                     childNum = childNum + ifnull(v_t_child_num,0),
                    mannum = mannum + ifnull(v_t_man_num,0),
                    ageperiod = CONCAT(ageperiod,ifnull(v_t_ageperiod,'')),
                    memberno = ifNUll(v_memberno,v_t_memberno)
              WHERE orderid = v_orderid;
 

            END IF;
          ELSE
            
        
         UPDATE t_table
												SET status = '1',
														orderid = v_orderid
												WHERE tableid = v_t_tableid_t;
						 

												
												 
						UPDATE t_order
												SET payway = '1',
														relateorderid = CONCAT(v_tableid, ',', v_t_tableid_t),
																custnum = custnum + ifnull(v_t_person_num,0),
																womanNum = womanNum + ifnull(v_t_woman_num,0),
																 childNum = childNum + ifnull(v_t_child_num,0),
																mannum = mannum + ifnull(v_t_man_num,0),
																ageperiod = CONCAT(ageperiod,ifnull(v_t_ageperiod,'')),
																memberno = ifNUll(v_memberno,v_t_memberno)
												WHERE orderid = v_orderid;
 

          END IF;

        END IF;

      END IF;

      end if;

   
     
SET o_result = '0';
 
    ELSE
     
SET o_result = '1';
 
    END IF;
  ELSE
     
SET o_result = '1';
 
  END IF;

 
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_orderdatas_cleanup` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_orderdatas_cleanup` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `p_orderdatas_cleanup`(IN  pi_branchid INT(11), 
                                                IN  pi_type     SMALLINT, 
                                                IN  pi_orderid  VARCHAR(5000), 
                                                IN  pi_ksrq     DATETIME, 
                                                IN  pi_jsrq     DATETIME, 
                                                OUT po_errmsg   VARCHAR(500)
                                                
)
    SQL SECURITY INVOKER
    COMMENT '按时间或订单编号清理订单数据'
label_main:
BEGIN
  
  
  declare v_result VARCHAR(500) DEFAULT '';


  
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    GET DIAGNOSTICS CONDITION 1 v_result = MESSAGE_TEXT;
    set po_errmsg = concat('清理失败！原因：',v_result);
    ROLLBACK;
  END;


  IF pi_branchid IS NULL THEN
    SET po_errmsg = '清理失败！原因：分店ID输入不能为空';
    LEAVE label_main;
  END IF;

  IF pi_type != 0 AND pi_type != 1 THEN
    SET po_errmsg = '清理失败！原因：请按照指定值填写清理方式：0：按订单编号清理，1：按时间清理';
    LEAVE label_main;
  END IF;

  IF pi_type = 0 AND pi_orderid IS NULL THEN
    SET po_errmsg = '清理失败！原因：你选择的是按订单编号清理，请输入订单编号';
    LEAVE label_main;
  END IF;

  IF pi_type = 1 AND (pi_ksrq IS NULL OR pi_jsrq IS NULL) THEN
    SET po_errmsg = '清理失败！原因：你选择的是按时间清理，请正确输入开始结束时间';
    LEAVE label_main;
  END IF;

  
  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_type = 0 THEN
    INSERT INTO t_temp_order
    SELECT orderid
    FROM
      t_order
    WHERE
      branchid = pi_branchid
      AND find_in_set(orderid, pi_orderid);
  ELSE
    INSERT INTO t_temp_order
    SELECT orderid
    FROM
      t_order
    WHERE
      branchid = pi_branchid
      AND begintime BETWEEN pi_ksrq AND pi_jsrq;
  END IF;

  

  
  START TRANSACTION; 
  delete from t_order where orderid in (SELECT orderid from t_temp_order);
  set po_errmsg =concat('t_order：' , ROW_COUNT());

  delete from t_order_detail where orderid in (SELECT orderid from t_temp_order);
  SET po_errmsg = concat(po_errmsg , ' t_order_detail：' , ROW_COUNT());

  delete from t_order_detail_discard where orderid in (SELECT orderid from t_temp_order);
  SET po_errmsg = concat(po_errmsg , ' t_order_detail_discard：' , ROW_COUNT());

  
  delete from t_settlement where orderid in (SELECT orderid from t_temp_order);
  SET po_errmsg = concat(po_errmsg , ' t_settlement：' , ROW_COUNT());

  delete from t_settlement_history where orderid in (SELECT orderid from t_temp_order);
  SET po_errmsg = concat(po_errmsg , ' t_settlement_history：' , ROW_COUNT());

  delete from t_settlement_detail_history where orderid in (SELECT orderid from t_temp_order);
  SET po_errmsg = concat(po_errmsg , ' t_settlement_detail_history：' , ROW_COUNT());

  delete from t_order_member where orderid in (SELECT orderid from t_temp_order);
  SET po_errmsg = concat(po_errmsg , ' t_order_member：' , ROW_COUNT());

  delete from t_settlement_detail where orderid in (SELECT orderid from t_temp_order);
  SET po_errmsg = concat(po_errmsg , ' t_settlement_detail：' , ROW_COUNT());

  
  delete from t_printdish where printobjid in (SELECT distinct a.id from t_printobj a, t_temp_order b where a.orderno = b.orderid);
  delete from t_printobj where orderno in (SELECT orderid from t_temp_order);

  
  delete from t_gift_log where order_id in (SELECT orderid from t_temp_order);

  
  delete from t_invoice where orderid in (SELECT orderid from t_temp_order);

  update t_table set orderid=null ,status='0' where orderid in(SELECT orderid from t_temp_order);
  SET po_errmsg = concat(po_errmsg , ' t_table：' , ROW_COUNT());

  if pi_type = 1 then
    DELETE FROM t_open_log;
    delete FROM t_open_log;
    DELETE FROM t_branch_biz_log;  
    update t_printer SET printnum = 0;
  
    
    UPDATE sequence SET val = 1 WHERE name = 'one';
  end if;

  commit;

  SET po_errmsg = concat('清理成功！\n', po_errmsg);
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_orderdish` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_orderdish` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_orderdish`(IN i_orderid varchar(50), IN i_printobjid int, INOUT o_code varchar(50), INOUT o_msg varchar(300))
    SQL SECURITY INVOKER
label_main:
  BEGIN

    DECLARE flag int;

    DECLARE v_g_printobjid int;

    DECLARE v_r_status varchar(2);

    DECLARE V_TABLEID varchar(100);

    DECLARE v_order varchar(100);

    DECLARE v_printobj_count int;

    DECLARE v_printdish_count int;

    DECLARE v_tablearea varchar(100) CHARACTER SET utf8;

    DECLARE v_orderid varchar(50);

    DECLARE v_tableno varchar(100) CHARACTER SET utf8;

    DECLARE v_dishid varchar(50);

    DECLARE v_dishstatus varchar(50);

    DECLARE v_begintime datetime;

    DECLARE v_endtime datetime;

    DECLARE v_sperequire varchar(200) CHARACTER SET utf8;

    DECLARE v_dishnum varchar(50);

    DECLARE v_userName varchar(100) CHARACTER SET utf8;

    DECLARE v_orderprice decimal(10, 2);

    DECLARE v_discountrate decimal(10, 2);

    DECLARE v_discountamount decimal(10, 2);

    DECLARE v_fishcode varchar(50);

    DECLARE v_dishtype int(11);

    DECLARE v_status int(11);

    DECLARE v_dishunit varchar(50) CHARACTER SET utf8;

    DECLARE v_payamount decimal(10, 2);

    DECLARE v_predisamount decimal(10, 2);

    DECLARE v_couponid varchar(50);

    DECLARE v_disuserid varchar(50);

    DECLARE v_orignalprice decimal(10, 2);

    DECLARE v_pricetype varchar(10);

    DECLARE v_printtype varchar(10);

    DECLARE v_printdishid int;

    DECLARE v_maxDishCount int;

    DECLARE v_printaddress varchar(100);

    DECLARE v_printport varchar(10);

    DECLARE v_timemsg varchar(30);

    DECLARE v_customeraddress varchar(100);

    DECLARE v_customerport varchar(10);

    DECLARE v_relatedishid varchar(4000);

    DECLARE v_orderseq integer;

    DECLARE v_dishname varchar(300) CHARACTER SET utf8;

    DECLARE v_full_name varchar(30) CHARACTER SET utf8;

    DECLARE done int DEFAULT 0;

    DECLARE tmpFlag int;

    DECLARE v_printer varchar(100);

    DECLARE v_ordertype int;

    DECLARE v_parentkey varchar(255);

    DECLARE v_superkey varchar(255);

    DECLARE v_ismaster int;

    DECLARE v_primarykey varchar(255);

    DECLARE v_islatecooke int;

    DECLARE v_isadddish int;

    DECLARE v_childdishtype int;

    DECLARE v_ispot int;

    DECLARE v_dish_count int;

    DECLARE v_menu_dish_count int;

    DECLARE v_branch_id varchar(50);

    DECLARE v_order_detail_count int;

    DECLARE v_menuid varchar(50);

    DECLARE autocommit int;

    DECLARE cur_order CURSOR FOR

    SELECT
      orderid,
      dishid,
      dishstatus,
      begintime,
      endtime,
      sperequire,
      dishnum,
      username,
      orderprice,
      discountrate,
      discountamount,
      fishcode,
      dishtype,
      status,
      dishunit,
      payamount,
      predisamount,
      couponid,
      username,
      orignalprice,
      pricetype,
      printtype,
      relatedishid,
      ordertype,
      parentkey,
      superkey,
      ismaster,
      primarykey,
      islatecooke,
      isadddish,
      childdishtype,
      ispot
    FROM t_order_detail_temp t
    WHERE t.orderid = i_orderid FOR UPDATE;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
      
      SET o_code = '3';
      GET DIAGNOSTICS CONDITION 1 o_msg = MESSAGE_TEXT;
      
      DELETE
        FROM t_order_detail_temp
      WHERE orderid = i_orderid;
    END;

    SELECT
      COUNT(1) INTO v_order_detail_count
    FROM t_order_detail
    WHERE orderid = i_orderid;

    IF v_order_detail_count = 0 THEN
      SET v_orderseq = 1;
    ELSE
      SELECT
        MAX(orderseq) + 1 INTO v_orderseq
      FROM t_order_detail
      WHERE orderid = i_orderid;
    END IF;

    SELECT
      tm.menuid INTO v_menuid
    FROM t_branch_info tbf,
         t_menu_branch mb,
         t_menu tm
    WHERE tbf.branchid = mb.branchid
    AND mb.menuid = tm.menuid
    AND tm.status = '1'
    ORDER BY tm.effecttime DESC LIMIT 1;

    IF done = 1 THEN
      SET o_code = '1';
      SET o_msg = '菜谱更新中';

      
      DELETE
        FROM t_order_detail_temp
      WHERE orderid = i_orderid;

      LEAVE label_main;
    END IF;

    OPEN cur_order;

    REPEAT

      FETCH cur_order INTO v_orderid,

      v_dishid,

      v_dishstatus,

      v_begintime,

      v_endtime,

      v_sperequire,

      v_dishnum,

      v_userName,

      v_orderprice,

      v_discountrate,

      v_discountamount,

      v_fishcode,

      v_dishtype,

      v_status,

      v_dishunit,

      v_payamount,

      v_predisamount,

      v_couponid,

      v_disuserid,

      v_orignalprice,

      v_pricetype,

      v_printtype,

      v_relatedishid,

      v_ordertype,

      v_parentkey,

      v_superkey,

      v_ismaster,

      v_primarykey,

      v_islatecooke,

      v_isadddish,

      v_childdishtype,

      v_ispot;

      IF done <> 1 THEN

        INSERT INTO t_order_detail (orderdetailid, orderid, dishid, dishstatus, begintime, endtime, sperequire, dishnum,
        username, orderprice, discountrate, discountamount, fishcode,
        dishtype, status, dishunit, payamount, predisamount,
        couponid, disuserid, orignalprice,
        pricetype, orderseq, relatedishid,
        ordertype,
        parentkey,
        superkey,
        ismaster,
        primarykey,
        islatecooke,
        isadddish,
        childdishtype,
        ispot)
          VALUES (UUID(), v_orderid, v_dishid, v_dishstatus, v_begintime, v_endtime, v_sperequire, v_dishnum, v_userName, v_orderprice, v_discountrate, v_discountamount, v_fishcode, v_dishtype, v_status, v_dishunit, v_payamount, v_predisamount, v_couponid, v_disuserid, v_orignalprice, v_pricetype, v_orderseq, v_relatedishid, v_ordertype, v_parentkey, v_superkey, v_ismaster, v_primarykey, v_printtype, v_isadddish, v_childdishtype, v_ispot);

        SELECT
          tableseqno('printdishid') INTO v_printdishid;

        SELECT
          COUNT(1) INTO v_dish_count
        FROM t_dish td
        WHERE dishid = v_dishid;

        IF v_dish_count > 0 THEN

          SELECT
            COUNT(1) INTO v_menu_dish_count
          FROM t_template_dishunit
          WHERE menuid = v_menuid
          AND dishid = v_dishid;

          IF v_menu_dish_count > 0 THEN

            SELECT
              IFNULL(dishname, '') INTO v_dishname
            FROM t_template_dishunit
            WHERE menuid = v_menuid
            AND dishid = v_dishid LIMIT 1;

          ELSE

            SELECT
              td.title INTO v_dishname
            FROM t_dish td
            WHERE dishid = v_dishid;

          END IF;

					ELSE 
						SET o_code = '2';
						SET o_msg = '菜品更新中';

						
						DELETE
							FROM t_order_detail_temp
						WHERE orderid = i_orderid;

						LEAVE label_main;
        END IF;

        IF done = 1 THEN
          SET o_code = '2';
          SET o_msg = '菜品更新中';

          
          DELETE
            FROM t_order_detail_temp
          WHERE orderid = i_orderid;

          LEAVE label_main;
        END IF;

        SELECT
          currenttableid INTO V_TABLEID
        FROM t_order T
        WHERE T.orderid = v_orderid;


        SELECT
          CONCAT('桌号:', ' ', IFNULL(tableno, '')) INTO v_tableno
        FROM t_tablearea ta,
             t_table tb
        WHERE tb.areaid = ta.areaid
        AND tb.tableid = V_TABLEID;


        IF v_dishtype IS NOT NULL
          AND v_dishtype = '1'
          AND v_parentkey != v_primarykey THEN
          SET v_dishname = CONCAT('-', v_dishname);

        END IF;

        IF v_dishtype IS NOT NULL
          AND v_dishtype = '2'
          AND v_parentkey != v_primarykey THEN
          SET v_dishname = CONCAT('-', v_dishname);
        END IF;


        INSERT INTO t_printdish (printdishid
        , printobjid
        , dishname
        , dishnum
        , dishprice
        , totalamount
        , payamount
        , sperequire
        , tableNomsg
        , dishunit
        , printipaddress
        , printport
        , printnum
        , dishid
        , printtype
        , relatedishid
        , orderseq
        , dishtype
        , printerId,
        ordertype,
        parentkey,
        superkey,
        ismaster,
        primarykey,
        islatecooke,
        isadddish,
        childdishtype,
        ispot)

          VALUES (v_printdishid, i_printobjid, v_dishname, v_dishnum, v_orderprice, 0, 0, v_sperequire, v_tableno, v_dishunit, v_printaddress, v_pricetype, '0', v_dishid, IFNULL(v_printtype, 2), v_relatedishid, v_orderseq, v_dishtype, v_printer, v_ordertype, v_parentkey, v_superkey, v_ismaster, v_primarykey, v_printtype, v_isadddish, v_childdishtype, v_ispot);


        SET v_dishname = NULL;
        SET v_printtype = NULL;

        IF done = 1 THEN
          SET o_code = '3';
          GET DIAGNOSTICS CONDITION 1 o_msg = MESSAGE_TEXT;

          
          DELETE
            FROM t_order_detail_temp
          WHERE orderid = i_orderid;

          LEAVE label_main;
        END IF;

      END IF;

    UNTIL done = 1
    END REPEAT;

    CLOSE cur_order;

    SET done = 0;

    SELECT
      COUNT(1) INTO v_printobj_count
    FROM t_printobj tp

    WHERE tp.orderno = i_orderid;

    IF v_printobj_count = 0 THEN

      SELECT
        currenttableid INTO V_TABLEID
      FROM t_order T
      WHERE T.orderid = v_orderid;

      UPDATE t_order
      SET orderstatus = '0'
      WHERE orderid = v_orderid;

      SELECT
        CURRENT_TIMESTAMP() INTO v_timemsg;

      SELECT
        branchid INTO v_branch_id
      FROM t_branch_info LIMIT 1;

      SELECT
        name INTO v_full_name
      FROM t_b_user
      WHERE id
      IN (SELECT
          user_id
        FROM t_b_employee
        WHERE job_number = v_userName
        AND branch_id = v_branch_id);

      SELECT
        currenttableid INTO V_TABLEID
      FROM t_order T
      WHERE T.orderid = v_orderid;

      SELECT
        ta.areaname INTO v_tablearea
      FROM t_tablearea ta,
           t_table tb
      WHERE tb.areaid = ta.areaid
      AND tb.tableid = V_TABLEID;

      INSERT INTO t_printobj (id,
      printtype
      , orderno
      , username
      , tableno
      , timemsg
      , customerprinterip
      , customerprinterport
      , tableArea,
      tableid)

        VALUES (i_printobjid, '3', i_orderid, v_full_name, v_tableno, v_timemsg, v_customeraddress, v_customerport, v_tablearea, V_TABLEID);

    END IF;

    IF done = 1 THEN
      SET o_code = '3';
      GET DIAGNOSTICS CONDITION 1 o_msg = MESSAGE_TEXT;

      
      DELETE
        FROM t_order_detail_temp
      WHERE orderid = i_orderid;

      LEAVE label_main;
    END IF;

    DELETE

      FROM t_order_detail_temp

    WHERE orderid = i_orderid;

    SET o_code = '0';
    SET o_msg = '下单成功';

  END */$$
DELIMITER ;

/* Procedure structure for procedure `p_pbckbb` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_pbckbb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_pbckbb`(IN  pi_branchid INT(11), 
                                      IN  pi_sb       SMALLINT, 
                                      IN  pi_ksrq     DATETIME, 
                                      IN  pi_jsrq     DATETIME, 
                                      IN  pi_sjjg     SMALLINT, 
                                      IN  pi_week     VARCHAR(100), 
                                      OUT po_errmsg   VARCHAR(100)
                                      )
    SQL SECURITY INVOKER
    COMMENT '排班参考报表'
label_main:
BEGIN 
  
  
  
  

  DECLARE v_datetimeStr     DATETIME; 
  DECLARE v_dateinterval    VARCHAR(50); 
  DECLARE v_openNum         INT; 
  DECLARE v_guestNum        INT; 
  DECLARE v_orderamount     DOUBLE(13, 2); 
  DECLARE v_alreadycheckNum INT; 
  DECLARE v_checkamount     DOUBLE(13, 2); 
  DECLARE v_notcheckNum     INT; 
  DECLARE v_IntheNum        INT; 
  DECLARE v_countNum        INT;
  DECLARE v_status          INT;

  DECLARE v_openTime        TIME; 
  DECLARE v_endTime         TIME; 
  DECLARE v_isacrossday     INT; 
  DECLARE v_date_count      INT;
  DECLARE v_dateCount       INT;


  DECLARE v_count           INT;
  DECLARE v_date_start      DATETIME;
  DECLARE v_date_end        DATETIME;
  
  DECLARE v_time_interval   DATETIME; 
  
  DECLARE v_table_index     INT;
  DECLARE v_table_id        VARCHAR(50);
  DECLARE v_fetch_done      INT DEFAULT FALSE;

  
  DECLARE c_begintime       VARCHAR(50);
  DECLARE c_endtime         VARCHAR(50);

  DECLARE c_datetimeStr     VARCHAR(50); 
  DECLARE c_dateinterval    VARCHAR(50); 
  DECLARE c_openNum         INT; 
  DECLARE c_guestNum        INT; 
  DECLARE c_orderamount     DOUBLE(13, 2); 
  DECLARE c_alreadycheckNum INT; 
  DECLARE c_checkamount     DOUBLE(13, 2); 
  DECLARE c_notcheckNum     INT; 
  DECLARE c_IntheNum        INT; 
  DECLARE c_countNum        INT;
  DECLARE c_now             DATETIME DEFAULT now();

  DECLARE Select_cursor CURSOR FOR SELECT date(begintime)
                                        , date(begintime)
                                   FROM
                                     t_temp_order
                                   WHERE
                                     1 = 1
                                   GROUP BY
                                     date(begintime);
  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE;
  END;



  
  
  
  



  
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
  
  
  END;

  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;

  
  
  
  SET v_date_start = str_to_date(pi_ksrq, '%Y-%m-%d %H:%i:%s');
  SET v_date_end = str_to_date(pi_jsrq, '%Y-%m-%d %H:%i:%s');
  SET v_date_count = day(v_date_end) - day(v_date_start);
  
  


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50),
    orderstatus INT,
    branchid INT,
    womanNum INT,
    childNum INT,
    mannum INT,
    begintime DATETIME,
    endtime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_sb = '-1' THEN
    INSERT INTO t_temp_order
    SELECT orderid
         , orderstatus
         , branchid
         , womanNum
         , childNum
         , mannum
         , begintime
         , endtime
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      CASE
      WHEN pi_week = '-1' THEN
        branchid = pi_branchid
        AND begintime BETWEEN v_date_start AND v_date_end
      ELSE
        branchid = pi_branchid
        AND begintime BETWEEN v_date_start AND v_date_end
        AND find_in_set(weekday(begintime), pi_week)
      END;
  ELSE
    INSERT INTO t_temp_order
    SELECT orderid
         , orderstatus
         , branchid
         , womanNum
         , childNum
         , mannum
         , begintime
         , endtime
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      CASE
      WHEN pi_week = '-1' THEN
        branchid = pi_branchid
        AND begintime BETWEEN v_date_start AND v_date_end
        AND shiftid = pi_sb
      ELSE
        branchid = pi_branchid
        AND begintime BETWEEN v_date_start AND v_date_end
        AND shiftid = pi_sb
        AND find_in_set(weekday(begintime), pi_week)
      END;
  END IF;

  
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);

  SET v_openTime = (SELECT concat(substring_index(time(begintime), ":", 1), ":00")
                    FROM
                      t_temp_order
                    ORDER BY
                      time(begintime) ASC
                    LIMIT
                      1); 
  SET v_endTime = (SELECT time(endtime)
                   FROM
                     t_temp_order
                   ORDER BY
                     time(endtime) DESC
                   LIMIT
                     1); 



  SELECT sum(day(endtime) - day(begintime))
  INTO
    v_dateCount
  FROM
    t_temp_order;


  
  IF v_dateCount > 0 THEN
    SET v_endTime = '23:59:59';
  END IF;

  
  IF v_date_count = 0 AND v_date_end >= c_now AND v_date_start <= c_now THEN
    SET v_endTime = c_now;
  END IF;


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
  CREATE TEMPORARY TABLE t_temp_order_detail
  (
    orderid VARCHAR(50),
    dishid VARCHAR(50),
    dishnum INT,
    orignalprice DECIMAL(13, 2),
    begintime DATETIME,
    endtime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_order_detail
  SELECT tod.orderid
       , tod.dishid
       , tod.dishnum
       , tod.orignalprice
       , tod.begintime
       , tod.endtime
  FROM
    t_temp_order too, t_order_detail tod
  WHERE
    too.orderid = tod.orderid;
  CREATE INDEX ix_t_temp_order_detail_orderid ON t_temp_order_detail (orderid);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    datetimeStr DATE, 
    dateinterval VARCHAR(50), 
    openNum INT, 
    guestNum INT, 
    orderamount DECIMAL(13, 2), 
    alreadycheckNum INT, 
    checkamount DECIMAL(13, 2), 
    notcheckNum INT, 
    IntheNum INT, 
    countNum INT 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_two;
  CREATE TEMPORARY TABLE t_temp_res_two
  (
    datetimeStr VARCHAR(50), 
    dateinterval VARCHAR(50), 
    openNum DECIMAL(13, 2), 
    guestNum DECIMAL(13, 2), 
    orderamount DECIMAL(13, 2), 
    alreadycheckNum DECIMAL(13, 2), 
    checkamount DECIMAL(13, 2), 
    notcheckNum DECIMAL(13, 2), 
    IntheNum DECIMAL(13, 2), 
    countNum INT 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;


  OPEN Select_cursor;


  Select_cursor_loop:
  LOOP
    FETCH Select_cursor INTO c_begintime, c_endtime;
    IF v_fetch_done THEN
      LEAVE Select_cursor_loop;
    END IF;
    
    SET c_begintime = concat(date(c_begintime), " ", substring_index(time(v_openTime), ":", 2));
    SET c_endtime = concat(date(c_begintime), " ", substring_index(time(v_endTime), ":", 2));

    WHILE c_begintime <= c_endtime
    DO
      
      SET v_time_interval = date_sub(date_add(c_begintime, INTERVAL pi_sjjg MINUTE) + 1, INTERVAL 1 SECOND);

      SET v_countNum = 1;
      
      SET v_openNum = (SELECT count(1)
                       FROM
                         t_temp_order too
                       WHERE
                         branchid = pi_branchid
                         AND begintime > c_begintime
                         AND begintime < v_time_interval);

      
      SET v_datetimeStr = date(c_begintime);
      
      SET v_dateinterval = concat(concat(substring_index(time(c_begintime), ":", 2), "-"), substring_index(time(v_time_interval), ":", 2));
      
      SET v_guestNum = (SELECT ifnull(sum(womanNum + childNum + mannum), 0)
                        FROM
                          t_temp_order
                        WHERE
                          branchid = pi_branchid
                          AND begintime > c_begintime
                          AND begintime < v_time_interval);
      
      SET v_orderamount = (SELECT sum(tod.dishnum * tod.orignalprice)
                           FROM
                             t_temp_order too, t_temp_order_detail tod, t_dish dd
                           WHERE
                             too.orderid = tod.orderid
                             AND tod.dishid = dd.dishid
                             AND dd.dishtype NOT IN (2)
                             AND too.branchid = pi_branchid
                             AND tod.begintime > c_begintime
                             AND tod.begintime < v_time_interval);

      
      SET v_alreadycheckNum = (SELECT count(1)
                               FROM
                                 t_temp_order too
                               WHERE
                                 orderstatus = '3'
                                 AND branchid = pi_branchid
                                 AND endtime > c_begintime
                                 AND endtime < v_time_interval);

      
      SET v_checkamount = (SELECT sum(tod.dishnum * tod.orignalprice)
                           FROM
                             t_temp_order too, t_temp_order_detail tod, t_dish dd
                           WHERE
                             too.orderid = tod.orderid
                             AND too.orderstatus = '3'
                             AND tod.dishid = dd.dishid
                             AND dd.dishtype NOT IN (2)
                             AND too.branchid = pi_branchid
                             AND too.endtime > c_begintime
                             AND too.endtime < v_time_interval);

      
      SET v_notcheckNum = (SELECT count(1)
                           FROM
                             t_temp_order
                           WHERE
                             branchid = pi_branchid
                             AND begintime < v_time_interval
                             AND (endtime > v_time_interval
                             OR endtime IS NULL));


      
      SET v_IntheNum = v_alreadycheckNum + v_notcheckNum;

      INSERT INTO t_temp_res (datetimeStr, dateinterval, openNum, guestNum, orderamount, alreadycheckNum, checkamount, notcheckNum, IntheNum, countNum) VALUES (v_datetimeStr, v_dateinterval, v_openNum, v_guestNum, v_orderamount, v_alreadycheckNum, v_checkamount, v_notcheckNum, v_IntheNum, v_countNum);

      SET c_begintime = date_add(c_begintime, INTERVAL pi_sjjg MINUTE);
      SET v_time_interval = date_add(v_time_interval, INTERVAL pi_sjjg MINUTE);
    END WHILE;
  END LOOP Select_cursor_loop;
  COMMIT;

  CLOSE Select_cursor;
  SET v_fetch_done = FALSE;

  
  BEGIN DECLARE Select_cursor_detail CURSOR FOR SELECT 'avg'
                                                     , dateinterval
                                                     , sum(openNum)
                                                     , sum(guestNum)
                                                     , sum(ifnull(orderamount, 0))
                                                     , sum(alreadycheckNum)
                                                     , sum(ifnull(checkamount, 0))
                                                     , sum(notcheckNum)
                                                     , sum(IntheNum)
                                                     , sum(countNum)
                                                FROM
                                                  t_temp_res
                                                GROUP BY
                                                  dateinterval;

    OPEN Select_cursor_detail;

  Select_cursor_detail_loop:
    LOOP
      FETCH Select_cursor_detail INTO c_datetimeStr, c_dateinterval, c_openNum, c_guestNum, c_orderamount, c_alreadycheckNum, c_checkamount, c_notcheckNum, c_IntheNum, c_countNum;
      IF v_fetch_done THEN
        LEAVE Select_cursor_detail_loop;
      END IF;

      INSERT INTO t_temp_res_two (datetimeStr, dateinterval, openNum, guestNum, orderamount, alreadycheckNum, checkamount, notcheckNum, IntheNum, countNum) VALUES (c_datetimeStr, c_dateinterval, c_openNum / c_countNum, c_guestNum / c_countNum, c_orderamount / c_countNum, c_alreadycheckNum / c_countNum, c_checkamount / c_countNum, c_notcheckNum / c_countNum, c_IntheNum / c_countNum, c_countNum);

    
    END LOOP Select_cursor_detail_loop;
    COMMIT;
    CLOSE Select_cursor_detail;
  END;

  
  SELECT date_format(datetimeStr, '%Y/%m/%d') AS datetimeStr
       , dateinterval
       , openNum
       , guestNum
       , ifnull(orderamount, 0) AS orderamount
       , alreadycheckNum
       , ifnull(checkamount, 0) AS checkamount
       , notcheckNum
       , IntheNum
  FROM
    t_temp_res
  UNION
  SELECT datetimeStr
       , dateinterval
       , openNum
       , guestNum
       , ifnull(orderamount, 0) AS orderamount
       , alreadycheckNum
       , ifnull(checkamount, 0) AS checkamount
       , notcheckNum
       , IntheNum
  FROM
    t_temp_res_two;

END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_fwyxstjb` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_fwyxstjb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `p_report_fwyxstjb`(IN pi_branchid INT(11),
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
    dishnum VARCHAR(50),
    dishtype INT,
    orderprice DECIMAL(10, 2),
    dishunit VARCHAR(100),
    begintime VARCHAR(50),
    pricetype VARCHAR(50),
    debitamount VARCHAR(50)
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
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_gzxxmxb` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_gzxxmxb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `p_report_gzxxmxb`(IN  pi_branchid INT(11),

                                              IN  pi_ksrq     DATETIME, 

                                              IN  pi_jsrq     DATETIME, 

                                              IN  pi_gzdw     VARCHAR(50), 

                                              IN  pi_qsbz     SMALLINT, 

                                              IN  pi_dqym     INT, 

                                              IN  pi_myts     INT, 

                                              OUT po_errmsg   VARCHAR(100)

                                              )
    SQL SECURITY INVOKER
    COMMENT '挂账信息明细表'
label_main:

BEGIN



  DECLARE c_current_time DATETIME DEFAULT now();

  DECLARE c_qsbz_all     INT DEFAULT 0;

  DECLARE c_qsbz_yqs     INT DEFAULT 1;

  DECLARE c_qsbz_wqs     INT DEFAULT 2;

  DECLARE c_gzdw_all     VARCHAR(50) DEFAULT 0;

  



  

  IF pi_qsbz NOT IN (1, 2, 0) OR pi_qsbz IS NULL THEN

    SELECT NULL;

    SET po_errmsg = '清算标志不正确';

    LEAVE label_main;

  END IF;







  IF pi_gzdw IS NULL THEN

    SELECT NULL;

    SET po_errmsg = '挂账单位不能为空';

    LEAVE label_main;

  END IF;



  SET @@max_heap_table_size = 1024 * 1024 * 300;

  SET @@tmp_table_size = 1024 * 1024 * 300;

  SET @@sql_safe_updates = 0;



  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail; 

  CREATE TEMPORARY TABLE t_temp_settlement_detail(

    inserttime DATETIME DEFAULT NULL,

    orderid VARCHAR(50) NOT NULL,

    payamount DECIMAL(10, 2) DEFAULT NULL COMMENT '实际支付金额',

    bankcardno VARCHAR(50) DEFAULT NULL COMMENT '挂账单位'



  ) ENGINE = HEAP DEFAULT CHARSET = utf8;



  

  IF pi_gzdw = c_gzdw_all THEN

    INSERT INTO t_temp_settlement_detail 

    SELECT t.inserttime

         , t.orderid

         , t.payamount

         , t.bankcardno

    FROM

      t_settlement_detail t,

      t_order b

    WHERE

      b.begintime BETWEEN pi_ksrq AND pi_jsrq

      AND t.orderid = b.orderid

      AND b.branchid = pi_branchid

      AND b.orderstatus = 3

      AND t.payway = 13

      AND t.payamount != 0

    ;



  ELSE



    INSERT INTO t_temp_settlement_detail 

    SELECT t.inserttime

         , t.orderid

         , t.payamount

         , t.bankcardno



    FROM

      t_settlement_detail t,

      t_order b

    WHERE

      b.begintime BETWEEN pi_ksrq AND pi_jsrq

      AND t.orderid = b.orderid

      AND b.branchid = pi_branchid

      AND b.orderstatus = 3

      AND t.payway = 13

      AND t.payamount != 0

      AND t.bankcardno = pi_gzdw;



  END IF;





  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid; 

  CREATE TEMPORARY TABLE t_temp_orderid(

    orderid VARCHAR(50) NOT NULL

  ) ENGINE = HEAP DEFAULT CHARSET = utf8;





  IF pi_qsbz = c_qsbz_yqs THEN 

    INSERT INTO t_temp_orderid

    SELECT a.orderid

    FROM

      t_temp_settlement_detail a

    LEFT JOIN t_billing_detail b

    ON a.orderid = b.orderid

    AND b.branchid = pi_branchid

    GROUP BY

      a.orderid

    HAVING

      sum(ifnull(b.payamount + b.disamount, 0)) < max(a.payamount);



  END IF;



  IF pi_qsbz = c_qsbz_wqs THEN

    INSERT INTO t_temp_orderid

    SELECT a.orderid

    FROM

      t_temp_settlement_detail a

    LEFT JOIN t_billing_detail b

    ON a.orderid = b.orderid

    AND b.branchid = pi_branchid

    GROUP BY

      a.orderid

    HAVING

      sum(ifnull(b.payamount + b.disamount, 0)) >= max(a.payamount);



  END IF;





  

  DELETE

  FROM

    t_temp_settlement_detail

  WHERE

    orderid IN (SELECT orderid

                FROM

                  t_temp_orderid);





  DROP TEMPORARY TABLE IF EXISTS t_temp_res; 

  CREATE TEMPORARY TABLE t_temp_res(

    temp_id INT UNSIGNED NOT NULL AUTO_INCREMENT,

    ddsj DATETIME,

    ddbh VARCHAR(50),

    gzje DECIMAL(10, 2),

    yjje DECIMAL(10, 2),

    wjje DECIMAL(10, 2),

    qsbz VARCHAR(20),

    qssj DATETIME,

    beizhu VARCHAR(500),



    PRIMARY KEY (temp_id)

  ) ENGINE = HEAP DEFAULT CHARSET = utf8;



  INSERT INTO t_temp_res (ddsj, ddbh, gzje, yjje, wjje, qsbz, qssj, beizhu)

  SELECT inserttime

       , orderid

       , payamount

       , 0

       , 0

       , '1'

       , NULL

       , NULL

  FROM

    t_temp_settlement_detail;



  

  UPDATE t_temp_res t, t_order a



  SET

    t.ddsj = a.begintime

  WHERE

    t.ddbh = a.orderid;



  

  UPDATE t_temp_res t, (SELECT a.orderid

                             , max(b.inserttime) qssj

                             , sum(ifnull(b.payamount + b.disamount, 0)) amount

                             , group_concat(b.remark SEPARATOR ';') remark

                        FROM

                          t_temp_settlement_detail a

                        LEFT JOIN t_billing_detail b

                        ON a.orderid = b.orderid

                        AND b.branchid = pi_branchid

                        GROUP BY

                          a.orderid) temp

  SET

    t.yjje = temp.amount, t.wjje = t.gzje - temp.amount, t.qssj = temp.qssj, t.beizhu = temp.remark

  WHERE

    t.ddbh = temp.orderid;



  UPDATE t_temp_res t

  SET

    t.qsbz = '2', t.qssj = NULL

  WHERE

    t.wjje > 0;





  IF pi_dqym > -1 THEN 

    SET @a = v_increment_offset + pi_dqym * pi_myts * v_increment_increment - v_increment_increment;

    SET @b = pi_myts;

    PREPARE s1 FROM 'select date_format(ddsj,\'%Y-%c-%d %H:%i:%s\') as ddsj, ddbh, gzje, yjje, wjje, qsbz,date_format(qssj,\'%Y-%c-%d %H:%i:%s\') as qssj,beizhu from t_temp_res where temp_id > ? limit ?';

    EXECUTE s1 USING @a, @b;

  ELSE

    PREPARE s1 FROM 'select date_format(ddsj,\'%Y-%c-%d %H:%i:%s\') as ddsj, ddbh, gzje, yjje, wjje, qsbz,date_format(qssj,\'%Y-%c-%d %H:%i:%s\') as qssj,beizhu from t_temp_res';

    EXECUTE s1;

  END IF;



  DEALLOCATE PREPARE s1;





END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_gzxxtjb` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_gzxxtjb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `p_report_gzxxtjb`(IN  pi_branchid INT(11),

                                              IN  pi_ksrq     DATETIME, 

                                              IN  pi_jsrq     DATETIME, 

                                              IN  pi_gzdw     VARCHAR(50), 

                                              IN  pi_qsbz     SMALLINT, 

                                              IN  pi_dqym     INT, 

                                              IN  pi_myts     INT, 

                                              OUT po_errmsg   VARCHAR(100)

                                              )
    SQL SECURITY INVOKER
    COMMENT '挂账信息统计表'
label_main:

BEGIN



  DECLARE c_current_time DATETIME DEFAULT now();

  DECLARE c_qsbz_all     INT DEFAULT 0;

  DECLARE c_qsbz_yqs     INT DEFAULT 1;

  DECLARE c_qsbz_wqs     INT DEFAULT 2;



  DECLARE c_gzdw_all     VARCHAR(50) DEFAULT 0;



  DECLARE EXIT HANDLER FOR SQLEXCEPTION 

  BEGIN

    SELECT NULL;

  

  END;





  

  IF pi_qsbz NOT IN (1, 2, 0) OR pi_qsbz IS NULL THEN

    SELECT NULL;

    SET po_errmsg = '清算标志不正确';

    LEAVE label_main;

  END IF;







  IF pi_gzdw IS NULL THEN

    SELECT NULL;

    SET po_errmsg = '挂账单位不能为空';

    LEAVE label_main;

  END IF;





  SET @@max_heap_table_size = 1024 * 1024 * 300;

  SET @@tmp_table_size = 1024 * 1024 * 300;

  SET @@sql_safe_updates = 0;



  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail; 

  CREATE TEMPORARY TABLE t_temp_settlement_detail(

    orderid VARCHAR(50) NOT NULL,

    payamount DECIMAL(10, 2) DEFAULT NULL COMMENT '实际支付金额',

    bankcardno VARCHAR(50) DEFAULT NULL COMMENT '挂账单位',

    inserttime DATETIME DEFAULT NULL

  ) ENGINE = HEAP DEFAULT CHARSET = utf8;



  

  IF pi_gzdw = c_gzdw_all THEN

    INSERT INTO t_temp_settlement_detail 

    SELECT t.orderid

         , t.payamount

         , t.bankcardno

         , b.begintime

    FROM

      t_settlement_detail t,

      t_order b

    WHERE

      b.begintime BETWEEN pi_ksrq AND pi_jsrq

      AND t.orderid = b.orderid

      AND b.branchid = pi_branchid

      AND b.orderstatus = 3

      AND t.payway = 13

      AND t.payamount != 0

    ;



  ELSE



    INSERT INTO t_temp_settlement_detail 

    SELECT t.orderid

         , t.payamount

         , t.bankcardno

         , t.inserttime

    FROM

      t_settlement_detail t,

      t_order b

    WHERE

      b.begintime BETWEEN pi_ksrq AND pi_jsrq

      AND t.orderid = b.orderid

      AND b.branchid = pi_branchid

      AND b.orderstatus = 3

      AND t.payway = 13

      AND t.payamount != 0

      AND t.bankcardno LIKE concat('%', pi_gzdw, '%');



  END IF;





  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid; 

  CREATE TEMPORARY TABLE t_temp_orderid(

    orderid VARCHAR(50) NOT NULL

  ) ENGINE = HEAP DEFAULT CHARSET = utf8;





  IF pi_qsbz = c_qsbz_yqs THEN 

    INSERT INTO t_temp_orderid

    SELECT a.orderid

    FROM

      t_temp_settlement_detail a

    LEFT JOIN t_billing_detail b

    ON a.orderid = b.orderid

    AND b.branchid = pi_branchid

    GROUP BY

      a.orderid

    HAVING

      sum(ifnull(b.payamount + b.disamount, 0)) < max(a.payamount);

  END IF;



  IF pi_qsbz = c_qsbz_wqs THEN 

    INSERT INTO t_temp_orderid

    SELECT a.orderid

    FROM

      t_temp_settlement_detail a

    LEFT JOIN t_billing_detail b

    ON a.orderid = b.orderid

    AND b.branchid = pi_branchid

    GROUP BY

      a.orderid

    HAVING

      sum(ifnull(b.payamount + b.disamount, 0)) >= max(a.payamount);

  END IF;





  

  DELETE

  FROM

    t_temp_settlement_detail

  WHERE

    orderid IN (SELECT orderid

                FROM

                  t_temp_orderid);





  DROP TEMPORARY TABLE IF EXISTS t_temp_res; 

  CREATE TEMPORARY TABLE t_temp_res(

    temp_id INT UNSIGNED NOT NULL AUTO_INCREMENT,

    gzdw VARCHAR(50),

    gzds INT,

    gzze DECIMAL(10, 2),

    yjje DECIMAL(10, 2),

    wjje DECIMAL(10, 2),

    zcgzsj INT,

    PRIMARY KEY (temp_id)

  ) ENGINE = HEAP DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_res (gzdw, gzds, gzze, yjje, wjje, zcgzsj)

  SELECT bankcardno

       , count(1)

       , sum(payamount)

       , 0

       , 0

       , 0

  FROM

    t_temp_settlement_detail

  GROUP BY

    bankcardno;







  

  UPDATE t_temp_res a, (SELECT creaditname

                             , sum(t.payamount + t.disamount) amount

                        FROM

                          t_billing_detail t

                        WHERE

                          t.orderid IN (SELECT orderid

                                        FROM

                                          t_temp_settlement_detail)

                        GROUP BY

                          creaditname) temp

  SET

    a.yjje = temp.amount

  WHERE

    a.gzdw = temp.creaditname;



  

  UPDATE t_temp_res a

  SET

    a.wjje = a.gzze - a.yjje;



  



  UPDATE t_temp_res t, (SELECT temp.bankcardno

                             , datediff(c_current_time, min(temp.inserttime)) gzsj

                        FROM

                          (SELECT a.orderid

                                , CASE

                                  WHEN min(b.inserttime) IS NULL THEN

                                    min(a.inserttime)

                                  ELSE

                                    min(b.inserttime)

                                  END AS inserttime

                                , min(a.bankcardno) bankcardno

                           FROM

                             t_temp_settlement_detail a

                           LEFT JOIN t_billing_detail b

                           ON a.orderid = b.orderid

                           GROUP BY

                             a.orderid

                           HAVING

                             sum(ifnull(b.payamount + b.disamount, 0)) < max(a.payamount)) temp

                        GROUP BY

                          temp.bankcardno) temp2

  SET

    t.zcgzsj = temp2.gzsj

  WHERE

    t.wjje >= 0

    AND t.gzdw = temp2.bankcardno;



  IF pi_dqym > -1 THEN 

    SET @a = v_increment_offset + pi_dqym * pi_myts * v_increment_increment - v_increment_increment;

    SET @b = pi_myts;

    PREPARE s1 FROM 'select gzdw, gzds, gzze, yjje, wjje, zcgzsj from t_temp_res where temp_id > ? limit ?';

    EXECUTE s1 USING @a, @b;

  ELSE

    PREPARE s1 FROM 'select gzdw, gzds, gzze, yjje, wjje, zcgzsj from t_temp_res';

    EXECUTE s1;

  END IF;



  DEALLOCATE PREPARE s1;





END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_jsfsmxb` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_jsfsmxb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_jsfsmxb`(IN  pi_branchid INT(11), 
                                  IN  pi_sb       SMALLINT, 
                                  IN  pi_ksrq     DATETIME, 
                                  IN  pi_jsrq     DATETIME, 
                                  OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '结算方式明细表'
label_main:
BEGIN

  DECLARE v_date_start DATETIME;
  DECLARE v_date_end   DATETIME;

  
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
    orderid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_sb > -1 THEN
    INSERT INTO t_temp_order
    SELECT orderid
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
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      branchid = pi_branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND orderstatus = 3;
  END IF;

  
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    membercardno varchar(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_settlement_detail
  SELECT b.orderid
       , b.payway
       , b.payamount
       , b.membercardno
  FROM
    t_temp_order a, t_settlement_detail b
  WHERE
    a.orderid = b.orderid
    and b.payamount > 0
    AND b.payway IN (0, 1, 5, 8, 11, 12, 13,17, 18,30);
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    payway VARCHAR(50),
    nums INT,
    prices DOUBLE(13, 2),
    membercardno VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_res
  SELECT payway
       , count(1)
       , ifnull(sum(payamount), 0)
       , CASE payway WHEN 1 THEN membercardno ELSE NULL END
  FROM
    t_temp_settlement_detail
  GROUP BY
    payway,membercardno;
  
  SELECT (CASE payway WHEN 1 THEN (select c.itemDesc from t_dictionary c where c.type ='BANK' and c.itemid = a.membercardno) ELSE b.itemDesc END)  AS payway
       , b.itemid
       , sum(a.nums) AS nums
       , sum(a.prices) AS prices
       , a.membercardno
  FROM
    t_temp_res a left join t_dictionary b on a.payway = b.itemid 
  WHERE
    b.type = 'PAYWAY'   
  GROUP BY
    b.itemDesc,a.membercardno
  ORDER BY
    payway;
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_jsfsxx` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_jsfsxx` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_jsfsxx`(IN  pi_branchid VARCHAR(50), 
								  IN  pi_jsfs     INT(11),
								  IN  pi_hdmc     VARCHAR(50),
                                  IN  pi_sb       SMALLINT, 
                                  IN  pi_ksrq     DATETIME, 
                                  IN  pi_jsrq     DATETIME, 
								  IN `pi_brandid` int(11),
								  IN `pi_marketid` int(11),
								  IN `pi_areaid` int(11),
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
  DECLARE v_shouldamount decimal(13, 2);
  DECLARE v_paidinamount decimal(13, 2);
  DECLARE v_inflated     decimal(13, 2); 
  DECLARE v_shouldamount_total     decimal(13, 2); 
  DECLARE v_paidinamount_total     decimal(13, 2); 
  DECLARE v_payways_total     	   decimal(13, 2); 
  DECLARE v_inflated_total     	   decimal(13, 2); 
  
  DECLARE v_fetch_done          BOOL DEFAULT FALSE; 
  
  DECLARE v_paywaypercent decimal(5, 2); 
  DECLARE v_shouldpercent decimal(5, 2); 
  DECLARE v_paidinpercent decimal(5, 2); 
  
  
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
  
	SELECT id,name INTO v_brandid,v_brand_name  FROM t_c_brand WHERE id = pi_brandid;
  ELSE
	SELECT id,name INTO v_brandid,v_brand_name FROM t_c_brand LIMIT 1;
  END IF;  
  
  IF pi_marketid > -1 THEN
  
	SELECT id,name INTO v_marketid,v_market_name  FROM t_c_market WHERE id = pi_marketid;
  ELSE
	SELECT id,name INTO v_marketid,v_market_name FROM t_c_market LIMIT 1;
  END IF;  
  
  IF pi_areaid > -1 THEN
  
	SELECT id,name INTO v_areaid,v_area_name  FROM t_c_area WHERE id = pi_areaid;
  ELSE
	SELECT id,name INTO v_areaid,v_area_name FROM t_c_area LIMIT 1;
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
	SELECT ifnull(COUNT(1),0) INTO v_branchid FROM t_temp_branch;
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
	begintime datetime
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
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   

  
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

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
           ifnull(ifnull(a.free_reason, a.company_name), b.name)
         END) AS name
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
	datetime VARCHAR(50),
	brandid VARCHAR(50),
	brandname VARCHAR(50),
	marketid VARCHAR(50),
	market  VARCHAR(50),
	areaid VARCHAR(50),
	area  VARCHAR(50),
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
    payamount decimal(13, 2), 
    shouldamount decimal(13, 2)DEFAULT 0.00, 
    paidinamount decimal(13, 2)DEFAULT 0.00, 
	paywaypercent decimal(5, 2)DEFAULT 0.00, 
	shouldpercent decimal(5, 2)DEFAULT 0.00, 
	paidinpercent decimal(5, 2) DEFAULT 0.00, 
    PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_sub;
  CREATE TEMPORARY TABLE t_temp_res_sub
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
	datetime VARCHAR(50),
	brandid VARCHAR(50),
	brandname VARCHAR(50),
	marketid VARCHAR(50),
	market  VARCHAR(50),
	areaid VARCHAR(50),
	area  VARCHAR(50),
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
    payamount decimal(13, 2), 
    shouldamount decimal(13, 2) DEFAULT 0.00, 
    paidinamount decimal(13, 2) DEFAULT 0.00, 
	paywaypercent decimal(5, 2) DEFAULT 0.00, 
	shouldpercent decimal(5, 2) DEFAULT 0.00, 
	paidinpercent decimal(5, 2) DEFAULT 0.00, 
    PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
 
  BEGIN
	DECLARE cur_branchid CURSOR FOR SELECT branchid,branchname FROM t_temp_branch;
	
	OPEN cur_branchid;
	
	
	IF DATEDIFF(v_date_end,v_date_start) > 0 THEN 
		SET v_datetime = concat(date_format(v_date_start,'%Y-%m-%d'),' - ',date_format(v_date_end,'%Y-%m-%d'));
	ELSE
		SET v_datetime = concat(date_format(v_date_start,'%Y-%m-%d'));
	END IF;
	
	
	lable_fetch_branchid_loop:
		LOOP
		
			FETCH cur_branchid INTO v_branchid_cur,v_branchname_cur;
			IF v_fetch_done THEN
				  LEAVE lable_fetch_branchid_loop;
				END IF;
				
			
			SELECT ifnull(branch_id,0)
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
				
			
			SELECT ifnull(sum(a.orignalprice * a.dishnum), 0)
			INTO
			  v_shouldamount_total
			FROM
			  t_temp_order_detail_sub a , t_temp_orderid b
			WHERE
			  a.orderid = b.orderid;
					  
			
			SELECT ifnull(sum(a.payamount), 0)
			INTO
				v_paidinamount_total
			FROM
				t_temp_settlement_detail_sub a,t_temp_orderid b
			WHERE
				a.orderid = b.orderid
			AND
				a.payway IN (0, 1, 5, 8, 13, 17, 18); 
			  
			
			SELECT ifnull(sum(a.Inflated), 0)
			INTO
			  v_inflated_total
			FROM
			  t_temp_order_member a, t_temp_orderid b
			WHERE
			  a.orderid = b.orderid;
			  
			
			SELECT ifnull(count(DISTINCT orderid ),0)
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
				   , ifnull(sum(couponNum), 0)
				   , ifnull(sum(payamount), 0)
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
		  SELECT ifnull(max(id), 0)
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
			  t_temp_order_detail_sub a USE INDEX (ix_t_temp_order_detail_sub_orderid) join t_temp_orderid b USE INDEX (ix_t_temp_orderid_orderid)
			on
			  a.orderid = b.orderid;
			  
			TRUNCATE t_temp_settlement_detail_sub_new;
			
			INSERT INTO t_temp_settlement_detail_sub_new
			SELECT a.*
			FROM
			  t_temp_settlement_detail_sub a USE INDEX (ix_t_temp_settlement_detail_sub_orderid,ix_t_temp_settlement_detail_sub_payway) join t_temp_orderid b USE INDEX (ix_t_temp_orderid_orderid)
			on
			  a.orderid = b.orderid
			WHERE a.payway IN (0, 1, 5, 8, 13, 17, 18);
			
			
			SELECT ifnull(sum(a.orignalprice * a.dishnum), 0)
			INTO
			  v_shouldamount
			FROM
			  t_temp_order_detail_sub_new a;

			
			SELECT ifnull(sum(a.payamount), 0)
			INTO
			  v_paidinamount
			FROM
			  t_temp_settlement_detail_sub_new a;

			
			SELECT ifnull(sum(a.Inflated), 0)
			INTO
			  v_inflated
			FROM
			  t_temp_order_member a JOIN t_temp_orderid b USE INDEX (ix_t_temp_orderid_orderid)
			ON
			  a.orderid = b.orderid;
		  
			
			SELECT ifnull(count(DISTINCT orderid),0)
			INTO v_payways
			FROM t_temp_orderid a;
		
			
			SET v_paywaypercent = v_payways / v_payways_total ;
			
			
			SET v_shouldpercent = v_shouldamount / v_shouldamount_total ;
			
			
			SET v_paidinpercent = (v_paidinamount - v_inflated ) / ( v_paidinamount_total - v_inflated_total ) ;

			
			UPDATE t_temp_res_sub
			SET
			  brandid=v_brandid,brandname=v_brand_name,marketid=v_marketid,market=v_market_name,areaid=v_areaid,
			  area=v_area_name,managePattern='1',managePatternid ='自营',jde=v_jde,datetime=v_datetime,
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
			AND type = 'PAYWAY';

		  
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
		  
		INSERT INTO t_temp_res (brandid,brandname,marketid,market,areaid,area,managePattern,managePatternid ,jde,datetime,
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
				area,
				managePattern,
				managePatternid,
				jde jdecode,
				datetime durdate,
				branchid storeid,
				branchname store,
				paywaydesc settermenttype,
				pname activityid,
				pname activityname,
				ptype,
				payway settlementno,
				couponNum no,
				payamount happenmoney,
				shouldamount pullreceivable,
				paidinamount pullactive,
				paywaypercent oftotalsetter,
				shouldpercent ofreceivable,
				paidinpercent ofpullactive
		  FROM
			t_temp_res;
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_jsfsxxxq` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_jsfsxxxq` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_jsfsxxxq`(IN  pi_branchid INT(11), 
								  IN  pi_jsfs     INT(11),
								  IN  pi_hdmc     VARCHAR(50),
                                  IN  pi_sb       SMALLINT, 
                                  IN  pi_ksrq     DATETIME, 
                                  IN  pi_jsrq     DATETIME, 
								  IN `pi_brandid` int(11),
								  IN `pi_marketid` int(11),
								  IN `pi_areaid` int(11),
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
  
	SELECT id,name INTO v_brandid,v_brand_name  FROM t_c_brand WHERE id = pi_brandid;
  ELSE
	SELECT id,name INTO v_brandid,v_brand_name FROM t_c_brand LIMIT 1;
  END IF;  
  
  IF pi_marketid > -1 THEN
  
	SELECT id,name INTO v_marketid,v_market_name  FROM t_c_market WHERE id = pi_marketid;
  ELSE
	SELECT id,name INTO v_marketid,v_market_name FROM t_c_market LIMIT 1;
  END IF;  
  
  IF pi_areaid > -1 THEN
  
	SELECT id,name INTO v_areaid,v_area_name  FROM t_c_area WHERE id = pi_areaid;
  ELSE
	SELECT id,name INTO v_areaid,v_area_name FROM t_c_area LIMIT 1;
  END IF;  
  
  SET v_branchid = pi_branchid;
  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;
  
  SELECT ifnull(branch_id,0)
  INTO v_jde
  FROM t_branch_code a WHERE a.branch_id_code = v_branchid;

  
  SET v_date_start = pi_ksrq;
  SET v_date_end = pi_jsrq;

  SELECT branchname into v_branchname 
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
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   

  
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;
  

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
           ifnull(ifnull(a.free_reason, a.company_name), b.name)
         END) AS name
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
	datetime date,
	brandid VARCHAR(50),
	brandname VARCHAR(50),
	marketid VARCHAR(50),
	market  VARCHAR(50),
	areaid VARCHAR(50),
	area  VARCHAR(50),
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
    payamount decimal(13, 2), 
    shouldamount decimal(13, 2), 
    paidinamount decimal(13, 2), 
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
				
				INSERT INTO t_temp_res (datetime, orderid,pname, payamount, couponNum, shouldamount, paidinamount)
				SELECT date_format(max(begintime), '%Y-%m-%d')
					 , orderid
					 , pname
					 , ifnull(sum(payamount), 0)
					 , ifnull(sum(couponNum), 0)
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
				   , sum(a.orignalprice * a.dishnum)
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
				   , ifnull(sum(a.Inflated), 0)
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
				   , ifnull(sum(a.payamount), 0)
			  FROM
				t_temp_settlement_detail a, t_temp_orderid b
			  WHERE
				a.orderid = b.orderid
				AND payway IN (0, 1, 5, 8,13,17,18)
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
			    area=v_area_name,managePattern='1',managePatternid ='自营',jde=v_jde;
			
			  
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
				datetime durdate,
				brandid,
				brandname,
				marketid,
				market ,
				areaid,
				area ,
				managePattern,
				managePatternid ,
				jde jdecode,
				branchid,
				branchname store,
				pname activityname,
				orderid orderno,
				couponNum no,
				payamount happenmoney,
				shouldamount pullreceivable,
				paidinamount pullactive
		  FROM
			t_temp_res;
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_jzd` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_jzd` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_jzd`(IN pi_orderid varchar(50),
OUT po_errmsg varchar(100))
    SQL SECURITY INVOKER
    COMMENT '结账单'
label_main:
BEGIN

  DECLARE v_payway double(13, 2) DEFAULT 0;              
  DECLARE v_pamount double(13, 2) DEFAULT 0;             
  DECLARE v_totalconsumption double(13, 2) DEFAULT 0;    
  DECLARE v_paidamount double(13, 2) DEFAULT 0;          
  DECLARE v_giveamount double(13, 2) DEFAULT 0;          
  DECLARE v_couponamount double(13, 2) DEFAULT 0;        
  DECLARE v_taocan double(13, 2) DEFAULT 0;              

  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SELECT
      NULL;
    GET DIAGNOSTICS CONDITION 1 po_errmsg = MESSAGE_TEXT;
  END;

  IF pi_orderid IS NULL THEN
    SELECT
      NULL;
    SET po_errmsg = '订单号输入不能为空';
    LEAVE label_main;
  END IF;

  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;

  SELECT
    IFNULL(SUM(tod.dishnum * tod.orderprice), 0.00) INTO v_totalconsumption
  FROM t_order_detail tod
  WHERE orderid = pi_orderid AND pricetype <> 1;

  
  
  
  
  

  SELECT
    payway,
    IFNULL(payamount, 0.00) INTO v_payway, v_pamount
  FROM t_settlement_detail
  WHERE orderid = pi_orderid AND payway IN (7, 20);

  SELECT
    IFNULL(SUM(payamount), 0.00) INTO v_paidamount
  FROM t_settlement_detail
  WHERE orderid = pi_orderid AND payway IN (0, 1, 5, 8, 13, 17, 18);

  SELECT
    IFNULL(SUM(orignalprice), 0.00) INTO v_giveamount
  FROM t_order_detail
  WHERE orderid = pi_orderid AND pricetype = 1;

  
  
  SET v_couponamount = v_totalconsumption - v_paidamount;

  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res (
    payway varchar(50),               
    payamount double(13, 2),          
    totalconsumption double(13, 2),   
    paidamount double(13, 2),         
    giveamount double(13, 2),         
    couponamount double(13, 2),       
    invoiceamount double(13, 2)       
  ) ENGINE = MEMORY DEFAULT charset = utf8;

  
  

  INSERT INTO t_temp_res (payway, payamount, totalconsumption, paidamount, giveamount, couponamount, invoiceamount)
    VALUES (v_payway, v_pamount, v_totalconsumption, v_paidamount, v_giveamount, v_couponamount, 0.00);

  SELECT
    *
  FROM t_temp_res;

END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_pxxsmxb` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_pxxsmxb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_pxxsmxb`(IN  pi_branchid INT(11), 
                                  IN  pi_sb       SMALLINT, 
                                  IN  pi_ksrq     DATETIME, 
                                  IN  pi_jsrq     DATETIME, 
                                  IN  pi_pl       VARCHAR(50), 
                                  IN  pi_lx       VARCHAR(10), 
                                  OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '品项销售明细表'
label_main:
BEGIN


  
  
  
  
  


  DECLARE v_count       INT;
  DECLARE v_sum         INT;
  DECLARE v_total_count DOUBLE(13, 2);
  DECLARE v_item_desc   VARCHAR(50); 
  DECLARE v_item_id     VARCHAR(50); 
  DECLARE v_date_start  DATETIME;
  DECLARE v_date_end    DATETIME;
  DECLARE v_total_custnum_count    DOUBLE(13, 2); 
  DECLARE v_total_shouldmount_count    DOUBLE(13, 2); 
  DECLARE v_canju_mount DOUBLE(13, 2); 

  
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
    ispot TINYINT,
    parentkey VARCHAR(40),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
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
  FROM
    t_temp_order b, t_order_detail a
  WHERE
    b.orderid = a.orderid
    AND a.orignalprice > 0;
  
  
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   

  
  delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

  CREATE INDEX ix_t_temp_order_detail_dishid ON t_temp_order_detail (dishid);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_parentkey;
  CREATE TEMPORARY TABLE t_temp_parentkey
  (
    parentkey VARCHAR(40)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_parentkey
  SELECT DISTINCT parentkey
  FROM
    t_temp_order_detail
  WHERE
    ispot = 1
    AND dishnum = 0;

  
  DELETE
  FROM
    t_temp_order_detail
  WHERE
    ispot = 1
    AND dishnum = 0;

  
  UPDATE t_temp_order_detail a, t_temp_parentkey b
  SET
    a.dishtype = 0
  WHERE
    a.parentkey = b.parentkey;

  


  
  UPDATE t_temp_order_detail a, t_dish b
  SET
    a.dishtype = b.dishtype
  WHERE
    a.dishid = b.dishid
    AND a.dishtype IS NULL;

  
  SELECT ifnull(sum(dishnum), 0)
  INTO
    v_total_count
  FROM
    t_temp_order_detail;

  IF v_total_count <= 0 THEN
    SELECT NULL;
    SET po_errmsg = '数据为空，无查询结果';
    LEAVE label_main;
  END IF;

  SELECT ifnull(sum(custnum), 0)
  INTO
    v_total_custnum_count
  FROM
    t_temp_order;

  IF v_total_custnum_count <= 0 THEN
    SELECT NULL;
    SET po_errmsg = '数据为空，无查询结果';
    LEAVE label_main;
  END IF;


  SELECT ifnull(sum(dishnum*orignalprice), 0)
  INTO
    v_total_shouldmount_count
  FROM
    t_temp_order_detail;

  IF v_total_shouldmount_count <= 0 THEN
    SELECT NULL;
    SET po_errmsg = '数据为空，无查询结果';
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
    turnover DOUBLE(13, 2),
    share DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;


  INSERT INTO t_temp_res (id, dishType, number,orignalprice)
  SELECT b.columnid
       , a.dishtype
       , ifnull(sum(a.dishnum), 0), ifnull(sum(a.dishnum*a.orignalprice), 0)
  FROM
    t_temp_order_detail a, t_dish_dishtype b
  WHERE
    a.dishid = b.dishid
  GROUP BY
    b.columnid
  , a.dishtype
  ORDER BY
    NULL;


  
  UPDATE t_temp_res
  SET
    dishtypetitle =
    CASE dishType
    WHEN 0 THEN
      '单品'
    WHEN 1 THEN
      '鱼锅'
    ELSE
      '套餐'
    END;
  
  UPDATE t_temp_res t, t_basicdata a
  SET
    t.itemDesc = a.itemDesc
  WHERE
    t.Id = a.id
    AND a.status = 1;

  
  UPDATE t_temp_res
  SET
    share = number / v_total_count * 100,thousandstimes= number/v_total_custnum_count * 1000,turnover = orignalprice/v_total_shouldmount_count*100;


  
  IF pi_pl = -1 OR pi_pl = 'DISHES_98' THEN
    SELECT ifnull(sum(dishnum), 0),ifnull(sum(dishnum*orignalprice), 0)
    INTO
      v_sum,v_canju_mount
    FROM
      t_temp_order_detail
    WHERE
      dishid = 'DISHES_98';

    IF v_sum > 0 THEN
      INSERT INTO t_temp_res VALUES ('DISHES_98', '餐具', 0, '单品', v_sum,v_sum/v_total_custnum_count*1000,v_canju_mount,v_canju_mount/v_total_shouldmount_count*100,v_sum / v_total_count * 100); 
    END IF;
  END IF;
  COMMIT;

  
  IF pi_pl = -1 AND pi_lx = -1 THEN
    SELECT *
    FROM
      t_temp_res;
  ELSEIF pi_pl != -1 AND pi_lx = -1 THEN
    SELECT *
    FROM
      t_temp_res
    WHERE
      Id = pi_pl;
  ELSEIF pi_pl = -1 AND pi_lx != -1 THEN
    SELECT *
    FROM
      t_temp_res
    WHERE
      dishtype = pi_lx;
  ELSE
    SELECT *
    FROM
      t_temp_res
    WHERE
      Id = pi_pl
      AND dishtype = pi_lx;
  END IF;







END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_pxxsmxb_zhixiang` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_pxxsmxb_zhixiang` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_pxxsmxb_zhixiang`(IN  pi_branchid INT(11), 
                                           IN  pi_sb       SMALLINT, 
                                           IN  pi_ksrq     DATETIME, 
                                           IN  pi_jsrq     DATETIME, 
                                           IN  pi_pl       VARCHAR(50), 
                                           IN  pi_pxlx     INT, 
                                           OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '品项销售明细表'
label_main:
BEGIN 
  
  
  
  

  DECLARE v_total_count DOUBLE(13, 2) DEFAULT 0;
  DECLARE v_title       VARCHAR(300); 
  DECLARE v_dishNo      VARCHAR(50); 
  DECLARE v_price       DOUBLE(13, 2); 
  DECLARE v_unit        VARCHAR(50); 
  DECLARE v_number      DOUBLE(13, 2); 
  DECLARE v_share       DOUBLE(13, 2); 
  DECLARE v_sum_price   DOUBLE(13, 2); 
  DECLARE v_fetch_done  INT DEFAULT FALSE;
  DECLARE v_dishid      VARCHAR(50);
  DECLARE v_dishclass   VARCHAR(50);
  DECLARE v_dishtype    INT;
  DECLARE v_date_start  DATETIME;
  DECLARE v_date_end    DATETIME;
  DECLARE v_total_custnum_count    DOUBLE(13, 2); 
  DECLARE v_total_shouldmount_count    DOUBLE(13, 2); 
  DECLARE v_canju_mount DOUBLE(13, 2); 
  DECLARE cur_dish_detail CURSOR FOR SELECT a.dishid
                                          , a.dishunit
                                          , a.dishtype
                                          , b.columnid
                                          , ifnull(sum(a.dishnum), 0)
                                          , ifnull(max(a.orignalprice), 0)
																					, ifnull(sum(a.orignalprice*a.dishnum), 0)
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

  
  IF pi_sb IS NULL OR pi_ksrq IS NULL OR pi_jsrq IS NULL OR pi_pl IS NULL OR pi_pxlx IS NULL THEN
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
    dishunit VARCHAR(10),
    orignalprice DOUBLE(13, 2),
    ispot TINYINT,
    parentkey VARCHAR(40),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_order_detail
  SELECT a.orderid
       , a.dishnum
       , a.dishid
       , a.dishtype
       , a.dishunit
       , a.orignalprice
       , a.ispot
       , a.parentkey
       , a.childdishtype
       , a.primarykey
       , a.superkey
  FROM
    t_temp_order b, t_order_detail a
  WHERE
    b.orderid = a.orderid
    AND a.orignalprice > 0;

   
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   

  
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

  CREATE INDEX ix_t_tmp_order_detail_dishid ON t_temp_order_detail (dishid);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_parentkey;
  CREATE TEMPORARY TABLE t_temp_parentkey
  (
    parentkey VARCHAR(40)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_parentkey
  SELECT DISTINCT parentkey
  FROM
    t_temp_order_detail
  WHERE
    ispot = 1
    AND dishnum = 0;

  
  DELETE
  FROM
    t_temp_order_detail
  WHERE
    ispot = 1
    AND dishnum = 0;

  
  UPDATE t_temp_order_detail a, t_temp_parentkey b
  SET
    a.dishtype = 0
  WHERE
    a.parentkey = b.parentkey;

  

  
  UPDATE t_temp_order_detail a, t_dish b
  SET
    a.dishtype = b.dishtype
  WHERE
    a.dishid = b.dishid
    AND a.dishtype IS NULL;

  
  SELECT ifnull(sum(dishnum), 0)
  INTO
    v_total_count
  FROM
    t_temp_order_detail;

  IF v_total_count <= 0 THEN
    SELECT NULL;
    SET po_errmsg = '数据为空，无查询结果';
    LEAVE label_main;
  END IF;

  SELECT ifnull(sum(custnum), 0)
  INTO
    v_total_custnum_count
  FROM
    t_temp_order;

  IF v_total_custnum_count <= 0 THEN
    SELECT NULL;
    SET po_errmsg = '数据为空，无查询结果';
    LEAVE label_main;
  END IF;

  SELECT ifnull(sum(dishnum*orignalprice), 0)
  INTO
    v_total_shouldmount_count
  FROM
    t_temp_order_detail;

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
    unit VARCHAR(50), 
    number DOUBLE(13, 2), 
    thousandstimes DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
    turnover DOUBLE(13, 2),
    share DOUBLE(13, 2) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;


  
  OPEN cur_dish_detail;

read_loop:
  LOOP
    FETCH cur_dish_detail INTO v_dishid, v_unit, v_dishtype, v_dishclass, v_number, v_price,v_sum_price;

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

    INSERT INTO t_temp_res VALUES (v_dishclass, v_dishtype, v_title, v_dishNo, v_price, v_unit, v_number,round(v_number / v_total_custnum_count * 1000, 2),v_sum_price,v_sum_price/v_total_shouldmount_count*100, round(v_number / v_total_count * 100, 2));
  END LOOP;
  COMMIT;
  
  CLOSE cur_dish_detail;

  
  IF pi_pl = -1 OR pi_pl = 'DISHES_98' THEN
    SELECT sum(dishnum)
         , ifnull(max(orignalprice), 0),ifnull(sum(dishnum*orignalprice), 0)
    INTO
      @cnt, @price,@sumprice
    FROM
      t_temp_order_detail
    WHERE
      dishid = 'DISHES_98';

    SELECT ifnull(title, '餐具')
         , ifnull(dishno, '')
    INTO
      @title, @dishno
    FROM
      t_dish
    WHERE
      dishid = 'DISHES_98'
    LIMIT
      1;

    IF @cnt > 0 THEN
      INSERT INTO t_temp_res VALUES ('DISHES_98', 0, @title, @dishno, @price, '份', @cnt,round(@cnt / v_total_custnum_count * 1000, 2),@sumprice,@sumprice/v_total_shouldmount_count*100, round(@cnt / v_total_count * 100, 2));
    END IF;
  END IF;


  
  IF pi_pl != '-1' AND pi_pxlx != -1 THEN
    SELECT title
         , dishNo
         , price
         , unit
         , number
         , share
         , thousandstimes
         , orignalprice
         , turnover
    FROM
      t_temp_res
    WHERE
      columnid = pi_pl
      AND dishtype = pi_pxlx;

  ELSEIF pi_pl = '-1' AND pi_pxlx != -1 THEN
    SELECT title
         , dishNo
         , price
         , unit
         , number
         , share
         , thousandstimes
         , orignalprice
         , turnover
    FROM
      t_temp_res
    WHERE
      dishtype = pi_pxlx;

  ELSEIF pi_pl != '-1' AND pi_pxlx = -1 THEN
    SELECT title
         , dishNo
         , price
         , unit
         , number
         , share
         , thousandstimes
         , orignalprice
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
         , number
         , share
         , thousandstimes
         , orignalprice
         , turnover
    FROM
      t_temp_res;

  END IF;







END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_tcmxb` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_tcmxb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_tcmxb`(IN  pi_branchid INT(11), 
                                IN  pi_sb       SMALLINT, 
                                IN  pi_ksrq     DATETIME, 
                                IN  pi_jsrq     DATETIME, 
                                IN  pi_dqym     INT, 
                                IN  pi_myts     INT, 
                                OUT po_errmsg   VARCHAR(100)
                                
)
    SQL SECURITY INVOKER
    COMMENT '退菜明细表'
label_main:
BEGIN
  
  
  
  
  
  DECLARE v_date_start          DATETIME;
  DECLARE v_date_end            DATETIME;
  DECLARE v_increment_offset    INT;
  DECLARE v_increment_increment INT;

  
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

  IF pi_myts < 1 THEN
    SELECT NULL;
    SET po_errmsg = '每页显示记录条数不能少于1';
    LEAVE label_main;
  END IF;

  IF pi_dqym < -1 THEN
    SELECT NULL;
    SET po_errmsg = '当前页面只能输入大于-1的正整数';
    LEAVE label_main;
  END IF;

  
  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;
  SET v_increment_offset = @@auto_increment_offset;
  SET v_increment_increment = @@auto_increment_increment;

  
  SET v_date_start = pi_ksrq; 
  SET v_date_end = pi_jsrq; 

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_sb > -1 THEN
    INSERT INTO t_temp_order
    SELECT orderid
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
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      branchid = pi_branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND orderstatus = 3;
  END IF;
  CREATE UNIQUE INDEX uix_t_temp_order_orderid ON t_temp_order (orderid);


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_tcb;
  CREATE TEMPORARY TABLE t_temp_tcb
  (
    
    begintime DATETIME,
    orderid VARCHAR(50),
    dishid VARCHAR(50),
    dishnum DOUBLE(13, 2),
    amount DOUBLE(13, 2),
    username VARCHAR(50),
    discarduserid VARCHAR(50),
    discardreason VARCHAR(100)
  ) ENGINE = HEAP DEFAULT CHARSET = utf8;

  
  INSERT INTO t_temp_tcb
  SELECT b.begintime
       , b.orderid
       , b.dishid
       , b.dishnum
       , b.orderprice * b.dishnum
       , b.username
       , b.discarduserid
       , b.discardreason
  FROM
    t_temp_order a, t_order_detail_discard b
  WHERE
    a.orderid = b.orderid
    AND b.orderprice IS NOT NULL
  ORDER BY
    b.begintime DESC;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_re;
  CREATE TEMPORARY TABLE t_temp_re
  (
    temp_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    beginTime DATETIME,
    orderid VARCHAR(50),
    title VARCHAR(50),
    num DOUBLE(13, 2),
    amount DOUBLE(13, 2),
    waiter VARCHAR(50),
    discardusername VARCHAR(50),
    discardreason VARCHAR(100),
    PRIMARY KEY (temp_id)
  ) ENGINE = HEAP DEFAULT CHARSET = utf8;

  
  INSERT INTO t_temp_re (beginTime, orderid, title, num, amount, waiter, discardusername, discardreason)
  SELECT b.begintime
       , b.orderid
       , c.title
       , b.dishnum
       , b.amount
       , b.username
       , b.discarduserid
       , b.discardreason
  FROM
    t_temp_tcb b, t_dish c
  WHERE
    b.dishid = c.dishid;


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_user;
  CREATE TEMPORARY TABLE t_temp_user
  (
    job_number CHAR(10),
    name VARCHAR(32)
  ) ENGINE = HEAP DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_user
  SELECT a.job_number
       , b.name
  FROM
    t_b_employee a, t_b_user b
  WHERE
    a.user_id = b.id
    AND a.branch_id = pi_branchid;


  
  UPDATE t_temp_re t, t_temp_user b
  SET
    t.waiter = b.name
  WHERE
    t.waiter = b.job_number;

  
  UPDATE t_temp_re t, t_temp_user b
  SET
    t.discardusername = b.name
  WHERE
    t.discardusername = b.job_number;

  
  
  
  IF pi_dqym > -1 THEN
    SET @a = v_increment_offset + pi_dqym * pi_myts * v_increment_increment - v_increment_increment;
    SET @b = pi_myts;
    PREPARE s1 FROM 'select date_format(beginTime, \'%Y-%m-%d %H:%i\') beginTime,orderid,title,num,amount,waiter,discardusername,discardreason from t_temp_re where temp_id > ? limit ?';
    EXECUTE s1 USING @a, @b;
  ELSE
    PREPARE s1 FROM 'select date_format(beginTime, \'%Y-%m-%d %H:%i\') beginTime,orderid,title,num,amount,waiter,discardusername,discardreason from t_temp_re';
    EXECUTE s1;
  END IF;

  DEALLOCATE PREPARE s1;







END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_xxsjtjb` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_xxsjtjb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_xxsjtjb`(IN  pi_branchid INT(11), 
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

  
  SET v_date_start = str_to_date(concat(date_format(pi_ksrq, '%Y-%m-%d'), '00:00:00'), '%Y-%m-%d %H:%i:%s');
  SET v_date_end = str_to_date(concat(date_format(pi_jsrq, '%Y-%m-%d'), '23:59:59'), '%Y-%m-%d %H:%i:%s');
  SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 DAY), INTERVAL 1 SECOND);
  SET v_loop_num = timestampdiff(DAY, v_date_start, v_date_end) + 1;

  
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
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   

  
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

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
      AND b.payway IN (0, 1, 5, 8, 13, 17, 18)   
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
           , date_format(v_date_start, '%Y/%m/%d')
           , sum(dishnum * orignalprice)
      FROM
        t_temp_order_detail
      WHERE
        begintime BETWEEN v_date_start AND v_date_interval
      GROUP BY
        tableid;

    
    ELSEIF pi_cxlx = 2 THEN
      INSERT INTO t_temp_res (tableid, stime, svalue)
      SELECT tableid
           , date_format(v_date_start, '%Y/%m/%d')
           , ifnull(sum(payamount), 0)
      FROM
        t_temp_settlement_detail
      WHERE
        begintime BETWEEN v_date_start AND v_date_interval
      GROUP BY
        tableid;

      INSERT INTO t_temp_inflated
      SELECT tableid
           , date_format(v_date_start, '%Y/%m/%d')
           , ifnull(sum(Inflated), 0)
      FROM
        t_temp_order_member
      WHERE
        begintime BETWEEN v_date_start AND v_date_interval
      GROUP BY
        tableid;

    

    
    ELSEIF pi_cxlx = 3 THEN
      INSERT INTO t_temp_res (tableid, stime, svalue)
      SELECT tableid
           , date_format(v_date_start, '%Y/%m/%d')
           , ifnull(sum(mannum + womanNum + childNum), 0)
      FROM
        t_temp_order
      WHERE
        begintime BETWEEN v_date_start AND v_date_interval
      GROUP BY
        tableid;

    
    ELSEIF pi_cxlx = 4 THEN
      INSERT INTO t_temp_res (tableid, stime, svalue)
      SELECT tableid
           , date_format(v_date_start, '%Y/%m/%d')
           , count(1)
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

    
    SET @date_start = date_format(v_date_start, '%Y/%m/%d');
    SET v_sql = concat(v_sql, ',max(case stime when \'', @date_start, '\' then svalue else 0 end) \'', @date_start, '\'');

    SET v_date_start = date_add(v_date_start, INTERVAL 1 DAY);
    SET v_date_interval = date_add(v_date_interval, INTERVAL 1 DAY);
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

  
  SET v_sql = concat('SELECT areaname, tableid', v_sql);
  SET v_sql = concat(v_sql, ' FROM t_temp_res GROUP BY areaname, tableid order by tableid-0,areaname');
  SET @sql_xxsj = v_sql;
  PREPARE s1 FROM @sql_xxsj;
  EXECUTE s1;
  DEALLOCATE PREPARE s1;










END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_yhhdmxb` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_yhhdmxb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_yhhdmxb`(IN  pi_branchid INT(11), 
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
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   

  
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

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
       , sum(b.payamount)
       , sum(b.couponNum)
       , b.bankcardno
       , b.coupondetailid
       , '1'
       , (a.mannum + a.womanNum + a.childNum)
  FROM
    t_temp_order a, t_settlement_detail b
  WHERE
    a.orderid = b.orderid group by b.orderid,b.payway,b.coupondetailid;
  
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
    payway IN (0, 1, 5, 8, 17, 18 ,13,30);
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
           ifnull(ifnull(a.free_reason, a.company_name), b.name)
         END) AS name
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
       , ifnull(sum(couponNum), 0)
       , ifnull(sum(payamount), 0)
       , ifnull(count(1), 0)
       , ifnull(sum(perCapita), 0)
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
  SELECT ifnull(max(id), 0)
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

    
    SELECT ifnull(sum(a.orignalprice * a.dishnum), 0)
    INTO
      v_shouldamount
    FROM
      t_temp_order_detail a, t_temp_orderid b
    WHERE
      a.orderid = b.orderid;

    
    SELECT ifnull(sum(a.payamount), 0)
    INTO
      v_paidinamount
    FROM
      t_temp_paidinamout a, t_temp_orderid b
    WHERE
      a.orderid = b.orderid;

    
    SELECT ifnull(sum(a.Inflated), 0)
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
    AND type = 'PAYWAY';

  
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
     
       , round(paidinamount / perCapita, 2) AS perCapita
     , singular
FROM
  t_temp_res;

















END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_yhhdmxb_zhixiang` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_yhhdmxb_zhixiang` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_yhhdmxb_zhixiang`(IN  pi_branchid INT(11), 
                                           IN  pi_sb       SMALLINT, 
                                           IN  pi_ksrq     DATETIME, 
                                           IN  pi_jsrq     DATETIME, 
                                           IN  pi_hdmc     VARCHAR(50), 
                                           IN  pi_jsfs     INT, 
                                           IN  pi_hdlx     VARCHAR(10), 
                                           IN  pi_dqy      INT, 
                                           IN  pi_myts     INT, 
                                           OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '优惠活动明细表-子项'
label_main:
BEGIN 
  
  
  
  
  

  DECLARE v_date_start          DATETIME;
  DECLARE v_date_end            DATETIME;
  DECLARE v_loop_index          INT;
  DECLARE v_pname               VARCHAR(50);
  DECLARE v_ptype               CHAR(4);
  DECLARE v_payway              INT;
  DECLARE v_shouldamount        DOUBLE(13, 2);
  DECLARE v_paidinamount        DOUBLE(13, 2);
  DECLARE v_inflated            DOUBLE(13, 2); 
  DECLARE v_fetch_done          BOOL DEFAULT FALSE; 
  DECLARE v_increment_offset    INT;
  DECLARE v_increment_increment INT;



  
  DECLARE cur_p_detail CURSOR FOR SELECT DISTINCT pname
                                                , ptype
                                                , payway
                                  FROM
                                    t_temp_res_detail;

  
  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; 
  END;

  
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

  IF pi_myts < 1 THEN
    SELECT NULL;
    SET po_errmsg = '每页显示记录条数不能少于1';
    LEAVE label_main;
  END IF;

  IF pi_dqy < -1 THEN
    SELECT NULL;
    SET po_errmsg = '当前页面只能输入大于-1的正整数';
    LEAVE label_main;
  END IF;


  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;
  SET v_increment_offset = @@auto_increment_offset;
  SET v_increment_increment = @@auto_increment_increment;

  
  SET v_date_start = pi_ksrq; 
  SET v_date_end = pi_jsrq; 

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50),
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  IF pi_sb > -1 THEN
    INSERT INTO t_temp_order
    SELECT orderid
         , begintime
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
    a.orderid = b.orderid;

   
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   

  
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

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
  
  CREATE INDEX ix_t_temp_settlement_detail_orderid ON t_temp_settlement_detail (orderid);

  
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
           ifnull(ifnull(a.free_reason, a.company_name), b.name)
         END) AS name
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
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  INSERT INTO t_temp_res_detail
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


  INSERT INTO t_temp_res_detail
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

  IF pi_hdlx != '-1' THEN
    DELETE
    FROM
      t_temp_res_detail
    WHERE
      ptype != pi_hdlx;
  END IF;
  CREATE INDEX ix_t_temp_res_detail ON t_temp_res_detail (pname, ptype, payway);


  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    temp_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    begintime CHAR(10),
    orderid VARCHAR(50),
    price DOUBLE(13, 2),
    couponNum INT, 
    payamount DOUBLE(13, 2), 
    shouldamount DOUBLE(13, 2), 
    paidinamount DOUBLE(13, 2), 
    PRIMARY KEY (temp_id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;



  
  OPEN cur_p_detail;

lable_fetch_loop:
  LOOP

    FETCH cur_p_detail INTO v_pname, v_ptype, v_payway;
    IF v_fetch_done THEN
      LEAVE lable_fetch_loop;
    END IF;
    INSERT INTO t_temp_res (begintime, orderid, price, couponNum, payamount, shouldamount, paidinamount)
    SELECT date_format(max(begintime), '%Y-%m-%d')
         , orderid
         , max(payamount)
         , ifnull(sum(couponNum), 0)
         , ifnull(sum(payamount), 0)
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
       , sum(a.orignalprice * a.dishnum)
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
       , ifnull(sum(a.Inflated), 0)
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
       , ifnull(sum(a.payamount), 0)
  FROM
    t_temp_settlement_detail a, t_temp_orderid b
  WHERE
    a.orderid = b.orderid
    AND payway IN (0, 1, 5, 8, 13, 17, 18)  
  GROUP BY
    a.orderid;

  
  UPDATE t_temp_paidinamount a, t_temp_inflated b
  SET
    a.paidinamount = a.paidinamount - b.inflated
  WHERE
    a.orderid = b.orderid;


  
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


  
  
  IF pi_dqy > -1 THEN
    SET @a = v_increment_offset + pi_dqy * pi_myts * v_increment_increment - v_increment_increment;
    SET @b = pi_myts;
    PREPARE s1 FROM 'SELECT begintime,orderid,price,couponNum,payamount,shouldamount,paidinamount FROM t_temp_res where temp_id > ? limit ?';
    EXECUTE s1 USING @a, @b;
  ELSE
    PREPARE s1 FROM 'SELECT begintime,orderid,price,couponNum,payamount,shouldamount,paidinamount FROM t_temp_res';
    EXECUTE s1;
  END IF;
  DEALLOCATE PREPARE s1;
















END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_yyfx_pxxstj` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_yyfx_pxxstj` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_yyfx_pxxstj`(IN  pi_branchid INT(11), 
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
  DECLARE v_dishunit      VARCHAR(50);
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
    SET v_statistictime = date_format(pi_ksrq, '%Y-%m-%d');
    SET v_date_start = str_to_date(concat(v_statistictime, '00:00:00'), '%Y-%m-%d %H:%i:%s');
    SET v_date_end = str_to_date(concat(date_format(pi_jsrq, '%Y-%m-%d'), '23:59:59'), '%Y-%m-%d %H:%i:%s');
    SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 DAY), INTERVAL 1 SECOND);
    SET v_loop_num = timestampdiff(DAY, v_date_start, v_date_end) + 1;
  ELSEIF pi_xslx = 1 THEN
    SET v_statistictime = date_format(pi_ksrq, '%Y-%m');
    SET v_date_start = str_to_date(concat(v_statistictime, '-01 00:00:00'), '%Y-%m-%d %H:%i:%s');
    SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 MONTH), INTERVAL 1 SECOND);
    SET v_date_end = date_sub(date_add(str_to_date(concat(date_format(pi_jsrq, '%Y-%m'), '-01 00:00:00'), '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH), INTERVAL 1 SECOND);
    SET v_loop_num = timestampdiff(MONTH, v_date_start, v_date_end) + 1;
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
    dishunit VARCHAR(10),
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


 
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   

  
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;
  
  CREATE INDEX ix_t_temp_order_detail_begintime ON t_temp_order_detail (begintime);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail;
  CREATE TEMPORARY TABLE t_temp_res_detail
  (
    statistictime VARCHAR(20),
    dishid VARCHAR(50),
    dishunit VARCHAR(10),
    dishnum INT,
    dishprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  WHILE v_loop_num > 0
  DO
    INSERT INTO t_temp_res_detail
    SELECT v_statistictime
         , dishid
         , dishunit
         , sum(dishnum)
         , sum(dishnum * orignalprice)
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
      SET v_date_start = date_add(v_date_start, INTERVAL 1 DAY);
      SET v_date_interval = date_add(v_date_interval, INTERVAL 1 DAY);
      SET v_statistictime = date_format(v_date_start, '%Y-%m-%d');
    ELSE
      SET v_date_start = date_add(v_date_start, INTERVAL 1 MONTH);
      SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 MONTH), INTERVAL 1 SECOND);
      SET v_statistictime = date_format(v_date_start, '%Y-%m');
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
    dishunit VARCHAR(50),
    total_num INT,
    detail_num VARCHAR(1000),
    PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  INSERT INTO t_temp_res (showtype, dishid, dishunit, total_num)
  SELECT 0
       , dishid
       , dishunit
       , sum(dishnum)
  FROM
    t_temp_res_detail
  GROUP BY
    dishid
  , dishunit
  ORDER BY
    sum(dishnum) DESC
  LIMIT
    10;

  
  INSERT INTO t_temp_res (showtype, dishid, dishunit, total_num)
  SELECT 1
       , dishid
       , dishunit
       , sum(dishprice)
  FROM
    t_temp_res_detail
  GROUP BY
    dishid
  , dishunit
  ORDER BY
    sum(dishprice) DESC
  LIMIT
    10;


  
  SET @offset = @@auto_increment_offset;
  SET @increment = @@auto_increment_increment;
  SELECT ifnull(max(id), 0)
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
      SELECT group_concat(t.value_detail SEPARATOR '|')
      INTO
        v_value_detail
      FROM
        (
        SELECT concat(statistictime, ',', dishnum) AS value_detail
        FROM
          t_temp_res_detail
        WHERE
          dishid = v_dishid
          AND dishunit = v_dishunit
        ORDER BY
          statistictime ASC) t;

    ELSEIF v_showtype = 1 THEN
      SELECT group_concat(t.value_detail SEPARATOR '|')
      INTO
        v_value_detail
      FROM
        (
        SELECT concat(statistictime, ',', dishprice) AS value_detail
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
       , concat(b.title, '(', a.dishunit, ')') title
       , a.total_num
       , a.detail_num
  FROM
    t_temp_res a, t_dish b
  WHERE
    a.dishid = b.dishid;










END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_yyfx_pxxstj_column` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_yyfx_pxxstj_column` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_yyfx_pxxstj_column`(IN  pi_branchid INT(11), 
                                      IN  pi_xslx     SMALLINT, 
                                      IN  pi_ksrq     DATETIME, 
                                      IN  pi_jsrq     DATETIME, 
            IN pi_columnid   VARCHAR(100),                             OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '营业分析_品项销售统计'
label_main:
BEGIN
  
  DECLARE v_date_start    DATETIME;
  DECLARE v_date_end      DATETIME;
  DECLARE v_date_interval DATETIME; 
  DECLARE v_statistictime VARCHAR(15); 
  DECLARE v_dishid        VARCHAR(50);
  DECLARE v_dishunit      VARCHAR(50);
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
    SET v_statistictime = date_format(pi_ksrq, '%Y-%m-%d');
    SET v_date_start = str_to_date(concat(v_statistictime, '00:00:00'), '%Y-%m-%d %H:%i:%s');
    SET v_date_end = str_to_date(concat(date_format(pi_jsrq, '%Y-%m-%d'), '23:59:59'), '%Y-%m-%d %H:%i:%s');
    SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 DAY), INTERVAL 1 SECOND);
    SET v_loop_num = timestampdiff(DAY, v_date_start, v_date_end) + 1;
  ELSEIF pi_xslx = 1 THEN
    SET v_statistictime = date_format(pi_ksrq, '%Y-%m');
    SET v_date_start = str_to_date(concat(v_statistictime, '-01 00:00:00'), '%Y-%m-%d %H:%i:%s');
    SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 MONTH), INTERVAL 1 SECOND);
    SET v_date_end = date_sub(date_add(str_to_date(concat(date_format(pi_jsrq, '%Y-%m'), '-01 00:00:00'), '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH), INTERVAL 1 SECOND);
    SET v_loop_num = timestampdiff(MONTH, v_date_start, v_date_end) + 1;
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
    dishunit VARCHAR(10),
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

  
 
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;

   
  
  delete from t_temp_order_detail where dishid not in (select dishid from t_dish_dishtype where columnid = pi_columnid) or (dishtype =2 and superkey <> primarykey);

  
  CREATE INDEX ix_t_temp_order_detail_begintime ON t_temp_order_detail (begintime);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail;
  CREATE TEMPORARY TABLE t_temp_res_detail
  (
    statistictime VARCHAR(20),
    dishid VARCHAR(50),
    dishunit VARCHAR(10),
    dishnum INT,
    dishprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  WHILE v_loop_num > 0
  DO
    INSERT INTO t_temp_res_detail
    SELECT v_statistictime
         , dishid
         , dishunit
         , sum(dishnum)
         , sum(dishnum * orignalprice)
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
      SET v_date_start = date_add(v_date_start, INTERVAL 1 DAY);
      SET v_date_interval = date_add(v_date_interval, INTERVAL 1 DAY);
      SET v_statistictime = date_format(v_date_start, '%Y-%m-%d');
    ELSE
      SET v_date_start = date_add(v_date_start, INTERVAL 1 MONTH);
      SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 MONTH), INTERVAL 1 SECOND);
      SET v_statistictime = date_format(v_date_start, '%Y-%m');
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
    dishunit VARCHAR(50),
    total_num INT,
    detail_num VARCHAR(1000),
    PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  INSERT INTO t_temp_res (showtype, dishid, dishunit, total_num)
  SELECT 0
       , dishid
       , dishunit
       , sum(dishnum)
  FROM
    t_temp_res_detail
  GROUP BY
    dishid
  , dishunit
  ORDER BY
    sum(dishnum) DESC
  LIMIT
    10;

  
  INSERT INTO t_temp_res (showtype, dishid, dishunit, total_num)
  SELECT 1
       , dishid
       , dishunit
       , sum(dishprice)
  FROM
    t_temp_res_detail
  GROUP BY
    dishid
  , dishunit
  ORDER BY
    sum(dishprice) DESC
  LIMIT
    10;


  
  SET @offset = @@auto_increment_offset;
  SET @increment = @@auto_increment_increment;
  SELECT ifnull(max(id), 0)
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
      SELECT group_concat(t.value_detail SEPARATOR '|')
      INTO
        v_value_detail
      FROM
        (
        SELECT concat(statistictime, ',', dishnum) AS value_detail
        FROM
          t_temp_res_detail
        WHERE
          dishid = v_dishid
          AND dishunit = v_dishunit
        ORDER BY
          statistictime ASC) t;

    ELSEIF v_showtype = 1 THEN
      SELECT group_concat(t.value_detail SEPARATOR '|')
      INTO
        v_value_detail
      FROM
        (
        SELECT concat(statistictime, ',', dishprice) AS value_detail
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
       , concat(b.title, '(', a.dishunit, ')') title
       , a.total_num
       , a.detail_num
  FROM
    t_temp_res a, t_dish b
  WHERE
    a.dishid = b.dishid;










END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_yyfx_yhhdtj` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_yyfx_yhhdtj` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_yyfx_yhhdtj`(IN  pi_branchid INT(11), 
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
    SET v_statistictime = date_format(pi_ksrq, '%Y-%m-%d');
    SET v_date_start = str_to_date(concat(v_statistictime, '00:00:00'), '%Y-%m-%d %H:%i:%s');
    SET v_date_end = str_to_date(concat(date_format(pi_jsrq, '%Y-%m-%d'), '23:59:59'), '%Y-%m-%d %H:%i:%s');
    SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 DAY), INTERVAL 1 SECOND);
    SET v_loop_num = timestampdiff(DAY, v_date_start, v_date_end) + 1;
  ELSEIF pi_xslx = 1 THEN
    SET v_statistictime = date_format(pi_ksrq, '%Y-%m');
    SET v_date_start = str_to_date(concat(v_statistictime, '-01 00:00:00'), '%Y-%m-%d %H:%i:%s');
    SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 MONTH), INTERVAL 1 SECOND);
    SET v_date_end = date_sub(date_add(str_to_date(concat(date_format(pi_jsrq, '%Y-%m'), '-01 00:00:00'), '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH), INTERVAL 1 SECOND);
    SET v_loop_num = timestampdiff(MONTH, v_date_start, v_date_end) + 1;
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
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   

  
  delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

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
       , ifnull(sum(payamount), 0)
  FROM
    t_temp_settlement_detail
  WHERE
    payamount > 0
    AND payway IN (0, 1, 5, 8,13,17,18)
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
           ifnull(ifnull(a.free_reason, a.company_name), b.name)
         END) AS name
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

        SELECT max(ptype)
             , ifnull(sum(couponNum), 0)
             , ifnull(sum(payamount), 0)
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

        
        SELECT ifnull(sum(a.orignalprice * a.dishnum), 0)
        INTO
          v_shouldamount
        FROM
          t_temp_order_detail a, t_temp_orderid b
        WHERE
          a.orderid = b.orderid;


        
        SELECT ifnull(sum(payamount), 0)
        INTO
          v_paidinacount
        FROM
          t_temp_settlement_paidinamount a, t_temp_orderid b
        WHERE
          a.orderid = b.orderid;


        
        SELECT ifnull(sum(Inflated), 0)
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
      SET v_date_start = date_add(v_date_start, INTERVAL 1 DAY);
      SET v_date_interval = date_add(v_date_interval, INTERVAL 1 DAY);
      SET v_statistictime = date_format(v_date_start, '%Y-%m-%d');
    ELSE
      SET v_date_start = date_add(v_date_start, INTERVAL 1 MONTH);
      SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 MONTH), INTERVAL 1 SECOND);
      SET v_statistictime = date_format(v_date_start, '%Y-%m');
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
         , sum(pcount)
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
         , sum(pamount)
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
         , sum(pshouldamount)
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
         , sum(ppaidinamount)
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
         , sum(pcount)
    FROM
      t_temp_res_detail
    GROUP BY
      ptype;

    
    INSERT INTO t_temp_res (showtype, pname, total_num)
    SELECT 1
         , ptype
         , sum(pamount)
    FROM
      t_temp_res_detail
    GROUP BY
      ptype;

    
    INSERT INTO t_temp_res (showtype, pname, total_num)
    SELECT 2
         , ptype
         , sum(pshouldamount)
    FROM
      t_temp_res_detail
    GROUP BY
      ptype;

    
    INSERT INTO t_temp_res (showtype, pname, total_num)
    SELECT 3
         , ptype
         , sum(ppaidinamount)
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
         , count(1)
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
        SELECT group_concat(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT concat(statistictime, ',', pcount) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            pname = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSEIF v_showtype = 1 THEN
        SELECT group_concat(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT concat(statistictime, ',', pamount) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            pname = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSEIF v_showtype = 2 THEN
        SELECT group_concat(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT concat(statistictime, ',', pshouldamount) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            pname = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSEIF v_showtype = 3 THEN
        SELECT group_concat(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT concat(statistictime, ',', ppaidinamount) AS value_detail
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
        SELECT group_concat(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT concat(max(statistictime), ',', sum(pcount)) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            ptype = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSEIF v_showtype = 1 THEN
        SELECT group_concat(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT concat(max(statistictime), ',', sum(pamount)) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            ptype = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSEIF v_showtype = 2 THEN
        SELECT group_concat(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT concat(max(statistictime), ',', sum(pshouldamount)) AS value_detail
          FROM
            t_temp_res_detail
          WHERE
            ptype = v_pname
          ORDER BY
            statistictime ASC) t;

      ELSEIF v_showtype = 3 THEN
        SELECT group_concat(t.value_detail SEPARATOR '|')
        INTO
          v_value_detail
        FROM
          (
          SELECT concat(max(statistictime), ',', sum(ppaidinamount)) AS value_detail
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



END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_yyfx_yysjtj` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_yyfx_yysjtj` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_yyfx_yysjtj`(IN  pi_branchid INT(11), 
                                      IN  pi_xslx     SMALLINT, 
                                      IN  pi_ksrq     DATETIME, 
                                      IN  pi_jsrq     DATETIME, 
                                      OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '营业分析_营业数据统计'
label_main:
BEGIN
  
  
  
  
  
  

  DECLARE v_date_start        DATETIME;
  DECLARE v_date_end          DATETIME;
  DECLARE v_date_interval     DATETIME; 
  DECLARE v_loop_num          INT DEFAULT 0; 
  DECLARE v_statistictime     VARCHAR(15); 
  DECLARE v_shouldamount      DOUBLE(13, 2); 
  DECLARE v_paidinamount      DOUBLE(13, 2); 
  DECLARE v_inflated          DOUBLE(13, 2); 
  DECLARE v_person_con        DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_table_num         INT DEFAULT 0; 
  DECLARE v_sa_settlementnum  INT DEFAULT 0; 

  
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
    SET v_statistictime = date_format(pi_ksrq, '%Y-%m-%d');
    SET v_date_start = str_to_date(concat(v_statistictime, '00:00:00'), '%Y-%m-%d %H:%i:%s');
    SET v_date_end = str_to_date(concat(date_format(pi_jsrq, '%Y-%m-%d'), '23:59:59'), '%Y-%m-%d %H:%i:%s');
    SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 DAY), INTERVAL 1 SECOND);
    SET v_loop_num = timestampdiff(DAY, v_date_start, v_date_end) + 1;
  ELSEIF pi_xslx = 1 THEN
    SET v_statistictime = date_format(pi_ksrq, '%Y-%m');
    SET v_date_start = str_to_date(concat(v_statistictime, '-01 00:00:00'), '%Y-%m-%d %H:%i:%s');
    SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 MONTH), INTERVAL 1 SECOND);
    SET v_date_end = date_sub(date_add(str_to_date(concat(date_format(pi_jsrq, '%Y-%m'), '-01 00:00:00'), '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH), INTERVAL 1 SECOND);
    SET v_loop_num = timestampdiff(MONTH, v_date_start, v_date_end) + 1;
  ELSE
    SELECT NULL;
    LEAVE label_main;
  END IF;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
    orderid VARCHAR(50),
		womanNum TINYINT UNSIGNED,
    childNum TINYINT UNSIGNED,
    mannum TINYINT UNSIGNED,
    ordertype TINYINT,
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_order
  SELECT orderid
       , womanNum
       , childNum
       , mannum
       , ordertype
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
       , b.dishnum
       , b.orignalprice
       , b.begintime
       , b.childdishtype
       , b.primarykey
       , b.superkey
       , b.dishtype
  FROM
    t_temp_order a, t_order_detail b
  WHERE
    a.orderid = b.orderid
    AND orignalprice > 0;
 
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   

  
  delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

  
  CREATE INDEX ix_t_temp_order_detail_begintime ON t_temp_order_detail (begintime);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_settlement_detail
  SELECT b.orderid
       , b.payway
       , b.payamount
       , a.begintime
  FROM
    t_temp_order a, t_settlement_detail b
  WHERE
    a.orderid = b.orderid
    AND b.payamount > 0
    AND b.payway IN (0, 1, 5, 8,13,17,18,30);
  CREATE INDEX ix_t_temp_settlement_detail_begintime ON t_temp_settlement_detail (begintime);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  CREATE TEMPORARY TABLE t_temp_order_member
  (
    orderid VARCHAR(50),
    Inflated DOUBLE(13, 2),
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
       , a.begintime
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid;
  CREATE INDEX ix_t_temp_order_member_begintime ON t_temp_order_member (begintime);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    statistictime VARCHAR(15), 
    shouldamount DOUBLE(13, 2), 
    paidinamount DOUBLE(13, 2), 
    discountamount DOUBLE(13, 2), 
    personpercent  DOUBLE(13, 2), 
    tablecount   INT DEFAULT 0 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  
  WHILE v_loop_num > 0
  DO

    
    SELECT ifnull(sum(a.orignalprice * a.dishnum), 0)
    INTO
      v_shouldamount
    FROM
      t_temp_order_detail a left join t_temp_order b on a.orderid = b.orderid
    WHERE
      b.begintime BETWEEN v_date_start AND v_date_interval;

    
    SELECT ifnull(sum(payamount), 0)
    INTO
      v_paidinamount
    FROM
      t_temp_settlement_detail
    WHERE
      begintime BETWEEN v_date_start AND v_date_interval;

    
    SELECT ifnull(sum(Inflated), 0)
    INTO
      v_inflated
    FROM
      t_temp_order_member
    WHERE
      begintime BETWEEN v_date_start AND v_date_interval;

    
    SELECT IFNULL(count(orderid),0) 
         , IFNULL(sum(womanNum + childNum + mannum),0) 
    INTO
      v_table_num, v_sa_settlementnum
    FROM
      t_temp_order
    WHERE
      begintime BETWEEN v_date_start AND v_date_interval;

    IF v_table_num > 0 THEN
      SET v_person_con = (v_paidinamount - v_inflated) / v_sa_settlementnum;
    END IF;

    INSERT INTO t_temp_res VALUES (v_statistictime, v_shouldamount, v_paidinamount - v_inflated, v_shouldamount - v_paidinamount + v_inflated,v_person_con,v_table_num);

    IF pi_xslx = 0 THEN
      SET v_date_start = date_add(v_date_start, INTERVAL 1 DAY);
      SET v_date_interval = date_add(v_date_interval, INTERVAL 1 DAY);
      SET v_statistictime = date_format(v_date_start, '%Y-%m-%d');
    ELSE
      SET v_date_start = date_add(v_date_start, INTERVAL 1 MONTH);
      SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 MONTH), INTERVAL 1 SECOND);
      SET v_statistictime = date_format(v_date_start, '%Y-%m');
    END IF;

    SET v_loop_num = v_loop_num - 1;
  END WHILE;
  COMMIT;

  
  SELECT *
  FROM
    t_temp_res;









END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_yyhzb` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_yyhzb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_yyhzb`(IN `pi_branchid` varchar(50)
																,IN  pi_sb	SMALLINT
																,IN `pi_ksrq` datetime
																,IN `pi_jsrq` datetime
																,IN `pi_brandid` int(11)
																,IN `pi_marketid` int(11)
																,IN `pi_areaid` int(11)
																,OUT `po_errormsg` varchar(100))
    SQL SECURITY INVOKER
    COMMENT '营业汇总表'
label_main:
BEGIN
  DECLARE v_date_start    DATETIME;
  DECLARE v_date_end      DATETIME;
  DECLARE v_cur_branchid  VARCHAR(50);
  DECLARE v_branchname  VARCHAR(50);
  DECLARE v_fetch_done BOOL DEFAULT FALSE; 
  DECLARE v_branchid VARCHAR(50) DEFAULT NULL; 
  DECLARE v_shouldamount VARCHAR(50); 
  DECLARE v_paidinamount  VARCHAR(128); 
  DECLARE v_brandid  VARCHAR(128); 
  DECLARE v_marketid  VARCHAR(128); 
  DECLARE v_areaid  VARCHAR(128); 
  DECLARE v_jde  VARCHAR(128); 
  DECLARE v_brand_name  VARCHAR(128); 
  DECLARE v_market_name  VARCHAR(128); 
  DECLARE v_area_name  VARCHAR(128); 
  DECLARE v_datetime  VARCHAR(128); 
  DECLARE v_num  INT(11); 

  
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
  DECLARE v_da_credit2      	  DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_da_free2      		  DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_da_totalfree      	  DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_da_mebervalueaddnet   DOUBLE(13, 2) DEFAULT 0; 
  
  
  DECLARE v_pid         VARCHAR(50); 
  DECLARE v_pname         VARCHAR(128); 
  DECLARE v_ptype         CHAR(4); 
  DECLARE v_pamount       DOUBLE(13, 2); 
  DECLARE v_value_detail  VARCHAR(10000); 
  DECLARE v_value_total  VARCHAR(10000); 

  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; 
  END;

  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
	SELECT NULL;
	GET DIAGNOSTICS CONDITION 1 po_errormsg = MESSAGE_TEXT;
  END;
  
  IF pi_brandid > -1 THEN
  
	SELECT id,name INTO v_brandid,v_brand_name  FROM t_c_brand WHERE id = pi_brandid;
  ELSE
	SELECT id,name INTO v_brandid,v_brand_name FROM t_c_brand LIMIT 1;
  END IF;  
  
  IF pi_marketid > -1 THEN
  
	SELECT id,name INTO v_marketid,v_market_name  FROM t_c_market WHERE id = pi_marketid;
  ELSE
	SELECT id,name INTO v_marketid,v_market_name FROM t_c_market LIMIT 1;
  END IF;  
  
  IF pi_areaid > -1 THEN
  
	SELECT id,name INTO v_areaid,v_area_name  FROM t_c_area WHERE id = pi_areaid;
  ELSE
	SELECT id,name INTO v_areaid,v_area_name FROM t_c_area LIMIT 1;
  END IF;  
  
  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;
  SET SESSION group_concat_max_len=102400;

  SET v_date_start = str_to_date(concat(date_format(pi_ksrq,'%Y-%m-%d'),'00:00:00'),'%Y-%m-%d %H:%i:%s');
  SET v_date_end = str_to_date(concat(date_format(pi_jsrq,'%Y-%m-%d'),'23:59:59'),'%Y-%m-%d %H:%i:%s');
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_branch;
  CREATE TEMPORARY TABLE t_temp_branch
  (
	branchid VARCHAR(50),
    branchname VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
   IF pi_branchid > -1 THEN
	
	INSERT INTO t_temp_branch SELECT branchid,branchname  FROM t_branch WHERE branchid=CONCAT(pi_branchid) OR branchname LIKE CONCAT('%',pi_branchid,'%'); 
	SELECT ifnull(COUNT(1),0) INTO v_branchid FROM t_temp_branch;
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
    ordertype TINYINT,
	branchid VARCHAR(50),
    begintime DATETIME,
    endtime DATETIME
	
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);
  
  CREATE INDEX ix_t_temp_order_branchid ON t_temp_order (branchid);
  
  
  IF pi_sb > -1 THEN
    INSERT INTO t_temp_order
    SELECT orderid
         , ordertype
		 , t_temp_branch.branchid branchid
         , begintime
         , endtime
    FROM
      t_order USE INDEX (IX_t_order_begintime), t_temp_branch
    WHERE
      t_order.branchid = t_temp_branch.branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND shiftid = pi_sb
      AND orderstatus = 3;
  ELSE
    INSERT INTO t_temp_order
    SELECT orderid
         , ordertype
		 , t_temp_branch.branchid branchid
         , begintime
         , endtime
    FROM
      t_order USE INDEX (IX_t_order_begintime), t_temp_branch
    WHERE
      t_order.branchid = t_temp_branch.branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND orderstatus = 3;
  END IF;
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
  CREATE TEMPORARY TABLE t_temp_order_detail
  (
    orderid VARCHAR(50),
    dishnum DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
    payamount DOUBLE(13, 2),
    ordertype TINYINT,
    pricetype CHAR(1),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50),
    dishtype TINYINT
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_order_detail_orderid ON t_temp_order_detail (orderid);
  
  INSERT INTO t_temp_order_detail
  SELECT b.orderid
       , b.dishnum
       , b.orignalprice
       , b.payamount
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
    AND orignalprice > 0;  

	 
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   

  
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;
 
	
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    couponid VARCHAR(50),
    ordertype TINYINT,
    isviporder TINYINT, 
    coupondetailid VARCHAR(50),
	couponNum INT,
	bankcardno VARCHAR(50),
    membercardno VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_settlement_detail_orderid ON t_temp_settlement_detail (orderid);
  
  INSERT INTO t_temp_settlement_detail
  SELECT b.orderid
       , b.payway
       , b.payamount
       , b.couponid
       , a.ordertype
       , CASE
         WHEN b.payway = 8 AND char_length(b.membercardno) > 1 THEN
           1
         ELSE
           0
         END
		, b.coupondetailid
		, b.couponNum
		, b.bankcardno
        , b.membercardno
  FROM
    t_temp_order a, t_settlement_detail b
  WHERE
    a.orderid = b.orderid;
	
  
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
  
  CREATE INDEX ix_t_temp_order_member_orderid ON t_temp_order_member (orderid);

  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
       , b.netvalue
       , a.ordertype
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid;
	
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid;
  CREATE TEMPORARY TABLE t_temp_orderid
  (
	orderid VARCHAR(50),
	begintime datetime
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  
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
           ifnull(ifnull(a.free_reason, a.company_name), b.name)
         END) AS name
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
    a.preferential = b.id
  ORDER BY b.code; 

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new;
  CREATE TEMPORARY TABLE t_temp_settlement_new
  (
    orderid VARCHAR(50),
    payway INT, 
    payamount DOUBLE(13, 2), 
	pid VARCHAR(50),
    pname VARCHAR(128), 
    ptype CHAR(4) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  CREATE INDEX ix_t_temp_settlement_new ON t_temp_settlement_new(orderid);

  
  INSERT INTO t_temp_settlement_new
  SELECT a.orderid
       , a.payway
       , a.payamount
	   , b.pid
       , b.pname
       , b.ptype
  FROM
    t_temp_settlement_detail a, t_temp_preferential b
  WHERE
    a.payway = 6
    AND a.coupondetailid = b.pid;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new_sub;
  CREATE TEMPORARY TABLE t_temp_settlement_new_sub
  (
    orderid VARCHAR(50),
    payway INT, 
    payamount DOUBLE(13, 2), 
    pid VARCHAR(50), 
    pname VARCHAR(128), 
    ptype CHAR(4) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  CREATE INDEX ix_t_temp_settlement_new_sub ON t_temp_settlement_new_sub(orderid);
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail_sub;
  CREATE TEMPORARY TABLE t_temp_order_detail_sub
  (
    orderid VARCHAR(50),
    dishnum DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
    payamount DOUBLE(13, 2),
    ordertype TINYINT,
    pricetype CHAR(1),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_order_detail_sub_orderid ON t_temp_order_detail_sub (orderid);
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail_sub;
  CREATE TEMPORARY TABLE t_temp_settlement_detail_sub
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    couponid VARCHAR(50),
    ordertype TINYINT,
    isviporder TINYINT, 
    coupondetailid VARCHAR(50),
	couponNum INT,
	bankcardno VARCHAR(50),
    membercardno VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_settlement_detail_sub_orderid ON t_temp_settlement_detail_sub (orderid);
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
	datetime VARCHAR(50),
	brandid VARCHAR(50),
	brandname VARCHAR(50),
	marketid VARCHAR(50),
	market  VARCHAR(50),
	areaid VARCHAR(50),
	area  VARCHAR(50),
	managePattern  VARCHAR(50),
	managePatternid VARCHAR(50),
	jde VARCHAR(50),
	branchid VARCHAR(50),  
	branchname VARCHAR(50), 
    shouldamount decimal(13, 2), 
    paidinamount decimal(13, 2), 
    discountamount decimal(13, 2), 
    cash decimal(13, 2) DEFAULT 0.00, 
    credit decimal(13, 2) DEFAULT 0.00, 
    credit2 decimal(13, 2) DEFAULT 0.00, 
    card decimal(13, 2) DEFAULT 0.00, 
    icbccard decimal(13, 2) DEFAULT 0.00, 
    weixin decimal(13, 2) DEFAULT 0.00, 
    zhifubao decimal(13, 2) DEFAULT 0.00, 
    merbervaluenet decimal(13, 2) DEFAULT 0.00, 
    integralconsum decimal(13, 2) DEFAULT 0.00, 
    meberTicket decimal(13, 2) DEFAULT 0.00, 
    fraction decimal(13, 2) DEFAULT 0.00, 
    give decimal(13, 2) DEFAULT 0.00, 
    roundoff decimal(13, 2) DEFAULT 0.00, 
    mebervalueadd decimal(13, 2) DEFAULT 0.00, 
    free decimal(13, 2) DEFAULT 0.00, 
    free2 decimal(13, 2) DEFAULT 0.00, 
	preferentialdetail VARCHAR(10000), 
	totalfree VARCHAR(10000), 
	PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail;
  CREATE TEMPORARY TABLE t_temp_res_detail
  (
	pid VARCHAR(50) NOT NULL,
    pname VARCHAR(128), 
    ptype CHAR(4), 
    payamount DOUBLE(13, 2), 
	PRIMARY KEY(pid)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  
  CREATE UNIQUE INDEX ix_t_temp_res_detail_pid ON t_temp_res_detail (pid);	
	
	
	BEGIN
		DECLARE cur_total CURSOR FOR SELECT DISTINCT pname,pid
										   FROM
											 t_temp_settlement_new;
			
		SET v_fetch_done = FALSE;
		OPEN cur_total;
		TRUNCATE t_temp_res_detail;
			 
		lable_fetch_total_loop:
		LOOP
			FETCH cur_total INTO v_pname,v_pid;
			
			IF v_fetch_done THEN
			SET v_fetch_done = FALSE;
			LEAVE lable_fetch_total_loop;
			END IF;

			SELECT max(ptype)
				, ifnull(sum(payamount), 0)
			INTO
				v_ptype, v_pamount
			FROM
				t_temp_settlement_new
			WHERE
				pname = v_pname
			AND pid = v_pid;
				  
			INSERT INTO t_temp_res_detail (pid,pname,ptype,payamount) VALUES( v_pid, v_pname , v_ptype , v_pamount );

		END LOOP lable_fetch_total_loop;
		
		
		INSERT INTO t_temp_res_detail (pid,pname,ptype,payamount) VALUES ('youmian', '会员优免' , 'ym' , 0);
		
		SELECT group_concat(t.value_detail SEPARATOR '|')
			INTO
			  v_value_total
			FROM
			 (
			  SELECT concat(pid, ',', pname, ',', payamount) AS value_detail
			  FROM
				t_temp_res_detail
			  ) t;
		
		CLOSE cur_total;
	END;
	
 
  BEGIN
  
	DECLARE cur_branchid CURSOR FOR SELECT DISTINCT branchid
										FROM t_temp_order USE INDEX (ix_t_temp_order_branchid)  GROUP BY branchid;
										
	
	IF DATEDIFF(v_date_end,v_date_start) > 0 THEN 
		SET v_datetime = concat(date_format(v_date_start,'%Y-%m-%d'),' - ',date_format(v_date_end,'%Y-%m-%d'));
	ELSE
		SET v_datetime = concat(date_format(v_date_start,'%Y-%m-%d'));
	END IF;
	
	SET v_fetch_done = FALSE;
	
	OPEN cur_branchid;
	
	label_fetch_loop:
		LOOP
			FETCH cur_branchid into v_branchid;
			
			IF v_fetch_done THEN
				leave label_fetch_loop;
			END IF;	
			
			SELECT branchname into v_branchname 
			FROM t_temp_branch
			WHERE branchid = v_branchid;
			
			TRUNCATE t_temp_orderid;
			
			INSERT INTO t_temp_orderid
			SELECT DISTINCT orderid,begintime
			FROM t_temp_order USE INDEX (ix_t_temp_order_branchid) WHERE branchid = v_branchid;
			
			
			TRUNCATE t_temp_order_detail_sub;
			
			
			INSERT INTO t_temp_order_detail_sub
		    SELECT a.orderid
			     , a.dishnum
			     , a.orignalprice
				 , a.payamount
			     , a.ordertype
			     , a.pricetype
			     , a.childdishtype
			     , a.primarykey
			     , a.superkey
		    FROM
			  t_temp_order_detail a, t_temp_orderid b
		    WHERE
			  a.orderid = b.orderid
			AND orignalprice > 0;
			
			
			TRUNCATE t_temp_settlement_detail_sub;
			
			  INSERT INTO t_temp_settlement_detail_sub
			  SELECT a.orderid
				   , a.payway
				   , a.payamount
				   , a.couponid
				   , a.ordertype
				   , a.payway
				   , a.coupondetailid
				   , a.couponNum
				   , a.bankcardno
				   , a.membercardno
			  FROM
				t_temp_settlement_detail a, t_temp_orderid b
			  WHERE
				a.orderid = b.orderid;
			
			
			SELECT ifnull(sum(orignalprice * dishnum), 0)
			INTO
			  v_shouldamount
			FROM
			  t_temp_order_detail_sub;
			  

			
		    SELECT ifnull(sum(
				 CASE
				 WHEN payway = 0 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
				,ifnull(sum(
				 CASE
				 WHEN payway = 6 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
				,ifnull(sum(
				 CASE
				 WHEN payway = 13 THEN 
				   payamount
				 ELSE
				   0
				 END), 0) 
			   ,ifnull(sum(
				 CASE
				 WHEN payway = 1 and membercardno='0' and payamount<>0 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 1 and membercardno='1' and payamount<>0 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 5 AND payamount > 0 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 8 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 11 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 12 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 7 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , 0 - ifnull(sum(
				 CASE
				 WHEN payway = 20 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 17 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 18 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
		    INTO
			  v_pa_cash,v_da_free,v_da_credit2, v_pa_card, v_pa_icbc_card, v_pa_credit, v_pa_paidinamount, v_da_integralconsum, v_da_meberTicket, v_da_fraction, v_da_roundoff, v_pa_weixin, v_pa_zhifubao
		    FROM
			  t_temp_settlement_detail_sub;
				
			
			SELECT 
				ifnull(sum(
						CASE pricetype
						WHEN '1' THEN
						orignalprice * dishnum
						ELSE
							0
						END), 0) 
			INTO
				v_da_give 
			FROM
				t_temp_order_detail_sub;
			
			
			SELECT ifnull(sum(Inflated), 0)
			INTO
			  v_da_mebervalueadd
			FROM
			  t_temp_order_member
			JOIN  t_temp_orderid
			ON
				t_temp_order_member.orderid = t_temp_orderid.orderid;
				
			
			SET v_paidinamount = v_pa_cash + v_pa_credit + v_pa_card + v_pa_paidinamount - v_da_mebervalueadd + v_pa_icbc_card + v_pa_weixin + v_pa_zhifubao+v_da_credit2;
				
			
			TRUNCATE t_temp_settlement_new_sub;

			
			INSERT INTO t_temp_settlement_new_sub
			SELECT a.orderid ,a.payway,a.payamount,a.pid,a.pname,a.ptype
			FROM
			  t_temp_settlement_new a
			 JOIN t_temp_orderid b
			ON
			  a.orderid = b.orderid
			WHERE
			 a.payway = '6';

			BEGIN
			
			DECLARE cur_pname CURSOR FOR SELECT DISTINCT pname
										   FROM
											 t_temp_settlement_new_sub;
			
			SET v_fetch_done = FALSE;
			OPEN cur_pname;
			  
			UPDATE t_temp_res_detail SET payamount = 0;
			 
			lable_fetch_pname_loop:
			  LOOP
				FETCH cur_pname INTO v_pname;
				IF v_fetch_done THEN
				  SET v_fetch_done = FALSE;
				  LEAVE lable_fetch_pname_loop;
				END IF;

				SELECT pid,max(ptype)
					 , ifnull(sum(payamount), 0)
				INTO
				  v_pid,v_ptype, v_pamount
				FROM
				  t_temp_settlement_new_sub
				WHERE
				  pname = v_pname;
				  
				UPDATE t_temp_res_detail SET payamount = v_pamount  WHERE pid = v_pid;

			  END LOOP lable_fetch_pname_loop;
			  
			  
			  CLOSE cur_pname;
			  
			 END;
			 
			SET v_da_totalfree = 0;
			SELECT ifnull(sum(payamount),0) INTO
			     v_da_totalfree
			FROM  t_temp_res_detail;
			
		    
		    SET v_da_free2 = v_shouldamount - v_paidinamount - ifnull(v_da_roundoff,0)- ifnull(v_da_integralconsum,0)- ifnull(v_da_meberTicket,0)
							- ifnull(v_da_totalfree,0)- ifnull(v_da_fraction,0)- ifnull(v_da_give,0)- ifnull(v_da_mebervalueadd,0);		
							
			SELECT SUM(b.orignalprice*b.dishnum - ifnull(b.payamount,0)) into v_da_free FROM t_temp_order_detail_sub b  WHERE  b.pricetype = 0 AND b.orignalprice*b.dishnum <> IFNULL(b.payamount,0);
			
			UPDATE t_temp_res_detail SET payamount = v_da_free2  WHERE pid = 'youmian';
			 
			SELECT group_concat(t.value_detail SEPARATOR '|')
			INTO
			  v_value_detail
			FROM
			 (
			  SELECT concat(pid, ',', pname, ',', payamount) AS value_detail
			  FROM
				t_temp_res_detail
			  ) t;
				
			SELECT ifnull(branch_id,0)
			INTO v_jde
			FROM t_branch_code a WHERE a.branch_id_code = v_branchid;
			
			
			INSERT INTO t_temp_res 
				(brandid,brandname,marketid,market,areaid,area,managePattern,managePatternid ,jde,datetime,
				 branchid,branchname,shouldamount,paidinamount,discountamount,cash,credit,credit2,card,icbccard,weixin,zhifubao,merbervaluenet,
				integralconsum,meberTicket,fraction,give,roundoff,mebervalueadd,free,free2,preferentialdetail,totalfree) 
			VALUES 
				(v_brandid,v_brand_name,v_marketid,v_market_name,v_areaid,v_area_name,1,'自营',v_jde,v_datetime,
				v_branchid,v_branchname,v_shouldamount,v_paidinamount,v_shouldamount-v_paidinamount,v_pa_cash,v_pa_credit,v_da_credit2,v_pa_card,v_pa_icbc_card,
				v_pa_weixin,v_pa_zhifubao,v_pa_paidinamount - v_da_mebervalueadd,v_da_integralconsum,v_da_meberTicket,v_da_fraction,v_da_give,v_da_roundoff,
				v_da_mebervalueadd,v_da_free2,v_da_free,v_value_detail,v_da_totalfree);
				
		END LOOP label_fetch_loop;
		
		
		CLOSE cur_branchid;
  END;
  
  SELECT COUNT(1) INTO v_num FROM t_temp_res ;
  
  IF v_num <> 0 THEN 
  
  SELECT 
	  ifnull(sum(shouldamount),0),ifnull(sum(paidinamount),0),ifnull(sum(discountamount),0),ifnull(sum(cash),0),ifnull(sum(credit),0),ifnull(sum(credit2),0),
	  ifnull(sum(card),0),ifnull(sum(icbccard),0),ifnull(sum(weixin),0),ifnull(sum(zhifubao),0),ifnull(sum(merbervaluenet),0),ifnull(sum(integralconsum),0),
	  ifnull(sum(meberTicket),0),ifnull(sum(fraction),0),ifnull(sum(give),0),ifnull(sum(roundoff),0),ifnull(sum(mebervalueadd),0),ifnull(sum(free),0),
	  ifnull(sum(totalfree),0)
  INTO 
	v_shouldamount,v_paidinamount,v_da_discount,v_pa_cash,v_pa_credit,v_da_credit2,v_pa_card,v_pa_icbc_card,
	v_pa_weixin,v_pa_zhifubao,v_da_mebervalueaddnet,v_da_integralconsum,v_da_meberTicket,v_da_fraction,v_da_give,v_da_roundoff,
	v_da_mebervalueadd,v_da_free2,v_da_totalfree
  FROM 
      t_temp_res;
	  
	SELECT REPLACE(v_value_total,'youmian,会员优免,0.00',CONCAT('youmian,会员优免,',v_da_free2)) INTO v_value_total;
	
    INSERT INTO t_temp_res
	  (branchid,jde,branchname,shouldamount,paidinamount,discountamount,cash,credit,credit2,card,icbccard,weixin,zhifubao,merbervaluenet,
	  integralconsum,meberTicket,fraction,give,roundoff,mebervalueadd,free,preferentialdetail,totalfree)
	VALUES
	  (NULL,'最终合计',NULL,v_shouldamount,v_paidinamount,v_da_discount,v_pa_cash,v_pa_credit,v_da_credit2,v_pa_card,v_pa_icbc_card,
	   v_pa_weixin,v_pa_zhifubao,v_da_mebervalueaddnet,v_da_integralconsum,v_da_meberTicket,v_da_fraction,v_da_give,v_da_roundoff,
	   v_da_mebervalueadd,v_da_free2,v_value_total,v_da_totalfree
	  );
	  
  SELECT * FROM t_temp_res ORDER BY id asc;
  END IF;
  
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_yyhzbxq` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_yyhzbxq` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_yyhzbxq`(IN `pi_branchid` int(11)
																,IN  pi_sb	SMALLINT
																,IN `pi_ksrq` datetime
																,IN `pi_jsrq` datetime
																,IN `pi_brandid` int(11)
																,IN `pi_marketid` int(11)
																,IN `pi_areaid` int(11)
																,OUT `po_errormsg` varchar(100))
    SQL SECURITY INVOKER
    COMMENT '营业活动汇总表详情'
label_main:
BEGIN
  DECLARE v_date_start    DATETIME;
  DECLARE v_date_end      DATETIME;
  DECLARE v_cur_orderid  VARCHAR(50);
  DECLARE v_fetch_done BOOL DEFAULT FALSE; 
  DECLARE v_branchid VARCHAR(50); 
  DECLARE v_branchname VARCHAR(50); 
  DECLARE v_shouldamount VARCHAR(50); 
  DECLARE v_paidinamount  VARCHAR(128); 
  DECLARE v_brandid  VARCHAR(128); 
  DECLARE v_marketid  VARCHAR(128); 
  DECLARE v_areaid  VARCHAR(128); 
  DECLARE v_jde  VARCHAR(128); 
  DECLARE v_brand_name  VARCHAR(128); 
  DECLARE v_market_name  VARCHAR(128); 
  DECLARE v_area_name  VARCHAR(128); 
  DECLARE v_datetime  VARCHAR(128); 

  
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
  DECLARE v_da_credit2      	  DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_da_free2      		  DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_da_totalfree      	  DOUBLE(13, 2) DEFAULT 0; 
  DECLARE v_da_mebervalueaddnet   DOUBLE(13, 2) DEFAULT 0; 
  
  
  DECLARE v_pid         VARCHAR(50); 
  DECLARE v_pname         VARCHAR(128); 
  DECLARE v_ptype         CHAR(4); 
  DECLARE v_pamount       DOUBLE(13, 2); 
  DECLARE v_value_detail  VARCHAR(10000); 
  DECLARE v_value_total  VARCHAR(10000); 

  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; 
  END;

  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
	SELECT NULL;
	GET DIAGNOSTICS CONDITION 1 po_errormsg = MESSAGE_TEXT;
  END;
	
  IF pi_branchid IS NULL THEN
  SELECT NULL;
  SET po_errormsg = '分店ID输入不能为空';
  LEAVE label_main;
  END IF;
  
  IF pi_brandid > -1 THEN
  
	SELECT id,name INTO v_brandid,v_brand_name  FROM t_c_brand WHERE id = pi_brandid;
  ELSE
	SELECT id,name INTO v_brandid,v_brand_name FROM t_c_brand LIMIT 1;
  END IF;  
  
  IF pi_marketid > -1 THEN
  
	SELECT id,name INTO v_marketid,v_market_name  FROM t_c_market WHERE id = pi_marketid;
  ELSE
	SELECT id,name INTO v_marketid,v_market_name FROM t_c_market LIMIT 1;
  END IF;  
  
  IF pi_areaid > -1 THEN
  
	SELECT id,name INTO v_areaid,v_area_name  FROM t_c_area WHERE id = pi_areaid;
  ELSE
	SELECT id,name INTO v_areaid,v_area_name FROM t_c_area LIMIT 1;
  END IF; 
	
  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;
  SET SESSION group_concat_max_len=102400;

  SET v_date_start = str_to_date(concat(date_format(pi_ksrq,'%Y-%m-%d'),'00:00:00'),'%Y-%m-%d %H:%i:%s');
  SET v_date_end = str_to_date(concat(date_format(pi_jsrq,'%Y-%m-%d'),'23:59:59'),'%Y-%m-%d %H:%i:%s');
  SET v_branchid = pi_branchid;
	
  DROP TEMPORARY TABLE IF EXISTS t_temp_branch;
  CREATE TEMPORARY TABLE t_temp_branch
  (
	branchid VARCHAR(50),
    branchname VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  INSERT INTO t_temp_branch
  SELECT branchid
	   , branchname
  FROM
	t_branch;
	
  SELECT branchname INTO v_branchname FROM
  t_temp_branch WHERE branchid = v_branchid;
  
  IF v_branchname IS NULL THEN 
  SELECT NULL;
  SET po_errormsg = '分店id不存在';
  LEAVE label_main;
  END IF;
	
  DROP TEMPORARY TABLE IF EXISTS t_temp_order;
  CREATE TEMPORARY TABLE t_temp_order
  (
	orderid VARCHAR(50),
    begintime DATETIME,
    endtime DATETIME
	
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);
  
  IF pi_sb > -1 THEN
    INSERT INTO t_temp_order
    SELECT orderid
         , begintime
         , endtime
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
	  t_order.branchid = v_branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND shiftid = pi_sb
      AND orderstatus = 3;
  ELSE
    INSERT INTO t_temp_order
    SELECT orderid
         , begintime
         , endtime
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
	  t_order.branchid = v_branchid
      AND begintime BETWEEN v_date_start AND v_date_end 
      AND orderstatus = 3;
  END IF;
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
  CREATE TEMPORARY TABLE t_temp_order_detail
  (
    orderid VARCHAR(50),
    dishnum DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
    pricetype CHAR(1),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50),
    dishtype TINYINT
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_order_detail_orderid ON t_temp_order_detail (orderid);
  
  INSERT INTO t_temp_order_detail
  SELECT b.orderid
       , b.dishnum
       , b.orignalprice
       , b.pricetype
       , b.childdishtype
       , b.primarykey
       , b.superkey
       , b.dishtype
  FROM
    t_temp_order a, t_order_detail b
  WHERE
    a.orderid = b.orderid
    AND orignalprice > 0;  
	
   
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   

  
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;
	
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    couponid VARCHAR(50),
    isviporder TINYINT, 
    coupondetailid VARCHAR(50),
	couponNum INT,
	bankcardno VARCHAR(50),
    membercardno VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_settlement_detail_orderid ON t_temp_settlement_detail (orderid);
  
  INSERT INTO t_temp_settlement_detail
  SELECT b.orderid
       , b.payway
       , b.payamount
       , b.couponid
       , CASE
         WHEN b.payway = 8 AND char_length(b.membercardno) > 1 THEN
           1
         ELSE
           0
         END
		, b.coupondetailid
		, b.couponNum
		, b.bankcardno
        , b.membercardno
  FROM
    t_temp_order a, t_settlement_detail b
  WHERE
    a.orderid = b.orderid;
	
  
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
    netvalue DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_order_member_orderid ON t_temp_order_member (orderid);

  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
       , b.netvalue
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid;
	
  
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
           ifnull(ifnull(a.free_reason, a.company_name), b.name)
         END) AS name
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
    a.preferential = b.id
  ORDER BY b.code; 
  
    
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail_sub;
  CREATE TEMPORARY TABLE t_temp_order_detail_sub
  (
    orderid VARCHAR(50),
    dishnum DOUBLE(13, 2),
    orignalprice DOUBLE(13, 2),
    pricetype CHAR(1),
    childdishtype TINYINT,
    primarykey VARCHAR(50),
    superkey VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_order_detail_sub_orderid ON t_temp_order_detail_sub (orderid);

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new;
  CREATE TEMPORARY TABLE t_temp_settlement_new
  (
    orderid VARCHAR(50),
    payway INT, 
    payamount DOUBLE(13, 2), 
	pid VARCHAR(50),
    pname VARCHAR(128), 
    ptype CHAR(4) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  CREATE INDEX ix_t_temp_settlement_new ON t_temp_settlement_new(orderid);

  
  INSERT INTO t_temp_settlement_new
  SELECT a.orderid
       , a.payway
       , a.payamount
	   , b.pid
       , b.pname
       , b.ptype
  FROM
    t_temp_settlement_detail a, t_temp_preferential b
  WHERE
    a.payway = 6
    AND a.coupondetailid = b.pid;

  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new_sub;
  CREATE TEMPORARY TABLE t_temp_settlement_new_sub
  (
    orderid VARCHAR(50),
    payway INT, 
    payamount DOUBLE(13, 2), 
    pid VARCHAR(50), 
    pname VARCHAR(128), 
    ptype CHAR(4) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  CREATE INDEX ix_t_temp_settlement_new_sub ON t_temp_settlement_new_sub(orderid);
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail_sub;
  CREATE TEMPORARY TABLE t_temp_settlement_detail_sub
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    couponid VARCHAR(50),
    isviporder TINYINT, 
    coupondetailid VARCHAR(50),
	couponNum INT,
	bankcardno VARCHAR(50),
    membercardno VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_settlement_detail_sub_orderid ON t_temp_settlement_detail_sub (orderid);
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
	datetime date,
	brandid VARCHAR(50),
	brandname VARCHAR(50),
	marketid VARCHAR(50),
	market  VARCHAR(50),
	areaid VARCHAR(50),
	area  VARCHAR(50),
	managePattern  VARCHAR(50),
	managePatternid VARCHAR(50),
	jde VARCHAR(50),
	branchid VARCHAR(50),  
	branchname VARCHAR(50), 
	orderid VARCHAR(50), 
    shouldamount decimal(13, 2), 
    paidinamount decimal(13, 2), 
    discountamount decimal(13, 2), 
    cash decimal(13, 2) DEFAULT 0.00, 
    credit decimal(13, 2) DEFAULT 0.00, 
    credit2 decimal(13, 2) DEFAULT 0.00, 
    card decimal(13, 2) DEFAULT 0.00, 
    icbccard decimal(13, 2) DEFAULT 0.00, 
    weixin decimal(13, 2) DEFAULT 0.00, 
    zhifubao decimal(13, 2) DEFAULT 0.00, 
    merbervaluenet decimal(13, 2) DEFAULT 0.00, 
    integralconsum decimal(13, 2) DEFAULT 0.00, 
    meberTicket decimal(13, 2) DEFAULT 0.00, 
    fraction decimal(13, 2) DEFAULT 0.00, 
    give decimal(13, 2) DEFAULT 0.00, 
    roundoff decimal(13, 2) DEFAULT 0.00, 
    mebervalueadd decimal(13, 2) DEFAULT 0.00, 
    free decimal(13, 2) DEFAULT 0.00, 
	preferentialdetail VARCHAR(10000), 
	totalfree VARCHAR(10000), 
	PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail;
  CREATE TEMPORARY TABLE t_temp_res_detail
  (
	pid VARCHAR(50),
    pname VARCHAR(128), 
    ptype CHAR(4), 
    payamount DOUBLE(13, 2) 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  	
	BEGIN
		DECLARE cur_total CURSOR FOR SELECT DISTINCT pname,pid
										   FROM
											 t_temp_settlement_new;
			
		SET v_fetch_done = FALSE;
		OPEN cur_total;
		TRUNCATE t_temp_res_detail;
			 
		lable_fetch_total_loop:
		LOOP
			FETCH cur_total INTO v_pname,v_pid;
			
			IF v_fetch_done THEN
			SET v_fetch_done = FALSE;
			LEAVE lable_fetch_total_loop;
			END IF;

			SELECT max(ptype)
				, ifnull(sum(payamount), 0)
			INTO
				v_ptype, v_pamount
			FROM
				t_temp_settlement_new
			WHERE
				pname = v_pname
			AND pid = v_pid;
				  
			INSERT INTO t_temp_res_detail (pid,pname,ptype,payamount) VALUES( v_pid, v_pname , v_ptype , v_pamount );

		END LOOP lable_fetch_total_loop;
		
		
		INSERT INTO t_temp_res_detail (pid,pname,ptype,payamount) VALUES ('youmian', '会员优免' , 'ym' , 0);
		
		SELECT group_concat(t.value_detail SEPARATOR '|')
			INTO
			  v_value_total
			FROM
			 (
			  SELECT concat(pid, ',', pname, ',', payamount) AS value_detail
			  FROM
				t_temp_res_detail) t;
		
		CLOSE cur_total;
	END;
	
 
  BEGIN
	DECLARE cur_orderid CURSOR FOR SELECT orderid,begintime
										FROM t_temp_order USE INDEX (ix_t_temp_order_orderid);
	
	SET v_fetch_done = FALSE;
	
	OPEN cur_orderid;
	
	label_fetch_loop:
		LOOP
			FETCH cur_orderid into v_cur_orderid,v_datetime;
			
			IF v_fetch_done THEN
				leave label_fetch_loop;
			END IF;	
			
			
			
			TRUNCATE t_temp_settlement_detail_sub;
			
			  INSERT INTO t_temp_settlement_detail_sub
			  SELECT a.orderid
				   , a.payway
				   , a.payamount
				   , a.couponid
				   , a.payway
				   , a.coupondetailid
				   , a.couponNum
				   , a.bankcardno
				   , a.membercardno
			  FROM
				t_temp_settlement_detail a
			  WHERE
				a.orderid = v_cur_orderid;
				
			
			TRUNCATE t_temp_order_detail_sub;
			
			INSERT INTO t_temp_order_detail_sub
			SELECT a.orderid
			     , a.dishnum
			     , a.orignalprice
			     , a.pricetype
			     , a.childdishtype
			     , a.primarykey
			     , a.superkey
		    FROM
			  t_temp_order_detail a
		    WHERE
				a.orderid = v_cur_orderid
			AND a.orignalprice > 0;
			
			
			SELECT ifnull(sum(orignalprice * dishnum), 0)
			INTO
			  v_shouldamount
			FROM
			  t_temp_order_detail_sub;

			
		    SELECT ifnull(sum(
				 CASE
				 WHEN payway = 0 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
				,ifnull(sum(
				 CASE
				 WHEN payway = 6 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
				,ifnull(sum(
				 CASE
				 WHEN payway = 13 THEN 
				   payamount
				 ELSE
				   0
				 END), 0) 
			   ,ifnull(sum(
				 CASE
				 WHEN payway = 1 and membercardno='0' and payamount<>0 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 1 and membercardno='1' and payamount<>0 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 5 AND payamount > 0 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 8 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 11 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 12 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 7 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , 0 - ifnull(sum(
				 CASE
				 WHEN payway = 20 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 17 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 18 THEN 
				   payamount
				 ELSE
				   0
				 END), 0)
		    INTO
			  v_pa_cash,v_da_free,v_da_credit2, v_pa_card, v_pa_icbc_card, v_pa_credit, v_pa_paidinamount, v_da_integralconsum, v_da_meberTicket, v_da_fraction, v_da_roundoff, v_pa_weixin, v_pa_zhifubao
		    FROM
			  t_temp_settlement_detail_sub;
					
			
			SELECT 
				ifnull(sum(
						CASE pricetype
						WHEN '1' THEN
						orignalprice * dishnum
						ELSE
							0
						END), 0) 
			INTO
				v_da_give 
			FROM
				t_temp_order_detail_sub;
			
			
			SELECT ifnull(sum(Inflated), 0)
			INTO
			  v_da_mebervalueadd
			FROM
			  t_temp_order_member a
			WHERE
			  a.orderid = v_cur_orderid;
				
			
			SET v_paidinamount = v_pa_cash + v_pa_credit + v_pa_card + v_pa_paidinamount - v_da_mebervalueadd + v_pa_icbc_card + v_pa_weixin + v_pa_zhifubao+v_da_credit2;
				
			
			TRUNCATE t_temp_settlement_new_sub;

			
			INSERT INTO t_temp_settlement_new_sub
			SELECT a.orderid ,a.payway,a.payamount,a.pid,a.pname,a.ptype
			FROM
			  t_temp_settlement_new a
			WHERE
			  a.orderid = v_cur_orderid
			AND
			 a.payway = '6';

			BEGIN
			
			DECLARE cur_pname CURSOR FOR SELECT DISTINCT pname
										   FROM
											 t_temp_settlement_new_sub;
							
			SET v_fetch_done = FALSE;
			OPEN cur_pname;
			  
			UPDATE t_temp_res_detail SET payamount = 0;
			 
			lable_fetch_pname_loop:
			  LOOP
				FETCH cur_pname INTO v_pname;
				
				IF v_fetch_done THEN
				  SET v_fetch_done = FALSE;
				  LEAVE lable_fetch_pname_loop;
				END IF;

				SELECT pid,max(ptype)
					 , ifnull(sum(payamount), 0)
				INTO
				  v_pid,v_ptype, v_pamount
				FROM
				  t_temp_settlement_new_sub
				WHERE
				  pname = v_pname;
				  
				UPDATE t_temp_res_detail SET payamount = v_pamount  WHERE pid = v_pid;

			  END LOOP lable_fetch_pname_loop;
			  
			  
			  CLOSE cur_pname;
			  
			 END;
			 
			SET v_da_totalfree = 0;
			SELECT ifnull(sum(payamount),0) INTO
			     v_da_totalfree
			FROM  t_temp_res_detail;
			
		    
		    SET v_da_free2 = v_shouldamount - v_paidinamount - ifnull(v_da_roundoff,0)- ifnull(v_da_integralconsum,0)- ifnull(v_da_meberTicket,0)
							- ifnull(v_da_totalfree,0)- ifnull(v_da_fraction,0)- ifnull(v_da_give,0)- ifnull(v_da_mebervalueadd,0);		

			UPDATE t_temp_res_detail SET payamount = v_da_free2  WHERE pid = 'youmian';
			 
			SELECT group_concat(t.value_detail SEPARATOR '|')
			INTO
			  v_value_detail
			FROM
			 (
			  SELECT concat(pid, ',', pname, ',', payamount) AS value_detail
			  FROM
				t_temp_res_detail) t;
				
			SELECT ifnull(branch_id,0)
			INTO v_jde
			FROM t_branch_code a WHERE a.branch_id_code = v_branchid;
				
			
			INSERT INTO t_temp_res 
				(brandid,brandname,marketid,market,areaid,area,managePattern,managePatternid ,jde,datetime,
				branchid,branchname,orderid,shouldamount,paidinamount,discountamount,cash,credit,credit2,card,icbccard,weixin,zhifubao,merbervaluenet,
				integralconsum,meberTicket,fraction,give,roundoff,mebervalueadd,free,preferentialdetail,totalfree) 
			VALUES 
				(v_brandid,v_brand_name,v_marketid,v_market_name,v_areaid,v_area_name,1,'自营',v_jde,v_datetime,
				v_branchid,v_branchname,v_cur_orderid,v_shouldamount,v_paidinamount,v_shouldamount-v_paidinamount,v_pa_cash,v_pa_credit,v_da_credit2,v_pa_card,v_pa_icbc_card,
				v_pa_weixin,v_pa_zhifubao,v_pa_paidinamount - v_da_mebervalueadd,v_da_integralconsum,v_da_meberTicket,v_da_fraction,v_da_give,v_da_roundoff,
				v_da_mebervalueadd,v_da_free2,v_value_detail,v_da_totalfree);
				
		END LOOP label_fetch_loop;
		
		
		CLOSE cur_orderid;
  END;
  
    SELECT 
	  ifnull(sum(shouldamount),0),ifnull(sum(paidinamount),0),ifnull(sum(discountamount),0),ifnull(sum(cash),0),ifnull(sum(credit),0),ifnull(sum(credit2),0),
	  ifnull(sum(card),0),ifnull(sum(icbccard),0),ifnull(sum(weixin),0),ifnull(sum(zhifubao),0),ifnull(sum(merbervaluenet),0),ifnull(sum(integralconsum),0),
	  ifnull(sum(meberTicket),0),ifnull(sum(fraction),0),ifnull(sum(give),0),ifnull(sum(roundoff),0),ifnull(sum(mebervalueadd),0),ifnull(sum(free),0),
	  ifnull(sum(totalfree),0)
	INTO 
	  v_shouldamount,v_paidinamount,v_da_discount,v_pa_cash,v_pa_credit,v_da_credit2,v_pa_card,v_pa_icbc_card,
				v_pa_weixin,v_pa_zhifubao,v_da_mebervalueaddnet,v_da_integralconsum,v_da_meberTicket,v_da_fraction,v_da_give,v_da_roundoff,
				v_da_mebervalueadd,v_da_free2,v_da_totalfree
	FROM 
      t_temp_res;
	
	SELECT REPLACE(v_value_total,'youmian,会员优免,0.00',CONCAT('youmian,会员优免,',v_da_free2)) INTO v_value_total;
	
    INSERT INTO t_temp_res
	  (branchid,orderid,branchname,shouldamount,paidinamount,discountamount,cash,credit,credit2,card,icbccard,weixin,zhifubao,merbervaluenet,
	  integralconsum,meberTicket,fraction,give,roundoff,mebervalueadd,free,preferentialdetail,totalfree)
	VALUES
	  (NULL,'最终合计',NULL,v_shouldamount,v_paidinamount,v_da_discount,v_pa_cash,v_pa_credit,v_da_credit2,v_pa_card,v_pa_icbc_card,
	   v_pa_weixin,v_pa_zhifubao,v_da_mebervalueaddnet,v_da_integralconsum,v_da_meberTicket,v_da_fraction,v_da_give,v_da_roundoff,
	   v_da_mebervalueadd,v_da_free2,v_value_total,v_da_totalfree );
  
  SELECT * FROM t_temp_res ORDER BY id asc;
  
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_yysjmxb` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_yysjmxb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_report_yysjmxb`(IN  pi_branchid INT(11), 
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
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   

  
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

  
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
         WHEN b.payway = 8 AND char_length(b.membercardno) > 1 THEN
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


  
  
  SELECT sum(
         CASE ordertype
         WHEN 0 THEN
           1
         ELSE
           0
         END) 
         , ifnull(sum(
         CASE ordertype
         WHEN 0 THEN
           mannum + womanNum + childNum
         ELSE
           0
         END), 0) 
         , sum(
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


  
  SELECT ifnull(sum(
         CASE b.ordertype
         WHEN 0 THEN
           b.orignalprice * b.dishnum
         ELSE
           0
         END), 0) 
         , ifnull(sum(
         CASE b.ordertype
         WHEN 0 THEN
           0
         ELSE
           b.orignalprice * b.dishnum
         END), 0) 
         , ifnull(sum(
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

  
  SELECT ifnull(sum(Inflated), 0)
       , ifnull(sum(
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

  
  SELECT ifnull(sum(payamount), 0)
  INTO
    v_oa_paidinamount
  FROM
    t_temp_settlement_detail
  WHERE
    payway IN (0, 1, 5, 8, 13, 17, 18,30)  
    AND ordertype > 0;

  
  SET v_oa_paidinamount = v_oa_paidinamount - v_oa_mebervalueadd;

  
  SELECT ifnull(sum(
         CASE
         WHEN payway = 0 THEN 
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway = 1 and membercardno<>'1' and payamount<>0 THEN 
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway = 1 and membercardno='1' and payamount<>0 THEN 
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway in(5,13) AND payamount > 0 THEN 
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway = 8 THEN 
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway = 11 THEN 
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway = 12 THEN 
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway = 7 THEN 
           payamount
         ELSE
           0
         END), 0)
       , 0 - ifnull(sum(
         CASE
         WHEN payway = 20 THEN 
           payamount
         ELSE
           0
         END), 0)
       , sum(isviporder) 
       , ifnull(sum(
         CASE
         WHEN payway = 17 THEN 
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
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

  
  SELECT ifnull(sum(a.payamount), 0)
  INTO
    v_da_discount
  FROM
    t_temp_settlement_detail a, t_p_preferential_activity b
  WHERE
    a.couponid = b.id
    AND b.type = '02';

  
  

  
  SET v_da_free = v_sa_shouldamount + v_oa_shouldamount - v_paidinamount - v_da_roundoff - v_da_integralconsum - v_da_meberTicket - v_da_discount - v_da_fraction - v_da_give - v_da_mebervalueadd;

  
  SELECT count(1)
       , sum(a.personNum)
  INTO
    v_other_tablecount, v_other_tableperson
  FROM
    t_table a, t_tablearea b
  WHERE
    a.areaid = b.areaid
    AND b.branchid = pi_branchid
    AND a.status IN (0, 1);

  
	SELECT IFNULL(sum(too.custnum),0),    
    IFNULL(count(too.orderid),0)
  INTO v_closed_person_nums,v_closed_bill_nums
	FROM
	  t_temp_order too
  WHERE too.orderstatus = 3;

  
  SELECT IFNULL(sum(tod.orignalprice * tod.dishnum),0) 
  INTO v_closed_bill_shouldamount
  FROM t_temp_order_detail tod
  WHERE tod.orderid in (select orderid from t_temp_order where orderstatus = 3);

  
  SELECT IFNULL(sum(too.custnum),0),
    IFNULL(count(too.orderid),0)
  INTO v_no_person_nums,v_no_bill_nums
  FROM
	  t_temp_order too
  WHERE too.orderstatus = 0;

  
  SELECT IFNULL(sum(tod.orignalprice * tod.dishnum),0) 
  INTO v_no_bill_shouldamount
  FROM t_temp_order_detail tod
  WHERE tod.orderid in (select orderid from t_temp_order where orderstatus = 0);

  
IF pi_sb > -1 THEN
  SELECT count(orderid)
  INTO v_zaitaishu
  FROM t_order USE INDEX (IX_t_order_begintime)
  WHERE branchid = pi_branchid AND orderstatus <> 2 AND shiftid = pi_sb
  AND ((begintime BETWEEN v_date_start and v_date_end) OR (endtime BETWEEN v_date_start and v_date_end)); 
ELSE
  SELECT count(orderid)
  INTO v_zaitaishu
  FROM t_order USE INDEX (IX_t_order_begintime)
  WHERE branchid = pi_branchid AND orderstatus <> 2
  AND ((begintime BETWEEN v_date_start and v_date_end) OR (endtime BETWEEN v_date_start and v_date_end)); 
END IF;

  
IF pi_sb > -1 THEN
  SELECT count(orderid)
  INTO v_kaitaishu
  FROM t_temp_order
  WHERE orderstatus <> 2 AND shiftid = pi_sb; 
ELSE
  SELECT count(orderid)
  INTO v_kaitaishu
  FROM t_temp_order
  WHERE orderstatus <> 2;
END IF;

  
  SET v_bill_nums = v_closed_bill_nums + v_no_bill_nums;
  
  
  SET v_bill_shouldamount = v_closed_bill_shouldamount + v_no_bill_shouldamount;
   
  
  SET v_person_nums = v_closed_person_nums + v_no_person_nums;

  IF v_sa_ordercount > 0 THEN
    
    SET v_sa_attendance = v_sa_settlementnum / (timestampdiff(DAY, v_date_start, v_date_end) + 1) / v_other_tableperson * 100;

    
    SET v_sa_overtaiwan = v_sa_ordercount / (timestampdiff(DAY, v_date_start, v_date_end) + 1) / v_other_tablecount * 100;

    
    SET v_sa_tableconsumption = (v_paidinamount - v_oa_paidinamount) / v_sa_ordercount;

    
    SET v_sa_shouldaverage = v_sa_shouldamount / v_sa_settlementnum;

    
    SET v_sa_paidinaverage = (v_paidinamount - v_oa_paidinamount) / v_sa_settlementnum; 

    
    SELECT sum(timestampdiff(SECOND, begintime, endtime)) / v_sa_ordercount / 60
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







END */$$
DELIMITER ;

/* Procedure structure for procedure `p_setOrderDish` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_setOrderDish` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_setOrderDish`(IN i_orderid varchar(50), INOUT o_code varchar(50), INOUT o_msg varchar(300))
    SQL SECURITY INVOKER
BEGIN


  DECLARE v_printobjid int;

  DECLARE v_printobj_count int;


  SELECT

    COUNT(1) INTO v_printobj_count

  FROM t_printobj tp

  WHERE tp.orderno = i_orderid;


  IF v_printobj_count = 0 THEN


    SELECT

      tableseqno('printobjid') INTO v_printobjid;


  ELSE


    SELECT DISTINCT

      tp.id INTO v_printobjid

    FROM t_printobj tp

    WHERE tp.orderno = i_orderid;

  END IF;

  CALL newspicyway.p_orderdish(i_orderid, v_printobjid, o_code, o_msg);


END */$$
DELIMITER ;

/* Procedure structure for procedure `p_setordermember` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_setordermember` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_setordermember`(IN v_orderid varchar(50), IN v_pricetype int)
    SQL SECURITY INVOKER
BEGIN
  DECLARE done int default 0;
  DECLARE v_dishid varchar(50);
  DECLARE v_orderdetailid varchar(50);
  DECLARE v_dishunit varchar(100);
  DECLARE v_template_vipprice decimal(10, 2);
  declare v_menuid varchar(100);
  DECLARE cur_orderlist CURSOR FOR
  select a.dishid,a.orderdetailid,a.dishunit from t_order_detail a inner join t_dish b on(a.dishid=b.dishid) where a.orderprice>0 and a.orderid=v_orderid;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
  select menuid into v_menuid from v_currmenu;
  OPEN cur_orderlist;
  REPEAT
    FETCH cur_orderlist INTO v_dishid, v_orderdetailid,v_dishunit;
    IF done <> 1 THEN
      if(v_pricetype=0)then
        select vipprice INTO v_template_vipprice from t_template_dishunit where (menuid =v_menuid or(dishid='DISHES_98')) and dishid=v_dishid and (unit=v_dishunit or (dishid='DISHES_98')) LIMIT 1;
       else
        select price INTO v_template_vipprice from t_template_dishunit where (menuid =v_menuid or(dishid='DISHES_98')) and dishid=v_dishid and (unit=v_dishunit or (dishid='DISHES_98')) LIMIT 1;
      END IF;
      if(v_template_vipprice>0)THEN
        update t_order_detail set orderprice=v_template_vipprice where orderid=v_orderid and orderdetailid=v_orderdetailid;
      end if;
   END IF;
  UNTIL done = 1
  END REPEAT;
  CLOSE cur_orderlist;
  CALL newspicyway.p_caletableamount(v_orderid);
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_start_cloud_menu` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_start_cloud_menu` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_start_cloud_menu`()
    SQL SECURITY INVOKER
BEGIN

  UPDATE t_menu tm SET tm.status='1' WHERE tm.status='2' AND tm.effecttime<=NOW();
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_switch` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_switch` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_switch`(IN i_targettableno varchar(50), IN i_orignaltableno varchar(50), INOUT o_result varchar(50))
    SQL SECURITY INVOKER
BEGIN
  DECLARE v_count int;
  DECLARE v_status varchar(2);
  DECLARE v_orderid varchar(100);
  DECLARE v_t_orderid varchar(100);
  DECLARE v_table_orginal_id varchar(100);

  DECLARE v_tableid varchar(100);
  DECLARE v_orderstatus varchar(2);
  DECLARE v_t_orderstatus varchar(2);

  DECLARE v_tableno  varchar(300);

  SELECT
    COUNT(1) INTO v_count
  FROM t_table
  WHERE tableno = i_orignaltableno;
  IF v_count = 1 THEN
    SELECT
      COUNT(1) INTO v_count
    FROM t_table
    WHERE tableno = i_targettableno
    AND status = '0';
    IF v_count = 1 THEN
      SELECT
        status,
        orderid,
        tableid INTO v_status, v_orderid, v_table_orginal_id
      FROM t_table
      WHERE tableno = i_orignaltableno;

      SELECT
        tableid,
        orderid INTO v_tableid, v_t_orderid
      FROM t_table
      WHERE tableno = i_targettableno;

      IF v_status = 0 THEN
        SET v_status = 1;
      END IF;

     


      
      UPDATE t_table
      SET orderid = NULL,
          status = 0
      WHERE orderid = v_orderid;



      
      UPDATE t_table
      SET orderid = v_orderid,
          status = v_status
      WHERE tableNo = i_targettableno;

      UPDATE t_order
      SET currenttableid = v_tableid,
          relateorderid = NULL,
          payway = NULL
      WHERE orderid = v_orderid;


     SELECT
        CONCAT('桌号:', ' ', IFNULL(tableno, '')) INTO v_tableno
      FROM t_tablearea ta,
           t_table tb
      WHERE tb.areaid = ta.areaid
      AND tb.tableid = v_tableid;

        
     UPDATE t_printobj tp
      SET tp.tableno = v_tableno,
          tp.tableid = v_tableid
      where tp.orderno =   v_orderid ;



    
      SET o_result = '0';
    ELSE
      SET o_result = '1';
    END IF;
  ELSE
    SET o_result = '1';
  END IF;


END */$$
DELIMITER ;

/* Procedure structure for procedure `p_syndata` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_syndata` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_syndata`(IN i_branch_id varchar(50), INOUT i_result varchar(500))
    SQL SECURITY INVOKER
BEGIN
  
  DECLARE done int DEFAULT 0;
  DECLARE v_id varchar(50);
  DECLARE v_branchid varchar(50);
  DECLARE v_sqltext longtext;
  DECLARE v_inserttime datetime;
  DECLARE v_status int;
  DECLARE v_generatetime datetime;
  DECLARE v_insert_str varchar(50);
  DECLARE v_orderSeqno integer;
  DECLARE v_error boolean DEFAULT FALSE;
  
  DECLARE cur_order CURSOR FOR
  SELECT
    id,
    branchid,
    sqltext,
    inserttime,
    status,
    generattime,
    orderSeqno
  FROM t_syn_sql t
  WHERE t.status = '0' 
  AND t.sqltext IS NOT NULL AND t.sqltext <> ''
  ORDER BY orderseqno, generattime;

  DROP TEMPORARY TABLE IF EXISTS t_temp_syn_sqlid;
  CREATE TEMPORARY TABLE t_temp_syn_sqlid
    (
      id VARCHAR(50) NOT NULL
    ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
	
	DROP TEMPORARY TABLE IF EXISTS t_temp_syn_delid;
  CREATE TEMPORARY TABLE t_temp_syn_delid
    (
      id VARCHAR(50) NOT NULL
    ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
	
  BEGIN
  
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION  SET v_error = TRUE;

    OPEN cur_order;
  
    REPEAT
  
      FETCH cur_order INTO v_id, v_branchid, v_sqltext, v_inserttime, v_status, v_generatetime, v_orderSeqno;

      IF done <> 1 AND v_error = FALSE  THEN
        SET v_insert_str = UPPER(SUBSTRING(TRIM(v_sqltext), 1, 6));
        IF v_insert_str = 'INSERT' THEN
          SET v_sqltext = CONCAT('INSERT IGNORE ', SUBSTRING(TRIM(v_sqltext), 7));
        END IF;
        SET @sqlstr = v_sqltext;
        PREPARE v_sql FROM @sqlstr;
        EXECUTE v_sql;
        DEALLOCATE PREPARE v_sql;
      END IF;
	  
	  INSERT INTO t_temp_syn_delid (id) VALUES (v_id);
	  
      IF  v_error = TRUE THEN
		SET v_error = FALSE;
	  ELSE
		INSERT INTO t_temp_syn_sqlid (id) VALUES (v_id);
      END  IF;

    UNTIL done = 1

    END REPEAT;

	CLOSE cur_order;
  END;
  
  
  DELETE FROM t_syn_sql_temp  using  t_temp_syn_delid ,t_syn_sql_temp  WHERE  t_temp_syn_delid.id = t_syn_sql_temp.id;

  
  UPDATE t_syn_sql a,t_temp_syn_sqlid b SET a.STATUS = '1' WHERE a.id = b.id; 
	
  SET i_result = '1';

END */$$
DELIMITER ;

/* Procedure structure for procedure `p_update2vipprice` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_update2vipprice` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_update2vipprice`(IN i_orderid varchar(50))
    SQL SECURITY INVOKER
BEGIN
 
 
 DECLARE done int DEFAULT 0;
 DECLARE v_orderprice decimal;
 DECLARE v_orignalprice decimal;
 DECLARE v_vipprice  decimal;
 DECLARE v_dishid  varchar(50);
 DECLARE v_dishunit varchar(50);

 declare v_menuid  varchar(50);
 

DECLARE cur_order CURSOR FOR

  SELECT t.orderprice,t.orignalprice,t.dishid,t.dishunit
    FROM t_order_detail t
  WHERE t.orderid =  i_orderid AND dishid !='DISHES_98' AND orderprice > 0;

 DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
 
OPEN cur_order;

 
REPEAT

     
IF done <> 1 THEN

      
select menuid into v_menuid   from v_currmenu;
 
select DISTINCT vipprice INTO   v_vipprice from t_template_dishunit 
        where menuid  = v_menuid and unit =v_dishunit  and dishid = v_dishid;
 
UPDATE t_order_detail 
        set orderprice = v_vipprice
      WHERE dishid = v_dishid AND dishunit = v_dishunit
      AND  ORDERid = i_orderid;
 
       
   END IF;

  UNTIL done = 1
  END REPEAT;

 
CLOSE cur_order;

 
END */$$
DELIMITER ;

/* Procedure structure for procedure `p_update_primarykey` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_update_primarykey` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p_update_primarykey`(in primarykey varchar(100))
    COMMENT '更新重复的primarykey'
BEGIN

DECLARE done int DEFAULT 0;

DECLARE v_detail_id varchar(100);

DECLARE  temp_uuid varchar(100);

DECLARE cur_detailids CURSOR FOR SELECT orderdetailid from t_order_detail d where  d.primarykey=primarykey;

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
  OPEN cur_detailids;
  REPEAT
    FETCH cur_detailids INTO v_detail_id;
				IF done <> 1 THEN
								SELECT  UUID() into temp_uuid;
								UPDATE t_order_detail set primarykey=temp_uuid where orderdetailid=v_detail_id;
				 END IF;
     UNTIL done = 1
  END REPEAT;

END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
