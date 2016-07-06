package com.candao.www.webroom.service;

import java.util.Map;

import com.candao.www.webroom.model.SettlementInfo;
import com.candao.www.weixin.dto.SettlementStrInfoDto;
import com.candao.www.weixin.dto.WxPayResult;

public interface OrderSettleService {

	public String settleOrder(SettlementInfo settlementInfo);
	
	public String rebackSettleOrder(SettlementInfo settlementInfo) ;
	
	public int rebaceSettleOrder(SettlementInfo settlementInfo);

	public String saveposcash(SettlementInfo settlementInfo);
	
	public SettlementStrInfoDto setInitData(SettlementStrInfoDto settlementStrInfoDto,WxPayResult payResult);
 	public String calDebitAmount(String orderId);
 	public void updatePadData(String attach);
 	public Map<String, Object> selectorderinfos(String orderid);
 	//咖啡模式结账
	public String saveOrder(SettlementInfo settlementInfo);
}
