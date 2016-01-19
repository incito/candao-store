package com.candao.www.webroom.service;

import com.candao.www.webroom.model.SettlementInfo;

public interface OrderSettleService {

	public String settleOrder(SettlementInfo settlementInfo);
	
	public String rebackSettleOrder(SettlementInfo settlementInfo) ;
	
	public int rebaceSettleOrder(SettlementInfo settlementInfo);

	public String saveposcash(SettlementInfo settlementInfo);
	
	
 	public String calDebitAmount(String orderId);
}
