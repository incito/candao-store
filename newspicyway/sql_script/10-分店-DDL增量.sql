alter table t_settlement_history add column again_settle_nums int(3) DEFAULT 1;
alter table t_settlement_history add column authorizer_name VARCHAR(50);
alter table t_settlement_history add column Inflated decimal(8,2);