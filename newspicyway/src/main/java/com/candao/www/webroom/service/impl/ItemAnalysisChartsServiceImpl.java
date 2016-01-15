package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.DateUtils;
import com.candao.www.data.dao.TItemAnalysisChartsDao;
import com.candao.www.webroom.service.ItemAnalysisChartsService;

/**
 * 品项分析图表
 * @author Administrator
 *
 */
@Service
public class ItemAnalysisChartsServiceImpl implements ItemAnalysisChartsService {

	@Autowired
	private TItemAnalysisChartsDao titemAnalysisChartsDao;
	
	/**
	 * 查询品项分析图表存储过程
	 * @author weizhifang
	 * @since 2015-07-04
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemAnalysisChartsForPro(Map<String,Object> params){
		List<Map<String,Object>> itemCharsList = titemAnalysisChartsDao.itemAnalysisChartsForPro(params);
		return itemCharsList;
	}
	
	/**
	 * 品类销售份数top10
	 * @author weizhifang
	 * @since 2015-7-4
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemDishNumTop10(List<Map<String,Object>> itemCharsList){
		return setItemTop10(itemCharsList,"0");
	}
	
	/**
	 * 品类销售份数top10趋势图
	 * @author weizhifang
	 * @since 2015-7-4
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemDishNumTop10Trend(List<Map<String,Object>> dishNumList){
		return setItemTop10trend(dishNumList,"0");
	}
	
	/**
	 * 品类销售金额趋势top10
	 * @author weizhifang
	 * @since 2015-7-4
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemAmountTop10trend(List<Map<String,Object>> amountList){
		return setItemTop10trend(amountList,"1");
	}
	
	/**
	 * 品类销售金额top10
	 * @author weizhifang
	 * @since 2015-7-4
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemAmountTop10(List<Map<String,Object>> itemCharsList){
		return setItemTop10(itemCharsList,"1");
	}
	
	/**
	 * 品项销售和金额查询公共方法
	 * @author weizhifang
	 * @since 2015-07-14
	 * @param itemCharsList
	 * @param showtype
	 * @return
	 */
	private List<Map<String,Object>> setItemTop10(List<Map<String,Object>> itemCharsList,String type){
		List<Map<String,Object>> itemList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map : itemCharsList){
			Map<String,Object> newRes = new HashMap<String,Object>();
			String showtype = map.get("showtype").toString();
			if(showtype.equals(type)){
				newRes.put("itemdesc", map.get("title").toString());
				newRes.put("detailNum", map.get("detail_num").toString());
				if(type.equals("0")){
					newRes.put("num", map.get("total_num").toString());
				}else if(type.equals("1")){
					newRes.put("share", map.get("total_num").toString());
				}
				itemList.add(newRes);
			}
		}
		return itemList;
	}
	
	/**
	 * 品项趋势图查询公共方法
	 * @author weizhifang
	 * @since 2015-07-14
	 * @param itemList
	 * @return
	 */
	private List<Map<String,Object>> setItemTop10trend(List<Map<String,Object>> itemList,String type){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map : itemList){
			List<Map<String,Object>> it = new ArrayList<Map<String,Object>>();
			Map<String,Object> max = new HashMap<String,Object>(); 
			String numStr = map.get("detailNum").toString();
			String itemdesc = map.get("itemdesc").toString();
			String arr [] = numStr.split("\\|");
			for(int i=0;i<arr.length;i++){
				Map<String,Object> newRes = new HashMap<String,Object>();
				String str[] = arr[i].split(",");
				String statistictime = str[0];
				newRes.put("itemdesc", itemdesc);
				if(type.equals("0")){
					newRes.put("num", str[1]);
				}else if(type.equals("1")){
					newRes.put("share", str[1]);
				}
				newRes.put("statistictime", statistictime);
				it.add(newRes);
			}
			max.put("name", itemdesc);
			max.put("list", it);
			resultList.add(max);
		}
		return resultList;
	}
	/**
	 * 查询品项分析图表存储过程(指定分类统计)
	 * @author weizhifang
	 * @since 2015-07-04
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemAnalysisChartsForColumnPro(Map<String,Object> params){
		List<Map<String,Object>> itemCharsList = titemAnalysisChartsDao.itemAnalysisChartsForColumnPro(params);
		return itemCharsList;
	}
	
	/**
	 * 查询品项的销售千次信息
	 * 
	 */
	@Override
	public List<Map<String, String>> getColumnItemThousandsTimesReportForView(Map<String, Object> params) {
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		//第一步：获取所有的订单详情信息以及订单人数信息
		List<Map<String,Object>> orderList = titemAnalysisChartsDao.getAllOrderInfo(params);
		
		List<Map<String, Object>> custnumMapList = titemAnalysisChartsDao.getAllOrderCustnum(params);
		
		if(orderList==null||orderList.size()<=0){
			return returnList;
		}
		Map<String, BigDecimal> custnumMap = new HashMap<String, BigDecimal>();
		for(Map<String, Object> map : custnumMapList){
			custnumMap.put(String.valueOf(map.get("time")), new BigDecimal(String.valueOf(map.get("custnum"))));
		}
		Map<String,Double> dishnumMap = new HashMap<String,Double>();
		List<String> dateList = null;
 		//第二步：遍历所有的订单信息
		for(Map<String,Object> orderInfoMap : orderList){
			if(!orderInfoMap.containsKey("dishid")||StringUtils.isBlank(String.valueOf(orderInfoMap.get("dishid")))||!orderInfoMap.containsKey("dishnum")||StringUtils.isBlank(String.valueOf(orderInfoMap.get("dishnum")))||!orderInfoMap.containsKey("custnum")||StringUtils.isBlank(String.valueOf(orderInfoMap.get("custnum")))||!orderInfoMap.containsKey("begintime")||StringUtils.isBlank(String.valueOf(orderInfoMap.get("begintime")))){
				continue;
			}
			String dishid = String.valueOf(orderInfoMap.get("dishid"));
			if(StringUtils.isBlank(dishid)){
				continue;
			}
			String begintime = String.valueOf(orderInfoMap.get("begintime"));
			if(StringUtils.isBlank(begintime)||begintime.length()<19){
				continue;
			}
			double dishnum = parseDouble(String.valueOf(orderInfoMap.get("dishnum")));
			if(dishnum<=0){
				continue;
			}
			int custnum = parseInt(String.valueOf(orderInfoMap.get("custnum")));
			if(custnum<=0){
				continue;
			}
			String time = "";
			if(String.valueOf(params.get("dateType")).equals("0")){//天
				time = begintime.substring(0, 10);
				dateList = DateUtils.getDateDayArrayByParams(String.valueOf(params.get("beginTime")), String.valueOf(params.get("endTime")));
			}else if(String.valueOf(params.get("dateType")).equals("1")){//月
				time = begintime.substring(0, 7);
				dateList = DateUtils.getDateMonthArrayByParams(String.valueOf(params.get("beginTime")), String.valueOf(params.get("endTime")));
			}
			dishnumMap.put(time, dishnumMap.containsKey(time)?dishnumMap.get(time)+dishnum:dishnum);
		}
		
		DecimalFormat df = new DecimalFormat("###00.00"); 
		for(Entry<String, Double> entry : dishnumMap.entrySet()){
			Map<String,String> infoMap = new HashMap<String,String>();
			if(entry==null||entry.getValue()==null||entry.getKey()==null||StringUtils.isBlank(entry.getKey())||entry.getValue()<=0){
				continue;
			}
			double dishnum =entry.getValue();
			if(dishnum<=0){
				continue;
			}
			BigDecimal cuntnum = custnumMap.containsKey(entry.getKey())?new BigDecimal(String.valueOf(custnumMap.get(entry.getKey()))):new BigDecimal(0);
			if(cuntnum.compareTo(new BigDecimal(0))<=0){
				continue;
			}
			infoMap.put("time",entry.getKey());
			infoMap.put("thousandstimes", df.format(dishnum/cuntnum.doubleValue()*1000));
			returnList.add(infoMap);
		}
		if(dateList!=null&&dateList.size()>0){
			for(String date : dateList){
				if(dishnumMap.containsKey(date)){
					continue;
				}
				Map<String,String> infoMap = new HashMap<String,String>();
				infoMap.put("time",date);
				infoMap.put("thousandstimes", "0.00");
				returnList.add(infoMap);
			}
		}
		
		Collections.sort(returnList,new ThousandstimesComparator());
		
		return returnList;
	}
	private int parseInt(String value){
		try{
			return Integer.parseInt(value);
		}catch(Exception ex){
			return 0;
		}
	}
	
	private double parseDouble(String value){
		try{
			return Double.parseDouble(value);
		}catch(Exception ex){
			return 0.0;
		}
	}
	
	 class ThousandstimesComparator implements Comparator<Map<String, String>>{

		    /**
		     * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
		     */
		    @Override
		    public int compare(Map<String, String> o1, Map<String, String> o2) {
		    	String time1 = o1.get("time");
		    	String time2 = o2.get("time");
		    	if(time1.length()==7){
		    		Date acceptTime1=DateUtils.parse(time1,"yyyy-MM");
			        Date acceptTime2=DateUtils.parse(time2,"yyyy-MM");
			        if(acceptTime1.after(acceptTime2)) return 1;
		    	}else{
		    		Date acceptTime1=DateUtils.parse(time1);
			        Date acceptTime2=DateUtils.parse(time2);
			        if(acceptTime1.after(acceptTime2)) return 1;
		    	}
		        
		        return -1;
		    }

		}
}
