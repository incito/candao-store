package com.candao.www.webroom.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.candao.www.dataserver.service.member.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.data.model.TOrderMember;
import com.candao.www.data.model.Torder;
import com.candao.www.dataserver.service.order.OrderOpService;
import com.candao.www.security.controller.BaseController;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.service.OrderService;
import com.candao.www.webroom.service.impl.CallWaiterServiceImpl;
import com.candao.www.webroom.service.OrderMemberService;

@Controller
@RequestMapping("/member")
public class MemberController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private OrderService orderService ;
	
	@Autowired
	private OrderMemberService orderMemberService ;

    @Autowired
    private OrderOpService orderOpService;
	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/MemberLogin")
	@ResponseBody
	public String memberLogin(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonString){
		
		Map<String, Object> resultmap = new HashMap<String , Object>();
		
		try{
			HashMap<String , String> paramsJson = JacksonJsonMapper.jsonToObject(jsonString, HashMap.class);
			String orderid = paramsJson.get("orderid");
			String mobile = paramsJson.get("mobile");
			
			HashMap<String , Object> params =  new HashMap<String , Object>();
			params.put("orderid", orderid);
			params.put("pricetype", 0);  //设置会员价
			memberService.setMemberPrice("",orderid,mobile);

	        //重新计算应收金额
	        orderOpService.calcOrderAmount(orderid);
			
			resultmap = ReturnMap.getSuccessMap("会员登录成功");
		}catch(Exception e){
			logger.error("-->",e);
			resultmap = ReturnMap.getFailureMap("登录失败，服务器异常，请联系管理员");
		}
		return JacksonJsonMapper.objectToJson(resultmap);
		
	}
	
	
	@RequestMapping("/MemberLogout")
	@ResponseBody
	public String memberLogout(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonString){
		
		Map<String, Object> resultmap = new HashMap<String , Object>();
		
		try{
			HashMap<String , String> paramsJson = JacksonJsonMapper.jsonToObject(jsonString, HashMap.class);
			String orderid = paramsJson.get("orderid");

		 //还原会员价
			memberService.revertMemberPrice2("userId",orderid,"");
	        //重新计算应收金额
	        orderOpService.calcOrderAmount(orderid);
	        
			resultmap = ReturnMap.getSuccessMap("会员退出成功");
		}catch(Exception e){
			logger.error("-->",e);
			resultmap = ReturnMap.getFailureMap("操作失败，服务器异常，请联系管理员");
		}
		return JacksonJsonMapper.objectToJson(resultmap);
	}
	
	
	@RequestMapping("/AddOrderMember")
	@ResponseBody
	public String addOrderMember(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonString){
		
		Map<String, Object> resultmap = new HashMap<String , Object>();
		try{
			
			TOrderMember tOrderMember = JacksonJsonMapper.jsonToObject(jsonString, TOrderMember.class);
			
			TOrderMember member = orderMemberService.get(tOrderMember.getOrderid(), null);
			if(member != null){
				orderMemberService.update(tOrderMember);
			}else{
				orderMemberService.saveOrderMember(tOrderMember);
			}

            // 计算菜品实收
            orderSettleService.calDebitAmount(orderid);
			
			resultmap.put("Retcode", "0");
			resultmap.put("RetInfo", "会员消费分店保存成功");
			
		}catch(Exception e){
			logger.error("-->",e);
			resultmap.put("Retcode", "1");
			resultmap.put("RetInfo", e.getMessage());
		}
		
		return JacksonJsonMapper.objectToJson(resultmap);
	}
	
	@RequestMapping("/GetOrderMember")
	@ResponseBody
	public String getOrderMember(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonString){
		
		Map<String, Object> resultmap = new HashMap<String , Object>() ;
		
		HashMap<String , String> paramsJson = JacksonJsonMapper.jsonToObject(jsonString, HashMap.class);
		String orderid = paramsJson.get("orderid");
		
		TOrderMember tOrderMember = orderMemberService.get(orderid, 0);
		
		if( tOrderMember != null) {
			resultmap.put("orderid", tOrderMember.getOrderid());
			resultmap.put("cardno", tOrderMember.getCardno());
			resultmap.put("userid", tOrderMember.getUserid());
			resultmap.put("business", tOrderMember.getBusiness());
			
			resultmap.put("terminal", tOrderMember.getTerminal());
			resultmap.put("serial", tOrderMember.getSerial());
			resultmap.put("businessname", tOrderMember.getBusinessname());
			
			resultmap.put("score", tOrderMember.getScore());
			resultmap.put("coupons", tOrderMember.getCoupons());
			resultmap.put("stored", tOrderMember.getStored());
			resultmap.put("scorebalance", tOrderMember.getScorebalance());
			resultmap.put("couponsbalance", tOrderMember.getCouponsbalance());
			
			resultmap.put("storedbalance", tOrderMember.getStoredbalance());
			resultmap.put("psexpansivity", tOrderMember.getPsexpansivity());
			resultmap.put("netvalue", tOrderMember.getNetvalue());
			resultmap.put("Inflated", tOrderMember.getInflated());
			
			resultmap.put("Retcode", "1");
			resultmap.put("RetInfo", "会员消费获取成功");
		}else{
			resultmap.put("Retcode", "0");
			resultmap.put("RetInfo", "会员消费获取失败");
		}
		
		return JacksonJsonMapper.objectToJson(resultmap);
	}
	
	@RequestMapping("/DeleteOrderMember")
	@ResponseBody
	public String DeleteOrderMember(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonString){
		
		Map<String, Object> resultmap = new HashMap<String , Object>();
		
		try{
			HashMap<String , String> paramsJson = JacksonJsonMapper.jsonToObject(jsonString, HashMap.class);
			String orderid = paramsJson.get("orderid");
			orderMemberService.updateValid(orderid);
			
			resultmap.put("Retcode", "1");
			resultmap.put("RetInfo", "会员消费分店保存成功");
			
		}catch(Exception e){
			logger.error("-->",e);
			resultmap.put("Retcode", "0");
			resultmap.put("RetInfo", e.getMessage());
			
		}
		
		return JacksonJsonMapper.objectToJson(resultmap);
	}
	

}



