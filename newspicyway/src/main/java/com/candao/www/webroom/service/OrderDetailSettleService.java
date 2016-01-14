package com.candao.www.webroom.service;

import com.candao.www.data.model.TsettlementDetail;

public interface OrderDetailSettleService {

	public int settleOrderDetail(TsettlementDetail settlementInfo);
	
	public int rebaceSettleOrderDetail(TsettlementDetail settlementInfo);
}
