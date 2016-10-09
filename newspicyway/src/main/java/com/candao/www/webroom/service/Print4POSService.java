package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.print.entity.ResultInfo4Pos;
import com.candao.print.entity.ResultTip4Pos;
import com.candao.print.entity.SettlementInfo4Pos;

public interface Print4POSService {

	void print(List<SettlementInfo4Pos> settlementInfos, String printType, String deviceid) throws Exception;

	void printClearMachine(List<SettlementInfo4Pos> settlementInfos, String posId) throws Exception;

	void printMemberSaleInfo(List<SettlementInfo4Pos> settlementInfos, String deviceid) throws Exception;

	void printItemSellDetail(ResultInfo4Pos resultInfo4Pos, String deviceid) throws Exception;

	void printTip(ResultTip4Pos resultInfo4Pos, String deviceid) throws Exception;

	void printInvoice(Map<String, Object> map) throws Exception;

	void printStoredCard(Map<String, Object> map) throws Exception;

	void printBusinessDetail(Map<String, Object> map, String... params) throws Exception;

	void printPreSettlement(Map<String, Object> map, String... params) throws Exception;
}
