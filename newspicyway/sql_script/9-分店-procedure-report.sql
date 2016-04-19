SET NAMES 'utf8';


DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_yysjmxb$$
CREATE PROCEDURE p_report_yysjmxb(IN  pi_branchid INT(11), -- 分店id
                                  IN  pi_sb       SMALLINT, -- 市别，0:午市；1:晚市；-1:全天
                                  IN  pi_ksrq     DATETIME, -- 开始日期，
                                  IN  pi_jsrq     DATETIME, -- 结束日期
                                  OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '营业数据明细表'
label_main:
BEGIN
  -- 返回字段说明如下(23个字段)：
  -- 应收总额，实收总额，折扣总额，现金，挂账，刷卡，会员储值消费净值，优免，会员积分消费，会员券消费，折扣优惠，抹零，赠送金额，会员储值消费虚增，桌数，桌均消费，结算人数，应收人均，实收人均，上座率，翻台率，平均消费时间，合计
  --
  -- 返回数据举例（只返回一条数据）：
  -- 3895.00 3636.00 259.00 3895 0 0 0 14.50 0 0 0 0.5 0.5 0.5 9 432.78 28 129.86	139.11 0.41	0.70 31 0.00 0.00	0.00 0.00	0.00


  DECLARE v_date_start            DATETIME;
  DECLARE v_date_end              DATETIME;
  DECLARE v_paidinamount          DOUBLE(13, 2) DEFAULT 0; #实收

  #以下为实收明细统计项
  DECLARE v_pa_cash               DOUBLE(13, 2) DEFAULT 0; #实收（现金）
  DECLARE v_pa_credit             DOUBLE(13, 2) DEFAULT 0; #实收（挂账）
  DECLARE v_pa_card               DOUBLE(13, 2) DEFAULT 0; #实收（刷卡——刷他行卡）
  DECLARE v_pa_icbc_card          DOUBLE(13, 2) DEFAULT 0; #实收（刷卡——刷工行卡）
  DECLARE v_pa_paidinamount       DOUBLE(13, 2) DEFAULT 0; #会员储值消费（含虚增）
  DECLARE v_pa_weixin             DOUBLE(13, 2) DEFAULT 0; #实收（微信）
  DECLARE v_pa_zhifubao           DOUBLE(13, 2) DEFAULT 0; #实收（支付宝）

  #以下为折扣明细统计项
  DECLARE v_da_free               DOUBLE(13, 2) DEFAULT 0; #折扣(优免）
  DECLARE v_da_integralconsum     DOUBLE(13, 2) DEFAULT 0; #折扣(会员积分消费）
  DECLARE v_da_meberTicket        DOUBLE(13, 2) DEFAULT 0; #折扣(会员券）
  DECLARE v_da_discount           DOUBLE(13, 2) DEFAULT 0; #折扣(折扣额）
  DECLARE v_da_fraction           DOUBLE(13, 2) DEFAULT 0; #折扣(抹零）
  DECLARE v_da_roundoff           DOUBLE(13, 2) DEFAULT 0; #折扣(抹零）
  DECLARE v_da_give               DOUBLE(13, 2) DEFAULT 0; #折扣(赠送）
  DECLARE v_da_mebervalueadd      DOUBLE(13, 2) DEFAULT 0; #折扣(虚增）

  #以下为营业数据统计项(堂吃)
  DECLARE v_sa_ordercount         INT DEFAULT 0; #营业数据统计(桌数）
  DECLARE v_sa_tableconsumption   DOUBLE(13, 2) DEFAULT 0; #营业数据统计(桌均消费）  
  DECLARE v_sa_settlementnum      INT DEFAULT 0; #营业数据统计(总人数）
  DECLARE v_sa_shouldamount       DOUBLE(13, 2) DEFAULT 0; #营业数据统计(应收）
  DECLARE v_sa_shouldaverage      DOUBLE(13, 2) DEFAULT 0; #营业数据统计(应收人均）
  DECLARE v_sa_paidinaverage      DOUBLE(13, 2) DEFAULT 0; #营业数据统计(实收人均）
  DECLARE v_sa_attendance         DOUBLE(13, 2) DEFAULT 0; #营业数据统计(上座率）
  DECLARE v_sa_overtaiwan         DOUBLE(13, 2) DEFAULT 0; #营业数据统计(翻台率）
  DECLARE v_sa_avgconsumtime      DOUBLE(13, 2) DEFAULT 0; #营业数据统计(平均消费时间）

  #以下为会员数据统计项目
  DECLARE v_ma_total              DOUBLE(13, 2) DEFAULT 0; #会员券消费额

  #以下为外卖数据统计项
  DECLARE v_oa_shouldamount       DOUBLE(13, 2) DEFAULT 0; #外卖（应收）
  DECLARE v_oa_paidinamount       DOUBLE(13, 2) DEFAULT 0; #外卖（实收）
  DECLARE v_oa_mebervalueadd      DOUBLE(13, 2) DEFAULT 0; #外卖（虚增）
  DECLARE v_oa_ordercount         INT(11) DEFAULT 0; #外卖（单数）
  DECLARE v_oa_avgprice           DOUBLE(13, 2) DEFAULT 0; #外卖（单均）

  #其他数据
  DECLARE v_other_tablecount      INT DEFAULT 0; #餐台总的餐台数量
  DECLARE v_other_tableperson     INT DEFAULT 0; #餐台所有餐台的总的座位数
  DECLARE v_other_vipordercount   INT DEFAULT 0; #会员消费笔数
  DECLARE v_other_viporderpercent DOUBLE(13, 2) DEFAULT 0; #会员消费占比

  #账单信息统计
  DECLARE v_closed_bill_nums          INT DEFAULT 0; #已结账单数
  DECLARE v_closed_bill_shouldamount  DOUBLE(13, 2) DEFAULT 0; #已结账单应收
  DECLARE v_closed_person_nums        INT DEFAULT 0; #已结人数
  DECLARE v_no_bill_nums          INT DEFAULT 0; #未结账单数
  DECLARE v_no_bill_shouldamount  DOUBLE(13, 2) DEFAULT 0; #未结账单应收
  DECLARE v_no_person_nums        INT DEFAULT 0; #未结人数
  DECLARE v_bill_nums             INT DEFAULT 0; #全部账单数
  DECLARE v_bill_shouldamount     DOUBLE(13, 2) DEFAULT 0; #全部账单应收
  DECLARE v_person_nums           INT DEFAULT 0; #全部人数
  DECLARE v_zaitaishu             INT DEFAULT 0; #在台数
  DECLARE v_kaitaishu             INT DEFAULT 0; #开台数



  -- 异常处理模块，出现异常返回null
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


  #处理开始结束时间
  SET v_date_start = pi_ksrq; #str_to_date(concat(date_format(pi_ksrq, '%Y-%m-%d'), '00:00:00'), '%Y-%m-%d %H:%i:%s');
  SET v_date_end = pi_jsrq; #str_to_date(concat(date_format(pi_jsrq, '%Y-%m-%d'), '23:59:59'), '%Y-%m-%d %H:%i:%s');

  #创建订单临时内存表
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
      AND begintime BETWEEN v_date_start AND v_date_end -- 需要创建索引IX_t_order_begintime  
      AND shiftid = pi_sb;
      #AND orderstatus = 3;
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
      AND begintime BETWEEN v_date_start AND v_date_end; -- 需要创建索引IX_t_order_begintime  
      #AND orderstatus = 3;
  END IF;
  #为订单内存表创建索引
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);


  #创建订单详情临时内存表（方便计算应收）
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

  # 向临时订单详情内存表中放值
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
    #AND a.orderstatus = 3;

  -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

  #创建结算明细内存表
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

  #生产临时结算明细表数据
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


  #除会员验证的那条数据外，删除其它无效数据
  DELETE
  FROM
    t_temp_settlement_detail
  WHERE
    isviporder = 0
    AND payamount = 0;

  #创建会员消费内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  CREATE TEMPORARY TABLE t_temp_order_member
  (
    orderid VARCHAR(50),
    Inflated DOUBLE(13, 2),
    netvalue DOUBLE(13, 2),
    ordertype TINYINT
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  #生成临时会员结算数据  
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


  ####################开始计算数据##################
  #设置订单数和人数
  SELECT sum(
         CASE ordertype
         WHEN 0 THEN
           1
         ELSE
           0
         END) #堂吃的订单数量
         , ifnull(sum(
         CASE ordertype
         WHEN 0 THEN
           mannum + womanNum + childNum
         ELSE
           0
         END), 0) #堂吃的就餐总人数
         , sum(
         CASE ordertype
         WHEN 0 THEN
           0
         ELSE
           1
         END) #外卖的订单数量
  INTO
    v_sa_ordercount, v_sa_settlementnum, v_oa_ordercount
  FROM
    t_temp_order
  WHERE orderstatus = 3;


  #计算应收总额
  SELECT ifnull(sum(
         CASE b.ordertype
         WHEN 0 THEN
           b.orignalprice * b.dishnum
         ELSE
           0
         END), 0) #堂吃应收
         , ifnull(sum(
         CASE b.ordertype
         WHEN 0 THEN
           0
         ELSE
           b.orignalprice * b.dishnum
         END), 0) #外卖应收
         , ifnull(sum(
         CASE b.pricetype
         WHEN '1' THEN
           b.orignalprice * b.dishnum
         ELSE
           0
         END), 0) #赠送金额
  INTO
    v_sa_shouldamount, v_oa_shouldamount, v_da_give
  FROM 
    t_temp_order a, t_temp_order_detail b
  WHERE
    a.orderid = b.orderid
    AND b.orignalprice > 0
    AND a.orderstatus = 3;

  #计算虚增，储值消费净值
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

  #计算外卖订单的实收金额（含虚增）
  SELECT ifnull(sum(payamount), 0)
  INTO
    v_oa_paidinamount
  FROM
    t_temp_settlement_detail
  WHERE
    payway IN (0, 1, 5, 8, 13, 17, 18)  -- SHANGWENCHAO 2015/12/21 23:28:59 增加结算方式13
    AND ordertype > 0;

  #设置实收（外卖） = 外卖实收- 外卖虚增
  SET v_oa_paidinamount = v_oa_paidinamount - v_oa_mebervalueadd;

  #计算每种结算方式的结算金额
  SELECT ifnull(sum(
         CASE
         WHEN payway = 0 THEN #现金
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway = 1 and membercardno<>'1' and payamount<>0 THEN #刷它行卡
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway = 1 and membercardno='1' and payamount<>0 THEN #刷工行卡
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway in(5,13) AND payamount > 0 THEN #挂账
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway = 8 THEN #会员卡消费
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway = 11 THEN #会员积分消费
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway = 12 THEN #会员券消费
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway = 7 THEN #抹零
           payamount
         ELSE
           0
         END), 0)
       , 0 - ifnull(sum(
         CASE
         WHEN payway = 20 THEN #四舍五入调整
           payamount
         ELSE
           0
         END), 0)
       , sum(isviporder) #会员订单数量
       , ifnull(sum(
         CASE
         WHEN payway = 17 THEN #微信支付
           payamount
         ELSE
           0
         END), 0)
       , ifnull(sum(
         CASE
         WHEN payway = 18 THEN #支付宝支付
           payamount
         ELSE
           0
         END), 0)
  INTO
    v_pa_cash, v_pa_card, v_pa_icbc_card, v_pa_credit, v_pa_paidinamount, v_da_integralconsum, v_da_meberTicket, v_da_fraction, v_da_roundoff, v_other_vipordercount, v_pa_weixin, v_pa_zhifubao
  FROM
    t_temp_settlement_detail;

  #设置实收（全部） = 现金 + 挂账 + 刷他行卡 + 会员储值消费 - 会员储值消费虚增 + 刷工行卡 + 微信支付 + 支付宝支付
  SET v_paidinamount = v_pa_cash + v_pa_credit + v_pa_card + v_pa_paidinamount - v_da_mebervalueadd + v_pa_icbc_card + v_pa_weixin + v_pa_zhifubao;

  #设置折扣额
  SELECT ifnull(sum(a.payamount), 0)
  INTO
    v_da_discount
  FROM
    t_temp_settlement_detail a, t_p_preferential_activity b
  WHERE
    a.couponid = b.id
    AND b.type = '02';

  #设置赠送金额（目前暂时默认为0）
  #SET v_da_give = 0;

  #设置优免金额 =堂吃应收+外卖应收 - 实收 - 四舍五入调整 - 会员积分消费 - 会员券消费 - 折扣优惠 - 抹零 - 赠送 - 虚增
  SET v_da_free = v_sa_shouldamount + v_oa_shouldamount - v_paidinamount - v_da_roundoff - v_da_integralconsum - v_da_meberTicket - v_da_discount - v_da_fraction - v_da_give - v_da_mebervalueadd;

  #获取餐厅总的餐台数
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

  #已结人数、已结账单数
	SELECT IFNULL(sum(too.custnum),0),    
    IFNULL(count(too.orderid),0)
  INTO v_closed_person_nums,v_closed_bill_nums
	FROM
	  t_temp_order too
  WHERE too.orderstatus = 3;

  #已结账单应收
  SELECT IFNULL(sum(tod.orignalprice * tod.dishnum),0) 
  INTO v_closed_bill_shouldamount
  FROM t_temp_order_detail tod
  WHERE tod.orderid in (select orderid from t_temp_order where orderstatus = 3);

  #未结人数、未结账单数
  SELECT IFNULL(sum(too.custnum),0),
    IFNULL(count(too.orderid),0)
  INTO v_no_person_nums,v_no_bill_nums
  FROM
	  t_temp_order too
  WHERE too.orderstatus = 0;

  #未结账单应收
  SELECT IFNULL(sum(tod.orignalprice * tod.dishnum),0) 
  INTO v_no_bill_shouldamount
  FROM t_temp_order_detail tod
  WHERE tod.orderid in (select orderid from t_temp_order where orderstatus = 0);

  #在台数
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

  #开台数
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

  #全部账单数 = 已结账单数 + 未结账单数
  SET v_bill_nums = v_closed_bill_nums + v_no_bill_nums;
  
  #全部账单应收 = 已结账单应收 + 外卖应收 + 赠送金额 + 未结账单应收
  SET v_bill_shouldamount = v_closed_bill_shouldamount + v_no_bill_shouldamount;
   
  #全部人数 = 已结人数 + 未结人数
  SET v_person_nums = v_closed_person_nums + v_no_person_nums;

  IF v_sa_ordercount > 0 THEN
    #计算上座率  总人数/总天数=平均每天总人数；平均每天总人数/标准座位数=上座率
    SET v_sa_attendance = v_sa_settlementnum / (timestampdiff(DAY, v_date_start, v_date_end) + 1) / v_other_tableperson * 100;

    #计算翻台率  总用餐桌数/总天数=平均每天总桌数；平均每天总桌数/标准桌数=翻台率
    SET v_sa_overtaiwan = v_sa_ordercount / (timestampdiff(DAY, v_date_start, v_date_end) + 1) / v_other_tablecount * 100;

    #计算桌均消费 = （实收总额 - 外卖实收） / 总桌数
    SET v_sa_tableconsumption = (v_paidinamount - v_oa_paidinamount) / v_sa_ordercount;

    #应收人均 = 应收总额 / 总就餐人数
    SET v_sa_shouldaverage = v_sa_shouldamount / v_sa_settlementnum;

    #实收人均 = （实收总额 - 外卖实收） / 总就餐人数
    SET v_sa_paidinaverage = (v_paidinamount - v_oa_paidinamount) / v_sa_settlementnum; #营业数据统计(实收人均）

    #计算平均就餐时间（分钟）=总时长/总用餐桌数
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
    #外卖单均 = 外卖应收 / 外卖单数
    SET v_oa_avgprice = v_oa_paidinamount / v_oa_ordercount;
  END IF;

  #会员消费合计 = 会员券消费 + 会员积分消费 + 会员储值消费（包括净值和虚增）
  SET v_ma_total = v_da_meberTicket + v_da_integralconsum + v_pa_paidinamount;

  #设置会员消费占比 = 会员消费笔数 / 订单总数   订单总数 = 堂吃订单数 + 外买订单数
  SET v_other_viporderpercent = v_other_vipordercount / (v_sa_ordercount + v_oa_ordercount) * 100;

  ####################开始处理结果数据##################
  #创建结果内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    shouldamount DOUBLE(13, 2), #应收总额
    paidinamount DOUBLE(13, 2), #实收总额
    discountamount DOUBLE(13, 2), #折扣总额
    cash DOUBLE(13, 2), #实收（现金）
    credit DOUBLE(13, 2), #实收（挂账）
    card DOUBLE(13, 2), #实收（刷他行卡）
    icbccard DOUBLE(13, 2), #实收（刷工行卡）
    weixin DOUBLE(13, 2), #实收（微信）
    zhifubao DOUBLE(13, 2), #实收（支付宝）
    merbervaluenet DOUBLE(13, 2), #实收（会员储值消费净值）
    free DOUBLE(13, 2), #折扣(优免）
    integralconsum DOUBLE(13, 2), #折扣(会员积分消费）
    meberTicket DOUBLE(13, 2), #折扣(会员券）
    discount DOUBLE(13, 2), #折扣(折扣额）
    fraction DOUBLE(13, 2), #折扣(抹零）
    give DOUBLE(13, 2), #折扣(赠送）
    roundoff DOUBLE(13, 2), #折扣(四舍五入调整）
    mebervalueadd DOUBLE(13, 2), #折扣(虚增）
    tablecount DOUBLE(13, 2), #营业数据统计(桌数）
    tableconsumption DOUBLE(13, 2), #营业数据统计(桌均消费）  
    settlementnum DOUBLE(13, 2), #营业数据统计(总人数）
    shouldaverage DOUBLE(13, 2), #营业数据统计(应收人均）
    paidinaverage DOUBLE(13, 2), #营业数据统计(实收人均）
    attendance DOUBLE(13, 2), #营业数据统计(上座率）
    overtaiwan DOUBLE(13, 2), #营业数据统计(翻台率）
    avgconsumtime DOUBLE(13, 2), #营业数据统计(平均消费时间）
    shouldamount_normal DOUBLE(13, 2), #营业数据统计(堂吃应收）
    shouldamount_takeout DOUBLE(13, 2), #外卖统计(应收）
    paidinamount_takeout DOUBLE(13, 2), #外卖统计(实收）
    ordercount_takeout DOUBLE(13, 2), #外卖统计(订单数）
    avgprice_takeout DOUBLE(13, 2), #外卖统计(订单平均价格）
    vipordercount INT(11), #会员消费笔数
    viporderpercent DOUBLE(13, 2), #会员消费占比
    membertotal DOUBLE(13, 2), #会员消费合计
    closedbillnums INT(11), #已结账单数
    closedbillshouldamount DOUBLE(13, 2), #已结账单应收
    closedpersonnums  INT(11), #已结人数
    nobillnums    INT(11), #未结账单数
		nobillshouldamount  DOUBLE(13, 2), #未结账单应收
		nopersonnums  INT(11), #未结人数
		billnums   INT(11), #全部账单数
		billshouldamount  DOUBLE(13, 2), #全部账单应收
		personnums  DOUBLE(13, 2), #全部人数
		zaitaishu   INT(11), #在台数
		kaitaishu    INT(11) #开台数
	) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  INSERT INTO t_temp_res VALUES (v_sa_shouldamount + v_oa_shouldamount, v_paidinamount, v_sa_shouldamount + v_oa_shouldamount - v_paidinamount, v_pa_cash, v_pa_credit, v_pa_card, v_pa_icbc_card, v_pa_weixin, v_pa_zhifubao, v_pa_paidinamount - v_da_mebervalueadd, v_da_free, v_da_integralconsum, v_da_meberTicket, v_da_discount, v_da_fraction, v_da_give, v_da_roundoff, v_da_mebervalueadd, v_sa_ordercount, v_sa_tableconsumption, v_sa_settlementnum, v_sa_shouldaverage, v_sa_paidinaverage, v_sa_attendance, v_sa_overtaiwan, v_sa_avgconsumtime, v_sa_shouldamount, v_oa_shouldamount, v_oa_paidinamount, v_oa_ordercount, v_oa_avgprice, v_other_vipordercount, v_other_viporderpercent, v_ma_total,v_closed_bill_nums,v_closed_bill_shouldamount,v_closed_person_nums, v_no_bill_nums, v_no_bill_shouldamount, v_no_person_nums, v_bill_nums, v_bill_shouldamount, v_person_nums, v_zaitaishu, v_kaitaishu);
  SELECT *
  FROM
    t_temp_res;

