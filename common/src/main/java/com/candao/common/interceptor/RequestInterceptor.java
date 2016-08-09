package com.candao.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class RequestInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
	System.out.println(arg2.getClass().getSimpleName());
//	Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
//	String id=String.valueOf(pathVariables.get("id"));
//	TemplateService  templateServiceImpl=SpringContext.getApplicationContext().getBean(TemplateServiceImpl.class);
//	TbTemplate tbTemplate=templateServiceImpl.findById(id);
//	List<String> allowAccessButtons = (List<String>) SessionUtils.get(Constant.ALLOW_ACCESS_BUTTONS);
//	if(allowAccessButtons.contains("/template/goback")||allowAccessButtons.contains("/template/approve")){//审核者
//		return true;
//	}else{
//		if(tbTemplate.getStatus()>2){
//			response.setContentType("text/html; charset=utf-8"); // 设置编码格式要在创建PrintWriter对象之前.不然不能生效
//			PrintWriter pw = response.getWriter();
//			pw.write("<script language='javascript'>alert('已经审核,无法删除')</script>");
//			pw.flush();
//			pw.close();
//			return false;
//		}
//	}
		return true;
	}

}
