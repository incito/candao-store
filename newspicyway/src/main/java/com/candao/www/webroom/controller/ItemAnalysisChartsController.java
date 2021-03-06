package com.candao.www.webroom.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.utils.DateTimeUtils;
import com.candao.www.webroom.service.ItemAnalysisChartsService;

/**
 * 品项销售图表
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/itemAnalysisCharts")
public class ItemAnalysisChartsController {

	@Autowired
	private ItemAnalysisChartsService itemAnalysisChartsService;
	
	/**
	 * 营业分析-品项销售统计
	 * @author weizhifang
	 * @since 2015-06-06
	 * @param params
	 * @return
	 */
	@RequestMapping("/getItemReport")
	@ResponseBody
	public ModelAndView getItemReportForView(@RequestParam Map<String, Object> params) {
		setParameter(params);
		ModelAndView mav = new ModelAndView();
		List<Map<String,Object>> itemCharsList = itemAnalysisChartsService.itemAnalysisChartsForPro(params);
		//售卖份数top10
		List<Map<String,Object>> dishNumList = itemAnalysisChartsService.getItemDishNumTop10(itemCharsList);
		//售卖份数top10趋势图
		List<Map<String,Object>> dishNumTrendList = itemAnalysisChartsService.getItemDishNumTop10Trend(dishNumList);
		//售卖金额top10
		List<Map<String,Object>> amountList = itemAnalysisChartsService.getItemAmountTop10(itemCharsList);
		//售卖金额top10趋势图
		List<Map<String,Object>> amountTrendList = itemAnalysisChartsService.getItemAmountTop10trend(amountList);
		mav.addObject("ItemReportList", dishNumList);
		mav.addObject("ItemNumQushiReport", dishNumTrendList);
		mav.addObject("ItemSharezhuzhuangReport", amountList);
		mav.addObject("ItemShareQushiReport", amountTrendList);
		return mav;
	}
	
	/**
	 * 营业分析-品项销售千次统计(指定分类统计)
	 * @author YANGZHONGLI
	 * @since 2015-06-06
	 * @param params
	 * @return
	 */
	@RequestMapping("/getColumnItemThousandsTimesReportForView")
	@ResponseBody
	public ModelAndView getColumnItemThousandsTimesReportForView(@RequestParam Map<String, Object> params) {
		setParameter(params);
		ModelAndView mav = new ModelAndView();
		params.put("beginTime", String.valueOf(params.get("beginTime"))+" 00:00:00");
		params.put("endTime", String.valueOf(params.get("endTime"))+" 23:59:59");
		List<Map<String,String>> itemCharsList = itemAnalysisChartsService.getColumnItemThousandsTimesReportForView(params);
		mav.addObject("thousandsTimesInfo", itemCharsList);
		return mav;
	}
	
	/**
     * 设置查询参数
     * @author weizhifang
     * @since 2015-07-04
     * @param params
     * @return
     */
    private Map<String,Object> setParameter(Map<String,Object> params){
    	String branchId = "";
    	if(params.get("branchId") != null && !"".equals(params.get("branchId"))){
    		branchId = params.get("branchId").toString();
    	}else{
    		branchId = PropertiesUtils.getValue("current_branch_id");
    	}
    	String columnid  = "";
    	if(params.get("columnid") != null && !"".equals(params.get("columnid"))){
    		columnid = params.get("columnid").toString();
    	}
    	
    	String type  = "";
    	if(params.get("type") != null && !"".equals(params.get("type"))){
    		type = params.get("type").toString();
    	}
    	params.put("columnid", columnid);
    	params.put("branchId", branchId);
    	params.put("type", type);
    	String beginTime = params.get("beginTime").toString();
		String endTime = params.get("endTime").toString();
		if(params.get("Datetype").equals("D")){
			params.put("dateType", 0);
		}else if(params.get("Datetype").equals("M")){
			params.put("dateType", 1);
			int beginmonth = Integer.parseInt(beginTime.split("-")[1]);
			int endmonth = Integer.parseInt(endTime.split("-")[1]);
			int beginyear = Integer.parseInt(beginTime.split("-")[0]);
			int endyear = Integer.parseInt(endTime.split("-")[0]);
			params.put("beginTime", DateTimeUtils.getMonthFirstTime(beginmonth, beginyear));
			params.put("endTime", DateTimeUtils.getMonthLastTime(endmonth,endyear));
		}
		return params;
    }
    /**
	 * 营业分析-品项销售统计(指定分类统计)
	 * @author weizhifang
	 * @since 2015-06-06
	 * @param params
	 * @return
	 */
	@RequestMapping("/getColumnItemReport")
	@ResponseBody
	public ModelAndView getColumnItemReportForView(@RequestParam Map<String, Object> params) {
		setParameter(params);
		ModelAndView mav = new ModelAndView();
		List<Map<String,Object>> itemCharsList = itemAnalysisChartsService.itemAnalysisChartsForColumnPro(params);
		if(String.valueOf(params.get("type")).equals("0")){//份数
			//售卖份数top10
			List<Map<String,Object>> dishNumList = itemAnalysisChartsService.getItemDishNumTop10(itemCharsList);
			//售卖份数top10趋势图
			List<Map<String,Object>> dishNumTrendList = itemAnalysisChartsService.getItemDishNumTop10Trend(dishNumList);
			mav.addObject("ItemReportList", dishNumList);
			mav.addObject("ItemNumQushiReport", dishNumTrendList);
		}
        if(String.valueOf(params.get("type")).equals("1")){//金额
        	//售卖金额top10
    		List<Map<String,Object>> amountList = itemAnalysisChartsService.getItemAmountTop10(itemCharsList);
    		//售卖金额top10趋势图
    		List<Map<String,Object>> amountTrendList = itemAnalysisChartsService.getItemAmountTop10trend(amountList);
    		mav.addObject("ItemSharezhuzhuangReport", amountList);
    		mav.addObject("ItemShareQushiReport", amountTrendList);
		}
		return mav;
	}
}
