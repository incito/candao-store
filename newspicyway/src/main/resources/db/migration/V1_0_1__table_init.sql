/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.6.25-log : Database - newspicyway
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
 

/*Table structure for table `v_currmenu` */

DROP TABLE IF EXISTS `v_currmenu`;

/*!50001 DROP VIEW IF EXISTS `v_currmenu` */;
/*!50001 DROP TABLE IF EXISTS `v_currmenu` */;

/*!50001 CREATE TABLE  `v_currmenu`(
 `menuid` varchar(50) 
)*/;

/*Table structure for table `v_order` */

DROP TABLE IF EXISTS `v_order`;

/*!50001 DROP VIEW IF EXISTS `v_order` */;
/*!50001 DROP TABLE IF EXISTS `v_order` */;

/*!50001 CREATE TABLE  `v_order`(
 `tableNo` varchar(50) ,
 `tableid` varchar(50) ,
 `orderid` varchar(50) ,
 `dishid` varchar(50) ,
 `title` varchar(300) ,
 `dishunit` varchar(100) ,
 `dishnum` varchar(50) ,
 `orderprice` decimal(10,2) ,
 `orderdetailid` varchar(50) ,
 `orderseq` varchar(50) 
)*/;

/*Table structure for table `v_revenuepayway` */

DROP TABLE IF EXISTS `v_revenuepayway`;

/*!50001 DROP VIEW IF EXISTS `v_revenuepayway` */;
/*!50001 DROP TABLE IF EXISTS `v_revenuepayway` */;

/*!50001 CREATE TABLE  `v_revenuepayway`(
 `dictid` varchar(50) ,
 `itemid` varchar(50) ,
 `itemDesc` varchar(50) ,
 `itemSort` int(11) ,
 `status` int(11) ,
 `type` varchar(50) ,
 `typename` varchar(50) ,
 `begin_time` varchar(20) ,
 `end_time` varchar(20) ,
 `charges_status` varchar(5) ,
 `member_price` varchar(10) ,
 `price` varchar(10) ,
 `date_type` varchar(5) ,
 `item_value` varchar(100) 
)*/;

/*Table structure for table `v_t_p_preferential_activity` */

DROP TABLE IF EXISTS `v_t_p_preferential_activity`;

/*!50001 DROP VIEW IF EXISTS `v_t_p_preferential_activity` */;
/*!50001 DROP TABLE IF EXISTS `v_t_p_preferential_activity` */;

/*!50001 CREATE TABLE  `v_t_p_preferential_activity`(
 `id` varchar(200) ,
 `name` varchar(200) ,
 `type` varchar(4) ,
 `type_name` varchar(32) ,
 `sub_type` varchar(4) ,
 `sub_type_name` varchar(32) ,
 `preferential` varchar(50) 
)*/;

/*View structure for view v_currmenu */

/*!50001 DROP TABLE IF EXISTS `v_currmenu` */;
/*!50001 DROP VIEW IF EXISTS `v_currmenu` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY INVOKER VIEW `v_currmenu` AS select `tm`.`menuid` AS `menuid` from ((`t_branch_info` `tbf` join `t_menu_branch` `mb`) join `t_menu` `tm`) where ((`tbf`.`branchid` = `mb`.`branchid`) and (`mb`.`menuid` = `tm`.`menuid`) and (`tm`.`status` = '1')) order by `tm`.`effecttime` desc limit 1 */;

/*View structure for view v_order */

/*!50001 DROP TABLE IF EXISTS `v_order` */;
/*!50001 DROP VIEW IF EXISTS `v_order` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY INVOKER VIEW `v_order` AS select `a`.`tableNo` AS `tableNo`,`a`.`tableid` AS `tableid`,`a`.`orderid` AS `orderid`,`c`.`dishid` AS `dishid`,`c`.`title` AS `title`,`b`.`dishunit` AS `dishunit`,`b`.`dishnum` AS `dishnum`,`b`.`orderprice` AS `orderprice`,`b`.`orderdetailid` AS `orderdetailid`,`b`.`orderseq` AS `orderseq` from ((`t_table` `a` join `t_order_detail` `b`) join `t_dish` `c`) where ((`a`.`orderid` = `b`.`orderid`) and (`b`.`dishid` = `c`.`dishid`)) */;

/*View structure for view v_revenuepayway */

/*!50001 DROP TABLE IF EXISTS `v_revenuepayway` */;
/*!50001 DROP VIEW IF EXISTS `v_revenuepayway` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY INVOKER VIEW `v_revenuepayway` AS select `t_dictionary`.`dictid` AS `dictid`,`t_dictionary`.`itemid` AS `itemid`,`t_dictionary`.`itemDesc` AS `itemDesc`,`t_dictionary`.`itemSort` AS `itemSort`,`t_dictionary`.`status` AS `status`,`t_dictionary`.`type` AS `type`,`t_dictionary`.`typename` AS `typename`,`t_dictionary`.`begin_time` AS `begin_time`,`t_dictionary`.`end_time` AS `end_time`,`t_dictionary`.`charges_status` AS `charges_status`,`t_dictionary`.`member_price` AS `member_price`,`t_dictionary`.`price` AS `price`,`t_dictionary`.`date_type` AS `date_type`,`t_dictionary`.`item_value` AS `item_value` from `t_dictionary` where ((`t_dictionary`.`itemid` in ('0','1','5','8','13','17','18','30')) and (`t_dictionary`.`type` = 'PAYWAY')) */;

/*View structure for view v_t_p_preferential_activity */

/*!50001 DROP TABLE IF EXISTS `v_t_p_preferential_activity` */;
/*!50001 DROP VIEW IF EXISTS `v_t_p_preferential_activity` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY INVOKER VIEW `v_t_p_preferential_activity` AS select (case when (`a`.`preferential` = `a`.`id`) then `b`.`name` else ifnull(ifnull(`a`.`free_reason`,`a`.`company_name`),`b`.`name`) end) AS `id`,(case when (`a`.`preferential` = `a`.`id`) then `b`.`name` else ifnull(ifnull(`a`.`free_reason`,`a`.`company_name`),`b`.`name`) end) AS `name`,`b`.`type` AS `type`,`b`.`type_name` AS `type_name`,`b`.`sub_type` AS `sub_type`,`b`.`sub_type_name` AS `sub_type_name`,`a`.`preferential` AS `preferential` from (`t_p_preferential_detail` `a` join `t_p_preferential_activity` `b` on((`a`.`preferential` = `b`.`id`))) union select distinct `t_settlement_detail`.`bankcardno` AS `id`,`t_settlement_detail`.`bankcardno` AS `name`,'12' AS `type`,'会员' AS `type_name`,'' AS `sub_type`,'' AS `sub_type_name`,`t_settlement_detail`.`bankcardno` AS `preferential` from `t_settlement_detail` where (`t_settlement_detail`.`payway` = '12') */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
