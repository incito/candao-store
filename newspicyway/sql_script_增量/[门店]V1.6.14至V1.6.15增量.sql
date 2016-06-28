--
-- Definition for procedure p_orderdish
--

SET NAMES 'utf8';
DELIMITER $$
DROP PROCEDURE IF EXISTS `p_orderdish`$$
CREATE PROCEDURE `p_orderdish`(IN i_orderid varchar(50), IN i_printobjid int, INOUT o_code varchar(50), INOUT o_msg varchar(300))
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
      ##其他错误 code 3
      SET o_code = '3';
      GET DIAGNOSTICS CONDITION 1 o_msg = MESSAGE_TEXT;
      ##删除临时表
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

      ##删除临时表
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

						##删除临时表
						DELETE
							FROM t_order_detail_temp
						WHERE orderid = i_orderid;

						LEAVE label_main;
        END IF;

        IF done = 1 THEN
          SET o_code = '2';
          SET o_msg = '菜品更新中';

          ##删除临时表
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

          ##删除临时表
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

      UPDATE t_table
      SET status = '1',
          orderid = v_orderid
      WHERE tableid = V_TABLEID;

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

      ##删除临时表
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

  END
$$
DELIMITER ;