package com.candao.www.security.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.exception.AuthException;
import com.candao.common.utils.Constant;
import com.candao.common.utils.ValidateCode;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.data.dao.TbResourceDao;
import com.candao.www.data.model.TbResource;
import com.candao.www.data.model.TbUser;
import com.candao.www.data.model.User;
import com.candao.www.security.model.Credentials;
import com.candao.www.security.service.LoginService;
import com.candao.www.security.service.UserService;
import com.candao.www.spring.SpringContext;
import com.candao.www.support.FunctionTag;
import com.candao.www.utils.SessionUtils;

@Controller("n_loginController")
@RequestMapping("/n_login")
public class LoginController {
	@Autowired
	private LoginService loginService;
	//@Autowired
	private UserService userService;
	/**
	 * 跳转到登陆页面
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@ModelAttribute("credentials") Credentials credentials) {
		return "login";
	}

	/**
	 * 执行登陆操作
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String auth(@ModelAttribute("credentials") Credentials credentials, Model model) {
		String sysCap = (String) SessionUtils.get(Constant.CURRENT_USER_VALIDATE_CODE_KEY);
		if (!ValidateUtils.isEmpty(credentials.getCaptcha())) {
//			if (sysCap != null && sysCap.equalsIgnoreCase(credentials.getCaptcha())) {
				if (sysCap != null) {
				try {
					User tbUser = loginService.authUser(credentials,1);
					SessionUtils.put(Constant.CURRENT_USER, tbUser);
					List<String> allowAccessButtons=userService.getAllowAccessButton(tbUser.getId());
					if(!ValidateUtils.isEmpty(allowAccessButtons)){
					  SessionUtils.put(Constant.ALLOW_ACCESS_BUTTONS, allowAccessButtons);
					}
					return "redirect:/login/index";
				} catch (AuthException e) {
					credentials.setPassword(null);
					model.addAttribute("message", e.getMessage());
				}
			} else {
				model.addAttribute("message", "验证码错误，请重新输入。");
			}
		} else {
			model.addAttribute("message", "请输入验证码。");
		}
		credentials.setCaptcha(null);
		return "login";
	}
	
	@RequestMapping(value = "/padlogin", method = RequestMethod.POST)
	public String authPad(@ModelAttribute("credentials") Credentials credentials, Model model) {
		 
	 try {
		    User tbUser = loginService.authUser(credentials,0);
		    SessionUtils.put(Constant.CURRENT_USER, tbUser);
 
		    return "redirect:/login/index";
		    
		} catch (AuthException e) {
			credentials.setPassword(null);
			model.addAttribute("message", e.getMessage());
		}
		 
	 
		return "login";
	}

	/**
	 * 执行登出操作
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(SessionStatus status) {
		status.setComplete();
		SessionUtils.clear();
		return "redirect:/login/index";
	}

	/**
	 * 首页
	 */
	@RequestMapping(value = {
			"/index", "/"
	})
	public String index() {
		//TbUser currentUser = SessionUtils.getCurrentUser();
		User currentUser=SessionUtils.getCurrentUser();
		if (currentUser == null) {
			return "redirect:/login/login";
		} else {
//			return "mainzd";
			return "main_new";
		}
	}

	/**
	 * 获取验证码
	 */
	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
	public void validateImages(HttpServletResponse response) throws IOException {
		ValidateCode code = new ValidateCode(60, 24, 4);
		BufferedImage image = code.getImage();
		String validateCode = code.getCode();
		SessionUtils.put(Constant.CURRENT_USER_VALIDATE_CODE_KEY, validateCode);
		ImageIO.write(image, "png", response.getOutputStream());
	}

	/**
	 * 未授权
	 */
	@RequestMapping(value = "/error")
	public String unauthorized() {
		return "error";
	}

	/**
	 * 当前用户信息
	 */
	@ResponseBody
	@RequestMapping(value = "/current", method = RequestMethod.POST)
	public User current() {
		return SessionUtils.getCurrentUser();
	}
	
	/**
	 * 非法访问跳转
	 */
	@RequestMapping(value = "/IllegalAccess")
	public String IllegalAccess() {
		return "illegalPage";
	}
	private static final Logger logger = LoggerFactory.getLogger(FunctionTag.class);
	@RequestMapping(value="/getMenuList")
	@ResponseBody
	public ModelAndView  getMenu() {
		User currentUser = SessionUtils.getCurrentUser();
		ModelAndView mav = new ModelAndView();
		
		if (logger.isDebugEnabled()) {
			logger.debug(">>>MenuTag getMenu");
		}
		if (null == currentUser) {
			return null;
		}
		TbResourceDao tbResourceDao = (TbResourceDao) SpringContext.getBean(TbResourceDao.class);
		List<TbResource> list = tbResourceDao.getLeftMenu(currentUser.getId());
		if (logger.isDebugEnabled()) {
			logger.debug("<<<getMenu by " + currentUser.getId() + " with row " + list.size());
		}
		mav.addObject("list", list);
		return mav;
	}
}
