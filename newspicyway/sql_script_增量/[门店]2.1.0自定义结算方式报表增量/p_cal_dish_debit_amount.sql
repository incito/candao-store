DELIMITER $$


DROP PROCEDURE IF EXISTS `p_cal_dish_debit_amount`$$

CREATE PROCEDURE `p_cal_dish_debit_amount`(IN  i_orderid VARCHAR(255),
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

  
  SELECT IFNULL(SUM(Inflated), 0)
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

  
  SELECT IFNULL(SUM(t.payamount) - v_xz_amount, 0)
  INTO
    @amount1
  FROM
    t_settlement_detail t
  WHERE
    t.orderid = i_orderid
    AND t.payamount > 0
    AND t.payway IN (SELECT itemid FROM v_revenuepayway);

  SELECT IFNULL(SUM(t.debitamount), 0)
  INTO
    @amount2
  FROM
    t_temp_order_detail t
  WHERE
    t.dishtype = 2
    AND t.primarykey = t.superkey;

  SELECT IFNULL(SUM(t.debitamount), 0)
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

END$$

DELIMITER ;