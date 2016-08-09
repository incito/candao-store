package com.candao.www.webroom.controller;

import java.text.DecimalFormat;
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

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.SettlementOptionService;

/**
 * 结算方式
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/settlementOption")
public class SettlementOptionController {

	@Autowired
	private SettlementOptionService settlementOptionService;
	@Autowired
	private ItemDetailService itemDetailService;

	@RequestMapping("/paywayrept")
	public String toPayWayRept() {
		return "/billDetails/paywayrept";
	}

	@RequestMapping("/paywayChart")
	public String paywayChart() {
		return "/billDetails/paywayChart";
	}

	@RequestMapping("/settlementOptionList")
	@ResponseBody
	public ModelAndView settlementOptionList(@RequestParam Map<String, Object> params) {
		if (params.get("branchId") == null) {
			String branchId = PropertiesUtils.getValue("current_branch_id");
			params.put("branchId", branchId);// 门店id
		}

		params.put("result", "success");// 存储过程返回信息
		List<Map<String, Object>> settlementOptionList = settlementOptionService.settlementOptionList(params);
		if(settlementOptionList==null||settlementOptionList.size()<=0){
			settlementOptionList = new ArrayList<Map<String, Object>>();
		}
		
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		if (settlementOptionList != null && settlementOptionList.size() > 0) {
			for (Map<String, Object> map : settlementOptionList) {
				map.put("prices", decimalFormat.format(map.get("prices")));
			}
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("settlementOptionList", settlementOptionList);
		return mav;
	}

	/**
	 * 结算方式明细表
	 *
	 * @param req
	 * @param response
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 */
	@RequestMapping("/exportXls")
	@ResponseBody
	public void exportXls(
			HttpServletRequest req,
			HttpServletResponse response,
			@RequestParam(value = "beginTime", defaultValue = "") String beginTime,
			@RequestParam(value = "endTime", defaultValue = "") String endTime,
			@RequestParam(value = "shiftid", defaultValue = "") String shiftid,
			@RequestParam(value = "searchType") String searchType) {
		Map<String, Object> map = new HashMap<String, Object>();
		String branchId = PropertiesUtils.getValue("current_branch_id");
		String branchname = itemDetailService.getBranchName(branchId);
		map.put("beginTime", beginTime);
		map.put("shiftid", shiftid);
		map.put("endTime", endTime);
		map.put("branchname", branchname);
		map.put("searchType", searchType);
		map.put("names", "结算方式明细表");
		map.put("shopname", "新辣道");
		String dateShowbegin = formatDate2(beginTime);
        String dateShowend = formatDate2(endTime);
		if (dateShowbegin.equals(dateShowend)) {
			map.put("dateShow", dateShowbegin);
		} else {
			map.put("dateShow", dateShowbegin + "-" + dateShowend);
		}

        map.put("branchId", branchId);// 门店id
        map.put("result", "success");// 存储过程返回信息
        try {
			settlementOptionService.exportXls(map, req, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

}
