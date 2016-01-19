package com.candao.www.webroom.service.impl;

import com.candao.www.data.dao.TSettlementOptionDao;
import com.candao.www.webroom.service.SettlementOptionService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			String membercardno = (String)map.get("membercardno");
			String itemid = (String)map.get("itemid");
			String payway = (String)map.get("payway");
			if(StringUtils.isNotBlank(itemid) && itemid.equals("1")){
				if(StringUtils.isNotBlank(membercardno) && membercardno.equals("1")){
					payway = payway + "(刷工行卡)";
				}else{
					payway = payway + "(刷他行卡)";
				}
			}
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
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for (Map<String, Object> map : settlementOptionList){
    			Map<String,Object> s = new HashMap<String,Object>();
    			String membercardno = (String)map.get("membercardno");
    			String itemid = (String)map.get("itemid");
    			String payway = (String)map.get("payway");
    			if(StringUtils.isNotBlank(itemid) && itemid.equals("1")){
    				if(StringUtils.isNotBlank(membercardno) && membercardno.equals("1")){
    					payway = payway + "(刷工行卡)";
    				}else{
    					payway = payway + "(刷他行卡)";
    				}
    			}
    			s.put("payway",payway);
    			s.put("nums",map.get("nums"));
    			s.put("prices", decimalFormat.format(map.get("prices")));
    			list.add(s);
        }
		PoiExcleTest.exportExcleC(list,params, vasd, req,resp);
	}
}
