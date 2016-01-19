package com.candao.www.webroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.candao.www.webroom.service.DishTypeService;

@Controller
@RequestMapping("/posdinterface")
public class PosInterfaceController {

	@Autowired
	private DishTypeService dishTypeService;
	
	/**
	 * 获取预结算信息，根据订单或桌号
	 * @author zhao
	 * @return  json 数据，返回按照桌号查询到的订单信息
	 */
	@RequestMapping("/findOrder/{tableNo}")
	public String getDishByDishType(@PathVariable(value = "tableNo") String tableNo){
		
		
		return "/dishtype/show";
	}
	
	/**
	 * 反结算接口
	 * @author zhao
	 * @return  json 数据，包括分类的菜品
	 */
	@RequestMapping("/index1")
	public String rebackSettle(){
		return "/dishtype/show";
	}
	
	/**
	 * 会员结算接口
	 * @author zhao
	 * @return  json 数据， 
	 */
	public String memeberSettle(){
		return "/dishtype/show";
	}
	
	/**
	 * 现金结算接口
	 * @author zhao
	 * @return  json 数据， 
	 */
	public String cashSettle(){
		return "/dishtype/show";
	}
	
	/**
	 * 银行卡结算接口
	 * @author zhao
	 * @return  json 数据， 
	 */
	public String bankCardSettle(){
		return "/dishtype/show";
	}
	
	/**
	 * 查询会员接口
	 * @author zhao
	 * @return  json 数据， 
	 */
	public String queryMemeberInfo(){
		return "/dishtype/show";
	}
	
	/**
	 *  会员储值接口
	 * @author zhao
	 * @return  json 数据， 
	 */
	public String saveMemberMoney(){
		return "/dishtype/show";
	}
	
	/**
	 *  会员开卡接口
	 * @author zhao
	 * @return  json 数据， 
	 */
	public String openMemberCard(){
		return "/dishtype/show";
	}
	
	/**
	 *  会员注销接口
	 * @author zhao
	 * @return  json 数据， 
	 */
	public String discardMemberCard(){
		return "/dishtype/show";
	}
	
}
