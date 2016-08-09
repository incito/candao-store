package com.candao.www.permit.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.page.PageContainer;
import com.candao.common.utils.AjaxResponse;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.model.EmployeeUser;
import com.candao.www.data.model.User;
import com.candao.www.permit.common.BusinessException;
import com.candao.www.permit.common.ScopeDict;
import com.candao.www.permit.service.EmployeeUserService;
import com.candao.www.permit.service.RoleService;
import com.candao.www.permit.service.UserService;
import com.candao.www.permit.vo.RoleVO;

/**
 * 员工管理类
 * @author YHL
 *
 */
@Controller
@RequestMapping("/t_employeeUser/")
public class EmployeeUserController {

	@Autowired
	@Qualifier("t_userService")
	private UserService userService;
	
	@Autowired
	private EmployeeUserService employeeUserService;

	@Autowired
	private RoleService roleService;

	
	@RequestMapping("")
	public ModelAndView index(HttpServletRequest request ){
		return this.userList(request);
	}
	
	
	/**
	 * 显示门店用户的列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView userList(HttpServletRequest request ){
		
		/**
		 *  获取配置文件config.properties中得配置参数。
		 * employee.function.need_account 表示 门店员工管理 如果包含了这些模块中得任意一个，则可以发送账号 
		 */
		
		String  EMPLOYEE_FUNCTION_NEED_ACCOUNT=PropertiesUtils.getValue("employee.function.need_account");//
		String[]  codes=EMPLOYEE_FUNCTION_NEED_ACCOUNT.split(",");
		
		ModelAndView mav = new ModelAndView("employee/employees");
		int current=1;
		int pagesize=10;
		Page page=null;
		 
		boolean hasStore=false; 
		Map m=this.employeeUserService.getBranchById( this.getCurrentBranchId(request) );
		if( null!=m){
			hasStore=true; 
			try{
				pagesize= Integer.parseInt( request.getParameter("pagesize") );
			}catch(Exception e){
				
			}
			
			try{
				current= Integer.parseInt( request.getParameter("current") );
			}catch(Exception e){
				
			}
			Map<String , Object > params = new HashMap();
			params.put("branch_id", this.getCurrentBranchId(request) );
			//判断是否有查询
			if(request.getParameter("searchText")!=null){
				String serachText = String.valueOf(request.getParameter("searchText"));
				params.put("searchText", serachText);
			}
			page=this.employeeUserService.page(params, current, pagesize);
			
		}else{
			page=new PageContainer(0, 10, 1, new ArrayList(), new HashMap());
		}
		
		Map<String , EmployeeUser> needSendAccountEmployeesMap=new HashMap();
		
		Collection<EmployeeUser> users=page.getRows();
		Iterator<EmployeeUser> iterator=users.iterator();
		List l=new ArrayList();
		while (iterator.hasNext()) {
            EmployeeUser element = (EmployeeUser) iterator.next();
            
            boolean willSend=false;
            for( String s:codes){
            	if( null!=element.getFunctions().get(s)){
            		willSend=willSend||true;
            	}
            }
            
            if(willSend){
            	needSendAccountEmployeesMap.put(element.getId(), element);
            }
		}
		
