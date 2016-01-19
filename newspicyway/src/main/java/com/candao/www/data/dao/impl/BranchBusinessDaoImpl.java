package com.candao.www.data.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.BranchBusinessDao;
import com.candao.www.data.model.TBranchBusinessInfo;
@Repository
public class BranchBusinessDaoImpl implements BranchBusinessDao {
	
	@Autowired
	private DaoSupport daoSupport;

	/**
	 * 
	 * 查询所有指定门店，指定日期的所有的数据信息
	 * 
	 * @param paramMap
	 * @return
	 */
	@Override
	public List<TBranchBusinessInfo> getBuinessInfos(Map<String, Object> paramMap) {
		return daoSupport.find(PREFIX+".getBuinessInfos",paramMap);
	}

	
	/**
	 * 
	 * 按天查询门店的数据信息
	 * 
	 * @param paramMap
	 * @return
	 */
	@Override
	public List<TBranchBusinessInfo> getBuinessInfosByDay(Map<String, Object> paramMap) {
		return daoSupport.find(PREFIX+".getBuinessInfosByDay",paramMap);
	}
	
	/**
	 * 
	 * 按天查询订单
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,String>> getBranchDayOrders(Map<String,String> paramMap){
		List<Map<String,Object>> list = daoSupport.find(PREFIX+".getBranchDayOrders",paramMap);
		if(list==null){
			list =  new ArrayList<Map<String,Object>>();
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>(); 
		for(Map<String,Object> map :list){
			Timestamp beginTime =(Timestamp)map.get("beginTime");
			Timestamp endTime =(Timestamp)map.get("endTime");
			
			Date begindate = new Date(beginTime.getTime());
			Date endDate = new Date(endTime.getTime());
			
			String orderId = map.containsKey("orderId")?String.valueOf(map.get("orderId")):"0";
			String shouldamount = map.containsKey("shouldamount")&&!String.valueOf(map.get("shouldamount")).equals("null")?String.valueOf(map.get("shouldamount")):"0";
			String paidinamount = map.containsKey("paidinamount")&&!String.valueOf(map.get("paidinamount")).equals("null")?String.valueOf(map.get("paidinamount")):"0";
			String discountamount = map.containsKey("discountamount")&&!String.valueOf(map.get("discountamount")).equals("null")?String.valueOf(map.get("discountamount")):"0";
			String cash = map.containsKey("cash")&&!String.valueOf(map.get("cash")).equals("null")?String.valueOf(map.get("cash")):"0";
			
			String card = map.containsKey("card")&&!String.valueOf(map.get("card")).equals("null")?String.valueOf(map.get("card")):"0";
			String othercard = map.containsKey("othercard")&&!String.valueOf(map.get("othercard")).equals("null")?String.valueOf(map.get("othercard")):"0";
			String weixin = map.containsKey("weixin")&&!String.valueOf(map.get("weixin")).equals("null")?String.valueOf(map.get("weixin")):"0";
			String zhifubao = map.containsKey("zhifubao")&&!String.valueOf(map.get("zhifubao")).equals("null")?String.valueOf(map.get("zhifubao")):"0";
			
			String credit = map.containsKey("credit")&&!String.valueOf(map.get("credit")).equals("null")?String.valueOf(map.get("credit")):"0";
			String merbervaluenet = map.containsKey("merbervaluenet")&&!String.valueOf(map.get("merbervaluenet")).equals("null")?String.valueOf(map.get("merbervaluenet")):"0";
			String mebervalueadd = map.containsKey("mebervalueadd")&&!String.valueOf(map.get("mebervalueadd")).equals("null")?String.valueOf(map.get("mebervalueadd")):"0";
			String integralconsum = map.containsKey("integralconsum")&&!String.valueOf(map.get("integralconsum")).equals("null")?String.valueOf(map.get("integralconsum")):"0";

			String meberTicket = map.containsKey("meberTicket")&&!String.valueOf(map.get("meberTicket")).equals("null")?String.valueOf(map.get("meberTicket")):"0";
			
			BigDecimal shouldamountbd = new BigDecimal(Double.parseDouble(shouldamount));  
			BigDecimal paidinamountbd = new BigDecimal(Double.parseDouble(paidinamount));  
			BigDecimal discountamountbd = new BigDecimal(Double.parseDouble(discountamount));  
			BigDecimal cashbd = new BigDecimal(Double.parseDouble(cash));  
			BigDecimal cardbd = new BigDecimal(Double.parseDouble(card)); 
			
			BigDecimal othercarddb= new BigDecimal(Double.parseDouble(othercard)); 
			BigDecimal weixindb = new BigDecimal(Double.parseDouble(weixin)); 
			BigDecimal zhifubaodb = new BigDecimal(Double.parseDouble(zhifubao)); 
			
			BigDecimal creditbd = new BigDecimal(Double.parseDouble(credit));  
			BigDecimal merbervaluenetbd = new BigDecimal(Double.parseDouble(merbervaluenet));  
			BigDecimal mebervalueaddbd = new BigDecimal(Double.parseDouble(mebervalueadd));  
			BigDecimal integralconsumbd = new BigDecimal(Double.parseDouble(integralconsum));  
			BigDecimal meberTicketbd = new BigDecimal(Double.parseDouble(meberTicket));
			
			Map<String,String> infoMap = new HashMap<String,String>();
			infoMap.put("beginTime", sdf.format(begindate));
			infoMap.put("endTime", sdf.format(endDate));
			infoMap.put("orderId", orderId);
			infoMap.put("shouldamount", shouldamountbd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			infoMap.put("discountamount", discountamountbd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			infoMap.put("paidinamount", paidinamountbd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			infoMap.put("cash", cashbd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			
			infoMap.put("othercard", othercarddb.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			infoMap.put("weixin", weixindb.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			infoMap.put("zhifubao", zhifubaodb.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			
			infoMap.put("card", cardbd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			infoMap.put("credit", creditbd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			infoMap.put("merbervaluenet", merbervaluenetbd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			infoMap.put("mebervalueadd", mebervalueaddbd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			infoMap.put("integralconsum", integralconsumbd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			infoMap.put("meberTicket", meberTicketbd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			
			returnList.add(infoMap);
		}
		return returnList;
	}
}
