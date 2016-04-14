package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.BizDao;
import com.candao.www.webroom.service.BizService;

/**
 * 
 * 礼物列表操作接口
 * @author Administrator
 *
 */
@Service
public class BizServiceImpl implements BizService{
	
	@Autowired
	private BizDao bizDao;


	@Override
	public List<Map<String, String>> getBizInfos(String branchId,String beginTime,String endTime) {
		
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		
		if(StringUtils.isBlank(branchId)||StringUtils.isBlank(beginTime)||StringUtils.isBlank(endTime)){
			return returnList;
		}
		if(beginTime.length()==7){
			beginTime = beginTime+" 00:00:00";
		}else if(beginTime.length()==10){
			beginTime = beginTime+"-01 00:00:00";
		}
		if(endTime.length()==7){
			endTime = endTime+" 23:59:59";
		}else if(endTime.length()==10){
			endTime = endTime+"-31 23:59:59";
		}
		
		List<Map<String, Object>> userMapList = bizDao.getUsers(branchId);
		if(userMapList==null||userMapList.size()<=0){
			return returnList;
		}
		Map<String, String> userMap = new HashMap<String,String>();
		for(Map<String, Object> map : userMapList){
			if(map==null){
				continue;
			}
			userMap.put(map.containsKey("job")?String.valueOf(map.get("job")):"", map.containsKey("userName")?String.valueOf(map.get("userName")):"");
		}
		List<Map<String, Object>> bizInfoList = bizDao.getBizInfos(beginTime,endTime,branchId);
		if(bizInfoList==null||bizInfoList.size()<=0){
			return returnList;
		}
		
		for(Map<String, Object> bizInfo: bizInfoList){
			if(bizInfo==null||bizInfo.size()!=4){
				continue;
			}
			String openuser = bizInfo.containsKey("openuser")?String.valueOf(bizInfo.get("openuser")):"";
			String enduser = bizInfo.containsKey("enduser")?String.valueOf(bizInfo.get("enduser")):"";
			String opentime =  bizInfo.containsKey("opendate")?String.valueOf(bizInfo.get("opendate")):"";
			String completiontime =  bizInfo.containsKey("enddate")?String.valueOf(bizInfo.get("enddate")):"";
			if(opentime.length()>19){
				opentime = opentime.substring(0, 19);
			}
			if(completiontime.length()>19){
				completiontime = completiontime.substring(0, 19);
			}
			Map<String, String> infoMap = new HashMap<String, String>();
			infoMap.put("opentime",opentime.substring(0, 16));
			infoMap.put("openauthorized", userMap.containsKey(openuser)?String.valueOf(userMap.get(openuser)):"");
			infoMap.put("completiontime", completiontime.substring(0, 16));
			infoMap.put("completionauthorized", userMap.containsKey(enduser)?String.valueOf(userMap.get(enduser)):"");
			returnList.add(infoMap);
		}
		
		return returnList;
	}
	
	@Override
	public List<Map<String, String>> getBizNodeClassInfos(String branchId,String beginTime,String endTime) {
		
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		
		
		if(StringUtils.isBlank(branchId)||StringUtils.isBlank(beginTime)||StringUtils.isBlank(endTime)){
			return returnList;
		}
		List<Map<String, Object>> bizInfoList = bizDao.getBizNodeClassInfos(beginTime,endTime,branchId);
		if(bizInfoList==null||bizInfoList.size()<=0){
			return returnList;
		}
		
		for(Map<String, Object> bizInfo: bizInfoList){
			if(bizInfo==null||bizInfo.size()!=5){
				continue;
			}
			Map<String, String> infoMap = new HashMap<String, String>();
			infoMap.put("cashier", bizInfo.containsKey("operatorName")?String.valueOf(bizInfo.get("operatorName")):"");
			infoMap.put("starttime", bizInfo.containsKey("vIn")?String.valueOf(bizInfo.get("vIn")):"");
			infoMap.put("endtime", bizInfo.containsKey("vOut")?String.valueOf(bizInfo.get("vOut")):"");
			infoMap.put("endauthorized", bizInfo.containsKey("authorizer")?String.valueOf(bizInfo.get("authorizer")):"");
			returnList.add(infoMap);
		}
		return returnList;
	}
}
