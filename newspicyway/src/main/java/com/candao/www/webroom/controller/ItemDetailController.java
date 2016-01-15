package com.candao.www.webroom.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.webroom.model.Code;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.impl.ExportItemDetailService;

/**
 * 品项销售明细表
 * @author ministrator
 *
 */
@Controller
@RequestMapping(value="/itemDetail")
public class ItemDetailController {

	@Autowired
	private ItemDetailService itemDetailService;
	
	@Autowired
	private ExportItemDetailService exportItemDetailService;
	
	// 品项明细
	@RequestMapping("/dishsalerept")
	public ModelAndView itemDetailReports(@RequestParam Map<String, Object> params) {
		return getData("/billDetails/itemDetailReports", params);
	}
	
	/**
	 * 报表请求时参数传递
	 * @param path
	 * @param params
	 * @return
	 */
	private ModelAndView getData(String path, Map<String, Object> params){
		ModelAndView mav = new ModelAndView(path);
		Timestamp beginTime = null;
		Timestamp endTime = null;
		String dateType = null;
		if (params.get("beginTime") != null && params.get("beginTime") != "") {
			String beginTimeStr = params.get("beginTime").toString();
			beginTime = Timestamp.valueOf(beginTimeStr);
		}
		if (params.get("endTime") != null && params.get("endTime") != "") {
			String endTimeStr = params.get("endTime").toString();
			endTime = Timestamp.valueOf(endTimeStr);
		}
		if (params.get("dateType") != null && params.get("dateType") != "") {
			dateType = params.get("dateType").toString();
		}
		mav.addObject("beginTime", beginTime);
		mav.addObject("endTime", endTime);
		mav.addObject("dateType", dateType);
		return mav;
	}
	
	/**
	 * 品项销售报表
	 * 
	 * @author weizhifang
	 * @since 2015-05-25
	 * @param params
	 * @return
	 */
	@RequestMapping("/getItemForList")
	public ModelAndView getItemForList(
			@RequestParam Map<String, Object> params, HttpServletRequest request) {
		setParameter(params);
		List<Map<String,Object>> result = itemDetailService.itemDetailProcedure(params);
		return new ModelAndView("/billDetails/itemDetailReports", "result", result);
	}

	/**
	 * 品项销售报表明细表子表
	 * 
	 * @author weizhifang
	 * @since 2015-05-26
	 * @param params
	 * @return
	 */
    @RequestMapping("/getItemDetailForList")
    public @ResponseBody List<Map<String,Object>> getItemDetailForList(@RequestParam Map<String, Object> params, HttpServletRequest request){
    	setParameter(params);
    	List<Map<String,Object>> result = itemDetailService.itemSubDetailProcedure(params);
    	return result;
    }
    
    /**
     * 设置查询参数
     * @author weizhifang
     * @since 2015-07-04
     * @param params
     * @return
     */
    private Map<String,Object> setParameter(Map<String,Object> params){
        String branchid = PropertiesUtils.getValue("current_branch_id");
        params.put("branchId", branchid);
		if(params.get("dishType") == null || "".equals(params.get("dishType"))){
			params.put("dishType", -1);
		}
		if(params.get("shiftid") == null || "".equals(params.get("shiftid"))){
			params.put("shiftid", -1);
		}
		if(params.get("id") == null || "".equals(params.get("id"))){
			params.put("id", -1);
		}
		return params;
    }
    
    /**
     * 品项列表品类查询
     * @author weizhifang
     * @since 2015-5-28
     * @param request
     * @return
     */
    @RequestMapping("/getItemTypeList")
	@ResponseBody
	public ModelAndView getItemTypeList(
			@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {
		List<Code> ItemTypeLis = itemDetailService.getItemDescList();
		Code code =  new Code();
		code.setCodeId("DISHES_98");
		code.setCodeDesc("餐具");
		ItemTypeLis.add(code);
		ModelAndView mad = new ModelAndView();
		mad.addObject("ItemTypeLis", ItemTypeLis);
		return mad;
	}
    
    
    /**
	 * 品项销售明细表导出
	 * @author weizhifang
	 * @since 2015-5-30
	 * @param request
	 * @param response
	 * @param dishtype
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 * @param itemIdFlag
	 */
	@RequestMapping("/exportxlsA/{beginTime}/{endTime}/{shiftid}/{id}/{dishType}/{itemids}/{searchType}")
	@ResponseBody
	public void exportxlsA(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "beginTime") String beginTime,
			@PathVariable(value = "endTime") String endTime,
			@PathVariable(value = "shiftid") String shiftid,
			@PathVariable(value = "id") String id,
			@PathVariable(value = "dishType") String dishType,
			@PathVariable(value = "itemids") String itemids,
			@PathVariable(value = "searchType") String searchType) throws Exception{
		//设置参数
		Map<String,Object> params = setItemQueryParams(beginTime,endTime,shiftid,id,dishType,searchType);
		Map<String,Object> map = setParameter(params);
		//查询门店名称
		String branchid = PropertiesUtils.getValue("current_branch_id");
        String branchname = itemDetailService.getBranchName(branchid);
        map.put("branchname", branchname);
		List<Map<String,Object>> itemList = itemDetailService.itemDetailProcedure(map);
		List<Map<String,Object>> itemDetail = itemDetailService.itemSubDetailProcedure(map);
		exportItemDetailService.createExcel(request,response,itemList,itemDetail,params);
	}
	
	/**
	 * 品项导出拼数据
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 * @param id
	 * @param dishType
	 * @return
	 */
	private Map<String,Object> setItemQueryParams(String beginTime,String endTime,String shiftid,String id,String dishType,String searchType){
		Map<String, Object> map = new HashMap<String, Object>();
        String branchid = PropertiesUtils.getValue("current_branch_id");
        map.put("branchId", branchid);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		if(!shiftid.equals("null")){
			map.put("shiftid", shiftid);
		}
        if(!id.equals("null")){
        	map.put("id", id);
        }
        if(!dishType.equals("null")){
        	map.put("dishType", dishType);
        }
        map.put("searchType", searchType);
        return map;
	}
	
    
    /**
     * 品项列表品类查询
     * @author weizhifang
     * @since 2015-5-28
     * @param request
     * @return
     */
    @RequestMapping("/getItemTypeListForPx")
	@ResponseBody
	public ModelAndView getItemTypeListForPx(
			@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {
		List<Code> ItemTypeLis = itemDetailService.getItemDescList();
		List<Code> returnItemTypeLis = new ArrayList<Code>();
		for(Code co : ItemTypeLis){
			if(!co.getCodeDesc().equals("鱼锅")){
				returnItemTypeLis.add(co);
			}
		}
		ModelAndView mad = new ModelAndView();
		mad.addObject("ItemTypeLis", returnItemTypeLis);
		return mad;
	}
	/**
	 * 查询所有门店列表
	 * @author weizhifang
	 * @since 2015-8-21
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getBranchList")
	public @ResponseBody ModelAndView getBranchList(HttpServletRequest request, HttpServletResponse response){
		List<Map<String,Object>> branchList = new ArrayList<Map<String,Object>>();
		Map<String,Object> branchMap = new HashMap<String,Object>();
		branchMap.put("branchId", PropertiesUtils.getValue("current_branch_id"));
		branchMap.put("branchname", "");
		branchList.add(branchMap);
		ModelAndView mad = new ModelAndView();
		mad.addObject("branchList", branchList);
		return mad;
	}
}
