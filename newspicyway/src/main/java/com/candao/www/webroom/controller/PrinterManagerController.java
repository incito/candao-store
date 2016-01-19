package com.candao.www.webroom.controller;
//在响应中查看jason数据
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.ValidateUtils;
import com.candao.print.entity.TbPrinterArea;
import com.candao.print.entity.TbPrinterDetail;
import com.candao.print.entity.TbPrinterManager;
import com.candao.print.service.PrinterManagerService;
import com.candao.www.constant.Constant;
import com.candao.www.data.model.Tdish;
//*
@Controller
@RequestMapping("/printerManager")
public class PrinterManagerController {
	@Autowired
	private PrinterManagerService printerManagerService;

	@RequestMapping("/page")
	@ResponseBody
	public ModelAndView page(@RequestParam Map<String, Object> params, int page, int rows) {
		Page<Map<String, Object>> pageMap = printerManagerService.grid(params, page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		// return JacksonJsonMapper.objectToJson(page);
		return mav;
	}
/**
 * 获取所有打印机
 * @param params
 * @return
 */

	@RequestMapping("/find")
	@ResponseBody
	public ModelAndView find(@RequestParam Map<String, Object> params) {
		List<Map<String,Object>> list = printerManagerService.find(params);
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			lists.add(list.get(i));
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("find", list);
		// return JacksonJsonMapper.objectToJson(page);
		return mav;
	}
	/**
	 * 根据params中的条件获取餐台和分区信息
	 * @param params
	 * @return
	 */
	@RequestMapping("/findPrinterArea")
	@ResponseBody
	public ModelAndView findPrinterArea(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		TbPrinterManager tbPrinterManager = new TbPrinterManager();
		List<TbPrinterArea> areaList = printerManagerService.findArea(params);
		tbPrinterManager.setTbPrinterAreaList(areaList);

		mav.addObject("tbPrinterManager", tbPrinterManager);
		return mav;
	}
	/**
	 * 根据params中的条件获取菜品和类区信息
	 * @param params
	 * @return
	 */
	@RequestMapping("/findPrinterDish")
	@ResponseBody
	public ModelAndView findPrinterDish(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		TbPrinterManager tbPrinterManager = new TbPrinterManager();
		List<TbPrinterDetail> dishesList = printerManagerService.findDishes(params);
		tbPrinterManager.setTbPrinterDetailList(dishesList);

		mav.addObject("tbPrinterManager", tbPrinterManager);
		return mav;
	}
	@RequestMapping("/index")
	public String index() {
		return "printer/printerManager";
	}
/**
 * 保存打印信息
 * @param tbPrinterManager
 * @return
 */
	@RequestMapping("/save")
	@ResponseBody
	public String save(TbPrinterManager tbPrinterManager) {
		boolean b = false;
		tbPrinterManager.setStatus(1);
		String id = tbPrinterManager.getPrinterid();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (ValidateUtils.isEmpty(id)) {// 增加
				tbPrinterManager.setPrinterid(IdentifierUtils.getId().generate().toString());
				b = printerManagerService.save(tbPrinterManager);
				
			} else {// 修改
				b = printerManagerService.update(tbPrinterManager);
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
		map.put("printerid", tbPrinterManager.getPrinterid());
		return JacksonJsonMapper.objectToJson(map);
	}
	/**
	 * 打印机名称不能重复验证
	 * @param tbPrinterManager
	 * @return
	 */
	@RequestMapping("/validatePrinter")
	@ResponseBody
	public ModelAndView validateArticle(TbPrinterManager tbPrinterManager){
		ModelAndView mav = new ModelAndView();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("printername", tbPrinterManager.getPrintername());
//		params.put("status", 1);
		List<Map<String, Object>> list=printerManagerService.find(params);
		TbPrinterManager a=printerManagerService.findById(tbPrinterManager.getPrinterid());
		//新增
		if(ValidateUtils.isEmpty(tbPrinterManager.getPrinterid())){
			
			if(list!=null&&list.size()>0){
				mav.addObject("messagePrintername", "打印机名称不能重复");
			}
		}else{
			//修改
			if(!a.getPrintername().equals(tbPrinterManager.getPrintername())){
				if(list!=null&&list.size()>0){
					mav.addObject("messagePrintername", "打印机名称不能重复");
				}
			}else{
				if(list!=null&&list.size()>1){
					mav.addObject("messagePrintername", "打印机名称不能重复");
				}
			}
		}
		
		return mav;
	}
	/**
	 * 根据printerid获取该打印机的所有信息
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/findById/{id}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "id") String id, Model model) {
		ModelAndView mav = new ModelAndView();
		TbPrinterManager tbPrinterManager = printerManagerService.findById(id);
		int printerType = tbPrinterManager.getPrintertype();
		Map<String,Object> params=new HashMap<String,Object>();
		String printerid = id;
		params.put("printerid", id);
		
		if(printerType==1){
			
			List<TbPrinterArea> areaList = printerManagerService.findArea(params);
			tbPrinterManager.setTbPrinterAreaList(areaList);
			List<TbPrinterDetail> dishesList = printerManagerService.findDishes(params);
			tbPrinterManager.setTbPrinterDetailList(dishesList);
			List<Map<String, Object>> areaslistTag = printerManagerService.getAreaslistTag(printerid);
			tbPrinterManager.setAreaslistTag(areaslistTag);
			List<Map<String, Object>> dishTypeslistTag = printerManagerService.getDishTypeslistTag(printerid);
			tbPrinterManager.setDishTypeslistTag(dishTypeslistTag);
			
		}else if(printerType==2){
			List<TbPrinterArea> areaList = printerManagerService.findArea(params);
			tbPrinterManager.setTbPrinterAreaList(areaList);
			List<Map<String, Object>> areaslistTag = printerManagerService.getAreaslistTag(printerid);
			tbPrinterManager.setAreaslistTag(areaslistTag);
		}
//		tbPrinterManager.setTbPrinterAreaList(List);
//		printerManagerService.
		
		
		mav.addObject("tbPrinterManager", tbPrinterManager);
		return mav;
	}
	@RequestMapping("/findById2/{id}")
	@ResponseBody
	public ModelAndView findById2(@PathVariable(value = "id") String id, Model model) {
		TbPrinterManager tbPrinterManager = printerManagerService.findById2(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("tbPrinterManager", tbPrinterManager);
		return mav;
	}
	/**
	 * 根据printerid删除打印机
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete/{id}")
	@ResponseBody
	public ModelAndView deleteById(@PathVariable(value = "id") String id) {
		boolean b = printerManagerService.deleteById(id);
		ModelAndView mav = new ModelAndView();
		if (b) {
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("message", "删除失败");
		}
		return mav;
	}
	
	
	
//	@RequestMapping("/getTableTag")
//	@ResponseBody
//	public ModelAndView getTableTag() {
//		
//		List<Map<String, Object>> list = printerManagerService.getTableTag();
//		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
//		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("printername", "全部");
//		maps.put("printerid", "");
//		lists.add(maps);
//		for (int i = 0; i < list.size(); i++) {
//			lists.add(list.get(i));
//		}
//		ModelAndView mav = new ModelAndView("printer/printerManager");
//		mav.addObject(lists);
//		return mav;
//	}
	/**
	 * 更新打印机选中的餐台
	 * @param list
	 * @return
	 */
	@RequestMapping("/addPrinterTables")
	public ModelAndView  addPrinterTables(@RequestBody List<Map<String,Object>> list){
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean a = printerManagerService.deleteTablesByPrinterd((String) list.get(0).get("printerid"));
		
		if (a) {
			map.put("messageDelete", "删除成功");
		} else {
			map.put("messageDelete", "删除失败");
		}
		int b=printerManagerService.addPrinterTables(list);
		
		
		if(b>=0){
			map.put("messageAdd", "success");
		}else{
			map.put("messageAdd", "fail");
		}
		mav.addObject(map);
		return mav; 
	}
	/**
	 * 餐台智能添加（当该分区中餐台被全选后，
	 * 在餐台管理该分区中增加餐台，打印管理中该餐台会被勾选）
	 * @param list
	 * @return
	 */
	@RequestMapping("/addOnePrinterTable")
	public ModelAndView  addOnePrinterTable(@RequestBody List<Map<String,Object>> list){
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		int b=printerManagerService.addPrinterTables(list);
		if(b>=0){
			map.put("messageAdd", "success");
		}else{
			map.put("messageAdd", "fail");
		}
		mav.addObject(map);
		return mav; 
	}
	/**
	 * 更新打印机选中的菜品
	 * @param list
	 * @return
	 */
	@RequestMapping("/addPrinterDishes")
	public ModelAndView  addPrinterDishes(@RequestBody List<Map<String,Object>> list){
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		boolean a = printerManagerService.deleteDishesByPrinterd((String) list.get(0).get("printerid"));
		
		if (a) {
			map.put("messageDelete", "删除成功");
		} else {
			map.put("messageDelete", "删除失败");
		}
		int b=printerManagerService.addPrinterDishes(list);
		
		
		if(b>=0){
			map.put("messageAdd", "success");
		}else{
			map.put("messageAdd", "fail");
		}
		mav.addObject(map);
		return mav; 
	}
	/**
	 * 菜品智能添加（当该分类中菜品被全选后，
	 * 在菜品管理该分区中增加菜品，菜品管理中该菜品会被勾选）
	 * @param list
	 * @return
	 */
	@RequestMapping("/addOnePrinterDish")
	public ModelAndView  addOnePrinterDish(@RequestBody List<Map<String,Object>> list){
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		int b=printerManagerService.addPrinterDishes(list);
		if(b>=0){
			map.put("messageAdd", "success");
		}else{
			map.put("messageAdd", "fail");
		}
		mav.addObject(map);
		return mav; 
	}
	/**
	 * 打印机名称不能重复验证
	 * @param list
	 * @return
	 */
	@RequestMapping("/check_same_IpAddress")
	
	public ModelAndView validatePrintername(@RequestBody List<List<Map<String, Object>>> list){
		ModelAndView mav = new ModelAndView();
		Map<String,Object> paramsTable=new HashMap<String,Object>();
		Map<String,Object> paramsDish=new HashMap<String,Object>();
	
		List<Map<String, Object>> listTable = list.get(0);
//		List<Map<String, Object>> listIp=printerManagerService.findPrinternameByAreaid(listArea);
//		List<Map<String, Object>> listAllName=printerManagerService.findPrintername();
		List<Map<String, Object>> listNameByTableids=printerManagerService.findPrinternameByTableids(paramsTable);
		
		List<Map<String, Object>> listNameByDishids=printerManagerService.findPrinternameByDishids(paramsDish);
		List<Map<String, Object>> tableList= new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> dishList= new ArrayList<Map<String, Object>>();
		for(Map<String,Object> mapTable:listNameByTableids){
			for(Map<String,Object> mapTable2:listTable){
				if(mapTable.get("tableid").equals(mapTable2.get("tableid"))&&!mapTable.get("printerid").equals(mapTable2.get("printerid"))){
					tableList.add(mapTable);
				}
			}
		}
		for(Map<String,Object> mapDish:listNameByDishids){
			for(Map<String,Object> mapDish2:listTable){
				if(mapDish.get("tableid").equals(mapDish2.get("tableid"))&&!mapDish.get("printerid").equals(mapDish2.get("printerid"))){
					dishList.add(mapDish);
				}
			}
		}
//		TbPrinterManager a=printerManagerService.findById((String) listArea.get(0).get("printerid"));
////		新增
//		if(ValidateUtils.isEmpty((String) listArea.get(0).get("printerid"))){
//			
//			if(listIp!=null&&listIp.size()>0){
//				mav.addObject("message", "打印机IP不能重复");
//			}
//		}else{
//			//修改
//			if(!a.getPrintername().equals(tbPrinterManager.getPrintername())){
//				if(listIp!=null&&listIp.size()>0){
//					mav.addObject("message", "打印机IP不能重复");
//				}
//			}else{
//				if(listIp!=null&&listIp.size()>1){
//					mav.addObject("message", "打印机IP不能重复");
//				}
//			}
//		}
		
		return mav;
	}

}
