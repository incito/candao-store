package com.candao.www.webroom.controller;

import java.util.ArrayList;
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
import com.candao.www.data.model.TbDataDictionary;
import com.candao.www.webroom.service.DataDictionaryService;

@Controller
@RequestMapping("/datadictionary")
public class DataDictionaryController {
	@Autowired
	private DataDictionaryService datadictionaryService;

	@RequestMapping("/page")
	@ResponseBody
	public ModelAndView page(@RequestParam Map<String, Object> params, int page, int rows) {
		Page<Map<String, Object>> pageMap = datadictionaryService.grid(params, page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		// return JacksonJsonMapper.objectToJson(page);
		return mav;
	}

	@RequestMapping("/index")
	public String index() {
		return "datadictionary/datadictionaryShow";
	}

	@RequestMapping("/save")
	@ResponseBody
	public String save(@RequestBody TbDataDictionary tbDatadictionary, String json, Model model) {
		boolean b = false;
		tbDatadictionary.setStatus(1);
		String id = tbDatadictionary.getId();
		try {
			if (ValidateUtils.isEmpty(id)) {// 增加
				 id = IdentifierUtils.getId().generate().toString();
				tbDatadictionary.setId(id);
				b = datadictionaryService.save(tbDatadictionary);
			} else {// 修改
				b = datadictionaryService.update(tbDatadictionary);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (b) {
			if (ValidateUtils.isEmpty(id)) {
				map.put("maessge", "添加成功");
			} else {
				map.put("maessge", "修改成功");
			}
		} else {
			if (ValidateUtils.isEmpty(id)) {
				map.put("maessge", "添加失败");
			} else {
				map.put("maessge", "修改失败");
			}
		}
		map.put("id", id);
		return JacksonJsonMapper.objectToJson(map);
	}

	@RequestMapping("/findById/{id}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "id") String id, Model model) {
		TbDataDictionary tbDataDictionary = datadictionaryService.findById(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("tbDataDictionary", tbDataDictionary);
		return mav;
	}

	@RequestMapping("/delete/{id}")
	@ResponseBody
	public ModelAndView deleteById(@PathVariable(value = "id") String id) {
		boolean b = datadictionaryService.deleteById(id);
		ModelAndView mav = new ModelAndView();
		if (b) {
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("message", "删除失败");
		}
		return mav;
	}

	/**
	 * 取得数据字典
	 * 
	 * @return
	 */
	@RequestMapping("/getDataDictionaryTag")
	@ResponseBody
	public ModelAndView getDataDictionaryTag() {
		List<Map<String, Object>> list = datadictionaryService.getDataDictionaryTag();
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = new HashMap<String, Object>();
		
		maps.put("itemid", "所有分类");
		
		lists.add(maps);
		for (int i = 0; i < list.size(); i++) {
			lists.add(list.get(i));
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject(lists);
		return mav;
	}
	@RequestMapping("/getTypeandTypename")
	@ResponseBody
	public ModelAndView getTypeandTypename() {
		List<Map<String, Object>> list = datadictionaryService.getTypeandTypename();
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("typename", "全部");
//		maps.put("type", "");
//		lists.add(maps);
		for (int i = 0; i < list.size(); i++) {
			lists.add(list.get(i));
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject(lists);
		return mav;
	}
	/**
	 * 获取某个分类下的所有字典数据
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping("/getDatasByType/{type}")
	@ResponseBody
	public ModelAndView getDatasByType(@PathVariable(value = "type") String type, Model model) {
		List<Map<String, Object>> list = datadictionaryService.getDatasByType(type);
		ModelAndView mav = new ModelAndView();
		mav.addObject(list);
		return mav;
	}
	
	@RequestMapping("/delDishTaste/{dishTasteId}")
	@ResponseBody
	public ModelAndView delDishTaste(@PathVariable(value = "dishTasteId") String dishTasteId, Model model) {
		boolean flag = datadictionaryService.delDishTasteService(dishTasteId);
		ModelAndView mav = new ModelAndView();
		if (flag) {
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("message", "删除失败");
		}
		return mav;
	}
}
