package com.candao.www.webroom.controller;

import com.candao.common.log.LoggerHelper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.SettlementOptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String branchid = PropertiesUtils.getValue("current_branch_id");
        params.put("branchid", branchid);// 门店id
        params.put("result", "success");// 存储过程返回信息
		List<Map<String, Object>> settlementOptionList = settlementOptionService.settlementOptionList(params);
        if (params.get("result") == null)
        {
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			if (settlementOptionList != null && settlementOptionList.size() > 0) {
				for (Map<String, Object> map : settlementOptionList) {
					map.put("prices", decimalFormat.format(map.get("prices")));
				}
			}
        } else {
			// 存储过程出错处理
			LoggerHelper loggerHelper = LoggerHelper.getLogger("aa");
			loggerHelper.error("结算明细存储过程报错", "error");
			settlementOptionList = null;

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
	 * @param settlementWay
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
		String branchid = PropertiesUtils.getValue("current_branch_id");
		String branchname = itemDetailService.getBranchName(branchid);
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

        map.put("branchid", branchid);// 门店id
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
