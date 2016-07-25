package com.candao.www.webroom.controller;
//*
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.data.model.TbTable;
import com.candao.www.data.model.Tdish;
import com.candao.www.security.controller.BaseController;
import com.candao.www.webroom.service.TableAreaService;
import com.candao.www.webroom.service.TableService;
//can not connect 是连接不上数据库，报错不一定准确
@Controller
@RequestMapping("/table")
public class TableController extends BaseController{
	
	@Autowired
	private TableService tableService;
	@Autowired
	private TableAreaService tableAreaService;
	//餐桌id
	private static int tableId = 1;
	
	/**
	 * 获取所有餐桌
	 * @return
	 */
	 
	 @RequestMapping(value = "/page")
	  public ModelAndView page() {
	    ModelAndView mv = new ModelAndView("table/table");
	  
	    HashMap<String, Object>  pageMap = this.getReqParamMap();
	    Page<Map<String, Object>> page = tableService.grid(pageMap,
	        1,
	        1000);
	    mv.addObject("datas", page.getRows());
	    mv.addAllObjects(pageMap);
	   // mv.addObject("total", page.getTotal());
	    //mv.addObject("totalpage", page.getPageCount());
	    return mv;
	  }
	 /**
		 * 获取所有餐桌
		 * @return
		*/
	 @RequestMapping(value = "/index")
	  public ModelAndView index() {
	    ModelAndView mv = new ModelAndView("table/table");
	    Map<String, Object> map2=new HashMap<String, Object>();
	    List<Map<String, Object>> list = tableAreaService.findTableCountAndAreaname();
	    int areasLength = list.size();
	    if(areasLength>0){
	    	Map<String, Object> map = list.get(0);
	    	mv.addObject("firstArea",map);
	    	map2.put("areaid", list.get(0).get("areaid"));
			String branchId = PropertiesUtils.getValue("current_branch_id");
	    	map2.put("branchid", branchId);
	    }
	    
	    
	    
	    Page<Map<String, Object>> page = tableService.grid(map2,
	        1,
	        10000);
	    
	    mv.addObject("datas", page.getRows());     
	    mv.addObject("total", page.getTotal());
	    mv.addObject("totalpage", page.getPageCount());
	    mv.addObject("areanames", list);
	    mv.addObject("areasLength",areasLength);
	    
	    return mv;
	  }
	 
	 
	/**
	 * 保存餐台
	 * @param tableMap
	 * @return
	 */

