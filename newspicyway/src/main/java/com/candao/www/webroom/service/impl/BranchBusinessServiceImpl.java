package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.BranchBusinessDao;
import com.candao.www.data.model.TBranchBusinessInfo;
import com.candao.www.webroom.service.BranchBusinessService;

/**
 * 
 * 礼物列表操作接口
 * @author Administrator
 *
 */
@Service
public class BranchBusinessServiceImpl implements BranchBusinessService{
	
	@Autowired
	private BranchBusinessDao branchBusinessDao;

	/**
	 * 
	 * 查询所有指定门店，指定日期的所有的数据信息
	 * 
	 * @param paramMap
	 * @return
	 */
	@Override
	public List<TBranchBusinessInfo> getBuinessInfos(Map<String, Object> paramMap) {
		
		List<TBranchBusinessInfo> returnList =null;
		
		returnList = branchBusinessDao.getBuinessInfos(paramMap);
		
		if(returnList==null){
			returnList	= new ArrayList<TBranchBusinessInfo>();
		}
		return returnList;
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
		List<TBranchBusinessInfo> returnList = null;
		returnList = branchBusinessDao.getBuinessInfosByDay(paramMap);
		if(returnList==null){
			returnList = new ArrayList<TBranchBusinessInfo>();
		}
		return returnList;
	}
	
	/**
	 * 
	 * 查询门店每天所有订单信息
	 * 
	 * @param paramMap
	 * @return
	 */
	@Override
	public  List<Map<String,String>> getBranchDayOrders(Map<String, String> paramMap) {
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		returnList = branchBusinessDao.getBranchDayOrders(paramMap);
		if(returnList==null){
			returnList = new ArrayList<Map<String,String>>();
		}
		return returnList;
	}
	/**
	 * 导出查询所有指定门店，指定日期的所有的数据信息/按天查询门店的数据信息
	 * @param params
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public void exportReportInfos(Map<String, Object> params, HttpServletRequest req, HttpServletResponse resp) throws Exception{
		try {
			String day = params.get("day").toString();
			if(day.equals("0")){
				List<TBranchBusinessInfo> resultList = branchBusinessDao.getBuinessInfos(params);
				String vasd ="营业报表";
				PoiExcleTest poi = new PoiExcleTest();
				poi.exportExcleInfos(resultList, params, vasd, req,resp);
			}else{
				List<TBranchBusinessInfo> resultList = branchBusinessDao.getBuinessInfosByDay(params);
				String vasd ="营业报表";
				PoiExcleTest poi = new PoiExcleTest();
				poi.exportExcleInfosDetail(resultList, params, vasd, req,resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 查询门店每天所有订单信息
	 * @param params
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public void exportReportDayOrders(Map<String, String> params, HttpServletRequest req, HttpServletResponse resp) throws Exception{
		try {
			    List<Map<String,String>>  resultList = branchBusinessDao.getBranchDayOrders(params);
				String vasd ="营业报表";
				PoiExcleTest poi = new PoiExcleTest();
				poi.exportExcleIDayOrders(resultList, params, vasd, req,resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
