package com.candao.www.webroom.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TSettlementOptionDao;
import com.candao.www.webroom.service.SettlementOptionService;

/**
 * 结算方式明细表
 * @author Administrator
 *
 */
@Service("settlementOptionService")
public class SettlementOptionServiceImpl implements SettlementOptionService {

	@Autowired
	private TSettlementOptionDao tsettlementOptionDao;

	@Override
	public List<Map<String, Object>> settlementOptionList(Map<String, Object> params) {
		List<Map<String,Object>> settlement = tsettlementOptionDao.settlementOptionList(params);
		return settlement;
	}

	@Override
	public void exportXls(Map<String, Object> params, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<Map<String,Object>> settlementOptionList = tsettlementOptionDao.settlementOptionList(params);
		String vasd ="结算方式明细表";
		PoiExcleTest.exportExcleC(settlementOptionList,params, vasd, req,resp);
	}
}