	@RequestMapping("/save")
	@ResponseBody
	public String save(@RequestBody HashMap<String, Object> tableMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean b = false;
		TbTable tbTable = new TbTable();
		tbTable.setAreaid((String) tableMap.get("areaid"));
		
		tbTable.setCustPrinter((String) tableMap.get("printerid"));
		String fixprice = (String) tableMap.get("fixprice");
		
		tbTable.setFixprice( new BigDecimal((String) (fixprice== "" ? "0":fixprice)));
		String minprice = (String) tableMap.get("minprice");
		tbTable.setMinprice( new BigDecimal ((String) (minprice== "" ? "0":minprice)));
		String personNum = (String) tableMap.get("personNum");
		tbTable.setPersonNum( new Integer((String) (personNum== "" ? "0":personNum)));
		tbTable.setTableid((String) tableMap.get("tableid"));
		tbTable.setTableName((String) tableMap.get("tableName"));
		tbTable.setTableNo((String) tableMap.get("tableNo"));
		tbTable.setTabletype((String) tableMap.get("tabletype"));
		
		String id = tbTable.getTableid();
	
		//只是获取页面的数据
		try {
			if (ValidateUtils.isEmpty(id)) {// 增加
				synchronized (this) {
					id = tableId++ + "-" + PropertiesUtils.getValue("tenant_id");
					tbTable.setTableid(id);
				}
				tbTable.setStatus(0);
				//0空闲
				b = tableService.save(tbTable);
				map.put("tableid", tbTable.getTableid());
			} else {// 修改
				b = tableService.update(tbTable);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		if (b) {
			if (ValidateUtils.isEmpty(id)) {
				map.put("maessge", "添加成功");
			} else {
				map.put("maessge", "修改成功");
			}
		} else {
			if (ValidateUtils.isEmpty(id)) {
				map.put("maessge", "添加失败");
			} else {
				map.put("maessge", "修改失败");
			}
		}
		return JacksonJsonMapper.objectToJson(map);
	}
	
	public static void main(String args[]){
		System.out.println(IdentifierUtils.getId().generate().toString());
	}
//	@RequestMapping("/save")
//	@ResponseBody
//	public String save(@RequestBody TbTable tbTable, String json, Model model) {
	
	/**
	 * 餐台名称不能重复验证
	 * @param tbTable
	 * @return
	 */
	@RequestMapping("/validateTable")
	@ResponseBody
	public ModelAndView validateArticle(TbTable tbTable){
		String areaname="";
		ModelAndView mav = new ModelAndView("table/table");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("tableName", tbTable.getTableName());
//		params.put("status", 0);
		
		List<Map<String, Object>> list=tableService.find(params);
		TbTable a=tableService.findById(tbTable.getTableid());
		//新增
		if(ValidateUtils.isEmpty(tbTable.getTableid())){
			if(list!=null&&list.size()>0){
				areaname=(String) list.get(0).get("areaname");
				mav.addObject("message", "餐台名称不能重复");
				mav.addObject("messageDetail","该餐台名称在"+areaname+"分区已存在");
			}
		}else{
			//修改
			if(!a.getTableName().equals(tbTable.getTableName())){
				if(list!=null&&list.size()>0){
					areaname=(String) list.get(0).get("areaname");
					mav.addObject("message", "餐台名称不能重复");
					mav.addObject("messageDetail","该餐台名称在"+areaname+"分区已存在");
				
					
				}
			}else{
				if(list!=null&&list.size()>1){
					areaname=(String) list.get(1).get("areaname");
					mav.addObject("message", "餐台名称不能重复");
					mav.addObject("messageDetail","该餐台名称在"+areaname+"分区已存在");
					
					
				}
			}
		}
		
		return mav;
	}
/**
 * 根据id获取餐台
 * @param id
 * @param model
 * @return
 */
	
	
	@RequestMapping("/findById/{id}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "id") String id, Model model) {
		TbTable tbTable = tableService.findById(id);
		ModelAndView mav = new ModelAndView("table/table");
//		
//		BigDecimal fixprice = tbTable.getFixprice();
//		if(fixprice.compareTo(new BigDecimal(0)) ==0){
//			tbTable.setFixprice(new   BigDecimal(""));
//		}
		mav.addObject("tbTable", tbTable);
		return mav;
	}
/**
 * 删除餐台
 * @param id
 * @return
 */
	@RequestMapping("/delete/{ss}")
	@ResponseBody
	public ModelAndView deleteById(@PathVariable(value = "ss") String id) {
		boolean b = tableService.deleteById(id);
		ModelAndView mav = new ModelAndView("table/table");
		if (b) {
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("message", "删除失败");
		}
		return mav;
	}
	@RequestMapping("/deleteTablesByAreaid/{areaid}")
	@ResponseBody
	public ModelAndView deleteTablesByAreaid(@PathVariable(value = "areaid") String areaid) {
		boolean b = tableService.deleteTablesByAreaid(areaid);
		ModelAndView mav = new ModelAndView("table/table");
		if (b) {
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("message", "删除失败");
		}
		return mav;
	}
/**
 * 获取所有区域名和区域id

 * @return
 */
	@RequestMapping("/getTableTag")
	@ResponseBody
	public ModelAndView getTableTag() {
		List<Map<String, Object>> list = tableService.getTableTag();
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("areaname", "全部");
//		maps.put("areaid", "0");
//		lists.add(maps);
		for (int i = 0; i < list.size(); i++) {
			lists.add(list.get(i));
		}
		ModelAndView mav = new ModelAndView("table/table");
		mav.addObject(lists);
		return mav;
	}
	/**
	 * 获取所有打印名和打印id

	 * @return
	 */
	@RequestMapping("/getPrinterTag")
	@ResponseBody
	public ModelAndView getPrinterTag() {
		
		List<Map<String, Object>> list = tableService.getPrinterTag();
//		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
//		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("areaname", "全部");
//		maps.put("areaid", "0");
//		lists.add(maps);
//		for (int i = 0; i < list.size(); i++) {
//			lists.add(list.get(i));
//		}
		ModelAndView mav = new ModelAndView("table/table");
		mav.addObject(list);
		return mav;
	}
	@RequestMapping("/getbuildingNoANDTableTypeTag")
	@ResponseBody
	public ModelAndView getbuildingNoANDTableTypeTag() {
		
		List<Map<String, Object>> list = tableService.getbuildingNoANDTableTypeTag();
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
//		Map<String, Object> map2 = new HashMap<String, Object>();
		
		
	
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map=list.get(i);
		
			
			if(map.get("buildingNo").equals("1")){
				map.put("BName", "一楼");
			}else{
				map.put("BName", "未知");
			}
			if(map.get("tabletype").equals("0")){
				map.put("TName","散台");
			}else if(map.get("tabletype").equals("1")){
				map.put("TName","包间");
			}else{
				map.put("TName","未知");
			}
			list2.add(map);
		}
		ModelAndView mav = new ModelAndView("table/table");
		mav.addObject(list2);
		return mav;
	}
	/**
	 * 通过餐台分类的id，获取分类下的所有餐台
	 * @return
	 */
	@RequestMapping("/getTablesByTableType/{areaid}")
	@ResponseBody
	public ModelAndView getDishesByDishType(@PathVariable(value = "areaid") String areaid){
		ModelAndView mav = new ModelAndView();
		List<TbTable> list=tableService.getTablesByTableType(areaid);
		mav.addObject("list", list);
		return mav;
	}
	/**
	 * 获取所有餐台和对应分区
	 * @return
	 */
	@RequestMapping("/getTypeAndTableMap")
	@ResponseBody
	public ModelAndView getTypeAndDishMap(){
		ModelAndView mav = new ModelAndView();
		List<Map<String,List<Map<String, Object>>>> list=new ArrayList<Map<String,List<Map<String, Object>>>>();
	    List<Map<String,Object>> listTableArea=tableAreaService.getTableAreaTag();
	    Map<String,Object> mapAreaid = new HashMap<String, Object>();
	    for(Map<String,Object> map:listTableArea){
	      Map<String,List<Map<String, Object>>> maptotal=new  HashMap<String,List<Map<String, Object>>>();
	      mapAreaid.put("areaid", map.get("areaid"));
	      List<Map<String, Object>> tablelist= tableService.find(mapAreaid);
	      
	      maptotal.put(JacksonJsonMapper.objectToJson(map), tablelist);
	      list.add(maptotal);
	    }

		mav.addObject("page", list);
		return mav;
	}
}
