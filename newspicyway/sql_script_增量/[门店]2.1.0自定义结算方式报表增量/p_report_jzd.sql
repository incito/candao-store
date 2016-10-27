DELIMITER $$


DROP PROCEDURE IF EXISTS `p_report_jzd`$$

CREATE PROCEDURE `p_report_jzd`(IN pi_orderid VARCHAR(50),
OUT po_errmsg VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '结账单'
label_main:
BEGIN

  DECLARE v_payway DOUBLE(13, 2) DEFAULT 0;              
  DECLARE v_pamount DOUBLE(13, 2) DEFAULT 0;             
  DECLARE v_totalconsumption DOUBLE(13, 2) DEFAULT 0;    
  DECLARE v_paidamount DOUBLE(13, 2) DEFAULT 0;          
  DECLARE v_giveamount DOUBLE(13, 2) DEFAULT 0;          
  DECLARE v_couponamount DOUBLE(13, 2) DEFAULT 0;        
  DECLARE v_taocan DOUBLE(13, 2) DEFAULT 0;              

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
  WHERE orderid = pi_orderid AND payway IN (SELECT itemid FROM v_revenuepayway);

  SELECT
    IFNULL(SUM(orignalprice), 0.00) INTO v_giveamount
  FROM t_order_detail
  WHERE orderid = pi_orderid AND pricetype = 1;

  
  
  SET v_couponamount = v_totalconsumption - v_paidamount;

  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res (
    payway VARCHAR(50),               
    payamount DOUBLE(13, 2),          
    totalconsumption DOUBLE(13, 2),   
    paidamount DOUBLE(13, 2),         
    giveamount DOUBLE(13, 2),         
    couponamount DOUBLE(13, 2),       
    invoiceamount DOUBLE(13, 2)       
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  
  

  INSERT INTO t_temp_res (payway, payamount, totalconsumption, paidamount, giveamount, couponamount, invoiceamount)
    VALUES (v_payway, v_pamount, v_totalconsumption, v_paidamount, v_giveamount, v_couponamount, 0.00);

  SELECT
    *
  FROM t_temp_res;

END$$

DELIMITER ;