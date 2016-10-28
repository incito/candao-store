package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.ReportExportDao;
import com.candao.www.utils.DateTimeUtils;
import com.candao.www.webroom.service.ReportExportService;

/**
 * 报表导出接口
 * @since 2015-6-5
 *
 */
@Service
public class ReportExportServiceImpl implements ReportExportService{

	@Autowired
	private ReportExportDao reportExportDao;
	
	/**
	 * 品项售卖份数top10
	 * @author weizhifang
	 * @since 2015-6-6
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemDishNumTop10(Map<String,Object> params){
		getformatDayParam(params);
		return reportExportDao.getItemDishNumTop10(params);
	}
	
	/**
	 * 品项售卖份数top10趋势图
	 * @author weizhifang
	 * @since 2015-6-6
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemDishNumTop10Trend(List<Map<String,Object>> dishNumTrendList,Map<String,Object> params){
		List<Map<String,Object>> numList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map : dishNumTrendList){
			HashMap<String, Object> mapNum = new HashMap<String, Object>();
			String dishid = map.get("dishid").toString();
			String dishunit = map.get("dishunit").toString();
			params.put("dishid", dishid);
			params.put("dishunit", dishunit);
			getformatDayParam(params);
			List<Map<String,Object>> itemList = reportExportDao.getItemDishNumTop10Trend(params);
			List<Map<String,Object>> it = setItemDate(itemList,params);
			mapNum.put("name", map.get("itemdesc").toString());
			mapNum.put("list", it);
			numList.add(mapNum);
		}
		return numList;
	}
	
	/**
	 * 组装返回值
	 * @author weizhifang
	 * @since 2015-6-7
	 * @param itemList
	 * @param params
	 * @return
	 */
	private List<Map<String,Object>> setItemDate(List<Map<String,Object>> itemList,Map<String,Object> params){
		List<Map<String,Object>> it = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map : itemList){
			String date = map.get("statistictime").toString();
			String datetype = params.get("Datetype").toString();
			String statistictime = DateTimeUtils.formatDate(date,datetype);
			double share = (double)map.get("share");
			double num = (double)map.get("num");
			String dishunit = (String)map.get("dishunit");
			String itemdesc = map.get("itemdesc").toString();
			Map<String,Object> nm = new HashMap<String,Object>();
			nm.put("itemdesc", itemdesc);
			nm.put("num", Math.round(num));
			nm.put("share", new java.text.DecimalFormat("#.00").format(share));
			nm.put("statistictime", statistictime);
			nm.put("dishunit", dishunit);
			it.add(nm);
		}
		return it;
	}
	
	/**
	 * 品项售卖金额top10top10
	 * @author weizhifang
	 * @since 2015-6-6
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemAmountTop10(Map<String,Object> params){
		getformatDayParam(params);
		return reportExportDao.getItemAmountTop10(params);
	}
	
	
	/**
	 * 格式化日期格式
	 * @param params
	 * @param beginTime
	 * @param datetype
	 */
	private Map<String, Object> getformatDayParam(Map<String,Object> params) {
		String beginTime = params.get("beginTime").toString();
		String endTime = params.get("endTime").toString();
		String datetype = params.get("Datetype").toString();
		if (StringUtils.equals(datetype, "D")) {//日查询
			params.put("beginTime", beginTime);
			params.put("endTime", endTime);
			params.put("Datetype", datetype);
		}
		if (StringUtils.equals(datetype, "M")) {//月查询
			int beginmonth = Integer.parseInt(beginTime.split("-")[1]);
			int endmonth = Integer.parseInt(endTime.split("-")[1]);
			int beginyear = Integer.parseInt(beginTime.split("-")[0]);
			int endyear = Integer.parseInt(endTime.split("-")[0]);
			params.put("beginTime", DateTimeUtils.getMonthFirstTime(beginmonth, beginyear));
			params.put("endTime", DateTimeUtils.getMonthLastTime(endmonth,endyear));
			params.put("Datetype", datetype);
		}
		if (StringUtils.equals(datetype, "Y")) {//月查询
			int beginyear = Integer.parseInt(beginTime.split("-")[0]);
			int endyear = Integer.parseInt(endTime.split("-")[0]);
			params.put("beginTime", DateTimeUtils.getMonthFirstTime(1,beginyear));
			params.put("endTime", DateTimeUtils.getMonthLastTime(12,endyear));
			params.put("Datetype", datetype);
		}
		return params;
	}
	
}
