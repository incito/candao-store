alter table t_b_weixin add COLUMN weixintype int;
alter table t_b_weixin add COLUMN personweixinurl varchar(100);
alter table t_b_weixin add COLUMN status int;

DROP TABLE IF EXISTS `t_b_padconfig`;
CREATE TABLE `t_b_padconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `padloginpass` varchar(20) DEFAULT NULL,
  `social` tinyint(1) DEFAULT NULL,
  `seatimageurls` varchar(500) DEFAULT NULL,
  `seatimagenames` varchar(100) DEFAULT NULL,
  `vipstatus` tinyint(1) DEFAULT NULL,
  `viptype` varchar(8) DEFAULT NULL,
  `vipcandaourl` varchar(50) DEFAULT NULL,
  `vipotherurl` varchar(50) DEFAULT NULL,
  `clickimagedish` tinyint(1) DEFAULT NULL,
  `onepage` tinyint(1) DEFAULT NULL,
  `newplayer` tinyint(1) DEFAULT NULL,
  `chinaEnglish` tinyint(1) DEFAULT NULL,
  `indexad` tinyint(1) DEFAULT NULL,
  `invoice` tinyint(1) DEFAULT NULL,
  `hidecarttotal` tinyint(1) DEFAULT NULL,
  `adtimes` varchar(10) DEFAULT NULL,
  `waiterreward` tinyint(1) DEFAULT NULL,
  `rewardmoney` varchar(10) DEFAULT NULL,
  `youmengappkey` varchar(50) DEFAULT NULL,
  `youmengchinnal` varchar(50) DEFAULT NULL,
  `bigdatainterface` varchar(100) DEFAULT NULL,
  `braceletgappkey` varchar(50) DEFAULT NULL,
  `braceletchinnal` varchar(50) DEFAULT NULL,
  `weixintype` varchar(8) DEFAULT NULL,
  `personweixinurl` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_b_padconfig
-- ----------------------------
INSERT INTO `t_b_padconfig` VALUES ('1', '0', '1', null,null, '1', '1', 'http://member.candaochina.com/member', null, '1', '1', '1', '1', '1', '1', '1', '60', '1', '5', null, null, null, null, null, 1, null);

DELIMITER $$

DROP FUNCTION IF EXISTS getseqno$$
CREATE FUNCTION getseqno(seq_name varchar(20))
  RETURNS varchar(100) CHARSET utf8
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
	-- 如果是子订单，需要截取
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

END
$$
DELIMITER ;