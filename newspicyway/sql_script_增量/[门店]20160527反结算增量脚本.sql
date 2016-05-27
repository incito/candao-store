-- 文件描述：反结算增量脚本

-- 描述：反结算增量脚本;
-- 作者： 韦志芳; 
-- 时间：2016-05-27
ALTER TABLE `t_order_history` ADD PRIMARY KEY (`orderid`);
ALTER TABLE `t_order_history` ADD INDEX `IX_t_order_begintime` (`branchid`,`begintime`);

ALTER TABLE `t_order_detail_history` ADD PRIMARY KEY (`orderdetailid`);
ALTER TABLE `t_order_detail_history` ADD UNIQUE key `primarykey` (`primarykey`);
ALTER TABLE `t_order_detail_history` ADD INDEX `IX_t_order_detail_begintime` (`begintime`);
ALTER TABLE `t_order_detail_history` ADD INDEX `t_order_detail_orderdetailid` (`orderdetailid`);
ALTER TABLE `t_order_detail_history` ADD INDEX `t_order_detail_orderid` (`orderid`);
ALTER TABLE `t_order_detail_history` MODIFY orderseq VARCHAR(50);

ALTER TABLE `t_settlement_history` ADD PRIMARY KEY (`settledId`);
ALTER TABLE `t_settlement_history` ADD UNIQUE key `t_order_settlement_orderid` (`settledId`);
ALTER TABLE `t_settlement_history` ADD INDEX `t_order_settlement_orderid` (`orderid`);

ALTER TABLE `t_settlement_detail_history` ADD PRIMARY KEY (`sdetailid`);
ALTER TABLE `t_settlement_detail_history` ADD UNIQUE key `UQ_t_settlement_detail_sdetailid` (`sdetailid`);
ALTER TABLE `t_settlement_detail_history` ADD INDEX `t_order_settlement_detail_orderid` (`orderid`);