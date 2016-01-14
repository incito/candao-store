package com.candao.www.permit.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.AjaxResponse;
import com.candao.common.utils.Constant;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.common.utils.ValidateCode;
import com.candao.www.data.model.User;
import com.candao.www.permit.service.RoleService;
import com.candao.www.permit.service.SendService;
import com.candao.www.permit.service.UserService;
import com.candao.www.utils.SessionUtils;

/**
 * 
 * 基本用户-控制类
 * @author lishoukun
 * @date 2015/04/21
 */
@Controller("userController1")
@RequestMapping("/t_user/")
public class UserController{

	@Autowired
	@Qualifier("t_userService")
	private UserService userService;

	@Autowired
	@Qualifier("roleServiceImpl1")
	private RoleService roleService;
	
	@Autowired
	private SendService sendService;
	
	/**
	 * 跳转至我的账户页面
	 * @return model and view
	 */
	@RequestMapping("/myAccount")
	public ModelAndView myAccount(HttpServletRequest request,@RequestParam Map<String, Object> reqMap){
		ModelAndView mav = new ModelAndView("employee/myAccount");
		// 当前已经登录，进入初始页面
		if(request.getSession().getAttribute(Constant.CURRENT_USER)!=null){
	  		User user = (User)request.getSession().getAttribute(Constant.CURRENT_USER);
	  		mav.addObject("id",user.getId());
	  		mav.addObject("type", user.getUserType());
	  	}
		mav.addAllObjects(reqMap);
		return mav;
	}
	/**
	 * 跳转至找回密码页面
	 * @return model and view
	 */
	@RequestMapping("/retrievePwd")
	public ModelAndView retrievePwd(){
		ModelAndView mav = new ModelAndView("rePwd");
		return mav;
	}
	/**
	 * 验证密码
	 * @param req
	 * @param resp
	 * @return json obj string
	 */
	@RequestMapping("/validatePassword")
	@ResponseBody
	public String validatePassword(@RequestParam String id,@RequestParam String password){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			User user = userService.validatePassword(id, password);
			if(user!=null){
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
	
	/**
	 * 验证是否存在邮箱。
	 * 
	 * @param userId 用户id，如果编辑用户信息的时候，需要判断验证的邮箱是否是当前用户的。否则无法进行编辑。如果默认为空，则认为是新增的时候校验
	 * @param email
	 * @return
	 */
	@RequestMapping("/validateEmail")
	@ResponseBody
	public String validateEmail(@RequestParam String uid,@RequestParam String email){
		AjaxResponse ar =new AjaxResponse();
		try {
			Map<String,Object> dataMap=new HashMap();
			List<User> userList=this.userService.queryUserList( new HashMap() ); //默认不传任何参数，则获取所有的用户对象
			boolean isExist=false;
			for(User u:userList){
				if( !StringUtils.isBlank(uid) ){
					if( !StringUtils.isBlank(u.getEmail() ) && u.getEmail().equalsIgnoreCase(email) && !u.getId().equals(uid)){
						isExist=true;
						break;
					}
				}else{
					if( !StringUtils.isBlank(u.getEmail() ) &&  u.getEmail().equalsIgnoreCase(email)  ){
						isExist=true;
						break;
					}
				}
			}
			ar.setIsSuccess(true);
			dataMap.put("isExist", isExist);
			ar.setData(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			ar.setIsSuccess(false);
			ar.setErrorMsg("验证邮箱请求失败！");
			ar.setData(e);
		}
		return JacksonJsonMapper.objectToJson(ar);
	}
	
	
	/**
	 * 验证是否存在手机号。
	 * 
	 * @param userId 用户id，如果编辑用户信息的时候，需要判断验证的手机号是否是当前用户的。否则无法进行编辑。如果默认为空，则认为是新增的时候校验
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/validateMobile")
	@ResponseBody
	public String validateMobile(@RequestParam String uid,@RequestParam String mobile){
		AjaxResponse ar =new AjaxResponse();
		try {
			Map<String,Object> dataMap=new HashMap();
			List<User> userList=this.userService.queryUserList( new HashMap() ); //默认不传任何参数，则获取所有的用户对象
			boolean isExist=false;
			for(User u:userList){
				if( !StringUtils.isBlank(uid) ){
					if( !StringUtils.isBlank(u.getMobile() ) && u.getMobile().equalsIgnoreCase(mobile) && !u.getId().equals(uid)){
						isExist=true;
						break;
					}
				}else{
					if(  !StringUtils.isBlank(u.getMobile() ) && u.getMobile().equalsIgnoreCase(mobile)  ){
						isExist=true;
						break;
					}
				}
			}
			ar.setIsSuccess(true);
			dataMap.put("isExist", isExist);
			ar.setData(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			ar.setIsSuccess(false);
			ar.setErrorMsg("验证手机号请求失败！");
			ar.setData(e);
		}
		
		return JacksonJsonMapper.objectToJson(ar);
	}
	
	/**
	 * 查询基本用户列表，根据条件
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
	  * 查询基本用户分页数据，根据条件和分页参数
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
	 * 获取一条基本用户,根据主键
	 * @param id 主键
	 * @return json obj string
	 */
	@RequestMapping("/getUserById/{id}")
	@ResponseBody
	public String getUserById(@PathVariable(value = "id") String id){
	  User user = userService.getUserById(id);
	  
	  Map<String,Object> resultMap = new HashMap<String,Object>();
	  resultMap.put("data", user);
	  return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
   * 获取一条基本用户,根据条件
   * @param reqMap 请求参数
   * @return json obj string
   */
  @RequestMapping("/getUser")
  @ResponseBody
  public String getUser(@RequestParam Map<String, Object> reqMap){
    User user = userService.getUser(reqMap);
    
    Map<String,Object> resultMap = new HashMap<String,Object>();
	resultMap.put("data", user);
	return JacksonJsonMapper.objectToJson(resultMap);
  }
	/**
	 * 保存基本用户
	 * @param req
   * @param resp
   * @return json obj string
   */
	@RequestMapping("/saveUser")
	@ResponseBody
	public String saveUser(User user){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			User resultUser = null;
			//判断是否存在id,如果存在id则为更新操作，如不存在id，则为添加操作。
			if("".equals(user.getId())||user.getId()==null){
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
	 * 修改用户状态
	 * @param req
	 * @param resp
	 * @return json obj string
   */
	@RequestMapping("/updateStatus")
	@ResponseBody
	public String updateStatus(@RequestParam String id,@RequestParam String status){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			boolean result = false;
			result = userService.updateStatus(id, status);
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
	/**
	 * 删除基本用户,根据主键
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
  	/**
	 * 计算账户安全分数
	 */
	@RequestMapping(value = "/computeSafeScore/{id}")
	@ResponseBody
	public String computeSafeScore(@PathVariable(value = "id") String id) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			int score = userService.computeSafeScore(id);
		  	resultMap.put("success",true);
		  	resultMap.put("data",score);
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg",e.getMessage());
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 修改密码
	 * @param req
	 * @param resp
	 * @return json obj string
	 */
	@RequestMapping("/updatePassword")
	@ResponseBody
	public String updatePassword(@RequestParam String id,@RequestParam String oldPwd,@RequestParam String newPwd){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			boolean result = false;
			if(oldPwd!=null&&!"".equals(oldPwd)&&newPwd!=null&&!"".equals(newPwd)){
				result = userService.updatePassword(id, oldPwd, newPwd);
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
  	/**
	 * 发送帐号信息，获取验证码
	 */
	@RequestMapping(value = "/sendAccountByMailValicode")
	public void sendAccountByMailValicode(HttpServletRequest request,HttpServletResponse response) throws IOException {
		//如果session中没有验证码
		ValidateCode code = null;
		//if(request.getSession().getAttribute("valicode")==null){
			code = new ValidateCode(60, 24, 4);
			//放入session
			request.getSession().setAttribute("valicode", code);
			request.getSession().setMaxInactiveInterval(30*60);
			//放入cookie
			response.addCookie(new Cookie("valicode", code.getCode()));
			BufferedImage image = code.getImage();
			ImageIO.write(image, "png", response.getOutputStream());
//		}else{
//			code = (ValidateCode)request.getSession().getAttribute("valicode");
//			BufferedImage image = code.getImage();
//			ImageIO.write(image, "png", response.getOutputStream());
//		}
	}
	/**
	 * 发送帐号信息，使用邮件发送
	 */
	@RequestMapping(value = "/sendAccountByMail")
	@ResponseBody
	public String sendAccountByMail(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> reqMap) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
//			String valicode = String.valueOf(reqMap.get("valicode")).toLowerCase();
//			//验证验证码
//			boolean isVali = false;
//			if(reqMap.get("valicode")!=null&&!"".equals(reqMap.get("valicode"))){
//				if(request.getSession().getAttribute("valicode")!=null){
//					ValidateCode code = (ValidateCode)request.getSession().getAttribute("valicode");
//					String sessionCode = code.getCode().toLowerCase();
//					if(valicode.equals(sessionCode)){
//						isVali = true;
//						request.getSession().removeAttribute("valicode");
//					}
//				}
//			}
//			//通过Cookie验证
//			if(!isVali){
//				Cookie[] cookies = request.getCookies();
//				for(Cookie ck : cookies){
//					if(ck.getName().equals("valicode")){
//						if(valicode.toLowerCase().equals(ck.getValue().toLowerCase())){
//							isVali = true;
//							Cookie newCookie = new Cookie("valicode",null);
//							response.addCookie(newCookie);
//							break;
//						}
//					}
//				}
//			}
//			
//			//验证通过
//			if(isVali){
//				//发送邮件
//				String account = String.valueOf(reqMap.get("account"));
//				String email = String.valueOf(reqMap.get("email"));
//				sendService.sendAccountByMail(email, account);
//				resultMap.put("success",true);
//				resultMap.put("msg","验证通过，发送成功！");
//			}else{
//				resultMap.put("success",false);
//				resultMap.put("msg","验证码不正确！");
//			}
			//发送邮件
			String account = String.valueOf(reqMap.get("account"));
			String email = String.valueOf(reqMap.get("email"));
			sendService.sendAccountByMail(email, account);
			resultMap.put("success",true);
			resultMap.put("msg","帐号发送成功！");
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg","帐号发送失败！请重发");
			resultMap.put("errorMsg",e.getMessage());
			resultMap.put("exception",e);
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 发送帐号信息，使用邮件发送
	 */
	@RequestMapping(value = "/sendAccountByMobile")
	@ResponseBody
	public String sendAccountByMobile(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> reqMap) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			String valicode = String.valueOf(reqMap.get("valicode")).toLowerCase();
			//验证验证码
			boolean isVali = false;
			if(reqMap.get("valicode")!=null&&!"".equals(reqMap.get("valicode"))){
				if(request.getSession().getAttribute("valicode")!=null){
					ValidateCode code = (ValidateCode)request.getSession().getAttribute("valicode");
					String sessionCode = code.getCode().toLowerCase();
					if(valicode.equals(sessionCode)){
						isVali = true;
						request.getSession().removeAttribute("valicode");
					}
				}
			}
			//通过Cookie验证
			if(!isVali){
				Cookie[] cookies = request.getCookies();
				for(Cookie ck : cookies){
					if(ck.getName().equals("valicode")){
						if(valicode.toLowerCase().equals(ck.getValue().toLowerCase())){
							isVali = true;
							Cookie newCookie = new Cookie("valicode",null);
							response.addCookie(newCookie);
							break;
						}
					}
				}
			}
			//验证通过
			if(isVali){
				//发送手机短信
				String account = String.valueOf(reqMap.get("account"));
				String mobile = String.valueOf(reqMap.get("mobile"));
				sendService.sendAccountBySms(mobile, account);
				resultMap.put("success",true);
				resultMap.put("msg","帐号发送成功！");
			}else{
				resultMap.put("success",false);
				resultMap.put("msg","图文验证码错误");
			}
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg","帐号发送失败！请重发");
			resultMap.put("errorMsg",e.getMessage());
			resultMap.put("exception",e);
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 设置手机，发送手机验证码
	 */
	@RequestMapping(value = "/setMobileValicode")
	@ResponseBody
	public String setMobileValicode(HttpServletRequest request,HttpServletResponse response,@RequestParam String mobile) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//验证邮箱是否存在
			Map<String,Object> queryMap = new HashMap<String,Object>();
			queryMap.put("mobile", mobile);
			int count = userService.getUserTotal(queryMap);
			if(count>0){
				resultMap.put("success",false);
				resultMap.put("msg","请输入系统中未注册的手机号");
				return JacksonJsonMapper.objectToJson(resultMap);
			}
			//如果session中没有验证码
			ValidateCode code = null;
//			if(request.getSession().getAttribute("setMObileValicode")==null){
				code = new ValidateCode(4);
				request.getSession().setAttribute("setMObileValicode", code);
				request.getSession().setMaxInactiveInterval(30*60);
				//放入cookie
				response.addCookie(new Cookie("setMObileValicode", code.getCode()));
//			}else{
//				code = (ValidateCode)request.getSession().getAttribute("setMObileValicode");
//			}
			String validateCode = code.getCode();
			//发送手机验证码mobile
			sendService.sendValicodeBySms(mobile, validateCode);
			resultMap.put("success",true);
			resultMap.put("msg","验证码发送成功！");
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg","验证码发送失败！请重发");
			resultMap.put("errorMsg",e.getMessage());
			resultMap.put("exception",e);
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 验证手机验证码，并设置手机
	 */
	@RequestMapping(value = "/setMobile/{id}")
	@ResponseBody
	public String setMobile(HttpServletRequest request,HttpServletResponse response,@PathVariable(value = "id") String id,@RequestParam String valicode,@RequestParam String mobile) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//验证验证码
			boolean isVali = false;
			if(valicode!=null&&!"".equals(valicode)){
				if(request.getSession().getAttribute("setMObileValicode")!=null){
					ValidateCode code = (ValidateCode)request.getSession().getAttribute("setMObileValicode");
					String sessionCode = code.getCode().toLowerCase();
					if(valicode.toLowerCase().equals(sessionCode)){
						isVali = true;
						request.getSession().removeAttribute("setMObileValicode");
					}
				}
			}
			//通过Cookie验证
			if(!isVali){
				Cookie[] cookies = request.getCookies();
				for(Cookie ck : cookies){
					if(ck.getName().equals("setMObileValicode")){
						if(valicode.toLowerCase().equals(ck.getValue().toLowerCase())){
							isVali = true;
							Cookie newCookie = new Cookie("setMObileValicode",null);
							response.addCookie(newCookie);
							break;
						}
					}
				}
			}
			//验证通过
			if(isVali){
				//修改手机
				userService.setMobile(id,mobile);
				resultMap.put("success",true);
				resultMap.put("msg","验证通过，修改成功！");
			}else{
				resultMap.put("success",false);
				resultMap.put("msg","验证码不正确！请重新验证");
			}
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg","验证码不正确！请重新验证");
			resultMap.put("errorMsg",e.getMessage());
			resultMap.put("exception",e);
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 发送邮箱验证码
	 */
	@RequestMapping(value = "/setEmailValicode")
	@ResponseBody
	public String setEmailValicode(HttpServletRequest request,HttpServletResponse response,@RequestParam String email) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//验证邮箱是否存在
			Map<String,Object> queryMap = new HashMap<String,Object>();
			queryMap.put("email", email);
			int count = userService.getUserTotal(queryMap);
			if(count>0){
				resultMap.put("success",false);
				resultMap.put("msg","请输入系统中未注册的邮箱");
				return JacksonJsonMapper.objectToJson(resultMap);
			}
			//如果session中没有验证码
			ValidateCode code = null;
//			if(request.getSession().getAttribute("setEmailValicode")==null){
				code = new ValidateCode(4);
				request.getSession().setAttribute("setEmailValicode", code);
				request.getSession().setMaxInactiveInterval(30*60);
				//放入cookie
				response.addCookie(new Cookie("setEmailValicode", code.getCode()));
//			}else{
//				code = (ValidateCode)request.getSession().getAttribute("setEmailValicode");
//			}
			//发送邮箱验证码email
			sendService.sendValicodeByMail(email, code.getCode());
			resultMap.put("success",true);
			resultMap.put("msg","验证码发送成功！");
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg","验证码发送失败！请重发");
			resultMap.put("errorMsg",e.getMessage());
			resultMap.put("exception",e);
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 验证邮箱验证码，并设置邮箱
	 */
	@RequestMapping(value = "/setEmail/{id}")
	@ResponseBody
	public String setEmail(HttpServletRequest request,HttpServletResponse response,@PathVariable(value = "id") String id,@RequestParam String valicode,@RequestParam String email) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//验证验证码
			boolean isVali = false;
			if(valicode!=null&&!"".equals(valicode)){
				if(request.getSession().getAttribute("setEmailValicode")!=null){
					ValidateCode code = (ValidateCode)request.getSession().getAttribute("setEmailValicode");
					String sessionCode = code.getCode().toLowerCase();
					if(valicode.toLowerCase().equals(sessionCode)){
						isVali = true;
						request.getSession().removeAttribute("setEmailValicode");
					}
				}
			}
			//通过Cookie验证
			if(!isVali){
				Cookie[] cookies = request.getCookies();
				for(Cookie ck : cookies){
					if(ck.getName().equals("setEmailValicode")){
						if(valicode.toLowerCase().equals(ck.getValue().toLowerCase())){
							isVali = true;
							Cookie newCookie = new Cookie("setEmailValicode",null);
							response.addCookie(newCookie);
							break;
						}
					}
				}
			}
			//验证通过
			if(isVali){
				//发送邮件
				userService.setEmail(id,email);
				resultMap.put("success",true);
				resultMap.put("msg","验证通过，修改成功！");
			}else{
				resultMap.put("success",false);
				resultMap.put("msg","验证码不正确！请重新验证");
			}
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg","验证码不正确！请重新验证");
			resultMap.put("errorMsg",e.getMessage());
			resultMap.put("exception",e);
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 通过邮箱找回密码验证码
	 */
	@RequestMapping(value = "/retrievePwdEmailValicode")
	@ResponseBody
	public String retrievePwdEmailValicode(HttpServletRequest request,HttpServletResponse response,@RequestParam String email) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//验证邮箱是否存在
			Map<String,Object> queryMap = new HashMap<String,Object>();
			queryMap.put("email", email);
			int count = userService.getUserTotal(queryMap);
			if(count==0){
				resultMap.put("success",false);
				resultMap.put("msg","请输入已注册的邮箱");
				return JacksonJsonMapper.objectToJson(resultMap);
			}
			//如果session中没有验证码
			ValidateCode code = null;
//			if(request.getSession().getAttribute("retrievePwdEmailValicode")==null){
				code = new ValidateCode(4);
				request.getSession().setAttribute("retrievePwdEmailValicode", code);
				request.getSession().setMaxInactiveInterval(30*60);
				//放入cookie
				response.addCookie(new Cookie("retrievePwdEmailValicode", code.getCode()));
//			}else{
//				code = (ValidateCode)request.getSession().getAttribute("retrievePwdEmailValicode");
//			}
			//发送邮箱验证码email
			sendService.sendRetrievePwdValicodeByMail(email, code.getCode());
			resultMap.put("success",true);
			resultMap.put("msg","验证码发送成功！");
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg","验证码发送失败！请重发");
			resultMap.put("errorMsg",e.getMessage());
			resultMap.put("exception",e);
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 验证 找回密码 邮箱验证码
	 */
	@RequestMapping(value = "/retrievePwdEmailVali")
	@ResponseBody
	public String retrievePwdEmailVali(HttpServletRequest request,HttpServletResponse response,@RequestParam String valicode) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//验证验证码
			boolean isVali = false;
			if(valicode!=null&&!"".equals(valicode)){
				if(request.getSession().getAttribute("retrievePwdEmailValicode")!=null){
					ValidateCode code = (ValidateCode)request.getSession().getAttribute("retrievePwdEmailValicode");
					String sessionCode = code.getCode().toLowerCase();
					if(valicode.toLowerCase().equals(sessionCode)){
						isVali = true;
					}
				}
			}
			//通过Cookie验证
			if(!isVali){
				Cookie[] cookies = request.getCookies();
				for(Cookie ck : cookies){
					if(ck.getName().equals("retrievePwdEmailValicode")){
						if(valicode.toLowerCase().equals(ck.getValue().toLowerCase())){
							isVali = true;
							Cookie newCookie = new Cookie("retrievePwdEmailValicode",null);
							response.addCookie(newCookie);
							break;
						}
					}
				}
			}
			//验证通过
			if(isVali){
				resultMap.put("success",true);
				resultMap.put("msg","验证通过！");
			}else{
				resultMap.put("success",false);
				resultMap.put("msg","验证码不正确！请重新验证");
			}
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg","验证码不正确！请重新验证");
			resultMap.put("errorMsg",e.getMessage());
			resultMap.put("exception",e);
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 设置找回密码
	 */
	@RequestMapping(value = "/retrievePwdEmail")
	@ResponseBody
	public String retrievePwdEmail(HttpServletRequest request,HttpServletResponse response,@RequestParam String valicode,@RequestParam String email,@RequestParam String password) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//验证验证码
			boolean isVali = false;
			if(valicode!=null&&!"".equals(valicode)){
				if(request.getSession().getAttribute("retrievePwdEmailValicode")!=null){
					ValidateCode code = (ValidateCode)request.getSession().getAttribute("retrievePwdEmailValicode");
					String sessionCode = code.getCode().toLowerCase();
					if(valicode.toLowerCase().equals(sessionCode)){
						isVali = true;
						request.getSession().removeAttribute("retrievePwdEmailValicode");
					}
				}
			}
			//通过Cookie验证
			if(!isVali){
				Cookie[] cookies = request.getCookies();
				for(Cookie ck : cookies){
					if(ck.getName().equals("retrievePwdEmailValicode")){
						if(valicode.toLowerCase().equals(ck.getValue().toLowerCase())){
							isVali = true;
							Cookie newCookie = new Cookie("retrievePwdEmailValicode",null);
							response.addCookie(newCookie);
							break;
						}
					}
				}
			}
			//验证通过
			if(isVali){
				userService.retrievePwdByEmail(email, password);
				resultMap.put("success",true);
				resultMap.put("msg","操作完成！");
			}else{
				resultMap.put("success",false);
				resultMap.put("msg","验证码不正确！请重新验证");
			}
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg","验证码不正确！请重新验证");
			resultMap.put("errorMsg",e.getMessage());
			resultMap.put("exception",e);
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 通过手机 找回密码 页面验证码
	 */
	@RequestMapping(value = "/retrievePwdMobilePageValicode")
	public void retrievePwdMobilePageValicode(HttpServletRequest request,HttpServletResponse response) throws IOException {
		//如果session中没有验证码
		ValidateCode code = null;
		code = new ValidateCode(60, 24, 4);
		//放入session
		request.getSession().setAttribute("retrievePwdMobilePageValicode", code);
		request.getSession().setMaxInactiveInterval(30*60);
		//放入cookie
		response.addCookie(new Cookie("retrievePwdMobilePageValicode", code.getCode()));
		BufferedImage image = code.getImage();
		ImageIO.write(image, "png", response.getOutputStream());
	}
	/**
	 * 通过手机 找回密码 验证手机是否存在
	 */
	@RequestMapping(value = "/retrievePwdMobileIsExists")
	@ResponseBody
	public String retrievePwdMobileIsExists(HttpServletRequest request,HttpServletResponse response,@RequestParam String mobile) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//验证手机是否存在
			Map<String,Object> queryMap = new HashMap<String,Object>();
			queryMap.put("mobile", mobile);
			int count = userService.getUserTotal(queryMap);
			if(count==0){
				resultMap.put("success",false);
				resultMap.put("msg","请输入已注册的手机号");
			}else{
				resultMap.put("success",true);
				resultMap.put("msg","手机号正确");
			}
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg","请输入已注册的手机号");
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 通过手机 找回密码 验证码
	 */
	@RequestMapping(value = "/retrievePwdMobileValicode")
	@ResponseBody
	public String retrievePwdMobileValicode(HttpServletRequest request,HttpServletResponse response,@RequestParam String mobile,@RequestParam String valicode) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//验证验证码
			boolean isVali = false;
			if(valicode!=null&&!"".equals(valicode)){
				if(request.getSession().getAttribute("retrievePwdMobilePageValicode")!=null){
					ValidateCode code = (ValidateCode)request.getSession().getAttribute("retrievePwdMobilePageValicode");
					String sessionCode = code.getCode().toLowerCase();
					if(valicode.toLowerCase().equals(sessionCode)){
						isVali = true;
						request.getSession().removeAttribute("retrievePwdMobilePageValicode");
					}
				}
			}
			//通过Cookie验证
			if(!isVali){
				Cookie[] cookies = request.getCookies();
				for(Cookie ck : cookies){
					if(ck.getName().equals("retrievePwdMobilePageValicode")){
						if(valicode.toLowerCase().equals(ck.getValue().toLowerCase())){
							isVali = true;
							Cookie newCookie = new Cookie("retrievePwdMobilePageValicode",null);
							response.addCookie(newCookie);
							break;
						}
					}
				}
			}
			
			//验证通过
			if(isVali){
				//如果session中没有验证码
				ValidateCode code = new ValidateCode(4);
				request.getSession().setAttribute("retrievePwdMobileValicode", code);
				request.getSession().setMaxInactiveInterval(30*60);
				//放入cookie
				response.addCookie(new Cookie("retrievePwdMobileValicode", code.getCode()));
				//发送手机验证码
				sendService.sendRetrievePwdValicodeBySms(mobile, code.getCode());
				resultMap.put("success",true);
			}else{
				resultMap.put("success",false);
				resultMap.put("msg","图文验证码错误");
			}
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg","图文验证码错误");
			resultMap.put("errorMsg",e.getMessage());
			resultMap.put("exception",e);
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 验证 找回密码 邮箱验证码
	 */
	@RequestMapping(value = "/retrievePwdMobileVali")
	@ResponseBody
	public String retrievePwdMobileVali(HttpServletRequest request,HttpServletResponse response,@RequestParam String valicode) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//验证验证码
			boolean isVali = false;
			if(valicode!=null&&!"".equals(valicode)){
				if(request.getSession().getAttribute("retrievePwdMobileValicode")!=null){
					ValidateCode code = (ValidateCode)request.getSession().getAttribute("retrievePwdMobileValicode");
					String sessionCode = code.getCode().toLowerCase();
					if(valicode.toLowerCase().equals(sessionCode)){
						isVali = true;
					}
				}
			}
			//通过Cookie验证
			if(!isVali){
				Cookie[] cookies = request.getCookies();
				for(Cookie ck : cookies){
					if(ck.getName().equals("retrievePwdMobileValicode")){
						if(valicode.toLowerCase().equals(ck.getValue().toLowerCase())){
							isVali = true;
							Cookie newCookie = new Cookie("retrievePwdMobileValicode",null);
							response.addCookie(newCookie);
							break;
						}
					}
				}
			}
			//验证通过
			if(isVali){
				resultMap.put("success",true);
				resultMap.put("msg","验证通过！");
			}else{
				resultMap.put("success",false);
				resultMap.put("msg","验证码不正确！请重新验证");
			}
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg","验证码不正确！请重新验证");
			resultMap.put("errorMsg",e.getMessage());
			resultMap.put("exception",e);
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 设置找回密码
	 */
	@RequestMapping(value = "/retrievePwdMobile")
	@ResponseBody
	public String retrievePwdMobile(HttpServletRequest request,HttpServletResponse response,@RequestParam String valicode,@RequestParam String mobile,@RequestParam String password) throws IOException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//验证验证码
			boolean isVali = false;
			if(valicode!=null&&!"".equals(valicode)){
				if(request.getSession().getAttribute("retrievePwdMobileValicode")!=null){
					ValidateCode code = (ValidateCode)request.getSession().getAttribute("retrievePwdMobileValicode");
					String sessionCode = code.getCode().toLowerCase();
					if(valicode.toLowerCase().equals(sessionCode)){
						isVali = true;
						request.getSession().removeAttribute("retrievePwdMobileValicode");
					}
				}
			}
			//通过Cookie验证
			if(!isVali){
				Cookie[] cookies = request.getCookies();
				for(Cookie ck : cookies){
					if(ck.getName().equals("retrievePwdMobileValicode")){
						if(valicode.toLowerCase().equals(ck.getValue().toLowerCase())){
							isVali = true;
							Cookie newCookie = new Cookie("retrievePwdMobileValicode",null);
							response.addCookie(newCookie);
							break;
						}
					}
				}
			}
			//验证通过
			if(isVali){
				userService.retrievePwdByMobile(mobile, password);
				resultMap.put("success",true);
				resultMap.put("msg","操作完成！");
			}else{
				resultMap.put("success",false);
				resultMap.put("msg","验证码不正确！请重新验证");
			}
		}catch(Exception e){
			resultMap.put("success",false);
			resultMap.put("msg","验证码不正确！请重新验证");
			resultMap.put("errorMsg",e.getMessage());
			resultMap.put("exception",e);
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
}
