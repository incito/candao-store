DELIMITER $$


DROP PROCEDURE IF EXISTS `p_report_jzd`$$

CREATE PROCEDURE `p_report_jzd`(IN pi_orderid VARCHAR(50),
OUT po_errmsg VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '结账单'
label_main:
BEGIN

  DECLARE v_payway DOUBLE(13, 2) DEFAULT 0;              -- 结算方式（四舍五入/抹零）
  DECLARE v_pamount DOUBLE(13, 2) DEFAULT 0;             -- 结算金额（四舍五入/抹零）
  DECLARE v_totalconsumption DOUBLE(13, 2) DEFAULT 0;    -- 应收金额（不含赠菜）
  DECLARE v_paidamount DOUBLE(13, 2) DEFAULT 0;          -- 实收金额
  DECLARE v_giveamount DOUBLE(13, 2) DEFAULT 0;          -- 赠菜金额
  DECLARE v_couponamount DOUBLE(13, 2) DEFAULT 0;        -- 优惠金额
  DECLARE v_taocan DOUBLE(13, 2) DEFAULT 0;              -- 套餐金额

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
  #服务费
	SELECT IFNULL(SUM(chargeAmount),0)+v_totalconsumption INTO v_totalconsumption
	FROM t_service_charge
	WHERE orderid=pi_orderid AND chargeOn=1;

  #added by caicai
  #SELECT
  #  IFNULL(SUM(dishnum * orignalprice), 0.00) INTO v_taocan
  #FROM t_order_detail
  #WHERE orderid = pi_orderid AND dishtype = 2 AND superkey <> primarykey;

  SELECT
    payway,
    IFNULL(payamount, 0.00) INTO v_payway, v_pamount
  FROM t_settlement_detail
  WHERE orderid = pi_orderid AND payway IN (7, 20);

  SELECT
    IFNULL(SUM(payamount), 0.00) INTO v_paidamount
  FROM t_settlement_detail
  RIGHT JOIN v_revenuepayway vr ON vr.itemid = payway
  WHERE orderid = pi_orderid;

  SELECT
    IFNULL(SUM(orignalprice*dishnum), 0.00) INTO v_giveamount
  FROM t_order_detail
  WHERE orderid = pi_orderid AND pricetype = 1;

  #SET v_couponamount = v_totalconsumption - v_taocan - v_paidamount;
  #added by caicai
  SET v_couponamount = v_totalconsumption - v_paidamount;

  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res (
    payway VARCHAR(50),               -- 结算方式（四舍五入/抹零） 
    payamount DOUBLE(13, 2),          -- 结算金额（四舍五入/抹零)                                                                         
    totalconsumption DOUBLE(13, 2),   -- 应收金额（不含赠菜）                                                                     
    paidamount DOUBLE(13, 2),         -- 实收金额                                                                    
    giveamount DOUBLE(13, 2),         -- 赠菜金额                                                                   
    couponamount DOUBLE(13, 2),       -- 优惠金额
    invoiceamount DOUBLE(13, 2)       -- 发票金额 
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  #INSERT INTO t_temp_res (payway, payamount, totalconsumption, paidamount, giveamount, couponamount, invoiceamount)
  #VALUES (v_payway, v_pamount, v_totalconsumption - v_taocan, v_paidamount, v_giveamount, v_couponamount, 0.00);

  INSERT INTO t_temp_res (payway, payamount, totalconsumption, paidamount, giveamount, couponamount, invoiceamount)
    VALUES (v_payway, v_pamount, v_totalconsumption, v_paidamount, v_giveamount, v_couponamount, 0.00);

  SELECT
    *
  FROM t_temp_res;

END$$

DELIMITER ;