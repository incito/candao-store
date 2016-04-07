/**
 * 
 */
package com.candao.www.webroom.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.OtherCouponService;


//*
/**
 * 挂账信息统计
 *  @author 
 *  @serialData 
 */
@Controller
@RequestMapping("/gisterBill")
//@RequestMapping("/printer2")
public class RegisterBillController {
  @Autowired
  private com.candao.www.webroom.service.impl.RegisterBillService  regiterBillService;
  @Autowired
	private ItemDetailService itemDetailService;
  /**
	 * 查询挂账信息统计
	 * @param params
	 * @return
	 * @author lizongren
	 */
	@RequestMapping("/getBillCount")
//	@RequestMapping("/find")
	@ResponseBody
	public ModelAndView BillCount(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		try {
			String branchid = PropertiesUtils.getValue("current_branch_id");
			params.put("branchid", branchid);
			List<Map<String,Object>> resultList = regiterBillService.BillCount(params);
			mav.addObject("code","1");
			mav.addObject("msg","查询成功!");
			mav.addObject("resultList",resultList);
		} catch (Exception e) {

			mav.addObject("code","0");
			mav.addObject("msg","查询失败!");
		}
		return mav;
	}
	
	/**
	 * 查询挂账明细表
	 * @param params
	 * @return
	 * @author lizongren
	 */
	@RequestMapping("/getBillDetail")
//	@RequestMapping("/find")
	@ResponseBody
	public ModelAndView BillDetail(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		try {
			String branchid = PropertiesUtils.getValue("current_branch_id");
			params.put("branchid", branchid);
			List<Map<String,Object>> resultList = regiterBillService.BillDetail(params);
			mav.addObject("code","1");
			mav.addObject("msg","查询成功!");
			mav.addObject("resultList",resultList);
		} catch (Exception e) {

			mav.addObject("code","0");
			mav.addObject("msg","查询失败!");
		}
		return mav;
	}
	 
	
	/**
	 * 查询挂账单位
	 * @param params
	 * @return
	 * @author lizongren
	 */
	@RequestMapping("/getBillUnit")
//	@RequestMapping("/find")
	@ResponseBody
	public ModelAndView BillUnit(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		try {
			List<Map<String,Object>> resultList = regiterBillService.BillUnit(params);
			mav.addObject("code","1");
			mav.addObject("msg","查询成功!");
			mav.addObject("resultList",resultList);
		} catch (Exception e) {

			mav.addObject("code","0");
			mav.addObject("msg","查询失败!");
		}
		return mav;
	}
	
	/**
	 * 查询挂账结算历史
	 * @param params
	 * @return
	 * @author lizongren
	 */
	@RequestMapping("/getBillHistory")
//	@RequestMapping("/find")
	@ResponseBody
	public ModelAndView BillHistory(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		try {
			String branchid = PropertiesUtils.getValue("current_branch_id");
			params.put("branchid", branchid);
			List<Map<String,Object>> resultList = regiterBillService.BillHistory(params);
			mav.addObject("code","1");
			mav.addObject("msg","查询成功!");
			mav.addObject("resultList",resultList);
		} catch (Exception e) {

			mav.addObject("code","0");
			mav.addObject("msg","查询失败!");
		}
		return mav;
	}
	
	/**
	 * 结算
	 * @param params
	 * @return
	 * @author lizongren
	 */
	@RequestMapping("/Billing")
//	@RequestMapping("/find")
	@ResponseBody
	public ModelAndView Billing(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		try {
			String branchid = PropertiesUtils.getValue("current_branch_id");
			params.put("branchid", branchid);
			List<Map<String,Object>> resultList = regiterBillService.Billing(params);
			mav.addObject("code","1");
			mav.addObject("msg","查询成功!");
//			mav.addObject("resultList",resultList);
		} catch (Exception e) {

			mav.addObject("code","0");
			mav.addObject("msg","查询失败!");
		}
		return mav;
	}
	
	/**
	 * 导出挂账统计表主表
	 * @author weizhifang
	 * @since 2016-3-16
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/exportRegisterBill")
	@ResponseBody
	public void exportRegisterBill(@RequestParam Map<String, Object> params,HttpServletRequest request, HttpServletResponse response){
//		String branchid = PropertiesUtils.getValue("current_branch_id");
//		String branchname = itemDetailService.getBranchName(branchid);
//		params.put("branchname", branchname);
//		params.put("branchId", branchid);
//		params.put("pi_dqym","-1");
//		params.put("pi_myts","-1");
//		List<Map<String,Object>> list =  regiterBillService.BillCount(params);
		
		String branchid = PropertiesUtils.getValue("current_branch_id");
		String branchname = itemDetailService.getBranchName(branchid);
		params.put("branchid", branchid);
		params.put("branchname", branchname);
		params.put("pi_dqym","-1");
    	params.put("pi_myts","-1");
		List<Map<String,Object>> list = regiterBillService.BillCount(params);
		
		regiterBillService.createMainExcel(request, response, list, params);
	}
	
}
