package com.candao.www.webroom.service;

import java.util.List;

import com.candao.print.entity.ResultInfo4Pos;
import com.candao.print.entity.ResultTip4Pos;
import com.candao.print.entity.SettlementInfo4Pos;

public interface Print4POSService {

	void print(List<SettlementInfo4Pos> settlementInfos, String printType) throws Exception;

	void printClearMachine(List<SettlementInfo4Pos> settlementInfos);

	void printMemberSaleInfo(List<SettlementInfo4Pos> settlementInfos);

	void printItemSellDetail(ResultInfo4Pos resultInfo4Pos);

	void printTip(ResultTip4Pos resultInfo4Pos);

	
}
