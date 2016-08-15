package com.candao.www.permit.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.Constant;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.common.utils.ValidateCode;
import com.candao.www.data.model.Function;
import com.candao.www.data.model.User;
import com.candao.www.data.model.UserBranch;
import com.candao.www.permit.common.BusinessException;
import com.candao.www.permit.common.Constants;
import com.candao.www.permit.service.EmployeeUserService;
import com.candao.www.permit.service.FunctionService;
import com.candao.www.permit.service.UserService;
import com.candao.www.utils.SessionUtils;

@Controller
@RequestMapping("/login")
public class LoginController {
	@Autowired
	@Qualifier("t_userService")
	private UserService userService;

	@Autowired
	private FunctionService functionService;

	@Autowired
	@Qualifier("businessUserService")
	private UserService businessUserService;

	@Autowired
	private EmployeeUserService employeeUserService;

	/**
	 * 后台首页
	 */
	@RequestMapping(value = { "/index", "/" })
	public ModelAndView index(HttpServletRequest request) {
		if (request.getSession().getAttribute(Constant.CURRENT_USER) != null) { // 当前已经登录，进入初始页面
			ModelAndView mav = new ModelAndView("main_branch");
			mav.addObject("psishow", PropertiesUtils.getValue("PSI_SHOW"));
			return mav;
		}
		return new ModelAndView("loginNew");// 对应 login.jsp
	}

	/**
	 * 后台首页-登录
	 */
	@RequestMapping("/login")
	public ModelAndView login(String username, String password) {
		ModelAndView mav = new ModelAndView("main_branch");
//		 是否显示进销存模块
		mav.addObject("psishow", PropertiesUtils.getValue("PSI_SHOW"));
//		 获取门店ID
		String branchid = PropertiesUtils.getValue("current_branch_id");
		try {
			if (username == null || username.trim().length() <= 0) {
				throw new BusinessException("缺少账号、手机号或者邮箱！");
			}
			User webUser = userService.login(username, password);
			
//			对门店管理员的判断
			verifyBranchManage(webUser, branchid);
			
//			不是门店管理员，判断是否有登录权限
			HashSet<String> urlSet = userService.getAuthedUrls(webUser);
			verifyLogin(urlSet);
			
//			获取该用户对应的function和code
			List<Function> fnList = this.functionService.getMenuFunction4User(webUser.getId());
			Map<String, Function> menumap = setFunctionCode(fnList);
			mav.addObject("menumap", menumap);

//			 获取门店名称
			String showName = getBranchName(branchid);
			mav.addObject("showName", showName);
			mav.addObject("isSuccess", true);

//			 登录成功将当前用户保存到session中
			SessionUtils.put(Constant.CURRENT_USER, webUser);
			SessionUtils.put(Constant.CURRENT_TENANT_ID, webUser.getTenantid());
			SessionUtils.put(Constant.ALLOW_ACCESS_BUTTONS, urlSet);
		} catch (BusinessException be) {
			mav.addObject("isSuccess", false);
			mav.addObject("message", be.getMessage());
			mav.addObject("j_username", username);
			mav.setViewName("loginNew");
		} catch (Exception e){
			e.printStackTrace();
			mav.addObject("isSuccess", false);
			mav.addObject("message", "服务器内部错误,请联系管理员");
			mav.addObject("j_username", username);
			mav.setViewName("loginNew");
		}
		return mav;
	}

	/**
	 * 判断该账号是否有登录权限
	 */
	private void verifyLogin(HashSet<String> urlSet){
		String loginPath = "/login/shopLogin";
//		判断是否有登录权限
		boolean isLoginOk = false;
		if (urlSet != null) {
			for (String userUrl : urlSet) {
				if (userUrl != null && !"".equals(userUrl)) {
					if (loginPath.startsWith(userUrl) || loginPath.startsWith("/" + userUrl)) {
						isLoginOk = true;
						break;
					}
				}
			}
		}
		if (!isLoginOk) {
			throw new BusinessException("该账号没有权限登录门店后台");
		}
	}
	
	
	/**
	 *  判断当前用户是不是门店管理员，如果是，再获取是哪些门店的管理员
	 *  如果不是，不做处理
	 * @param webUser
	 * @param branchid
	 */
	private void verifyBranchManage(User webUser,String branchid){
		if (Constants.HEADQUARTER_USER.equals(webUser.getUserType())) {
			List<UserBranch> userBranchList = userService.queryUserBranchListByUserId(webUser.getId());
			boolean isTheBranch = false;
			for (UserBranch urObj : userBranchList) {
				if (branchid.equals(urObj.getBranchId())) {
					isTheBranch = true;
					break;
				}
			}
			if (!isTheBranch) {
				throw new BusinessException("登录失败，该账号不是该门店管理员");
			}
		}
	}
	
	
	/**
	 * 获取对应的function和code
	 * @param fnList
	 * @return
	 */
	private Map<String, Function> setFunctionCode(List<Function> fnList){
		Map<String, Function> menumap = new TreeMap<>();
		String mCode = PropertiesUtils.getValue("shop_admin_function_code"); //门店管理员code
		for (Function f : fnList) {
			if(mCode.equals(f.getCode())){//如果有门店管理员权限，则不需要其他任何权限
				menumap.clear();
				menumap.put(mCode, f);
				break;
			}
			menumap.put(f.getCode(), f);
		}
		return menumap;
	}
	
	
	/**
	 * 获取门店名称
	 * 
	 * @return
	 */
	private String getBranchName(String branchId) {
		Map<String, Object> branchMap = this.employeeUserService.getBranchById(branchId);
		String branchName = "";
		if (branchMap == null || branchMap.get("branchname") == null
				|| branchMap.get("branchname").toString().isEmpty()) {
			branchName = "当前门店未设置门店名称";
		} else {
			branchName = String.valueOf(branchMap.get("branchname"));
		}
		return branchName;
	}

