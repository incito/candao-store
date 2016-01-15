package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TSettlementDetailChildDao;
import com.candao.www.webroom.service.SettlementDetailChildService;

/**
 * 结算方式明细表子表
 * @author weizhifang
 * @since 2015-11-21
 *
 */
@Service
public class SettlementDetailChildServiceImpl implements SettlementDetailChildService {

	@Autowired
	private TSettlementDetailChildDao tSettlementDetailChildDao;
	
	/**
	 * 查询结算方式明细
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> querySettDetailList(Map<String,Object> params){
		List<Map<String,Object>> list = tSettlementDetailChildDao.querySettDetailList(params);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> data : list){
			Map<String,Object> value = new HashMap<String,Object>();
			value.put("insertTime", data.get("insertTime"));
			value.put("orderid", data.get("orderid"));
			value.put("payAmount", data.get("payAmount"));
			result.add(value);
		}
		return result;
	}
	
	/**
	 * 查询结算方式明细
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param params
	 * @return
	 */
	public Map<String,Object> querySettList(Map<String,Object> params){
		Map<String,Object> map = tSettlementDetailChildDao.querySettList(params);
		String payWayName = (String)map.get("payWayName");
	    String membercardno = (String)map.get("membercardno");
	    String itemid = (String)map.get("itemid");
	    Map<String,Object> obj = new HashMap<String,Object>();
	    if(StringUtils.isNotBlank(itemid) && itemid.equals("1")&&StringUtils.isNotBlank(membercardno)){
	    	Map<String,Object> namemap = tSettlementDetailChildDao.queryPayName(params);
	    	if(namemap!=null&&namemap.containsKey("itemDesc")){
	    		payWayName = payWayName+"-"+String.valueOf(namemap.get("itemDesc"));
	    	}
		}
	    obj.put("payWayName", payWayName);
	    obj.put("nums", map.get("nums"));
	    obj.put("price", map.get("price"));
		return obj;
	}
}
