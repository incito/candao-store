package com.candao.www.webroom.controller;

import com.candao.www.webroom.service.DataDetailService;
import com.candao.www.webroom.service.DayIncomeBillService;
import com.candao.www.webroom.service.impl.ExportDateStatisticsExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 详细数据统计表
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/dataDetailController")
public class DataDetailController {
	@Autowired
	private DataDetailService dataDetailService;
	@Autowired
	private DayIncomeBillService dayIncomeBillService;
	@Autowired
	private ExportDateStatisticsExcelService exportDateStatisticsExcelService;

	/**
	 *  详细数据分析       
	 *  @author zhouyao
	 *  @serialData 2015-07-05
	 */
	@RequestMapping("/findDataStatistics")
	public List<Map<String,Object>> findDataStatistics(@RequestParam Map<String, Object> params) {
		List<Map<String,Object>>  DataStatisticsList = dataDetailService.insertDataStatistics(params);
	    return DataStatisticsList;
	}

	/**
	 * 营业数据统计导出（应收、实收、结算人数、开台数）
	 *
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/exportxlsG/{beginTime}/{endTime}/{shiftid}/{areaid}/{dateType}")
	@ResponseBody
	public ModelAndView exportxlsG(
			@PathVariable(value = "beginTime") String beginTime,
			@PathVariable(value = "endTime") String endTime,
			@PathVariable(value = "shiftid") String shiftid,
			@PathVariable(value = "areaid") String areaid,
			@PathVariable(value = "dateType") String dateType,
			HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		try {

			if (shiftid.equals("-1")) {
				shiftid = "";
			}
			if (areaid.equals("-1")) {
				areaid = "";
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("beginTime", beginTime);
			params.put("endTime", endTime);
			params.put("shiftId", shiftid);
			params.put("dataType", dateType);
			params.put("areaid", areaid);
			List<Map<String,Object>>  DataStatisticsList = dataDetailService.insertDataStatistics(params);

			exportDateStatisticsExcelService.formatExcel(DataStatisticsList, req, response, params);

			mav.addObject("message", "导出成功!");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("message", "导出失败!");
		}
		return mav;
	}

}
