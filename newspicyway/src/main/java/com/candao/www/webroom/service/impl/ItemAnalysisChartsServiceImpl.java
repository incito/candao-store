package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
}
