package com.candao.www.webroom.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.common.utils.ValidateUtils;
import com.candao.file.fastdfs.service.FileService;
import com.candao.www.constant.Constant;
import com.candao.www.data.model.Tdish;
import com.candao.www.security.controller.BaseController;
import com.candao.www.webroom.model.TcomboDishGroupList;
import com.candao.www.webroom.service.ComboDishService;
import com.candao.www.webroom.service.DishService;
import com.candao.www.webroom.service.DishTypeService;
//*
@Controller
@RequestMapping("/combodish")
public class ComboDishController extends BaseController{
	@Autowired
	private ComboDishService comboDishService;
	@Autowired
	private DishTypeService dishTypeService;
	@Autowired
	private DishService  dishService;
	@Autowired
	FileService  fileService;

	
	/**
	 * 保存套餐的接口
	 */
	@RequestMapping(value = "/savedishset" )
	@ResponseBody
	public String saveDishSet(HttpServletRequest request,String json, @RequestParam("main_img2") MultipartFile file1){
		TcomboDishGroupList  tcomboDishGroupList=JacksonJsonMapper.jsonToObject(json, TcomboDishGroupList.class);
//		File file = null;
		if (!file1.isEmpty() && ValidateUtils.isEmpty(tcomboDishGroupList.getDish().getImage())) {
//			Date time = new Date();
			String fileName = file1.getOriginalFilename();
//			String dirTime = String.valueOf(time.getTime());
			String extName = fileName.substring(fileName.lastIndexOf(".")+1);
//			String fileupload=PropertiesUtils.getValue("image.path");
//			file = new File(request.getRealPath(fileupload), dirTime + extName);
			try {
//				file1.transferTo(file);
				 String fileUrlpath = fileService.uploadFile(file1.getInputStream(),extName);
				 tcomboDishGroupList.getDish().setImage(fileUrlpath);
//				tcomboDishGroupList.getDish().setImage(fileupload.substring(1)+"/"+ dirTime + extName);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(tcomboDishGroupList == null){
			return   Constant.FAILUREMSG;
		}
	   String jsonString = "";
	   Map<String, Object> map = new HashMap<String, Object>();
		try { 
			
			comboDishService.save(tcomboDishGroupList);
			map.put("tdish", tcomboDishGroupList.getDish());
			jsonString = Constant.SUCCESSMSG;
			map.put("message", jsonString);
		 
		} catch (Exception e) {
			e.printStackTrace();
			jsonString = Constant.FAILUREMSG;
			map.put("message", jsonString);
		}
		return JacksonJsonMapper.objectToJson(map);
	}
	

	/**
	 * 保存套餐的接口
	 */
	@RequestMapping(value = "/savefishpot" )
	@ResponseBody
	public String savefishpot(HttpServletRequest request,String json, @RequestParam("main_img3") MultipartFile file1){
		TcomboDishGroupList  tcomboDishGroupList=JacksonJsonMapper.jsonToObject(json, TcomboDishGroupList.class);
		File file = null;
		if (!file1.isEmpty() && ValidateUtils.isEmpty(tcomboDishGroupList.getDish().getImage())) {
//			Date time = new Date();
			String fileName = file1.getOriginalFilename();
//			String dirTime = String.valueOf(time.getTime());
			String extName = fileName.substring(fileName.lastIndexOf(".")+1);
//			String fileupload=PropertiesUtils.getValue("image.path");
//			file = new File(request.getRealPath(fileupload), dirTime + extName);
			try {
//				file1.transferTo(file);
				String fileUrlpath = fileService.uploadFile(file1.getInputStream(),extName);
				 tcomboDishGroupList.getDish().setImage(fileUrlpath);
//				tcomboDishGroupList.getDish().setImage(fileupload.substring(1)+"/"+ dirTime + extName);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(tcomboDishGroupList == null){
			return   Constant.FAILUREMSG;
		}
	   String jsonString = "";
	   Map<String, Object> map = new HashMap<String, Object>();
		try { 
			
			comboDishService.save(tcomboDishGroupList);
			map.put("tdish", tcomboDishGroupList.getDish());
			jsonString = Constant.SUCCESSMSG;
			map.put("message", jsonString);
		 
		} catch (Exception e) {
			e.printStackTrace();
			jsonString = Constant.FAILUREMSG;
			map.put("message", jsonString);
		}
		return JacksonJsonMapper.objectToJson(map);
	}
	/**
	 * 修改套餐的接口
	 */
	@RequestMapping(value = "/updatedishset" )
	@ResponseBody
	public String updateDishSet(@RequestBody TcomboDishGroupList  tcomboDishGroupList){
		
		if(tcomboDishGroupList == null){
			return   Constant.FAILUREMSG;
		}
	   String jsonString = "";
 
		try { 
			
			comboDishService.update(tcomboDishGroupList);
			 
			jsonString = Constant.SUCCESSMSG;
		 
		} catch (Exception e) {
			e.printStackTrace();
			jsonString = Constant.FAILUREMSG;
		}
		return jsonString;
	}
	@RequestMapping("/findById/{id}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "id") String id, Model model) {
		ModelAndView mav = new ModelAndView();
		Tdish tdish = dishService.findById(id);
		mav.addObject("tdish", tdish);
		List<Map<String,Object>> groupList=comboDishService.getTdishGroupList(id);
		for(Map<String,Object> map:groupList){
			List<Map<String,Object>> list=comboDishService.getTgroupDetailList(map.get("id").toString());
			for(Map<String,Object> dmap:list){
				if("1".equals(dmap.get("dishtype").toString())){
					List<Map<String,Object>> tgroupDetailList2=comboDishService.getTgroupDetailList(dmap.get("id").toString());
					dmap.put("list", tgroupDetailList2);
				}
			}
			map.put("groupDetailList", list);
		}
		mav.addObject("groupList", groupList);
	    List<Map<String,Object>> listType=dishTypeService.findAll("0");
	    mav.addObject("listType", listType);
		return mav;
	}
	@RequestMapping("/findfishpotDetailById/{id}")
	@ResponseBody
	public ModelAndView findfishpotDetailById(@PathVariable(value = "id") String id, Model model) {
		ModelAndView mav = new ModelAndView();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("dishid", id);
		List<Map<String,Object>> list=comboDishService.getFishPotDetailList(map);
		mav.addObject("list", list);
		return mav;
	}
	/**
	 * 通过菜品的id，判断是否被鱼锅、套餐使用
	 * @return
	 */
	@RequestMapping("/ifDishesDetail/{dishid}")
	@ResponseBody
	public ModelAndView ifDishesDetail(@PathVariable(value = "dishid") String dishid){
		ModelAndView mav = new ModelAndView();
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("dishid", dishid);
		List<Map<String,Object>> list=comboDishService.ifDishesDetail(map);
		mav.addObject("list", list);
		return mav;
	}
	/**
	 * 通过菜品分类的id，判断是否被鱼锅、套餐使用
	 * @return
	 */
	@RequestMapping("/ifDelDishType")
	@ResponseBody
	public ModelAndView comfirmDelDishType(@RequestParam Map<String, Object> params){
		ModelAndView mav = new ModelAndView();
		List<Tdish> listDishes=dishService.getDishesByDishType((String) params.get("dishType"));
		String message = "0";
		for(Tdish tdish:listDishes){
			Map<String,Object> mapDish =new HashMap<String,Object>();
			mapDish.put("dishid", tdish.getDishid());
			List<Map<String,Object>> list=comboDishService.ifDishesDetail(mapDish);
			if(list.size()!=0){
				message = "1";
				break;
			}
		}

		mav.addObject("message", message);
		
		return mav;
	}
}
