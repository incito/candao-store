package com.candao.www.security.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.MD5;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.data.model.TbUser;
import com.candao.www.security.model.UserRole;
import com.candao.www.security.service.UserRoleService;
import com.candao.www.security.service.UserService;
import com.candao.www.spring.SpringContext;

//@Controller
@RequestMapping("/user")
public class UserController {
	//@Autowired
	private UserService userService;
	//@Autowired
	private UserRoleService userRoleService;

	@RequestMapping("/page")
	@ResponseBody
	public ModelAndView page(int page, int rows,@RequestParam Map<String, Object> param) {
		Page<Map<String, Object>> pageMap = userService.grid(param, page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		// return JacksonJsonMapper.objectToJson(page);
		return mav;
	}

	@RequestMapping("/index")
	public String index() {
		return "user/show";
	}

	@RequestMapping("/save")
	@ResponseBody
	public String save(HttpServletRequest request, TbUser tbUser,@RequestParam("userrole") String userrole, Model model) {
		// TbUser tbUser=JacksonJsonMapper.jsonToObject(json, TbUser.class);;
		String[] roles={userrole};
		boolean b = false;
		String userid=tbUser.getUserid();
		try {
			if (ValidateUtils.isEmpty(userid)) {// 增加
				tbUser.setUserid(IdentifierUtils.getId().generate().toString());
				tbUser.setPassword(MD5.md5(tbUser.getPassword()));
				b = userService.save(tbUser)&&userRoleService.inserts(tbUser.getUserid(), roles);
			} else {// 修改
				b = userService.update(tbUser)&&userRoleService.inserts(tbUser.getUserid(), roles);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (b) {
			if (ValidateUtils.isEmpty(userid)) {
				map.put("maessge", "添加成功");
			} else {
				map.put("maessge", "修改成功");
			}
		} else {
			if (ValidateUtils.isEmpty(userid)) {
				map.put("maessge", "添加失败");
			} else {
				map.put("maessge", "修改失败");
			}
		}
		return JacksonJsonMapper.objectToJson(map);
	}

	@RequestMapping("/findById/{userid}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "userid") String userid, Model model) {
		TbUser tbUser = userService.findById(userid);
		ModelAndView mav = new ModelAndView();
		mav.addObject("tbUser", tbUser);
		return mav;
	}
	@RequestMapping("/delete/{userid}/{status}")
	@ResponseBody
	public ModelAndView deleteById(@PathVariable(value = "userid") String userid,@PathVariable(value = "status") int status) {
		boolean b = userService.deleteById(userid,status);
		ModelAndView mav = new ModelAndView();
		if(b){
		    mav.addObject("message", "禁用成功");
		}else{
			mav.addObject("message", "禁用失败");	
		}
		return mav;
	}
	
	@RequestMapping("/updatePassword")
	@ResponseBody
	public String updatePassword(String userid,String password) {
		boolean b=userService.updatePassword(userid,password);
		if(b){
			return "修改成功";
		}else{
			return "修改失败";
		}
	
	}
	
	@RequestMapping("/getUserTat")
	@ResponseBody
	public ModelAndView getUserTat() {
		List<Map<String,Object>> list = userService.getUserTat();
		ModelAndView mav = new ModelAndView();
		mav.addObject(list);
		return mav;
	}
	
	@RequestMapping("/addUserRole")
	@ResponseBody
	public ModelAndView addUserRole(@RequestBody UserRole userrole) {
		
		boolean b=userRoleService.inserts(userrole.getUserid(), userrole.getRoleids());
		String message;
		if(b){
			message="设置成功";
		}else{
			message="设置失败";
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject(message);
		return mav;
	}
	
	@RequestMapping("/getUserRole/{userid}")
	@ResponseBody
	public ModelAndView getUserRole(@PathVariable(value="userid") String userid) {
		List list=userRoleService.getUserRole(userid);
		ModelAndView mav = new ModelAndView();
		mav.addObject(list);
		return mav;
	}
	/**
	 * 获取用户的角色（一个用户只能是一个角色）
	 * @param userid
	 * @return
	 */
	@RequestMapping("/getUserRoleOne/{userid}")
	@ResponseBody
	public ModelAndView getUserRoleOne(@PathVariable(value="userid") String userid) {
		List<String> list=userRoleService.getUserRole(userid);
		ModelAndView mav = new ModelAndView();
		if(list!=null&&list.size()>0){
			mav.addObject(list.get(0));
		}else{
			mav.addObject("");
		}
		return mav;
	}
	

	@RequestMapping("/checkUesrExist")
	@ResponseBody
	public boolean checkUesrExist(@RequestParam String username) {
		boolean b=userService.checkUesrExist(username);
		return b;
	}
	public static void main(String[] args) {
		 SpringContext.getBean("userRoleService");
	}
}
