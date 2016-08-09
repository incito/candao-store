package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.DateUtils;
import com.candao.www.data.member.MemberMapper;
import com.candao.www.webroom.service.MemberService;

/**
 * 
 * 礼物列表操作接口
 * @author Administrator
 *
 */
@Service
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private  MemberMapper memberMapper;
	
	@Autowired
	private SystemServiceImpl systemService;

	/**
	 * 
	 *查询指定门店，指定时间段，指定类型的会员操作数据汇总 
	 * 
	 */
	@Override
	public Map<String, String> queryMemberDealInfos(String branchId, List<Integer> typeList, String beginTime,String endTime, String shiftid,String cardno) {
		//定义返回值
		Map<String, String> infoMap= new HashMap<String,String>();
		
		infoMap.put("timeslot", beginTime.substring(0, 10)+"-"+endTime.substring(0, 10));//操作时间
		infoMap.put("stored", "0.00");//储值金额
		infoMap.put("deposit", "0.00");//工本费
		infoMap.put("total", "0.00");//合计
		infoMap.put("present", "0.00");//会员储值赠送
		infoMap.put("numbers", "0");//操作笔数
		
		//判断参数是否符合需求
		if(StringUtils.isBlank(branchId)){
			return infoMap;
		}
		if(StringUtils.isBlank(beginTime)){
			return infoMap;
		}
		if(StringUtils.isBlank(endTime)){
			return infoMap;
		}
		if(StringUtils.isBlank(cardno)){
			return infoMap;
		}
		if(StringUtils.isBlank(shiftid)){
			return infoMap;
		}
		if(typeList==null||typeList.size()<=0){
		    return infoMap;	
		}
		
		List<Map<String,Object>> infolist = memberMapper.queryMemberDealInfos(branchId, typeList, beginTime, endTime, cardno);
		
		if(infolist==null||infolist.size()<=0){
			return infoMap;
		}
		String businessTime = "";
		if(!shiftid.equals("-1")){
			businessTime = systemService.getBusinessTime(shiftid);
		}
		BigDecimal totalamount = new BigDecimal(0);
		BigDecimal totalpresentvalue = new BigDecimal(0);
		BigDecimal allValue = new BigDecimal(0);
		int numbers = 0;
		
		for(Map<String,Object> map : infolist){
			String dealTime = map.containsKey("deal_time")?String.valueOf(map.get("deal_time")):"";
			Integer dealType = map.containsKey("deal_type")?parInt(String.valueOf(map.get("deal_type"))):0;
			BigDecimal amount = map.containsKey("amount")?new BigDecimal(String.valueOf(map.get("amount"))):new BigDecimal(0);
			BigDecimal presentvalue = map.containsKey("present_value")?new BigDecimal(String.valueOf(map.get("present_value"))):new BigDecimal(0);
			
			if(StringUtils.isBlank(dealTime)){
				continue;
			}
			if(dealType<0){
				continue;
			}
			if(amount.compareTo(new BigDecimal(0))<=0){
				continue;
			}
			if(!shiftid.equals("-1")){//判断是否在午市，晚市范围
				String tempTime = dealTime.substring(0, 10)+" "+businessTime;
				if(shiftid.equals("0")){
					if(DateUtils.parse(tempTime,"yyyy-MM-dd HH:mm").after(DateUtils.parse(dealTime,DateUtils.DEFAULT_TIME_FORMAT))){
						continue;
					}
				}else if(shiftid.equals("1")){
					if(DateUtils.parse(tempTime,"yyyy-MM-dd HH:mm").before(DateUtils.parse(dealTime,DateUtils.DEFAULT_TIME_FORMAT))){
						continue;
					}
				}
			}
			totalamount = totalamount.add(amount);
			totalpresentvalue = totalpresentvalue.add(presentvalue);
			allValue = allValue.add(amount);
			numbers++;
		}
		infoMap.put("stored", totalamount.toString());//储值金额
		infoMap.put("total", allValue.toString());//合计
		infoMap.put("numbers", numbers+"");//操作笔数
		infoMap.put("present", totalpresentvalue.toString());//会员储值赠送
		return infoMap;
	}
	
	
	/**
	 * 
	 *查询指定门店，指定时间段，指定类型的会员操作数据汇总 
	 * 
	 */
	@Override
	public List<Map<String, String>> queryMemberDealInfosByDay(String branchId, List<Integer> typeList, String beginTime,String endTime, String shiftid,String cardno) {
		
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		//定义返回值
		Map<String,Map<String, String>> infoMap= new HashMap<String,Map<String,String>>();
		
		//判断参数是否符合需求
		if(StringUtils.isBlank(branchId)){
			return returnList;
		}
		if(StringUtils.isBlank(beginTime)){
			return returnList;
		}
		if(StringUtils.isBlank(endTime)){
			return returnList;
		}
		if(StringUtils.isBlank(shiftid)){
			return returnList;
		}
		if(StringUtils.isBlank(cardno)){
			return returnList;
		}
		if(typeList==null||typeList.size()<=0){
		    return returnList;	
		}
		
		List<Map<String,Object>> infolist = memberMapper.queryMemberDealInfos(branchId, typeList, beginTime, endTime, cardno);
		
		if(infolist==null||infolist.size()<=0){
			return returnList;
		}
		String businessTime = "";
		if(!shiftid.equals("-1")){
			businessTime = systemService.getBusinessTime(shiftid);
		}
		for(Map<String,Object> map : infolist){
			String dealTime = map.containsKey("deal_time")?String.valueOf(map.get("deal_time")):"";
			Integer dealType = map.containsKey("deal_type")?parInt(String.valueOf(map.get("deal_type"))):0;
			BigDecimal amount = map.containsKey("amount")?new BigDecimal(String.valueOf(map.get("amount"))):new BigDecimal(0);
			BigDecimal presentvalue = map.containsKey("present_value")?new BigDecimal(String.valueOf(map.get("present_value"))):new BigDecimal(0);
			
			if(StringUtils.isBlank(dealTime)){
				continue;
			}
			if(dealType<0){
				continue;
			}
			if(amount.compareTo(new BigDecimal(0))<=0){
				continue;
			}
			if(!shiftid.equals("-1")){//判断是否在午市，晚市范围
				String tempTime = dealTime.substring(0, 10)+" "+businessTime;
				if(shiftid.equals("0")){
					if(DateUtils.parse(tempTime,"yyyy-MM-dd HH:mm").after(DateUtils.parse(dealTime,DateUtils.DEFAULT_TIME_FORMAT))){
						continue;
					}
				}else if(shiftid.equals("1")){
					if(DateUtils.parse(tempTime,"yyyy-MM-dd HH:mm").before(DateUtils.parse(dealTime,DateUtils.DEFAULT_TIME_FORMAT))){
						continue;
					}
				}
			}
			String day = dealTime.substring(0, 10);
			
			Map<String, String> tempMap = infoMap.containsKey(day)?infoMap.get(day):getMapInfo();
			
			
			tempMap.put("stored", new BigDecimal(tempMap.get("stored")).add(amount).toString());//储值金额
			tempMap.put("total", new BigDecimal(tempMap.get("total")).add(amount).toString());//合计
			tempMap.put("numbers", String.valueOf(parInt(tempMap.get("numbers"))+1));//操作笔数
			tempMap.put("present", new BigDecimal(tempMap.get("present")).add(presentvalue).toString());//会员储值赠送
			
			infoMap.put(day, tempMap);
		}
		for(Entry<String, Map<String,String>> entry:infoMap.entrySet()){
			if(StringUtils.isBlank(entry.getKey())||entry.getValue()==null){
				continue;
			}
			entry.getValue().put("day", entry.getKey());
			
			returnList.add(entry.getValue());
		}
		
		return returnList;
	}
	
	
	private int parInt(String value){
		try{
			return Integer.parseInt(value);
		}catch(Exception ex){
			
		}
		return -1;
	}
	
	/**
	 * 
	 *查询指定门店，指定时间段，指定类型的会员操作数据汇总 
	 * 
	 */
	@Override
	public List<Map<String, String>> queryMemberDealInfosToTime(String branchId, List<Integer> typeList, String beginTime,String endTime, String shiftid,String cardno) {
		
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		/*//定义返回值
		List<Map<String, String>> infoList = new ArrayList<Map<String,String>>();*/
		
		//判断参数是否符合需求
		if(StringUtils.isBlank(branchId)){
			return returnList;
		}
		if(StringUtils.isBlank(beginTime)){
			return returnList;
		}
		if(StringUtils.isBlank(endTime)){
			return returnList;
		}
		if(StringUtils.isBlank(shiftid)){
			return returnList;
		}
		if(StringUtils.isBlank(cardno)){
			return returnList;
		}
		if(typeList==null||typeList.size()<=0){
		    return returnList;	
		}
		
		List<Map<String,Object>> infolist = memberMapper.queryMemberDealInfosToTime(branchId, typeList, beginTime, endTime, cardno);
		
		if(infolist==null||infolist.size()<=0){
			return returnList;
		}
		String businessTime = "";
		if(!shiftid.equals("-1")){
			businessTime = systemService.getBusinessTime(shiftid);
		}
		for(Map<String,Object> map : infolist){
			String dealTime = map.containsKey("deal_time")?String.valueOf(map.get("deal_time")):"";
			String cardnoStr = map.containsKey("cardno")?String.valueOf(map.get("cardno")):"";
			String mobile = map.containsKey("mobile")?String.valueOf(map.get("mobile")):"";
			String name = map.containsKey("name")?String.valueOf(map.get("name")):"";
			String level = map.containsKey("level")?String.valueOf(map.get("level")):"";
			Integer dealType = map.containsKey("deal_type")?parInt(String.valueOf(map.get("deal_type"))):0;
			BigDecimal amount = map.containsKey("mobile")?new BigDecimal(String.valueOf(map.get("amount"))):new BigDecimal(0);
			BigDecimal actual_value = map.containsKey("actual_value")?new BigDecimal(String.valueOf(map.get("actual_value"))):new BigDecimal(0);
			BigDecimal value = map.containsKey("value")?new BigDecimal(String.valueOf(map.get("value"))):new BigDecimal(0);
			String deal_user = map.containsKey("deal_user")?String.valueOf(map.get("deal_user")):"";
			
			if(StringUtils.isBlank(dealTime)){
				continue;
			}
			if(dealType<0){
				continue;
			}
			if(amount.compareTo(new BigDecimal(0))<=0){
				continue;
			}
			if(!shiftid.equals("-1")){//判断是否在午市，晚市范围
				String tempTime = dealTime.substring(0, 10)+" "+businessTime;
				if(shiftid.equals("0")){
					if(DateUtils.parse(tempTime,"yyyy-MM-dd HH:mm").after(DateUtils.parse(dealTime,DateUtils.DEFAULT_TIME_FORMAT))){
						continue;
					}
				}else if(shiftid.equals("1")){
					if(DateUtils.parse(tempTime,"yyyy-MM-dd HH:mm").before(DateUtils.parse(dealTime,DateUtils.DEFAULT_TIME_FORMAT))){
						continue;
					}
				}
			}
			/*String day = dealTime.substring(0, 10);
			
			Map<String, String> tempMap = infoMap.containsKey(day)?infoMap.get(day):getMapInfo();
			*/
			Map<String, String> tempMap = new HashMap<String, String>();
			/*tempMap.put("stored", new BigDecimal(tempMap.get("stored")).add(amount).toString());//储值金额
			tempMap.put("total", new BigDecimal(tempMap.get("total")).add(amount).toString());//合计
			tempMap.put("numbers", String.valueOf(parInt(tempMap.get("numbers"))+1));//操作笔数
			tempMap.put("present", new BigDecimal(tempMap.get("present")).add(presentvalue).toString());//会员储值赠送
			
*/		    tempMap.put("dealTime", dealTime);
            tempMap.put("cardno", cardnoStr);
            tempMap.put("mobile", mobile);
            tempMap.put("name", name);
            tempMap.put("level", level);
            tempMap.put("value", value.toString());
            tempMap.put("dealType", dealType.toString());
            tempMap.put("amount",amount.toString());
            tempMap.put("actual_value", actual_value.toString());
            tempMap.put("deal_user", deal_user);
			
            returnList.add(tempMap);
		}
		
		return returnList;
	}

	
	
	private Map<String,String> getMapInfo(){
		Map<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("stored", "0.00");//储值金额
		tempMap.put("deposit", "0.00");//工本费
		tempMap.put("total", "0.00");//合计
		tempMap.put("present", "0.00");//会员储值赠送
		tempMap.put("numbers", "0");//操作笔数
		return tempMap;
	}
	
}
