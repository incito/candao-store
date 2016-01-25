package com.candao.www.webroom.controller;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.utils.DateTimeUtils;
import com.candao.www.webroom.model.*;
import com.candao.www.webroom.service.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.DateUtils;
import com.candao.www.webroom.service.impl.ExportDataDetailExcelService;
import com.candao.www.webroom.service.impl.ExportDateStatisticsExcelService;
import com.candao.www.webroom.service.impl.ExportItemDetailService;
import com.candao.www.webroom.service.impl.ExportReturnDishService;

@Controller
@RequestMapping("/daliyReports")
public class DayIncomeBillController<V, K> {
	@Autowired
	private DataDictionaryService datadictionaryService;
	@Autowired
	private DayIncomeBillService dayIncomeBillService;
	@Autowired
	private ExportReturnDishService exportReturnDishService;
	@Autowired
	private ExportDateStatisticsExcelService exportDateStatisticsExcelService;
	@Autowired
	private ExportItemDetailService exportItemDetailService;
	@Autowired
	private ReportExportService reportExportService;
	@Autowired
	private BusinessAnalysisChartsService businessAnalysisChartsService;
	@Autowired
	private BusinessDataDetailService businessDataDetailService;
	@Autowired
	private DataDetailService dataDetailService;
	@Autowired
	private ExportDataDetailExcelService exportDataDetailExcelService;
	@Autowired
	private ItemDetailService itemDetailService;

	// 营业图报表明细
	@RequestMapping("/daysalerept")
	public ModelAndView daliyReports(@RequestParam Map<String, Object> params) {
		return getData("/billDetails/daliyReports", params);
	}

	@RequestMapping("/daliyChart")
	public String daliyChart() {
		return "/billDetails/daliyChart";
	}

	// 优惠分析明细
	@RequestMapping("/couponsrept")
	public ModelAndView couponReports(@RequestParam Map<String, Object> params) {
		return getData("/billDetails/couponDetailReports", params);
	}

	// 品项明细
	@RequestMapping("/dishsalerept")
	public ModelAndView itemDetailReports(
			@RequestParam Map<String, Object> params) {
		return getData("/billDetails/itemDetailReports", params);
	}
	//load页面
	@RequestMapping("/topage")
	public ModelAndView topage(@RequestParam Map<String, Object> params){
		String path = params.get("path").toString();
		ModelAndView newProduct = new ModelAndView(path);
		return newProduct;
	}

	/**
	 * 营业分析进入明细报表时参数传递
	 *
	 * @param path
	 * @param params
	 * @return
	 */
	private ModelAndView getData(String path, Map<String, Object> params) {
		ModelAndView mav = new ModelAndView(path);
		String beginTime = null;
		String endTime = null;
		String dateType = null;
		if (params.get("beginTime") != null && params.get("beginTime") != "") {
			beginTime = params.get("beginTime").toString();
		}
		if (params.get("endTime") != null && params.get("endTime") != "") {
			endTime = params.get("endTime").toString();
		}
		if (params.get("dateType") != null && params.get("dateType") != "") {
			dateType = params.get("dateType").toString();
		}
		mav.addObject("beginTime", beginTime);
		mav.addObject("endTime", endTime);
		mav.addObject("dateType", dateType);
		return mav;
	}

	//营业分析
	@RequestMapping("/reportAnalysis")
	public String analysis() {
		return "/billDetails/reportAnalysis";
	}

	// 详细数据统计表
	@RequestMapping("/dataStatistics")
	public String statistics() {
		return "/billDetails/dataStatistics";
	}

	// 品项总图表
	@RequestMapping("/itemReport")
	public String item() {
		return "/billDetails/itemReport";
	}

	// 营业总图表
	@RequestMapping("/bussinessReport")
	public String bussiness() {
		return "/billDetails/bussinessReport";
	}

	// 优惠分析总图表
	@RequestMapping("/preferentialReport")
	public String preferential() {
		return "/billDetails/preferentialReport";
	}

	// 结算方式图表明细
	@RequestMapping("/paywayrept")
	public String index5() {
		return "/billDetails/paywayrept";
	}

	@RequestMapping("/paywayChart")
	public String paywayChart() {
		return "/billDetails/paywayChart";
	}

