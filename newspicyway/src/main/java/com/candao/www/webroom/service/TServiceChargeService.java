package com.candao.www.webroom.service;

import java.math.BigDecimal;
import java.util.Map;

import com.candao.www.data.model.TServiceCharge;

public interface TServiceChargeService {
	 int insertChargeInfo(TServiceCharge charge);
	 int updateChargeInfo(TServiceCharge chargeInfo);
	 int changChargeInfo(TServiceCharge chargeInfo);
	 TServiceCharge getChargeInfo(Map<String, Object> params);
	
	 int delChargeInfo(Map<String, Object> params);
	 TServiceCharge serviceCharge(String orderid, Map<String, Object> userOrderInfo, BigDecimal payDecimal,
				BigDecimal MenuDecimal);
}