	/**
	 * 显示用户菜单
	 */
	@RequestMapping("/showAuthInfo")
	@ResponseBody
	public String showAuthInfo(String account, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = functionService.getRoleFunctionUrlMap(account);
		return JacksonJsonMapper.objectToJson(map);
	}

	/**
	 * 验证后台登录
	 */
	@RequestMapping("/validateLogin")
	public ModelAndView validateLogin(String username, String password, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		if (username == null || username.trim().length() <= 0) {
			mav.addObject("isSuccess", false);
			mav.addObject("message", "缺少账号、手机号或者邮箱！");
			return mav;
		}
		try {
			User webUser = userService.login(username, password);
			// 登录成功将当前用户保存到session中。
			request.getSession().setAttribute(Constant.CURRENT_USER, webUser);
			HashSet<String> urlSet = userService.getAuthedUrls(webUser);
			request.getSession().setAttribute(Constant.ALLOW_ACCESS_BUTTONS, urlSet);
		} catch (BusinessException be) {
			// 用户名密码验证失败
			mav.addObject("isSuccess", false);
			mav.addObject("message", be.getMessage());
		}
		return mav;
	}

	/**
	 * 后台-退出登录
	 */
	@RequestMapping("/logout")
	public ModelAndView quit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("loginNew");// 跳转到登录页面
		// 注销session
		request.getSession().invalidate();
		return mav;
	}

	/**
	 * 权限错误提示
	 * 
	 * @return
	 */
	@RequestMapping("/securityError")
	public ModelAndView securityError(HttpServletRequest request) {
		ModelAndView ma = new ModelAndView("common/noSecurity");
		if (request.getParameter("url") != null) {
			ma.addObject("url", request.getParameter("url"));
		}
		return ma;
	}

	/**
	 * 门店跳转到云端登录
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/ssoLogin")
	public void ssoLogin(String username, String password, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map<String, Object> mav = new HashMap<>();
		String jsonp = request.getParameter("jsonp");
		if (username == null || username.trim().length() <= 0) {
			mav.put("isSuccess", false);
			mav.put("message", "缺少账号！");
			response.getWriter().write(jsonp + "(" + JacksonJsonMapper.objectToJson(mav) + ")");
		}
		try {
			User webUser = userService.login(username, password);
			// 登录成功将当前用户保存到session中。
			request.getSession().setAttribute(Constant.CURRENT_USER, webUser);
			HashSet<String> urlSet = userService.getAuthedUrls(webUser);
			request.getSession().setAttribute(Constant.ALLOW_ACCESS_BUTTONS, urlSet);
			mav.put("isSuccess", true);
			mav.put("session1", request.getSession().getId());
		} catch (BusinessException be) {
			// 用户名密码验证失败
			mav.put("isSuccess", false);
			mav.put("message", be.getMessage());
		}
		response.getWriter().write(jsonp + "(" + JacksonJsonMapper.objectToJson(mav) + ")");
	}

	// --------------原来的用户登录部分复制过来得函数---------
	/**
	 * 获取验证码
	 */
	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
	public void validateImages(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ValidateCode code = new ValidateCode(60, 24, 4);
		BufferedImage image = code.getImage();
		String validateCode = code.getCode();
		request.getSession().setAttribute(Constant.CURRENT_USER_VALIDATE_CODE_KEY, validateCode);
		ImageIO.write(image, "png", response.getOutputStream());
	}

}
