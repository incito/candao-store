package com.candao.www.webroom.controller;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.DateUtils;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.utils.DateTimeUtils;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.model.CouponsReptDtail;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.PreferentialAnalysisChartsService;

/**
 * 优惠分析图表
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/preferentialAnalysisCharts")
public class PreferentialAnalysisChartsController {
	@Autowired
	private PreferentialAnalysisChartsService preferentialAnalysisChartsService;
	@Autowired
	private ItemDetailService itemDetailService;
	/**
	 * 优惠报表统计明细
	 *  @author zhouyao
	 *  @serialData 2015-07-05
	 */
	@RequestMapping("/findPreferential")
	public ModelAndView findPreferential(@RequestParam Map<String, Object> params) {
		ModelAndView mod = new ModelAndView();
		try {
			String bankcardno = params.get("bankcardno").toString();
			bankcardno = URLDecoder.decode(bankcardno,"UTF-8");
			params.put("bankcardno",bankcardno);
			List<Map<String,Object>> CouponsReptList = preferentialAnalysisChartsService.insertPreferential(params);
			mod.addObject("CouponsReptList", CouponsReptList);
		} catch (Exception e) {
			e.getStackTrace();
		}
		return mod;
	}
	/**
	 * 优惠报表统计明细-子项
	 *  @author zhouyao
	 *  @serialData 2015-07-05
	 */
	@RequestMapping("/findPreferentialDetail")
	public ModelAndView findPreferentialDetail(@RequestParam Map<String, Object> params) {
		ModelAndView mod = new ModelAndView();
		try {
			String couponsname = params.get("couponsname").toString();
			couponsname = URLDecoder.decode(couponsname,"UTF-8");
			params.put("couponsname",couponsname);
			List<Map<String,Object>>  CouponsReptDtailList = preferentialAnalysisChartsService.insertPreferentialDetail(params);
			mod.addObject("CouponsReptDtailList", CouponsReptDtailList);
		} catch (Exception e) {
			e.getStackTrace();
		}
		return mod;
	}
	/**
	 *  优惠数据统计图表
	 *  @author zhouyao
	 *  @serialData 2015-07-05
	 */
	@RequestMapping("/findPreferentialView")
	public ModelAndView findPreferentialView(@RequestParam Map<String, Object> params) {
		ModelAndView mod = new ModelAndView();
		List<Object> mapCouDtalTime = new ArrayList<Object>();
		String dateStatus = params.get("dateType").toString();
		if(dateStatus.equals("1")){
			String beginTime = params.get("beginTime").toString();
			String endTime = params.get("endTime").toString();
			int beginmonth = Integer.parseInt(beginTime.split("-")[1]);
			int endmonth = Integer.parseInt(endTime.split("-")[1]);
			int beginyear = Integer.parseInt(beginTime.split("-")[0]);
			int endyear = Integer.parseInt(endTime.split("-")[0]);
			params.put("beginTime", DateTimeUtils.getMonthFirstTime(beginmonth, beginyear));
			params.put("endTime", DateTimeUtils.getMonthLastTime(endmonth,endyear));
		}
		List<Map<String,Object>> CouponsReptList = preferentialAnalysisChartsService.insertPreferentialView(params);
		List<CouponsReptDtail> findCouponsReptList = new ArrayList<CouponsReptDtail>();
		String status = params.get("dataType").toString();
		if(CouponsReptList.size()>0){
			for (int i = 0; i < CouponsReptList.size(); i++) {
				if(CouponsReptList.get(i)!=null && !CouponsReptList.get(i).equals("")){
					CouponsReptDtail couReDetal = new CouponsReptDtail();
					//数量
					BigDecimal nums = null; 
					if(CouponsReptList.get(i).get("showtype")!=null&&!CouponsReptList.get(i).get("showtype").equals("")){
						if(CouponsReptList.get(i).get("showtype").toString().equals("0")){
							nums = new BigDecimal(CouponsReptList.get(i).get("total_num").toString()).setScale(0, BigDecimal.ROUND_HALF_UP); 
						}else{
							nums = new BigDecimal(CouponsReptList.get(i).get("total_num").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						}
						couReDetal.setTotal(nums);
					}
					if(status.equals("0")){
						if(CouponsReptList.get(i).get("pname")!=null&&!CouponsReptList.get(i).get("pname").equals("")){
							couReDetal.setCouponsname(CouponsReptList.get(i).get("pname").toString());
						}
					}else{
						if(CouponsReptList.get(i).get("ptypename")!=null&&!CouponsReptList.get(i).get("ptypename").equals("")){
							couReDetal.setCouponsname(CouponsReptList.get(i).get("ptypename").toString());
						}
					}
					if(CouponsReptList.get(i).get("showtype")!=null&&!CouponsReptList.get(i).get("showtype").equals("")){
						couReDetal.setCode(CouponsReptList.get(i).get("showtype").toString());
					}
					String detail_num=null;
					if(CouponsReptList.get(i).get("detail_num")!=null&&!CouponsReptList.get(i).get("detail_num").equals("")){
						detail_num =CouponsReptList.get(i).get("detail_num").toString();
						findCouponsReptList.add(couReDetal);
					}
					List<CouponsReptDtail> findCouponsReptTimeList = new ArrayList<CouponsReptDtail>();
					if(detail_num.contains("|")){
						String[] detailStr = detail_num.split("\\|");
						for (int j = 0; j < detailStr.length; j++) {
							CouponsReptDtail couReDetalTime = new CouponsReptDtail();
							String[] StrCodes = detailStr[j].split(",");
							//couReDetalTime.setCouponsname(CouponsReptList.get(i).get("pname").toString());
							couReDetalTime.setInsertTime(StrCodes[0].toString());
							//根据时间分组统计
							BigDecimal numsCount = null; 
							if(CouponsReptList.get(i).get("showtype")!=null&&!CouponsReptList.get(i).get("showtype").equals("")){
								if(CouponsReptList.get(i).get("showtype").toString().equals(0)){
									numsCount = new BigDecimal(StrCodes[1].toString()).setScale(0, BigDecimal.ROUND_HALF_UP); 
								}else{
									numsCount = new BigDecimal(StrCodes[1].toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
								}
								couReDetalTime.setTotal(numsCount);
							}
							if(CouponsReptList.get(i).get("showtype")!=null&&!CouponsReptList.get(i).get("showtype").equals("")){
								couReDetalTime.setCode(CouponsReptList.get(i).get("showtype").toString());
							}
							findCouponsReptTimeList.add(couReDetalTime);
						}
					}else{
						    CouponsReptDtail couReDetalTime = new CouponsReptDtail();
							String[] StrCodes = detail_num.split(",");
							//couReDetalTime.setCouponsname(CouponsReptList.get(i).get("pname").toString());
							couReDetalTime.setInsertTime(StrCodes[0].toString());
							//根据时间分组统计
							BigDecimal numsCount = null;
							if(CouponsReptList.get(i).get("showtype")!=null&&!CouponsReptList.get(i).get("showtype").equals("")){
								if(CouponsReptList.get(i).get("showtype").toString().equals("0")){
									numsCount = new BigDecimal(StrCodes[1].toString()).setScale(0, BigDecimal.ROUND_HALF_UP); 
								}else{
									numsCount = new BigDecimal(StrCodes[1].toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
								}
								couReDetalTime.setCode(CouponsReptList.get(i).get("showtype").toString());
								couReDetalTime.setTotal(numsCount);
							}
							findCouponsReptTimeList.add(couReDetalTime);
					}
					HashMap<String, Object> mapCouDtal = new HashMap<String, Object>();
					if(status.equals("0")){
						mapCouDtal.put("name", CouponsReptList.get(i).get("pname").toString());
					}else{
						mapCouDtal.put("name", CouponsReptList.get(i).get("ptypename").toString());
					}
					
					mapCouDtal.put("code", CouponsReptList.get(i).get("showtype").toString());
					mapCouDtal.put("list", findCouponsReptTimeList);
					mapCouDtalTime.add(mapCouDtal);
					
				}
			}
		}
		mod.addObject("findCouponsReptList", findCouponsReptList);
		mod.addObject("mapCouDtalTime", mapCouDtalTime);
		return mod;
	}
	
	/**
	 * 优惠分析报表子表导出
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/exportReportCouDetailSub")
	@ResponseBody
	public ModelAndView exportReportCouDetailSub(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		try {
			String branchid = PropertiesUtils.getValue("current_branch_id");
			String branchname = itemDetailService.getBranchName(branchid);
			String bankcardno = params.get("bankcardno").toString();
			bankcardno = URLDecoder.decode(bankcardno,"UTF-8");
			params.put("branchname", branchname);
			params.put("bankcardno",bankcardno);
			params.put("names","优惠活动明细表");
			mav.addObject("message", "导出成功！");
			String dateShowbegin = DateUtils.stringDateFormat(params.get("beginTime").toString());
			String dateShowend = DateUtils.stringDateFormat(params.get("endTime").toString());
			if (dateShowbegin.equals(dateShowend)) {
				params.put("dateShow", dateShowbegin);
			} else {
				params.put("dateShow", dateShowbegin + "-" + dateShowend);
			}
			preferentialAnalysisChartsService.exportxlsB(params, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("message", "导出失败！");
		}
		return mav;
	}
	
	/**
	 * 2015-01-06 14:24:58 变成2015年01月06日
	 *
	 * @param date
	 * @return
	 */
	private String formatDate2(String date) {
		if (date == null || "".equals(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.format(new Date());
		}
		date = date.substring(0, 4) + "年" + date.substring(5, 7) + "月"
				+ date.substring(8, 10) + "日";

		return date;
	}
	
	/**
	 * 优惠分析主表导出
	 * @author weizhifang
	 * @since 2016-4-27
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/exportReportCouDetail")
	@ResponseBody
	public Map<String,Object> exportReportCouDetail(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {
		try {
			String bankcardno = params.get("bankcardno").toString();
			bankcardno = URLDecoder.decode(bankcardno,"UTF-8");
			params.put("bankcardno",bankcardno);
			String branchid = PropertiesUtils.getValue("current_branch_id");
			String branchname = itemDetailService.getBranchName(branchid);
			params.put("branchname", branchname);
			List<Map<String,Object>> couponsReptList = preferentialAnalysisChartsService.insertPreferential(params);
			preferentialAnalysisChartsService.exportPreferentialDetailtable(request,response,couponsReptList,params);
			return ReturnMap.getReturnMap(1, "001", "导出成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnMap.getReturnMap(0, "002", "导出失败");
		}
	}
}
