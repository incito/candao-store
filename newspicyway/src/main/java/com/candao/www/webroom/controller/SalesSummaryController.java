package com.candao.www.webroom.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.FindDates;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.common.utils.ValidateUtils;
import com.candao.common.utils.timeSlotUtils;
import com.candao.print.service.PrinterService;
import com.candao.www.data.model.TbasicData;
import com.candao.www.data.model.Tdish;
import com.candao.www.utils.SessionUtils;
import com.candao.www.webroom.service.DayIncomeBillService;
import com.candao.www.webroom.service.DishService;
import com.candao.www.webroom.service.DishTypeService;
import com.candao.www.webroom.service.SalesSummaryService;

@Controller
@RequestMapping("/salesSummary")
public class SalesSummaryController {
	@Autowired
	private SalesSummaryService salesSummary;


	@RequestMapping("/index")
	public String index(){
		return "/billDetails/salesSummary";
	}
	/**
	 * 分页查询
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 * @throws ParseException 
	 * 
	 */

	@RequestMapping("/page")
	@ResponseBody
	public ModelAndView page(@RequestParam Map<String, Object> params, int page, int rows
			) throws ParseException  {
		Page<Map<String, Object>> pageMap = salesSummary.grid(params, page, rows);
//		String begintime = (String) params.get("begintime");
//		String endtime = (String) params.get("endtime");
//		if(begintime==null&&endtime==null){
//			
//			params.putAll(timeSlotUtils.choose(0));
//		}
//		List<Map<String, Object>> lists = salesSummary.dates(params);
//		Map<String, Object> map1 = new HashMap();
//		Map<String, Object> map2 = new HashMap();
//		map1.put("inserttime", (String)params.get("begintime"));
//		map2.put("inserttime", (String) params.get("endtime"));
//		System.out.println(map1);
//		System.out.println(map2);
//		List<Map<String, Object>> lists2=FindDates.findDates((String)params.get("begintime"),(String) params.get("endtime"));
//		lists2.removeAll(lists);
//		Iterator it = lists2.iterator();
//		while(it.hasNext()){
//			pageMap.getRows().add((Map<String, Object>) it.next());
//		}
//		System.out.println(lists2);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		return mav;
	}

	@RequestMapping("/exportxlsD")
	@ResponseBody
	public void exportxlsC(HttpServletRequest req, HttpServletResponse response,
			
			@RequestParam(value = "begintime", defaultValue = "") String begintime,
			@RequestParam(value = "endtime", defaultValue = "") String endtime){
		Map<String, Object> map = new HashMap<String, Object>();
	
		map.put("begintime", begintime);
		map.put("endtime", endtime);
	
		map.put("names", "营业额统计表");
		map.put("shopname", "新辣道惠新店");
		String dateShowbegin=formatDate2(begintime);
		String dateShowend=formatDate2(endtime);
		if(dateShowbegin.equals(dateShowend)){
			map.put("dateShow", dateShowbegin);
		}else{
			map.put("dateShow", dateShowbegin+"-"+dateShowend);
		}
		try {				 
			salesSummary.exportxlsD(map,req,response);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
/**
 * 2015-01-06 14:24:58  变成2015年01月06日
 * @param date
 * @return
 */
private String formatDate2(String date){
	if(date==null||"".equals(date)){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		date= sdf.format(new Date());
	}
	date=date.substring(0, 4)+"年"+date.substring(5, 7)+"月"+date.substring(8, 10)+"日";
	
	return date;
}

}
