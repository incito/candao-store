package com.candao.www.webroom.model;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.candao.common.utils.JacksonJsonMapper;

public class Test {

	public static void main(String[] args) {
		SettlementInfo  settlementInfo = new SettlementInfo();
		settlementInfo.setOrderNo("001");
		settlementInfo.setUserName("admin");
		
		ArrayList<SettlementDetail> list = new ArrayList<SettlementDetail>();
		SettlementDetail  detail = new SettlementDetail();
		detail.setPayWay("0");
		detail.setPayAmount(new BigDecimal(100.00));
		
		SettlementDetail  detail2 = new SettlementDetail();
		detail2.setPayWay("1");
		detail2.setBankCardNo("7777777777");
		detail2.setPayAmount(new BigDecimal(200.00));
		
		SettlementDetail  detail3 = new SettlementDetail();
		detail3.setPayWay("2");
		detail3.setMemerberCardNo("9999");
		detail3.setPayAmount(new BigDecimal(300.00));
		
		list.add(detail);
		list.add(detail2);
		list.add(detail3);
		
		settlementInfo.setPayDetail(list);
		
		System.out.println(JacksonJsonMapper.objectToJson(settlementInfo));
	}

}
