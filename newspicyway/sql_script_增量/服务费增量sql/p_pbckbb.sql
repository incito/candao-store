DELIMITER $$


DROP PROCEDURE IF EXISTS `p_pbckbb`$$

CREATE PROCEDURE `p_pbckbb`(IN  pi_branchid INT(11), -- 分店id 
                                      IN  pi_sb       SMALLINT, -- 市别，0:午市；1:晚市；-1:全天
                                      IN  pi_ksrq     DATETIME, -- 开始日期，
                                      IN  pi_jsrq     DATETIME, -- 结束日期 
                                      IN  pi_sjjg     SMALLINT, -- 时间间隔  -- IN  pi_zsjxz       INT(11), -- 周时间选择
                                      IN  pi_week     VARCHAR(100), -- 周时间，0-星期一，1-星期二，2-星期三，3-星期四，4-星期五，5-星期六，6-星期天(不选择返回-1)
                                      OUT po_errmsg   VARCHAR(100))
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
  DECLARE c_now             DATETIME DEFAULT NOW();

  DECLARE Select_cursor CURSOR FOR SELECT DATE(begintime)
                                        , DATE(begintime)
                                   FROM
                                     t_temp_order
                                   WHERE
                                     1 = 1
                                   GROUP BY
                                     DATE(begintime);
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
  SET v_date_start = STR_TO_DATE(pi_ksrq, '%Y-%m-%d %H:%i:%s');
  SET v_date_end = STR_TO_DATE(pi_jsrq, '%Y-%m-%d %H:%i:%s');
  SET v_date_count = DAY(v_date_end) - DAY(v_date_start);
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
        AND FIND_IN_SET(WEEKDAY(begintime), pi_week)
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
        AND FIND_IN_SET(WEEKDAY(begintime), pi_week)
      END;
  END IF;

  #为订单内存表创建索引
  CREATE UNIQUE INDEX ix_t_temp_order_orderid ON t_temp_order (orderid);

  SET v_openTime = (SELECT CONCAT(SUBSTRING_INDEX(TIME(begintime), ":", 1), ":00")
                    FROM
                      t_temp_order
                    ORDER BY
                      TIME(begintime) ASC
                    LIMIT
                      1); #获取最小的订单时间
  SET v_endTime = (SELECT TIME(endtime)
                   FROM
                     t_temp_order
                   ORDER BY
                     TIME(endtime) DESC
                   LIMIT
                     1); #获取最大的订单时间



  SELECT SUM(DAY(endtime) - DAY(begintime))
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
    dishnum DOUBLE(13,2),
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
    SET c_begintime = CONCAT(DATE(c_begintime), " ", SUBSTRING_INDEX(TIME(v_openTime), ":", 2));
    SET c_endtime = CONCAT(DATE(c_begintime), " ", SUBSTRING_INDEX(TIME(v_endTime), ":", 2));

    WHILE c_begintime <= c_endtime
    DO
      -- 根据开始和结束时间来查分时间段的值 15分钟
      SET v_time_interval = DATE_SUB(DATE_ADD(c_begintime, INTERVAL pi_sjjg MINUTE) + 1, INTERVAL 1 SECOND);

      SET v_countNum = 1;
      #开台数
      SET v_openNum = (SELECT COUNT(1)
                       FROM
                         t_temp_order too
                       WHERE
                         branchid = pi_branchid
                         AND begintime > c_begintime
                         AND begintime < v_time_interval);

      #日期
      SET v_datetimeStr = DATE(c_begintime);
      #时间区间
      SET v_dateinterval = CONCAT(CONCAT(SUBSTRING_INDEX(TIME(c_begintime), ":", 2), "-"), SUBSTRING_INDEX(TIME(v_time_interval), ":", 2));
      #上客数
      SET v_guestNum = (SELECT IFNULL(SUM(womanNum + childNum + mannum), 0)
                        FROM
                          t_temp_order
                        WHERE
                          branchid = pi_branchid
                          AND begintime > c_begintime
                          AND begintime < v_time_interval);
      #已点餐金额
      SET v_orderamount = (SELECT SUM(tod.dishnum * tod.orignalprice)
                           FROM
                             t_temp_order too, t_temp_order_detail tod, t_dish dd
                           WHERE
                             too.orderid = tod.orderid
                             AND tod.dishid = dd.dishid
                             AND dd.dishtype NOT IN (2)
                             AND too.branchid = pi_branchid
                             AND tod.begintime > c_begintime
                             AND tod.begintime < v_time_interval);
      #服务费
			SET v_orderamount=IFNULL(v_orderamount,0)+(SELECT IFNULL(SUM(chargeAmount),0) FROM t_service_charge WHERE orderid IN (SELECT orderid FROM t_temp_order too WHERE too.branchid = pi_branchid
                             AND too.begintime > c_begintime
                             AND too.begintime < v_time_interval) AND chargeOn=1);
      #已结账台数
      SET v_alreadycheckNum = (SELECT COUNT(1)
                               FROM
                                 t_temp_order too
                               WHERE
                                 orderstatus = '3'
                                 AND branchid = pi_branchid
                                 AND endtime > c_begintime
                                 AND endtime < v_time_interval);

      #已结账点餐金额
      SET v_checkamount = (SELECT SUM(tod.dishnum * tod.orignalprice)
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
      #服务费
			SET v_checkamount=IFNULL(v_checkamount,0)+(SELECT IFNULL(SUM(chargeAmount),0) FROM t_service_charge WHERE orderid IN (SELECT orderid FROM t_temp_order too WHERE too.branchid = pi_branchid
                             AND too.endtime > c_begintime
                             AND too.endtime < v_time_interval) AND chargeOn=1);

      #未结账台数
      SET v_notcheckNum = (SELECT COUNT(1)
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

      SET c_begintime = DATE_ADD(c_begintime, INTERVAL pi_sjjg MINUTE);
      SET v_time_interval = DATE_ADD(v_time_interval, INTERVAL pi_sjjg MINUTE);
    END WHILE;
  END LOOP Select_cursor_loop;
  COMMIT;

  CLOSE Select_cursor;
  SET v_fetch_done = FALSE;

  -- 第二个游标开始
  BEGIN DECLARE Select_cursor_detail CURSOR FOR SELECT 'avg'
                                                     , dateinterval
                                                     , SUM(openNum)
                                                     , SUM(guestNum)
                                                     , SUM(IFNULL(orderamount, 0))
                                                     , SUM(alreadycheckNum)
                                                     , SUM(IFNULL(checkamount, 0))
                                                     , SUM(notcheckNum)
                                                     , SUM(IntheNum)
                                                     , SUM(countNum)
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
  SELECT DATE_FORMAT(datetimeStr, '%Y/%m/%d') AS datetimeStr
       , dateinterval
       , openNum
       , guestNum
       , IFNULL(orderamount, 0) AS orderamount
       , alreadycheckNum
       , IFNULL(checkamount, 0) AS checkamount
       , notcheckNum
       , IntheNum
  FROM
    t_temp_res
  UNION
  SELECT datetimeStr
       , dateinterval
       , openNum
       , guestNum
       , IFNULL(orderamount, 0) AS orderamount
       , alreadycheckNum
       , IFNULL(checkamount, 0) AS checkamount
       , notcheckNum
       , IntheNum
  FROM
    t_temp_res_two;

END$$

DELIMITER ;