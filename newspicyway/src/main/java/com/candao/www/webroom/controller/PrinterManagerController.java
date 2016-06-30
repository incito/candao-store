package com.candao.www.webroom.controller;
//在响应中查看jason数据
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.tools.GrapeUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.candao.print.entity.GroupDishBusiness;
import com.candao.print.entity.TbPrinterArea;
import com.candao.print.entity.TbPrinterDetail;
import com.candao.print.entity.TbPrinterManager;
import com.candao.print.service.PrinterManagerService;
import com.candao.www.printer.listener.PrinterListenerManager;
import com.candao.www.spring.SpringContext;
import com.candao.www.webroom.service.DishService;
import com.candao.www.webroom.service.DishTypeService;
//*
@Controller
@RequestMapping("/printerManager")
public class PrinterManagerController {
	@Autowired
	private PrinterManagerService printerManagerService;
	@Autowired
	private DishService dishService;
	@Autowired
	private DishTypeService dishTypeService;
	
	private Log log = LogFactory.getLog(PrinterManagerController.class.getName());

	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 200, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5000));
	
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
		List<Map<String, Object>> dishesList = printerManagerService.findDishes(params);
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
			log.error("-----------------打印机状态更新失败！", e);
			e.printStackTrace();
		}
		//added by caicai
		//更新mq监听队列
		executor.execute(new Runnable() {
			public void run() {
				try {
					PrinterListenerManager printerListener = (PrinterListenerManager) SpringContext
							.getBean(PrinterListenerManager.class);
					printerListener.updateListenerTemplate();
				} catch (Exception e) {
					log.error("----------------------------------");
					log.error("打印机模板字体更新失败！", e);
					e.printStackTrace();
				}
			}
		});
		// TODO: 2016/6/17 刷新监听器有可能造成线程阻塞 ，弃用
//		printerListener.updateListener();
		
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
		
		if(printerType==1){//厨打单
			
			List<TbPrinterArea> areaList = printerManagerService.findArea(params);
			tbPrinterManager.setTbPrinterAreaList(areaList);
			List<Map<String, Object>> dishesList = printerManagerService.findDishes(params);
			tbPrinterManager.setTbPrinterDetailList(dishesList);
			//菜谱分组信息
			List<GroupDishBusiness> groupDishList = findGroupDishList(dishesList);
			tbPrinterManager.setGroupDishList(groupDishList);
			
			List<Map<String, Object>> areaslistTag = printerManagerService.getAreaslistTag(printerid);
			tbPrinterManager.setAreaslistTag(areaslistTag);
			List<Map<String, Object>> dishTypeslistTag = printerManagerService.getDishTypeslistTag(printerid);
			tbPrinterManager.setDishTypeslistTag(dishTypeslistTag);
			
		}else if(printerType==2){//客用单
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
	/**
	 * 获取菜品的分组信息
	 * @param dishesList
	 * @return
	 */
	private List<GroupDishBusiness> findGroupDishList(List<Map<String, Object>> dishesList) {
		List<GroupDishBusiness> groupDishList = new ArrayList<>();
		Map<String, List<Map<String, Object>>> groupMap = new HashMap<>();
		for (Map<String, Object> map : dishesList) {
			String groupSequence = (String) map.get("groupsequence");
			if(groupSequence != null){
				List<Map<String, Object>> dishList = groupMap.get(groupSequence);
				if(dishList == null){
					GroupDishBusiness groupDish = new GroupDishBusiness();
					groupDish.setGroupid(groupSequence);
					dishList = new ArrayList<>();
					groupDish.setValues(dishList);
					groupMap.put(groupSequence, dishList);
					groupDishList.add(groupDish);
				}
				//菜品去重
				boolean isExist = false;
				String dishid = (String) map.get("dishid");
				for (Map<String, Object> existDish : dishList) {
					if(dishid.equals(existDish.get("dishid"))){
						isExist = true;
						break;
					}
				}
				if(!isExist){
					dishList.add(map);
				}
			}
		}
		return groupDishList;
	}
	@RequestMapping("/getDishOfPrinter")
	@ResponseBody
	public ModelAndView getDishOfPrinter(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		List<String> idList = JacksonJsonMapper.jsonToList((String) params.get("dishids"), String.class);
		//
		List<Map<String, List<Map<String, Object>>>> list = new ArrayList<Map<String, List<Map<String, Object>>>>();
		List<Map<String, Object>> listDishType = dishTypeService.findAll("0");
		for (Map<String, Object> map : listDishType) {
			Map<String, List<Map<String, Object>>> maptotal = new HashMap<String, List<Map<String, Object>>>();
			// 只获取"打印菜品"中已勾选的单品菜
			List<Map<String, Object>> choosedDish = new ArrayList<>();
			List<Map<String, Object>> dishlist = dishService.getDishMapByType(map.get("id").toString());
			for (Map<String, Object> dish : dishlist) {
				for (String dishId : idList) {
					if (dish.get("dishid").equals(dishId) && (Integer)dish.get("dishtype") == 0) {
						choosedDish.add(dish);
					}
				}
			}
			if (!choosedDish.isEmpty()) {
				maptotal.put(JacksonJsonMapper.objectToJson(map), choosedDish);
				list.add(maptotal);
			}
		}

		mav.addObject("dishesList", list);
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
	
	@RequestMapping("/addGroupDishes")
	public ModelAndView  addGroupDishes(@RequestBody List<Map<String,Object>> list){
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		boolean a = printerManagerService.cleanDishGroup((String) list.get(0).get("printerid"));
		if (a) {
			map.put("messageDelete", "删除成功");
		} else {
			map.put("messageDelete", "删除失败");
		}
		
		boolean success = printerManagerService.updateDishGroup(list);
		
		if(success){
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