#清空内存表
--   DROP TEMPORARY TABLE IF EXISTS t_temp_order;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_res;
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_yyhzbxq$$
CREATE PROCEDURE p_report_yyhzbxq(IN `pi_branchid` int(11)
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
  DECLARE v_cur_orderid  VARCHAR(50);#当前订单id
  DECLARE v_fetch_done BOOL DEFAULT FALSE; 
  DECLARE v_branchid VARCHAR(50); 
  DECLARE v_branchname VARCHAR(50); 
  DECLARE v_shouldamount VARCHAR(50); 
  DECLARE v_paidinamount  VARCHAR(128); #实收总额
  DECLARE v_brandid  VARCHAR(128); #品牌
  DECLARE v_marketid  VARCHAR(128); #市场
  DECLARE v_areaid  VARCHAR(128); #区域
  DECLARE v_jde  VARCHAR(128); #jde
  DECLARE v_brand_name  VARCHAR(128); #品牌
  DECLARE v_market_name  VARCHAR(128); #市场
  DECLARE v_area_name  VARCHAR(128); #区域
  DECLARE v_datetime  VARCHAR(128); #日期

  #以下为实收明细统计项
  DECLARE v_pa_cash               DOUBLE(13, 2) DEFAULT 0; #实收（现金）
  DECLARE v_pa_credit             DOUBLE(13, 2) DEFAULT 0; #实收（挂账）
  DECLARE v_pa_card               DOUBLE(13, 2) DEFAULT 0; #实收（刷卡——刷他行卡）
  DECLARE v_pa_icbc_card          DOUBLE(13, 2) DEFAULT 0; #实收（刷卡——刷工行卡）
  DECLARE v_pa_paidinamount       DOUBLE(13, 2) DEFAULT 0; #会员储值消费（含虚增）
  DECLARE v_pa_weixin             DOUBLE(13, 2) DEFAULT 0; #实收（微信）
  DECLARE v_pa_zhifubao           DOUBLE(13, 2) DEFAULT 0; #实收（支付宝）
  
  #以下为折扣明细统计项
  DECLARE v_da_free               DOUBLE(13, 2) DEFAULT 0; #折扣(优免）
  DECLARE v_da_integralconsum     DOUBLE(13, 2) DEFAULT 0; #折扣(会员积分消费）
  DECLARE v_da_meberTicket        DOUBLE(13, 2) DEFAULT 0; #折扣(会员券）
  DECLARE v_da_discount           DOUBLE(13, 2) DEFAULT 0; #折扣(折扣额）
  DECLARE v_da_fraction           DOUBLE(13, 2) DEFAULT 0; #折扣(抹零）
  DECLARE v_da_roundoff           DOUBLE(13, 2) DEFAULT 0; #折扣(四舍五入调整）
  DECLARE v_da_give               DOUBLE(13, 2) DEFAULT 0; #折扣(赠送）
  DECLARE v_da_mebervalueadd      DOUBLE(13, 2) DEFAULT 0; #折扣(会员储值消费虚增）
  DECLARE v_da_credit2      	  DOUBLE(13, 2) DEFAULT 0; #挂账2
  DECLARE v_da_free2      		  DOUBLE(13, 2) DEFAULT 0; #优惠2
  DECLARE v_da_totalfree      	  DOUBLE(13, 2) DEFAULT 0; #优惠活动优免
  DECLARE v_da_mebervalueaddnet   DOUBLE(13, 2) DEFAULT 0; #会员净增
  
  #以下是活动明细统计项
  DECLARE v_pid         VARCHAR(50); #活动id
  DECLARE v_pname         VARCHAR(128); #活动名称
  DECLARE v_ptype         CHAR(4); #活动分类编码
  DECLARE v_pamount       DOUBLE(13, 2); #活动发生金额
  DECLARE v_value_detail  VARCHAR(10000); #活动发生金额
  DECLARE v_value_total  VARCHAR(10000); #活动发生金额统计

  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; #当读到数据的最后一条时,设置v_fetch_done变量为TRUE
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
  #设置当前品牌id
	SELECT id,name INTO v_brandid,v_brand_name  FROM t_c_brand WHERE id = pi_brandid;
  ELSE
	SELECT id,name INTO v_brandid,v_brand_name FROM t_c_brand LIMIT 1;
  END IF;  
  
  IF pi_marketid > -1 THEN
  #设置当前市场id
	SELECT id,name INTO v_marketid,v_market_name  FROM t_c_market WHERE id = pi_marketid;
  ELSE
	SELECT id,name INTO v_marketid,v_market_name FROM t_c_market LIMIT 1;
  END IF;  
  
  IF pi_areaid > -1 THEN
  #设置当前区域id
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
  
  #创建orderid索引
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);
  #早市晚市
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
	
   -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;
	
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    couponid VARCHAR(50),
    isviporder TINYINT, #是否会员卡消费 1:是; 0:否;
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
	
  #除会员验证的那条数据外，删除其它无效数据  
  DELETE
  FROM
    t_temp_settlement_detail
  WHERE
    isviporder = 0
    AND payamount = 0;
  
  #创建会员消费内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  CREATE TEMPORARY TABLE t_temp_order_member
  (
    orderid VARCHAR(50),
    Inflated DOUBLE(13, 2),
    netvalue DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_order_member_orderid ON t_temp_order_member (orderid);

  #生成临时会员结算数据  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
       , b.netvalue
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid;
	
  #创建优惠活动内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_preferential;
  CREATE TEMPORARY TABLE t_temp_preferential
  (
    pid VARCHAR(50),
    pname VARCHAR(128),
    ptype CHAR(4),
    ptypename VARCHAR(32)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
	
  #生成正常的优惠券数据，包括将其他优惠中的具体原因和合作单位也看成单独的优惠
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
  ORDER BY b.code; #按code排序
  
    #创建订单详情临时表
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

  #生成结算详情活动联合表
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new;
  CREATE TEMPORARY TABLE t_temp_settlement_new
  (
    orderid VARCHAR(50),
    payway INT, #结算方式
    payamount DOUBLE(13, 2), #结算金额
	pid VARCHAR(50),#活动id
    pname VARCHAR(128), #活动名称
    ptype CHAR(4) #活动分类
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  CREATE INDEX ix_t_temp_settlement_new ON t_temp_settlement_new(orderid);

  #生成挂账和优免数据（不包含金总挂账数据,另外统计在结果表中）
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

  #创建优惠中间临时表
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new_sub;
  CREATE TEMPORARY TABLE t_temp_settlement_new_sub
  (
    orderid VARCHAR(50),
    payway INT, #结算方式
    payamount DOUBLE(13, 2), #结算金额
    pid VARCHAR(50), #活动id
    pname VARCHAR(128), #活动名称
    ptype CHAR(4) #活动分类
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  CREATE INDEX ix_t_temp_settlement_new_sub ON t_temp_settlement_new_sub(orderid);
  
  #创建结算详情临时表
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail_sub;
  CREATE TEMPORARY TABLE t_temp_settlement_detail_sub
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    couponid VARCHAR(50),
    isviporder TINYINT, #是否会员卡消费 1:是; 0:否;
    coupondetailid VARCHAR(50),
	couponNum INT,
	bankcardno VARCHAR(50),
    membercardno VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_settlement_detail_sub_orderid ON t_temp_settlement_detail_sub (orderid);
  
  ##创建结果内存表
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
	jde VARCHAR(50),#jde
	branchid VARCHAR(50),  #活动id
	branchname VARCHAR(50), #活动名称
	orderid VARCHAR(50), #订单id
    shouldamount decimal(13, 2), #应收总额
    paidinamount decimal(13, 2), #实收总额
    discountamount decimal(13, 2), #折扣总额
    cash decimal(13, 2) DEFAULT 0.00, #实收（现金）
    credit decimal(13, 2) DEFAULT 0.00, #实收（挂账）
    credit2 decimal(13, 2) DEFAULT 0.00, #实收（挂账2）
    card decimal(13, 2) DEFAULT 0.00, #实收（刷他行卡）
    icbccard decimal(13, 2) DEFAULT 0.00, #实收（刷工行卡）
    weixin decimal(13, 2) DEFAULT 0.00, #实收（微信）
    zhifubao decimal(13, 2) DEFAULT 0.00, #实收（支付宝）
    merbervaluenet decimal(13, 2) DEFAULT 0.00, #实收（会员储值消费净值）
    integralconsum decimal(13, 2) DEFAULT 0.00, #折扣(会员积分消费）
    meberTicket decimal(13, 2) DEFAULT 0.00, #折扣(会员券）
    fraction decimal(13, 2) DEFAULT 0.00, #折扣(抹零）
    give decimal(13, 2) DEFAULT 0.00, #折扣(赠送）
    roundoff decimal(13, 2) DEFAULT 0.00, #折扣(四舍五入调整）
    mebervalueadd decimal(13, 2) DEFAULT 0.00, #折扣(虚增）
    free decimal(13, 2) DEFAULT 0.00, #折扣(优免)
	preferentialdetail VARCHAR(10000), #活动详细信息
	totalfree VARCHAR(10000), #测试
	PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  ##创建结果内存优惠详情页
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail;
  CREATE TEMPORARY TABLE t_temp_res_detail
  (
	pid VARCHAR(50),#活动id
    pname VARCHAR(128), #活动名称
    ptype CHAR(4), #活动分类
    payamount DOUBLE(13, 2) #结算金额
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  	#创建所有优惠集合
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
		
		#增加 优免
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
	
 #遍历分店
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
			
			
			#清空订单详情临时表
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
				
			#清空订单临时表
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
			
			#计算应收
			SELECT ifnull(sum(orignalprice * dishnum), 0)
			INTO
			  v_shouldamount
			FROM
			  t_temp_order_detail_sub;

			#计算每种结算方式的结算金额
		    SELECT ifnull(sum(
				 CASE
				 WHEN payway = 0 THEN #现金
				   payamount
				 ELSE
				   0
				 END), 0)
				,ifnull(sum(
				 CASE
				 WHEN payway = 6 THEN #优免
				   payamount
				 ELSE
				   0
				 END), 0)
				,ifnull(sum(
				 CASE
				 WHEN payway = 13 THEN #挂账2
				   payamount
				 ELSE
				   0
				 END), 0) 
			   ,ifnull(sum(
				 CASE
				 WHEN payway = 1 and membercardno='0' and payamount<>0 THEN #刷它行卡
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 1 and membercardno='1' and payamount<>0 THEN #刷工行卡
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 5 AND payamount > 0 THEN #挂账
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 8 THEN #会员卡消费
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 11 THEN #会员积分消费
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 12 THEN #会员券消费
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 7 THEN #抹零
				   payamount
				 ELSE
				   0
				 END), 0)
			   , 0 - ifnull(sum(
				 CASE
				 WHEN payway = 20 THEN #四舍五入调整
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 17 THEN #微信支付
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 18 THEN #支付宝支付
				   payamount
				 ELSE
				   0
				 END), 0)
		    INTO
			  v_pa_cash,v_da_free,v_da_credit2, v_pa_card, v_pa_icbc_card, v_pa_credit, v_pa_paidinamount, v_da_integralconsum, v_da_meberTicket, v_da_fraction, v_da_roundoff, v_pa_weixin, v_pa_zhifubao
		    FROM
			  t_temp_settlement_detail_sub;
					
			#赠送金额
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
			
			#计算虚增
			SELECT ifnull(sum(Inflated), 0)
			INTO
			  v_da_mebervalueadd
			FROM
			  t_temp_order_member a
			WHERE
			  a.orderid = v_cur_orderid;
				
			#设置实收（全部） = 现金 + 挂账 + 刷他行卡 + 会员储值消费 - 会员储值消费虚增 + 刷工行卡 + 微信支付 + 支付宝支付+挂账2
			SET v_paidinamount = v_pa_cash + v_pa_credit + v_pa_card + v_pa_paidinamount - v_da_mebervalueadd + v_pa_icbc_card + v_pa_weixin + v_pa_zhifubao+v_da_credit2;
				
			#清空所有的中间临时小表
			TRUNCATE t_temp_settlement_new_sub;

			#把t_temp_settlement_new拆分成一个个字表，加快循环速度(只计算优免信息)
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
			  
			  #最后关闭游标.
			  CLOSE cur_pname;
			  
			 END;
			 
			SET v_da_totalfree = 0;
			SELECT ifnull(sum(payamount),0) INTO
			     v_da_totalfree
			FROM  t_temp_res_detail;
			
		    #设置优免金额(实际值来源于会员VIP菜价) =应收 - 实收 - 四舍五入调整 - 会员积分消费 - 会员券消费 - 优惠活动优免 - 抹零 - 赠送 - 虚增
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
				
			#生成一条分店统计结果
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
		
		#最后关闭游标.
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
	#更新优免信息
	SELECT REPLACE(v_value_total,'youmian,会员优免,0.00',CONCAT('youmian,会员优免,',v_da_free2)) INTO v_value_total;
	#合计
    INSERT INTO t_temp_res
	  (branchid,orderid,branchname,shouldamount,paidinamount,discountamount,cash,credit,credit2,card,icbccard,weixin,zhifubao,merbervaluenet,
	  integralconsum,meberTicket,fraction,give,roundoff,mebervalueadd,free,preferentialdetail,totalfree)
	VALUES
	  (NULL,'最终合计',NULL,v_shouldamount,v_paidinamount,v_da_discount,v_pa_cash,v_pa_credit,v_da_credit2,v_pa_card,v_pa_icbc_card,
	   v_pa_weixin,v_pa_zhifubao,v_da_mebervalueaddnet,v_da_integralconsum,v_da_meberTicket,v_da_fraction,v_da_give,v_da_roundoff,
	   v_da_mebervalueadd,v_da_free2,v_value_total,v_da_totalfree );
  
  SELECT * FROM t_temp_res ORDER BY id asc;
  
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_yyhzb$$
CREATE PROCEDURE p_report_yyhzb(IN `pi_branchid` varchar(50)
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
  DECLARE v_branchname  VARCHAR(50);#当前分店名称
  DECLARE v_fetch_done BOOL DEFAULT FALSE; 
  DECLARE v_branchid VARCHAR(50) DEFAULT NULL; 
  DECLARE v_shouldamount VARCHAR(50); 
  DECLARE v_paidinamount  VARCHAR(128); #实收总额
  DECLARE v_brandid  VARCHAR(128); #品牌
  DECLARE v_marketid  VARCHAR(128); #市场
  DECLARE v_areaid  VARCHAR(128); #区域
  DECLARE v_jde  VARCHAR(128); #jde
  DECLARE v_brand_name  VARCHAR(128); #品牌
  DECLARE v_market_name  VARCHAR(128); #市场
  DECLARE v_area_name  VARCHAR(128); #区域
  DECLARE v_datetime  VARCHAR(128); #日期
  DECLARE v_num  INT(11); #数量

  #以下为实收明细统计项
  DECLARE v_pa_cash               DOUBLE(13, 2) DEFAULT 0; #实收（现金）
  DECLARE v_pa_credit             DOUBLE(13, 2) DEFAULT 0; #实收（挂账）
  DECLARE v_pa_card               DOUBLE(13, 2) DEFAULT 0; #实收（刷卡——刷他行卡）
  DECLARE v_pa_icbc_card          DOUBLE(13, 2) DEFAULT 0; #实收（刷卡——刷工行卡）
  DECLARE v_pa_paidinamount       DOUBLE(13, 2) DEFAULT 0; #会员储值消费（含虚增）
  DECLARE v_pa_weixin             DOUBLE(13, 2) DEFAULT 0; #实收（微信）
  DECLARE v_pa_zhifubao           DOUBLE(13, 2) DEFAULT 0; #实收（支付宝）
  
  #以下为折扣明细统计项
  DECLARE v_da_free               DOUBLE(13, 2) DEFAULT 0; #折扣(优免）
  DECLARE v_da_integralconsum     DOUBLE(13, 2) DEFAULT 0; #折扣(会员积分消费）
  DECLARE v_da_meberTicket        DOUBLE(13, 2) DEFAULT 0; #折扣(会员券）
  DECLARE v_da_discount           DOUBLE(13, 2) DEFAULT 0; #折扣(折扣额）
  DECLARE v_da_fraction           DOUBLE(13, 2) DEFAULT 0; #折扣(抹零）
  DECLARE v_da_roundoff           DOUBLE(13, 2) DEFAULT 0; #折扣(四舍五入调整）
  DECLARE v_da_give               DOUBLE(13, 2) DEFAULT 0; #折扣(赠送）
  DECLARE v_da_mebervalueadd      DOUBLE(13, 2) DEFAULT 0; #折扣(会员储值消费虚增）
  DECLARE v_da_credit2      	  DOUBLE(13, 2) DEFAULT 0; #挂账2
  DECLARE v_da_free2      		  DOUBLE(13, 2) DEFAULT 0; #优惠2
  DECLARE v_da_totalfree      	  DOUBLE(13, 2) DEFAULT 0; #优惠活动优免
  DECLARE v_da_mebervalueaddnet   DOUBLE(13, 2) DEFAULT 0; #会员净增
  
  #以下是活动明细统计项
  DECLARE v_pid         VARCHAR(50); #活动id
  DECLARE v_pname         VARCHAR(128); #活动名称
  DECLARE v_ptype         CHAR(4); #活动分类编码
  DECLARE v_pamount       DOUBLE(13, 2); #活动发生金额
  DECLARE v_value_detail  VARCHAR(10000); #活动发生金额
  DECLARE v_value_total  VARCHAR(10000); #活动发生金额统计

  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; #当读到数据的最后一条时,设置v_fetch_done变量为TRUE
  END;

  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
	SELECT NULL;
	GET DIAGNOSTICS CONDITION 1 po_errormsg = MESSAGE_TEXT;
  END;
  
  IF pi_brandid > -1 THEN
  #设置当前品牌id
	SELECT id,name INTO v_brandid,v_brand_name  FROM t_c_brand WHERE id = pi_brandid;
  ELSE
	SELECT id,name INTO v_brandid,v_brand_name FROM t_c_brand LIMIT 1;
  END IF;  
  
  IF pi_marketid > -1 THEN
  #设置当前市场id
	SELECT id,name INTO v_marketid,v_market_name  FROM t_c_market WHERE id = pi_marketid;
  ELSE
	SELECT id,name INTO v_marketid,v_market_name FROM t_c_market LIMIT 1;
  END IF;  
  
  IF pi_areaid > -1 THEN
  #设置当前区域id
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
	#设置当前分店id
	INSERT INTO t_temp_branch SELECT branchid,branchname  FROM t_branch WHERE branchid=CONCAT(pi_branchid) OR branchname LIKE CONCAT('%',pi_branchid,'%'); 
	SELECT ifnull(COUNT(1),0) INTO v_branchid FROM t_temp_branch;
	IF v_branchid = 0 THEN 
		SELECT NULL;
		SET po_errormsg = '分店ID不存在';
		LEAVE label_main;
	END IF;
  END IF;
  
  #生成要统计的分店信息
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
  
  #创建orderid索引
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);
  #创建branchid索引
  CREATE INDEX ix_t_temp_order_branchid ON t_temp_order (branchid);
  
  #早市晚市
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

	 -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;
 
	
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    couponid VARCHAR(50),
    ordertype TINYINT,
    isviporder TINYINT, #是否会员卡消费 1:是; 0:否;
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
	
  #除会员验证的那条数据外，删除其它无效数据  
  DELETE
  FROM
    t_temp_settlement_detail
  WHERE
    isviporder = 0
    AND payamount = 0;
  
  #创建会员消费内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  CREATE TEMPORARY TABLE t_temp_order_member
  (
    orderid VARCHAR(50),
    Inflated DOUBLE(13, 2),
    netvalue DOUBLE(13, 2),
    ordertype TINYINT
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_order_member_orderid ON t_temp_order_member (orderid);

  #生成临时会员结算数据  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
       , b.netvalue
       , a.ordertype
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid;
	
  #创建订单id临时表
  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid;
  CREATE TEMPORARY TABLE t_temp_orderid
  (
	orderid VARCHAR(50),
	begintime datetime
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  #创建优惠活动内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_preferential;
  CREATE TEMPORARY TABLE t_temp_preferential
  (
    pid VARCHAR(50),
    pname VARCHAR(128),
    ptype CHAR(4),
    ptypename VARCHAR(32)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
	
  #生成正常的优惠券数据，包括将其他优惠中的具体原因和合作单位也看成单独的优惠
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
  ORDER BY b.code; #按code排序

  #生成结算详情活动联合表
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new;
  CREATE TEMPORARY TABLE t_temp_settlement_new
  (
    orderid VARCHAR(50),
    payway INT, #结算方式
    payamount DOUBLE(13, 2), #结算金额
	pid VARCHAR(50),#活动id
    pname VARCHAR(128), #活动名称
    ptype CHAR(4) #活动分类
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  CREATE INDEX ix_t_temp_settlement_new ON t_temp_settlement_new(orderid);

  #生成优免数据（不包含金总挂账数据）
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

  #创建优惠中间临时表
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new_sub;
  CREATE TEMPORARY TABLE t_temp_settlement_new_sub
  (
    orderid VARCHAR(50),
    payway INT, #结算方式
    payamount DOUBLE(13, 2), #结算金额
    pid VARCHAR(50), #活动id
    pname VARCHAR(128), #活动名称
    ptype CHAR(4) #活动分类
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  CREATE INDEX ix_t_temp_settlement_new_sub ON t_temp_settlement_new_sub(orderid);
  
  #创建订单详情临时表
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
  
  #创建结算详情临时表
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail_sub;
  CREATE TEMPORARY TABLE t_temp_settlement_detail_sub
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    couponid VARCHAR(50),
    ordertype TINYINT,
    isviporder TINYINT, #是否会员卡消费 1:是; 0:否;
    coupondetailid VARCHAR(50),
	couponNum INT,
	bankcardno VARCHAR(50),
    membercardno VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_settlement_detail_sub_orderid ON t_temp_settlement_detail_sub (orderid);
  
  ##创建结果内存表
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
	jde VARCHAR(50),#jde
	branchid VARCHAR(50),  #活动id
	branchname VARCHAR(50), #活动名称
    shouldamount decimal(13, 2), #应收总额
    paidinamount decimal(13, 2), #实收总额
    discountamount decimal(13, 2), #折扣总额
    cash decimal(13, 2) DEFAULT 0.00, #实收（现金）
    credit decimal(13, 2) DEFAULT 0.00, #实收（挂账）
    credit2 decimal(13, 2) DEFAULT 0.00, #实收（挂账2）
    card decimal(13, 2) DEFAULT 0.00, #实收（刷他行卡）
    icbccard decimal(13, 2) DEFAULT 0.00, #实收（刷工行卡）
    weixin decimal(13, 2) DEFAULT 0.00, #实收（微信）
    zhifubao decimal(13, 2) DEFAULT 0.00, #实收（支付宝）
    merbervaluenet decimal(13, 2) DEFAULT 0.00, #实收（会员储值消费净值）
    integralconsum decimal(13, 2) DEFAULT 0.00, #折扣(会员积分消费）
    meberTicket decimal(13, 2) DEFAULT 0.00, #折扣(会员券）
    fraction decimal(13, 2) DEFAULT 0.00, #折扣(抹零）
    give decimal(13, 2) DEFAULT 0.00, #折扣(赠送）
    roundoff decimal(13, 2) DEFAULT 0.00, #折扣(四舍五入调整）
    mebervalueadd decimal(13, 2) DEFAULT 0.00, #折扣(虚增）
    free decimal(13, 2) DEFAULT 0.00, #折扣(会员优免 - 直接相减 )
    free2 decimal(13, 2) DEFAULT 0.00, #折扣(会员优免2 - 详细计算 )
	preferentialdetail VARCHAR(10000), #活动详细信息
	totalfree VARCHAR(10000), #测试
	PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  ##创建结果内存优惠详情页
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail;
  CREATE TEMPORARY TABLE t_temp_res_detail
  (
	pid VARCHAR(50) NOT NULL,#活动id
    pname VARCHAR(128), #活动名称
    ptype CHAR(4), #活动分类
    payamount DOUBLE(13, 2), #结算金额
	PRIMARY KEY(pid)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  #创建pid索引
  CREATE UNIQUE INDEX ix_t_temp_res_detail_pid ON t_temp_res_detail (pid);	
	
	#创建所有优惠集合
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
		
		#增加 优免
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
	
 #遍历分店
  BEGIN
  
	DECLARE cur_branchid CURSOR FOR SELECT DISTINCT branchid
										FROM t_temp_order USE INDEX (ix_t_temp_order_branchid)  GROUP BY branchid;
										
	#设置统计时间
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
			
			#清空订单详情临时表
			TRUNCATE t_temp_order_detail_sub;
			
			#计算该分店所包括的订单号
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
			
			#清空订单详情临时表
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
			
			#计算应收
			SELECT ifnull(sum(orignalprice * dishnum), 0)
			INTO
			  v_shouldamount
			FROM
			  t_temp_order_detail_sub;
			  

			#计算每种结算方式的结算金额
		    SELECT ifnull(sum(
				 CASE
				 WHEN payway = 0 THEN #现金
				   payamount
				 ELSE
				   0
				 END), 0)
				,ifnull(sum(
				 CASE
				 WHEN payway = 6 THEN #优免
				   payamount
				 ELSE
				   0
				 END), 0)
				,ifnull(sum(
				 CASE
				 WHEN payway = 13 THEN #挂账2
				   payamount
				 ELSE
				   0
				 END), 0) 
			   ,ifnull(sum(
				 CASE
				 WHEN payway = 1 and membercardno='0' and payamount<>0 THEN #刷它行卡
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 1 and membercardno='1' and payamount<>0 THEN #刷工行卡
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 5 AND payamount > 0 THEN #挂账
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 8 THEN #会员卡消费
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 11 THEN #会员积分消费
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 12 THEN #会员券消费
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 7 THEN #抹零
				   payamount
				 ELSE
				   0
				 END), 0)
			   , 0 - ifnull(sum(
				 CASE
				 WHEN payway = 20 THEN #四舍五入调整
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 17 THEN #微信支付
				   payamount
				 ELSE
				   0
				 END), 0)
			   , ifnull(sum(
				 CASE
				 WHEN payway = 18 THEN #支付宝支付
				   payamount
				 ELSE
				   0
				 END), 0)
		    INTO
			  v_pa_cash,v_da_free,v_da_credit2, v_pa_card, v_pa_icbc_card, v_pa_credit, v_pa_paidinamount, v_da_integralconsum, v_da_meberTicket, v_da_fraction, v_da_roundoff, v_pa_weixin, v_pa_zhifubao
		    FROM
			  t_temp_settlement_detail_sub;
				
			#赠送金额
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
			
			#计算虚增
			SELECT ifnull(sum(Inflated), 0)
			INTO
			  v_da_mebervalueadd
			FROM
			  t_temp_order_member
			JOIN  t_temp_orderid
			ON
				t_temp_order_member.orderid = t_temp_orderid.orderid;
				
			#设置实收（全部） = 现金 + 挂账 + 刷他行卡 + 会员储值消费 - 会员储值消费虚增 + 刷工行卡 + 微信支付 + 支付宝支付+挂账2
			SET v_paidinamount = v_pa_cash + v_pa_credit + v_pa_card + v_pa_paidinamount - v_da_mebervalueadd + v_pa_icbc_card + v_pa_weixin + v_pa_zhifubao+v_da_credit2;
				
			#清空所有的中间临时小表
			TRUNCATE t_temp_settlement_new_sub;

			#把t_temp_settlement_new拆分成一个个字表，加快循环速度(只计算优免信息)
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
			  
			  #最后关闭游标.
			  CLOSE cur_pname;
			  
			 END;
			 
			SET v_da_totalfree = 0;
			SELECT ifnull(sum(payamount),0) INTO
			     v_da_totalfree
			FROM  t_temp_res_detail;
			
		    #设置优免金额(实际值来源于会员VIP菜价) =应收 - 实收 - 四舍五入调整 - 会员积分消费 - 会员券消费 - 优惠活动优免 - 抹零 - 赠送 - 虚增
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
			
			#生成一条分店统计结果
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
		
		#最后关闭游标.
		CLOSE cur_branchid;
  END;
  
  SELECT COUNT(1) INTO v_num FROM t_temp_res ;
  
  IF v_num <> 0 THEN 
  #合计
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
  
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_yyfx_yysjtj$$
CREATE PROCEDURE p_report_yyfx_yysjtj(IN  pi_branchid INT(11), 
                                      IN  pi_xslx     SMALLINT, 
                                      IN  pi_ksrq     DATETIME, 
                                      IN  pi_jsrq     DATETIME, 
                                      OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '营业分析_营业数据统计'
label_main:
BEGIN
  -- 返回字段说明如下：
  -- 时间，应收总额，实收总额， 折扣总额
  --
  -- 返回数据举例（多行数据）：
  -- 2015-05  3636.00  3630.00  6.00
  -- 2015-06  4636.00  4630.00  6.00

  DECLARE v_date_start        DATETIME;
  DECLARE v_date_end          DATETIME;
  DECLARE v_date_interval     DATETIME; #时间间隔
  DECLARE v_loop_num          INT DEFAULT 0; #根据开始结束时间和显示类型，来设置循环次数
  DECLARE v_statistictime     VARCHAR(15); #统计日期
  DECLARE v_shouldamount      DOUBLE(13, 2); #应收
  DECLARE v_paidinamount      DOUBLE(13, 2); #实收(含虚增)
  DECLARE v_inflated          DOUBLE(13, 2); #虚增
  DECLARE v_person_con        DOUBLE(13, 2) DEFAULT 0; #人均
  DECLARE v_table_num         INT DEFAULT 0; #桌数
  DECLARE v_sa_settlementnum  INT DEFAULT 0; #总人数

  -- 异常处理模块，出现异常返回null
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

  #设置临时内存表
  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;


  #设置循环次数,处理开始结算时间
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

  #创建订单临时内存表
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
    AND begintime BETWEEN v_date_start AND v_date_end # 需要创建索引IX_t_order_begintime  
    AND orderstatus = 3;


  #为订单内存表创建索引
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);
  CREATE INDEX ix_t_temp_order_begintime ON t_temp_order (begintime);

  #创建订单详情临时内存表（方便计算应收）
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

  # 向临时订单详情内存表中放值
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
 -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
  delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

  #为订单详情表创建索引
  CREATE INDEX ix_t_temp_order_detail_begintime ON t_temp_order_detail (begintime);

  #创建结算明细内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail
  (
    orderid VARCHAR(50),
    payway INT,
    payamount DOUBLE(13, 2),
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  #生产临时结算明细表数据
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
    AND b.payway IN (0, 1, 5, 8,13,17,18);
  CREATE INDEX ix_t_temp_settlement_detail_begintime ON t_temp_settlement_detail (begintime);

  #创建会员消费内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  CREATE TEMPORARY TABLE t_temp_order_member
  (
    orderid VARCHAR(50),
    Inflated DOUBLE(13, 2),
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  #生成临时会员结算数据  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
       , a.begintime
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid;
  CREATE INDEX ix_t_temp_order_member_begintime ON t_temp_order_member (begintime);

  #创建结果内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    statistictime VARCHAR(15), #统计日期
    shouldamount DOUBLE(13, 2), #应收
    paidinamount DOUBLE(13, 2), #实收
    discountamount DOUBLE(13, 2), #折扣
    personpercent  DOUBLE(13, 2), #人均
    tablecount   INT DEFAULT 0 #桌数
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  #循环计算所需的数据
  WHILE v_loop_num > 0
  DO

    #计算应收
    SELECT ifnull(sum(a.orignalprice * a.dishnum), 0)
    INTO
      v_shouldamount
    FROM
      t_temp_order_detail a left join t_temp_order b on a.orderid = b.orderid
    WHERE
      b.begintime BETWEEN v_date_start AND v_date_interval;

    #计算实收（含虚增）
    SELECT ifnull(sum(payamount), 0)
    INTO
      v_paidinamount
    FROM
      t_temp_settlement_detail
    WHERE
      begintime BETWEEN v_date_start AND v_date_interval;

    #计算虚增
    SELECT ifnull(sum(Inflated), 0)
    INTO
      v_inflated
    FROM
      t_temp_order_member
    WHERE
      begintime BETWEEN v_date_start AND v_date_interval;

    #设置订单数和人数
    SELECT IFNULL(count(orderid),0) #堂吃的订单数量
         , IFNULL(sum(womanNum + childNum + mannum),0) #堂吃的就餐总人数
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

  #返回结果集
  SELECT *
  FROM
    t_temp_res;

#清空内存表
--   DROP TEMPORARY TABLE IF EXISTS t_temp_order;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_res;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_keys;

END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_yyfx_yhhdtj$$
CREATE PROCEDURE p_report_yyfx_yhhdtj(IN  pi_branchid INT(11), -- 分店id
                                                IN  pi_xslx     SMALLINT, -- 显示类别 0:日  1:月  2:年
                                                IN  pi_ksrq     DATETIME, -- 开始日期
                                                IN  pi_jsrq     DATETIME, -- 结束日期
                                                IN  pi_tjx      SMALLINT, -- 统计项：0:活动名称  1:活动类别
                                                IN  pi_hdlx     SMALLINT, -- 活动类别
                                                OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '营业分析_优惠活动统计'
label_main:
BEGIN
  -- 返回字段说明如下（4个字段）：
  -- 类别（0:笔数；1:金额；2:应收；3:实收），名称，类型，汇总值，拆分值（格式为：时间,数值|时间,数值|时间,数值|时间,数值|....） 
  --
  -- 返回数据举例（多行数据）：
  -- 0  1元尝鲜 99 100  6.1,15|6.2,13|6.3,8|6.4,0|6.5,14|6.6,7|6.7,9
  -- 0  美团券  99  98  6.1,15|6.2,13|6.3,8|6.4,0|6.5,14|6.6,7|6.7,9
  -- 1  1元尝鲜 99 1806  6.1,151|6.2,131|6.3,81|6.4,10|6.5,141|6.6,71|6.7,91
  -- 1  美团券  99 7896  6.1,15|6.2,132|6.3,82|6.4,20|6.5,214|6.6,72|6.7,92
  -- 2  1元尝鲜 99  10000  6.1,15|6.2,1300|6.3,800|6.4,100|6.5,1400|6.6,700|6.7,900
  -- 2  美团券  99 20000  6.1,15|6.2,1300|6.3,899|6.4,299|6.5,1499|6.6,799|6.7,999
  -- 3  1元尝鲜 99 8888  6.1,15|6.2,13|6.3,8|6.4,0|6.5,14|6.6,7|6.7,9
  -- 3  美团券  99 9999  6.1,15|6.2,13|6.3,8|6.4,0|6.5,14|6.6,7|6.7,9

  DECLARE v_date_start    DATETIME;
  DECLARE v_date_end      DATETIME;
  DECLARE v_date_interval DATETIME; #时间间隔  
  DECLARE v_statistictime VARCHAR(15); #统计日期
  DECLARE v_loop_num      INT DEFAULT 0; #根据开始结束时间和显示类型，来设置循环次数
  DECLARE v_fetch_done    BOOL DEFAULT FALSE;
  DECLARE v_pname         VARCHAR(128); #活动名称
  DECLARE v_ptype         CHAR(4); #活动分类编码
  DECLARE v_pcount        INT UNSIGNED; #活动发生笔数
  DECLARE v_pamount       DOUBLE(13, 2); #活动发生金额
  DECLARE v_shouldamount  DOUBLE(13, 2); #活动拉动应收
  DECLARE v_paidinacount  DOUBLE(13, 2); #活动拉动实收（含虚增）
  DECLARE v_inflated      DOUBLE(13, 2); #虚增量
  DECLARE v_showtype      TINYINT; #统计项（0:笔数；1:金额；2:应收；3:实收）
  DECLARE v_value_detail  VARCHAR(1000);

  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; #当读到数据的最后一条时,设置v_fetch_done变量为TRUE
  END;

  -- 异常处理模块，出现异常返回null
  -- DECLARE EXIT HANDLER FOR SQLEXCEPTION
  --	BEGIN
  -- ELECT NULL;
  -- GET DIAGNOSTICS CONDITION 1 po_errmsg = MESSAGE_TEXT;
  -- END;

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

  #设置临时内存表
  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;

  #设置循环次数,处理开始结算时间
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


  #创建订单临时内存表
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
    AND begintime BETWEEN v_date_start AND v_date_end -- 需要创建索引IX_t_order_begintime  
    AND orderstatus = 3;

  #为订单内存表创建索引
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);

  #创建订单详情内存表
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

  #向临时订单详情内存表中放值
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
   --  计算套餐金额结束 

  # 删除套餐明细
  delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

  CREATE INDEX ix_t_temp_order_detail_orderid ON t_temp_order_detail (orderid);


  #创建结算明细内存表
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

  #生产临时结算明细表数据
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
  #创建查询索引


  #创建实收明细内存表
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

  #创建会员消费内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  CREATE TEMPORARY TABLE t_temp_order_member
  (
    orderid VARCHAR(50),
    Inflated DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  #生成临时会员结算数据  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid;
  CREATE INDEX ix_t_temp_order_member_orderid ON t_temp_order_member (orderid);


  #创建优惠活动内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_preferential;
  CREATE TEMPORARY TABLE t_temp_preferential
  (
    pid VARCHAR(50),
    pname VARCHAR(128),
    ptype CHAR(4),
    ptypename VARCHAR(32)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  #生成正常的优惠券数据，包括将其他优惠中的具体原因和合作单位也看成单独的优惠
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

  ##############################################生成新的结算明细表####################################
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new;
  CREATE TEMPORARY TABLE t_temp_settlement_new
  (
    orderid VARCHAR(50),
    payway INT, #结算方式
    payamount DOUBLE(13, 2), #结算金额
    couponNum INT, #使用数量
    pname VARCHAR(128), #活动名称
    ptype CHAR(4), #活动分类
    begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_sub;
  CREATE TEMPORARY TABLE t_temp_settlement_sub
  (
    orderid VARCHAR(50),
    couponNum INT, #使用数量
    pname VARCHAR(128) #活动名称
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;


  #生成挂账和优免数据（不包含金总挂账数据）
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


  #由于使用一张团购券，会产生2条结算明细（1条挂账，1条优免），需要处理成1条，统计团购券的使用数量时，才可能正确
  UPDATE t_temp_settlement_new a, t_temp_settlement_sub b
  SET
    a.couponNum = 0
  WHERE
    a.orderid = b.orderid
    AND a.pname = b.pname
    AND a.couponNum = b.couponNum
    AND a.payway = 5;


  #生成金总挂账优免数据
  --   INSERT INTO t_temp_settlement_new
  --   SELECT a.orderid
  --        , 6
  --        , a.payamount
  --        , 1
  --        , '金总挂账'
  --        , '0601'
  --        , a.begintime
  --   FROM
  --     t_temp_settlement_detail a
  --   WHERE
  --     a.payway = 13;

  #生成雅座优惠券数据
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


  ##########################################根据在界面选择的时间单位，生成新中间临时数据####################################

  #创建中间临时表
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_new_sub;
  CREATE TEMPORARY TABLE t_temp_settlement_new_sub
  (
    orderid VARCHAR(50),
    payway INT, #结算方式
    payamount DOUBLE(13, 2), #结算金额
    couponNum INT, #使用数量
    pname VARCHAR(128), #活动名称
    ptype CHAR(4), #活动分类
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

  #生成应收表
  DROP TEMPORARY TABLE IF EXISTS t_temp_shouldamount;
  CREATE TEMPORARY TABLE t_temp_shouldamount
  (
    statistictime VARCHAR(20),
    pname VARCHAR(50),
    shouldamount DOUBLE(13, 2) #应收
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid;
  CREATE TEMPORARY TABLE t_temp_orderid
  (
    orderid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  #循环计算所需的数据
  WHILE v_loop_num > 0
  DO

    #清空所有的中间临时小表
    TRUNCATE t_temp_settlement_new_sub;

    #把t_temp_settlement_new拆分成一个个字表，加快循环速度
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

        #计算应收
        SELECT ifnull(sum(a.orignalprice * a.dishnum), 0)
        INTO
          v_shouldamount
        FROM
          t_temp_order_detail a, t_temp_orderid b
        WHERE
          a.orderid = b.orderid;


        #计算实收（含虚增）
        SELECT ifnull(sum(payamount), 0)
        INTO
          v_paidinacount
        FROM
          t_temp_settlement_paidinamount a, t_temp_orderid b
        WHERE
          a.orderid = b.orderid;


        #计算虚增
        SELECT ifnull(sum(Inflated), 0)
        INTO
          v_inflated
        FROM
          t_temp_order_member a, t_temp_orderid b
        WHERE
          a.orderid = b.orderid;

        INSERT INTO t_temp_res_detail VALUE (v_statistictime, v_pname, v_ptype, v_pcount, v_pamount, v_shouldamount, v_paidinacount - v_inflated);

      END LOOP;

      #最后关闭游标.
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

  #创建结果内存表
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


  #按活动名称来统计
  IF pi_tjx = 0 THEN

    #发生笔数
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

    #发生金额
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

    #拉动应收
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

    #拉动实收
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
  #按活动类型来统计
  ELSE
    #发生笔数
    INSERT INTO t_temp_res (showtype, pname, total_num)
    SELECT 0
         , ptype
         , sum(pcount)
    FROM
      t_temp_res_detail
    GROUP BY
      ptype;

    #发生金额
    INSERT INTO t_temp_res (showtype, pname, total_num)
    SELECT 1
         , ptype
         , sum(pamount)
    FROM
      t_temp_res_detail
    GROUP BY
      ptype;

    #拉动应收
    INSERT INTO t_temp_res (showtype, pname, total_num)
    SELECT 2
         , ptype
         , sum(pshouldamount)
    FROM
      t_temp_res_detail
    GROUP BY
      ptype;

    #拉动实收
    INSERT INTO t_temp_res (showtype, pname, total_num)
    SELECT 3
         , ptype
         , sum(ppaidinamount)
    FROM
      t_temp_res_detail
    GROUP BY
      ptype;
  END IF;


  #组装每天（每月）拆分的值
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

    #循环结束标识
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



    #设置拆分值
    UPDATE t_temp_res
    SET
      detail_num = v_value_detail
    WHERE
      id = v_loop_num;
    SET v_loop_num = v_loop_num + 1;
  END LOOP;

  #如果统计的活动类型，还需要将活动类型编码转化为活动类型名称
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
  #返回结果
  ELSE
    SELECT showtype
         , pname
         , ptype
         , total_num
         , detail_num
    FROM
      t_temp_res;
  END IF;

  #清空内存表
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



END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_yyfx_pxxstj_column$$
CREATE PROCEDURE p_report_yyfx_pxxstj_column(IN  pi_branchid INT(11), 
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

  
 -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;

   --  计算套餐金额结束 
  -- 删除套餐数据
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










END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_yyfx_pxxstj$$
CREATE PROCEDURE p_report_yyfx_pxxstj(IN  pi_branchid INT(11), 
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


 -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
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










END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_yhhdmxb_zhixiang$$
CREATE PROCEDURE p_report_yhhdmxb_zhixiang(IN  pi_branchid INT(11), 
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

   -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
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
















END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_yhhdmxb$$
CREATE PROCEDURE p_report_yhhdmxb(IN  pi_branchid INT(11), -- 分店id
                                  IN  pi_sb       SMALLINT, -- 市别，0:午市；1:晚市；-1:全天
                                  IN  pi_ksrq     DATETIME, -- 开始日期，
                                  IN  pi_jsrq     DATETIME, -- 结束日期
                                  IN  pi_hdmc     VARCHAR(50), -- 活动名称 -1:全部  非-1:对应的活动名称
                                  IN  pi_jsfs     INT, -- 计算方式 -1:全部  5:挂账 6:优免
                                  IN  pi_hdlx     VARCHAR(10), -- 活动类型 -1:全部 99:雅座优惠券 其他值:活动类型code
                                  OUT po_errmsg   VARCHAR(100))
    SQL SECURITY INVOKER
    COMMENT '优惠活动明细表'
label_main:
BEGIN
  -- 返回字段说明如下(7个字段)：
  -- 活动名称	结算方式 活动类型 发生笔数 发生金额 拉动应收 拉动实收
  --
  -- 返回数据举例（返回值包含多条数据）：
  -- 点评88抵100 挂账 团购券 3 264 550 505
  -- 美团40元鱼券 挂账 团购券 4 364 650 605

  DECLARE v_date_start   DATETIME;
  DECLARE v_date_end     DATETIME;
  DECLARE v_loop_index   INT;
  DECLARE v_pname        VARCHAR(50);
  DECLARE v_ptype        CHAR(4);
  DECLARE v_payway       INT;
  DECLARE v_shouldamount DOUBLE(13, 2);
  DECLARE v_paidinamount DOUBLE(13, 2);
  DECLARE v_inflated     DOUBLE(13, 2); #虚增
  -- 异常处理模块，出现异常返回null
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SELECT NULL;
  -- GET DIAGNOSTICS CONDITION 1 po_errmsg = MESSAGE_TEXT;
  END;

  IF pi_branchid IS NULL THEN
    SELECT NULL;
    SET po_errmsg = '分店ID输入不能为空';
    LEAVE label_main;
  END IF;

  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;

  #处理开始结束时间
  SET v_date_start = pi_ksrq; # str_to_date(concat(date_format(pi_ksrq, '%Y-%m-%d'), '00:00:00'), '%Y-%m-%d %H:%i:%s');
  SET v_date_end = pi_jsrq; #str_to_date(concat(date_format(pi_jsrq, '%Y-%m-%d'), '23:59:59'), '%Y-%m-%d %H:%i:%s');

  #创建订单临时内存表
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
      AND begintime BETWEEN v_date_start AND v_date_end -- 需要创建索引IX_t_order_begintime  
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
      AND begintime BETWEEN v_date_start AND v_date_end -- 需要创建索引IX_t_order_begintime  
      AND orderstatus = 3;
  END IF;
  #为订单内存表创建索引
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);

  #创建订单详情内存表
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

  #向临时订单详情内存表中放值
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

  -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

  CREATE INDEX ix_t_temp_order_detail_orderid ON t_temp_order_detail (orderid);

  #创建结算明细内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
  CREATE TEMPORARY TABLE t_temp_settlement_detail
  (
    orderid VARCHAR(50),
    payway INT,
    singular INT, #使用的单数
    perCapita DOUBLE(13, 2), #人均
    payamount DOUBLE(13, 2),
    couponNum INT,
    bankcardno VARCHAR(50),
    coupondetailid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  #生产临时结算明细表数据
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
  #创建查询索引
  CREATE INDEX ix_t_temp_settlement_detail_payway ON t_temp_settlement_detail (payway);


  #创建结算明细内存表(只有实收数据)
  DROP TEMPORARY TABLE IF EXISTS t_temp_paidinamout;
  CREATE TEMPORARY TABLE t_temp_paidinamout
  (
    orderid VARCHAR(50),
    payamount DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  #生产临时结算明细表数据
  INSERT INTO t_temp_paidinamout
  SELECT orderid
       , payamount
  FROM
    t_temp_settlement_detail
  WHERE
    payway IN (0, 1, 5, 8, 17, 18 ,13);
  CREATE INDEX ix_t_temp_paidinamout_orderid ON t_temp_paidinamout (orderid);


  #创建会员消费内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  CREATE TEMPORARY TABLE t_temp_order_member
  (
    orderid VARCHAR(50),
    Inflated DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  #生成临时会员结算数据  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid;
  CREATE INDEX ix_t_temp_order_member_orderid ON t_temp_order_member (orderid);


  #创建优惠活动内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_preferential;
  CREATE TEMPORARY TABLE t_temp_preferential
  (
    pid VARCHAR(50),
    pname VARCHAR(128),
    ptype CHAR(4),
    ptypename VARCHAR(32)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  #生成正常的优惠券数据，包括将其他优惠中的具体原因和合作单位也看成单独的优惠
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

  ##############################################根据选择，生成中间临时数据####################################
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail;
  CREATE TEMPORARY TABLE t_temp_res_detail
  (
    orderid VARCHAR(50),
    payway INT, #结算方式
    singular INT, #使用的单数
    payamount DOUBLE(13, 2), #结算金额
    couponNum INT, #使用数量
    pname VARCHAR(128), #活动名称
    ptype CHAR(4), #活动分类
    perCapita DOUBLE(13, 2), #人均
    ptypename VARCHAR(32) #活动分类名称
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  #生成挂账和优免数据（不包含金总挂账数据）
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

  --   #生成金总挂账优免数据
  --   INSERT INTO t_temp_res_detail
  --   SELECT a.orderid
  --        , 6
  --        , a.payamount
  --        , 1
  --        , '金总挂账'
  --        , '0601'
  --        , '手工优免'
  --   FROM
  --     t_temp_settlement_detail a
  --   WHERE
  --     a.payway = 13;

  #生成雅座优惠券数据
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

  ##############################################生成最终结果数据####################################
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    pname VARCHAR(128), #活动名称
    ptype CHAR(4), #活动分类
    ptypename VARCHAR(32), #活动分类名称
    payway INT, #结算方式
    paywaydesc VARCHAR(50), #结算方式名称
    couponNum INT, #使用数量
    singular INT, #使用的单数
    payamount DOUBLE(13, 2), #发生金额
    perCapita DOUBLE(13, 2), #人均
    shouldamount DOUBLE(13, 2), #应收
    paidinamount DOUBLE(13, 2), #实收
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


  #获取自增长字段的偏移量和自增量，以及自增字段的最大值
  SET @offset = @@auto_increment_offset;
  SET @increment = @@auto_increment_increment;
  SELECT ifnull(max(id), 0)
  INTO
    @maxid
  FROM
    t_temp_res;

lable_loop:
  LOOP
    #循环结束标识
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

    #获取订单列表
    INSERT INTO t_temp_orderid
    SELECT DISTINCT orderid
    FROM
      t_temp_res_detail
    WHERE
      pname = v_pname
      AND ptype = v_ptype
      AND payway = v_payway;

    #计算拉动应收
    SELECT ifnull(sum(a.orignalprice * a.dishnum), 0)
    INTO
      v_shouldamount
    FROM
      t_temp_order_detail a, t_temp_orderid b
    WHERE
      a.orderid = b.orderid;

    #计算拉动实收(含虚增)
    SELECT ifnull(sum(a.payamount), 0)
    INTO
      v_paidinamount
    FROM
      t_temp_paidinamout a, t_temp_orderid b
    WHERE
      a.orderid = b.orderid;

    #计算虚增
    SELECT ifnull(sum(a.Inflated), 0)
    INTO
      v_inflated
    FROM
      t_temp_order_member a, t_temp_orderid b
    WHERE
      a.orderid = b.orderid;

    #更新结果数据
    UPDATE t_temp_res
    SET
      shouldamount = v_shouldamount, paidinamount = v_paidinamount - v_inflated
    WHERE
      id = @offset;

    #获取下一个自增id值
    SET @offset = @offset + @increment;
  END LOOP;


  #更新支付方式名称
  UPDATE t_temp_res a, t_dictionary b
  SET
    a.paywaydesc = b.itemDesc
  WHERE
    a.payway = b.itemid
    AND type = 'PAYWAY';

  #更新活动类型名称
  UPDATE t_temp_res a, t_temp_preferential b
  SET
    a.ptypename = b.ptypename
  WHERE
    a.ptype = b.ptype;

  #更新雅座优惠券活动类型
  UPDATE t_temp_res a
  SET
    a.ptypename = '雅座优惠券'
  WHERE
    a.ptype = '99';

  #根据用户选择，删除多余的数据
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


  #返回结果集
  SELECT pname
     , #活动名称
       ptype
     , #活动分类编码
       ptypename
     , #活动分类名称
       payway
     , #结算方式编码
       paywaydesc
     , #结算方式名称
       couponNum
     , #使用数量
       payamount
     , #发生金额
       shouldamount
     , #应收
       paidinamount
     #实收
       , round(paidinamount / perCapita, 2) AS perCapita
     , singular
FROM
  t_temp_res;


/*SELECT pname
       , ptype
       , payway
       , ifnull(count(1), 0)
       , ifnull(sum(payamount), 0)
       , ifnull(sum(couponNum), 0)
       , ifnull(sum(perCapita), 0)
  FROM
    t_temp_res_detail
  GROUP BY
    pname
  , ptype
  , payway;*/



#清空内存表
--   DROP TEMPORARY TABLE IF EXISTS t_temp_order;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_order_detail;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_paidinamout;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_preferential;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_res;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_orderid;
--   DROP TEMPORARY TABLE IF EXISTS t_temp_keys;
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_xxsjtjb$$
CREATE PROCEDURE p_report_xxsjtjb(IN  pi_branchid INT(11), 
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

    
   
     -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
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










END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_tcmxb$$
CREATE PROCEDURE p_report_tcmxb(IN  pi_branchid INT(11), 
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







END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_pxxsmxb_zhixiang$$
CREATE PROCEDURE p_report_pxxsmxb_zhixiang(IN  pi_branchid INT(11), 
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
  DECLARE v_total_custnum_count    DOUBLE(13, 2); -- 来客总人数
  DECLARE v_total_shouldmount_count    DOUBLE(13, 2); -- 应收总额
  DECLARE v_canju_mount DOUBLE(13, 2); -- 餐具金额
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

   -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
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







END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_pxxsmxb$$
CREATE PROCEDURE p_report_pxxsmxb(IN  pi_branchid INT(11), 
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
  DECLARE v_total_custnum_count    DOUBLE(13, 2); -- 来客总人数
  DECLARE v_total_shouldmount_count    DOUBLE(13, 2); -- 应收总额
  DECLARE v_canju_mount DOUBLE(13, 2); -- 餐具金额

  
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
  
  -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
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
      INSERT INTO t_temp_res VALUES ('DISHES_98', '餐具', 0, '单品', v_sum,v_sum/v_total_custnum_count*1000,v_canju_mount,v_canju_mount/v_total_shouldmount_count*100,v_sum / v_total_count * 100); -- 字段问题
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







END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_jsfsxxxq$$
CREATE PROCEDURE p_report_jsfsxxxq(IN  pi_branchid INT(11), # 分店id
								  IN  pi_jsfs     INT(11),#结算方式ID
								  IN  pi_hdmc     VARCHAR(50),#活动名称ID
                                  IN  pi_sb       SMALLINT, # 市别，0:午市；1:晚市；-1:全天
                                  IN  pi_ksrq     DATETIME, # 开始日期，
                                  IN  pi_jsrq     DATETIME, # 结束日期
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
  DECLARE v_branchid   INT(11);#当前分店id
  DECLARE v_branchname VARCHAR(50);#当前分店名称
  DECLARE v_brandid  VARCHAR(128); #品牌
  DECLARE v_marketid  VARCHAR(128); #市场
  DECLARE v_areaid  VARCHAR(128); #区域
  DECLARE v_jde  VARCHAR(128); #jde
  DECLARE v_brand_name  VARCHAR(128); #品牌
  DECLARE v_market_name  VARCHAR(128); #市场
  DECLARE v_area_name  VARCHAR(128); #区域
  DECLARE v_datetime  VARCHAR(128); #日期
  
  DECLARE v_pname        VARCHAR(50);#活动名称
  DECLARE v_ptype        CHAR(4);#活动类型
  DECLARE v_payway       INT;#支付方式
  DECLARE v_payways       INT;#支付方式数量
  DECLARE v_shouldamount DOUBLE(13, 2);#拉动应收
  DECLARE v_paidinamount DOUBLE(13, 2);#拉动实收
  DECLARE v_inflated     DOUBLE(13, 2); #虚增
  
  DECLARE v_fetch_done          BOOL DEFAULT FALSE; #游标结束标识
  
  #定义一个优惠活动的游标
  DECLARE cur_p_detail CURSOR FOR SELECT DISTINCT pname
                                                , ptype
                                                , payway
                                  FROM
                                    t_temp_res_detail;

  # 异常处理模块，出现异常返回null
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SELECT NULL;
    GET DIAGNOSTICS CONDITION 1 po_errormsg = MESSAGE_TEXT;
  END;
  
  #游标结束时的处理程序
  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; #当读到数据的最后一条时,设置v_fetch_done变量为TRUE
  END;
  
     #设置当前分店id
  IF pi_branchid IS NULL THEN
    SELECT NULL;
    SET po_errormsg = '分店ID输入不能为空';
    LEAVE label_main;
  END IF;
  
  IF pi_brandid > -1 THEN
  #设置当前分店id
	SELECT id,name INTO v_brandid,v_brand_name  FROM t_c_brand WHERE id = pi_brandid;
  ELSE
	SELECT id,name INTO v_brandid,v_brand_name FROM t_c_brand LIMIT 1;
  END IF;  
  
  IF pi_marketid > -1 THEN
  #设置当前分店id
	SELECT id,name INTO v_marketid,v_market_name  FROM t_c_market WHERE id = pi_marketid;
  ELSE
	SELECT id,name INTO v_marketid,v_market_name FROM t_c_market LIMIT 1;
  END IF;  
  
  IF pi_areaid > -1 THEN
  #设置当前分店id
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

  #处理开始结束时间
  SET v_date_start = pi_ksrq;
  SET v_date_end = pi_jsrq;

  SELECT branchname into v_branchname 
  FROM t_branch WHERE branchid = v_branchid;
 
  #创建订单临时内存表
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
      AND begintime BETWEEN v_date_start AND v_date_end # 需要创建索引IX_t_order_begintime  
      AND shiftid = pi_sb
      AND orderstatus = 3;
  ELSE
    INSERT INTO t_temp_order
    SELECT orderid,begintime
    FROM
      t_order USE INDEX (IX_t_order_begintime)
    WHERE
      v_branchid = t_order.branchid
      AND begintime BETWEEN v_date_start AND v_date_end # 需要创建索引IX_t_order_begintime  
      AND orderstatus = 3;
  END IF;

  #为订单内存表创建索引
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);

  #创建订单详情内存表
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

  #向临时订单详情内存表中放值
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
	
 
 -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;
  

  CREATE INDEX ix_t_temp_order_detail_orderid ON t_temp_order_detail (orderid);
  
  #创建结算明细内存表
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

  #生产临时结算明细表数据
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
  #创建查询索引
  CREATE INDEX ix_t_temp_settlement_detail_payway ON t_temp_settlement_detail (payway);  
 
  #创建会员消费内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  CREATE TEMPORARY TABLE t_temp_order_member
  (
    orderid VARCHAR(50),
    Inflated DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  #生成临时会员结算数据  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid;
  CREATE INDEX ix_t_temp_order_member_orderid ON t_temp_order_member (orderid);


  #创建优惠活动内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_preferential;
  CREATE TEMPORARY TABLE t_temp_preferential
  (
    pid VARCHAR(50),
    pname VARCHAR(128),
    ptype CHAR(4),
    ptypename VARCHAR(32)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  #生成正常的优惠券数据，包括将其他优惠中的具体原因和合作单位也看成单独的优惠
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
    payway INT, #结算方式
    payamount DOUBLE(13, 2), #结算金额
    couponNum INT, #使用数量
    pname VARCHAR(128), #活动名称
    ptype CHAR(4), #活动分类
    ptypename VARCHAR(32), #活动分类名称
	begintime DATETIME
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  #生成挂账和优免数据（不包含金总挂账数据）
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

  #生成雅座优惠券数据
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
  ##############################################删除多余数据#######################################
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
  
  #结果集合
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
	jde VARCHAR(50),#jde
	branchid VARCHAR(50),#分店id
	branchname VARCHAR(50),#分店名称
    pname VARCHAR(128), #活动名称
    orderid VARCHAR(50), #订单id
    ptype CHAR(4), #活动分类
    ptypename VARCHAR(32), #活动分类名称
    couponNum INT, #笔数
    payamount decimal(13, 2), #发生金额
    shouldamount decimal(13, 2), #拉动应收
    paidinamount decimal(13, 2), #拉动实收
    PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
 
  
  #遍历分店
  BEGIN
			#开始统计每条优惠的数据
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

			  #最后关闭游标.
			  CLOSE cur_p_detail;
			  
		    #生成优惠对应的订单号的内存表
			  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid;
			  CREATE TEMPORARY TABLE t_temp_orderid
			  (
				orderid VARCHAR(50)
			  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

			  INSERT INTO t_temp_orderid
			  SELECT DISTINCT orderid
			  FROM
				t_temp_res;

			  #生成应收表
			  DROP TEMPORARY TABLE IF EXISTS t_temp_shouldamount;
			  CREATE TEMPORARY TABLE t_temp_shouldamount
			  (
				orderid VARCHAR(50),
				shouldamount DOUBLE(13, 2) #应收
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

			  #生成虚增表
			  DROP TEMPORARY TABLE IF EXISTS t_temp_inflated;
			  CREATE TEMPORARY TABLE t_temp_inflated
			  (
				orderid VARCHAR(50),
				inflated DOUBLE(13, 2) #虚增
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

			  #生成实收表
			  DROP TEMPORARY TABLE IF EXISTS t_temp_paidinamount;
			  CREATE TEMPORARY TABLE t_temp_paidinamount
			  (
				orderid VARCHAR(50),
				paidinamount DOUBLE(13, 2) #实收
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

			  #更新实收表
			  UPDATE t_temp_paidinamount a, t_temp_inflated b
			  SET
				a.paidinamount = a.paidinamount - b.inflated
			  WHERE
				a.orderid = b.orderid;

			  #更新结果集（分店）
			  UPDATE t_temp_res
			  SET branchid = v_branchid,branchname = v_branchname,
				brandid=v_brandid,brandname=v_brand_name,marketid=v_marketid,market=v_market_name,areaid=v_areaid,
			    area=v_area_name,managePattern='1',managePatternid ='自营',jde=v_jde;
			
			  #更新结果集（应收）
			  UPDATE t_temp_res a, t_temp_shouldamount b
			  SET
				a.shouldamount = b.shouldamount
			  WHERE
				a.orderid = b.orderid;

			  #更新结果集（实收）
			  UPDATE t_temp_res a, t_temp_paidinamount b
			  SET
				a.paidinamount = b.paidinamount
			  WHERE
				a.orderid = b.orderid;

		  #更新活动类型名称
		  UPDATE t_temp_res a, t_temp_preferential b
		  SET
			a.ptypename = b.ptypename
		  WHERE
			a.ptype = b.ptype;

		  #更新雅座优惠券活动类型
		  UPDATE t_temp_res a
		  SET
			a.ptypename = '雅座优惠券'
		  WHERE
			a.ptype = '99';
		  
  END;
  
  #返回结果集
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
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_jsfsxx$$
CREATE PROCEDURE p_report_jsfsxx(IN  pi_branchid VARCHAR(50), # 分店id
								  IN  pi_jsfs     INT(11),#结算方式ID
								  IN  pi_hdmc     VARCHAR(50),#活动名称ID
                                  IN  pi_sb       SMALLINT, # 市别，0:午市；1:晚市；-1:全天
                                  IN  pi_ksrq     DATETIME, # 开始日期，
                                  IN  pi_jsrq     DATETIME, # 结束日期
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
  DECLARE v_branchid   VARCHAR(50) DEFAULT NULL;#当前分店id
  DECLARE v_branchid_cur   VARCHAR(50);#当前遍历分店id
  DECLARE v_branchname_cur   VARCHAR(50);#当前遍历分店name
  DECLARE v_brandid  VARCHAR(128); #品牌
  DECLARE v_marketid  VARCHAR(128); #市场
  DECLARE v_areaid  VARCHAR(128); #区域
  DECLARE v_jde  VARCHAR(128); #jde
  DECLARE v_brand_name  VARCHAR(128); #品牌
  DECLARE v_market_name  VARCHAR(128); #市场
  DECLARE v_area_name  VARCHAR(128); #区域
  DECLARE v_datetime  VARCHAR(128); #日期
  DECLARE v_hdmc  VARCHAR(50); #活动名称
  
  
  DECLARE v_pname        VARCHAR(50);#活动名称
  DECLARE v_ptype        CHAR(4);#活动类型
  DECLARE v_payway       INT;#支付方式
  DECLARE v_payways       INT;#支付方式数量
  DECLARE v_shouldamount decimal(13, 2);#拉动应收
  DECLARE v_paidinamount decimal(13, 2);#拉动实收
  DECLARE v_inflated     decimal(13, 2); #虚增
  DECLARE v_shouldamount_total     decimal(13, 2); #总应收数
  DECLARE v_paidinamount_total     decimal(13, 2); #总实收数
  DECLARE v_payways_total     	   decimal(13, 2); #总支付方式数
  DECLARE v_inflated_total     	   decimal(13, 2); #总虚增数
  
  DECLARE v_fetch_done          BOOL DEFAULT FALSE; #游标结束标识
  
  DECLARE v_paywaypercent decimal(5, 2); #笔数比
  DECLARE v_shouldpercent decimal(5, 2); #应收比
  DECLARE v_paidinpercent decimal(5, 2); #实收比
  
  #定义一个优惠活动的游标
  DECLARE cur_p_detail CURSOR FOR SELECT DISTINCT pname
                                                , ptype
                                                , payway
                                  FROM
                                    t_temp_res_detail_sub;

  # 异常处理模块，出现异常返回null
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SELECT NULL;
    GET DIAGNOSTICS CONDITION 1 po_errormsg = MESSAGE_TEXT;
  END;
  
    #游标结束时的处理程序
  DECLARE CONTINUE HANDLER FOR NOT FOUND
  BEGIN
    SET v_fetch_done = TRUE; #当读到数据的最后一条时,设置v_fetch_done变量为TRUE
  END;
  
  IF pi_brandid > -1 THEN
  #设置当前分店id
	SELECT id,name INTO v_brandid,v_brand_name  FROM t_c_brand WHERE id = pi_brandid;
  ELSE
	SELECT id,name INTO v_brandid,v_brand_name FROM t_c_brand LIMIT 1;
  END IF;  
  
  IF pi_marketid > -1 THEN
  #设置当前分店id
	SELECT id,name INTO v_marketid,v_market_name  FROM t_c_market WHERE id = pi_marketid;
  ELSE
	SELECT id,name INTO v_marketid,v_market_name FROM t_c_market LIMIT 1;
  END IF;  
  
  IF pi_areaid > -1 THEN
  #设置当前分店id
	SELECT id,name INTO v_areaid,v_area_name  FROM t_c_area WHERE id = pi_areaid;
  ELSE
	SELECT id,name INTO v_areaid,v_area_name FROM t_c_area LIMIT 1;
  END IF; 

  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;

  #处理开始结束时间
  SET v_date_start = pi_ksrq;
  SET v_date_end = pi_jsrq;
  
  DROP TEMPORARY TABLE IF EXISTS t_temp_branch;
  CREATE TEMPORARY TABLE t_temp_branch
  (
	branchid VARCHAR(50),
    branchname VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  #为分店内存表创建索引
  CREATE UNIQUE INDEX ix_t_temp_branch_branchid ON t_temp_branch (branchid);  
  
   IF pi_branchid > -1 THEN
	#设置当前分店id
	INSERT INTO t_temp_branch SELECT branchid,branchname  FROM t_branch WHERE branchid =CONCAT(pi_branchid) OR branchname LIKE CONCAT('%',pi_branchid,'%'); 
	SELECT ifnull(COUNT(1),0) INTO v_branchid FROM t_temp_branch;
	IF v_branchid = 0 THEN 
		SELECT NULL;
		SET po_errormsg = '分店ID不存在';
		LEAVE label_main;
	END IF;
  END IF;
  
  #生成要统计的分店信息
  IF v_branchid IS NULL THEN 
	  INSERT INTO t_temp_branch
	  SELECT branchid
		   , branchname
	  FROM
		t_branch;
  END IF;
 
  #创建订单临时内存表
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
      AND begintime BETWEEN v_date_start AND v_date_end # 需要创建索引IX_t_order_begintime  
      AND shiftid = pi_sb
      AND orderstatus = 3;
  ELSE
    INSERT INTO t_temp_order
    SELECT orderid,t_temp_branch.branchid,begintime
    FROM
      t_order USE INDEX (IX_t_order_begintime), t_temp_branch
    WHERE
      t_temp_branch.branchid = t_order.branchid
      AND begintime BETWEEN v_date_start AND v_date_end # 需要创建索引IX_t_order_begintime  
      AND orderstatus = 3;
  END IF;

  #为订单内存表创建索引
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);

  #创建订单详情内存表
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

  #向临时订单详情内存表中放值
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
	
  
 -- 计算套餐金额开始
   DROP TEMPORARY TABLE IF EXISTS t_temp_taocan;
   CREATE TEMPORARY TABLE t_temp_taocan
  (
    primarykey VARCHAR(50),
    orignalprice DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  INSERT INTO t_temp_taocan select superkey,sum(dishnum*orignalprice) from t_temp_order_detail  where dishtype = 2 and superkey <> primarykey group by superkey;
  update t_temp_order_detail d,t_temp_taocan c set d.orignalprice = c.orignalprice  where c.primarykey = d.primarykey;
   --  计算套餐金额结束 

  # 删除套餐明细
   delete from t_temp_order_detail where dishtype =2 and superkey <> primarykey;

  CREATE INDEX ix_t_temp_order_detail_orderid ON t_temp_order_detail (orderid);
  
  #创建结算明细内存表
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

  #生产临时结算明细表数据
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
  #创建查询索引
  CREATE INDEX ix_t_temp_settlement_detail_payway ON t_temp_settlement_detail (payway);  

  #创建会员消费内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_order_member;
  CREATE TEMPORARY TABLE t_temp_order_member
  (
    orderid VARCHAR(50),
    Inflated DOUBLE(13, 2)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;

  #生成临时会员结算数据  
  INSERT INTO t_temp_order_member
  SELECT b.orderid
       , b.Inflated
  FROM
    t_temp_order a, t_order_member b
  WHERE
    a.orderid = b.orderid;
  CREATE INDEX ix_t_temp_order_member_orderid ON t_temp_order_member (orderid);

  #创建优惠活动内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_preferential;
  CREATE TEMPORARY TABLE t_temp_preferential
  (
    pid VARCHAR(50),
    pname VARCHAR(128),
    ptype CHAR(4),
    ptypename VARCHAR(32)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  #生成正常的优惠券数据，包括将其他优惠中的具体原因和合作单位也看成单独的优惠
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
    payway INT, #结算方式
    payamount DOUBLE(13, 2), #结算金额
    couponNum INT, #使用数量
    pname VARCHAR(128), #活动名称
    ptype CHAR(4), #活动分类
    ptypename VARCHAR(32) #活动分类名称
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  #生成挂账和优免数据（不包含挂账2数据）
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

  #生成雅座优惠券数据
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
  
  ########################################生成中间表,加快循环速度##############################################
  #创建订单id临时表
  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid;
  CREATE TEMPORARY TABLE t_temp_orderid
  (
	orderid VARCHAR(50)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8 MAX_ROWS = 1000000;
  
  CREATE INDEX ix_t_temp_orderid_orderid ON t_temp_orderid (orderid);
  
  #创建订单详情临时内存表
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
  
  #创建订单详情临时内存表
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
  
    #创建结算明细临时内存表
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

  #创建查询索引
  CREATE INDEX ix_t_temp_settlement_detail_sub_payway ON t_temp_settlement_detail_sub (payway); 
  #创建查询索引
  CREATE INDEX ix_t_temp_settlement_detail_sub_orderid ON t_temp_settlement_detail_sub (orderid);  
  
    #创建结算明细临时内存表
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
  #创建查询索引
  CREATE INDEX ix_t_temp_settlement_detail_sub_new_payway ON t_temp_settlement_detail_sub_new (payway); 
  #创建查询索引
  CREATE INDEX ix_t_temp_settlement_detail_sub_new_orderid ON t_temp_settlement_detail_sub_new (orderid);   
  
  
  #创建优惠活动临时内存表
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_detail_sub;
  CREATE TEMPORARY TABLE t_temp_res_detail_sub
  (
    orderid VARCHAR(50),
    payway INT, #结算方式
    payamount DOUBLE(13, 2), #结算金额
    couponNum INT, #使用数量
    pname VARCHAR(128), #活动名称
    ptype CHAR(4), #活动分类
    ptypename VARCHAR(32) #活动分类名称
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  CREATE INDEX ix_t_temp_res_detail_sub ON t_temp_res_detail_sub (pname, ptype, payway);
  
  #结果集合
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
	jde VARCHAR(50),#jde
	branchid VARCHAR(50),#分店id
	branchname VARCHAR(50),#分店名称
    pname VARCHAR(128), #活动名称
    ptype CHAR(4), #活动分类
    ptypename VARCHAR(32), #活动分类名称
    payway INT, #结算方式
    paywaydesc VARCHAR(50), #结算方式名称
    couponNum INT, #笔数
    payamount decimal(13, 2), #发生金额
    shouldamount decimal(13, 2)DEFAULT 0.00, #拉动应收
    paidinamount decimal(13, 2)DEFAULT 0.00, #拉动实收
	paywaypercent decimal(5, 2)DEFAULT 0.00, #笔数比
	shouldpercent decimal(5, 2)DEFAULT 0.00, #应收比
	paidinpercent decimal(5, 2) DEFAULT 0.00, #实收比
    PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
  
  #结果临时集合
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
	jde VARCHAR(50),#jde
	branchid VARCHAR(50),#分店id
	branchname VARCHAR(50),#分店名称
    pname VARCHAR(128), #活动名称
    ptype CHAR(4), #活动分类
    ptypename VARCHAR(32), #活动分类名称
    payway INT, #结算方式
    paywaydesc VARCHAR(50), #结算方式名称
    couponNum INT, #笔数
    payamount decimal(13, 2), #发生金额
    shouldamount decimal(13, 2) DEFAULT 0.00, #拉动应收
    paidinamount decimal(13, 2) DEFAULT 0.00, #拉动实收
	paywaypercent decimal(5, 2) DEFAULT 0.00, #笔数比
	shouldpercent decimal(5, 2) DEFAULT 0.00, #应收比
	paidinpercent decimal(5, 2) DEFAULT 0.00, #实收比
    PRIMARY KEY (id)
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;
 
  BEGIN
	DECLARE cur_branchid CURSOR FOR SELECT branchid,branchname FROM t_temp_branch;
	
	OPEN cur_branchid;
	
	#设置统计时间
	IF DATEDIFF(v_date_end,v_date_start) > 0 THEN 
		SET v_datetime = concat(date_format(v_date_start,'%Y-%m-%d'),' - ',date_format(v_date_end,'%Y-%m-%d'));
	ELSE
		SET v_datetime = concat(date_format(v_date_start,'%Y-%m-%d'));
	END IF;
	
	#遍历分店
	lable_fetch_branchid_loop:
		LOOP
		
			FETCH cur_branchid INTO v_branchid_cur,v_branchname_cur;
			IF v_fetch_done THEN
				  LEAVE lable_fetch_branchid_loop;
				END IF;
				
			#生成JDE
			SELECT ifnull(branch_id,0)
			INTO v_jde
			FROM t_branch_code a WHERE a.branch_id_code = v_branchid_cur;
				
			#清空订单Id临时内存表
			TRUNCATE t_temp_orderid;
			
			#生成订单id临时数据
			INSERT INTO t_temp_orderid
			SELECT a.orderid  FROM t_temp_order a
			WHERE a.branchid = v_branchid_cur;
			
			#################################清空中间表########################################
			
			#包含该分店所有的订单信息	
			TRUNCATE t_temp_order_detail_sub;
			
			INSERT INTO t_temp_order_detail_sub
				SELECT a.* FROM t_temp_order_detail a,t_temp_orderid b
				WHERE a.orderid = b.orderid;

			#包含该分店所有的结算信息	
			TRUNCATE t_temp_settlement_detail_sub;
			
			INSERT INTO t_temp_settlement_detail_sub
				SELECT a.* FROM t_temp_settlement_detail a,t_temp_orderid b
				WHERE a.orderid = b.orderid;
				
			#包含该分店所有的优惠活动信息				
			TRUNCATE t_temp_res_detail_sub;

			INSERT INTO t_temp_res_detail_sub
				SELECT a.* FROM t_temp_res_detail a,t_temp_orderid b
				WHERE a.orderid = b.orderid;
			
			#清空临时订单表
			TRUNCATE t_temp_orderid;
			
			INSERT INTO t_temp_orderid 
				SELECT DISTINCT orderid
				FROM t_temp_res_detail_sub;
				
			#计算应收
			SELECT ifnull(sum(a.orignalprice * a.dishnum), 0)
			INTO
			  v_shouldamount_total
			FROM
			  t_temp_order_detail_sub a , t_temp_orderid b
			WHERE
			  a.orderid = b.orderid;
					  
			#计算拉动实收(含虚增)
			SELECT ifnull(sum(a.payamount), 0)
			INTO
				v_paidinamount_total
			FROM
				t_temp_settlement_detail_sub a,t_temp_orderid b
			WHERE
				a.orderid = b.orderid
			AND
				a.payway IN (0, 1, 5, 8, 13, 17, 18); #增加挂账2 标记13
			  
			#计算虚增
			SELECT ifnull(sum(a.Inflated), 0)
			INTO
			  v_inflated_total
			FROM
			  t_temp_order_member a, t_temp_orderid b
			WHERE
			  a.orderid = b.orderid;
			  
			#计算总优惠活动结算单数
			SELECT ifnull(count(DISTINCT orderid ),0)
			INTO 
				v_payways_total
			FROM
				t_temp_orderid a;
			
			TRUNCATE t_temp_res_sub;
			
			##################根据筛选条件(活动名称,结算方式),只在统计活动中删除多余数据###################
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
			
			#开始统计每条优惠的数据
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

			  #最后关闭游标.
			  CLOSE cur_p_detail;
			  
		  TRUNCATE t_temp_orderid;

		  #获取自增长字段的偏移量和自增量，以及自增字段的最大值
		  SET @offset = @@auto_increment_offset;
		  SET @increment = @@auto_increment_increment;
		  SELECT ifnull(max(id), 0)
		  INTO
			@maxid
		  FROM
			t_temp_res_sub;
			
		lable_loop:
		  LOOP
			#循环结束标识
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

			#获取订单列表
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
			
			#计算拉动应收
			SELECT ifnull(sum(a.orignalprice * a.dishnum), 0)
			INTO
			  v_shouldamount
			FROM
			  t_temp_order_detail_sub_new a;

			#计算拉动实收(含虚增)
			SELECT ifnull(sum(a.payamount), 0)
			INTO
			  v_paidinamount
			FROM
			  t_temp_settlement_detail_sub_new a;

			#计算虚增
			SELECT ifnull(sum(a.Inflated), 0)
			INTO
			  v_inflated
			FROM
			  t_temp_order_member a JOIN t_temp_orderid b USE INDEX (ix_t_temp_orderid_orderid)
			ON
			  a.orderid = b.orderid;
		  
			#计算结算方式数量
			SELECT ifnull(count(DISTINCT orderid),0)
			INTO v_payways
			FROM t_temp_orderid a;
		
			#计算占总结算方式比: 该分店包含该优惠活动的订单数/该分店所有包含优惠活动的订单数
			SET v_paywaypercent = v_payways / v_payways_total ;
			
			#计算占应收比 : 该分店包含该优惠活动的订单的应收/该分店所有包含优惠活动的订单的应收
			SET v_shouldpercent = v_shouldamount / v_shouldamount_total ;
			
			#计算占实收比 : 该分店包含该优惠活动的订单的实收/该分店所有包含优惠活动的订单的实收
			SET v_paidinpercent = (v_paidinamount - v_inflated ) / ( v_paidinamount_total - v_inflated_total ) ;

			#更新结果数据
			UPDATE t_temp_res_sub
			SET
			  brandid=v_brandid,brandname=v_brand_name,marketid=v_marketid,market=v_market_name,areaid=v_areaid,
			  area=v_area_name,managePattern='1',managePatternid ='自营',jde=v_jde,datetime=v_datetime,
			  shouldamount = v_shouldamount, paidinamount = v_paidinamount - v_inflated, branchid = v_branchid_cur,
			  branchname = v_branchname_cur, paywaypercent = v_paywaypercent, shouldpercent = v_shouldpercent,
			  paidinpercent = v_paidinpercent
			WHERE
			  id = @offset;

			#获取下一个自增id值
			SET @offset = @offset + @increment;
		  END LOOP;

		  #更新支付方式名称
		  UPDATE t_temp_res_sub a, t_dictionary b
		  SET
			a.paywaydesc = b.itemDesc
		  WHERE
			a.payway = b.itemid
			AND type = 'PAYWAY';

		  #更新活动类型名称
		  UPDATE t_temp_res_sub a, t_temp_preferential b
		  SET
			a.ptypename = b.ptypename
		  WHERE
			a.ptype = b.ptype;

		  #更新雅座优惠券活动类型
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
  #返回结果集
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
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_jsfsmxb$$
CREATE PROCEDURE p_report_jsfsmxb(IN  pi_branchid INT(11), 
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
    AND b.payway IN (0, 1, 5, 8, 11, 12, 13,17, 18);
  
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
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_pbckbb$$
CREATE PROCEDURE p_pbckbb(IN  pi_branchid INT(11), -- 分店id 
                                      IN  pi_sb       SMALLINT, -- 市别，0:午市；1:晚市；-1:全天
                                      IN  pi_ksrq     DATETIME, -- 开始日期，
                                      IN  pi_jsrq     DATETIME, -- 结束日期 
                                      IN  pi_sjjg     SMALLINT, -- 时间间隔  -- IN  pi_zsjxz       INT(11), -- 周时间选择
                                      IN  pi_week     VARCHAR(100), -- 周时间，0-星期一，1-星期二，2-星期三，3-星期四，4-星期五，5-星期六，6-星期天(不选择返回-1)
                                      OUT po_errmsg   VARCHAR(100)
                                      )
    SQL SECURITY INVOKER
    COMMENT '排班参考报表'
label_main:
BEGIN -- 返回字段说明如下
  -- 日期 时间段 开台数 上客数 已点餐金额 已结账台数 结账金额 未结账台数 在台数
  --
  -- 返回数据举例（返回值包含多条数据）：
  -- 2015-12-23 09:00-09:30 2 3 34.00 4 78.00 8 8

  DECLARE v_datetimeStr     DATETIME; #日期
  DECLARE v_dateinterval    VARCHAR(50); #时间区间
  DECLARE v_openNum         INT; #开台数
  DECLARE v_guestNum        INT; #上客数
  DECLARE v_orderamount     DOUBLE(13, 2); #已点餐金额
  DECLARE v_alreadycheckNum INT; #已结账台数
  DECLARE v_checkamount     DOUBLE(13, 2); #结账金额
  DECLARE v_notcheckNum     INT; #未结账台数
  DECLARE v_IntheNum        INT; #在台数
  DECLARE v_countNum        INT;
  DECLARE v_status          INT;

  DECLARE v_openTime        TIME; #最小的开业时间
  DECLARE v_endTime         TIME; #最大的结业时间
  DECLARE v_isacrossday     INT; #是否有跨天的数据
  DECLARE v_date_count      INT;
  DECLARE v_dateCount       INT;


  DECLARE v_count           INT;
  DECLARE v_date_start      DATETIME;
  DECLARE v_date_end        DATETIME;
  #DECLARE v_date_interval   DATETIME; #时间间隔  
  DECLARE v_time_interval   DATETIME; #时间间隔
  #DECLARE v_loop_num        INT DEFAULT 0; #根据开始结束时间和显示类型，来设置循环次数
  DECLARE v_table_index     INT;
  DECLARE v_table_id        VARCHAR(50);
  DECLARE v_fetch_done      INT DEFAULT FALSE;

  # 定义游标
  DECLARE c_begintime       VARCHAR(50);
  DECLARE c_endtime         VARCHAR(50);

  DECLARE c_datetimeStr     VARCHAR(50); #日期
  DECLARE c_dateinterval    VARCHAR(50); #时间区间
  DECLARE c_openNum         INT; #开台数
  DECLARE c_guestNum        INT; #上客数
  DECLARE c_orderamount     DOUBLE(13, 2); #已点餐金额
  DECLARE c_alreadycheckNum INT; #已结账台数
  DECLARE c_checkamount     DOUBLE(13, 2); #结账金额
  DECLARE c_notcheckNum     INT; #未结账台数
  DECLARE c_IntheNum        INT; #在台数
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



  --   DECLARE CONTINUE HANDLER FOR NOT FOUND
  --   BEGIN
  --     SET v_fetch_done = TRUE;
  --   END;



  -- 异常处理模块，出现异常返回null
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
  -- SELECT NULL;
  -- GET DIAGNOSTICS CONDITION 1 po_errmsg = MESSAGE_TEXT;
  END;

  SET @@max_heap_table_size = 1024 * 1024 * 300;
  SET @@tmp_table_size = 1024 * 1024 * 300;

  #设置循环次数,处理开始结算时间
  -- SET v_date_start = str_to_date(concat(date_format(pi_ksrq, '%Y-%m-%d'), '00:00:00'), '%Y-%m-%d %H:%i:%s');
  -- SET v_date_end = str_to_date(concat(date_format(pi_jsrq, '%Y-%m-%d'), '23:59:59'), '%Y-%m-%d %H:%i:%s');
  SET v_date_start = str_to_date(pi_ksrq, '%Y-%m-%d %H:%i:%s');
  SET v_date_end = str_to_date(pi_jsrq, '%Y-%m-%d %H:%i:%s');
  SET v_date_count = day(v_date_end) - day(v_date_start);
  --   SET v_date_interval = date_sub(date_add(v_date_start, INTERVAL 1 DAY), INTERVAL 1 SECOND);
  --   SET v_loop_num = timestampdiff(DAY, v_date_start, v_date_end) + 1; #获取两个时间算相差的天数


  #创建订单临时内存表
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

  #为订单内存表创建索引
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);

  SET v_openTime = (SELECT concat(substring_index(time(begintime), ":", 1), ":00")
                    FROM
                      t_temp_order
                    ORDER BY
                      time(begintime) ASC
                    LIMIT
                      1); #获取最小的订单时间
  SET v_endTime = (SELECT time(endtime)
                   FROM
                     t_temp_order
                   ORDER BY
                     time(endtime) DESC
                   LIMIT
                     1); #获取最大的订单时间



  SELECT sum(day(endtime) - day(begintime))
  INTO
    v_dateCount
  FROM
    t_temp_order;


  #如果一个订单跨天结账
  IF v_dateCount > 0 THEN
    SET v_endTime = '23:59:59';
  END IF;

  #传入的时间为今天
  IF v_date_count = 0 AND v_date_end >= c_now AND v_date_start <= c_now THEN
    SET v_endTime = c_now;
  END IF;


  #创建订单详情内存表
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

  #向临时订单详情内存表中放值
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

  -- 存放结果集
  DROP TEMPORARY TABLE IF EXISTS t_temp_res;
  CREATE TEMPORARY TABLE t_temp_res
  (
    datetimeStr DATE, #日期
    dateinterval VARCHAR(50), #时间区间
    openNum INT, #开台数
    guestNum INT, #上客数
    orderamount DECIMAL(13, 2), #已点餐金额
    alreadycheckNum INT, #已结账台数
    checkamount DECIMAL(13, 2), #结账金额
    notcheckNum INT, #未结账台数
    IntheNum INT, #在台数
    countNum INT #数据条数
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;

  -- 存放avg结果集
  DROP TEMPORARY TABLE IF EXISTS t_temp_res_two;
  CREATE TEMPORARY TABLE t_temp_res_two
  (
    datetimeStr VARCHAR(50), #日期
    dateinterval VARCHAR(50), #时间区间
    openNum DECIMAL(13, 2), #开台数
    guestNum DECIMAL(13, 2), #上客数
    orderamount DECIMAL(13, 2), #已点餐金额
    alreadycheckNum DECIMAL(13, 2), #已结账台数
    checkamount DECIMAL(13, 2), #结账金额
    notcheckNum DECIMAL(13, 2), #未结账台数
    IntheNum DECIMAL(13, 2), #在台数
    countNum INT #数据条数
  ) ENGINE = MEMORY DEFAULT CHARSET = utf8;


  OPEN Select_cursor;

#日期拆分时间区间
  Select_cursor_loop:
  LOOP
    FETCH Select_cursor INTO c_begintime, c_endtime;
    IF v_fetch_done THEN
      LEAVE Select_cursor_loop;
    END IF;
    -- SET c_begintime = concat(substring_index(time(c_begintime), ":", 1), ":00:00");
    SET c_begintime = concat(date(c_begintime), " ", substring_index(time(v_openTime), ":", 2));
    SET c_endtime = concat(date(c_begintime), " ", substring_index(time(v_endTime), ":", 2));

    WHILE c_begintime <= c_endtime
    DO
      -- 根据开始和结束时间来查分时间段的值 15分钟
      SET v_time_interval = date_sub(date_add(c_begintime, INTERVAL pi_sjjg MINUTE) + 1, INTERVAL 1 SECOND);

      SET v_countNum = 1;
      #开台数
      SET v_openNum = (SELECT count(1)
                       FROM
                         t_temp_order too
                       WHERE
                         branchid = pi_branchid
                         AND begintime > c_begintime
                         AND begintime < v_time_interval);

      #日期
      SET v_datetimeStr = date(c_begintime);
      #时间区间
      SET v_dateinterval = concat(concat(substring_index(time(c_begintime), ":", 2), "-"), substring_index(time(v_time_interval), ":", 2));
      #上客数
      SET v_guestNum = (SELECT ifnull(sum(womanNum + childNum + mannum), 0)
                        FROM
                          t_temp_order
                        WHERE
                          branchid = pi_branchid
                          AND begintime > c_begintime
                          AND begintime < v_time_interval);
      #已点餐金额
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

      #已结账台数
      SET v_alreadycheckNum = (SELECT count(1)
                               FROM
                                 t_temp_order too
                               WHERE
                                 orderstatus = '3'
                                 AND branchid = pi_branchid
                                 AND endtime > c_begintime
                                 AND endtime < v_time_interval);

      #已结账点餐金额
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

      #未结账台数
      SET v_notcheckNum = (SELECT count(1)
                           FROM
                             t_temp_order
                           WHERE
                             branchid = pi_branchid
                             AND begintime < v_time_interval
                             AND (endtime > v_time_interval
                             OR endtime IS NULL));


      #在台数
      SET v_IntheNum = v_alreadycheckNum + v_notcheckNum;

      INSERT INTO t_temp_res (datetimeStr, dateinterval, openNum, guestNum, orderamount, alreadycheckNum, checkamount, notcheckNum, IntheNum, countNum) VALUES (v_datetimeStr, v_dateinterval, v_openNum, v_guestNum, v_orderamount, v_alreadycheckNum, v_checkamount, v_notcheckNum, v_IntheNum, v_countNum);

      SET c_begintime = date_add(c_begintime, INTERVAL pi_sjjg MINUTE);
      SET v_time_interval = date_add(v_time_interval, INTERVAL pi_sjjg MINUTE);
    END WHILE;
  END LOOP Select_cursor_loop;
  COMMIT;

  CLOSE Select_cursor;
  SET v_fetch_done = FALSE;

  -- 第二个游标开始
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

    -- 第二个游标结束
    END LOOP Select_cursor_detail_loop;
    COMMIT;
    CLOSE Select_cursor_detail;
  END;

  #返回数据
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

END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS p_report_fwyxstjb$$
CREATE PROCEDURE p_report_fwyxstjb (IN pi_branchid int(11),
IN pi_ksrq datetime, -- 开始日期
IN pi_jsrq datetime, -- 结束日期
IN pi_fwyxm varchar(30), -- 服务员姓名
IN pi_smcp varchar(300), -- 菜品名称
IN pi_dqym int, -- 当前页码 第一次进入时从0开始
IN pi_myts int, -- 每页显示的条数
OUT po_errmsg varchar(100))
SQL SECURITY INVOKER
COMMENT '服务员销售统计表'
label_main:
BEGIN
  DECLARE v_waiter_name varchar(300);
  DECLARE v_dish_name varchar(30);
  DECLARE v_date_start datetime;
  DECLARE v_date_end datetime;
  DECLARE v_current_page int;
  DECLARE v_nums_page int;

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
    orderid varchar(50),
    userid varchar(50)
  ) ENGINE = MEMORY DEFAULT charset = utf8;

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
    orderid varchar(50),
    dishid varchar(50),
    primarykey varchar(50),
    superkey varchar(50),
    dishnum varchar(50),
    dishtype int,
    orderprice DECIMAL(10, 2),
    dishunit varchar(100)
  ) ENGINE = MEMORY DEFAULT charset = utf8;

  INSERT INTO t_temp_order_detail
    SELECT
      tod.orderid,
      tod.dishid,
      tod.primarykey,
      tod.superkey,
      tod.dishnum,
      tod.dishtype,
      tod.orderprice,
      tod.dishunit
    FROM t_order_detail tod,
         t_temp_order too
    WHERE tod.orderid = too.orderid;

  #删除套餐中的明细
  DELETE
    FROM t_temp_order_detail
  WHERE dishtype = '2' AND primarykey <> superkey;

  #删除鱼锅名称
  DELETE
    FROM t_temp_order_detail
  WHERE orderprice IS NULL;

  SELECT
    too.orderid,
    too.userid,
    tbu.NAME,
    td.title,
    tod.dishunit,
    td.dishid,
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
END
$$

DELIMITER ;

DELIMITER $$

 CREATE DEFINER=`root`@`%` PROCEDURE `p_report_gzxxmxb`(IN  pi_branchid INT(11),

                                              IN  pi_ksrq     DATETIME, #开始时间

                                              IN  pi_jsrq     DATETIME, #结束时间

                                              IN  pi_gzdw     VARCHAR(50), # 挂账单位 0=全部

                                              IN  pi_qsbz     SMALLINT, # 清算标志0=全部 1=已清算 2=未清算

                                              IN  pi_dqym     INT, #当前页码

                                              IN  pi_myts     INT, #每页条数

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

  /*

  DECLARE EXIT HANDLER FOR SQLEXCEPTION #异常处理程序

  BEGIN

    SELECT null;

    GET DIAGNOSTICS CONDITION 1 po_errmsg = MESSAGE_TEXT;

  END;

*/



  #处理参数异常 

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



  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail; #新建结算方式明细表的临时内存表

  CREATE TEMPORARY TABLE t_temp_settlement_detail(

    inserttime DATETIME DEFAULT NULL,

    orderid VARCHAR(50) NOT NULL,

    payamount DECIMAL(10, 2) DEFAULT NULL COMMENT '实际支付金额',

    bankcardno VARCHAR(50) DEFAULT NULL COMMENT '挂账单位'



  ) ENGINE = HEAP DEFAULT CHARSET = utf8;



  #g根据传入挂账单位参数判断是否查询全部挂账统计

  IF pi_gzdw = c_gzdw_all THEN

    INSERT INTO t_temp_settlement_detail #插入数据

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



    INSERT INTO t_temp_settlement_detail #插入数据

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





  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid; #用来存放中间临时订单号

  CREATE TEMPORARY TABLE t_temp_orderid(

    orderid VARCHAR(50) NOT NULL

  ) ENGINE = HEAP DEFAULT CHARSET = utf8;





  IF pi_qsbz = c_qsbz_yqs THEN #已清算

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





  #删除无效的订单号

  DELETE

  FROM

    t_temp_settlement_detail

  WHERE

    orderid IN (SELECT orderid

                FROM

                  t_temp_orderid);





  DROP TEMPORARY TABLE IF EXISTS t_temp_res; # 结果表

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



  #更新结果表中的订单时间

  UPDATE t_temp_res t, t_order a



  SET

    t.ddsj = a.begintime

  WHERE

    t.ddbh = a.orderid;



  #更新结果表中的已结金额和未结金额

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





  IF pi_dqym > -1 THEN # 分页程序

    SET @a = v_increment_offset + pi_dqym * pi_myts * v_increment_increment - v_increment_increment;

    SET @b = pi_myts;

    PREPARE s1 FROM 'select date_format(ddsj,\'%Y-%c-%d %H:%i:%s\') as ddsj, ddbh, gzje, yjje, wjje, qsbz,date_format(qssj,\'%Y-%c-%d %H:%i:%s\') as qssj,beizhu from t_temp_res where temp_id > ? limit ?';

    EXECUTE s1 USING @a, @b;

  ELSE

    PREPARE s1 FROM 'select date_format(ddsj,\'%Y-%c-%d %H:%i:%s\') as ddsj, ddbh, gzje, yjje, wjje, qsbz,date_format(qssj,\'%Y-%c-%d %H:%i:%s\') as qssj,beizhu from t_temp_res';

    EXECUTE s1;

  END IF;



  DEALLOCATE PREPARE s1;





END $$
DELIMITER ;


DELIMITER $$

 CREATE DEFINER=`root`@`%` PROCEDURE `p_report_gzxxtjb`(IN  pi_branchid INT(11),

                                              IN  pi_ksrq     DATETIME, #开始时间

                                              IN  pi_jsrq     DATETIME, #结束时间

                                              IN  pi_gzdw     VARCHAR(50), # 挂账单位 0=全部

                                              IN  pi_qsbz     SMALLINT, # 清算标志0=全部 1=已清算 2=未清算

                                              IN  pi_dqym     INT, #当前页码

                                              IN  pi_myts     INT, #每页条数

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



  DECLARE EXIT HANDLER FOR SQLEXCEPTION #异常处理程序

  BEGIN

    SELECT NULL;

  #GET DIAGNOSTICS CONDITION 1 po_errmsg = MESSAGE_TEXT;

  END;





  #处理参数异常 

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



  DROP TEMPORARY TABLE IF EXISTS t_temp_settlement_detail; #新建结算方式明细表的临时内存表

  CREATE TEMPORARY TABLE t_temp_settlement_detail(

    orderid VARCHAR(50) NOT NULL,

    payamount DECIMAL(10, 2) DEFAULT NULL COMMENT '实际支付金额',

    bankcardno VARCHAR(50) DEFAULT NULL COMMENT '挂账单位',

    inserttime DATETIME DEFAULT NULL

  ) ENGINE = HEAP DEFAULT CHARSET = utf8;



  #g根据传入挂账单位参数判断是否查询全部挂账统计

  IF pi_gzdw = c_gzdw_all THEN

    INSERT INTO t_temp_settlement_detail #插入数据

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



    INSERT INTO t_temp_settlement_detail #插入数据

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





  DROP TEMPORARY TABLE IF EXISTS t_temp_orderid; #用来存放中间临时订单号

  CREATE TEMPORARY TABLE t_temp_orderid(

    orderid VARCHAR(50) NOT NULL

  ) ENGINE = HEAP DEFAULT CHARSET = utf8;





  IF pi_qsbz = c_qsbz_yqs THEN #已清算

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



  IF pi_qsbz = c_qsbz_wqs THEN #未清算

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





  #删除无效的订单号

  DELETE

  FROM

    t_temp_settlement_detail

  WHERE

    orderid IN (SELECT orderid

                FROM

                  t_temp_orderid);





  DROP TEMPORARY TABLE IF EXISTS t_temp_res; # 结果表

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







  #更新结果表中的已结金额和未结金额

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



  #更新结果表中的未结金额

  UPDATE t_temp_res a

  SET

    a.wjje = a.gzze - a.yjje;



  #更新结果表中的挂账天数



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



  IF pi_dqym > -1 THEN # 分页程序

    SET @a = v_increment_offset + pi_dqym * pi_myts * v_increment_increment - v_increment_increment;

    SET @b = pi_myts;

    PREPARE s1 FROM 'select gzdw, gzds, gzze, yjje, wjje, zcgzsj from t_temp_res where temp_id > ? limit ?';

    EXECUTE s1 USING @a, @b;

  ELSE

    PREPARE s1 FROM 'select gzdw, gzds, gzze, yjje, wjje, zcgzsj from t_temp_res';

    EXECUTE s1;

  END IF;



  DEALLOCATE PREPARE s1;





END $$
DELIMITER ;
