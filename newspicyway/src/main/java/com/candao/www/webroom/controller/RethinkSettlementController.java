package com.candao.www.webroom.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.RethinkSettlementService;
import com.candao.www.webroom.service.impl.ExportRethinkSettlementService;

/**
 * 反结算统计表
 * @author weizhifang
 * @since 2015-11-18
 *
 */
@Controller
@RequestMapping("/dishtypeSettlement")
public class RethinkSettlementController {

	@Autowired
	private RethinkSettlementService rethinkSettlementService;
	
	@Autowired
	private ItemDetailService itemDetailService;
	
	@Autowired
	private ExportRethinkSettlementService exportRethinkSettlementService;
	
	private static Log log = LogFactory.getLog(RethinkSettlementController.class);
	
	/**
	 * 查询反结算列表数据
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getRethinkSettlementList", method = RequestMethod.POST)
	public JSONObject getRethinkSettlementList(HttpServletRequest request){
		String branchId = request.getParameter("branchId");
		if(branchId == null){
			branchId = PropertiesUtils.getValue("current_branch_id");
		}
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("branchId", branchId);
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			log.info("查询反结算列表参数："+params.toString());
			List<Map<String,Object>> rs = rethinkSettlementService.queryRethinkSettlement(params);
			log.info("查询反结算列表数据："+rs.toString());
			JSONArray data = JSONArray.fromObject(rs);
		    map = ReturnMap.getReturnMap(1, "001", "查询反结算数据成功");
		    map.put("data", data);
		}catch(Exception e){
			map = ReturnMap.getReturnMap(0, "002", "查询反结算数据失败");
			e.printStackTrace();
		}
		return JSONObject.fromObject(map);
	}
	
	/**
	 * 查询结账单
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param reqeust
	 * @return
	 */
	@RequestMapping(value="/queryStatement",method = RequestMethod.POST)
	public JSONObject queryStatement(HttpServletRequest request){
		String orderid = request.getParameter("orderid");
//		String branchId = request.getParameter("branchId");
//		if(branchId == null){
			String branchId = PropertiesUtils.getValue("current_branch_id");
//		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderid", orderid);
		params.put("branchId", branchId);
		if (StringUtils.isBlank(branchId)) {
            branchId = PropertiesUtils.getValue("current_branch_id");
        }
        String branchname = itemDetailService.getBranchName(branchId);
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			String tableNo = rethinkSettlementService.queryTableNo(params);
			if(tableNo == null){
				map = ReturnMap.getReturnMap(0, "003", "没有相匹配桌台信息");
			}else{
				Map<String,Object> rs = rethinkSettlementService.queryStatement(params);
				rs.put("branchname", branchname);
				JSONObject data = JSONObject.fromObject(rs);
			    map = ReturnMap.getReturnMap(1, "001", "查询反结算数结账单数据成功");
			    map.put("data", data);
			}
		}catch(Exception e){
			map = ReturnMap.getReturnMap(0, "002", "查询结账单数据失败");
			e.printStackTrace();
		}
		return JSONObject.fromObject(map);
	}
	
	/**
	 * 反结算统计表导出
	 * @author weizhifang
	 * @since 2015-11-24
	 * @param request
	 * @param response
	 * @param branchId
	 * @param beginTime
	 * @param endTime
	 * @param searchType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/exportSettDetailChildList/{beginTime}/{endTime}/{searchType}")
	@ResponseBody
	public JSONObject exportSettDetailChildList(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "beginTime") String beginTime,
			@PathVariable(value = "endTime") String endTime,
			@PathVariable(value = "searchType") String searchType)throws Exception {
		String branchId = PropertiesUtils.getValue("current_branch_id");
		Map<String,Object> params = setItemQueryParams(beginTime,endTime,branchId,searchType);
		String branchName = itemDetailService.getBranchName(branchId);
		params.put("branchname", branchName);
		params.put("shiftid", "-1");
		Map<String,Object> map = new HashMap<String,Object>();
	    try{
	    	List<Map<String,Object>> rs = rethinkSettlementService.queryRethinkSettlement(params);
	    	exportRethinkSettlementService.createExcel(request, response, rs, params);
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
	 * @since 2015-11-24
	 * @param beginTime
	 * @param endTime
	 * @param branchId
	 * @param searchType
	 * @return
	 */
	private Map<String,Object> setItemQueryParams(String beginTime,String endTime,
			String branchId,String searchType){
		Map<String, Object> map = new HashMap<String, Object>();
    	if(branchId == null && "".equals(branchId)){
    		branchId = PropertiesUtils.getValue("current_branch_id");
    	}
    	map.put("branchId", branchId);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
        map.put("searchType", searchType);
        return map;
	}
}
