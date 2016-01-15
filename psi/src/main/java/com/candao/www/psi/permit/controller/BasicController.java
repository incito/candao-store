package com.candao.www.psi.permit.controller;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.Constant;
import com.candao.www.data.model.User;




/**
 * 进销存基础信息模块
 * @author xiaokai
 *
 */
@Controller
@RequestMapping("/psi")
public class BasicController {

	private String web_user_id="";
	
	private void getinfos(HttpServletRequest request){
		if(request.getSession().getAttribute(Constant.CURRENT_USER)!=null){
	  		User user = (User)request.getSession().getAttribute(Constant.CURRENT_USER);
	  		if(user!=null){
	  			web_user_id=user.getId();
	  		}
		}
	}

	
	protected Properties prop = new Properties();
	
	private String getProperties(String location,String key){
		String result="";
		try {
			InputStream in =BasicController.class.getClassLoader().getResourceAsStream(location);
			prop.load(in);     ///加载属性列表
			result=prop.getProperty(key);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping("/basic/container")
	public ModelAndView  container(HttpServletRequest request){
		getinfos(request);
		String redirectUrl="redirect:http://"+getProperties("config.properties", "PSI_URL")+"/admin/basic/container?web_user_id="+web_user_id;
		return new ModelAndView(redirectUrl);
	}
	
	@RequestMapping("/basic/inStorage")
	public ModelAndView  inStorage(HttpServletRequest request){
		getinfos(request);
		String redirectUrl="redirect:http://"+getProperties("config.properties", "PSI_URL")+"/admin/instorage/main?web_user_id="+web_user_id;
		return new ModelAndView(redirectUrl);
	}
	
	@RequestMapping("/basic/outStorage")
	public ModelAndView  outStorage(HttpServletRequest request){
		getinfos(request);
		String redirectUrl="redirect:http://"+getProperties("config.properties", "PSI_URL")+"/admin/outstorage/main?web_user_id="+web_user_id;
		return new ModelAndView(redirectUrl);
	}
	
	@RequestMapping("/basic/inventory")
	public ModelAndView  inventory(HttpServletRequest request){
		getinfos(request);
		String redirectUrl="redirect:http://"+getProperties("config.properties", "PSI_URL")+"/admin/inventory/main?web_user_id="+web_user_id;
		return new ModelAndView(redirectUrl);
	}
	
	@RequestMapping("/basic/inventoryBill")
	public ModelAndView  inventoryBill(HttpServletRequest request){
		getinfos(request);
		String redirectUrl="redirect:http://"+getProperties("config.properties", "PSI_URL")+"/admin/inventorybill/main?web_user_id="+web_user_id;
		return new ModelAndView(redirectUrl);
	}
	
	@RequestMapping("/basic/order")
	public ModelAndView  order(HttpServletRequest request){
		getinfos(request);
		String redirectUrl="redirect:http://"+getProperties("config.properties", "PSI_URL")+"/admin/order/main?web_user_id="+web_user_id;
		return new ModelAndView(redirectUrl);
	}
	
	@RequestMapping("/chart/main")
	public ModelAndView  chart(HttpServletRequest request){
		getinfos(request);
		String redirectUrl="redirect:http://"+getProperties("config.properties", "PSI_URL")+"/admin/chart/main?web_user_id="+web_user_id;
		return new ModelAndView(redirectUrl);
	}
}
