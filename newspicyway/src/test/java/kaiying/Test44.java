package kaiying;

public class Test44 {
 
//    SELECT IFNULL(a.mon+b.mons,0) AS Money,'应收总额（元）' AS columnDetial FROM (SELECT IFNULL(SUM(orderprice*dishnum),0) AS mon  FROM t_order_detail WHERE substring(orderid,2,8)='20150121' ) a,
//    (SELECT IFNULL(SUM(payamount),0) AS mons FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway = 8) b
//    UNION 
//    SELECT IFNULL(SUM(payamount),0),'实收总额（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway IN (0,1,5,8) UNION 
//    SELECT IFNULL(SUM(payamount),0),'折扣总额（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway IN (2,3,4,6,7)
//
//    SELECT IFNULL(SUM(payamount),0) AS Money,'人民币（元）' AS columnDetial FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =0 UNION       
//    SELECT IFNULL(SUM(payamount),0),'挂账（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =5 UNION
//    SELECT IFNULL(SUM(payamount),0),'刷卡（元）-工商银行' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =1 AND debitParterner = '123' UNION
//    SELECT IFNULL(SUM(payamount),0),'刷卡（元）-其他银行' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =1 AND debitParterner != '123' UNION
//    SELECT IFNULL(SUM(payamount),0),'会员储值消费净值（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =8 UNION      
//    SELECT IFNULL(SUM(payamount),0),'合计（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway IN (0,1,5,8)
//
//    SELECT COUNT(*) AS num,'桌数（桌）' AS columnDetial FROM t_order WHERE substring(orderid,2,8)='20150121' AND orderstatus='3' UNION
//    SELECT IFNULL(SUM(mannum+womanNum+childNum),0),'结算人数（个）' FROM t_order WHERE substring(orderid,2,8)='20150121' AND orderstatus='3' UNION
//    SELECT ROUND(IFNULL(((SUM(UNIX_TIMESTAMP(IFNULL(endtime,0)))-SUM(UNIX_TIMESTAMP(IFNULL(begintime,0))))/COUNT(*))/60,0)),'平均消费时间（分）' FROM t_order WHERE substring(orderid,2,8)='20150121' AND orderstatus='3' UNION
//    SELECT ROUND(IFNULL(a.num/b.nums,0),4),'上座率（%）' FROM (SELECT IFNULL(SUM(mannum+womanNum+childNum),0) AS num FROM t_order WHERE substring(orderid,2,8)='20150121' AND orderstatus='3') a,
//    (SELECT IFNULL(SUM(personNum),0) AS nums FROM t_table WHERE tableid IN (SELECT tableids FROM t_order  WHERE substring(orderid,2,8)='20150121' AND orderstatus='3')) b
//
//     SELECT IFNULL(SUM(payamount),0) AS Money,'会员券消费（元）' AS columnDetial FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =3 UNION
//     SELECT IFNULL(SUM(payamount),0),'会员积分消费（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =11 UNION  
//     SELECT IFNULL(SUM(payamount),0),'会员储值消费净值（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =8 UNION  
//     SELECT IFNULL(SUM(payamount),0),'会员储值消费虚增（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =2 UNION 
//     SELECT IFNULL(SUM(payamount),0),'会员消费汇总（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway IN (2,3,8,11) UNION
//     SELECT IFNULL(SUM(payamount),0),'会员卡销售（张）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =9 UNION
//     SELECT IFNULL(SUM(payamount),0),'会员卡赠送（张）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =10 UNION
//     SELECT IFNULL(SUM(payamount),0),'会员卡总计（张）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway IN (9,10)  
//
//      SELECT IFNULL(SUM(payamount),0) AS Money,'优免（元）' AS columnDetial FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =6 UNION
//      SELECT IFNULL(SUM(payamount),0),'会员积分消费（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =11 UNION
//      SELECT IFNULL(SUM(payamount),0),'会员券消费（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =3 UNION
//      SELECT IFNULL(SUM(payamount),0),'会员储值消费虚增（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =2 UNION 
//      SELECT IFNULL(SUM(payamount),0),'折扣优惠（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =4 UNION
//      SELECT IFNULL(SUM(payamount),0),'抹零收入（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =7 UNION
//      SELECT IFNULL(SUM(payamount),0),'折扣总额（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway IN (2,3,4,6,7,11)
// 
//      SELECT IFNULL(SUM(payamount),0) AS Money,'工商银行刷卡（元）' AS columnDetial FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =1 AND debitParterner = '123' UNION
//      SELECT IFNULL(SUM(payamount),0),'工行应收账款（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =1 AND debitParterner = '123' UNION
//      SELECT IFNULL(SUM(payamount),0),'其他银行刷卡（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =1 AND debitParterner != '123' UNION
//      SELECT IFNULL(SUM(payamount),0),'他行应收账款（元）' FROM t_settlement_detail WHERE substring(orderid,2,8)='20150121' AND payway =1 AND debitParterner != '123'
}
