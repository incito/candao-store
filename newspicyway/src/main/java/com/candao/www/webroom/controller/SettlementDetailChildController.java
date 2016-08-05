package com.candao.www.webroom.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.SettlementDetailChildService;
import com.candao.www.webroom.service.impl.ExportSettlDetChildService;

import net.sf.json.JSONObject;

/**
 * 结算方式明细表子表
 * @author weizhifang
 * @since 2015-11-21
 *
 */
@Controller
@RequestMapping(value="/settlementDetailChild")
public class SettlementDetailChildController {

	@Autowired
	private SettlementDetailChildService settlementDetailChildService;
	
	@Autowired
	private ItemDetailService itemDetailService;
	
	@Autowired
	private ExportSettlDetChildService exportSettlDetChildService;
	
	/**
	 * 查询结算方式子表
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSettDetChildList")
	@ResponseBody
	public String getSettDetChildList(HttpServletRequest request){
		Map<String,Object> params = new HashMap<String,Object>();
		String branchId = request.getParameter("branchId");
		String payWay = request.getParameter("payWay");
		try{
			payWay = URLDecoder.decode(payWay,"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String shiftid = request.getParameter("shiftid");
		String itemid = request.getParameter("itemid");
		String membercardno = request.getParameter("membercardno");
		params.put("branchId", branchId);
		params.put("payWay", payWay);
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		params.put("shiftid", shiftid);
		params.put("itemid", itemid);
		params.put("membercardno", membercardno);
		Map<String,Object> map = new HashMap<String,Object>();
	    try{
	    	List<Map<String,Object>> list = settlementDetailChildService.querySettDetailList(params);
	    	map = ReturnMap.getReturnMap(1, "001", "查询结算明细子表成功");
	    	map.put("data", list);
	    }catch(Exception e){
	    	map = ReturnMap.getReturnMap(0, "002", "查询结算明细子表失败");
	    	e.printStackTrace();
	    }
	    return JacksonJsonMapper.objectToJson(map);
	}
	
	/**
	 * 导出结算方式明细表详情表
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param request
	 * @param response
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 * @param payWay
	 * @param branchId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/exportSettDetailChildList/{beginTime}/{endTime}/{payWay}/{shiftid}/{searchType}/{itemid}/{membercardno}")
	@ResponseBody
	public JSONObject exportSettDetailChildList(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "beginTime") String beginTime,
			@PathVariable(value = "endTime") String endTime,
			@PathVariable(value = "payWay") String payWay,
			@PathVariable(value = "shiftid") String shiftid,
			@PathVariable(value = "searchType") String searchType,
			@PathVariable(value = "itemid") String itemid,
			@PathVariable(value = "membercardno") String membercardno)throws Exception {
		try{
			payWay = URLDecoder.decode(payWay,"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		String branchId = PropertiesUtils.getValue("current_branch_id");
		Map<String,Object> params = setItemQueryParams(beginTime,endTime,shiftid,payWay,branchId,searchType,itemid,membercardno);
		String branchName = itemDetailService.getBranchName(branchId);
		params.put("branchname", branchName);
        Map<String,Object> map = new HashMap<String,Object>();
	    try{
	    	Map<String,Object> parentItem = settlementDetailChildService.querySettList(params);
	    	List<Map<String,Object>> dataMap = settlementDetailChildService.querySettDetailList(params);
	    	exportSettlDetChildService.createExcel(request, response, dataMap, params,parentItem);
	    	map = ReturnMap.getReturnMap(1, "001", "导出结算明细子表成功");
	    }catch(Exception e){
	    	map = ReturnMap.getReturnMap(0, "002", "导出结算明细子表失败");
	    	e.printStackTrace();
	    }
	    return JSONObject.fromObject(map);
	}
	
	/**
	 * 设置查询参数
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 * @param payWay
	 * @param branchId
	 * @param payWayName
	 * @param nums
	 * @param prices
	 * @return
	 */
	private Map<String,Object> setItemQueryParams(String beginTime,String endTime,String shiftid,String payWay,
			String branchId,String searchType,String itemid,String membercardno){
		Map<String, Object> map = new HashMap<String, Object>();
    	if(branchId == null && "".equals(branchId)){
    		branchId = PropertiesUtils.getValue("current_branch_id");
    	}
    	map.put("branchId", branchId);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
        map.put("searchType", searchType);
        map.put("shiftid", shiftid);
        map.put("payWay", payWay);
        map.put("itemid", itemid);
        map.put("membercardno", membercardno);
        return map;
	}
}
