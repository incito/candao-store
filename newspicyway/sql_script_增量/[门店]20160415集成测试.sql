-- 文件描述：本文件为本次集成测试所需增量sql脚本 

-- 描述：修改礼物字段长度 ;
-- 作者： 翟光涛; 
-- 时间：2016-04-21
ALTER TABLE t_gift MODIFY COLUMN gift_name VARCHAR(300);
ALTER TABLE t_gift MODIFY COLUMN gift_unit VARCHAR(100);
ALTER TABLE t_gift_type MODIFY COLUMN gift_type_name VARCHAR(100);

-- 描述：修改orderprice DECIMAL(10, 2),
-- 作者： 蔡蔡; 
-- 时间：2016-04-21
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

-- 描述：挂账存储过程,
-- 作者： 李宗仁; 
-- 时间：2016-04-21
DROP TABLE IF EXISTS `t_billing_detail`;

CREATE TABLE `t_billing_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `orderid` varchar(50) NOT NULL COMMENT '订单编号',
  `branchid` varchar(50) NOT NULL COMMENT '分店ID',
  `creaditname` varchar(100) NOT NULL COMMENT '挂账单位名称',
  `payamount` decimal(8,2) DEFAULT NULL COMMENT '已结金额',
  `disamount` decimal(8,2) DEFAULT NULL COMMENT '优免金额',
  `operator` varchar(255) NOT NULL COMMENT '操作员',
  `inserttime` datetime NOT NULL COMMENT '操作时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 COMMENT='挂账信息结算历史表';

/* Procedure structure for procedure `p_report_gzxxmxb` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_gzxxmxb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `p_report_gzxxmxb`(IN  pi_branchid INT(11),

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

                             , group_concat(left(b.remark,200) SEPARATOR '\n') remark

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





END */$$
DELIMITER ;

/* Procedure structure for procedure `p_report_gzxxtjb` */

/*!50003 DROP PROCEDURE IF EXISTS  `p_report_gzxxtjb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `p_report_gzxxtjb`(IN  pi_branchid INT(11),

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





END */$$
DELIMITER ;
