package com.candao.www.permit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.model.HeadquarterUser;
import com.candao.www.data.model.User;
import com.candao.www.data.model.UserBranch;
import com.candao.www.data.model.UserRole;
import com.candao.www.permit.service.RoleService;
import com.candao.www.permit.service.UserService;
import com.candao.www.utils.SessionUtils;

/**
 * 
 * 总店用户-控制类
 * @author lishoukun
 * @date 2015/04/21
 */
@Controller("headquarterUserController1")
@RequestMapping("/t_headuser/")
public class HeadquarterUserController{

	@Autowired
	@Qualifier("headquarterUserServiceImpl")
	private UserService userService;

	@Autowired
	@Qualifier("roleServiceImpl1")
	private RoleService roleService;
	
	/**
	 * 跳转至总店用户管理页面
	 * @return model and view
	 */
	@RequestMapping("/account")
	public ModelAndView account(){
		ModelAndView mav = new ModelAndView("account/account");
		mav.addObject("shop_admin_function_code", PropertiesUtils.getValue("shop_admin_function_code"));
		return mav;
	}
	/**
	 * 查询总店用户列表，根据条件
	 * @param reqMap 请求参数
	 * @return json array string
	 */
	@RequestMapping("/queryUserList")
	@ResponseBody
	public String queryUserList(@RequestParam Map<String, Object> reqMap){
	  List<User> userList = userService.queryUserList(reqMap);
	  
	  Map<String,Object> resultMap = new HashMap<String,Object>();
	  resultMap.put("data", userList);
	  return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	  * 查询总店用户分页数据，根据条件和分页参数
	  * @param reqMap 请求参数
	  * @param page 页数
	  * @param rows 每页条数
	  * @return json object string
	  */
	 @RequestMapping("/queryUserPage")
	 @ResponseBody
	 public String queryUserPage(@RequestParam Map<String, Object> reqMap,int page, int rows){
		Page<Map<String, Object>> pageMap = userService.queryUserPage(reqMap, page, rows);
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("data", pageMap);
		return JacksonJsonMapper.objectToJson(resultMap);
	 }
	/**
	 * 获取一条总店用户,根据主键
	 * @param id 主键
	 * @return json obj string
	 */
	@RequestMapping("/getUserById/{id}")
	@ResponseBody
	public String getUserById(@PathVariable(value = "id") String id){
		HeadquarterUser user = (HeadquarterUser)userService.getUserById(id);
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("data", user);
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 保存总店用户
	 * @param req
	 * @param resp
	 * @return json obj string
	 */
	@RequestMapping("/saveUser")
	@ResponseBody
	public String saveUser(HeadquarterUser user,String rolesJson,String branchsJson){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			List<UserRole> roles = new ArrayList<UserRole>();
			List<UserBranch> branchs = new ArrayList<UserBranch>();
			//处理角色关联和门店关联数据
			if(rolesJson!=null&&!rolesJson.equals("")){
				roles = JacksonJsonMapper.jsonToList(rolesJson, UserRole.class);
			}
			if(branchsJson!=null&&!branchsJson.equals("")){
				branchs = JacksonJsonMapper.jsonToList(branchsJson, UserBranch.class);
			}
			user.setRoles(roles);
			user.setBranchs(branchs);
			//判断是否存在id,如果存在id则为更新操作，如不存在id，则为添加操作。
			User resultUser = null;
			if("".equals(user.getId())||user.getId()==null){
				User usercurrent = SessionUtils.getCurrentUser();
				user.setTenantid(usercurrent.getTenantid());
				resultUser = userService.addUser(user);
			}else{
				resultUser = userService.updateUser(user);
			}
			if(resultUser!=null){
				resultMap.put("success",true);
				resultMap.put("data",resultUser);
			}else{
				resultMap.put("success",false);
			}
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg",e.getMessage());
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 删除总店用户,根据主键
	 * @param id  主键
	 * @return json obj string
	 */
	@RequestMapping("/deleteUserById/{id}")
	@ResponseBody
	public String deleteUserById(@PathVariable(value = "id") String id){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			Integer count = userService.deleteUserById(id);
			resultMap.put("success",true);
			resultMap.put("msg",count);
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg",e.getMessage());
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 批量删除基本用户,根据主键集合
	 * @param ids 主键集合
	 * @param resp
	 * @return json obj string
	 */
  @RequestMapping("/deleteUsersByIds/{ids}")
  @ResponseBody
  public String deleteUsersByIds(@PathVariable(value = "ids") String ids){
	  Map<String,Object> resultMap = new HashMap<String,Object>();
	  try{
		  Integer count = userService.deleteUsersByIds(ids);
		  resultMap.put("success",true);
		  resultMap.put("msg",count);
	  }catch(Exception e){
		  resultMap.put("success",false);
		  resultMap.put("msg",e.getMessage());
	  }
	  return JacksonJsonMapper.objectToJson(resultMap);
  }
  
  /**
   * 删除基本用户,根据条件
   * @param req
   * @param resp
   * @return json obj string
   */
  @RequestMapping("/deleteUser")
  @ResponseBody
  public String deleteUser(@RequestParam Map<String, Object> reqMap){
	  Map<String,Object> resultMap = new HashMap<String,Object>();
	  try{
		  	Integer count = userService.deleteUser(reqMap);
		  	resultMap.put("success",true);
		  	resultMap.put("msg",count);
	  }catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg",e.getMessage());
	  }
	  return JacksonJsonMapper.objectToJson(resultMap);
  }
}
