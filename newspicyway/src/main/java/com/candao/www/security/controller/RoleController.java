package com.candao.www.security.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.candao.common.utils.ValidateUtils;
import com.candao.www.data.model.TbRole;
import com.candao.www.security.service.RoleService;

//@Controller
@RequestMapping("/role")
public class RoleController {
	//@Autowired
	private RoleService roleService;

	@RequestMapping("/page")
	@ResponseBody
	public ModelAndView page(int page, int rows,@RequestParam Map<String, Object> param) {
		Page<Map<String, Object>> pageMap = roleService.grid(param, page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		// return JacksonJsonMapper.objectToJson(page);
		return mav;
	}
	
	@RequestMapping("/getRoleList")
	@ResponseBody
	public ModelAndView getRoleList() {
		List<Map<String,Object>> list = roleService.getRoleList();
		ModelAndView mav = new ModelAndView();
		mav.addObject(list);
		return mav;
	}

	@RequestMapping("/index")
	public String index() {
		return "role/show";
	}

	@RequestMapping("/save")
	@ResponseBody
	public String save(@RequestBody TbRole tbRole,  Model model) {
		boolean b = false;
		String roleid=tbRole.getRoleid();
		try {
			if (ValidateUtils.isEmpty(roleid)) {// 增加
				tbRole.setRoleid(IdentifierUtils.getId().generate().toString());
				b = roleService.save(tbRole);
			} else {// 修改
				b = roleService.update(tbRole);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (b) {
			if (ValidateUtils.isEmpty(roleid)) {
				map.put("maessge", "添加成功");
			} else {
				map.put("maessge", "修改成功");
			}
		} else {
			if (ValidateUtils.isEmpty(roleid)) {
				map.put("maessge", "添加失败");
			} else {
				map.put("maessge", "修改失败");
			}
		}
		return JacksonJsonMapper.objectToJson(map);
	}

	@RequestMapping("/findById/{roleid}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "roleid") String roleid, Model model) {
		TbRole tbRole = roleService.findById(roleid);
		ModelAndView mav = new ModelAndView();
		mav.addObject("tbRole", tbRole);
		return mav;
	}

	@RequestMapping("/delete/{roleid}/{status}")
	@ResponseBody
	public ModelAndView deleteById(@PathVariable(value = "roleid") String roleid,@PathVariable(value = "status") int status) {
		boolean b = roleService.deleteById(roleid,status);
		ModelAndView mav = new ModelAndView();
		if(b){
		    mav.addObject("message", "禁用成功");
		}else{
			mav.addObject("message", "禁用失败");	
		}
		return mav;
	}
	

	
}