		mav.addObject("hasStore", hasStore);
		mav.addObject("page", page);
		mav.addObject("needSendAccountEmployeesMap", needSendAccountEmployeesMap);
		//mav.addObject("EMPLOYEE_BACKGROUND_LOGIN_FUNCTION_CODE", EMPLOYEE_BACKGROUND_LOGIN_FUNCTION_CODE);
		return mav;
	}
	
	/**
	 * 保存门店员工用户
	 * @param employee
	 * @param request
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Map<String,Object> save(@RequestBody EmployeeUser employee,  HttpServletRequest request ){
		Map<String , Object> retMap=new HashMap();
		//设置当前保存的为总店范围的权限
		try{
			if( StringUtils.isBlank( employee.getId() )){
				//获取当前的门店。（因为是云端操作，所以需要获取当前登录后的门店）
				//TODO 目前写在这里，后期需要进行修改
				Map<String , Object > storeMap= this.employeeUserService.getBranchById( this.getCurrentBranchId(request) );
				String branchId = String.valueOf(storeMap.get("branchid"));
				String branchName = String.valueOf(storeMap.get("branchname"));
				
				employee.setBranchId( Integer.parseInt( branchId ));
				employee.setBranchName(branchName);
				
				this.employeeUserService.save(employee);
			}else{
				this.employeeUserService.update(employee);
			}
			retMap.put("isSuccess", true );
			retMap.put("message", "操作成功");
		}catch(Exception e){
			retMap.put("isSuccess", false );
			retMap.put("message", "操作失败");
			e.printStackTrace();
		}
		
		return  retMap;
	}
	
	/**
	 * 修改用户状态（启用/禁用）
	 * @param userId
	 * @param status
	 * @return
	 */
	@RequestMapping("/changeUserStatus")
	@ResponseBody
	public Map<String,Object> changeUserStatus(@RequestParam("id") String userId,@RequestParam("status") boolean status ){
		Map<String , Object> retMap=new HashMap();
		boolean isSuccess=true;
		String message="";
		
		try{
			int res=this.employeeUserService.updateUserStatus(userId, status);
			
			if(res>0){
				isSuccess=true;
			}else{
				isSuccess=false;
				message="修改状态失败！";
			}
		}catch(Exception e){
			isSuccess=false;
			message="数据保存过程中发生错误！";
			e.printStackTrace();
		}
		
		retMap.put("isSuccess", isSuccess);
		retMap.put("message", message);
		
		return retMap;
	}
	
	
	/**
	 * 获取一个员工对象的json数据
	 * @param employee
	 * @param request
	 * @return
	 */
	@RequestMapping("/get")
	@ResponseBody
	public EmployeeUser getEmployeeUser(@RequestParam("id") String id){
		EmployeeUser obj=new EmployeeUser();
		
		obj=this.employeeUserService.get(id);
		
		return  obj;
	}
	
	/**
	  * 获取一条企业用户,根据用户id
	  * 
	  * @param id 用户id
	  * @return json obj string
	  */
	 @RequestMapping("/getUserById/{id}")
	 @ResponseBody
	 public String getUserById(@PathVariable(value = "id") String id){
		 Map<String,Object> resultMap = new HashMap<String,Object>();
		 try {
	    	EmployeeUser user = employeeUserService.getUserById(id);
	    	resultMap.put("success", true);
	    	resultMap.put("data", user);
		 } catch (BusinessException be) {
	    	resultMap.put("success", false);
	        resultMap.put("msg", be.getMessage());
	      // be.printStackTrace();
		 }
		 return JacksonJsonMapper.objectToJson(resultMap);
	  }
	
	 /**
	  * 检查员工工号是否重复。
	  * isSuccess = true 则不重复
	  * @param jobNumber 工号
	  * @param uid     用户的id    
	  * @return
	  */
	 @RequestMapping("/checkEmployeeJobNumber")
	 @ResponseBody
	public String checkEmployeeJobNumber(String jobNumber,String uid ,HttpServletRequest request){
		AjaxResponse ar=new AjaxResponse();
		try{
			ar.setIsSuccess(true);
			Map dataMap=new HashMap();
			String branch_id = this.getCurrentBranchId(request);
			boolean isExist=this.employeeUserService.isExistJobNumberExcludeUser(jobNumber, uid, branch_id);
			dataMap.put("isExist", isExist);
			ar.setData(dataMap);
		}catch(Exception e){
			e.printStackTrace();
			ar.setIsSuccess(false);
			ar.setErrorMsg("验证工号过程中后台发生错误!");
		}
		return JacksonJsonMapper.objectToJson(ar);
	}
	 
	@RequestMapping("/checkEmployeeBackAccount")
	@ResponseBody
	public String checkEmployeeBackAccount(String backAccount,String uid ,HttpServletRequest request){
		AjaxResponse ar=new AjaxResponse();
		try{
			ar.setIsSuccess(true);
			Map dataMap=new HashMap();
			String branch_id = this.getCurrentBranchId(request);
			boolean isExist=this.employeeUserService.isExistBackAccountExcludeUser(backAccount, uid, branch_id);
			dataMap.put("isExist", isExist);
			ar.setData(dataMap);
		}catch(Exception e){
			e.printStackTrace();
			ar.setIsSuccess(false);
			ar.setErrorMsg("验证员工后台账号过程中后台发生错误!");
		}
		return JacksonJsonMapper.objectToJson(ar);
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String,Object> deleteEmployeeUser(@RequestParam("id") String id ){
		Map<String , Object> retMap=new HashMap();
		boolean isSuccess=true;
		String message="";
		
		try{
			int res=this.employeeUserService.delete(id);
			
			if(res>0){
				isSuccess=true;
			}else{
				isSuccess=false;
				message="修改状态失败！";
			}
		}catch(Exception e){
			isSuccess=false;
			message="数据保存过程中发生错误！";
			e.printStackTrace();
		}
		
		retMap.put("isSuccess", isSuccess);
		retMap.put("message", message);
		
		return retMap;
	}
	
	//临时方法，返回临时 门店ID，要在后续修改为 动态获取当前门店 
	private String getCurrentBranchId(HttpServletRequest request){
		String _branchid = request.getParameter("branchid");
		if( !StringUtils.isBlank(_branchid) ){
			request.getSession(true).setAttribute("_BRANCH_ID", _branchid);
		}
//		if( StringUtils.isBlank( (String) request.getSession().getAttribute("_BRANCH_ID") ) ){
//			String branchid = request.getParameter("branchid");
//			request.getSession(true).setAttribute("_BRANCH_ID", branchid);
//		}
		return (String) request.getSession().getAttribute("_BRANCH_ID");
	}
	
	/**
	 * 修改职务权限密码
	 * @param req
	 * @param resp
	 * @return json obj string
	 */
	@RequestMapping("/updatePaymentPassword")
	@ResponseBody
	public String updatePaymentPassword(@RequestParam String id,@RequestParam String oldPwd,@RequestParam String newPwd){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			boolean result = false;
			if(oldPwd!=null&&!"".equals(oldPwd)&&newPwd!=null&&!"".equals(newPwd)){
				result = employeeUserService.updatePaymentPassword(id, oldPwd, newPwd);
			}
			if(result){
				resultMap.put("success",true);
			}else{
				resultMap.put("success",false);
			}
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg",e.getMessage());
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	
}