	// 退菜明细表跳转
	@RequestMapping("/askedForARefund")
	public String askedForARefund() {
		return "/billDetails/askedForARefund";
	}
	//反结算统计表
	@RequestMapping("/theSettlement")
	public String theSettlement(){
		return "/billDetails/theSettlementStatistics";
	}
	//交接班统计表
	@RequestMapping("/presentStatistics")
	public String presentStatistics(){
		return "/billDetails/presentStatistics";
	}	
	//营业报表-一级
	@RequestMapping("/businessReportFirst")
	public String businessReport(){
		return "/billDetails/businessReportFirst";
	}
	//营业报表-二级
	@RequestMapping("/businessReportSec")
	public String businessReportSec(){
		return "/billDetails/businessReportSec";
	}
	//营业报表-二级
	@RequestMapping("/businessReportThird")
	public String businessReportThird(){
		return "/billDetails/businessReportThird";
	}
	@InitBinder
	protected void initBinder(HttpServletRequest request,
							  ServletRequestDataBinder binder) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor editor = new CustomDateEditor(df, true);
		binder.registerCustomEditor(Date.class, editor);
	}

	List<DataStatistics> DataStatisticsList = new ArrayList<DataStatistics>();
	List<DataStatistics> DataStatisticsHalfList = new ArrayList<DataStatistics>();

	public List<DataStatistics> getDataStatisticsList() {
		return DataStatisticsList;
	}

	public void setDataStatisticsList(List<DataStatistics> dataStatisticsList) {
		DataStatisticsList = dataStatisticsList;
	}

	public List<DataStatistics> getDataStatisticsHalfList() {
		return DataStatisticsHalfList;
	}

	public void setDataStatisticsHalfList(
			List<DataStatistics> dataStatisticsHalfList) {
		DataStatisticsHalfList = dataStatisticsHalfList;
	}

	private String orderid;
	private String payway;

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	/**
	 * 營業日报表
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getDayReportList")
	@ResponseBody
	public ModelAndView getReportList(@RequestParam Map<String, Object> params) {

		String branchid= PropertiesUtils.getValue("current_branch_id");
		params.put("branchid", branchid);
		List<BusinessReport1> reportlist = businessDataDetailService
				.isgetBusinessDetail(params);
		ModelAndView mav = new ModelAndView();
		mav.addObject("reportlist", reportlist);
		return mav;
	}


	/**
	 * 查询所有的桌号
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getTableNoList")
	@ResponseBody
	public ModelAndView getTableNoList(@RequestParam Map<String, Object> params) {
		List<Code> TableNoList = dayIncomeBillService.getTableNo(params);
		ModelAndView mav = new ModelAndView();
		mav.addObject("TableNoList", TableNoList);
		return mav;
	}

	/**
	 * 品项销售报表明细
	 *
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getItemDetail")
	@ResponseBody
	public ModelAndView getItemDetail(@RequestParam Map<String, Object> params) {
		List<Dishsalerept> ItemDetailList = dayIncomeBillService
				.findItemDetail(params);
		ModelAndView mav = new ModelAndView();
		mav.addObject("ItemDetailList", ItemDetailList);
		return mav;
	}

	/**
	 * 暂时没用
	 *
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/getDayReportListB")
	@ResponseBody
	public ModelAndView getReportListB(
			@RequestParam Map<String, Object> params, int page, int rows) {
		Page<Map<String, Object>> pageMap = dayIncomeBillService.gridB(params,
				page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		return mav;
	}

	/**
	 * 结算方式报表
	 *
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getDayReportListC")
	@ResponseBody
	public ModelAndView getReportListC(@RequestParam Map<String, Object> params) {
		List<PaywayRpet> dishRsult = dayIncomeBillService.gridC(params);
		ModelAndView mav = new ModelAndView();
		mav.addObject("dishRsult", dishRsult);
		return mav;
	}

	/**
	 * 营业日报表导出
	 *
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping("/exprotReport/{beginTime}/{endTime}/{shiftid}/{searchType}")
	@ResponseBody
	public ModelAndView exportReport(
			@PathVariable(value = "beginTime") String beginTime,
			@PathVariable(value = "endTime") String endTime,
			@PathVariable(value = "shiftid") String shiftid,
			@PathVariable(value = "searchType") String searchType,
			HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		ModelAndView mav = new ModelAndView();
		try {
			String branchid = PropertiesUtils.getValue("current_branch_id");
			String branchname = itemDetailService.getBranchName(branchid);
			map.put("searchType", searchType);
			map.put("beginTime", beginTime);
			map.put("branchid", branchid);
			map.put("branchname", branchname);
			map.put("endTime", endTime);
			map.put("shiftid", shiftid);
			map.put("names", "营业数据明细表");
			map.put("shopname", "新辣道");
			mav.addObject("message", "导出成功！");
			String dateShowbegin = formatDate2(beginTime);
			String dateShowend = formatDate2(endTime);
			if (dateShowbegin.equals(dateShowend)) {
				map.put("dateShow", dateShowbegin);
			} else {
				map.put("dateShow", dateShowbegin + "-" + dateShowend);
			}
			dayIncomeBillService.exportDaliyRport(map, req, response);
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("message", "导出失败！");
		}
		return mav;
	}

	/**
	 * 时间20150210变成2015年02月10日
	 *
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unused")
	private String formatDate(String date) {
		if (date == null || "".equals(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			date = sdf.format(new Date());
		}
		date = date.substring(0, 4) + "年" + date.substring(4, 6) + "月"
				+ date.substring(6, 8) + "日";

		return date;
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
	 * 优惠报表
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getDayReportList1")
	@ResponseBody
	public ModelAndView getReportList1(
			@RequestParam Map<String, Object> params, int page, int rows) {
		Page<Map<String, Object>> pageMap = dayIncomeBillService
				.getDaliyReport1(params, page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		return mav;
	}

	/**
	 * 优惠报表查询列表
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getpageCoupons")
	@ResponseBody
	public ModelAndView getpageCoupons(
			@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {
		//path encode 处理 bug ：优惠报表的不能下拉
		String bankcardno = params.get("bankcardno").toString();
		ModelAndView mad = new ModelAndView();
		try {
			/*bankcardno = new String(bankcardno.getBytes("ISO8859-1"), "UTF-8");
			bankcardno = URLDecoder.decode(bankcardno,"UTF-8");*/
			if (bankcardno.equals("null")) {
				bankcardno = "";
			}
			params.put("bankcardno", bankcardno);
			List<CouponsRept> Resulttlist = dayIncomeBillService.findCoupons(params);
			mad.addObject("Resulttlist", Resulttlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mad;
	}

	/**
	 * 结算方式
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getpaywayList")
	@ResponseBody
	public ModelAndView getpaywayList(@RequestParam Map<String, Object> params,
									  HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> paywayList = datadictionaryService
				.getDatasByType("PAYWAY");
		ModelAndView mad = new ModelAndView();
		mad.addObject("paywayList", paywayList);
		return mad;
	}

	/**
	 * 活动名称
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getActivityNameList")
	@ResponseBody
	public ModelAndView getActivityNameList(
			@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {
		List<Code> ActivityNameList = dayIncomeBillService
				.findPreferentialActivity(params);
		ModelAndView mad = new ModelAndView();
		mad.addObject("ActivityNameList", ActivityNameList);
		return mad;
	}

	/**
	 * 活动类型
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getTypeDictList")
	@ResponseBody
	public ModelAndView getTypeDictList(
			@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {
		List<Code> TypeDictList = dayIncomeBillService
				.findPreferentialTypeDict(params);
		ModelAndView mad = new ModelAndView();
		mad.addObject("TypeDictList", TypeDictList);
		return mad;
	}

	/**
	 * 支付方式code查询
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getpayway")
	@ResponseBody
	public void getpayway(HttpServletRequest req, HttpServletResponse response,
						  @RequestParam Map<String, Object> params) {
		String type = "PAYWAY";
		StringBuffer sb = new StringBuffer();
		try {
			List<Map<String, Object>> list = datadictionaryService
					.getDatasByType(type);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("itemid") != null
						&& list.get(i).get("itemid") != "") {
					sb.append(list.get(i).get("itemid").toString()).append(",");
					// pay.setItemid(list.get(i).get("itemid").toString());
				}
				if (list.get(i).get("itemDesc") != null
						&& list.get(i).get("itemDesc") != "") {
					// pay.setItemDesc(list.get(i).get("itemDesc").toString());
					sb.append(list.get(i).get("itemDesc").toString()).append(
							"|");
				}
				// listPay.add(pay);
			}
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out = response.getWriter();
			out.print(sb);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * ModelAndView mad = new ModelAndView("/billDetails/couponReports");
		 * mad.addObject("listPay",listPay); return mad;
		 */
	}

	/**
	 * 优惠报表明细统计
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getpageCouponsDtail")
	@ResponseBody
	public ModelAndView getpageCouponsDtail(HttpServletRequest req,
											HttpServletResponse response,
											@RequestParam Map<String, Object> params) {
		ModelAndView mad = new ModelAndView();
		try {

			String name = null;
			if (params.get("couponsname") != null
					&& params.get("couponsname") != "") {
				name = params.get("couponsname").toString();
			}
			/*name = URLDecoder.decode(name,"UTF-8");*/
			// path encode 处理 bug ：优惠报表的不能下拉
			//name = new String(name.getBytes("ISO8859-1"));
			// "UTF-8");
			//name = URLDecoder.decode(name, "UTF-8");
			params.put("1", "1");
			params.put("couponsname", name);
			List<CouponsReptDtail> ResultDetail = dayIncomeBillService.findCouponsDtail(params);
			mad.addObject("ResultDetail", ResultDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mad;
	}

	/**
	 * 营业图表数据查询
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getBusinessReport")
	@ResponseBody
	public ModelAndView getBusinessReport(
			@RequestParam Map<String, Object> params) {


		String branchid=PropertiesUtils.getValue("current_branch_id");
		params.put("branchid", branchid);
		//格式化日期格式
		List<BusinessReport> BusinessList = businessAnalysisChartsService
				.isfindBusinessReport(params);
		ModelAndView mav = new ModelAndView();
		mav.addObject("BusinessList", BusinessList);
		return mav;
	}

	/**
	 * //格式化日期格式
	 *
	 * @param params
	 * @param beginTime
	 * @param datetype
	 */
	@SuppressWarnings("unused")
	private void getformatDayParam(Map<String, Object> params, String beginTime, String datetype) {
		if (StringUtils.equals(datetype, "M")) {//月查询
			int month = Integer.parseInt(beginTime.split("-")[1]);
			int year = Integer.parseInt(beginTime.split("-")[0]);
			params.put("beginTime", DateTimeUtils.getMonthFirstDay(month, year));
			params.put("endTime", DateTimeUtils.getMonthLastDay(month, year));
		}
		if (StringUtils.equals(datetype, "Y")) {//月查询
			int year = Integer.parseInt(beginTime);
			params.put("beginTime", DateTimeUtils.getMonthFirstDay(1, year));
			params.put("endTime", DateTimeUtils.getMonthLastDay(12, year));
		}
	}

	/**
	 * 优惠分析图表数据查询
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getPreferentialReport")
	@ResponseBody
	public ModelAndView getPreferentialReport(
			@RequestParam Map<String, Object> params) {
		// 优惠分析饼图数根据活动名称分组据查询
		List<PreferentialReport> GroupByNmaeReport = dayIncomeBillService
				.findGroupByNmaeReport(params);
		List<Object> NameList = new ArrayList<Object>();
		if (GroupByNmaeReport.size() > 0) {
			for (int j = 0; j < GroupByNmaeReport.size(); j++) {
				params.put("activity_name", GroupByNmaeReport.get(j).getActivitytame());
				// 优惠分析趋势图数根据活动名称，日期分组据查询
				List<PreferentialReport> GroupByNmaeAndTimeReport = dayIncomeBillService
						.findGroupByNmaeAndTimeReport(params);
				if (GroupByNmaeAndTimeReport.size() > 0) {
					List<PreferentialReport> listPr = new ArrayList<PreferentialReport>();
					for (int i = 0; i < GroupByNmaeAndTimeReport.size(); i++) {
						if (GroupByNmaeReport.get(j).getActivitytame().equals(GroupByNmaeAndTimeReport.get(i).getActivitytame())) {
							PreferentialReport pr = new PreferentialReport();
							pr.setActivitytame(GroupByNmaeAndTimeReport.get(i)
									.getActivitytame());
							pr.setStatistictime(GroupByNmaeAndTimeReport.get(i)
									.getStatistictime());
							pr.setShouldamount(GroupByNmaeAndTimeReport.get(i)
									.getShouldamount());
							pr.setPaidinamount(GroupByNmaeAndTimeReport.get(i)
									.getPaidinamount());
							pr.setNum(GroupByNmaeAndTimeReport.get(i).getNum());
							pr.setAmount(GroupByNmaeAndTimeReport.get(i)
									.getAmount());
							listPr.add(pr);
						}
					}
					HashMap<String, Object> mapNamne = new HashMap<String, Object>();
					mapNamne.put("name", GroupByNmaeReport.get(j).getActivitytame());
					mapNamne.put("list", listPr);
					NameList.add(mapNamne);
				}
			}
		}
		// 优惠分析饼图数根据活动类别分组据查询
		List<PreferentialReport> GroupBytypeReport = dayIncomeBillService
				.findGroupBytypeReport(params);
		List<Object> TypeList = new ArrayList<Object>();
		if (GroupBytypeReport.size() > 0) {
			for (int j = 0; j < GroupBytypeReport.size(); j++) {
				params.put("activity_type", GroupBytypeReport.get(j)
						.getActivitytype());
				// 优惠分析趋势图数根据活动类别，日期分组据查询
				List<PreferentialReport> GroupBytypeAndTimeReport = dayIncomeBillService
						.findGroupBytypeAndTimeReport(params);
				if (GroupBytypeAndTimeReport.size() > 0) {
					List<PreferentialReport> listType = new ArrayList<PreferentialReport>();
					for (int i = 0; i < GroupBytypeAndTimeReport.size(); i++) {
						if (GroupBytypeReport.get(j).getActivitytype().equals(GroupBytypeAndTimeReport.get(i)
								.getActivitytype())) {
							PreferentialReport pr = new PreferentialReport();
							pr.setActivitytame(GroupBytypeAndTimeReport.get(i)
									.getActivitytame());
							pr.setActivitytype(GroupBytypeAndTimeReport.get(i)
									.getActivitytype());
							pr.setStatistictime(GroupBytypeAndTimeReport.get(i)
									.getStatistictime());
							pr.setShouldamount(GroupBytypeAndTimeReport.get(i)
									.getShouldamount());
							pr.setPaidinamount(GroupBytypeAndTimeReport.get(i)
									.getPaidinamount());
							pr.setNum(GroupBytypeAndTimeReport.get(i).getNum());
							pr.setAmount(GroupBytypeAndTimeReport.get(i)
									.getAmount());
							listType.add(pr);
						}
					}
					HashMap<String, Object> mapType = new HashMap<String, Object>();
					mapType.put("name", GroupBytypeReport.get(j).getActivitytype());
					mapType.put("list", listType);
					TypeList.add(mapType);
				}
			}
		}

		// 优惠分析饼图数根据支付类型、活动名称分组据查询(发生金额)
		List<PreferentialReport> GroupByPaywayandNameReport = dayIncomeBillService.findGroupByPaywayandName(params);
		List<Object> PaywayandNameReportList = new ArrayList<Object>();
		if (GroupByPaywayandNameReport.size() > 0) {
			for (int j = 0; j < GroupByPaywayandNameReport.size(); j++) {
				params.put("activity_name", GroupByPaywayandNameReport.get(j).getName());
				params.put("payway", GroupByPaywayandNameReport.get(j).getPayway());
				// 优惠分析饼图数根据支付类型、活动名称、日期分组据查询(发生金额)
				List<PreferentialReport> GroupByPaywayandNametiemReport = dayIncomeBillService.findGroupByPaywayandNameandtiem(params);
				if (GroupByPaywayandNametiemReport.size() > 0) {
					List<PreferentialReport> GroupByPaywayandNameList = new ArrayList<PreferentialReport>();
					for (int i = 0; i < GroupByPaywayandNametiemReport.size(); i++) {
						if (GroupByPaywayandNameReport.get(j).getName().equals(GroupByPaywayandNametiemReport.get(i).getActivitytame())) {
							if (GroupByPaywayandNameReport.get(j).getPayway() == GroupByPaywayandNametiemReport.get(i).getPayway()) {
								PreferentialReport pr = new PreferentialReport();
								pr.setActivitytame(GroupByPaywayandNametiemReport.get(i)
										.getActivitytame());
								pr.setStatistictime(GroupByPaywayandNametiemReport.get(i)
										.getStatistictime());
								pr.setAmount(GroupByPaywayandNametiemReport.get(i)
										.getAmount());
								GroupByPaywayandNameList.add(pr);
							}
						}
					}
					HashMap<String, Object> mapPaywayandName = new HashMap<String, Object>();
					mapPaywayandName.put("name", GroupByPaywayandNameReport.get(j).getActivitytame());
					mapPaywayandName.put("list", GroupByPaywayandNameList);
					PaywayandNameReportList.add(mapPaywayandName);
				}
			}
		}

		// 优惠分析饼图数根据支付类型、活动类型分组据查询(发生金额)
		List<PreferentialReport> GroupByPaywayandTypeReport = dayIncomeBillService.findGroupByPaywayandType(params);
		List<Object> PaywayandTypeReportList = new ArrayList<Object>();
		if (GroupByPaywayandTypeReport.size() > 0) {
			for (int j = 0; j < GroupByPaywayandTypeReport.size(); j++) {
				params.put("activity_type", GroupByPaywayandTypeReport.get(j).getName());
				// 优惠分析饼图数根据支付类型、活动烈性、日期分组据查询(发生金额)
				List<PreferentialReport> GroupByPaywayandTypeandTimeReport = dayIncomeBillService.findGroupByPaywayandTypeandTime(params);
				if (GroupByPaywayandTypeandTimeReport.size() > 0) {
					List<PreferentialReport> GroupByPaywayandTypeandTimeList = new ArrayList<PreferentialReport>();
					for (int i = 0; i < GroupByPaywayandTypeandTimeReport.size(); i++) {
						if (GroupByPaywayandTypeReport.get(j).getName()
								.equals(GroupByPaywayandTypeandTimeReport.get(i).getActivitytype())) {
							if (GroupByPaywayandTypeReport.get(j).getPayway() == GroupByPaywayandTypeandTimeReport.get(i).getPayway()) {
								PreferentialReport pr = new PreferentialReport();
								pr.setActivitytame(GroupByPaywayandTypeandTimeReport.get(i)
										.getActivitytame());
								pr.setActivitytame(GroupByPaywayandTypeandTimeReport.get(i)
										.getActivitytype());
								pr.setStatistictime(GroupByPaywayandTypeandTimeReport.get(i)
										.getStatistictime());
								pr.setAmount(GroupByPaywayandTypeandTimeReport.get(i)
										.getAmount());
								GroupByPaywayandTypeandTimeList.add(pr);
							}
						}
					}
					HashMap<String, Object> mapPaywayandType = new HashMap<String, Object>();
					mapPaywayandType.put("name", GroupByPaywayandTypeReport.get(j).getActivitytype());
					mapPaywayandType.put("list", GroupByPaywayandTypeandTimeList);
					PaywayandTypeReportList.add(mapPaywayandType);
				}
			}
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("GroupByNmaeReport", GroupByNmaeReport);
		mav.addObject("GroupByNmaeAndTimeReport", NameList);
		mav.addObject("GroupBytypeReport", GroupBytypeReport);
		mav.addObject("GroupBytypeAndTimeReport", TypeList);
		//优惠分析饼图数根据支付类型、活动名称分组据查询(发生金额)
		mav.addObject("GroupByPaywayandNameReport", GroupByPaywayandNameReport);
		//优惠分析饼图数根据支付类型、活动名称、日期分组据查询(发生金额)
		mav.addObject("findGroupByPaywayandNameandtiem", PaywayandNameReportList);
		//优惠分析饼图数根据支付类型、活动类型分组据查询(发生金额)
		mav.addObject("GroupByPaywayandTypeReport", GroupByPaywayandTypeReport);
		//优惠分析饼图数根据支付类型、活动类型、日期分组据查询(发生金额)
		mav.addObject("findGroupByPaywayandTypeandTime", PaywayandTypeReportList);
		return mav;
	}

	/**
	 * 品项分析图表数据查询
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
//	@RequestMapping("/getItemReport")
//	@ResponseBody
	public ModelAndView getItemReport(@RequestParam Map<String, Object> params) {
		// 品项分析销售数量TOP10柱状图
		List<ItemReport> ItemReportList = dayIncomeBillService
				.findItemReport(params);
		List<Object> numList = new ArrayList<Object>();
		if (ItemReportList.size() > 0) {
			for (int j = 0; j < ItemReportList.size(); j++) {
				params.put("item_id", ItemReportList.get(j).getItemid());
				// 品项分析销售数量TOP10趋势图
				List<ItemReport> ItemNumQushiReport = dayIncomeBillService
						.findItemNumQushiReport(params);
				if (ItemNumQushiReport.size() > 0) {
					List<ItemReport> ItemNumQushiList = new ArrayList<ItemReport>();
					for (int i = 0; i < ItemNumQushiReport.size(); i++) {
						ItemReport it = new ItemReport();
						it.setNum(ItemNumQushiReport.get(i).getNum());
						it.setStatistictime(ItemNumQushiReport.get(i)
								.getStatistictime());
						ItemNumQushiList.add(it);
					}
					HashMap<String, Object> mapNum = new HashMap<String, Object>();
					mapNum.put("name", ItemReportList.get(j).getItemdesc());
					mapNum.put("list", ItemNumQushiList);
					numList.add(mapNum);
				}
			}
		}

		// 品项分析销售金额TOP10柱状图
		List<ItemReport> ItemSharezhuzhuangReport = dayIncomeBillService
				.findItemSharezhuzhuangReport(params);
		List<Object> shareList = new ArrayList<Object>();
		if (ItemSharezhuzhuangReport.size() > 0) {
			for (int j = 0; j < ItemSharezhuzhuangReport.size(); j++) {
				params.put("item_id", ItemSharezhuzhuangReport.get(j)
						.getItemid());
				// 品项分析销售金额TOP10趋势图
				List<ItemReport> ItemShareQushiReport = dayIncomeBillService
						.findItemShareQushiReport(params);
				if (ItemShareQushiReport.size() > 0) {
					List<ItemReport> ItemShareQushiList = new ArrayList<ItemReport>();
					for (int i = 0; i < ItemShareQushiReport.size(); i++) {
						ItemReport it = new ItemReport();
						it.setItemdesc(ItemShareQushiReport.get(i)
								.getItemdesc());
						it.setShare(ItemShareQushiReport.get(i).getShare());
						it.setStatistictime(ItemShareQushiReport.get(i)
								.getStatistictime());
						ItemShareQushiList.add(it);
					}
					HashMap<String, Object> mapShare = new HashMap<String, Object>();
					mapShare.put("name", ItemSharezhuzhuangReport.get(j)
							.getItemdesc());
					mapShare.put("list", ItemShareQushiList);
					shareList.add(mapShare);
				}
			}
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("ItemReportList", ItemReportList);
		mav.addObject("ItemNumQushiReport", numList);
		mav.addObject("ItemSharezhuzhuangReport", ItemSharezhuzhuangReport);
		mav.addObject("ItemShareQushiReport", shareList);
		return mav;
	}

	/**
	 * 营业分析-品项销售统计
	 *
	 * @param params
	 * @return
	 * @author weizhifang
	 * @since 2015-06-06
	 */
	@RequestMapping("/getItemReport")
	@ResponseBody
	public ModelAndView getItemReportForView(@RequestParam Map<String, Object> params) {
		//售卖份数top10
		List<Map<String, Object>> dishNumList = reportExportService.getItemDishNumTop10(params);
		//售卖份数top10趋势图
		List<Map<String, Object>> dishNumTrendList = reportExportService.getItemDishNumTop10Trend(dishNumList, params);
		//售卖金额top10
		List<Map<String, Object>> amountList = reportExportService.getItemAmountTop10(params);
		//售卖金额top10趋势图
		List<Map<String, Object>> amountTrendList = reportExportService.getItemDishNumTop10Trend(amountList, params);
		ModelAndView mav = new ModelAndView();
		mav.addObject("ItemReportList", dishNumList);
		mav.addObject("ItemNumQushiReport", dishNumTrendList);
		mav.addObject("ItemSharezhuzhuangReport", amountList);
		mav.addObject("ItemShareQushiReport", amountTrendList);
		return mav;
	}

	/**
	 * 结算方式析图表数据查询
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getSettlementReport")
	@ResponseBody
	public ModelAndView getSettlementReport(
			@RequestParam Map<String, Object> params) {
		List<SettlementReport> SettlementReportList = dayIncomeBillService
				.findSettlementReport(params);
		ModelAndView mav = new ModelAndView();
		mav.addObject("SettlementReportList", SettlementReportList);
		return mav;
	}


	/**
	 * 查询区域表
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getAreaList")
	@ResponseBody
	public ModelAndView getAreaList(
			@RequestParam Map<String, Object> params) {
		// 查询区域表
		List<TableArea> areaList = dayIncomeBillService.findTableArea(params);
		ModelAndView mav = new ModelAndView();
		mav.addObject("areaList", areaList);
		return mav;
	}

	/**
	 * 查询营业数据查询的类型
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getCodeList")
	@ResponseBody
	public ModelAndView getCodeList(
			@RequestParam Map<String, Object> params) {
		// 查询营业数据查询的类型
		List<Code> codeList = dayIncomeBillService.findCode(params);
		ModelAndView mav = new ModelAndView();
		mav.addObject("codeList", codeList);
		return mav;
	}


	/**
	 * 营业数据统计页面显示（应收、实收、结算人数、开台数）
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getDatastatistics")
	@ResponseBody
	public ModelAndView getDataStatistics(
			@RequestParam Map<String, Object> params, HttpServletRequest req,
			HttpServletResponse response) {
		List<Object> dataStatisticsHalfMapList = new ArrayList<Object>();
		List<Object> dataStatisticsMapList = new ArrayList<Object>();
		List<TableArea> areaList = dayIncomeBillService.findTableArea(params);
		ModelAndView mav = new ModelAndView();
		if (("").equals(params.get("shiftId"))) {
			List<DataStatistics> dataStatisticsList = dayIncomeBillService
					.findDataStatisticsDay(params);
			setDataStatisticsList(dataStatisticsList);
			if (dataStatisticsList.size() > 0) {
				if (areaList.size() > 0) {
					List<Object> areaMapList = new ArrayList<Object>();
					for (int j = 0; j < areaList.size(); j++) {
						Map<String, List<Object>> tbMap = new HashMap<String, List<Object>>();
						for (int i = 0; i < dataStatisticsList.size(); i++) {
							List<Object> tbResultList = new ArrayList<Object>();
							if (areaList.get(j).getAreaname().equals(dataStatisticsList.get(i).getArea())) {
								DataStatistics ds = new DataStatistics();
								ds.setDateTime(dataStatisticsList.get(i)
										.getDateTime());
								ds.setArea(dataStatisticsList.get(i).getArea());
								ds.setShiftId(dataStatisticsList.get(i)
										.getShiftId());
								ds.setTableId(dataStatisticsList.get(i)
										.getTableId());
								ds.setValues(dataStatisticsList.get(i)
										.getValues());

								String tbid = dataStatisticsList.get(i)
										.getTableId();

								if (tbMap.containsKey(tbid)) {
									tbResultList = tbMap.get(tbid);
									tbResultList.add(ds);
									tbMap.put(tbid, tbResultList);
								} else {
									tbResultList.add(ds);
									tbMap.put(tbid, tbResultList);
								}
							}

						}
						List<Object> tableList = new ArrayList<Object>();
						Iterator<String> it = tbMap.keySet().iterator();
						while (it.hasNext()) {
							String tableid = it.next();
							HashMap<String, Object> tableMap = new HashMap<String, Object>();
							tableMap.put("name", tableid);
							tableMap.put("list", tbMap.get(tableid));
							tableList.add(tableMap);
						}
						HashMap<String, Object> areaListMap = new HashMap<String, Object>();
						areaListMap.put("id", areaList.get(j).getAreaNo());
						areaListMap.put("name", areaList.get(j).getAreaname());
						areaListMap.put("list", tableList);
						areaMapList.add(areaListMap);
					}
					HashMap<String, Object> StatisticsListMap = new HashMap<String, Object>();
					StatisticsListMap.put("name", "全天");
					StatisticsListMap.put("list", areaMapList);
					dataStatisticsMapList.add(StatisticsListMap);
					mav.addObject("dataStatisticsMapList",
							dataStatisticsMapList);
				}
			}
		} else {
			List<DataStatistics> dataStatisticsHalfList = dayIncomeBillService
					.findDataStatisticsHalf(params);
			setDataStatisticsHalfList(dataStatisticsHalfList);
			if (dataStatisticsHalfList.size() > 0) {
				if (areaList.size() > 0) {
					List<Object> areaHalfMapList = new ArrayList<Object>();
					for (int j = 0; j < areaList.size(); j++) {
						Map<String, List<Object>> tbMap = new HashMap<String, List<Object>>();
						for (int i = 0; i < dataStatisticsHalfList.size(); i++) {
							List<Object> tbResultList = new ArrayList<Object>();
							if (areaList
									.get(j)
									.getAreaname()
									.equals(dataStatisticsHalfList.get(i)
											.getArea())) {
								DataStatistics ds = new DataStatistics();
								ds.setShiftId(dataStatisticsHalfList.get(i)
										.getShiftId());
								ds.setDateTime(dataStatisticsHalfList.get(i)
										.getDateTime());
								ds.setArea(dataStatisticsHalfList.get(i)
										.getArea());
								ds.setShiftId(dataStatisticsHalfList.get(i)
										.getShiftId());
								ds.setTableId(dataStatisticsHalfList.get(i)
										.getTableId());
								ds.setValues(dataStatisticsHalfList.get(i)
										.getValues());
								String tbid = dataStatisticsHalfList.get(i)
										.getTableId();

								if (tbMap.containsKey(tbid)) {
									tbResultList = tbMap.get(tbid);
									tbResultList.add(ds);
									tbMap.put(tbid, tbResultList);
								} else {
									tbResultList.add(ds);
									tbMap.put(tbid, tbResultList);
								}
							}
						}
						List<Object> tableList = new ArrayList<Object>();
						Iterator<String> it = tbMap.keySet().iterator();
						while (it.hasNext()) {
							String tableid = it.next();
							HashMap<String, Object> tableMap = new HashMap<String, Object>();
							tableMap.put("name", tableid);
							tableMap.put("list", tbMap.get(tableid));
							tableList.add(tableMap);
						}

						HashMap<String, Object> areaListHalfMap = new HashMap<String, Object>();
						areaListHalfMap.put("id", areaList.get(j).getAreaNo());
						areaListHalfMap.put("name", areaList.get(j)
								.getAreaname());
						areaListHalfMap.put("list", tableList);
						areaHalfMapList.add(areaListHalfMap);
					}
					HashMap<String, Object> StatisticsLisHalftMap = new HashMap<String, Object>();
					if (("1").equals(params.get("shiftId"))) {
						StatisticsLisHalftMap.put("name", "晚市");
					} else {
						StatisticsLisHalftMap.put("name", "午市");
					}
					StatisticsLisHalftMap.put("list", areaHalfMapList);
					dataStatisticsHalfMapList.add(StatisticsLisHalftMap);
					mav.addObject("dataStatisticsHalfMapList",
							dataStatisticsHalfMapList);
				}
			}
		}
		return mav;
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
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("beginTime", beginTime);
			params.put("endTime", endTime);
			params.put("shiftId", shiftid);
			params.put("dataType", dateType);
			params.put("areaid", areaid);
			String branchid = PropertiesUtils.getValue("current_branch_id");
	        String branchname = itemDetailService.getBranchName(branchid);
	        params.put("branchname", branchname);
			List<Map<String,Object>>  DataStatisticsList = dataDetailService.insertDataStatistics(params);
			exportDataDetailExcelService.formatExcel(DataStatisticsList, req, response, params);
			mav.addObject("message", "导出成功!");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("message", "导出失败!");
		}
		return mav;
	}
	/**
	 * 营业数据统计导出（应收、实收、结算人数、开台数）
	 *
	 * @param params
	 * @return
	 * @author zhouyao
	 *//*
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
			if (dateType.equals("null")) {
				dateType = "1";
			}
			if (shiftid.equals("null")) {
				shiftid = "";
			}
			if (areaid.equals("null")) {
				areaid = "";
			}
			// 查询区域表
			Map<String, Object> params = new HashMap<String, Object>();
			List<Code> TableNoList = dayIncomeBillService.getTableNo(params);
			params.put("beginTime", beginTime);
			params.put("endTime", endTime);
			params.put("shiftId", shiftid);
			params.put("dataType", dateType);
			params.put("areaid", areaid);
			List<TableArea> areaList = dayIncomeBillService
					.findTableArea(params);
			System.out.println(params.get("shiftId"));
			List<Map<String,Object>> dataStatisticsList = new ArrayList<Map<String,Object>>();
				exportDateStatisticsExcelService.formatExcel(dataStatisticsList,req, response, params);

			mav.addObject("message", "导出成功!");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("message", "导出失败!");
		}
		return mav;
	}*/

	/**
	 * 优惠分析报表导出
	 *
	 * @param settlementWay
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 * @param bankcardno
	 * @param type
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping("/exprotReport1/{settlementWay}/{beginTime}/{endTime}/{shiftid}/{bankcardno}/{type}/{codes}")
	@ResponseBody
	public ModelAndView exportReport1(
			@PathVariable(value = "settlementWay") String settlementWay,
			@PathVariable(value = "beginTime") String beginTime,
			@PathVariable(value = "endTime") String endTime,
			@PathVariable(value = "shiftid") String shiftid,
			@PathVariable(value = "bankcardno") String bankcardno,
			@PathVariable(value = "type") String type,
			@PathVariable(value = "codes") String codes, HttpServletRequest req,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		ModelAndView mav = new ModelAndView();
		try {
			//path encode 处理 bug ：优惠报表的不能下拉
			//bankcardno = new String(bankcardno.getBytes("ISO8859-1"), "UTF-8");
			if(!bankcardno.equals("-1")){
				bankcardno = URLDecoder.decode(bankcardno,"UTF-8");
			}
			if (settlementWay.equals("-1")) {
				settlementWay = "";
			}
			if (shiftid.equals("-1")) {
				shiftid = "";
			}
			if (bankcardno.equals("-1")) {
				bankcardno = "";
			}
			if (type.equals("-1")) {
				type = "";
			}
			map.put("settlementWay", settlementWay);
			map.put("beginTime", beginTime);
			map.put("shiftid", shiftid);
			map.put("endTime", endTime);
			map.put("bankcardno", bankcardno);
			map.put("codes", codes);
			map.put("type", type);
			map.put("names", "优惠分析报表");
			map.put("shopname", "新辣道");
			mav.addObject("message", "导出成功！");
			String dateShowbegin = formatDate2(beginTime);
			String dateShowend = formatDate2(endTime);
			if (dateShowbegin.equals(dateShowend)) {
				map.put("dateShow", dateShowbegin);
			} else {
				map.put("dateShow", dateShowbegin + "-" + dateShowend);
			}
			dayIncomeBillService.exportxlsB(map, req, response);
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("message", "导出失败！");
		}
		return mav;
	}

	/**
	 * 暂时没用
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping("/getDayReportList2")
	@ResponseBody
	public ModelAndView getReportList2(@RequestParam Map<String, Object> params) {
		if (params.isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd ");
			params.put("dateTime", sdf.format(new Date()));
		}
		List<TjObj> reportlist = dayIncomeBillService.getDaliyReport2(params);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", reportlist);
		return mav;
	}

	/**
	 * 营业额汇总报表
	 *
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping("/exprotReport2/{beginTime}/{endTime}/{shiftid}")
	@ResponseBody
	public ModelAndView exportReport2(
			@PathVariable(value = "beginTime") String beginTime,
			@PathVariable(value = "endTime") String endTime,
			@PathVariable(value = "shiftid") String shiftid,
			HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		ModelAndView mav = new ModelAndView();
		try {
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("shiftid", shiftid);
			map.put("names", "营业额汇总报表");
			map.put("shopname", "新辣道");
			mav.addObject("message", "导出成功！");
			String dateShowbegin = formatDate2(beginTime);
			String dateShowend = formatDate2(endTime);
			if (dateShowbegin.equals(dateShowend)) {
				map.put("dateShow", dateShowbegin);
			} else {
				map.put("dateShow", dateShowbegin + "-" + dateShowend);
			}
			dayIncomeBillService.exportDaliyRport2(map, req, response);
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("message", "导出失败！");
		}
		return mav;
	}

	/**
	 * 品项销售明细表导出
	 *
	 * @param request
	 * @param response
	 * @param dishtype
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 * @param itemIdFlag
	 * @author weizhifang
	 * @since 2015-5-30
	 */
	@RequestMapping("/exportxlsA/{beginTime}/{endTime}/{shiftid}/{id}/{dishType}/{itemids}")
	@ResponseBody
	public void exportxlsA(HttpServletRequest request, HttpServletResponse response,
						   @PathVariable(value = "beginTime") String beginTime,
						   @PathVariable(value = "endTime") String endTime,
						   @PathVariable(value = "shiftid") String shiftid,
						   @PathVariable(value = "id") String id,
						   @PathVariable(value = "dishType") String dishType,
						   @PathVariable(value = "itemids") String itemids) throws Exception {
		//查询品项销售总表
		Map<String, Object> map = setItemQueryParams(beginTime, endTime, shiftid, id, dishType);
		String shiftname = "";


		if (map.get("shiftid").toString().equals("0")) {
			shiftname = "午市";
		}
		if (map.get("shiftid").toString().equals("-1")) {
			shiftname = "全天";
		}
		if (map.get("shiftid").toString().equals("1")) {
			shiftname = "晚市";
		}
		List<Map<String, Object>> itemList = dayIncomeBillService.getItemReportForList(map);
		if (StringUtils.isBlank(itemids)) {
//			exportItemDetailService.exportItemDetailList2(shiftname,itemList, null, response);
		} else {
			//查询品项销售明细表
			List<Map<String, Object>> itemDetail = dayIncomeBillService.getItemDetailList(map);
//			exportItemDetailService.exportItemDetailList2(shiftname,itemList, itemDetail, response);
		}
	}

	/**
	 * 品项导出拼数据
	 *
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 * @param id
	 * @param dishType
	 * @return
	 */
	private Map<String, Object> setItemQueryParams(String beginTime, String endTime, String shiftid, String id, String dishType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		if (!shiftid.equals("null")) {
			map.put("shiftid", shiftid);
		}
		if (!id.equals("null")) {
			map.put("id", id);
		}
		if (!dishType.equals("null")) {
			map.put("dishType", dishType);
		}
		return map;
	}

	/**
	 * 优惠方式明细表
	 *
	 * @param req
	 * @param response
	 * @param settlementWay
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 */
	@RequestMapping("/exportxlsB")
	@ResponseBody
	public void exportxlsB(
			HttpServletRequest req,
			HttpServletResponse response,
			@RequestParam(value = "settlementWay", defaultValue = "") String settlementWay,
			@RequestParam(value = "beginTime", defaultValue = "") String beginTime,
			@RequestParam(value = "endTime", defaultValue = "") String endTime,
			@RequestParam(value = "shiftid", defaultValue = "") String shiftid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("settlementWay", settlementWay);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		map.put("shiftid", shiftid);
		map.put("names", "优惠方式明细表");
		map.put("shopname", "新辣道");
		String dateShowbegin = formatDate2(beginTime);
		String dateShowend = formatDate2(endTime);
		if (dateShowbegin.equals(dateShowend)) {
			map.put("dateShow", dateShowbegin);
		} else {
			map.put("dateShow", dateShowbegin + "-" + dateShowend);
		}
		try {
			dayIncomeBillService.exportxlsB(map, req, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	@RequestMapping("/exportxlsC")
	@ResponseBody
	public void exportxlsC(
			HttpServletRequest req,
			HttpServletResponse response,
			@RequestParam(value = "settlementWay", defaultValue = "") String settlementWay,
			@RequestParam(value = "beginTime", defaultValue = "") String beginTime,
			@RequestParam(value = "endTime", defaultValue = "") String endTime,
			@RequestParam(value = "shiftid", defaultValue = "") String shiftid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("settlementWay", settlementWay);
		map.put("beginTime", beginTime);
		map.put("shiftid", shiftid);
		map.put("endTime", endTime);
		map.put("names", "结算方式明细表");
		map.put("shopname", "新辣道");
		String dateShowbegin = formatDate2(beginTime);
		String dateShowend = formatDate2(endTime);
		if (dateShowbegin.equals(dateShowend)) {
			map.put("dateShow", dateShowbegin);
		} else {
			map.put("dateShow", dateShowbegin + "-" + dateShowend);
		}
		try {
			dayIncomeBillService.exportxlsC(map, req, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setParameter(Map<String, Object> params) {
		if (params.get("beginTime") == null
				|| "".equals(String.valueOf(params.get("")))) {
			params.put("beginTime", DateUtils.toString(new Date()) + " "
					+ "00:00:00");
		}
		if (params.get("endTime") == null
				|| "".equals(String.valueOf(params.get("")))) {
			params.put("endTime", DateUtils.dateToString(new Date()));
		}
		if (params.get("shiftid") == null
				|| "".equals(String.valueOf(params.get("")))) {
			params.put("shiftid", "2");
		}
	}

	/**
	 * 退菜明细列表
	 *
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 * @author weizhifang
	 * @since 2015-05-22
	 */
	@RequestMapping("/getReturnDishList")
	public ModelAndView getReturnDishList(
			@RequestParam Map<String, Object> params) {
		List<Map<String, Object>> dishRsult = dayIncomeBillService.getReturnDishList(params);
		return new ModelAndView("/billDetails/askedForARefund", "dishRsult", dishRsult);
	}

	/**
	 * 退菜明细表导出
	 *
	 * @param req
	 * @param response
	 * @param settlementWay
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 */
	@RequestMapping("/exportReturnDishxls")
	@ResponseBody
	public void exportReturnDishxls(
			HttpServletRequest req,
			HttpServletResponse response,
			@RequestParam(value = "beginTime", defaultValue = "") String beginTime,
			@RequestParam(value = "endTime", defaultValue = "") String endTime,
			@RequestParam(value = "shiftid", defaultValue = "") String shiftid) {
		Map<String, Object> params = getParameterMap(beginTime, endTime,
				shiftid);
		List<Map<String, Object>> dishRsult = dayIncomeBillService
				.getReturnDishList(params);
//		exportReturnDishService.formatExcel("全天",dishRsult, req, response);

	}

	/**
	 * 参数拼接
	 *
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 * @return
	 * @author weizhifang
	 * @since 2015-05-23
	 */
	private Map<String, Object> getParameterMap(String beginTime,
												String endTime, String shiftid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		params.put("shiftid", shiftid);
		return params;
	}

	/**
	 * 品项销售报表
	 *
	 * @param params
	 * @return
	 * @author weizhifang
	 * @since 2015-05-25
	 */
	@RequestMapping("/getItemForList")
	public ModelAndView getItemForList(
			@RequestParam Map<String, Object> params, HttpServletRequest request) {
		List<Map<String, Object>> result = dayIncomeBillService.getItemReportForList(params);
		return new ModelAndView("/billDetails/itemDetailReports", "result", result);
	}

	/**
	 * 品项销售报表明细表
	 *
	 * @param params
	 * @return
	 * @author weizhifang
	 * @since 2015-05-26
	 */
	@RequestMapping("/getItemDetailForList")
	public
	@ResponseBody
	List<Map<String, Object>> getItemDetailForList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		List<Map<String, Object>> result = dayIncomeBillService.getItemDetailList(params);
		return result;
	}

	/**
	 * 品项列表品类查询
	 *
	 * @param request
	 * @return
	 * @author weizhifang
	 * @since 2015-5-28
	 */
	@RequestMapping("/getItemTypeList")
	@ResponseBody
	public ModelAndView getItemTypeList(
			@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {
		List<Code> ItemTypeLis = dayIncomeBillService.getItemDescList();
		Code code = new Code();
		code.setCodeId("DISHES_98");
		code.setCodeDesc("餐具");
		ItemTypeLis.add(code);
		ModelAndView mad = new ModelAndView();
		mad.addObject("ItemTypeLis", ItemTypeLis);
		return mad;
	}

}