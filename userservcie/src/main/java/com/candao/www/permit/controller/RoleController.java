package com.candao.www.permit.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.AjaxResponse;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.model.Function;
import com.candao.www.data.model.Role;
import com.candao.www.permit.common.ScopeDict;
import com.candao.www.permit.service.EmployeeUserService;
import com.candao.www.permit.service.FunctionService;
import com.candao.www.permit.service.RoleService;
import com.candao.www.permit.vo.RoleVO;


@Controller("roleController1")
@RequestMapping("/t_role") 
public class RoleController{

	@Autowired
	@Qualifier("roleServiceImpl1")
	private RoleService roleService;

	@Autowired
	private FunctionService functionService;

	@Autowired
	private EmployeeUserService employeeUserService;
	
 
  	//[start] 通用权限操作
  
  	@RequestMapping("/delete")
	@ResponseBody
	public Map<String,Object> roleDelete(HttpServletRequest request ){
		  Map<String,Object> returnMap=new HashMap();
		  boolean isSuccess=true;
		  String message="";
		  String id=request.getParameter("id");
		  Role role=this.roleService.getRole(id);
		  if(StringUtils.isBlank(id)){
			  isSuccess=false;
			  message="无效的权限角色主键！";
		  }else{
			  //根据权限ID，查询当前是否有在使用
			  boolean b=this.roleService.queryWhether2DeletedRole(id);
			  if(!b){
				  isSuccess=false;
				  //message="当前角色正在使用！";
				  message="角色“"+role.getName()+"”正在使用，删除失败!";
			  }else{
				  this.roleService.deleteRole(id ,this.getCurrentBranchId(request));
			  }
		  }
		  
		  returnMap.put("isSuccess", isSuccess);
		  returnMap.put("message", message);
		  
		  return returnMap;
	}
  	
  	@RequestMapping("/get")
	@ResponseBody
	public RoleVO roleGet(HttpServletRequest request ){
  		RoleVO vo = new RoleVO();
		String id=request.getParameter("id");
		if(!StringUtils.isBlank(id)){
			 vo=this.roleService.getRoleVOById(id);
		}
		return vo;
	}
  	//[end] 
  	
  	//[start] 总店权限角色
  	/**
  	 * 显示角色（包含功能编号列表）列表
  	 * @param request
  	 * @return
  	 */
	@RequestMapping("/roleFunctionCodeList4HeadOffice")
	public ModelAndView roleFunctionCodeList4HeadOffice( HttpServletRequest request ){
		ModelAndView mv=new ModelAndView("account/accountRole");
		//List roleList=this.roleService.getRoleList(null);
		List roleList=roleService.getRoleFunctionCodeListByScope(ScopeDict.HEAD_OFFICE);
		mv.addObject("roleList", roleList);
		return mv;
	}
	/**
  	 * 显示角色列表
  	 * @param request
  	 * @return
  	 */
	@RequestMapping("/roleList4HeadOffice")
	public ModelAndView roleList4HeadOffice( HttpServletRequest request ){
		ModelAndView mv=new ModelAndView("account/accountRole");
		//List roleList=this.roleService.getRoleList(null);
		List roleList=roleService.getRoleListByScope(ScopeDict.HEAD_OFFICE);
		mv.addObject("roleList", roleList);
		return mv;
	}
	
