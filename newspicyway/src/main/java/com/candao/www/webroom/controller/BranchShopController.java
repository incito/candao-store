package com.candao.www.webroom.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.data.model.Tbranchshop;
import com.candao.www.webroom.service.BranchShopService;

@Controller
@RequestMapping("/branchshop")
public class BranchShopController {
	@Autowired
	private BranchShopService branchShopService;
	@RequestMapping("/index")
	public String index(){
		return "branchshop/show";
	}
	
	@RequestMapping("/page")
	@ResponseBody
	public ModelAndView page(@RequestParam Map<String, Object> params, int page, int rows) {
		Page<Map<String, Object>> pageMap = branchShopService.grid(params, page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		return mav;
	}
	@RequestMapping("/save")
	@ResponseBody
	public String save(Tbranchshop tbranchshop) {
		boolean b = false;
		String id = tbranchshop.getId();
		try { 
			if (ValidateUtils.isEmpty(id)) {// 增加
				tbranchshop.setId(IdentifierUtils.getId().generate().toString());
				b = branchShopService.save(tbranchshop);
			} else {// 修改
				b = branchShopService.update(tbranchshop);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (b) {
			if (ValidateUtils.isEmpty(id)) {
				map.put("message", "添加成功");
			} else {
				map.put("message", "修改成功");
			}
		} else {
			if (ValidateUtils.isEmpty(id)) {
				map.put("message", "添加失败");
			} else {
				map.put("message", "修改失败");
			}
		}
		return JacksonJsonMapper.objectToJson(map);
	}
	@RequestMapping("/findById/{id}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "id") String id, Model model) {
		Tbranchshop tbranchshop = branchShopService.findById(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("tbranchshop", tbranchshop);
		return mav;
	}
	@RequestMapping("/delete/{id}")
	@ResponseBody
	public ModelAndView deleteById(@PathVariable(value = "id") String id) {
		boolean b = branchShopService.deleteById(id);
		ModelAndView mav = new ModelAndView();
		if (b) {
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("message", "删除失败");
		}
		return mav;
	}
	@RequestMapping("/findlist")
	@ResponseBody
	public  ModelAndView findlist(@RequestParam Map<String, Object> params){
		List<Tbranchshop> lis=branchShopService.getTbParternerList(params);
		ModelAndView mav = new ModelAndView();
		mav.addObject("list", lis);
		return mav;
	}
}
