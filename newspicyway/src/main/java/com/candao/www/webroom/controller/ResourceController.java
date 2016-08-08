package com.candao.www.webroom.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.webroom.model.ResourceRole;
import com.candao.www.webroom.model.TreeNode;
import com.candao.www.webroom.service.ResourceService;

@Controller
@RequestMapping("/resource")
public class ResourceController {
	@Autowired
	private ResourceService resourceService;

	@RequestMapping("/getTreeNode")
	@ResponseBody
	public String getTreeNode() {
		TreeNode treeNode = resourceService.getList("0");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("treeNode", treeNode);
		String json = JacksonJsonMapper.objectToJson(map);
		return json;
	}

	@RequestMapping("/index")
	public String index() {
		return "role/show";
	}

	@RequestMapping("/addResourceRole")
	@ResponseBody
	public ModelAndView addResourceRole(@RequestBody ResourceRole resourceRole) {
		boolean b = resourceService.addResourceRole(resourceRole.getRoleId(), resourceRole.getResourceIds());
		String message = "";
		if (b) {
			message = "操作成功";
		} else {
			message = "操作失败";
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject(message);
		return mav;
	}

	@RequestMapping("/getRoleResource/{roleid}")
	@ResponseBody
	public ModelAndView getRoleResource(@PathVariable(value = "roleid") String roleid) {
		List<String> list = resourceService.getRoleResource(roleid);
		ModelAndView mav = new ModelAndView();
		mav.addObject(list);
		return mav;
	}
}
