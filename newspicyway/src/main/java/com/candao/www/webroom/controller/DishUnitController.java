package com.candao.www.webroom.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.candao.www.data.model.TdishUnit;
import com.candao.www.webroom.service.DishUnitService;
//*
@RequestMapping("/dishunit")
@Controller
public class DishUnitController {
	@Autowired 
	private DishUnitService dishUnitService;
	/**
	 * 保存多计量单位
	 * @param tdishUnits
	 * @return
	 */
	@RequestMapping("/save")
	public ModelAndView  save(@RequestBody TdishUnit[] tdishUnits){
		boolean b=dishUnitService.addDishUnit(tdishUnits);
		Map<String, Object> map = new HashMap<String, Object>();
		ModelAndView mov=new ModelAndView();
		if(b){
			map.put("message", "success");
		}else{
			map.put("message", "fail");
		}
		mov.addObject(map);
		return mov; 
	}
	/**
	 * 查询菜品的多计量单位
	 */
	@RequestMapping("/getunitList/{dishId}")
	public ModelAndView getunitList(@PathVariable("dishId") String dishId){
		List<TdishUnit> list=dishUnitService.getUnitsBydishId(dishId);
		ModelAndView mov=new ModelAndView();
		mov.addObject(list);
		return mov; 
	}
}