	/**
  	 *  根据角色的名字判断角色名称必须唯一
  	 * @param request
  	 * @return
  	 */
	@RequestMapping("/getRoleByName")
	@ResponseBody
	public void getRoleByName(HttpServletRequest req, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		StringBuffer sb = new StringBuffer();
		try {
			String count  = roleService.getRoleByName(params);
			sb.append(count);
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out = response.getWriter();
			out.print(sb);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 编辑/新增 权限角色。
	 * <pre>
	 * 		根据传递过来的参数进行判断是要做何种操作。
	 * 		<code>id</code>
	 * 		如果ID 不为空，则判断：
	 * 			<code>isModify</code> true : 编辑
	 * 							 	  false: 不是编辑，一般认为是要查看详细内容
	 * 		如果ID 为空，则认为是要进行增加操作。
	 * </pre>
	 * @param request
	 * @return
	 */
	@RequestMapping("/roleList4HeadOffice/edit")
	public ModelAndView roleList4HeadOffice_Edit(HttpServletRequest request ){
		
		ModelAndView mv=new ModelAndView();
		mv.setViewName("account/accountRole_edit");
		String id = request.getParameter("id");
		boolean isModify = Boolean.parseBoolean(request.getParameter("isModify"));
		String callBackMethod=request.getParameter("callBackMethod");
		
		if( !StringUtils.isBlank(id)){
			
			RoleVO roleVO= this.roleService.getRoleVOById(id);
			mv.addObject("roleVO", roleVO);
			//设置一个map，放角色选择的模块功能。用于还原选中状态
			Map selectedMap=new HashMap();
			if( null!=roleVO.getFunctions()){
				for(int i=0;i<roleVO.getFunctions().size();i++){
					Function f=roleVO.getFunctions().get(i);
					selectedMap.put(f.getId(), f);
				}
			}
			mv.addObject("selectedMap", selectedMap);
			
		}else{
			
		}

		mv.addObject("isModify",isModify);
		mv.addObject("callBackMethod", callBackMethod);
		//查询 系统功能模块
		List headOfficeFunctions = this.functionService.getTopLevelFunctionsByScopeCode(ScopeDict.HEAD_OFFICE);
		mv.addObject("headOfficeFunctions", headOfficeFunctions);
		
		return mv;
	}
	
	
	@RequestMapping("/roleList4HeadOffice/save")
	@ResponseBody
	public Map<String,Object> roleList4HeadOffice_Save(@RequestBody RoleVO roleVO,  HttpServletRequest request ){
		Map<String , Object> retMap=new HashMap();
		//设置当前保存的为总店范围的权限
		try{
			roleVO.getRole().setScopeCode(ScopeDict.HEAD_OFFICE.getValue());
			this.roleService.saveOrUpdateRoleVO(roleVO , null);
			
			retMap.put("isSuccess", true );
			retMap.put("message", "操作成功");
		}catch(Exception e){
			retMap.put("isSuccess", false );
			retMap.put("message", e.getMessage());
		}
		
		return  retMap;
	}
  
	//[end]
  
  
	//[start] 分店权限操作
	
	/**
	 * 分店列出所有权限的页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/roleList4Store")
	public ModelAndView roleList4Store( HttpServletRequest request ){
		
		
		ModelAndView mv=new ModelAndView("employee/employeeRole");
		//List roleList=this.roleService.getRoleList(null);
		//List roleList=roleService.getRoleListByScope(ScopeDict.ALL_OF_STORE);
		//先判断是否存在默认门店。如果不存在，那么
		List roleList=new ArrayList();
		boolean hasStore=false;
		
		Map m=this.employeeUserService.getBranchById( this.getCurrentBranchId(request) );
		if( null!=m){
			hasStore=true;
			roleList=roleService.getRoleListForCurrentStore( this.getCurrentBranchId(request)  );
		}
		
		mv.addObject("roleList", roleList);
		mv.addObject("hasStore", hasStore);
		return mv;
	}
	
	/**
	 * 编辑/新增 权限角色。
	 * <pre>
	 * 		根据传递过来的参数进行判断是要做何种操作。
	 * 		<code>id</code>
	 * 		如果ID 不为空，则判断：
	 * 			<code>isModify</code> true : 编辑
	 * 							 	  false: 不是编辑，一般认为是要查看详细内容
	 * 		如果ID 为空，则认为是要进行增加操作。
	 * 
	 * 		<code>callBackMethod</code>是可选参数。如果是存在，那么在 取消/保存表单的时候，进行回调的javascript方法
	 * </pre>
	 * @param request
	 * @return
	 */
	@RequestMapping("/roleList4Store/edit")
	public ModelAndView roleList4Store_Edit(HttpServletRequest request ){
		
		ModelAndView mv=new ModelAndView();
		mv.setViewName("employee/employeeRole_edit");
		String id = request.getParameter("id");
		boolean isModify = Boolean.parseBoolean(request.getParameter("isModify"));
		String callBackMethod=request.getParameter("callBackMethod");
		
		//后台登陆编码
		String  EMPLOYEE_BACKGROUND_LOGIN_FUNCTION_CODE=PropertiesUtils.getValue("employee.background_login.function_code");//
		//后台登陆级联的模块编码
		String  EMPLOYEE_BACKGROUND_LOGIN_FUNCTION_CASCADE_CODE=PropertiesUtils.getValue("employee.background_login.function_cascade_code");//
		
		if( !StringUtils.isBlank(id)){
			RoleVO roleVO= this.roleService.getRoleVOById(id);
			mv.addObject("roleVO", roleVO);
			
			//设置一个map，放角色选择的模块功能。用于还原选中状态
			Map selectedMap=new HashMap();
			if( null!=roleVO.getFunctions()){
				for(int i=0;i<roleVO.getFunctions().size();i++){
					Function f=roleVO.getFunctions().get(i);
					selectedMap.put(f.getId(), f);
				}
			}
			mv.addObject("selectedMap", selectedMap);			
		}else{
			
		}
		mv.addObject("isModify",isModify);
		mv.addObject("callBackMethod",callBackMethod);
		//查询 系统功能模块
		List functions = this.functionService.getTopLevelFunctionsByScopeCode(ScopeDict.ALL_OF_STORE);
		mv.addObject("functions", functions);
		mv.addObject("EMPLOYEE_BACKGROUND_LOGIN_FUNCTION_CODE", EMPLOYEE_BACKGROUND_LOGIN_FUNCTION_CODE);
		mv.addObject("EMPLOYEE_BACKGROUND_LOGIN_FUNCTION_CASCADE_CODE",EMPLOYEE_BACKGROUND_LOGIN_FUNCTION_CASCADE_CODE);
		return mv;
	}
	
	
	/*
	 * 
	 * 此处获取的参数 id ,可能为一个主键，也可能是多个主键用逗号分隔。
	 */
	@RequestMapping("/roleList4Store/detail")
	public ModelAndView roleList4Store_detail(HttpServletRequest request ){
		
		ModelAndView mv=new ModelAndView();
		mv.setViewName("employee/employeeRole_detail");
		String id = request.getParameter("id");
		
		if( !StringUtils.isBlank(id)){
			
			String[] ids= id.split(",");
			//设置一个map，放角色选择的模块功能。用于还原选中状态
			Map selectedMap=new HashMap();
			List<RoleVO> roleVOList=new ArrayList();
			for( String t_id : ids){
				RoleVO roleVO= this.roleService.getRoleVOById(t_id);
				//mv.addObject("roleVO", roleVO);
				roleVOList.add(roleVO);
				if( null!=roleVO.getFunctions()){
					for(int i=0;i<roleVO.getFunctions().size();i++){
						Function f=roleVO.getFunctions().get(i);
						selectedMap.put(f.getId(), f);
					}
				}
			
			}
			
			mv.addObject("selectedMap", selectedMap);
			mv.addObject("roleVOList", roleVOList);
		}
		//查询 系统功能模块
		List functions = null;
		if(request.getParameter("scope")!=null&&!"".equals(request.getParameter("scope"))){
			String scope = String.valueOf(request.getParameter("scope"));
			if("01".equals(scope)){
				functions = this.functionService.getTopLevelFunctionsByScopeCode(ScopeDict.ALL);
			}else if("02".equals(scope)){
				functions = this.functionService.getTopLevelFunctionsByScopeCode(ScopeDict.HEAD_OFFICE);
			}else if("03".equals(scope)){
				functions = this.functionService.getTopLevelFunctionsByScopeCode(ScopeDict.ALL_OF_STORE);
			}else if("04".equals(scope)){
				functions = this.functionService.getTopLevelFunctionsByScopeCode(ScopeDict.APPOINTED_STORE);
			}else{
				functions = this.functionService.getTopLevelFunctionsByScopeCode(ScopeDict.ALL_OF_STORE);
			}
			
		}else{
			functions = this.functionService.getTopLevelFunctionsByScopeCode(ScopeDict.ALL_OF_STORE);
		}
		mv.addObject("functions", functions);
		
		return mv;
	}
	
	
	@RequestMapping("/roleList4Store/save")
	@ResponseBody
	public Map<String,Object> roleList4Store_Save(@RequestBody RoleVO roleVO,  HttpServletRequest request ){
		Map<String , Object> retMap=new HashMap();
		//设置当前保存的为总店范围的权限
		try{
			roleVO.getRole().setScopeCode(ScopeDict.ALL_OF_STORE.getValue());
			this.roleService.saveOrUpdateRoleVO(roleVO ,this.getCurrentBranchId(request) );
			
			retMap.put("isSuccess", true );
			retMap.put("message", "操作成功");
		}catch(Exception e){
			retMap.put("isSuccess", false );
			retMap.put("message", "操作失败:"+e.getMessage());
			e.printStackTrace();
		}
		
		return  retMap;
	}
	
	@RequestMapping("/roleList4Store/roleNameCheck")
	@ResponseBody
	public String roleNameCheck(String roleName ,String roleId, HttpServletRequest request){
		AjaxResponse ar =new AjaxResponse();
		String branch_id=this.getCurrentBranchId(request);
		boolean isExist=false;
		Map<String ,Object> dataMap=new HashMap();
		try{
			isExist=this.roleService.checkRoleName4Store(roleName, roleId,branch_id);
			dataMap.put("isExist", isExist);
			ar.setIsSuccess(true);
			ar.setData(dataMap);
		}catch(Exception e){
			ar.setIsSuccess(false);
			ar.setErrorMsg("请求过程中，后台发生错误!");
			e.printStackTrace();
		}
		
		return JacksonJsonMapper.objectToJson(ar);
	}
	
	//[end]
  
  
  /**
   * 递归处理子功能
   * @param function
   * @param permittedFuns
   * @return
   */
  private List<Map> recursiveChildRoleFun(Function function, Map permittedFuns){
    List<Function> childFuns = function.getChildFuns();
    if(childFuns == null){
      return null;
    }
    List list = new ArrayList();
    Function fn = null;
    for(int i = 0; i < childFuns.size(); i++){
      Map fun = new HashMap();
      fn = childFuns.get(i);
      fun.put("id", fn.getId());
      fun.put("name", fn.getName());
      fun.put("level", fn.getLevel());
      if(permittedFuns.get(fn.getId()) != null){
        fun.put("checked", true);
      } else {
        fun.put("checked", false);
      }
      List<Map> childs = recursiveChildRoleFun(fn, permittedFuns);
      if(childs != null){
        fun.put("child", childs);
      }
      list.add(fun);
    }
    return list;
  }
  
  /**
   * <pre>
   * 检查一下员工所选择的角色，是否需要根据角色创建账号：
   * 因为 情况1和情况2 都是根据权限对行号进行部分操作，所以两种情况是并行的。
   * 情况3包含在情况1中。
   * --------------
   * 
说明
1、当员工授权角色的权限包含以下任意一种时，需要为对应员工创建后台账号并发送到员工手机号上。账号名称需用户输入，密码后台自动生成
权限：后台管理-登录、退菜、登录收银机、收银、开业、结业、清机、反结算
2、当员工授权角色的权限不包含以下任意一种时，系统会为对应员工自动生成一套后台账号和密码，该账号密码不可见。
权限：后台管理-登录、退菜、登录收银机、收银、开业、结业、清机、反结算
（后台账号为16位字符串）
3、当员工授权角色的权限包含以下任意一种时，需要为对应员工创建以”工号“为账号，初始密码为123456的账户信息，此密码为职位权限密码，员工可以在“我的账户”页面对其进行修改。
权限：退菜、登录收银机、收银、开业、结业、清机、反结算

第三种情况为，可以在pad 、pos用工号登陆。密码为  employee中得支付密码
   * --------------
   * </pre>
   * @param request
   * @return
   */
  @RequestMapping("/roleList4Store/checkEmployeeRole")
  @ResponseBody
  public String checkEmployeeRole(HttpServletRequest request){
	  AjaxResponse ar=new AjaxResponse();
	  String ids= request.getParameter("ids");
	  String SPLIT_CHAR=",";
	  if( StringUtils.isBlank(ids)){
		  ar.setIsSuccess(false);
		  ar.setErrorMsg("检查的权限ID不合法!");
	  }else{
		  try{
			  Map typeMap=new HashMap();
			  //情况1：如果有其中任意权限，需要创建账号
			  String  str_a = PropertiesUtils.getValue("employee.function.need_account");
			  
			  String[] id_array= ids.split(",");
			  //设置一个map，放角色选择的模块功能
			  Map selectedMap=new HashMap();
			  List<RoleVO> roleVOList=new ArrayList();
			  for( String t_id : id_array){
					RoleVO roleVO= this.roleService.getRoleVOById(t_id);
					roleVOList.add(roleVO);
					if( null!=roleVO.getFunctions()){
						for(int i=0;i<roleVO.getFunctions().size();i++){
							Function f=roleVO.getFunctions().get(i);
							selectedMap.put(f.getCode(), f);
						}
					}
				
			 }
			 
			 //遍历 判断第一种情况。
			  int count_have=0;
			  String[] functions=null; //这里存放是 function的code
			  functions=str_a.split( SPLIT_CHAR );
			  if( null != functions || functions.length > 0 ){
				  for(String f:functions){
					  if( null!= selectedMap.get( f ) ){
						  //能在选中的权限中 取到指定的编码，符合条件
						  count_have=count_have+1;
					  }
				  }
			  }
			  
			  //如果满足第一种情况，则返回，不继续判断第二种情况，否则判断第二中情况。
			  if( count_have >0){
				  typeMap.put("needAccount", true);
			  }else{ //第二种情况。
				  typeMap.put("needAccount", false);
			  }
			  
			 ar.setIsSuccess(true);
			 ar.setData(typeMap);
		  }catch(Exception e){
			  ar.setIsSuccess(false);
			  ar.setErrorMsg("权限检查过程中发生错误..");
		  }
	  }
	  
	  return JacksonJsonMapper.objectToJson(ar);
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

	
}
