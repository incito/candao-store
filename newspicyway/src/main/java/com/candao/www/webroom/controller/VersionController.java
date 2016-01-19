package com.candao.www.webroom.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.data.model.TbVersion;
import com.candao.www.webroom.service.VersionService;

@Controller
@RequestMapping("/version")
public class VersionController {
	@Autowired
	private VersionService versionService;
	@RequestMapping("/index")
	public String index() {
		return "version/show";
	}
	
	@RequestMapping("/findAll")
	@ResponseBody
	public ModelAndView findAll(){
		List<TbVersion> list=versionService.findAll();
		ModelAndView mav=new ModelAndView();
		mav.addObject("all", list);
		return mav;
	}
	@RequestMapping("/findByType/{type}")
	@ResponseBody
	public ModelAndView findByType(@PathVariable(value="type" )Integer type ){
		TbVersion tbversion=versionService.findOne(type);
		ModelAndView mav=new ModelAndView();
		mav.addObject("tbversion", tbversion);
		return mav;
	}
	@RequestMapping("/save")
	@ResponseBody
	public String updateVersion(@RequestBody TbVersion tbVersion,Model model){
		boolean b=versionService.update(tbVersion);
		Map<String, Object> map = new HashMap<String, Object>();
		if(b){
			map.put("maessge", "修改成功");
		}else{
			map.put("maessge", "修改失败");
		}
		return JacksonJsonMapper.objectToJson(map);
	}
}
