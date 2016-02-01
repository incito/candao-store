alter table t_settlement_history add column again_settle_nums int(3) DEFAULT 1;
alter table t_settlement_history add column authorizer_name VARCHAR(50);
alter table t_settlement_history add column Inflated decimal(8,2);

--
-- Definition for table t_order_history
--
DROP TABLE IF EXISTS t_order_history;
CREATE TABLE t_order_history (
  orderid varchar(50) NOT NULL,
  userid varchar(50) DEFAULT NULL,
  begintime datetime DEFAULT NULL,
  endtime datetime DEFAULT NULL,
  orderstatus int(11) NOT NULL COMMENT '0 ???e? 1 ???????? 2 ????????????????  3 ???????',
  custnum int(11) DEFAULT NULL,
  tableids varchar(1000) DEFAULT NULL,
  specialrequied varchar(100) DEFAULT NULL,
  womanNum int(11) DEFAULT NULL,
  childNum int(11) DEFAULT NULL,
  mannum int(11) DEFAULT NULL,
  currenttableid varchar(200) DEFAULT NULL,
  fulldiscountrate decimal(8,2) unsigned zerofill DEFAULT '000000.00' COMMENT '全单折扣',
  dueamount decimal(8,2) unsigned zerofill DEFAULT '000000.00' COMMENT '应收金额',
  discountamount decimal(8,2) unsigned zerofill DEFAULT '000000.00' COMMENT '折扣金额',
  freeamount decimal(8,2) unsigned zerofill DEFAULT '000000.00' COMMENT '优惠金额',
  wipeamount decimal(8,2) unsigned zerofill DEFAULT '000000.00' COMMENT '抹零金额',
  payway int(11) DEFAULT NULL,
  partnername varchar(50) DEFAULT NULL,
  couponname varchar(100) DEFAULT NULL,
  disuserid varchar(50) DEFAULT NULL,
  relateorderid varchar(50) DEFAULT NULL,
  printcount int(11) DEFAULT '0',
  befprintcount int(11) DEFAULT '0',
  workdate datetime DEFAULT NULL COMMENT '工作日期',
  shiftid int(11) DEFAULT '0' COMMENT '班别编号 0早班 1晚班',
  closeshiftid int(11) DEFAULT '0',
  memberno varchar(50) DEFAULT NULL,
  ymamount decimal(8,2) DEFAULT '0.00' COMMENT '优免金额',
  gzamount decimal(8,2) DEFAULT '0.00' COMMENT '挂帐金额',
  ssamount decimal(8,2) DEFAULT '0.00' COMMENT '实收金额 现金，银行卡，会员卡，会员积分',
  ageperiod varchar(255) DEFAULT NULL,
  meid varchar(100) DEFAULT NULL COMMENT 'PAD 端编码',
  orderseq int(11) DEFAULT NULL,
  branchid int(11) DEFAULT NULL,
  ordertype int(11) DEFAULT '0',
  gzcode varchar(20) DEFAULT NULL,
  gzname varchar(50) DEFAULT NULL,
  gztele varchar(20) DEFAULT NULL,
  gzuser varchar(20) DEFAULT NULL,
  invoice_id varchar(50) DEFAULT NULL,
  gift_status char(2) DEFAULT '0'
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Definition for table t_order_detail_history
--
DROP TABLE IF EXISTS t_order_detail_history;
CREATE TABLE t_order_detail_history (
  orderid varchar(50) DEFAULT NULL,
  dishid varchar(50) DEFAULT NULL,
  dishstatus varchar(50) DEFAULT NULL COMMENT '0 ??????? 1 ??? 2 ??? ',
  begintime datetime DEFAULT NULL,
  endtime datetime DEFAULT NULL,
  sperequire varchar(200) DEFAULT NULL,
  dishnum varchar(50) DEFAULT NULL,
  userName varchar(100) DEFAULT NULL,
  orderprice decimal(10,2) DEFAULT NULL,
  discountrate decimal(10,2) DEFAULT '0.00' COMMENT '折扣率',
  discountamount decimal(10,2) DEFAULT '0.00' COMMENT '折扣金额',
  fishcode varchar(50) DEFAULT NULL COMMENT '使用鱼券',
  dishtype int(11) DEFAULT '0' COMMENT '0 单品 1 火锅 2 套餐',
  status int(11) DEFAULT '0',
  dishunit varchar(50) DEFAULT NULL,
  payamount decimal(10,2) DEFAULT NULL,
  predisamount decimal(10,2) DEFAULT NULL,
  couponid varchar(50) DEFAULT NULL,
  disuserid varchar(50) DEFAULT NULL,
  orignalprice decimal(10,2) DEFAULT NULL COMMENT '原始价格',
  pricetype varchar(10) DEFAULT NULL,
  orderseq int(11) DEFAULT NULL,
  orderdetailid varchar(50) NOT NULL,
  relatedishid varchar(4000) DEFAULT NULL,
  ordertype int(3) DEFAULT NULL COMMENT '''下单类型 0 正常 1 赠送''',
  parentkey varchar(255) DEFAULT NULL COMMENT '父类主键',
  superkey varchar(255) DEFAULT NULL COMMENT '套餐的主键',
  ismaster int(255) DEFAULT NULL COMMENT '是否是master 默认是 0 不是 1是',
  primarykey varchar(255) DEFAULT NULL COMMENT '菜品主键',
  islatecooke int(2) DEFAULT '0' COMMENT '0 否 1 是',
  isadddish int(2) DEFAULT NULL COMMENT '0 否 1 是',
  childdishtype int(2) DEFAULT NULL COMMENT '0 单品 1 火锅 2 套餐',
  ispot int(2) DEFAULT '0' COMMENT '是否是锅底 0 否 1 是',
  relateorderid varchar(255) DEFAULT NULL,
  discarduserid varchar(255) DEFAULT NULL,
  discardreason varchar(255) DEFAULT NULL,
  debitamount decimal(20,2) DEFAULT NULL COMMENT '单个菜品加权实际收入'
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci;