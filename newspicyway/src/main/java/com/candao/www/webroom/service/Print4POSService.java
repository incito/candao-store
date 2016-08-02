package com.candao.www.webroom.service;

import java.util.List;

import com.candao.print.entity.SettlementInfo4Pos;

public interface Print4POSService {

	void print(List<SettlementInfo4Pos> settlementInfos, String printType) throws Exception;

	
}
