//package com.candao.www.webroom.controller;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//import com.candao.common.page.Page;
//import com.candao.common.utils.timeSlotUtils;
//import com.candao.www.webroom.model.TjObj;
//import com.candao.www.webroom.service.DayIncomeBillService;
//
//@Controller
//@RequestMapping("/salesReport")
//public class salesReportController {
//
//@Autowired	
//private DayIncomeBillService dayIncomeBillService;
//	
//@RequestMapping("/index")
//
//	
//	public ModelAndView index(@RequestParam Map<String, Object> params) {
//			Map map = timeSlotUtils.choose(0);
//				params.put("begintime", map.get("begintime")+"0000");
//				params.put("endtime", map.get("endtime")+"0000");
//		
//			List<TjObj> reportlist= dayIncomeBillService.getDaliyReport(params);
//			ModelAndView mav = new ModelAndView("salesReport/salesReport");
//			mav.addObject("datas", reportlist);
//			return mav;
//	
//}	
//@RequestMapping("/getDayReportList")
//@ResponseBody
//public ModelAndView getReportList(
//		@RequestParam(value = "begintime", defaultValue = "") String begintime,
//		@RequestParam(value = "endtime", defaultValue = "") String endtime,
//		@RequestParam(value = "hour", defaultValue = "") int hour){
//	Map<String, Object> params = new HashMap<String, Object>();
//	Map map = timeSlotUtils.choose(0);
//	if(begintime==""||endtime==""){
//		params.put("begintime", map.get("begintime")+"0000");
//		params.put("endtime", map.get("endtime")+"0000");
//	}else{
//		params.put("begintime", begintime);
//		params.put("endtime", endtime);
//	}
// 