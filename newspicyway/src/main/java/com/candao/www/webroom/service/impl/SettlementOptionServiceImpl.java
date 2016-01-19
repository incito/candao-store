package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
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
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map : settlement){
			Map<String,Object> s = new HashMap<String,Object>();
			String membercardno = String.valueOf(map.get("membercardno"));
			String itemid = String.valueOf(map.get("itemid"));
			String payway = String.valueOf(map.get("payway"));
			s.put("payway",payway);
			s.put("nums",map.get("nums"));
			s.put("prices",map.get("prices"));
			s.put("membercardno", membercardno);
			s.put("itemid", itemid);
			list.add(s);
		}
		return list;
	}

	@Override
	public void exportXls(Map<String, Object> params, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<Map<String,Object>> settlementOptionList = tsettlementOptionDao.settlementOptionList(params);
		String vasd ="结算方式明细表";
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for (Map<String, Object> map : settlementOptionList){
        	Map<String,Object> s = new HashMap<String,Object>();
			String membercardno = String.valueOf(map.get("membercardno"));
			String itemid = String.valueOf(map.get("itemid"));
			String payway = String.valueOf(map.get("payway"));
			s.put("payway",payway);
			s.put("nums",map.get("nums"));
			s.put("prices",map.get("prices"));
			s.put("membercardno", membercardno);
			s.put("itemid", itemid);
			list.add(s);
        }
		PoiExcleTest.exportExcleC(list,params, vasd, req,resp);
	}
}
